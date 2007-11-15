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

import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.talend.designer.components.ComponentsLocalProviderPlugin;
import org.talend.designer.components.i18n.Messages;

/**
 * This class represents a preference page that is contributed to the Preferences dialog. By subclassing
 * <samp>FieldEditorPreferencePage</samp>, we can use the field support built into JFace that allows us to create a
 * page that is small and knows how to save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They are stored in the preference store that belongs to the main
 * plug-in class. That way, preferences can be accessed directly via the preference store.
 */
public class ComponentsPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    public static final String USER_COMPONENTS_FOLDER = "USER_COMPONENTS_FOLDER"; //$NON-NLS-1$

    public ComponentsPreferencePage() {
        super(GRID);
        setPreferenceStore(ComponentsLocalProviderPlugin.getDefault().getPreferenceStore());
    }

    @Override
    protected void performApply() {
        // TODO Auto-generated method stub
        super.performApply();
    }

    public void createFieldEditors() {
        DirectoryFieldEditor filePathTemp = new DirectoryFieldEditor(USER_COMPONENTS_FOLDER, Messages.getString("ComponentsPreferencePage.directoryFieldLabel"), //$NON-NLS-1$
                getFieldEditorParent());
        addField(filePathTemp);
    }

    public void init(IWorkbench workbench) {
    }

}
