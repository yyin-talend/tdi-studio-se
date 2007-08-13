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
package org.talend.repository.ui.actions.metadata;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.model.metadata.builder.ConvertionHelper;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.DelimitedFileConnection;
import org.talend.core.model.metadata.builder.connection.GenericSchemaConnection;
import org.talend.core.model.metadata.builder.connection.LdifFileConnection;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.PositionalFileConnection;
import org.talend.core.model.metadata.builder.connection.RegexpFileConnection;
import org.talend.core.model.metadata.builder.connection.TableHelper;
import org.talend.core.model.metadata.builder.connection.XmlFileConnection;
import org.talend.core.model.metadata.builder.database.ExtractMetaDataFromDataBase;
import org.talend.core.model.properties.DatabaseConnectionItem;
import org.talend.core.model.properties.DelimitedFileConnectionItem;
import org.talend.core.model.properties.GenericSchemaConnectionItem;
import org.talend.core.model.properties.LdifFileConnectionItem;
import org.talend.core.model.properties.PositionalFileConnectionItem;
import org.talend.core.model.properties.RegExFileConnectionItem;
import org.talend.core.model.properties.XmlFileConnectionItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryNode.EProperties;
import org.talend.repository.ui.utils.ManagerConnection;
import org.talend.repository.ui.wizards.metadata.connection.genericshema.GenericSchemaTableWizard;
import org.talend.repository.ui.wizards.metadata.connection.genericshema.GenericSchemaWizard;
import org.talend.repository.ui.wizards.metadata.table.database.DatabaseTableWizard;
import org.talend.repository.ui.wizards.metadata.table.files.FileDelimitedTableWizard;
import org.talend.repository.ui.wizards.metadata.table.files.FileLdifTableWizard;
import org.talend.repository.ui.wizards.metadata.table.files.FilePositionalTableWizard;
import org.talend.repository.ui.wizards.metadata.table.files.FileRegexpTableWizard;
import org.talend.repository.ui.wizards.metadata.table.files.FileXmlTableWizard;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public abstract class AbstractCreateTableAction extends AbstractCreateAction {

    protected static final int WIZARD_WIDTH = 800;

    protected static final int WIZARD_HEIGHT = 475;

    /**
     * DOC mhelleboid Comment method "handleWizard".
     * 
     * @param node
     * @param wizardDialog
     */
    private void handleWizard(RepositoryNode node, WizardDialog wizardDialog) {
        wizardDialog.setPageSize(WIZARD_WIDTH, WIZARD_HEIGHT);
        wizardDialog.create();
        wizardDialog.open();
        getViewPart().expand(node, true);
        refresh(node);
    }

    protected String getStringIndexed(String string) {
        int indiceIndex = string.length();

        for (int f = 0; f <= string.length() - 1; f++) {
            try {
                String s = string.substring(f, string.length());
                if (String.valueOf(Integer.parseInt(s)).equals(s)) {
                    indiceIndex = f;
                    f = string.length();
                }
            } catch (Exception e) {
                // by default : indiceIndex = input.length()
            }
        }

        // validate the value is unique and indice then if needed
        while (!isUniqLabel(string)) {
            try {
                String indiceString = string.substring(indiceIndex, string.length());
                string = string.substring(0, indiceIndex) + (Integer.parseInt(indiceString) + 1);
            } catch (Exception e) {
                string = string + "1"; //$NON-NLS-1$
            }
        }
        return string;
    }

    /**
     * DOC ocarbone Comment method "isUniqLabel".
     * 
     * @param label
     * @return boolean
     */
    private boolean isUniqLabel(String label) {
        // Find the existings Metadata Name of Node
        String[] existingLabel = getExistingNames();
        if (existingLabel == null) {
            return true;
        } else {
            for (int i = 0; i < existingLabel.length; i++) {
                if (label.equals(existingLabel[i])) {
                    i = existingLabel.length;
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * DOC ocarbone Comment method "createFilePositionalTableWizard".
     * 
     * @param selection
     * @return
     */
    @SuppressWarnings("unchecked")//$NON-NLS-1$
    protected void createFilePositionalTableWizard(IStructuredSelection selection, boolean forceReadOnly) {
        Object obj = ((IStructuredSelection) selection).getFirstElement();
        RepositoryNode node = (RepositoryNode) obj;
        PositionalFileConnection connection = null;
        MetadataTable metadataTable = null;

        boolean creation = false;
        if (node.getType() == ENodeType.REPOSITORY_ELEMENT) {
            ERepositoryObjectType nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
            String tableLabel = (String) node.getProperties(EProperties.LABEL);

            PositionalFileConnectionItem item = null;
            switch (nodeType) {
            case METADATA_CON_TABLE:
                item = (PositionalFileConnectionItem) node.getParent().getObject().getProperty().getItem();
                connection = (PositionalFileConnection) item.getConnection();
                metadataTable = TableHelper.findByLabel(connection, tableLabel);
                creation = false;
                break;
            case METADATA_FILE_POSITIONAL:
                item = (PositionalFileConnectionItem) node.getObject().getProperty().getItem();
                connection = (PositionalFileConnection) item.getConnection();
                metadataTable = ConnectionFactory.eINSTANCE.createMetadataTable();
                // PTODO OCA : replace getStringIndexed by IndiceHelper.getIndexedLabel(metadataTable.getLabel(),
                // existingNames)
                // PTODO OCA : use IndiceHelper on multiple tableRefect
                metadataTable.setLabel(getStringIndexed(metadataTable.getLabel()));
                String nextId = ProxyRepositoryFactory.getInstance().getNextId();
                metadataTable.setId(nextId);
                connection.getTables().add(metadataTable);
                creation = true;
                break;
            default:
                return;
            }
            FilePositionalTableWizard filePositionalTableWizard = new FilePositionalTableWizard(PlatformUI.getWorkbench(),
                    creation, item, metadataTable, forceReadOnly);
            filePositionalTableWizard.setRepositoryObject(node.getObject());

            WizardDialog wizardDialog = new WizardDialog(new Shell(), filePositionalTableWizard);
            handleWizard(node, wizardDialog);
        }
    }

    /**
     * DOC ocarbone Comment method "createFileRegexpTableWizard".
     * 
     * @param selection
     * @return
     */
    @SuppressWarnings("unchecked")//$NON-NLS-1$
    protected void createFileRegexpTableWizard(IStructuredSelection selection, boolean forceReadOnly) {
        Object obj = ((IStructuredSelection) selection).getFirstElement();
        RepositoryNode node = (RepositoryNode) obj;
        RegexpFileConnection connection = null;
        MetadataTable metadataTable = null;

        boolean creation = false;
        if (node.getType() == ENodeType.REPOSITORY_ELEMENT) {
            ERepositoryObjectType nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
            String tableLabel = (String) node.getProperties(EProperties.LABEL);

            RegExFileConnectionItem item = null;
            switch (nodeType) {
            case METADATA_CON_TABLE:
                item = (RegExFileConnectionItem) node.getParent().getObject().getProperty().getItem();
                connection = (RegexpFileConnection) item.getConnection();
                metadataTable = TableHelper.findByLabel(connection, tableLabel);
                creation = false;
                break;
            case METADATA_FILE_REGEXP:
                item = (RegExFileConnectionItem) node.getObject().getProperty().getItem();
                connection = (RegexpFileConnection) item.getConnection();
                metadataTable = ConnectionFactory.eINSTANCE.createMetadataTable();
                String nextId = ProxyRepositoryFactory.getInstance().getNextId();
                metadataTable.setId(nextId);
                metadataTable.setLabel(getStringIndexed(metadataTable.getLabel()));
                connection.getTables().add(metadataTable);
                creation = true;
                break;
            default:
                return;
            }
            // set the repositoryObject, lock and set isRepositoryObjectEditable
            FileRegexpTableWizard fileRegexpTableWizard = new FileRegexpTableWizard(PlatformUI.getWorkbench(), creation, item,
                    metadataTable, forceReadOnly);
            fileRegexpTableWizard.setRepositoryObject(node.getObject());

            WizardDialog wizardDialog = new WizardDialog(new Shell(), fileRegexpTableWizard);
            handleWizard(node, wizardDialog);
        }
    }

    /**
     * DOC cantoine Comment method "createFileXmlTableWizard".
     * 
     * @param selection
     * @return
     */
    @SuppressWarnings("unchecked")//$NON-NLS-1$
    protected void createFileXmlTableWizard(IStructuredSelection selection, boolean forceReadOnly) {
        Object obj = ((IStructuredSelection) selection).getFirstElement();
        RepositoryNode node = (RepositoryNode) obj;
        XmlFileConnection connection = null;
        MetadataTable metadataTable = null;

        boolean creation = false;
        if (node.getType() == ENodeType.REPOSITORY_ELEMENT) {
            ERepositoryObjectType nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
            String tableLabel = (String) node.getProperties(EProperties.LABEL);

            XmlFileConnectionItem item = null;
            switch (nodeType) {
            case METADATA_CON_TABLE:
                item = (XmlFileConnectionItem) node.getParent().getObject().getProperty().getItem();
                connection = (XmlFileConnection) item.getConnection();
                metadataTable = TableHelper.findByLabel(connection, tableLabel);
                creation = false;
                break;
            case METADATA_FILE_XML:
                item = (XmlFileConnectionItem) node.getObject().getProperty().getItem();
                connection = (XmlFileConnection) item.getConnection();
                metadataTable = ConnectionFactory.eINSTANCE.createMetadataTable();
                String nextId = ProxyRepositoryFactory.getInstance().getNextId();
                metadataTable.setId(nextId);
                metadataTable.setLabel(getStringIndexed(metadataTable.getLabel()));
                connection.getTables().add(metadataTable);
                creation = true;
                break;
            default:
                return;
            }
            // set the repositoryObject, lock and set isRepositoryObjectEditable
            FileXmlTableWizard fileXmlTableWizard = new FileXmlTableWizard(PlatformUI.getWorkbench(), creation, item,
                    metadataTable, forceReadOnly);
            fileXmlTableWizard.setRepositoryObject(node.getObject());

            WizardDialog wizardDialog = new WizardDialog(new Shell(), fileXmlTableWizard);
            handleWizard(node, wizardDialog);
        }
    }

    /**
     * DOC ocarbone Comment method "createFileDelimitedTableWizard".
     * 
     * @param selection
     * @return
     */
    @SuppressWarnings("unchecked")//$NON-NLS-1$
    protected void createFileDelimitedTableWizard(IStructuredSelection selection, boolean forceReadOnly) {
        Object obj = ((IStructuredSelection) selection).getFirstElement();
        RepositoryNode node = (RepositoryNode) obj;
        DelimitedFileConnection connection = null;
        MetadataTable metadataTable = null;

        boolean creation = false;
        if (node.getType() == ENodeType.REPOSITORY_ELEMENT) {
            ERepositoryObjectType nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
            String tableLabel = (String) node.getProperties(EProperties.LABEL);

            DelimitedFileConnectionItem item = null;
            switch (nodeType) {
            case METADATA_CON_TABLE:
                item = (DelimitedFileConnectionItem) node.getParent().getObject().getProperty().getItem();
                connection = (DelimitedFileConnection) item.getConnection();
                metadataTable = TableHelper.findByLabel(connection, tableLabel);
                creation = false;
                break;
            case METADATA_FILE_DELIMITED:
                item = (DelimitedFileConnectionItem) node.getObject().getProperty().getItem();
                connection = (DelimitedFileConnection) item.getConnection();
                metadataTable = ConnectionFactory.eINSTANCE.createMetadataTable();
                String nextId = ProxyRepositoryFactory.getInstance().getNextId();
                metadataTable.setId(nextId);
                metadataTable.setLabel(getStringIndexed(metadataTable.getLabel()));
                connection.getTables().add(metadataTable);
                creation = true;
                break;
            default:
                return;
            }

            // set the repositoryObject, lock and set isRepositoryObjectEditable
            FileDelimitedTableWizard fileDelimitedTableWizard = new FileDelimitedTableWizard(PlatformUI.getWorkbench(), creation,
                    item, metadataTable, forceReadOnly);
            fileDelimitedTableWizard.setRepositoryObject(node.getObject());

            WizardDialog wizardDialog = new WizardDialog(new Shell(), fileDelimitedTableWizard);
            handleWizard(node, wizardDialog);
        }
    }

    /**
     * DOC cantoine Comment method "createFileLdifTableWizard".
     * 
     * @param selection
     * @return
     */
    @SuppressWarnings("unchecked")//$NON-NLS-1$
    protected void createFileLdifTableWizard(IStructuredSelection selection, boolean forceReadOnly) {
        Object obj = ((IStructuredSelection) selection).getFirstElement();
        RepositoryNode node = (RepositoryNode) obj;
        LdifFileConnection connection = null;
        MetadataTable metadataTable = null;

        boolean creation = false;
        if (node.getType() == ENodeType.REPOSITORY_ELEMENT) {
            ERepositoryObjectType nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
            String tableLabel = (String) node.getProperties(EProperties.LABEL);

            LdifFileConnectionItem item = null;
            switch (nodeType) {
            case METADATA_CON_TABLE:
                item = (LdifFileConnectionItem) node.getParent().getObject().getProperty().getItem();
                connection = (LdifFileConnection) item.getConnection();
                metadataTable = TableHelper.findByLabel(connection, tableLabel);
                creation = false;
                break;
            case METADATA_FILE_LDIF:
                item = (LdifFileConnectionItem) node.getObject().getProperty().getItem();
                connection = (LdifFileConnection) item.getConnection();
                metadataTable = ConnectionFactory.eINSTANCE.createMetadataTable();
                String nextId = ProxyRepositoryFactory.getInstance().getNextId();
                metadataTable.setId(nextId);
                metadataTable.setLabel(getStringIndexed(metadataTable.getLabel()));
                connection.getTables().add(metadataTable);
                creation = true;
                break;
            default:
                return;
            }

            // set the repositoryObject, lock and set isRepositoryObjectEditable
            FileLdifTableWizard fileLdifTableWizard = new FileLdifTableWizard(PlatformUI.getWorkbench(), creation, item,
                    metadataTable, forceReadOnly);
            fileLdifTableWizard.setRepositoryObject(node.getObject());

            WizardDialog wizardDialog = new WizardDialog(new Shell(), fileLdifTableWizard);
            handleWizard(node, wizardDialog);
        }
    }
    
    protected void createGenericSchemaWizard(IStructuredSelection selection, final boolean forceReadOnly)
    {
        Object obj = ((IStructuredSelection) selection).getFirstElement();
        RepositoryNode node = (RepositoryNode) obj;
        GenericSchemaConnection connection = null;
        MetadataTable metadataTable = null;

        boolean creation = false;
        if (node.getType() == ENodeType.REPOSITORY_ELEMENT) {
            ERepositoryObjectType nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
            String tableLabel = (String) node.getProperties(EProperties.LABEL);

            GenericSchemaConnectionItem item = null;
            switch (nodeType) {
            case METADATA_CON_TABLE:
                item = (GenericSchemaConnectionItem) node.getParent().getObject().getProperty().getItem();
                connection = (GenericSchemaConnection) item.getConnection();
                metadataTable = TableHelper.findByLabel(connection, tableLabel);
                creation = false;
                break;
            case METADATA_GENERIC_SCHEMA:
                item = (GenericSchemaConnectionItem) node.getObject().getProperty().getItem();
                connection = (GenericSchemaConnection) item.getConnection();
                metadataTable = ConnectionFactory.eINSTANCE.createMetadataTable();
                String nextId = ProxyRepositoryFactory.getInstance().getNextId();
                metadataTable.setId(nextId);
                metadataTable.setLabel(getStringIndexed(metadataTable.getLabel()));
                connection.getTables().add(metadataTable);
                creation = true;
                break;
            default:
                return;
            }

            // set the repositoryObject, lock and set isRepositoryObjectEditable
            GenericSchemaTableWizard genericSchemaWizard = new GenericSchemaTableWizard(PlatformUI.getWorkbench(),
                    creation, item, metadataTable, forceReadOnly);
            genericSchemaWizard.setRepositoryObject(node.getObject());

            WizardDialog wizardDialog = new WizardDialog(new Shell(), genericSchemaWizard);
            handleWizard(node, wizardDialog);
        }
        
    }

    /**
     * DOC ocarbone Comment method "creataDatabaseTableWizard".
     * 
     * @param selection
     * @return
     */
    @SuppressWarnings("unchecked")//$NON-NLS-1$
    protected void createDatabaseTableWizard(IStructuredSelection selection, final boolean forceReadOnly) {
        Object obj = ((IStructuredSelection) selection).getFirstElement();
        final RepositoryNode node = (RepositoryNode) obj;

        // Define the repositoryObject
        DatabaseConnection connection = null;
        MetadataTable metadataTable = null;

        boolean creation = false;
        if (node.getType() == ENodeType.REPOSITORY_ELEMENT) {
            ERepositoryObjectType nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
            String metadataTableLabel = (String) node.getProperties(EProperties.LABEL);

            DatabaseConnectionItem item = null;
            switch (nodeType) {
            case METADATA_CON_TABLE:
                item = (DatabaseConnectionItem) node.getObject().getProperty().getItem();
                connection = (DatabaseConnection) item.getConnection();
                metadataTable = TableHelper.findByLabel(connection, metadataTableLabel);
                creation = false;
                break;
            case METADATA_CONNECTIONS:
                item = (DatabaseConnectionItem) node.getObject().getProperty().getItem();
                connection = (DatabaseConnection) item.getConnection();
                // metadataTable = ConnectionFactory.eINSTANCE.createMetadataTable();
                // try {
                // int nextId = RepositoryFactoryProvider.getInstance().getNextId();
                // metadataTable.setId(nextId);
                // } catch (PersistenceException e) {
                // e.printStackTrace();
                // }
                // metadataTable.setLabel(getStringIndexed(metadataTable.getLabel()));
                // connection.getTables().add(metadataTable);
                creation = true;
                break;
            default:
                break;
            }
            openDatabaseTableWizard(item, metadataTable, forceReadOnly, node, creation);
        }
    }

    private void openDatabaseTableWizard(final DatabaseConnectionItem item, final MetadataTable metadataTable,
            final boolean forceReadOnly, final RepositoryNode node, final boolean creation) {
        Job job = new Job(Messages.getString("CreateTableAction.action.createTitle")) {

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.core.runtime.jobs.Job#run(org.eclipse.core.runtime.IProgressMonitor)
             */
            @Override
            protected IStatus run(IProgressMonitor monitor) {

                monitor.beginTask(Messages.getString("CreateTableAction.action.createTitle"), IProgressMonitor.UNKNOWN);

                final ManagerConnection managerConnection = new ManagerConnection();
                final boolean skipStep = checkConnectStatus(managerConnection, item);

                if(!monitor.isCanceled()){
                    try {
                        Display.getDefault().syncExec(new Runnable() {

                            public void run() {
                                DatabaseTableWizard databaseTableWizard = new DatabaseTableWizard(PlatformUI.getWorkbench(),
                                        creation, item, metadataTable, getExistingNames(), forceReadOnly, managerConnection);
                                databaseTableWizard.setSkipStep(skipStep);
                                databaseTableWizard.setRepositoryObject(node.getObject());

                                WizardDialog wizardDialog = new WizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow()
                                        .getShell(), databaseTableWizard);
                                wizardDialog.setBlockOnOpen(true);
                                handleWizard(node, wizardDialog);
                            }
                        });
                    } catch (Exception e) {
                        ExceptionHandler.process(e);
                    }
                }
                monitor.done();
                return Status.OK_STATUS;
            };
        };

        job.setUser(true);
        job.schedule();
    }

    public boolean checkConnectStatus(ManagerConnection managerConnection, DatabaseConnectionItem connectionItem) {
        boolean skipStep = false;

        managerConnection.check(ConvertionHelper.convert((DatabaseConnection) connectionItem.getConnection()));
        if (managerConnection.getIsValide()) {
            List<String> itemTableName = ExtractMetaDataFromDataBase.returnTablesFormConnection(ConvertionHelper
                    .convert((DatabaseConnection) connectionItem.getConnection()));
            if (itemTableName == null || itemTableName.isEmpty()) {
                skipStep = true;
            }
        } else {
            skipStep = true;
        }
        return skipStep;
    }
}
