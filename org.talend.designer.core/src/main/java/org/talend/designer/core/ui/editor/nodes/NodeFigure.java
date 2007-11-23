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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.AnchorListener;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.talend.commons.utils.workbench.gef.SimpleHtmlFigure;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.INodeConnector;
import org.talend.designer.core.DesignerPlugin;
import org.talend.designer.core.ui.editor.connections.ConnectionFigure;
import org.talend.designer.core.ui.preferences.TalendDesignerPrefConstants;

/**
 * This class create a figure with the given image. <br/>eh
 * 
 * $Id$
 * 
 */
public class NodeFigure extends Figure {

    private final ImageFigure fig;

    private final ImageDescriptor im;

    private final SimpleHtmlFigure hint;

    private int alpha = -1;

    private final NodeBorder lineBorder = new NodeBorder();

    private Map<ConnectionFigure, ConnectionFigure> sourceDummyMap;

    private Map<ConnectionFigure, AnchorListener> listenerSourceDummyMap = new HashMap<ConnectionFigure, AnchorListener>();

    private boolean dummy;

    public static final Color START_COLOR = new Color(null, new RGB(0xB0, 0xE7, 0));

    private ConnectionFigure targetConnection;

    private ConnectionFigure targetDummy;

    private AnchorListener targetListener;

    private List<ConnectionFigure> newSourceConnections = new ArrayList<ConnectionFigure>();

    private boolean newTargetConnection;

    // private List<ConnectionFigure> sourceConnectionList = new ArrayList<ConnectionFigure>();

    private Node node;

    public NodeFigure(Node node) {
        this.node = node;
        fig = new ImageFigure();
        im = node.getIcon32();
        fig.setImage(im.createImage());
        fig.setSize(new Dimension(Node.DEFAULT_SIZE, Node.DEFAULT_SIZE));
        add(fig);

        INodeConnector mainNodeConnector = node.getConnectorFromType(EConnectionType.FLOW_MAIN);
        if (mainNodeConnector != null) {
            sourceDummyMap = new HashMap<ConnectionFigure, ConnectionFigure>();
        }
        this.setSize(node.getSize());
        this.setOpaque(false);
        hint = new SimpleHtmlFigure();
        this.setBorder(lineBorder);
    }

    public void setHint(String hintText) {
        if (hintText.equals("")) { //$NON-NLS-1$
            setToolTip(null);
        } else {
            hint.setText(hintText);
            setToolTip(hint);
        }
    }

    public ImageDescriptor getImageDescriptor() {
        return im;
    }

    @Override
    public void setBounds(final Rectangle rect) {
        super.setBounds(rect);

        Point location = (new Point(rect.getCenter()))
                .translate(new Point(-fig.getSize().width / 2, -(fig.getSize().height / 2)));
        Rectangle figBounds = new Rectangle(location, fig.getSize());
        this.fig.setBounds(figBounds);
        if (dummy) {
            if (sourceDummyMap != null) {
                if (targetConnection != null && sourceDummyMap.keySet().size() != 0) {
                    for (final ConnectionFigure curConn : newSourceConnections) {
                        AnchorListener listener = new AnchorListener() {

                            public void anchorMoved(ConnectionAnchor anchor) {
                                if (curConn != null) {
                                    ConnectionFigure connection = sourceDummyMap.get(curConn);
                                    curConn.getConnectionRouter().route(curConn);
                                    Point endPoint = curConn.getStart();
                                    if (!endPoint.equals(connection.getEnd())) {
                                        connection.setEnd(endPoint);
                                    }
                                }
                            }

                        };
                        if (curConn.getTargetAnchor() != null) {
                            curConn.getTargetAnchor().addAnchorListener(listener);
                        }
                    }

                    newSourceConnections.clear();
                    newTargetConnection = false;

                    Point figCenter = fig.getBounds().getCenter();

                    if (targetConnection.getTargetAnchor() == null) {
                        targetDummy.setVisible(false);
                    } else {
                        targetConnection.getConnectionRouter().route(targetConnection);
                        Point startPoint = targetConnection.getEnd();
                        if (!startPoint.equals(targetDummy.getStart())) {
                            targetDummy.setStart(startPoint);
                        }
                        if (!figCenter.equals(targetDummy.getEnd())) {
                            targetDummy.setEnd(figCenter);
                        }
                    }

                    List<ConnectionFigure> toRemove = new ArrayList<ConnectionFigure>();
                    for (ConnectionFigure curConn : sourceDummyMap.keySet()) {
                        ConnectionFigure connection = sourceDummyMap.get(curConn);
                        if (curConn.getTargetAnchor() == null || curConn.getSourceAnchor() == null) {
                            toRemove.add(curConn);
                            connection.setVisible(false);
                            continue;
                        }
                        curConn.getConnectionRouter().route(curConn);
                        Point endPoint = curConn.getStart();
                        if (!figCenter.equals(connection.getStart())) {
                            connection.setStart(figCenter);
                        }
                        if (!endPoint.equals(connection.getEnd())) {
                            connection.setEnd(endPoint);
                        }
                    }
                    sourceDummyMap.keySet().removeAll(toRemove);

                } else {
                    INodeConnector mainNodeConnector = node.getConnectorFromType(EConnectionType.FLOW_MAIN);
                    ConnectionFigure connection = new ConnectionFigure(mainNodeConnector
                            .getConnectionProperty(EConnectionType.FLOW_MAIN));
                    connection.setTargetDecoration(null);
                    connection.setStart(new Point(figBounds.x, figBounds.y + figBounds.height / 2));
                    connection.setEnd(new Point(figBounds.x + figBounds.width, figBounds.y + figBounds.height / 2));
                }
            }
        }
        if (!rect.getSize().equals(fig.getSize())) {
            lineBorder.setUseRectangle(true);
        } else {
            lineBorder.setUseRectangle(false);
        }
    }

