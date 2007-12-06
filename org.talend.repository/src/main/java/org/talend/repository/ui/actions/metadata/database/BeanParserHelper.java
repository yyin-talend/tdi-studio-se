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

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import org.talend.commons.utils.VersionUtils;
import org.talend.core.model.metadata.editor.MetadataEmfTableEditor;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.RepositoryConstants;
import org.talend.repository.ui.actions.metadata.importing.bean.DelimitedTableForDelimitedBean;
import org.talend.repository.ui.actions.metadata.importing.bean.TablesForDelimitedBean;

/**
 * DOC ggu class global comment. Detailled comment
 */
public final class BeanParserHelper {

    public static TablesForDelimitedBean parseLineToBean(ERepositoryObjectType nodeType, List<String> row) throws IOException {

        switch (nodeType) {
        case METADATA_CONNECTIONS:
            return createDBBean(row);
        case METADATA_FILE_DELIMITED:
            return createDelimitedBean(row);
        default:
            return null;

        }
    }

    private static DBTableForDelimitedBean createDBBean(List<String> input) throws IOException {
        DBTableForDelimitedBean bean = new DBTableForDelimitedBean();

        if (!addMainInfo(bean, input)) {
            return null;
        }
        if (!addDBConnection(bean, input)) {
            return null;
        }
        // table name
        String tmp = input.get(bean.getIndexOriginalTableName()).trim();
        if (!checkTableName(tmp)) {
            return null;
        }
        bean.setOriginalTableName(tmp);
        if (isNullable(bean.getTableName())) {
            bean.setTableName(bean.getOriginalTableName());
        } else if (!checkTableName(bean.getTableName())) {
            return null;

        }
        // label
        tmp = input.get(bean.getIndexOriginalLabel()).trim();
        if (!checkLabel(tmp)) {
            return null;
        }

        bean.setOriginalLabel(tmp);
        if (isNullable(bean.getLabel())) {
            bean.setLabel(bean.getOriginalLabel());
        } else if (!checkLabel(bean.getLabel())) {
            return null;
        }
        // dbType
        bean.setDbType(input.get(bean.getIndexLengh() + 5).trim());
        return bean;
    }

    private static DelimitedTableForDelimitedBean createDelimitedBean(List<String> input) throws IOException {
        DelimitedTableForDelimitedBean bean = new DelimitedTableForDelimitedBean();

        if (!addMainInfo(bean, input)) {
            return null;
        }
        if (!addDelimitedConnection(bean, input)) {
            return null;
        }
        if (!checkTableName(bean.getTableName())) {
            return null;
        }
        if (!checkLabel(bean.getLabel())) {
            return null;
        }
        return bean;
    }

    private static boolean addMainInfo(TablesForDelimitedBean bean, List<String> input) throws IOException {
        int size = input.size();
        if (size < bean.getIndexLengh() || size > bean.getVarNum()) {
            return false;
        }
        extendData(input, bean.getVarNum());

        if (!addProperties(bean, input)) {
            return false;
        }
        if (!addFields(bean, input)) {
            return false;
        }
        return true;
    }

    private static boolean addProperties(TablesForDelimitedBean bean, List<String> input) throws IOException {
        final int base = bean.getIndexName();
        String tmp = "";
        // name
        tmp = input.get(base).trim();
        if (isNullable(tmp)) {
            return false;
        }
        bean.setName(tmp);
        bean.setPurpose(input.get(base + 1).trim());
        bean.setDescription(input.get(base + 2).trim());
        // version
        tmp = input.get(base + 3).trim();
        if (isNullable(tmp)) {
            bean.setVersion(VersionUtils.DEFAULT_VERSION);
        } else {
            bean.setVersion(tmp);
        }
        // Status
        bean.setStatus(input.get(base + 4).trim());

        return true;
    }

    private static boolean addFields(TablesForDelimitedBean bean, List<String> input) throws IOException {
        bean.setTableName(input.get(bean.getIndexTableName()).trim());
        bean.setLabel(input.get(bean.getIndexLabel()).trim());
        final int base = bean.getIndexKey();
        // commit
        String str = input.get(base - 2).trim();
        bean.setComment(str);
        // defaultValue
        bean.setDefaultValue(input.get(base - 1).trim());
        // key
        str = input.get(base).trim();
        if (isNullable(str)) {
            return false;
        }
        str = str.trim().toLowerCase();
        if (Boolean.TRUE.toString().equals(str)) {
            bean.setKey(true);
        } else if (Boolean.FALSE.toString().equals(str)) {
            bean.setKey(false);
        } else {
            return false;
        }
        // length
        str = input.get(bean.getIndexLengh()).trim();
        if (isNullable(str)) {
            return false;
        }
        str = str.trim();
        if (!Pattern.matches("^[0-9]+$", str)) { //$NON-NLS-1$
            return false;
        }
        try {
            bean.setLength(Integer.parseInt(str));
        } catch (NumberFormatException e) {
            return false;
        }
        // Nullable
        str = input.get(base + 2).trim().toLowerCase();
        if (Boolean.TRUE.toString().equals(str)) {
            bean.setNullable(true);
        } else if (Boolean.FALSE.toString().equals(str) || isNullable(str)) {
            bean.setNullable(false);
        } else {
            return false;
        }
        // Pattern
        bean.setPattern(input.get(base + 3).trim());
        // Precision
        str = input.get(base + 4).trim();
        if (isNullable(str)) {
            bean.setPrecision(0);
        } else {
            try {
                int precision = Integer.parseInt(str);
                bean.setPrecision(precision);
            } catch (NumberFormatException e) {
                bean.setPrecision(0);
            }
        }
        // Talend Type
        bean.setTalendType(input.get(base + 5).trim());
        return true;
    }

