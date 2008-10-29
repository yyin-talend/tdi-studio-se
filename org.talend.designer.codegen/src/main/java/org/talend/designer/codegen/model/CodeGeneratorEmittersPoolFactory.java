// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.codegen.model;

import java.beans.PropertyChangeEvent;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.codegen.jet.JETEmitter;
import org.eclipse.emf.codegen.jet.JETException;
import org.eclipse.emf.common.CommonPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.swt.widgets.Display;
import org.talend.commons.CommonsPlugin;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.utils.StringUtils;
import org.talend.commons.utils.io.IOUtils;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.model.components.ComponentCompilations;
import org.talend.core.model.components.ComponentUtilities;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.components.IComponentFileNaming;
import org.talend.core.model.components.IComponentsFactory;
import org.talend.core.model.temp.ECodePart;
import org.talend.core.utils.AccessingEmfJob;
import org.talend.designer.codegen.CodeGeneratorActivator;
import org.talend.designer.codegen.config.CodeGeneratorProgressMonitor;
import org.talend.designer.codegen.config.JetBean;
import org.talend.designer.codegen.config.LightJetBean;
import org.talend.designer.codegen.config.TalendJetEmitter;
import org.talend.designer.codegen.config.TemplateUtil;
import org.talend.designer.codegen.i18n.Messages;
import org.talend.repository.model.ComponentsFactoryProvider;
import org.talend.repository.model.ExternalNodesFactory;

/**
 * Pool of initialized Jet Emitters. There are as many Emitters in this pool as Templzte available. Used for generation
 * performance constraint.
 * 
 * $Id$
 * 
 */
public final class CodeGeneratorEmittersPoolFactory {

    private static HashMap<JetBean, JETEmitter> emitterPool = null;

    private static boolean initialized = false;

    private static boolean initInProgress = false;

    private static Logger log = Logger.getLogger(CodeGeneratorEmittersPoolFactory.class);

    private static List<JetBean> jetFilesCompileFail = new ArrayList<JetBean>();

    /**
     * Default Constructor. Must not be used.
     */
    private CodeGeneratorEmittersPoolFactory() {
    }

