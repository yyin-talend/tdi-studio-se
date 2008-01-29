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
package org.talend.repository.ui.actions.importproject;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IPathVariableManager;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.undo.CreateFileOperation;
import org.eclipse.ui.ide.undo.WorkspaceUndoUtil;
import org.eclipse.ui.internal.ide.IDEWorkbenchMessages;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.CorePlugin;
import org.talend.core.model.general.ILibrariesService;
import org.talend.core.prefs.GeneralParametersProvider;
import org.talend.core.prefs.GeneralParametersProvider.GeneralParameters;
import org.talend.repository.i18n.Messages;
import org.talend.repository.ui.wizards.newproject.copyfromeclipse.TalendZipFileExportWizard;

/**
 * Action used to refresh a repository view.<br/>
 * 
 * $Id: RefreshAction.java 824 2006-12-01 15:49:55 +0000 (ven., 01 déc. 2006) smallet $
 * 
 */
public class ExportProjectsAsAction extends Action implements IWorkbenchWindowActionDelegate {

    private IWorkbenchWindow window;

    public ExportProjectsAsAction() {
        super();
    }

    public void run() {
        initializeExternalLibraries();
        Shell activeShell = Display.getCurrent().getActiveShell();
        TalendZipFileExportWizard docWizard = new TalendZipFileExportWizard();
        WizardDialog dialog = new WizardDialog(activeShell, docWizard);
        docWizard.setWindowTitle(Messages.getString("ExportProjectsAsAction.actionTitle"));
        dialog.create();
        dialog.open();
    }

