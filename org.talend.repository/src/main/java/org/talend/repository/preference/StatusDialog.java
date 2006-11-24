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
package org.talend.repository.preference;

import java.util.Collections;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * DOC tguiu class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class StatusDialog extends Dialog {

    private String label = ""; //$NON-NLS-1$

    private String code = ""; //$NON-NLS-1$

    private Button okButton;

    private Text labelText;

    private Text codeText;

    private Label errorMessageText;

    private boolean creation = false;

    private final List existingCodes;

    public StatusDialog(Shell parentShell, List existingCodes) {
        this(parentShell, existingCodes, null, null);
        creation = true;
    }

    public StatusDialog(Shell parentShell, List existingCodes, String initialCode, String initialLabel) {
        super(parentShell);
        this.existingCodes = existingCodes == null ? Collections.EMPTY_LIST : existingCodes;
        code = initialCode == null ? "" : initialCode;
        label = initialLabel == null ? "" : initialLabel;
    }

    @Override
    protected void buttonPressed(int buttonId) {
        if (buttonId == IDialogConstants.OK_ID) {
            code = codeText.getText();
            label = labelText.getText();
        } else {
            code = "";
            label = "";
        }
        super.buttonPressed(buttonId);
    }

    @Override
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        if (creation) {
            shell.setText("Create new status");
        } else {
            shell.setText("Edit status");
        }
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        // create OK and Cancel buttons by default
        okButton = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
        // do this here because setting the text will set enablement on the ok
        // button
        if (code != null) {
            codeText.setText(code);
            codeText.selectAll();
        }
        if (label != null) {
            labelText.setText(label);
        }
        if (creation) {
            codeText.setFocus();
        } else {
            codeText.setEnabled(false);
            labelText.setFocus();
        }
    }

    /*
     * (non-Javadoc) Method declared on Dialog.
     */
    @Override
    protected Control createDialogArea(Composite parent) {
        // create composite
        Composite composite = (Composite) super.createDialogArea(parent);
        // create message
        // if (message != null) {
        // Label label = new Label(composite, SWT.WRAP);
        // label.setText(message);
        // GridData data = new GridData(GridData.GRAB_HORIZONTAL
        // | GridData.GRAB_VERTICAL | GridData.HORIZONTAL_ALIGN_FILL
        // | GridData.VERTICAL_ALIGN_CENTER);
        // data.widthHint = convertHorizontalDLUsToPixels(IDialogConstants.MINIMUM_MESSAGE_AREA_WIDTH);
        // label.setLayoutData(data);
        // label.setFont(parent.getFont());
        // }
        GridLayout layout = new GridLayout();
        layout.numColumns = 3;
        composite.setLayout(layout);
        Label label1 = new Label(composite, SWT.None);
        label1.setText("Code:");
        // label.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
        GridData data = new GridData(GridData.GRAB_VERTICAL | GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_CENTER);
        label1.setLayoutData(data);
        label1.setFont(parent.getFont());
        codeText = new Text(composite, SWT.SINGLE | SWT.BORDER);
        data = new GridData();
        data.widthHint = convertHorizontalDLUsToPixels(30);
        codeText.setLayoutData(data);
        codeText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                validateInput();
            }
        });
        label1 = new Label(composite, SWT.WRAP);
        data = new GridData(GridData.FILL_HORIZONTAL);
        label1.setLayoutData(data);

        label1 = new Label(composite, SWT.WRAP);
        label1.setText("Label:");
        label1.setFont(parent.getFont());
        labelText = new Text(composite, SWT.SINGLE | SWT.BORDER);
        data = new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL);
        data.horizontalSpan = 2;
        labelText.setLayoutData(data);
        labelText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                validateInput();
            }
        });
        errorMessageText = new Label(composite, SWT.NONE);
        errorMessageText.setLayoutData(data);
        errorMessageText.setBackground(errorMessageText.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));

        applyDialogFont(composite);
        return composite;
    }

    protected Button getOkButton() {
        return okButton;
    }

    protected void validateInput() {
        String errorMessage = null;
        if (creation && codeText.getText().length() != 3) {
            errorMessage = "Code must have 3 letters.";
        } else if (creation && existingCodes.contains(codeText.getText())) {
            errorMessage = "This code is already used";
        } else if (labelText.getText().equals("")) {
            errorMessage = "Label cannot be empty.";
        }
        setErrorMessage(errorMessage);
    }

    public void setErrorMessage(String errorMessage) {
        if (errorMessageText != null && !errorMessageText.isDisposed()) {
            errorMessageText.setText(errorMessage == null ? "" : errorMessage); //$NON-NLS-1$
            errorMessageText.getParent().update();
            // Access the ok button by id, in case clients have overridden button creation.
            // See https://bugs.eclipse.org/bugs/show_bug.cgi?id=113643
            Control button = getButton(IDialogConstants.OK_ID);
            if (button != null) {
                button.setEnabled(errorMessage == null);
            }
        }
    }

    /**
     * Getter for code.
     * 
     * @return the code
     */
    public String getCode() {
        return this.code;
    }

    /**
     * Getter for label.
     * 
     * @return the label
     */
    public String getLabel() {
        return this.label;
    }
}
