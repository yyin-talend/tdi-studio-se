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

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.actions.SelectionProviderAction;
import org.talend.designer.fileoutputxml.data.Element;
import org.talend.designer.fileoutputxml.data.FOXTreeNode;
import org.talend.designer.fileoutputxml.data.NameSpaceNode;
import org.talend.designer.fileoutputxml.i18n.Messages;
import org.talend.designer.fileoutputxml.ui.FOXUI;
import org.talend.designer.fileoutputxml.util.StringUtil;

/**
 * 
 * $Id: CreateNameSpaceAction.java,v 1.1 2007/10/30 07:20:38 xzhang Exp $
 * 
 */
public class CreateNameSpaceAction extends SelectionProviderAction {

    // the xml viewer, see FOXUI.
    private TreeViewer xmlViewer;

    private FOXUI foxui;

    /**
     * CreateNode constructor comment.
     * 
     * @param provider
     * @param text
     */
    public CreateNameSpaceAction(TreeViewer xmlViewer, String text) {
        super(xmlViewer, text);
        this.xmlViewer = xmlViewer;
    }

    public CreateNameSpaceAction(TreeViewer xmlViewer, FOXUI foxui, String text) {
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
        if (node != null) {
            createChildNode(node);
        }
    }

    /**
     * Create the child node of the input node
     * 
     * @param node
     */
    private void createChildNode(FOXTreeNode node) {
        String label = null; //$NON-NLS-1$
        while (!StringUtil.validateLabelForXML(label)) {
            InputDialog dialog = new InputDialog(null, Messages.getString("CreateNameSpaceAction.1"), //$NON-NLS-1$
                    Messages.getString("CreateNameSpaceAction.2"), "", null); //$NON-NLS-1$ //$NON-NLS-2$
            int status = dialog.open();
            if (status == InputDialog.OK) {
                label = dialog.getValue().trim();
            }
            if (status == InputDialog.CANCEL) {
                return;
            }
        }
        FOXTreeNode child = new NameSpaceNode(label);
        node.addChild(child);
        this.xmlViewer.refresh(node);
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
        FOXTreeNode node = (FOXTreeNode) this.getStructuredSelection().getFirstElement();
        if (node != null && node.getClass() != Element.class) {
            this.setEnabled(false);
        } else {
            if (node == null) {
                this.setEnabled(false);
            } else {
                this.setEnabled(true);
            }
        }
    }
}
