// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2007 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
//
// ============================================================================
package org.talend.designer.core.ui.editor;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.draw2d.Cursors;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.draw2d.SimpleRaisedBorder;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.parts.ScrollableThumbnail;
import org.eclipse.draw2d.parts.Thumbnail;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.KeyStroke;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.MouseWheelHandler;
import org.eclipse.gef.MouseWheelZoomHandler;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.SnapToGeometry;
import org.eclipse.gef.SnapToGrid;
import org.eclipse.gef.editparts.LayerManager;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.DirectEditAction;
import org.eclipse.gef.ui.actions.ToggleGridAction;
import org.eclipse.gef.ui.actions.ToggleSnapToGeometryAction;
import org.eclipse.gef.ui.actions.ZoomInAction;
import org.eclipse.gef.ui.actions.ZoomOutAction;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.palette.PaletteViewerProvider;
import org.eclipse.gef.ui.palette.FlyoutPaletteComposite.FlyoutPreferences;
import org.eclipse.gef.ui.parts.ContentOutlinePage;
import org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.gef.ui.parts.TreeViewer;
import org.eclipse.gef.ui.rulers.RulerComposite;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.commands.ActionHandler;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IKeyBindingService;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;
import org.talend.commons.utils.workbench.preferences.GlobalConstant;
import org.talend.core.CorePlugin;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.components.IComponentsFactory;
import org.talend.core.model.general.ILibrariesService;
import org.talend.core.model.properties.Property;
import org.talend.designer.core.DesignerPlugin;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.ui.action.ConnectionSetAsMainRef;
import org.talend.designer.core.ui.action.GEFCopyAction;
import org.talend.designer.core.ui.action.GEFDeleteAction;
import org.talend.designer.core.ui.action.GEFPasteAction;
import org.talend.designer.core.ui.action.ModifyMergeOrderAction;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.nodes.NodePart;
import org.talend.designer.core.ui.editor.outline.NodeTreeEditPart;
import org.talend.designer.core.ui.editor.outline.ProcessTreePartFactory;
import org.talend.designer.core.ui.editor.palette.TalendPaletteViewerProvider;
import org.talend.designer.core.ui.editor.process.Process;
import org.talend.designer.core.ui.editor.process.ProcessPart;
import org.talend.designer.core.ui.editor.process.ProcessTemplateTransferDropTargetListener;
import org.talend.designer.core.ui.editor.process.TalendEditorDropTargetListener;
import org.talend.repository.job.deletion.IJobResourceProtection;
import org.talend.repository.job.deletion.JobResource;
import org.talend.repository.job.deletion.JobResourceManager;
import org.talend.repository.model.ComponentsFactoryProvider;
import org.talend.repository.model.IRepositoryService;
import org.talend.repository.model.RepositoryConstants;

/**
 * Main class of the Gef Editor. <br/>
 * 
 * $Id$
 * 
 */
