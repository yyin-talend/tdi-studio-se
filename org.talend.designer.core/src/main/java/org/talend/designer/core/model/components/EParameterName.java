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
package org.talend.designer.core.model.components;

import org.talend.designer.core.i18n.Messages;

/**
 * Enumeration that describes all the standard name used in the properties.
 * 
 * $Id$
 * 
 */
public enum EParameterName {
    NAME(Messages.getString("EParameterName.Name")), //$NON-NLS-1$
    UNIQUE_NAME(Messages.getString("EParameterName.uniqueName")), //$NON-NLS-1$
    TRANSLATED_UNIQUE_NAME("Translated Unique Name"), //$NON-NLS-1$
    AUTHOR(Messages.getString("EParameterName.Author")), //$NON-NLS-1$
    PURPOSE(Messages.getString("EParameterName.Purpose")), //$NON-NLS-1$
    HELP(Messages.getString("EParameterName.help")), //$NON-NLS-1$
    START(Messages.getString("EParameterName.start")), //$NON-NLS-1$
    STARTABLE(Messages.getString("EParameterName.startable")), //$NON-NLS-1$
    STATUS(Messages.getString("EParameterName.Status")), //$NON-NLS-1$
    DESCRIPTION(Messages.getString("EParameterName.Description")), //$NON-NLS-1$
    VERSION(Messages.getString("EParameterName.Version")), //$NON-NLS-1$
    PLATFORM(Messages.getString("EParameterName.Platform")), //$NON-NLS-1$
    FAMILY(Messages.getString("EParameterName.Family")), //$NON-NLS-1$
    LOG(Messages.getString("EParameterName.Log")), //$NON-NLS-1$
    ACTIVATE(Messages.getString("EParameterName.Activate")), //$NON-NLS-1$
    LABEL(Messages.getString("EParameterName.LabelFormat")), //$NON-NLS-1$
    HINT(Messages.getString("EParameterName.HintFormat")), //$NON-NLS-1$
    SHOW_HINT(Messages.getString("EParameterName.ShowHint")), //$NON-NLS-1$
    COMMENT(Messages.getString("EParameterName.Comment")), //$NON-NLS-1$
    LOG_FILENAME(Messages.getString("EParameterName.FileName")), //$NON-NLS-1$
    LEVEL_LOG_TO_FILE(Messages.getString("EParameterName.Level.Log.File")), //$NON-NLS-1$
    LEVEL_LOG_TO_DB(Messages.getString("EParameterName.Level.Log.Db")), //$NON-NLS-1$
    LEVEL_LOG_TO_STDOUT(Messages.getString("EParameterName.Level.Log.Stdout")), //$NON-NLS-1$
    LOG_TO_FILE(Messages.getString("EParameterName.Log.File")), //$NON-NLS-1$
    LOG_TO_DB(Messages.getString("EParameterName.Log.Db")), //$NON-NLS-1$
    LOG_TO_STDOUT(Messages.getString("EParameterName.Log.Stdout")), //$NON-NLS-1$
    SCHEMA_TYPE(Messages.getString("EParameterName.schemaType")), //$NON-NLS-1$
    REPOSITORY_SCHEMA_TYPE("Repository"), //$NON-NLS-1$  Hidden parameter so no translation needed
    QUERYSTORE_TYPE(Messages.getString("EParameterName.queryType")), //$NON-NLS-1$
    REPOSITORY_QUERYSTORE_TYPE("Repository"), //$NON-NLS-1$  Hidden parameter so no translation needed
    PROPERTY_TYPE(Messages.getString("EParameterName.propertyType")), //$NON-NLS-1$
    REPOSITORY_PROPERTY_TYPE("Repository"), //$NON-NLS-1$  Hidden parameter so no translation needed
    CONDITION(Messages.getString("EParameterName.condition")), //$NON-NLS-1$
    COMPONENT_NAME(Messages.getString("EParameterName.componentName")), //$NON-NLS-1$
    TRANSLATED_COMPONENT_NAME("Translated Component Name"), //$NON-NLS-1$  Hidden parameter so no translation needed
    UPDATE_COMPONENTS("Update components"), //$NON-NLS-1$  Hidden parameter so no translation needed
    PROCESS_TYPE(Messages.getString("EParameterName.generateCode")), //$NON-NLS-1$
    PROCESS_TYPE_PROCESS(Messages.getString("EParameterName.process")), //$NON-NLS-1$
    PROCESS_TYPE_CONTEXT(Messages.getString("EParameterName.context")), //$NON-NLS-1$
    PREVIEW(Messages.getString("EParameterName.preview")), //$NON-NLS-1$
    COLUMN_LIST(Messages.getString("EParameterName.columnList")), //$NON-NLS-1$
    PREV_COLUMN_LIST(Messages.getString("EParameterName.prevColumnList")), //$NON-NLS-1$
    LOOKUP_COLUMN_LIST(Messages.getString("EParameterName.lookupColumnList")), //$NON-NLS-1$
    TSTATCATCHER_STATS(Messages.getString("EParameterName.tStatCatcherStats")), //$NON-NLS-1$
    COMP_DEFAULT_FILE_DIR("COMP_DEFAULT_FILE_DIR"), //$NON-NLS-1$  Hidden parameter so no translation needed
    REPOSITORY_ENCODING_TYPE("Repository"),
    ENCODING_TYPE(Messages.getString("EParameterName.encodingType")),
    COMPONENT_LIST("Component List"),
    MAPPING_TYPE("Mapping"),

