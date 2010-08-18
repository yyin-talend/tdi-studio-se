package org.talend.designer.business.diagram.custom.actions;

import java.util.List;
import java.util.Properties;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.intro.IIntroSite;
import org.eclipse.ui.intro.config.IIntroAction;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.RepositoryManager;
import org.talend.core.ui.images.ECoreImage;
import org.talend.core.ui.images.OverlayImageProvider;
import org.talend.designer.business.diagram.i18n.Messages;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.ProjectRepositoryNode;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNodeUtilities;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.IRepositoryNode.EProperties;
import org.talend.repository.ui.actions.AContextualAction;
import org.talend.repository.ui.views.IRepositoryView;
import org.talend.repository.ui.views.RepositoryView;

/**
 * DOC mhelleboid class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class CreateDiagramAction extends AContextualAction implements IIntroAction {

    private RepositoryNode repositoryNode;

    public CreateDiagramAction() {
        super();
        Image folderImg = ImageProvider.getImage(ECoreImage.BUSINESS_PROCESS_ICON);
        this.setImageDescriptor(OverlayImageProvider.getImageWithNew(folderImg));
        setText(Messages.getString("CreateDiagramAction.CreateBusinessModel")); //$NON-NLS-1$
    }

    public CreateDiagramAction(boolean isToolbar) {
        super();
        setToolbar(isToolbar);
        Image folderImg = ImageProvider.getImage(ECoreImage.BUSINESS_PROCESS_ICON);
        this.setImageDescriptor(OverlayImageProvider.getImageWithNew(folderImg));
        setText(Messages.getString("CreateDiagramAction.CreateBusinessModel")); //$NON-NLS-1$
    }

    protected void doRun() {
        SimpleBusinessCreationWizard wizard = new SimpleBusinessCreationWizard(getActivePage().getWorkbenchWindow()
                .getWorkbench(), getPath());

        WizardDialog wizardDialog = new WizardDialog(new Shell(), wizard);
        wizardDialog.create();
        wizardDialog.open();

        RepositoryManager.refreshCreatedNode(ERepositoryObjectType.BUSINESS_PROCESS);
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
                if (repositoryNode.getObject() != null
                        && repositoryNode.getObject().getProperty().getItem().getState().isDeleted()) {
                    enabled = false;
                }
            }
        }

        if (ProxyRepositoryFactory.getInstance().isUserReadOnlyOnCurrentProject()) {
            enabled = false;
        }
        if (enabled) {
            enabled = ProjectManager.getInstance().isInCurrentMainProject(repositoryNode);
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
        if (isToolbar()) {

            return null;

        } else {
            if (repositoryNode != null) {
                if (repositoryNode.getType() == ENodeType.SIMPLE_FOLDER || repositoryNode.getType() == ENodeType.SYSTEM_FOLDER) {
                    path = RepositoryNodeUtilities.getPath(repositoryNode);
                } else {
                    path = new Path(""); //$NON-NLS-1$
                }
                return path;
            }
        }
        return null;

    }

    public IRepositoryView getRepositoryView() {
        IViewPart findView = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(
                IRepositoryView.VIEW_ID);
        return (IRepositoryView) findView;
    }

    public RepositoryNode getProcessNode() {
        List<IRepositoryNode> children = getRepositoryView().getRoot().getChildren();
        for (IRepositoryNode repositoryNode : children) {
            if (repositoryNode.getContentType() == ERepositoryObjectType.BUSINESS_PROCESS) {
                return (RepositoryNode) repositoryNode;
            }

        }

        return null;
    }

    /*
     * added by wchen for intro
     * 
     * @see org.eclipse.ui.intro.config.IIntroAction#run(org.eclipse.ui.intro.IIntroSite, java.util.Properties)
     */
    public void run(IIntroSite site, Properties params) {
        PlatformUI.getWorkbench().getIntroManager().closeIntro(PlatformUI.getWorkbench().getIntroManager().getIntro());
        setRepositoryNode(params);
        doRun();
    }

    private void setRepositoryNode(Properties params) {

        try {
            IViewPart findView = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(RepositoryView.ID);
            if (findView == null) {
                findView = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(RepositoryView.ID);
            }
            RepositoryView view = (RepositoryView) findView;

            Object type = params.get("type");
            if (ERepositoryObjectType.BUSINESS_PROCESS.name().equals(type)) {
                RepositoryNode processNode = ((ProjectRepositoryNode) view.getRoot()).getProcessNode();
                view.getViewer().expandToLevel(processNode, 1);
                this.repositoryNode = processNode;
            }
        } catch (PartInitException e) {
            ExceptionHandler.process(e);
        }
    }
}
