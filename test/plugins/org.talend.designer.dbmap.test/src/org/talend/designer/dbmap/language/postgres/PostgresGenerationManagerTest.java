package org.talend.designer.dbmap.language.postgres;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.talend.core.model.components.ComponentCategory;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.context.JobContext;
import org.talend.core.model.context.JobContextManager;
import org.talend.core.model.context.JobContextParameter;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.MetadataColumn;
import org.talend.core.model.metadata.MetadataTable;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.utils.TalendTextUtils;
import org.talend.core.ui.component.ComponentsFactoryProvider;
import org.talend.designer.core.model.components.ElementParameter;
import org.talend.designer.core.model.process.DataConnection;
import org.talend.designer.core.model.process.DataNode;
import org.talend.designer.core.ui.editor.connections.Connection;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.process.Process;
import org.talend.designer.dbmap.DbMapComponent;
import org.talend.designer.dbmap.external.data.ExternalDbMapData;
import org.talend.designer.dbmap.external.data.ExternalDbMapEntry;
import org.talend.designer.dbmap.external.data.ExternalDbMapTable;
import org.talend.designer.dbmap.language.generation.DbGenerationManagerTestHelper;

public class PostgresGenerationManagerTest extends DbGenerationManagerTestHelper {

    @Before
    public void setUp() throws Exception {
        dbMapComponent = new DbMapComponent();
        if (dbMapComponent.getElementParameters() == null) {
            dbMapComponent.setElementParameters(Collections.EMPTY_LIST);
        }
    }

    @After
    public void tearDown() throws Exception {
        dbMapComponent = null;
    }

    private void init(String schema, String main_table, String lookup_table) {
        List<IConnection> incomingConnections = new ArrayList<IConnection>();
        String[] mainTableEntities = new String[] { "id", "name", "classNum" };
        String[] lookupEndtities = new String[] { "id", "score" };
        incomingConnections.add(mockConnection(schema, main_table, mainTableEntities, mainTableEntities));
        incomingConnections.add(mockConnection(schema, lookup_table, lookupEndtities, lookupEndtities));
        dbMapComponent.setIncomingConnections(incomingConnections);

        ExternalDbMapData externalData = new ExternalDbMapData();
        List<ExternalDbMapTable> inputs = new ArrayList<ExternalDbMapTable>();
        List<ExternalDbMapTable> outputs = new ArrayList<ExternalDbMapTable>();
        // main table
        ExternalDbMapTable inputTable = new ExternalDbMapTable();
        inputTable.setTableName(schema + "." + main_table);
        List<ExternalDbMapEntry> entities = getMetadataEntities(mainTableEntities, new String[3]);
        inputTable.setMetadataTableEntries(entities);
        inputs.add(inputTable);
        // lookup table
        inputTable = new ExternalDbMapTable();
        inputTable.setTableName(schema + "." + lookup_table);
        entities = getMetadataEntities(lookupEndtities, new String[2]);
        inputTable.setMetadataTableEntries(entities);
        inputs.add(inputTable);

        // output
        ExternalDbMapTable outputTable = new ExternalDbMapTable();
        outputTable.setName("grade");
        List<ExternalDbMapEntry> conditions = new ArrayList<ExternalDbMapEntry>();
        conditions.add(new ExternalDbMapEntry(schema + "." + main_table + ".id =3"));
        outputTable.setCustomWhereConditionsEntries(conditions);
        String[] names = new String[] { "id", "name", "classNum", "score" };
        String[] expressions = new String[] { schema + "." + main_table + ".id", schema + "." + main_table + ".name",
                schema + "." + main_table + ".classNum", schema + "." + lookup_table + ".score" };
        outputTable.setMetadataTableEntries(getMetadataEntities(names, expressions));
        outputs.add(outputTable);

        externalData.setInputTables(inputs);
        externalData.setOutputTables(outputs);
        dbMapComponent.setExternalData(externalData);
        List<IMetadataTable> metadataList = new ArrayList<IMetadataTable>();
        MetadataTable metadataTable = getMetadataTable(names, names);
        metadataTable.setLabel("grade");
        metadataList.add(metadataTable);
        dbMapComponent.setMetadataList(metadataList);
        Process process = mock(Process.class);
        when(process.getContextManager()).thenReturn(new JobContextManager());
        dbMapComponent.setProcess(process);
    }

    private IConnection mockConnection(String schemaName, String tableName, String[] columns, String[] dbColumns) {
        Connection connection = mock(Connection.class);
        Node node = mock(Node.class);
        ElementParameter param = new ElementParameter(node);
        param.setName("ELT_SCHEMA_NAME");
        param.setValue(schemaName);
        when(node.getElementParameter("ELT_SCHEMA_NAME")).thenReturn(param);
        param = new ElementParameter(node);
        param.setName("ELT_TABLE_NAME");
        param.setValue(tableName);
        when(node.getElementParameter("ELT_TABLE_NAME")).thenReturn(param);
        when(connection.getName()).thenReturn(schemaName + "." + tableName);
        when(connection.getSource()).thenReturn(node);
        IMetadataTable table = new MetadataTable();
        table.setLabel(tableName);
        table.setTableName(tableName);
        List<IMetadataColumn> listColumns = new ArrayList<IMetadataColumn>();
        for (int i = 0; i < columns.length; i++) {
            String columnName = columns[i];
            IMetadataColumn column = new MetadataColumn();
            column.setLabel(columnName);
            column.setOriginalDbColumnName(dbColumns[i]);
            listColumns.add(column);
        }
        table.setListColumns(listColumns);
        when(connection.getMetadataTable()).thenReturn(table);

        return connection;
    }