    @Override
    public void paint(Graphics graphics) {
        if (alpha != -1) {
            graphics.setAlpha(alpha);
        }
        if (DesignerPlugin.getDefault().getPreferenceStore().getBoolean(TalendDesignerPrefConstants.EDITOR_ANTIALIASING)) {
            graphics.setInterpolation(SWT.HIGH);
        }
        super.paint(graphics);
    }

    public void setDummy(boolean value) {
        dummy = value;
        if (sourceDummyMap != null) {
            if (dummy) {
                for (ConnectionFigure connection : sourceDummyMap.values()) {
                    connection.setAlpha(255);
                    connection.setVisible(true);
                }
            } else {
                for (ConnectionFigure connection : sourceDummyMap.values()) {
                    connection.setVisible(false);
                }
            }
        }
        if (targetDummy != null) {
            if (dummy) {
                targetDummy.setAlpha(255);
                targetDummy.setVisible(true);
            } else {
                targetDummy.setVisible(false);
            }
        }
    }

    public int getAlpha() {
        return this.alpha;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    public void setStart(boolean start) {
        if (start) {
            setBackgroundColor(START_COLOR);
            setOpaque(true);
        } else {
            setOpaque(false);
        }
    }

    /**
     * DOC nrousseau NodeFigure class global comment. Detailled comment <br/>
     * 
     */
    class NodeBorder extends AbstractBorder {

        private boolean useRectangle;

        public Insets getInsets(IFigure figure) {
            return null;
        }

        public void paint(IFigure figure, Graphics g, Insets theInsets) {
            Rectangle r = getPaintRectangle(figure, theInsets);

            if (useRectangle) {
                g.setLineWidth(2);
                g.setForegroundColor(Display.getDefault().getSystemColor(SWT.COLOR_DARK_GRAY));
                g.drawRectangle(r);
                g.drawLine(r.x, 1, r.right(), 1);
                g.drawLine(1, r.y, 1, r.bottom());

                g.setForegroundColor(Display.getDefault().getSystemColor(SWT.COLOR_GRAY));
                g.drawLine(r.x, r.bottom() - 1, r.right(), r.bottom() - 1);
                g.drawLine(r.right() - 1, r.y, r.right() - 1, r.bottom());
            }

        }

        public void setUseRectangle(boolean useRectangle) {
            this.useRectangle = useRectangle;
        }

    }

    /**
     * Sets the startConnection.
     * 
     * @param startConnection the startConnection to set
     */
    public void addSourceConnection(ConnectionFigure sourceConnection) {
        if (!sourceDummyMap.keySet().contains(sourceConnection)) {
            ConnectionFigure connection = new ConnectionFigure(sourceConnection.getConnectionProperty());
            connection.setTargetDecoration(null);
            add(connection);
            if (dummy) {
                connection.setAlpha(255);
                connection.setVisible(true);
            } else {
                connection.setVisible(false);
            }
            this.sourceDummyMap.put(sourceConnection, connection);
            newSourceConnections.add(sourceConnection);
        }
    }

    /**
     * Sets the endConnection.
     * 
     * @param endConnection the endConnection to set
     */
    public void setTargetConnection(ConnectionFigure targetConnection) {
        this.targetConnection = targetConnection;
        ConnectionFigure connection = new ConnectionFigure(targetConnection.getConnectionProperty());
        connection.setTargetDecoration(null);
        add(connection);
        if (dummy) {
            connection.setAlpha(255);
            connection.setVisible(true);
        } else {
            connection.setVisible(false);
        }
        targetDummy = connection;
        newTargetConnection = true;
    }
}
