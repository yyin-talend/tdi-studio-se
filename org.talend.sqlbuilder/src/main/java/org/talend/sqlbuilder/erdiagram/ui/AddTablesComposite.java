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

import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
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
public class AddTablesComposite extends Composite {

    private RepositoryNode rootNode;

    private org.eclipse.swt.widgets.List listTables;

    private CTabFolder tabFolder;

    private List<MetadataTable> tables;

    /**
     * DOC admin AddTablesComposite constructor comment.
     * 
     * @param parent
     * @param style
     */
    public AddTablesComposite(Composite parent, int style) {
        super(parent, style);
        this.setLayout(new GridLayout());
        this.setLayoutData(new GridData(GridData.FILL_BOTH));

    }

    private void createTabFolder() {

        if (tabFolder == null || tabFolder.isDisposed()) {

            clearParent();

            // create tab folder for different sessions
            tabFolder = new CTabFolder(this, SWT.NULL | SWT.BORDER);
            tabFolder.setLayout(new GridLayout());
            tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));
            tabFolder.setSimple(false);
            this.layout();
            this.redraw();
        }
    }

    private void clearParent() {

        Control[] children = this.getChildren();
        if (children != null) {
            for (int i = 0; i < children.length; i++) {
                children[i].dispose();
            }
        }
    }

    private void createTabItem() {
        CTabItem tabItem = new CTabItem(tabFolder, SWT.NULL);
        tabItem.setText("Tables");
        tabItem.setControl(createTableList());
        tabFolder.layout();
        tabFolder.redraw();
    }

    /**
     * DOC admin Comment method "createTableList".
     */
    @SuppressWarnings("unchecked")
    private Composite createTableList() {
        Composite div1 = new Composite(tabFolder, SWT.NONE);
        div1.setLayout(new GridLayout());
        GridData lgid = new GridData();
        lgid.grabExcessHorizontalSpace = true;
        lgid.horizontalAlignment = GridData.FILL_BOTH;
        lgid.heightHint = 1;
        lgid.verticalIndent = 1;
        div1.setLayoutData(lgid);
        div1.setBackground(ColorConstants.white);
        
        listTables = new org.eclipse.swt.widgets.List(div1, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
        listTables.setLayoutData(new GridData(GridData.FILL_BOTH));
        DatabaseConnectionItem item = SQLBuilderRepositoryNodeManager.getItem(getRootNode());
        tables = ((DatabaseConnection) item.getConnection()).getTables();
        for (MetadataTable table : tables) {
            listTables.add(table.getSourceName());
        }
        return div1;
    }

    public RepositoryNode getRootNode() {
        return this.rootNode;
    }

    public void setRootNode(RepositoryNode rootNode) {
        this.rootNode = rootNode;
        createTabFolder();
        createTabItem();
    }

    public org.eclipse.swt.widgets.List getListTables() {
        return this.listTables;
    }

}
