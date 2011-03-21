package org.talend.designer.xmlmap.figures.anchors;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.swt.SWT;
import org.talend.designer.xmlmap.figures.TreeNodeFigure;
import org.talend.designer.xmlmap.figures.XmlTreeBranch;
import org.talend.designer.xmlmap.model.emf.xmlmap.AbstractInOutTree;
import org.talend.designer.xmlmap.parts.AbstractInOutTreeEditPart;
import org.talend.designer.xmlmap.parts.BaseConnectionEditPart;
import org.talend.designer.xmlmap.parts.LookupConnectionEditPart;
import org.talend.designer.xmlmap.parts.TreeNodeEditPart;

public class LookupColumnAnchor extends ChopboxAnchor {

    private ConnectionEditPart connectionPart;

    private TreeNodeEditPart treeNodePart;

    public LookupColumnAnchor(IFigure owner, ConnectionEditPart connectionPart, TreeNodeEditPart treeNodePart) {
        super(owner);
        this.connectionPart = connectionPart;
        this.treeNodePart = treeNodePart;
    }

    public IFigure getOwner() {
        return super.getOwner();
    }

    public Point getReferencePoint() {
        if (connectionPart == null) {
            return getOwner().getBounds().getLeft();
        }

        AbstractInOutTreeEditPart inOutTreeEditPart = treeNodePart.getInOutTreeEditPart(treeNodePart);

        Point ref = null;
        if (inOutTreeEditPart != null && ((AbstractInOutTree) inOutTreeEditPart.getModel()).isMinimized()) {
            ref = inOutTreeEditPart.getFigure().getBounds().getLeft();
        } else {

            if (getOwner() == null) {
                return null;
            } else if (getOwner() instanceof TreeNodeFigure) {
                TreeNodeFigure nodeFigure = (TreeNodeFigure) getOwner();
                // normal column
                if (nodeFigure.getTreeBranch() == null) {
                    if (nodeFigure.getColumnExpressionFigure() != null) {
                        ref = nodeFigure.getColumnExpressionFigure().getBounds().getLeft();
                    } else {
                        ref = getOwner().getBounds().getLeft();
                    }
                    getOwner().translateToAbsolute(ref);

                }
            } else if (getOwner() instanceof XmlTreeBranch) {
                XmlTreeBranch treeBranch = (XmlTreeBranch) getOwner();
                Rectangle elembounds = treeBranch.getElement().getBounds().getCopy();
                Rectangle bounds = treeBranch.getRoot().getBounds().getCopy();
                elembounds.x = bounds.x;
                elembounds.width = bounds.width;
                if (treeBranch.getExpressionFigure() != null) {
                    ref = treeBranch.getExpressionFigure().getBounds().getLeft();
                } else {
                    ref = elembounds.getLeft();
                }
                getOwner().translateToAbsolute(ref);
            } else {
                ref = getOwner().getBounds().getCenter();
                getOwner().translateToAbsolute(ref);
            }
        }

        if (inOutTreeEditPart != null && ref != null) {
            IFigure treeFigure = inOutTreeEditPart.getFigure();
            int avialableX = treeFigure.getBounds().x;
            if (ref.x < avialableX) {
                ref.x = avialableX;
            }

        }

        if (connectionPart instanceof BaseConnectionEditPart && connectionPart.getFigure() instanceof PolylineConnection) {
            BaseConnectionEditPart baseConnectionPart = (BaseConnectionEditPart) connectionPart;
            PolylineConnection connFigure = (PolylineConnection) connectionPart.getFigure();

            org.eclipse.swt.graphics.Point avilableSize = treeNodePart.getViewer().getControl().getSize();

            if (ref != null) {
                if (ref.y < 0) {
                    if (!(baseConnectionPart instanceof LookupConnectionEditPart)) {
                        ref.y = 0;
                    }
                    baseConnectionPart.setTargetContained(false);
                    if (baseConnectionPart.isDOTStyle()) {
                        connFigure.setLineStyle(SWT.LINE_DASHDOTDOT);
                    } else {
                        connFigure.setLineStyle(SWT.LINE_SOLID);
                    }
                } else if (ref.y > avilableSize.y) {
                    if (!(baseConnectionPart instanceof LookupConnectionEditPart)) {
                        ref.y = avilableSize.y;
                    }
                    baseConnectionPart.setTargetContained(false);
                    if (baseConnectionPart.isDOTStyle()) {
                        connFigure.setLineStyle(SWT.LINE_DASHDOTDOT);
                    } else {
                        connFigure.setLineStyle(SWT.LINE_SOLID);
                    }
                } else {
                    baseConnectionPart.setTargetContained(true);
                    if (!baseConnectionPart.isDOTStyle()) {
                        connFigure.setLineStyle(SWT.LINE_SOLID);
                    }
                }
            }
        }

        return ref;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.draw2d.ChopboxAnchor#getLocation(org.eclipse.draw2d.geometry.Point)
     */
    @Override
    public Point getLocation(Point reference) {
        Point referencePoint = getReferencePoint();
        if (referencePoint != null) {
            return new Point(referencePoint.x, referencePoint.y);
        } else {
            return new Point(0, 0);
        }

    }

    @Override
    protected Rectangle getBox() {
        return super.getBox();
    }

}
