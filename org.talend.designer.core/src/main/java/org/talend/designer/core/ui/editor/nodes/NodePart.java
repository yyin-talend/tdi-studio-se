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
package org.talend.designer.core.ui.editor.nodes;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.exception.MessageBoxExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.CorePlugin;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IConnectionCategory;
import org.talend.core.model.process.IExternalNode;
import org.talend.core.model.process.INode;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.designer.core.DesignerPlugin;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.model.components.ExternalUtilities;
import org.talend.designer.core.ui.MultiPageTalendEditor;
import org.talend.designer.core.ui.editor.ProcessEditorInput;
import org.talend.designer.core.ui.editor.cmd.ExternalNodeChangeCommand;
import org.talend.designer.core.ui.editor.connections.Connection;
import org.talend.designer.core.ui.editor.connections.ConnectionFigure;
import org.talend.designer.core.ui.editor.nodecontainer.NodeContainer;
import org.talend.designer.core.ui.editor.nodecontainer.NodeContainerPart;
import org.talend.designer.core.ui.editor.process.ProcessPart;
import org.talend.designer.core.ui.editor.properties.DynamicTabbedPropertySection;
import org.talend.designer.core.ui.views.properties.ComponentSettingsView;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.ui.views.IRepositoryView;

/**
 * Graphical part of the node of Gef. <br/>
 * 
 * $Id$
 * 
 */
public class NodePart extends AbstractGraphicalEditPart implements PropertyChangeListener, NodeEditPart {

    protected DirectEditManager manager;

    protected NodeContainerPart nodeContainerPart;

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editparts.AbstractEditPart#setSelected(int)
     */
    @Override
    @SuppressWarnings("unchecked")//$NON-NLS-1$
    public void setSelected(final int value) {
        if (value == SELECTED) {
            super.setSelected(SELECTED_PRIMARY);
        } else {
            super.setSelected(value);
        }
        Control ctrl = this.getViewer().getControl();
        String helpLink = (String) ((Node) getModel()).getPropertyValue(EParameterName.HELP.getName());
        PlatformUI.getWorkbench().getHelpSystem().setHelp(ctrl, helpLink);
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IViewPart view = page.findView("org.eclipse.help.ui.HelpView"); //$NON-NLS-1$
        if (view != null) {
            PlatformUI.getWorkbench().getHelpSystem().displayHelp(helpLink);
        }

        if (value == SELECTED) {
            view = page.findView(ComponentSettingsView.ID); //$NON-NLS-1$
            if (view != null) {
                ComponentSettingsView compSettings = (ComponentSettingsView) view;
                compSettings.setElement((Node) getModel());
            }
        }
    }

