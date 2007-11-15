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

import org.talend.core.model.metadata.designerproperties.RepositoryToComponentProperty;

/**
 * This class is used for storing the same variable name of JAVA and Perl.
 * 
 * @author ftang <br/>
 * 
 */
public class StatsAndLogsConstants {

    private static final String[] PERL_DISPLAY_DBNAMES = new String[] {
            "Generic ODBC", "MySQL", "Microsoft SQL Server (Odbc driver)", "Oracle", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
            "PostgreSQL", "IBM DB2", "Sybase", "Ingres", "Interbase", "SQLite", "FireBird", "Informix",  "Access", "Teradata"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ 

    private static final String[] JAVA_DISPLAY_DBNAMES = new String[] {
            "Generic ODBC", "MySQL", "Microsoft SQL Server", "Oracle", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
            "PostgreSQL", "IBM DB2", "Sybase", "Ingres", "Interbase", "SQLite", "FireBird", "Informix", "Access", "Teradata" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

    private static final String[] PERL_DB_COMPONENTS = new String[] {
            "tDBOutput", "tMysqlOutput", "tDBOutput", "tOracleOutput", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
            "tPostgresqlOutput", "tDB2Output", "tSybaseOutput", "tIngresOutput", "tInterbaseOutput", "tSQLiteOutput", "tFirebirdOutput", "tInformixOutput", "tAccessOutput", "tTeradataOutput" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

    private static final String[] JAVA_DB_COMPONENTS = new String[] {
            "tDBOutput", "tMysqlOutput", "tMSSqlOutput", "tOracleOutput", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
            "tPostgresqlOutput", "tDB2Output", "tSybaseOutput", "tIngresOutput", "tInterbaseOutput", "tSQLiteOutput", "tFirebirdOutput", "tInformixOutput", "tAccessOutput", "tTeradataOutput" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

    private static final String[] PERL_REPOSITORY_ITEMS = new String[] { RepositoryToComponentProperty.ODBC,
            RepositoryToComponentProperty.MYSQL, RepositoryToComponentProperty.ODBC,
            RepositoryToComponentProperty.ORACLE, RepositoryToComponentProperty.POSTGRESQL,
            RepositoryToComponentProperty.IBM_DB2, RepositoryToComponentProperty.SYBASE,
            RepositoryToComponentProperty.INGRES, RepositoryToComponentProperty.INTERBASE,
            RepositoryToComponentProperty.SQLITE, RepositoryToComponentProperty.FIREBIRD,
            RepositoryToComponentProperty.INFORMIX, RepositoryToComponentProperty.ACCESS,
            RepositoryToComponentProperty.TERADATA};

    private static final String[] PERL_CODE_LIST = new String[] { RepositoryToComponentProperty.ODBC,
            RepositoryToComponentProperty.MYSQL,
            RepositoryToComponentProperty.ODBC,
            "OCLE", RepositoryToComponentProperty.POSTGRESQL, //$NON-NLS-1$
            RepositoryToComponentProperty.IBM_DB2, RepositoryToComponentProperty.SYBASE,
            RepositoryToComponentProperty.INGRES, RepositoryToComponentProperty.INTERBASE,
            RepositoryToComponentProperty.SQLITE, RepositoryToComponentProperty.FIREBIRD,
            RepositoryToComponentProperty.INFORMIX, RepositoryToComponentProperty.ACCESS,
            RepositoryToComponentProperty.TERADATA};

    private static final String[] JAVA_REPOSITORY_ITEMS = new String[] { RepositoryToComponentProperty.ODBC,
            RepositoryToComponentProperty.MYSQL, RepositoryToComponentProperty.SQL_SERVER,
            RepositoryToComponentProperty.ORACLE, RepositoryToComponentProperty.POSTGRESQL,
            RepositoryToComponentProperty.IBM_DB2, RepositoryToComponentProperty.SYBASE,
            RepositoryToComponentProperty.INGRES, RepositoryToComponentProperty.INTERBASE,
            RepositoryToComponentProperty.SQLITE, RepositoryToComponentProperty.FIREBIRD,
            RepositoryToComponentProperty.INFORMIX, RepositoryToComponentProperty.ACCESS,
            RepositoryToComponentProperty.TERADATA};

    private static final String[] JAVA_CODE_LIST = new String[] { RepositoryToComponentProperty.ODBC,
            RepositoryToComponentProperty.MYSQL,
            RepositoryToComponentProperty.SQL_SERVER,
            "OCLE", RepositoryToComponentProperty.POSTGRESQL, //$NON-NLS-1$
            RepositoryToComponentProperty.IBM_DB2, RepositoryToComponentProperty.SYBASE,
            RepositoryToComponentProperty.INGRES, RepositoryToComponentProperty.INTERBASE,
            RepositoryToComponentProperty.SQLITE, RepositoryToComponentProperty.FIREBIRD,
            RepositoryToComponentProperty.INFORMIX, RepositoryToComponentProperty.ACCESS,
            RepositoryToComponentProperty.TERADATA};

    public static final String[][] DISPLAY_DBNAMES = new String[][] { PERL_DISPLAY_DBNAMES, JAVA_DISPLAY_DBNAMES };

    public static final String[][] DB_COMPONENTS = new String[][] { PERL_DB_COMPONENTS, JAVA_DB_COMPONENTS };

    public static final String[][] REPOSITORY_ITEMS = new String[][] { PERL_REPOSITORY_ITEMS, JAVA_REPOSITORY_ITEMS };

    public static final String[][] CODE_LIST = new String[][] { PERL_CODE_LIST, JAVA_CODE_LIST };

}
