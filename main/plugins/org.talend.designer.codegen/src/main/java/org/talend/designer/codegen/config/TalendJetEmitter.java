// ============================================================================
//
// Copyright (C) 2006-2015 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.codegen.config;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.emf.codegen.CodeGenPlugin;
import org.eclipse.emf.codegen.jet.JETCompiler;
import org.eclipse.emf.codegen.jet.JETEmitter;
import org.eclipse.emf.codegen.jet.JETException;
import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.Monitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.talend.commons.debug.TalendDebugHandler;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.designer.codegen.i18n.Messages;

/**
 * DOC mhirt class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class TalendJetEmitter extends JETEmitter {

    private String templateName;

    private String templateLanguage;

    private String codePart;

    private TalendEclipseHelper talendEclipseHelper;

    private String componentFamily;

    private static Logger log = Logger.getLogger(TalendJetEmitter.class);

    /**
     * DOC mhirt TalendJetEmitter constructor comment.
     * 
     * @param arg0
     * @param arg1
     * @param globalClasspath
     * @throws JETException
     */
    public TalendJetEmitter(String arg0, ClassLoader arg1, IProgressMonitor progressMonitor,
            HashMap<String, String> globalClasspath, boolean rebuild) throws JETException {
        super(arg0, arg1);

        for (String classKey : globalClasspath.keySet()) {
            this.addVariable(classKey, globalClasspath.get(classKey));
        }
        this.talendEclipseHelper = new TalendEclipseHelper(progressMonitor, this, rebuild);
    }

    public TalendJetEmitter(String arg0, ClassLoader arg1, String componentFamily, String templateName, String templateLanguage,
            String codePart, TalendEclipseHelper teh) {
        super(arg0, arg1);
        if (templateName.endsWith(codePart + "" + templateLanguage)) { //$NON-NLS-1$
            this.templateName = templateName.substring(templateName.lastIndexOf(".") + 1, templateName.lastIndexOf(codePart)); //$NON-NLS-1$
        } else {
            this.templateName = templateName;
        }
        this.componentFamily = componentFamily;
        this.templateLanguage = templateLanguage;
        this.codePart = codePart;
        this.talendEclipseHelper = teh;
    }

    public TalendEclipseHelper getTalendEclipseHelper() {
        return this.talendEclipseHelper;
    }

    public void setTalendEclipseHelper(TalendEclipseHelper talendEclipseHelper) {
        this.talendEclipseHelper = talendEclipseHelper;
    }

    /**
     * Compiles the template to {@link #setMethod set} the method will be invoked to generate template results.
     * 
     * @param progressMonitor the progress monitor for tracking progress.
     */
    @Override
    public void initialize(IProgressMonitor progressMonitor) throws JETException {
        initialize(BasicMonitor.toMonitor(progressMonitor));
    }

    /**
     * Compiles the template to {@link #setMethod set} the method will be invoked to generate template results.
     * 
     * @param progressMonitor the progress monitor for tracking progress.
     */
    @Override
    public void initialize(Monitor progressMonitor) throws JETException {
        if (EMFPlugin.IS_ECLIPSE_RUNNING) {
            talendEclipseHelper.initialize(progressMonitor, this, componentFamily, templateName, templateLanguage, codePart);
        }
    }

    @Override
    public List getClasspathEntries() {
        // TODO Auto-generated method stub
        return super.getClasspathEntries();
    }

    @Override
    public void addVariable(String variableName, String pluginID) throws JETException {
        super.addVariable(variableName, pluginID);
    }

    /**
     * .
     */
    public class TalendEclipseHelper {

        IProject project;

        IJavaProject javaProject;

        public TalendEclipseHelper(IProgressMonitor progressMonitor, TalendJetEmitter jetEmitter, boolean rebuild)
                throws JETException {
            progressMonitor.beginTask("", 10); //$NON-NLS-1$

            try {
                final IWorkspace workspace = ResourcesPlugin.getWorkspace();
                IJavaModel javaModel = JavaCore.create(ResourcesPlugin.getWorkspace().getRoot());
                if (!javaModel.isOpen()) {
                    javaModel.open(new SubProgressMonitor(progressMonitor, 1));
                } else {
                    progressMonitor.worked(1);
                }

                project = workspace.getRoot().getProject(projectName);
                progressMonitor.subTask(CodeGenPlugin.getPlugin().getString("_UI_JETPreparingProject_message", //$NON-NLS-1$
                        new Object[] { project.getName() }));

                if (!project.exists()) {
                    progressMonitor.subTask("JET creating project " + project.getName()); //$NON-NLS-1$
                    project.create(new SubProgressMonitor(progressMonitor, 1));
                    progressMonitor.subTask(CodeGenPlugin.getPlugin().getString("_UI_JETCreatingProject_message", //$NON-NLS-1$
                            new Object[] { project.getName() }));
                }
                if (!project.isOpen()) {
                    project.open(new SubProgressMonitor(progressMonitor, 5));
                    project.refreshLocal(IResource.DEPTH_INFINITE, new SubProgressMonitor(progressMonitor, 1));
                }
                IProjectDescription description = project.getDescription();
                // only in case it's one old workspace and got no nature defined.
                if (!ArrayUtils.contains(description.getNatureIds(), JavaCore.NATURE_ID)) {
                    description.setNatureIds(new String[] { JavaCore.NATURE_ID });
                    project.setDescription(description, new SubProgressMonitor(progressMonitor, 1));
                }
                javaProject = JavaCore.create(project);

                progressMonitor.subTask(CodeGenPlugin.getPlugin().getString("_UI_JETInitializingProject_message", //$NON-NLS-1$
                        new Object[] { project.getName() }));
                IClasspathEntry classpathEntry = JavaCore.newSourceEntry(new Path("/" + project.getName() + "/src")); //$NON-NLS-1$ //$NON-NLS-2$

                IClasspathEntry jreClasspathEntry = JavaCore
                        .newContainerEntry(new Path("org.eclipse.jdt.launching.JRE_CONTAINER")); //$NON-NLS-1$

                Set<IClasspathEntry> classpath = new HashSet<IClasspathEntry>();
                classpath.add(classpathEntry);
                classpath.add(jreClasspathEntry);
                classpath.addAll(jetEmitter.getClasspathEntries());

                IFolder sourceFolder = project.getFolder(new Path("src")); //$NON-NLS-1$
                if (!sourceFolder.exists()) {
                    sourceFolder.create(false, true, new SubProgressMonitor(progressMonitor, 1));
                }
                IFolder runtimeFolder = project.getFolder(new Path("runtime")); //$NON-NLS-1$
                if (!runtimeFolder.exists()) {
                    runtimeFolder.create(false, true, new SubProgressMonitor(progressMonitor, 1));
                }

                if (isClasspathDifferent(javaProject, classpath)) {
                    IClasspathEntry[] classpathEntryArray = classpath.toArray(new IClasspathEntry[classpath.size()]);
                    javaProject.setRawClasspath(classpathEntryArray, new SubProgressMonitor(progressMonitor, 1));
                    javaProject.setOutputLocation(new Path("/" + project.getName() + "/runtime"), new SubProgressMonitor( //$NON-NLS-1$ //$NON-NLS-2$
                            progressMonitor, 1));
                    javaProject.getProject().build(IncrementalProjectBuilder.INCREMENTAL_BUILD, new NullProgressMonitor());
                }

                progressMonitor.done();
            } catch (CoreException exception) {
                throw new JETException(exception);
            } catch (Exception exception) {
                throw new JETException(exception);
            } finally {
                progressMonitor.done();
            }
        }

        private boolean isClasspathDifferent(IJavaProject javaProject, Set<IClasspathEntry> newClasspath) {
            IClasspathEntry[] rawClasspath;
            try {
                rawClasspath = javaProject.getRawClasspath();
            } catch (JavaModelException e) {
                return true;
            }

            Set<IClasspathEntry> settedClasspath = new HashSet<IClasspathEntry>();
            for (IClasspathEntry classpathEntry : rawClasspath) {
                settedClasspath.add(classpathEntry);
            }

            // source and target classpath must be the same
            for (IClasspathEntry classpathEntry : newClasspath) {
                if (!settedClasspath.contains(classpathEntry)) {
                    return true;
                }
            }

            for (IClasspathEntry classpathEntry : settedClasspath) {
                if (!newClasspath.contains(classpathEntry)) {
                    return true;
                }
            }

            return false;
        }

        public void initialize(Monitor monitor, TalendJetEmitter jetEmitter, String componentFamily, String templateName,
                String templateLanguage, String codePart) throws JETException {
            IProgressMonitor progressMonitor = BasicMonitor.toIProgressMonitor(monitor);
            progressMonitor.beginTask("", 10); //$NON-NLS-1$
            progressMonitor.subTask(CodeGenPlugin.getPlugin().getString("_UI_GeneratingJETEmitterFor_message", //$NON-NLS-1$
                    new Object[] { jetEmitter.templateURI }));

            ByteArrayOutputStream outputStream = null;
            InputStream contents = null;

            try {
                JETCompiler jetCompiler = null;
                if (jetEmitter.templateURIPath == null) {
                    jetCompiler = new TalendJETCompiler(jetEmitter.templateURI, jetEmitter.encoding, jetEmitter.classLoader);
                } else {
                    jetCompiler = new TalendJETCompiler(jetEmitter.templateURIPath, jetEmitter.templateURI, jetEmitter.encoding,
                            jetEmitter.classLoader);
                }
                progressMonitor.subTask(CodeGenPlugin.getPlugin().getString("_UI_JETParsing_message", //$NON-NLS-1$
                        new Object[] { jetCompiler.getResolvedTemplateURI() }));
                jetCompiler.parse();
                jetCompiler.getSkeleton().setPackageName("org.talend.designer.codegen.translators." + componentFamily); //$NON-NLS-1$
                jetCompiler.getSkeleton().setClassName(templateName + codePart + templateLanguage);
                progressMonitor.worked(1);

                outputStream = new ByteArrayOutputStream();
                jetCompiler.generate(outputStream);
                contents = new ByteArrayInputStream(outputStream.toByteArray());

                progressMonitor.subTask(CodeGenPlugin.getPlugin().getString("_UI_JETOpeningJavaProject_message", //$NON-NLS-1$
                        new Object[] { project.getName() }));

                String packageName = jetCompiler.getSkeleton().getPackageName();
                StringTokenizer stringTokenizer = new StringTokenizer(packageName, "."); //$NON-NLS-1$
                IProgressMonitor subProgressMonitor = new SubProgressMonitor(progressMonitor, 1);
                subProgressMonitor.beginTask("", stringTokenizer.countTokens() + 4); //$NON-NLS-1$
                subProgressMonitor.subTask(CodeGenPlugin.getPlugin().getString("_UI_CreateTargetFile_message")); //$NON-NLS-1$
                IFolder sourceContainer = project.getFolder("src");

                while (stringTokenizer.hasMoreElements()) {
                    String folderName = stringTokenizer.nextToken();
                    sourceContainer = sourceContainer.getFolder(new Path(folderName));
                    if (!sourceContainer.exists()) {
                        try {
                            sourceContainer.create(true, true, new SubProgressMonitor(subProgressMonitor, 1));
                        } catch (Exception e) {
                            ExceptionHandler.process(e);
                        }
                    }
                }
                boolean needRebuild = true;
                IFile targetFile = sourceContainer.getFile(new Path(jetCompiler.getSkeleton().getClassName() + ".java")); //$NON-NLS-1$
                if (!targetFile.exists()) {
                    subProgressMonitor.subTask(CodeGenPlugin.getPlugin().getString("_UI_JETCreating_message", //$NON-NLS-1$
                            new Object[] { targetFile.getFullPath() }));
                    targetFile.create(contents, true, new SubProgressMonitor(subProgressMonitor, 1));
                } else {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();

                    DataInputStream dis = new DataInputStream(targetFile.getContents());
                    int len = 0;
                    byte[] buf = new byte[1024];
                    while (((len = dis.read(buf))) != -1) {
                        baos.write(buf, 0, len);
                    }
                    dis.close();

                    String currentContent = baos.toString();
                    String newContent = outputStream.toString();
                    // since the build is done on linux, if use windows, it will use different rules for \r + \n
                    // so compare without \r, at least to see if original string is the same
                    // if yes, consider all is ok, no need to replace the content and recompile
                    currentContent = currentContent.replace("\r", ""); //$NON-NLS-1$//$NON-NLS-2$ 
                    newContent = newContent.replace("\r", ""); //$NON-NLS-1$//$NON-NLS-2$ 
                    if (!newContent.equals(currentContent)) {
                        subProgressMonitor.subTask(CodeGenPlugin.getPlugin().getString("_UI_JETUpdating_message", //$NON-NLS-1$
                                new Object[] { targetFile.getFullPath() }));
                        targetFile.setContents(contents, true, true, new SubProgressMonitor(subProgressMonitor, 1));
                    } else {
                        needRebuild = false;
                    }
                }

                // if jetEmitter.getMethod() == null, means the class file doesn't exist anymore
                // it should be impossible to have only the class file deleted and the .java never modified, but still
                // handle this case.
                if (needRebuild || jetEmitter.getMethod() == null) {
                    subProgressMonitor.subTask(CodeGenPlugin.getPlugin().getString("_UI_JETBuilding_message", //$NON-NLS-1$
                            new Object[] { project.getName() }));

                    project.build(IncrementalProjectBuilder.INCREMENTAL_BUILD, new SubProgressMonitor(subProgressMonitor, 1));

                    IMarker[] markers = targetFile.findMarkers(IMarker.PROBLEM, true, IResource.DEPTH_INFINITE);
                    boolean errors = false;
                    for (int i = 0; i < markers.length; ++i) {
                        IMarker marker = markers[i];
                        if (marker.getAttribute(IMarker.SEVERITY, IMarker.SEVERITY_INFO) == IMarker.SEVERITY_ERROR) {
                            errors = true;
                            subProgressMonitor.subTask(marker.getAttribute(IMarker.MESSAGE) + " : " //$NON-NLS-1$
                                    + (CodeGenPlugin.getPlugin().getString("jet.mark.file.line", new Object[] { //$NON-NLS-1$
                                            targetFile.getLocation(), marker.getAttribute(IMarker.LINE_NUMBER) })));
                            log.error(jetEmitter.templateURI.substring(jetEmitter.templateURI.lastIndexOf("/") + 1) //$NON-NLS-1$
                                    + Messages.getString(
                                            "TalendJetEmitter.compileFail", //$NON-NLS-1$
                                            marker.getAttribute(IMarker.MESSAGE),
                                            (CodeGenPlugin.getPlugin().getString("jet.mark.file.line", new Object[] { //$NON-NLS-1$
                                                    targetFile.getLocation(), marker.getAttribute(IMarker.LINE_NUMBER) }))));
                        }
                    }

                    if (!errors) {
                        subProgressMonitor.subTask(CodeGenPlugin.getPlugin().getString("_UI_JETLoadingClass_message", //$NON-NLS-1$
                                new Object[] { jetCompiler.getSkeleton().getClassName() + ".class" })); //$NON-NLS-1$

                        // Construct a proper URL for relative lookup.
                        //
                        URL url = new File(project.getLocation()
                                + "/" + javaProject.getOutputLocation().removeFirstSegments(1) + "/") //$NON-NLS-1$ //$NON-NLS-2$
                                .toURL();
                        URLClassLoader theClassLoader = new URLClassLoader(new URL[] { url }, jetEmitter.classLoader);
                        Class theClass = theClassLoader.loadClass((packageName.length() == 0 ? "" : packageName + ".") //$NON-NLS-1$ //$NON-NLS-2$
                                + jetCompiler.getSkeleton().getClassName());
                        String methodName = jetCompiler.getSkeleton().getMethodName();
                        Method[] methods = theClass.getDeclaredMethods();
                        for (int i = 0; i < methods.length; ++i) {
                            if (methods[i].getName().equals(methodName)) {
                                jetEmitter.setMethod(methods[i]);
                                break;
                            }
                        }
                    }
                }
                subProgressMonitor.done();
            } catch (CoreException exception) {
                TalendDebugHandler.debug(exception);
                throw new JETException(
                        Messages.getString("TalendJetEmitter.exception") + templateName + codePart + templateLanguage, exception); //$NON-NLS-1$
            } catch (Exception exception) {
                TalendDebugHandler.debug(exception);
                throw new JETException(
                        Messages.getString("TalendJetEmitter.exception") + templateName + codePart + templateLanguage, exception); //$NON-NLS-1$
            } finally {
                try {
                    contents.close();
                } catch (Exception e) {
                    // ignore me even if i'm null
                }

                try {
                    outputStream.close();
                } catch (Exception e) {
                    // ignore me even if i'm null
                }

                progressMonitor.done();
            }
        }
    }
}
