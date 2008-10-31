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
package org.talend.repository.ui.utils;

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;

/**
 * @author ocarbone
 * 
 */
public class DataStringConnection {

    private final DataConnection[] dataConnection;

    private final String[] defaultTable;

    public static String mySQlDefaultValue = "noDatetimeStringSync=true";

    public static String as400DefaultValue = "prompt=false";

    public final static int DBTYPE_GENERIC_ODBC = 4;

    public final static int DBTYPE_MICORSOFT_SQL_SERVER_ODBC_DRIVER = 5;

    public final static int DBTYPE_SQLITE = 8;

    public final static int DBTYPE_INTERBASE = 10;

    public final static int DBTYPE_FIREBIRD = 12;

    public final static int DBTYPE_INFORMIX = 13;

    public final static int DBTYPE_ACCESS = 14;

    public final static int DBTYPE_TERDATA = 15;

    public final static int DBTYPE_AS400 = 16;

    public final static int DBTYPE_JAVADB_EMBEDED = 17;

    public final static int DBTYPE_HSQLDB_IN_PROCESS = 22;

    public final static String GENERAL_JDBC = "General JDBC";

    // private Combo combo;

    private int selectionIndex;

    private String urlConnectionStr;

    public DataStringConnection() {
        String host = "(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.|[\\w\\.\\-_]{0,})"; //$NON-NLS-1$
        String port = "(\\d{0,5})"; //$NON-NLS-1$
        String word = "([\\w\\.\\-_]{0,})"; //$NON-NLS-1$
        String sid = "([\\w\\.\\-_]{0,})"; //$NON-NLS-1$
        String fileMdb = "([\\w\\.\\-_]{0,}).mdb"; //$NON-NLS-1$
        String file = "([\\w\\.\\-_]{0,})"; //$NON-NLS-1$
        String addParam = "([\\w\\.\\-_=]{0,})"; //$NON-NLS-1$

        dataConnection = new DataConnection[31];

        defaultTable = new String[31];

        dataConnection[0] = new DataConnection(
                "MySQL", "jdbc:mysql://<host>:<port>/<sid>?<property>", "jdbc:mysql://" + host + ":" + port //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                        + "/" + sid + "\\?" + addParam, "3306"); //$NON-NLS-1$ //$NON-NLS-2$

        dataConnection[1] = new DataConnection("PostgreSQL", "jdbc:postgresql://<host>:<port>/<sid>", "jdbc:postgresql://" + host //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                + ":" + port + "/" + sid, "5432"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        dataConnection[2] = new DataConnection("Oracle with SID", "jdbc:oracle:thin:@<host>:<port>:<sid>", "jdbc:oracle:thin:@" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                + host + ":" + port + ":" + sid, "1521"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        dataConnection[3] = new DataConnection(
                "Oracle with service name", //$NON-NLS-1$
                "jdbc:oracle:thin:@(description=(address=(protocol=tcp)(host=<host>)(port=<port>))(connect_data=(service_name=<service_name>)))", //$NON-NLS-1$
                "jdbc:oracle:thin:@\\(description=\\(address=\\(protocol=tcp\\)\\(host=" + host + "\\)\\(port=" + port //$NON-NLS-1$ //$NON-NLS-2$
                        + "\\)\\)\\(connect_data=\\(service_name=" + sid + "\\)\\)\\)", "1521"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        dataConnection[DBTYPE_GENERIC_ODBC] = new DataConnection("Generic ODBC", "jdbc:odbc:<datasource>", "jdbc:odbc:" + word); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        dataConnection[DBTYPE_MICORSOFT_SQL_SERVER_ODBC_DRIVER] = new DataConnection(
                "Microsoft SQL Server (Odbc driver)", "jdbc:odbc:<datasource>", "jdbc:odbc:" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                        + word);

        dataConnection[6] = new DataConnection(
                "Sybase ASE", "jdbc:sybase:Tds:<host>:<port>/<sid>", "jdbc:sybase:Tds:" + host + ":" + port //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                        + "/" + sid, "5001"); //$NON-NLS-1$ //$NON-NLS-2$

        dataConnection[7] = new DataConnection("IBM DB2", "jdbc:db2://<host>:<port>/<sid>", "jdbc:db2://" + host + //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                ":" + port + "/" + sid, "50000"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        dataConnection[DBTYPE_SQLITE] = new DataConnection("SQLite", "jdbc:sqlite:/<filename>", "jdbc:sqlite:/" + file); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        dataConnection[9] = new DataConnection("Ingres", "jdbc:ingres://<host>:<port>/<sid>",
                "jdbc:ingres://" + host + ":" + port + "/" + sid, "II7"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        dataConnection[DBTYPE_INTERBASE] = new DataConnection("Interbase", "jdbc:interbase://<host>/<sid>",
                "jdbc:interbase://" + host + "/" + sid); //$NON-NLS-1$ //$NON-NLS-2$ 

        // dataConnection[11] = new DataConnection("Microsoft SQL Server", "jdbc:jtds:sqlserver://<host>:<port>/<sid>",
        // "jdbc:jtds:sqlserver://" + host + ":" + port + "/" + sid, "1433"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        dataConnection[11] = new DataConnection("Microsoft SQL Server", "jdbc:jtds:sqlserver://<host>:<port>/<sid>;<property>",
                "jdbc:jtds:sqlserver://" + host + ":" + port + "/" + sid + ";" + addParam, "1433"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        dataConnection[DBTYPE_FIREBIRD] = new DataConnection("FireBird", "jdbc:firebirdsql:<host>:<filename>",
                "jdbc:firebirdsql:" + host + ":" + file); //$NON-NLS-1$ //$NON-NLS-2$ 

        dataConnection[DBTYPE_INFORMIX] = new DataConnection("Informix",
                "jdbc:informix-sqli://<host>:<port>/<sid>:informixserver=<datasource>;<property>",
                "jdbc:informix-sqli://" + host + ":" + port + "/" + sid + ":informixserver=" + word + ";" + addParam); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        dataConnection[DBTYPE_ACCESS] = new DataConnection("Access",
                "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb, *.accdb)};DBQ=<filename>",
                "jdbc:odbc:Driver={Microsoft Access Driver \\(\\*\\.mdb, \\*\\.accdb\\)};DBQ=" + file); //$NON-NLS-1$ 

        dataConnection[DBTYPE_TERDATA] = new DataConnection(
                "Teradata", "jdbc:teradata://<host>/<sid>", "jdbc:teradata://" + host + "/" + sid); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        // dataConnection[16] = new DataConnection("AS400", "jdbc:as400://<host>/<sid>;prompt=false;libraries=<sid>",
        // "jdbc:as400://" + host + "/" + sid + ";prompt=false;libraries=" + sid);
        dataConnection[DBTYPE_AS400] = new DataConnection("AS400", "jdbc:as400://<host>/<sid>;libraries=<sid>;<property>",
                "jdbc:as400://" + host + "/" + sid + ";libraries=" + sid + ";" + addParam);

        dataConnection[DBTYPE_JAVADB_EMBEDED] = new DataConnection("JavaDB Embeded", "jdbc:derby:<dbRootPath>", "jdbc:derby:"
                + word);
        //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        dataConnection[18] = new DataConnection("JavaDB JCCJDBC", "jdbc:derby:net://<host>:<port>/<sid>",
                "jdbc:derby:net://" + host + ":" + port + "/" + sid, "1527"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        dataConnection[19] = new DataConnection("JavaDB DerbyClient", "jdbc:derby://<host>:<port>/<sid>",
                "jdbc:derby://" + host + ":" + port + "/" + sid, "1527"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        dataConnection[20] = new DataConnection("HSQLDB Server", "jdbc:hsqldb:hsql://<host>:<port>/<sid>",
                "jdbc:hsqldb:hsql://" + host + ":" + port + "/" + sid, "9001"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        dataConnection[21] = new DataConnection("HSQLDB WebServer", "jdbc:hsqldb:http://<host>:<port>/<sid>",
                "jdbc:hsqldb:http://" + host + ":" + port + "/" + sid, "9001"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        dataConnection[DBTYPE_HSQLDB_IN_PROCESS] = new DataConnection("HSQLDB In-Process",
                "jdbc:hsqldb:file:<dbRootPath>/<sid>;ifexists=true", "jdbc:hsqldb:file:" + file + "/" + sid + ";ifexists=true"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        dataConnection[23] = new DataConnection("MaxDB", "jdbc:sapdb://<host>:<port>/<sid>",
                "jdbc:sapdb://" + host + ":" + port + "/" + sid, "7210"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        dataConnection[24] = new DataConnection(
                "PostgresPlus", "jdbc:postgresql://<host>:<port>/<sid>", "jdbc:postgresql://" + host //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                        + ":" + port + "/" + sid, "5432"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        dataConnection[25] = new DataConnection("IBM DB2 ZOS", "jdbc:db2://<host>:<port>/<sid>", "jdbc:db2://" + host + //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                ":" + port + "/" + sid, "557"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        dataConnection[26] = new DataConnection("SAS", "jdbc:sasiom://<host>:<port>", "jdbc:sasiom://" + host //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                + ":" + port, "7070"); //$NON-NLS-1$ //$NON-NLS-2$ 

        dataConnection[27] = new DataConnection("Greenplum", "jdbc:postgresql://<host>:<port>/<sid>", "jdbc:postgresql://" + host //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                + ":" + port + "/" + sid, "5432"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        dataConnection[28] = new DataConnection("ParAccel", "jdbc:paraccel://<host>:<port>/<sid>", "jdbc:paraccel://" + host //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                + ":" + port + "/" + sid, "5439"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        dataConnection[29] = new DataConnection("Netezza", "jdbc:netezza://<host>:<port>/<sid>", "jdbc:netezza://" + host //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                + ":" + port + "/" + sid, "5480");

        // General jdbc
        dataConnection[30] = new DataConnection(GENERAL_JDBC, "jdbc:xxx://<xxx>:<xxx>", "jdbc:xxx://" + host //$NON-NLS-1$ //$NON-NLS-2$ 
                + ":" + port, "xxxx"); //$NON-NLS-1$ //$NON-NLS-2$ 

        // dataConnection[8] = new DataConnection("Sybase IQ", "jdbc:sybase:Tds:<host>:<port>/<sid>", "jdbc:sybase:Tds:"
        // + host + ":" + port //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        // + "/" + sid, "2638"); //$NON-NLS-1$ //$NON-NLS-2$

        // TODO CAN : reactivate this Connections when PerlModule can connect with this Databases.
        // dataConnection[3] = new DataConnection("Oracle Thin", "jdbc:oracle:thin:@<host>:<port>:<sid>",
        // "jdbc:oracle:thin:@" + host + ":"
        // + port + ":" + sid, "1521");

        // dataConnection[4] = new DataConnection("Oracle Oci", "jdbc:oracle:oci:@<host>:<port>:<sid>",
        // "jdbc:oracle:oci:@" + host + ":"
        // + port + ":" + sid, "1521");

        // dataConnection[6] = new DataConnection("Sybase", "jdbc:sybase:Tds:<host>:<port>/<sid>", "jdbc:sybase:Tds:" +
        // host + ":" + port
        // + "/" + sid, "2048");

        // dataConnection[8] = new DataConnection("Microsoft Access",
        // "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=<filename>;UID=%<login>%;PWD=%<password>%",
        // "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=" + fileMdb + ";UID=%" + word + ";PWD=%" + word);
    }

    public String[] getItem() {
        String[] s = defaultTable;
        for (int i = 0; i < dataConnection.length; i++) {
            s[i] = new String(dataConnection[i].getLabel());
        }
        return s;
    }

    /**
     * SetVisible fields : Serveur, Login, Password.
     * 
     * @param int dbTypeItemIndex
     * @param String host
     * @param String login
     * @param String password
     * @param String port
     * @param String sid
     * @param String filename
     * @param String datasource
     * 
     * @return string
     */
    public String getString(final int dbTypeItemIndex, final String host, final String login, final String password,
            final String port, final String sid, final String filename, final String datasource) {
        String s = getStringConnectionTemplate();
        if (s == null) {
            return ""; //$NON-NLS-1$
        }
        s = getStringReplace(s, "<login>", login); //$NON-NLS-1$
        s = getStringReplace(s, "<password>", password); //$NON-NLS-1$
        s = getStringReplace(s, "<host>", host); //$NON-NLS-1$
        s = getStringReplace(s, "<login>", login); //$NON-NLS-1$
        s = getStringReplace(s, "<password>", password); //$NON-NLS-1$
        s = getStringReplace(s, "<port>", port); //$NON-NLS-1$
        s = getStringReplace(s, "<sid>", sid); //$NON-NLS-1$
        s = getStringReplace(s, "<service_name>", sid); //$NON-NLS-1$
        s = getStringReplace(s, "<datasource>", datasource); //$NON-NLS-1$
        // PTODO OCA : if needed, adapt the file separator to all OS (not only backslashes)
        s = getStringReplace(s, "<filename>", filename); //$NON-NLS-1$
        this.urlConnectionStr = s;
        return s;
    }

    public String getStringConnectionTemplate() {
        if (selectionIndex < 0) {
            return null;
        } else {
            return dataConnection[selectionIndex].getString();
        }
    }

    public String getRegex() {
        if (selectionIndex < 0) {
            return null;
        } else {
            return dataConnection[selectionIndex].getRegex();
        }
    }

    public String getDefaultPort() {
        if (selectionIndex < 0) {
            return null;
        } else {
            return dataConnection[selectionIndex].getDefaultPort();
        }
    }

    public String getStringReplace(final String init, final String before, final String after) {
        String s = init;

        if (after != null) {
            // s = init.replaceFirst(before, after);
            s = init.replace(before, after);
        }
        return s;
    }

    /**
     * Method getAnalyse extact serveur, port, sid of stringConnection and check the dbType.
     * 
     * @param stringConnection
     * @return string[] { selectionIndex, serveur, port, sid }
     */
    public String[] getAnalyse(final String stringConnection) {
        Integer selectionIndex = getSelectionIndex();
        String[] s = { selectionIndex.toString(), "", "", "", "", "" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        String regex = getRegex();
        if (stringConnection == "") { //$NON-NLS-1$
            return s;
        }

        Perl5Compiler compiler = new Perl5Compiler();
        Perl5Matcher matcher = new Perl5Matcher();
        Pattern pattern = null;
        try {
            pattern = compiler.compile(regex);
            if (matcher.contains(stringConnection, pattern)) {
                matcher.matches(stringConnection, pattern);
                MatchResult matchResult = matcher.getMatch();
                s[0] = selectionIndex.toString();
                if (matchResult != null) {
                    for (int i = 1; i < matchResult.groups(); i++) {
                        s[i] = matchResult.group(i);
                    }
                }
            } else {
                // search if another regex corresponding at the string of connection
                int i = searchGoodRegex(stringConnection);
                if (i != getSelectionIndex()) {
                    setSelectionIndex(i);
                    s = getAnalyse(stringConnection);
                }
            }

        } catch (MalformedPatternException e) {
            e.printStackTrace();
        }
        return s;
    }

    /**
     * method searchGoodRegex search the regex corresponding at the string of connection.
     * 
     * @param stringConnection
     * @return selectionIndex
     */
    private int searchGoodRegex(String stringConnection) {
        String startStringConnection;
        String startTemplateString;
        for (int i = 0; i < dataConnection.length; i++) {
            startTemplateString = dataConnection[i].getString().substring(0, dataConnection[i].getString().indexOf("<")); //$NON-NLS-1$
            if (startTemplateString.length() <= stringConnection.length()) {
                startStringConnection = stringConnection.substring(0, startTemplateString.length());
                if (stringConnection.contains("(description=(address=(protocol=tcp)")) { //$NON-NLS-1$
                    return 3;
                } else if (startTemplateString.equals(startStringConnection)) {
                    return i;
                }
            }
        }
        return getSelectionIndex();
    }

    /**
     * method getIndexOfLabel return the index of a Connection String label.
     * 
     * @param string label
     */
    public int getIndexOfLabel(String label) {
        for (int i = 0; i < dataConnection.length; i++) {
            if (dataConnection[i].getLabel().equals(label)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * set selectionIndex (the index of the StringConnection in the Array DataConnection).
     * 
     * @param selectionIndex
     */
    public void setSelectionIndex(int selectionIndex) {
        this.selectionIndex = selectionIndex;
    }

    /**
     * set gelectionIndex (the index of the StringConnection in the Array DataConnection).
     * 
     * @return selectionIndex
     */
    public int getSelectionIndex() {
        return selectionIndex;
    }

    /**
     * Return true when the String Connection contains "jdbc:oracle:".
     * 
     * @return
     */
    public boolean isSchemaNeeded() {
        if (selectionIndex < 0) {
            return false;
        }
        return getStringConnectionTemplate().substring(0, 12).equals("jdbc:oracle:") //$NON-NLS-1$
                || getStringConnectionTemplate().substring(0, 15).equals("jdbc:postgresql")
                || getStringConnectionTemplate().substring(0, 8).equals("jdbc:db2"); //$NON-NLS-1$
    }

    /**
     * Return true when the String Connection contains "jdbc:jtds:sqlserver".
     * 
     * @return
     */
    public boolean isAddtionParamsNeeded() {
        if (selectionIndex < 0) {
            return false;
        }
        return getStringConnectionTemplate().substring(0, 19).equals("jdbc:jtds:sqlserver")
                || getStringConnectionTemplate().substring(0, 18).equals("jdbc:informix-sqli")
                || getStringConnectionTemplate().substring(0, 10).equals("jdbc:mysql")
                || getStringConnectionTemplate().substring(0, 10).equals("jdbc:as400"); //$NON-NLS-1$

    }

    private static String[] dataBaseNeededList = { "jdbc:mysql", "jdbc:sybase", "jdbc:db2", "jdbc:ingres", "jdbc:interbase",
            "jdbc:jtds:sqlserver", "jdbc:informix-sqli", "jdbc:teradata", "jdbc:as400", "jdbc:derby", "jdbc:derby:net",
            "jdbc:hsqldb:hsql", "jdbc:hsqldb:http", "jdbc:hsqldb:file", "jdbc:sapdb", "jdbc:postgresql", "jdbc:db2",
            "jdbc:netezza" };

    public boolean isDatabaseNeeded() {
        if (selectionIndex < 0) {
            return false;
        }
        String connectionTemplate = getStringConnectionTemplate();
        int subIndex = 0;
        for (String connection : dataBaseNeededList) {
            subIndex = connection.length();
            boolean needed = connectionTemplate.substring(0, subIndex).equals(connection);
            if (needed) {
                return needed;
            }
        }
        return false;

    }

    /**
     * DOC qiang.zhang Comment method "getString".
     * 
     * @param selectionIndex2
     * @param text
     * @param text2
     * @param text3
     * @param text4
     * @param text5
     * @param lowerCase
     * @param text6
     * @param text7
     * @return
     */
    public String getString(final int dbTypeItemIndex, final String host, final String login, final String password,
            final String port, final String sid, final String filename, final String datasource, String dbrootPath) {
        String string = getString(dbTypeItemIndex, host, login, password, port, sid, filename, datasource);
        if (string.equals("")) {
            return ""; //$NON-NLS-1$
        }
        if (dbTypeItemIndex == 22) {
            string = getStringReplace(string, "<dbRootPath>", dbrootPath);
        } else {
            string = getStringReplace(string, "<dbRootPath>", sid);
        }
        this.urlConnectionStr = string;
        return string;
    }

    public String getString(final int dbTypeItemIndex, final String host, final String login, final String password,
            final String port, final String sid, final String filename, final String datasource, String dbrootPath,
            String addParams) {
        String string = getString(dbTypeItemIndex, host, login, password, port, sid, filename, datasource);
        if (string.equals("")) {
            return ""; //$NON-NLS-1$
        }
        if (dbTypeItemIndex == 22) {
            string = getStringReplace(string, "<dbRootPath>", dbrootPath);
        } else {
            string = getStringReplace(string, "<dbRootPath>", sid);
        }
        if (dbTypeItemIndex == 11 || dbTypeItemIndex == 13 || dbTypeItemIndex == 0 || dbTypeItemIndex == 16) {
            string = getStringReplace(string, "<property>", addParams);
        }
        this.urlConnectionStr = string;
        return string;
    }

    public String getUrlConnectionStr() {
        return this.urlConnectionStr;
    }

}
