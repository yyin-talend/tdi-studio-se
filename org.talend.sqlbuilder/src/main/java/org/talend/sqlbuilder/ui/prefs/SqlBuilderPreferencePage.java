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
package org.talend.sqlbuilder.ui.prefs;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.talend.core.CorePlugin;
import org.talend.core.prefs.ITalendCorePrefConstants;

/**
 * DOC qiang.zhang class global comment. Detailled comment <br/>
 * 
 */
public class SqlBuilderPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    public SqlBuilderPreferencePage() {
        super(GRID);
        setPreferenceStore(CorePlugin.getDefault().getPreferenceStore());
    }

    private BooleanFieldEditor booleanFieldEditor;

    public void init(IWorkbench workbench) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
     */
    @Override
    protected void createFieldEditors() {
        booleanFieldEditor = new BooleanFieldEditor(ITalendCorePrefConstants.SQL_ADD_QUOTE,
                "add quotes, when you generate sql statement", getFieldEditorParent());
        addField(booleanFieldEditor);
    }

}
