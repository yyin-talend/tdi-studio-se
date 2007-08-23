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
package org.talend.designer.core.ui.editor.cmd;

import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.PropertySheet;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.ui.editor.connections.Connection;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.process.Process;

/**
 * Command that will set or remove the start status on a node. <br/>
 * 
 * $Id: ChangeActivateStatusNodeCommand.java 3351 2007-05-04 12:14:00 +0000
 * (ven., 04 mai 2007) plegall $
 * 
 */
public class ChangeActivateStatusElementCommand extends Command {

	private List<Node> nodeList;

	private List<Connection> connectionList;

	private boolean value;

	/**
	 * Gives the node where the status will be set or removed.
	 * 
	 * @param connectionList
	 * 
	 * @param newStartNode
	 */
	public ChangeActivateStatusElementCommand(boolean activate,
			List<Node> nodeList, List<Connection> connectionList) {
		this.nodeList = nodeList;
		this.connectionList = connectionList;
		value = activate;
		if (activate) {
			setLabel(Messages
					.getString("ChangeActivateStatusNodeCommand.Label.Active")); //$NON-NLS-1$
		} else {
			setLabel(Messages
					.getString("ChangeActivateStatusNodeCommand.Label.Deactive")); //$NON-NLS-1$
		}
	}

	private void refreshPropertyView() {
		IWorkbenchPage page = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();
		IViewPart view = page.findView("org.eclipse.ui.views.PropertySheet"); //$NON-NLS-1$
		PropertySheet sheet = (PropertySheet) view;
		if (sheet.getCurrentPage() instanceof TabbedPropertySheetPage) {
			TabbedPropertySheetPage tabbedPropertySheetPage = (TabbedPropertySheetPage) sheet
					.getCurrentPage();
			if (tabbedPropertySheetPage.getCurrentTab() != null) {
				tabbedPropertySheetPage.refresh();
			}
		}
	}

	public void execute() {
		Process process;
		if (nodeList.size() > 0) {
			process = (Process) nodeList.get(0).getProcess();
		} else {
			process = (Process) connectionList.get(0).getSource().getProcess();
		}
		process.setActivate(false);
		for (Node node : nodeList) {
			// node.setActivate(value);
			node.setPropertyValue(EParameterName.ACTIVATE.getName(), value);
		}
		for (Connection connection : connectionList) {
			// connection.setActivate(value);
			connection.setPropertyValue(EParameterName.ACTIVATE.getName(),
					value);
		}
		process.setActivate(true);
		process.checkStartNodes();
		process.checkProcess();
		refreshPropertyView();
	}

	public void undo() {
		Process process;
		if (nodeList.size() > 0) {
			process = (Process) nodeList.get(0).getProcess();
		} else {
			process = (Process) connectionList.get(0).getSource().getProcess();
		}
		process.setActivate(false);
		for (Node node : nodeList) {
			// node.setActivate(!value);
			node.setPropertyValue(EParameterName.ACTIVATE.getName(), !value);
		}
		for (Connection connection : connectionList) {
			// connection.setActivate(!value);
			connection.setPropertyValue(EParameterName.ACTIVATE.getName(),
					!value);
		}
		process.setActivate(true);
		process.checkStartNodes();
		process.checkProcess();
		refreshPropertyView();
	}

	public void redo() {
		this.execute();
	}
}
