// ============================================================================
//
// Copyright (C) 2006-2010 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.ui.actions.metadata;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.UIJob;
import org.talend.core.database.conn.DatabaseConnStrUtil;
import org.talend.core.database.conn.template.EDatabaseConnTemplate;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.builder.ConvertionHelper;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.DelimitedFileConnection;
import org.talend.core.model.metadata.builder.connection.FileExcelConnection;
import org.talend.core.model.metadata.builder.connection.GenericSchemaConnection;
import org.talend.core.model.metadata.builder.connection.LDAPSchemaConnection;
import org.talend.core.model.metadata.builder.connection.LdifFileConnection;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.PositionalFileConnection;
import org.talend.core.model.metadata.builder.connection.RegexpFileConnection;
import org.talend.core.model.metadata.builder.connection.SalesforceSchemaConnection;
import org.talend.core.model.metadata.builder.connection.TableHelper;
import org.talend.core.model.metadata.builder.connection.WSDLSchemaConnection;
import org.talend.core.model.metadata.builder.connection.XmlFileConnection;
import org.talend.core.model.metadata.builder.database.ExtractMetaDataFromDataBase;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.DatabaseConnectionItem;
import org.talend.core.model.properties.DelimitedFileConnectionItem;
import org.talend.core.model.properties.ExcelFileConnectionItem;
import org.talend.core.model.properties.GenericSchemaConnectionItem;
import org.talend.core.model.properties.LDAPSchemaConnectionItem;
import org.talend.core.model.properties.LdifFileConnectionItem;
import org.talend.core.model.properties.PositionalFileConnectionItem;
import org.talend.core.model.properties.RegExFileConnectionItem;
import org.talend.core.model.properties.SalesforceSchemaConnectionItem;
import org.talend.core.model.properties.WSDLSchemaConnectionItem;
import org.talend.core.model.properties.XmlFileConnectionItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.RepositoryManager;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryNode.EProperties;
import org.talend.repository.ui.utils.ConnectionContextHelper;
import org.talend.repository.ui.utils.ManagerConnection;
import org.talend.repository.ui.wizards.metadata.connection.files.salesforce.SalesforceSchemaTableWizard;
import org.talend.repository.ui.wizards.metadata.connection.genericshema.GenericSchemaTableWizard;
import org.talend.repository.ui.wizards.metadata.connection.ldap.LDAPSchemaTableWizard;
import org.talend.repository.ui.wizards.metadata.connection.wsdl.WSDLSchemaTableWizard;
import org.talend.repository.ui.wizards.metadata.table.database.DatabaseTableWizard;
import org.talend.repository.ui.wizards.metadata.table.files.FileDelimitedTableWizard;
import org.talend.repository.ui.wizards.metadata.table.files.FileExcelTableWizard;
import org.talend.repository.ui.wizards.metadata.table.files.FileLdifTableWizard;
import org.talend.repository.ui.wizards.metadata.table.files.FilePositionalTableWizard;
import org.talend.repository.ui.wizards.metadata.table.files.FileRegexpTableWizard;
import org.talend.repository.ui.wizards.metadata.table.files.FileXmlTableWizard;

/**
 * DOC smallet class global comment. Detailed comment <br/>
 * 
 * $Id$
 * 
 */
public abstract class AbstractCreateTableAction extends AbstractCreateAction {

    protected static final int WIZARD_WIDTH = 800;

