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
import org.talend.repository.ui.actions.metadata.importing.PositionHelper;

/**
 * DOC ggu class global comment. Detailled comment
 */
public final class DBBeanParserHelper {

    private static PositionHelper position;

    /**
     * 
     * there are some problem about parsing the line, <br/>
     * 
     * process the data that contain semicolon, such as Description= "a;b,c". <br/>
     * 
     * need improve the splitted value.
     */
    public static DBTableForDelimitedBean parseLineToBean(ERepositoryObjectType nodeType, String line) {
        // TODO improving
        String[] values = line.split(";");
        switch (nodeType) {
        default:
            // not process
            return null;
        case METADATA_CONNECTIONS:
            position = new PositionHelper(29, 1, 6, 18, 20, 23, 24);
            break;
        case METADATA_FILE_DELIMITED:
            // use the bean var
            position = new PositionHelper(16, 1, 6, 7, 8, 11, 12);
            break;
        // case METADATA_FILE_POSITIONAL:
        // case METADATA_FILE_REGEXP:
        // case METADATA_FILE_XML:
        // case METADATA_FILE_LDIF:
        // case METADATA_GENERIC_SCHEMA:
        // case METADATA_LDAP_SCHEMA:

        }
        return getCheckedBeanType(nodeType, values);
    }

    private static DBTableForDelimitedBean getCheckedBeanType(ERepositoryObjectType nodeType, String[] tmpDatas) {
        DBTableForDelimitedBean bean = new DBTableForDelimitedBean();
        int length = tmpDatas.length;
        BeanType type = BeanType.UNKNOWN;
        if (length < position.getConnectionIndex() || length > position.getTotal()) {
            type = BeanType.UNKNOWN;
        } else {
            type = checkedColumn(nodeType, tmpDatas, bean);
        }
        bean.setBeanType(type);
        return bean;

    }

    private static BeanType checkedColumn(ERepositoryObjectType nodeType, String[] datas, DBTableForDelimitedBean bean) {
        final int length = datas.length;

        // verify forward table part
        BeanType type = checkedTable(nodeType, datas, bean);
        switch (type) {
        case UNKNOWN:
        case CONNECTION:
            return type;
        case TABLE:
            if (isAllEmpty(position.getOriginalTablenameIndex(), datas)) {
                return type;
            }
        default:
        }

        if (length < position.getLengthIndex()) {
            return BeanType.UNKNOWN;
        }
        // OriginalLabel
        String str = datas[position.getOriginalLabelIndex() - 1].trim();
        if (isNullable(str)) {
            return BeanType.UNKNOWN;
        }
        if (!Pattern.matches(MetadataEmfTableEditor.VALID_PATTERN_COLUMN_NAME, str)) {
            return BeanType.UNKNOWN;
        }
        bean.setOriginalLabel(str);

        if (nodeType == ERepositoryObjectType.METADATA_CONNECTIONS) {
            // Label
            str = datas[position.getOriginalLabelIndex() - 2].trim();
            if (isNullable(str)) {
                bean.setLabel(bean.getOriginalLabel());
            } else {
                if (!Pattern.matches(MetadataEmfTableEditor.VALID_PATTERN_COLUMN_NAME, str)) {
                    return BeanType.UNKNOWN;
                }
                bean.setLabel(str);
            }
        }

        // key
        str = datas[position.getKeyIndex() - 1];
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
        str = datas[position.getLengthIndex() - 1];
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
        addColumnsInfo(nodeType, datas, bean);

        return BeanType.COLUMN;
    }

    private static void addColumnsInfo(ERepositoryObjectType nodeType, String[] datas, DBTableForDelimitedBean bean) {
        // Comment
        bean.setComment(datas[position.getOriginalLabelIndex()].trim());
        // Default Value
        bean.setDefaultValue(datas[position.getOriginalLabelIndex() + 1].trim());

        final int start = position.getLengthIndex();
        final int end = position.getTotal();
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
        if (nodeType == ERepositoryObjectType.METADATA_CONNECTIONS) {
            bean.setDbType(tmpDatas[4]);
        }

    }

    private static BeanType checkedTable(ERepositoryObjectType nodeType, String[] datas, DBTableForDelimitedBean bean) {
        final int length = datas.length;

        // verify forward connection part
        BeanType type = checkedConnectin(nodeType, datas, bean);
        switch (type) {
        case UNKNOWN:
            return type;
        case CONNECTION:
            int step = 0;
            if (nodeType == ERepositoryObjectType.METADATA_CONNECTIONS) {
                step = 1;
            }
            if (isAllEmpty(position.getOriginalTablenameIndex() - 1 - step, datas)) {
                return BeanType.CONNECTION;
            }
        default:
        }

        if (length < position.getOriginalTablenameIndex()) {
            return BeanType.UNKNOWN;
        }
        // OriginalTableName
        String tableName = datas[position.getOriginalTablenameIndex() - 1].trim();
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

        if (nodeType == ERepositoryObjectType.METADATA_CONNECTIONS) {

            tableName = datas[position.getOriginalTablenameIndex() - 2].trim();
            if (isNullable(tableName)) { //$NON-NLS-1$
                bean.setTableName(bean.getOriginalTableName());
            } else {
                bean.setTableName(tableName);
            }
        } else {
            bean.setTableName(bean.getOriginalTableName());
        }
        // Table Name

        return BeanType.TABLE;

    }

    private static BeanType checkedConnectin(ERepositoryObjectType nodeType, String[] datas, DBTableForDelimitedBean bean) {
        final int length = datas.length;
        if (length < position.getConnectionIndex()) {
            return BeanType.UNKNOWN;
        }
        String name = datas[position.getNameIndex() - 1].trim();
        // verify the Name
        if (isNullable(name)) {
            return BeanType.UNKNOWN;
        }

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

        // Connections
        switch (nodeType) {
        default:
            // not process
            return BeanType.UNKNOWN;
        case METADATA_CONNECTIONS:
            // Database Type
            bean.setDatabaseType(datas[position.getConnectionIndex() - 1].trim()); // 5
            // add others
            if (!addDBConnectionInfo(datas, bean)) {
                return BeanType.UNKNOWN;
            }
            break;
        case METADATA_FILE_DELIMITED:
            // Delimited connection
            bean.setFile(datas[position.getConnectionIndex() - 1].trim());
            break;
        // TODO
        // case METADATA_FILE_POSITIONAL:
        // case METADATA_FILE_REGEXP:
        // case METADATA_FILE_XML:
        // case METADATA_FILE_LDIF:
        // case METADATA_GENERIC_SCHEMA:
        // case METADATA_LDAP_SCHEMA:
        }

        return BeanType.CONNECTION;
    }

    private static boolean addDBConnectionInfo(String[] datas, DBTableForDelimitedBean bean) {
        final int start = position.getConnectionIndex();
        final int end = position.getOriginalTablenameIndex() - 2;
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
        // String str = "oracleGen;test;generated Metadata;1.0;;C://test.csv;e;Kd6Ozu;;;false;3;true;;5;id_String";
        String str = "oracleGen;;;;;Oracle with SID;;;;;;;;;;;;f;a;xxx;;;false;19;true;;0;;";
        DBTableForDelimitedBean bean = parseLineToBean(ERepositoryObjectType.METADATA_CONNECTIONS, str);
        System.out.println(bean.getBeanType());

    }
}
