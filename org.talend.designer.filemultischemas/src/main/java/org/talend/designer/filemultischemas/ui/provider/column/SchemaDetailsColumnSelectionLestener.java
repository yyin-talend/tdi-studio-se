// ============================================================================
//
// Copyright (C) 2006-2008 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.filemultischemas.ui.provider.column;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.talend.designer.filemultischemas.data.EPropertyName;
import org.talend.designer.filemultischemas.data.MultiMetadataColumn;

/**
 * cLi class global comment. Detailled comment
 * 
 * @deprecated not used so far.
 */
public class SchemaDetailsColumnSelectionLestener extends SelectionAdapter {

    private TreeEditor treeEditor;

    private Tree tree;

    public SchemaDetailsColumnSelectionLestener(Tree tree, TreeEditor treeEditor) {
        super();
        this.tree = tree;
        this.treeEditor = treeEditor;
    }

    protected Control getEditor() {
        return treeEditor.getEditor();
    }

    protected TreeEditor getTreeEditor() {
        return this.treeEditor;
    }

    protected Tree getTree() {
        return this.tree;
    }

    protected int getColumnIndex() {
        return getTreeEditor().getColumn();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
     */
    @Override
    public void widgetSelected(SelectionEvent e) {
        // Clean up any previous editor control
        Control oldEditor = getEditor();
        if (oldEditor != null)
            oldEditor.dispose();

        // Identify the selected row
        TreeItem item = (TreeItem) e.item;
        if (item == null) {
            return;
        }
        if (getColumnIndex() < 2) { // ingore first
            return;
        }
        final Object data = item.getData();
        if (!(data instanceof ColumnLineData)) {
            return;
        }
        ColumnLineData lineData = (ColumnLineData) data;
        final MultiMetadataColumn multiMetadataColumn = lineData.getKeyData().getMetadataColumns().get(getColumnIndex());

        switch (lineData.getProperty()) {
        case NAME:
        case TAGLEVEL:
            return;
        case TYPE:
            // PTODO
            // create combo
            break;
        case LENGTH:
        case CARD:
        case PATTERN:
            createTextControl(item, lineData.getProperty(), multiMetadataColumn);
            break;
        }

    }

    private void createTextControl(TreeItem item, final EPropertyName pName, final MultiMetadataColumn multiMetadataColumn) {
        Text newEditor = new Text(getTree(), SWT.NONE);
        switch (pName) {
        case LENGTH:
            newEditor.setText(multiMetadataColumn.getLength() != null ? multiMetadataColumn.getLength().toString() : "");
            break;
        case CARD:
            newEditor.setText(multiMetadataColumn.getCard() != null ? multiMetadataColumn.getCard().toString() : "");
        case PATTERN:
            newEditor.setText(multiMetadataColumn.getPattern() != null ? multiMetadataColumn.getPattern().toString() : "");
            break;
        }
        newEditor.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                Text text = (Text) getEditor();

                // final String value = text.getText();
                // switch(pName){
                // case LENGTH:
                // multiMetadataColumn.setLength(value);
                // break;
                // case CARD:
                // newEditor.setText(multiMetadataColumn.getCard()!=null?multiMetadataColumn.getCard().toString():"");
                // case PATTERN:
                //newEditor.setText(multiMetadataColumn.getPattern()!=null?multiMetadataColumn.getPattern().toString():""
                // );
                // break;
                // }

            }
        });
        newEditor.selectAll();
        newEditor.setFocus();
        getTreeEditor().setEditor(newEditor, item, getColumnIndex());
    }
}
