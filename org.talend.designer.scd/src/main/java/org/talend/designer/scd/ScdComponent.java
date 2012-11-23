// ============================================================================
//
// Copyright (C) 2006-2012 Talend Inc. - www.talend.com
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
import java.util.List;
import java.util.Map;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.talend.core.model.components.IODataComponent;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.MetadataTable;
import org.talend.core.model.process.AbstractExternalNode;
import org.talend.core.model.process.IComponentDocumentation;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.IExternalData;
import org.talend.core.model.process.IExternalNode;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.IProcess;
import org.talend.designer.scd.ui.ScdUI;

/**
 * DOC hcw class global comment. Detailled comment
 */
public class ScdComponent extends AbstractExternalNode {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.AbstractExternalNode#renameMetadataColumnName(java.lang.String,
     * java.lang.String, java.lang.String)
     */
    @Override
    protected void renameMetadataColumnName(String conectionName, String oldColumnName, String newColumnName) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IExternalNode#getComponentDocumentation(java.lang.String, java.lang.String)
     */
    public IComponentDocumentation getComponentDocumentation(String componentName, String tempFolderPath) {
        // ScdComponentDocumentation componentDocumentation = new ScdComponentDocumentation();
        // componentDocumentation.setComponentName(componentName);
        // componentDocumentation.setTempFolderPath(tempFolderPath);
        // componentDocumentation.setPreviewPicPath(HTMLDocUtils.getPreviewPicPath(this));
        //
        // return componentDocumentation;
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IExternalNode#getExternalData()
     */
    @Override
    public IExternalData getExternalData() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IExternalNode#initialize()
     */
    public void initialize() {
        // special handle scd's version columns ,set custom to be true
        List<Object> listNames = getVertionColumnNames();
        if (listNames.isEmpty()) {
            return;
        }
        int customId = 0;
        List<IMetadataTable> listTables = getScdNodeMetadataList();
        for (IMetadataTable table : listTables) {
            if (table instanceof MetadataTable) {
                MetadataTable metable = (MetadataTable) table;
                List<IMetadataColumn> metColumns = metable.getListColumns();
                for (IMetadataColumn column : metColumns) {
                    if (listNames.contains(column.getLabel())) {
                        column.setCustomId(customId);
                        column.setCustom(true);
                        column.setReadOnly(true);
                        customId++;
                    }
                }
            }
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IExternalNode#open(org.eclipse.swt.widgets.Display)
     */
    public int open(Display display) {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IExternalNode#open(org.eclipse.swt.widgets.Composite)
     */
    public int open(Composite parent) {
        ScdUI ui = new ScdUI();
        ScdManager manager = new ScdManager(this);
        ui.createUI(parent, manager);
        return manager.getDialogResponse();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IExternalNode#renameInputConnection(java.lang.String, java.lang.String)
     */
    public void renameInputConnection(String oldName, String newName) {
        // TODO Auto-generated method stub
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IExternalNode#renameOutputConnection(java.lang.String, java.lang.String)
     */
    public void renameOutputConnection(String oldName, String newName) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IExternalNode#setExternalData(org.talend.core.model.process.IExternalData)
     */
    public void setExternalData(IExternalData persistentData) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IExternalNode#getTMapExternalData()
     */
    public IExternalData getTMapExternalData() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void metadataInputChanged(IODataComponent dataComponent, String connectionToApply) {
        // when delete columns,synchronization filed type
        IMetadataTable metadataTable = dataComponent.getConnMetadataTable();
        List<IMetadataColumn> listColumns = metadataTable.getListColumns();
        List<String> listNames = new ArrayList<String>();
        for (IMetadataColumn meta : listColumns) {
            listNames.add(meta.getLabel());
        }
        List<? extends IElementParameter> list = this.getElementParameters();
        // update sorcekeys
        IElementParameter param = getElementParameter(ScdParameterConstants.SOURCE_KEYS_PARAM_NAME);
        List<Map<String, String>> sorcevalues = (List<Map<String, String>>) param.getValue();
        updateFileds(listNames, list, sorcevalues, param, null);

        // update zero filed type
        IElementParameter useL0 = getElementParameter(ScdParameterConstants.USE_L0);
        if (useL0 != null && useL0.getValue().equals(Boolean.TRUE)) {
            IElementParameter l0FieldsParam = getElementParameter(ScdParameterConstants.L0_FIELDS_PARAM_NAME);
            List<Map<String, String>> values = (List<Map<String, String>>) l0FieldsParam.getValue();
            updateFileds(listNames, list, values, l0FieldsParam, useL0);
        }
        // update one filed type
        IElementParameter useL1 = getElementParameter(ScdParameterConstants.USE_L1);
        if (useL1 != null && useL1.getValue().equals(Boolean.TRUE)) {
            IElementParameter l1FieldsParam = getElementParameter(ScdParameterConstants.L1_FIELDS_PARAM_NAME);
            List<Map<String, String>> values = (List<Map<String, String>>) l1FieldsParam.getValue();
            updateFileds(listNames, list, values, l1FieldsParam, useL1);
        }
        // update two filed type
        IElementParameter useL2 = getElementParameter(ScdParameterConstants.USE_L2);
        if (useL2 != null && useL2.getValue().equals(Boolean.TRUE)) {
            IElementParameter l2FieldsParam = getElementParameter(ScdParameterConstants.L2_FIELDS_PARAM_NAME);
            List<Map<String, String>> values = (List<Map<String, String>>) l2FieldsParam.getValue();
            updateFileds(listNames, list, values, l2FieldsParam, useL2);
            // update ListColumns
            if (useL2.getValue().equals(Boolean.FALSE)) {
                updateScdNodeColumn(sorcevalues);
            }
        }
        // update three filed type
        IElementParameter useL3 = getElementParameter(ScdParameterConstants.USE_L3);
        if (useL3 != null && useL3.getValue().equals(Boolean.TRUE)) {
            IElementParameter l3FieldsParam = getElementParameter(ScdParameterConstants.L3_FIELDS_PARAM_NAME);
            List<Map<String, String>> values = (List<Map<String, String>>) l3FieldsParam.getValue();
            updateFileds(listNames, list, values, l3FieldsParam, useL3);
        }

    }

    private void updateScdNodeColumn(List<Map<String, String>> sorcevalues) {
        List<Object> versionColumns = getVertionColumnNames();
        IElementParameter sourceKey = getElementParameter(ScdParameterConstants.SURROGATE_KEY);
        versionColumns.remove(sourceKey.getValue());
        List<IMetadataColumn> needRemoveColumns = new ArrayList<IMetadataColumn>();
        List<IMetadataTable> listTables = getScdNodeMetadataList();
        List<IMetadataColumn> needUpdataColumn = null;
        for (IMetadataTable table : listTables) {
            if (table instanceof MetadataTable) {
                MetadataTable metable = (MetadataTable) table;
                List<IMetadataColumn> metColumns = metable.getListColumns();
                for (IMetadataColumn column : metColumns) {
                    if (versionColumns.contains(column.getLabel())) {
                        needRemoveColumns.add(column);
                        if (needUpdataColumn == null) {
                            needUpdataColumn = metColumns;
                        }
                    }
                }
            }
        }
        if (needUpdataColumn != null) {
            needUpdataColumn.removeAll(needRemoveColumns);
        }
    }

    private void updateFileds(List<String> listNames, List<? extends IElementParameter> list, List<Map<String, String>> values,
            IElementParameter parameter, IElementParameter parameter2) {
        if (list == null || values == null || parameter == null) {
            return;
        }
        List<Map<String, String>> needRemoves = new ArrayList<Map<String, String>>();
        for (Map<String, String> entry : values) {
            for (String value : entry.values()) {
                if (value != null) {
                    if (!listNames.contains(value)) {
                        Object obj = list.get(list.indexOf(parameter)).getValue();
                        if (obj != null && obj instanceof List) {
                            List<Map<String, String>> values2 = (List<Map<String, String>>) obj;
                            for (Map<String, String> entry2 : values2) {
                                for (String value2 : entry2.values()) {
                                    if (value2.equals(value)) {
                                        needRemoves.add(entry2);
                                    }
                                }
                            }
                        }
                    }
                }
                break;
            }
        }
        for (Map<String, String> entry : needRemoves) {
            values.remove(entry);
        }
        if (parameter2 != null && values != null && values.isEmpty()) {
            parameter2.setValue(false);
        }
    }

    private List<IMetadataTable> getScdNodeMetadataList() {
        List<IMetadataTable> metadataTables = new ArrayList<IMetadataTable>();
        IProcess process = this.getProcess();
        List<? extends INode> nodeList = process.getGraphicalNodes();
        for (INode node : nodeList) {
            IExternalNode component = node.getExternalNode();
            if (component != null && component instanceof ScdComponent && component.equals(this)) {
                metadataTables = node.getMetadataList();
                break;
            }
        }
        return metadataTables;
    }

    private List<Object> getVertionColumnNames() {
        IElementParameter sourceKey = getElementParameter(ScdParameterConstants.SURROGATE_KEY);
        IElementParameter startData = getElementParameter(ScdParameterConstants.L2_STARTDATE_FIELD);
        IElementParameter endData = getElementParameter(ScdParameterConstants.L2_ENDDATE_FIELD);
        IElementParameter version = getElementParameter(ScdParameterConstants.L2_VERSION_FIELD);
        IElementParameter active = getElementParameter(ScdParameterConstants.L2_ACTIVE_FIELD);
        List<Object> list = new ArrayList<Object>();
        if (sourceKey != null && startData != null && endData != null && version != null && active != null) {
            if (!("").equals(sourceKey.getValue())) {
                list.add(sourceKey.getValue());
            }
            if (!("").equals(startData.getValue())) {
                list.add(startData.getValue());
            }
            if (!("").equals(endData.getValue())) {
                list.add(endData.getValue());
            }
            if (!("").equals(version.getValue())) {
                list.add(version.getValue());
            }
            if (!("").equals(active.getValue())) {
                list.add(active.getValue());
            }
        }
        return list;
    }

}
