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
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.talend.core.CorePlugin;
import org.talend.core.prefs.ITalendCorePrefConstants;
import org.talend.sqlbuilder.Messages;

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

    private RadioGroupFieldEditor choiceAS4Sql;

    private static final String STANDARD_MODE = "true";

    private static final String SYSTEM_MODE = "false";

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
        choiceAS4Sql = new RadioGroupFieldEditor(ITalendCorePrefConstants.AS400_SQL_SEG, Messages
                .getString("SqlBuilderPreferencePage.AS400SqlGen"), 1, new String[][] {
                { Messages.getString("SqlBuilderPreferencePage.StandardSQL"), STANDARD_MODE },
                { Messages.getString("SqlBuilderPreferencePage.SystemSQL"), SYSTEM_MODE }, }, getFieldEditorParent());
        addField(booleanFieldEditor);
        addField(choiceAS4Sql);

    }
}
