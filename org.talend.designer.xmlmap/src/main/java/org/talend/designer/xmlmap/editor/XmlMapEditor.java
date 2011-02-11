// ============================================================================
//
// Copyright (C) 2006-2011 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.xmlmap.editor;

import java.util.EventObject;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.editparts.ScalableRootEditPart;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.gef.ui.rulers.RulerComposite;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.talend.designer.xmlmap.dnd.XmlDragSourceListener;
import org.talend.designer.xmlmap.dnd.XmlDropTargetListener;
import org.talend.designer.xmlmap.editor.actions.CreateAttributeAction;
import org.talend.designer.xmlmap.editor.actions.CreateElementAction;
import org.talend.designer.xmlmap.editor.actions.DeleteTreeNodeAction;
import org.talend.designer.xmlmap.editor.actions.ImportTreeFromXml;
import org.talend.designer.xmlmap.editor.actions.SetGroupAction;
import org.talend.designer.xmlmap.editor.actions.SetLoopAction;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputTreeNode;
import org.talend.designer.xmlmap.parts.OutputTreeNodeEditPart;
import org.talend.designer.xmlmap.parts.TreeNodeEditPart;
import org.talend.designer.xmlmap.ui.tabs.MapperManager;
import org.talend.designer.xmlmap.util.XmlMapUtil;

/**
 * wchen class global comment. Detailled comment
 */
public class XmlMapEditor extends GraphicalEditor {

    private RulerComposite rulerComp;

    private KeyHandler sharedKeyHandler;

    MapperManager mapperManager;

    public XmlMapEditor(MapperManager mapperManager) {
        DefaultEditDomain defaultEditDomain = new DefaultEditDomain(this);
        setEditDomain(defaultEditDomain);
        this.mapperManager = mapperManager;
    }

    protected void createGraphicalViewer(final Composite parent) {
        rulerComp = new RulerComposite(parent, SWT.BORDER);
        GraphicalViewer viewer = new XmlMapGraphicViewer();
        viewer.createControl(rulerComp);
        setGraphicalViewer(viewer);
        configureGraphicalViewer();
        hookGraphicalViewer();
        initializeGraphicalViewer();
        // super.createGraphicalViewer(rulerComp);
        rulerComp.setGraphicalViewer((ScrollingGraphicalViewer) getGraphicalViewer());
    }

    /**
     * @see org.eclipse.gef.commands.CommandStackListener#commandStackChanged(java.util.EventObject)
     */
    public void commandStackChanged(EventObject event) {
        firePropertyChange(IEditorPart.PROP_DIRTY);
        super.commandStackChanged(event);
    }

    /**
     * @see org.eclipse.gef.ui.parts.GraphicalEditor#createActions()
     */
    protected void createActions() {
        ImportTreeFromXml importAction = new ImportTreeFromXml(this, getGraphicalViewer().getControl().getShell());
        importAction.setMapperManager(mapperManager);
        getActionRegistry().registerAction(importAction);
        getSelectionActions().add(importAction.getId());

        CreateAttributeAction createAttribute = new CreateAttributeAction(this);
        createAttribute.setMapperManager(mapperManager);
        getActionRegistry().registerAction(createAttribute);
        getSelectionActions().add(createAttribute.getId());

        CreateElementAction createElement = new CreateElementAction(this);
        createElement.setMapperManager(mapperManager);
        getActionRegistry().registerAction(createElement);
        getSelectionActions().add(createElement.getId());

        DeleteTreeNodeAction deleteAction = new DeleteTreeNodeAction(this);
        deleteAction.setMapperManager(mapperManager);
        getActionRegistry().registerAction(deleteAction);
        getSelectionActions().add(deleteAction.getId());

        IAction loopAction = new SetLoopAction(this);
        getActionRegistry().registerAction(loopAction);
        getSelectionActions().add(loopAction.getId());

        IAction groupAction = new SetGroupAction(this);
        getActionRegistry().registerAction(groupAction);
        getSelectionActions().add(groupAction.getId());

    }

    /**
     * @see org.eclipse.gef.ui.parts.GraphicalEditor#configureGraphicalViewer()
     */
    protected void configureGraphicalViewer() {
        super.configureGraphicalViewer();
        getGraphicalViewer().setRootEditPart(new ScalableRootEditPart());
        getGraphicalViewer().setEditPartFactory(new XmlMapPartFactory());
        getGraphicalViewer().setKeyHandler(new GraphicalViewerKeyHandler(getGraphicalViewer()).setParent(getCommonKeyHandler()));

    }

