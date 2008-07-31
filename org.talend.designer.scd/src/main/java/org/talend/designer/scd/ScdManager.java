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
package org.talend.designer.scd;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.process.IElementParameter;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.scd.model.SurrogateCreationType;
import org.talend.designer.scd.model.SurrogateKey;
import org.talend.designer.scd.model.VersionEndType;
import org.talend.designer.scd.model.VersionStartType;
import org.talend.designer.scd.model.Versioning;

/**
 * Manage the state of ui input and element parameters.
 */
public class ScdManager {

    private ScdComponent component;

    private int dialogResponse;

    private Map<String, IElementParameter> paramsMap;

    private List<String> sourceKeys;

    private List<SurrogateKey> surrogateKeys;

    private List<String> type0Table;

    private List<String> type1Table;

    private List<String> type2Table;

    private Versioning versionData;

    private Map<String, String> type3Table;

    /**
     * DOC hcw ScdManager constructor comment.
     * 
     * @param scdComponent
     */
    public ScdManager(ScdComponent scdComponent) {
        component = scdComponent;

    }

    public List<String> getUnusedFields() {
        List<String> unused = new ArrayList<String>();
        List<IMetadataColumn> inputSchema = getMetadataColumn(component);
        if (inputSchema != null) {
            for (IMetadataColumn col : inputSchema) {
                unused.add(col.getLabel());
            }
        }

        return removeAll(unused, getUsedColumns());
    }

    /**
     * DOC hcw Comment method "getMetadataColumn".
     * 
     * @param component2
     * @return
     */
    private List<IMetadataColumn> getMetadataColumn(ScdComponent component) {

        List<IMetadataTable> metaDataList = component.getMetadataList();
        List<IMetadataColumn> columns = new ArrayList<IMetadataColumn>();
        if (metaDataList != null) {
            for (IMetadataTable table : metaDataList) {
                columns.addAll(table.getListColumns());
            }
        }
        return columns;
    }

    /**
     * DOC hcw Comment method "removeAll".
     * 
     * @param unused
     * @param usedColumns
     * @return
     */
    private List<String> removeAll(List<String> unused, List<String> usedColumns) {
        List<String> result = new ArrayList<String>(unused.size());

        Map<String, Boolean> lookup = new HashMap<String, Boolean>();
        for (String col : usedColumns) {
            lookup.put(col, Boolean.TRUE);
        }

        for (String field : unused) {
            if (lookup.get(field) == null) {
                result.add(field);
            }
        }
        return result;
    }

    // public List<IMetadataColumn> getInputSchema(INode node) {
    // List<IMetadataColumn> inputSchema = null;
    // List<? extends IConnection> incomingConnections = node.getIncomingConnections();
    // if (incomingConnections != null && incomingConnections.size() > 0) {
    // for (IConnection incomingConnection : incomingConnections) {
    // if (incomingConnection.getLineStyle().hasConnectionCategory(IConnectionCategory.DATA)) {
    // IMetadataTable schemaTable = incomingConnection.getMetadataTable();
    // if (schemaTable != null) {
    // inputSchema = schemaTable.getListColumns();
    // }
    // }
    // }
    // }
    // return inputSchema;
    // }

    public int getDialogResponse() {
        return dialogResponse;
    }

    public void setDialogResponse(int dialogResponse) {
        this.dialogResponse = dialogResponse;
    }

    // @SuppressWarnings("unchecked")
    // public IElementParameter createParameter(String name, EParameterFieldType field, Object value) {
    // IElementParameter param = new ElementParameter(null);
    // param.setField(field);
    // param.setCategory(EComponentCategory.BASIC);
    // param.setValue(value);
    // param.setName(name);
    // param.setShow(false);
    // param.setNumRow(1);
    // List list = component.getElementParameters();
    // list.add(param);
    // return param;
    // }

    public void restoreUIData() {
        List<? extends IElementParameter> list = component.getElementParameters();
        initParamsMap(list);

        reloadSurrogateKeyParameter();
        reloadSourceKeyParameter();
        reloadType0Parameter();
        reloadType1Parameter();
        reloadType2Parameter();
        reloadType3Parameter();
    }