    @Test
    public void testBuildSqlSelect() {
        // without context
        String schema = "school";
        String main_table = "classInfo";
        String lookup_table = "scoreInfo";
        init(schema, main_table, lookup_table);
        PostgresGenerationManager manager = new PostgresGenerationManager();
        String query = manager.buildSqlSelect(dbMapComponent, "grade");
        assertNotNull(query);
        String expectedQuery = "\"SELECT\n"
                + "\\\"school\\\".\\\"classInfo\\\".\\\"id\\\", \\\"school\\\".\\\"classInfo\\\".\\\"name\\\", \\\"school\\\".\\\"classInfo\\\".\\\"classNum\\\", \\\"school\\\".\\\"scoreInfo\\\".\\\"score\\\"\n"
                + "FROM\n \\\"school\\\".\\\"classInfo\\\" , \\\"school\\\".\\\"scoreInfo\\\"\n"
                + "WHERE \\\"school\\\".\\\"classInfo\\\".\\\"id\\\" =3\"";
        assertEquals(expectedQuery, query);

        manager.setAddQuotesInColumns(true);
        String queryWithQuotes = manager.buildSqlSelect(dbMapComponent, "grade");
        assertEquals(expectedQuery, queryWithQuotes);

        // with context
        schema = "context.schema";
        main_table = "context.main_table";
        lookup_table = "context.lookup";
        init(schema, main_table, lookup_table);
        JobContext newContext = new JobContext("Default");
        List<IContextParameter> newParamList = new ArrayList<IContextParameter>();
        newContext.setContextParameterList(newParamList);
        JobContextParameter param = new JobContextParameter();
        param.setName("schema");
        newParamList.add(param);
        param = new JobContextParameter();
        param.setName("main_table");
        newParamList.add(param);
        param = new JobContextParameter();
        param.setName("lookup");
        newParamList.add(param);
        dbMapComponent.getProcess().getContextManager().setDefaultContext(newContext);
        query = manager.buildSqlSelect(dbMapComponent, "grade");
        expectedQuery = "\"SELECT\n"
                + "\\\"\"+context.schema+\"\\\".\\\"\"+context.main_table+\"\\\".\\\"id\\\", \\\"\"+context.schema+\"\\\".\\\"\"+context.main_table+\"\\\".\\\"name\\\","
                + " \\\"\"+context.schema+\"\\\".\\\"\"+context.main_table+\"\\\".\\\"classNum\\\", \\\"\"+context.schema+\"\\\".\\\"\"+context.lookup+\"\\\".\\\"score\\\"\n"
                + "FROM\n"
                + " \\\"\"+context.schema+\"\\\".\\\"\"+context.main_table+\"\\\" , \\\"\"+context.schema+\"\\\".\\\"\"+context.lookup+\"\\\"\n"
                + "WHERE \\\"\"+context.schema+\"\\\".\\\"\"+context.main_table+\"\\\".\\\"id\\\" =3\"";
        assertEquals(expectedQuery, query);

        manager.setAddQuotesInColumns(true);
        String queryWithQuotesContext = manager.buildSqlSelect(dbMapComponent, "grade");
        assertEquals(expectedQuery, queryWithQuotesContext);
    }

    @Test
    public void testBuildSqlSelectForGlobalMap() {
        String schema = "((String)globalMap.get(\"schema\"))";
        String main_table = "((String)globalMap.get(\"main_table\"))";
        String lookup_table = "((String)globalMap.get(\"lookup_table\"))";

        // ((String)globalMap.get("tableName")).columnName
        init("", main_table, null, lookup_table, null);
        String expectedQuery = "\"SELECT\n"
                + "\\\"\"+((String)globalMap.get(\"main_table\"))+\"\\\".\\\"id\\\", \\\"\"+((String)globalMap.get(\"main_table\"))+\"\\\".\\\"name\\\","
                + " \\\"\"+((String)globalMap.get(\"main_table\"))+\"\\\".\\\"age\\\", \\\"\"+((String)globalMap.get(\"lookup_table\"))+\"\\\".\\\"score\\\"\n"
                + "FROM\n"
                + " \\\"\"+((String)globalMap.get(\"main_table\"))+\"\\\" , \\\"\"+((String)globalMap.get(\"lookup_table\"))+\"\\\" \"";
        PostgresGenerationManager manager = new PostgresGenerationManager();
        String query = manager.buildSqlSelect(dbMapComponent, "grade");
        assertEquals(expectedQuery, query);

        manager.setAddQuotesInColumns(true);
        String queryWithQuotes = manager.buildSqlSelect(dbMapComponent, "grade");
        assertEquals(expectedQuery, queryWithQuotes);

        // schema.((String)globalMap.get("tableName")).columnName
        init(schema, main_table, null, lookup_table, null);
        manager = new PostgresGenerationManager();
        expectedQuery = "\"SELECT\n"
                + "\\\"\"+((String)globalMap.get(\"schema\"))+\"\\\".\\\"\"+((String)globalMap.get(\"main_table\"))+\"\\\".\\\"id\\\", \\\"\"+((String)globalMap.get(\"schema\"))+\"\\\".\\\"\"+((String)globalMap.get(\"main_table\"))+\"\\\".\\\"name\\\", "
                + "\\\"\"+((String)globalMap.get(\"schema\"))+\"\\\".\\\"\"+((String)globalMap.get(\"main_table\"))+\"\\\".\\\"age\\\", \\\"\"+((String)globalMap.get(\"schema\"))+\"\\\".\\\"\"+((String)globalMap.get(\"lookup_table\"))+\"\\\".\\\"score\\\"\n"
                + "FROM\n"
                + " \\\"\"+((String)globalMap.get(\"schema\"))+\"\\\".\\\"\"+((String)globalMap.get(\"main_table\"))+\"\\\" , \\\"\"+((String)globalMap.get(\"schema\"))+\"\\\".\\\"\"+((String)globalMap.get(\"lookup_table\"))+\"\\\" \"";
        query = manager.buildSqlSelect(dbMapComponent, "grade");
        assertEquals(expectedQuery, query);

        manager.setAddQuotesInColumns(true);
        String queryWithQuotesSchema = manager.buildSqlSelect(dbMapComponent, "grade");
        assertEquals(expectedQuery, queryWithQuotesSchema);

        // schema.((String)globalMap.get("tableName")).columnName
        schema = "my_schema";
        manager = new PostgresGenerationManager();
        init(schema, main_table, null, lookup_table, null);
        manager = new PostgresGenerationManager();
        expectedQuery = "\"SELECT\n"
                + "\\\"my_schema\\\".\\\"\"+((String)globalMap.get(\"main_table\"))+\"\\\".\\\"id\\\", \\\"my_schema\\\".\\\"\"+((String)globalMap.get(\"main_table\"))+\"\\\".\\\"name\\\","
                + " \\\"my_schema\\\".\\\"\"+((String)globalMap.get(\"main_table\"))+\"\\\".\\\"age\\\", "
                + "\\\"my_schema\\\".\\\"\"+((String)globalMap.get(\"lookup_table\"))+\"\\\".\\\"score\\\"\n"
                + "FROM\n"
                + " \\\"my_schema\\\".\\\"\"+((String)globalMap.get(\"main_table\"))+\"\\\" , \\\"my_schema\\\".\\\"\"+((String)globalMap.get(\"lookup_table\"))+\"\\\" \"";
        query = manager.buildSqlSelect(dbMapComponent, "grade");
        assertEquals(expectedQuery, query);

        // ((String)globalMap.get("schema")).tableName.columnName
        schema = "((String)globalMap.get(\"schema\"))";
        main_table = "main_table";
        init(schema, main_table, null, lookup_table, null);
        manager = new PostgresGenerationManager();
        expectedQuery = "\"SELECT\n"
                + "\\\"\"+((String)globalMap.get(\"schema\"))+\"\\\".\\\"main_table\\\".\\\"id\\\", \\\"\"+((String)globalMap.get(\"schema\"))+\"\\\".\\\"main_table\\\".\\\"name\\\","
                + " \\\"\"+((String)globalMap.get(\"schema\"))+\"\\\".\\\"main_table\\\".\\\"age\\\", "
                + "\\\"\"+((String)globalMap.get(\"schema\"))+\"\\\".\\\"\"+((String)globalMap.get(\"lookup_table\"))+\"\\\".\\\"score\\\"\n"
                + "FROM\n"
                + " \\\"\"+((String)globalMap.get(\"schema\"))+\"\\\".\\\"main_table\\\" , \\\"\"+((String)globalMap.get(\"schema\"))+\"\\\".\\\"\"+((String)globalMap.get(\"lookup_table\"))+\"\\\" \"";
        query = manager.buildSqlSelect(dbMapComponent, "grade");
        assertEquals(expectedQuery, query);
    }

