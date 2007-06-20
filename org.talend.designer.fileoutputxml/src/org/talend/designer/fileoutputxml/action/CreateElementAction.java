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
package org.talend.designer.fileoutputxml.action;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.actions.SelectionProviderAction;
import org.talend.designer.fileoutputxml.data.Attribute;
import org.talend.designer.fileoutputxml.data.Element;
import org.talend.designer.fileoutputxml.data.FOXTreeNode;
import org.talend.designer.fileoutputxml.ui.FOXUI;
import org.talend.designer.fileoutputxml.util.StringUtil;
import org.talend.designer.fileoutputxml.util.TreeUtil;

/**
 * bqian Create a xml node. <br/>
 * 
 * $Id: CreateElementAction.java,v 1.1 2007/06/12 07:20:38 gke Exp $
 * 
 */
public class CreateElementAction extends SelectionProviderAction {

    // the xml viewer, see FOXUI.
    private TreeViewer xmlViewer;

    private FOXUI foxui;

    /**
     * CreateNode constructor comment.
     * 
     * @param provider
     * @param text
     */
    public CreateElementAction(TreeViewer xmlViewer, String text) {
        super(xmlViewer, text);
        this.xmlViewer = xmlViewer;
    }

    public CreateElementAction(TreeViewer xmlViewer, FOXUI foxui, String text) {
        super(xmlViewer, text);
        this.xmlViewer = xmlViewer;
        this.foxui = foxui;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {
        FOXTreeNode node = (FOXTreeNode) this.getStructuredSelection().getFirstElement();
        createChildNode(node);
        if (TreeUtil.refreshTree((FOXTreeNode) xmlViewer.getTree().getItem(0).getData())) {
            xmlViewer.refresh();
        }
        foxui.redrawLinkers();
    }

    /**
     * Create the child node of the input node
     * 
     * @param node
     */
    private void createChildNode(FOXTreeNode node) {
        if (node.getColumn() != null) {
            if (!MessageDialog.openConfirm(xmlViewer.getControl().getShell(), "Warning",
                    "Do you want to disconnect the existing linker and then add an sub element for the selected element \""
                            + node.getLabel() + "\"?")) {
                return;
            }
            node.setColumn(null);
        }
        String label = "";
        while (!StringUtil.validateLabelForXML(label)) {
            InputDialog dialog = new InputDialog(null, "Input element's label", "Input the new element's valid label",
                    "DEFAULT", null);
            int status = dialog.open();
            if (status == InputDialog.OK) {
                label = dialog.getValue().trim();
            }
            if (status == InputDialog.CANCEL) {
                return;
            }
        }
        FOXTreeNode child = new Element(label);
        node.addChild(child);
        this.xmlViewer.refresh(node);
        this.xmlViewer.expandAll();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.actions.SelectionProviderAction#selectionChanged(org.eclipse.jface.viewers.IStructuredSelection)
     */
    @Override
    public void selectionChanged(IStructuredSelection selection) {
        FOXTreeNode node = (FOXTreeNode) this.getStructuredSelection().getFirstElement();
        if (node == null) {
            this.setEnabled(false);
            return;
        }
        if (node instanceof Attribute) {
            this.setEnabled(false);
            return;
        }
        Element e = (Element) node;
        if (e.getParent() == null) {
            if (e.getElementChildren().size() >= 1) {
                this.setEnabled(false);
                return;
            }
        }
        this.setEnabled(true);

    }
}
