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

package org.talend.designer.core.ui.editor.process;

import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.properties.DatabaseConnectionItem;
import org.talend.core.model.properties.DelimitedFileConnectionItem;
import org.talend.core.model.properties.ExcelFileConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.PositionalFileConnectionItem;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.RegExFileConnectionItem;
import org.talend.core.model.properties.SAPConnectionItem;
import org.talend.core.model.properties.SalesforceSchemaConnectionItem;
import org.talend.core.model.properties.WSDLSchemaConnectionItem;
import org.talend.core.model.properties.XmlFileConnectionItem;
import org.talend.core.model.properties.impl.ConnectionItemImpl;
import org.talend.core.model.properties.impl.LdifFileConnectionItemImpl;
import org.talend.core.model.repository.ERepositoryObjectType;

/**
 * DOC bqian TalendEditor class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ææäº, 29 ä¹æ 2006) nrousseau $
 * 
 */
public enum EDatabaseComponentName {
    // DATABASES
    DBMYSQL(DatabaseConnectionItem.class, EDatabaseTypeName.MYSQL, "tMysqlInput", "tMysqlOutput", true),
    DBPSQL(DatabaseConnectionItem.class, EDatabaseTypeName.PSQL, "tPostgresqlInput", "tPostgresqlOutput", true),
    PARACCEL(DatabaseConnectionItem.class, EDatabaseTypeName.PARACCEL, "tParAccelInput", "tParAccelOutput", true),
    GREENPLUM(DatabaseConnectionItem.class, EDatabaseTypeName.GREENPLUM, "tGreenplumInput", "tGreenplumOutput", true),

    NETEZZA(DatabaseConnectionItem.class, EDatabaseTypeName.NETEZZA, "tNetezzaInput", "tNetezzaOutput", true),

    DBPLUSPSQL(DatabaseConnectionItem.class, EDatabaseTypeName.PLUSPSQL, "tPostgresPlusInput", "tPostgresPlusOutput", true),
    DBORACLEFORSID(DatabaseConnectionItem.class, EDatabaseTypeName.ORACLEFORSID, "tOracleInput", "tOracleOutput", true),

    DBORACLESN(DatabaseConnectionItem.class, EDatabaseTypeName.ORACLESN, "tOracleInput", "tOracleOutput", true),

    DBGODBC(DatabaseConnectionItem.class, EDatabaseTypeName.GODBC, "tDBInput", "tDBOutput", true),
    MSODBC(DatabaseConnectionItem.class, EDatabaseTypeName.MSODBC, "tDBInput", "tDBOutput", true),
    IBMDB2(DatabaseConnectionItem.class, EDatabaseTypeName.IBMDB2, "tDB2Input", "tDB2Output", true),
    SYBASEASE(DatabaseConnectionItem.class, EDatabaseTypeName.SYBASEASE, "tSybaseInput", "tSybaseOutput", true),
    AS400(DatabaseConnectionItem.class, EDatabaseTypeName.AS400, "tAS400Input", "tAS400Output", true),

    // General JDBC
    GENERAL_JDBC(DatabaseConnectionItem.class, EDatabaseTypeName.GENERAL_JDBC, "tJDBCInput", "tJDBCOutput", true),
    // this Sybase IQ not used.
    // SYBASEIQ(DatabaseConnectionItem.class,"SYBASE", "Sybase IQ", new Boolean(false), "SYBASE"),
    MSSQLODBC(DatabaseConnectionItem.class, EDatabaseTypeName.MSSQL, "tMSSqlInput", "tMSSqlOutput", true),
    // this don't use in Branch 2.0
    HSQL(DatabaseConnectionItem.class, EDatabaseTypeName.HSQLDB, "tHSQLDbInput", "tHSQLDbOutput", true),
    JAVADB(DatabaseConnectionItem.class, EDatabaseTypeName.JAVADB, "tJavaDBInput", "tJavaDBOutput", true),
    INGRES(DatabaseConnectionItem.class, EDatabaseTypeName.INGRES, "tIngresInput", "tIngresOutput", true), // "INGRES"),
    INTERBASE(DatabaseConnectionItem.class, EDatabaseTypeName.INTERBASE, "tInterbaseInput", "tInterbaseOutput", true), // "INTERBASE"
    // )
    // ,
    SQLITE(DatabaseConnectionItem.class, EDatabaseTypeName.SQLITE, "tSQLiteInput", "tSQLiteOutput", true), // "SQLITE"),
    FIREBIRD(DatabaseConnectionItem.class, EDatabaseTypeName.FIREBIRD, "tFirebirdInput", "tFirebirdOutput", true), // "FIREBIRD"
    // ),
    INFORMIX(DatabaseConnectionItem.class, EDatabaseTypeName.INFORMIX, "tInformixInput", "tInformixOutput", true), // "INFORMIX"
    // );
    ACCESS(DatabaseConnectionItem.class, EDatabaseTypeName.ACCESS, "tAccessInput", "tAccessOutput", true), // "ACCESS");

