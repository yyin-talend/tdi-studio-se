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
package org.talend.repository.ui.wizards.metadata.connection.files.delimited;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.swt.dialogs.ErrorDialogWidthDetailArea;
import org.talend.commons.utils.VersionUtils;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.DelimitedFileConnection;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.ui.images.EImage;
import org.talend.core.ui.images.ImageProvider;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNodeUtilities;
import org.talend.repository.ui.wizards.RepositoryWizard;
import org.talend.repository.ui.wizards.metadata.connection.Step0WizardPage;

/**
 * FileWizard present the FileForm. Use to create a new connection to a DB.
 */

public class DelimitedFileWizard extends RepositoryWizard implements INewWizard {

    private static Logger log = Logger.getLogger(DelimitedFileWizard.class);

    private WizardPage delimitedFileWizardPage0;

    private DelimitedFileWizardPage delimitedFileWizardPage1;

    private DelimitedFileWizardPage delimitedFileWizardPage2;

    private DelimitedFileWizardPage delimitedFileWizardPage3;

    private DelimitedFileConnection connection;

    private Property connectionProperty;

    private ConnectionItem connectionItem;

    /**
     * Constructor for FileWizard.
     * 
     * @param workbench
     * @param selection
     * @param strings
     */
    @SuppressWarnings("unchecked")
    public DelimitedFileWizard(IWorkbench workbench, boolean creation, ISelection selection, String[] existingNames) {
        super(workbench, creation);
        this.selection = selection;
        this.existingNames = existingNames;
        setNeedsProgressMonitor(true);
        setDefaultPageImageDescriptor(ImageProvider.getImageDesc(EImage.METADATA_FILE_DELIMITED_WIZ));

        Object obj = ((IStructuredSelection) selection).getFirstElement();
        RepositoryNode node = (RepositoryNode) obj;
        switch (node.getType()) {
        case SIMPLE_FOLDER:
        case REPOSITORY_ELEMENT:
            pathToSave = RepositoryNodeUtilities.getPath(node);
            break;
        case SYSTEM_FOLDER:
            pathToSave = new Path("");
            break;
        }

        switch (node.getType()) {
        case SIMPLE_FOLDER:
        case SYSTEM_FOLDER:
            connection = ConnectionFactory.eINSTANCE.createDelimitedFileConnection();
            MetadataTable metadataTable = ConnectionFactory.eINSTANCE.createMetadataTable();
            ProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
            metadataTable.setId(factory.getNextId());
            connection.getTables().add(metadataTable);
            connectionProperty = PropertiesFactory.eINSTANCE.createProperty();
            connectionProperty
                    .setAuthor(((RepositoryContext) CorePlugin.getContext().getProperty(Context.REPOSITORY_CONTEXT_KEY))
                            .getUser());
            connectionProperty.setVersion(VersionUtils.DEFAULT_VERSION);
            connectionProperty.setStatusCode("");

            connectionItem = PropertiesFactory.eINSTANCE.createDelimitedFileConnectionItem();
            connectionItem.setProperty(connectionProperty);
            connectionItem.setConnection(connection);
            break;

        case REPOSITORY_ELEMENT:
            connection = (DelimitedFileConnection) ((ConnectionItem) node.getObject().getProperty().getItem()).getConnection();
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
        delimitedFileWizardPage0 = new Step0WizardPage(connectionProperty, pathToSave,
                ERepositoryObjectType.METADATA_FILE_DELIMITED, !isRepositoryObjectEditable());
        delimitedFileWizardPage1 = new DelimitedFileWizardPage(1, connectionItem, isRepositoryObjectEditable(), existingNames);
        delimitedFileWizardPage2 = new DelimitedFileWizardPage(2, connectionItem, isRepositoryObjectEditable(), existingNames);

        if (creation) {
            setWindowTitle(Messages.getString("DelimitedFileWizard.windowTitleCreate"));

            delimitedFileWizardPage0.setTitle(Messages.getString("FileWizardPage.titleCreate") + " 1 "
                    + Messages.getString("FileWizardPage.of") + " 4");
            delimitedFileWizardPage0.setDescription(Messages.getString("FileWizardPage.descriptionCreateStep0"));
            addPage(delimitedFileWizardPage0);

            delimitedFileWizardPage1.setTitle(Messages.getString("FileWizardPage.titleCreate") + " 2 "
                    + Messages.getString("FileWizardPage.of") + " 4");
            delimitedFileWizardPage1.setDescription(Messages.getString("FileWizardPage.descriptionCreateStep1"));
            addPage(delimitedFileWizardPage1);

            delimitedFileWizardPage2.setTitle(Messages.getString("FileWizardPage.titleCreate") + " 3 "
                    + Messages.getString("FileWizardPage.of") + " 4");
            delimitedFileWizardPage2.setDescription(Messages.getString("FileWizardPage.descriptionCreateStep2"));
            addPage(delimitedFileWizardPage2);

            delimitedFileWizardPage3 = new DelimitedFileWizardPage(3, connectionItem, isRepositoryObjectEditable(), null);
            delimitedFileWizardPage3.setTitle(Messages.getString("FileWizardPage.titleCreate") + " 4 "
                    + Messages.getString("FileWizardPage.of") + " 4");
            delimitedFileWizardPage3.setDescription(Messages.getString("FileWizardPage.descriptionCreateStep3"));
            addPage(delimitedFileWizardPage3);

            // delimitedFileWizardPage0.setPageComplete(false);
            delimitedFileWizardPage1.setPageComplete(false);
            delimitedFileWizardPage2.setPageComplete(false);
            delimitedFileWizardPage3.setPageComplete(false);

        } else {
            setWindowTitle(Messages.getString("DelimitedFileWizard.windowTitleUpdate"));

            delimitedFileWizardPage0.setTitle(Messages.getString("FileWizardPage.titleUpdate") + " 1 "
                    + Messages.getString("FileWizardPage.of") + " 3");
            delimitedFileWizardPage0.setDescription(Messages.getString("FileWizardPage.descriptionUpdateStep0"));
            addPage(delimitedFileWizardPage0);

            delimitedFileWizardPage1.setTitle(Messages.getString("FileWizardPage.titleUpdate") + " 2 "
                    + Messages.getString("FileWizardPage.of") + " 3");
            delimitedFileWizardPage1.setDescription(Messages.getString("FileWizardPage.descriptionUpdateStep1"));
            addPage(delimitedFileWizardPage1);

            delimitedFileWizardPage2.setTitle(Messages.getString("FileWizardPage.titleUpdate") + " 3 "
                    + Messages.getString("FileWizardPage.of") + " 3");
            delimitedFileWizardPage2.setDescription(Messages.getString("FileWizardPage.descriptionUpdateStep2"));
            addPage(delimitedFileWizardPage2);

            delimitedFileWizardPage1.setPageComplete(true);
            delimitedFileWizardPage2.setPageComplete(isRepositoryObjectEditable());
        }
    }

    /**
     * This method determine if the 'Finish' button is enable This method is called when 'Finish' button is pressed in
     * the wizard. We will create an operation and run it using wizard as execution context.
     */
    public boolean performFinish() {

        boolean formIsPerformed;
        if (delimitedFileWizardPage3 == null) {
            formIsPerformed = delimitedFileWizardPage2.isPageComplete();
        } else {
            formIsPerformed = delimitedFileWizardPage3.isPageComplete();
        }

        if (formIsPerformed) {
            ProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
            try {
                if (creation) {
                    String nextId = factory.getNextId();
                    connectionProperty.setId(nextId);
                    factory.create(connectionItem, pathToSave);
                } else {
                    factory.save(connectionItem);
                    closeLockStrategy();
                }
            } catch (PersistenceException e) {
                String detailError = e.toString();
                new ErrorDialogWidthDetailArea(getShell(), PID, Messages.getString("CommonWizard.persistenceException"),
                        detailError);
                log.error(Messages.getString("CommonWizard.persistenceException") + "\n" + detailError);
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
