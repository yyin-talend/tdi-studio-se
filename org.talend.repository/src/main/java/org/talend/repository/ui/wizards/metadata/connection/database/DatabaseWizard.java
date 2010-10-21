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
package org.talend.repository.ui.wizards.metadata.connection.database;

import java.util.ArrayList;
import java.util.List;

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
import org.talend.core.database.EDatabase4DriverClassName;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.builder.ConvertionHelper;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.database.EDatabaseSchemaOrCatalogMapping;
import org.talend.core.model.metadata.builder.database.ExtractMetaDataUtils;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.update.RepositoryUpdateManager;
import org.talend.core.ui.images.ECoreImage;
import org.talend.cwm.helper.CatalogHelper;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.SchemaHelper;
import org.talend.repository.ProjectManager;
import org.talend.repository.RepositoryPlugin;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNodeUtilities;
import org.talend.repository.ui.utils.ConnectionContextHelper;
import org.talend.repository.ui.wizards.CheckLastVersionRepositoryWizard;
import org.talend.repository.ui.wizards.PropertiesWizardPage;
import org.talend.repository.ui.wizards.metadata.connection.Step0WizardPage;
import orgomg.cwm.resource.relational.Catalog;
import orgomg.cwm.resource.relational.Schema;

/**
 * DatabaseWizard present the DatabaseForm. Use to manage the metadata connection.
 */
public class DatabaseWizard extends CheckLastVersionRepositoryWizard implements INewWizard {

    private static Logger log = Logger.getLogger(DatabaseWizard.class);

    private PropertiesWizardPage propertiesWizardPage;

    private DatabaseWizardPage databaseWizardPage;

    private DatabaseConnection connection;

    private Property connectionProperty;

    private ConnectionItem connectionItem;

    private String originaleObjectLabel;

    private String originalVersion;

    private String originalPurpose;

    private String originalDescription;

    private String originalStatus;

    private boolean isToolBar;