    /**
     * DOC hcw Comment method "getUsedColumn".
     * 
     * @return
     */
    private List<String> getUsedColumns() {
        List<String> usedColumns = new ArrayList<String>();
        addAll(usedColumns, sourceKeys, type0Table, type1Table, type2Table);

        usedColumns.addAll(getUsedColumns(versionData));
        usedColumns.addAll(getUsedColumns(type3Table));
        usedColumns.addAll(getUsedColumns(surrogateKeys));
        return usedColumns;
    }

    private void addAll(List<String> usedColumns, List<String>... tables) {
        for (List<String> table : tables) {
            if (table != null) {
                usedColumns.addAll(table);
            }
        }
    }

    /**
     * DOC hcw Comment method "getUsedColumns".
     * 
     * @param surrogateKeys2
     * @return
     */
    private Collection<? extends String> getUsedColumns(List<SurrogateKey> keys) {
        if (keys == null) {
            return Collections.EMPTY_LIST;
        } else {
            List<String> columns = new ArrayList<String>();
            for (SurrogateKey key : keys) {
                if (StringUtils.isNotEmpty(key.getColumn())) {
                    columns.add(key.getColumn());
                }
            }
            return columns;
        }
    }

    /**
     * DOC hcw Comment method "getUsedColumns".
     * 
     * @param type3Table2
     * @return
     */
    private Collection<? extends String> getUsedColumns(Map<String, String> type3) {
        if (type3 == null) {
            return Collections.EMPTY_LIST;
        } else {
            List<String> columns = new ArrayList<String>();
            columns.addAll(type3.keySet());
            columns.addAll(type3.values());
            return columns;
        }
    }

    /**
     * DOC hcw Comment method "getUsedColumns".
     * 
     * @param versionData2
     * @return
     */
    private Collection<? extends String> getUsedColumns(Versioning ver) {
        if (ver == null) {
            return Collections.EMPTY_LIST;
        } else {
            List<String> columns = new ArrayList<String>();
            columns.add(ver.getStartName());
            columns.add(ver.getEndName());
            if (ver.getStartType() == VersionStartType.INPUT_FIELD) {
                columns.add(ver.getStartComplement());
            }
            if (ver.isActiveChecked()) {
                columns.add(ver.getActiveName());
            }
            if (ver.isVersionChecked()) {
                columns.add(ver.getVersionName());
            }
            return columns;
        }
    }

    public void reloadSourceKeyParameter() {
        IElementParameter param = paramsMap.get(ScdParameterConstants.SOURCE_KEYS_PARAM_NAME);
        List<Map<String, String>> values = (List<Map<String, String>>) param.getValue();
        sourceKeys = convertTableParameterValue(values);
    }

    /**
     * DOC hcw Comment method "convertParameterValue".
     * 
     * @param values
     * @return
     */
    private List<String> convertTableParameterValue(List<Map<String, String>> values) {
        List<String> columns = new ArrayList<String>();
        for (Map<String, String> entry : values) {
            for (String value : entry.values()) {
                if (value != null) {
                    columns.add(value);
                }
            }
        }
        return columns;
    }

    public void saveUIData(List<String> sourceKeys, List<SurrogateKey> surrogateKeys, List<String> type0Table,
            List<String> type1Table, List<String> type2Table, Versioning versionData, Map<String, String> type3Table) {
        this.sourceKeys = sourceKeys;
        this.surrogateKeys = surrogateKeys;
        this.type0Table = type0Table;
        this.type1Table = type1Table;
        this.type2Table = type2Table;
        this.versionData = versionData;
        this.type3Table = type3Table;
    }

    public void updateElementParameters() {
        List<? extends IElementParameter> list = component.getElementParameters();
        initParamsMap(list);
        updateSourceKeyParameter(sourceKeys);
        // java component only support one key now
        if (surrogateKeys != null && surrogateKeys.size() > 0) {
            updateSurrogateKeyParameter(surrogateKeys.get(0));
        }
        updateType0Parameter(type0Table);
        updateType1Parameter(type1Table);
        updateType2Parameter(type2Table, versionData);
        updateType3Parameter(type3Table);
        setUpdateComponent(list);
    }