    TERADATA(DatabaseConnectionItem.class, EDatabaseTypeName.TERADATA, "tELTTeradataMap", "tELTTeradataMap"), // "TERADATA"
    // );
    TERADATA_TABLE(DatabaseConnectionItem.class, EDatabaseTypeName.TERADATA, "tELTTeradataInput", "tELTTeradataOutput", true), // "TERADATA"
    // )
    // ;

    MAXDB(DatabaseConnectionItem.class, EDatabaseTypeName.MAXDB, "tMaxDBInput", "tMaxDBOutput", true), // "MAXDB");

    // FILES
    FILEARFF(FakeFileConnectionItem.class, "tFileInputARFF", "tFileOutputARFF", "DELIMITED"),
    FILEDELIMITED(DelimitedFileConnectionItem.class, "tFileInputDelimited", "tFileOutputDelimited", "DELIMITED"),
    FILEEXCEL(ExcelFileConnectionItem.class, "tFileInputExcel", "tFileOutputExcel", "EXCEL"),
    FILELDIF(LdifFileConnectionItemImpl.class, "tFileInputLDIF", "tFileOutputLDIF", "LDIF"),
    FILEPOSITIONAL(PositionalFileConnectionItem.class, "tFileInputPositional", "tFileOutputPositional", "POSITIONAL"),
    FILEREGEX(RegExFileConnectionItem.class, "tFileInputRegex", null, "REGEX"),
    FILEXML(XmlFileConnectionItem.class, "tFileInputXML", "tFileOutputXML", "XML"),
    SAPFFUNCTION(SAPConnectionItem.class, "tSAPInput", "tSAPOutput", "SAP"),

    WSDL(WSDLSchemaConnectionItem.class, "tWebServiceInput", null, "WSDL"),
    SALESFORCE(SalesforceSchemaConnectionItem.class, "tSalesforceInput", null, "SALESFORCE"),

    // RunJob
    RunJob(ProcessItem.class, "tRunJob", "tRunJob");

    // DBORACLESN("ORACLE", "Oracle with service name", new Boolean(true), "ORACLE"),
    // DBGODBC(DatabaseConnectionItem.class,"MSODBC", "Generic ODBC", new Boolean(false), "MSODBC"),
    // DBMSODBC("Microsoft SQL (Odbc driver)", "Microsoft SQL Server (Odbc driver)", new Boolean(false), "MSODBC"),
    // DBIBMDB2("IBM DB2", "IBM DB2", new Boolean(false), "IBMDB2"),
    // DBSYBASEASE("SybaseASE", "Sybase ASE", new Boolean(false), "SYBASE"),
    //       
    // // this Sybase IQ not used.
    // DBSYBASEIQ("Sybase IQ", "Sybase IQ", new Boolean(false), "SYBASE"),
    // DBMSSQL("MSSQL", "Microsoft SQL Server", new Boolean(false), "MSODBC"),
    // // this don't use in Branch 2.0
    // DBHSQL("HSQL","HSQL",new Boolean(false),"HSQLDB"),
    // DBJAVADB("JavaDB", "JavaDB", new Boolean(false), "JAVADB"),
    // DBINGRES("Ingres", "Ingres", new Boolean(false), "INGRES"), // "INGRES"),
    // DBINTERBASE("Interbase", "Interbase", new Boolean(false), "Interbase"), // "INTERBASE"),
    // DBSQLITE("SQLite", "SQLite", new Boolean(false), "SQLITE"), // "SQLITE"),
    // DBFIREBIRD("FireBird", "FireBird", new Boolean(false), "FIREBIRD"), // "FIREBIRD"),
    // DBINFORMIX("Informix", "Informix", new Boolean(true), "INFORMIX"), // "INFORMIX");
    //
    // DBACCESS("Access", "Access", new Boolean(false), "ACCESS"); // "ACCESS");
    // JAVADB_EMBEDED("JavaDB Embeded", "JavaDB Embeded", new Boolean(false), "JAVADB"),
    // JAVADB_JCCJDBC("JavaDB JCCJDBC", "JavaDB JCCJDBC", new Boolean(false), "JAVADB"),
    // JAVADB_DERBYCLIENT("JavaDB DerbyClient", "JavaDB DerbyClient", new Boolean(false), "JAVADB");

    Class clazz;

    String inputComponentName;

    String outPutComponentName;

    EDatabaseTypeName dbTypeName;

    boolean forTableItem;

    private String productName;

