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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.datatools.enablement.oda.xml.util.ui.ATreeNode;
import org.eclipse.datatools.enablement.oda.xml.util.ui.SchemaPopulationUtil;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.actions.SelectionProviderAction;
import org.talend.designer.fileoutputxml.data.Attribute;
import org.talend.designer.fileoutputxml.data.Element;
import org.talend.designer.fileoutputxml.data.FOXTreeNode;
import org.talend.designer.fileoutputxml.ui.FOXUI;

/**
 * bqian Create a xml node. <br/>
 * 
 * $Id: ImportTreeFromXMLAction.java,v 1.1 2007/06/12 07:20:38 gke Exp $
 * 
 */
public class ImportTreeFromXMLAction extends SelectionProviderAction {

    // the xml viewer, see FOXUI.
    private TreeViewer xmlViewer;

    private FOXUI foxui;

    /**
     * CreateNode constructor comment.
     * 
     * @param provider
     * @param text
     */
    public ImportTreeFromXMLAction(TreeViewer xmlViewer, String text) {
        super(xmlViewer, text);
        this.xmlViewer = xmlViewer;
    }

    public ImportTreeFromXMLAction(TreeViewer xmlViewer, FOXUI foxui, String text) {
        super(xmlViewer, text);
        this.xmlViewer = xmlViewer;
        this.foxui = foxui;
    }

    private List treeNodeAdapt() {
        List<FOXTreeNode> list = new ArrayList<FOXTreeNode>();
        FileDialog f = new FileDialog(foxui.getFoxUIParent().getShell());
        String file = f.open();
        if (file == null) {
            return list;
        }
        try {
            ATreeNode treeNode = SchemaPopulationUtil.getSchemaTree(file, true, 0);
            FOXTreeNode root = cloneATreeNode(treeNode);
            root = ((Element) root).getChildren().get(0);
            root.setParent(null);
            list.add(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private FOXTreeNode cloneATreeNode(ATreeNode treeNode) throws Exception {
        FOXTreeNode node = null;
        if (treeNode.getType() == ATreeNode.ATTRIBUTE_TYPE) {
            node = new Attribute();
        } else {
            node = new Element();
        }

        node.setLabel((String) treeNode.getValue());

        Object[] children = treeNode.getChildren();
        if (children != null) {
            for (int i = 0; i < children.length; i++) {
                ATreeNode child = (ATreeNode) children[i];
                FOXTreeNode foxChild = cloneATreeNode(child);
                node.addChild(foxChild);
            }
        }
        return node;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {
        List<FOXTreeNode> newInput = treeNodeAdapt();
        if (newInput.size() == 0) {
            return;
        }
        foxui.getFoxManager().setTreeData(newInput);
        xmlViewer.setInput(foxui.getFoxManager().getTreeData());
        // TreeUtil.guessAndSetLoopNode((FOXTreeNode) xmlViewer.getTree().getItem(0).getData());
        xmlViewer.refresh();
        xmlViewer.expandAll();
        foxui.redrawLinkers();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.actions.SelectionProviderAction#selectionChanged(org.eclipse.jface.viewers.IStructuredSelection)
     */
    @Override
    public void selectionChanged(IStructuredSelection selection) {
        this.setEnabled(true);
        FOXTreeNode node = (FOXTreeNode) this.getStructuredSelection().getFirstElement();
        if (node != null) {
            foxui.setSelectedText(node.getLabel());
        }
    }
}
