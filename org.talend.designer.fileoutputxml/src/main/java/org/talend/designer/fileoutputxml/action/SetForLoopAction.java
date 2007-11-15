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
package org.talend.designer.fileoutputxml.action;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.actions.SelectionProviderAction;
import org.talend.designer.fileoutputxml.data.Attribute;
import org.talend.designer.fileoutputxml.data.FOXTreeNode;
import org.talend.designer.fileoutputxml.data.NameSpaceNode;
import org.talend.designer.fileoutputxml.util.TreeUtil;

/**
 * bqian Create a xml node. <br/>
 * 
 * $Id: CreateElementAction.java,v 1.1 2007/06/12 07:20:38 gke Exp $
 * 
 */
public class SetForLoopAction extends SelectionProviderAction {

    // the xml viewer, see FOXUI.
    private TreeViewer xmlViewer;

    /**
     * CreateNode constructor comment.
     * 
     * @param provider
     * @param text
     */
    public SetForLoopAction(TreeViewer xmlViewer, String text) {
        super(xmlViewer, text);
        this.xmlViewer = xmlViewer;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {
        FOXTreeNode node = (FOXTreeNode) this.getStructuredSelection().getFirstElement();
        if (node.isLoop()) {
            return;
        }

        TreeUtil.clearSubGroupNode(node);
        // make sure group element is a ancestor of loop, or no group element.
        if (TreeUtil.findUpGroupNode(node) == null) {
            TreeUtil.clearSubGroupNode((FOXTreeNode) xmlViewer.getTree().getItem(0).getData());
        }
        TreeUtil.clearLoopNode((FOXTreeNode) xmlViewer.getTree().getItem(0).getData());
        if (node.isGroup()) {
            node.setGroup(false);
        }
        node.setLoop(true);
        xmlViewer.refresh();
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

        if (node instanceof NameSpaceNode) {
            this.setEnabled(false);
            return;
        }
        this.setEnabled(TreeUtil.checkTreeLoopNode(node));
    }
}
