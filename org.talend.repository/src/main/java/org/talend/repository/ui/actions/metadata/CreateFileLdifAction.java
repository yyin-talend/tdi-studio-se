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
package org.talend.repository.ui.actions.metadata;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.core.model.properties.LdifFileConnectionItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.ui.images.OverlayImageProvider;
import org.talend.core.ui.images.ECoreImage;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNode.EProperties;
import org.talend.repository.ui.wizards.metadata.connection.files.ldif.LdifFileWizard;

/**
 * Action used to create a new "file ldif" metadata.<br/>
 * 
 * $Id: CreateFileLdifAction.java 344 2006-11-08 14:29:42 +0000 (mer., 08 nov. 2006) cantoine $
 * 
 */
public class CreateFileLdifAction extends AbstractCreateAction {

    private static final String EDIT_LABEL = Messages.getString("CreateFileLdifAction.action.editTitle"); //$NON-NLS-1$

    private static final String CREATE_LABEL = Messages.getString("CreateFileLdifAction.action.createTitle"); //$NON-NLS-1$

    private static final String OPEN_LABEL = Messages.getString("CreateFileLdifAction.action.openTitle"); //$NON-NLS-1$

    protected static final int WIZARD_WIDTH = 800;

    protected static final int WIZARD_HEIGHT = 475;

    private boolean creation = false;

    private boolean isToolbar = false;

    ImageDescriptor defaultImage = ImageProvider.getImageDesc(ECoreImage.METADATA_FILE_LDIF_ICON);

    ImageDescriptor createImage = OverlayImageProvider
            .getImageWithNew(ImageProvider.getImage(ECoreImage.METADATA_FILE_LDIF_ICON));

    public CreateFileLdifAction() {
        super();

        this.setText(CREATE_LABEL);
        this.setToolTipText(CREATE_LABEL);
        this.setImageDescriptor(ImageProvider.getImageDesc(ECoreImage.METADATA_FILE_LDIF_ICON));
    }

    public CreateFileLdifAction(boolean isToolbar) {
        super();
        this.isToolbar = isToolbar;
        this.setText(CREATE_LABEL);
        this.setToolTipText(CREATE_LABEL);
        this.setImageDescriptor(ImageProvider.getImageDesc(ECoreImage.METADATA_FILE_LDIF_ICON));
    }

    public void run() {
        RepositoryNode metadataNode = getViewPart().getRoot().getChildren().get(6);
        RepositoryNode fileLdifNode = metadataNode.getChildren().get(5);
        ISelection selection = null;
        WizardDialog wizardDialog;
        if (isToolbar) {
            init(fileLdifNode);
            wizardDialog = new WizardDialog(new Shell(), new LdifFileWizard(PlatformUI.getWorkbench(), creation, fileLdifNode,
                    getExistingNames()));
        } else {
            selection = getSelection();
            wizardDialog = new WizardDialog(new Shell(), new LdifFileWizard(PlatformUI.getWorkbench(), creation, selection,
                    getExistingNames()));
        }

        wizardDialog.setPageSize(WIZARD_WIDTH, WIZARD_HEIGHT);
        wizardDialog.create();
        wizardDialog.open();
        if (isToolbar) {
            refresh(fileLdifNode);
        } else {
            refresh(((IStructuredSelection) selection).getFirstElement());
        }

    }

    protected void init(RepositoryNode node) {
        ERepositoryObjectType nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
        if (!ERepositoryObjectType.METADATA_FILE_LDIF.equals(nodeType)) {
            return;
        }

        switch (node.getType()) {
        case SIMPLE_FOLDER:
        case SYSTEM_FOLDER:
            this.setText(CREATE_LABEL);
            collectChildNames(node);
            creation = true;
            this.setImageDescriptor(createImage);
            break;
        case REPOSITORY_ELEMENT:
            IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
            if (factory.isPotentiallyEditable(node.getObject())) {
                this.setText(EDIT_LABEL);
                this.setImageDescriptor(defaultImage);
                collectSiblingNames(node);
            } else {
                this.setText(OPEN_LABEL);
                this.setImageDescriptor(defaultImage);
            }
            collectSiblingNames(node);
            creation = false;
            break;
        default:
            return;
        }
        setEnabled(true);
    }

    public Class getClassForDoubleClick() {
        return LdifFileConnectionItem.class;
    }

}