    /**
     * initialization of the pool.
     * 
     * @return
     */
    public static Job initialize() {
        if (!initInProgress) {
            // Code Generator initialisation with Progress Bar
            Job job = new AccessingEmfJob(Messages.getString("CodeGeneratorEmittersPoolFactory.initMessage")) {

                @Override
                protected IStatus doRun(IProgressMonitor monitor) {
                    try {
                        ComponentsFactoryProvider.saveComponentVisibilityStatus();

                        jetFilesCompileFail.clear();
                        initInProgress = true;

                        IProgressMonitor monitorWrap = null;
                        if (!CommonsPlugin.isHeadless()) {
                            monitorWrap = new CodeGeneratorProgressMonitor(monitor);
                        } else {
                            monitorWrap = new NullProgressMonitor();
                        }

                        IComponentsFactory componentsFactory = ComponentsFactoryProvider.getInstance();
                        componentsFactory.init();

                        long startTime = System.currentTimeMillis();
                        RepositoryContext repositoryContext = (RepositoryContext) CorePlugin.getContext().getProperty(
                                Context.REPOSITORY_CONTEXT_KEY);
                        ECodeLanguage codeLanguage = repositoryContext.getProject().getLanguage();

                        List<JetBean> jetBeans = new ArrayList<JetBean>();

                        CodeGeneratorInternalTemplatesFactory templatesFactory = CodeGeneratorInternalTemplatesFactoryProvider
                                .getInstance();
                        templatesFactory.init();

                        List<TemplateUtil> templates = templatesFactory.getTemplates();
                        List<IComponent> components = componentsFactory.getComponents();

                        monitorWrap.beginTask(Messages.getString("CodeGeneratorEmittersPoolFactory.initMessage"), (2 * templates
                                .size() + 5 * components.size()));

                        int monitorBuffer = 0;
                        for (TemplateUtil template : templates) {
                            JetBean jetBean = initializeUtilTemplate(template, codeLanguage);
                            jetBeans.add(jetBean);
                            monitorBuffer++;
                            if (monitorBuffer % 100 == 0) {
                                monitorWrap.worked(100);
                                monitorBuffer = 0;
                            }
                        }

                        if (components != null) {
                            ECodePart codePart = ECodePart.MAIN;
                            for (IComponent component : components) {
                                // if (component.isTechnical() || component.isVisible()) {
                                if (component.getAvailableCodeParts().size() > 0) {
                                    initComponent(codeLanguage, jetBeans, codePart, component);
                                }
                                // }
                                monitorBuffer++;
                                if (monitorBuffer % 100 == 0) {
                                    monitorWrap.worked(100);
                                    monitorBuffer = 0;
                                }
                            }
                        }
                        monitorWrap.worked(monitorBuffer);

                        initializeEmittersPool(jetBeans, codeLanguage, monitorWrap);
                        monitorWrap.done();

                        if (!CommonsPlugin.isHeadless()) {
                            Display.getDefault().asyncExec(new Runnable() {

                                public void run() {
                                    CorePlugin.getDefault().getDesignerCoreService().synchronizeDesignerUI(
                                            new PropertyChangeEvent(this, ComponentUtilities.NORMAL, null, null));
                                }

                            });
                        }
                        log.debug("Components compiled in " + (System.currentTimeMillis() - startTime) + " ms");
                        initialized = true;

                        // remove compilations markers
                        ComponentCompilations.deleteMarkers();

                    } catch (Exception e) {
                        log.error("Exception during Initialization", e);
                        return new Status(IStatus.ERROR, CodeGeneratorActivator.PLUGIN_ID, "Exception during Initialization", e);
                    } finally {
                        initInProgress = false;
                        try {
                            IWorkspace workspace = ResourcesPlugin.getWorkspace();
                            IProject project = workspace.getRoot().getProject(".JETEmitters");
                            project.build(IncrementalProjectBuilder.AUTO_BUILD, null);
                        } catch (CoreException e) {
                            ExceptionHandler.process(e);
                        }

                    }
                    if (jetFilesCompileFail.size() > 0) {
                        StringBuilder message = new StringBuilder();
                        for (JetBean tmpJetBean : jetFilesCompileFail) {
                            if (message.length() > 0) {
                                message.append(",\r\n").append(tmpJetBean.getTemplateRelativeUri());
                            } else {
                                message.append(tmpJetBean.getTemplateRelativeUri());
                            }
                        }
                        return new Status(IStatus.ERROR, CodeGeneratorActivator.PLUGIN_ID, "Components compile fail : \r\n"
                                + message.toString());
                    }
                    CorePlugin.getDefault().getRcpService().activeSwitchProjectAction();
                    return Status.OK_STATUS;
                }

            };
            job.setUser(true);
            job.setPriority(Job.INTERACTIVE);
            job.schedule();
            job.wakeUp(); // start as soon as possible

            return job;
        }

        return null;
    }

    /**
     * initialization of available templates.
     * 
     * @param template
     * @param codeLanguage
     * @return
     */
    private static JetBean initializeUtilTemplate(TemplateUtil template, ECodeLanguage codeLanguage) {
        JetBean jetBean = new JetBean(CodeGeneratorActivator.PLUGIN_ID, TemplateUtil.RESOURCES_DIRECTORY + TemplateUtil.DIR_SEP
                + template.getResourceName() + TemplateUtil.EXT_SEP + codeLanguage.getExtension() + TemplateUtil.TEMPLATE_EXT,
                template.getResourceName(), template.getVersion(), codeLanguage.getName(), "");
        jetBean.addClassPath("CORE_LIBRARIES", CorePlugin.PLUGIN_ID);
        jetBean.addClassPath("CODEGEN_LIBRARIES", CodeGeneratorActivator.PLUGIN_ID);
        jetBean.addClassPath("COMMON_LIBRARIES", CommonsPlugin.PLUGIN_ID);
        jetBean.setClassLoader(new CodeGeneratorEmittersPoolFactory().getClass().getClassLoader());
        return jetBean;
    }

