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
package org.talend.designer.filemultischemas.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.gef.commands.Command;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.MetadataTable;
import org.talend.core.model.metadata.MetadataTool;
import org.talend.core.model.metadata.MultiSchemasUtil;
import org.talend.core.model.metadata.builder.connection.DelimitedFileConnection;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.utils.TalendTextUtils;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.filemultischemas.data.IMultiSchemaConstant;
import org.talend.designer.filemultischemas.data.MultiMetadataColumn;
import org.talend.designer.filemultischemas.data.SchemasKeyData;

/**
 * cLi class global comment. Detailled comment
 */
public class ChangeMultiSchemasCommand extends Command {

    private INode node;

    private SchemasKeyData rootSchemaData;

    private Object oldFilePath, newFilePath;

    private Object oldRowSeperator, newRowSeperator;

    private Object oldFieldSeperator, newFieldSeperator;

    private Object oldEncoding, newEncoding;

    private List<Map<String, String>> oldSchemasListMap, newSchemasListMap;

    private List<IMetadataTable> oldMetadataTable, newMetadataTable;

    private DelimitedFileConnection fakeConnection;

    public ChangeMultiSchemasCommand(INode node, SchemasKeyData rootSchemaData, DelimitedFileConnection fakeConnection) {
        super();
        this.node = node;
        this.rootSchemaData = rootSchemaData;
        this.fakeConnection = fakeConnection;
        initNew();
        initOld();
    }

    private void initNew() {
        this.newFieldSeperator = fakeConnection.getFieldSeparatorValue();
        this.newRowSeperator = fakeConnection.getRowSeparatorValue();
        this.newEncoding = fakeConnection.getEncoding();
        this.newFilePath = fakeConnection.getFilePath();
        // map
        this.newSchemasListMap = new ArrayList<Map<String, String>>();
        // metadata table
        this.newMetadataTable = new ArrayList<IMetadataTable>();
        addSchemasMap(newSchemasListMap, newMetadataTable, this.rootSchemaData);

    }

    private void addSchemasMap(List<Map<String, String>> newValueList, List<IMetadataTable> newTables, SchemasKeyData keyData) {
        if (keyData == null) {
            return;
        }
        if (keyData.getParent() != null) { // not root
            //
            Map<String, String> map = new HashMap<String, String>();
            final String key = keyData.getRecordType();
            map.put(IMultiSchemaConstant.SCHEMA, key);
            map.put(IMultiSchemaConstant.RECORD, TalendTextUtils.addQuotes(key));
            map.put(IMultiSchemaConstant.PARENT_RECORD, TalendTextUtils.addQuotes(keyData.getParent().getRecordType()));
            map.put(IMultiSchemaConstant.CARDINALITY, keyData.getCard());
            newValueList.add(map);
            //
            String connectionBaseName = MetadataTool.validateColumnName(MultiSchemasUtil.getConnectionBaseName(key));
            String uniqueConnName = node.getProcess().generateUniqueConnectionName(connectionBaseName);
            MetadataTable table = new MetadataTable();
            table.setLabel(key);
            table.setTableName(uniqueConnName);

            String keyIndex = ""; //$NON-NLS-1$
            boolean first = true;
            List<MultiMetadataColumn> metadataColumnsInModel = keyData.getMetadataColumnsInModel();

            for (int i = 0; i < metadataColumnsInModel.size(); i++) {
                MultiMetadataColumn column = metadataColumnsInModel.get(i);
                table.getListColumns().add(column.clone(true));
                if (column.isKey()) {
                    if (first) {
                        keyIndex += i;
                    } else {
                        keyIndex += "," + i; //$NON-NLS-1$
                    }
                    first = false;
                }
            }
            map.put(IMultiSchemaConstant.KEY_COLUMN_INDEX, TalendTextUtils.addQuotes(keyIndex));
            newTables.add(table);

        }
        for (SchemasKeyData child : keyData.getChildren()) {
            addSchemasMap(newValueList, newTables, child);
        }
    }

