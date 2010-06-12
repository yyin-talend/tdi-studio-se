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
package org.talend.repository.ui.wizards.metadata.connection.files.xml;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.commons.ui.swt.dialogs.ErrorDialogWidthDetailArea;
import org.talend.commons.utils.VersionUtils;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.metadata.MetadataTalendType;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.SchemaTarget;
import org.talend.core.model.metadata.builder.connection.XmlFileConnection;
import org.talend.core.model.metadata.builder.connection.XmlXPathLoopDescriptor;
import org.talend.core.model.metadata.types.JavaDataTypeHelper;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.core.model.metadata.types.PerlDataTypeHelper;
import org.talend.core.model.metadata.types.PerlTypesManager;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.XmlFileConnectionItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.update.RepositoryUpdateManager;
import org.talend.core.model.utils.TalendTextUtils;
import org.talend.core.prefs.ui.MetadataTypeLengthConstants;
import org.talend.core.ui.images.ECoreImage;
import org.talend.core.utils.CsvArray;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.repository.ProjectManager;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNodeUtilities;
import org.talend.repository.preview.ProcessDescription;
import org.talend.repository.ui.utils.ColumnNameValidator;
import org.talend.repository.ui.utils.ConnectionContextHelper;
import org.talend.repository.ui.utils.OtherConnectionContextUtils;
import org.talend.repository.ui.utils.ShadowProcessHelper;
import org.talend.repository.ui.wizards.CheckLastVersionRepositoryWizard;
import org.talend.repository.ui.wizards.PropertiesWizardPage;
import org.talend.repository.ui.wizards.metadata.connection.Step0WizardPage;

/**
 * FileWizard present the FileForm. Use to create a new connection to a DB.
 */

public class XmlFileWizard extends CheckLastVersionRepositoryWizard implements INewWizard {

    private static Logger log = Logger.getLogger(XmlFileWizard.class);

    private PropertiesWizardPage xmlFileWizardPage0;

    private XmlFileWizardPage xmlFileWizardPage1;

    private XmlFileWizardPage xmlFileWizardPage2;

    private XmlFileWizardPage xmlFileWizardPage3;

    private XmlFileConnection connection;

    private Property connectionProperty;

    private XmlFileConnectionItem connectionItem;

    private boolean isToolbar;

    private String originaleObjectLabel;

    private String originalVersion;

    private String originalPurpose;

    private String originalDescription;

    private String originalStatus;

    protected static final String DEFAULT_LABEL = "Column"; //$NON-NLS-1$

    /**
     * Sets the isToolbar.
     * 
     * @param isToolbar the isToolbar to set
     */
    public void setToolbar(boolean isToolbar) {
        this.isToolbar = isToolbar;
    }

    /**
     * Constructor for FileWizard.
     * 
     * @param workbench
     * @param selection
     * @param strings
     */
    @SuppressWarnings("unchecked")//$NON-NLS-1$
    public XmlFileWizard(IWorkbench workbench, boolean creation, ISelection selection, String[] existingNames) {
        super(workbench, creation);
        this.selection = selection;
        this.existingNames = existingNames;
        setNeedsProgressMonitor(true);
        // setDefaultPageImageDescriptor(ImageProvider.getImageDesc(EImage.METADATA_FILE_XML_WIZ));

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
            connection = ConnectionFactory.eINSTANCE.createXmlFileConnection();
            // MetadataSchema metadataSchema = ConnectionFactory.eINSTANCE.createMetadataSchema();
            MetadataTable metadataTable = ConnectionFactory.eINSTANCE.createMetadataTable();
            IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
            metadataTable.setId(factory.getNextId());
            // connection.getTables().add(metadataSchema);
            connection.getTables().add(metadataTable);
            connectionProperty = PropertiesFactory.eINSTANCE.createProperty();
            connectionProperty
                    .setAuthor(((RepositoryContext) CorePlugin.getContext().getProperty(Context.REPOSITORY_CONTEXT_KEY))
                            .getUser());
            connectionProperty.setVersion(VersionUtils.DEFAULT_VERSION);
            connectionProperty.setStatusCode(""); //$NON-NLS-1$

            connectionItem = PropertiesFactory.eINSTANCE.createXmlFileConnectionItem();
            connectionItem.setProperty(connectionProperty);
            connectionItem.setConnection(connection);
            break;

        case REPOSITORY_ELEMENT:
            connection = (XmlFileConnection) ((ConnectionItem) node.getObject().getProperty().getItem()).getConnection();
            connectionProperty = node.getObject().getProperty();
            connectionItem = (XmlFileConnectionItem) node.getObject().getProperty().getItem();
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
    }