    /**
     * initialization of the available components.
     * 
     * @param codeLanguage
     * @param jetBeans
     * @param codePart
     * @param component
     */
    private static void initComponent(ECodeLanguage codeLanguage, List<JetBean> jetBeans, ECodePart codePart, IComponent component) {

        if (component.getAvailableCodeParts().contains(codePart)) {
            IComponentFileNaming fileNamingInstance = ComponentsFactoryProvider.getFileNamingInstance();
            String templateURI = component.getPathSource() + TemplateUtil.DIR_SEP + component.getName() + TemplateUtil.DIR_SEP
                    + fileNamingInstance.getJetFileName(component, codeLanguage.getExtension(), codePart);

            JetBean jetBean = new JetBean(IComponentsFactory.COMPONENTS_LOCATION, templateURI, component.getName(), component
                    .getVersion(), codeLanguage.getName(), codePart.getName());
            jetBean.addClassPath("CORE_LIBRARIES", CorePlugin.PLUGIN_ID);
            jetBean.addClassPath("CODEGEN_LIBRARIES", CodeGeneratorActivator.PLUGIN_ID);
            jetBean.addClassPath("COMMON_LIBRARIES", CommonsPlugin.PLUGIN_ID);

            for (String pluginDependency : component.getPluginDependencies()) {
                jetBean.addClassPath(pluginDependency.toUpperCase().replaceAll("\\.", "_") + "_LIBRARIES", pluginDependency);
            }

            String familyName = component.getFamily();
            if (familyName.contains("|")) {
                familyName = component.getFamily().substring(0, component.getFamily().indexOf("|"));
            }
            jetBean.setFamily(StringUtils.removeSpecialCharsForPackage(familyName.toLowerCase()));

            if (component.getPluginFullName().compareTo(IComponentsFactory.COMPONENTS_LOCATION) != 0) {
                jetBean.addClassPath("EXTERNAL_COMPONENT_" + component.getPluginFullName().toUpperCase().replaceAll("\\.", "_"),
                        component.getPluginFullName());
                jetBean.setClassLoader(ExternalNodesFactory.getInstance(component.getPluginFullName()).getClass()
                        .getClassLoader());
            } else {
                jetBean.setClassLoader(new CodeGeneratorEmittersPoolFactory().getClass().getClassLoader());
            }
            jetBeans.add(jetBean);
        }
        if (codePart.compareTo(ECodePart.MAIN) == 0) {
            if (component.getAvailableCodeParts().contains(ECodePart.BEGIN)) {
                initComponent(codeLanguage, jetBeans, ECodePart.BEGIN, component);
            }
            if (component.getAvailableCodeParts().contains(ECodePart.END)) {
                initComponent(codeLanguage, jetBeans, ECodePart.END, component);
            }
        }

    }

