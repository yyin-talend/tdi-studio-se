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
package org.talend.repository.ui.actions.metadata.database;

import java.util.regex.Pattern;

import org.talend.commons.utils.VersionUtils;
import org.talend.core.model.metadata.editor.MetadataEmfTableEditor;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.RepositoryConstants;
import org.talend.repository.ui.actions.metadata.database.DBTableForDelimitedBean.BeanType;

/**
 * DOC ggu class global comment. Detailled comment
 */
public final class DBBeanParserHelper {

    /**
     * 
     * there are some problem about parsing the line, <br/>
     * 
     * process the data that contain semicolon, such as Description= "a;b,c". <br/>
     * 
     * need improve the splitted value.
     */
    public static DBTableForDelimitedBean parseLineToBean(String line) {
        // TODO improving
        String[] values = line.split(";");
        return getCheckedBeanType(values);
    }

    private static DBTableForDelimitedBean getCheckedBeanType(String[] tmpDatas) {
        DBTableForDelimitedBean bean = new DBTableForDelimitedBean();
        int length = tmpDatas.length;
        BeanType type = BeanType.UNKNOWN;
        if (length < DBTableForDelimitedBean.POSITION_DATABASETYPE || length > DBTableForDelimitedBean.TOTAL) {
            type = BeanType.UNKNOWN;
        } else {
            type = checkedColumn(tmpDatas, bean);
        }
        bean.setBeanType(type);
        return bean;

    }

    private static BeanType checkedColumn(String[] datas, DBTableForDelimitedBean bean) {
        final int length = datas.length;

        // verify forward table part
        BeanType type = checkedTable(datas, bean);
        switch (type) {
        case UNKNOWN:
        case CONNECTION:
            return type;
        case TABLE:
            if (isAllEmpty(DBTableForDelimitedBean.POSITION_ORIGINALTABLENAME, datas)) {
                return type;
            }
        default:
        }

        if (length < DBTableForDelimitedBean.POSITION_LENGTH) {
            return BeanType.UNKNOWN;
        }
        // OriginalLabel
        String str = datas[DBTableForDelimitedBean.POSITION_ORIGINALLABEL - 1].trim();
        if (isNullable(str)) {
            return BeanType.UNKNOWN;
        }
        if (!Pattern.matches(MetadataEmfTableEditor.VALID_PATTERN_COLUMN_NAME, str)) {
            return BeanType.UNKNOWN;
        }
        bean.setOriginalLabel(str);
        // Label
        str = datas[DBTableForDelimitedBean.POSITION_ORIGINALLABEL - 2].trim();
        if (isNullable(str)) {
            bean.setLabel(bean.getOriginalLabel());
        } else {
            if (!Pattern.matches(MetadataEmfTableEditor.VALID_PATTERN_COLUMN_NAME, str)) {
                return BeanType.UNKNOWN;
            }
            bean.setLabel(str);
        }
        // key
        str = datas[DBTableForDelimitedBean.POSITION_KEY - 1];
        if (isNullable(str)) {
            return BeanType.UNKNOWN;
        }
        str = str.trim().toLowerCase();
        if (Boolean.TRUE.toString().equals(str)) {
            bean.setKey(true);
        } else if (Boolean.FALSE.toString().equals(str)) {
            bean.setKey(false);
        } else {
            return BeanType.UNKNOWN;
        }
        // length
        str = datas[DBTableForDelimitedBean.POSITION_LENGTH - 1];
        if (isNullable(str)) {
            return BeanType.UNKNOWN;
        }
        str = str.trim();
        if (!Pattern.matches("^[0-9]+$", str)) { //$NON-NLS-1$
            return BeanType.UNKNOWN;
        }
        try {
            bean.setLength(Integer.parseInt(str));
        } catch (NumberFormatException e) {
            return BeanType.UNKNOWN;
        }
        addColumnsInfo(datas, bean);

        return BeanType.COLUMN;
    }

    private static void addColumnsInfo(String[] datas, DBTableForDelimitedBean bean) {
        // Comment
        bean.setComment(datas[20].trim());
        // Default Value
        bean.setDefaultValue(datas[21].trim());

        final int start = DBTableForDelimitedBean.POSITION_LENGTH;
        final int end = DBTableForDelimitedBean.TOTAL;
        String[] tmpDatas = getExtendArrays(start, end, datas);

        // Nullable
        String tmp = tmpDatas[0].toLowerCase();
        if (Boolean.TRUE.toString().equals(tmp)) {
            bean.setNullable(true);
        } else {
            bean.setNullable(false);
        }
        // Pattern
        bean.setPattern(tmpDatas[1]);
        // Precision
        tmp = tmpDatas[2];
        if (isNullable(tmp)) {
            bean.setPrecision(0);
        } else {
            try {
                int precision = Integer.parseInt(tmp);
                bean.setPrecision(precision);
            } catch (NumberFormatException e) {
                bean.setPrecision(0);
            }
        }
        // Talend Type and DB Type
        bean.setTalendType(tmpDatas[3]);
        bean.setDbType(tmpDatas[4]);

    }