    @Test
    public void testBuildSqlSelectForGlobalMapForSpecialCharacters() {
        // test special charactor in globalmap
        String main_table = "((String)globalMap.get(\"#main_table%\"))";
        String lookup_table = "((String)globalMap.get(\"@lookup_table*\"))";
        init("", main_table, null, lookup_table, null);
        String expectedQuery = "\"SELECT\n"
                + "\\\"\"+((String)globalMap.get(\"#main_table%\"))+\"\\\".\\\"id\\\", \\\"\"+((String)globalMap.get(\"#main_table%\"))+\"\\\".\\\"name\\\","
                + " \\\"\"+((String)globalMap.get(\"#main_table%\"))+\"\\\".\\\"age\\\", \\\"\"+((String)globalMap.get(\"@lookup_table*\"))+\"\\\".\\\"score\\\"\n"
                + "FROM\n"
                + " \\\"\"+((String)globalMap.get(\"#main_table%\"))+\"\\\" , \\\"\"+((String)globalMap.get(\"@lookup_table*\"))+\"\\\" \"";
        PostgresGenerationManager manager = new PostgresGenerationManager();
        String query = manager.buildSqlSelect(dbMapComponent, "grade");
        assertEquals(expectedQuery, query);

    }

    @Test
    public void testBuildSqlSelectWithAlias() {
        String schema = "";
        String main_table = "((String)globalMap.get(\"main_table\"))+\"abc\"";
        String main_alias = "main_table";
        String lookup_table = "((String)globalMap.get(\"lookup_table\"))";
        String lookup_alias = "";
        init(schema, main_table, main_alias, lookup_table, lookup_alias);
        String expectedQuery = "\"SELECT\n"
                + "\\\"main_table\\\".\\\"id\\\", \\\"main_table\\\".\\\"name\\\", \\\"main_table\\\".\\\"age\\\", \\\"\"+((String)globalMap.get(\"lookup_table\"))+\"\\\".\\\"score\\\"\n"
                + "FROM\n"
                + " \\\"\"+((String)globalMap.get(\"main_table\"))+\"abc\"+\"\\\" \\\"main_table\\\" , \\\"\"+((String)globalMap.get(\"lookup_table\"))+\"\\\" \"";
        PostgresGenerationManager manager = new PostgresGenerationManager();
        String query = manager.buildSqlSelect(dbMapComponent, "grade");
        assertEquals(expectedQuery, query);

        main_table = "((String)globalMap.get(\"main_table\"))+((String)globalMap.get(\"main_table1\"))";
        main_alias = "main_table";
        lookup_table = "((String)globalMap.get(\"lookup_table\"))";
        lookup_alias = "lookup_table";
        init(schema, main_table, main_alias, lookup_table, lookup_alias);
        expectedQuery = "\"SELECT\n"
                + "\\\"main_table\\\".\\\"id\\\", \\\"main_table\\\".\\\"name\\\", \\\"main_table\\\".\\\"age\\\", \\\"lookup_table\\\".\\\"score\\\"\n"
                + "FROM\n"
                + " \\\"\"+((String)globalMap.get(\"main_table\"))+((String)globalMap.get(\"main_table1\"))+\"\\\" \\\"main_table\\\" , \\\"\"+((String)globalMap.get(\"lookup_table\"))+\"\\\" \\\"lookup_table\\\" \"";
        manager = new PostgresGenerationManager();
        query = manager.buildSqlSelect(dbMapComponent, "grade");
        assertEquals(expectedQuery, query);

        manager.setAddQuotesInColumns(true);
        String queryWithQuotesWithAlias = manager.buildSqlSelect(dbMapComponent, "grade");
        assertEquals(expectedQuery, queryWithQuotesWithAlias);

    }

