// ============================================================================
//
// Copyright (C) 2006-2011 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.mapper;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IExternalData;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.IProcess;
import org.talend.core.service.IDesignerMapperService;
import org.talend.designer.core.model.utils.emf.talendfile.AbstractExternalData;
import org.talend.designer.mapper.external.data.ExternalMapperData;
import org.talend.designer.mapper.external.data.ExternalMapperTable;
import org.talend.designer.mapper.external.data.ExternalMapperTableEntry;
import org.talend.designer.mapper.model.emf.mapper.InputTable;
import org.talend.designer.mapper.model.emf.mapper.MapperData;
import org.talend.designer.mapper.model.emf.mapper.MapperFactory;
import org.talend.designer.mapper.model.emf.mapper.MapperTableEntry;
import org.talend.designer.mapper.model.emf.mapper.OutputTable;
import org.talend.designer.mapper.utils.MapperHelper;

/**
 * DOC YeXiaowei class global comment. Detailled comment <br/>
 * 
 */
public class DesignerMapperService implements IDesignerMapperService {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.IDesignerMapperSerivce#isVirtualComponent(org.talend.core.model.process.INode)
     */
    public boolean isVirtualComponent(INode node) {
        return MapperHelper.isGeneratedAsVirtualComponent(node);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.IDesignerMapperService#renameJoinTable(java.lang.Process,
     * org.talend.core.model.process.IExternalData, java.util.List)
     */
    public void renameJoinTable(IProcess process, IExternalData data, List<String> createdNames) {
        if (data instanceof ExternalMapperData) {
            ExternalMapperData extenalData = (ExternalMapperData) data;
            final List<ExternalMapperTable> outputTables = extenalData.getOutputTables();
            if (outputTables != null) {
                for (ExternalMapperTable table : outputTables) {
                    if (table.getIsJoinTableOf() != null && !"".equals(table.getIsJoinTableOf())) {
                        final String newName = createNewConnectionName(process, table.getName(), createdNames);
                        table.setName(newName);
                    }
                }
            }
        }

    }

    private String createNewConnectionName(IProcess process, String oldName, List<String> createdNames) {
        String newName = null;

        if (process.checkValidConnectionName(oldName, true)) {
            newName = oldName;
        } else {
            newName = checkExistingNames(process, "copyOf" + oldName); //$NON-NLS-1$
        }
        newName = checkNewNames(process, newName, createdNames);

        createdNames.add(newName);
        return newName;
    }

    private String checkExistingNames(IProcess process, final String oldName) {
        String tmpName = oldName + "_"; //$NON-NLS-1$
        String newName = oldName;

        int index = 0;
        while (!process.checkValidConnectionName(newName, true)) {
            newName = tmpName + (index++);
        }
        return newName;
    }

