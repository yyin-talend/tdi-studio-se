// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2007 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
//
// ============================================================================
package org.talend.designer.core.ui.editor.cmd;

import java.util.List;
import java.util.Map;

import org.eclipse.gef.commands.Command;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.PropertySheet;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.QueryUtil;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.Element;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.utils.TalendTextUtils;
import org.talend.designer.core.ui.editor.nodes.Node;

/**
 * This class is used for generating new query when "Guess Query" button is selected. <br/>
 * 
 */
public class QueryGuessCommand extends Command {

    private Element node;

    private String propName;

    private Object oldValue;

    private IMetadataTable newOutputMetadataTable;

    private IMetadataTable newOutputMetadata;

    private Map<String, String> dbNameAndDbTypeMap;

    private Map<String, String> dbNameAndSchemaMap;

    private Map<String, IMetadataTable> repositoryTableMap;

    private String realTableId;

    private String realTableName;

    private String realDBType;

    /**
     * The property is defined in an element, which can be either a node or a connection.
     * 
     * @param node
     * @param propName
     * @param newOutputMetadataTable
     */
    public QueryGuessCommand(Element node, IMetadataTable newOutputMetadataTable) {
        this.node = node;
        this.newOutputMetadataTable = newOutputMetadataTable;
    }

    private void refreshPropertyView() {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IViewPart view = page.findView("org.eclipse.ui.views.PropertySheet"); //$NON-NLS-1$
        PropertySheet sheet = (PropertySheet) view;
        TabbedPropertySheetPage tabbedPropertySheetPage = (TabbedPropertySheetPage) sheet.getCurrentPage();
        tabbedPropertySheetPage.refresh();
    }

    @Override
    public void execute() {

        // used for generating new Query.

        String dbType = "";
        if (realDBType != null) {
            dbType = realDBType;
        }
        if (this.realTableId != null && this.dbNameAndDbTypeMap.containsKey(this.realTableId)) {
            dbType = this.dbNameAndDbTypeMap.get(this.realTableId);
        }

        String schema = this.dbNameAndSchemaMap.get(this.realTableId);

        if (schema == null) {
            for (IElementParameter param : this.node.getElementParameters()) {
                if (param.getRepositoryValue() != null && param.getRepositoryValue().equals("SCHEMA")) {
                    schema = (String) param.getValue();
                    schema = schema.replace("\"", "");
                    schema = schema.replace("\'", "");
                }
            }
        }

        String newQuery = TalendTextUtils.addSQLQuotes(QueryUtil.generateNewQuery(node, newOutputMetadataTable, dbType, schema,
                realTableName));

        for (IElementParameter param : (List<IElementParameter>) node.getElementParameters()) {
            if (param.getField() == EParameterFieldType.MEMO_SQL) {
                oldValue = node.getPropertyValue(param.getName());
                this.propName = param.getName();
                node.setPropertyValue(param.getName(), newQuery);
                param.setRepositoryValueUsed(true);
            }
        }

        if (this.node instanceof Node) {
            ((Node) this.node).checkAndRefreshNode();
        }

        refreshPropertyView();
        // Ends
    }

    @Override
    public void undo() {
        node.setPropertyValue(this.propName, oldValue);
        refreshPropertyView();
    }

    @Override
    public void redo() {
        this.execute();
    }

    // /**
    // * //* Generates new Query.
    // *
    // * @param repositoryMetadata
    // * @param dbType
    // * @param schema
    // * @return *
    // * @deprecated use {@link QueryUtil#generateNewQuery()} method instead.
    // */
    // public String generateNewQuery(IMetadataTable repositoryMetadata,
    // String dbType, String schema) {
    // return QueryUtil.generateNewQuery(node, repositoryMetadata, dbType,
    // schema, realTableName);
    // // List<IMetadataColumn> metaDataColumnList =
    // // repositoryMetadata.getListColumns();
    // // int index = metaDataColumnList.size();
    // // if (index == 0) {
    // // return "";
    // // }
    // //
    // // StringBuffer query = new StringBuffer();
    // // String enter = "\n";
    // // String space = " ";
    // // query.append("SELECT").append(space);
    // //
    // // String tableNameForColumnSuffix =
    // // TalendTextUtils.addQuotesWithSpaceField(
    // // getTableName(repositoryMetadata, schema, dbType), dbType)
    // // + ".";
    // //
    // // for (int i = 0; i < metaDataColumnList.size(); i++) {
    // // IMetadataColumn metaDataColumn = metaDataColumnList.get(i);
    // // String columnName =
    // //
    // TalendTextUtils.addQuotesWithSpaceField(getColumnName(metaDataColumn.getOriginalDbColumnName(),
    // // dbType), dbType);
    // // if (i != index - 1) {
    // //
    // query.append(tableNameForColumnSuffix).append(columnName).append(",").append(space);
    // // } else {
    // //
    // query.append(tableNameForColumnSuffix).append(columnName).append(space);
    // // }
    // // }
    // //
    // query.append(enter).append("FROM").append(space).append(getTableName(repositoryMetadata,
    // // schema, dbType));
    // //
    // // return query.toString();
    // }

    // /**
    // * Gets the table name.
    // *
    // * @param tableName
    // * @param schema
    // * @return
    // * @deprecated use {@link MetadataTool#getTableName()} method instead.
    // */
    // private String getTableName(IMetadataTable repositoryMetadata,
    // String schema, String dbType) {
    // return MetadataTool.getTableName(node, repositoryMetadata, schema,
    // dbType, this.realTableName);
    // }
    //
    // /**
    // * Checks if database type is Postgres, add quoutes around column name.
    // *
    // * @param name
    // * @param dbType
    // * @return
    // */
    // private String getColumnName(String name, String dbType) {
    // String nameAfterChanged;
    // if (!dbType.equalsIgnoreCase("PostgreSQL")) {
    // nameAfterChanged = name;
    // } else {
    // nameAfterChanged = "\"" + name + "\"";
    // }
    // return nameAfterChanged;
    // }

    /**
     * Sets a set of maps what used for generating new Query.
     * 
     * @param dbNameAndDbTypeMap
     * @param dbNameAndSchemaMap
     * @param getRepositoryTableMap
     */
    public void setMaps(Map<String, String> dbNameAndDbTypeMap, Map<String, String> dbNameAndSchemaMap,
            Map<String, IMetadataTable> repositoryTableMap) {
        this.dbNameAndDbTypeMap = dbNameAndDbTypeMap;
        this.dbNameAndSchemaMap = dbNameAndSchemaMap;
        this.repositoryTableMap = repositoryTableMap;

    }

    /**
     * Sets the real table id and table name.
     * 
     * @param realTableId
     * @param realTableName
     * @param type
     */
    public void setParameters(String realTableId, String realTableName, String type) {
        this.realTableId = realTableId;
        this.realTableName = realTableName;
        realDBType = type;
    }
}
