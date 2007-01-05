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

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Text;

/**
 * Manage the direct edit of a text in GEF. <br/>
 * 
 * $Id$
 * 
 */
public class NodeLabelEditManager extends DirectEditManager {

    Font scaledFont;

    public NodeLabelEditManager(GraphicalEditPart source, Class editorType, CellEditorLocator locator) {
        super(source, editorType, locator);
    }

    protected void bringDown() {
        Font disposeFont = scaledFont;
        scaledFont = null;
        super.bringDown();
        if (disposeFont != null) {
            disposeFont.dispose();
        }
    }

    protected void initCellEditor() {
        Connection connection = ((ConnectionLabel) this.getEditPart().getModel()).getConnection();
        if (getCellEditor() instanceof NodeLabelCellEditor) {
            ((NodeLabelCellEditor) getCellEditor()).setCurrentConnection(connection);
        }
        Label label = (Label) ((GraphicalEditPart) getEditPart()).getFigure();
        String initialLabelText = label.getText();
        getCellEditor().setValue(initialLabelText);
        Text text = (Text) getCellEditor().getControl();
        IFigure figure = ((GraphicalEditPart) getEditPart()).getFigure();
        scaledFont = figure.getFont();
        FontData data = scaledFont.getFontData()[0];
        Dimension fontSize = new Dimension(0, data.getHeight());
        label.translateToAbsolute(fontSize);
        data.setHeight(fontSize.height);
        scaledFont = new Font(null, data);
        text.setFont(scaledFont);
        text.selectAll();
    }
}
