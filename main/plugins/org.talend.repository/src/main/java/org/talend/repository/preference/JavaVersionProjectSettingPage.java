// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
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

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.talend.commons.ui.runtime.image.EImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.commons.utils.generation.JavaUtils;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.runtime.projectsetting.AbstractProjectSettingPage;
import org.talend.repository.i18n.Messages;

public class JavaVersionProjectSettingPage extends AbstractProjectSettingPage {

    private Combo combo;

    private String version;

    private Button checkbox;

    private CLabel warnLabel;

    public JavaVersionProjectSettingPage() {
        noDefaultAndApplyButton();
    }

    @Override
    protected String getPreferenceName() {
        return CoreRuntimePlugin.PLUGIN_ID;
    }

    @Override
    protected void createFieldEditors() {
        version = JavaUtils.getProjectJavaVersion();
        if (version == null) {
            version = JavaUtils.DEFAULT_VERSION;
            JavaUtils.updateProjectJavaVersion(version);
        }
        Composite parent = getFieldEditorParent();
        parent.setLayout(new GridLayout(2, true));
        Label label = new Label(parent, SWT.NONE);
        label.setText(Messages.getString("JavaVersionProjectSettingPage.versionLabel")); //$NON-NLS-1$
        GridData labelData = new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 1, 1);
        label.setLayoutData(labelData);
        combo = new Combo(parent, SWT.READ_ONLY);
        GridData comboData = new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 1, 1);
        GC gc = new GC(combo);
        Point labelSize = gc.stringExtent(JavaUtils.DEFAULT_VERSION);
        gc.dispose();
        int hint = labelSize.x + (ITabbedPropertyConstants.HSPACE * 14);
        comboData.widthHint = hint;
        combo.setLayoutData(comboData);
        combo.setItems(JavaUtils.AVAILABLE_VERSIONS.toArray(new String[] {}));
        combo.select(JavaUtils.AVAILABLE_VERSIONS.indexOf(version));

        checkbox = new Button(parent, SWT.CHECK);
        checkbox.setText(Messages.getString("JavaVersionProjectSettingPage.internalAccessLabel")); //$NON-NLS-1$
        boolean selected = getPreferenceStore().getBoolean(JavaUtils.ALLOW_JAVA_INTERNAL_ACCESS);
        checkbox.setSelection(selected);
        GridData checkboxData = new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 2, 1);
        checkbox.setLayoutData(checkboxData);
        checkbox.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                warnLabel.setVisible(checkbox.getSelection());
            }

        });

        warnLabel = new CLabel(parent, SWT.NONE);
        warnLabel.setText(Messages.getString("JavaVersionProjectSettingPage.internalAccessWarningLabel"));//$NON-NLS-1$
        warnLabel.setImage(ImageProvider.getImage(EImage.WARNING_ICON));
        warnLabel.setVisible(selected);
    }

    @Override
    public boolean performOk() {
        if (combo == null) {
            return true;
        }
        String newVersion = combo.getItem(combo.getSelectionIndex());
        if (!newVersion.equals(version)) {
            JavaUtils.updateProjectJavaVersion(newVersion);
        }
        getPreferenceStore().setValue(JavaUtils.ALLOW_JAVA_INTERNAL_ACCESS, checkbox.getSelection());
        return super.performOk();
    }

}
