// ============================================================================
//
// Copyright (C) 2006-2009 Talend Inc. - www.talend.com
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
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.talend.commons.ui.swt.preferences.CheckBoxFieldEditor;
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

    private CheckBoxFieldEditor dbConnTimeoutActive;

    private IntegerFieldEditor dbConnTimeout;

    private static final String STANDARD_MODE = "true"; //$NON-NLS-1$

    private static final String SYSTEM_MODE = "false"; //$NON-NLS-1$

    public void init(IWorkbench workbench) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
     */
    @Override
    protected void createFieldEditors() {
        booleanFieldEditor = new BooleanFieldEditor(ITalendCorePrefConstants.SQL_ADD_QUOTE, Messages
                .getString("SqlBuilderPreferencePage.AddQuotes"), getFieldEditorParent()); //$NON-NLS-1$
        choiceAS4Sql = new RadioGroupFieldEditor(ITalendCorePrefConstants.AS400_SQL_SEG, Messages
                .getString("SqlBuilderPreferencePage.AS400SqlGen"), 1, new String[][] { //$NON-NLS-1$
                { Messages.getString("SqlBuilderPreferencePage.StandardSQL"), STANDARD_MODE }, //$NON-NLS-1$
                        { Messages.getString("SqlBuilderPreferencePage.SystemSQL"), SYSTEM_MODE }, }, getFieldEditorParent()); //$NON-NLS-1$

        dbConnTimeoutActive = new CheckBoxFieldEditor(ITalendCorePrefConstants.DB_CONNECTION_TIMEOUT_ACTIVED, Messages
                .getString("SqlBuilderPreferencePage.ActivedTimeoutSetting"), getFieldEditorParent()); //$NON-NLS-1$
        dbConnTimeoutActive.getButton().addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                checkDBTimeout();
            }
        });
        dbConnTimeout = new IntegerFieldEditor(ITalendCorePrefConstants.DB_CONNECTION_TIMEOUT, Messages
                .getString("SqlBuilderPreferencePage.ConnectionTimeout"), //$NON-NLS-1$
                getFieldEditorParent());
        Text textControl = dbConnTimeout.getTextControl(getFieldEditorParent());
        textControl.setToolTipText(Messages.getString("SqlBuilderPreferencePage.ConnectionTimeoutTip")); //$NON-NLS-1$
        dbConnTimeout.setValidRange(0, Short.MAX_VALUE);

        addField(booleanFieldEditor);
        addField(choiceAS4Sql);
        addField(dbConnTimeoutActive);
        addField(dbConnTimeout);

    }

    @Override
    protected void initialize() {
        super.initialize();
        checkDBTimeout();
    }

    private void checkDBTimeout() {
        if (dbConnTimeout != null) {
            Text textControl = dbConnTimeout.getTextControl(getFieldEditorParent());
            if (textControl != null && dbConnTimeoutActive != null) {
                textControl.setEnabled(dbConnTimeoutActive.getBooleanValue());
            }
        }
    }
}