    @Test
    public void testBuildSqlSelectForExpressionWithQuote() {
        PostgresGenerationManager manager = new PostgresGenerationManager();
        String schema = "context.schema";
        String tableName = "eltinput";
        String[] columns = new String[] { "_String", "_void" };
        String[] dbColumns = new String[] { "String", "void", "age" };
        String[] expressions = new String[] { schema + "." + tableName + "._String", schema + "." + tableName + "._void" };
        init(schema, tableName, tableName);
        initForExpression(schema, tableName, columns, dbColumns, "grade", columns, dbColumns, expressions);
        ExternalDbMapTable externalData = dbMapComponent.getExternalData().getOutputTables().get(0);
        ExternalDbMapEntry whereEntity = new ExternalDbMapEntry();
        whereEntity.setName("where_entity");
        whereEntity.setExpression("\\\"eltinput\\\".\\\"String\\\">10");
        List<ExternalDbMapEntry> whereEntries = new ArrayList<ExternalDbMapEntry>();
        whereEntries.add(whereEntity);
        externalData.setCustomWhereConditionsEntries(whereEntries);

        String expectedQuery = "\"SELECT\n"
                + "\\\"\"+context.schema+\"\\\".\\\"eltinput\\\".\\\"String\\\", \\\"\"+context.schema+\"\\\".\\\"eltinput\\\".\\\"void\\\"\n"
                + "FROM\n" + " \\\"\"+context.schema+\"\\\".\\\"eltinput\\\"\n" + "WHERE \\\"eltinput\\\".\\\"String\\\">10\"";
        String query = manager.buildSqlSelect(dbMapComponent, "grade");
        assertEquals(expectedQuery, query);

        ExternalDbMapEntry whereEntityExpression = new ExternalDbMapEntry();
        whereEntityExpression.setName("where_entity");
        whereEntityExpression.setExpression(
                "\\\"eltinput\\\".\\\"String\\\">=to_char(now() + '-\" + context.JIKO_TARGET_DAYS + \" days', 'yyyymmddhh24mi')");
        List<ExternalDbMapEntry> whereEntriesExpression = new ArrayList<ExternalDbMapEntry>();
        whereEntriesExpression.add(whereEntityExpression);
        externalData.setCustomWhereConditionsEntries(whereEntriesExpression);

        String expectedQueryExpression = "\"SELECT\n"
                + "\\\"\"+context.schema+\"\\\".\\\"eltinput\\\".\\\"String\\\", \\\"\"+context.schema+\"\\\".\\\"eltinput\\\".\\\"void\\\"\n"
                + "FROM\n" + " \\\"\"+context.schema+\"\\\".\\\"eltinput\\\"\n"
                + "WHERE \\\"eltinput\\\".\\\"String\\\">=to_char(now() + '-\" + context.JIKO_TARGET_DAYS + \" days', 'yyyymmddhh24mi')\"";
        String queryExpression = manager.buildSqlSelect(dbMapComponent, "grade");
        assertEquals(expectedQueryExpression, queryExpression);

        dbMapComponent = new DbMapComponent();
        schema = "eltschema";
        tableName = "elttable";
        expressions = new String[] { "\\\"eltschema\\\".\\\"elttable\\\".\\\"String\\\"",
                "\\\"eltschema\\\".\\\"elttable\\\".\\\"void\\\"" };
        initForExpression(schema, tableName, columns, dbColumns, "grade", columns, dbColumns, expressions);
        expectedQuery = "\"SELECT\n"
                + "\\\"eltschema\\\".\\\"elttable\\\".\\\"String\\\", \\\"eltschema\\\".\\\"elttable\\\".\\\"void\\\"\n"
                + "FROM\n" + " \\\"eltschema\\\".\\\"elttable\\\" ";

    }

    @Test
    public void testBuildSqlSelectForUpdateOutputTable() {
        // fix for tuj BugTDI32594_tELTPostgresqlOutput_ColFunc/BugTDI22916_tELTPostgresqlXX_FullOutterJoin
        PostgresGenerationManager manager = new PostgresGenerationManager();
        String schema = "context.schema";
        String tableName = "bugtdi32594_src";
        String outputTable = "bugtdi32594_dest";
        String[] inputColumns = new String[] { "name", "register" };
        String[] outputColumns = new String[] { "name", "name_uppercase", "register" };
        String[] expressions = new String[] { "context.schema.bugtdi32594_src.name",
                "UPPER(context.schema.bugtdi32594_src.name)", "to_date(context.schema.bugtdi32594_src.register, 'yyyy-MM-dd')" };
        init(schema, tableName, tableName);
        initForExpression(schema, tableName, inputColumns, inputColumns, outputTable, outputColumns, outputColumns, expressions);
        ExternalDbMapTable externalData = dbMapComponent.getExternalData().getOutputTables().get(0);
        ExternalDbMapEntry whereEntity = new ExternalDbMapEntry();
        whereEntity.setName("where_entity");
        whereEntity.setExpression("bugtdi32594_src.id=bugtdi32594_dest.id");
        List<ExternalDbMapEntry> whereEntries = new ArrayList<ExternalDbMapEntry>();
        whereEntries.add(whereEntity);
        externalData.setCustomWhereConditionsEntries(whereEntries);

        String expectedQuery = "\"SELECT\n"
                + "\\\"\"+context.schema+\"\\\".\\\"bugtdi32594_src\\\".\\\"name\\\", UPPER(\\\"\"+context.schema+\"\\\".\\\"bugtdi32594_src\\\".\\\"name\\\"), "
                + "to_date(\\\"\"+context.schema+\"\\\".\\\"bugtdi32594_src\\\".\\\"register\\\", 'yyyy-MM-dd')\n" + "FROM\n"
                + " \\\"\"+context.schema+\"\\\".\\\"bugtdi32594_src\\\"\n"
                + "WHERE \\\"bugtdi32594_src\\\".id=\\\"bugtdi32594_dest\\\".id\"";
        String query = manager.buildSqlSelect(dbMapComponent, outputTable);
        assertEquals(expectedQuery, query);

    }

    private void initForExpression(String schema, String main_table, String[] inputColumns, String[] inputDbColumns,
            String outputTableName, String[] outputColumns, String[] outputDbColumns, String[] expressions) {
        List<IConnection> incomingConnections = new ArrayList<IConnection>();
        incomingConnections.add(mockConnection(schema, main_table, inputColumns, inputDbColumns));
        dbMapComponent.setIncomingConnections(incomingConnections);

        ExternalDbMapData externalData = new ExternalDbMapData();
        List<ExternalDbMapTable> inputs = new ArrayList<ExternalDbMapTable>();
        List<ExternalDbMapTable> outputs = new ArrayList<ExternalDbMapTable>();
        // main table
        ExternalDbMapTable inputTable = new ExternalDbMapTable();
        String mainTableName = "".equals(schema) ? main_table : schema + "." + main_table;
        // quote will be removed in the ui for connections ,so we do the same for test
        String mainTableNameNoQuote = TalendTextUtils.removeQuotes(mainTableName);
        inputTable.setTableName(mainTableNameNoQuote);
        inputTable.setName(mainTableName);
        List<ExternalDbMapEntry> entities = getMetadataEntities(inputColumns, new String[3]);
        inputTable.setMetadataTableEntries(entities);
        inputs.add(inputTable);

        // output
        ExternalDbMapTable outputTable = new ExternalDbMapTable();
        outputTable.setName(outputTableName);
        outputTable.setMetadataTableEntries(getMetadataEntities(outputColumns, expressions));
        outputs.add(outputTable);

        externalData.setInputTables(inputs);
        externalData.setOutputTables(outputs);
        dbMapComponent.setExternalData(externalData);
        List<IMetadataTable> metadataList = new ArrayList<IMetadataTable>();
        MetadataTable metadataTable = getMetadataTable(outputColumns, outputDbColumns);
        metadataTable.setLabel(outputTableName);
        metadataList.add(metadataTable);
        dbMapComponent.setMetadataList(metadataList);
        JobContext newContext = new JobContext("Default");
        List<IContextParameter> newParamList = new ArrayList<IContextParameter>();
        newContext.setContextParameterList(newParamList);
        JobContextParameter param = new JobContextParameter();
        param.setName("schema");
        newParamList.add(param);
        param = new JobContextParameter();
        param.setName("main_table");
        newParamList.add(param);
        Process process = mock(Process.class);
        JobContextManager contextManger = new JobContextManager();
        contextManger.setDefaultContext(newContext);
        when(process.getContextManager()).thenReturn(contextManger);
        dbMapComponent.setProcess(process);
    }

