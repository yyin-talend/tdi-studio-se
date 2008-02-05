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
package org.talend.componentdesigner.ui.preferencepage;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.talend.componentdesigner.ComponentDesigenerPlugin;
import org.talend.componentdesigner.PluginConstant;

/**
 * DOC rli class global comment. Detailled comment
 */
public class ComponentProjectPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

    private Text directoryText;

    public void init(IWorkbench workbench) {
        setPreferenceStore(ComponentDesigenerPlugin.getDefault().getPreferenceStore());
    }

    @Override
    protected Control createContents(Composite parent) {
        Label settingsLabel = new Label(parent, SWT.None);
        settingsLabel.setText("Choose a project folder for this component designer"); //$NON-NLS-1$
        GridDataFactory.fillDefaults().span(2, 1).applyTo(settingsLabel);
        Composite projSelComposite = new Composite(parent, SWT.NONE);
        projSelComposite.setLayout(new GridLayout(9, false));
        GridData data = new GridData(GridData.FILL_BOTH);
        data.widthHint = 350;
        projSelComposite.setLayoutData(data);

        Label label = new Label(projSelComposite, SWT.None);
        GridData gridData = new GridData();
        gridData.horizontalSpan = 2;
        label.setLayoutData(gridData);
        label.setText("Component Project:");

        directoryText = new Text(projSelComposite, SWT.BORDER);
        gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.horizontalSpan = 6;
        directoryText.setLayoutData(gridData);

        Button browserButton = new Button(projSelComposite, SWT.None);
        gridData = new GridData();
        gridData.horizontalSpan = 1;
        browserButton.setLayoutData(gridData);
        browserButton.setText(PluginConstant.BROWSER_LABEL);
        browserButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                directoryText.setText(getPathFromDialog());
            }
        });
        if (this.getPreferenceStore().getString(PluginConstant.PROJECT_URL) != null) {
            this.directoryText.setText(getPreferenceStore().getString(PluginConstant.PROJECT_URL));
        }
        return parent;
    }

    private String getPathFromDialog() {
        DirectoryDialog dialog = new DirectoryDialog(this.getShell());
        dialog.setMessage("Select Component Project Path");
        String path = dialog.open();
        return path == null ? PluginConstant.EMPTY_STRING : path;
    }

    @Override
    public boolean performOk() {
        ComponentDesigenerPlugin.getDefault().getPreferenceStore().setValue(PluginConstant.PROJECT_URL,
                this.directoryText.getText());
        ComponentDesigenerPlugin.getDefault().creatComponentProj();
        return super.performOk();
    }
}
