// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2007 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License.
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
package org.talend.designer.codegen.config;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.core.resources.IContainer;
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
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;

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

    /**
     * DOC mhirt TalendJetEmitter constructor comment.
     * 
     * @param arg0
     * @param arg1
     * @param globalClasspath
     * @throws JETException
     */
    public TalendJetEmitter(String arg0, ClassLoader arg1, IProgressMonitor progressMonitor,
            HashMap<String, String> globalClasspath) throws JETException {
        super(arg0, arg1);

        for (String classKey : globalClasspath.keySet()) {
            this.addVariable(classKey, globalClasspath.get(classKey));
        }
        this.talendEclipseHelper = new TalendEclipseHelper(progressMonitor, this);
    }

    public TalendJetEmitter(String arg0, ClassLoader arg1, String templateName, String templateLanguage,
            String codePart, TalendEclipseHelper teh) {
        super(arg0, arg1);
        if (templateName.endsWith(codePart + "" + templateLanguage)) {
            this.templateName = templateName.substring(templateName.lastIndexOf(".") + 1, templateName
                    .lastIndexOf(codePart));
        } else {
            this.templateName = templateName;
        }
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
            talendEclipseHelper.initialize(progressMonitor, this, templateName, templateLanguage, codePart);
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

        public TalendEclipseHelper(IProgressMonitor progressMonitor, TalendJetEmitter jetEmitter) throws JETException {
            progressMonitor.beginTask("", 10);

            try {
                final IWorkspace workspace = ResourcesPlugin.getWorkspace();
                IJavaModel javaModel = JavaCore.create(ResourcesPlugin.getWorkspace().getRoot());
                if (!javaModel.isOpen()) {
                    javaModel.open(new SubProgressMonitor(progressMonitor, 1));
                } else {
                    progressMonitor.worked(1);
                }

                project = workspace.getRoot().getProject(projectName);
                progressMonitor.subTask(CodeGenPlugin.getPlugin().getString("_UI_JETPreparingProject_message",
                        new Object[] { project.getName() }));

                if (!project.exists()) {
                    progressMonitor.subTask("JET creating project " + project.getName());
                    project.create(new SubProgressMonitor(progressMonitor, 1));
                    progressMonitor.subTask(CodeGenPlugin.getPlugin().getString("_UI_JETCreatingProject_message",
                            new Object[] { project.getName() }));
                    IProjectDescription description = workspace.newProjectDescription(project.getName());
                    description.setNatureIds(new String[] { JavaCore.NATURE_ID });
                    description.setLocation(null);
                    project.open(new SubProgressMonitor(progressMonitor, 1));
                    project.setDescription(description, new SubProgressMonitor(progressMonitor, 1));
                } else {
                    project.open(new SubProgressMonitor(progressMonitor, 5));
                    IProjectDescription description = project.getDescription();
                    description.setNatureIds(new String[] { JavaCore.NATURE_ID });
                    project.setDescription(description, new SubProgressMonitor(progressMonitor, 1));
                }

                javaProject = JavaCore.create(project);

                progressMonitor.subTask(CodeGenPlugin.getPlugin().getString("_UI_JETInitializingProject_message",
                        new Object[] { project.getName() }));
                IClasspathEntry classpathEntry = JavaCore.newSourceEntry(new Path("/" + project.getName() + "/src"));

                IClasspathEntry jreClasspathEntry = JavaCore.newContainerEntry(new Path(
                        "org.eclipse.jdt.launching.JRE_CONTAINER"));

                List classpath = new ArrayList();
                classpath.add(classpathEntry);
                classpath.add(jreClasspathEntry);
                classpath.addAll(jetEmitter.getClasspathEntries());

                IFolder sourceFolder = project.getFolder(new Path("src"));
                if (!sourceFolder.exists()) {
                    sourceFolder.create(false, true, new SubProgressMonitor(progressMonitor, 1));
                }
                IFolder runtimeFolder = project.getFolder(new Path("runtime"));
                if (!runtimeFolder.exists()) {
                    runtimeFolder.create(false, true, new SubProgressMonitor(progressMonitor, 1));
                }

                IClasspathEntry[] classpathEntryArray = (IClasspathEntry[]) classpath
                        .toArray(new IClasspathEntry[classpath.size()]);

                javaProject.setRawClasspath(classpathEntryArray, new SubProgressMonitor(progressMonitor, 1));
                javaProject.setOutputLocation(new Path("/" + project.getName() + "/runtime"), new SubProgressMonitor(
                        progressMonitor, 1));
                javaProject.close();
                progressMonitor.done();
            } catch (CoreException exception) {
                throw new JETException(exception);
            } catch (Exception exception) {
                throw new JETException(exception);
            } finally {
                progressMonitor.done();
            }
        }

        public void initialize(Monitor monitor, TalendJetEmitter jetEmitter, String templateName,
                String templateLanguage, String codePart) throws JETException {
            IProgressMonitor progressMonitor = BasicMonitor.toIProgressMonitor(monitor);
            progressMonitor.beginTask("", 10);
            progressMonitor.subTask(CodeGenPlugin.getPlugin().getString("_UI_GeneratingJETEmitterFor_message",
                    new Object[] { jetEmitter.templateURI }));

            try {
                final JETCompiler jetCompiler = jetEmitter.templateURIPath == null ? new MyBaseJETCompiler(
                        jetEmitter.templateURI, jetEmitter.encoding, jetEmitter.classLoader)
                        : new MyBaseJETCompiler(jetEmitter.templateURIPath, jetEmitter.templateURI,
                                jetEmitter.encoding, jetEmitter.classLoader);

                progressMonitor.subTask(CodeGenPlugin.getPlugin().getString("_UI_JETParsing_message",
                        new Object[] { jetCompiler.getResolvedTemplateURI() }));
                jetCompiler.parse();
                jetCompiler.getSkeleton().setClassName(templateName + codePart + templateLanguage);
                progressMonitor.worked(1);

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                jetCompiler.generate(outputStream);
                final InputStream contents = new ByteArrayInputStream(outputStream.toByteArray());

                progressMonitor.subTask(CodeGenPlugin.getPlugin().getString("_UI_JETOpeningJavaProject_message",
                        new Object[] { project.getName() }));
                javaProject.open(new SubProgressMonitor(progressMonitor, 1));

                IPackageFragmentRoot[] packageFragmentRoots = javaProject.getPackageFragmentRoots();
                IPackageFragmentRoot sourcePackageFragmentRoot = null;
                for (int j = 0; j < packageFragmentRoots.length; ++j) {
                    IPackageFragmentRoot packageFragmentRoot = packageFragmentRoots[j];
                    if (packageFragmentRoot.getKind() == IPackageFragmentRoot.K_SOURCE) {
                        sourcePackageFragmentRoot = packageFragmentRoot;
                        break;
                    }
                }

                String packageName = jetCompiler.getSkeleton().getPackageName();
                StringTokenizer stringTokenizer = new StringTokenizer(packageName, ".");
                IProgressMonitor subProgressMonitor = new SubProgressMonitor(progressMonitor, 1);
                subProgressMonitor.beginTask("", stringTokenizer.countTokens() + 4);
                subProgressMonitor.subTask(CodeGenPlugin.getPlugin().getString("_UI_CreateTargetFile_message"));
                IContainer sourceContainer = (IContainer) sourcePackageFragmentRoot.getCorrespondingResource();
                while (stringTokenizer.hasMoreElements()) {
                    String folderName = stringTokenizer.nextToken();
                    sourceContainer = sourceContainer.getFolder(new Path(folderName));
                    if (!sourceContainer.exists()) {
                        ((IFolder) sourceContainer).create(false, true, new SubProgressMonitor(subProgressMonitor, 1));
                    }
                }
                IFile targetFile = sourceContainer
                        .getFile(new Path(jetCompiler.getSkeleton().getClassName() + ".java"));
                if (!targetFile.exists()) {
                    subProgressMonitor.subTask(CodeGenPlugin.getPlugin().getString("_UI_JETCreating_message",
                            new Object[] { targetFile.getFullPath() }));
                    targetFile.create(contents, true, new SubProgressMonitor(subProgressMonitor, 1));
                } else {
                    subProgressMonitor.subTask(CodeGenPlugin.getPlugin().getString("_UI_JETUpdating_message",
                            new Object[] { targetFile.getFullPath() }));
                    targetFile.setContents(contents, true, true, new SubProgressMonitor(subProgressMonitor, 1));
                }

                subProgressMonitor.subTask(CodeGenPlugin.getPlugin().getString("_UI_JETBuilding_message",
                        new Object[] { project.getName() }));
                project.build(IncrementalProjectBuilder.INCREMENTAL_BUILD,
                        new SubProgressMonitor(subProgressMonitor, 1));

                IMarker[] markers = targetFile.findMarkers(IMarker.PROBLEM, true, IResource.DEPTH_INFINITE);
                boolean errors = false;
                for (int i = 0; i < markers.length; ++i) {
                    IMarker marker = markers[i];
                    if (marker.getAttribute(IMarker.SEVERITY, IMarker.SEVERITY_INFO) == IMarker.SEVERITY_ERROR) {
                        errors = true;
                        subProgressMonitor.subTask(marker.getAttribute(IMarker.MESSAGE)
                                + " : "
                                + (CodeGenPlugin.getPlugin().getString("jet.mark.file.line", new Object[] {
                                        targetFile.getLocation(), marker.getAttribute(IMarker.LINE_NUMBER) })));
                    }
                }

                if (!errors) {
                    subProgressMonitor.subTask(CodeGenPlugin.getPlugin().getString("_UI_JETLoadingClass_message",
                            new Object[] { jetCompiler.getSkeleton().getClassName() + ".class" }));

                    // Construct a proper URL for relative lookup.
                    //
                    URL url = new File(project.getLocation() + "/"
                            + javaProject.getOutputLocation().removeFirstSegments(1) + "/").toURL();
                    URLClassLoader theClassLoader = new URLClassLoader(new URL[] { url }, jetEmitter.classLoader);
                    Class theClass = theClassLoader.loadClass((packageName.length() == 0 ? "" : packageName + ".")
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

                subProgressMonitor.done();
            } catch (CoreException exception) {
                throw new JETException(exception);
            } catch (Exception exception) {
                throw new JETException(exception);
            } finally {
                progressMonitor.done();
            }
        }
    }
}
