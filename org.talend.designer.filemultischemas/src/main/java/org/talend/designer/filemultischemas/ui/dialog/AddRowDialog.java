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
package org.talend.designer.filemultischemas.ui.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * DOC hywang class global comment. Detailled comment
 */
public class AddRowDialog extends Dialog {

    private Label keyLbl, recoredLbl, sepLbl;

    private Text key, record, sep;

    private String keyValue, recordValue, sepValue;

    private Composite main;

    public AddRowDialog(Shell parentShell) {
        super(parentShell);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        main = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        GridData data = new GridData(GridData.CENTER);
        layout.numColumns = 2;
        main.setLayout(layout);
        main.setLayoutData(data);
        createControls(main);
        return main;
    }

    private void createControls(Composite main) {
        keyLbl = new Label(main, SWT.NONE);
        keyLbl.setText("Schema:");
        key = new Text(main, SWT.BORDER);
        key.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                keyValue = key.getText();
            }
        });

        recoredLbl = new Label(main, SWT.NONE);
        recoredLbl.setText("Record:");
        record = new Text(main, SWT.BORDER);
        record.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                recordValue = record.getText();
            }
        });

        sepLbl = new Label(main, SWT.NONE);
        sepLbl.setText("Seperator:");
        sep = new Text(main, SWT.BORDER);
        sep.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                sepValue = sep.getText();
            }
        });
    }

    public String getKeyValue() {
        if (this.keyValue == null) {
            return "";
        }
        return this.keyValue;
    }

    public String getRecordValue() {
        if (this.recordValue == null) {
            return "";
        }
        return this.recordValue;
    }

    public String getSepValue() {
        if (this.sepValue == null) {
            return "";
        }
        return this.sepValue;
    }

}
