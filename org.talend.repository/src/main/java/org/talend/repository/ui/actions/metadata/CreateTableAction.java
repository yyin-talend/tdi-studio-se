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
package org.talend.repository.ui.actions.metadata;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.DelimitedFileConnection;
import org.talend.core.model.metadata.builder.connection.LdifFileConnection;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.PositionalFileConnection;
import org.talend.core.model.metadata.builder.connection.RegexpFileConnection;
import org.talend.core.model.metadata.builder.connection.TableHelper;
import org.talend.core.model.metadata.builder.connection.XmlFileConnection;
import org.talend.core.model.properties.DatabaseConnectionItem;
import org.talend.core.model.properties.DelimitedFileConnectionItem;
import org.talend.core.model.properties.LdifFileConnectionItem;
import org.talend.core.model.properties.PositionalFileConnectionItem;
import org.talend.core.model.properties.RegExFileConnectionItem;
import org.talend.core.model.properties.XmlFileConnectionItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.ui.images.ECoreImage;
import org.talend.repository.RepositoryPlugin;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.ERepositoryStatus;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryNode.EProperties;
import org.talend.repository.ui.wizards.metadata.table.database.DatabaseTableWizard;
import org.talend.repository.ui.wizards.metadata.table.files.FileDelimitedTableWizard;
import org.talend.repository.ui.wizards.metadata.table.files.FileLdifTableWizard;
import org.talend.repository.ui.wizards.metadata.table.files.FilePositionalTableWizard;
import org.talend.repository.ui.wizards.metadata.table.files.FileRegexpTableWizard;
import org.talend.repository.ui.wizards.metadata.table.files.FileXmlTableWizard;

/**
 * Action used to create table on metadata.<br/>
 * 
 * $Id$
 * 
 */
public class CreateTableAction extends AbstractCreateAction {

    protected static Logger log = Logger.getLogger(CreateConnectionAction.class);

    protected static final String PID = RepositoryPlugin.PLUGIN_ID;

    protected static final String CREATE_LABEL = Messages.getString("CreateTableAction.action.createTitle"); //$NON-NLS-1$

    protected static final String EDIT_LABEL = Messages.getString("CreateTableAction.action.editTitle"); //$NON-NLS-1$

    protected static final int WIZARD_WIDTH = 800;

    protected static final int WIZARD_HEIGHT = 475;

    public CreateTableAction() {
        super();

        this.setText(CREATE_LABEL);
        this.setToolTipText(CREATE_LABEL);
        this.setImageDescriptor(ImageProvider.getImageDesc(ECoreImage.METADATA_TABLE_ICON));
    }

    public void run() {
        RepositoryNode metadataNode = getViewPart().getRoot().getChildren().get(6);

        // Force focus to the repositoryView and open Metadata and DbConnection nodes
        getViewPart().setFocus();
        getViewPart().expand(metadataNode, true);

        IStructuredSelection selection = (IStructuredSelection) getSelection();
        RepositoryNode node = (RepositoryNode) selection.getFirstElement();

        // Init the content of the Wizard
        init(node);

        ERepositoryObjectType nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);

        if (ERepositoryObjectType.METADATA_CON_TABLE.equals(nodeType)) {
            nodeType = (ERepositoryObjectType) node.getParent().getProperties(EProperties.CONTENT_TYPE);
        }

