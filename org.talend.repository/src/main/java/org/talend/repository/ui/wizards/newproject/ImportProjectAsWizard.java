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
package org.talend.repository.ui.wizards.newproject;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
import org.eclipse.ui.internal.wizards.datatransfer.ArchiveFileManipulations;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.core.ui.images.ECoreImage;
import org.talend.repository.i18n.Messages;
import org.talend.repository.ui.actions.importproject.ImportProjectsUtilities;

/**
 * Wizard for the creation of a new project. <br/>
 * 
 * $Id: NewProjectWizard.java 1877 2007-02-06 17:16:43Z amaumont $
 * 
 */
public class ImportProjectAsWizard extends Wizard {

    /** Main page. */
    private ImportProjectAsWizardPage mainPage;

    private String name;

    /**
     * Constructs a new NewProjectWizard.
     * 
     * @param author Project author.
     * @param server
     * @param password
     * @param port2
     */
    public ImportProjectAsWizard() {
        super();
        setDefaultPageImageDescriptor(ImageProvider.getImageDesc(ECoreImage.PROJECT_WIZ));
        setNeedsProgressMonitor(true);
    }

    /**
     * @see org.eclipse.jface.wizard.Wizard#addPages()
     */
    @Override
    public void addPages() {
        mainPage = new ImportProjectAsWizardPage();
        addPage(mainPage);

        setWindowTitle(Messages.getString("ImportProjectAsWizard.windowTitle"));
        setDefaultPageImageDescriptor(IDEWorkbenchPlugin.getIDEImageDescriptor("wizban/importproj_wiz.png")); //$NON-NLS-1$
    }

    public String getProjectName() {
        return name;
    }

    /**
     * @see org.eclipse.jface.wizard.Wizard#performFinish()
     */
    @Override
    public boolean performFinish() {
        name = mainPage.getName();
        final String technicalName = mainPage.getTechnicalName();
        final String sourcePath = mainPage.getSourcePath();
        final boolean isArchive = mainPage.isArchive();

        WorkspaceModifyOperation op = new WorkspaceModifyOperation() {

            protected void execute(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                try {
                    monitor.beginTask("", 1); //$NON-NLS-1$
                    if (monitor.isCanceled()) {
                        throw new OperationCanceledException();
                    }

                    if (!isArchive) {
                        ImportProjectsUtilities.importProjectAs(getShell(), name, technicalName, sourcePath,
                                new SubProgressMonitor(monitor, 1));
                    } else {
                        ImportProjectsUtilities.importArchiveProjectAs(getShell(), name, technicalName, sourcePath,
                                new SubProgressMonitor(monitor, 1));
                    }
                } finally {
                    monitor.done();
                }
            }
        };

        // run the new project creation operation
        try {
            getContainer().run(false, true, op);
        } catch (InterruptedException e) {
            return false;
        } catch (InvocationTargetException e) {
            // one of the steps resulted in a core exception
            Throwable t = e.getTargetException();
            String message = Messages.getString("ImportProjectAsWizardPage.error.message");
            IStatus status;
            if (t instanceof CoreException) {
                status = ((CoreException) t).getStatus();
            } else {
                status = new Status(IStatus.ERROR, IDEWorkbenchPlugin.IDE_WORKBENCH, 1, message, t);
            }
            ErrorDialog.openError(getShell(), message, null, status);
            e.printStackTrace();
            return false;
        } finally {
            ArchiveFileManipulations.clearProviderCache(getContainer().getShell());
        }
        return true;

        // MessageBoxExceptionHandler.process(e, shell);

    }
}
