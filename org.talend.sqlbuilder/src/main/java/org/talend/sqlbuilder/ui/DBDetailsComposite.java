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
package org.talend.sqlbuilder.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.talend.sqlbuilder.Messages;
import org.talend.sqlbuilder.dbdetail.DetailTabManager;
import org.talend.sqlbuilder.dbstructure.nodes.INode;

/**
 * Show the details informaiton of DB selected in the DB structure composite <br/>.
 * 
 * $Id: DBDetailsComposite.java,v 1.13 2006/11/01 06:56:31 peiqin.hou Exp $
 * 
 */

public class DBDetailsComposite extends Composite {

    private Composite panel;

    public DBDetailsComposite(Composite parent, int style) {
        super(parent, style);

        setLayout(new FillLayout());

        panel = new Composite(this, SWT.NONE);

        panel.setLayout(new FillLayout());
        setSelectedNode(null, null);
    }

    /**
     * DOC qianbing Comment method "setSelectedNode". Displays the message input,if inputs null,it will show default
     * message.
     * 
     * @param node INode
     * @param message String
     */
    public void setSelectedNode(INode node, String message) {
        Composite parent = panel.getParent();
        panel.dispose();
        panel = new Composite(parent, SWT.NONE);
        panel.setLayout(new FillLayout());

        if (node == null) {
            Label label = new Label(panel, SWT.FILL | SWT.WRAP);
            if (message == null) {
                // add default message
                message = Messages.getString("DatabaseDetailView.NoSelection"); //$NON-NLS-1$
            }
            label.setText(message);
        } else {
            // add tabs
            DetailTabManager.createTabs(panel, node);
        }

        panel.layout();
        panel.getParent().layout();
        panel.redraw();
    }
}
