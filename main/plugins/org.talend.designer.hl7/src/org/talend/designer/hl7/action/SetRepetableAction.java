// ============================================================================
//
// Copyright (C) 2006-2015 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.hl7.action;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.actions.SelectionProviderAction;
import org.talend.designer.hl7.ui.HL7UI;
import org.talend.designer.hl7.ui.data.Attribute;
import org.talend.designer.hl7.ui.data.Element;
import org.talend.designer.hl7.ui.data.HL7TreeNode;
import org.talend.designer.hl7.ui.data.NameSpaceNode;
import org.talend.repository.ui.swt.utils.AbstractForm;

/**
 * bqian Create a xml node. <br/>
 * 
 * $Id: CreateElementAction.java,v 1.1 2007/06/12 07:20:38 gke Exp $
 * 
 */
public class SetRepetableAction extends SelectionProviderAction {

    // the xml viewer, see HL7UI.
    private TreeViewer xmlViewer;

    private HL7UI hl7ui;

    private boolean value;

    private AbstractForm form;

    /**
     * SetForLoopAction constructor comment.
     * 
     * @param provider
     * @param text
     */
    public SetRepetableAction(TreeViewer xmlViewer, String text) {
        super(xmlViewer, text);
        this.xmlViewer = xmlViewer;
    }

    public SetRepetableAction(TreeViewer xmlViewer, String text, AbstractForm form) {
        super(xmlViewer, text);
        this.xmlViewer = xmlViewer;
        this.form = form;
    }

    /**
     * 
     * SetForLoopAction constructor comment.
     * 
     * @param xmlViewer
     * @param text
     * @param hl7ui
     */
    public SetRepetableAction(TreeViewer xmlViewer, HL7UI hl7ui, String text, boolean value) {
        super(xmlViewer, text);
        this.xmlViewer = xmlViewer;
        this.hl7ui = hl7ui;
        this.value = value;
    }

    public void init() {
        HL7TreeNode node = (HL7TreeNode) this.getStructuredSelection().getFirstElement();
        if (node == null) {
            this.setEnabled(false);
            return;
        }
        if (node.getParent() == null) {
            this.setEnabled(false);
            return;
        }
        if (node.getParent().getParent() != null) {
            this.setEnabled(false);
            return;
        }
        if (node.isRepetable()) {
            this.setEnabled(false);
            return;
        }
        if (!node.isMain()) {
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
        this.setEnabled(true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {
        HL7TreeNode node = (HL7TreeNode) this.getStructuredSelection().getFirstElement();
        if (node.isRepetable()) {
            return;
        }

        // HL7Manager hl7Manager = hl7ui.gethl7Manager();

        // HL7TreeNode rootTreeData = hl7Manager.getRootHL7TreeNode(node);
        // TreeUtil.clearSubGroupNode(node);
        // // make sure group element is a ancestor of loop, or no group element.
        // if (TreeUtil.findUpGroupNode(node) == null) {
        // TreeUtil.clearSubGroupNode(rootTreeData);
        // }
        // TreeUtil.clearLoopNode(rootTreeData);
        // TreeUtil.clearMainNode(rootTreeData);

        if (node.isGroup()) {
            node.setGroup(false);
        }
        node.setRepetable(true);
        // if (this.value) {
        // if (hl7ui != null && node.isGroup()) {
        // // hl7ui.updateStatus();
        // }
        // // TreeUtil.upsetMainNode(node);
        // // xmlViewer.refresh();
        // } else {
        // // if (hl7ui != null) {
        // // hl7ui.updateStatus();
        // // }
        // // upsetMainNode(node);
        // // xmlViewer.refresh();
        // }
        upsetMainNode(node);
        xmlViewer.refresh();
        if (form != null) {
            form.refreshLinks();
        }

        // this.hl7ui.updateStatus();
    }

    public void upsetMainNode(HL7TreeNode node) {
        if (node instanceof Element) {
            HL7TreeNode parent = node;
            while (parent != null) {
                parent.setMain(true);
                parent = parent.getParent();
            }

        }
    }
}