    /**
     * DOC hcw Comment method "initParamsMap".
     * 
     * @param list
     */
    private void initParamsMap(List<? extends IElementParameter> list) {
        paramsMap = new HashMap<String, IElementParameter>();
        for (IElementParameter param : list) {
            paramsMap.put(param.getName(), param);
        }
    }

    private void setUpdateComponent(List<? extends IElementParameter> list) {
        // Updates component parameter
        for (IElementParameter elementParameter : list) {
            if (EParameterName.UPDATE_COMPONENTS.getName().equals(elementParameter.getName())) {
                elementParameter.setValue(Boolean.TRUE);
            }
        }
    }

    // update parameters
    public void updateSourceKeyParameter(List<String> keys) {
        IElementParameter param = paramsMap.get(ScdParameterConstants.SOURCE_KEYS_PARAM_NAME);
        List<Map<String, String>> values = createTableFieldValues(ScdParameterConstants.SOURCE_KEYS_ITEM_NAME, keys);
        param.setValue(values);
    }

    /**
     * DOC hcw Comment method "createTableFieldValues".
     * 
     * @param itemName
     * @param keys
     * @return
     */
    private List<Map<String, String>> createTableFieldValues(String itemName, List<String> keys) {
        List<Map<String, String>> table = new ArrayList<Map<String, String>>();
        if (keys != null) {
            for (String key : keys) {
                Map<String, String> row = new HashMap<String, String>();
                row.put(itemName, key);
                table.add(row);
            }
        }
        return table;
    }

    public void reloadType0Parameter() {
        IElementParameter useL0 = paramsMap.get(ScdParameterConstants.USE_L0);
        if (useL0 != null && useL0.getValue().equals(Boolean.TRUE)) {
            IElementParameter l0FieldsParam = paramsMap.get(ScdParameterConstants.L0_FIELDS_PARAM_NAME);
            List<Map<String, String>> values = (List<Map<String, String>>) l0FieldsParam.getValue();
            type0Table = convertTableParameterValue(values);
        }
    }

    public void reloadType1Parameter() {
        IElementParameter useL1 = paramsMap.get(ScdParameterConstants.USE_L1);
        if (useL1 != null && useL1.getValue().equals(Boolean.TRUE)) {
            IElementParameter l1FieldsParam = paramsMap.get(ScdParameterConstants.L1_FIELDS_PARAM_NAME);
            List<Map<String, String>> values = (List<Map<String, String>>) l1FieldsParam.getValue();
            type1Table = convertTableParameterValue(values);
        }
    }

    public void reloadType2Parameter() {
        IElementParameter useL2 = paramsMap.get(ScdParameterConstants.USE_L2);
        if (useL2 != null && useL2.getValue().equals(Boolean.TRUE)) {
            versionData = new Versioning();
            IElementParameter l2FieldsParam = paramsMap.get(ScdParameterConstants.L2_FIELDS_PARAM_NAME);
            List<Map<String, String>> values = (List<Map<String, String>>) l2FieldsParam.getValue();
            type2Table = convertTableParameterValue(values);

            // start date
            versionData.setStartName(getStringParameter(ScdParameterConstants.L2_STARTDATE_FIELD));

            IElementParameter param = paramsMap.get(ScdParameterConstants.L2_STARTDATE_VALUE);
            if (param != null) {
                versionData.setStartType(VersionStartType.getTypeByValue((String) param.getValue()));

                if (versionData.getStartType() == VersionStartType.INPUT_FIELD) {
                    versionData.setStartComplement(getStringParameter(ScdParameterConstants.L2_STARTDATE_INPUT_FIELD));
                }
            }

            // end date
            versionData.setEndName(getStringParameter(ScdParameterConstants.L2_ENDDATE_FIELD));

            param = paramsMap.get(ScdParameterConstants.L2_ENDDATE_VALUE);
            if (param != null) {
                versionData.setEndType(VersionEndType.getTypeByValue((String) param.getValue()));

                if (versionData.getEndType() == VersionEndType.FIXED_YEAR) {
                    versionData.setEndComplement(getStringParameter(ScdParameterConstants.L2_ENDDATE_FIXED_VALUE));
                }
            }

            // version
            versionData.setVersionChecked(getBooleanParameter(ScdParameterConstants.USE_L2_VERSION));
            if (versionData.isVersionChecked()) {
                versionData.setVersionName(getStringParameter(ScdParameterConstants.L2_VERSION_FIELD));
            }

            // activate
            versionData.setActiveChecked(getBooleanParameter(ScdParameterConstants.USE_L2_ACTIVE));
            if (versionData.isVersionChecked()) {
                versionData.setActiveName(getStringParameter(ScdParameterConstants.L2_ACTIVE_FIELD));
            }

        }
    }