public class TalendEditor extends GraphicalEditorWithFlyoutPalette implements ITabbedPropertySheetPageContributor,
        IJobResourceProtection {

    private boolean savePreviouslyNeeded;

    private static PaletteRoot paletteRoot;

    private Process process;

    private KeyHandler sharedKeyHandler;

    private OutlinePage outlinePage;

    private RulerComposite rulerComp;

    private IComponentsFactory components;

    private Property property;

    private boolean readOnly;

    public static final int GRID_SIZE = 32;

    private boolean dirtyState = false;

    private final JobResource currentJobResource;

    private final String projectName;

    private final Map<String, JobResource> protectedJobs;

    /** The verify key listener for activation code triggering. */
    private ActivationCodeTrigger fActivationCodeTrigger = new ActivationCodeTrigger();

    public TalendEditor() {
        this(false);
    }

    public TalendEditor(boolean readOnly) {

        // an EditDomain is a "session" of editing which contains things
        // like the CommandStack
        setEditDomain(new TalendEditDomain(this));
        this.readOnly = readOnly;

        projectName = ((RepositoryContext) CorePlugin.getContext().getProperty(Context.REPOSITORY_CONTEXT_KEY)).getProject()
                .getLabel();
        currentJobResource = new JobResource();
        protectedJobs = new HashMap<String, JobResource>();
        initializeKeyBindingScopes();
    }

    private String[] fKeyBindingScopes;

    /**
     * Sets the key binding scopes for this editor.
     * 
     * @param scopes a non-empty array of key binding scope identifiers
     * @since 2.1
     */
    protected void setKeyBindingScopes(String[] scopes) {
        Assert.isTrue(scopes != null && scopes.length > 0);
        fKeyBindingScopes = scopes;
    }

    /*
     * @see ITextEditor#setAction(String, IAction)
     */
    public void setAction(String actionID, IAction action) {
        Assert.isNotNull(actionID);
        if (action == null) {
            action = (IAction) getActionRegistry().getAction(actionID);
            if (action != null)
                fActivationCodeTrigger.unregisterActionFromKeyActivation(action);
        } else {
            // getActionRegistry().registerAction(action);
            fActivationCodeTrigger.registerActionForKeyActivation(action);
        }
    }

    /**
     * Initializes the key binding scopes of this editor.
     */
    protected void initializeKeyBindingScopes() {
        setKeyBindingScopes(new String[] { "org.eclipse.ui.textEditorScope" }); //$NON-NLS-1$
    }

    /**
     * Internal key verify listener for triggering action activation codes.
     */
    class ActivationCodeTrigger implements VerifyKeyListener {

        /** Indicates whether this trigger has been installed. */
        private boolean fIsInstalled = false;

        /**
         * The key binding service to use.
         * 
         * @since 2.0
         */
        private IKeyBindingService fKeyBindingService;

        /*
         * @see VerifyKeyListener#verifyKey(org.eclipse.swt.events.VerifyEvent)
         */
        public void verifyKey(VerifyEvent event) {

            // ActionActivationCode code = null;
            // int size = fActivationCodes.size();
            // for (int i = 0; i < size; i++) {
            // code = (ActionActivationCode) fActivationCodes.get(i);
            // if (code.matches(event)) {
            // IAction action = getAction(code.fActionId);
            // if (action != null) {
            //
            // if (action instanceof IUpdate)
            // ((IUpdate) action).update();
            //
            // if (!action.isEnabled() && action instanceof IReadOnlyDependent) {
            // IReadOnlyDependent dependent = (IReadOnlyDependent) action;
            // boolean writable = dependent.isEnabled(true);
            // if (writable) {
            // event.doit = false;
            // return;
            // }
            // } else if (action.isEnabled()) {
            // event.doit = false;
            // action.run();
            // return;
            // }
            // }
            // }
            // }
        }

        /**
         * Installs this trigger on the editor's text widget.
         * 
         * @since 2.0
         */
        public void install() {
            if (!fIsInstalled) {

                // if (fSourceViewer instanceof ITextViewerExtension) {
                // ITextViewerExtension e = (ITextViewerExtension) fSourceViewer;
                // e.prependVerifyKeyListener(this);
                // } else {
                // StyledText text = fSourceViewer.getTextWidget();
                // text.addVerifyKeyListener(this);
                // }

                fKeyBindingService = getEditorSite().getKeyBindingService();
                fIsInstalled = true;
            }
        }

        /**
         * Uninstalls this trigger from the editor's text widget.
         * 
         * @since 2.0
         */
        public void uninstall() {
            if (fIsInstalled) {

                // if (fSourceViewer instanceof ITextViewerExtension) {
                // ITextViewerExtension e = (ITextViewerExtension) fSourceViewer;
                // e.removeVerifyKeyListener(this);
                // } else if (fSourceViewer != null) {
                // StyledText text = fSourceViewer.getTextWidget();
                // if (text != null && !text.isDisposed())
                // text.removeVerifyKeyListener(fActivationCodeTrigger);
                // }

                fIsInstalled = false;
                fKeyBindingService = null;
            }
        }

        /**
         * Registers the given action for key activation.
         * 
         * @param action the action to be registered
         * @since 2.0
         */
        public void registerActionForKeyActivation(IAction action) {
            if (action.getActionDefinitionId() != null)
                fKeyBindingService.registerAction(action);
        }

        /**
         * The given action is no longer available for key activation
         * 
         * @param action the action to be unregistered
         * @since 2.0
         */
        public void unregisterActionFromKeyActivation(IAction action) {
            if (action.getActionDefinitionId() != null)
                fKeyBindingService.unregisterAction(action);
        }

        /**
         * Sets the key binding scopes for this editor.
         * 
         * @param keyBindingScopes the key binding scopes
         * @since 2.1
         */
        public void setScopes(String[] keyBindingScopes) {
            if (keyBindingScopes != null && keyBindingScopes.length > 0)
                fKeyBindingService.setScopes(keyBindingScopes);
        }
    }

    /**
     * Representation of action activation codes.
     */
    static class ActionActivationCode {

        /** The action id. */
        public String fActionId;

        /** The character. */
        public char fCharacter;

        /** The key code. */
        public int fKeyCode = -1;

        /** The state mask. */
        public int fStateMask = SWT.DEFAULT;

        /**
         * Creates a new action activation code for the given action id.
         * 
         * @param actionId the action id
         */
        public ActionActivationCode(String actionId) {
            fActionId = actionId;
        }

        /**
         * Returns <code>true</code> if this activation code matches the given verify event.
         * 
         * @param event the event to test for matching
         * @return whether this activation code matches <code>event</code>
         */
        public boolean matches(VerifyEvent event) {
            return (event.character == fCharacter && (fKeyCode == -1 || event.keyCode == fKeyCode) && (fStateMask == SWT.DEFAULT || event.stateMask == fStateMask));
        }
    }

    /**
     * Initializes the activation code trigger.
     * 
     * @since 2.1
     */
    private void initializeActivationCodeTrigger() {
        fActivationCodeTrigger.install();
        fActivationCodeTrigger.setScopes(fKeyBindingScopes);
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public Process getProcess() {
        return this.process;
    }

    public Property getProperty() {
        return this.property;
    }

    /**
     * Sets the property.
     * 
     * @param property the property to set
     */
    public void setProperty(Property property) {
        this.property = property;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.ui.parts.GraphicalEditor#setFocus()
     */
    @Override
    public void setFocus() {
        super.setFocus();

        if (!readOnly) {
            // When gain focus, check read-only to disable read-only mode if process has been restore while opened :
            // 1. Enabled/disabled components :
            process.checkReadOnly();
            // 2. Set backgroung color :
            ProcessPart rep = (ProcessPart) getViewer().getRootEditPart().getChildren().get(0);
            rep.ajustReadOnly();
        }
    }

    protected KeyHandler getCommonKeyHandler() {
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

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    public List<String> getActions() {
        return getSelectionActions();
    }

    // ------------------------------------------------------------------------
    // Overridden from GraphicalEditor

    @Override
    @SuppressWarnings("unchecked")//$NON-NLS-1$
    protected void configureGraphicalViewer() {
        super.configureGraphicalViewer();
        /** * Manage the view in the Outline ** */
        ScrollingGraphicalViewer viewer = (ScrollingGraphicalViewer) getGraphicalViewer();
        viewer.setSelectionManager(new TalendSelectionManager());

        TalendScalableFreeformRootEditPart root = new TalendScalableFreeformRootEditPart(getEditorInput());

        List<String> zoomLevels = new ArrayList<String>();
        zoomLevels.add(ZoomManager.FIT_ALL);
        zoomLevels.add(ZoomManager.FIT_WIDTH);
        zoomLevels.add(ZoomManager.FIT_HEIGHT);
        root.getZoomManager().setZoomLevelContributions(zoomLevels);
        // root.getZoomManager().setZoomAnimationStyle(ZoomManager.ANIMATE_NEVER);

        IAction zoomIn = new ZoomInAction(root.getZoomManager());
        IAction zoomOut = new ZoomOutAction(root.getZoomManager());
        getActionRegistry().registerAction(zoomIn);
        getActionRegistry().registerAction(zoomOut);
        IHandlerService service = (IHandlerService) getEditorSite().getService(IHandlerService.class);
        service.activateHandler(zoomIn.getActionDefinitionId(), new ActionHandler(zoomIn));

        service.activateHandler(zoomOut.getActionDefinitionId(), new ActionHandler(zoomOut));

        IAction directEditAction = new DirectEditAction(this);
        getActionRegistry().registerAction(directEditAction);
        getSelectionActions().add(directEditAction.getId());

        IAction copyAction = new GEFCopyAction(this);
        getActionRegistry().registerAction(copyAction);
        getSelectionActions().add(copyAction.getId());
        // setAction(copyAction.getId(), copyAction);

        IAction pasteAction = new GEFPasteAction(this);
        getActionRegistry().registerAction(pasteAction);
        getSelectionActions().add(pasteAction.getId());
        // setAction(pasteAction.getId(), pasteAction);

        IAction deleteAction = new GEFDeleteAction(this);
        getActionRegistry().registerAction(deleteAction);
        getSelectionActions().add(deleteAction.getId());
        // setAction(deleteAction.getId(), deleteAction);

        IAction setRefAction = new ConnectionSetAsMainRef(this);
        getActionRegistry().registerAction(setRefAction);
        getSelectionActions().add(setRefAction.getId());

        IAction modifyMergeAction = new ModifyMergeOrderAction(this);
        getActionRegistry().registerAction(modifyMergeAction);
        getSelectionActions().add(modifyMergeAction.getId());

        viewer.setRootEditPart(root);

        PartFactory partFactory = new PartFactory();
        // set the factory to use for creating EditParts for elements in the model
        getGraphicalViewer().setEditPartFactory(partFactory);
        getGraphicalViewer().setKeyHandler(new GraphicalViewerKeyHandler(getGraphicalViewer()).setParent(getCommonKeyHandler()));

        initializeActivationCodeTrigger();

        /** * Management of the context menu ** */

        ContextMenuProvider cmProvider = new TalendEditorContextMenuProvider(this, viewer, getActionRegistry());
        viewer.setContextMenu(cmProvider);

        /** * Management of the Zoom ** */
        /*
         * ZoomManager manager = (ZoomManager) getGraphicalViewer().getProperty(ZoomManager.class.toString()); if
         * (manager != null) { manager.setZoom(getProcess().getZoom()); }
         */
        // Scroll-wheel Zoom
        getGraphicalViewer().setProperty(MouseWheelHandler.KeyGenerator.getKey(SWT.MOD1), MouseWheelZoomHandler.SINGLETON);

        /** * Snap To Grid ** */
        // Grid properties
        getGraphicalViewer().setProperty(SnapToGrid.PROPERTY_GRID_SPACING, new Dimension(GRID_SIZE, GRID_SIZE));
        getGraphicalViewer().setProperty(SnapToGrid.PROPERTY_GRID_ENABLED, new Boolean(true/* getProcess().isGridEnabled() */));
        // We keep grid visibility and enablement in sync
        getGraphicalViewer().setProperty(SnapToGrid.PROPERTY_GRID_VISIBLE, new Boolean(true/* getProcess().isGridEnabled() */));
        IAction showGrid = new ToggleGridAction(getGraphicalViewer());
        getActionRegistry().registerAction(showGrid);

        /** * Snap To Geometry ** */
        getGraphicalViewer().setProperty(SnapToGeometry.PROPERTY_SNAP_ENABLED,
                new Boolean(false/* getProcess().isSnapToGeometryEnabled() */));
        IAction snapAction = new ToggleSnapToGeometryAction(getGraphicalViewer());
        getActionRegistry().registerAction(snapAction);

        for (Iterator iterator = getSelectionActions().iterator(); iterator.hasNext();) {
            String actionID = (String) iterator.next();
            IAction action = getActionRegistry().getAction(actionID);
            setAction(actionID, action);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.ui.parts.GraphicalEditor#getActionRegistry()
     */
    @Override
    public ActionRegistry getActionRegistry() {
        return super.getActionRegistry();
    }

    /**
     * Save the outline picture for this editor.
     * 
     * @param viewer
     */
    private void saveOutlinePicture(ScrollingGraphicalViewer viewer) {
        GlobalConstant.generatingScreenShoot = true;
        LayerManager layerManager = (LayerManager) viewer.getEditPartRegistry().get(LayerManager.ID);
        // save image using swt
        // get root figure
        IFigure backgroundLayer = layerManager.getLayer(LayerConstants.GRID_LAYER);
        IFigure contentLayer = layerManager.getLayer(LayerConstants.PRINTABLE_LAYERS);

        // create image from root figure
        Image img = new Image(null, contentLayer.getSize().width, contentLayer.getSize().height);
        GC gc = new GC(img);
        Graphics graphics = new SWTGraphics(gc);
        Point point = contentLayer.getBounds().getTopLeft();
        graphics.translate(-point.x, -point.y);
        process.setPropertyValue(Process.SCREEN_OFFSET_X, String.valueOf(-point.x));
        process.setPropertyValue(Process.SCREEN_OFFSET_Y, String.valueOf(-point.y));
        backgroundLayer.paint(graphics);
        contentLayer.paint(graphics);
        graphics.dispose();
        gc.dispose();

        // save image to file
        ImageLoader il = new ImageLoader();
        il.data = new ImageData[] { img.getImageData() };

        IRepositoryService service = DesignerPlugin.getDefault().getRepositoryService();
        IPath filePath = service.getPathFileName(RepositoryConstants.IMG_DIRECTORY_OF_JOB_OUTLINE, "");
        String outlineFileName = process.getName();
        filePath = filePath.append(outlineFileName + ".jpg");

        il.save(filePath.toPortableString(), SWT.IMAGE_JPEG);

        service.getProxyRepositoryFactory().refreshJobPictureFolder();
        GlobalConstant.generatingScreenShoot = false;

    }

    @Override
    protected Control getGraphicalControl() {
        return rulerComp;
    }

    @Override
    protected void createGraphicalViewer(final Composite parent) {
        rulerComp = new RulerComposite(parent, SWT.NONE);
        super.createGraphicalViewer(rulerComp);
        rulerComp.setGraphicalViewer((ScrollingGraphicalViewer) getGraphicalViewer());
    }

    @Override
    public void commandStackChanged(final EventObject event) {
        if (isDirty()) {
            if (!this.savePreviouslyNeeded) {
                this.savePreviouslyNeeded = true;
                firePropertyChange(IEditorPart.PROP_DIRTY);
            }
        } else {
            savePreviouslyNeeded = false;
            firePropertyChange(IEditorPart.PROP_DIRTY);
        }
        super.commandStackChanged(event);
    }

    /**
     * This function is used only for the TabbedPropertySheetPage.
     * 
     * @return contributorId String
     */
    public String getContributorId() {
        return "org.talend.repository.views.repository"; //$NON-NLS-1$
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

    // ------------------------------------------------------------------------
    // Abstract methods from GraphicalEditor

    @Override
    protected void initializeGraphicalViewer() {
        super.initializeGraphicalViewer();
        // this uses the PartFactory set in configureGraphicalViewer
        // to create an EditPart for the diagram and sets it as the
        // content for the viewer
        getGraphicalViewer().setContents(this.process);

        getGraphicalViewer().addDropTargetListener(new ProcessTemplateTransferDropTargetListener(getGraphicalViewer()));
        getGraphicalViewer().addDropTargetListener(new TalendEditorDropTargetListener(this));
        getGraphicalViewer().getControl().addMouseListener(new MouseAdapter() {

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.events.MouseAdapter#mouseUp(org.eclipse.swt.events.MouseEvent)
             */
            @Override
            public void mouseUp(MouseEvent e) {
                updateActions(getSelectionActions());
            }
        });
    }

    // ------------------------------------------------------------------------
    // Abstract methods from EditorPart

    @Override
    public void doSave(IProgressMonitor monitor) {
        monitor.beginTask(Messages.getString("TalendEditor.monitorBeginText"), 100); //$NON-NLS-1$
        monitor.worked(10);

        try {
            if (getEditorInput() instanceof ProcessEditorInput) {
                boolean saved = ((ProcessEditorInput) getEditorInput()).saveProcess(new SubProgressMonitor(monitor, 80), null);
                if (!saved) {
                    monitor.setCanceled(true);
                    throw new InterruptedException();
                }
            }
            getCommandStack().markSaveLocation();
            setDirty(false);
            ((ILibrariesService) GlobalServiceRegister.getDefault().getService(ILibrariesService.class)).resetModulesNeeded();
            monitor.worked(10);

            saveOutlinePicture((ScrollingGraphicalViewer) getGraphicalViewer());
        } catch (Exception e) {
            e.printStackTrace();
            monitor.setCanceled(true);
        } finally {
            monitor.done();
        }

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

            saveOutlinePicture((ScrollingGraphicalViewer) getGraphicalViewer());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void gotoMarker(final IMarker marker) {
    }

    @Override
    public boolean isDirty() {
        // rely on the command stack to determine dirty flag
        return dirtyState || getCommandStack().isDirty();
    }

    public void setDirty(boolean dirty) {
        dirtyState = dirty;
        if (dirtyState) {
            firePropertyChange(IEditorPart.PROP_DIRTY);
        }
    }

    @Override
    public boolean isSaveAsAllowed() {
        // allow Save As
        return true;
    }

    @Override
    protected FlyoutPreferences getPalettePreferences() {
        return TalendEditorPaletteFactory.createPalettePreferences();
    }

    @Override
    protected PaletteRoot getPaletteRoot() {
        if (paletteRoot == null) {

            /** * Components Factory ** */
            components = ComponentsFactoryProvider.getInstance();

            paletteRoot = TalendEditorPaletteFactory.createPalette(components);
        }
        return paletteRoot;
    }

    public IComponent getComponent(String name) {
        if (components == null) {
            components = ComponentsFactoryProvider.getInstance();
        }
        return components.get(name);
    }

    public static void resetPaletteRoot() {
        paletteRoot = null;
    }

    protected FigureCanvas getEditor() {
        return (FigureCanvas) getGraphicalViewer().getControl();
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
            if (fActivationCodeTrigger != null) {
                fActivationCodeTrigger.uninstall();
                fActivationCodeTrigger = null;
            }
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

    /**
     * This function will allow the use of the Delete in the Multipage Editor.
     */
    @Override
    public void selectionChanged(final IWorkbenchPart part, final ISelection selection) {
        super.selectionChanged(part, selection);
        if (this.equals(part)) { // Propagated from MyMultiPageEditor.
            updateActions(getSelectionActions());
        }
    }

    /**
     * Get the viewer in the editor.
     * 
     * @return
     */
    public GraphicalViewer getViewer() {
        return getGraphicalViewer();
    }

    @Override
    public void dispose() {

        JobResourceManager manager = JobResourceManager.getInstance();
        manager.removeProtection(this);
        for (JobResource r : protectedJobs.values()) {
            manager.deleteResource(r);
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

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.job.deletion.IResourceProtection#getProtectedIds()
     */
    public String[] calculateProtectedIds() {
        Set<String> subJobs = process.getSubJobs(null);

        for (String protectedJobName : subJobs) {
            String jobName = protectedJobName;
            protectedJobName = "subjob_of_" + process.getLabel() + "_" + projectName + "_" + protectedJobName; //$NON-NLS-1$
            protectedJobs.put(protectedJobName, new JobResource(projectName, jobName));
        }
        String currentJobId = "talend_editor_" + projectName + "_" + process.getLabel(); //$NON-NLS-1$
        protectedJobs.put(currentJobId, currentJobResource);

        Set<String> set = protectedJobs.keySet();

        return set.toArray(new String[set.size()]); //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.job.deletion.IResourceProtection#getProjectedIds()
     */
    public String[] getProtectedIds() {
        Set<String> set = protectedJobs.keySet();
        return set.toArray(new String[set.size()]);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.job.deletion.IResourceProtection#getJobResource()
     */
    public JobResource getJobResource(String id) {
        return protectedJobs.get(id);
    }

    /**
     * Remove all the protected resources.
     * 
     * yzhang Comment method "resetJobResources".
     */
    public void resetJobResources() {
        String[] ids = getProtectedIds();
        for (String id : ids) {
            protectedJobs.remove(id);
        }
    }

    /**
     * Getter for currentJobResource.
     * 
     * @return the currentJobResource
     */
    public JobResource getCurrentJobResource() {
        return this.currentJobResource;
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

        DragProcessor processor = null;

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
            if (mouseEvent.button == 2) {
                getEditor().setCursor(Cursors.HAND);
                processor = new DragProcessor();
                processor.x = mouseEvent.x;
                processor.y = mouseEvent.y;
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
            }
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.gef.EditDomain#mouseUp(org.eclipse.swt.events.MouseEvent, org.eclipse.gef.EditPartViewer)
         */
        @Override
        public void mouseUp(org.eclipse.swt.events.MouseEvent mouseEvent, EditPartViewer viewer) {
            if (mouseEvent.button == 1) {
                super.mouseUp(mouseEvent, viewer);
            } else if (mouseEvent.button == 2) {
                getEditor().setCursor(null);
                processor = null;
            }
        }
    }

    /**
     * Comment method "getAction".
     * 
     * @param actionID
     * @return
     */
    public IAction getAction(String actionID) {
        return getActionRegistry().getAction(actionID);
    }

}
