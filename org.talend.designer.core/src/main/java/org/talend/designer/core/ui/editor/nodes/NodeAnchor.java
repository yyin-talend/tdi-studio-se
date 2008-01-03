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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.draw2d.ui.geometry.LineSeg;
import org.eclipse.gmf.runtime.draw2d.ui.geometry.LineSeg.KeyPoint;
import org.talend.designer.core.ui.editor.connections.Connection;

/**
 * DOC bqian class global comment. Detailled comment
 */
public class NodeAnchor extends ChopboxAnchor {

    Node source, target = null;

    Connection connection = null;

    boolean isTargetAnchor = false;

    Point sourceLocation, targetLocation;

    /**
     * bqian NodeAnchor constructor comment.
     * 
     * @param owner
     */
    public NodeAnchor(NodeFigure owner, Node source, boolean isTargetAnchor) {
        super(owner);
        this.source = source;
        this.isTargetAnchor = isTargetAnchor;
    }

    public NodeAnchor(NodeFigure owner, Node source, Node target, boolean isTargetAnchor) {
        super(owner);
        this.source = source;
        this.target = target;
        this.isTargetAnchor = isTargetAnchor;
    }

    public void setTarget(Node target) {
        this.target = target;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.draw2d.ChopboxAnchor#getLocation(org.eclipse.draw2d.geometry.Point)
     */
    @Override
    public Point getLocation(Point reference) {
        if (target != null && !target.equals(source)) {
            if (!isTargetAnchor) {
                Point newRef = reference.getTranslated(-(target.getSize().width / 2), -(target.getSize().height / 2));
                sourceLocation = source.getLocation();
                targetLocation = target.getLocation();
                Point diff = new Point(newRef.x - targetLocation.x, newRef.y - targetLocation.y);
                sourceLocation = sourceLocation.getTranslated(diff);
                targetLocation = targetLocation.getTranslated(diff);
            } else {
                Point newRef = reference.getTranslated(-(source.getSize().width / 2), -(source.getSize().height / 2));
                sourceLocation = source.getLocation();
                targetLocation = target.getLocation();
                Point diff = new Point(newRef.x - sourceLocation.x, newRef.y - sourceLocation.y);
                sourceLocation = sourceLocation.getTranslated(diff);
                targetLocation = targetLocation.getTranslated(diff);
            }
            int nb = 0;
            int connectionId = 0;
            for (Connection connection : (List<Connection>) source.getOutgoingConnections()) {
                if (connection.getTarget().equals(target)) {
                    nb++;
                    if (connection.equals(this.connection)) {
                        connectionId = nb;
                    }
                }
            }
            if (nb <= 1) {
                return getLocationForSimpleConnection(reference);
            } else {
                return getLocationForMultipleConnections(connectionId);
            }
        }

        return super.getLocation(reference);
    }

    public Point getLocationForSimpleConnection(Point reference) {
        // if (!isTargetAnchor) {
        // if (connection != null) {
        // System.out.println("Simple: refresh source anchor of:" + source + " to:" + target + " connection:"
        // + connection.getName());
        // } else {
        // System.out.println("Simple: refresh source anchor of:" + source + " to:" + target);
        // }
        // } else {
        // if (connection != null) {
        // System.out.println("Simple: refresh target anchor of:" + source + " to:" + target + " connection:"
        // + connection.getName());
        // } else {
        // System.out.println("Simple: refresh target anchor of:" + source + " to:" + target);
        // }
        // }
        Rectangle sourceRect = new Rectangle(sourceLocation, source.getSize());
        Rectangle targetRect = new Rectangle(targetLocation, target.getSize());

        Point sourcePoint = null, targetPoint = null;

        if ((sourceLocation.y < targetRect.getCenter().y)
                && (targetRect.getCenter().y < (sourceLocation.y + source.getSize().height))) {
            // contains
            sourcePoint = new Point(sourceRect.getCenter().x, targetRect.getCenter().y);
        }

        if ((sourceLocation.x < targetRect.getCenter().x)
                && (targetRect.getCenter().x < (sourceLocation.x + source.getSize().width))) {
            // contains
            sourcePoint = new Point(targetRect.getCenter().x, sourceRect.getCenter().y);
        }

        targetPoint = targetRect.getCenter();

        if (sourcePoint == null) {
            if ((targetLocation.y < sourceRect.getCenter().y)
                    && (sourceRect.getCenter().y < (targetLocation.y + target.getSize().height))) {
                // contains
                sourcePoint = new Point(sourceRect.getCenter().x, sourceRect.getCenter().y);
                targetPoint = new Point(targetRect.getCenter().x, sourceRect.getCenter().y);
            }

            if ((targetLocation.x < sourceRect.getCenter().x)
                    && (sourceRect.getCenter().x < (targetLocation.x + target.getSize().width))) {
                // contains
                sourcePoint = new Point(sourceRect.getCenter().x, sourceRect.getCenter().y);
                targetPoint = new Point(sourceRect.getCenter().x, targetRect.getCenter().y);
            }
        }

        if (!isTargetAnchor && sourcePoint == null) {
            return super.getLocation(reference);
        }
        if (sourcePoint != null && targetPoint != null) {
            return calculateLocationFromRef(sourceRect, targetRect, sourcePoint, targetPoint);
        }
        return super.getLocation(reference);
    }

    /**
     * DOC nrousseau Comment method "calculateLocationFromRef".
     * 
     * @param sourceRect
     * @param targetRect
     * @param sourcePoint
     * @param targetPoint
     */
    private Point calculateLocationFromRef(Rectangle sourceRect, Rectangle targetRect, Point sourcePoint, Point targetPoint) {
        LineSeg lineSourceToTarget = new LineSeg(sourcePoint, targetPoint);
        List<LineSeg> lineList = new ArrayList<LineSeg>();
        if (!isTargetAnchor) {
            lineList.add(new LineSeg(sourceRect.getTopLeft(), sourceRect.getTopRight()));
            lineList.add(new LineSeg(sourceRect.getTopLeft(), sourceRect.getBottomLeft()));
            lineList.add(new LineSeg(sourceRect.getTopRight(), sourceRect.getBottomRight()));
            lineList.add(new LineSeg(sourceRect.getBottomLeft(), sourceRect.getBottomRight()));
        } else {
            lineList.add(new LineSeg(targetRect.getTopLeft(), targetRect.getTopRight()));
            lineList.add(new LineSeg(targetRect.getTopLeft(), targetRect.getBottomLeft()));
            lineList.add(new LineSeg(targetRect.getTopRight(), targetRect.getBottomRight()));
            lineList.add(new LineSeg(targetRect.getBottomLeft(), targetRect.getBottomRight()));
        }
        int tolerance = 0;
        Point inter = null;
        while (inter == null && (tolerance < 10)) {
            for (LineSeg line : lineList) {
                inter = lineSourceToTarget.intersect(line, tolerance);
                if (inter != null) {
                    return inter;
                }
            }
            tolerance++;
        }
        return super.getLocation(getReferencePoint());
    }

    public Point getLocationForMultipleConnections(int connectionId) {
        // if (!isTargetAnchor) {
        // if (connection != null) {
        // System.out.println("Multiple: refresh source anchor of:" + source + " to:" + target + " connection:"
        // + connection.getName());
        // } else {
        // System.out.println("Multiple: refresh source anchor of:" + source + " to:" + target);
        // }
        // } else {
        // if (connection != null) {
        // System.out.println("Multiple: refresh target anchor of:" + source + " to:" + target + " connection:"
        // + connection.getName());
        // } else {
        // System.out.println("Multiple: refresh target anchor of:" + source + " to:" + target);
        // }
        // }

        Rectangle sourceRect = new Rectangle(sourceLocation, source.getSize());
        Rectangle targetRect = new Rectangle(targetLocation, target.getSize());
        if ((sourceLocation.y >= targetLocation.y)
                && ((sourceLocation.y + source.getSize().height) <= (targetLocation.y + target.getSize().height))) {
            targetRect = new Rectangle(new Point(targetLocation.x, sourceLocation.y), new Dimension(target.getSize().width,
                    source.getSize().height));
        } else if ((sourceLocation.x >= targetLocation.x)
                && ((sourceLocation.x + source.getSize().width) <= (targetLocation.x + target.getSize().width))) {
            targetRect = new Rectangle(new Point(sourceLocation.x, targetLocation.y), new Dimension(source.getSize().width,
                    source.getSize().height));
        }

        // will calculate the numerator and denominator for the function to place the points.
        // example: 1/2; 3/4; 1/4; 5/8; 3/8; 7/8; 1/8; 9/16; 7/16; 11/16; 5/16; 13/16; 3/16...
        int num = 1;
        int numPair = 1;
        int numImpair = 1;
        int denom = 2;
        for (int i = 1; i < connectionId + 1; i++) {
            if ((i % 2) == 0) {
                // pair
                if ((numPair + 2) > denom) {
                    denom = denom * 2;
                    numPair = (denom / 2) + 1;
                    numImpair = numPair;
                } else {
                    numPair += 2;
                }

                num = numPair;
            } else {
                // impair
                numImpair -= 2;
                if (numImpair > 0) {
                    num = numImpair;
                }
            }
        }

        LineSeg lineSource, lineTarget;
        if ((sourceLocation.x < targetRect.getCenter().x)
                && (targetRect.getCenter().x < (sourceLocation.x + source.getSize().width))) {
            lineSource = new LineSeg(sourceRect.getLeft(), sourceRect.getRight());
            lineTarget = new LineSeg(targetRect.getLeft(), targetRect.getRight());
        } else {
            lineSource = new LineSeg(sourceRect.getTop(), sourceRect.getBottom());
            lineTarget = new LineSeg(targetRect.getTop(), targetRect.getBottom());
        }
        Double length = (lineSource.length() * num / denom);
        Point pointSource = new Point();
        lineSource.pointOn(length.longValue(), KeyPoint.ORIGIN, pointSource);

        length = (lineTarget.length() * num / denom);
        Point pointTarget = new Point();
        lineTarget.pointOn(length.longValue(), KeyPoint.ORIGIN, pointTarget);

        if ((sourceLocation.y < pointTarget.y) && (pointTarget.y < (sourceLocation.y + source.getSize().height))) {
            // contains
            pointSource.y = pointTarget.y;
        }

        if ((sourceLocation.x < pointTarget.x) && (pointTarget.x < (sourceLocation.x + source.getSize().width))) {
            // contains
            pointSource.x = pointTarget.x;
        }

        return calculateLocationFromRef(sourceRect, targetRect, pointSource, pointTarget);
    }

    /**
     * Sets the connection.
     * 
     * @param connection the connection to set
     */
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

}
