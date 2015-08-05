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

import java.util.List;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.actions.SelectionProviderAction;
import org.talend.designer.hl7.ui.HL7UI;
import org.talend.designer.hl7.ui.data.Attribute;
import org.talend.designer.hl7.ui.data.Element;
import org.talend.designer.hl7.ui.data.HL7TreeNode;
import org.talend.repository.ui.swt.utils.AbstractForm;

/**
 * bqian Create a xml node. <br/>
 * 
 * $Id: CreateAttributeAction.java,v 1.1 2007/06/12 07:20:38 gke Exp $
 * 
 */
public class CreateHL7AttributeAction extends SelectionProviderAction {

    // the xml viewer, see HL7UI.
    private TreeViewer xmlViewer;

    private HL7UI hl7ui;

    private AbstractForm from;

    /**
     * CreateNode constructor comment.
     * 
     * @param provider
     * @param text
     */
    public CreateHL7AttributeAction(TreeViewer xmlViewer, String text) {
        super(xmlViewer, text);
        this.xmlViewer = xmlViewer;
    }

    public CreateHL7AttributeAction(TreeViewer xmlViewer, String text, AbstractForm from) {
        super(xmlViewer, text);
        this.xmlViewer = xmlViewer;
        this.from = from;
    }

    public CreateHL7AttributeAction(TreeViewer xmlViewer, HL7UI hl7ui, String text) {
        super(xmlViewer, text);
        this.xmlViewer = xmlViewer;
        this.hl7ui = hl7ui;
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
        if (node instanceof Element) {
            if (((Element) node).getAttributeChildren().size() > 0) {
                this.setEnabled(false);
                return;
            }

            String nodeName = node.getLabel() + "-";
            List<HL7TreeNode> childs = ((Element) node).getElementChildren();
            if (childs.size() > 0) {
                for (HL7TreeNode child : childs) {
                    if (!child.getLabel().startsWith(nodeName)) {
                        this.setEnabled(false);
                        return;
                    }
                }
            } else {
                this.setEnabled(false);
                return;
            }
        }

        if (node.getClass() != Element.class) {
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
        if (node != null) {
            createChildNode(node);
        }
    }

    /**
     * Create the child node of the input node
     * 
     * @param node
     */
    private void createChildNode(HL7TreeNode node) {
        String label = ""; //$NON-NLS-1$
        while (!StringUtil.validateLabelForXML(label)) {
            InputDialog dialog = new InputDialog(null, "Input attribute's label", "Input the new attribute's valid label", "",
                    null);
            int status = dialog.open();
            if (status == InputDialog.OK) {
                label = dialog.getValue().trim();
            }
            if (status == InputDialog.CANCEL) {
                return;
            }
        }
        HL7TreeNode child = new Attribute(label);
        child.setRow(node.getRow());
        node.addChild(child);
        this.xmlViewer.refresh();
        xmlViewer.expandAll();
        if (hl7ui != null) {
            hl7ui.redrawLinkers();
        } else if (from != null) {
            from.refreshLinks();
        }
    }

}
