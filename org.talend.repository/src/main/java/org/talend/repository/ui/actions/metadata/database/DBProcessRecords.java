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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.repository.ui.actions.metadata.importing.bean.TablesForDelimitedBean;

/**
 * ggu class global comment. Detailled comment <br/>
 * 
 */
public class DBProcessRecords {

    /**
     * record the rejected details
     */
    public enum RejectedType {
        TALENDTYPE,
        DATABASETYPE,
        // delimited
        FILE,
        FORMAT,
        ESCAPE

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
        TYPE_NOT_MAPPING,
        EXISTEDTABLE,
        REJECT,
        IMPORT
    }

    private Map<ProcessType, Set<String>> processToConnMap = new HashMap<ProcessType, Set<String>>();

    private Map<String, Set<String>> importedConnToTableMap = new HashMap<String, Set<String>>();

    private Map<String, Set<String>> importedTableToFieldMap = new HashMap<String, Set<String>>();

    private Map<String, Set<String>> rejectedConnToTableMap = new HashMap<String, Set<String>>();

    private Map<String, Set<String>> rejectedTableToFieldMap = new HashMap<String, Set<String>>();

    private Map<RejectedType, Set<ValueTypeBean>> rejectedMap = new HashMap<RejectedType, Set<ValueTypeBean>>();

    // not supported by perl
    private Set<ValueTypeBean> perlDatabaseRejectedMap = new HashSet<ValueTypeBean>();

    private Map<String, Set<String>> existedTableMap = new HashMap<String, Set<String>>();

    private List<TalendTypeMappingBean> dbTypeNotMappingList = new ArrayList<TalendTypeMappingBean>();

    private ERepositoryObjectType nodeType;

    public DBProcessRecords(ERepositoryObjectType nodeType) {
        super();
        this.nodeType = nodeType;
        initNum();
    }

    private void initNum() {
        processToConnMap.put(ProcessType.IMPORT, new HashSet<String>());
        processToConnMap.put(ProcessType.REJECT, new HashSet<String>());

        rejectedMap.put(RejectedType.DATABASETYPE, new HashSet<ValueTypeBean>());
        rejectedMap.put(RejectedType.TALENDTYPE, new HashSet<ValueTypeBean>());
        rejectedMap.put(RejectedType.FILE, new HashSet<ValueTypeBean>());
    }

    public void setTypeNotMapping(DBTableForDelimitedBean bean) {
        addRecords(ProcessType.TYPE_NOT_MAPPING, bean);

    }

    public List<TalendTypeMappingBean> getTypeNotMapping() {
        return dbTypeNotMappingList;
    }

    /**
     * 
     * DOC ggu Comment method "addRejectedRecords".
     * 
     * @param rType
     * @param name
     */
    public void addRejectedRecords(RejectedType rType, String name, String connName) {
        if (name == null || !isValid(connName) || !isValid(rType)) {
            return;
        }

        switch (rType) {
        case TALENDTYPE:
        case DATABASETYPE:
        case FILE:
        case FORMAT:
        case ESCAPE:
            Set<ValueTypeBean> rejectedSet = rejectedMap.get(rType);
            if (rejectedSet == null) {
                rejectedSet = new HashSet<ValueTypeBean>();
                rejectedMap.put(rType, rejectedSet);
            }
            boolean found = false;
            for (ValueTypeBean bean : rejectedSet) {
                if (bean.getConnName().equals(connName) && bean.getValue().equals(name)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                rejectedSet.add(new ValueTypeBean(connName, name));
            }
        default:
        }

    }

    public Set<ValueTypeBean> getRejectedRecords(RejectedType rType) {
        if (!isValid(rType)) {
            return Collections.emptySet();
        }
        switch (rType) {
        case TALENDTYPE:
        case DATABASETYPE:
        case FILE:
        case FORMAT:
        case ESCAPE:
            Set<ValueTypeBean> rejectedSet = rejectedMap.get(rType);
            if (rejectedSet == null) {
                return Collections.emptySet();
            }
            return rejectedSet;
        default:
        }
        return Collections.emptySet();
    }

    /**
     * 
     * DOC ggu Comment method "setUnknownDBForPerl".
     * 
     * @param dbName
     */

    public void setUnknownDBForPerl(String dbName, String connName) {
        if (!isValid(connName) || dbName == null) {
            return;
        }
        perlDatabaseRejectedMap.add(new ValueTypeBean(connName, dbName));
    }

    public Set<ValueTypeBean> getUnknownDBForPerl() {
        return perlDatabaseRejectedMap;
    }

    /**
     * 
     * DOC ggu Comment method "addExistedTable".
     * 
     * @param bean
     */
    public void addExistedTable(TablesForDelimitedBean bean) {
        addRecords(ProcessType.EXISTEDTABLE, bean);
    }

    public Map<String, Set<String>> getExistedTableMap() {
        return existedTableMap;
    }

    /**
     * 
     * DOC ggu Comment method "importedRecords".
     * 
     * @param bean
     */
    public void importedRecords(TablesForDelimitedBean bean) {
        addRecords(ProcessType.IMPORT, bean);
    }

    private void addRecords(ProcessType pType, TablesForDelimitedBean bean) {
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
        case EXISTEDTABLE:
            Set<String> tableSet = existedTableMap.get(bean.getName());
            if (tableSet == null) {
                tableSet = new HashSet<String>();
                existedTableMap.put(bean.getName(), tableSet);
            }
            tableSet.add(bean.getTableName());

            break;
        case TYPE_NOT_MAPPING:
            if (nodeType == ERepositoryObjectType.METADATA_CONNECTIONS) {
                DBTableForDelimitedBean theBean = (DBTableForDelimitedBean) bean;
                if (!isValid(theBean.getTalendType())) {
                    return;
                }
                TalendTypeMappingBean typeBean = new TalendTypeMappingBean(theBean.getName(), theBean.getTableName(), theBean
                        .getLabel(), theBean.getTalendType(), theBean.getDbType());
                dbTypeNotMappingList.add(typeBean);
            }
            break;
        default:
        }

    }

    private void addTable(ProcessType pType, TablesForDelimitedBean bean) {
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

    private void addField(ProcessType pType, TablesForDelimitedBean bean) {
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
    public void rejectRecords(TablesForDelimitedBean bean) {
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

    private boolean checkBean(TablesForDelimitedBean bean) {
        if (!isValid(bean.getName())) {
            return false;
        }

        if (!isValid(bean.getLabel())) {
            return false;
        }

        if (!isValid(bean.getTableName())) {
            return false;
        }

        return true;
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
