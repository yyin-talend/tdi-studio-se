// ============================================================================
//
// Copyright (C) 2006-2010 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.xmlmap.commands;

import org.eclipse.gef.commands.Command;
import org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode;

/**
 * wchen class global comment. Detailled comment
 */
public class CreateTreeNodeCommand extends Command {

    private TreeNode parent, newNode;

    public CreateTreeNodeCommand(TreeNode parent, TreeNode newNode) {
        this.parent = parent;
        this.newNode = newNode;
    }

    @Override
    public void execute() {
        parent.getChildren().add(newNode);
    }

    @Override
    public void undo() {
        parent.getChildren().remove(newNode);
    }

    @Override
    public void redo() {
        execute();
    }

}
