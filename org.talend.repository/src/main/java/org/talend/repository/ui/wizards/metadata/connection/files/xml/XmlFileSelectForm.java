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
package org.talend.repository.ui.wizards.metadata.connection.files.xml;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.talend.commons.ui.swt.formtools.Form;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.repository.ui.swt.utils.AbstractXmlFileStepForm;

/**
 * wzhang class global comment. Detailled comment
 */
public class XmlFileSelectForm extends AbstractXmlFileStepForm {

    private Button inputModeButton;

    private Button outputModeButton;

    private Label label;

    private static boolean isInputModel = true;

    private boolean creation;

    private static final int WIDTH_GRIDDATA_PIXEL = 300;

    public XmlFileSelectForm(boolean creation, Composite parent, ConnectionItem connectionItem, String[] existingNames) {
        super(parent, connectionItem, existingNames);
        this.creation = creation;
        setupForm();
    }

    @Override
    protected void adaptFormToReadOnly() {

    }

    @Override
    protected void addFields() {
        Group group = Form.createGroup(this, 1, "Model Setting", 100);
        Composite compositeFileLocation = Form.startNewDimensionnedGridLayout(group, 1, WIDTH_GRIDDATA_PIXEL, 100);
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.heightHint = 120;
        group.setLayoutData(data);

        label = new Label(compositeFileLocation, SWT.NONE);
        GridData labelData = new GridData();
        labelData.verticalSpan = 8;
        label.setLayoutData(labelData);
        label.setText("Select one model to create XML metadata");

        inputModeButton = new Button(compositeFileLocation, SWT.RADIO);
        inputModeButton.setText("Input XML");
        inputModeButton.setSelection(getConnection().isInputModel());
        inputModeButton.setEnabled(creation);

        outputModeButton = new Button(compositeFileLocation, SWT.RADIO);
        outputModeButton.setText("OutPut XML");
        outputModeButton.setSelection(!getConnection().isInputModel());
        outputModeButton.setEnabled(creation);
    }

    @Override
    protected void addFieldsListeners() {

    }

    @Override
    protected void addUtilsButtonListeners() {
        inputModeButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                isInputModel = true;
                getConnection().setInputModel(true);
            }

        });
        outputModeButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                isInputModel = false;
                getConnection().setInputModel(false);
            }

        });
    }

    @Override
    protected boolean checkFieldsValue() {
        return false;
    }

    @Override
    protected void initialize() {
        if (creation) {
            this.setReadOnly(true);
        }
    }

    // public static boolean isInputModel() {
    // return isInputModel;
    // }

}
