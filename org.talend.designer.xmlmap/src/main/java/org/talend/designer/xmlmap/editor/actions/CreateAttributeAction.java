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
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IWorkbenchPart;
import org.talend.designer.xmlmap.model.emf.xmlmap.InputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.NodeType;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapFactory;
import org.talend.designer.xmlmap.parts.TreeNodeEditPart;
import org.talend.designer.xmlmap.ui.tabs.MapperManager;
import org.talend.designer.xmlmap.util.XmlMapUtil;

/**
 * wchen class global comment. Detailled comment
 */
public class CreateAttributeAction extends SelectionAction {

    private TreeNode parent;

    private boolean input;

    private MapperManager mapperManager;

    public static final String ID = "org.talend.designer.xmlmap.editor.actions.CreateAttributeAction";

    public CreateAttributeAction(IWorkbenchPart part) {
        super(part);
        setId(ID);
        setText("Create Attribute");
    }

    @Override
    public void run() {
        TreeNode treeNode = null;
        if (input) {
            treeNode = XmlmapFactory.eINSTANCE.createTreeNode();
        } else {
            treeNode = XmlmapFactory.eINSTANCE.createOutputTreeNode();
        }

        InputDialog dialog = new InputDialog(null, "Create New Element", "Input the new element's valid label", "", null);
        int open = dialog.open();
        if (open == Window.OK) {
            treeNode.setName(dialog.getValue());
            treeNode.setNodeType(NodeType.ATTRIBUT);
            treeNode.setXpath(XmlMapUtil.getXPath(this.parent.getXpath(), treeNode.getName(), treeNode.getNodeType()));
            treeNode.setType(XmlMapUtil.DEFAULT_DATA_TYPE);
            final EList<TreeNode> children = parent.getChildren();
            int index = 0;
            for (int i = 0; i < children.size(); i++) {
                final TreeNode child = children.get(i);
                if (child.getNodeType() == NodeType.NAME_SPACE || child.getNodeType() == NodeType.ATTRIBUT) {
                    if (i == children.size() - 1) {
                        index = children.size();
                    }
                    continue;
                } else {
                    index = i;
                    break;
                }
            }
            children.add(index, treeNode);
        }
        if (open == Window.OK && mapperManager != null) {
            TreeNode docRoot = XmlMapUtil.getTreeNodeRoot(parent);
            if (input) {
                if (docRoot != null && docRoot.eContainer() instanceof InputXmlTree) {
                    mapperManager.refreshInputTreeSchemaEditor((InputXmlTree) docRoot.eContainer());
                }
            } else {
                if (docRoot != null && docRoot.eContainer() instanceof OutputXmlTree) {
                    mapperManager.refreshOutputTreeSchemaEditor((OutputXmlTree) docRoot.eContainer());
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

    public void update(Object selection) {
        setSelection(new StructuredSelection(selection));
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
