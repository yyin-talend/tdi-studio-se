// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
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
import org.talend.commons.CommonsPlugin;
import org.talend.commons.exception.BusinessException;
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
import org.talend.designer.dbmap.DbMapActivator;
import org.talend.repository.model.ComponentsFactoryProvider;
import org.talend.repository.model.ExternalNodesFactory;

/**
 * Pool of initalized Jet Emitters. There are as many Emitters in this pool as Templzte available. Used for generation
 * performance constraint.
 * 
 * $Id$
 * 
 */
public final class CodeGeneratorEmittersPoolFactory {

    private static HashMap<JetBean, JETEmitter> emitterPool = null;

    private static boolean initialized = false;

    private static Logger log = Logger.getLogger(CodeGeneratorEmittersPoolFactory.class);

    /**
     * Default Constructor. Must not be used.
     */
    private CodeGeneratorEmittersPoolFactory() {
    }

    /**
     * initialization of the pool.
     */
    public static void initialize() {
        // Code Generator initialisation with Progress Bar
        Job job = new Job(Messages.getString("CodeGeneratorEmittersPoolFactory.initMessage")) {

            protected IStatus run(IProgressMonitor monitor) {
                IProgressMonitor monitorWrap = new CodeGeneratorProgressMonitor(monitor);

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
                log.debug("Components compiled in " + (System.currentTimeMillis() - startTime) + " ms");
                initialized = true;
                return Status.OK_STATUS;
            }
        };
        job.setUser(true);
        job.setPriority(Job.LONG);
        job.schedule(); // start as soon as possible
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
        // if this function returns null then there is no code to generate
        if (component.getAvailableCodeParts().size() == 0) {
            return;
        }
        IComponentFileNaming fileNamingInstance = ComponentsFactoryProvider.getFileNamingInstance();
        String templateURI = component.getPathSource() + TemplateUtil.DIR_SEP + component.getName()
                + TemplateUtil.DIR_SEP
                + fileNamingInstance.getJetFileName(component, codeLanguage.getExtension(), codePart);

        JetBean jetBean = new JetBean(IComponentsFactory.COMPONENTS_LOCATION, templateURI, component.getName(),
                component.getVersion(), codeLanguage.getName(), codePart.getName());
        jetBean.addClassPath("CORE_LIBRARIES", CorePlugin.PLUGIN_ID);
        jetBean.addClassPath("CODEGEN_LIBRARIES", CodeGeneratorActivator.PLUGIN_ID);
        jetBean.addClassPath("COMMON_LIBRARIES", CommonsPlugin.PLUGIN_ID);

        // PTODO MHIRT Tmp Solution, corres ASAP 
        if (component.getFamily().compareTo("ELT") == 0) {
            jetBean.addClassPath("TMP_DBMAP", DbMapActivator.PLUGIN_ID);
        }

        if (component.getPluginFullName().compareTo(IComponentsFactory.COMPONENTS_LOCATION) != 0) {
            jetBean.addClassPath("EXTERNAL_COMPONENT_" + component.getPluginFullName().toUpperCase(), component
                    .getPluginFullName());
            jetBean.setClassLoader(ExternalNodesFactory.getInstance(component.getPluginFullName()).getClass()
                    .getClassLoader());
        } else {
            jetBean.setClassLoader(new CodeGeneratorEmittersPoolFactory().getClass().getClassLoader());
        }

        if (codePart.compareTo(ECodePart.MAIN) == 0) {
            if (component.getAvailableCodeParts().contains(ECodePart.BEGIN)) {
                initComponent(codeLanguage, jetBeans, ECodePart.BEGIN, component);
            }
            if (component.getAvailableCodeParts().contains(ECodePart.END)) {
                initComponent(codeLanguage, jetBeans, ECodePart.END, component);
            }
        }
        jetBeans.add(jetBean);
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

        HashMap<String, String> globalClasspath = new HashMap<String, String>();
        for (JetBean jetBean : components) {
            globalClasspath.putAll(jetBean.getClassPath());
        }

        emitterPool = new HashMap<JetBean, JETEmitter>();
        List<JetBean> alreadyCompiledEmitters = new ArrayList<JetBean>();
        try {
            alreadyCompiledEmitters = loadEmfPersistentData(EmfEmittersPersistenceFactory.getInstance(codeLanguage)
                    .loadEmittersPool(), components);
            for (JetBean jetBean : alreadyCompiledEmitters) {
                JETEmitter emitter = new JETEmitter(jetBean.getTemplateFullUri(), jetBean.getClassLoader());
                emitter.setMethod(jetBean.getMethod());
                emitterPool.put(jetBean, emitter);
            }
        } catch (BusinessException e) {
            // error already loggued
            emitterPool = new HashMap<JetBean, JETEmitter>();
        }
        for (JetBean jetBean : components) {
            if (!emitterPool.containsKey(jetBean)) {
                TalendJetEmitter emitter = new TalendJetEmitter(jetBean.getTemplateFullUri(), jetBean.getClassLoader(),
                        jetBean.getClassName(), jetBean.getLanguage(), jetBean.getCodePart());
                try {
                    for (String classKey : globalClasspath.keySet()) {
                        emitter.addVariable(classKey, globalClasspath.get(classKey));
                    }
                    emitter.initialize(sub);
                    if (emitter.getMethod() != null) {
                        jetBean.setMethod(emitter.getMethod());
                        jetBean.setClassName(emitter.getMethod().getDeclaringClass().getName());
                        alreadyCompiledEmitters.add(jetBean);
                    }
                } catch (JETException e) {
                    log.error("Error during JetEmitter initalization " + e.getMessage(), e);
                }
                emitterPool.put(jetBean, emitter);
            }

            monitorWrap.worked(1);
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
            e.printStackTrace();
        }
        return unitCRC;
    }

    private static List<JetBean> loadEmfPersistentData(List<LightJetBean> datas, List<JetBean> completeJetBeanList)
            throws BusinessException {
        List<JetBean> toReturn = new ArrayList<JetBean>();
        for (JetBean unit : completeJetBeanList) {
            for (LightJetBean lightBean : datas) {
                if ((lightBean.getTemplateRelativeUri().compareTo(unit.getTemplateFullUri()) == 0)
                        && (lightBean.getVersion().compareTo(unit.getVersion()) == 0)
                        && (lightBean.getCrc() == extractTemplateHashCode(unit))) {
                    unit.setClassName(lightBean.getClassName());
                    try {
                        Method method = loadMethod(lightBean.getMethodName(), unit);
                        if (method != null) {
                            unit.setMethod(method);
                            toReturn.add(unit);
                        }
                    } catch (MalformedURLException e) {
                        log.error(Messages.getString("CodeGeneratorEmittersPoolFactory.JETEmitters.NoPresent")); //$NON-NLS-1$
                        throw new BusinessException(e);
                    } catch (ClassNotFoundException e) {
                        log.info(Messages.getString("CodeGeneratorEmittersPoolFactory.Class.NotFound", unit
                                .getClassName()));
                    }

                }
            }
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
    private static Method loadMethod(String methodName, JetBean unit) throws MalformedURLException,
            ClassNotFoundException {
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        IProject project = workspace.getRoot().getProject(".JETEmitters");

        URL url = new File(project.getLocation() + "/runtime").toURL();
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
