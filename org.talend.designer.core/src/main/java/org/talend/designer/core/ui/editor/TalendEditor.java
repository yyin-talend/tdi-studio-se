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
package org.talend.designer.core.ui.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.Cursors;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.SimpleRaisedBorder;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.parts.ScrollableThumbnail;
import org.eclipse.draw2d.parts.Thumbnail;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.KeyStroke;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.palette.PaletteViewerProvider;
import org.eclipse.gef.ui.palette.FlyoutPaletteComposite.FlyoutPreferences;
import org.eclipse.gef.ui.parts.ContentOutlinePage;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.gef.ui.parts.TreeViewer;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.talend.core.model.components.ComponentUtilities;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.INodeConnector;
import org.talend.designer.core.DesignerPlugin;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.ui.MultiPageTalendEditor;
import org.talend.designer.core.ui.action.TalendConnectionCreationTool;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.nodes.NodePart;
import org.talend.designer.core.ui.editor.outline.NodeTreeEditPart;
import org.talend.designer.core.ui.editor.outline.ProcessTreePartFactory;
import org.talend.designer.core.ui.editor.palette.TalendPaletteViewerProvider;
import org.talend.designer.core.ui.editor.process.Process;
import org.talend.designer.core.ui.editor.process.ProcessPart;
import org.talend.designer.core.ui.views.properties.ComponentSettingsView;
import org.talend.repository.job.deletion.JobResource;
import org.talend.repository.job.deletion.JobResourceManager;
import org.talend.repository.model.ComponentsFactoryProvider;

/**
 * Main class of the Gef Editor. <br/>
 * 
 * $Id$
 * 
 */
public class TalendEditor extends AbstractTalendEditor {

    private KeyHandler sharedKeyHandler;

    private OutlinePage outlinePage;

    public TalendEditor() {
        this(false);

    }