    /**
     * Constructor for DatabaseWizard. Analyse Iselection to extract DatabaseConnection and the pathToSave. Start the
     * Lock Strategy.
     * 
     * @param selection
     * @param existingNames
     */
    public DatabaseWizard(IWorkbench workbench, boolean creation, ISelection selection, String[] existingNames) {
        super(workbench, creation);
        this.selection = selection;
        this.existingNames = existingNames;
        setNeedsProgressMonitor(true);
        RepositoryNode node = null;
        Object obj = ((IStructuredSelection) selection).getFirstElement();
        if (obj instanceof RepositoryNode) {
            node = (RepositoryNode) obj;
        } else {
            return;
        }

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
            connection = ConnectionFactory.eINSTANCE.createDatabaseConnection();
            connectionProperty = PropertiesFactory.eINSTANCE.createProperty();
            connectionProperty
                    .setAuthor(((RepositoryContext) CorePlugin.getContext().getProperty(Context.REPOSITORY_CONTEXT_KEY))
                            .getUser());
            connectionProperty.setVersion(VersionUtils.DEFAULT_VERSION);
            connectionProperty.setStatusCode(""); //$NON-NLS-1$

            connectionItem = PropertiesFactory.eINSTANCE.createDatabaseConnectionItem();
            connectionItem.setProperty(connectionProperty);
            connectionItem.setConnection(connection);
            break;

        case REPOSITORY_ELEMENT:
            connection = (DatabaseConnection) ((ConnectionItem) node.getObject().getProperty().getItem()).getConnection();
            connectionProperty = node.getObject().getProperty();
            connectionItem = (ConnectionItem) node.getObject().getProperty().getItem();
            // set the repositoryObject, lock and set isRepositoryObjectEditable
            setRepositoryObject(node.getObject());
            isRepositoryObjectEditable();
            initLockStrategy();
            break;
        }
        if (!creation) {
            this.originaleObjectLabel = this.connectionItem.getProperty().getLabel();
            this.originalVersion = this.connectionItem.getProperty().getVersion();
            this.originalDescription = this.connectionItem.getProperty().getDescription();
            this.originalPurpose = this.connectionItem.getProperty().getPurpose();
            this.originalStatus = this.connectionItem.getProperty().getStatusCode();
        }
        // initialize the context mode
        ConnectionContextHelper.checkContextMode(connectionItem);
    }

    /**
     * Constructor for DatabaseWizard. Analyse Iselection to extract DatabaseConnection and the pathToSave. Start the
     * Lock Strategy.
     * 
     * @param selection
     * @param existingNames
     */
    public DatabaseWizard(IWorkbench workbench, boolean creation, RepositoryNode node, String[] existingNames) {
        super(workbench, creation);
        this.existingNames = existingNames;
        setNeedsProgressMonitor(true);
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
            connection = ConnectionFactory.eINSTANCE.createDatabaseConnection();
            connectionProperty = PropertiesFactory.eINSTANCE.createProperty();
            connectionProperty
                    .setAuthor(((RepositoryContext) CorePlugin.getContext().getProperty(Context.REPOSITORY_CONTEXT_KEY))
                            .getUser());
            connectionProperty.setVersion(VersionUtils.DEFAULT_VERSION);
            connectionProperty.setStatusCode(""); //$NON-NLS-1$

            connectionItem = PropertiesFactory.eINSTANCE.createDatabaseConnectionItem();
            connectionItem.setProperty(connectionProperty);
            connectionItem.setConnection(connection);
            break;

        case REPOSITORY_ELEMENT:
            connection = (DatabaseConnection) ((ConnectionItem) node.getObject().getProperty().getItem()).getConnection();
            connectionProperty = node.getObject().getProperty();
            connectionItem = (ConnectionItem) node.getObject().getProperty().getItem();
            // set the repositoryObject, lock and set isRepositoryObjectEditable
            setRepositoryObject(node.getObject());
            isRepositoryObjectEditable();
            initLockStrategy();
            break;
        }
        if (!creation) {
            this.originaleObjectLabel = this.connectionItem.getProperty().getLabel();
            this.originalVersion = this.connectionItem.getProperty().getVersion();
            this.originalDescription = this.connectionItem.getProperty().getDescription();
            this.originalPurpose = this.connectionItem.getProperty().getPurpose();
            this.originalStatus = this.connectionItem.getProperty().getStatusCode();
        }
        // initialize the context mode
        ConnectionContextHelper.checkContextMode(connectionItem);
    }

    /**
     * yzhang Comment method "setToolBar".
     * 
     * @param isToolbar
     */
    public void setToolBar(boolean isToolbar) {
        this.isToolBar = isToolbar;
    }

    /**
     * Adding the page to the wizard and set Title, Description and PageComplete.
     */
    @Override
    public void addPages() {
        setWindowTitle(Messages.getString("DatabaseWizard.windowTitle")); //$NON-NLS-1$
        setDefaultPageImageDescriptor(ImageProvider.getImageDesc(ECoreImage.METADATA_CONNECTION_WIZ));
        if (isToolBar) {
            pathToSave = null;
        }
        propertiesWizardPage = new Step0WizardPage(connectionProperty, pathToSave, ERepositoryObjectType.METADATA_CONNECTIONS,
                !isRepositoryObjectEditable(), creation);
        databaseWizardPage = new DatabaseWizardPage(connectionItem, isRepositoryObjectEditable(), existingNames);

        if (creation) {
            propertiesWizardPage.setTitle(Messages.getString("DatabaseWizardPage.titleCreate.Step1")); //$NON-NLS-1$
            propertiesWizardPage.setDescription(Messages.getString("DatabaseWizardPage.descriptionCreate.Step1")); //$NON-NLS-1$
            propertiesWizardPage.setPageComplete(false);

            databaseWizardPage.setTitle(Messages.getString("DatabaseWizardPage.titleCreate.Step2")); //$NON-NLS-1$
            databaseWizardPage.setDescription(Messages.getString("DatabaseWizardPage.descriptionCreate.Step2")); //$NON-NLS-1$
            databaseWizardPage.setPageComplete(false);
        } else {
            propertiesWizardPage.setTitle(Messages.getString("DatabaseWizardPage.titleUpdate.Step1")); //$NON-NLS-1$
            propertiesWizardPage.setDescription(Messages.getString("DatabaseWizardPage.descriptionUpdate.Step1")); //$NON-NLS-1$
            propertiesWizardPage.setPageComplete(isRepositoryObjectEditable());

            databaseWizardPage.setTitle(Messages.getString("DatabaseWizardPage.titleUpdate.Step2")); //$NON-NLS-1$
            databaseWizardPage.setDescription(Messages.getString("DatabaseWizardPage.descriptionUpdate.Step2")); //$NON-NLS-1$
            databaseWizardPage.setPageComplete(isRepositoryObjectEditable());
        }
        addPage(propertiesWizardPage);
        addPage(databaseWizardPage);
    }

    /**
     * This method is called when 'Finish' button is pressed in the wizard. Save metadata close Lock Strategy and close
     * wizard.
     */
    @Override
    public boolean performFinish() {
        if (databaseWizardPage.isPageComplete()) {
            /*
             * if create connection in TOS with context model,should use the original value when create catalog or //
             * schema,see bug 0016636,using metadataConnection can be sure that all the values has been parse to
             * original. hywang
             */
            IMetadataConnection metadataConnection = ConvertionHelper.convert(connection);
            IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
            try {
                if (creation) {
                    String nextId = factory.getNextId();
                    connectionProperty.setId(nextId);
                    if (connectionItem.getConnection() instanceof DatabaseConnection) {
                        DatabaseConnection c = (DatabaseConnection) connectionItem.getConnection();
                        final boolean equals = c.getProductId().equals(EDatabaseTypeName.ORACLEFORSID.getProduct());
                        if (equals && !c.isContextMode()) {
                            if (c.getUiSchema() == null) {
                                c.setUiSchema(""); //$NON-NLS-1$
                            } else {
                                c.setUiSchema(c.getUiSchema().toUpperCase());
                            }
                        }
                    }
                    EDatabaseTypeName type = EDatabaseTypeName.getTypeFromDbType(metadataConnection.getDbType());
                    if (!type.equals(EDatabaseTypeName.GENERAL_JDBC)) {
                        this.connection.setDriverClass(EDatabase4DriverClassName.getDriverClassByDbType(metadataConnection
                                .getDbType()));
                    }
                    this.connection.setName(connectionProperty.getLabel());
                    addCatalogOrSchema(metadataConnection);
                    factory.create(connectionItem, propertiesWizardPage.getDestinationPath());
                } else {
                    if (connectionItem.getConnection() instanceof DatabaseConnection) {
                        DatabaseConnection c = (DatabaseConnection) connectionItem.getConnection();
                        final boolean equals = c.getProductId().equals(EDatabaseTypeName.ORACLEFORSID.getProduct());
                        if (equals && !c.isContextMode()) {
                            if (c.getUiSchema() == null || "".equals(c.getUiSchema())) {
                                c.setUiSchema(c.getUsername()); //$NON-NLS-1$
                            } else {
                                c.setUiSchema(c.getUiSchema().toUpperCase());
                            }
                        }
                        if (!datapackageExsit(metadataConnection)) {
                            addCatalogOrSchema(metadataConnection);
                        }
                        // update
                        RepositoryUpdateManager.updateDBConnection(connectionItem);
                    }
                    factory.save(connectionItem);
                    // 0005170: Schema renamed - new name not pushed out to dependant jobs
                    boolean isModified = propertiesWizardPage.isNameModifiedByUser();
                    if (isModified) {
                        CorePlugin.getDefault().getDesignerCoreService().refreshComponentView(connectionItem);
                    }
                    ProxyRepositoryFactory.getInstance().saveProject(ProjectManager.getInstance().getCurrentProject());
                    closeLockStrategy();

                }
            } catch (PersistenceException e) {
                String detailError = e.toString();
                new ErrorDialogWidthDetailArea(getShell(), PID, Messages.getString("CommonWizard.persistenceException"), //$NON-NLS-1$
                        detailError);
                log.error(Messages.getString("CommonWizard.persistenceException") + "\n" + detailError); //$NON-NLS-1$ //$NON-NLS-2$
                return false;
            }
            List<IRepositoryViewObject> list = new ArrayList<IRepositoryViewObject>();
            list.add(repositoryObject);
            RepositoryPlugin.getDefault().getRepositoryService().notifySQLBuilder(list);

            return true;
        } else {
            return false;
        }
    }

    /**
     * DOC hywang Comment method "datapackageExsit".
     * 
     * @return
     */
    private boolean datapackageExsit(IMetadataConnection metadataConnection) {
        EDatabaseSchemaOrCatalogMapping catalog = null;
        EDatabaseSchemaOrCatalogMapping schema = null;
        EDatabaseTypeName type = EDatabaseTypeName.getTypeFromDbType(metadataConnection.getDbType());
        if (type.equals(EDatabaseTypeName.GENERAL_JDBC)) {
            String realtype = ExtractMetaDataUtils.getDbTypeByClassName(metadataConnection.getDriverClass());
            type = EDatabaseTypeName.getTypeFromDbType(realtype);
            catalog = type.getCatalogMappingField();
            schema = type.getSchemaMappingField();
        } else {
            catalog = type.getCatalogMappingField();
            schema = type.getSchemaMappingField();
        }

        Schema s = null;
        Catalog c = null;
        List<Schema> schemas = new ArrayList<Schema>();
        String user = metadataConnection.getUsername();
        String defaultname = this.connection.getName();
        String dbsid = metadataConnection.getDatabase();
        String dbuischema = metadataConnection.getSchema();
        if (schema != null && catalog != null) {
            if (schema.equals(EDatabaseSchemaOrCatalogMapping.None) && !catalog.equals(EDatabaseSchemaOrCatalogMapping.None)) {// only
                // catalog
                if (catalog.equals(EDatabaseSchemaOrCatalogMapping.Sid)) {
                    if (!findCatalog(dbsid)) {
                        return false;
                    }
                    return true;
                }

            } else if (!schema.equals(EDatabaseSchemaOrCatalogMapping.None) // only schema
                    && catalog.equals(EDatabaseSchemaOrCatalogMapping.None)) {
                if (schema.equals(EDatabaseSchemaOrCatalogMapping.Schema)) {
                    if (!findSchema(dbuischema)) {
                        return false;
                    }
                    return true;
                }
                if (schema.equals(EDatabaseSchemaOrCatalogMapping.Login)) {
                    if (!findSchema(user)) {
                        return false;
                    }
                    return true;
                }
                if (schema.equals(EDatabaseSchemaOrCatalogMapping.Default_Name)) { // for databases like access
                    if (!findSchema(defaultname)) {
                        return false;
                    }
                    return true;
                }
            } else { // both schema and catalog
                String cvalue = dbsid;
                String svalue = null;
                cvalue = dbsid;
                switch (schema) {
                case Sid:
                    svalue = dbsid;
                    break;
                case Schema:
                    svalue = dbuischema;
                    break;
                case Login:
                    svalue = user;
                    break;
                }
                if (!findSchemaInCatalog(cvalue, svalue)) {
                    return false;
                }
                return true;
            }
        }

        return false;
    }

    /**
     * DOC hywang Comment method "findSchemaInCatalog".
     * 
     * @return
     */
    private boolean findSchemaInCatalog(String catalog, String schema) {
        Catalog findCatalog = null;
        for (Catalog c : ConnectionHelper.getCatalogs(this.connection)) {
            if (c.getName().equals(catalog)) {
                findCatalog = c;
                break;
            }
        }
        if (findCatalog != null) {
            for (Schema s : CatalogHelper.getSchemas(findCatalog)) {
                if (s.getName().equals(catalog)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean findSchema(String dbuischema) {
        for (Schema s : ConnectionHelper.getSchema(this.connection)) {
            if (s.getName().equals(dbuischema))
                return true;
        }
        return false;
    }

    private boolean findCatalog(String dbsid) {
        for (Catalog c : ConnectionHelper.getCatalogs(this.connection)) {
            if (c.getName().equals(dbsid))
                return true;
        }
        return false;
    }

    @Override
    public boolean performCancel() {
        if (!creation) {
            connectionItem.getProperty().setVersion(this.originalVersion);
            connectionItem.getProperty().setLabel(this.originaleObjectLabel);
            connectionItem.getProperty().setDescription(this.originalDescription);
            connectionItem.getProperty().setPurpose(this.originalPurpose);
            connectionItem.getProperty().setStatusCode(this.originalStatus);
        }
        return super.performCancel();
    }

    private void addCatalogOrSchema(IMetadataConnection metadataConnection) {
        EDatabaseSchemaOrCatalogMapping catalog = null;
        EDatabaseSchemaOrCatalogMapping schema = null;
        EDatabaseTypeName type = EDatabaseTypeName.getTypeFromDbType(metadataConnection.getDbType());
        if (type.equals(EDatabaseTypeName.GENERAL_JDBC)) {
            String realtype = ExtractMetaDataUtils.getDbTypeByClassName(metadataConnection.getDriverClass());
            type = EDatabaseTypeName.getTypeFromDbType(realtype);
            catalog = type.getCatalogMappingField();
            schema = type.getSchemaMappingField();
        } else {
            catalog = type.getCatalogMappingField();
            schema = type.getSchemaMappingField();
        }
        fillValuesForSchemaOrCatalog(catalog, schema, metadataConnection);
    }

    private void fillValuesForSchemaOrCatalog(EDatabaseSchemaOrCatalogMapping catalog, EDatabaseSchemaOrCatalogMapping schema,
            IMetadataConnection metadataConnection) {
        Schema s = null;
        Catalog c = null;
        List<Schema> schemas = new ArrayList<Schema>();
        String user = metadataConnection.getUsername();
        String defaultname = this.connection.getName();
        String dbsid = metadataConnection.getDatabase();
        String dbuischema = metadataConnection.getSchema();
        if (schema != null && catalog != null) {
            if (schema.equals(EDatabaseSchemaOrCatalogMapping.None) && !catalog.equals(EDatabaseSchemaOrCatalogMapping.None)) {// only
                // catalog
                if (catalog.equals(EDatabaseSchemaOrCatalogMapping.Sid)) {
                    c = CatalogHelper.createCatalog(dbsid);
                    c.getDataManager().add(this.connection);
                    ConnectionHelper.addCatalog(c, this.connection);
                }

            } else if (!schema.equals(EDatabaseSchemaOrCatalogMapping.None) // only schema
                    && catalog.equals(EDatabaseSchemaOrCatalogMapping.None)) {
                if (schema.equals(EDatabaseSchemaOrCatalogMapping.Schema)) {
                    s = SchemaHelper.createSchema(dbuischema);
                    s.getDataManager().add(this.connection);
                    ConnectionHelper.addSchema(s, this.connection);
                }
                if (schema.equals(EDatabaseSchemaOrCatalogMapping.Login)) {
                    s = SchemaHelper.createSchema(user);
                    s.getDataManager().add(this.connection);
                    ConnectionHelper.addSchema(s, this.connection);
                }
                if (schema.equals(EDatabaseSchemaOrCatalogMapping.Default_Name)) { // for databases like access
                    s = SchemaHelper.createSchema(defaultname);
                    s.getDataManager().add(this.connection);
                    ConnectionHelper.addSchema(s, this.connection);
                }
            } else { // both schema and catalog
                String cvalue = dbsid;
                String svalue = null;
                cvalue = dbsid;
                switch (schema) {
                case Sid:
                    svalue = dbsid;
                    break;
                case Schema:
                    svalue = dbuischema;
                    break;
                case Login:
                    svalue = user;
                    break;
                }
                c = CatalogHelper.createCatalog(cvalue);
                s = SchemaHelper.createSchema(svalue);
                schemas.add(s);
                CatalogHelper.addSchemas(schemas, c);
                c.getDataManager().add(this.connection);
                ConnectionHelper.addCatalog(c, this.connection);
            }
        }

    }

    /**
     * We will accept the selection in the workbench to see if we can initialize from it.
     * 
     * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
     */
    public void init(final IWorkbench workbench, final IStructuredSelection selection2) {
        super.setWorkbench(workbench);
        this.selection = selection2;
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