    private String checkNewNames(IProcess process, final String oldName, List<String> createdNames) {
        String tmpName = oldName + "_"; //$NON-NLS-1$

        String newName = oldName;
        int index = 0;
        while (createdNames.contains(newName)) {
            newName = tmpName + index++;
        }
        // check the name again in process.
        while (!process.checkValidConnectionName(newName, true)) {
            newName = tmpName + (index++);
        }
        return newName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.designer.mapper.IDesignerMapperService#getJoinTableNames(org.talend.core.model.process.IExternalData)
     */
    public List<String> getJoinTableNames(IExternalData data) {
        List<String> namesList = new ArrayList<String>();
        if (data instanceof ExternalMapperData) {
            ExternalMapperData extenalData = (ExternalMapperData) data;
            final List<ExternalMapperTable> outputTables = extenalData.getOutputTables();
            if (outputTables != null) {
                for (ExternalMapperTable table : outputTables) {
                    if (table.getIsJoinTableOf() != null && !"".equals(table.getIsJoinTableOf())) {
                        namesList.add(table.getName());
                    }
                }
            }
        }

        return namesList;
    }

    public void createAutoMappedNode(INode node, IConnection inputConnection, IConnection outputConnection) {
        MapperData data = (MapperData) node.getExternalNode().getExternalEmfData();
        data.setUiProperties(MapperFactory.eINSTANCE.createUiProperties());
        data.getUiProperties().setShellMaximized(true);

        InputTable inputTable = MapperFactory.eINSTANCE.createInputTable();
        data.getInputTables().add(inputTable);
        inputTable.setName(inputConnection.getName());

        for (IMetadataColumn column : inputConnection.getMetadataTable().getListColumns()) {
            MapperTableEntry tableEntry = MapperFactory.eINSTANCE.createMapperTableEntry();
            tableEntry.setName(column.getLabel());
            tableEntry.setType(column.getTalendType());
            tableEntry.setNullable(column.isNullable());
            inputTable.getMapperTableEntries().add(tableEntry);
        }

        OutputTable outputTable = MapperFactory.eINSTANCE.createOutputTable();
        data.getOutputTables().add(outputTable);
        outputTable.setName(outputConnection.getName());

        for (IMetadataColumn column : outputConnection.getMetadataTable().getListColumns()) {
            MapperTableEntry tableEntry = MapperFactory.eINSTANCE.createMapperTableEntry();
            tableEntry.setName(column.getLabel());
            tableEntry.setType(column.getTalendType());
            tableEntry.setNullable(column.isNullable());
            tableEntry.setExpression(inputConnection.getName() + "." + column.getLabel());
            outputTable.getMapperTableEntries().add(tableEntry);
        }

        ((MapperComponent) node.getExternalNode()).buildExternalData(data);
    }

    public void updateLink(INode node, IConnection oldConnection, IConnection newConnection) {
        // MapperData data = (MapperData) node.getExternalNode().getExternalEmfData();
        //
        // for (InputTable inputTable : data.getInputTables()) {
        // if (inputTable.getName().equals(oldConnection.getName())) {
        // inputTable.setName(newConnection.getName());
        // }
        // }
        //
        // ((MapperComponent) node.getExternalNode()).buildExternalData(data);
        ((MapperComponent) node.getExternalNode()).renameInputConnection(oldConnection.getName(), newConnection.getName());
    }

    public List<String> getRepositorySchemaIds(AbstractExternalData nodeData) {
        List<String> schemaIds = new ArrayList<String>();
        if (nodeData != null && nodeData instanceof MapperData) {
            MapperData mapperData = (MapperData) nodeData;
            EList<InputTable> inputTables = mapperData.getInputTables();
            if (inputTables != null && inputTables.size() > 0) {
                for (InputTable inputTable : inputTables) {
                    String id = inputTable.getId();
                    if (id != null && !schemaIds.contains(id)) {
                        schemaIds.add(id);
                    }
                }
            }
            EList<OutputTable> outputTables = mapperData.getOutputTables();
            if (outputTables != null && outputTables.size() > 0) {
                for (OutputTable outputTable : outputTables) {
                    String id = outputTable.getId();
                    if (id != null && !schemaIds.contains(id)) {
                        schemaIds.add(id);
                    }
                }
            }
        }

        return schemaIds;
    }

    public List<String> getRepositorySchemaIds(IExternalData nodeData) {
        List<String> schemaIds = new ArrayList<String>();
        if (nodeData != null && nodeData instanceof ExternalMapperData) {
            ExternalMapperData mapperData = (ExternalMapperData) nodeData;
            List<ExternalMapperTable> inputTables = mapperData.getInputTables();
            if (inputTables != null && inputTables.size() > 0) {
                for (ExternalMapperTable inputTable : inputTables) {
                    String id = inputTable.getId();
                    if (id != null && !schemaIds.contains(id)) {
                        schemaIds.add(id);
                    }
                }
            }
            List<ExternalMapperTable> outputTables = mapperData.getOutputTables();
            if (outputTables != null && outputTables.size() > 0) {
                for (ExternalMapperTable outputTable : outputTables) {
                    String id = outputTable.getId();
                    if (id != null && !schemaIds.contains(id)) {
                        schemaIds.add(id);
                    }
                }
            }
        }

        return schemaIds;
    }

    public void updateMapperTableEntries(IExternalData nodeData, String schemaId, IMetadataTable metadataTable) {
        if (nodeData == null || schemaId == null || metadataTable == null)
            return;
        if (nodeData != null && nodeData instanceof ExternalMapperData) {
            ExternalMapperData mapperData = (ExternalMapperData) nodeData;
            List<ExternalMapperTable> inputTables = mapperData.getInputTables();
            if (inputTables != null && inputTables.size() > 0) {
                for (ExternalMapperTable inputTable : inputTables) {
                    updateEntriesByMetaColumns(schemaId, metadataTable, inputTable);
                }
            }
            List<ExternalMapperTable> outputTables = mapperData.getOutputTables();
            if (outputTables != null && outputTables.size() > 0) {
                for (ExternalMapperTable outputTable : outputTables) {
                    updateEntriesByMetaColumns(schemaId, metadataTable, outputTable);
                }
            }
        }
    }

    public boolean isSameMetadata(IExternalData nodeData, String schemaId, IMetadataTable metadataTable) {
        boolean isSame = true;
        if (nodeData == null || schemaId == null || metadataTable == null)
            return false;
        List<ExternalMapperTable> extTables = new ArrayList<ExternalMapperTable>();
        if (nodeData != null && nodeData instanceof ExternalMapperData) {
            ExternalMapperData mapperData = (ExternalMapperData) nodeData;
            List<ExternalMapperTable> inputTables = mapperData.getInputTables();
            if (inputTables != null && inputTables.size() > 0) {
                for (ExternalMapperTable inputTable : inputTables) {
                    if (schemaId.equals(inputTable.getId()) && !extTables.contains(inputTable)) {
                        extTables.add(inputTable);
                    }
                }
            }
            List<ExternalMapperTable> outputTables = mapperData.getOutputTables();
            if (outputTables != null && outputTables.size() > 0) {
                for (ExternalMapperTable outputTable : outputTables) {
                    if (schemaId.equals(outputTable.getId()) && !extTables.contains(outputTable)) {
                        extTables.add(outputTable);
                    }
                }
            }
            for (ExternalMapperTable extTable : extTables) {
                isSame = isMetadataSame(extTable, metadataTable);
                if (!isSame)
                    return isSame;
            }
        }
        return isSame;
    }

    private void updateEntriesByMetaColumns(String schemaId, IMetadataTable metadataTable, ExternalMapperTable table) {
        String id = table.getId();
        if (schemaId.equals(id)) {
            List<ExternalMapperTableEntry> mapperTableEntries = table.getMetadataTableEntries();
            mapperTableEntries.clear();
            List<IMetadataColumn> columns = metadataTable.getListColumns();
            if (columns != null) {
                for (IMetadataColumn metadataColumn : columns) {
                    ExternalMapperTableEntry tableEntry = new ExternalMapperTableEntry();
                    tableEntry.setName(metadataColumn.getLabel());
                    tableEntry.setType(metadataColumn.getTalendType());
                    tableEntry.setNullable(metadataColumn.isNullable());
                    mapperTableEntries.add(tableEntry);
                }
            }
        }
    }

    private boolean isMetadataSame(ExternalMapperTable extTable, IMetadataTable metadataTable) {
        if (extTable == null || metadataTable == null)
            return false;
        List<ExternalMapperTableEntry> extTableColumns = extTable.getMetadataTableEntries();
        List<IMetadataColumn> tableColumns = metadataTable.getListColumns();
        if (extTableColumns == null && tableColumns == null) {
            return true;
        } else if (extTableColumns != null && tableColumns != null) {
            if (extTableColumns.size() != tableColumns.size()) {
                return false;
            }
            boolean isSame = true;
            for (ExternalMapperTableEntry extColumn : extTableColumns) {
                for (IMetadataColumn column : tableColumns) {
                    if (extColumn != null && column != null && extColumn.getName() != null
                            && extColumn.getName().equals(column.getLabel())) {
                        isSame = isMetacolumnSame(extColumn, column);
                        if (!isSame)
                            return isSame;
                    }
                }
            }
            return isSame;
        }
        return false;
    }

    public boolean isMetacolumnSame(ExternalMapperTableEntry extColumn, IMetadataColumn column) {
        if (extColumn == null || column == null)
            return false;
        if (!sameStringValue(extColumn.getName(), column.getLabel())) {
            return false;
        }
        if (!sameStringValue(extColumn.getType(), column.getTalendType())) {
            return false;
        }
        if (extColumn.isNullable() != column.isNullable()) {
            return false;
        }

        return true;
    }

    private boolean sameStringValue(String value1, String value2) {
        if (value1 == null) {
            if (value2 == null) {
                return true;
            } else {
                return value2.equals(""); //$NON-NLS-1$
            }
        } else {
            if (value1.equals("") && value2 == null) { //$NON-NLS-1$
                return true;
            } else {
                return value1.equals(value2);
            }
        }
    }

}
