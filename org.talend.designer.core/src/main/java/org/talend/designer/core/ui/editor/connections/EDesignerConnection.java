// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
//
// ============================================================================
package org.talend.designer.core.ui.editor.connections;

import org.eclipse.draw2d.Graphics;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.talend.core.model.process.EConnectionType;
import org.talend.designer.core.i18n.Messages;

/**
 * Enumeration that will store all graphical parts for the connections.
 * 
 * $Id$
 * 
 */
public enum EDesignerConnection {
    FLOW_MAIN(EConnectionType.FLOW_MAIN, "Main", Messages.getString("EDesignerConnection.main"), new Integer(Graphics.LINE_SOLID), new Color(null, new RGB(230, //$NON-NLS-1$ //$NON-NLS-2$
            100, 0))),
    FLOW_REF(EConnectionType.FLOW_REF, "Lookup", Messages.getString("EDesignerConnection.lookup"), new Integer(Graphics.LINE_DOT), new Color(null, new RGB(230, //$NON-NLS-1$ //$NON-NLS-2$
            100, 0))),
    RUN_BEFORE(EConnectionType.RUN_BEFORE, "RunBefore", Messages.getString("EDesignerConnection.runBefore"), new Integer(Graphics.LINE_SOLID), new Color(null, //$NON-NLS-1$ //$NON-NLS-2$
            new RGB(100, 100, 100))),
    RUN_AFTER(EConnectionType.RUN_AFTER, "RunAfter", Messages.getString("EDesignerConnection.runAfter"), new Integer(Graphics.LINE_SOLID), new Color(null, //$NON-NLS-1$ //$NON-NLS-2$
            new RGB(100, 100, 100))),
    REFERENCE(EConnectionType.REFERENCE, "Ref", Messages.getString("EDesignerConnection.reference"), new Integer(Graphics.LINE_DASHDOTDOT), new Color(null, //$NON-NLS-1$ //$NON-NLS-2$
            new RGB(150, 150, 0))),
    RUN_IF_OK(EConnectionType.RUN_IF_OK, "OnOk", Messages.getString("EDesignerConnection.runIfOK"), new Integer(Graphics.LINE_SOLID), new Color(null, //$NON-NLS-1$ //$NON-NLS-2$
            new RGB(0, 150, 0))),
    RUN_IF_ERROR(EConnectionType.RUN_IF_ERROR, "OnError", Messages.getString("EDesignerConnection.runIfError"), new Integer(Graphics.LINE_SOLID), new Color( //$NON-NLS-1$ //$NON-NLS-2$
            null, new RGB(200, 0, 0))),
    RUN_IF(EConnectionType.RUN_IF, "If", Messages.getString("EDesignerConnection.RunIf"), new Integer(Graphics.LINE_DASHDOTDOT), new Color(null, new RGB(180, //$NON-NLS-1$ //$NON-NLS-2$
            100, 30))),
    ITERATE(EConnectionType.ITERATE, "Iterate", Messages.getString("EDesignerConnection.Iterator"), new Integer(Graphics.LINE_SOLID), new Color(null, new RGB( //$NON-NLS-1$ //$NON-NLS-2$
            100, 230, 0)));

    private EConnectionType connectionType;

    private Color color;

    private Integer lineStyle;

    private String linkName, menuName;

    private EDesignerConnection(EConnectionType connectionType, String linkName, String menuName, Integer lineStyle,
            Color color) {
        this.connectionType = connectionType;
        this.color = color;
        this.lineStyle = lineStyle;
        this.linkName = linkName;
        this.menuName = menuName;
    }

    public static EDesignerConnection getConnection(EConnectionType type) {
        for (EDesignerConnection connection : EDesignerConnection.values()) {
            if (connection.getConnectionType().equals(type)) {
                return connection;
            }
        }
        // Default Value
        return EDesignerConnection.FLOW_MAIN;
    }

    /**
     * Getter for color.
     * 
     * @return the color
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Sets the color.
     * 
     * @param color the color to set
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Getter for connectionType.
     * 
     * @return the connectionType
     */
    public EConnectionType getConnectionType() {
        return this.connectionType;
    }

    /**
     * Sets the connectionType.
     * 
     * @param connectionType the connectionType to set
     */
    public void setConnectionType(EConnectionType connectionType) {
        this.connectionType = connectionType;
    }

    /**
     * Getter for lineStyle.
     * 
     * @return the lineStyle
     */
    public Integer getLineStyle() {
        return this.lineStyle;
    }

    /**
     * Sets the lineStyle.
     * 
     * @param lineStyle the lineStyle to set
     */
    public void setLineStyle(Integer lineStyle) {
        this.lineStyle = lineStyle;
    }

    public String getLinkName() {
        return this.linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public String getMenuName() {
        return this.menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

}
