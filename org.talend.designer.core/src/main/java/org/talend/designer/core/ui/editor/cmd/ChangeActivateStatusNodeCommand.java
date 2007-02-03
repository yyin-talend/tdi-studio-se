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
package org.talend.designer.core.ui.editor.cmd;

import org.eclipse.gef.commands.Command;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.PropertySheet;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.process.Process;

/**
 * Command that will set or remove the start status on a node. <br/>
 * 
 * $Id$
 * 
 */
public class ChangeActivateStatusNodeCommand extends Command {

    private Node node;

    private boolean value;

    /**
     * Gives the node where the status will be set or removed.
     * 
     * @param newStartNode
     */
    public ChangeActivateStatusNodeCommand(Node node) {
        this.node = node;
        if (node.isActivate()) {
            value = false;
            setLabel(Messages.getString("ChangeActivateStatusNodeCommand.0")); //$NON-NLS-1$
        } else {
            value = true;
            setLabel(Messages.getString("ChangeActivateStatusNodeCommand.1")); //$NON-NLS-1$
        }
    }

    private void refreshPropertyView() {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IViewPart view = page.findView("org.eclipse.ui.views.PropertySheet"); //$NON-NLS-1$
        PropertySheet sheet = (PropertySheet) view;
        TabbedPropertySheetPage tabbedPropertySheetPage = (TabbedPropertySheetPage) sheet.getCurrentPage();
        tabbedPropertySheetPage.refresh();
    }

    public void execute() {
        Process process = (Process) node.getProcess();
        process.setActivate(node, value);
        process.checkStartNodes();
        process.checkProcess();
        refreshPropertyView();
    }

    public void undo() {
        Process process = (Process) node.getProcess();
        process.setActivate(node, !value);
        process.checkStartNodes();
        process.checkProcess();
        refreshPropertyView();
    }

    public void redo() {
        this.execute();
    }
}
