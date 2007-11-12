// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the  agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//   
// ============================================================================
package org.talend.repository.ui.wizards.newproject;

import java.io.IOException;
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
import org.eclipse.ui.internal.wizards.datatransfer.TarException;
import org.eclipse.ui.internal.wizards.datatransfer.WizardProjectsImportPage;
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
public class ImportUnitTestProjectWizard extends Wizard {

    /** Main page. */
    private ImportProjectAsWizardPage mainPage;

    private String name;
    
    ///////////////////////////////
    private String technicalName;
    private String sourcePath; 
    private boolean isArchive;

    
    private WizardProjectsImportPage manyProjectsPage;

    /**
     * Constructs a new NewProjectWizard.
     * 
     * @param author Project author.
     * @param server
     * @param password
     * @param port2
     */
    public ImportUnitTestProjectWizard() {
        super();
        setDefaultPageImageDescriptor(ImageProvider.getImageDesc(ECoreImage.PROJECT_WIZ));
        setNeedsProgressMonitor(true);
        

    	/////// bypass the project selection window ///////
    	name = "test_unitaire";//project name : test_unitaire
    	technicalName = "TEST_UNITAIRE";
    	sourcePath = "D:\\A.zip";//define the path that contains the project archive
    	isArchive = true;
    	///////////////////////////////////////////////////
    	
    }

    /**
     * @see org.eclipse.jface.wizard.Wizard#addPages()
     */
    @Override
    public void addPages() {
        mainPage = new ImportProjectAsWizardPage();
        addPage(mainPage);

        manyProjectsPage = new WizardProjectsImportPage();
        addPage(manyProjectsPage);

        setWindowTitle(Messages.getString("ImportProjectAsWizard.windowTitle")); //$NON-NLS-1$
        setDefaultPageImageDescriptor(IDEWorkbenchPlugin.getIDEImageDescriptor("wizban/importproj_wiz.png")); //$NON-NLS-1$
    }

    public String getProjectName() {
        return name;
    }

    @Override
    public boolean canFinish() {
        return getContainer().getCurrentPage().isPageComplete();
    }

    /**
     * @see org.eclipse.jface.wizard.Wizard#performFinish()
     */
    @Override
    public boolean performFinish() {
//        if (getContainer().getCurrentPage().equals(manyProjectsPage)) {
//            return manyProjectsPage.createProjects();
//        } else {
//            name = mainPage.getName();
//            final String technicalName = mainPage.getTechnicalName();
//            final String sourcePath = mainPage.getSourcePath();
//            final boolean isArchive = mainPage.isArchive();

    	
    	
    	
    	
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
                            try {
                                ImportProjectsUtilities.importArchiveProjectAs(getShell(), name, technicalName, sourcePath,
                                        new SubProgressMonitor(monitor, 1));
                            } catch (TarException e) {
                                throw new InvocationTargetException(e, "Encoutering problems opening archive file");
                            } catch (IOException e) {
                                throw new InvocationTargetException(e, "Encoutering problems opening archive file");
                            }
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
                String message = Messages.getString("ImportProjectAsWizardPage.error.message"); //$NON-NLS-1$
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
//        }
    }
}