    @Test
    public void testGetHandledTableName() {
        PostgresGenerationManager manager = new PostgresGenerationManager();
        String schema = "";
        String main_table = "\"mainTable\"";
        String main_alias = null;
        init(schema, main_table, main_alias, "", "");
        String tableName = "";
        if (!"".equals(schema)) {
            tableName = TalendTextUtils.removeQuotes(schema) + ".";
        }
        tableName = TalendTextUtils.removeQuotes(main_table);
        String expectedValue = "\\\"mainTable\\\"";
        String handledTableName = manager.getHandledTableName(dbMapComponent, tableName, main_alias);
        Assert.assertEquals(expectedValue, handledTableName);

        main_table = "((String)globalMap.get(\"#main_table%\"))";
        main_alias = null;
        init(schema, main_table, main_alias, "", "");
        tableName = "";
        if (!"".equals(schema)) {
            tableName = TalendTextUtils.removeQuotes(schema) + ".";
        }
        tableName = TalendTextUtils.removeQuotes(main_table);
        expectedValue = "\\\"\"+((String)globalMap.get(\"#main_table%\"))+\"\\\"";
        handledTableName = manager.getHandledTableName(dbMapComponent, tableName, main_alias);
        Assert.assertEquals(expectedValue, handledTableName);

        main_table = "((String)globalMap.get(\"#main_table%\"))";
        main_alias = "main_table";
        init(schema, main_table, main_alias, "", "");
        tableName = "";
        if (!"".equals(schema)) {
            tableName = TalendTextUtils.removeQuotes(schema) + ".";
        }
        tableName = TalendTextUtils.removeQuotes(main_table);
        expectedValue = "\\\"\"+((String)globalMap.get(\"#main_table%\"))+\"\\\"";
        handledTableName = manager.getHandledTableName(dbMapComponent, tableName, main_alias);
        Assert.assertEquals(expectedValue, handledTableName);

        main_table = "context.main_table";
        main_alias = "main_table";
        init(schema, main_table, main_alias, "", "");
        tableName = "";
        if (!"".equals(schema)) {
            tableName = TalendTextUtils.removeQuotes(schema) + ".";
        }
        tableName = TalendTextUtils.removeQuotes(main_table);
        expectedValue = "\\\"\"+context.main_table+\"\\\"";
        handledTableName = manager.getHandledTableName(dbMapComponent, tableName, main_alias);
        Assert.assertEquals(expectedValue, handledTableName);

        main_table = "\"abc\"+context.main_table";
        main_alias = "main_table";
        init(schema, main_table, main_alias, "", "");
        tableName = "";
        if (!"".equals(schema)) {
            tableName = TalendTextUtils.removeQuotes(schema) + ".";
        }
        tableName = TalendTextUtils.removeQuotes(main_table);
        expectedValue = "\\\"\"+\"abc\"+context.main_table+\"\\\"";
        handledTableName = manager.getHandledTableName(dbMapComponent, tableName, main_alias);
        Assert.assertEquals(expectedValue, handledTableName);

        main_table = "((String)globalMap.get(\"#main_table\"))+context.main_table";
        main_alias = "main_table";
        init(schema, main_table, main_alias, "", "");
        tableName = "";
        if (!"".equals(schema)) {
            tableName = TalendTextUtils.removeQuotes(schema) + ".";
        }
        tableName = TalendTextUtils.removeQuotes(main_table);
        expectedValue = "\\\"\"+((String)globalMap.get(\"#main_table\"))+context.main_table+\"\\\"";
        handledTableName = manager.getHandledTableName(dbMapComponent, tableName, main_alias);
        Assert.assertEquals(expectedValue, handledTableName);

    }

