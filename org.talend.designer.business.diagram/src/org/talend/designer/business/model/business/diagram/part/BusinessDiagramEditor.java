package org.talend.designer.business.model.business.diagram.part;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.DelegatingLayout;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayeredPane;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.ui.actions.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.editparts.CompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramRootEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.NoteEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.editparts.NoteAttachmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramGraphicalViewer;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.document.StorageDiagramDocumentProvider;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.editor.FileDiagramEditor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IGotoMarker;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.properties.BusinessProcessItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.RepositoryManager;
import org.talend.designer.business.diagram.custom.actions.DiagramResourceManager;
import org.talend.designer.business.diagram.custom.dnd.BusinessDiagramDropTargetListener;
import org.talend.designer.business.diagram.custom.edit.parts.BaseBusinessItemRelationShipEditPart;
import org.talend.designer.business.diagram.custom.edit.parts.BusinessItemShapeEditPart;
import org.talend.designer.business.diagram.custom.figures.BusinessItemShapeFigure;
import org.talend.designer.business.model.business.diagram.edit.parts.BusinessEditPartFactory;
import org.talend.designer.business.model.business.diagram.edit.parts.BusinessProcessEditPart;
import org.talend.designer.business.model.business.diagram.providers.BusinessDiagramActionProvider;
import org.talend.designer.core.ui.ActiveProcessTracker;
import org.talend.designer.core.ui.views.jobsettings.JobSettingsView;
import org.talend.repository.editor.RepositoryEditorInput;
import org.talend.repository.model.ERepositoryStatus;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;

/**
 * @generated
 */
public class BusinessDiagramEditor extends FileDiagramEditor implements IGotoMarker {

    /**
     * @generated
     */
    public static final String ID = "org.talend.designer.business.model.business.diagram.part.BusinessDiagramEditorID"; //$NON-NLS-1$

    /**
     * @generated
     */
    public BusinessDiagramEditor() {
        super(true);
        ActiveProcessTracker.initialize();
    }

    /**
     * @generated
     */
    protected String getEditingDomainID() {
        return "org.talend.designer.business.diagram.EditingDomain"; //$NON-NLS-1$
    }

    /**
     * @generated
     */
    protected TransactionalEditingDomain createEditingDomain() {
        TransactionalEditingDomain domain = super.createEditingDomain();
        domain.setID(getEditingDomainID());
        return domain;
    }

    /**
     * @generated
     */
    protected void setDocumentProvider(IEditorInput input) {
        if (input.getAdapter(IFile.class) != null) {
            setDocumentProvider(new BusinessDocumentProvider());
        } else {
            setDocumentProvider(new StorageDiagramDocumentProvider());
        }
    }

    /**
     * @generated
     */
    protected void configureGraphicalViewer() {
        super.configureGraphicalViewer();

        IDiagramGraphicalViewer viewer = getDiagramGraphicalViewer();
        /* customize popup menu */
        ContextMenuProvider provider = new BusinessDiagramActionProvider(this, viewer);
        viewer.setContextMenu(provider);
        getSite().registerContextMenu(ActionIds.DIAGRAM_EDITOR_CONTEXT_MENU, provider, viewer);

        DiagramRootEditPart root = (DiagramRootEditPart) getDiagramGraphicalViewer().getRootEditPart();
        LayeredPane printableLayers = (LayeredPane) root.getLayer(LayerConstants.PRINTABLE_LAYERS);
        FreeformLayer extLabelsLayer = new FreeformLayer();
        extLabelsLayer.setLayoutManager(new DelegatingLayout());
        printableLayers.addLayerAfter(extLabelsLayer, BusinessEditPartFactory.EXTERNAL_NODE_LABELS_LAYER,
                LayerConstants.PRIMARY_LAYER);
        LayeredPane scalableLayers = (LayeredPane) root.getLayer(LayerConstants.SCALABLE_LAYERS);
        FreeformLayer scaledFeedbackLayer = new FreeformLayer();
        scaledFeedbackLayer.setEnabled(false);
        scalableLayers.addLayerAfter(scaledFeedbackLayer, LayerConstants.SCALED_FEEDBACK_LAYER,
                DiagramRootEditPart.DECORATION_UNPRINTABLE_LAYER);
    }

    /**
     * @generated
     */
    protected PreferencesHint getPreferencesHint() {
        return BusinessDiagramEditorPlugin.DIAGRAM_PREFERENCES_HINT;
    }

    protected void initializeGraphicalViewer() {
        super.initializeGraphicalViewer();
        getDiagramGraphicalViewer().addDropTargetListener(new BusinessDiagramDropTargetListener(getDiagramGraphicalViewer()));
    }

    RepositoryEditorInput repositoryEditorInput;

    public void setInput(IEditorInput input) {
        super.setInput(input);
        if (input instanceof RepositoryEditorInput) {
            repositoryEditorInput = (RepositoryEditorInput) input;
        }
    }