    public boolean isForTableItem() {
        return this.forTableItem;
    }

    /**
     * Getter for clazz.
     * 
     * @return the clazz
     */
    public Class getMappingKey() {
        return this.clazz;
    }

    public String getDBType() {
        return dbTypeName.getDisplayName();
    }

    public String getProductName() {
        if (dbTypeName != null) {
            return "DATABASE:" + dbTypeName.getProduct();
        }

        return this.productName;
    }

    public String getDefaultComponentName() {
        return getInputComponentName();
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * Getter for inputComponentName.
     * 
     * @return the inputComponentName
     */
    public String getInputComponentName() {
        return this.inputComponentName;
    }

    /**
     * Getter for outPutComponentName.
     * 
     * @return the outPutComponentName
     */
    public String getOutPutComponentName() {
        return this.outPutComponentName;
    }

    EDatabaseComponentName(Class clazz, String inputComponentName, String outPutComponentName, String productName) {
        this.clazz = clazz;
        this.inputComponentName = inputComponentName;
        this.outPutComponentName = outPutComponentName;
        this.productName = productName;

    }

    // TODO need to be removed after implementing this feature
    // 
    EDatabaseComponentName(Class clazz, String inputComponentName, String outPutComponentName) {
        this.clazz = clazz;
        this.inputComponentName = inputComponentName;
        this.outPutComponentName = outPutComponentName;
    }

    /**
     * Contructor for files.
     * 
     * @param clazz
     * @param dbTypeName
     * @param inputComponentName
     * @param outPutComponentName
     */
    EDatabaseComponentName(Class clazz, EDatabaseTypeName dbTypeName, String inputComponentName, String outPutComponentName) {
        this(clazz, dbTypeName, inputComponentName, outPutComponentName, false);
    }

    /**
     * Contructor for databases.
     * 
     * @param clazz
     * @param type
     * @param inputComponentName
     * @param outPutComponentName
     */
    EDatabaseComponentName(Class clazz, EDatabaseTypeName dbTypeName, String inputComponentName, String outPutComponentName,
            boolean tableItem) {
        this(clazz, inputComponentName, outPutComponentName);
        this.dbTypeName = dbTypeName;
        this.forTableItem = tableItem;
    }

    public static EDatabaseComponentName getCorrespondingComponentName(Item item, ERepositoryObjectType type) {
        if (type == ERepositoryObjectType.METADATA_CON_TABLE) {
            for (EDatabaseComponentName typeName : EDatabaseComponentName.values()) {
                if (typeName.getMappingKey().isAssignableFrom(item.getClass())) {

                    if (typeName.getMappingKey() == DatabaseConnectionItem.class) {
                        DatabaseConnectionItem dbItem = (DatabaseConnectionItem) item;
                        DatabaseConnection dbConnection = (DatabaseConnection) dbItem.getConnection();
                        if (typeName.getDBType().equals(dbConnection.getDatabaseType()) && typeName.isForTableItem()) {
                            return typeName;
                        }
                    } else {
                        return typeName;
                    }
                }
            }
            return null;
        } else {
            return getCorrespondingComponentName(item, true);
        }
    }

    private static EDatabaseComponentName getCorrespondingComponentName(Item item, boolean flag) {

        for (EDatabaseComponentName typeName : EDatabaseComponentName.values()) {
            if (typeName.getMappingKey().isAssignableFrom(item.getClass())) {

                if (typeName.getMappingKey() == DatabaseConnectionItem.class) {
                    DatabaseConnectionItem dbItem = (DatabaseConnectionItem) item;
                    DatabaseConnection dbConnection = (DatabaseConnection) dbItem.getConnection();
                    if (typeName.getDBType().equals(dbConnection.getDatabaseType()) && flag) {
                        return typeName;
                    }
                } else {
                    return typeName;
                }
            }
        }
        return null;
    }

    public static String getProductName(Item item) {
        for (EDatabaseComponentName typeName : EDatabaseComponentName.values()) {
            if (typeName.getMappingKey().isAssignableFrom(item.getClass())) {
                return typeName.getProductName();
                // if (typeName.getMappingKey() == DatabaseConnectionItem.class) {
                // DatabaseConnectionItem dbItem = (DatabaseConnectionItem) item;
                // DatabaseConnection dbConnection = (DatabaseConnection) dbItem.getConnection();
                // if (typeName.getDBType().equals(dbConnection.getDatabaseType()) && flag) {
                // return typeName;
                // }
                // } else {
                // return typeName;
                // }
            }
        }
        return null;
    }

    /*******************************************************************************************************************
     * 
     * DOC xye EDatabaseComponentName class global comment. Detailled comment
     */
    class FakeFileConnectionItem extends ConnectionItemImpl {

    }
}
