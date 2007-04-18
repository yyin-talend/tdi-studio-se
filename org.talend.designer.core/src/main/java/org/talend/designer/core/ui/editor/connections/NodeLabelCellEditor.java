// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2007 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License.
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

import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IConnectionCategory;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 * $Id: talend-code-templates.xml 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
 * 
 */
public class NodeLabelCellEditor extends TextCellEditor {

    private static final Color ERROR_COLOR = new Color(null, new RGB(0xFF, 0, 0));

    private IConnection connection;

    public NodeLabelCellEditor() {
        super();
        // TODO Auto-generated constructor stub
    }

    public NodeLabelCellEditor(Composite parent, int style) {
        super(parent, style);
        // TODO Auto-generated constructor stub
    }

    public NodeLabelCellEditor(Composite parent) {
        super(parent);
        // TODO Auto-generated constructor stub
    }

    public void setCurrentConnection(IConnection connection) {
        this.connection = connection;
    }

    @Override
    protected void editOccured(ModifyEvent e) {
        super.editOccured(e);
        text.setBackground(null);
        if (connection != null) {
            if (text.getText() != null) {
                if (connection.getLineStyle().hasConnectionCategory(IConnectionCategory.FLOW)) {
                    if (!connection.getSource().getProcess().checkValidConnectionName(text.getText(), true)) {
                        text.setBackground(ERROR_COLOR);
                        return;
                    }
                }
                if (connection.getLineStyle().equals(EConnectionType.TABLE)) {
                    if (text.getText().equals("")) {
                        text.setBackground(ERROR_COLOR);
                        return;
                    }
                    List<? extends IConnection> cons = connection.getTarget().getIncomingConnections();
                    for (Iterator iter = cons.iterator(); iter.hasNext();) {
                        Connection conn = (Connection) iter.next();

                        if (conn.getName().equals(text.getText())) {
                            text.setBackground(ERROR_COLOR);
                            return;
                        }
                    }
                }
            }
        }
    }

}
