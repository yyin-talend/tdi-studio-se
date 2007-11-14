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
package org.talend.repository.ui.actions.metadata.database.wizard;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.dialogs.EventLoopProgressMonitor;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.MessageBoxExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.commons.ui.swt.dialogs.ProgressDialog;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.ui.images.ECoreImage;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNodeUtilities;
import org.talend.repository.ui.actions.metadata.database.ConnectionDBTableHelper;
import org.talend.repository.ui.actions.metadata.database.DBTableForDelimitedBean;
import org.talend.repository.ui.wizards.RepositoryWizard;

/**
 * ggu class global comment. Detailled comment <br/>
 * 
 */
public class ImportDBTableWizard extends RepositoryWizard implements IImportWizard {

    private static Logger log = Logger.getLogger(ImportDBTableWizard.class);

    private static final int TOTALWORK = 100;

    private static final IProxyRepositoryFactory FACTORY = ProxyRepositoryFactory.getInstance();

    private ImportDBTableWizardPage importWizardPage;

    /**
     * ggu ImportDBTableWizard constructor comment.
     */
    public ImportDBTableWizard(IWorkbench workbench, ISelection selection) {
        super(workbench, true);
        this.selection = selection;
        setNeedsProgressMonitor(true);
        initSetting();

    }

    private void initSetting() {
        if (selection == null) {
            pathToSave = new Path(""); //$NON-NLS-1$
            return;
        }

        Object userSelection = ((IStructuredSelection) selection).getFirstElement();
        if (userSelection instanceof RepositoryNode) {
            switch (((RepositoryNode) userSelection).getType()) {
            case SIMPLE_FOLDER:
                pathToSave = RepositoryNodeUtilities.getPath((RepositoryNode) userSelection);
                break;
            case SYSTEM_FOLDER:
                pathToSave = new Path(""); //$NON-NLS-1$
                break;
            default:
            }
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
     * org.eclipse.jface.viewers.IStructuredSelection)
     */
    public void init(IWorkbench workbench, IStructuredSelection selection) {
        super.setWorkbench(workbench);
        this.selection = selection;

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.Wizard#addPages()
     */
    @Override
    public void addPages() {
        setWindowTitle(Messages.getString("ImportDBTableWizard.WizardTitle")); //$NON-NLS-1$
        setDefaultPageImageDescriptor(ImageProvider.getImageDesc(ECoreImage.METADATA_CONNECTION_WIZ));
        importWizardPage = new ImportDBTableWizardPage();
        importWizardPage.setTitle(Messages.getString("ImportDBTableWizard.Title")); //$NON-NLS-1$
        importWizardPage.setDescription(Messages.getString("ImportDBTableWizard.Description")); //$NON-NLS-1$
        importWizardPage.setPageComplete(false);
        addPage(importWizardPage);

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.Wizard#performFinish()
     */
    @Override
    public boolean performFinish() {
        if (importWizardPage.isPageComplete()) {

            File file = importWizardPage.getFormSetting().getImportFile();
            if (file == null) {
                return false;
            }
            progressDialog(file);
            return true;
        }
        return false;
    }

    private void progressDialog(final File file) {
        Shell activeShell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
        ProgressDialog progressDialog = new ProgressDialog(activeShell) {

            @Override
            public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                IProgressMonitor monitorWrap = new EventLoopProgressMonitor(monitor);
                monitorWrap.setCanceled(false);
                monitor.beginTask(Messages.getString("ImportDBTableWizard.ProcessLabel"), 1000); //$NON-NLS-1$
                process(file, monitorWrap);
                monitorWrap.done();

            }
        };

        try {
            progressDialog.executeProcess();
        } catch (InvocationTargetException e) {
            MessageBoxExceptionHandler.process(e.getTargetException(), activeShell);
        } catch (InterruptedException e) {
            // Nothing to do
        }
    }

    private void process(File file, IProgressMonitor monitor) {

        BufferedReader reader = null;
        ConnectionDBTableHelper helper = new ConnectionDBTableHelper(file);

        try {
            reader = new BufferedReader(new FileReader(file));
            String line;
            int i = 0;
            while ((line = reader.readLine()) != null) {
                if (i < TOTALWORK * 0.8 && i % 2 == 0) {
                    i++;
                }
                monitor.worked(i);

                DBTableForDelimitedBean bean = helper.getRowData(line);
                if (bean != null) { // the line is suitable format.

                    ConnectionItem connItem = null;
                    try {
                        connItem = helper.setConnectionItemData(bean);
                    } catch (PersistenceException e) {
                        helper.recordRejects(bean, null);
                        continue;
                    } catch (BusinessException e) {
                        helper.recordRejects(bean, null);
                        continue;
                    }
                    if (connItem == null) {
                        helper.recordRejects(bean, null);
                        continue;
                    }
                    try {
                        if (helper.isConnectionCreated()) {
                            connItem.getProperty().setId(FACTORY.getNextId());
                            FACTORY.create(connItem, pathToSave);
                        } else {
                            FACTORY.save(connItem);
                        }
                    } catch (PersistenceException e) {
                        helper.recordRejects(bean, connItem);
                        continue;
                    }
                    helper.addRecords(bean);
                } else { // this line isn't right format. record it in ".log" and ".rejects"
                    // bean = null
                    helper.recordRejects(null, null);

                }
            }
            monitor.worked(TOTALWORK * 90 / 100);
            // write the .log and .rejects
            helper.writeRejects();
            helper.writeLogs();
        } catch (FileNotFoundException e) {
            MessageBoxExceptionHandler.process(e, getShell());

        } catch (IOException e) {
            MessageBoxExceptionHandler.process(e, getShell());

        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    // nothing to do
                }
                reader = null;
            }
        }

    }
}
