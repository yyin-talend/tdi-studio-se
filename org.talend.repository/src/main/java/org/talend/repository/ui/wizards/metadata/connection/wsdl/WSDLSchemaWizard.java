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
package org.talend.repository.ui.wizards.metadata.connection.wsdl;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.commons.ui.swt.dialogs.ErrorDialogWidthDetailArea;
import org.talend.commons.utils.VersionUtils;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.metadata.IMetadataContextModeManager;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.WSDLSchemaConnection;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.update.RepositoryUpdateManager;
import org.talend.core.ui.images.ECoreImage;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNodeUtilities;
import org.talend.repository.ui.utils.ConnectionContextHelper;
import org.talend.repository.ui.wizards.CheckLastVersionRepositoryWizard;
import org.talend.repository.ui.wizards.PropertiesWizardPage;
import org.talend.repository.ui.wizards.metadata.MetadataContextModeManager;
import org.talend.repository.ui.wizards.metadata.connection.Step0WizardPage;

/**
 * DOC qwei class global comment. Detailled comment
 */
public class WSDLSchemaWizard extends CheckLastVersionRepositoryWizard implements INewWizard {

    private static Logger log = Logger.getLogger(WSDLSchemaWizard.class);

    private PropertiesWizardPage wsdlSchemaWizardPage0;

    private WSDLSchemaWizardPage wsdlSchemaWizardPage1;

    private WSDLSchemaWizardPage wsdlSchemaWizardPage2;

    private Property connectionProperty;

    private ConnectionItem connectionItem;

    private WSDLSchemaConnection connection;

    private boolean isSinglePageOnly;

    private static final String ALL_STEPS = "3"; //$NON-NLS-1$

    private Map<String, String> oldTableMap;

    private IMetadataContextModeManager contextModeManager;

    private List<IMetadataTable> oldMetadataTable;

    private boolean isToolbar;

    /**
     * Sets the isToolbar.
     * 
     * @param isToolbar the isToolbar to set
     */
    public void setToolbar(boolean isToolbar) {
        this.isToolbar = isToolbar;
    }

    /**
     * WSDLSchemaWizard constructor comment.
     * 
     * @param workbench
     * @param creation
     * @param selection
     * @param existingNames
     * @param b
     */
    public WSDLSchemaWizard(IWorkbench workbench, boolean creation, ISelection selection, String[] existingNames,
            boolean isSinglePageOnly) {
        super(workbench, creation);
        this.selection = selection;
        this.existingNames = existingNames;
        this.isSinglePageOnly = isSinglePageOnly;
        setNeedsProgressMonitor(true);

        // TODO: should to changed icon.
        setDefaultPageImageDescriptor(ImageProvider.getImageDesc(ECoreImage.DEFAULT_WIZ));

        if (selection == null || existingNames == null) {
            connection = ConnectionFactory.eINSTANCE.createWSDLSchemaConnection();
            connection.setTimeOut(WSDLSchemaStep1Form.TIMEOUT);
            MetadataTable metadataTable = ConnectionFactory.eINSTANCE.createMetadataTable();
            IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
            metadataTable.setId(factory.getNextId());
            connection.getTables().add(metadataTable);
            connectionProperty = PropertiesFactory.eINSTANCE.createProperty();
            connectionProperty
                    .setAuthor(((RepositoryContext) CorePlugin.getContext().getProperty(Context.REPOSITORY_CONTEXT_KEY))
                            .getUser());
            connectionProperty.setVersion(VersionUtils.DEFAULT_VERSION);
            connectionProperty.setStatusCode(""); //$NON-NLS-1$

            connectionItem = PropertiesFactory.eINSTANCE.createWSDLSchemaConnectionItem();
            connectionItem.setProperty(connectionProperty);
            connectionItem.setConnection(connection);
            initTable();
            return;
        }

        Object obj = ((IStructuredSelection) selection).getFirstElement();
        RepositoryNode node = (RepositoryNode) obj;
        switch (node.getType()) {
        case SIMPLE_FOLDER:
        case REPOSITORY_ELEMENT:
            pathToSave = RepositoryNodeUtilities.getPath(node);
            break;
        case SYSTEM_FOLDER:
            pathToSave = new Path(""); //$NON-NLS-1$
            break;
        }

        switch (node.getType()) {
        case SIMPLE_FOLDER:
        case SYSTEM_FOLDER:
            connection = ConnectionFactory.eINSTANCE.createWSDLSchemaConnection();
            connection.setTimeOut(WSDLSchemaStep1Form.TIMEOUT);
            MetadataTable metadataTable = ConnectionFactory.eINSTANCE.createMetadataTable();
            IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
            metadataTable.setId(factory.getNextId());
            connection.getTables().add(metadataTable);
            connectionProperty = PropertiesFactory.eINSTANCE.createProperty();
            connectionProperty
                    .setAuthor(((RepositoryContext) CorePlugin.getContext().getProperty(Context.REPOSITORY_CONTEXT_KEY))
                            .getUser());
            connectionProperty.setVersion(VersionUtils.DEFAULT_VERSION);
            connectionProperty.setStatusCode(""); //$NON-NLS-1$

            connectionItem = PropertiesFactory.eINSTANCE.createWSDLSchemaConnectionItem();
            connectionItem.setProperty(connectionProperty);
            connectionItem.setConnection(connection);
            break;

        case REPOSITORY_ELEMENT:
            connection = (WSDLSchemaConnection) ((ConnectionItem) node.getObject().getProperty().getItem()).getConnection();
            connectionProperty = node.getObject().getProperty();
            connectionItem = (ConnectionItem) node.getObject().getProperty().getItem();
            // set the repositoryObject, lock and set isRepositoryObjectEditable
            setRepositoryObject(node.getObject());
            isRepositoryObjectEditable();
            initLockStrategy();
            break;
        }
        initTable();
        initConnection();
    }

