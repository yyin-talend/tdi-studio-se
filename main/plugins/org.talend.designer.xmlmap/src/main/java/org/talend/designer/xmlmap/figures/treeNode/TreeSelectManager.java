// ============================================================================
//
// Copyright (C) 2006-2015 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.xmlmap.figures.treeNode;

/**
 * wchen class global comment. Detailled comment
 */
public class TreeSelectManager {

    private static TreeSelectManager manager;

    private TreeSelectManager() {
        //
    }

    public static TreeSelectManager getManager() {
        if (manager == null) {
            manager = new TreeSelectManager();
        }
        return manager;
    }

    private RowFigure selection;

    public RowFigure getSelection() {
        return this.selection;
    }

    public void setSelection(RowFigure selection) {
        select(this.selection, false); // de-select the old selection

        this.selection = selection;

        select(this.selection, true); // select the new one.

    }

    private void select(RowFigure selection, boolean selected) {
        if (selection != null) {
            selection.setSelected(selected);
        }
    }

}