    public void reloadType3Parameter() {
        IElementParameter useL3 = paramsMap.get(ScdParameterConstants.USE_L3);
        if (useL3 != null && useL3.getValue().equals(Boolean.TRUE)) {
            IElementParameter l3FieldsParam = paramsMap.get(ScdParameterConstants.L3_FIELDS_PARAM_NAME);

            type3Table = new HashMap<String, String>();
            List<Map<String, String>> values = (List<Map<String, String>>) l3FieldsParam.getValue();
            for (Map<String, String> entry : values) {
                String current = entry.get(ScdParameterConstants.L3_ITEM_CURRENT_VALUE);
                String previous = entry.get(ScdParameterConstants.L3_ITEM_PREV_VALUE);

                type3Table.put(current, previous);
            }
        }

    }

    /**
     * DOC hcw Comment method "reloadSurrogateKeyParameter".
     */
    private void reloadSurrogateKeyParameter() {
        surrogateKeys = new ArrayList<SurrogateKey>();
        SurrogateKey key = new SurrogateKey();
        surrogateKeys.add(key);

        key.setColumn(getStringParameter(ScdParameterConstants.SURROGATE_KEY));
        key.setCreation(SurrogateCreationType.getTypeByValue(getStringParameter(ScdParameterConstants.SK_CREATION)));

        if (key.getCreation() == SurrogateCreationType.INPUT_FIELD) {
            key.setComplement(getStringParameter(ScdParameterConstants.SK_INPUT_FIELD));
        } else if (key.getCreation() == SurrogateCreationType.ROUTINE) {
            key.setComplement(getStringParameter(ScdParameterConstants.SK_ROUTINE));
        }
    }

    private String getStringParameter(String name) {
        IElementParameter param = paramsMap.get(name);
        if (param == null) {
            return null;
        } else {
            return (String) param.getValue();
        }
    }

    private boolean getBooleanParameter(String name) {
        IElementParameter param = paramsMap.get(name);
        return ((Boolean) param.getValue()).booleanValue();
    }

    public void updateType0Parameter(List<String> keys) {
        IElementParameter useL0 = paramsMap.get(ScdParameterConstants.USE_L0);
        if (useL0 == null) {
            return;
        }
        IElementParameter l0FieldsParam = paramsMap.get(ScdParameterConstants.L0_FIELDS_PARAM_NAME);
        if (keys == null || keys.size() == 0) {
            useL0.setValue(Boolean.FALSE);
            l0FieldsParam.setValue(null);
        } else {
            useL0.setValue(Boolean.TRUE);
            List<Map<String, String>> values = createTableFieldValues(ScdParameterConstants.L0_FIELDS_ITEM_NAME, keys);
            l0FieldsParam.setValue(values);
        }
    }

    public void updateType1Parameter(List<String> keys) {
        IElementParameter useL1 = paramsMap.get(ScdParameterConstants.USE_L1);
        IElementParameter l1FieldsParam = paramsMap.get(ScdParameterConstants.L1_FIELDS_PARAM_NAME);
        if (keys == null || keys.size() == 0) {
            useL1.setValue(Boolean.FALSE);
            l1FieldsParam.setValue(null);
        } else {
            useL1.setValue(Boolean.TRUE);
            List<Map<String, String>> values = createTableFieldValues(ScdParameterConstants.L1_FIELDS_ITEM_NAME, keys);
            l1FieldsParam.setValue(values);
        }
    }

