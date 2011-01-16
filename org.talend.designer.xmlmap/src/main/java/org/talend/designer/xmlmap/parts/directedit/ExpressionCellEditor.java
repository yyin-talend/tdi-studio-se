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
package org.talend.designer.xmlmap.parts.directedit;

import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.widgets.Composite;
import org.talend.commons.ui.swt.tableviewer.celleditor.CellEditorDialogBehavior;
import org.talend.commons.ui.swt.tableviewer.celleditor.ExtendedTextCellEditor;

/**
 * DOC XYuser class global comment. Detailled comment
 */
public class ExpressionCellEditor extends ExtendedTextCellEditor {

    public ExpressionCellEditor(Composite parent, CellEditorDialogBehavior cellEditorBehavior) {
        super(parent, cellEditorBehavior);
        ((Composite) getControl()).addFocusListener(new FocusAdapter() {

            public void focusLost(FocusEvent e) {
                if (isActivated()) {
                    fireApplyEditorValue();
                    deactivate();
                }
            }

            @Override
            public void focusGained(FocusEvent e) {
                // TODO Auto-generated method stub
                super.focusGained(e);
            }

        });
    }

}
