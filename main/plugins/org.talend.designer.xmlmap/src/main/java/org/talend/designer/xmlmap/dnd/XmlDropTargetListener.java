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
package org.talend.designer.xmlmap.dnd;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.Request;
import org.eclipse.gef.dnd.TemplateTransfer;
import org.eclipse.gef.dnd.TemplateTransferDropTargetListener;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.talend.designer.xmlmap.editor.XmlMapGraphicViewer;
import org.talend.designer.xmlmap.figures.ExpressionFigure;
import org.talend.designer.xmlmap.figures.SashSeparator;
import org.talend.designer.xmlmap.figures.layout.XmlMapDataLayout;
import org.talend.designer.xmlmap.figures.sash.ISash;
import org.talend.designer.xmlmap.figures.table.ColumnSash;
import org.talend.designer.xmlmap.figures.table.ITable;
import org.talend.designer.xmlmap.figures.table.Table;
import org.talend.designer.xmlmap.figures.table.TableColumn;
import org.talend.designer.xmlmap.figures.table.TableTree;
import org.talend.designer.xmlmap.figures.treesettings.FilterTextArea;
import org.talend.designer.xmlmap.model.emf.xmlmap.AbstractInOutTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.InputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.NodeType;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputTreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlMapData;
import org.talend.designer.xmlmap.parts.InputXmlTreeEditPart;
import org.talend.designer.xmlmap.parts.OutputTreeNodeEditPart;
import org.talend.designer.xmlmap.parts.TreeNodeEditPart;
import org.talend.designer.xmlmap.parts.VarNodeEditPart;
import org.talend.designer.xmlmap.util.XmlMapUtil;

/**
 * wchen class global comment. Detailled comment
 */
public class XmlDropTargetListener extends TemplateTransferDropTargetListener {

    private IFigure targetFigure;

    public XmlDropTargetListener(EditPartViewer viewer) {
        super(viewer);
    }

    @Override
    protected void updateTargetRequest() {
        ((CreateRequest) getTargetRequest()).setLocation(getDropLocation());
    }

    @Override
    protected Request createTargetRequest() {
        CreateNodeConnectionRequest request = new CreateNodeConnectionRequest(getTargetEditPart());
        if (targetFigure instanceof ExpressionFigure || targetFigure instanceof FilterTextArea) {
            request.setDropType(CreateNodeConnectionRequest.DROP_EXPRESSION);
        }

        request.setFactory(new NewNodeCreationFactory(TemplateTransfer.getInstance().getObject()));
        return request;
    }

    @Override
    protected void updateTargetEditPart() {
        super.updateTargetEditPart();

        // if(getTargetEditPart() instanceof TreeNode){
        // TreeNodeEditPart treeNodePart = (TreeNodeEditPart)getTargetEditPart();
        // final IFigure figure = treeNodePart.getFigure();
        // }
        //

        if (getViewer() instanceof XmlMapGraphicViewer) {
            Point dropLocation = getDropLocation();
            EditPartViewer.Conditional condition = new EditPartViewer.Conditional() {

                public boolean evaluate(EditPart editpart) {
                    return editpart.getTargetEditPart(getTargetRequest()) != null;
                }
            };
            targetFigure = ((XmlMapGraphicViewer) getViewer()).findFigureAt(dropLocation.x, dropLocation.y, getExclusionSet(),
                    condition);
        }
    }

    public void dragEnter(DropTargetEvent event) {
    }

    public void dragLeave(DropTargetEvent event) {

    }

    public void dragOperationChanged(DropTargetEvent event) {

    }