    public TalendEditor(boolean readOnly) {
        super(readOnly);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.AbstractTalendEditor#getTalendEditDomain()
     */
    @Override
    protected DefaultEditDomain getTalendEditDomain() {
        return new TalendEditDomain(this);
    }

    public Process getProcess() {
        return (Process) this.process;
    }

    public KeyHandler getCommonKeyHandler() {
        if (sharedKeyHandler == null) {
            sharedKeyHandler = new KeyHandler();
            sharedKeyHandler.put(KeyStroke.getPressed(SWT.F1, 0), new Action() {

                @Override
                public void run() {
                    ISelection selection = getGraphicalViewer().getSelection();
                    if (selection instanceof IStructuredSelection) {

                        Object input = ((IStructuredSelection) selection).getFirstElement();
                        Node node = null;
                        if (input instanceof NodeTreeEditPart) {
                            NodeTreeEditPart nTreePart = (NodeTreeEditPart) input;
                            node = (Node) nTreePart.getModel();
                        } else {
                            if (input instanceof NodePart) {
                                EditPart editPart = (EditPart) input;
                                node = (Node) editPart.getModel();
                            }
                        }
                        if (node != null) {
                            String helpLink = (String) node.getPropertyValue(EParameterName.HELP.getName());
                            PlatformUI.getWorkbench().getHelpSystem().displayHelp(helpLink);
                        }
                    }
                }
            });
            sharedKeyHandler.put(KeyStroke.getPressed(SWT.DEL, 0), getActionRegistry().getAction(ActionFactory.DELETE.getId()));
            // deactivate the F2 shortcut as it's not used anymore
            // sharedKeyHandler.put(KeyStroke.getPressed(SWT.F2, 0),
            // getActionRegistry().getAction(GEFActionConstants.DIRECT_EDIT));
        }
        return sharedKeyHandler;
    }

    @Override
    public Object getAdapter(final Class type) {

        // this will select the TabbedPropertyPage instead of the classic property view
        // available in Eclipse 3.2
        if (type == IPropertySheetPage.class) {
            IPropertySheetPage properties = new TalendTabbedPropertySheetPage(this);
            return properties;
        }

        if (type == IContentOutlinePage.class) {
            outlinePage = new OutlinePage(new TreeViewer());
            return outlinePage;
        }
        if (type == ZoomManager.class) {
            return getGraphicalViewer().getProperty(ZoomManager.class.toString());
        }

        return super.getAdapter(type);
    }

    // ------------------------------------------------------------------------
    // Overridden from EditorPart

    @Override
    protected void setInput(final IEditorInput input) {
        super.setInput(input);

        try {
            if (input instanceof ProcessEditorInput) {
                process = ((ProcessEditorInput) input).getLoadedProcess();
                property = ((ProcessEditorInput) input).getItem().getProperty();
            }
        } catch (Exception e) { // if there's an error, create a new diagram
            e.printStackTrace();
            process = new Process();
        }
        currentJobResource.setJobName(process.getLabel());
        currentJobResource.setProjectName(projectName);

        JobResourceManager.getInstance().addProtection(this);

    }

    @Override
    public void doSaveAs() {
        SaveAsDialog dialog = new SaveAsDialog(getSite().getWorkbenchWindow().getShell());
        dialog.setOriginalFile(((IFileEditorInput) getEditorInput()).getFile());
        dialog.open();
        IPath path = dialog.getResult();

        if (path == null) {
            return;
        }

        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        final IFile file = workspace.getRoot().getFile(path);

        WorkspaceModifyOperation op = new WorkspaceModifyOperation() {

            @Override
            public void execute(final IProgressMonitor monitor) throws CoreException {
                try {
                    process.saveXmlFile(file);
                    file.refreshLocal(IResource.DEPTH_ONE, monitor);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        try {
            new ProgressMonitorDialog(getSite().getWorkbenchWindow().getShell()).run(false, true, op);
            // setInput(new FileEditorInput((IFile) file));
            getCommandStack().markSaveLocation();
            setDirty(false);

            savePreviewPictures();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected FlyoutPreferences getPalettePreferences() {
        return TalendEditorPaletteFactory.createPalettePreferences();
    }

    @Override
    protected PaletteRoot getPaletteRoot() {
        if (paletteRoot == null || ComponentUtilities.isComponentPaletteNeedRefresh) {

            /** * Components Factory ** */
            components = ComponentsFactoryProvider.getInstance();
            paletteRoot = TalendEditorPaletteFactory.createPalette(components);

            ComponentUtilities.isComponentPaletteNeedRefresh = false;
        }
        return paletteRoot;
    }

    public static void resetPaletteRoot() {
        paletteRoot = null;
    }

    /**
     * Outline view page. <br/>
     * 
     * $Id$
     * 
     */
    class OutlinePage extends ContentOutlinePage implements IAdaptable {

        private PageBook pageBook;

        private Control outline;

        private Canvas overview;

        private IAction showOutlineAction, showOverviewAction;

        static final int ID_OUTLINE = 0;

        static final int ID_OVERVIEW = 1;

        static final int BORDER_SIZE = 3;

        private Thumbnail thumbnail;

        private DisposeListener disposeListener;

        public OutlinePage(EditPartViewer viewer) {
            super(viewer);
        }

        @Override
        public void init(final IPageSite pageSite) {
            super.init(pageSite);
            ActionRegistry registry = getActionRegistry();
            IActionBars bars = pageSite.getActionBars();
            String id = ActionFactory.UNDO.getId();
            bars.setGlobalActionHandler(id, registry.getAction(id));
            id = ActionFactory.REDO.getId();
            bars.setGlobalActionHandler(id, registry.getAction(id));
            id = ActionFactory.DELETE.getId();
            bars.setGlobalActionHandler(id, registry.getAction(id));
            bars.updateActionBars();
        }

        protected void configureOutlineViewer() {
            getViewer().setEditDomain(getEditDomain());
            getViewer().setEditPartFactory(new ProcessTreePartFactory());
            getViewer().setKeyHandler(getCommonKeyHandler());
            IToolBarManager tbm = getSite().getActionBars().getToolBarManager();
            showOutlineAction = new Action() {

                @Override
                public void run() {
                    showPage(ID_OUTLINE);
                }
            };
            showOutlineAction.setImageDescriptor(ImageDescriptor.createFromFile(DesignerPlugin.class, "/icons/outline.gif")); //$NON-NLS-1$
            tbm.add(showOutlineAction);
            showOverviewAction = new Action() {

                @Override
                public void run() {
                    showPage(ID_OVERVIEW);
                }
            };
            showOverviewAction.setImageDescriptor(ImageDescriptor.createFromFile(DesignerPlugin.class, "/icons/overview.gif")); //$NON-NLS-1$
            tbm.add(showOverviewAction);
            showPage(ID_OUTLINE);
        }

        @Override
        public void createControl(final Composite parent) {

            pageBook = new PageBook(parent, SWT.NONE);
            outline = getViewer().createControl(pageBook);
            overview = new Canvas(pageBook, SWT.NONE);
            pageBook.showPage(outline);
            configureOutlineViewer();
            hookOutlineViewer();
            initializeOutlineViewer();
        }

        @Override
        public void dispose() {
            unhookOutlineViewer();
            if (thumbnail != null) {
                thumbnail.deactivate();
                thumbnail = null;
            }
            super.dispose();
            TalendEditor.this.outlinePage = null;
            outlinePage = null;
            // if (AbstractTalendEditor.this.fActivationCodeTrigger != null) {
            // fActivationCodeTrigger.uninstall();
            // fActivationCodeTrigger = null;
            // }
        }

        public Object getAdapter(final Class type) {
            if (type == ZoomManager.class) {
                return getGraphicalViewer().getProperty(ZoomManager.class.toString());
            }
            return null;
        }

        @Override
        public Control getControl() {
            return pageBook;
        }

        protected void hookOutlineViewer() {
            getSelectionSynchronizer().addViewer(getViewer());
        }

        protected void initializeOutlineViewer() {
            setContents(getProcess());
        }

        protected void initializeOverview() {
            LightweightSystem lws = new LightweightSystem(overview);
            RootEditPart rep = getGraphicalViewer().getRootEditPart();
            if (rep instanceof ScalableFreeformRootEditPart) {
                ScalableFreeformRootEditPart root = (ScalableFreeformRootEditPart) rep;
                thumbnail = new ScrollableThumbnail((Viewport) root.getFigure());
                thumbnail.setBorder(new SimpleRaisedBorder());
                thumbnail.setSource(root.getLayer(LayerConstants.PRINTABLE_LAYERS));
                lws.setContents(thumbnail);
                disposeListener = new DisposeListener() {

                    public void widgetDisposed(final DisposeEvent e) {
                        if (thumbnail != null) {
                            thumbnail.deactivate();
                            thumbnail = null;
                        }
                    }
                };
                getEditor().addDisposeListener(disposeListener);
            }
        }

        public void setContents(final Object contents) {
            getViewer().setContents(contents);
        }

        protected void showPage(final int id) {
            if (id == ID_OUTLINE) {
                showOutlineAction.setChecked(true);
                showOverviewAction.setChecked(false);
                pageBook.showPage(outline);
                if (thumbnail != null) {
                    thumbnail.setVisible(true);
                }
            } else if (id == ID_OVERVIEW) {
                if (thumbnail == null) {
                    initializeOverview();
                }
                showOutlineAction.setChecked(false);
                showOverviewAction.setChecked(true);
                pageBook.showPage(overview);
                thumbnail.setVisible(true);
            }
        }

        protected void unhookOutlineViewer() {
            getSelectionSynchronizer().removeViewer(getViewer());
            if (disposeListener != null && getEditor() != null && !getEditor().isDisposed()) {
                getEditor().removeDisposeListener(disposeListener);
            }
        }
    }

    @Override
    public void dispose() {
        if (!getParent().isKeepPropertyLocked()) {
            JobResourceManager manager = JobResourceManager.getInstance();
            manager.removeProtection(this);
            for (JobResource r : protectedJobs.values()) {
                manager.deleteResource(r);
            }
        }
        ComponentSettingsView viewer = (ComponentSettingsView) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
                .getActivePage().findView(ComponentSettingsView.ID);
        if (viewer != null) {
            viewer.cleanDisplay();
        }
        super.dispose();
    }

    @Override
    protected PaletteViewerProvider createPaletteViewerProvider() {
        return new TalendPaletteViewerProvider(getEditDomain());
    }

    public void savePaletteState() {
        PaletteViewer paletteViewer = getPaletteViewerProvider().getEditDomain().getPaletteViewer();
        if (paletteViewer != null) {
            TalendEditorPaletteFactory.saveFamilyState(paletteViewer);
        }
    }

    /**
     * bqian class global comment. Detailled comment <br/>
     * 
     * $Id$
     * 
     */
    class TalendEditDomain extends DefaultEditDomain {

        /**
         * DOC bqian TalendEditor.TalendEditDomain class global comment. Detailled comment <br/>
         * 
         * $Id$
         * 
         */
        class DragProcessor {

            int x;

            int y;

        }

        private DragProcessor processor = null;

        private boolean createConnection = false;

        private Point startPoint = null;

        /**
         * bqian TalendEditDomain constructor comment.
         * 
         * @param editorPart
         */
        public TalendEditDomain(IEditorPart editorPart) {
            super(editorPart);
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.gef.EditDomain#mouseDown(org.eclipse.swt.events.MouseEvent, org.eclipse.gef.EditPartViewer)
         */
        @Override
        public void mouseDown(org.eclipse.swt.events.MouseEvent mouseEvent, EditPartViewer viewer) {
            TalendEditorContextMenuProvider.setEnableContextMenu(true);
            createConnection = false;
            if (mouseEvent.button == 2) {
                getEditor().setCursor(Cursors.HAND);
                processor = new DragProcessor();
                processor.x = mouseEvent.x;
                processor.y = mouseEvent.y;
            } else if (mouseEvent.button == 3) {
                startPoint = new Point(mouseEvent.x, mouseEvent.y);
                createConnection = true;
                super.mouseDown(mouseEvent, viewer);
            } else {
                super.mouseDown(mouseEvent, viewer);
            }
        }

        public void updateViewport(int offX, int offY) {
            RootEditPart rep = getGraphicalViewer().getRootEditPart();

            if (rep instanceof ScalableFreeformRootEditPart) {
                ScalableFreeformRootEditPart root = (ScalableFreeformRootEditPart) rep;
                Viewport viewport = (Viewport) root.getFigure();
                Point viewOriginalPosition = viewport.getViewLocation();
                viewOriginalPosition.x -= offX;
                viewOriginalPosition.y -= offY;

                viewport.setViewLocation(viewOriginalPosition.x, viewOriginalPosition.y);
            }
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.gef.EditDomain#mouseDrag(org.eclipse.swt.events.MouseEvent, org.eclipse.gef.EditPartViewer)
         */
        @Override
        public void mouseDrag(org.eclipse.swt.events.MouseEvent mouseEvent, EditPartViewer viewer) {
            super.mouseDrag(mouseEvent, viewer);
            if (processor != null) {
                int offX = mouseEvent.x - processor.x;
                int offY = mouseEvent.y - processor.y;

                updateViewport(offX, offY);
                processor.x = mouseEvent.x;
                processor.y = mouseEvent.y;
            } else if (createConnection) {
                handleCreateDefaultConnection(new Point(mouseEvent.x, mouseEvent.y));
            }
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.gef.EditDomain#mouseUp(org.eclipse.swt.events.MouseEvent, org.eclipse.gef.EditPartViewer)
         */
        @Override
        public void mouseUp(org.eclipse.swt.events.MouseEvent mouseEvent, EditPartViewer viewer) {
            createConnection = false;
            if (mouseEvent.button != 2) {
                super.mouseUp(mouseEvent, viewer);
            } else {
                getEditor().setCursor(null);
                processor = null;
            }
        }

        public void handleCreateDefaultConnection(Point currentLocation) {
            if (getActiveTool() instanceof TalendConnectionCreationTool) {
                // if a connection is already in creation, no need to create it again
                return;
            }

            if (getViewer().getSelectedEditParts().size() != 1) {
                return;
            }

            EditPart part = (EditPart) getViewer().getSelectedEditParts().get(0);
            if (!(part instanceof NodePart)) {
                return;
            }

            IFigure figure = ((NodePart) part).getFigure();

            if (!figure.getBounds().contains(startPoint)) {
                // if the start location of the drag is not in the component, ignore it
                return;
            }

            if (startPoint.getDistance2(currentLocation) < 30) {
                // need to move a minimum the mouse for the drag, if not enough, display the context menu
                return;
            }

            Node node = (Node) part.getModel();

            final INodeConnector mainConnector;
            if (node.isELTComponent()) {
                mainConnector = node.getConnectorFromType(EConnectionType.TABLE);
            } else {
                mainConnector = node.getConnectorFromType(EConnectionType.FLOW_MAIN);
            }

            if (mainConnector == null) {
                return;
            }
            if (mainConnector.getMaxLinkOutput() != -1) {
                if (mainConnector.getCurLinkNbOutput() >= mainConnector.getMaxLinkOutput()) {
                    return;
                }
            }
            if (mainConnector.getMaxLinkOutput() == 0) {
                return;
            }

            figure.translateToAbsolute(startPoint);

            Canvas canvas = (Canvas) getViewer().getControl();
            Event event = new Event();
            event.button = 3;
            event.count = 0;
            event.detail = 0;
            event.end = 0;
            event.height = 0;
            event.keyCode = 0;
            event.start = 0;
            event.stateMask = 0;
            event.time = 9516624; // any old time... doesn't matter
            event.type = 3;
            event.widget = canvas;
            event.width = 0;
            event.x = startPoint.x + 3;
            event.y = startPoint.y + 3;
            /**
             * Set the connection tool to be the current tool
             */

            final List<Object> listArgs = new ArrayList<Object>();

            listArgs.add(null);
            listArgs.add(null);
            listArgs.add(null);

            TalendConnectionCreationTool myConnectTool = new TalendConnectionCreationTool(new CreationFactory() {

                public Object getNewObject() {
                    return listArgs;
                }

                public Object getObjectType() {
                    return mainConnector.getName();
                }
            }, false);
            myConnectTool.performConnectionStartWith(part);
            getViewer().getEditDomain().setActiveTool(myConnectTool);
            canvas.notifyListeners(3, event);
            TalendEditorContextMenuProvider.setEnableContextMenu(false);
        }

    }

    public ProcessPart getProcessPart() {
        RootEditPart rootEditPart = ((ScrollingGraphicalViewer) getGraphicalViewer()).getRootEditPart();
        return (ProcessPart) rootEditPart.getChildren().get(0);
    }

    public void setParent(MultiPageTalendEditor multiPageTalendEditor) {
        super.setParent(multiPageTalendEditor);
    }

    public MultiPageTalendEditor getParent() {
        return (MultiPageTalendEditor) super.getParent();
    }

}
