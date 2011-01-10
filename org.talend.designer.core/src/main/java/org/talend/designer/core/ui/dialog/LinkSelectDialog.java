// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.core.ui.dialog;

import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ListDialog;
import org.talend.designer.core.ui.editor.connections.Connection;
import org.talend.designer.core.ui.editor.connections.ConnectionPart;

/**
 * wzhang class global comment. Detailled comment
 */
public class LinkSelectDialog extends ListDialog {

    public LinkSelectDialog(Shell parent, List<ConnectionPart> input) {
        super(parent);
        setTitle("Choose Link");
        setMessage("Target reference several links, please choose one to create.");
        setInput(input);
        setContentProvider(new ArrayContentProvider());
        setLabelProvider(new LabelProvider() {

            @Override
            public String getText(Object element) {
                ConnectionPart connPart = (ConnectionPart) element;
                Connection conn = (Connection) connPart.getModel();
                return conn.getName();
            }

        });

    }

    protected Control createContents(Composite parent) {
        Control control = super.createContents(parent);
        getTableViewer().getTable().showSelection();
        return control;
    }

    public ConnectionPart getSelectLink() {
        return (ConnectionPart) getResult()[0];
    }
}
