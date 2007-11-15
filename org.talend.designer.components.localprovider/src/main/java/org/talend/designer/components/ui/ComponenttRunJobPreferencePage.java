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
package org.talend.designer.components.ui;

import org.eclipse.gmf.runtime.common.ui.preferences.CheckBoxFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.talend.designer.components.ComponentsLocalProviderPlugin;
import org.talend.designer.components.i18n.Messages;

/**
 * DOC Administrator class global comment. Detailled comment
 * 
 * @author ftang, 17/08, 2007<br/>
 * 
 */
public class ComponenttRunJobPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    private CheckBoxFieldEditor doNotShowJobAfterDoubleClickCheckBoxField;

    public static final String IS_AVOID = "isAvoidShowJobAfterDoulbleClick";

    private static final String TITLE = "tRunJob";

    public ComponenttRunJobPreferencePage() {
        super(TITLE, GRID);
        setPreferenceStore(ComponentsLocalProviderPlugin.getDefault().getPreferenceStore());
    }

    @Override
    public void createFieldEditors() {

        doNotShowJobAfterDoubleClickCheckBoxField = new CheckBoxFieldEditor(IS_AVOID, Messages
                .getString("ComponenttRunJobPreferencePage.label"), getFieldEditorParent());

        addField(doNotShowJobAfterDoubleClickCheckBoxField);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
     */
    public void init(IWorkbench workbench) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.FieldEditorPreferencePage#performDefaults()
     */
    @Override
    protected void performDefaults() {
        super.performDefaults();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.IPreferencePage#performOk()
     */
    public boolean performOk() {
        boolean ok = super.performOk();
        ComponentsLocalProviderPlugin.getDefault().savePluginPreferences();
        return ok;
    }

}