    private static BeanType checkedTable(String[] datas, DBTableForDelimitedBean bean) {
        final int length = datas.length;

        // verify forward connection part
        BeanType type = checkedConnectin(datas, bean);
        switch (type) {
        case UNKNOWN:
            return type;
        case CONNECTION:
            if (isAllEmpty(DBTableForDelimitedBean.POSITION_ORIGINALTABLENAME - 2, datas)) {
                return BeanType.CONNECTION;
            }
        default:
        }

        if (length < DBTableForDelimitedBean.POSITION_ORIGINALTABLENAME) {
            return BeanType.UNKNOWN;
        }
        // OriginalTableName
        String tableName = datas[DBTableForDelimitedBean.POSITION_ORIGINALTABLENAME - 1].trim();
        if (isNullable(tableName)) {
            return BeanType.UNKNOWN;
        }
        // the OriginalLabel verify
        if (!Pattern.matches(RepositoryConstants.getPattern(ERepositoryObjectType.METADATA_CON_TABLE), tableName)) {
            return BeanType.UNKNOWN;
        }
        /**
         * Metadata Schema
         */
        // Original Table Name
        bean.setOriginalTableName(tableName);
        // Table Name
        tableName = datas[DBTableForDelimitedBean.POSITION_ORIGINALTABLENAME - 2].trim();
        if (isNullable(tableName)) { //$NON-NLS-1$
            bean.setTableName(bean.getOriginalTableName());
        } else {
            bean.setTableName(tableName);
        }
        return BeanType.TABLE;

    }

    private static BeanType checkedConnectin(String[] datas, DBTableForDelimitedBean bean) {
        final int length = datas.length;
        if (length < DBTableForDelimitedBean.POSITION_DATABASETYPE) {
            return BeanType.UNKNOWN;
        }
        String name = datas[DBTableForDelimitedBean.POSITION_NAME - 1].trim();
        // verify the Name and DatabaseType
        if (isNullable(name)) {
            return BeanType.UNKNOWN;
        }
        // if(isNullable(datas[DBTableForDelimitedBean.POSITION_DATABASETYPE - 1])){
        // return BeanType.UNKNOWN;
        // }
        // // the name verify
        // if (!Pattern.matches(RepositoryConstants.getPattern(ERepositoryObjectType.METADATA_CONNECTIONS), name)) {
        // return BeanType.UNKNOWN;
        // }
        /**
         * Properties
         */
        // name
        bean.setName(name); // 0
        // Purpose and Description
        bean.setPurpose(datas[1].trim());
        bean.setDescription(datas[2].trim());
        // Version
        String tmp = datas[3].trim();
        if ("".equals(tmp)) { //$NON-NLS-1$
            bean.setVersion(VersionUtils.DEFAULT_VERSION);
        } else {
            bean.setVersion(tmp);
        }
        // Status
        bean.setStatus(datas[4].trim());

        /**
         * Database Connection
         */
        // Database Type
        bean.setDatabaseType(datas[DBTableForDelimitedBean.POSITION_DATABASETYPE - 1].trim()); // 5
        // add others
        if (!addDBConnectionInfo(datas, bean)) {
            return BeanType.UNKNOWN;
        }
        return BeanType.CONNECTION;
    }

    private static boolean addDBConnectionInfo(String[] datas, DBTableForDelimitedBean bean) {
        final int start = DBTableForDelimitedBean.POSITION_DATABASETYPE;
        final int end = DBTableForDelimitedBean.POSITION_ORIGINALTABLENAME - 2;
        String[] tmpDatas = getExtendArrays(start, end, datas);

        // Connection String
        bean.setConnectionStr(tmpDatas[0]);

        // Login and Password
        bean.setLogin(tmpDatas[1]);
        bean.setPassword(tmpDatas[2]);
        // Server
        bean.setServer(tmpDatas[3]);
        // Port
        String tmp = tmpDatas[4];
        boolean b = true;
        if (!isNullable(tmp) && !isNullable(bean.getDatabaseType())) {
            if (bean.getDatabaseType().equals("Ingres")) { //$NON-NLS-1$
                b = Pattern.matches(Messages.getString("DatabaseForm.ingresDBRegex"), tmp); //$NON-NLS-1$
            } else {
                b = Pattern.matches(Messages.getString("DatabaseForm.otherDBRegex"), tmp); //$NON-NLS-1$
            }
        }
        if (!b) { // not right
            return false;
        }
        bean.setPort(tmp);

        // Database, Schema, DataSource
        bean.setDatabase(tmpDatas[5]);
        bean.setDbSchema(tmpDatas[6]);
        bean.setDataSource(tmpDatas[7]);
        // file and Database Root Path
        bean.setFile(tmpDatas[8]);
        bean.setDbRootPath(tmpDatas[9]);
        return true;
    }

    private static String[] getExtendArrays(int start, int end, String[] datas) {
        String[] tmpDatas = new String[end - start];
        for (int i = 0; i < tmpDatas.length; i++) {
            tmpDatas[i] = "";
        }
        for (int i = 0; i < datas.length - start && i < tmpDatas.length; i++) {
            tmpDatas[i] = datas[start + i].trim();
        }
        return tmpDatas;
    }

    private static boolean isAllEmpty(int start, String[] datas) {
        boolean isAllEmpty = true;
        for (int i = start; i < datas.length; i++) {
            if (!isNullable(datas[i])) {
                isAllEmpty = false;
                break;
            }
        }
        return isAllEmpty;
    }

    private static boolean isNullable(String name) {
        if (name == null || "".equals(name.trim())) { //$NON-NLS-1$
            return true;
        }
        return false;
    }

    // test
    public static void main(String[] args) {
        String str = "oracleGen;some;Description;1.3;;Oracle with SID;sfsd;;;;1200;;;;;;x;c;new;OUeTq;comment;xxx;false;7;true;yy:mm;0;id_String;VARCHAR";
        DBTableForDelimitedBean bean = getCheckedBeanType(str.split(";"));
        System.out.println(bean.getBeanType());

    }
}
