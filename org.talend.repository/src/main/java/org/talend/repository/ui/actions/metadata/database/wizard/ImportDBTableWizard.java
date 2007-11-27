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
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.dialogs.EventLoopProgressMonitor;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.MessageBoxExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.commons.ui.swt.dialogs.ProgressDialog;
import org.talend.commons.ui.swt.formtools.Form;
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

    /**
     * DOC ggu class global comment. Detailled comment
     */
    private final class LogsMessageDialog extends MessageDialog {

        private String logs;

        /**
         * DOC ggu LogsMessageDialog constructor comment.
         * 
         * @param parentShell
         */
        private LogsMessageDialog(Shell parentShell, String title, String messages, String logs) {
            super(parentShell, title, null, messages, INFORMATION, new String[] { IDialogConstants.OK_LABEL }, 0); //$NON-NLS-1$
            this.logs = logs;
        }

        @Override
        protected Control createCustomArea(Composite parent) {
            Group group = Form.createGroup(parent, 1, null, 200);

            Text text = new Text(group, SWT.V_SCROLL | SWT.H_SCROLL | SWT.READ_ONLY);
            // text.setBackground(new Color(text.getBackground().getDevice(), 255, 255, 255));
            GridData data = new GridData(GridData.FILL_BOTH);
            text.setLayoutData(data);
            if (logs != null) {
                text.setText(logs);
            }
            return group;
        }

    }

    private static Logger log = Logger.getLogger(ImportDBTableWizard.class);

    private static int totalWorks = 100;

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
            ConnectionDBTableHelper helper = new ConnectionDBTableHelper(file);
            progressDialog(helper);
            showLogsDialog(helper);
            return true;
        }
        return false;
    }

    private void progressDialog(final ConnectionDBTableHelper helper) {
        Shell activeShell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
        ProgressDialog progressDialog = new ProgressDialog(activeShell) {

            @Override
            public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                IProgressMonitor monitorWrap = new EventLoopProgressMonitor(monitor);
                monitorWrap.setCanceled(false);
                totalWorks = 0;
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(helper.getImportedFile()));
                    while (reader.readLine() != null) {
                        totalWorks++;
                    }
                } catch (FileNotFoundException e) {
                    return;
                } catch (IOException e) {
                    return;
                }
                monitor.beginTask(Messages.getString("ImportDBTableWizard.ProcessLabel"), totalWorks); //$NON-NLS-1$
                process(helper, monitorWrap);
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

    private void process(ConnectionDBTableHelper helper, IProgressMonitor monitor) {

        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(helper.getImportedFile()));
            String line;
            while ((line = reader.readLine()) != null) {
                monitor.worked(1);
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

    private void showLogsDialog(ConnectionDBTableHelper helper) {
        BufferedReader reader = null;
        try {
            File logsFile = helper.getLogsFile();
            StringBuilder logsMessage = new StringBuilder();
            reader = new BufferedReader(new FileReader(logsFile));
            String line;
            while ((line = reader.readLine()) != null) {
                logsMessage.append(line);
                logsMessage.append("\r\n"); //$NON-NLS-1$
            }
            // show log
            String title = Messages.getString("ImportDBTableWizard.LogMessageTitle"); //$NON-NLS-1$
            String message = Messages.getString("ImportDBTableWizard.LogMessageLabel", logsFile.toString()); //$NON-NLS-1$
            Dialog dialog = new LogsMessageDialog(getShell(), title, message, logsMessage.toString());
            dialog.open();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
                reader = null;
            }
        }
    }
}
