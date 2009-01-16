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
    CONNECTION_FORMAT(Messages.getString("EParameterName.ConnectionFormat")), //$NON-NLS-1$
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
    PROCESS_TYPE_VERSION(Messages.getString("EParameterName.Version")),
    PROCESS_TYPE_CONTEXT(Messages.getString("EParameterName.context")), //$NON-NLS-1$
    PREVIEW(Messages.getString("EParameterName.preview")), //$NON-NLS-1$
    COLUMN_LIST(Messages.getString("EParameterName.columnList")), //$NON-NLS-1$
    CONNECTION_LIST(Messages.getString("EParameterName.connectionList")), //$NON-NLS-1$
    PREV_COLUMN_LIST(Messages.getString("EParameterName.prevColumnList")), //$NON-NLS-1$
    LOOKUP_COLUMN_LIST(Messages.getString("EParameterName.lookupColumnList")), //$NON-NLS-1$
    TSTATCATCHER_STATS(Messages.getString("EParameterName.tStatCatcherStats")), //$NON-NLS-1$
    COMP_DEFAULT_FILE_DIR("COMP_DEFAULT_FILE_DIR"), //$NON-NLS-1$  Hidden parameter so no translation needed
    REPOSITORY_ALLOW_AUTO_SWITCH("REPOSITORY_ALLOW_AUTO_SWITCH"), //$NON-NLS-1$  Hidden parameter so no translation needed
    ENCODING_TYPE(Messages.getString("EParameterName.encodingType")), //$NON-NLS-1$
    COMPONENT_LIST(Messages.getString("EParameterName.componentList")), //$NON-NLS-1$
    MAPPING_TYPE(Messages.getString("EParameterName.mapping")), //$NON-NLS-1$
    DUMMY("DUMMY"), //$NON-NLS-1$ Hidden parameter so no translation needed
    DBTABLE(Messages.getString("EParameterName.TableName")), //$NON-NLS-1$
    QUERY_CONDITION("Query Condition"),
    MODULE_LIST(Messages.getString("EParameterName.ModuleList")), //$NON-NLS-1$
    CURRENT_OS(Messages.getString("EParameterName.CurrentOS")), //$NON-NLS-1$
    IREPORT_PATH("IREPORT_PATH"), //$NON-NLS-1$  Hidden parameter so no translation needed
    CONNECTION("CONNECTION"), //$NON-NLS-1$  Parameter without label displayed so no translation needed
    NOT_SYNCHRONIZED_SCHEMA("NOT_SYNCHRONIZED_SCHEMA"), //$NON-NLS-1$  not displayed so no translation needed
    SCHEMA_OPTIONS(Messages.getString("EParameterName.schemaOptions")),
    COLLAPSED("COLLAPSED"), //$NON-NLS-1$  Hidden parameter so no translation needed
    SUBJOB_DISPLAYED("DISPLAY_SUBJOB"), //$NON-NLS-1$  Hidden parameter so no translation needed
    SHOW_SUBJOB_TITLE("Show subjob title"),
    SUBJOB_TITLE("Title"),
    SUBJOB_TITLE_COLOR("Title color"),
    SUBJOB_COLOR("Subjob color"),
    SQLPATTERN_DB_NAME("SQL Pattern DB"),
    SQLPATTERN_VALUE("SQL Pattern"),
    NOTE_COLOR("Note color"),
    NOTETXT_COLOR("Font color"),
    NOTETXT_LEFT("left"),
    NOTETXT_RIGHT("right"),
    NOTETXT_CENTER("center"),
    NOTETXT_TOP("top"),
    NOTETXT_BOTTOM("bottom"),
    NOTELABEL_CENTER("center"),
    NOTE_FONT("Font"),
    FONT_SIZE("Font Size"),
    FONT_BOLD("Bold"),
    FONT_ITALIC("Italic"),
    NOTE_LINECOLOR("Note Line Color"),
    SCHEMAS("Schema(s)"),
    /**
     * For stats & logs parameters.
     */
    ON_STATCATCHER_FLAG(Messages.getString("EParameterName.UseStatistics")), //$NON-NLS-1$
    ON_LOGCATCHER_FLAG(Messages.getString("EParameterName.UseLogs")), //$NON-NLS-1$
    ON_METERCATCHER_FLAG(Messages.getString("EParameterName.UseVolumetrics")), //$NON-NLS-1$
    ON_CONSOLE_FLAG(Messages.getString("EParameterName.onConsoleFlag")), //$NON-NLS-1$
    ON_FILES_FLAG(Messages.getString("EParameterName.onFileFlag")), //$NON-NLS-1$
    FILE_PATH(Messages.getString("EParameterName.filePath")), //$NON-NLS-1$
    FILENAME(Messages.getString("EParameterName.FileName")), //$NON-NLS-1$
    FILENAME_STATS(Messages.getString("EParameterName.fileNameStats")), //$NON-NLS-1$
    FILENAME_LOGS(Messages.getString("EParameterName.fileNameLogs")), //$NON-NLS-1$
    FILENAME_METTER(Messages.getString("EParameterName.MeterFileName")), //$NON-NLS-1$
    ON_DATABASE_FLAG(Messages.getString("EParameterName.onDatabaseFlag")), //$NON-NLS-1$
    DB_TYPE(Messages.getString("EParameterName.dbType")), //$NON-NLS-1$
    CONNECTION_TYPE(Messages.getString("EParameterName.connectionType")), //$NON-NLS-1$
    HOST(Messages.getString("EParameterName.host")), //$NON-NLS-1$
    PORT(Messages.getString("EParameterName.port")), //$NON-NLS-1$
    DBNAME(Messages.getString("EParameterName.dbName")), //$NON-NLS-1$
    SCHEMA_DB(Messages.getString("EParameterName.schemaDb")), //$NON-NLS-1$
    USER(Messages.getString("EParameterName.user")), //$NON-NLS-1$
    PASS(Messages.getString("EParameterName.password")), //$NON-NLS-1$
    TABLE_STATS(Messages.getString("EParameterName.tableStats")), //$NON-NLS-1$
    TABLE_LOGS(Messages.getString("EParameterName.tableLogs")), //$NON-NLS-1$
    TABLE_METER(Messages.getString("EParameterName.tableMeter")), //$NON-NLS-1$
    CATCH_RUNTIME_ERRORS(Messages.getString("EParameterName.catchRuntimeErrors")), //$NON-NLS-1$
    CATCH_USER_ERRORS(Messages.getString("EParameterName.catchUserErrors")), //$NON-NLS-1$
    CATCH_USER_WARNING(Messages.getString("EParameterName.catchUserWarning")), //$NON-NLS-1$
    CATCH_REALTIME_STATS(Messages.getString("EParameterName.catchRealtimeStats")), //$NON-NLS-1$
    PROPERTIES(Messages.getString("EParameterName.additionParam")), //$NON-NLS-1$
    DBFILE(Messages.getString("EParameterName.database")), //$NON-NLS-1$
    DB_VERSION(Messages.getString("EParameterName.dbVersion")), //$NON-NLS-1$
    /**
     * For Job Settings extra parameters.
     */
    MULTI_THREAD_EXECATION(Messages.getString("EParameterName.MultiThread")), //$NON-NLS-1$
    IMPLICIT_TCONTEXTLOAD(Messages.getString("EParameterName.ImplicitContextLoad")), //$NON-NLS-1$
    IMPLICIT_TCONTEXTLOAD_FILE(Messages.getString("EParameterName.FromFile")), //$NON-NLS-1$
    FIELDSEPARATOR(Messages.getString("EParameterName.FieldSeparator")),
    ROWSEPARATOR(Messages.getString("EParameterName.RowSeparator")),
    FROM_FILE_FLAG(Messages.getString("EParameterName.fromFileFlag")), //$NON-NLS-1$
    FROM_DATABASE_FLAG(Messages.getString("EParameterName.fromDatabaseFlag")), //$NON-NLS-1$
    // implict tConextLoad parameters.
    LOAD_NEW_VARIABLE(Messages.getString("EParameterName.LoadNewVariableLabel")), //$NON-NLS-1$
    NOT_LOAD_OLD_VARIABLE(Messages.getString("EParameterName.NotLoadOldVariableLabel")), //$NON-NLS-1$
    PRINT_OPERATIONS(Messages.getString("EParameterName.PrintOperations")), //$NON-NLS-1$
    DISABLE_ERROR(Messages.getString("EParameterName.DisableErrors")), //$NON-NLS-1$
    DISABLE_INFO(Messages.getString("EParameterName.DisableInfos")), //$NON-NLS-1$
    DISABLE_WARNINGS(Messages.getString("EParameterName.DisableWarnings")), //$NON-NLS-1$
    /*
     * PERL_ON_FILES_FLAG(Messages.getString("EParameterName.onFileFlag")), //$NON-NLS-1$
     * PERL_FILE_PATH(Messages.getString("EParameterName.filePath")), //$NON-NLS-1$
     * PERL_FILENAME_STATS(Messages.getString("EParameterName.fileNameStats")), //$NON-NLS-1$
     * PERL_FILENAME_LOGS(Messages.getString("EParameterName.fileNameLogs")), //$NON-NLS-1$
     * PERL_ON_DATABASE_FLAG(Messages.getString("EParameterName.onDatabaseFlag")), //$NON-NLS-1$
     * PERL_DB_TYPE(Messages.getString("EParameterName.dbType")), //$NON-NLS-1$
     * PERL_PROPERTY_TYPE(Messages.getString("EParameterName.propertyType")), //$NON-NLS-1$
     * PERL_REPOSITORY_PROPERTY_TYPE("Repository"), //$NON-NLS-1$ Hidden parameter so no translation needed
     * PERL_HOST(Messages.getString("EParameterName.host")), //$NON-NLS-1$
     * PERL_PORT(Messages.getString("EParameterName.port")), //$NON-NLS-1$
     * PERL_DBNAME(Messages.getString("EParameterName.dbName")), //$NON-NLS-1$
     * PERL_SCHEMA_DB(Messages.getString("EParameterName.schemaDb")), //$NON-NLS-1$
     * PERL_USER(Messages.getString("EParameterName.user")), //$NON-NLS-1$
     * PERL_PASS(Messages.getString("EParameterName.password")), //$NON-NLS-1$
     * PERL_TABLE_STATS(Messages.getString("EParameterName.tableStats")), //$NON-NLS-1$
     * PERL_TABLE_LOGS(Messages.getString("EParameterName.tableLogs")), //$NON-NLS-1$
     * PERL_CATCH_RUNTIME_ERRORS(Messages.getString("EParameterName.catchRuntimeErrors")), //$NON-NLS-1$
     * PERL_CATCH_USER_ERRORS(Messages.getString("EParameterName.catchUserErrors")), //$NON-NLS-1$
     * PERL_CATCH_USER_WARNING(Messages.getString("EParameterName.catchUserWarning")), //$NON-NLS-1$
     * PERL_CATCH_REALTIME_STATS(Messages.getString("EParameterName.catchRealtimeStats")), //$NON-NLS-1$
     * 
     * JAVA_ON_FILES_FLAG(Messages.getString("EParameterName.onFileFlag")), //$NON-NLS-1$
     * JAVA_FILE_PATH(Messages.getString("EParameterName.filePath")), //$NON-NLS-1$
     * JAVA_FILENAME_STATS(Messages.getString("EParameterName.fileNameStats")), //$NON-NLS-1$
     * JAVA_FILENAME_LOGS(Messages.getString("EParameterName.fileNameLogs")), //$NON-NLS-1$
     * JAVA_ON_DATABASE_FLAG(Messages.getString("EParameterName.onDatabaseFlag")), //$NON-NLS-1$
     * JAVA_DB_TYPE(Messages.getString("EParameterName.dbType")), //$NON-NLS-1$
     * JAVA_PROPERTY_TYPE(Messages.getString("EParameterName.propertyType")), //$NON-NLS-1$
     * JAVA_REPOSITORY_PROPERTY_TYPE("Repository"), //$NON-NLS-1$ Hidden parameter so no translation needed
     * JAVA_HOST(Messages.getString("EParameterName.host")), //$NON-NLS-1$
     * JAVA_PORT(Messages.getString("EParameterName.port")), //$NON-NLS-1$
     * JAVA_DBNAME(Messages.getString("EParameterName.dbName")), //$NON-NLS-1$
     * JAVA_SCHEMA_DB(Messages.getString("EParameterName.schemaDb")), //$NON-NLS-1$
     * JAVA_USER(Messages.getString("EParameterName.user")), //$NON-NLS-1$
     * JAVA_PASS(Messages.getString("EParameterName.password")), //$NON-NLS-1$
     * JAVA_TABLE_STATS(Messages.getString("EParameterName.tableStats")), //$NON-NLS-1$
     * JAVA_TABLE_LOGS(Messages.getString("EParameterName.tableLogs")), //$NON-NLS-1$
     * JAVA_CATCH_RUNTIME_ERRORS(Messages.getString("EParameterName.catchRuntimeErrors")), //$NON-NLS-1$
     * JAVA_CATCH_USER_ERRORS(Messages.getString("EParameterName.catchUserErrors")), //$NON-NLS-1$
     * JAVA_CATCH_USER_WARNING(Messages.getString("EParameterName.catchUserWarning")), //$NON-NLS-1$
     * JAVA_CATCH_REALTIME_STATS(Messages.getString("EParameterName.catchRealtimeStats")), //$NON-NLS-1$
     */

    // main tab within job setting.
    JOB_MAIN_NAME("Name"),
    JOB_MAIN_AUTHOR("Author"),
    JOB_MAIN_VERSION("Version"),
    JOB_MAIN_PURPOSE("Purpose"),
    JOB_MAIN_STATUS("Status"),
    JOB_MAIN_DESCRIPTION("Description"),
    JOB_MAIN_CREATION("Creation"),
    JOB_MAIN_MODIFICATION("Modification"),

    // version tab within job setting.
    JOB_VERSION("Version"),

    PARALLELIZE("Enable parallel execution"),
    PARALLELIZE_NUMBER("Number of parallel executions"),
    PARALLELIZE_UNIT_SIZE("Parallelize Buffer Unit Size"),

    // use project setting
    IMPLICITCONTEXT_USE_PROJECT_SETTINGS(Messages.getString("Extra.UseProjectSettings")),
    STATANDLOG_USE_PROJECT_SETTINGS(Messages.getString("StatsAndLogs.UseProjectSettings"));

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
