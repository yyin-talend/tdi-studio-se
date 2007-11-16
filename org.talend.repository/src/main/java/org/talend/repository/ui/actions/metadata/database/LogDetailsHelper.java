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

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.talend.repository.i18n.Messages;
import org.talend.repository.ui.actions.metadata.database.DBProcessRecords.ProcessType;
import org.talend.repository.ui.actions.metadata.database.DBProcessRecords.RecordsType;
import org.talend.repository.ui.actions.metadata.database.DBProcessRecords.RejectedType;

/**
 * DOC ggu class global comment. Detailled comment
 */
public final class LogDetailsHelper {

    public static final String RETURN = "\r\n"; //$NON-NLS-1$

    public static final String SPACE = " "; //$NON-NLS-1$

    public static final String SEMICOLON = ","; //$NON-NLS-1$

    public static String getMainReportLogs(ProcessType pType, DBProcessRecords processRecords) {
        StringBuilder sb = new StringBuilder();

        int conNum = processRecords.getRecords(pType, RecordsType.CONNECTION);
        int tableNum = processRecords.getRecords(pType, RecordsType.TABLE);
        int fieldNum = processRecords.getRecords(pType, RecordsType.FIELD);
        switch (pType) {
        case IMPORT:
            sb.append(Messages.getString("ConnectionDBTableHelper.Imported")); //$NON-NLS-1$
            break;
        case REJECT:
            sb.append(Messages.getString("ConnectionDBTableHelper.Rejected")); //$NON-NLS-1$
            break;
        default:
            return ""; //$NON-NLS-1$
        }
        sb.append(RETURN);
        sb.append(SPACE + SPACE);
        sb.append(Messages.getString("LogDetailsHelper.ReportedInfo", conNum, tableNum, fieldNum)); //$NON-NLS-1$
        sb.append(RETURN);
        return sb.toString();
    }

    public static String getExistedTableLogs(DBProcessRecords processRecords) {
        StringBuilder sb = new StringBuilder();
        Map<String, Set<String>> map = processRecords.getExistedTableMap();
        if (map.isEmpty()) {
            return ""; //$NON-NLS-1$
        }
        for (String key : map.keySet()) {
            Set<String> values = map.get(key);
            if (values != null) {
                for (String value : values) {
                    sb.append(Messages.getString("LogDetailsHelper.ExitedTableInfo", value, key)); //$NON-NLS-1$
                    sb.append(RETURN);
                }
            }
        }
        return sb.toString();
    }

    public static String getUnknownLogs(DBProcessRecords processRecords) {
        StringBuilder sb = new StringBuilder();
        sb.append(getUnknownLogs(RejectedType.DATABASETYPE, processRecords));
        sb.append(getUnknownDBForPerl(processRecords));
        sb.append(getUnknownLogs(RejectedType.TALENDTYPE, processRecords));
        sb.append(getTypeNotMappingLogs(processRecords));
        return sb.toString();

    }

    private static String getUnknownLogs(RejectedType rType, DBProcessRecords processRecords) {
        StringBuilder sb = new StringBuilder();

        Set<String> records = processRecords.getRejectedRecords(rType);
        if (records.isEmpty()) {
            return ""; //$NON-NLS-1$
        }
        switch (rType) {
        case TALENDTYPE:
            sb.append(Messages.getString("LogDetailsHelper.UnknownTalendType")); //$NON-NLS-1$
            break;
        case DATABASETYPE:
            sb.append(Messages.getString("LogDetailsHelper.UnknownDatabaseType")); //$NON-NLS-1$
            break;
        default:
            return ""; //$NON-NLS-1$
        }
        sb.append(SPACE);
        for (String type : records) {
            sb.append(type);
            sb.append(SEMICOLON);
            sb.append(SPACE);
        }
        sb = sb.deleteCharAt(sb.lastIndexOf(SEMICOLON));
        sb.append(RETURN);
        return sb.toString();
    }

    private static String getUnknownDBForPerl(DBProcessRecords processRecords) {
        StringBuilder sb = new StringBuilder();
        Set<String> records = processRecords.getUnknownDBForPerl();
        if (records.isEmpty()) {
            return ""; //$NON-NLS-1$
        }
        sb.append(Messages.getString("LogDetailsHelper.UnknownDatabaseTypeByPerl")); //$NON-NLS-1$
        sb.append(SPACE);
        for (String type : records) {
            sb.append(type);
            sb.append(SEMICOLON);
            sb.append(SPACE);
        }
        sb = sb.deleteCharAt(sb.lastIndexOf(SEMICOLON));
        sb.append(RETURN);
        return sb.toString();

    }

    private static String getTypeNotMappingLogs(DBProcessRecords processRecords) {
        StringBuilder sb = new StringBuilder();
        List<TalendTypeMappingBean> beanList = processRecords.getTypeNotMapping();
        if (beanList.isEmpty()) {
            return ""; //$NON-NLS-1$
        }
        sb.append(RETURN);
        sb.append(Messages.getString("LogDetailsHelper.NotSupportedTitle")); //$NON-NLS-1$
        sb.append(RETURN);
        sb.append(SPACE);
        sb.append(SPACE);
        sb.append(getSuitableWidth(Messages.getString("LogDetailsHelper.DatabaseName"))); //$NON-NLS-1$
        sb.append(getSuitableWidth(Messages.getString("LogDetailsHelper.TableName"))); //$NON-NLS-1$
        sb.append(getSuitableWidth(Messages.getString("LogDetailsHelper.ColumnName"))); //$NON-NLS-1$
        sb.append(getSuitableWidth(Messages.getString("LogDetailsHelper.TalendType"))); //$NON-NLS-1$
        sb.append(Messages.getString("LogDetailsHelper.DatabaseType")); //$NON-NLS-1$
        sb.append(RETURN);

        for (TalendTypeMappingBean bean : beanList) {
            sb.append(SPACE);
            sb.append(SPACE);
            sb.append(getSuitableWidth(bean.getDbName()));
            sb.append(getSuitableWidth(bean.getTableName()));
            sb.append(getSuitableWidth(bean.getColumnName()));
            sb.append(getSuitableWidth(bean.getTalendType()));
            sb.append(bean.getDbType());
            sb.append(RETURN);
        }

        return sb.toString();

    }

    private static String getSuitableWidth(String str) {
        if (str == null) {
            str = ""; //$NON-NLS-1$
        }
        final int width = 20;
        if (str.length() >= width) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        for (int i = 0; i < width - str.length(); i++) {
            sb.append(SPACE);
        }
        return sb.toString();

    }
}