    public WSDLSchemaWizard(IWorkbench workbench, boolean creation, RepositoryNode node, String[] existingNames,
            boolean isSinglePageOnly) {
        super(workbench, creation);
        this.existingNames = existingNames;
        this.isSinglePageOnly = isSinglePageOnly;
        setNeedsProgressMonitor(true);

        // TODO: should to changed icon.
        setDefaultPageImageDescriptor(ImageProvider.getImageDesc(ECoreImage.DEFAULT_WIZ));
        switch (node.getType()) {
        case SIMPLE_FOLDER:
        case REPOSITORY_ELEMENT:
            pathToSave = RepositoryNodeUtilities.getPath(node);
            break;
        case SYSTEM_FOLDER:
            pathToSave = new Path(""); //$NON-NLS-1$
            break;
        }

        switch (node.getType()) {
        case SIMPLE_FOLDER:
        case SYSTEM_FOLDER:
            connection = ConnectionFactory.eINSTANCE.createWSDLSchemaConnection();
            connection.setTimeOut(WSDLSchemaStep1Form.TIMEOUT);
            MetadataTable metadataTable = ConnectionFactory.eINSTANCE.createMetadataTable();
            IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
            metadataTable.setId(factory.getNextId());
            connection.getTables().add(metadataTable);
            connectionProperty = PropertiesFactory.eINSTANCE.createProperty();
            connectionProperty
                    .setAuthor(((RepositoryContext) CorePlugin.getContext().getProperty(Context.REPOSITORY_CONTEXT_KEY))
                            .getUser());
            connectionProperty.setVersion(VersionUtils.DEFAULT_VERSION);
            connectionProperty.setStatusCode(""); //$NON-NLS-1$

            connectionItem = PropertiesFactory.eINSTANCE.createWSDLSchemaConnectionItem();
            connectionItem.setProperty(connectionProperty);
            connectionItem.setConnection(connection);
            break;

        case REPOSITORY_ELEMENT:
            connection = (WSDLSchemaConnection) ((ConnectionItem) node.getObject().getProperty().getItem()).getConnection();
            connectionProperty = node.getObject().getProperty();
            connectionItem = (ConnectionItem) node.getObject().getProperty().getItem();
            // set the repositoryObject, lock and set isRepositoryObjectEditable
            setRepositoryObject(node.getObject());
            isRepositoryObjectEditable();
            initLockStrategy();
            break;
        }
        initTable();
        initConnection();
    }

    private void initTable() {
        if (connectionItem != null) {
            oldTableMap = RepositoryUpdateManager.getTableIdAndNameMap(connectionItem);
            oldMetadataTable = RepositoryUpdateManager.getConversionMetadataTables(connectionItem.getConnection());
        }
    }

    private void initConnection() {
        ConnectionContextHelper.checkContextMode(connectionItem);
        contextModeManager = new MetadataContextModeManager();
        if (connectionItem.getConnection().isContextMode()) {
            ContextType contextTypeForContextMode = ConnectionContextHelper.getContextTypeForContextMode(connectionItem
                    .getConnection(), true);
            contextModeManager.setSelectedContextType(contextTypeForContextMode);
        }
    }