    @Test
    public void testReplaceContextQuotes() {
        String schema = "context.schema";
        String main_table = "context.main_table";
        String lookup_table = "context.lookup";
        String contextValue = "context.value";
        JobContext context = new JobContext("Default");

        IContextParameter schemaContext = new JobContextParameter();
        schemaContext.setName("schema");
        schemaContext.setValue("schema");
        schemaContext.setType("String");
        context.getContextParameterList().add(schemaContext);

        IContextParameter mainTableContext = new JobContextParameter();
        mainTableContext.setName("main_table");
        mainTableContext.setValue("mainTable");
        mainTableContext.setType("String");
        context.getContextParameterList().add(mainTableContext);

        IContextParameter lookupTableContext = new JobContextParameter();
        lookupTableContext.setName("lookup");
        lookupTableContext.setValue("lookupTable");
        lookupTableContext.setType("String");
        context.getContextParameterList().add(lookupTableContext);

        IContextParameter valueContext = new JobContextParameter();
        valueContext.setName("value");
        valueContext.setValue("999999");
        valueContext.setType("Integer");
        context.getContextParameterList().add(valueContext);

        List<IConnection> incomingConnections = new ArrayList<IConnection>();
        String[] mainTableEntities = new String[] { "id", "name", "classNum" };
        String[] lookupEndtities = new String[] { "id", "score" };
        incomingConnections.add(mockConnection(schema, main_table, mainTableEntities, mainTableEntities));
        incomingConnections.add(mockConnection(schema, lookup_table, lookupEndtities, lookupEndtities));
        dbMapComponent.setIncomingConnections(incomingConnections);

        ExternalDbMapData externalData = new ExternalDbMapData();
        List<ExternalDbMapTable> inputs = new ArrayList<ExternalDbMapTable>();
        List<ExternalDbMapTable> outputs = new ArrayList<ExternalDbMapTable>();
        // main table
        ExternalDbMapTable inputTable = new ExternalDbMapTable();
        inputTable.setTableName(schema + "." + main_table);
        inputTable.setName(schema + "." + main_table);
        List<ExternalDbMapEntry> entities = getMetadataEntities(mainTableEntities, new String[3]);

        ExternalDbMapEntry entity = new ExternalDbMapEntry();
        entity.setName("id");
        entity.setExpression(contextValue);
        entity.setOperator("=");
        entities.set(0, entity);
        inputTable.setMetadataTableEntries(entities);
        inputs.add(inputTable);

        // lookup table
        inputTable = new ExternalDbMapTable();
        inputTable.setTableName(schema + "." + lookup_table);
        entities = getMetadataEntities(lookupEndtities, new String[2]);
        inputTable.setMetadataTableEntries(entities);
        inputs.add(inputTable);

        // output
        ExternalDbMapTable outputTable = new ExternalDbMapTable();
        outputTable.setName("grade");
        String[] names = new String[] { "id", "name", "classNum", "score" };
        String[] expressions = new String[] { schema + "." + main_table + ".id", schema + "." + main_table + ".name",
                schema + "." + main_table + ".classNum", schema + "." + lookup_table + ".score" };
        outputTable.setMetadataTableEntries(getMetadataEntities(names, expressions));
        outputs.add(outputTable);

        externalData.setInputTables(inputs);
        externalData.setOutputTables(outputs);
        dbMapComponent.setExternalData(externalData);
        List<IMetadataTable> metadataList = new ArrayList<IMetadataTable>();
        MetadataTable metadataTable = getMetadataTable(names, names);
        metadataTable.setLabel("grade");
        metadataList.add(metadataTable);
        dbMapComponent.setMetadataList(metadataList);
        Process process = mock(Process.class);
        when(process.getContextManager()).thenReturn(new JobContextManager());
        dbMapComponent.setProcess(process);
        dbMapComponent.getProcess().getContextManager().setDefaultContext(context);

        PostgresGenerationManager manager = new PostgresGenerationManager();
        String query = manager.buildSqlSelect(dbMapComponent, "grade");

        String expectedQuery = "\"SELECT\n"
                + "\\\"\"+context.schema+\"\\\".\\\"\"+context.main_table+\"\\\".\\\"id\\\", \\\"\"+context.schema+\"\\\".\\\"\"+context.main_table+\"\\\".\\\"name\\\","
                + " \\\"\"+context.schema+\"\\\".\\\"\"+context.main_table+\"\\\".\\\"classNum\\\", \\\"\"+context.schema+\"\\\".\\\"\"+context.lookup+\"\\\".\\\"score\\\"\n"
                + "FROM\n"
                + " \\\"\"+context.schema+\"\\\".\\\"\"+context.main_table+\"\\\" , \\\"\"+context.schema+\"\\\".\\\"\"+context.lookup+\"\\\"\n"
                + "WHERE\n  \\\"\"+context.schema+\"\\\".\\\"\"+context.main_table+\"\\\".\\\"id\\\" = \" +context.value";
        System.out.println(query);
        System.out.println("==========================================");
        System.out.println(expectedQuery);

        assertEquals(expectedQuery, query);
    }

    @Test
    public void testUpdateWithQuotes() {
        PostgresGenerationManager dbManager = new PostgresGenerationManager();
        dbManager.setAddQuotesInColumns(true);
        String schema = "dbo";
        String inputTable1 = "src1";
        String inputTable2 = "src2";
        String outTable1 = "tar";

        dbMapComponent = new DbMapComponent();

        List<IMetadataTable> metadataList = new ArrayList<IMetadataTable>();

        MetadataTable metadataTable = getMetadataTable(new String[] { "newColumn", "newColumn1" }, new String[] { "id", "name" });
        metadataTable.setLabel(schema + "." + outTable1);
        metadataList.add(metadataTable);

        dbMapComponent.setMetadataList(metadataList);

        // main table
        ExternalDbMapData externalData = new ExternalDbMapData();
        List<ExternalDbMapTable> inputs = new ArrayList<ExternalDbMapTable>();
        List<ExternalDbMapTable> outputs = new ArrayList<ExternalDbMapTable>();
        // main table
        ExternalDbMapTable inputTable = new ExternalDbMapTable();
        inputTable.setTableName(schema + "." + inputTable1);
        inputTable.setName(schema + "." + inputTable1);
        inputTable.setAlias("A");
        List<ExternalDbMapEntry> entities = getMetadataEntities(new String[] { "newColumn", "newColumn1" }, new String[2]);
        inputTable.setMetadataTableEntries(entities);
        inputs.add(inputTable);

        // lookup table
        inputTable = new ExternalDbMapTable();
        inputTable.setTableName(schema + "." + inputTable2);
        inputTable.setName(schema + "." + inputTable2);
        inputTable.setAlias("B");
        entities = getMetadataEntities(new String[] { "newColumn", "newColumn1" }, new String[2]);
        ExternalDbMapEntry newColumn = entities.get(0);
        newColumn.setExpression("A.newColumn");
        newColumn.setOperator("=");
        inputTable.setJoinType("INNER_JOIN");
        newColumn.setJoin(true);
        inputTable.setMetadataTableEntries(entities);
        inputs.add(inputTable);

        // output
        ExternalDbMapTable outputTable = new ExternalDbMapTable();
        outputTable.setName(schema + "." + outTable1);
        outputTable.setTableName(schema + "." + outTable1);
        String[] names = new String[] { "tarColumn", "tarColumn1" };
        String[] expressions = new String[] { "A.newColumn", "A.newColumn1" };
        outputTable.setMetadataTableEntries(getMetadataEntities(names, expressions));
        outputs.add(outputTable);

        externalData.setInputTables(inputs);
        externalData.setOutputTables(outputs);
        dbMapComponent.setExternalData(externalData);

        List<IConnection> incomingConnections = new ArrayList<IConnection>();
        incomingConnections.add(
                mockConnection(schema, inputTable1, new String[] { "newColumn", "newColumn1" }, new String[] { "id", "name" }));
        incomingConnections.add(
                mockConnection(schema, inputTable2, new String[] { "newColumn", "newColumn1" }, new String[] { "id", "name" }));
        dbMapComponent.setIncomingConnections(incomingConnections);

        List<IConnection> outputConnections = new ArrayList<IConnection>();
        Node map1 = mockNode(dbMapComponent);
        IConnection connection = mockConnection(map1, schema, inputTable1, new String[] { "id", "name" });
        IComponent targetComponent = ComponentsFactoryProvider.getInstance().get("tPostgresqlOutput",
                ComponentCategory.CATEGORY_4_DI.getName());
        connection.getMetadataTable().getColumn("id").setLabel("newColumn");
        connection.getMetadataTable().getColumn("name").setLabel("newColumn1");
        // add target
        DataNode output = new DataNode();
        List<IElementParameter> paraList = new ArrayList<IElementParameter>();
        ElementParameter param = new ElementParameter(output);
        param.setName("USE_UPDATE_STATEMENT"); //$NON-NLS-1$
        param.setValue("true"); //$NON-NLS-1$
        paraList.add(param);
        output.setElementParameters(paraList);
        output.setComponent(targetComponent);

        DataConnection dataConnection = new DataConnection();
        dataConnection.setName(schema + "." + outTable1);
        dataConnection.setActivate(true);
        dataConnection.setLineStyle(EConnectionType.FLOW_MAIN);
        dataConnection.setTarget(output);
        IMetadataTable table = new MetadataTable();
        table.setLabel(outTable1);
        table.setTableName(outTable1);
        List<IMetadataColumn> listColumns = new ArrayList<IMetadataColumn>();
        for (String columnName : new String[] { "id", "name" }) {
            IMetadataColumn column = new MetadataColumn();
            column.setLabel(columnName);
            column.setOriginalDbColumnName(columnName);
            listColumns.add(column);
        }
        table.setListColumns(listColumns);
        dataConnection.setMetadataTable(table);
        // List<DataConnection> dataConnections = new ArrayList<>();
        outputConnections.add(dataConnection);
        outputConnections.add(connection);
        dbMapComponent.setOutgoingConnections(outputConnections);

        Process process = mock(Process.class);
        when(process.getContextManager()).thenReturn(new JobContextManager());
        dbMapComponent.setProcess(process);

        IContextParameter lookupTableContext = new JobContextParameter();
        lookupTableContext.setName("lookup");
        lookupTableContext.setValue("lookupTable");
        lookupTableContext.setType("String");
        String query = dbManager.buildSqlSelect(dbMapComponent, schema + "." + outTable1);

        String expectedQuery = "\"UPDATE \\\"dbo.tar\\\"\n" + "SET \\\"tarColumn\\\" = \\\"A\\\".\\\"id\\\",\n"
                + "\\\"tarColumn1\\\" = \\\"A\\\".\\\"name\\\"\n" + "FROM\n"
                + " \\\"\"+dbo+\"\\\".\\\"\"+src1+\"\\\" \\\"A\\\" INNER JOIN  \\\"\"+dbo+\"\\\".\\\"\"+src2+\"\\\" \\\"B\\\" ON(  \\\"B\\\".\\\"id\\\" = \\\"A\\\".\\\"id\\\" )\"";
        assertEquals(expectedQuery, query);

    }