    protected static final int WIZARD_HEIGHT = 495;

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
        ERepositoryObjectType nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
        if (nodeType.isSubItem()) { // edit table
            RepositoryNode parent = node.getParent();
            if (parent.getObject() == null) { // db
                parent = parent.getParent();
            }
            RepositoryManager.refreshSavedNode(parent);
        } else {
            RepositoryManager.refreshCreatedNode(nodeType);
        }
    }

    /**
     * DOC mhelleboid Comment method "handleWizard".
     * 
     * @param node
     * @param wizardDialog
     */
    protected void handleWizard(RepositoryNode node, WizardDialog wizardDialog, boolean notSetSize) {
        if (!notSetSize) {
            wizardDialog.setPageSize(WIZARD_WIDTH, WIZARD_HEIGHT);
        }
        wizardDialog.create();
        if (wizardDialog.open() == wizardDialog.OK) {
            getViewPart().expand(node, true);
            refresh(node);
        }
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

    protected void initContextMode(ConnectionItem item) {
        ConnectionContextHelper.checkContextMode(item);
    }

    /**
     * DOC ocarbone Comment method "createFilePositionalTableWizard".
     * 
     * @param selection
     * @return
     */
    @SuppressWarnings("unchecked")//$NON-NLS-1$
    protected void createFilePositionalTableWizard(RepositoryNode node, boolean forceReadOnly) {

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
            initContextMode(item);
            FilePositionalTableWizard filePositionalTableWizard = new FilePositionalTableWizard(PlatformUI.getWorkbench(),
                    creation, item, metadataTable, forceReadOnly);
            filePositionalTableWizard.setRepositoryObject(node.getObject());

            WizardDialog wizardDialog = new WizardDialog(Display.getCurrent().getActiveShell(), filePositionalTableWizard);
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
    protected void createFileRegexpTableWizard(RepositoryNode node, boolean forceReadOnly) {
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
            initContextMode(item);
            // set the repositoryObject, lock and set isRepositoryObjectEditable
            FileRegexpTableWizard fileRegexpTableWizard = new FileRegexpTableWizard(PlatformUI.getWorkbench(), creation, item,
                    metadataTable, forceReadOnly);
            fileRegexpTableWizard.setRepositoryObject(node.getObject());

            WizardDialog wizardDialog = new WizardDialog(Display.getCurrent().getActiveShell(), fileRegexpTableWizard);
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
    protected void createFileXmlTableWizard(RepositoryNode node, boolean forceReadOnly) {
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
            initContextMode(item);
            // set the repositoryObject, lock and set isRepositoryObjectEditable
            FileXmlTableWizard fileXmlTableWizard = new FileXmlTableWizard(PlatformUI.getWorkbench(), creation, item,
                    metadataTable, forceReadOnly);
            fileXmlTableWizard.setRepositoryObject(node.getObject());

            WizardDialog wizardDialog = new WizardDialog(Display.getCurrent().getActiveShell(), fileXmlTableWizard);
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
    protected void createFileDelimitedTableWizard(RepositoryNode node, boolean forceReadOnly) {
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
            initContextMode(item);
            // set the repositoryObject, lock and set isRepositoryObjectEditable
            FileDelimitedTableWizard fileDelimitedTableWizard = new FileDelimitedTableWizard(PlatformUI.getWorkbench(), creation,
                    item, metadataTable, forceReadOnly);
            fileDelimitedTableWizard.setRepositoryObject(node.getObject());

            WizardDialog wizardDialog = new WizardDialog(Display.getCurrent().getActiveShell(), fileDelimitedTableWizard);
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
    protected void createFileLdifTableWizard(RepositoryNode node, boolean forceReadOnly) {
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
            initContextMode(item);
            // set the repositoryObject, lock and set isRepositoryObjectEditable
            FileLdifTableWizard fileLdifTableWizard = new FileLdifTableWizard(PlatformUI.getWorkbench(), creation, item,
                    metadataTable, forceReadOnly);
            fileLdifTableWizard.setRepositoryObject(node.getObject());

            WizardDialog wizardDialog = new WizardDialog(Display.getCurrent().getActiveShell(), fileLdifTableWizard);
            handleWizard(node, wizardDialog);
        }
    }

    /**
     * 
     * DOC yexiaowei Comment method "createFileExcelTableWizard".
     * 
     * @param selection
     * @param forceReadOnly
     */
    @SuppressWarnings("unchecked")//$NON-NLS-1$
    protected void createFileExcelTableWizard(RepositoryNode node, boolean forceReadOnly) {
        FileExcelConnection connection = null;
        MetadataTable metadataTable = null;

        boolean creation = false;
        if (node.getType() == ENodeType.REPOSITORY_ELEMENT) {
            ERepositoryObjectType nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
            String tableLabel = (String) node.getProperties(EProperties.LABEL);

            ExcelFileConnectionItem item = null;
            switch (nodeType) {
            case METADATA_CON_TABLE:
                item = (ExcelFileConnectionItem) node.getParent().getObject().getProperty().getItem();
                connection = (FileExcelConnection) item.getConnection();
                metadataTable = TableHelper.findByLabel(connection, tableLabel);
                creation = false;
                break;
            case METADATA_FILE_EXCEL:
                item = (ExcelFileConnectionItem) node.getObject().getProperty().getItem();
                connection = (FileExcelConnection) item.getConnection();
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
            initContextMode(item);
            // set the repositoryObject, lock and set isRepositoryObjectEditable
            FileExcelTableWizard fileExcelTableWizard = new FileExcelTableWizard(PlatformUI.getWorkbench(), creation, item,
                    metadataTable, forceReadOnly);
            fileExcelTableWizard.setRepositoryObject(node.getObject());

            WizardDialog wizardDialog = new WizardDialog(Display.getCurrent().getActiveShell(), fileExcelTableWizard);
            handleWizard(node, wizardDialog);
        }
    }

    protected void createGenericSchemaWizard(RepositoryNode node, final boolean forceReadOnly) {
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
            initContextMode(item);
            // set the repositoryObject, lock and set isRepositoryObjectEditable
            GenericSchemaTableWizard genericSchemaWizard = new GenericSchemaTableWizard(PlatformUI.getWorkbench(), creation,
                    item, metadataTable, forceReadOnly);
            genericSchemaWizard.setRepositoryObject(node.getObject());

            WizardDialog wizardDialog = new WizardDialog(Display.getCurrent().getActiveShell(), genericSchemaWizard);
            handleWizard(node, wizardDialog);
        }
    }

    /**
     * DOC Administrator Comment method "createLDAPSchemaWizard".
     * 
     * @param selection
     * @param b
     */
    public void createLDAPSchemaWizard(RepositoryNode node, final boolean forceReadOnly) {
        LDAPSchemaConnection connection = null;
        MetadataTable metadataTable = null;

        boolean creation = false;
        if (node.getType() == ENodeType.REPOSITORY_ELEMENT) {
            ERepositoryObjectType nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
            String tableLabel = (String) node.getProperties(EProperties.LABEL);

            LDAPSchemaConnectionItem item = null;
            switch (nodeType) {
            case METADATA_CON_TABLE:
                item = (LDAPSchemaConnectionItem) node.getParent().getObject().getProperty().getItem();
                connection = (LDAPSchemaConnection) item.getConnection();
                metadataTable = TableHelper.findByLabel(connection, tableLabel);
                creation = false;
                break;
            case METADATA_LDAP_SCHEMA:
                item = (LDAPSchemaConnectionItem) node.getObject().getProperty().getItem();
                connection = (LDAPSchemaConnection) item.getConnection();
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
            initContextMode(item);
            // set the repositoryObject, lock and set isRepositoryObjectEditable
            LDAPSchemaTableWizard ldapSchemaWizard = new LDAPSchemaTableWizard(PlatformUI.getWorkbench(), creation, item,
                    metadataTable, forceReadOnly);
            ldapSchemaWizard.setRepositoryObject(node.getObject());

            WizardDialog wizardDialog = new WizardDialog(Display.getCurrent().getActiveShell(), ldapSchemaWizard);
            handleWizard(node, wizardDialog);
        }

    }

    /**
     * 
     * DOC YeXiaowei Comment method "createSalesforceSchemaWizard".
     * 
     * @param selection
     * @param forceReadOnly
     */
    public void createSalesforceSchemaWizard(RepositoryNode node, final boolean forceReadOnly) {
        SalesforceSchemaConnection connection = null;
        MetadataTable metadataTable = null;

        boolean creation = false;
        if (node.getType() == ENodeType.REPOSITORY_ELEMENT) {
            ERepositoryObjectType nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
            String tableLabel = (String) node.getProperties(EProperties.LABEL);

            SalesforceSchemaConnectionItem item = null;
            switch (nodeType) {
            case METADATA_CON_TABLE:
                item = (SalesforceSchemaConnectionItem) node.getParent().getObject().getProperty().getItem();
                connection = (SalesforceSchemaConnection) item.getConnection();
                metadataTable = TableHelper.findByLabel(connection, tableLabel);
                creation = false;
                break;
            case METADATA_SALESFORCE_SCHEMA:
                item = (SalesforceSchemaConnectionItem) node.getObject().getProperty().getItem();
                connection = (SalesforceSchemaConnection) item.getConnection();
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
            initContextMode(item);
            // set the repositoryObject, lock and set isRepositoryObjectEditable
            SalesforceSchemaTableWizard salesforceSchemaWizard = new SalesforceSchemaTableWizard(PlatformUI.getWorkbench(),
                    creation, item, metadataTable, forceReadOnly);
            salesforceSchemaWizard.setRepositoryObject(node.getObject());

            WizardDialog wizardDialog = new WizardDialog(Display.getCurrent().getActiveShell(), salesforceSchemaWizard);
            handleWizard(node, wizardDialog);
        }
    }

    /**
     * DOC qzhang Comment method "createWSDLSchemaWizard".
     * 
     * @param selection
     * @param forceReadOnly
     */
    public void createWSDLSchemaWizard(RepositoryNode node, final boolean forceReadOnly) {
        WSDLSchemaConnection connection = null;
        MetadataTable metadataTable = null;

        boolean creation = false;
        if (node.getType() == ENodeType.REPOSITORY_ELEMENT) {
            ERepositoryObjectType nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
            String tableLabel = (String) node.getProperties(EProperties.LABEL);

            WSDLSchemaConnectionItem item = null;
            switch (nodeType) {
            case METADATA_CON_TABLE:
                item = (WSDLSchemaConnectionItem) node.getParent().getObject().getProperty().getItem();
                connection = (WSDLSchemaConnection) item.getConnection();
                metadataTable = TableHelper.findByLabel(connection, tableLabel);
                creation = false;
                break;
            case METADATA_WSDL_SCHEMA:
                item = (WSDLSchemaConnectionItem) node.getObject().getProperty().getItem();
                connection = (WSDLSchemaConnection) item.getConnection();
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
            initContextMode(item);
            // set the repositoryObject, lock and set isRepositoryObjectEditable
            WSDLSchemaTableWizard ldapSchemaWizard = new WSDLSchemaTableWizard(PlatformUI.getWorkbench(), creation, item,
                    metadataTable, forceReadOnly);
            ldapSchemaWizard.setRepositoryObject(node.getObject());

            WizardDialog wizardDialog = new WizardDialog(Display.getCurrent().getActiveShell(), ldapSchemaWizard);
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
    protected void createDatabaseTableWizard(final RepositoryNode node, final boolean forceReadOnly) {

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
            initContextMode(item);
            openDatabaseTableWizard(item, metadataTable, forceReadOnly, node, creation);
        }
    }

    private void openDatabaseTableWizard(final DatabaseConnectionItem item, final MetadataTable metadataTable,
            final boolean forceReadOnly, final RepositoryNode node, final boolean creation) {
        UIJob job = new UIJob(Messages.getString("CreateTableAction.action.createTitle")) { //$NON-NLS-1$

            @Override
            public IStatus runInUIThread(IProgressMonitor monitor) {

                monitor.beginTask(Messages.getString("CreateTableAction.action.createTitle"), IProgressMonitor.UNKNOWN); //$NON-NLS-1$

                if (!monitor.isCanceled()) {
                    final ManagerConnection managerConnection = new ManagerConnection();

                    IMetadataConnection metadataConnection = ConvertionHelper.convert((DatabaseConnection) item.getConnection());
                    if (!metadataConnection.getDbType().equals(EDatabaseConnTemplate.GODBC.getDBDisplayName())
                            && !metadataConnection.getDbType().equals(EDatabaseConnTemplate.ACCESS.getDBDisplayName())
                            && !metadataConnection.getDbType().equals(EDatabaseConnTemplate.GENERAL_JDBC.getDBDisplayName())) {
                        String genUrl = DatabaseConnStrUtil.getURLString(metadataConnection.getDbType(), metadataConnection
                                .getDbVersionString(), metadataConnection.getServerName(), metadataConnection.getUsername(),
                                metadataConnection.getPassword(), metadataConnection.getPort(), metadataConnection.getDatabase(),
                                metadataConnection.getFileFieldName(), metadataConnection.getDataSourceName(), metadataConnection
                                        .getDbRootPath(), metadataConnection.getAdditionalParams());
                        metadataConnection.setUrl(genUrl);
                    }

                    final boolean skipStep = checkConnectStatus(managerConnection, metadataConnection);
                    // if (skipStep) {
                    // MessageDialog.openError(null, "Connection error", managerConnection.getMessageException());
                    // }
                    DatabaseTableWizard databaseTableWizard = new DatabaseTableWizard(PlatformUI.getWorkbench(), creation, item,
                            metadataTable, getExistingNames(), forceReadOnly, managerConnection, metadataConnection);
                    databaseTableWizard.setSkipStep(skipStep);
                    databaseTableWizard.setRepositoryObject(node.getObject());
                    UIJob uijob = new UIJob("") { //$NON-NLS-1$

                        // modified by wzhang. when connection failed,error message display.
                        public IStatus runInUIThread(IProgressMonitor monitor) {
                            if (!managerConnection.getIsValide()) {
                                MessageDialog.openError(null, Messages.getString("AbstractCreateTableAction.connError"), //$NON-NLS-1$
                                        Messages.getString("AbstractCreateTableAction.errorMessage")); //$NON-NLS-1$
                            }
                            return Status.OK_STATUS;
                        }

                    };
                    WizardDialog wizardDialog = new WizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                            databaseTableWizard);
                    wizardDialog.setBlockOnOpen(true);
                    uijob.schedule(1300);
                    handleWizard(node, wizardDialog);
                }
                monitor.done();
                return Status.OK_STATUS;
            };
        };
        job.setUser(true);
        job.schedule();

    }

    public boolean checkConnectStatus(ManagerConnection managerConnection, IMetadataConnection metadataConnection) {
        boolean skipStep = false;

        managerConnection.check(metadataConnection);
        if (managerConnection.getIsValide()) {
            List<String> itemTableName = null;
            // hyWang remove the second parameter of method returnTablesFormConnection for bug7374
            itemTableName = ExtractMetaDataFromDataBase.returnTablesFormConnection(metadataConnection);
            if (itemTableName == null || itemTableName.isEmpty()) {
                skipStep = true;
            }
        } else {
            skipStep = true;
        }
        return skipStep;
    }
}