    /**
     * Adding the page to the wizard.
     */
    public void addPages() {
        wsdlSchemaWizardPage0 = null;
        wsdlSchemaWizardPage1 = null;
        wsdlSchemaWizardPage2 = null;

        if (isToolbar) {
            pathToSave = null;
        }

        if (creation) {
            setWindowTitle(Messages.getString("WSDLSchemaWizard.CreateNewWSDLSchema"));// Messages.getString( //$NON-NLS-1$
            // "DelimitedFileWizard.windowTitleCreate"
            // ));
            // //$NON-NLS-1$
            // //$NON-NLS-1$
            wsdlSchemaWizardPage0 = new Step0WizardPage(connectionProperty, pathToSave,
                    ERepositoryObjectType.METADATA_WSDL_SCHEMA, !isRepositoryObjectEditable(), creation);
            wsdlSchemaWizardPage0.setTitle(Messages.getString("FileWizardPage.titleCreate") + " 1 " //$NON-NLS-1$ //$NON-NLS-2$
                    + Messages.getString("FileWizardPage.of") + " " + ALL_STEPS); //$NON-NLS-1$ //$NON-NLS-2$
            wsdlSchemaWizardPage0.setDescription(Messages.getString("FileWizardPage.descriptionCreateStep0")); //$NON-NLS-1$
            addPage(wsdlSchemaWizardPage0);
            wsdlSchemaWizardPage1 = new WSDLSchemaWizardPage(1, connectionItem, isRepositoryObjectEditable(), null,
                    contextModeManager);
            wsdlSchemaWizardPage1.setTitle(Messages.getString("FileWizardPage.titleCreate") + " 2 " //$NON-NLS-1$ //$NON-NLS-2$
                    + Messages.getString("FileWizardPage.of") + " " + ALL_STEPS); //$NON-NLS-1$ //$NON-NLS-2$
            wsdlSchemaWizardPage1.setDescription(Messages.getString("FileWizardPage.descriptionCreateStep1")); //$NON-NLS-1$
            addPage(wsdlSchemaWizardPage1);
            wsdlSchemaWizardPage2 = new WSDLSchemaWizardPage(2, connectionItem, isRepositoryObjectEditable(), null,
                    contextModeManager);
            wsdlSchemaWizardPage2.setTitle(Messages.getString("FileWizardPage.titleCreate") + " 3 " //$NON-NLS-1$ //$NON-NLS-2$
                    + Messages.getString("FileWizardPage.of") + " " + ALL_STEPS); //$NON-NLS-1$ //$NON-NLS-2$
            wsdlSchemaWizardPage2.setDescription(Messages.getString("FileWizardPage.descriptionCreateStep2")); //$NON-NLS-1$
            addPage(wsdlSchemaWizardPage2);
            wsdlSchemaWizardPage0.setPageComplete(false);
            wsdlSchemaWizardPage1.setPageComplete(false);
            wsdlSchemaWizardPage2.setPageComplete(isRepositoryObjectEditable());

        } else if (this.isSinglePageOnly == false) {

            setWindowTitle(Messages.getString("WSDLSchemaWizard.UpdateWSDLSchema"));// Messages.getString( //$NON-NLS-1$
            // "DelimitedFileWizard.windowTitleUpdate"
            // ));
            // //$NON-NLS-1$
            // //$NON-NLS-1$

            wsdlSchemaWizardPage0 = new Step0WizardPage(connectionProperty, pathToSave,
                    ERepositoryObjectType.METADATA_WSDL_SCHEMA, !isRepositoryObjectEditable(), creation);

            wsdlSchemaWizardPage0.setTitle(Messages.getString("FileWizardPage.titleCreate") + " 1 " //$NON-NLS-1$ //$NON-NLS-2$
                    + Messages.getString("FileWizardPage.of") + " " + ALL_STEPS); //$NON-NLS-1$ //$NON-NLS-2$
            wsdlSchemaWizardPage0.setDescription(Messages.getString("FileWizardPage.descriptionCreateStep0")); //$NON-NLS-1$
            addPage(wsdlSchemaWizardPage0);
            wsdlSchemaWizardPage1 = new WSDLSchemaWizardPage(1, connectionItem, isRepositoryObjectEditable(), null,
                    contextModeManager);
            wsdlSchemaWizardPage1.setTitle(Messages.getString("FileWizardPage.titleUpdate") + " 2 " //$NON-NLS-1$ //$NON-NLS-2$
                    + Messages.getString("FileWizardPage.of") + " " + ALL_STEPS); //$NON-NLS-1$ //$NON-NLS-2$
            wsdlSchemaWizardPage1.setDescription(Messages.getString("FileWizardPage.descriptionUpdateStep1")); //$NON-NLS-1$
            addPage(wsdlSchemaWizardPage1);

            wsdlSchemaWizardPage2 = new WSDLSchemaWizardPage(2, connectionItem, isRepositoryObjectEditable(), null,
                    contextModeManager);
            wsdlSchemaWizardPage2.setTitle(Messages.getString("FileWizardPage.titleUpdate") + " 3 " //$NON-NLS-1$ //$NON-NLS-2$
                    + Messages.getString("FileWizardPage.of") + " " + ALL_STEPS); //$NON-NLS-1$ //$NON-NLS-2$
            wsdlSchemaWizardPage2.setDescription(Messages.getString("FileWizardPage.descriptionUpdateStep2")); //$NON-NLS-1$
            addPage(wsdlSchemaWizardPage2);

        } else {
            setWindowTitle(Messages.getString("WSDLSchemaWizard.SaveAsWSDLSchema"));// Messages.getString( //$NON-NLS-1$
            // "DelimitedFileWizard.windowTitleUpdate"
            // ));
            // //$NON-NLS-1$
            // //$NON-NLS-1$
            wsdlSchemaWizardPage0 = new Step0WizardPage(connectionProperty, pathToSave,
                    ERepositoryObjectType.METADATA_WSDL_SCHEMA, !isRepositoryObjectEditable(), true);
            wsdlSchemaWizardPage0.setTitle(Messages.getString("FileWizardPage.titleCreate") + " 1 " //$NON-NLS-1$ //$NON-NLS-2$
                    + Messages.getString("FileWizardPage.of") + " " + ALL_STEPS); //$NON-NLS-1$ //$NON-NLS-2$
            wsdlSchemaWizardPage0.setDescription(Messages.getString("FileWizardPage.descriptionCreateStep0")); //$NON-NLS-1$

            addPage(wsdlSchemaWizardPage0);
            wsdlSchemaWizardPage1 = new WSDLSchemaWizardPage(1, connectionItem, isRepositoryObjectEditable(), null,
                    contextModeManager);
            wsdlSchemaWizardPage1.setTitle(Messages.getString("FileWizardPage.titleUpdate") + " 2 " //$NON-NLS-1$ //$NON-NLS-2$
                    + Messages.getString("FileWizardPage.of") + " " + ALL_STEPS); //$NON-NLS-1$ //$NON-NLS-2$
            wsdlSchemaWizardPage1.setDescription(Messages.getString("FileWizardPage.descriptionUpdateStep1")); //$NON-NLS-1$
            addPage(wsdlSchemaWizardPage1);

            wsdlSchemaWizardPage2 = new WSDLSchemaWizardPage(2, connectionItem, isRepositoryObjectEditable(), null,
                    contextModeManager);
            wsdlSchemaWizardPage2.setTitle(Messages.getString("FileWizardPage.titleUpdate") + " 3 " //$NON-NLS-1$ //$NON-NLS-2$
                    + Messages.getString("FileWizardPage.of") + " " + ALL_STEPS); //$NON-NLS-1$ //$NON-NLS-2$
            wsdlSchemaWizardPage2.setDescription(Messages.getString("FileWizardPage.descriptionUpdateStep2")); //$NON-NLS-1$
            addPage(wsdlSchemaWizardPage2);

        }
    }

