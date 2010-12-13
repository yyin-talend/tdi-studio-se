package org.talend.designer.business.diagram.custom.actions;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.impl.EdgeImpl;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.ui.runtime.image.EImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.core.CorePlugin;
import org.talend.designer.business.diagram.custom.commands.DeleteAssignmentCommand;
import org.talend.designer.business.diagram.custom.edit.parts.BaseBusinessItemRelationShipEditPart;
import org.talend.designer.business.diagram.custom.edit.parts.BusinessItemShapeEditPart;
import org.talend.designer.business.diagram.i18n.Messages;
import org.talend.designer.business.model.business.BusinessItem;
import org.talend.designer.business.model.business.diagram.part.BusinessDiagramEditor;
import org.talend.repository.ui.actions.AContextualAction;

public class DeleteAssignmentAction extends AContextualAction {

    private ISelection selection;

    public DeleteAssignmentAction(ISelection selection) {
        super();
        this.setImageDescriptor(ImageProvider.getImageDesc(EImage.DELETE_ICON));
        setText(Messages.getString("DeleteAssignmentAction.DeleteAssignment")); //$NON-NLS-1$
        this.selection = selection;
    }

    @Override
    protected void doRun() {

        IEditorPart activeEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        if (activeEditor instanceof BusinessDiagramEditor) {
            BusinessDiagramEditor editor = (BusinessDiagramEditor) activeEditor;
            ISelection selection2 = editor.getSelection();

            EObject element = null;
            Object firstElement = ((IStructuredSelection) selection2).getFirstElement();
            if (firstElement instanceof BusinessItemShapeEditPart) {
                BusinessItemShapeEditPart editpart = (BusinessItemShapeEditPart) firstElement;
                element = ((Node) editpart.getModel()).getElement();
            } else if (firstElement instanceof BaseBusinessItemRelationShipEditPart) {
                BaseBusinessItemRelationShipEditPart editpart = (BaseBusinessItemRelationShipEditPart) firstElement;
                element = ((EdgeImpl) editpart.getModel()).getElement();
            }
            if (element instanceof BusinessItem) {
                BusinessItem businessItem = (BusinessItem) element;
                DeleteAssignmentCommand command = new DeleteAssignmentCommand(businessItem, selection);
                try {
                    command.execute(null, null);
                } catch (ExecutionException e) {
                    ExceptionHandler.process(e);
                }
                IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
                for (IEditorReference editors : page.getEditorReferences()) {
                    CorePlugin.getDefault().getDiagramModelService().refreshBusinessModel(editors);
                }
            }
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.swt.actions.ITreeContextualAction#init(org.eclipse.jface.viewers.TreeViewer,
     * org.eclipse.jface.viewers.IStructuredSelection)
     */
    public void init(TreeViewer viewer, IStructuredSelection selection) {
        setEnabled(true);
    }

}
