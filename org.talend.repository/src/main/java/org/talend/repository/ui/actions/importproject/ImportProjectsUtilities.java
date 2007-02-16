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
package org.talend.repository.ui.actions.importproject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipFile;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.IOverwriteQuery;
import org.eclipse.ui.internal.wizards.datatransfer.ZipLeveledStructureProvider;
import org.eclipse.ui.wizards.datatransfer.FileSystemStructureProvider;
import org.eclipse.ui.wizards.datatransfer.IImportStructureProvider;
import org.eclipse.ui.wizards.datatransfer.ImportOperation;
import org.osgi.framework.Bundle;
import org.talend.commons.utils.io.FilesUtils;
import org.talend.repository.i18n.Messages;
import org.talend.resources.ResourcesPlugin;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * TODO SML Order methods
 * 
 * $Id$
 * 
 */
public class ImportProjectsUtilities {

    public static final String TALENDDEMOS_NAME = "TalendDemos";

    public static final String TALENDDEMOS_TECH_NAME = "TALENDDEMOS";

    public static final String TALEND_PROJECT_FILE_NAME = "talend.project";

    public static final String OLD_TALEND_PROJECT_FILE_NAME = "talendProject";

    public static void importProjectAs(Shell shell, String newName, String technicalName, String sourcePath,
            IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
        IImportStructureProvider provider = FileSystemStructureProvider.INSTANCE;

        importProject(shell, provider, new File(sourcePath), new Path(technicalName), true, false, monitor);

        afterImportAs(newName, technicalName);
    }

    /**
     * DOC smallet Comment method "afterImportAs".
     * 
     * @param newName
     * @param technicalName
     * @throws InvocationTargetException
     */
    private static void afterImportAs(String newName, String technicalName) throws InvocationTargetException {
        // Rename in ".project" and "talendProject" or "talend.project"
        // TODO SML Optimize
        final IWorkspace workspace = org.eclipse.core.resources.ResourcesPlugin.getWorkspace();
        IContainer containers = (IProject) workspace.getRoot().findMember(new Path(technicalName));

        IResource file2 = containers.findMember(IProjectDescription.DESCRIPTION_FILE_NAME);
        try {
            FilesUtils.replaceInFile("<name>.*</name>", file2.getLocation().toOSString(), "<name>" + technicalName + "</name>");

            IResource file3 = containers.findMember(OLD_TALEND_PROJECT_FILE_NAME);
            if (!file3.exists()) {
                file3 = containers.findMember(TALEND_PROJECT_FILE_NAME);
            }
            FilesUtils.replaceInFile("label=\".*?\"", file3.getLocation().toOSString(), "label=\"" + newName + "\"");
            FilesUtils.replaceInFile("technicalLabel=\".*?\"", file3.getLocation().toOSString(), "technicalLabel=\""
                    + technicalName + "\"");
        } catch (IOException e) {
            throw new InvocationTargetException(e);
        }
    }

    public static void importArchiveProjectAs(Shell shell, String newName, String technicalName, String sourcePath,
            IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
        importArchiveProject(shell, technicalName, sourcePath, monitor);

        afterImportAs(newName, technicalName);
    }

    public static void importArchiveProject(Shell shell, String technicalName, String sourcePath, IProgressMonitor monitor)
            throws InvocationTargetException, InterruptedException {
        ZipLeveledStructureProvider provider;
        try {
            provider = new ZipLeveledStructureProvider(new ZipFile(sourcePath));
        } catch (IOException e) {
            throw new InvocationTargetException(e);
        }

        importProject(shell, provider, provider.getRoot(), new Path(technicalName), false, true, monitor);
    }

    public static void importDemoProject(Shell shell) throws IOException, InvocationTargetException, InterruptedException {
        Bundle bundle = Platform.getBundle(ResourcesPlugin.PLUGIN_ID);
        URL url = FileLocator.resolve(bundle.getEntry("resources/TALENDDEMOS.zip")); //$NON-NLS-1$
        String archiveFilePath = new Path(url.getFile()).toOSString();

        importArchiveProject(shell, TALENDDEMOS_TECH_NAME, archiveFilePath, null);
    }

