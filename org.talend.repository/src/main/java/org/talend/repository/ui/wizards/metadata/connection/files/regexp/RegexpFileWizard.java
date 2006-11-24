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
package org.talend.repository.ui.wizards.metadata.connection.files.regexp;

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
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.RegexpFileConnection;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.ui.EImage;
import org.talend.core.ui.ImageProvider;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.IRepositoryFactory;
import org.talend.repository.model.RepositoryFactoryProvider;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNodeUtilities;
import org.talend.repository.ui.wizards.RepositoryWizard;
import org.talend.repository.ui.wizards.metadata.connection.Step0WizardPage;

/**
 * FileWizard present the FileForm. Use to create a new connection to a DB.
 */

public class RegexpFileWizard extends RepositoryWizard implements INewWizard {

    private static Logger log = Logger.getLogger(RegexpFileWizard.class);

    private WizardPage regexpFileWizardPage0;

    private RegexpFileWizardPage regexpFileWizardPage1;

    private RegexpFileWizardPage regexpFileWizardPage2;

    private RegexpFileWizardPage regexpFileWizardPage3;

    private RegexpFileConnection connection;

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
    public RegexpFileWizard(IWorkbench workbench, boolean creation, ISelection selection, String[] existingNames) {
        super(workbench, creation);
        this.selection = selection;
        this.existingNames = existingNames;
        setNeedsProgressMonitor(true);
        setDefaultPageImageDescriptor(ImageProvider.getImageDesc(EImage.METADATA_FILE_REGEXP_WIZ));

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
            connection = ConnectionFactory.eINSTANCE.createRegexpFileConnection();
            MetadataTable metadataTable = ConnectionFactory.eINSTANCE.createMetadataTable();
            IRepositoryFactory factory = RepositoryFactoryProvider.getInstance();
            metadataTable.setId(factory.getNextId());
            connection.getTables().add(metadataTable);
            connectionProperty = PropertiesFactory.eINSTANCE.createProperty();
            connectionProperty
                    .setAuthor(((RepositoryContext) CorePlugin.getContext().getProperty(Context.REPOSITORY_CONTEXT_KEY))
                            .getUser());
            connectionProperty.setVersion(VersionUtils.DEFAULT_VERSION);
            connectionProperty.setStatusCode("");

            connectionItem = PropertiesFactory.eINSTANCE.createRegExFileConnectionItem();
            connectionItem.setProperty(connectionProperty);
            connectionItem.setConnection(connection);
            break;

        case REPOSITORY_ELEMENT:
            connection = (RegexpFileConnection) ((ConnectionItem) node.getObject().getProperty().getItem()).getConnection();
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
        regexpFileWizardPage0 = new Step0WizardPage(connectionProperty, pathToSave, ERepositoryObjectType.METADATA_FILE_REGEXP,
                !isRepositoryObjectEditable());
        regexpFileWizardPage1 = new RegexpFileWizardPage(1, connectionItem, isRepositoryObjectEditable(), existingNames);
        regexpFileWizardPage2 = new RegexpFileWizardPage(2, connectionItem, isRepositoryObjectEditable(), existingNames);

        if (creation) {
            setWindowTitle(Messages.getString("RegexpFileWizard.windowTitleCreate"));

            regexpFileWizardPage0.setTitle(Messages.getString("FileWizardPage.titleCreate") + " 1 "
                    + Messages.getString("FileWizardPage.of") + " 4");
            regexpFileWizardPage0.setDescription(Messages.getString("FileWizardPage.descriptionCreateStep0"));
            addPage(regexpFileWizardPage0);

            regexpFileWizardPage1.setTitle(Messages.getString("FileWizardPage.titleCreate") + " 2 "
                    + Messages.getString("FileWizardPage.of") + " 4");
            regexpFileWizardPage1.setDescription(Messages.getString("FileWizardPage.descriptionCreateStep1"));
            addPage(regexpFileWizardPage1);

            regexpFileWizardPage2.setTitle(Messages.getString("FileWizardPage.titleCreate") + " 3 "
                    + Messages.getString("FileWizardPage.of") + " 4");
            regexpFileWizardPage2.setDescription(Messages.getString("FileWizardPage.descriptionCreateStep2"));
            addPage(regexpFileWizardPage2);

            regexpFileWizardPage3 = new RegexpFileWizardPage(3, connectionItem, isRepositoryObjectEditable(), null);
            regexpFileWizardPage3.setTitle(Messages.getString("FileWizardPage.titleCreate") + " 4 "
                    + Messages.getString("FileWizardPage.of") + " 4");
            regexpFileWizardPage3.setDescription(Messages.getString("FileWizardPage.descriptionCreateStep3"));
            addPage(regexpFileWizardPage3);

            regexpFileWizardPage1.setPageComplete(false);
            regexpFileWizardPage2.setPageComplete(false);
            regexpFileWizardPage3.setPageComplete(false);

        } else {
            setWindowTitle(Messages.getString("RegexpFileWizard.windowTitleUpdate"));

            regexpFileWizardPage0.setTitle(Messages.getString("FileWizardPage.titleUpdate") + " 1 "
                    + Messages.getString("FileWizardPage.of") + " 3");
            regexpFileWizardPage0.setDescription(Messages.getString("FileWizardPage.descriptionUpdateStep0"));
            addPage(regexpFileWizardPage0);

            regexpFileWizardPage1.setTitle(Messages.getString("FileWizardPage.titleUpdate") + " 2 "
                    + Messages.getString("FileWizardPage.of") + " 3");
            regexpFileWizardPage1.setDescription(Messages.getString("FileWizardPage.descriptionUpdateStep1"));
            addPage(regexpFileWizardPage1);

            regexpFileWizardPage2.setTitle(Messages.getString("FileWizardPage.titleUpdate") + " 3 "
                    + Messages.getString("FileWizardPage.of") + " 3");
            regexpFileWizardPage2.setDescription(Messages.getString("FileWizardPage.descriptionUpdateStep2"));
            addPage(regexpFileWizardPage2);

            regexpFileWizardPage1.setPageComplete(true);
            regexpFileWizardPage2.setPageComplete(isRepositoryObjectEditable());
        }
    }

    /**
     * This method determine if the 'Finish' button is enable This method is called when 'Finish' button is pressed in
     * the wizard. We will create an operation and run it using wizard as execution context.
     */
    public boolean performFinish() {

        boolean formIsPerformed;
        if (regexpFileWizardPage3 == null) {
            formIsPerformed = regexpFileWizardPage2.isPageComplete();
        } else {
            formIsPerformed = regexpFileWizardPage3.isPageComplete();
        }

        if (formIsPerformed) {
            closeLockStrategy();

            IRepositoryFactory factory = RepositoryFactoryProvider.getInstance();
            try {
                if (creation) {
                    String nextId = factory.getNextId();
                    connectionProperty.setId(nextId);
                    factory.create(connectionItem, pathToSave);
                } else {
                    factory.save(connectionItem);
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
