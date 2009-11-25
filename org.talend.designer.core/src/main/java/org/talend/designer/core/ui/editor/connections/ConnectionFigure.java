// ============================================================================
//
// Copyright (C) 2006-2009 Talend Inc. - www.talend.com
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

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.swt.graphics.Color;
import org.talend.commons.utils.ResourceDisposeUtil;
import org.talend.commons.utils.image.ColorUtils;
import org.talend.core.model.process.IConnectionCategory;
import org.talend.core.model.process.IConnectionProperty;
import org.talend.designer.core.ui.editor.nodes.Node;

/**
 * Figure corresponding the the connection. <br/>
 * 
 * $Id$
 * 
 */
public class ConnectionFigure extends PolylineConnection {

    private IConnectionProperty connectionProperty;

    private Node linkedNode;

    private Connection connection;

    /**
     * Used for standard connections.
     * 
     * @param connection
     * @param connectionProperty
     * @param node
     */
    public ConnectionFigure(Connection connection, IConnectionProperty connectionProperty, Node node) {
        linkedNode = node;
        this.connection = connection;
        setTargetDecoration(new PolygonDecoration());
        setConnectionProperty(connectionProperty);
    }

    /**
     * Only used for partial connections used for dummy state.
     * 
     * @param connectionProperty
     * @param node
     */
    public ConnectionFigure(IConnectionProperty connectionProperty, Node node) {
        this(null, connectionProperty, node);
    }

    @Override
    public void paint(Graphics graphics) {
        if (linkedNode.getNodeContainer().getSubjobContainer() != null
                && linkedNode.getNodeContainer().getSubjobContainer().isCollapsed() && connection != null
                && !connection.isSubjobConnection()) {

            Node subjobStartNode = linkedNode.getNodeContainer().getSubjobContainer().getSubjobStartNode();
            // only dependency links will be drawn
            if (!connection.getLineStyle().hasConnectionCategory(IConnectionCategory.DEPENDENCY)) {
                return;
            }
            if (!connection.getSource().equals(subjobStartNode) && !connection.getTarget().equals(subjobStartNode)) {
                return;
            }
            if (connection.getTarget().getDesignSubjobStartNode().equals(subjobStartNode)) {
                return;
            }
        }

        if (getAlpha() != null && getAlpha() != -1) {
            graphics.setAlpha(getAlpha());
        }
        super.paint(graphics);
    }

    protected void setConnectionProperty(IConnectionProperty connectionProperty) {
        this.connectionProperty = connectionProperty;
        setLineStyle(connectionProperty.getLineStyle());
        Color color = ColorUtils.getCacheColor(connectionProperty.getRGB());
        ResourceDisposeUtil.setAndCheckColor(this, color, true);
    }

    public void disposeColors() {
        // ResourceDisposeUtil.disposeColor(getForegroundColor());
    }

    /**
     * Getter for connectionProperty.
     * 
     * @return the connectionProperty
     */
    public IConnectionProperty getConnectionProperty() {
        return this.connectionProperty;
    }

    /**
     * Getter for connection.
     * 
     * @return the connection
     */
    public Connection getConnection() {
        return this.connection;
    }

}
