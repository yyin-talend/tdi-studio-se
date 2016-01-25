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
package org.talend.designer.runprocess.prefs;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.talend.core.CorePlugin;
import org.talend.core.prefs.ITalendCorePrefConstants;
import org.talend.designer.runprocess.i18n.Messages;

/**
 * ftang class global comment. Detailled comment <br/>
 * 
 * 
 */
public class ShellSettingPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    private StringFieldEditor commandField;

    /**
     * ftang DocumentationPreferencePage constructor comment.
     */
    public ShellSettingPreferencePage() {
        super(FLAT);
        setPreferenceStore(CorePlugin.getDefault().getPreferenceStore());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createContents(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Control createContents(Composite p) {
        Composite parent = (Composite) super.createContents(p);

        org.eclipse.swt.widgets.Label cmdLbl = new Label(parent, SWT.NONE);

        cmdLbl.setText(Messages.getString("ShellSettingPreferencePage.command")); //$NON-NLS-1$
        cmdLbl.setLayoutData(new GridData());

        final StyledText cmdTxt = new StyledText(parent, SWT.MULTI | SWT.WRAP | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        cmdTxt.setLayoutData(new GridData(GridData.FILL_BOTH));
        cmdTxt.setText(CorePlugin.getDefault().getPreferenceStore().getString(ITalendCorePrefConstants.COMMAND_STR));

        cmdTxt.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                CorePlugin.getDefault().getPreferenceStore().setValue(ITalendCorePrefConstants.COMMAND_STR,
                        ((StyledText) e.getSource()).getText().trim());
            }
        });

        return parent;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
     */
    @Override
    protected void createFieldEditors() {
        // do nothing.
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
     */
    public void init(IWorkbench workbench) {
        // do nothing.
    }

}
