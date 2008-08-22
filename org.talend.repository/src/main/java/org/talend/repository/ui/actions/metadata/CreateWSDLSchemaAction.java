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
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.core.model.properties.WSDLSchemaConnectionItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.ui.images.ECoreImage;
import org.talend.core.ui.images.OverlayImageProvider;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNode.EProperties;
import org.talend.repository.ui.wizards.metadata.connection.wsdl.WSDLSchemaWizard;

/**
 * DOC qwei class global comment. Detailled comment
 */
public class CreateWSDLSchemaAction extends AbstractCreateAction {

    private String createLabel;

    private ImageDescriptor defaultImage, createImage;

    private ERepositoryObjectType currentNodeType;

    private boolean creation = false;

    private String openLabel;

    private String editLabel; //$NON-NLS-1$

    protected static final int WIZARD_WIDTH = 800;

    protected static final int WIZARD_HEIGHT = 540;

    public CreateWSDLSchemaAction() {
        super();

        // TODO: should to fix the I18N issue.
        createLabel = "Create WSDL schema";
        editLabel = "Edit WSDL schema";
        openLabel = "Open WSDL schema";
        // TODO: should change to another icon.
        defaultImage = ImageProvider.getImageDesc(ECoreImage.METADATA_WSDL_SCHEMA_ICON);
        createImage = OverlayImageProvider.getImageWithNew(ImageProvider.getImage(ECoreImage.METADATA_WSDL_SCHEMA_ICON));

        setText(createLabel);
        setToolTipText(createLabel);
        setImageDescriptor(defaultImage);

        currentNodeType = ERepositoryObjectType.METADATA_WSDL_SCHEMA;
    }

    public CreateWSDLSchemaAction(boolean isToolbar) {
        this();
        setToolbar(isToolbar);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.ui.actions.metadata.AbstractCreateAction#init(org.talend.repository.model.RepositoryNode)
     */
    @Override
    protected void init(RepositoryNode node) {
        ERepositoryObjectType nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
        if (!currentNodeType.equals(nodeType)) {
            return;
        }

        IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
        switch (node.getType()) {
        case SIMPLE_FOLDER:
        case SYSTEM_FOLDER:
            if (factory.isUserReadOnlyOnCurrentProject() || !ProjectManager.getInstance().isInCurrentMainProject(node)) {
                setEnabled(false);
                return;
            }
            this.setText(createLabel);
            collectChildNames(node);
            this.setImageDescriptor(createImage);
            creation = true;
            break;
        case REPOSITORY_ELEMENT:
            if (factory.isPotentiallyEditable(node.getObject())) {
                this.setText(editLabel);
                this.setImageDescriptor(defaultImage);
                collectSiblingNames(node);
            } else {
                this.setText(openLabel);
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
        return WSDLSchemaConnectionItem.class;
    }

    public void run() {
        RepositoryNode wsdlSchemaNode = getCurrentRepositoryNode();
        if (isToolbar()) {
            if (wsdlSchemaNode != null && wsdlSchemaNode.getContentType() != currentNodeType) {
                wsdlSchemaNode = null;
            }
            if (wsdlSchemaNode == null) {
                wsdlSchemaNode = getRepositoryNodeForDefault(currentNodeType);
            }
        }
        WizardDialog wizardDialog;
        ISelection selection = null;
        if (isToolbar()) {
            init(wsdlSchemaNode);
            WSDLSchemaWizard wsdlSchemaWizard = new WSDLSchemaWizard(PlatformUI.getWorkbench(), creation,
                    wsdlSchemaNode, getExistingNames(), false);
            wizardDialog = new WizardDialog(new Shell(), wsdlSchemaWizard);
        } else {
            selection = getSelection();
            wizardDialog = new WizardDialog(new Shell(), new WSDLSchemaWizard(PlatformUI.getWorkbench(), creation, selection,
                    getExistingNames(), false));
        }

        wizardDialog.setPageSize(WIZARD_WIDTH, WIZARD_HEIGHT);
        wizardDialog.create();

        wizardDialog.open();
        if (isToolbar()) {
            refresh(wsdlSchemaNode);
        } else {
            refresh(getCurrentRepositoryNode());
        }

    }
}