    @Test
    public void testSelectWithContextAndQuotes() {
        String schema = "context.schema";
        String main_table = "context.main_table";
        String lookup_table = "context.lookup";
        String contextValue = "context.value";
        JobContext context = new JobContext("Default");

        IContextParameter schemaContext = new JobContextParameter();
        schemaContext.setName("schema");
        schemaContext.setValue("schema");
        schemaContext.setType("String");
        context.getContextParameterList().add(schemaContext);

        IContextParameter mainTableContext = new JobContextParameter();
        mainTableContext.setName("main_table");
        mainTableContext.setValue("mainTable");
        mainTableContext.setType("String");
        context.getContextParameterList().add(mainTableContext);

        IContextParameter lookupTableContext = new JobContextParameter();
        lookupTableContext.setName("lookup");
        lookupTableContext.setValue("lookupTable");
        lookupTableContext.setType("String");
        context.getContextParameterList().add(lookupTableContext);

        IContextParameter valueContext = new JobContextParameter();
        valueContext.setName("value");
        valueContext.setValue("999999");
        valueContext.setType("Integer");
        context.getContextParameterList().add(valueContext);

        List<IConnection> incomingConnections = new ArrayList<IConnection>();
        String[] mainTableEntities = new String[] { "id", "name", "classNum" };
        String[] lookupEndtities = new String[] { "id", "score" };
        incomingConnections.add(mockConnection(schema, main_table, mainTableEntities, mainTableEntities));
        incomingConnections.add(mockConnection(schema, lookup_table, lookupEndtities, lookupEndtities));
        dbMapComponent.setIncomingConnections(incomingConnections);

        ExternalDbMapData externalData = new ExternalDbMapData();
        List<ExternalDbMapTable> inputs = new ArrayList<ExternalDbMapTable>();
        List<ExternalDbMapTable> outputs = new ArrayList<ExternalDbMapTable>();
        // main table
        ExternalDbMapTable inputTable = new ExternalDbMapTable();
        inputTable.setTableName(schema + "." + main_table);
        inputTable.setName(schema + "." + main_table);
        List<ExternalDbMapEntry> entities = getMetadataEntities(mainTableEntities, new String[3]);

        ExternalDbMapEntry entity = new ExternalDbMapEntry();
        entity.setName("id");
        entity.setExpression(contextValue);
        entity.setOperator("=");
        entities.set(0, entity);
        inputTable.setMetadataTableEntries(entities);
        inputs.add(inputTable);

        // lookup table
        inputTable = new ExternalDbMapTable();
        inputTable.setTableName(schema + "." + lookup_table);
        inputTable.setName(schema + "." + lookup_table);
        entities = getMetadataEntities(lookupEndtities, new String[2]);
        ExternalDbMapEntry newColumn = entities.get(0);
        newColumn.setExpression(schema + "." + main_table + ".id");
        newColumn.setOperator("=");
        inputTable.setJoinType("INNER_JOIN");
        newColumn.setJoin(true);
        inputTable.setMetadataTableEntries(entities);
        inputs.add(inputTable);

        // output
        ExternalDbMapTable outputTable = new ExternalDbMapTable();
        outputTable.setName("grade");
        String[] names = new String[] { "id", "name", "classNum", "score" };
        String[] expressions = new String[] { schema + "." + main_table + ".id", schema + "." + main_table + ".name",
                schema + "." + main_table + ".classNum", schema + "." + lookup_table + ".score" };
        outputTable.setMetadataTableEntries(getMetadataEntities(names, expressions));
        outputs.add(outputTable);

        externalData.setInputTables(inputs);
        externalData.setOutputTables(outputs);
        dbMapComponent.setExternalData(externalData);
        List<IMetadataTable> metadataList = new ArrayList<IMetadataTable>();
        MetadataTable metadataTable = getMetadataTable(names, names);
        metadataTable.setLabel("grade");
        metadataList.add(metadataTable);
        dbMapComponent.setMetadataList(metadataList);
        Process process = mock(Process.class);
        when(process.getContextManager()).thenReturn(new JobContextManager());
        dbMapComponent.setProcess(process);
        dbMapComponent.getProcess().getContextManager().setDefaultContext(context);

        PostgresGenerationManager manager = new PostgresGenerationManager();
        manager.setAddQuotesInColumns(true);
        String query = manager.buildSqlSelect(dbMapComponent, "grade");

        String expectedQuery = "\"SELECT\n"
                + "\\\"\"+context.schema+\"\\\".\\\"\"+context.main_table+\"\\\".\\\"id\\\", \\\"\"+context.schema+\"\\\".\\\"\"+context.main_table+\"\\\".\\\"name\\\", \\\"\"+context.schema+\"\\\".\\\"\"+context.main_table+\"\\\".\\\"classNum\\\", \\\"\"+context.schema+\"\\\".\\\"\"+context.lookup+\"\\\".\\\"score\\\"\n"
                + "FROM\n"
                + " \\\"\"+context.schema+\"\\\".\\\"\"+context.main_table+\"\\\" INNER JOIN  \\\"\"+context.schema+\"\\\".\\\"\"+context.lookup+\"\\\" ON(  \\\"\"+context.schema+\"\\\".\\\"\"+context.lookup+\"\\\".\\\"id\\\" = \\\"\"+context.schema+\"\\\".\\\"\"+context.main_table+\"\\\".\\\"id\\\" )\n"
                + "WHERE\n"
                + "  \\\"\"+context.schema+\"\\\".\\\"\"+context.main_table+\"\\\".\\\"id\\\" = \" +context.value";

        assertEquals(expectedQuery, query);
    }