    private void initOld() {
        if (node == null) {
            return;
        }
        IElementParameter elementParameter = this.node.getElementParameter(EParameterName.FILENAME.getName());
        if (elementParameter != null) {
            this.oldFilePath = elementParameter.getValue();
        }
        elementParameter = this.node.getElementParameter(EParameterName.ENCODING_TYPE.getName());
        if (elementParameter != null) {
            this.oldEncoding = elementParameter.getValue();
        }
        elementParameter = this.node.getElementParameter(EParameterName.ROWSEPARATOR.getName());
        if (elementParameter != null) {
            this.oldRowSeperator = elementParameter.getValue();
        }
        elementParameter = this.node.getElementParameter(EParameterName.FIELDSEPARATOR.getName());
        if (elementParameter != null) {
            this.oldFieldSeperator = elementParameter.getValue();
        }
        // schema table
        elementParameter = this.node.getElementParameter(EParameterName.SCHEMAS.getName());
        if (elementParameter != null) {
            Object value = elementParameter.getValue();
            List<Map<String, String>> oldvalueList = new ArrayList<Map<String, String>>();

            if (value != null) {
                List<Map<String, String>> valueList = (List<Map<String, String>>) value;
                for (Map<String, String> map : valueList) {
                    Map<String, String> valueMap = new HashMap<String, String>();
                    for (String key : map.keySet()) {
                        valueMap.put(key, map.get(key));
                    }
                    oldvalueList.add(valueMap);
                }
            }
            this.oldSchemasListMap = oldvalueList;
        }
        // metadata
        List<IMetadataTable> oldMetadataTable = new ArrayList<IMetadataTable>();
        for (IMetadataTable table : this.node.getMetadataList()) {
            oldMetadataTable.add(table.clone(true));
        }
        this.oldMetadataTable = oldMetadataTable;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.commands.Command#execute()
     */
    @Override
    public void execute() {
        if (node == null || rootSchemaData == null) {
            return;
        }
        //
        setParameterValues(false);

    }

    private void setParameterValues(boolean undo) {
        IElementParameter elementParameter = null;

        elementParameter = this.node.getElementParameter(EParameterName.FILENAME.getName());
        if (elementParameter != null) {
            Object value = null;
            if (undo) {
                value = this.oldFilePath;
            } else {
                value = this.newFilePath;
            }
            elementParameter.setValue(value);
        }
        elementParameter = this.node.getElementParameter(EParameterName.ENCODING_TYPE.getName());
        if (elementParameter != null) {
            Object value = null;
            if (undo) {
                value = this.oldEncoding;
            } else {
                value = this.newEncoding;
            }
            elementParameter.setValue(value);
        }
        elementParameter = this.node.getElementParameter(EParameterName.ROWSEPARATOR.getName());
        if (elementParameter != null) {
            Object value = null;
            if (undo) {
                value = this.oldRowSeperator;
            } else {
                value = this.newRowSeperator;
            }
            elementParameter.setValue(value);
        }

        elementParameter = this.node.getElementParameter(EParameterName.FIELDSEPARATOR.getName());
        if (elementParameter != null) {
            Object value = null;
            if (undo) {
                value = this.oldFieldSeperator;
            } else {
                value = this.newFieldSeperator;
            }
            elementParameter.setValue(value);
        }
        // PTODO need check again.
        elementParameter = this.node.getElementParameter(EParameterName.SCHEMAS.getName());

        if (elementParameter != null) {
            List<Map<String, String>> value = null;
            if (undo) {
                value = this.oldSchemasListMap;
            } else {
                value = this.newSchemasListMap;
            }
            elementParameter.setValue(value);
        }

        // PTODO need check again.

        if (undo) {
            if (this.oldMetadataTable != null) {
                addMetadataTable(this.oldMetadataTable);
            }
        } else {
            if (this.newMetadataTable != null) {
                addMetadataTable(this.newMetadataTable);
            }
        }
    }

    private void addMetadataTable(List<IMetadataTable> newTables) {
        // remove
        IProcess process = this.node.getProcess();

        List<IMetadataTable> needAddedTables = new ArrayList<IMetadataTable>();
        List<String> schemaNames = new ArrayList<String>();

        // record added table, and update the existed table.
        for (IMetadataTable table : newTables) {
            final String tableLabel = table.getLabel();
            schemaNames.add(tableLabel);

            IMetadataTable mappingTable = MetadataTool.getMetadataTableFromNode(node, tableLabel);
            if (mappingTable == null) { // added
                needAddedTables.add(table);
                process.addUniqueConnectionName(table.getTableName());
            } else {
                MetadataTool.copyTable(table, mappingTable);
            }
        }
        // record removed table
        List<IMetadataTable> needRemovedTables = new ArrayList<IMetadataTable>();
        for (IMetadataTable table : node.getMetadataList()) {
            if (!schemaNames.contains(table.getLabel())) {
                needRemovedTables.add(table);
                process.removeUniqueConnectionName(table.getTableName());
            }
        }
        node.getMetadataList().removeAll(needRemovedTables);
        node.getMetadataList().addAll(needAddedTables);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.commands.Command#undo()
     */
    @Override
    public void undo() {
        if (node == null || rootSchemaData == null) {
            return;
        }
        setParameterValues(true);

    }

}
