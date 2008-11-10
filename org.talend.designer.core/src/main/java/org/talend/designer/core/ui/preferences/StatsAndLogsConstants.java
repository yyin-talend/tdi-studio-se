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
package org.talend.designer.core.ui.preferences;

/**
 * This class is used for storing the same variable name of JAVA and Perl.
 * 
 * @author ftang <br/>
 * 
 */
public class StatsAndLogsConstants {

    public static final String ORACLE_WITH_SID_CONN_TYPE = "ORACLE_SID"; //$NON-NLS-1$

    public static final String ORACLE_WITH_SERVICE_CONN_TYPE = "ORACLE_SERVICE_NAME"; //$NON-NLS-1$

    public static final String ORACLE_SID_ALIAS = "tOracleOutput_sid";

    public static final String ORACLE_SN_ALIAS = "tOracleOutput_servername";

    private static final String MYSQL = "MYSQL"; //$NON-NLS-1$

    private static final String POSTGRESQL = "POSTGRESQL"; //$NON-NLS-1$

    private static final String ODBC = "ODBC"; //$NON-NLS-1$

    public static final String ORACLE_OCI = "ORACLE_OCI";

    private static final String IBM_DB2 = "IBM_DB2"; //$NON-NLS-1$

    private static final String SYBASE = "SYBASE"; //$NON-NLS-1$

    private static final String SQL_SERVER = "SQL_SERVER"; //$NON-NLS-1$

    private static final String INGRES = "INGRES"; //$NON-NLS-1$

    private static final String INTERBASE = "INTERBASE"; //$NON-NLS-1$

    private static final String SQLITE = "SQLITE"; //$NON-NLS-1$

    private static final String FIREBIRD = "FIREBIRD"; //$NON-NLS-1$

    private static final String INFORMIX = "INFORMIX"; //$NON-NLS-1$

    private static final String ACCESS = "ACCESS"; //$NON-NLS-1$

    private static final String TERADATA = "TERADATA"; //$NON-NLS-1$

    private static final String[] PERL_DISPLAY_DBNAMES = new String[] {
            "Generic ODBC", "MySQL", "Microsoft SQL Server (Odbc driver)", "Oracle with SID", "Oracle with service name", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
            "PostgreSQL", "IBM DB2", "Sybase", "Ingres", "Interbase", "SQLite", "FireBird", "Informix", "Access", "Teradata" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ 

    private static final String[] JAVA_DISPLAY_DBNAMES = new String[] {
            "Generic ODBC", "MySQL", "Microsoft SQL Server", "Oracle with SID", "Oracle with service name", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
            "PostgreSQL", "IBM DB2", "Sybase", "Ingres", "Interbase", "SQLite", "FireBird", "Informix", "Access", "Teradata" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

    private static final String[] PERL_DB_COMPONENTS = new String[] {
            "tDBOutput", "tMysqlOutput", "tDBOutput", ORACLE_SID_ALIAS, ORACLE_SN_ALIAS, //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
            "tPostgresqlOutput", "tDB2Output", "tSybaseOutput", "tIngresOutput", "tInterbaseOutput", "tSQLiteOutput", "tFirebirdOutput", "tInformixOutput", "tAccessOutput", "tTeradataOutput" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

    private static final String[] JAVA_DB_COMPONENTS = new String[] {
            "tDBOutput", "tMysqlOutput", "tMSSqlOutput", ORACLE_SID_ALIAS, ORACLE_SN_ALIAS,//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
            "tPostgresqlOutput", "tDB2Output", "tSybaseOutput", "tIngresOutput", "tInterbaseOutput", "tSQLiteOutput", "tFirebirdOutput", "tInformixOutput", "tAccessOutput", "tTeradataOutput" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

    private static final String[] PERL_REPOSITORY_ITEMS = new String[] { ODBC, MYSQL, ODBC, ORACLE_WITH_SID_CONN_TYPE,
            ORACLE_WITH_SERVICE_CONN_TYPE, POSTGRESQL, IBM_DB2, SYBASE, INGRES, INTERBASE, SQLITE, FIREBIRD, INFORMIX, ACCESS,
            TERADATA };

    private static final String[] PERL_CODE_LIST = new String[] { ODBC, MYSQL, ODBC, "OCLE", "OCLE", POSTGRESQL, //$NON-NLS-1$
            IBM_DB2, SYBASE, INGRES, INTERBASE, SQLITE, FIREBIRD, INFORMIX, ACCESS, TERADATA };

    private static final String[] JAVA_REPOSITORY_ITEMS = new String[] { ODBC, MYSQL, SQL_SERVER, ORACLE_WITH_SID_CONN_TYPE,
            ORACLE_WITH_SERVICE_CONN_TYPE, POSTGRESQL, IBM_DB2, SYBASE, INGRES, INTERBASE, SQLITE, FIREBIRD, INFORMIX, ACCESS,
            TERADATA };

    private static final String[] JAVA_CODE_LIST = new String[] { ODBC, MYSQL, SQL_SERVER, "OCLE", "OCLE", POSTGRESQL, //$NON-NLS-1$
            IBM_DB2, SYBASE, INGRES, INTERBASE, SQLITE, FIREBIRD, INFORMIX, ACCESS, TERADATA };

    public static final String[] ORACLE_VERSION_DISPLAY = new String[] { "Oracle 11", "Oracle 10", "Oracle 9", "Oracle 8" };

    public static final String[] ORACLE_VERSION_CODE = new String[] { "ORACLE_11", "ORACLE_10", "ORACLE_9", "ORACLE_8" };

    public static final String[] ORACLE_VERSION_DRIVER = new String[] { "ojdbc5-11g.jar", "ojdbc14-10g.jar", "ojdbc14-9i.jar",
            "ojdbc12-8i.jar" };

    public static final String[][] DISPLAY_DBNAMES = new String[][] { PERL_DISPLAY_DBNAMES, JAVA_DISPLAY_DBNAMES };

    public static final String[][] DB_COMPONENTS = new String[][] { PERL_DB_COMPONENTS, JAVA_DB_COMPONENTS };

    public static final String[][] REPOSITORY_ITEMS = new String[][] { PERL_REPOSITORY_ITEMS, JAVA_REPOSITORY_ITEMS };

    public static final String[][] CODE_LIST = new String[][] { PERL_CODE_LIST, JAVA_CODE_LIST };

}
