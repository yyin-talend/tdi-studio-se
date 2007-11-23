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
    CONNECTION_FORMAT("Connection format"),
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
    DBTABLE("Table Name"),
    MODULE_LIST("Module List"),

    /**
     * For stats & logs parameters.
     */
    ON_STATCATCHER_FLAG("Use statistics (tStatCatcher)"),
    ON_LOGCATCHER_FLAG("Use logs (tLogCatcher)"),
    ON_METERCATCHER_FLAG("Use volumetrics (tMeterCatcher)"),
    ON_CONSOLE_FLAG(Messages.getString("EParameterName.onConsoleFlag")), //$NON-NLS-1$
    ON_FILES_FLAG(Messages.getString("EParameterName.onFileFlag")), //$NON-NLS-1$
    FILE_PATH(Messages.getString("EParameterName.filePath")), //$NON-NLS-1$
    FILENAME_STATS(Messages.getString("EParameterName.fileNameStats")), //$NON-NLS-1$
    FILENAME_LOGS(Messages.getString("EParameterName.fileNameLogs")), //$NON-NLS-1$
    FILENAME_METTER("Metter file name"),
    ON_DATABASE_FLAG(Messages.getString("EParameterName.onDatabaseFlag")), //$NON-NLS-1$
    DB_TYPE(Messages.getString("EParameterName.dbType")), //$NON-NLS-1$
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
    ;

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