    public XmlFileWizard(IWorkbench workbench, boolean creation, RepositoryNode node, String[] existingNames) {
        super(workbench, creation);
        this.selection = selection;
        this.existingNames = existingNames;
        setNeedsProgressMonitor(true);
        // setDefaultPageImageDescriptor(ImageProvider.getImageDesc(EImage.METADATA_FILE_XML_WIZ));
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
            connection = ConnectionFactory.eINSTANCE.createXmlFileConnection();
            // MetadataSchema metadataSchema = ConnectionFactory.eINSTANCE.createMetadataSchema();
            MetadataTable metadataTable = ConnectionFactory.eINSTANCE.createMetadataTable();
            IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
            metadataTable.setId(factory.getNextId());
            // connection.getTables().add(metadataSchema);
            connection.getTables().add(metadataTable);
            connectionProperty = PropertiesFactory.eINSTANCE.createProperty();
            connectionProperty
                    .setAuthor(((RepositoryContext) CorePlugin.getContext().getProperty(Context.REPOSITORY_CONTEXT_KEY))
                            .getUser());
            connectionProperty.setVersion(VersionUtils.DEFAULT_VERSION);
            connectionProperty.setStatusCode(""); //$NON-NLS-1$

            connectionItem = PropertiesFactory.eINSTANCE.createXmlFileConnectionItem();
            connectionItem.setProperty(connectionProperty);
            connectionItem.setConnection(connection);
            break;

        case REPOSITORY_ELEMENT:
            connection = (XmlFileConnection) ((ConnectionItem) node.getObject().getProperty().getItem()).getConnection();
            connectionProperty = node.getObject().getProperty();
            connectionItem = (XmlFileConnectionItem) node.getObject().getProperty().getItem();
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
    }

    /**
     * Adding the page to the wizard.
     */
    public void addPages() {
        if (isToolbar) {
            pathToSave = null;
        }
        xmlFileWizardPage0 = new Step0WizardPage(connectionProperty, pathToSave, ERepositoryObjectType.METADATA_FILE_XML,
                !isRepositoryObjectEditable(), creation);
        xmlFileWizardPage1 = new XmlFileWizardPage(1, connectionItem, isRepositoryObjectEditable(), existingNames);
        xmlFileWizardPage2 = new XmlFileWizardPage(2, connectionItem, isRepositoryObjectEditable(), existingNames);

        setDefaultPageImageDescriptor(ImageProvider.getImageDesc(ECoreImage.METADATA_FILE_XML_WIZ));

        if (creation) {
            setWindowTitle(Messages.getString("XmlFileWizard.windowTitleCreate")); //$NON-NLS-1$

            xmlFileWizardPage0
                    .setTitle(Messages.getString("FileWizardPage.titleCreate") + " 1 " + Messages.getString("FileWizardPage.of") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                            + " 4"); //$NON-NLS-1$
            xmlFileWizardPage0.setDescription(Messages.getString("FileWizardPage.descriptionCreateStep0")); //$NON-NLS-1$
            addPage(xmlFileWizardPage0);

            xmlFileWizardPage1
                    .setTitle(Messages.getString("FileWizardPage.titleCreate") + " 2 " + Messages.getString("FileWizardPage.of") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                            + " 4"); //$NON-NLS-1$
            xmlFileWizardPage1.setDescription(Messages.getString("FileWizardPage.descriptionCreateStep1")); //$NON-NLS-1$
            addPage(xmlFileWizardPage1);

            xmlFileWizardPage2
                    .setTitle(Messages.getString("FileWizardPage.titleCreate") + " 3 " + Messages.getString("FileWizardPage.of") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                            + " 4"); //$NON-NLS-1$
            xmlFileWizardPage2.setDescription(Messages.getString("FileWizardPage.descriptionCreateStep2")); //$NON-NLS-1$
            addPage(xmlFileWizardPage2);

            xmlFileWizardPage3 = new XmlFileWizardPage(3, connectionItem, isRepositoryObjectEditable(), null);
            xmlFileWizardPage3
                    .setTitle(Messages.getString("FileWizardPage.titleCreate") + " 4 " + Messages.getString("FileWizardPage.of") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                            + " 4"); //$NON-NLS-1$
            xmlFileWizardPage3.setDescription(Messages.getString("FileWizardPage.descriptionCreateStep3")); //$NON-NLS-1$
            addPage(xmlFileWizardPage3);

            xmlFileWizardPage1.setPageComplete(false);
            xmlFileWizardPage2.setPageComplete(false);
            xmlFileWizardPage3.setPageComplete(false);

        } else {
            setWindowTitle(Messages.getString("XmlFileWizard.windowTitleUpdate")); //$NON-NLS-1$

            xmlFileWizardPage0
                    .setTitle(Messages.getString("FileWizardPage.titleUpdate") + " 1 " + Messages.getString("FileWizardPage.of") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                            + " 3"); //$NON-NLS-1$
            xmlFileWizardPage0.setDescription(Messages.getString("FileWizardPage.descriptionUpdateStep0")); //$NON-NLS-1$
            addPage(xmlFileWizardPage0);

            xmlFileWizardPage1
                    .setTitle(Messages.getString("FileWizardPage.titleUpdate") + " 2 " + Messages.getString("FileWizardPage.of") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                            + " 3"); //$NON-NLS-1$
            xmlFileWizardPage1.setDescription(Messages.getString("FileWizardPage.descriptionUpdateStep1")); //$NON-NLS-1$
            addPage(xmlFileWizardPage1);

            xmlFileWizardPage2
                    .setTitle(Messages.getString("FileWizardPage.titleUpdate") + " 3 " + Messages.getString("FileWizardPage.of") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                            + " 3"); //$NON-NLS-1$
            xmlFileWizardPage2.setDescription(Messages.getString("FileWizardPage.descriptionUpdateStep2")); //$NON-NLS-1$
            addPage(xmlFileWizardPage2);

            xmlFileWizardPage1.setPageComplete(true);
            xmlFileWizardPage2.setPageComplete(isRepositoryObjectEditable());
        }
    }

