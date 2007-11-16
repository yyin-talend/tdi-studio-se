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
package org.talend.repository.ui.wizards.metadata.connection.files.xml;

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
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.XmlFileConnection;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.ui.images.ECoreImage;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNodeUtilities;
import org.talend.repository.ui.wizards.PropertiesWizardPage;
import org.talend.repository.ui.wizards.RepositoryWizard;
import org.talend.repository.ui.wizards.metadata.connection.Step0WizardPage;

/**
 * FileWizard present the FileForm. Use to create a new connection to a DB.
 */

public class XmlFileWizard extends RepositoryWizard implements INewWizard {

    private static Logger log = Logger.getLogger(XmlFileWizard.class);

    private PropertiesWizardPage xmlFileWizardPage0;

    private XmlFileWizardPage xmlFileWizardPage1;

    private XmlFileWizardPage xmlFileWizardPage2;

    private XmlFileWizardPage xmlFileWizardPage3;

    private XmlFileConnection connection;

    private Property connectionProperty;

    private ConnectionItem connectionItem;

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
            connectionItem = (ConnectionItem) node.getObject().getProperty().getItem();
            // set the repositoryObject, lock and set isRepositoryObjectEditable
            setRepositoryObject(node.getObject());
            isRepositoryObjectEditable();
            initLockStrategy();
            break;
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
            connectionItem = (ConnectionItem) node.getObject().getProperty().getItem();
            // set the repositoryObject, lock and set isRepositoryObjectEditable
            setRepositoryObject(node.getObject());
            isRepositoryObjectEditable();
            initLockStrategy();
            break;
        }
    }

    /**
     * Adding the page to the wizard.
     */
    public void addPages() {
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
                    factory.save(connectionItem);
                    closeLockStrategy();
                }
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

    /**
     * We will accept the selection in the workbench to see if we can initialize from it.
     * 
     * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
     */
    public void init(final IWorkbench workbench, final IStructuredSelection selection2) {
        this.selection = selection2;
    }
}
