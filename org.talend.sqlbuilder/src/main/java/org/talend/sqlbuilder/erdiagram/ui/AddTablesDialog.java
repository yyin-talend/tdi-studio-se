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
package org.talend.sqlbuilder.erdiagram.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.properties.DatabaseConnectionItem;
import org.talend.repository.model.RepositoryNode;
import org.talend.sqlbuilder.repository.utility.SQLBuilderRepositoryNodeManager;

/**
 * DOC qzhang class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ææäº, 29 ä¹æ 2006) nrousseau $
 * 
 */
public class AddTablesDialog extends Dialog {

    private AddTablesComposite composite1;

    private RepositoryNode rootNode;

    private List<MetadataTable> tables;

    private List<MetadataTable> selectedTables;

    /**
     * DOC admin AddTablesDialog constructor comment.
     * 
     * @param parentShell
     */
    public AddTablesDialog(Shell parentShell) {
        super(parentShell);
        setShellStyle(SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM | SWT.RESIZE | SWT.RESIZE | SWT.MIN | SWT.MAX);

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
     */
    @Override
    protected void configureShell(Shell newShell) {
        // TODO Auto-generated method stub
        super.configureShell(newShell);
        newShell.setText("Add Tables");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Control createDialogArea(Composite parent) {
        // TODO Auto-generated method stub
        Composite container = (Composite) super.createDialogArea(parent);
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        layout.marginLeft = 0;
        layout.horizontalSpacing = 0;
        layout.verticalSpacing = 0;
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        container.setLayout(layout);
        composite1 = new AddTablesComposite(container, SWT.BORDER);
        composite1.setRootNode(getRootNode());

        return container;
    }

    protected Point getInitialSize() {
        return new Point(180, 300);
    }

    public RepositoryNode getRootNode() {
        return this.rootNode;
    }

    @SuppressWarnings("unchecked")
    public void setRootNode(RepositoryNode rootNode) {
        this.rootNode = rootNode;
        DatabaseConnectionItem item = SQLBuilderRepositoryNodeManager.getItem(getRootNode());
        tables = ((DatabaseConnection) item.getConnection()).getTables();
    }

    public List<MetadataTable> getTables() {
        return selectedTables;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#close()
     */
    @Override
    public boolean close() {
        setTables();
        return super.close();
    }

    /**
     * DOC admin Comment method "setTables".
     */
    private void setTables() {
        selectedTables = new ArrayList<MetadataTable>();
        for (String string : composite1.getListTables().getSelection()) {
            for (MetadataTable table1 : tables) {
                if (table1.getSourceName().equals(string)) {
                    selectedTables.add(table1);
                }
            }

        }
    }
}
