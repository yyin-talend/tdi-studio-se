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
package org.talend.repository.ui.wizards.metadata.connection.genericshema;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IPath;
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
import org.talend.core.model.metadata.builder.connection.GenericSchemaConnection;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
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
import org.talend.repository.ui.wizards.metadata.connection.files.delimited.DelimitedFileWizard;

/**
 * DOC Administrator class global comment. Detailled comment <br/>
 * 
 */
public class GenericSchemaWizard extends RepositoryWizard implements INewWizard {

    private static Logger log = Logger.getLogger(GenericSchemaWizard.class);

    private PropertiesWizardPage genericSchemaWizardPage0;

    // private DelimitedFileWizardPage delimitedFileWizardPage1;
    //
    // private DelimitedFileWizardPage delimitedFileWizardPage2;

    private GenericSchemaWizardPage genericSchemaWizardPage1;

    private GenericSchemaConnection connection;

    private Property connectionProperty;

    private ConnectionItem connectionItem;

    private boolean isSinglePageOnly;

    /**
     * Constructor for FileWizard.
     * 
     * @param workbench
     * @param selection
     * @param strings
     */
    @SuppressWarnings("unchecked")//$NON-NLS-1$
    public GenericSchemaWizard(IWorkbench workbench, boolean creation, ISelection selection, String[] existingNames,
            boolean isSinglePageOnly) {
        super(workbench, creation);
        this.selection = selection;
        this.existingNames = existingNames;
        this.isSinglePageOnly = isSinglePageOnly;
        setNeedsProgressMonitor(true);
        setDefaultPageImageDescriptor(ImageProvider.getImageDesc(ECoreImage.METADATA_FILE_DELIMITED_WIZ));

        if (selection == null || existingNames == null) {
            connectionProperty = PropertiesFactory.eINSTANCE.createProperty();
            connectionProperty.setAuthor(((RepositoryContext) CorePlugin.getContext().getProperty(
                    Context.REPOSITORY_CONTEXT_KEY)).getUser());
            connectionProperty.setVersion(VersionUtils.DEFAULT_VERSION);
            connectionProperty.setStatusCode(""); //$NON-NLS-1$

            connectionItem = PropertiesFactory.eINSTANCE.createGenericSchemaConnectionItem();
            connectionItem.setProperty(connectionProperty);
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
            connection = ConnectionFactory.eINSTANCE.createGenericSchemaConnection();
            MetadataTable metadataTable = ConnectionFactory.eINSTANCE.createMetadataTable();
            IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
            metadataTable.setId(factory.getNextId());
            connection.getTables().add(metadataTable);
            connectionProperty = PropertiesFactory.eINSTANCE.createProperty();
            connectionProperty.setAuthor(((RepositoryContext) CorePlugin.getContext().getProperty(
                    Context.REPOSITORY_CONTEXT_KEY)).getUser());
            connectionProperty.setVersion(VersionUtils.DEFAULT_VERSION);
            connectionProperty.setStatusCode(""); //$NON-NLS-1$

            connectionItem = PropertiesFactory.eINSTANCE.createGenericSchemaConnectionItem();
            connectionItem.setProperty(connectionProperty);
            connectionItem.setConnection(connection);
            break;

        case REPOSITORY_ELEMENT:
            connection = (GenericSchemaConnection) ((ConnectionItem) node.getObject().getProperty().getItem())
                    .getConnection();
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

        genericSchemaWizardPage0 = null;
        genericSchemaWizardPage1 = null;
        // delimitedFileWizardPage1 = new DelimitedFileWizardPage(1, connectionItem, isRepositoryObjectEditable(),
        // existingNames);
        // delimitedFileWizardPage2 = new DelimitedFileWizardPage(2, connectionItem, isRepositoryObjectEditable(),
        // existingNames);

        if (creation) {
            setWindowTitle("Create new generic schema");// Messages.getString("DelimitedFileWizard.windowTitleCreate"));
            // //$NON-NLS-1$
            genericSchemaWizardPage0 = new Step0WizardPage(connectionProperty, pathToSave,
                    ERepositoryObjectType.METADATA_GENERIC_SCHEMA, !isRepositoryObjectEditable(), creation);
            genericSchemaWizardPage0.setTitle(Messages.getString("FileWizardPage.titleCreate") + " 1 " //$NON-NLS-1$ //$NON-NLS-2$
                    + Messages.getString("FileWizardPage.of") + " 2"); //$NON-NLS-1$ //$NON-NLS-2$
            genericSchemaWizardPage0.setDescription(Messages.getString("FileWizardPage.descriptionCreateStep0")); //$NON-NLS-1$
            addPage(genericSchemaWizardPage0);

            // delimitedFileWizardPage1.setTitle(Messages.getString("FileWizardPage.titleCreate") + " 2 " //$NON-NLS-1$
            // //$NON-NLS-2$
            // + Messages.getString("FileWizardPage.of") + " 4"); //$NON-NLS-1$ //$NON-NLS-2$
            // delimitedFileWizardPage1.setDescription(Messages.getString("FileWizardPage.descriptionCreateStep1"));
            // //$NON-NLS-1$
            // addPage(delimitedFileWizardPage1);
            //
            // delimitedFileWizardPage2.setTitle(Messages.getString("FileWizardPage.titleCreate") + " 3 " //$NON-NLS-1$
            // //$NON-NLS-2$
            // + Messages.getString("FileWizardPage.of") + " 4"); //$NON-NLS-1$ //$NON-NLS-2$
            // delimitedFileWizardPage2.setDescription(Messages.getString("FileWizardPage.descriptionCreateStep2"));
            // //$NON-NLS-1$
            // addPage(delimitedFileWizardPage2);
            genericSchemaWizardPage1 = new GenericSchemaWizardPage(2, connectionItem, isRepositoryObjectEditable(),
                    null);
            genericSchemaWizardPage1.setTitle("Create new generic schema" // Messages.getString("FileWizardPage.titleCreate")
                    // + " 2 " //$NON-NLS-1$ //$NON-NLS-2$
                    + Messages.getString("FileWizardPage.of") + " 2"); //$NON-NLS-1$ //$NON-NLS-2$
            genericSchemaWizardPage1.setDescription(Messages.getString("FileWizardPage.descriptionCreateStep3")); //$NON-NLS-1$
            addPage(genericSchemaWizardPage1);

            // delimitedFileWizardPage0.setPageComplete(false);
            // delimitedFileWizardPage1.setPageComplete(false);
            // delimitedFileWizardPage2.setPageComplete(false);
            genericSchemaWizardPage1.setPageComplete(false);

        } else if (this.isSinglePageOnly == false) {

            setWindowTitle("Update generic schema");// Messages.getString("DelimitedFileWizard.windowTitleUpdate"));
            // //$NON-NLS-1$
            
            genericSchemaWizardPage0 = new Step0WizardPage(connectionProperty, pathToSave,
                    ERepositoryObjectType.METADATA_GENERIC_SCHEMA, !isRepositoryObjectEditable(), creation);

            genericSchemaWizardPage0.setTitle(Messages.getString("FileWizardPage.titleCreate") + " 1 " //$NON-NLS-1$ //$NON-NLS-2$
                    + Messages.getString("FileWizardPage.of") + " 2"); //$NON-NLS-1$ //$NON-NLS-2$
            genericSchemaWizardPage0.setDescription(Messages.getString("FileWizardPage.descriptionCreateStep0")); //$NON-NLS-1$
            addPage(genericSchemaWizardPage0);
            genericSchemaWizardPage1 = new GenericSchemaWizardPage(2, connectionItem, isRepositoryObjectEditable(),
                    null);
            genericSchemaWizardPage1.setTitle(Messages.getString("FileWizardPage.titleUpdate") + " 2 " //$NON-NLS-1$ //$NON-NLS-2$
                    + Messages.getString("FileWizardPage.of") + " 2"); //$NON-NLS-1$ //$NON-NLS-2$
            genericSchemaWizardPage1.setDescription(Messages.getString("FileWizardPage.descriptionUpdateStep0")); //$NON-NLS-1$
            addPage(genericSchemaWizardPage1);
            genericSchemaWizardPage1.setPageComplete(true);

            // delimitedFileWizardPage1.setTitle(Messages.getString("FileWizardPage.titleUpdate") + " 2 " //$NON-NLS-1$
            // //$NON-NLS-2$
            // + Messages.getString("FileWizardPage.of") + " 3"); //$NON-NLS-1$ //$NON-NLS-2$
            // delimitedFileWizardPage1.setDescription(Messages.getString("FileWizardPage.descriptionUpdateStep1"));
            // //$NON-NLS-1$
            // addPage(delimitedFileWizardPage1);
            //
            // delimitedFileWizardPage2.setTitle(Messages.getString("FileWizardPage.titleUpdate") + " 3 " //$NON-NLS-1$
            // //$NON-NLS-2$
            // + Messages.getString("FileWizardPage.of") + " 3"); //$NON-NLS-1$ //$NON-NLS-2$
            // delimitedFileWizardPage2.setDescription(Messages.getString("FileWizardPage.descriptionUpdateStep2"));
            // //$NON-NLS-1$
            // addPage(delimitedFileWizardPage2);
            //
            genericSchemaWizardPage0.setPageComplete(true);
            genericSchemaWizardPage1.setPageComplete(isRepositoryObjectEditable());
        } else {
            setWindowTitle("Save as generic schema");// Messages.getString("DelimitedFileWizard.windowTitleUpdate"));
            // //$NON-NLS-1$
            genericSchemaWizardPage0 = new Step0WizardPage(connectionProperty, pathToSave,
                    ERepositoryObjectType.METADATA_GENERIC_SCHEMA, !isRepositoryObjectEditable(), true);
            genericSchemaWizardPage0.setTitle(Messages.getString("FileWizardPage.titleCreate") + " 1 " //$NON-NLS-1$ //$NON-NLS-2$
                    + Messages.getString("FileWizardPage.of") + " 1"); //$NON-NLS-1$ //$NON-NLS-2$
            genericSchemaWizardPage0.setDescription(Messages.getString("FileWizardPage.descriptionCreateStep0")); //$NON-NLS-1$

            addPage(genericSchemaWizardPage0);
            genericSchemaWizardPage0.setPageComplete(true);

        }
    }

    /**
     * This method determine if the 'Finish' button is enable This method is called when 'Finish' button is pressed in
     * the wizard. We will create an operation and run it using wizard as execution context.
     */
    public boolean performFinish() {
        if (isSinglePageOnly) {

            return true;
        }

        boolean formIsPerformed;
        if (genericSchemaWizardPage1 == null) {
            formIsPerformed = genericSchemaWizardPage0.isPageComplete();
        } else {
            formIsPerformed = genericSchemaWizardPage1.isPageComplete();
        }

        if (formIsPerformed) {
            IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
            try {
                if (creation) {
                    String nextId = factory.getNextId();
                    connectionProperty.setId(nextId);
                    factory.create(connectionItem, genericSchemaWizardPage0.getDestinationPath());
                } else {
                    factory.save(connectionItem);
                    closeLockStrategy();
                }
            } catch (PersistenceException e) {
                String detailError = e.toString();
                new ErrorDialogWidthDetailArea(getShell(), PID,
                        Messages.getString("CommonWizard.persistenceException"), //$NON-NLS-1$
                        detailError);
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

    /**
     * Getter for connectionProperty.
     * 
     * @return the connectionProperty
     */
    public Property getConnectionProperty() {
        return this.connectionProperty;
    }

    /**
     * Getter for destinationPath.
     * 
     * @return the destinationPath
     */
    public IPath getPathForSaveAsGenericSchema() {
        return this.genericSchemaWizardPage0.getPathForSaveAsGenericSchema();
    }

}
