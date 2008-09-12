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
package org.talend.designer.core.ui.preferences;

import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.utils.DesignerColorUtils;
import org.talend.designer.core.DesignerPlugin;
import org.talend.designer.core.i18n.Messages;

/**
 * ggu class global comment. Detailled comment
 */
public class DesignerColorsPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    public DesignerColorsPreferencePage() {
        super(GRID);
        setPreferenceStore(DesignerPlugin.getDefault().getPreferenceStore());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
     */
    @Override
    protected void createFieldEditors() {
        Composite parent = new Composite(getFieldEditorParent(), SWT.NONE);
        parent.setLayout(new GridLayout());
        GridData data = new GridData(GridData.FILL_BOTH);
        parent.setLayoutData(data);

        createSubjobFieldEditors(parent);
        createConnectionFieldEditors(parent);
    }

    private void createSubjobFieldEditors(Composite parent) {
        Group subjobGroup = new Group(parent, SWT.NULL);
        subjobGroup.setText(Messages.getString("DesignerPreferencePage.SubjobColorGroup")); //$NON-NLS-1$
        subjobGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        addField(new ColorFieldEditor(DesignerColorUtils.SUBJOB_TITLE_COLOR_NAME, Messages
                .getString("DesignerPreferencePage.SubjobTitleColorLabel"), subjobGroup)); //$NON-NLS-1$

        addField(new ColorFieldEditor(DesignerColorUtils.SUBJOB_COLOR_NAME, Messages
                .getString("DesignerPreferencePage.SubjobColorLabel"), subjobGroup)); //$NON-NLS-1$
        GridLayout layout = new GridLayout(2, false);
        layout.marginLeft = 10;
        subjobGroup.setLayout(layout);
    }

    private void createConnectionFieldEditors(Composite parent) {
        Group connGroup = new Group(parent, SWT.NULL);
        connGroup.setText(Messages.getString("DesignerColorsPreferencePage.ConnectionColorGroup")); //$NON-NLS-1$
        connGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        Label message = new Label(connGroup, SWT.NULL);
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.horizontalSpan = 2;
        data.minimumWidth = 400;
        data.heightHint = 20;
        message.setLayoutData(data);
        message.setText(Messages.getString("DesignerColorsPreferencePage.ConnectionColorMessages")); //$NON-NLS-1$

        for (EConnectionType connType : EConnectionType.values()) {
            addField(new ColorFieldEditor(DesignerColorUtils.getPreferenceConnectionName(connType),
                    connType.getDefaultMenuName(), connGroup));
        }
        GridLayout layout = new GridLayout(2, false);
        layout.marginLeft = 10;
        connGroup.setLayout(layout);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
     */
    public void init(IWorkbench workbench) {

    }

}
