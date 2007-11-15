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
package org.talend.repository.ui.actions.documentation;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.ui.images.OverlayImageProvider;
import org.talend.core.ui.images.CoreImageProvider;
import org.talend.core.ui.images.ECoreImage;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNodeUtilities;
import org.talend.repository.model.RepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryNode.EProperties;
import org.talend.repository.ui.actions.AContextualAction;
import org.talend.repository.ui.wizards.documentation.DocumentationCreateWizard;

/**
 * Action to create a new IDocumentation. <br/>
 * 
 * $Id$
 * 
 */
public class CreateDocumentationAction extends AContextualAction {

    /**
     * Constructs a new CreateDocumentationAction.
     */
    public CreateDocumentationAction() {
        super();

        setText(Messages.getString("CreateDocumentationAction.createDocActionText.addDoc")); //$NON-NLS-1$
        setToolTipText(Messages.getString("CreateDocumentationAction.createDocActionTipText.addDocItem")); //$NON-NLS-1$
        Image folderImg = ImageProvider.getImage(ECoreImage.DOCUMENTATION_ICON);
        this.setImageDescriptor(OverlayImageProvider.getImageWithNew(folderImg));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.actions.ITreeContextualAction#init(org.eclipse.jface.viewers.TreeViewer,
     * org.eclipse.jface.viewers.IStructuredSelection)
     */
    public void init(TreeViewer viewer, IStructuredSelection selection) {
        boolean canWork = !selection.isEmpty() && selection.size() == 1;
        if (ProxyRepositoryFactory.getInstance().isUserReadOnlyOnCurrentProject()) {
            canWork = false;
        }
        if (canWork) {
            RepositoryNode node = (RepositoryNode) selection.getFirstElement();
            switch (node.getType()) {
            case SIMPLE_FOLDER:
            case SYSTEM_FOLDER:
                ERepositoryObjectType nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
                if (nodeType != ERepositoryObjectType.DOCUMENTATION) {
                    canWork = false;
                }
                break;
            default:
                canWork = false;
            }
        }
        setEnabled(canWork);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {
        DocumentationCreateWizard docWizard = new DocumentationCreateWizard(PlatformUI.getWorkbench(), getPath());
        WizardDialog dlg = new WizardDialog(Display.getCurrent().getActiveShell(), docWizard);
        dlg.open();

        RepositoryNode node = (RepositoryNode) ((IStructuredSelection) getSelection()).getFirstElement();
        refresh(node);
    }

    private IPath getPath() {
        RepositoryNode node = (RepositoryNode) ((IStructuredSelection) getSelection()).getFirstElement();

        IPath path;
        if (node.getType() == ENodeType.SIMPLE_FOLDER || node.getType() == ENodeType.SYSTEM_FOLDER) {
            path = RepositoryNodeUtilities.getPath(node);
        } else {
            path = new Path(""); //$NON-NLS-1$
        }
        return path;
    }

}