        if (ERepositoryObjectType.METADATA_FILE_POSITIONAL.equals(nodeType)) {
            getViewPart().expand(metadataNode.getChildren().get(2), true);
            createFilePositionalTableWizard(selection);

        } else if (ERepositoryObjectType.METADATA_FILE_DELIMITED.equals(nodeType)) {
            getViewPart().expand(metadataNode.getChildren().get(1), true);
            createFileDelimitedTableWizard(selection);

        } else if (ERepositoryObjectType.METADATA_CONNECTIONS.equals(nodeType)) {
            getViewPart().expand(metadataNode.getChildren().get(0), true);
            createDatabaseTableWizard(selection);

        } else if (ERepositoryObjectType.METADATA_FILE_REGEXP.equals(nodeType)) {
            getViewPart().expand(metadataNode.getChildren().get(0), true);
            createFileRegexpTableWizard(selection);

        } else if (ERepositoryObjectType.METADATA_FILE_XML.equals(nodeType)) {
            getViewPart().expand(metadataNode.getChildren().get(0), true);
            createFileXmlTableWizard(selection);

        } else if (ERepositoryObjectType.METADATA_FILE_LDIF.equals(nodeType)) {
            getViewPart().expand(metadataNode.getChildren().get(0), true);
            createFileLdifTableWizard(selection);
        }
    }

    /**
     * DOC ocarbone Comment method "createFilePositionalTableWizard".
     * 
     * @param selection
     * @return
     */
    @SuppressWarnings("unchecked") //$NON-NLS-1$
    private void createFilePositionalTableWizard(IStructuredSelection selection) {
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
                    creation, item, metadataTable);
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
    @SuppressWarnings("unchecked") //$NON-NLS-1$
    private void createFileRegexpTableWizard(IStructuredSelection selection) {
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
                    metadataTable);
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
    @SuppressWarnings("unchecked") //$NON-NLS-1$
    private void createFileXmlTableWizard(IStructuredSelection selection) {
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
                    metadataTable);
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
    @SuppressWarnings("unchecked") //$NON-NLS-1$
    private void createFileDelimitedTableWizard(IStructuredSelection selection) {
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
                    item, metadataTable);
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
    @SuppressWarnings("unchecked") //$NON-NLS-1$
    private void createFileLdifTableWizard(IStructuredSelection selection) {
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
            FileLdifTableWizard fileLdifTableWizard = new FileLdifTableWizard(PlatformUI.getWorkbench(), creation,
                    item, metadataTable);
            fileLdifTableWizard.setRepositoryObject(node.getObject());

            WizardDialog wizardDialog = new WizardDialog(new Shell(), fileLdifTableWizard);
            handleWizard(node, wizardDialog);
        }
    }
    
    /**
     * DOC ocarbone Comment method "creataDatabaseTableWizard".
     * 
     * @param selection
     * @return
     */
    @SuppressWarnings("unchecked") //$NON-NLS-1$
    private void createDatabaseTableWizard(IStructuredSelection selection) {
        Object obj = ((IStructuredSelection) selection).getFirstElement();
        RepositoryNode node = (RepositoryNode) obj;

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
                item = (DatabaseConnectionItem) node.getParent().getObject().getProperty().getItem();
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
            DatabaseTableWizard databaseTableWizard = new DatabaseTableWizard(PlatformUI.getWorkbench(), creation, item,
                    metadataTable, getExistingNames());
            databaseTableWizard.setRepositoryObject(node.getObject());

            WizardDialog wizardDialog = new WizardDialog(Display.getCurrent().getActiveShell(), databaseTableWizard);
            handleWizard(node, wizardDialog);
        }
    }

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

    @Override
    public Class getClassForDoubleClick() {
        return IMetadataTable.class;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.actions.metadata.AbstractCreateAction#init(org.talend.repository.model.RepositoryNode)
     */
    @Override
    protected void init(RepositoryNode node) {
        setEnabled(false);
        if (ENodeType.REPOSITORY_ELEMENT.equals(node.getType())) {
            ERepositoryObjectType nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
            if (ERepositoryObjectType.METADATA_CON_TABLE.equals(nodeType)) {
                setText(EDIT_LABEL);
                collectSiblingNames(node);
                setEnabled(true);
                return;
            }

            IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
            if (factory.getStatus(node.getObject()) == ERepositoryStatus.DELETED) {
                return;
            }

            if (ERepositoryObjectType.METADATA_CONNECTIONS.equals(nodeType)
                    || ERepositoryObjectType.METADATA_FILE_DELIMITED.equals(nodeType)
                    || ERepositoryObjectType.METADATA_FILE_POSITIONAL.equals(nodeType)
                    || ERepositoryObjectType.METADATA_FILE_REGEXP.equals(nodeType)
                    || ERepositoryObjectType.METADATA_FILE_XML.equals(nodeType)
                    || ERepositoryObjectType.METADATA_FILE_LDIF.equals(nodeType)) {
                setText(CREATE_LABEL);
                collectChildNames(node);
                setEnabled(true);
                return;
            }
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

}
