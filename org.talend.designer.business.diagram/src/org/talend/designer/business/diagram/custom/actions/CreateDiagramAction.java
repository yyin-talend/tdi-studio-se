package org.talend.designer.business.diagram.custom.actions;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.ui.images.ECoreImage;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNodeUtilities;
import org.talend.repository.model.RepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryNode.EProperties;
import org.talend.repository.ui.actions.AContextualAction;

/**
 * DOC mhelleboid class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class CreateDiagramAction extends AContextualAction {

    private RepositoryNode repositoryNode;

    public CreateDiagramAction() {
        super();
        setImageDescriptor(ImageProvider.getImageDesc(ECoreImage.BUSINESS_PROCESS_ICON));
        setText("Create Business Model");
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
        // PTODO MHE refactor AContextualAction

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
        if (repositoryNode.getType() == ENodeType.SIMPLE_FOLDER || repositoryNode.getType() == ENodeType.SYSTEM_FOLDER) {
            path = RepositoryNodeUtilities.getPath(repositoryNode);
        } else {
            path = new Path("");
        }
        return path;
    }
}
