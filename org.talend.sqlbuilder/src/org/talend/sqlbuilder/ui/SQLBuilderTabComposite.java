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

import org.eclipse.jface.util.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.talend.sqlbuilder.sessiontree.model.SessionTreeNode;
import org.talend.sqlbuilder.util.ConnectionParameters;

/**
 * DOC dev class global comment. Detailled comment <br/>
 * 
 * $Id: SQLBuilderTabComposite.java,v 1.23 2006/11/09 08:40:43 tangfn Exp $
 * 
 */
public class SQLBuilderTabComposite extends Composite {

    private CTabFolder tabFolder;

    private List repositoryNames;

    public SQLBuilderTabComposite(Composite parent, int style) {
        super(parent, style);
        this.setLayout(new FillLayout());
    }

    public void openNewEditor(SessionTreeNode node, List repositoryNames, ConnectionParameters connParam,
            boolean isDefaultEditor) {

        Assert.isNotNull(node, "SessionTreeNode should not be null");

        this.repositoryNames = repositoryNames;
        createTabFolder();
        try {
            createTabItem(node, connParam, isDefaultEditor);
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
    private void createTabItem(SessionTreeNode node, ConnectionParameters connParam, boolean isDefaultEditor) {

        CTabItem tabItem = null;
        if (isDefaultEditor) {
            tabItem = new CTabItem(tabFolder, SWT.NONE);
        } else {
            // Do not allow user to close the default editor.
            tabItem = new CTabItem(tabFolder, SWT.CLOSE);
        }


        SQLBuilderEditorComposite builderEditorComposite = new SQLBuilderEditorComposite(tabFolder, tabItem, SWT.NONE);

        builderEditorComposite.setSessionTreeNode(node);
        builderEditorComposite.setEditorContent(connParam);
        builderEditorComposite.setDefaultEditor(isDefaultEditor);

        builderEditorComposite.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

        tabItem.setControl(builderEditorComposite);
        // tabItem.setData(sqlExecution);

        // set new tab as the active one
        tabFolder.setSelection(tabFolder.getItemCount() - 1);

        // refresh view
        tabFolder.layout();
        tabFolder.redraw();
    }

   /**
    * 
    * DOC dev Comment method "getDefaultTabSql".
    * @return
    */
    public String getDefaultTabSql() {
        Control control = tabFolder.getChildren()[0];
        System.out.println(control);
        
        SQLBuilderEditorComposite composite = (SQLBuilderEditorComposite)control;
        return composite.getSQLToBeExecuted();
    }
}