    /**
     * real pool initialization.
     * 
     * @param monitorWrap
     * 
     * @return
     */
    private static void initializeEmittersPool(List<JetBean> components, ECodeLanguage codeLanguage, IProgressMonitor monitorWrap) {
        IProgressMonitor monitor = new NullProgressMonitor();
        IProgressMonitor sub = new SubProgressMonitor(monitor, 1);
        int monitorBuffer = 0;

        HashMap<String, String> globalClasspath = new HashMap<String, String>();
        for (JetBean jetBean : components) {
            globalClasspath.putAll(jetBean.getClassPath());
            // compute the CRC
            jetBean.setCrc(extractTemplateHashCode(jetBean));
        }

        emitterPool = new HashMap<JetBean, JETEmitter>();
        List<JetBean> alreadyCompiledEmitters = new ArrayList<JetBean>();

        try {
            TalendJetEmitter dummyEmitter = new TalendJetEmitter(null, null, sub, globalClasspath, !ComponentCompilations
                    .getMarkers());

            boolean isSkeletonChanged = JetSkeletonManager.updateSkeletonPersistenceData();
            // if there is one skeleton changed, there need generate all jet--->java again. so, it won't load the
            // JetPersistenceJAVA
            if (!isSkeletonChanged) {
                try {
                    alreadyCompiledEmitters = loadEmfPersistentData(EmfEmittersPersistenceFactory.getInstance(codeLanguage)
                            .loadEmittersPool(), components, monitorWrap);
                    for (JetBean jetBean : alreadyCompiledEmitters) {
                        TalendJetEmitter emitter = new TalendJetEmitter(jetBean.getTemplateFullUri(), jetBean.getClassLoader(),
                                jetBean.getFamily(), jetBean.getClassName(), jetBean.getLanguage(), jetBean.getCodePart(),
                                dummyEmitter.getTalendEclipseHelper());
                        emitter.setMethod(jetBean.getMethod());
                        emitterPool.put(jetBean, emitter);
                        monitorBuffer++;
                        if (monitorBuffer % 100 == 0) {
                            monitorWrap.worked(100);
                            monitorBuffer = 0;
                        }
                    }
                } catch (BusinessException e) {
                    // error already loggued
                    emitterPool = new HashMap<JetBean, JETEmitter>();
                }
            }

            for (JetBean jetBean : components) {
                if (!emitterPool.containsKey(jetBean)) {
                    // System.out.println("The new file is not in JetPersistence* cache:" +
                    // jetBean.getTemplateFullUri());
                    TalendJetEmitter emitter = new TalendJetEmitter(jetBean.getTemplateFullUri(), jetBean.getClassLoader(),
                            jetBean.getFamily(), jetBean.getClassName(), jetBean.getLanguage(), jetBean.getCodePart(),
                            dummyEmitter.getTalendEclipseHelper());
                    emitter.initialize(sub);

                    if (emitter.getMethod() != null) {
                        jetBean.setMethod(emitter.getMethod());
                        jetBean.setClassName(emitter.getMethod().getDeclaringClass().getName());
                        alreadyCompiledEmitters.add(jetBean);
                    } else {
                        jetFilesCompileFail.add(jetBean);
                    }
                    emitterPool.put(jetBean, emitter);
                    monitorBuffer++;
                    if (monitorBuffer % 100 == 0) {
                        monitorWrap.worked(100);
                        monitorBuffer = 0;
                    }
                }
            }
            monitorWrap.worked(monitorBuffer);
        } catch (JETException e) {
            log.error("Error during JetEmitter initalization " + e.getMessage(), e);
        }
        try {
            EmfEmittersPersistenceFactory.getInstance(codeLanguage).saveEmittersPool(
                    extractEmfPersistenData(alreadyCompiledEmitters));
        } catch (BusinessException e) {
            log.error(Messages.getString("CodeGeneratorEmittersPoolFactory.PersitentData.Error") + e.getMessage(), e); //$NON-NLS-1$
        }
    }

    /**
     * DOC mhirt Comment method "extractEmfPersistenData".
     * 
     * @param alreadyCompiledEmitters
     * @return
     */
    private static List<LightJetBean> extractEmfPersistenData(List<JetBean> alreadyCompiledEmitters) {
        List<LightJetBean> toReturn = new ArrayList<LightJetBean>();
        for (JetBean unit : alreadyCompiledEmitters) {
            // long unitCRC = extractTemplateHashCode(unit);
            long unitCRC = unit.getCrc();
            toReturn.add(new LightJetBean(unit.getTemplateFullUri(), unit.getClassName(), unit.getMethod().getName(), unit
                    .getVersion(), unit.getLanguage(), unitCRC));
        }
        return toReturn;
    }

    /**
     * DOC mhirt Comment method "extractTemplateHashCode".
     * 
     * @param unit
     * @return
     * @throws MalformedURLException
     * @throws IOException
     */
    private static long extractTemplateHashCode(JetBean unit) {
        long unitCRC = 0;

        URI uri = URI.createURI(unit.getTemplateFullUri());
        uri = CommonPlugin.resolve(uri);
        URL url;
        try {
            url = new URL(uri.toString());
            unitCRC = IOUtils.computeCRC(url.openStream());
        } catch (Exception e) {
            // ignore me even if i'm null
        }

        return unitCRC;
    }