    private void handleSashDrag(DropTargetEvent event, ISash s) {
        if (s instanceof SashSeparator) {
            SashSeparator separator = (SashSeparator) s;
            final Point dropLocation = getDropLocation();
            Rectangle leftBounds = separator.getLeftFigure().getBounds().getCopy();
            Rectangle rightBounds = separator.getRightFigure().getBounds().getCopy();
            if (dropLocation.x < (leftBounds.x + separator.getZoneMinSize() + separator.getSashWidth())
                    || dropLocation.x > (rightBounds.x + rightBounds.width - separator.getZoneMinSize() - separator
                            .getSashWidth())) {
                event.detail = DND.DROP_NONE;
                return;
            }

            final LayoutManager layoutManager = separator.getParentFigure().getLayoutManager();
            if (layoutManager instanceof XmlMapDataLayout) {
                XmlMapDataLayout layout = (XmlMapDataLayout) layoutManager;
                int X = getDropLocation().x;
                final Rectangle separatorBounds = separator.getBounds().getCopy();
                separatorBounds.x = X;
                layout.setConstraint(separator, separatorBounds);

                // left figure width
                int wL = X - leftBounds.x;

                // right figure width
                int wR = rightBounds.getTopRight().x - X - separator.getSashWidth();
                int xR = X + separator.getSashWidth();

                if (wL < separator.getZoneMinSize()) {
                    wL = separator.getZoneMinSize();
                    wR = rightBounds.getTopRight().x - leftBounds.x - separator.getZoneMinSize() - separator.getSashWidth();
                    xR = leftBounds.x + separator.getZoneMinSize() + separator.getSashWidth();
                }
                if (wR < separator.getZoneMinSize()) {
                    wL = rightBounds.getTopRight().x - separator.getZoneMinSize() - separator.getSashWidth() - leftBounds.x;
                    wR = separator.getZoneMinSize();
                    xR = rightBounds.getTopRight().x - separator.getZoneMinSize();
                }
                leftBounds.width = wL;
                layout.setConstraint(separator.getLeftFigure(), leftBounds.getCopy());
                separator.getLeftFigure().invalidateTree();

                rightBounds.x = xR;
                rightBounds.width = wR;
                layout.setConstraint(separator.getRightFigure(), rightBounds.getCopy());
                separator.getRightFigure().invalidateTree();
                separator.getParentFigure().revalidate();
            }

        } else if (s instanceof ColumnSash) {
            ColumnSash columnSash = (ColumnSash) s;
            ITable t = columnSash.getTable();
            if (t instanceof TableTree) {
                TableTree table = (TableTree) t;
                Rectangle tableBounds = table.getBounds();
                int X = getDropLocation().x;
                if (X < tableBounds.x + columnSash.getSashWidth()
                        || X > tableBounds.x + tableBounds.width - columnSash.getSashWidth()) {
                    event.detail = DND.DROP_NONE;
                    return;
                }

                double newExpressionWidth = X - tableBounds.x;

                double defaultExpressionWidth = table.getDefaultExpressionWidth();
                double pecnetage = newExpressionWidth / defaultExpressionWidth;
                table.setExpressWidthPecentage(pecnetage);
            } else if (t instanceof Table) {
                Table table = (Table) t;
                Rectangle tableBounds = table.getBounds();
                int X = getDropLocation().x;
                if (X < tableBounds.x + 10 || X > tableBounds.x + tableBounds.width - 10) {
                    event.detail = DND.DROP_NONE;
                    return;
                }

                TableColumn leftColumn = columnSash.getLeftColumn();
                TableColumn rightColumn = columnSash.getRightColumn();
                Rectangle leftBounds = leftColumn.getBounds();
                Rectangle rightBounds = rightColumn.getBounds();

                int LW = X - leftBounds.x;
                int RW = rightBounds.x + rightBounds.width - X;

                if (LW < 10) {
                    LW = 10;
                }
                if (RW < 10) {
                    RW = 10;
                }
                double LP = (double) LW / (double) tableBounds.width;
                double RP = (double) RW / (double) tableBounds.width;

                table.setColumnPercentage(leftColumn.getColumnKey(), LP);
                table.setColumnPercentage(rightColumn.getColumnKey(), RP);
                table.invalidateTree();
                table.revalidate();
            }
        }

    }

