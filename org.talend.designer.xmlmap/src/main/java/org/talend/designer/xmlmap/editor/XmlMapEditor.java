// ============================================================================
//
// Copyright (C) 2006-2010 Talend Inc. - www.talend.com
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
import org.eclipse.gef.ui.actions.DeleteAction;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.gef.ui.rulers.RulerComposite;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorPart;
import org.talend.designer.xmlmap.dnd.XmlDragSourceListener;
import org.talend.designer.xmlmap.dnd.XmlDropTargetListener;
import org.talend.designer.xmlmap.editor.actions.DeleteTreeNodeAction;
import org.talend.designer.xmlmap.editor.actions.ImportTreeFromXml;
import org.talend.designer.xmlmap.editor.actions.SetGroupAction;
import org.talend.designer.xmlmap.editor.actions.SetLoopAction;
import org.talend.designer.xmlmap.model.emf.xmlmap.InputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputTreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode;
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

    public XmlMapEditor() {
        DefaultEditDomain defaultEditDomain = new DefaultEditDomain(this);
        setEditDomain(defaultEditDomain);
    }

    public Control getGraphicalControl() {
        rulerComp.setLayoutData(new GridData(GridData.FILL_BOTH));
        return rulerComp;
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
        IAction deleteAction = new DeleteTreeNodeAction(this);
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
                        DeleteTreeNodeAction action = (DeleteTreeNodeAction) getActionRegistry().getAction(DeleteAction.ID);
                        action.update();
                        menu.add(action);

                        SetLoopAction loopAction = (SetLoopAction) getActionRegistry().getAction(SetLoopAction.ID);
                        loopAction.update();
                        menu.add(loopAction);

                        SetGroupAction grouptAction = (SetGroupAction) getActionRegistry().getAction(SetGroupAction.ID);
                        grouptAction.update();
                        menu.add(grouptAction);
                    }

                } else if (selectedEditParts.get(0) instanceof TreeNodeEditPart) {
                    TreeNodeEditPart editPart = (TreeNodeEditPart) selectedEditParts.get(0);
                    TreeNode treeNode = (TreeNode) editPart.getModel();
                    if (treeNode.eContainer() instanceof InputXmlTree && XmlMapUtil.DOCUMENT.equals(treeNode.getType())) {
                        ImportTreeFromXml importAction = new ImportTreeFromXml(getGraphicalViewer().getControl().getShell(),
                                treeNode);
                        importAction.setMapperManager(getMapperManager());
                        menu.add(importAction);
                    }
                    SetLoopAction loopAction = (SetLoopAction) getActionRegistry().getAction(SetLoopAction.ID);
                    loopAction.update();
                    menu.add(loopAction);
                }
            }

        }
    }

}
