// ============================================================================
//
// Copyright (C) 2006-2011 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.xmlmap.editor.actions;

import org.eclipse.emf.common.util.EList;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IWorkbenchPart;
import org.talend.designer.xmlmap.editor.XmlMapEditor;
import org.talend.designer.xmlmap.model.emf.xmlmap.Connection;
import org.talend.designer.xmlmap.model.emf.xmlmap.InputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.NodeType;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputTreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapFactory;
import org.talend.designer.xmlmap.parts.TreeNodeEditPart;
import org.talend.designer.xmlmap.ui.tabs.MapperManager;
import org.talend.designer.xmlmap.util.XmlMapUtil;

/**
 * wchen class global comment. Detailled comment
 */
public class CreateElementAction extends SelectionAction {

    private TreeNode parent;

    private boolean input;

    private MapperManager mapperManager;

    public static final String ID = "org.talend.designer.xmlmap.editor.actions.CreateElementAction";

    public CreateElementAction(IWorkbenchPart part) {
        super(part);
        setId(ID);
        setText("Create Sub-Element");
    }

    @Override
    public void run() {
        TreeNode treeNode = null;
        boolean needWarning = false;
        if (input) {
            treeNode = XmlmapFactory.eINSTANCE.createTreeNode();
            if (!parent.getOutgoingConnections().isEmpty()) {
                needWarning = true;
            }
        } else {
            treeNode = XmlmapFactory.eINSTANCE.createOutputTreeNode();
            OutputTreeNode outputTreeNode = (OutputTreeNode) treeNode;
            EList<Connection> incomingConnections = parent.getIncomingConnections();
            if (!incomingConnections.isEmpty()) {
                needWarning = true;
            }
        }
        boolean canContinue = true;
        // Shell shell = this.part.getSite().getShell();
        if (needWarning) {
            canContinue = MessageDialog.openConfirm(null, "Warning",
                    "Do you want to disconnect the existing linker and then add an sub element for the selected element ?");
        }

        if (canContinue) {

            InputDialog dialog = new InputDialog(null, "Create New Element", "Input the new element's valid label", "", null);
            int open = dialog.open();
            if (open == Window.OK) {
                if (input) {
                    XmlMapUtil.detachConnectionsTarget(parent, mapperManager.getCopyOfMapData(), false);
                    XmlMapUtil.detachLookupSource(parent, mapperManager.getCopyOfMapData(), false);
                    XmlMapUtil.detachLookupTarget(parent, mapperManager.getCopyOfMapData(), false);
                } else {
                    XmlMapUtil.detachConnectionsSouce(parent, mapperManager.getCopyOfMapData(), false);
                }
                treeNode.setName(dialog.getValue());
                treeNode.setNodeType(NodeType.ELEMENT);
                treeNode.setXpath(XmlMapUtil.getXPath(this.parent.getXpath(), treeNode.getName(), treeNode.getNodeType()));
                treeNode.setType(XmlMapUtil.DEFAULT_DATA_TYPE);
                parent.getChildren().add(treeNode);
                parent.setExpression("");
            }

            if (open == Window.OK && mapperManager != null) {
                if (input) {
                    TreeNode docRoot = XmlMapUtil.getInputTreeNodeRoot(parent);
                    if (docRoot != null && docRoot.eContainer() instanceof InputXmlTree) {
                        mapperManager.refreshInputTreeSchemaEditor((InputXmlTree) docRoot.eContainer());
                    }
                } else {
                    TreeNode docRoot = XmlMapUtil.getOutputTreeNodeRoot((OutputTreeNode) parent);
                    if (docRoot != null && docRoot.eContainer() instanceof OutputXmlTree) {
                        mapperManager.refreshOutputTreeSchemaEditor((OutputXmlTree) docRoot.eContainer());
                    }
                }

            }
        }

    }

    @Override
    protected boolean calculateEnabled() {
        if (getSelectedObjects().isEmpty()) {
            return false;
        } else {
            Object object = getSelectedObjects().get(0);
            if (object instanceof TreeNodeEditPart) {
                TreeNodeEditPart nodePart = (TreeNodeEditPart) object;
                this.parent = (TreeNode) nodePart.getModel();
                boolean isElement = NodeType.ELEMENT.equals(parent.getNodeType());
                if (isElement && XmlMapUtil.getXPathLength(parent.getXpath()) > 2) {
                    return true;
                }
            }
        }

        return false;
    }

    public void update() {
        setSelection(((XmlMapEditor) getWorkbenchPart()).getViewer().getSelection());
    }

    public boolean isInput() {
        return input;
    }

    public void setInput(boolean input) {
        this.input = input;
    }

    public void setMapperManager(MapperManager mapperManager) {
        this.mapperManager = mapperManager;
    }
}