    private static void importProject(Shell shell, IImportStructureProvider provider, Object source, IPath path,
            boolean overwriteResources, boolean createContainerStructure, IProgressMonitor monitor)
            throws InvocationTargetException, InterruptedException {
        ArrayList fileSystemObjects = new ArrayList();
        ImportProjectsUtilities.getFilesForProject(fileSystemObjects, provider, source);

        ImportOperation operation = new ImportOperation(path, source, provider, //$NON-NLS-1$
                new MyOverwriteQuery(), fileSystemObjects);
        operation.setContext(shell);
        operation.setOverwriteResources(overwriteResources);
        operation.setCreateContainerStructure(createContainerStructure);
        operation.run(monitor);
    }

    /**
     * Return a list of all files in the project
     * 
     * Method as taken in org.eclipse.ui.internal.wizards.datatransfer.WizardProjectsImportPage.
     * 
     * @param provider The provider for the parent file
     * @param entry The root directory of the project
     * @return A list of all files in the project
     */
    public static boolean getFilesForProject(Collection files, IImportStructureProvider provider, Object entry) {
        List children = provider.getChildren(entry);
        Iterator childrenEnum = children.iterator();

        while (childrenEnum.hasNext()) {
            Object child = childrenEnum.next();
            // Add the child, this way we get every files except the project
            // folder itself which we don't want
            files.add(child);
            // We don't have isDirectory for tar so must check for children
            // instead
            if (provider.isFolder(child)) {
                getFilesForProject(files, provider, child);
            }
        }
        return true;
    }

    /**
     * Collect the list of .project files that are under directory into files.
     * 
     * <br/> Method almost as taken in org.eclipse.ui.internal.wizards.datatransfer.WizardProjectsImportPage.
     * Modifications are:
     * <ol>
     * <li>no recusrive search</li>
     * <li>add searchFileName as parameter</li>
     * <li>checks if monitor is null</li>
     * </ol>
     * 
     * @param files
     * @param directory
     * @param monitor The monitor to report to
     * @param searchFileName
     * @return boolean <code>true</code> if the operation was completed.
     */
    public static boolean collectProjectFilesFromDirectory(Collection files, File directory, IProgressMonitor monitor,
            String searchFileName) {
        if (monitor != null && monitor.isCanceled()) {
            return false;
        }
        if (monitor != null) {
            monitor.subTask(Messages.getString("ImportProjectAsWizardPage.form.checkingFolder", directory.getPath())); //$NON-NLS-1$
        }
        File[] contents = directory.listFiles();
        // first look for project description files
        for (int i = 0; i < contents.length; i++) {
            File file = contents[i];
            if (file.isFile() && file.getName().equals(searchFileName)) {
                files.add(file);
                // don't search sub-directories since we can't have nested
                // projects
                return true;
            }
        }
        return true;
    }

    /**
     * Collect the list of .project files that are under directory into files.
     * 
     * <br/> Method almost as taken in org.eclipse.ui.internal.wizards.datatransfer.WizardProjectsImportPage.
     * Modifications are:
     * <ol>
     * <li>no recusrive search</li>
     * <li>add searchFileName as parameter</li>
     * <li>checks if monitor is null</li>
     * </ol>
     * 
     * @param files
     * @param monitor The monitor to report to
     * @return boolean <code>true</code> if the operation was completed.
     */
    public static boolean collectProjectFilesFromProvider(Collection files, IImportStructureProvider provider, Object entry,
            int level, IProgressMonitor monitor, String searchFileName) {

        if (monitor != null && monitor.isCanceled()) {
            return false;
        }
        if (monitor != null) {
            monitor.subTask(Messages.getString("ImportProjectAsWizardPage.form.checkingFolder", provider.getLabel(entry))); //$NON-NLS-1$
        }
        List children = provider.getChildren(entry);
        if (children == null) {
            children = new ArrayList(1);
        }
        Iterator childrenEnum = children.iterator();
        while (childrenEnum.hasNext()) {
            Object child = childrenEnum.next();
            // if (provider.isFolder(child)) {
            // collectProjectFilesFromProvider(files, provider, child,
            // level + 1, monitor);
            // }
            String elementLabel = provider.getLabel(child);
            if (elementLabel.equals(searchFileName)) {
                files.add(elementLabel);
            }
        }
        return true;
    }

    /**
     * 
     * DOC smallet ImportDemoProjectAction class global comment. Detailled comment <br/>
     * 
     * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
     * 
     */
    private static class MyOverwriteQuery implements IOverwriteQuery {

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.ui.dialogs.IOverwriteQuery#queryOverwrite(java.lang.String)
         */
        public String queryOverwrite(String pathString) {
            return pathString;
        }

    }
}
