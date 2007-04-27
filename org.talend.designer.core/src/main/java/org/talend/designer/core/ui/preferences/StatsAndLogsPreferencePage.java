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
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.talend.commons.utils.workbench.preferences.ComboFieldEditor;
import org.talend.designer.core.DesignerPlugin;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.model.components.EmfComponent;

/**
 * This class is used to create a preference page for tabbed page 'Stats & logs'. <br/>
 * 
 */
public class StatsAndLogsPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    public StatsAndLogsPreferencePage() {
        super(GRID);
        setPreferenceStore(DesignerPlugin.getDefault().getPreferenceStore());
    }

    @Override
    protected void performApply() {
        // TODO Auto-generated method stub
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

        StringFieldEditor dbTypeField;
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

        onFilesField = new BooleanFieldEditor("ON_FILES_FLAG", "On Files", getFieldEditorParent());
        filePathField = new DirectoryFieldEditor("FIlLE_PATH", "File Path", getFieldEditorParent());
        statsFileNameField = new StringFieldEditor("FILENAME_STATS", "Stats File Name", getFieldEditorParent());
        logsFileNameField = new StringFieldEditor("FILENAME_LOGS", "Logs File Name", getFieldEditorParent());
        onDatabaseField = new BooleanFieldEditor("ON_DATABASE_FLAG", "On Database", getFieldEditorParent());

        String[][] stringSForPropertyType = new String[][] { { EmfComponent.BUILTIN, EmfComponent.TEXT_BUILTIN } };
        propertyTypeField = new ComboFieldEditor(EParameterName.PROPERTY_TYPE.getDisplayName(),
                EParameterName.PROPERTY_TYPE.getDisplayName(), stringSForPropertyType, getFieldEditorParent());

        dbTypeField = new StringFieldEditor("DB_NAME", "DB type", getFieldEditorParent());
        hostField = new StringFieldEditor("HOST", "Host", getFieldEditorParent());
        portField = new StringFieldEditor("PORT", "Port", getFieldEditorParent());
        dbNameField = new StringFieldEditor("DB_NAME", "DB name", getFieldEditorParent());
        schemaField = new StringFieldEditor("SCHEMA", "Schema", getFieldEditorParent());
        userField = new StringFieldEditor("USER", "User", getFieldEditorParent());
        passwordField = new StringFieldEditor("PASS", "Password", getFieldEditorParent());

        statsTableField = new StringFieldEditor("TABLE_STATS", "Stats table", getFieldEditorParent());
        logsTableField = new StringFieldEditor("TABLE_LOGS", "Logs table", getFieldEditorParent());

        catchRuntimeErrorsField = new BooleanFieldEditor("CATCH_RUNTIME_ERRORS", "Catch runtime errors",
                getFieldEditorParent());
        catchUserErrorsField = new BooleanFieldEditor("CATCH_USER_ERRORS", "Catch user errors", getFieldEditorParent());
        catchUserWarningField = new BooleanFieldEditor("CATCH_USER_WARNING", "Catch user warning",
                getFieldEditorParent());
        catchRealtimeStatsField = new BooleanFieldEditor("CATCH_USER_WARNING", "Catch realtime statistics",
                getFieldEditorParent());

        addField(onFilesField);
        addField(filePathField);
        addField(statsFileNameField);
        addField(logsFileNameField);
        addField(onDatabaseField);
        addField(propertyTypeField);

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
