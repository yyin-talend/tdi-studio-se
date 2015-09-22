// ============================================================================
//
// Copyright (C) 2006-2015 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.component.ui.wizard.ui;

import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.ui.swt.dialogs.ErrorDialogWidthDetailArea;
import org.talend.commons.utils.VersionUtils;
import org.talend.component.ui.model.genericMetadata.GenericConnection;
import org.talend.component.ui.model.genericMetadata.GenericMetadataFactory;
import org.talend.component.ui.wizard.constants.IGenericConstants;
import org.talend.component.ui.wizard.i18n.Messages;
import org.talend.component.ui.wizard.internal.IGenericWizardInternalService;
import org.talend.component.ui.wizard.internal.service.GenericWizardInternalService;
import org.talend.component.ui.wizard.update.GenericUpdateManager;
import org.talend.component.ui.wizard.util.GenericWizardServiceFactory;
import org.talend.components.api.properties.presentation.Form;
import org.talend.components.api.wizard.ComponentWizard;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.RepositoryObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.designer.core.IDesignerCoreService;
import org.talend.metadata.managment.ui.utils.ConnectionContextHelper;
import org.talend.metadata.managment.ui.wizard.CheckLastVersionRepositoryWizard;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNodeUtilities;

/**
 * 
 * created by ycbai on 2015年9月16日 Detailled comment
 *
 */
public class GenericConnWizard extends CheckLastVersionRepositoryWizard {

    private GenericConnWizardPage connPage;

    private GenericConnection connection;

    private Property connectionProperty;

    private String originalLabel;

    private String originalVersion;

    private String originalPurpose;

    private String originalDescription;

    private String originalStatus;

    private RepositoryNode repNode;

    public GenericConnWizard(IWorkbench workbench, boolean creation, RepositoryNode node, String[] existingNames) {
        super(workbench, creation);
        this.existingNames = existingNames;
        repNode = node;
        setNeedsProgressMonitor(true);
        ENodeType nodeType = node.getType();
        switch (nodeType) {
        case SIMPLE_FOLDER:
        case REPOSITORY_ELEMENT:
            pathToSave = RepositoryNodeUtilities.getPath(node);
            break;
        case SYSTEM_FOLDER:
            pathToSave = new Path(""); //$NON-NLS-1$
            break;
        }

        switch (nodeType) {
        case SIMPLE_FOLDER:
        case SYSTEM_FOLDER:
            connection = GenericMetadataFactory.eINSTANCE.createGenericConnection();
            connectionProperty = PropertiesFactory.eINSTANCE.createProperty();
            connectionProperty.setAuthor(((RepositoryContext) CoreRuntimePlugin.getInstance().getContext()
                    .getProperty(Context.REPOSITORY_CONTEXT_KEY)).getUser());
            connectionProperty.setVersion(VersionUtils.DEFAULT_VERSION);
            connectionProperty.setStatusCode(""); //$NON-NLS-1$

            connectionItem = GenericMetadataFactory.eINSTANCE.createGenericConnectionItem();
            connectionItem.setProperty(connectionProperty);
            connectionItem.setConnection(connection);
            break;

        case REPOSITORY_ELEMENT:
            RepositoryObject object = new RepositoryObject(node.getObject().getProperty());
            setRepositoryObject(object);
            connection = (GenericConnection) ((ConnectionItem) object.getProperty().getItem()).getConnection();
            connectionProperty = object.getProperty();
            connectionItem = (ConnectionItem) object.getProperty().getItem();
            // set the repositoryObject, lock and set isRepositoryObjectEditable
            isRepositoryObjectEditable();
            initLockStrategy();
            break;
        }
        if (!creation) {
            this.originalLabel = this.connectionItem.getProperty().getDisplayName();
            this.originalVersion = this.connectionItem.getProperty().getVersion();
            this.originalDescription = this.connectionItem.getProperty().getDescription();
            this.originalPurpose = this.connectionItem.getProperty().getPurpose();
            this.originalStatus = this.connectionItem.getProperty().getStatusCode();
        }
        // initialize the context mode
        ConnectionContextHelper.checkContextMode(connectionItem);
        setHelpAvailable(false);
    }

    @Override
    public void addPages() {
        setWindowTitle(Messages.getString("NoSQLWizard.windowTitle")); //$NON-NLS-1$
        String typeName = repNode.getContentType().getType();
        Image wiardImage = GenericWizardServiceFactory.getGenericWizardService().getWiardImage(typeName);
        setDefaultPageImageDescriptor(ImageDescriptor.createFromImage(wiardImage));

        IGenericWizardInternalService internalService = new GenericWizardInternalService();
        ComponentWizard componentWizard = internalService.getComponentWizard(typeName);
        List<Form> forms = componentWizard.getForms();
        if (forms.size() > 0) {
            connPage = new GenericConnWizardPage(connectionItem, isRepositoryObjectEditable(), existingNames, creation,
                    forms.get(0));
        }
        if (connPage != null) {
            if (creation) {
                connPage.setTitle(Messages.getString("NoSQLConnWizardPage.titleCreate.Step2")); //$NON-NLS-1$
                connPage.setDescription(Messages.getString("NoSQLConnWizardPage.descriptionCreate.Step2")); //$NON-NLS-1$
                connPage.setPageComplete(false);
            } else {
                connPage.setTitle(Messages.getString("NoSQLConnWizardPage.titleUpdate.Step2")); //$NON-NLS-1$
                connPage.setDescription(Messages.getString("NoSQLConnWizardPage.descriptionUpdate.Step2")); //$NON-NLS-1$
                connPage.setPageComplete(isRepositoryObjectEditable());
            }
        }
        addPage(connPage);
    }

    @Override
    public boolean performFinish() {
        if (connPage.isPageComplete()) {
            final IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
            String displayName = connectionProperty.getDisplayName();
            connectionProperty.setLabel(displayName);
            this.connection.setName(displayName);
            this.connection.setLabel(displayName);
            try {
                if (creation) {
                    String nextId = factory.getNextId();
                    connectionProperty.setId(nextId);
                    // factory.create(connectionItem, propertiesPage.getDestinationPath());
                } else {
                    GenericUpdateManager.updateGenericConnection(connectionItem);
                    updateConnectionItem();

                    // boolean isModified = propertiesPage.isNameModifiedByUser();
                    // if (isModified) {
                    if (GlobalServiceRegister.getDefault().isServiceRegistered(IDesignerCoreService.class)) {
                        IDesignerCoreService service = (IDesignerCoreService) GlobalServiceRegister.getDefault().getService(
                                IDesignerCoreService.class);
                        if (service != null) {
                            service.refreshComponentView(connectionItem);
                        }
                    }

                    // }
                }
            } catch (Exception e) {
                new ErrorDialogWidthDetailArea(getShell(), IGenericConstants.PLUGIN_ID,
                        Messages.getString("NoSQLWizard.persistenceException"), //$NON-NLS-1$
                        ExceptionUtils.getFullStackTrace(e));
                ExceptionHandler.process(e);
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
            connectionItem.getProperty().setDisplayName(this.originalLabel);
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
    public void init(final IWorkbench workbench, final IStructuredSelection sel) {
        super.setWorkbench(workbench);
        this.selection = sel;
    }

    @Override
    public ConnectionItem getConnectionItem() {
        return this.connectionItem;
    }

}