    private static boolean addDBConnection(DBTableForDelimitedBean bean, List<String> input) throws IOException {
        int base = bean.getIndexDatabaseType();
        // Databse Type
        bean.setDatabaseType(input.get(base).trim());
        bean.setConnectionStr(input.get(base + 1).trim());

        // Login and Password
        bean.setLogin(input.get(base + 2).trim());
        bean.setPassword(input.get(base + 3).trim());
        // Server
        bean.setServer(input.get(base + 4).trim());
        // Port
        String tmp = input.get(base + 5).trim();
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
        bean.setDatabase(input.get(base + 6).trim());
        bean.setDbSchema(input.get(base + 7).trim());
        bean.setDataSource(input.get(base + 8).trim());
        // file and Database Root Path
        bean.setFile(input.get(base + 9).trim());
        bean.setDbRootPath(input.get(base + 10).trim());

        return true;
    }

    private static boolean addDelimitedConnection(DelimitedTableForDelimitedBean bean, List<String> input) throws IOException {
        final int base = bean.getIndexFilePath();
        // file
        String tmp = input.get(base).trim();
        bean.setFilePath(tmp);

        bean.setFormat(input.get(base + 1).trim());
        bean.setEncoding(input.get(base + 2).trim());

        bean.setFieldSeparatorValue(input.get(base + 3).trim());
        bean.setRowSeparatorValue(input.get(base + 4).trim());
        bean.setEscapeType(input.get(base + 5).trim());
        bean.setEscapeChar(input.get(base + 6).trim());
        bean.setTextEnclosure(input.get(base + 7).trim());

        tmp = input.get(base + 8).trim().toLowerCase();
        if (Boolean.TRUE.toString().equals(tmp)) {
            bean.setFirstLineCaption(true);
        } else if (Boolean.FALSE.toString().equals(tmp) || isNullable(tmp)) {
            bean.setFirstLineCaption(false);
        } else {
            return false;
        }
        // HeaderValue
        tmp = input.get(base + 9).trim();
        if (isNullable(tmp)) {
            bean.setHeaderValue(0);
        } else {
            try {
                int value = Integer.parseInt(tmp);
                bean.setHeaderValue(value);
            } catch (NumberFormatException e) {
                return false;
            }
        }
        // FooterValue
        tmp = input.get(base + 10).trim();
        if (isNullable(tmp)) {
            bean.setFooterValue(0);
        } else {
            try {
                int value = Integer.parseInt(tmp);
                bean.setFooterValue(value);
            } catch (NumberFormatException e) {
                return false;
            }
        }
        // empty row
        tmp = input.get(base + 11).trim().toLowerCase();
        if (Boolean.TRUE.toString().equals(tmp)) {
            bean.setRemoveEmptyRow(true);
        } else if (Boolean.FALSE.toString().equals(tmp) || isNullable(tmp)) {
            bean.setRemoveEmptyRow(false);
        } else {
            return false;
        }
        // LimitValue
        tmp = input.get(base + 12).trim();
        if (isNullable(tmp)) {
            bean.setLimitValue(0);
        } else {
            try {
                int value = Integer.parseInt(tmp);
                bean.setLimitValue(value);
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return true;
    }

    private static void extendData(List<String> row, int length) {
        for (int i = 0; i < length - row.size(); i++) {
            row.add("");
        }
    }

    private static boolean checkTableName(String name) {

        return !isNullable(name)
                && Pattern.matches(RepositoryConstants.getPattern(ERepositoryObjectType.METADATA_CON_TABLE), name);

    }

    private static boolean checkLabel(String label) {
        return !isNullable(label) && Pattern.matches(MetadataEmfTableEditor.VALID_PATTERN_COLUMN_NAME, label);

    }

    private static boolean isNullable(String name) {
        if (name == null || "".equals(name.trim())) { //$NON-NLS-1$
            return true;
        }
        return false;
    }

}
