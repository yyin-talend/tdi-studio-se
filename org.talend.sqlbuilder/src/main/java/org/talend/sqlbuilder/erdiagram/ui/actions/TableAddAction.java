// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
//
// ============================================================================
package org.talend.sqlbuilder.erdiagram.ui.actions;

import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.sqlbuilder.Messages;
import org.talend.sqlbuilder.erdiagram.ui.AddTablesDialog;
import org.talend.sqlbuilder.erdiagram.ui.ErDiagramComposite;
import org.talend.sqlbuilder.erdiagram.ui.commands.CreateTableCommand;
import org.talend.sqlbuilder.erdiagram.ui.editor.ErdiagramDiagramEditor;
import org.talend.sqlbuilder.erdiagram.ui.nodes.ErDiagram;
import org.talend.sqlbuilder.repository.utility.SQLBuilderRepositoryNodeManager;

/**
 * DOC qzhang class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ææäº, 29 ä¹æ 2006) nrousseau $
 * 
 */
public class TableAddAction extends SelectionAction {

    public static final String TABLE_ADD = Messages.getString("TableAddAction.textAddTables"); //$NON-NLS-1$

    private IWorkbenchPart part;

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#getId()
     */
    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return TABLE_ADD;
    }

    /**
     * DOC admin TableAddAction constructor comment.
     * 
     * @param part
     */
    public TableAddAction(IWorkbenchPart part) {
        super(part);
        this.part = part;
        setText(TABLE_ADD);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#calculateEnabled()
     */
    @Override
    protected boolean calculateEnabled() {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    @SuppressWarnings("unchecked") //$NON-NLS-1$
    @Override
    public void run() {
        ErdiagramDiagramEditor erdiagramDiagramEditor = (ErdiagramDiagramEditor) part;
        Control control = erdiagramDiagramEditor.getGraphicalControl();
        AddTablesDialog dialog = new AddTablesDialog(new Shell(control.getShell()));
        if (control.getParent() instanceof ErDiagramComposite) {
            ErDiagramComposite parentComposite = (ErDiagramComposite) control.getParent();
            dialog.setRootNode(SQLBuilderRepositoryNodeManager.getRoot(parentComposite.getNodes().get(0)));
        }
        if (Window.OK == dialog.open()) {
            List tables1 = dialog.getTables();
            this.execute(createCreateCommand((ErDiagram) erdiagramDiagramEditor.getViewer().getContents().getModel(), tables1));
        }
    }

    public void update() {
        setSelection(((ErdiagramDiagramEditor) part).getViewer().getSelection());
    }

    private Command createCreateCommand(ErDiagram erDiagram, List<MetadataTable> tables) {
        Command cmd = new CreateTableCommand(erDiagram, tables);
        return cmd;
    }
}
