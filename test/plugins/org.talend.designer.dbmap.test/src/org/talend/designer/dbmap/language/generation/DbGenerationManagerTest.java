package org.talend.designer.dbmap.language.generation;


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.MetadataColumn;
import org.talend.core.model.metadata.MetadataTable;
import org.talend.core.model.process.IConnection;
import org.talend.designer.core.model.components.ElementParameter;
import org.talend.designer.core.ui.editor.connections.Connection;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.dbmap.DbMapComponent;
import org.talend.designer.dbmap.external.data.ExternalDbMapData;
import org.talend.designer.dbmap.external.data.ExternalDbMapEntry;
import org.talend.designer.dbmap.external.data.ExternalDbMapTable;


public class DbGenerationManagerTest {

    @Test
    public void testInitExpression() {
        testMore();
    }

    public void testMore() {
        checkValue("t1.\\\"id\\\"");
    }

    public void checkValue(String expression) {
        DbMapComponent dbMapComponent = new DbMapComponent();

        ExternalDbMapData externalDbMapData = new ExternalDbMapData();
        dbMapComponent.setExternalData(externalDbMapData);

        List<ExternalDbMapTable> inputTables = new ArrayList<>();
        externalDbMapData.setInputTables(inputTables);

        ExternalDbMapTable externalDbMapTable = new ExternalDbMapTable();
        inputTables.add(externalDbMapTable);

        externalDbMapTable.setName("t1");
        externalDbMapTable.setTableName("t1");
        externalDbMapTable.setJoinType("NO_JOIN");

        List<ExternalDbMapEntry> tableEntries = new ArrayList<>();
        externalDbMapTable.setMetadataTableEntries(tableEntries);
        ExternalDbMapEntry extMapEntry = new ExternalDbMapEntry("id", "t1.id", null);
        tableEntries.add(extMapEntry);

        List<IConnection> incomingConnections = new ArrayList<IConnection>();
        String[] mainTableEntities = new String[] { "\\\"id\\\"" };
        String[] lookupEndtities = new String[] { "\\\"id\\\"" };
        incomingConnections.add(mockConnection("t1", "t1", mainTableEntities));
        incomingConnections.add(mockConnection("t2", "t2", lookupEndtities));
        dbMapComponent.setIncomingConnections(incomingConnections);

        DbGenerationManager dbGenTest = new GenericDbGenerationManager();
        Assert.assertEquals(expression, dbGenTest.initExpression(dbMapComponent, extMapEntry));
    }

    private IConnection mockConnection(String schemaName, String tableName, String[] columns) {
        Connection connection = mock(Connection.class);
        Node node = mock(Node.class);
        ElementParameter param = new ElementParameter(node);
        param.setName("SCHEMA_NAME");
        param.setValue(schemaName);
        when(node.getElementParameter("SCHEMA_NAME")).thenReturn(param);
        param = new ElementParameter(node);
        param.setName("TABLE_NAME");
        param.setValue(tableName);
        when(node.getElementParameter("NAME")).thenReturn(param);
        when(connection.getName()).thenReturn(tableName);
        when(connection.getSource()).thenReturn(node);
        IMetadataTable table = new MetadataTable();
        table.setLabel(tableName);
        table.setTableName(tableName);
        List<IMetadataColumn> listColumns = new ArrayList<IMetadataColumn>();
        for (String columnName : columns) {
            IMetadataColumn column = new MetadataColumn();
            column.setLabel("id");
            column.setOriginalDbColumnName(columnName);
            listColumns.add(column);
        }
        table.setListColumns(listColumns);
        when(connection.getMetadataTable()).thenReturn(table);

        return connection;
    }
}
