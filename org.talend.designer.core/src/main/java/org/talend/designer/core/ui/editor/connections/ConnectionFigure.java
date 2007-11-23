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
package org.talend.designer.core.ui.editor.connections;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.swt.graphics.Color;
import org.talend.core.model.process.IConnectionProperty;

/**
 * Figure corresponding the the connection. <br/>
 * 
 * $Id$
 * 
 */
public class ConnectionFigure extends PolylineConnection {

    private int alpha = -1;

    private IConnectionProperty connectionProperty;

    public ConnectionFigure(IConnectionProperty connectionProperty) {
        setTargetDecoration(new PolygonDecoration());
        setConnectionProperty(connectionProperty);
    }

    @Override
    public void paint(Graphics graphics) {
        if (alpha != -1) {
            graphics.setAlpha(alpha);
        }
        super.paint(graphics);
    }

    public int getAlpha() {
        return this.alpha;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    protected void setConnectionProperty(IConnectionProperty connectionProperty) {
        this.connectionProperty = connectionProperty;
        setLineStyle(connectionProperty.getLineStyle());
        setForegroundColor(new Color(null, connectionProperty.getRGB()));
    }

    /**
     * Getter for connectionProperty.
     * 
     * @return the connectionProperty
     */
    public IConnectionProperty getConnectionProperty() {
        return this.connectionProperty;
    }

}
