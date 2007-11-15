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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.Adler32;
import java.util.zip.CheckedInputStream;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
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
import org.talend.commons.utils.StringUtils;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.components.IComponentFileNaming;
import org.talend.core.model.components.IComponentsFactory;
import org.talend.core.model.temp.ECodePart;
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
            Job job = new Job(Messages.getString("CodeGeneratorEmittersPoolFactory.initMessage")) {

                @Override
                protected IStatus run(IProgressMonitor monitor) {
                    try {
                        initInProgress = true;
                        IProgressMonitor monitorWrap = null;
                        if (!CorePlugin.getContext().isHeadless()) {
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

                        monitorWrap.beginTask(Messages.getString("CodeGeneratorEmittersPoolFactory.initMessage"),
                                (2 * templates.size() + 4 * components.size()));

                        for (TemplateUtil template : templates) {
                            JetBean jetBean = initializeUtilTemplate(template, codeLanguage);
                            jetBeans.add(jetBean);
                            monitorWrap.worked(1);
                        }

                        if (components != null) {
                            ECodePart codePart = ECodePart.MAIN;
                            for (IComponent component : components) {
                                if (component.getAvailableCodeParts().size() > 0) {
                                    initComponent(codeLanguage, jetBeans, codePart, component);
                                }
                                monitorWrap.worked(1);
                            }
                        }

                        initializeEmittersPool(jetBeans, codeLanguage, monitorWrap);
                        monitorWrap.done();

                        if (!CorePlugin.getContext().isHeadless()) {
                            Display.getDefault().asyncExec(new Runnable() {

                                public void run() {
                                    CorePlugin.getDefault().getDesignerCoreService().refreshDesignerPalette();
                                }

                            });
                        }

                        log.debug("Components compiled in " + (System.currentTimeMillis() - startTime) + " ms");
                        initialized = true;
                    } catch (Exception e) {
                        log.error("Exception during Initialization", e);
                        return new Status(IStatus.ERROR, CodeGeneratorActivator.PLUGIN_ID,
                                "Exception during Initialization", e);
                    } finally {
                        initInProgress = false;
                    }
                    return Status.OK_STATUS;
                }
            };
            job.setUser(true);
            job.setPriority(Job.LONG);
            job.schedule(); // start as soon as possible

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
        JetBean jetBean = new JetBean(CodeGeneratorActivator.PLUGIN_ID, TemplateUtil.RESOURCES_DIRECTORY
                + TemplateUtil.DIR_SEP + template.getResourceName() + TemplateUtil.EXT_SEP
                + codeLanguage.getExtension() + TemplateUtil.TEMPLATE_EXT, template.getResourceName(), template
                .getVersion(), codeLanguage.getName(), "");
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
    private static void initComponent(ECodeLanguage codeLanguage, List<JetBean> jetBeans, ECodePart codePart,
            IComponent component) {

        if (component.getAvailableCodeParts().contains(codePart)) {
            IComponentFileNaming fileNamingInstance = ComponentsFactoryProvider.getFileNamingInstance();
            String templateURI = component.getPathSource() + TemplateUtil.DIR_SEP + component.getName()
                    + TemplateUtil.DIR_SEP
                    + fileNamingInstance.getJetFileName(component, codeLanguage.getExtension(), codePart);

            JetBean jetBean = new JetBean(IComponentsFactory.COMPONENTS_LOCATION, templateURI, component.getName(),
                    component.getVersion(), codeLanguage.getName(), codePart.getName());
            jetBean.addClassPath("CORE_LIBRARIES", CorePlugin.PLUGIN_ID);
            jetBean.addClassPath("CODEGEN_LIBRARIES", CodeGeneratorActivator.PLUGIN_ID);
            jetBean.addClassPath("COMMON_LIBRARIES", CommonsPlugin.PLUGIN_ID);

            for (String pluginDependency : component.getPluginDependencies()) {
                jetBean.addClassPath(pluginDependency.toUpperCase().replaceAll("\\.", "_") + "_LIBRARIES",
                        pluginDependency);
            }

            String familyName = component.getFamily();
            if (familyName.contains("|")) {
                familyName = component.getFamily().substring(0, component.getFamily().indexOf("|"));
            }
            jetBean.setFamily(StringUtils.removeSpecialCharsForPackage(familyName.toLowerCase()));

            if (component.getPluginFullName().compareTo(IComponentsFactory.COMPONENTS_LOCATION) != 0) {
                jetBean.addClassPath("EXTERNAL_COMPONENT_"
                        + component.getPluginFullName().toUpperCase().replaceAll("\\.", "_"), component
                        .getPluginFullName());
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
    private static void initializeEmittersPool(List<JetBean> components, ECodeLanguage codeLanguage,
            IProgressMonitor monitorWrap) {
        IProgressMonitor monitor = new NullProgressMonitor();
        IProgressMonitor sub = new SubProgressMonitor(monitor, 1);

        HashMap<String, String> globalClasspath = new HashMap<String, String>();
        for (JetBean jetBean : components) {
            globalClasspath.putAll(jetBean.getClassPath());
        }

        emitterPool = new HashMap<JetBean, JETEmitter>();
        List<JetBean> alreadyCompiledEmitters = new ArrayList<JetBean>();

        try {
            TalendJetEmitter dummyEmitter = new TalendJetEmitter(null, null, sub, globalClasspath);

            try {
                alreadyCompiledEmitters = loadEmfPersistentData(EmfEmittersPersistenceFactory.getInstance(codeLanguage)
                        .loadEmittersPool(), components);
                for (JetBean jetBean : alreadyCompiledEmitters) {
                    TalendJetEmitter emitter = new TalendJetEmitter(jetBean.getTemplateFullUri(), jetBean
                            .getClassLoader(), jetBean.getFamily(), jetBean.getClassName(), jetBean.getLanguage(),
                            jetBean.getCodePart(), dummyEmitter.getTalendEclipseHelper());
                    emitter.setMethod(jetBean.getMethod());
                    emitterPool.put(jetBean, emitter);
                    monitorWrap.worked(1);
                }
            } catch (BusinessException e) {
                // error already loggued
                emitterPool = new HashMap<JetBean, JETEmitter>();
            }

            for (JetBean jetBean : components) {
                if (!emitterPool.containsKey(jetBean)) {
                    TalendJetEmitter emitter = new TalendJetEmitter(jetBean.getTemplateFullUri(), jetBean
                            .getClassLoader(), jetBean.getFamily(), jetBean.getClassName(), jetBean.getLanguage(),
                            jetBean.getCodePart(), dummyEmitter.getTalendEclipseHelper());
                    emitter.initialize(sub);
                    if (emitter.getMethod() != null) {
                        jetBean.setMethod(emitter.getMethod());
                        jetBean.setClassName(emitter.getMethod().getDeclaringClass().getName());
                        alreadyCompiledEmitters.add(jetBean);
                    }
                    emitterPool.put(jetBean, emitter);
                    monitorWrap.worked(1);
                }
            }
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
            long unitCRC = extractTemplateHashCode(unit);

            toReturn.add(new LightJetBean(unit.getTemplateFullUri(), unit.getClassName(), unit.getMethod().getName(),
                    unit.getVersion(), unit.getLanguage(), unitCRC));
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
        URL url;
        try {
            uri = CommonPlugin.resolve(uri);
            url = new URL(uri.toString());
            BufferedInputStream bufferedInputStream = new BufferedInputStream(url.openStream());

            // Compute Adler-32 checksum
            CheckedInputStream cis = new CheckedInputStream(bufferedInputStream, new Adler32());
            byte[] tempBuf = new byte[128];
            while (cis.read(tempBuf) >= 0) {
                // do nothing
            }
            unitCRC = cis.getChecksum().getValue();
        } catch (IOException e) {
            return -1;
        }
        return unitCRC;
    }

    private static List<JetBean> loadEmfPersistentData(List<LightJetBean> datas, List<JetBean> completeJetBeanList)
            throws BusinessException {
        List<JetBean> toReturn = new ArrayList<JetBean>();
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        IProject project = workspace.getRoot().getProject(".JETEmitters");
        URL url;
        try {
            url = new File(project.getLocation() + "/runtime").toURL();
            for (JetBean unit : completeJetBeanList) {
                String unitTemplateFullURI = unit.getTemplateFullUri();
                long unitTemplateHashCode = extractTemplateHashCode(unit);
                for (LightJetBean lightBean : datas) {
                    if ((lightBean.getTemplateRelativeUri().compareTo(unitTemplateFullURI) == 0)
                            && (lightBean.getVersion().compareTo(unit.getVersion()) == 0)
                            && (lightBean.getCrc() == unitTemplateHashCode)) {
                        unit.setClassName(lightBean.getClassName());
                        try {
                            Method method = loadMethod(url, lightBean.getMethodName(), unit);
                            if (method != null) {
                                unit.setMethod(method);
                                toReturn.add(unit);
                            }
                        } catch (ClassNotFoundException e) {
                            log.info(Messages.getString("CodeGeneratorEmittersPoolFactory.Class.NotFound", unit
                                    .getClassName()));
                        }

                    }
                }
            }
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
        URLClassLoader theClassLoader = new URLClassLoader(new URL[] { url }, unit.getClassLoader());
        Class theClass = theClassLoader.loadClass(unit.getClassName());
        Method[] methods = theClass.getDeclaredMethods();
        for (int i = 0; i < methods.length; ++i) {
            if (methods[i].getName().equals(methodName)) {
                return methods[i];
            }
        }
        return null;
    }

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
