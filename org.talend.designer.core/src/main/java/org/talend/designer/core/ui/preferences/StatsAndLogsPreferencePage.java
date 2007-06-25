// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2007 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
//
// ============================================================================
package org.talend.designer.core.ui.preferences;

import org.eclipse.gmf.runtime.common.ui.preferences.CheckBoxFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.talend.commons.utils.workbench.preferences.ComboFieldEditor;
import org.talend.core.language.ECodeLanguage;
import org.talend.designer.core.DesignerPlugin;
import org.talend.designer.core.model.components.EParameterName;

/**
 * DOC Administrator class global comment. Detailled comment <br/>
 * 
 */
public abstract class StatsAndLogsPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    private ECodeLanguage language;

    private String languagePrefix;

    /** controls. * */
    private CheckBoxFieldEditor onStatCatcherField;

    private CheckBoxFieldEditor onLogCatcherField;

    private CheckBoxFieldEditor onMetterCatcherField;

    private CheckBoxFieldEditor onFilesField;

    private DirectoryFieldEditor filePathField;

    private StringFieldEditor statsFileNameField;

    private StringFieldEditor logsFileNameField;

    private StringFieldEditor metterFileNameField;

    private CheckBoxFieldEditor onDatabaseField;

    private ComboFieldEditor dbTypeField;

    private StringFieldEditor hostField;

    private StringFieldEditor portField;

    private StringFieldEditor dbNameField;

    private StringFieldEditor schemaField;

    private StringFieldEditor userField;

    private StringFieldEditor passwordField;

    private StringFieldEditor logsTableField;

    private StringFieldEditor statsTableField;

    private StringFieldEditor metterTableField;

    private CheckBoxFieldEditor catchRuntimeErrorsField;

    private CheckBoxFieldEditor catchUserErrorsField;

    private CheckBoxFieldEditor catchUserWarningField;

    private CheckBoxFieldEditor catchRealtimeStatsField;

    private Composite parent;

    private Composite dbTypeComposite;

    private Composite finalPart;

    /**
     * Default constructor.
     */
    public StatsAndLogsPreferencePage(ECodeLanguage language) {
        super(GRID);
        this.language = language;
        setPreferenceStore(DesignerPlugin.getDefault().getPreferenceStore());
    }

    /**
     * Creates the field editors. Field editors are abstractions of the common GUI blocks needed to manipulate various
     * types of preferences. Each field editor knows how to save and restore itself.
     */
    public void createFieldEditors() {
        languagePrefix = language.toString() + "_";

        createFields();
        updateEnableStateFromPreferences();
        addListeners();
    }

    private void createFields() {
        parent = getFieldEditorParent();
        int languageType = language == ECodeLanguage.JAVA ? 1 : 0;

        Composite titlePart = new Composite(parent, SWT.None);
        titlePart.setLayout(new GridLayout());

        onStatCatcherField = new CheckBoxFieldEditor(languagePrefix + EParameterName.ON_STATCATCHER_FLAG.getName(),
                EParameterName.ON_STATCATCHER_FLAG.getDisplayName(), titlePart);
        onLogCatcherField = new CheckBoxFieldEditor(languagePrefix + EParameterName.ON_LOGCATCHER_FLAG.getName(),
                EParameterName.ON_LOGCATCHER_FLAG.getDisplayName(), titlePart);
        onMetterCatcherField = new CheckBoxFieldEditor(languagePrefix + EParameterName.ON_METTERCATCHER_FLAG.getName(),
                EParameterName.ON_METTERCATCHER_FLAG.getDisplayName(), titlePart);

        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.horizontalSpan = 4;
        titlePart.setLayoutData(gridData);

        GridLayout gridLayout = (GridLayout) titlePart.getLayout();
        gridLayout.numColumns = 12;
        titlePart.setLayout(gridLayout);

        onFilesField = new CheckBoxFieldEditor(languagePrefix + EParameterName.ON_FILES_FLAG.getName(),
                EParameterName.ON_FILES_FLAG.getDisplayName(), parent);
        filePathField = new DirectoryFieldEditor(languagePrefix + EParameterName.FILE_PATH.getName(),
                EParameterName.FILE_PATH.getDisplayName(), parent);
        statsFileNameField = new StringFieldEditor(languagePrefix + EParameterName.FILENAME_STATS.getName(),
                EParameterName.FILENAME_STATS.getDisplayName(), parent);
        logsFileNameField = new StringFieldEditor(languagePrefix + EParameterName.FILENAME_LOGS.getName(),
                EParameterName.FILENAME_LOGS.getDisplayName(), parent);
        metterFileNameField = new StringFieldEditor(languagePrefix + EParameterName.FILENAME_METTER.getName(),
                EParameterName.FILENAME_METTER.getDisplayName(), parent);

        onDatabaseField = new CheckBoxFieldEditor(languagePrefix + EParameterName.ON_DATABASE_FLAG.getName(),
                EParameterName.ON_DATABASE_FLAG.getDisplayName(), parent);

        dbTypeComposite = new Composite(parent, SWT.NONE);

        String[] strDisplay, strValue;
        strDisplay = StatsAndLogsConstants.DISPLAY_DBNAMES[languageType];
        strValue = StatsAndLogsConstants.DB_COMPONENTS[languageType];
        String[][] strsForDBType = new String[strDisplay.length][2];

        for (int i = 0; i < strDisplay.length; i++) {
            strsForDBType[i][0] = strDisplay[i];
            strsForDBType[i][1] = strValue[i];
        }

        dbTypeField = new ComboFieldEditor(languagePrefix + EParameterName.DB_TYPE.getName(), EParameterName.DB_TYPE
                .getDisplayName(), strsForDBType, dbTypeComposite);
        gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.horizontalSpan = 4;
        dbTypeComposite.setLayoutData(gridData);

        gridLayout = (GridLayout) dbTypeComposite.getLayout();
        gridLayout.numColumns = 6;
        dbTypeComposite.setLayout(gridLayout);

        hostField = new StringFieldEditor(languagePrefix + EParameterName.HOST.getName(), EParameterName.HOST
                .getDisplayName(), parent);

        portField = new StringFieldEditor(languagePrefix + EParameterName.PORT.getName(), EParameterName.PORT
                .getDisplayName(), parent);

        dbNameField = new StringFieldEditor(languagePrefix + EParameterName.DBNAME.getName(), EParameterName.DBNAME
                .getDisplayName(), parent);

        schemaField = new StringFieldEditor(languagePrefix + EParameterName.SCHEMA_DB.getName(),
                EParameterName.SCHEMA_DB.getDisplayName(), parent);

        userField = new StringFieldEditor(languagePrefix + EParameterName.USER.getName(), EParameterName.USER
                .getDisplayName(), parent);

        passwordField = new StringFieldEditor(languagePrefix + EParameterName.PASS.getName(), EParameterName.PASS
                .getDisplayName(), parent);

        statsTableField = new StringFieldEditor(languagePrefix + EParameterName.TABLE_STATS.getName(),
                EParameterName.TABLE_STATS.getDisplayName(), parent);

        logsTableField = new StringFieldEditor(languagePrefix + EParameterName.TABLE_LOGS.getName(),
                EParameterName.TABLE_LOGS.getDisplayName(), parent);

        metterTableField = new StringFieldEditor(languagePrefix + EParameterName.TABLE_METTER.getName(),
                EParameterName.TABLE_METTER.getDisplayName(), parent);

        finalPart = new Composite(parent, SWT.None);
        finalPart.setLayout(new GridLayout());

        catchRuntimeErrorsField = new CheckBoxFieldEditor(languagePrefix
                + EParameterName.CATCH_RUNTIME_ERRORS.getName(), EParameterName.CATCH_RUNTIME_ERRORS.getDisplayName(),
                finalPart);
        catchUserErrorsField = new CheckBoxFieldEditor(languagePrefix + EParameterName.CATCH_USER_ERRORS.getName(),
                EParameterName.CATCH_USER_ERRORS.getDisplayName(), finalPart);
        catchUserWarningField = new CheckBoxFieldEditor(languagePrefix + EParameterName.CATCH_USER_WARNING.getName(),
                EParameterName.CATCH_USER_WARNING.getDisplayName(), finalPart);
        catchRealtimeStatsField = new CheckBoxFieldEditor(languagePrefix
                + EParameterName.CATCH_REALTIME_STATS.getName(), EParameterName.CATCH_REALTIME_STATS.getDisplayName(),
                finalPart);

        gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.horizontalSpan = 4;
        finalPart.setLayoutData(gridData);

        gridLayout = (GridLayout) finalPart.getLayout();
        gridLayout.numColumns = 12;
        finalPart.setLayout(gridLayout);

        addField(onStatCatcherField);
        addField(onLogCatcherField);
        addField(onMetterCatcherField);

        addField(onFilesField);
        addField(filePathField);
        addField(statsFileNameField);
        addField(logsFileNameField);
        addField(metterFileNameField);
        addField(onDatabaseField);

        addField(dbTypeField);
        addField(hostField);
        addField(portField);
        addField(dbNameField);
        addField(schemaField);
        addField(userField);
        addField(passwordField);

        addField(statsTableField);
        addField(logsTableField);
        addField(metterTableField);

        addField(catchRuntimeErrorsField);
        addField(catchUserErrorsField);
        addField(catchUserWarningField);
        addField(catchRealtimeStatsField);
    }

    private void updateEnableStateFromPreferences() {
        IPreferenceStore preferenceStore = getPreferenceStore();

        boolean onStatCatcher = preferenceStore.getBoolean(languagePrefix
                + EParameterName.ON_STATCATCHER_FLAG.getName());
        boolean onLogCatcher = preferenceStore.getBoolean(languagePrefix + EParameterName.ON_LOGCATCHER_FLAG.getName());
        boolean onMetterCatcher = preferenceStore.getBoolean(languagePrefix
                + EParameterName.ON_METTERCATCHER_FLAG.getName());
        boolean onFiles = preferenceStore.getBoolean(languagePrefix + EParameterName.ON_FILES_FLAG.getName());
        boolean onDatabase = preferenceStore.getBoolean(languagePrefix + EParameterName.ON_DATABASE_FLAG.getName());
        String dbValue = preferenceStore.getString(languagePrefix + EParameterName.DB_TYPE.getName());
        updateEnableState(onStatCatcher, onLogCatcher, onMetterCatcher, onFiles, onDatabase, dbValue);
    }

    private void updateEnableStateFromDisplay() {
        boolean onStatCatcher = onStatCatcherField.getBooleanValue();
        boolean onLogCatcher = onLogCatcherField.getBooleanValue();
        boolean onMetterCatcher = onMetterCatcherField.getBooleanValue();
        boolean onFiles = onFilesField.getBooleanValue();
        boolean onDatabase = onDatabaseField.getBooleanValue();
        String dbValue = dbTypeField.getFieldValue();
        updateEnableState(onStatCatcher, onLogCatcher, onMetterCatcher, onFiles, onDatabase, dbValue);
    }

    private void updateEnableState(boolean onStatCatcher, boolean onLogCatcher, boolean onMetterCatcher,
            boolean onFiles, boolean onDatabase, String dbValue) {
        onFilesField.setEnabled(onStatCatcher || onLogCatcher || onMetterCatcher, parent);
        filePathField.setEnabled(onFiles && (onStatCatcher || onLogCatcher || onMetterCatcher), parent);
        statsFileNameField.setEnabled(onFiles && onStatCatcher, parent);
        logsFileNameField.setEnabled(onFiles && onLogCatcher, parent);
        metterFileNameField.setEnabled(onFiles && onMetterCatcher, parent);

        onDatabaseField.setEnabled(onStatCatcher || onLogCatcher || onMetterCatcher, parent);
        dbTypeField.getComboBoxControl(dbTypeComposite).setEnabled(
                onDatabase && (onStatCatcher || onLogCatcher || onMetterCatcher));
        dbTypeField.setEnabled(onDatabase && (onStatCatcher || onLogCatcher || onMetterCatcher), dbTypeComposite);
        hostField.setEnabled(onDatabase && (onStatCatcher || onLogCatcher || onMetterCatcher), parent);
        portField.setEnabled(onDatabase && (onStatCatcher || onLogCatcher || onMetterCatcher), parent);
        dbNameField.setEnabled(onDatabase && (onStatCatcher || onLogCatcher || onMetterCatcher), parent);
        
        schemaField.setEnabled((dbValue.equals("tOracleOutput") || dbValue.equals("tPostgresqlOutput")) && onDatabase
                && (onStatCatcher || onLogCatcher || onMetterCatcher), parent);
        userField.setEnabled(onDatabase && (onStatCatcher || onLogCatcher || onMetterCatcher), parent);
        passwordField.setEnabled(onDatabase && (onStatCatcher || onLogCatcher || onMetterCatcher), parent);
        statsTableField.setEnabled(onDatabase && onStatCatcher, parent);
        logsTableField.setEnabled(onDatabase && onLogCatcher, parent);
        metterTableField.setEnabled(onDatabase && onMetterCatcher, parent);

        catchRuntimeErrorsField.setEnabled(onLogCatcher, finalPart);
        catchUserErrorsField.setEnabled(onLogCatcher, finalPart);
        catchUserWarningField.setEnabled(onLogCatcher, finalPart);
        catchRealtimeStatsField.setEnabled(onStatCatcher, finalPart);
    }

    private void addListeners() {
        SelectionListener listener = new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
            }

            public void widgetSelected(SelectionEvent e) {
                updateEnableStateFromDisplay();
            }
        };
        onStatCatcherField.getCheckbox().addSelectionListener(listener);
        onLogCatcherField.getCheckbox().addSelectionListener(listener);
        onMetterCatcherField.getCheckbox().addSelectionListener(listener);
        onFilesField.getCheckbox().addSelectionListener(listener);
        onDatabaseField.getCheckbox().addSelectionListener(listener);
        dbTypeField.getComboBoxControl(dbTypeComposite).addSelectionListener(listener);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.FieldEditorPreferencePage#performDefaults()
     */
    @Override
    protected void performDefaults() {
        super.performDefaults();
        updateEnableStateFromDisplay();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
     */
    public void init(IWorkbench workbench) {
    }

}