    /**
     * This method determine if the 'Finish' button is enable This method is called when 'Finish' button is pressed in
     * the wizard. We will create an operation and run it using wizard as execution context.
     */
    public boolean performFinish() {

        boolean formIsPerformed;
        if (xmlFileWizardPage3 == null) {
            formIsPerformed = xmlFileWizardPage2.isPageComplete();
            if (formIsPerformed) {
                List schemas = connection.getSchema();
                List tables = connection.getTables();
                if (!schemas.isEmpty() && !tables.isEmpty()) {
                    XmlXPathLoopDescriptor currentSchema = (XmlXPathLoopDescriptor) schemas.get(0);
                    MetadataTable currentTable = (MetadataTable) tables.get(0);
                    if (currentSchema.getSchemaTargets().size() != currentTable.getColumns().size()) {
                        resetMetadata(currentSchema.getSchemaTargets());
                    }
                }
            }
        } else {
            formIsPerformed = xmlFileWizardPage3.isPageComplete();
        }

        if (formIsPerformed) {
            IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
            try {
                if (creation) {
                    String nextId = factory.getNextId();
                    connectionProperty.setId(nextId);
                    factory.create(connectionItem, xmlFileWizardPage0.getDestinationPath());
                } else {
                    // update
                    RepositoryUpdateManager.updateFileConnection(connectionItem);

                    factory.save(connectionItem);
                    closeLockStrategy();
                }
                ProxyRepositoryFactory.getInstance().saveProject(ProjectManager.getInstance().getCurrentProject());

            } catch (PersistenceException e) {
                String detailError = e.toString();
                new ErrorDialogWidthDetailArea(getShell(), PID,
                        Messages.getString("CommonWizard.persistenceException"), detailError); //$NON-NLS-1$
                log.error(Messages.getString("CommonWizard.persistenceException") + "\n" + detailError); //$NON-NLS-1$ //$NON-NLS-2$
                return false;
            }
            return true;
        } else {
            return false;
        }
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

    /**
     * We will accept the selection in the workbench to see if we can initialize from it.
     * 
     * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
     */
    public void init(final IWorkbench workbench, final IStructuredSelection selection2) {
        this.selection = selection2;
    }

    public ConnectionItem getConnectionItem() {
        return this.connectionItem;
    }

    private void resetMetadata(List<SchemaTarget> schemaTarget) {
        XmlFileConnection connection2 = OtherConnectionContextUtils.getOriginalValueConnection(connection, this.connectionItem,
                connection.isContextMode(), true);
        ProcessDescription processDescription = ShadowProcessHelper.getProcessDescription(connection2);
        CsvArray csvArray = null;
        try {
            csvArray = ShadowProcessHelper.getCsvArray(processDescription, "FILE_XML");//$NON-NLS-1$
        } catch (CoreException e) {
            ExceptionHandler.process(e);
        }

        List<MetadataColumn> newColumns = new ArrayList<MetadataColumn>();

        String file = ((XmlFileConnection) this.connectionItem.getConnection()).getXmlFilePath();
        if (connection.isContextMode()) {
            ContextType contextType = ConnectionContextHelper.getContextTypeForContextMode(connectionItem.getConnection(), true);
            file = TalendTextUtils.removeQuotes(ConnectionContextHelper.getOriginalValue(contextType, file));
        }

        if (file != null && file.endsWith(".xsd")) { //$NON-NLS-1$
            // prepareColumnsFromXSD(file, newColumns, schemaTarget);
            return;
        }

        if (csvArray == null || csvArray.getRows().isEmpty()) {
            return;
        } else {

            List<String[]> csvRows = csvArray.getRows();
            String[] fields = csvRows.get(0);
            int numberOfCol = fields.length;

            // define the label to the metadata width the content of the first row
            int firstRowToExtractMetadata = 0;

            // the first rows is used to define the label of any metadata
            String[] label = new String[numberOfCol];
            for (int i = 0; i < numberOfCol; i++) {
                label[i] = DEFAULT_LABEL + i; //$NON-NLS-1$

                if (firstRowToExtractMetadata == 0) {
                    if (schemaTarget.get(i).getTagName() != null && !schemaTarget.get(i).getTagName().equals("")) { //$NON-NLS-1$
                        label[i] = "" + schemaTarget.get(i).getTagName().trim().replaceAll(" ", "_"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                        label[i] = ColumnNameValidator.validateColumnNameFormat(label[i], i);
                    }
                }
            }

            for (int i = 0; i < numberOfCol; i++) {
                // define the first currentType and assimile it to globalType
                String globalType = null;
                int lengthValue = 0;
                int precisionValue = 0;

                int current = firstRowToExtractMetadata;
                while (globalType == null) {
                    if (LanguageManager.getCurrentLanguage() == ECodeLanguage.JAVA) {
                        if (i >= csvRows.get(current).length) {
                            globalType = "id_String"; //$NON-NLS-1$
                        } else {
                            globalType = JavaDataTypeHelper.getTalendTypeOfValue(csvRows.get(current)[i]);
                            current++;
                            // if (current == csvRows.size()) {
                            // globalType = "id_String"; //$NON-NLS-1$
                            // }
                        }
                    } else {
                        if (i >= csvRows.get(current).length) {
                            globalType = "String"; //$NON-NLS-1$
                        } else {
                            globalType = PerlDataTypeHelper.getTalendTypeOfValue(csvRows.get(current)[i]);
                            current++;
                            // if (current == csvRows.size()) {
                            // globalType = "String"; //$NON-NLS-1$
                            // }
                        }
                    }
                }
                // for another lines
                for (int f = firstRowToExtractMetadata; f < csvRows.size(); f++) {
                    fields = csvRows.get(f);
                    if (fields.length > i) {
                        String value = fields[i];
                        if (!value.equals("")) { //$NON-NLS-1$

                            if (LanguageManager.getCurrentLanguage() == ECodeLanguage.JAVA) {
                                if (!JavaDataTypeHelper.getTalendTypeOfValue(value).equals(globalType)) {
                                    globalType = JavaDataTypeHelper.getCommonType(globalType, JavaDataTypeHelper
                                            .getTalendTypeOfValue(value));
                                }
                            } else {
                                if (!PerlDataTypeHelper.getTalendTypeOfValue(value).equals(globalType)) {
                                    globalType = PerlDataTypeHelper.getCommonType(globalType, PerlDataTypeHelper
                                            .getTalendTypeOfValue(value));
                                }
                            }
                            if (lengthValue < value.length()) {
                                lengthValue = value.length();
                            }
                            int positionDecimal = 0;
                            if (value.indexOf(',') > -1) {
                                positionDecimal = value.lastIndexOf(',');
                                precisionValue = lengthValue - positionDecimal;
                            } else if (value.indexOf('.') > -1) {
                                positionDecimal = value.lastIndexOf('.');
                                precisionValue = lengthValue - positionDecimal;
                            }
                        } else {
                            if (LanguageManager.getCurrentLanguage() == ECodeLanguage.JAVA) {
                                if (CorePlugin.getDefault().getPreferenceStore().getString(
                                        MetadataTypeLengthConstants.VALUE_DEFAULT_TYPE) != null
                                        && !CorePlugin.getDefault().getPreferenceStore().getString(
                                                MetadataTypeLengthConstants.VALUE_DEFAULT_TYPE).equals("")) { //$NON-NLS-1$
                                    globalType = CorePlugin.getDefault().getPreferenceStore().getString(
                                            MetadataTypeLengthConstants.VALUE_DEFAULT_TYPE);
                                    if (CorePlugin.getDefault().getPreferenceStore().getString(
                                            MetadataTypeLengthConstants.VALUE_DEFAULT_LENGTH) != null
                                            && !CorePlugin.getDefault().getPreferenceStore().getString(
                                                    MetadataTypeLengthConstants.VALUE_DEFAULT_LENGTH).equals("")) { //$NON-NLS-1$
                                        lengthValue = Integer.parseInt(CorePlugin.getDefault().getPreferenceStore().getString(
                                                MetadataTypeLengthConstants.VALUE_DEFAULT_LENGTH));
                                    }
                                }
                            } else {
                                if (CorePlugin.getDefault().getPreferenceStore().getString(
                                        MetadataTypeLengthConstants.PERL_VALUE_DEFAULT_TYPE) != null
                                        && !CorePlugin.getDefault().getPreferenceStore().getString(
                                                MetadataTypeLengthConstants.PERL_VALUE_DEFAULT_TYPE).equals("")) { //$NON-NLS-1$
                                    globalType = CorePlugin.getDefault().getPreferenceStore().getString(
                                            MetadataTypeLengthConstants.PERL_VALUE_DEFAULT_TYPE);
                                    if (CorePlugin.getDefault().getPreferenceStore().getString(
                                            MetadataTypeLengthConstants.PERL_VALUE_DEFAULT_LENGTH) != null
                                            && !CorePlugin.getDefault().getPreferenceStore().getString(
                                                    MetadataTypeLengthConstants.PERL_VALUE_DEFAULT_LENGTH).equals("")) { //$NON-NLS-1$
                                        lengthValue = Integer.parseInt(CorePlugin.getDefault().getPreferenceStore().getString(
                                                MetadataTypeLengthConstants.PERL_VALUE_DEFAULT_LENGTH));
                                    }
                                }
                            }

                        }
                    }
                }

                // define the metadataColumn to field i
                MetadataColumn metadataColumn = ConnectionFactory.eINSTANCE.createMetadataColumn();
                // hshen bug7249
                metadataColumn.setPattern("\"dd-MM-yyyy\""); //$NON-NLS-1$
                // Convert javaType to TalendType
                String talendType = null;
                if (LanguageManager.getCurrentLanguage() == ECodeLanguage.JAVA) {
                    talendType = globalType;
                    if (globalType.equals(JavaTypesManager.FLOAT.getId()) || globalType.equals(JavaTypesManager.DOUBLE.getId())) {
                        metadataColumn.setPrecision(precisionValue);
                    } else {
                        metadataColumn.setPrecision(null);
                    }
                } else {
                    talendType = PerlTypesManager.getNewTypeName(MetadataTalendType.loadTalendType(globalType,
                            "TALENDDEFAULT", false)); //$NON-NLS-1$
                    if (globalType.equals("FLOAT") || globalType.equals("DOUBLE")) { //$NON-NLS-1$ //$NON-NLS-2$
                        metadataColumn.setPrecision(precisionValue);
                    } else {
                        metadataColumn.setPrecision(null);
                    }
                }
                metadataColumn.setTalendType(talendType);
                metadataColumn.setLength(lengthValue);
                metadataColumn.setLabel(label[i]);

                newColumns.add(i, metadataColumn);
            }
        }
        EList columns = ((MetadataTable) connection.getTables().get(0)).getColumns();
        columns.clear();
        columns.addAll(newColumns);
    }

}