    @Test
    public void testSelectWithContextAndQuotesNoSchema() {
        String schema = "";
        String main_table = "context.main_table";
        String lookup_table = "context.lookup";
        String contextValue = "context.value";
        JobContext context = new JobContext("Default");

        IContextParameter mainTableContext = new JobContextParameter();
        mainTableContext.setName("main_table");
        mainTableContext.setValue("mainTable");
        mainTableContext.setType("String");
        context.getContextParameterList().add(mainTableContext);

        IContextParameter lookupTableContext = new JobContextParameter();
        lookupTableContext.setName("lookup");
        lookupTableContext.setValue("lookupTable");
        lookupTableContext.setType("String");
        context.getContextParameterList().add(lookupTableContext);

        IContextParameter valueContext = new JobContextParameter();
        valueContext.setName("value");
        valueContext.setValue("999999");
        valueContext.setType("Integer");
        context.getContextParameterList().add(valueContext);

        List<IConnection> incomingConnections = new ArrayList<IConnection>();
        String[] mainTableEntities = new String[] { "id", "name", "classNum" };
        String[] lookupEndtities = new String[] { "id", "score" };
        incomingConnections.add(mockConnection(schema, main_table, mainTableEntities, mainTableEntities));
        incomingConnections.add(mockConnection(schema, lookup_table, lookupEndtities, lookupEndtities));
        dbMapComponent.setIncomingConnections(incomingConnections);

        ExternalDbMapData externalData = new ExternalDbMapData();
        List<ExternalDbMapTable> inputs = new ArrayList<ExternalDbMapTable>();
        List<ExternalDbMapTable> outputs = new ArrayList<ExternalDbMapTable>();
        // main table
        ExternalDbMapTable inputTable = new ExternalDbMapTable();
        inputTable.setTableName(StringUtils.isNotBlank(schema) ? schema + "." + main_table : main_table);
        inputTable.setName(StringUtils.isNotBlank(schema) ? schema + "." + main_table : main_table);
        List<ExternalDbMapEntry> entities = getMetadataEntities(mainTableEntities, new String[3]);

        ExternalDbMapEntry entity = new ExternalDbMapEntry();
        entity.setName("id");
        entity.setExpression(contextValue);
        entity.setOperator("=");
        entities.set(0, entity);
        inputTable.setMetadataTableEntries(entities);
        inputs.add(inputTable);

        // lookup table
        inputTable = new ExternalDbMapTable();
        inputTable.setTableName(StringUtils.isNotBlank(schema) ? schema + "." + lookup_table : lookup_table);
        inputTable.setName(StringUtils.isNotBlank(schema) ? schema + "." + lookup_table : lookup_table);
        entities = getMetadataEntities(lookupEndtities, new String[2]);
        ExternalDbMapEntry newColumn = entities.get(0);
        newColumn.setExpression(StringUtils.isNotBlank(schema) ? schema + "." + main_table + ".id" : main_table + ".id");
        newColumn.setOperator("=");
        inputTable.setJoinType("INNER_JOIN");
        newColumn.setJoin(true);
        inputTable.setMetadataTableEntries(entities);
        inputs.add(inputTable);

        // output
        ExternalDbMapTable outputTable = new ExternalDbMapTable();
        outputTable.setName("grade");
        String[] names = new String[] { "id", "name", "classNum", "score" };
        String[] expressions = new String[] { schema + "." + main_table + ".id", schema + "." + main_table + ".name",
                schema + "." + main_table + ".classNum", schema + "." + lookup_table + ".score" };
        if (StringUtils.isBlank(schema)) {
            expressions = new String[] { main_table + ".id", main_table + ".name", main_table + ".classNum",
                    lookup_table + ".score" };
        }
        outputTable.setMetadataTableEntries(getMetadataEntities(names, expressions));
        outputs.add(outputTable);

        externalData.setInputTables(inputs);
        externalData.setOutputTables(outputs);
        dbMapComponent.setExternalData(externalData);
        List<IMetadataTable> metadataList = new ArrayList<IMetadataTable>();
        MetadataTable metadataTable = getMetadataTable(names, names);
        metadataTable.setLabel("grade");
        metadataList.add(metadataTable);
        dbMapComponent.setMetadataList(metadataList);
        Process process = mock(Process.class);
        when(process.getContextManager()).thenReturn(new JobContextManager());
        dbMapComponent.setProcess(process);
        dbMapComponent.getProcess().getContextManager().setDefaultContext(context);

        PostgresGenerationManager manager = new PostgresGenerationManager();
        manager.setAddQuotesInColumns(true);
        String query = manager.buildSqlSelect(dbMapComponent, "grade");

        String expectedQuery = "\"SELECT\n"
                + "\\\"\"+context.main_table+\"\\\".id, \\\"\"+context.main_table+\"\\\".name, \\\"\"+context.main_table+\"\\\".classNum, \\\"\"+context.lookup+\"\\\".score\n"
                + "FROM\n"
                + " context.main_table INNER JOIN  context.lookup ON(  context.lookup.\\\"id\\\" = \\\"\"+context.main_table+\"\\\".id )\n"
                + "WHERE\n" + "  context.main_table.\\\"id\\\" = \" +context.value";
        assertEquals(expectedQuery, query);
    }

}