    /**
     * DOC bqian Comment method "test".
     */
    private void initializeExternalLibraries() {
        initializeLibPath();
        final InputStream initialContents = null;
        final List<List<LinkTargetStore>> pathesList = getNeededLibraries();

        IRunnableWithProgress op = new IRunnableWithProgress() {

            public void run(IProgressMonitor monitor) {
                for (List<LinkTargetStore> pathes : pathesList) {
                    monitor.beginTask("create external libraries' links", pathes.size());

                    for (LinkTargetStore store : pathes) {
                        CreateFileOperation op = new CreateFileOperation(store.file, store.uri, initialContents,
                                IDEWorkbenchMessages.WizardNewFileCreationPage_title);
                        try {
                            PlatformUI.getWorkbench().getOperationSupport().getOperationHistory().execute(op, monitor,
                                    WorkspaceUndoUtil.getUIInfoAdapter(window.getShell()));
                        } catch (final ExecutionException e) {
                            ExceptionHandler.process(e);
                        }
                        monitor.worked(1);
                    }
                    monitor.done();
                }
            }
        };
        try {
            PlatformUI.getWorkbench().getActiveWorkbenchWindow().run(true, true, op);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final static String EXTERNAL_LIB_JAVA_PATH = "external_lib_java_path";

    public final static String EXTERNAL_LIB_PERL_PATH = "external_lib_perl_path";

    /**
     * DOC bqian Comment method "initializeLibPath".
     */
    private void initializeLibPath() {
        IPathVariableManager pathVariableManager = ResourcesPlugin.getWorkspace().getPathVariableManager();
        ILibrariesService libService = CorePlugin.getDefault().getLibrariesService();
        String libPath = libService.getLibrariesPath();
        try {
            Pattern javaPattern = Pattern.compile("(.*)java$");
            Pattern perlPattern = Pattern.compile("(.*)perl$");
            Matcher javaMatcher = javaPattern.matcher(libPath);
            Matcher perlMatcher = perlPattern.matcher(libPath);
            if (javaMatcher.find()) {
                pathVariableManager.setValue(EXTERNAL_LIB_JAVA_PATH, new Path(libPath));
                pathVariableManager.setValue(EXTERNAL_LIB_PERL_PATH, new Path(javaMatcher.group(1) + "perl"));
            } else if (perlMatcher.find()) {
                pathVariableManager.setValue(EXTERNAL_LIB_PERL_PATH, new Path(libPath));
                pathVariableManager.setValue(EXTERNAL_LIB_JAVA_PATH, new Path(perlMatcher.group(1) + "java"));
            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
    }

    /**
     * Returns the currently entered container name. Null if the field is empty. Note that the container may not exist
     * yet if the user entered a new container name in the field.
     * 
     * @return IPath
     */
    public IPath getContainerFullPath(String pathName) {
        // TODO the name of current project
        if (pathName == null || pathName.length() < 1) {
            return null;
        }
        // The user may not have made this absolute so do it for them
        return (new Path(pathName)).makeAbsolute();
    }

    /**
     * DOC bqian Comment method "getNeededLibraries".
     */
    private List<List<LinkTargetStore>> getNeededLibraries() {
        // TODO this is only a sample.
        String linkTarget = null;
        List<List<LinkTargetStore>> pathsList = new ArrayList<List<LinkTargetStore>>();
        List<String> list = this.getAllNames();

        List<String> notExportProjects = Arrays.asList(GeneralParametersProvider
                .getStrings(GeneralParameters.PROJECTS_EXCLUDED_FROM_EXPORT));
        List<IProject> projectList = new ArrayList<IProject>();
        IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
        for (int i = 0; i < projects.length; i++) {
            if (projects[i].isOpen() && !notExportProjects.contains(projects[i].getName())) {
                projectList.add(projects[i]);
            }
        }
        for (IProject project : projectList) {
            IPath containerPath = getContainerFullPath(project.getName());
            List<LinkTargetStore> paths = new ArrayList<LinkTargetStore>();
            for (String string : list) {
                LinkTargetStore store = new LinkTargetStore();
                IPath newFilePath = containerPath.append(string);
                IPathVariableManager pathVariableManager = ResourcesPlugin.getWorkspace().getPathVariableManager();

                Pattern javaP = Pattern.compile("java(.*)$");
                Pattern perlP = Pattern.compile("perl(.*)$");
                Matcher javaM = javaP.matcher(string);
                Matcher perlM = perlP.matcher(string);
                if (javaM.find()) {
                    linkTarget = EXTERNAL_LIB_JAVA_PATH + javaM.group(1);
                } else if (perlM.find()) {
                    linkTarget = EXTERNAL_LIB_PERL_PATH + perlM.group(1);
                }

                try {
                    URI path = new URI(linkTarget.replace(java.io.File.separatorChar, '/'));
                    store.file = ResourcesPlugin.getWorkspace().getRoot().getFile(newFilePath);
                    store.uri = path;
                    paths.add(store);
                } catch (Exception e) {
                    ExceptionHandler.process(e);
                }
            }
            pathsList.add(paths);
        }

        return pathsList;
    }

    public void dispose() {
    }

    public void init(IWorkbenchWindow window) {
        this.window = window;
    }

    public void run(IAction action) {
        run();
    }

    public void selectionChanged(IAction action, ISelection selection) {
    }

    private List<String> getAllNames() {
        File libraryFile = null;
        List<String> list = new ArrayList<String>();

        String newPath = null;
        String path = CorePlugin.getDefault().getLibrariesService().getLibrariesPath();
        libraryFile = new File(path);
        Pattern javaPattern = Pattern.compile("(.*)java$");
        Pattern perlPattern = Pattern.compile("(.*)perl$");
        Matcher javaMatcher = javaPattern.matcher(libraryFile.getPath());
        Matcher perlMatcher = perlPattern.matcher(libraryFile.getPath());
        if (javaMatcher.find()) {
            list = this.fileExist(list, "java");
            Pattern perlP = Pattern.compile("(.*)java$");
            Matcher perlM = perlP.matcher(libraryFile.getPath());
            if (perlM.find()) {
                list = this.fileExist(list, "perl");
            }
        }
        if (perlMatcher.find()) {
            list = this.fileExist(list, "perl");
            Pattern javaP = Pattern.compile("(.*)perl$");
            Matcher javaM = javaP.matcher(libraryFile.getPath());
            if (javaM.find()) {
                list = this.fileExist(list, "java");
            }
        }
        return list;
    }

    /**
     * DOC zwang Comment method "getAddedResources".
     * 
     * @return
     */
    private List<String> getAddedResources(String type) {
        try {
            String path = null;
            List<String> textList = new ArrayList<String>();
            List<String> pathList = new ArrayList<String>();
            IPreferenceStore preferenceStore = CorePlugin.getDefault().getPreferenceStore();
            String[] names = null;

            if (preferenceStore.contains(type)) {
                String string = preferenceStore.getString(type);
                if (null == string || "".equals(string)) {
                    return pathList;
                } else {
                    names = string.split(":");

                    for (String s : names) {
                        textList.add(s);
                    }

                    for (String textName : textList) {
                        path = type + java.io.File.separatorChar + textName;
                        pathList.add(path);
                    }

                    return pathList;
                }
            } else {
                return pathList;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

    }

    private List<String> fileExist(List<String> addResources, String type) {
        List<String> addResourceList = getAddedResources(type);

        for (String s : addResourceList) {
            addResources.add(s);
        }

        return addResources;
    }

    /**
     * use to store the file and the file's corresponding link target temporarily. <br/>
     * 
     * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ææäº, 29 ä¹æ 2006) nrousseau $
     * 
     */
    class LinkTargetStore {

        IFile file;

        URI uri;
    }

}
