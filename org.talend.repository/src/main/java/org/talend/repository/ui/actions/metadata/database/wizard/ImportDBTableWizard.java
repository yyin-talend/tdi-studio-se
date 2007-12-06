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
import java.util.Arrays;

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
import org.talend.commons.exception.MessageBoxExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.commons.ui.swt.dialogs.ProgressDialog;
import org.talend.commons.ui.swt.formtools.Form;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.ui.images.ECoreImage;
import org.talend.core.utils.CsvArray;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNodeUtilities;
import org.talend.repository.model.RepositoryNode.EProperties;
import org.talend.repository.ui.actions.metadata.database.ConnectionDBTableHelper;
import org.talend.repository.ui.actions.metadata.database.BeanParserHelper;
import org.talend.repository.ui.actions.metadata.database.LogDetailsHelper;
import org.talend.repository.ui.actions.metadata.importing.AbstractImportingTablesHelper;
import org.talend.repository.ui.actions.metadata.importing.DelimitedConnectionTablesHelper;
import org.talend.repository.ui.actions.metadata.importing.bean.TablesForDelimitedBean;
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

            Text text = new Text(group, SWT.V_SCROLL | SWT.H_SCROLL | SWT.READ_ONLY | SWT.BORDER);
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

    private ERepositoryObjectType nodeType;

    /**
     * ggu ImportDBTableWizard constructor comment.
     */
    public ImportDBTableWizard(IWorkbench workbench, ISelection selection) {
        super(workbench, true);
        this.selection = selection;
        initSetting();

    }

    private void initSetting() {
        if (selection == null) {
            pathToSave = new Path(""); //$NON-NLS-1$
            return;
        }

        Object userSelection = ((IStructuredSelection) selection).getFirstElement();
        if (userSelection instanceof RepositoryNode) {
            RepositoryNode node = (RepositoryNode) userSelection;
            nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);

            switch (node.getType()) {
            case SIMPLE_FOLDER:
                pathToSave = RepositoryNodeUtilities.getPath(node);
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
        switch (nodeType) {
        default:
            // unenabled
            return;
        case METADATA_CONNECTIONS:
            setDefaultPageImageDescriptor(ImageProvider.getImageDesc(ECoreImage.METADATA_CONNECTION_WIZ));
            break;
        case METADATA_FILE_DELIMITED:
            setDefaultPageImageDescriptor(ImageProvider.getImageDesc(ECoreImage.METADATA_FILE_DELIMITED_WIZ));
            break;
        // case METADATA_FILE_POSITIONAL:
        // case METADATA_FILE_REGEXP:
        // case METADATA_FILE_XML:
        // case METADATA_FILE_LDIF:
        // case METADATA_GENERIC_SCHEMA:
        // case METADATA_LDAP_SCHEMA:
        // break;
        }

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
            AbstractImportingTablesHelper helper = null;
            switch (nodeType) {
            default:
                // not process
                return true;
            case METADATA_CONNECTIONS:
                helper = new ConnectionDBTableHelper(file);

                break;
            case METADATA_FILE_DELIMITED:
                helper = new DelimitedConnectionTablesHelper(file);
                break;
            }
            progressDialog(helper);
            showLogsDialog(helper);
            return true;
        }
        return false;
    }

    private void progressDialog(final AbstractImportingTablesHelper helper) {
        Shell activeShell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
        ProgressDialog progressDialog = new ProgressDialog(activeShell) {

            @Override
            public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                IProgressMonitor monitorWrap = new EventLoopProgressMonitor(monitor);
                monitorWrap.setCanceled(false);
                totalWorks = 0;
                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new FileReader(helper.getImportedFile()));
                    while (reader.readLine() != null) {
                        totalWorks++;
                    }
                } catch (FileNotFoundException e) {
                    return;
                } catch (IOException e) {
                    return;
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                        }
                        reader = null;
                    }

                }
                monitor.beginTask(Messages.getString("ImportDBTableWizard.ProcessLabel"), totalWorks + 20); //$NON-NLS-1$
                processDelimited(helper, monitorWrap);
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

    private void processDelimited(AbstractImportingTablesHelper helper, IProgressMonitor monitor) {
        CsvArray array = new CsvArray();
        try {

            array = array.createFrom(helper.getImportedFile());
            for (String[] row : array.getRows()) {
                monitor.worked(1);
                TablesForDelimitedBean bean = BeanParserHelper.parseLineToBean(nodeType, Arrays.asList(row));
                if (bean != null) {
                    // the line is suitable format.
                    ConnectionItem connItem = null;
                    try {
                        connItem = helper.convertBeanToItem(bean);
                    } catch (Exception e) {
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
                } else {
                    helper.recordRejects(null, null);
                }
            }
        } catch (IOException e1) {

        }
        // write the .log and .rejects
        helper.writeRejects();
        monitor.worked(10);
        helper.writeLogs();
        monitor.worked(5);
    }

    private void showLogsDialog(AbstractImportingTablesHelper helper) {
        BufferedReader reader = null;
        try {
            File logsFile = helper.getLogsFile();
            StringBuilder logsMessage = new StringBuilder();
            reader = new BufferedReader(new FileReader(logsFile));
            String line;
            while ((line = reader.readLine()) != null) {
                logsMessage.append(line);
                logsMessage.append(LogDetailsHelper.RETURN);
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
