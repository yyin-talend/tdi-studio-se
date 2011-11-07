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

import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IWorkbenchPart;
import org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode;
import org.talend.designer.xmlmap.parts.TreeNodeEditPart;
import org.talend.designer.xmlmap.ui.tabs.MapperManager;
import org.talend.designer.xmlmap.util.XmlMapUtil;

/**
 * DOC hcyi class global comment. Detailled comment
 */
public class RenameTreeNodeAction extends SelectionAction {

    private TreeNode selectedNode;

    private MapperManager mapperManager;

    public static final String ID = "org.talend.designer.xmlmap.editor.actions.RenameTreeNodeAction";

    public RenameTreeNodeAction(IWorkbenchPart part) {
        super(part);
        setId(ID);
        setText("Rename");
    }

    @Override
    public void run() {
        if (selectedNode != null) {
            InputDialog dialog = new InputDialog(null, "Rename Tree Node", "", selectedNode.getName(), null);
            if (dialog.open() == Window.OK) {
                selectedNode.setName(dialog.getValue());
            }
        }
    }

    @Override
    protected boolean calculateEnabled() {
        if (getSelectedObjects().isEmpty()) {
            return false;
        } else {
            boolean enable = true;
            for (Object obj : getSelectedObjects()) {
                if (obj instanceof TreeNodeEditPart) {
                    TreeNodeEditPart nodePart = (TreeNodeEditPart) obj;
                    this.selectedNode = (TreeNode) nodePart.getModel();
                    int xPathLength = XmlMapUtil.getXPathLength(selectedNode.getXpath());
                    if (xPathLength <= 2) {
                        enable = false;
                    }
                    if (selectedNode.eContainer() instanceof TreeNode
                            && XmlMapUtil.DOCUMENT.equals(((TreeNode) selectedNode.eContainer()).getType())) {
                        enable = false;
                    }

                } else {
                    enable = false;
                }
            }
            return enable;
        }
    }

    public void update(Object selection) {
        setSelection(new StructuredSelection(selection));
    }

    public MapperManager getMapperManager() {
        return this.mapperManager;
    }

    public void setMapperManager(MapperManager mapperManager) {
        this.mapperManager = mapperManager;
    }
}