    private static List<JetBean> loadEmfPersistentData(List<LightJetBean> datas, List<JetBean> completeJetBeanList,
            IProgressMonitor monitorWrap) throws BusinessException {
        List<JetBean> toReturn = new ArrayList<JetBean>();
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        IProject project = workspace.getRoot().getProject(".JETEmitters");
        URL url;
        try {
            url = new File(project.getLocation() + "/runtime").toURL();
            int lightBeanIndex = 0;
            LightJetBean lightBean = null;
            LightJetBean myLightJetBean = null;
            String unitTemplateFullURI = "";
            long unitTemplateHashCode = 0;

            HashMap<String, LightJetBean> mapOnName = new HashMap<String, LightJetBean>();
            boolean forceMethodLoad = ComponentCompilations.getMarkers();
            if (forceMethodLoad) {
                // init specific map based on component name : mapOnName
                for (LightJetBean ljb : datas) {
                    mapOnName.put(ljb.getTemplateRelativeUri().substring(ljb.getTemplateRelativeUri().lastIndexOf("/")), ljb);
                }
            }
            int monitorBuffer = 0;
            for (JetBean unit : completeJetBeanList) {
                monitorBuffer++;
                if (monitorBuffer % 200 == 0) {
                    monitorWrap.worked(200);
                    monitorBuffer = 0;
                }
                unitTemplateFullURI = unit.getTemplateFullUri();
                unitTemplateHashCode = unit.getCrc();

                myLightJetBean = new LightJetBean(unitTemplateFullURI, unit.getVersion(), unitTemplateHashCode);
                if (((lightBeanIndex = datas.indexOf(myLightJetBean)) > -1) || forceMethodLoad) {
                    if (!forceMethodLoad) {
                        lightBean = datas.get(lightBeanIndex);
                    } else {
                        lightBean = mapOnName.get(myLightJetBean.getTemplateRelativeUri().substring(
                                myLightJetBean.getTemplateRelativeUri().lastIndexOf("/")));
                    }
                    if (lightBean != null) {
                        unit.setClassName(lightBean.getClassName());
                        try {
                            Method method = loadMethod(url, lightBean.getMethodName(), unit);
                            if (method != null) {
                                unit.setMethod(method);
                                toReturn.add(unit);
                            }
                        } catch (ClassNotFoundException e) {
                            log.info(Messages.getString("CodeGeneratorEmittersPoolFactory.Class.NotFound", unit.getClassName()));
                        }
                    }
                }
            }
            monitorWrap.worked(monitorBuffer);
        } catch (MalformedURLException e) {
            log.error(Messages.getString("CodeGeneratorEmittersPoolFactory.JETEmitters.NoPresent")); //$NON-NLS-1$
            throw new BusinessException(e);
        }
        return toReturn;
    }

    /**
     * DOC mhirt Comment method "loadMethod".
     * 
     * @param methodName
     * @return
     * @throws MalformedURLException
     * @throws ClassNotFoundException
     */
    private static Method loadMethod(URL url, String methodName, JetBean unit) throws ClassNotFoundException {
        if (currentClassLoader != unit.getClassLoader()) {
            currentClassLoader = unit.getClassLoader();
            theClassLoader = new URLClassLoader(new URL[] { url }, unit.getClassLoader());
        }
        Class theClass = theClassLoader.loadClass(unit.getClassName());
        Method[] methods = theClass.getDeclaredMethods();
        for (int i = 0; i < methods.length; ++i) {
            if (methods[i].getName().equals(methodName)) {
                return methods[i];
            }
        }
        return null;
    }

    private static ClassLoader currentClassLoader = null;

    private static URLClassLoader theClassLoader = null;

    /**
     * Getter for emitterPool.
     * 
     * @return the emitterPool
     */
    public static HashMap<JetBean, JETEmitter> getEmitterPool() {
        if (!isInitialized()) {
            initialize();
        }
        return emitterPool;
    }

    /**
     * Getter for initialized.
     * 
     * @return the initialized
     */
    public static boolean isInitialized() {
        return initialized;
    }

    /**
     * Sets the initialized.
     * 
     * @param initialized the initialized to set
     */
    public static void setInitialized(boolean initialized) {
        CodeGeneratorEmittersPoolFactory.initialized = initialized;
    }
}
