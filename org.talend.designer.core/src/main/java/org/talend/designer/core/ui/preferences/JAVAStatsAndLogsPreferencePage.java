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

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.talend.commons.utils.workbench.preferences.ComboFieldEditor;
import org.talend.designer.core.DesignerPlugin;
import org.talend.designer.core.model.components.EmfComponent;

/**
 * DOC Administrator class global comment. Detailled comment <br/>
 * 
 */
public class JAVAStatsAndLogsPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    public JAVAStatsAndLogsPreferencePage() {
        super(GRID);
        setPreferenceStore(DesignerPlugin.getDefault().getPreferenceStore());
    }

    @Override
    protected void performApply() {
        super.performApply();
    }

    /**
     * Creates the field editors. Field editors are abstractions of the common GUI blocks needed to manipulate various
     * types of preferences. Each field editor knows how to save and restore itself.
     */
    public void createFieldEditors() {

        BooleanFieldEditor onFilesField;
        DirectoryFieldEditor filePathField;
        StringFieldEditor statsFileNameField;
        StringFieldEditor logsFileNameField;
        BooleanFieldEditor onDatabaseField;
        ComboFieldEditor propertyTypeField;
        ComboFieldEditor repositoryField;

        ComboFieldEditor dbTypeField;
        StringFieldEditor hostField;
        StringFieldEditor portField;
        StringFieldEditor dbNameField;
        StringFieldEditor schemaField;
        StringFieldEditor userField;
        StringFieldEditor passwordField;

        StringFieldEditor logsTableField;
        StringFieldEditor statsTableField;
        BooleanFieldEditor catchRuntimeErrorsField;
        BooleanFieldEditor catchUserErrorsField;
        BooleanFieldEditor catchUserWarningField;
        BooleanFieldEditor catchRealtimeStatsField;

        int languageType = 1;

        onFilesField = new BooleanFieldEditor(StatsAndLogsConstants.ON_FILE_FLAG[languageType].getName(),
                StatsAndLogsConstants.ON_FILE_FLAG[languageType].getDisplayName(), getFieldEditorParent());
        filePathField = new DirectoryFieldEditor(StatsAndLogsConstants.FILE_PATH[languageType].getName(),
                StatsAndLogsConstants.FILE_PATH[languageType].getDisplayName(), getFieldEditorParent());
        statsFileNameField = new StringFieldEditor(StatsAndLogsConstants.FILENAME_STATS[languageType].getName(),
                StatsAndLogsConstants.FILENAME_STATS[languageType].getDisplayName(), getFieldEditorParent());
        logsFileNameField = new StringFieldEditor(StatsAndLogsConstants.FILENAME_LOGS[languageType].getName(),
                StatsAndLogsConstants.FILENAME_LOGS[languageType].getDisplayName(), getFieldEditorParent());
        onDatabaseField = new BooleanFieldEditor(StatsAndLogsConstants.ON_DATABASE_FLAG[languageType].getName(),
                StatsAndLogsConstants.ON_DATABASE_FLAG[languageType].getDisplayName(), getFieldEditorParent());

         Composite composite = new Composite(getFieldEditorParent(), SWT.NONE);
        
         GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
         gridData.horizontalSpan = 4;
         composite.setLayoutData(gridData);

        String[][] stringsForPropertyType = new String[][] { { "Built-In", EmfComponent.BUILTIN } };
        // { "Repository", EmfComponent.REPOSITORY } };

        propertyTypeField = new ComboFieldEditor(StatsAndLogsConstants.PROPERTY_TYPE[languageType].getName(),
                StatsAndLogsConstants.PROPERTY_TYPE[languageType].getDisplayName(), stringsForPropertyType,
                composite);
        
        GridLayout gridLayout = (GridLayout) composite.getLayout();
        gridLayout.numColumns = 16;
        composite.setLayout(gridLayout);

        // propertyTypeField.setEnabled(false, getFieldEditorParent());

        // String[][] stringsForRepositoryPropertyType = new String[][] {};

        // repositoryField = new
        // ComboFieldEditor(StatsAndLogsConstants.REPOSITORY_PROPERTY_TYPE[languageType].getName(),
        // StatsAndLogsConstants.REPOSITORY_PROPERTY_TYPE[languageType].getDisplayName(),
        // stringsForRepositoryPropertyType, getFieldEditorParent());

        String[] strDisplay, strValue;
        strDisplay = new String[] { "Generic ODBC", "MySQL", "Microsoft SQL Server", "Oracle", "PostgreSQL", "IBM DB2",
                "Sybase", "Ingres" };
        strValue = new String[] { "tDBOutput", "tMysqlOutput", "tMSSqlOutput", "tOracleOutput", "tPostgresqlOutput",
                "tDB2Output", "tSybaseOutput", "tIngresOutput" };

        String[][] strsForDBType = new String[strDisplay.length][2];

        for (int i = 0; i < strDisplay.length; i++) {
            strsForDBType[i][0] = strDisplay[i];
            strsForDBType[i][1] = strValue[i];
        }

        dbTypeField = new ComboFieldEditor(StatsAndLogsConstants.DB_TYPE[languageType].getName(),
                StatsAndLogsConstants.DB_TYPE[languageType].getDisplayName(), strsForDBType, composite);
        hostField = new StringFieldEditor(StatsAndLogsConstants.HOST[languageType].getName(),
                StatsAndLogsConstants.HOST[languageType].getDisplayName(), getFieldEditorParent());
        portField = new StringFieldEditor(StatsAndLogsConstants.PORT[languageType].getName(),
                StatsAndLogsConstants.PORT[languageType].getDisplayName(), getFieldEditorParent());
        dbNameField = new StringFieldEditor(StatsAndLogsConstants.DBNAME[languageType].getName(),
                StatsAndLogsConstants.DBNAME[languageType].getDisplayName(), getFieldEditorParent());
        schemaField = new StringFieldEditor(StatsAndLogsConstants.SCHEMA_DB[languageType].getName(),
                StatsAndLogsConstants.SCHEMA_DB[languageType].getDisplayName(), getFieldEditorParent());
        userField = new StringFieldEditor(StatsAndLogsConstants.USER[languageType].getName(),
                StatsAndLogsConstants.USER[languageType].getDisplayName(), getFieldEditorParent());
        passwordField = new StringFieldEditor(StatsAndLogsConstants.PASS[languageType].getName(),
                StatsAndLogsConstants.PASS[languageType].getDisplayName(), getFieldEditorParent());

        statsTableField = new StringFieldEditor(StatsAndLogsConstants.TABLE_STATS[languageType].getName(),
                StatsAndLogsConstants.TABLE_STATS[languageType].getDisplayName(), getFieldEditorParent());
        logsTableField = new StringFieldEditor(StatsAndLogsConstants.TABLE_LOGS[languageType].getName(),
                StatsAndLogsConstants.TABLE_LOGS[languageType].getDisplayName(), getFieldEditorParent());

        catchRuntimeErrorsField = new BooleanFieldEditor(StatsAndLogsConstants.CATCH_RUNTIME_ERRORS[languageType]
                .getName(), StatsAndLogsConstants.CATCH_RUNTIME_ERRORS[languageType].getDisplayName(),
                getFieldEditorParent());
        catchUserErrorsField = new BooleanFieldEditor(StatsAndLogsConstants.CATCH_USER_ERRORS[languageType].getName(),
                StatsAndLogsConstants.CATCH_USER_ERRORS[languageType].getDisplayName(), getFieldEditorParent());
        catchUserWarningField = new BooleanFieldEditor(
                StatsAndLogsConstants.CATCH_USER_WARNING[languageType].getName(),
                StatsAndLogsConstants.CATCH_USER_WARNING[languageType].getDisplayName(), getFieldEditorParent());
        catchRealtimeStatsField = new BooleanFieldEditor(StatsAndLogsConstants.CATCH_REALTIME_STATS[languageType]
                .getName(), StatsAndLogsConstants.CATCH_REALTIME_STATS[languageType].getDisplayName(),
                getFieldEditorParent());

        addField(onFilesField);
        addField(filePathField);
        addField(statsFileNameField);
        addField(logsFileNameField);
        addField(onDatabaseField);
        addField(propertyTypeField);
        // addField(repositoryField);

        addField(dbTypeField);
        addField(hostField);
        addField(portField);
        addField(dbNameField);
        addField(schemaField);
        addField(userField);
        addField(passwordField);

        addField(statsTableField);
        addField(logsTableField);

        addField(catchRuntimeErrorsField);
        addField(catchUserErrorsField);
        addField(catchUserWarningField);
        addField(catchRealtimeStatsField);
    }

    public void init(IWorkbench workbench) {
    }

}