    public void doSave(IProgressMonitor progressMonitor) {
        super.doSave(progressMonitor);
        if (repositoryEditorInput != null) {
            DiagramResourceManager diagramResourceManager = new DiagramResourceManager();
            BusinessProcessItem businessProcessItem = (BusinessProcessItem) repositoryEditorInput.getItem();
            diagramResourceManager.updateFromResource(businessProcessItem, repositoryEditorInput.getFile());

            try {
                ProxyRepositoryFactory.getInstance().save(businessProcessItem);
            } catch (PersistenceException e) {
                e.printStackTrace();
            }
            propertyIsDirty = false;
            firePropertyChange(IEditorPart.PROP_DIRTY);
            RepositoryManager.refreshSavedNode(repositoryEditorInput.getRepositoryNode());

        }
    }

    public boolean isEditable() {
        if (repositoryEditorInput != null) {
            if (repositoryEditorInput.isReadOnly()) {
                return false;
            }

            Item item = repositoryEditorInput.getItem();
            IProxyRepositoryFactory repositoryFactory = ProxyRepositoryFactory.getInstance();

            return repositoryFactory.isEditableAndLockIfPossible(item);
        }
        return super.isEditable();
    }

    @Override
    public boolean isDirty() {
        return propertyIsDirty || super.isDirty();
    }

    public void dispose() {
        super.dispose();
        // Unlock the process :
        IProxyRepositoryFactory repFactory = ProxyRepositoryFactory.getInstance();
        try {
            Property property = repFactory.reload(repositoryEditorInput.getItem().getProperty());
            repositoryEditorInput.setItem(property.getItem());
            repFactory.unlock(repositoryEditorInput.getItem());
        } catch (PersistenceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        RepositoryNode repositoryNode = repositoryEditorInput.getRepositoryNode();
        if (repositoryNode != null) {
            if (repFactory.getStatus(repositoryEditorInput.getItem()) == ERepositoryStatus.DELETED) {
                RepositoryManager.refreshDeletedNode(null);
            } else {
                RepositoryManager.refresh(repositoryNode.getObjectType());
            }
        }
    }

    public void init(final IEditorSite site, final IEditorInput editorInput) throws PartInitException {
        super.init(site, editorInput);
        RepositoryEditorInput processEditorInput = (RepositoryEditorInput) editorInput;
        processEditorInput.getItem().getProperty().eAdapters().add(dirtyListener);
    }

    private boolean propertyIsDirty;

    private AdapterImpl dirtyListener = new AdapterImpl() {

        public void notifyChanged(Notification notification) {
            if (notification.getEventType() != Notification.REMOVING_ADAPTER) {
                propertyIsDirty = true;
                firePropertyChange(IEditorPart.PROP_DIRTY);
            }
        }
    };

    public void refresh() {
        DiagramEditPart diagramEditPart = this.getDiagramEditPart();
        List list = diagramEditPart.getChildren();
        for (Object o : list) {
            if (o instanceof BusinessItemShapeEditPart) {
                BusinessItemShapeEditPart element = (BusinessItemShapeEditPart) o;
                element.refresh();

            }
        }
    }

    public RepositoryEditorInput getDiagramEditorInput() {
        return this.repositoryEditorInput;

    }

    @Override
    public void selectionChanged(IWorkbenchPart part, ISelection selection) {
        super.selectionChanged(part, selection);
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        JobSettingsView view = (JobSettingsView) page.findView(JobSettingsView.ID);

        if (view == null) {
            return;
        }
        if (!(selection instanceof IStructuredSelection)) {
            return;
        }
        Object firstElement = ((IStructuredSelection) selection).getFirstElement();
        if (!(selection instanceof IStructuredSelection)) {
            return;
        } else if (firstElement instanceof RepositoryNode) {
            return;
        }

        // for Find Assignment
        if (((IStructuredSelection) selection).size() > 0) {
            DiagramEditPart diagramEditPart = this.getDiagramEditPart();
            if (diagramEditPart instanceof BusinessProcessEditPart) {
                BusinessProcessEditPart processPart = (BusinessProcessEditPart) diagramEditPart;
                for (Object object : processPart.getChildren()) {
                    if (object instanceof BusinessItemShapeEditPart) {
                        BusinessItemShapeEditPart shapEditPart = (BusinessItemShapeEditPart) object;
                        IFigure figure = shapEditPart.getFigure();
                        for (Object child : figure.getChildren()) {
                            if (child instanceof BusinessItemShapeFigure) {
                                BusinessItemShapeFigure shapFigure = (BusinessItemShapeFigure) child;
                                Border border = shapFigure.getBorder();
                                if (border != null) {
                                    shapFigure.setDrawFrame(false);
                                    shapFigure.revalidate();
                                    shapFigure.repaint();
                                }

                            }
                        }

                    }
                }
            }
        }

        // to refresh the jobsettings view
        if (((IStructuredSelection) selection).size() > 1) {
            view.cleanDisplay();
        } else {

            if (firstElement instanceof BusinessItemShapeEditPart || firstElement instanceof BaseBusinessItemRelationShipEditPart
                    || firstElement instanceof NoteEditPart || firstElement instanceof NoteAttachmentEditPart) {

                view.refresh(false, firstElement);
            } else if (firstElement instanceof BusinessProcessEditPart || firstElement instanceof CompartmentEditPart) {

                view.refresh(false, this);
            }

        }

    }

    public ISelection getSelection() {
        return this.getDiagramGraphicalViewer().getSelection();
    }
}
