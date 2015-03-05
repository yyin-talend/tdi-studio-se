// ============================================================================
//
// Copyright (C) 2006-2013 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.core.ui.editor.connections;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gmf.runtime.draw2d.ui.figures.PolylineConnectionEx;
import org.eclipse.gmf.runtime.draw2d.ui.internal.routers.BorderItemRectilinearRouter;
import org.talend.core.model.process.EConnectionCategory;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.IConnection;
import org.talend.designer.core.model.process.ConnectionManager;
import org.talend.designer.core.ui.editor.nodes.Node;

/**
 * DOC hwang class global comment. Detailled comment
 */
public class TalendBorderItemRectilinearRouter extends BorderItemRectilinearRouter {

    private final static int OFFSET = 16;

    private CreateConnectionRequest request;

    public TalendBorderItemRectilinearRouter() {

    }

    public TalendBorderItemRectilinearRouter(CreateConnectionRequest request) {
        this.request = request;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.gmf.runtime.draw2d.ui.internal.routers.ObliqueRouter#routeBendpoints(org.eclipse.draw2d.Connection)
     */
    @Override
    public void routeBendpoints(Connection conn) {
        super.routeBendpoints(conn);
        manualPosition(conn);
    }

    private boolean manualPosition(Connection conn) {
        boolean alreadyHandle = false;
        if (!(conn instanceof PolylineConnectionEx)) {
            return false;
        }
        IConnection connection = null;
        EConnectionCategory category = null;
        EConnectionType lineStyle = null;
        Rectangle sourceBounds = null;
        Rectangle targetBounds = null;
        if (conn instanceof ConnectionFigure) {
            connection = ((ConnectionFigure) conn).getConnection();
            if (!(connection.getSource() instanceof Node)) {
                return false;
            }
            lineStyle = connection.getLineStyle();
            category = lineStyle.getCategory();
            sourceBounds = new Rectangle(((Node) connection.getSource()).getLocation(), ((Node) connection.getSource()).getSize());
            targetBounds = new Rectangle(((Node) connection.getTarget()).getLocation(), ((Node) connection.getTarget()).getSize());
        }

        if (lineStyle == null) {
            lineStyle = ConnectionManager.getNewConnectionType();
            category = lineStyle.getCategory();
        }

        if (this.request != null) {
            if (this.request.getSourceEditPart() == null) {
                return false;
            }

            if (this.request.getTargetEditPart() == null) {
                if (conn.getPoints().size() <= 2) {
                    Point firstPoint = conn.getPoints().getFirstPoint();
                    Point lastPoint = conn.getPoints().getLastPoint();
                    PointList pointList = new PointList();
                    pointList.addPoint(firstPoint);
                    pointList.addPoint((lastPoint.x + firstPoint.x) / 2, firstPoint.y);

                    pointList.addPoint((lastPoint.x + firstPoint.x) / 2, (lastPoint.y + firstPoint.y) / 2);

                    pointList.addPoint((lastPoint.x + firstPoint.x) / 2, lastPoint.y);
                    pointList.addPoint(lastPoint);

                    conn.setPoints(pointList);
                }
                return false;
            }

        }

        if ((sourceBounds == null) && this.request != null) {
            Node source = (Node) this.request.getSourceEditPart().getModel();
            if (source != null) {
                sourceBounds = new Rectangle(source.getLocation(), source.getSize());
            }
        }

        if ((targetBounds == null) && this.request != null) {
            Node target = (Node) this.request.getTargetEditPart().getModel();
            if (target != null) {
                targetBounds = new Rectangle(target.getLocation(), target.getSize());
            }
        }

        if (targetBounds == null) {
            return false;
        }

        ((PolylineConnectionEx) conn).setRoundedBendpointsRadius(32);
        PointList points = conn.getPoints();
        PointList pointList = new PointList();
        Point firstpoint = points.getFirstPoint();
        Point lastpoint = points.getLastPoint();
        if (category == EConnectionCategory.MAIN && lineStyle != EConnectionType.FLOW_REF) {
            if ((sourceBounds.x > targetBounds.x) && (sourceBounds.y == targetBounds.y)) {
                pointList.addPoint(firstpoint);
                pointList.addPoint(firstpoint.x + OFFSET, firstpoint.y);
                pointList.addPoint(firstpoint.x + OFFSET, firstpoint.y + 2 * OFFSET);

                pointList.addPoint(lastpoint.x - OFFSET, lastpoint.y + 2 * OFFSET);
                pointList.addPoint(lastpoint.x - OFFSET, lastpoint.y);
                pointList.addPoint(lastpoint);
                alreadyHandle = true;
            } else if ((sourceBounds.x > targetBounds.x)
                    || (sourceBounds.getTopRight().x == targetBounds.getTopLeft().x)
                    || ((sourceBounds.getTopLeft().x == targetBounds.getTopRight().x) && (sourceBounds.getTopLeft().y != targetBounds
                            .getTopRight().y))) {
                pointList.addPoint(firstpoint);
                pointList.addPoint(firstpoint.x + OFFSET, firstpoint.y);
                pointList.addPoint(firstpoint.x + OFFSET, (sourceBounds.getCenter().y + targetBounds.getCenter().y) / 2);

                pointList.addPoint(lastpoint.x - OFFSET, (sourceBounds.getCenter().y + targetBounds.getCenter().y) / 2);
                pointList.addPoint(lastpoint.x - OFFSET, lastpoint.y);
                pointList.addPoint(lastpoint);
                alreadyHandle = true;
            } else if (sourceBounds.x == targetBounds.x) {
                pointList.addPoint(firstpoint);
                pointList.addPoint(firstpoint.x + 2 * OFFSET, firstpoint.y);
                pointList.addPoint(firstpoint.x + 2 * OFFSET, (sourceBounds.getCenter().y + targetBounds.getCenter().y) / 2);

                pointList.addPoint(lastpoint.x - 2 * OFFSET, (sourceBounds.getCenter().y + targetBounds.getCenter().y) / 2);
                pointList.addPoint(lastpoint.x - 2 * OFFSET, lastpoint.y);
                pointList.addPoint(lastpoint);
                alreadyHandle = true;
            } else if (Math.abs(sourceBounds.getCenter().y - targetBounds.getCenter().y) == 4 * OFFSET) {
                if ((firstpoint.x + lastpoint.x) / 2 - OFFSET <= firstpoint.x) {
                    return false;
                }
                ((PolylineConnectionEx) conn).setRoundedBendpointsRadius(16);
            }
        } else if (category == EConnectionCategory.OTHER
                && (lineStyle == EConnectionType.FLOW_REF || lineStyle == EConnectionType.TABLE_REF)) {
            if (targetBounds.y == sourceBounds.y) {
                pointList.addPoint(firstpoint);
                pointList.addPoint(firstpoint.x, firstpoint.y + OFFSET);

                pointList.addPoint(lastpoint.x, firstpoint.y + OFFSET);
                pointList.addPoint(lastpoint);
                alreadyHandle = true;
            } else if ((targetBounds.getTopRight().y == sourceBounds.getBottomLeft().y)) {
                pointList.addPoint(firstpoint);
                pointList.addPoint(firstpoint.x, firstpoint.y - OFFSET);

                pointList.addPoint(lastpoint.x, firstpoint.y - OFFSET);
                pointList.addPoint(lastpoint);
                alreadyHandle = true;
            } else if (targetBounds.getBottomLeft().y == sourceBounds.getTopRight().y) {
                pointList.addPoint(firstpoint);
                pointList.addPoint(firstpoint.x, firstpoint.y + OFFSET);

                pointList.addPoint(lastpoint.x, firstpoint.y + OFFSET);
                pointList.addPoint(lastpoint);
                alreadyHandle = true;
            } else if (Math.abs(sourceBounds.x - targetBounds.x) == 4 * OFFSET) {
                ((PolylineConnectionEx) conn).setRoundedBendpointsRadius(16);
            }
        }
        if (alreadyHandle && pointList.size() > 0) {
            conn.setPoints(pointList);
        }
        return alreadyHandle;
    }
}