    /**
     * We will accept the selection in the workbench to see if we can initialize from it.
     * 
     * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
     */
    public void init(final IWorkbench workbench, final IStructuredSelection selection2) {
        this.selection = selection2;
    }

    /**
     * Getter for connectionProperty.
     * 
     * @return the connectionProperty
     */
    public Property getConnectionProperty() {
        return this.connectionProperty;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.Wizard#performFinish()
     */
    @Override
    public boolean performFinish() {
        if (isSinglePageOnly) {
            return true;
        }

        boolean formIsPerformed;
        if (wsdlSchemaWizardPage2 == null) {
            formIsPerformed = wsdlSchemaWizardPage1.isPageComplete();
        } else {
            formIsPerformed = wsdlSchemaWizardPage2.isPageComplete();
        }

        if (formIsPerformed) {
            IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
            try {
                if (creation) {
                    String nextId = factory.getNextId();
                    connectionProperty.setId(nextId);
                    factory.create(connectionItem, wsdlSchemaWizardPage0.getDestinationPath());
                } else {
                    // update
                    RepositoryUpdateManager.updateMultiSchema(connectionItem, oldMetadataTable, oldTableMap);

                    factory.save(connectionItem);
                    closeLockStrategy();
                }
            } catch (PersistenceException e) {
                String detailError = e.toString();
                new ErrorDialogWidthDetailArea(getShell(), PID, Messages.getString("CommonWizard.persistenceException"), //$NON-NLS-1$
                        detailError);
                log.error(Messages.getString("CommonWizard.persistenceException") + "\n" + detailError); //$NON-NLS-1$ //$NON-NLS-2$
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.wizards.RepositoryWizard#getConnectionItem()
     */
    public ConnectionItem getConnectionItem() {
        return this.connectionItem;
    }

}