    @Override
    public void dragOver(DropTargetEvent event) {
        super.dragOver(event);
        Object object = TemplateTransfer.getInstance().getObject();
        if (object == null) {
            event.detail = DND.DROP_NONE;
            return;
        }

        // dnd the sash
        if (object instanceof ISash) {
            handleSashDrag(event, (ISash) object);
            return;
        }

        // dnd the tree node
        if (!(object instanceof TransferedObject)) {
            event.detail = DND.DROP_NONE;
            return;
        }
        TransferedObject transferedObj = (TransferedObject) object;
        if (transferedObj.getToTransfer() == null || transferedObj.getToTransfer().isEmpty() || transferedObj.getType() == null) {
            event.detail = DND.DROP_NONE;
            return;
        }

        if (getTargetEditPart() instanceof OutputTreeNodeEditPart) {

            OutputTreeNodeEditPart nodePart = (OutputTreeNodeEditPart) getTargetEditPart();
            OutputTreeNode model = (OutputTreeNode) nodePart.getModel();

            if (model.eContainer() instanceof AbstractInOutTree && !(targetFigure instanceof ExpressionFigure)) {
                event.detail = DND.DROP_NONE;
                return;
            }

            if ((NodeType.ATTRIBUT.equals(model.getNodeType()) || NodeType.NAME_SPACE.equals(model.getNodeType()))
                    && !(targetFigure instanceof ExpressionFigure)) {
                event.detail = DND.DROP_NONE;
                return;
            }

            if (targetFigure instanceof ExpressionFigure && !XmlMapUtil.isExpressionEditable(model)) {
                event.detail = DND.DROP_NONE;
            }

        } else if (getTargetEditPart() instanceof TreeNodeEditPart) {
            if (transferedObj.getType() == TransferdType.VAR) {
                event.detail = DND.DROP_NONE;
                return;
            }
            boolean isLookup = false;
            TreeNode inputTreeNodeRoot = XmlMapUtil.getTreeNodeRoot((TreeNode) getTargetEditPart().getModel());
            InputXmlTree targetTree = null;
            if (inputTreeNodeRoot != null && inputTreeNodeRoot.eContainer() instanceof InputXmlTree) {
                isLookup = ((InputXmlTree) inputTreeNodeRoot.eContainer()).isLookup();
                targetTree = (InputXmlTree) inputTreeNodeRoot.eContainer();
            }

            // can't drag and drop in the same lookup , can't drop if sources are from different trees
            InputXmlTree inputTree = null;
            if (isLookup) {
                for (Object obj : transferedObj.getToTransfer()) {
                    if (obj instanceof TreeNodeEditPart) {
                        inputTreeNodeRoot = XmlMapUtil.getTreeNodeRoot((TreeNode) ((TreeNodeEditPart) obj).getModel());
                        if (inputTreeNodeRoot != null && inputTreeNodeRoot.eContainer() instanceof InputXmlTree) {
                            InputXmlTree sourceTree = (InputXmlTree) inputTreeNodeRoot.eContainer();
                            if (targetTree == sourceTree) {
                                event.detail = DND.DROP_NONE;
                                return;
                            }
                            if (inputTree == null) {
                                inputTree = sourceTree;
                            } else if (inputTree != sourceTree) {
                                event.detail = DND.DROP_NONE;
                                return;
                            }

                            if (sourceTree.eContainer() instanceof XmlMapData) {
                                XmlMapData xmlMapData = (XmlMapData) sourceTree.eContainer();
                                int sourceIndex = xmlMapData.getInputTrees().indexOf(sourceTree);
                                int targetIndex = xmlMapData.getInputTrees().indexOf(targetTree);
                                if (sourceIndex > targetIndex) {
                                    event.detail = DND.DROP_NONE;
                                    return;
                                }
                            }

                        }
                    }
                }

            }

            if (!isLookup) {
                event.detail = DND.DROP_NONE;
            }
            TreeNodeEditPart nodePart = (TreeNodeEditPart) getTargetEditPart();
            TreeNode model = (TreeNode) nodePart.getModel();

            if (XmlMapUtil.DOCUMENT.equals(model.getType())) {
                event.detail = DND.DROP_NONE;
                return;
            }

            if (!XmlMapUtil.isExpressionEditable(model)) {
                event.detail = DND.DROP_NONE;
                return;
            }
        } else if (getTargetEditPart() instanceof VarNodeEditPart) {
            if (transferedObj.getType() == TransferdType.VAR) {
                event.detail = DND.DROP_NONE;
            }
        } else if (getTargetEditPart() instanceof InputXmlTreeEditPart) {
            if (transferedObj.getType() != TransferdType.INPUT || !(targetFigure instanceof FilterTextArea)) {
                event.detail = DND.DROP_NONE;
                return;
            }
            InputXmlTree targetTree = (InputXmlTree) ((InputXmlTreeEditPart) getTargetEditPart()).getModel();
            for (Object obj : transferedObj.getToTransfer()) {
                if (obj instanceof TreeNodeEditPart) {
                    TreeNode inputTreeNodeRoot = XmlMapUtil.getTreeNodeRoot((TreeNode) ((TreeNodeEditPart) obj).getModel());
                    if (inputTreeNodeRoot != null && inputTreeNodeRoot.eContainer() instanceof InputXmlTree) {
                        InputXmlTree sourceTree = (InputXmlTree) inputTreeNodeRoot.eContainer();
                        if (sourceTree.eContainer() instanceof XmlMapData) {
                            XmlMapData xmlMapData = (XmlMapData) sourceTree.eContainer();
                            int sourceIndex = xmlMapData.getInputTrees().indexOf(sourceTree);
                            int targetIndex = xmlMapData.getInputTrees().indexOf(targetTree);
                            if (sourceIndex > targetIndex) {
                                event.detail = DND.DROP_NONE;
                                return;
                            }
                        }
                    }
                }
            }

        }
    }

    @Override
    protected void handleDrop() {
        final Object object = TemplateTransfer.getInstance().getObject();
        if (object == null) {
            return;
        }
        super.handleDrop();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.dnd.AbstractTransferDropTargetListener#isEnabled(org.eclipse.swt.dnd.DropTargetEvent)
     */
    @Override
    public boolean isEnabled(DropTargetEvent event) {
        final Object object = TemplateTransfer.getInstance().getObject();
        if (object instanceof ISash) {
            return true;
        }
        return super.isEnabled(event);
    }

}
