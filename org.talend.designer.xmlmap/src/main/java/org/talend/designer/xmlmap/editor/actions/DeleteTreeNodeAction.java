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
package org.talend.designer.xmlmap.editor.actions;

import org.eclipse.gef.ui.actions.DeleteAction;
import org.eclipse.ui.IWorkbenchPart;
import org.talend.designer.xmlmap.editor.XmlMapEditor;

/**
 * DOC talend class global comment. Detailled comment
 */
public class DeleteTreeNodeAction extends DeleteAction {

    public DeleteTreeNodeAction(IWorkbenchPart part) {
        super(part);
        // TODO Auto-generated constructor stub
    }

    public void update() {
        setSelection(((XmlMapEditor) getWorkbenchPart()).getViewer().getSelection());
    }

}
