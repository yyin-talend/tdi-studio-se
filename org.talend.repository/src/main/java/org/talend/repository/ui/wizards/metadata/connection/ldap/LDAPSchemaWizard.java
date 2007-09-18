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
package org.talend.repository.ui.wizards.metadata.connection.ldap;

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
import org.talend.core.model.metadata.builder.connection.LDAPSchemaConnection;
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

/**
 * The class is used for LDAP schema on Repository View. <br/>
 * @author ftang, 18/09/2007
 * 
 */
public class LDAPSchemaWizard extends RepositoryWizard implements INewWizard {

    private static Logger log = Logger.getLogger(LDAPSchemaWizard.class);

    private PropertiesWizardPage ldapSchemaWizardPage0;

    private LDAPSchemaWizardPage ldapSchemaWizardPage1;

    private LDAPSchemaWizardPage ldapSchemaWizardPage2;

    private LDAPSchemaWizardPage ldapSchemaWizardPage3;

    private LDAPSchemaWizardPage ldapSchemaWizardPage4;

    private LDAPSchemaConnection connection;

    private Property connectionProperty;

    private ConnectionItem connectionItem;

    private boolean isSinglePageOnly;

    private static final String ALL_STEPS = "5";

    /**
     * LDAPSchemaWizard constructor comment.
     * 
     * @param workbench
     * @param creation
     * @param selection
     * @param existingNames
     * @param b
     */
    public LDAPSchemaWizard(IWorkbench workbench, boolean creation, ISelection selection, String[] existingNames,
            boolean isSinglePageOnly) {
        super(workbench, creation);
        this.selection = selection;
        this.existingNames = existingNames;
        this.isSinglePageOnly = isSinglePageOnly;
        setNeedsProgressMonitor(true);

        // TODO: should to changed icon.
        setDefaultPageImageDescriptor(ImageProvider.getImageDesc(ECoreImage.METADATA_FILE_DELIMITED_WIZ));

        if (selection == null || existingNames == null) {
            connectionProperty = PropertiesFactory.eINSTANCE.createProperty();
            connectionProperty.setAuthor(((RepositoryContext) CorePlugin.getContext().getProperty(
                    Context.REPOSITORY_CONTEXT_KEY)).getUser());
            connectionProperty.setVersion(VersionUtils.DEFAULT_VERSION);
            connectionProperty.setStatusCode(""); //$NON-NLS-1$

            connectionItem = PropertiesFactory.eINSTANCE.createLDAPSchemaConnectionItem();
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
            connection = ConnectionFactory.eINSTANCE.createLDAPSchemaConnection();
            MetadataTable metadataTable = ConnectionFactory.eINSTANCE.createMetadataTable();
            IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
            metadataTable.setId(factory.getNextId());
            connection.getTables().add(metadataTable);
            connectionProperty = PropertiesFactory.eINSTANCE.createProperty();
            connectionProperty.setAuthor(((RepositoryContext) CorePlugin.getContext().getProperty(
                    Context.REPOSITORY_CONTEXT_KEY)).getUser());
            connectionProperty.setVersion(VersionUtils.DEFAULT_VERSION);
            connectionProperty.setStatusCode(""); //$NON-NLS-1$

            connectionItem = PropertiesFactory.eINSTANCE.createLDAPSchemaConnectionItem();
            connectionItem.setProperty(connectionProperty);
            connectionItem.setConnection(connection);
            break;

        case REPOSITORY_ELEMENT:
            connection = (LDAPSchemaConnection) ((ConnectionItem) node.getObject().getProperty().getItem())
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

        ldapSchemaWizardPage0 = null;
        ldapSchemaWizardPage1 = null;
        ldapSchemaWizardPage2 = null;
        ldapSchemaWizardPage3 = null;
        ldapSchemaWizardPage4 = null;

        if (creation) {
            setWindowTitle("Create new LDAP schema");// Messages.getString("DelimitedFileWizard.windowTitleCreate"));
            // //$NON-NLS-1$
            ldapSchemaWizardPage0 = new Step0WizardPage(connectionProperty, pathToSave,
                    ERepositoryObjectType.METADATA_GENERIC_SCHEMA, !isRepositoryObjectEditable(), creation);
            ldapSchemaWizardPage0.setTitle(Messages.getString("FileWizardPage.titleCreate") + " 1 " //$NON-NLS-1$ //$NON-NLS-2$
                    + Messages.getString("FileWizardPage.of") + " " + ALL_STEPS); //$NON-NLS-1$ //$NON-NLS-2$
            ldapSchemaWizardPage0.setDescription(Messages.getString("FileWizardPage.descriptionCreateStep0")); //$NON-NLS-1$
            addPage(ldapSchemaWizardPage0);

            ldapSchemaWizardPage1 = new LDAPSchemaWizardPage(1, connectionItem, isRepositoryObjectEditable(), null);
            ldapSchemaWizardPage1.setTitle(Messages.getString("FileWizardPage.titleCreate") + " 2 " //$NON-NLS-1$ //$NON-NLS-2$
                    + Messages.getString("FileWizardPage.of") + " " + ALL_STEPS); //$NON-NLS-1$ //$NON-NLS-2$
            ldapSchemaWizardPage1.setDescription(Messages.getString("FileWizardPage.descriptionCreateStep1")); //$NON-NLS-1$
            addPage(ldapSchemaWizardPage1);

            ldapSchemaWizardPage2 = new LDAPSchemaWizardPage(2, connectionItem, isRepositoryObjectEditable(), null);
            ldapSchemaWizardPage2.setTitle(Messages.getString("FileWizardPage.titleCreate") + " 3 " //$NON-NLS-1$ //$NON-NLS-2$
                    + Messages.getString("FileWizardPage.of") + " " + ALL_STEPS); //$NON-NLS-1$ //$NON-NLS-2$
            ldapSchemaWizardPage2.setDescription(Messages.getString("FileWizardPage.descriptionCreateStep2")); //$NON-NLS-1$
            addPage(ldapSchemaWizardPage2);

            ldapSchemaWizardPage3 = new LDAPSchemaWizardPage(3, connectionItem, isRepositoryObjectEditable(), null);
            ldapSchemaWizardPage3.setTitle(Messages.getString("FileWizardPage.titleCreate") + " 4 " //$NON-NLS-1$ //$NON-NLS-2$
                    + Messages.getString("FileWizardPage.of") + " " + ALL_STEPS); //$NON-NLS-1$ //$NON-NLS-2$
            ldapSchemaWizardPage3.setDescription(Messages.getString("FileWizardPage.descriptionCreateStep2")); //$NON-NLS-1$
            addPage(ldapSchemaWizardPage3);

            ldapSchemaWizardPage4 = new LDAPSchemaWizardPage(4, connectionItem, isRepositoryObjectEditable(), null);
            ldapSchemaWizardPage4.setTitle(Messages.getString("FileWizardPage.titleCreate") + " 5 " //$NON-NLS-1$ //$NON-NLS-2$
                    + Messages.getString("FileWizardPage.of") + " " + ALL_STEPS); //$NON-NLS-1$ //$NON-NLS-2$
            ldapSchemaWizardPage4.setDescription(Messages.getString("FileWizardPage.descriptionCreateStep2")); //$NON-NLS-1$
            addPage(ldapSchemaWizardPage4);

            ldapSchemaWizardPage0.setPageComplete(false);
            ldapSchemaWizardPage1.setPageComplete(false);
            ldapSchemaWizardPage2.setPageComplete(false);
            ldapSchemaWizardPage3.setPageComplete(false);
            ldapSchemaWizardPage4.setPageComplete(false);

        } else {

            setWindowTitle("Update LDAP schema");// Messages.getString("DelimitedFileWizard.windowTitleUpdate"));
            // //$NON-NLS-1$

            ldapSchemaWizardPage0 = new Step0WizardPage(connectionProperty, pathToSave,
                    ERepositoryObjectType.METADATA_LDAP_SCHEMA, !isRepositoryObjectEditable(), creation);

            ldapSchemaWizardPage0.setTitle(Messages.getString("FileWizardPage.titleCreate") + " 1 " //$NON-NLS-1$ //$NON-NLS-2$
                    + Messages.getString("FileWizardPage.of") + " 5"); //$NON-NLS-1$ //$NON-NLS-2$
            ldapSchemaWizardPage0.setDescription(Messages.getString("FileWizardPage.descriptionCreateStep0")); //$NON-NLS-1$
            addPage(ldapSchemaWizardPage0);

            ldapSchemaWizardPage1 = new LDAPSchemaWizardPage(1, connectionItem, isRepositoryObjectEditable(), null);
            ldapSchemaWizardPage1.setTitle(Messages.getString("FileWizardPage.titleUpdate") + " 2 " //$NON-NLS-1$ //$NON-NLS-2$
                    + Messages.getString("FileWizardPage.of") + " 5"); //$NON-NLS-1$ //$NON-NLS-2$
            ldapSchemaWizardPage1.setDescription(Messages.getString("FileWizardPage.descriptionUpdateStep0")); //$NON-NLS-1$
            addPage(ldapSchemaWizardPage1);

            ldapSchemaWizardPage2 = new LDAPSchemaWizardPage(2, connectionItem, isRepositoryObjectEditable(), null);
            ldapSchemaWizardPage2.setTitle(Messages.getString("FileWizardPage.titleUpdate") + " 2 " //$NON-NLS-1$ //$NON-NLS-2$
                    + Messages.getString("FileWizardPage.of") + " 5"); //$NON-NLS-1$ //$NON-NLS-2$
            ldapSchemaWizardPage2.setDescription(Messages.getString("FileWizardPage.descriptionUpdateStep0")); //$NON-NLS-1$
            addPage(ldapSchemaWizardPage2);

            ldapSchemaWizardPage3 = new LDAPSchemaWizardPage(3, connectionItem, isRepositoryObjectEditable(), null);
            ldapSchemaWizardPage3.setTitle(Messages.getString("FileWizardPage.titleUpdate") + " 2 " //$NON-NLS-1$ //$NON-NLS-2$
                    + Messages.getString("FileWizardPage.of") + " 5"); //$NON-NLS-1$ //$NON-NLS-2$
            ldapSchemaWizardPage3.setDescription(Messages.getString("FileWizardPage.descriptionUpdateStep0")); //$NON-NLS-1$
            addPage(ldapSchemaWizardPage3);

            ldapSchemaWizardPage4 = new LDAPSchemaWizardPage(4, connectionItem, isRepositoryObjectEditable(), null);
            ldapSchemaWizardPage4.setTitle(Messages.getString("FileWizardPage.titleUpdate") + " 2 " //$NON-NLS-1$ //$NON-NLS-2$
                    + Messages.getString("FileWizardPage.of") + " 5"); //$NON-NLS-1$ //$NON-NLS-2$
            ldapSchemaWizardPage4.setDescription(Messages.getString("FileWizardPage.descriptionUpdateStep0")); //$NON-NLS-1$
            addPage(ldapSchemaWizardPage4);

            ldapSchemaWizardPage0.setPageComplete(false);
            ldapSchemaWizardPage1.setPageComplete(false);
            ldapSchemaWizardPage2.setPageComplete(false);
            ldapSchemaWizardPage3.setPageComplete(false);
            ldapSchemaWizardPage4.setPageComplete(true);
        }
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
        if (ldapSchemaWizardPage4 == null) {
            formIsPerformed = ldapSchemaWizardPage3.isPageComplete();
        } else {
            formIsPerformed = ldapSchemaWizardPage4.isPageComplete();
        }

        if (formIsPerformed) {
            IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
            try {
                if (creation) {
                    String nextId = factory.getNextId();
                    connectionProperty.setId(nextId);
                    factory.create(connectionItem, ldapSchemaWizardPage0.getDestinationPath());
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
}
