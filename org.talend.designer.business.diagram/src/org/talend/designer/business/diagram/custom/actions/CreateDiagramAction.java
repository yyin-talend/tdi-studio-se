package org.talend.designer.business.diagram.custom.actions;

import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.ui.images.ECoreImage;
import org.talend.core.ui.images.OverlayImageProvider;
import org.talend.designer.business.diagram.i18n.Messages;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNodeUtilities;
import org.talend.repository.model.RepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryNode.EProperties;
import org.talend.repository.ui.actions.AContextualAction;
import org.talend.repository.ui.views.IRepositoryView;

/**
 * DOC mhelleboid class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class CreateDiagramAction extends AContextualAction {

    private RepositoryNode repositoryNode;

    private boolean isToolbar = false;

    public CreateDiagramAction() {
        super();
        Image folderImg = ImageProvider.getImage(ECoreImage.BUSINESS_PROCESS_ICON);
        this.setImageDescriptor(OverlayImageProvider.getImageWithNew(folderImg));
        setText(Messages.getString("CreateDiagramAction.CreateBusinessModel")); //$NON-NLS-1$
    }

    public CreateDiagramAction(boolean isToolbar) {
        super();
        this.isToolbar = isToolbar;
        Image folderImg = ImageProvider.getImage(ECoreImage.BUSINESS_PROCESS_ICON);
        this.setImageDescriptor(OverlayImageProvider.getImageWithNew(folderImg));
        setText(Messages.getString("CreateDiagramAction.CreateBusinessModel")); //$NON-NLS-1$
    }

    public void run() {
        SimpleBusinessCreationWizard wizard = new SimpleBusinessCreationWizard(getActivePage().getWorkbenchWindow()
                .getWorkbench(), getPath());

        WizardDialog wizardDialog = new WizardDialog(new Shell(), wizard);
        wizardDialog.create();
        wizardDialog.open();

        refresh();
    }

    public void init(TreeViewer viewer, IStructuredSelection selection) {
        // PTODO mhelleboid refactor AContextualAction

        repositoryNode = getRepositoryNode(selection);
        boolean enabled = false;

        if (repositoryNode != null) {
            ERepositoryObjectType nodeType = (ERepositoryObjectType) repositoryNode.getProperties(EProperties.CONTENT_TYPE);
            if (repositoryNode.getType() == RepositoryNode.ENodeType.SYSTEM_FOLDER
                    || repositoryNode.getType() == RepositoryNode.ENodeType.SIMPLE_FOLDER) {
                if (nodeType == ERepositoryObjectType.BUSINESS_PROCESS) {
                    enabled = true;
                }
            }
        }

        if (ProxyRepositoryFactory.getInstance().isUserReadOnlyOnCurrentProject()) {
            enabled = false;
        }

        setEnabled(enabled);
    }

    private RepositoryNode getRepositoryNode(IStructuredSelection selection) {
        if (!selection.isEmpty() && selection.size() == 1) {
            Object object = selection.getFirstElement();
            if (object instanceof RepositoryNode) {
                return (RepositoryNode) object;
            }
        }
        return null;
    }

    private IPath getPath() {
        IPath path;
        if (isToolbar) {
            repositoryNode = getProcessNode();
        }

        if (repositoryNode.getType() == ENodeType.SIMPLE_FOLDER || repositoryNode.getType() == ENodeType.SYSTEM_FOLDER) {
            path = RepositoryNodeUtilities.getPath(repositoryNode);
        } else {
            path = new Path(""); //$NON-NLS-1$
        }
        return path;

    }

    public IRepositoryView getRepositoryView() {
        IViewPart findView = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(
                IRepositoryView.VIEW_ID);
        return (IRepositoryView) findView;
    }

    public RepositoryNode getProcessNode() {
        List<RepositoryNode> chindren = getRepositoryView().getRoot().getChildren();
        for (RepositoryNode repositoryNode : chindren) {
            if (repositoryNode.getContentType() == ERepositoryObjectType.BUSINESS_PROCESS) {
                return repositoryNode;
            }

        }

        return null;
    }
}
