// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
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

    private DataConnection[] dataConnection;

    private String[] defaultTable;

    // private Combo combo;

    private int selectionIndex;

    public DataStringConnection() {
        String host = "(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.|[\\w\\.\\-_]{0,})";
        String port = "(\\d{0,5})";
        String word = "([\\w\\.\\-_]{0,})";
        String sid = "([\\w\\.\\-_]{0,})";
        String fileMdb = "([\\w\\.\\-_]{0,}).mdb";

        dataConnection = new DataConnection[6];

        defaultTable = new String[6];

        dataConnection[0] = new DataConnection("MySQL", "jdbc:mysql://<host>:<port>/<sid>", "jdbc:mysql://" + host + ":" + port
                + "/" + sid, "3306");

        dataConnection[1] = new DataConnection("PostgreSQL", "jdbc:postgresql://<host>:<port>/<sid>", "jdbc:postgresql://" + host
                + ":" + port + "/" + sid, "5432");

        dataConnection[2] = new DataConnection("Oracle with SID", "jdbc:oracle:thin:@<host>:<port>:<sid>", "jdbc:oracle:thin:@"
                + host + ":" + port + ":" + sid, "1521");

        dataConnection[3] = new DataConnection(
                "Oracle with service name",
                "jdbc:oracle:thin:@(description=(address=(protocol=tcp)(host=<host>)(port=<port>))(connect_data=(service_name=<service_name>)))",
                "jdbc:oracle:thin:@\\(description=\\(address=\\(protocol=tcp\\)\\(host=" + host + "\\)\\(port=" + port
                        + "\\)\\)\\(connect_data=\\(service_name=" + sid + "\\)\\)\\)", "1521");

        dataConnection[4] = new DataConnection("Generic ODBC", "jdbc:odbc:<datasource>", "jdbc:odbc:" + word);

        dataConnection[5] = new DataConnection("Microsoft SQL Server (Odbc driver)", "jdbc:odbc:<datasource>", "jdbc:odbc:"
                + word);

        // TODO CAN : reactivate this Connections when PerlModule can connect with this Databases.
        // dataConnection[2] = new DataConnection("Odbc/Jdbc", "jdbc:odbc:<datasource>", "jdbc:odbc:" + word);
        //        
        // dataConnection[3] = new DataConnection("Oracle Thin", "jdbc:oracle:thin:@<host>:<port>:<sid>",
        // "jdbc:oracle:thin:@" + host + ":"
        // + port + ":" + sid, "1521");
        //        
        // dataConnection[4] = new DataConnection("Oracle Oci", "jdbc:oracle:oci:@<host>:<port>:<sid>",
        // "jdbc:oracle:oci:@" + host + ":"
        // + port + ":" + sid, "1521");
        //
        // dataConnection[5] = new DataConnection("IBM DB2", "jdbc:db2://<host>:<port>/<sid>", "jdbc:db2://" + host +
        // ":" + port + "/" + sid, "50000");
        //
        // dataConnection[6] = new DataConnection("Sybase", "jdbc:sybase:Tds:<host>:<port>/<sid>", "jdbc:sybase:Tds:" +
        // host + ":" + port
        // + "/" + sid, "2048");
        //
        // dataConnection[7] = new DataConnection("Microsoft SQL Server", "jdbc:jtds:sqlserver://<host>:<port>/<sid>",
        // "jdbc:jtds:sqlserver://"
        // + host + ":" + port + "/" + sid, "1433");
        //
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
        if (s == null)
            return "";
        s = getStringReplace(s, "<login>", login);
        s = getStringReplace(s, "<password>", password);
        s = getStringReplace(s, "<host>", host);
        s = getStringReplace(s, "<login>", login);
        s = getStringReplace(s, "<password>", password);
        s = getStringReplace(s, "<port>", port);
        s = getStringReplace(s, "<sid>", sid);
        s = getStringReplace(s, "<service_name>", sid);
        s = getStringReplace(s, "<datasource>", datasource);
        // PTODO OCA : if needed, adapt the file separator to all OS (not only backslashes)
        s = getStringReplace(s, "<filename>", filename);

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
            s = init.replaceFirst(before, after);
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
        String[] s = { selectionIndex.toString(), "", "", "" };
        String regex = getRegex();
        if (stringConnection == "") {
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
            startTemplateString = dataConnection[i].getString().substring(0, dataConnection[i].getString().indexOf("<"));
            if (startTemplateString.length() <= stringConnection.length()) {
                startStringConnection = stringConnection.substring(0, startTemplateString.length());
                if (stringConnection.contains("(description=(address=(protocol=tcp)")) {
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
        return getStringConnectionTemplate().substring(0, 12).equals("jdbc:oracle:")
                || getStringConnectionTemplate().substring(0, 15).equals("jdbc:postgresql");
    }
}