    PERL_ON_FILES_FLAG(Messages.getString("EParameterName.onFileFlag")),
    PERL_FILE_PATH(Messages.getString("EParameterName.filePath")),
    PERL_FILENAME_STATS(Messages.getString("EParameterName.fileNameStats")),
    PERL_FILENAME_LOGS(Messages.getString("EParameterName.fileNameLogs")),
    PERL_ON_DATABASE_FLAG(Messages.getString("EParameterName.onDatabaseFlag")),
    PERL_DB_TYPE(Messages.getString("EParameterName.dbType")),
    PERL_PROPERTY_TYPE(Messages.getString("EParameterName.propertyType")), //$NON-NLS-1$
    PERL_REPOSITORY_PROPERTY_TYPE("Repository"), //$NON-NLS-1$  Hidden parameter so no translation needed
    PERL_HOST(Messages.getString("EParameterName.host")),
    PERL_PORT(Messages.getString("EParameterName.port")),
    PERL_DBNAME(Messages.getString("EParameterName.dbName")),
    PERL_SCHEMA_DB(Messages.getString("EParameterName.schemaDb")),
    PERL_USER(Messages.getString("EParameterName.user")),
    PERL_PASS(Messages.getString("EParameterName.password")),
    PERL_TABLE_STATS(Messages.getString("EParameterName.tableStats")),
    PERL_TABLE_LOGS(Messages.getString("EParameterName.tableLogs")),
    PERL_CATCH_RUNTIME_ERRORS(Messages.getString("EParameterName.catchRuntimeErrors")),
    PERL_CATCH_USER_ERRORS(Messages.getString("EParameterName.catchUserErrors")),
    PERL_CATCH_USER_WARNING(Messages.getString("EParameterName.catchUserWarning")),
    PERL_CATCH_REALTIME_STATS(Messages.getString("EParameterName.catchRealtimeStats")),

    JAVA_ON_FILES_FLAG(Messages.getString("EParameterName.onFileFlag")),
    JAVA_FILE_PATH(Messages.getString("EParameterName.filePath")),
    JAVA_FILENAME_STATS(Messages.getString("EParameterName.fileNameStats")),
    JAVA_FILENAME_LOGS(Messages.getString("EParameterName.fileNameLogs")),
    JAVA_ON_DATABASE_FLAG(Messages.getString("EParameterName.onDatabaseFlag")),
    JAVA_DB_TYPE(Messages.getString("EParameterName.dbType")),
    JAVA_PROPERTY_TYPE(Messages.getString("EParameterName.propertyType")), //$NON-NLS-1$
    JAVA_REPOSITORY_PROPERTY_TYPE("Repository"), //$NON-NLS-1$  Hidden parameter so no translation needed
    JAVA_HOST(Messages.getString("EParameterName.host")),
    JAVA_PORT(Messages.getString("EParameterName.port")),
    JAVA_DBNAME(Messages.getString("EParameterName.dbName")),
    JAVA_SCHEMA_DB(Messages.getString("EParameterName.schemaDb")),
    JAVA_USER(Messages.getString("EParameterName.user")),
    JAVA_PASS(Messages.getString("EParameterName.password")),
    JAVA_TABLE_STATS(Messages.getString("EParameterName.tableStats")),
    JAVA_TABLE_LOGS(Messages.getString("EParameterName.tableLogs")),
    JAVA_CATCH_RUNTIME_ERRORS(Messages.getString("EParameterName.catchRuntimeErrors")),
    JAVA_CATCH_USER_ERRORS(Messages.getString("EParameterName.catchUserErrors")),
    JAVA_CATCH_USER_WARNING(Messages.getString("EParameterName.catchUserWarning")),
    JAVA_CATCH_REALTIME_STATS(Messages.getString("EParameterName.catchRealtimeStats")), 
    
    ON_CONSOLE_FLAG(Messages.getString("EParameterName.onConsoleFlag"));

    private String displayName;

    EParameterName(String displayName) {
        this.displayName = displayName;
    }

    public String getName() {
        return this.toString();
    }

    public String getDisplayName() {
        return this.displayName;
    }
}
