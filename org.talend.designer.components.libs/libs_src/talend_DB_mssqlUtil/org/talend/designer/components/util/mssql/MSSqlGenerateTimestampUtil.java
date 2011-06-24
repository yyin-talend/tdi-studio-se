// ============================================================================
//
// Copyright (C) 2006-2009 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.components.util.mssql;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * DOC Administrator class global comment. Detailled comment
 */
public class MSSqlGenerateTimestampUtil {

    private Map<Integer, Integer> columnDBTypeMapping = new HashMap<Integer, Integer>();

    public static final int STRING_TO_JAVA_SQL_DATE = 0x001;

    public static final int STRING_TO_JAVA_SQL_TIME = 0x002;

    public static final int STRING_TO_JAVA_SQL_TIMESTAMP = 0x003;

    public static final int JAVA_SQL_DATE = 0x101;

    /**
     * DOC jliu Comment method "getDate". <br>
     * DB Type ---------> jtds driver return type<br>
     * DATE -------------> nvarchar<br>
     * DATETIME ---------> datetime<br>
     * DATETIME2 --------> nvarchar<br>
     * DTIME ------------> nvarchar<br>
     * SMALLDATETIME ----> smalldatetime<br>
     * DATETIMEOFFSET ---> nvarchar<br>
     * TIMESTAMP --------> timestamp<br>
     * 
     * @param rsmd
     * @param rs
     * @param index
     * @return the java.util.Date convert from db
     * @throws SQLException
     */
    public java.util.Date getDate(final java.sql.ResultSetMetaData rsmd, final java.sql.ResultSet rs, int index)
            throws SQLException {
        if (rs.getObject(index) == null) {
            return null;
        }
        if (columnDBTypeMapping.get(index) != null) {
            switch (columnDBTypeMapping.get(index)) {
            case STRING_TO_JAVA_SQL_DATE:
                return new java.util.Date(java.sql.Date.valueOf(rs.getString(index)).getTime());
            case STRING_TO_JAVA_SQL_TIME:
                String tempDateString = rs.getString(index);
                tempDateString = tempDateString.indexOf(".") == -1 ? tempDateString : tempDateString.substring(0,
                        tempDateString.indexOf("."));
                return new java.util.Date(java.sql.Time.valueOf(tempDateString).getTime());
            case STRING_TO_JAVA_SQL_TIMESTAMP:
                return new java.util.Date(java.sql.Timestamp.valueOf(rs.getString(index)).getTime());
            case JAVA_SQL_DATE:
                return new java.util.Date(rs.getTimestamp(index).getTime());
            default:
                return null;
            }
        } else {
            // this code just run one times.
            if ("nvarchar".equals(rsmd.getColumnTypeName(index))) {
                String tempDateString = rs.getString(index);
                try {
                    columnDBTypeMapping.put(index, STRING_TO_JAVA_SQL_DATE);
                    return new java.util.Date(java.sql.Date.valueOf(tempDateString).getTime());
                } catch (java.lang.IllegalArgumentException iae1) {
                    try {
                        tempDateString = tempDateString.indexOf(".") == -1 ? tempDateString : tempDateString.substring(0,
                                tempDateString.indexOf("."));
                        columnDBTypeMapping.put(index, STRING_TO_JAVA_SQL_TIME);
                        return new java.util.Date(java.sql.Time.valueOf(tempDateString).getTime());
                    } catch (java.lang.IllegalArgumentException iae2) {
                        columnDBTypeMapping.put(index, STRING_TO_JAVA_SQL_TIMESTAMP);
                        return new java.util.Date(java.sql.Timestamp.valueOf(rs.getString(index)).getTime());
                    }
                }

            } else if ("datetime".equals(rsmd.getColumnTypeName(index)) || "smalldatetime".equals(rsmd.getColumnTypeName(index))) {
            	columnDBTypeMapping.put(index, JAVA_SQL_DATE);
                return new java.util.Date(rs.getTimestamp(index).getTime());
            }
        }
        return null;
    }
}
