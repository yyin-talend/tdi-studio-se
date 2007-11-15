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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * ggu class global comment. Detailled comment <br/>
 * 
 */
public class DBProcessRecords {

    /**
     * 
     */
    public enum DBType {
        CONNECTION,
        TABLE,
        FIELD
    }

    /**
     * 
     */
    public enum RecordsType {
        CONNECTION,
        TABLE,
        FIELD
    }

    /**
     * 
     */
    public enum ProcessType {
        REJECT,
        IMPORT
    }

    private Map<ProcessType, Set<String>> processToConnMap = new HashMap<ProcessType, Set<String>>();

    private Map<String, Set<String>> importedConnToTableMap = new HashMap<String, Set<String>>();

    private Map<String, Set<String>> importedTableToFieldMap = new HashMap<String, Set<String>>();

    private Map<String, Set<String>> rejectedConnToTableMap = new HashMap<String, Set<String>>();

    private Map<String, Set<String>> rejectedTableToFieldMap = new HashMap<String, Set<String>>();

    public DBProcessRecords() {
        super();
        initNum();
    }

    private void initNum() {
        processToConnMap.put(ProcessType.IMPORT, new HashSet<String>());
        processToConnMap.put(ProcessType.REJECT, new HashSet<String>());

    }

    public void importedRecords(DBTableForDelimitedBean bean) {
        addRecords(ProcessType.IMPORT, bean);
    }

    /**
     * 
     * ggu Comment method "addRecords".
     * 
     * according to ProcessType and bean, add records
     * 
     * @param pType
     * @param bean
     */
    private void addRecords(ProcessType pType, DBTableForDelimitedBean bean) {
        if (!checkBean(bean)) {
            return;
        }

        switch (pType) {
        case REJECT:
        case IMPORT:
            Set<String> connSet = processToConnMap.get(pType);
            if (connSet == null) {
                connSet = new HashSet<String>();
                processToConnMap.put(pType, connSet);
            }
            String connName = bean.getName();
            if (!connSet.contains(connName)) {
                connSet.add(connName);
            }
            addTable(pType, bean);
            break;
        default:
        }

    }

    private void addTable(ProcessType pType, DBTableForDelimitedBean bean) {
        Map<String, Set<String>> tmpMap;
        switch (pType) {
        case REJECT:
            tmpMap = rejectedConnToTableMap;
            break;
        case IMPORT:
            tmpMap = importedConnToTableMap;
            break;
        default:
            return;
        }
        String connName = bean.getName();
        Set<String> tableSet = tmpMap.get(connName);
        if (tableSet == null) {
            tableSet = new HashSet<String>();
            tmpMap.put(connName, tableSet);
        }
        String tableName = bean.getTableName();
        if (!tableSet.contains(tableName)) {
            tableSet.add(tableName);
        }
        addField(pType, bean);
    }

    private void addField(ProcessType pType, DBTableForDelimitedBean bean) {
        Map<String, Set<String>> tmpMap;
        switch (pType) {
        case REJECT:
            tmpMap = rejectedTableToFieldMap;
            break;
        case IMPORT:
            tmpMap = importedTableToFieldMap;
            break;
        default:
            return;
        }
        String tableName = bean.getTableName();
        Set<String> fieldSet = tmpMap.get(tableName);
        if (fieldSet == null) {
            fieldSet = new HashSet<String>();
            tmpMap.put(tableName, fieldSet);
        }
        String fieldName = bean.getLabel();
        if (!fieldSet.contains(fieldName)) {
            fieldSet.add(fieldName);
        }
    }

    /**
     * 
     * ggu Comment method "getRecords".
     * 
     * get the records number.
     * 
     * @param pType
     * @param rType
     * @return
     */

    public int getRecords(ProcessType pType, RecordsType rType) {
        if (!isValid(pType) || !isValid(rType)) {
            return 0;
        }
        switch (rType) {
        case CONNECTION:
            Set<String> connSet = processToConnMap.get(pType);
            if (connSet == null) {
                return 0;
            }
            return connSet.size();
        case TABLE:
            return getTableRecords(pType);
        case FIELD:
            return getFieldRecords(pType);
        default:
        }
        return 0;
    }

    private int getTableRecords(ProcessType pType) {
        Map<String, Set<String>> tmpMap;
        switch (pType) {
        case REJECT:
            tmpMap = rejectedConnToTableMap;
            break;
        case IMPORT:
            tmpMap = importedConnToTableMap;
            break;
        default:
            return 0;
        }

        int total = 0;
        for (String connName : tmpMap.keySet()) {
            Set<String> tableSet = tmpMap.get(connName);
            if (tableSet != null) {
                total += tableSet.size();
            }
        }
        return total;
    }

    private int getFieldRecords(ProcessType pType) {
        Map<String, Set<String>> tmpMap;
        switch (pType) {
        case REJECT:
            tmpMap = rejectedTableToFieldMap;
            break;
        case IMPORT:
            tmpMap = importedTableToFieldMap;
            break;
        default:
            return 0;
        }
        int total = 0;
        for (String tableName : tmpMap.keySet()) {
            Set<String> fieldSet = tmpMap.get(tableName);
            if (fieldSet != null) {
                total += fieldSet.size();
            }
        }
        return total;
    }

    /**
     * 
     * ggu Comment method "rejectRecords".
     * 
     * remove the imported records and add the rejected records.
     * 
     * @param bean
     */
    public void rejectRecords(DBTableForDelimitedBean bean) {
        if (!checkBean(bean)) {
            return;
        }
        String connName = bean.getName();
        String tableName = bean.getTableName();

        // process impoted records.
        Set<String> connSet = processToConnMap.get(ProcessType.IMPORT);
        if (connSet != null && connSet.contains(connName)) {
            Set<String> tableSet = importedConnToTableMap.get(connName);
            if (tableSet != null) {
                tableSet.remove(tableName);
                if (tableSet.isEmpty()) {
                    // also delete the connection name
                    connSet.remove(connName);
                    importedConnToTableMap.remove(connName);
                }
                importedTableToFieldMap.remove(tableName);
            }
        }

        // process rejected records
        addRecords(ProcessType.REJECT, bean);

    }

    public boolean isRejectedBean(DBTableForDelimitedBean bean) {
        String connName = bean.getName();
        String tableName = bean.getTableName();

        Set<String> connSet = processToConnMap.get(ProcessType.REJECT);
        if (connSet != null && connSet.contains(connName)) {
            Set<String> tableSet = importedConnToTableMap.get(connName);
            if (tableSet != null && tableSet.contains(tableName)) {
                return true;
            }
        }
        return false;

    }

    private boolean checkBean(DBTableForDelimitedBean bean) {
        if (isValid(bean.getName()) && isValid(bean.getTableName()) && isValid(bean.getLabel())) {
            return true;
        }
        return false;
    }

    private boolean isValid(Object name) {
        if (name == null) {
            return false;
        }
        if (name instanceof String) {
            if ("".equals(name.toString().trim())) {
                return false;
            }
        }
        return true;
    }
}
