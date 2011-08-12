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
import org.talend.designer.xmlmap.figures.treesettings.FilterTextArea;
import org.talend.designer.xmlmap.model.emf.xmlmap.AbstractInOutTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.InputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.NodeType;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputTreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlMapData;
import org.talend.designer.xmlmap.parts.InputXmlTreeEditPart;
import org.talend.designer.xmlmap.parts.OutputTreeNodeEditPart;
import org.talend.designer.xmlmap.parts.OutputXmlTreeEditPart;
import org.talend.designer.xmlmap.parts.TreeNodeEditPart;
import org.talend.designer.xmlmap.parts.VarNodeEditPart;
import org.talend.designer.xmlmap.util.SeparatorType;
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

    private void handleSashDrag(DropTargetEvent event, SashSeparator separator) {
        if (separator.getType() == SeparatorType.ZONE_SEPARATOR) {
            final Point dropLocation = getDropLocation();
            final Rectangle leftBounds = separator.getLeftFigure().getBounds();
            final Rectangle rightBounds = separator.getRightFigure().getBounds();
            if (dropLocation.x < (leftBounds.x + separator.getZoneMinSize() + separator.getWidth())
                    || dropLocation.x > (rightBounds.x + rightBounds.width - separator.getZoneMinSize() - separator.getWidth())) {
                event.detail = DND.DROP_NONE;
                return;
            }

        }

        final LayoutManager layoutManager = separator.getParentFigure().getLayoutManager();
        if (layoutManager instanceof XmlMapDataLayout) {
            XmlMapDataLayout layout = (XmlMapDataLayout) layoutManager;
            int X = getDropLocation().x;
            final Rectangle separatorBounds = separator.getBounds().getCopy();
            separatorBounds.x = X;
            layout.setConstraint(separator, separatorBounds);

            Rectangle leftBounds = separator.getLeftFigure().getBounds().getCopy();

            Rectangle rightBounds = separator.getRightFigure().getBounds().getCopy();
            int wL = X - leftBounds.x;

            int wR = rightBounds.getTopRight().x - X - separator.getWidth();
            int xR = X + separator.getWidth();

            if (wL < separator.getZoneMinSize()) {
                wL = separator.getZoneMinSize();
                wR = rightBounds.getTopRight().x - leftBounds.x - separator.getZoneMinSize() - separator.getWidth();
                xR = leftBounds.x + separator.getZoneMinSize() + separator.getWidth();
            }
            if (wR < separator.getZoneMinSize()) {
                wL = rightBounds.getTopRight().x - separator.getZoneMinSize() - separator.getWidth() - leftBounds.x;
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
    }

    @Override
    public void dragOver(DropTargetEvent event) {
        super.dragOver(event);
        Object object = TemplateTransfer.getInstance().getObject();

        // dnd the sash
        if (object instanceof SashSeparator) {
            handleSashDrag(event, (SashSeparator) object);
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
            if (transferedObj.getType() == TransferdType.OUTPUT) {
                event.detail = DND.DROP_NONE;
                return;
            }

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

            if (targetFigure instanceof ExpressionFigure && !model.getChildren().isEmpty()) {
                event.detail = DND.DROP_NONE;
            }

        } else if (getTargetEditPart() instanceof TreeNodeEditPart) {
            if (transferedObj.getType() == TransferdType.OUTPUT || transferedObj.getType() == TransferdType.VAR) {
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

            if (!model.getChildren().isEmpty()) {
                event.detail = DND.DROP_NONE;
                return;
            }
        } else if (getTargetEditPart() instanceof VarNodeEditPart) {
            if (transferedObj.getType() == TransferdType.OUTPUT || transferedObj.getType() == TransferdType.VAR) {
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

        } else if (getTargetEditPart() instanceof OutputXmlTreeEditPart) {
            if (transferedObj.getType() == TransferdType.OUTPUT) {
                event.detail = DND.DROP_NONE;
            }
        }
    }

    @Override
    protected void handleDrop() {
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
        if (object instanceof SashSeparator) {
            return true;
        }
        return super.isEnabled(event);
    }

}
