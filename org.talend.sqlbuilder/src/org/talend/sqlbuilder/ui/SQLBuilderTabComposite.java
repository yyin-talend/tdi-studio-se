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
package org.talend.sqlbuilder.ui;

import java.util.List;

import net.sourceforge.sqlexplorer.SQLAlias;

import org.eclipse.jface.util.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TabItem;
import org.talend.sqlbuilder.sessiontree.model.SessionTreeNode;
import org.talend.sqlbuilder.sqlcontrol.AbstractSQLExecution;

/**
 * DOC dev class global comment. Detailled comment <br/>
 * 
 * $Id: SQLBuilderTabComposite.java,v 1.17 2006/11/01 01:35:01 yi.zhang Exp $
 * 
 */
public class SQLBuilderTabComposite extends Composite {

    private int lastTabNumber = 0;

    private CTabFolder tabFolder;

    private List repositoryNames;

    public SQLBuilderTabComposite(Composite parent, int style) {
        super(parent, style);
        this.setLayout(new FillLayout());
    }

    public void openNewEditor(SessionTreeNode node, List repositoryNames, String content) {

        Assert.isNotNull(node, "SessionTreeNode should not be null");

        this.repositoryNames = repositoryNames;
        createTabFolder();
        try {
            createTabItem(node, content);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 
     * DOC dev Comment method "createTabFolder".
     * 
     */
    private void createTabFolder() {

        if (tabFolder == null || tabFolder.isDisposed()) {

            clearParent();

            // create tab folder for different sessions
            tabFolder = new CTabFolder(this, SWT.NULL);
            tabFolder.setToolTipText("SQL editor");
            this.layout();
            this.redraw();
        }
    }

    /**
     * 
     * DOC dev Comment method "clearParent".
     */
    private void clearParent() {

        Control[] children = this.getChildren();
        if (children != null) {
            for (int i = 0; i < children.length; i++) {
                children[i].dispose();
            }
        }

        lastTabNumber = 0;
    }

    /**
     * 
     * DOC dev Comment method "createTabItem".
     * 
     * @param node
     * @param content the content of color text.
     * 
     * @param sql execute query sql String
     * @throws Exception throw all exception
     */
    private void createTabItem(SessionTreeNode node, String content) {

      

        final CTabItem tabItem = new CTabItem(tabFolder, SWT.CLOSE);
       
        Composite composite = new Composite(tabFolder, SWT.NULL);

        SQLBuilderEditorComposite builderEditorComposite = new SQLBuilderEditorComposite(tabFolder, tabItem, SWT.NONE);
        
        builderEditorComposite.setSessionTreeNode(node);
        builderEditorComposite.setEditorContent(content);
       
        builderEditorComposite.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

        tabItem.setControl(builderEditorComposite);
        // tabItem.setData(sqlExecution);

        // set new tab as the active one
        tabFolder.setSelection(tabFolder.getItemCount() - 1);

        // refresh view
        composite.layout();
        tabFolder.layout();
        tabFolder.redraw();
    }
}