    /**
     * @see org.eclipse.gef.ui.parts.GraphicalEditor#initializeGraphicalViewer()
     */
    protected void initializeGraphicalViewer() {

        // getGraphicalViewer().setContents(getContents());

        getGraphicalViewer().addDragSourceListener(new XmlDragSourceListener(getGraphicalViewer()));

        getGraphicalViewer().addDropTargetListener(new XmlDropTargetListener(getGraphicalViewer()));

        this.getGraphicalViewer().addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                getGraphicalViewer().getSelectedEditParts();
                // XmlMapEditor.this.selectionChanged(XmlMapEditor.this, getGraphicalViewer().getSelection());
            }

        });
        getGraphicalViewer().addSelectionChangedListener(mapperManager);
        getGraphicalViewer().setContextMenu(new MenueProvider(getGraphicalViewer()));
        initializeActionRegistry();
    }

    public void setMapperManager(MapperManager listener) {
        this.mapperManager = listener;
        if (listener != null) {
            this.getGraphicalViewer().addSelectionChangedListener(listener);
        }
    }

    public MapperManager getMapperManager() {
        return this.mapperManager;
    }

    //
    // @Override
    // public void selectionChanged(IWorkbenchPart part, ISelection selection) {
    // updateActions(getSelectionActions());
    // }

    public void setContent(Object content) {
        getGraphicalViewer().setContents(content);
    }

    protected KeyHandler getCommonKeyHandler() {
        if (sharedKeyHandler == null) {
            // sharedKeyHandler = new KeyHandler();
            // sharedKeyHandler.put(KeyStroke.getPressed(SWT.DEL, 127, 0),
            // getActionRegistry().getAction(ActionFactory.DELETE.getId()));
            // sharedKeyHandler.put(KeyStroke.getPressed(SWT.F2, 0),
            // getActionRegistry().getAction(GEFActionConstants.DIRECT_EDIT));
        }
        return sharedKeyHandler;
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
    public void doSave(IProgressMonitor monitor) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void hookGraphicalViewer() {
        getSelectionSynchronizer().addViewer(getGraphicalViewer());
    }

    /**
     * wchen class global comment. Detailled comment
     */
    class MenueProvider extends ContextMenuProvider {

        public MenueProvider(EditPartViewer viewer) {
            super(viewer);
        }

        @Override
        public void buildContextMenu(IMenuManager menu) {
            // context menu should only display in the document tree
            List selectedEditParts = getGraphicalViewer().getSelectedEditParts();
            if (selectedEditParts != null && !selectedEditParts.isEmpty()) {
                if (selectedEditParts.get(0) instanceof OutputTreeNodeEditPart) {
                    OutputTreeNode model = (OutputTreeNode) ((OutputTreeNodeEditPart) selectedEditParts.get(0)).getModel();
                    if (XmlMapUtil.getXPathLength(model.getXpath()) > 2) {
                        CreateElementAction createElement = (CreateElementAction) getActionRegistry().getAction(
                                CreateElementAction.ID);
                        createElement.update();
                        createElement.setInput(false);
                        menu.add(createElement);

                        CreateAttributeAction createAttribute = (CreateAttributeAction) getActionRegistry().getAction(
                                CreateAttributeAction.ID);
                        createAttribute.update();
                        createAttribute.setInput(false);
                        menu.add(createAttribute);

                        DeleteTreeNodeAction action = (DeleteTreeNodeAction) getActionRegistry().getAction(
                                DeleteTreeNodeAction.ID);
                        action.update();
                        action.setInput(false);
                        menu.add(action);

                        SetLoopAction loopAction = (SetLoopAction) getActionRegistry().getAction(SetLoopAction.ID);
                        loopAction.update();
                        menu.add(loopAction);

                        SetGroupAction grouptAction = (SetGroupAction) getActionRegistry().getAction(SetGroupAction.ID);
                        grouptAction.update();
                        menu.add(grouptAction);
                    }

                } else if (selectedEditParts.get(0) instanceof TreeNodeEditPart) {
                    ImportTreeFromXml importAction = (ImportTreeFromXml) getActionRegistry().getAction(ImportTreeFromXml.ID);
                    importAction.update();
                    menu.add(importAction);

                    CreateElementAction createElement = (CreateElementAction) getActionRegistry().getAction(
                            CreateElementAction.ID);
                    createElement.setInput(true);
                    createElement.update();
                    menu.add(createElement);

                    CreateAttributeAction createAttribute = (CreateAttributeAction) getActionRegistry().getAction(
                            CreateAttributeAction.ID);
                    createAttribute.setInput(true);
                    createAttribute.update();
                    menu.add(createAttribute);

                    DeleteTreeNodeAction deleteAction = (DeleteTreeNodeAction) getActionRegistry().getAction(
                            DeleteTreeNodeAction.ID);
                    deleteAction.setInput(true);
                    deleteAction.update();
                    menu.add(deleteAction);

                    SetLoopAction loopAction = (SetLoopAction) getActionRegistry().getAction(SetLoopAction.ID);
                    loopAction.update();
                    menu.add(loopAction);
                }
            }

        }
    }

}
