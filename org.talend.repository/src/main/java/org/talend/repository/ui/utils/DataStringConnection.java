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

    // private Combo combo;

    private int selectionIndex;

    public DataStringConnection() {
        String host = "(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.|[\\w\\.\\-_]{0,})"; //$NON-NLS-1$
        String port = "(\\d{0,5})"; //$NON-NLS-1$
        String word = "([\\w\\.\\-_]{0,})"; //$NON-NLS-1$
        String sid = "([\\w\\.\\-_]{0,})"; //$NON-NLS-1$
        String fileMdb = "([\\w\\.\\-_]{0,}).mdb"; //$NON-NLS-1$
        String file = "([\\w\\.\\-_]{0,})"; //$NON-NLS-1$

        dataConnection = new DataConnection[23];

        defaultTable = new String[23];

        dataConnection[0] = new DataConnection(
                "MySQL", "jdbc:mysql://<host>:<port>/<sid>", "jdbc:mysql://" + host + ":" + port //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                        + "/" + sid, "3306"); //$NON-NLS-1$ //$NON-NLS-2$

        dataConnection[1] = new DataConnection(
                "PostgreSQL", "jdbc:postgresql://<host>:<port>/<sid>", "jdbc:postgresql://" + host //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                        + ":" + port + "/" + sid, "5432"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        dataConnection[2] = new DataConnection(
                "Oracle with SID", "jdbc:oracle:thin:@<host>:<port>:<sid>", "jdbc:oracle:thin:@" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                        + host + ":" + port + ":" + sid, "1521"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        dataConnection[3] = new DataConnection(
                "Oracle with service name", //$NON-NLS-1$
                "jdbc:oracle:thin:@(description=(address=(protocol=tcp)(host=<host>)(port=<port>))(connect_data=(service_name=<service_name>)))", //$NON-NLS-1$
                "jdbc:oracle:thin:@\\(description=\\(address=\\(protocol=tcp\\)\\(host=" + host + "\\)\\(port=" + port //$NON-NLS-1$ //$NON-NLS-2$
                        + "\\)\\)\\(connect_data=\\(service_name=" + sid + "\\)\\)\\)", "1521"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        dataConnection[4] = new DataConnection("Generic ODBC", "jdbc:odbc:<datasource>", "jdbc:odbc:" + word); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        dataConnection[5] = new DataConnection(
                "Microsoft SQL Server (Odbc driver)", "jdbc:odbc:<datasource>", "jdbc:odbc:" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                        + word);

        dataConnection[6] = new DataConnection(
                "Sybase ASE", "jdbc:sybase:Tds:<host>:<port>/<sid>", "jdbc:sybase:Tds:" + host + ":" + port //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                        + "/" + sid, "5001"); //$NON-NLS-1$ //$NON-NLS-2$

        dataConnection[7] = new DataConnection("IBM DB2", "jdbc:db2://<host>:<port>/<sid>", "jdbc:db2://" + host + //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                ":" + port + "/" + sid, "50000"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        dataConnection[8] = new DataConnection("SQLite", "jdbc:sqlite:/<filename>", "jdbc:sqlite:/" + file); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        dataConnection[9] = new DataConnection("Ingres", "jdbc:ingres://<host>:<port>/<sid>",
                "jdbc:ingres://" + host + ":" + port + "/" + sid, "II7"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        dataConnection[10] = new DataConnection("Interbase", "jdbc:interbase://<host>/<sid>",
                "jdbc:interbase://" + host + "/" + sid); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        dataConnection[11] = new DataConnection("Microsoft SQL Server", "jdbc:jtds:sqlserver://<host>:<port>/<sid>",
                "jdbc:jtds:sqlserver://" + host + ":" + port + "/" + sid, "1433"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        dataConnection[12] = new DataConnection("FireBird", "jdbc:firebirdsql:<host>:<sid>",
                "jdbc:firebirdsql:" + host + ":" + sid); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        dataConnection[13] = new DataConnection("Informix",
                "jdbc:informix-sqli://<host>:<port>/<sid>:informixserver=<datasource>",
                "jdbc:informix-sqli://" + host + ":" + port + "/" + sid + ":informixserver=" + word); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        dataConnection[14] = new DataConnection("Access",
                "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=<filename>",
                "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=" + file); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        dataConnection[15] = new DataConnection("Teradata", "jdbc:teradata://<host>/<sid>",
                "jdbc:teradata://" + host + "/" + sid); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        dataConnection[16] = new DataConnection("AS400", "jdbc:as400://<host>/<sid> ;prompt=false", "jdbc:as400://"
                + host + "/" + sid + ";prompt=false");

        dataConnection[17] = new DataConnection("JavaDB Embeded", "jdbc:derby:<dbRootPath>", "jdbc:derby:" + word);
        //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        dataConnection[18] = new DataConnection("JavaDB JCCJDBC", "jdbc:derby:net://<host>:<port>/<sid>",
                "jdbc:derby:net://" + host + ":" + port + "/" + sid, "1527"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        dataConnection[19] = new DataConnection("JavaDB DerbyClient", "jdbc:derby://<host>:<port>/<sid>",
                "jdbc:derby://" + host + ":" + port + "/" + sid, "1527"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        dataConnection[20] = new DataConnection("HSQLDB Server", "jdbc:hsqldb:hsql://<host>:<port>/<sid>",
                "jdbc:hsqldb:hsql://" + host + ":" + port + "/" + sid, "9001"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        dataConnection[21] = new DataConnection("HSQLDB WebServer", "jdbc:hsqldb:http://<host>:<port>/<sid>",
                "jdbc:derby://" + host + ":" + port + "/" + sid, "9001"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        dataConnection[22] = new DataConnection("HSQLDB In-Process",
                "jdbc:hsqldb:file:<dbRootPath>/<sid>;ifexists=true",
                "jdbc:hsqldb:file:" + file + "/" + sid + ";ifexists=true"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

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
        String[] s = { selectionIndex.toString(), "", "", "" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
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
            startTemplateString = dataConnection[i].getString()
                    .substring(0, dataConnection[i].getString().indexOf("<")); //$NON-NLS-1$
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
                || getStringConnectionTemplate().substring(0, 15).equals("jdbc:postgresql"); //$NON-NLS-1$
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
        return string;
    }
}