    public void updateType2Parameter(List<String> keys, Versioning ver) {
        IElementParameter useL2 = paramsMap.get(ScdParameterConstants.USE_L2);
        IElementParameter l2FieldsParam = paramsMap.get(ScdParameterConstants.L2_FIELDS_PARAM_NAME);
        if (keys == null || keys.size() == 0) {
            useL2.setValue(Boolean.FALSE);
            l2FieldsParam.setValue(null);
        } else {
            useL2.setValue(Boolean.TRUE);
            List<Map<String, String>> values = createTableFieldValues(ScdParameterConstants.L1_FIELDS_ITEM_NAME, keys);
            l2FieldsParam.setValue(values);

            // start date
            updateElementParameter(ScdParameterConstants.L2_STARTDATE_FIELD, ver.getStartName());

            updateElementParameter(ScdParameterConstants.L2_STARTDATE_VALUE, ver.getStartType().getValue());

            if (ver.getStartType() == VersionStartType.INPUT_FIELD) {
                updateElementParameter(ScdParameterConstants.L2_STARTDATE_INPUT_FIELD, ver.getStartComplement());
            }

            // end date
            updateElementParameter(ScdParameterConstants.L2_ENDDATE_FIELD, ver.getEndName());

            updateElementParameter(ScdParameterConstants.L2_ENDDATE_VALUE, ver.getEndType().getValue());

            if (ver.getEndType() == VersionEndType.FIXED_YEAR) {
                updateElementParameter(ScdParameterConstants.L2_ENDDATE_FIXED_VALUE, ver.getEndComplement());
            }

            // version
            updateElementParameter(ScdParameterConstants.USE_L2_VERSION, ver.isVersionChecked());

            if (ver.isVersionChecked()) {
                updateElementParameter(ScdParameterConstants.L2_VERSION_FIELD, ver.getVersionName());
            }

            // activate
            updateElementParameter(ScdParameterConstants.USE_L2_ACTIVE, Boolean.valueOf(ver.isActiveChecked()));

            if (ver.isActiveChecked()) {
                updateElementParameter(ScdParameterConstants.L2_ACTIVE_FIELD, ver.getActiveName());
            }
        }
    }

    public void updateType3Parameter(Map<String, String> model) {
        IElementParameter useL3 = paramsMap.get(ScdParameterConstants.USE_L3);
        if (useL3 == null) {
            return;
        }
        if (model == null || model.size() == 0) {
            useL3.setValue(Boolean.FALSE);
        } else {
            useL3.setValue(Boolean.TRUE);

            List<Map<String, String>> table = new ArrayList<Map<String, String>>();
            for (String current : model.keySet()) {
                String previous = model.get(current);
                Map<String, String> row = new HashMap<String, String>();
                row.put(ScdParameterConstants.L3_ITEM_CURRENT_VALUE, current);
                row.put(ScdParameterConstants.L3_ITEM_PREV_VALUE, previous);
                table.add(row);
            }

            updateElementParameter(ScdParameterConstants.L3_FIELDS_PARAM_NAME, table);
        }
    }

    public void updateSurrogateKeyParameter(SurrogateKey key) {
        if (key == null) {
            return;
        }
        updateElementParameter(ScdParameterConstants.SURROGATE_KEY, key.getColumn());

        updateElementParameter(ScdParameterConstants.SK_CREATION, key.getCreation().getValue());

        if (key.getCreation() == SurrogateCreationType.INPUT_FIELD) {
            updateElementParameter(ScdParameterConstants.SK_INPUT_FIELD, key.getComplement());
        } else if (key.getCreation() == SurrogateCreationType.ROUTINE) {
            updateElementParameter(ScdParameterConstants.SK_ROUTINE, key.getComplement());
        }

    }

    public void updateElementParameter(String name, Object value) {
        IElementParameter param = paramsMap.get(name);
        if (param != null) {
            param.setValue(value);
        }
    }

    public List<String> getSourceKeys() {
        return sourceKeys;
    }

    public List<SurrogateKey> getSurrogateKeys() {
        return surrogateKeys;
    }

    public List<String> getType0Table() {
        return type0Table;
    }

    public List<String> getType1Table() {
        return type1Table;
    }

    public List<String> getType2Table() {
        return type2Table;
    }

    public Versioning getVersionData() {
        return versionData;
    }

    public Map<String, String> getType3Table() {
        return type3Table;
    }

}
