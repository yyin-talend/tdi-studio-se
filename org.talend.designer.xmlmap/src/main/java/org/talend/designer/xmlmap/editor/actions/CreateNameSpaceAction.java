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
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IWorkbenchPart;
import org.talend.designer.xmlmap.model.emf.xmlmap.InputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.NodeType;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapFactory;
import org.talend.designer.xmlmap.parts.TreeNodeEditPart;
import org.talend.designer.xmlmap.ui.dialog.NameSpaceDialog;
import org.talend.designer.xmlmap.ui.tabs.MapperManager;
import org.talend.designer.xmlmap.util.XmlMapUtil;

/**
 * DOC talend class global comment. Detailled comment
 */
public class CreateNameSpaceAction extends SelectionAction {

    private TreeNode parent;

    private MapperManager mapperManager;

    private boolean input;

    public static final String ID = "org.talend.designer.xmlmap.editor.actions.CreateNameSapceInput";

    public CreateNameSpaceAction(IWorkbenchPart part) {
        super(part);
        setText("Set A Namespace");
        setId(ID);
    }

    @Override
    public void run() {
        String prefix = null;
        String defaultValue = null;

        NameSpaceDialog nsDialog = new NameSpaceDialog(null);
        nsDialog.setParentNode(parent);
        int status = nsDialog.open();
        if (status == nsDialog.OK) {
            defaultValue = nsDialog.getNSValue();
            if (defaultValue != null) {
                defaultValue = defaultValue.trim();
            }
            prefix = nsDialog.getPrefix().trim();
        }
        if (status == nsDialog.CANCEL) {
            return;
        }

        TreeNode createdNode = null;
        if (input) {
            createdNode = XmlmapFactory.eINSTANCE.createTreeNode();
        } else {
            createdNode = XmlmapFactory.eINSTANCE.createOutputTreeNode();
        }
        createdNode.setName(prefix);
        createdNode.setNodeType(NodeType.NAME_SPACE);
        createdNode.setDefaultValue(defaultValue);
        createdNode.setType(XmlMapUtil.DEFAULT_DATA_TYPE);
        String label = createdNode.getName();
        if (prefix == null || "".equals(prefix)) {
            label = XmlMapUtil.DEFAULT_NAME_SPACE_PREFIX;
            createdNode.setName(XmlMapUtil.DEFAULT_NAME_SPACE_PREFIX);
        }

        createdNode.setXpath(XmlMapUtil.getXPath(parent.getXpath(), label, NodeType.NAME_SPACE));

        final EList<TreeNode> children = parent.getChildren();
        int index = 0;
        for (int i = 0; i < children.size(); i++) {
            final TreeNode treeNode = children.get(i);
            if (treeNode.getNodeType() == NodeType.NAME_SPACE) {
                if (i == children.size() - 1) {
                    index = children.size();
                }
                continue;
            } else {
                index = i;
                break;
            }
        }
        children.add(index, createdNode);

        // children.add(createdNode);

        TreeNode docRoot = XmlMapUtil.getTreeNodeRoot(parent);
        if (docRoot != null && docRoot.eContainer() instanceof OutputXmlTree) {
            mapperManager.refreshOutputTreeSchemaEditor((OutputXmlTree) docRoot.eContainer());
        } else if (docRoot != null && docRoot.eContainer() instanceof InputXmlTree) {
            mapperManager.refreshInputTreeSchemaEditor((InputXmlTree) docRoot.eContainer());
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
                if (isElement && !XmlMapUtil.DOCUMENT.equals(parent.getType())) {
                    return true;
                }
            }
        }

        return false;
    }

    public void update(Object selection) {
        setSelection(new StructuredSelection(selection));
    }

    public void setMapperManager(MapperManager mapperManager) {
        this.mapperManager = mapperManager;
    }

    public boolean isInput() {
        return input;
    }

    public void setInput(boolean input) {
        this.input = input;
    }

}