    protected boolean findNodeContainerPart() {
        boolean found = false;
        EditPart diagramPart = this.getParent();
        List listPart = diagramPart.getChildren();
        INode node = ((Node) getModel());
        EditPart editPart;

        for (int i = 0; i < listPart.size() && !found; i++) {
            editPart = (EditPart) listPart.get(i);
            if (editPart instanceof NodeContainerPart) {
                if (node.equals(((NodeContainer) editPart.getModel()).getNode())) {
                    found = true;
                    nodeContainerPart = (NodeContainerPart) editPart;
                }
            }
        }
        return found;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#activate()
     */
    @Override
    public void activate() {
        if (!isActive()) {
            super.activate();
            ((Node) getModel()).addPropertyChangeListener(this);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#deactivate()
     */
    @Override
    public void deactivate() {
        if (isActive()) {
            super.deactivate();
            ((Node) getModel()).removePropertyChangeListener(this);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getModelSourceConnections()
     */
    @Override
    protected List getModelSourceConnections() {
        return ((INode) this.getModel()).getOutgoingConnections();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getModelTargetConnections()
     */
    @Override
    protected List getModelTargetConnections() {
        return ((INode) this.getModel()).getIncomingConnections();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
     */
    @Override
    @SuppressWarnings("unchecked")//$NON-NLS-1$
    protected void refreshVisuals() {
        if (nodeContainerPart == null) {
            findNodeContainerPart();
            nodeContainerPart.setNodePart(this);
            for (EditPart editPart : (List<EditPart>) nodeContainerPart.getChildren()) {
                editPart.refresh();
            }
        }
        Node node = (Node) this.getModel();
        Point loc = node.getLocation();
        Rectangle rectangle = new Rectangle(loc, node.getSize());
        ((GraphicalEditPart) getParent()).setLayoutConstraint(this, getFigure(), rectangle);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
     */
    @Override
    protected IFigure createFigure() {
        NodeFigure nodeFigure;
        EditPart parentPart = getParent();
        while (!(parentPart instanceof ProcessPart)) {
            parentPart = parentPart.getParent();
        }

        nodeFigure = new NodeFigure((Node) this.getModel());

        if (((INode) getModel()).isStart()) {
            nodeFigure.setStart(true);
        } else {
            nodeFigure.setStart(false);
        }
        if (((Node) getModel()).isSetShowHint()) {
            nodeFigure.setHint(((Node) getModel()).getShowHintText());
        }

        nodeFigure.setDummy(((Node) getModel()).isDummy());
        if (((INode) getModel()).isActivate()) {
            nodeFigure.setAlpha(-1);
        } else {
            nodeFigure.setAlpha(Node.ALPHA_VALUE);
        }
        return nodeFigure;
    }

    // ------------------------------------------------------------------------
    // Abstract methods from AbstractEditPart

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
     */
    @Override
    protected void createEditPolicies() {
        installEditPolicy(EditPolicy.COMPONENT_ROLE, new NodeEditPolicy());
        installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new NodeGraphicalEditPolicy());
        installEditPolicy(EditPolicy.LAYOUT_ROLE, new NodeResizableEditPolicy());
    }

    // ------------------------------------------------------------------------
    // Abstract methods from PropertyChangeListener

    /*
     * (non-Javadoc)
     * 
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(final PropertyChangeEvent changeEvent) {
        if (changeEvent.getPropertyName().equals(Node.LOCATION)) {
            if (nodeContainerPart != null) {
                nodeContainerPart.refresh();
            }
            refreshVisuals();
        } else if (changeEvent.getPropertyName().equals(Node.PERFORMANCE_DATA)) {
            refreshVisuals();
            getParent().refresh();
        } else if (changeEvent.getPropertyName().equals(Node.INPUTS)) {
            refreshTargetConnections();
        } else if (changeEvent.getPropertyName().equals(Node.OUTPUTS)) {
            refreshSourceConnections();
        } else if (changeEvent.getPropertyName().equals(Node.SIZE)) {
            refreshVisuals();
            if (nodeContainerPart != null) {
                nodeContainerPart.refresh();
                for (EditPart editPart : (List<EditPart>) nodeContainerPart.getChildren()) {
                    editPart.refresh();
                }
            }

            getParent().refresh();
        } else if (changeEvent.getPropertyName().equals(EParameterName.ACTIVATE.getName())) {
            if (((INode) getModel()).isActivate()) {
                ((NodeFigure) figure).setDummy(((Node) getModel()).isDummy());
                ((NodeFigure) figure).setAlpha(-1);
                ((NodeFigure) figure).repaint();
                refreshVisuals();
            } else {
                ((NodeFigure) figure).setDummy(((Node) getModel()).isDummy());
                ((NodeFigure) figure).setAlpha(Node.ALPHA_VALUE);
                ((NodeFigure) figure).repaint();
                refreshVisuals();
            }
        } else if (changeEvent.getPropertyName().equals(EParameterName.START.getName())) {
            if (((INode) getModel()).isStart()) {
                ((NodeFigure) figure).setStart(true);
                ((NodeFigure) figure).repaint();
                refreshVisuals();
            } else {
                ((NodeFigure) figure).setStart(false);
                ((NodeFigure) figure).repaint();
                refreshVisuals();
            }
        } else if (changeEvent.getPropertyName().equals(EParameterName.HINT.getName())) {
            if (((Node) getModel()).isSetShowHint()) {
                ((NodeFigure) figure).setHint(((Node) getModel()).getShowHintText());
            } else {
                ((NodeFigure) figure).setHint(""); //$NON-NLS-1$
            }
        } else if (changeEvent.getPropertyName().equals(EParameterName.CONNECTION_FORMAT.getName())) {
            Node node = (Node) getModel();

            for (IConnection conn : ((Node) getModel()).getOutgoingConnections()) {
                String connIdName = null;
                String oldName = conn.getName();
                node.getProcess().removeUniqueConnectionName(oldName);
                if (node.getProcess().checkValidConnectionName(node.getConnectionName(), false)) {
                    connIdName = node.getProcess().generateUniqueConnectionName(node.getConnectionName());
                } else {
                    connIdName = node.getProcess().generateUniqueConnectionName("row");
                }
                if (conn instanceof Connection && conn.getLineStyle().hasConnectionCategory(IConnectionCategory.FLOW)
                        && node.getProcess().checkValidConnectionName(connIdName, false)) {
                    ((Connection) conn).setUniqueName(connIdName);
                    node.getProcess().addUniqueConnectionName(connIdName);
                    ((Connection) conn).setName(connIdName);
                } else {
                    node.getProcess().addUniqueConnectionName(oldName);
                }

            }

        }
    }

    // ------------------------------------------------------------------------
    // Abstract methods from NodeEditPart

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.NodeEditPart#getSourceConnectionAnchor(org.eclipse.gef.ConnectionEditPart)
     */
    public ConnectionAnchor getSourceConnectionAnchor(final ConnectionEditPart connection) {
        if (((Connection) connection.getModel()).getLineStyle().hasConnectionCategory(IConnectionCategory.FLOW)) {
            ((NodeFigure) getFigure()).addSourceConnection((ConnectionFigure) connection.getFigure());
        }
        return new ChopboxAnchor(getFigure());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.NodeEditPart#getTargetConnectionAnchor(org.eclipse.gef.ConnectionEditPart)
     */
    public ConnectionAnchor getTargetConnectionAnchor(final ConnectionEditPart connection) {
        if (((Connection) connection.getModel()).getLineStyle().hasConnectionCategory(IConnectionCategory.FLOW)) {
            ((NodeFigure) getFigure()).setTargetConnection((ConnectionFigure) connection.getFigure());
        }
        return new ChopboxAnchor(getFigure());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.NodeEditPart#getSourceConnectionAnchor(org.eclipse.gef.Request)
     */
    public ConnectionAnchor getSourceConnectionAnchor(final Request request) {
        return new ChopboxAnchor(getFigure());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.NodeEditPart#getTargetConnectionAnchor(org.eclipse.gef.Request)
     */
    public ConnectionAnchor getTargetConnectionAnchor(final Request request) {
        return new ChopboxAnchor(getFigure());
    }

    @Override
    public void performRequest(Request req) {
        Node node = (Node) getModel();
        if (req.getType().equals("open")) { //$NON-NLS-1$
            IExternalNode externalNode = ExternalUtilities.getExternalNodeReadyToOpen(node);

            IWorkbenchPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
            if (externalNode != null && (part instanceof MultiPageTalendEditor)) {
                int returnValue = externalNode.open(getViewer().getControl().getDisplay());
                if (!node.isReadOnly()) {
                    if (returnValue == SWT.OK) {
                        Command cmd = new ExternalNodeChangeCommand(node, externalNode);
                        CommandStack cmdStack = (CommandStack) part.getAdapter(CommandStack.class);
                        cmdStack.execute(cmd);
                    } else {
                        externalNode.setExternalData(node.getExternalData());
                    }
                }
            } else {
                IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
                String processName = (String) node.getPropertyValue(EParameterName.PROCESS_TYPE_PROCESS.getName());

                boolean isAvoidShowJobAfterDoubleClick = CorePlugin.getDefault().getComponentsLocalProviderService()
                        .isAvoidToShowJobAfterDoubleClick();

                if (processName != null && !isAvoidShowJobAfterDoubleClick) {
                    try {
                        IProxyRepositoryFactory factory = DesignerPlugin.getDefault().getProxyRepositoryFactory();
                        IRepositoryObject selectedProcess = null;

                        List<IRepositoryObject> list = factory.getAll(ERepositoryObjectType.PROCESS);

                        for (IRepositoryObject process : list) {
                            if (processName.equals(process.getLabel())) {
                                if (process.getProperty().getItem() instanceof ProcessItem) {
                                    selectedProcess = process;
                                    break;
                                }
                            }
                        }
                        if (selectedProcess != null) {
                            ProcessItem processItem = (ProcessItem) selectedProcess.getProperty().getItem();
                            ProcessEditorInput fileEditorInput = new ProcessEditorInput(processItem, true);

                            IEditorPart editorPart = page.findEditor(fileEditorInput);

                            if (editorPart == null) {
                                IViewPart viewPart = page.findView(IRepositoryView.VIEW_ID);
                                if (viewPart != null) {
                                    fileEditorInput.setView((IRepositoryView) viewPart);
                                    fileEditorInput.setRepositoryNode(null);
                                    page.openEditor(fileEditorInput, MultiPageTalendEditor.ID, true);
                                }
                            } else {
                                page.activate(editorPart);
                            }
                        }
                    } catch (PartInitException e) {
                        MessageBoxExceptionHandler.process(e);
                    } catch (PersistenceException e) {
                        MessageBoxExceptionHandler.process(e);
                    }
                } else {
                    try {
                        page.showView("org.eclipse.ui.views.PropertySheet"); //$NON-NLS-1$
                    } catch (PartInitException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        super.performRequest(req);
    }

}
