// ============================================================================
//
// Copyright (C) 2006-2012 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.xmlmap.ui.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.talend.designer.xmlmap.model.emf.xmlmap.InputLoopNodesTable;
import org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode;

/**
 * DOC hcyi class global comment. Detailled comment
 */
public class SetLoopFunctionDialog extends Dialog {

    private Table table;

    private InputLoopNodesTable inputLoopNodesTable;

    public SetLoopFunctionDialog(Shell parentShell, InputLoopNodesTable inputLoopNodesTable) {
        super(parentShell);
        this.inputLoopNodesTable = inputLoopNodesTable;
    }

    public Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        composite.setLayout(layout);

        table = new Table(composite, SWT.SINGLE);
        GridData gd = new GridData();
        gd.verticalSpan = 3;
        gd.grabExcessHorizontalSpace = true;
        gd.grabExcessVerticalSpace = true;
        gd.verticalAlignment = GridData.FILL;
        gd.horizontalAlignment = GridData.FILL;
        table.setLayoutData(gd);
        table.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e) {
            }

            public void widgetSelected(SelectionEvent e) {
            }
        });
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        TableColumn variableNameColumn = new TableColumn(table, SWT.LEFT);
        variableNameColumn.setText("Sequence");
        variableNameColumn.setWidth(150);
        TableColumn parameterNameColumn = new TableColumn(table, SWT.LEFT);
        parameterNameColumn.setText("input Loop Nodes");
        parameterNameColumn.setWidth(225);
        updateTable();
        return composite;
    }

    private void updateTable() {
        if (inputLoopNodesTable != null) {
            for (int i = 0; i < inputLoopNodesTable.getInputloopnodes().size(); i++) {
                TreeNode treeNode = inputLoopNodesTable.getInputloopnodes().get(i);
                TableItem item = new TableItem(table, SWT.NONE);
                item.setText(new String[] { (i + 1) + "", treeNode.getXpath() == null ? "" : treeNode.getXpath() });
            }
        }
    }
}
