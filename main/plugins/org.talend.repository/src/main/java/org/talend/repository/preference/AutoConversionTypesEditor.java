// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.preference;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.AutoConversionTypeModel;

/**
 * 
 * created by hcyi on Aug 18, 2016 Detailled comment
 *
 */
public class AutoConversionTypesEditor extends FieldEditor {

    public static final String ID = "org.talend.repository.preference.AutoConversionTypesEditor"; //$NON-NLS-1$

    public AutoConversionTypesEditor(String name, Composite parent) {
        init(name, Messages.getString("AutoConversionTypesEditor.title"));//$NON-NLS-1$
        createControl(parent);
    }

    @Override
    protected void doFillIntoGrid(Composite parent, int numColumns) {
        Composite parentComposite = new Composite(parent, SWT.NULL);
        GridLayout parentCompLayout = new GridLayout(1, false);
        parentCompLayout.marginWidth = 0;
        parentCompLayout.marginHeight = 0;
        parentComposite.setLayout(parentCompLayout);
        parentComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        Button enableBtn = new Button(parentComposite, SWT.CHECK);
        enableBtn.setText(Messages.getString("AutoConversionTypesEditor.Button.enable"));//$NON-NLS-1$

        Label noteLabel = new Label(parentComposite, SWT.NONE);
        noteLabel.setText(Messages.getString("AutoConversionTypesEditor.Label.note"));//$NON-NLS-1$

        AutoConversionTypeModel model = new AutoConversionTypeModel();
        AutoConversionTypesEditorView tableEditorView = new AutoConversionTypesEditorView(parentComposite, model);
        Composite tableComposite = tableEditorView.getMainComposite();
        GridData tableData = new GridData(GridData.FILL_BOTH);
        tableData.heightHint = 300;
        tableComposite.setLayoutData(tableData);
        model.setModifiedBeanListenable(tableEditorView.getTableViewerCreator());
    }

    @Override
    public int getNumberOfControls() {
        return 2;
    }

    @Override
    protected void adjustForNumColumns(int numColumns) {

    }

    @Override
    protected void doLoad() {

    }

    @Override
    protected void doLoadDefault() {

    }

    @Override
    protected void doStore() {

    }
}
