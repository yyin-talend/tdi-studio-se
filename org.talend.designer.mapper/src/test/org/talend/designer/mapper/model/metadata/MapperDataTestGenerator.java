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
package org.talend.designer.mapper.model.metadata;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.talend.core.model.components.IComponent;
import org.talend.core.model.components.IODataComponent;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.MetadataColumn;
import org.talend.core.model.metadata.MetadataTable;
import org.talend.core.model.metadata.MetadataTalendType;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.INodeReturn;
import org.talend.core.model.process.IProcess;
import org.talend.designer.mapper.external.data.ExternalMapperData;
import org.talend.designer.mapper.external.data.ExternalMapperTable;
import org.talend.designer.mapper.external.data.ExternalMapperTableEntry;
import org.talend.designer.mapper.language.ILanguage;
import org.talend.designer.mapper.language.generation.GenerationManager;
import org.talend.designer.mapper.language.generation.GenerationManagerFactory;
import org.talend.designer.mapper.language.generation.TableType;
import org.talend.designer.mapper.model.table.VarsTable;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class MapperDataTestGenerator {

    private static final int N_TABLES = 4;

    private static final int N_FIELDS = 2;

    private boolean fixedData = true;

    private static final String COLUMN_NAME = "column";

    private static final String STRING_TYPE = "id_String";

    private static final String INTEGER_TYPE = "id_Integer";

    private GenerationManager gen;

    /**
     * 
     * DOC amaumont MapperDataTestGenerator class global comment. Detailled comment <br/>
     * 
     * $Id$
     * 
     */
    enum TEST {
        ONE_INNER_JOIN_AND_NOT_ANY_REJECT_AND_ONE_OUT_TABLE_WITHOUT_FILTER,
        ONE_INNER_JOIN_AND_ONLY_ONE_INNER_JOIN_REJECT_AND_ONE_OUT_TABLE_WITHOUT_FILTER,
        NO_INNER_JOIN_AND_ONLY_ONE_INNER_JOIN_REJECT_AND_ONE_OUT_TABLE_WITHOUT_FILTER,
        ONE_INNER_JOIN_AND_A_TABLE_WITH_REJECT_AND_INNER_JOIN_REJECT_AND_ONE_OUT_TABLE_WITHOUT_FILTER,
        NO_INNER_JOIN_AND_ONE_INNER_JOIN_REJECT_AND_2_REJECT_TABLE_WITHOUT_FILTER,
        NO_INNER_JOIN_AND_A_TABLE_WITH_FILTER_AND_A_TABLE_WITH_REJECT,
        ONE_INNER_JOIN_AND_2_TABLES_WITH_FILTER_AND_OTHER_TABLE_WITH_REJECT_AND_REJECT_INNER_JOIN,
        ONE_INNER_JOIN_AND_A_TABLE_WITH_FILTER_AND_A_TABLE_WITH_REJECT_AND_A_TABLE_WITH_REJECT_INNER_JOIN,
        MAIN_CONNECTION_WITH_INNER_JOIN_AND_A_TABLE_WITH_FILTER_AND_A_TABLE_WITH_REJECT_AND_A_TABLE_WITH_REJECT_INNER_JOIN,
        ONE_INNER_JOIN_AND_A_TABLE_WITH_FILTER_AND_A_TABLE_WITH_REJECT,
        ONE_INNER_JOIN_AND_A_TABLE_WITH_INNER_JOIN_REJECT_WITHOUT_REGULAR_TABLE,
        THREE_TABLES_INPUT_WITH_ONE_INNER_JOIN_AND_NO_OUTPUT_TABLE_AND_NO_CONNECTION,
        THREE_TABLES_INPUT_WITH_ONE_INNER_JOIN_AND_ONE_OUTPUT_TABLE_AND_NO_CONNECTION,
    };

    TEST currentTest = TEST.THREE_TABLES_INPUT_WITH_ONE_INNER_JOIN_AND_ONE_OUTPUT_TABLE_AND_NO_CONNECTION;

    public MapperDataTestGenerator(ILanguage language, boolean random) {
        super();

        this.random = random;

        gen = GenerationManagerFactory.getInstance().getGenerationManager(language);

        generateConnectionListIn();
        generateConnectionListOut();
        generateExternalData();
        // generateMetadataListOut();
    }

    public final Random rand = new Random();

    private static final String[] FIELDS = new String[] { "id", "name", "street", "address", "city", "country", "zip" };

    private int inputsCounter; // remove finally

    private int outputsCounter; // remove finally

    private boolean random;

    private ArrayList<IConnection> inputConnectionList;

    private boolean useConnectionsToGenerateExternalTables = true;

    private ExternalMapperData externalData;

    private ArrayList<IMetadataTable> metadataListOut;

    private ArrayList<IConnection> outputConnectionList;

    public void resetCounters() {
        inputsCounter = 0; // remove finally

        outputsCounter = 0; // remove finally

    }

    public List<IMetadataTable> generateMedataTables(int nb, int j) {

        ArrayList<IMetadataTable> list = new ArrayList<IMetadataTable>();

        for (int i = 0; i < nb; i++) {
            IMetadataTable metadataTable = null;
            if (j == 1) {
                metadataTable = initInputsDataMapTable();
            }
            if (j == 2) {
                metadataTable = initVarsDataMapTable();
            }
            if (j == 3) {
                metadataTable = initOutputsDataMapTable();
            }

            list.add(metadataTable);
        }
        return list;
    }

    /**
     * DOC amaumont Comment method "initProcessDataMapTable".
     * 
     * @return
     */
    private IMetadataTable initVarsDataMapTable() {
        MetadataTable metadataTable = new MetadataTable();
        metadataTable.setTableName(VarsTable.PREFIX_VARS_TABLE_NAME);

        ArrayList<IMetadataColumn> metadatColumns = new ArrayList<IMetadataColumn>();

        MetadataColumn metadataColumn = new MetadataColumn();
        metadataColumn.setLabel("id");
        metadataColumn.setKey(true);
        metadataColumn.setType(INTEGER_TYPE);
        metadatColumns.add(metadataColumn);

        metadataColumn = new MetadataColumn();
        metadataColumn.setLabel("name");
        metadataColumn.setKey(false);
        metadataColumn.setType(STRING_TYPE);
        metadatColumns.add(metadataColumn);

        metadataTable.setListColumns(metadatColumns);
        return metadataTable;
    }

    /**
     * DOC amaumont Comment method "initMetadataTable".
     * 
     * @return
     */
    private IMetadataTable initInputsDataMapTable() {
        MetadataTable metadataTable = new MetadataTable();
        metadataTable.setTableName("in" + ++inputsCounter);

        ArrayList<IMetadataColumn> metadatColumns = new ArrayList<IMetadataColumn>();

        MetadataColumn metadataColumn = new MetadataColumn();
        metadataColumn.setLabel("id");
        metadataColumn.setKey(true);
        metadataColumn.setType(INTEGER_TYPE);
        metadatColumns.add(metadataColumn);

        metadataColumn = new MetadataColumn();
        metadataColumn.setLabel("name");
        metadataColumn.setKey(false);
        metadataColumn.setType(INTEGER_TYPE);
        metadatColumns.add(metadataColumn);
        /*
         * metadataColumn = new MetadataColumn(); metadataColumn.setLabel("name2"); metadataColumn.setKey(false);
         * metadataColumn.setType(EMetadataType.STRING); metadatColumns.add(metadataColumn);
         * 
         * metadataColumn = new MetadataColumn(); metadataColumn.setLabel("name3"); metadataColumn.setKey(false);
         * metadataColumn.setType(EMetadataType.STRING); metadatColumns.add(metadataColumn);
         * 
         * metadataColumn = new MetadataColumn(); metadataColumn.setLabel("name4"); metadataColumn.setKey(false);
         * metadataColumn.setType(EMetadataType.STRING); metadatColumns.add(metadataColumn);
         * 
         * metadataColumn = new MetadataColumn(); metadataColumn.setLabel("name5"); metadataColumn.setKey(false);
         * metadataColumn.setType(EMetadataType.STRING); metadatColumns.add(metadataColumn);
         * 
         * metadataColumn = new MetadataColumn(); metadataColumn.setLabel("name6"); metadataColumn.setKey(false);
         * metadataColumn.setType(EMetadataType.STRING); metadatColumns.add(metadataColumn);
         */
        metadataTable.setListColumns(metadatColumns);

        return metadataTable;

    }

    /**
     * DOC amaumont Comment method "initMetadataTable".
     * 
     * @return
     */
    private IMetadataTable initOutputsDataMapTable() {
        MetadataTable metadataTable = new MetadataTable();
        metadataTable.setTableName("out" + ++outputsCounter);

        ArrayList<IMetadataColumn> metadatColumns = new ArrayList<IMetadataColumn>();

        MetadataColumn metadataColumn = new MetadataColumn();
        metadataColumn.setLabel("newId");
        metadataColumn.setKey(true);
        metadataColumn.setType(INTEGER_TYPE);
        metadatColumns.add(metadataColumn);

        metadataColumn = new MetadataColumn();
        metadataColumn.setLabel("newName");
        metadataColumn.setKey(false);
        metadataColumn.setType(INTEGER_TYPE);
        metadatColumns.add(metadataColumn);

        metadataTable.setListColumns(metadatColumns);

        return metadataTable;

    }

    /**
     * DOC amaumont Comment method "generateMedataPersistentData".
     * 
     * @param random TODO
     * 
     * @return
     */
    public Object generateExternalData() {

        if (random) {
            return generateRandomExternalData();
        } else {
            return generateConstantExternalData();
        }

    }

    /**
     * DOC amaumont Comment method "generateRandomPersistentData".
     * 
     * @return
     */
    private Object generateRandomExternalData() {
        externalData = new ExternalMapperData();

        externalData.setInputTables(generateExternalTables(TableType.INPUT, new TableType[] { TableType.INPUT }, 5, 5));
        externalData.setVarsTables(generateExternalTables(TableType.VARS, new TableType[] { TableType.INPUT }, 20));
        externalData.setOutputTables(generateExternalTables(TableType.OUTPUT, new TableType[] { TableType.INPUT,
                TableType.VARS }, 20));

        return externalData;
    }

    private List<ExternalMapperTable> generateExternalTables(TableType tableType, TableType[] tableTypes,
            int nExpressionsMax) {
        return generateExternalTables(tableType, tableTypes, nExpressionsMax, null);
    }

    private List<ExternalMapperTable> generateExternalTables(TableType tableType, TableType[] tableTypes,
            int nFieldsMaxInExpression, Integer nExpressionsMax) {
        List<ExternalMapperTable> tables = new ArrayList<ExternalMapperTable>();
        if (this.useConnectionsToGenerateExternalTables && tableType == TableType.INPUT) {

            for (IConnection connection : inputConnectionList) {

                ExternalMapperTable mapperTable = new ExternalMapperTable();

                mapperTable.setName(connection.getName());

                IMetadataTable metadataTable = connection.getMetadataTable();

                List<ExternalMapperTableEntry> tableEntries = new ArrayList<ExternalMapperTableEntry>();
                List<IMetadataColumn> listColumns = metadataTable.getListColumns();
                int nExpressions = 0;
                for (IMetadataColumn column : listColumns) {
                    ExternalMapperTableEntry mapperTableEntry = new ExternalMapperTableEntry();
                    mapperTableEntry.setName(column.getLabel());
                    if (!fixedData && rand.nextBoolean()) {
                        if (nExpressionsMax == null || nExpressions <= nExpressionsMax) {
                            mapperTableEntry.setExpression(generateExpression(tableTypes, N_FIELDS, rand
                                    .nextInt(nFieldsMaxInExpression), nExpressions));
                            nExpressions++;
                        }
                    }
                    tableEntries.add(mapperTableEntry);
                }
                mapperTable.setMetadataTableEntries(tableEntries);

                tables.add(mapperTable);

            }

        } else {

            for (int i = 1; i <= tableType.getNTables(); i++) {

                ExternalMapperTable mapperTable = new ExternalMapperTable();

                String tableName = tableType.getBaseTableName();
                if (tableType != TableType.VARS) {
                    tableName += i;
                }
                mapperTable.setName(tableName);

                List<ExternalMapperTableEntry> tableEntries = new ArrayList<ExternalMapperTableEntry>();

                for (int j = 1; j <= N_FIELDS; j++) {
                    ExternalMapperTableEntry mapperTableEntry = new ExternalMapperTableEntry();
                    String baseColumnName = FIELDS[rand.nextInt(FIELDS.length)];
                    if (fixedData) {
                        baseColumnName = COLUMN_NAME;
                    }
                    mapperTableEntry.setName(baseColumnName + j);
                    mapperTableEntry.setExpression(generateExpression(tableTypes, N_FIELDS, rand
                            .nextInt(nFieldsMaxInExpression), j));
                    tableEntries.add(mapperTableEntry);
                }

                mapperTable.setMetadataTableEntries(tableEntries);

                tables.add(mapperTable);
            }
        }
        return tables;

    }

    /**
     * DOC amaumont Comment method "generateExpression".
     * 
     * @param nExpressions
     * 
     * @param string
     * @param i
     * @return
     */
    private String generateExpression(TableType[] tables2, int nFields, int nExpressions, int currentIndex) {
        String expression = "";
        if (fixedData) {
            for (int iTable = 0; iTable < N_TABLES; iTable++) {
                // for (int iField = 0; iField < tables2.length; iField++) {
                for (int i = 0; i < tables2.length; i++) {
                    TableType tableType = tables2[i];
                    expression += gen.getTableColumnVariable(tableType.getBaseTableName() + (iTable + 1), COLUMN_NAME
                            + (currentIndex));
                }
                // }
            }
        } else {
            for (int j = 0; j < nExpressions; j++) {
                TableType tableType = tables2[rand.nextInt(tables2.length)];
                expression += (rand.nextInt(4) == 0 ? "\n" : "")
                        + (rand.nextBoolean() ? " + " : " - ")
                        + gen.getTableColumnVariable(tableType.getBaseTableName()
                                + (tableType != TableType.VARS ? (rand.nextInt(tableType.getNTables()) + 1) : ""),
                                FIELDS[rand.nextInt(FIELDS.length)] + (rand.nextInt(nFields) + 1))
                        + (rand.nextInt(4) == 0 ? (rand.nextBoolean() ? " + " : " - ") + "$array_var"
                                + rand.nextInt(10) + "[test_var]" : "")
                        + (rand.nextInt(4) == 0 ? (rand.nextBoolean() ? " + " : " - ") + "$hash_var" + rand.nextInt(10)
                                + "{test_var}" : "")
                        + (rand.nextInt(4) == 0 ? (rand.nextBoolean() ? " + " : " - ") + "$var" + rand.nextInt(10) : "");
            }
        }
        return expression;
    }

    /**
     * DOC amaumont Comment method "generateConstantPeristentData".
     * 
     * @return
     */
    private Object generateConstantExternalData() {
        externalData = new ExternalMapperData();

        List<ExternalMapperTable> tables = new ArrayList<ExternalMapperTable>();

        // ///////////////////////////////////////////
        // INPUTS

        ExternalMapperTable mapperTable = new ExternalMapperTable();

        if (currentTest == TEST.MAIN_CONNECTION_WITH_INNER_JOIN_AND_A_TABLE_WITH_FILTER_AND_A_TABLE_WITH_REJECT_AND_A_TABLE_WITH_REJECT_INNER_JOIN) {
            mapperTable.setInnerJoin(true);
        } else if (currentTest == TEST.ONE_INNER_JOIN_AND_A_TABLE_WITH_FILTER_AND_A_TABLE_WITH_REJECT
                || currentTest == TEST.ONE_INNER_JOIN_AND_A_TABLE_WITH_INNER_JOIN_REJECT_WITHOUT_REGULAR_TABLE
                || currentTest == TEST.THREE_TABLES_INPUT_WITH_ONE_INNER_JOIN_AND_NO_OUTPUT_TABLE_AND_NO_CONNECTION
                || currentTest == TEST.THREE_TABLES_INPUT_WITH_ONE_INNER_JOIN_AND_ONE_OUTPUT_TABLE_AND_NO_CONNECTION
                
        ) {
            mapperTable.setInnerJoin(false);
        } else {
            mapperTable.setInnerJoin(false);
        }

        mapperTable.setName("book");

        List<ExternalMapperTableEntry> tableEntries = new ArrayList<ExternalMapperTableEntry>();

        ExternalMapperTableEntry mapperTableEntry = new ExternalMapperTableEntry();
        mapperTableEntry.setName("id");
        mapperTableEntry.setExpression("test");
        mapperTableEntry.setType(INTEGER_TYPE);
        tableEntries.add(mapperTableEntry);

        mapperTableEntry = new ExternalMapperTableEntry();
        mapperTableEntry.setName("name");
        mapperTableEntry.setExpression("");
        mapperTableEntry.setType(STRING_TYPE);
        tableEntries.add(mapperTableEntry);

        mapperTableEntry = new ExternalMapperTableEntry();
        mapperTableEntry.setName("nb_pages");
        mapperTableEntry.setExpression("");
        mapperTableEntry.setType(INTEGER_TYPE);
        tableEntries.add(mapperTableEntry);

        mapperTable.setMetadataTableEntries(tableEntries);

        tables.add(mapperTable);

        // #########################################################################

        mapperTable = new ExternalMapperTable();
        mapperTable.setName("page");

        if (currentTest == TEST.ONE_INNER_JOIN_AND_NOT_ANY_REJECT_AND_ONE_OUT_TABLE_WITHOUT_FILTER
                || currentTest == TEST.ONE_INNER_JOIN_AND_ONLY_ONE_INNER_JOIN_REJECT_AND_ONE_OUT_TABLE_WITHOUT_FILTER
                || currentTest == TEST.NO_INNER_JOIN_AND_ONLY_ONE_INNER_JOIN_REJECT_AND_ONE_OUT_TABLE_WITHOUT_FILTER
                || currentTest == TEST.NO_INNER_JOIN_AND_ONE_INNER_JOIN_REJECT_AND_2_REJECT_TABLE_WITHOUT_FILTER
                || currentTest == TEST.ONE_INNER_JOIN_AND_A_TABLE_WITH_REJECT_AND_INNER_JOIN_REJECT_AND_ONE_OUT_TABLE_WITHOUT_FILTER
                || currentTest == TEST.MAIN_CONNECTION_WITH_INNER_JOIN_AND_A_TABLE_WITH_FILTER_AND_A_TABLE_WITH_REJECT_AND_A_TABLE_WITH_REJECT_INNER_JOIN
                || currentTest == TEST.ONE_INNER_JOIN_AND_A_TABLE_WITH_FILTER_AND_A_TABLE_WITH_REJECT
                || currentTest == TEST.ONE_INNER_JOIN_AND_A_TABLE_WITH_INNER_JOIN_REJECT_WITHOUT_REGULAR_TABLE
                || currentTest == TEST.THREE_TABLES_INPUT_WITH_ONE_INNER_JOIN_AND_NO_OUTPUT_TABLE_AND_NO_CONNECTION
                || currentTest == TEST.THREE_TABLES_INPUT_WITH_ONE_INNER_JOIN_AND_ONE_OUTPUT_TABLE_AND_NO_CONNECTION
        ) {
            mapperTable.setInnerJoin(false);
        } else {
            mapperTable.setInnerJoin(false);
        }

        tableEntries = new ArrayList<ExternalMapperTableEntry>();

        mapperTableEntry = new ExternalMapperTableEntry();
        mapperTableEntry.setName("id_page");
        mapperTableEntry.setExpression("");
        mapperTableEntry.setType(INTEGER_TYPE);
        tableEntries.add(mapperTableEntry);

        mapperTableEntry = new ExternalMapperTableEntry();
        mapperTableEntry.setName("id_book");
        mapperTableEntry.setExpression(gen.getTableColumnVariable("book", "id"));
        mapperTableEntry.setType(STRING_TYPE);
        tableEntries.add(mapperTableEntry);

        mapperTableEntry = new ExternalMapperTableEntry();
        mapperTableEntry.setName("content");
        mapperTableEntry.setExpression(gen.getTableColumnVariable("book", "name"));
        mapperTableEntry.setType(INTEGER_TYPE);
        tableEntries.add(mapperTableEntry);

        mapperTable.setMetadataTableEntries(tableEntries);

        if (currentTest == TEST.ONE_INNER_JOIN_AND_A_TABLE_WITH_INNER_JOIN_REJECT_WITHOUT_REGULAR_TABLE) {
            // nothing
        } else {
            tables.add(mapperTable);
        }

        // #########################################################################

        mapperTable = new ExternalMapperTable();
        mapperTable.setName("userInput");

        if (currentTest == TEST.ONE_INNER_JOIN_AND_NOT_ANY_REJECT_AND_ONE_OUT_TABLE_WITHOUT_FILTER
                || currentTest == TEST.ONE_INNER_JOIN_AND_ONLY_ONE_INNER_JOIN_REJECT_AND_ONE_OUT_TABLE_WITHOUT_FILTER
                || currentTest == TEST.ONE_INNER_JOIN_AND_2_TABLES_WITH_FILTER_AND_OTHER_TABLE_WITH_REJECT_AND_REJECT_INNER_JOIN
                || currentTest == TEST.ONE_INNER_JOIN_AND_A_TABLE_WITH_FILTER_AND_A_TABLE_WITH_REJECT_AND_A_TABLE_WITH_REJECT_INNER_JOIN
                || currentTest == TEST.ONE_INNER_JOIN_AND_A_TABLE_WITH_REJECT_AND_INNER_JOIN_REJECT_AND_ONE_OUT_TABLE_WITHOUT_FILTER
                || currentTest == TEST.ONE_INNER_JOIN_AND_A_TABLE_WITH_FILTER_AND_A_TABLE_WITH_REJECT
                || currentTest == TEST.ONE_INNER_JOIN_AND_A_TABLE_WITH_INNER_JOIN_REJECT_WITHOUT_REGULAR_TABLE
                || currentTest == TEST.THREE_TABLES_INPUT_WITH_ONE_INNER_JOIN_AND_ONE_OUTPUT_TABLE_AND_NO_CONNECTION
                || currentTest == TEST.THREE_TABLES_INPUT_WITH_ONE_INNER_JOIN_AND_NO_OUTPUT_TABLE_AND_NO_CONNECTION) {
            mapperTable.setInnerJoin(true);
        } else if (currentTest == TEST.NO_INNER_JOIN_AND_ONLY_ONE_INNER_JOIN_REJECT_AND_ONE_OUT_TABLE_WITHOUT_FILTER
                || currentTest == TEST.NO_INNER_JOIN_AND_ONE_INNER_JOIN_REJECT_AND_2_REJECT_TABLE_WITHOUT_FILTER
                || currentTest == TEST.MAIN_CONNECTION_WITH_INNER_JOIN_AND_A_TABLE_WITH_FILTER_AND_A_TABLE_WITH_REJECT_AND_A_TABLE_WITH_REJECT_INNER_JOIN
                || currentTest == TEST.ONE_INNER_JOIN_AND_A_TABLE_WITH_FILTER_AND_A_TABLE_WITH_REJECT) {
            mapperTable.setInnerJoin(false);
        } else {
            mapperTable.setInnerJoin(false);
        }

        tableEntries = new ArrayList<ExternalMapperTableEntry>();

        mapperTableEntry = new ExternalMapperTableEntry();
        mapperTableEntry.setName("id");
        mapperTableEntry.setExpression("");
        mapperTableEntry.setType(STRING_TYPE);
        tableEntries.add(mapperTableEntry);

        mapperTableEntry = new ExternalMapperTableEntry();
        mapperTableEntry.setName("id_book");
        mapperTableEntry.setExpression(gen.getTableColumnVariable("book", "id"));
        mapperTableEntry.setType(STRING_TYPE);
        tableEntries.add(mapperTableEntry);

        if (currentTest == TEST.ONE_INNER_JOIN_AND_A_TABLE_WITH_INNER_JOIN_REJECT_WITHOUT_REGULAR_TABLE) {
            // nothing
        } else {
            mapperTableEntry = new ExternalMapperTableEntry();
            mapperTableEntry.setName("id_page");
            mapperTableEntry.setType(STRING_TYPE);
            mapperTableEntry.setExpression(gen.getTableColumnVariable("page", "id_page") + " . "
                    + gen.getTableColumnVariable("book", "name"));
            tableEntries.add(mapperTableEntry);
        }

        mapperTable.setMetadataTableEntries(tableEntries);

        tables.add(mapperTable);

        // #########################################################################
        // #########################################################################

        externalData.setInputTables(tables);

        // INPUTS
        // ///////////////////////////////////////////

        // ///////////////////////////////////////////
        // VARS

        tables = new ArrayList<ExternalMapperTable>();

        // #########################################################################
        // #########################################################################

        mapperTable = new ExternalMapperTable();
        mapperTable.setName(VarsTable.PREFIX_VARS_TABLE_NAME);

        tableEntries = new ArrayList<ExternalMapperTableEntry>();

        mapperTableEntry = new ExternalMapperTableEntry();
        mapperTableEntry.setName("upperCaseContent");
        mapperTableEntry.setType(STRING_TYPE);
        mapperTableEntry.setExpression("uc " + gen.getTableColumnVariable("page", "content") + " + "
                + gen.getTableColumnVariable("book", "id_book") + " - 2 * "
                + gen.getTableColumnVariable("book", "id_book"));
        tableEntries.add(mapperTableEntry);

        mapperTableEntry = new ExternalMapperTableEntry();
        mapperTableEntry.setName("newId");
        mapperTableEntry.setExpression(gen.getTableColumnVariable("book", "id_book") + " + 1");
        mapperTableEntry.setType(STRING_TYPE);
        tableEntries.add(mapperTableEntry);

        mapperTable.setMetadataTableEntries(tableEntries);

        tables.add(mapperTable);

        // #########################################################################
        // #########################################################################

        externalData.setVarsTables(tables);

        // VARS
        // ///////////////////////////////////////////

        // ///////////////////////////////////////////
        // OUTPUTS

        tables = new ArrayList<ExternalMapperTable>();

        // #########################################################################
        // #########################################################################

        mapperTable = new ExternalMapperTable();
        mapperTable.setName("user");

        tableEntries = new ArrayList<ExternalMapperTableEntry>();

        mapperTableEntry = new ExternalMapperTableEntry();
        mapperTableEntry.setName("idUser");
        mapperTableEntry.setExpression(gen.getTableColumnVariable(VarsTable.PREFIX_VARS_TABLE_NAME, "newId"));
        mapperTableEntry.setType(STRING_TYPE);
        tableEntries.add(mapperTableEntry);

        mapperTableEntry = new ExternalMapperTableEntry();
        mapperTableEntry.setName("idBook");
        mapperTableEntry.setExpression(gen.getTableColumnVariable("book", "id_book"));
        mapperTableEntry.setType(STRING_TYPE);
        tableEntries.add(mapperTableEntry);

        mapperTableEntry = new ExternalMapperTableEntry();
        mapperTableEntry.setName("content");
        mapperTableEntry.setExpression("");
        mapperTableEntry.setType(INTEGER_TYPE);
        tableEntries.add(mapperTableEntry);

        mapperTable.setMetadataTableEntries(tableEntries);

        // CONSTRAINTS

        tableEntries = new ArrayList<ExternalMapperTableEntry>();

        mapperTableEntry = new ExternalMapperTableEntry();
        mapperTableEntry.setExpression(" $book[id] == 1");
        mapperTableEntry.setExpression(" " + gen.getTableColumnVariable("book", "id") + " == 1");
        tableEntries.add(mapperTableEntry);

        mapperTable.setConstraintTableEntries(tableEntries);

        if (currentTest == TEST.ONE_INNER_JOIN_AND_A_TABLE_WITH_INNER_JOIN_REJECT_WITHOUT_REGULAR_TABLE
                || currentTest == TEST.THREE_TABLES_INPUT_WITH_ONE_INNER_JOIN_AND_NO_OUTPUT_TABLE_AND_NO_CONNECTION
                ) {
            // nothing
        } else {
            tables.add(mapperTable);
        }

        // #########################################################################
        // #########################################################################

        mapperTable = new ExternalMapperTable();
        mapperTable.setName("newBook");

        tableEntries = new ArrayList<ExternalMapperTableEntry>();

        mapperTableEntry = new ExternalMapperTableEntry();
        mapperTableEntry.setName("newIdBook");
        mapperTableEntry.setExpression(gen.getTableColumnVariable(VarsTable.PREFIX_VARS_TABLE_NAME, "newId"));
        mapperTableEntry.setType(STRING_TYPE);
        tableEntries.add(mapperTableEntry);

        mapperTableEntry = new ExternalMapperTableEntry();
        mapperTableEntry.setName("id_book");
        mapperTableEntry.setExpression(gen.getTableColumnVariable("book", "id_book"));
        mapperTableEntry.setType(STRING_TYPE);
        tableEntries.add(mapperTableEntry);

        mapperTableEntry = new ExternalMapperTableEntry();
        mapperTableEntry.setName("content");
        mapperTableEntry.setType(STRING_TYPE);
        mapperTableEntry.setExpression("");
        tableEntries.add(mapperTableEntry);

        mapperTable.setMetadataTableEntries(tableEntries);

        tableEntries = new ArrayList<ExternalMapperTableEntry>();

        // #########################################################################
        // CONSTRAINTS newBook

        mapperTableEntry = new ExternalMapperTableEntry();
        mapperTableEntry.setExpression(" test == 3 ");
        mapperTableEntry.setType(STRING_TYPE);
        tableEntries.add(mapperTableEntry);

        // mapperTableEntry = new ExternalMapperTableEntry();
        // mapperTableEntry.setExpression(gen.getTableColumnVariable(VarsTable.PREFIX_VARS_TABLE_NAME, "newId") + " ==
        // 1");
        // tableEntries.add(mapperTableEntry);
        //
        // mapperTableEntry = new ExternalMapperTableEntry();
        // mapperTableEntry.setExpression(gen.getTableColumnVariable(VarsTable.PREFIX_VARS_TABLE_NAME, "newId") + " ==
        // 2");
        // tableEntries.add(mapperTableEntry);
        //
        // mapperTableEntry = new ExternalMapperTableEntry();
        // mapperTableEntry.setExpression(gen.getTableColumnVariable(VarsTable.PREFIX_VARS_TABLE_NAME, "newId") + " ==
        // 3");
        // tableEntries.add(mapperTableEntry);
        //
        mapperTable.setConstraintTableEntries(tableEntries);

        if (currentTest == TEST.NO_INNER_JOIN_AND_A_TABLE_WITH_FILTER_AND_A_TABLE_WITH_REJECT
                || currentTest == TEST.NO_INNER_JOIN_AND_A_TABLE_WITH_FILTER_AND_A_TABLE_WITH_REJECT
                || currentTest == TEST.ONE_INNER_JOIN_AND_A_TABLE_WITH_INNER_JOIN_REJECT_WITHOUT_REGULAR_TABLE
                || currentTest == TEST.THREE_TABLES_INPUT_WITH_ONE_INNER_JOIN_AND_NO_OUTPUT_TABLE_AND_NO_CONNECTION
                || currentTest == TEST.THREE_TABLES_INPUT_WITH_ONE_INNER_JOIN_AND_ONE_OUTPUT_TABLE_AND_NO_CONNECTION

        ) {

        } else {
            tables.add(mapperTable);
        }

        // #########################################################################
        // #########################################################################

        // REJECTS

        // #########################################################################
        // #########################################################################

        mapperTable = new ExternalMapperTable();
        mapperTable.setName("newPageRejected");

        if (currentTest == TEST.ONE_INNER_JOIN_AND_NOT_ANY_REJECT_AND_ONE_OUT_TABLE_WITHOUT_FILTER
                || currentTest == TEST.ONE_INNER_JOIN_AND_ONLY_ONE_INNER_JOIN_REJECT_AND_ONE_OUT_TABLE_WITHOUT_FILTER
                || currentTest == TEST.NO_INNER_JOIN_AND_ONLY_ONE_INNER_JOIN_REJECT_AND_ONE_OUT_TABLE_WITHOUT_FILTER
                || currentTest == TEST.ONE_INNER_JOIN_AND_A_TABLE_WITH_REJECT_AND_INNER_JOIN_REJECT_AND_ONE_OUT_TABLE_WITHOUT_FILTER) {
            mapperTable.setReject(false);
            mapperTable.setRejectInnerJoin(false);
        } else if (currentTest == TEST.NO_INNER_JOIN_AND_ONE_INNER_JOIN_REJECT_AND_2_REJECT_TABLE_WITHOUT_FILTER) {
            mapperTable.setReject(true);
            mapperTable.setRejectInnerJoin(true);
        } else if (currentTest == TEST.MAIN_CONNECTION_WITH_INNER_JOIN_AND_A_TABLE_WITH_FILTER_AND_A_TABLE_WITH_REJECT_AND_A_TABLE_WITH_REJECT_INNER_JOIN
                || currentTest == TEST.ONE_INNER_JOIN_AND_A_TABLE_WITH_INNER_JOIN_REJECT_WITHOUT_REGULAR_TABLE) {
            mapperTable.setReject(false);
            mapperTable.setRejectInnerJoin(true);
        } else if (currentTest == TEST.ONE_INNER_JOIN_AND_A_TABLE_WITH_FILTER_AND_A_TABLE_WITH_REJECT_AND_A_TABLE_WITH_REJECT_INNER_JOIN
                || currentTest == TEST.ONE_INNER_JOIN_AND_A_TABLE_WITH_FILTER_AND_A_TABLE_WITH_REJECT) {
            mapperTable.setReject(true);
            mapperTable.setRejectInnerJoin(false);
        } else if (currentTest == TEST.NO_INNER_JOIN_AND_A_TABLE_WITH_FILTER_AND_A_TABLE_WITH_REJECT
                || currentTest == TEST.NO_INNER_JOIN_AND_A_TABLE_WITH_FILTER_AND_A_TABLE_WITH_REJECT) {
            mapperTable.setReject(true);
            mapperTable.setRejectInnerJoin(false);
        } else {
            mapperTable.setReject(true);
            mapperTable.setRejectInnerJoin(true);
        }

        tableEntries = new ArrayList<ExternalMapperTableEntry>();

        mapperTableEntry = new ExternalMapperTableEntry();
        mapperTableEntry.setName("id_page");
        mapperTableEntry.setExpression("");
        mapperTableEntry.setType(INTEGER_TYPE);
        tableEntries.add(mapperTableEntry);

        mapperTableEntry = new ExternalMapperTableEntry();
        mapperTableEntry.setName("id_book");
        mapperTableEntry.setExpression(gen.getTableColumnVariable("page", "id_book"));
        mapperTableEntry.setType(STRING_TYPE);
        tableEntries.add(mapperTableEntry);

        mapperTableEntry = new ExternalMapperTableEntry();
        mapperTableEntry.setName("content");
        mapperTableEntry
                .setExpression(gen.getTableColumnVariable(VarsTable.PREFIX_VARS_TABLE_NAME, "upperCaseContent"));
        mapperTableEntry.setType(STRING_TYPE);
        tableEntries.add(mapperTableEntry);

        mapperTable.setMetadataTableEntries(tableEntries);

        // #########################################################################

        // FILTERS

        tableEntries = new ArrayList<ExternalMapperTableEntry>();

        mapperTableEntry = new ExternalMapperTableEntry();
        if (currentTest == TEST.ONE_INNER_JOIN_AND_NOT_ANY_REJECT_AND_ONE_OUT_TABLE_WITHOUT_FILTER
                || currentTest == TEST.ONE_INNER_JOIN_AND_ONLY_ONE_INNER_JOIN_REJECT_AND_ONE_OUT_TABLE_WITHOUT_FILTER
                || currentTest == TEST.NO_INNER_JOIN_AND_ONLY_ONE_INNER_JOIN_REJECT_AND_ONE_OUT_TABLE_WITHOUT_FILTER
                || currentTest == TEST.ONE_INNER_JOIN_AND_A_TABLE_WITH_REJECT_AND_INNER_JOIN_REJECT_AND_ONE_OUT_TABLE_WITHOUT_FILTER
                || currentTest == TEST.NO_INNER_JOIN_AND_ONE_INNER_JOIN_REJECT_AND_2_REJECT_TABLE_WITHOUT_FILTER
                || currentTest == TEST.NO_INNER_JOIN_AND_A_TABLE_WITH_FILTER_AND_A_TABLE_WITH_REJECT
                || currentTest == TEST.ONE_INNER_JOIN_AND_A_TABLE_WITH_INNER_JOIN_REJECT_WITHOUT_REGULAR_TABLE

        ) {
            // nothing
        } else {
            mapperTableEntry.setExpression(gen.getTableColumnVariable("page", "newId") + " == 1");
            tableEntries.add(mapperTableEntry);
        }

        mapperTable.setConstraintTableEntries(tableEntries);

        if(
                currentTest == TEST.THREE_TABLES_INPUT_WITH_ONE_INNER_JOIN_AND_NO_OUTPUT_TABLE_AND_NO_CONNECTION
                || currentTest == TEST.THREE_TABLES_INPUT_WITH_ONE_INNER_JOIN_AND_ONE_OUTPUT_TABLE_AND_NO_CONNECTION
                
        ) {
            
        } else {
            tables.add(mapperTable);
        }

        // #########################################################################
        // #########################################################################

        mapperTable = new ExternalMapperTable();
        mapperTable.setName("newPageRejected2");

        if (currentTest == TEST.ONE_INNER_JOIN_AND_NOT_ANY_REJECT_AND_ONE_OUT_TABLE_WITHOUT_FILTER) {
            mapperTable.setReject(false);
            mapperTable.setRejectInnerJoin(false);
        } else if (currentTest == TEST.ONE_INNER_JOIN_AND_ONLY_ONE_INNER_JOIN_REJECT_AND_ONE_OUT_TABLE_WITHOUT_FILTER
                || currentTest == TEST.NO_INNER_JOIN_AND_ONLY_ONE_INNER_JOIN_REJECT_AND_ONE_OUT_TABLE_WITHOUT_FILTER
                || currentTest == TEST.ONE_INNER_JOIN_AND_A_TABLE_WITH_FILTER_AND_A_TABLE_WITH_REJECT_AND_A_TABLE_WITH_REJECT_INNER_JOIN
                || currentTest == TEST.ONE_INNER_JOIN_AND_A_TABLE_WITH_REJECT_AND_INNER_JOIN_REJECT_AND_ONE_OUT_TABLE_WITHOUT_FILTER) {
            mapperTable.setReject(false);
            mapperTable.setRejectInnerJoin(true);
        } else if (currentTest == TEST.NO_INNER_JOIN_AND_ONE_INNER_JOIN_REJECT_AND_2_REJECT_TABLE_WITHOUT_FILTER
                || currentTest == TEST.MAIN_CONNECTION_WITH_INNER_JOIN_AND_A_TABLE_WITH_FILTER_AND_A_TABLE_WITH_REJECT_AND_A_TABLE_WITH_REJECT_INNER_JOIN) {
            mapperTable.setReject(true);
            mapperTable.setRejectInnerJoin(false);
        } else {
            mapperTable.setReject(true);
            mapperTable.setRejectInnerJoin(true);
        }

        tableEntries = new ArrayList<ExternalMapperTableEntry>();

        mapperTableEntry = new ExternalMapperTableEntry();
        mapperTableEntry.setName("id_page");
        mapperTableEntry.setExpression("");
        mapperTableEntry.setType(STRING_TYPE);
        tableEntries.add(mapperTableEntry);

        mapperTableEntry = new ExternalMapperTableEntry();
        mapperTableEntry.setName("id_book");
        mapperTableEntry.setExpression(gen.getTableColumnVariable("page", "id_book"));
        mapperTableEntry.setType(STRING_TYPE);
        tableEntries.add(mapperTableEntry);

        mapperTableEntry = new ExternalMapperTableEntry();
        mapperTableEntry.setName("content");
        mapperTableEntry
                .setExpression(gen.getTableColumnVariable(VarsTable.PREFIX_VARS_TABLE_NAME, "upperCaseContent"));
        mapperTableEntry.setType(INTEGER_TYPE);
        tableEntries.add(mapperTableEntry);

        mapperTable.setMetadataTableEntries(tableEntries);

        if (currentTest == TEST.NO_INNER_JOIN_AND_A_TABLE_WITH_FILTER_AND_A_TABLE_WITH_REJECT
                || currentTest == TEST.ONE_INNER_JOIN_AND_2_TABLES_WITH_FILTER_AND_OTHER_TABLE_WITH_REJECT_AND_REJECT_INNER_JOIN
                || currentTest == TEST.ONE_INNER_JOIN_AND_A_TABLE_WITH_FILTER_AND_A_TABLE_WITH_REJECT
                || currentTest == TEST.ONE_INNER_JOIN_AND_A_TABLE_WITH_REJECT_AND_INNER_JOIN_REJECT_AND_ONE_OUT_TABLE_WITHOUT_FILTER
                || currentTest == TEST.ONE_INNER_JOIN_AND_A_TABLE_WITH_INNER_JOIN_REJECT_WITHOUT_REGULAR_TABLE
                || currentTest == TEST.THREE_TABLES_INPUT_WITH_ONE_INNER_JOIN_AND_NO_OUTPUT_TABLE_AND_NO_CONNECTION
                || currentTest == TEST.THREE_TABLES_INPUT_WITH_ONE_INNER_JOIN_AND_ONE_OUTPUT_TABLE_AND_NO_CONNECTION

        ) {
            // nothing
        } else if (currentTest == TEST.ONE_INNER_JOIN_AND_A_TABLE_WITH_FILTER_AND_A_TABLE_WITH_REJECT_AND_A_TABLE_WITH_REJECT_INNER_JOIN) {
            tables.add(mapperTable);
        } else {
            tables.add(mapperTable);
        }

        // #########################################################################
        // #########################################################################

        externalData.setOutputTables(tables);
        // OUTPUTS
        // ///////////////////////////////////////////

        // #########################################################################
        // #########################################################################

        return externalData;
    }

    /**
     * DOC amaumont Comment method "generateMetadataListIn".
     * 
     * @return
     */
    public List<IConnection> generateConnectionListIn() {

        if (random) {
            return generateRandomConnectionListIn(TableType.INPUT);
        } else {
            return generateConstantConnectionListIn();
        }

    }

    /**
     * DOC amaumont Comment method "generateMetadataListIn".
     * 
     * @return
     */
    public List<IConnection> generateConnectionListOut() {

        if (random) {
            throw new UnsupportedOperationException();
        } else {
            return generateConstantConnectionListOut();
        }

    }

    /**
     * DOC amaumont Comment method "generateMetadataListOut".
     * 
     * @param b
     * @return
     */
    public List<IMetadataTable> generateMetadataListOut() {
        if (random) {
            return generateRandomMetadataList(TableType.OUTPUT);
        } else {
            return generateConstantMetadataListOut();
        }

    }

    /**
     * DOC amaumont Comment method "generateConstantMetadataListIn".
     * 
     * @return
     */
    private List<IConnection> generateRandomConnectionListIn(TableType tableType) {
        List<IMetadataTable> metadataTableList = generateRandomMetadataList(tableType);
        inputConnectionList = new ArrayList<IConnection>();
        int i = 0;
        int indexMain = rand.nextInt(metadataTableList.size());
        for (IMetadataTable table : metadataTableList) {
            Connection connection = new Connection();
            connection.setName(table.getTableName());
            connection.setMetadataTable(table);
            connection.setConnectionType((i == indexMain) ? EConnectionType.FLOW_MAIN : EConnectionType.FLOW_REF);
            inputConnectionList.add(connection);
            i++;
        }
        return inputConnectionList;
    }

    private List<IMetadataTable> generateRandomMetadataList(TableType tableType) {
        metadataListOut = new ArrayList<IMetadataTable>();
        for (int i = 1; i <= N_TABLES; i++) {
            MetadataTable metadataTable = new MetadataTable();
            String name = tableType.getBaseTableName() + i;
            metadataTable.setTableName(name);
            metadataListOut.add(metadataTable);
            ArrayList<IMetadataColumn> metadatColumns = new ArrayList<IMetadataColumn>();
            metadataTable.setListColumns(metadatColumns);
            for (int j = 1; j <= N_FIELDS; j++) {
                MetadataColumn metadataColumn = new MetadataColumn();
                // metadataColumn.setLabel(FIELD_NAME + j);
                String baseColumnName = FIELDS[rand.nextInt(FIELDS.length)];
                if (fixedData) {
                    baseColumnName = COLUMN_NAME;
                }
                metadataColumn.setLabel(baseColumnName + j);
                metadataColumn.setKey(rand.nextBoolean());
                String[] types = MetadataTalendType.getTalendTypesLabels();
                metadataColumn.setType(types[rand.nextInt(types.length - 1)]);
                metadataColumn.setNullable(rand.nextBoolean());
                metadatColumns.add(metadataColumn);
            }
        }

        return metadataListOut;
    }

    /**
     * DOC amaumont Comment method "generateRandomMetadataListIn".
     * 
     * @return
     */
    private List<IConnection> generateConstantConnectionListIn() {
        inputConnectionList = new ArrayList<IConnection>();

        Connection connection = new Connection();
        connection.setName("book");
        connection.setConnectionType(EConnectionType.FLOW_MAIN);

        MetadataTable metadataTable = new MetadataTable();
        metadataTable.setTableName("book2");

        connection.setMetadataTable(metadataTable);

        ArrayList<IMetadataColumn> metadatColumns = new ArrayList<IMetadataColumn>();

        MetadataColumn metadataColumn = new MetadataColumnTest();
        metadataColumn.setLabel("id_book");
        metadataColumn.setKey(true);
        metadataColumn.setType(INTEGER_TYPE);
        metadatColumns.add(metadataColumn);

        metadataColumn = new MetadataColumnTest();
        metadataColumn.setLabel("name");
        metadataColumn.setKey(false);
        metadataColumn.setType(STRING_TYPE);
        metadatColumns.add(metadataColumn);

        metadataColumn = new MetadataColumnTest();
        metadataColumn.setLabel("nb_pages");
        metadataColumn.setKey(false);
        metadataColumn.setType(INTEGER_TYPE);
        metadatColumns.add(metadataColumn);

        metadataTable.setListColumns(metadatColumns);

        inputConnectionList.add(connection);

        connection = new Connection();
        connection.setName("page");
        connection.setConnectionType(EConnectionType.FLOW_REF);

        metadataTable = new MetadataTable();
        metadataTable.setTableName("page2");

        connection.setMetadataTable(metadataTable);

        metadatColumns = new ArrayList<IMetadataColumn>();

        metadataColumn = new MetadataColumnTest();
        metadataColumn.setLabel("id");
        metadataColumn.setType(INTEGER_TYPE);
        metadatColumns.add(metadataColumn);

        metadataColumn = new MetadataColumnTest();
        metadataColumn.setLabel("id_book");
        metadataColumn.setKey(true);
        metadataColumn.setType(INTEGER_TYPE);
        metadatColumns.add(metadataColumn);

        metadataColumn = new MetadataColumnTest();
        metadataColumn.setLabel("content");
        metadataColumn.setKey(true);
        metadataColumn.setType(STRING_TYPE);
        metadatColumns.add(metadataColumn);

        metadataTable.setListColumns(metadatColumns);

        inputConnectionList.add(connection);

        connection = new Connection();
        connection.setName("userInput");
        connection.setConnectionType(EConnectionType.FLOW_REF);

        metadataTable = new MetadataTable();
        metadataTable.setTableName("user_input50");

        connection.setMetadataTable(metadataTable);

        metadatColumns = new ArrayList<IMetadataColumn>();

        metadataColumn = new MetadataColumnTest();
        metadataColumn.setLabel("id");
        metadataColumn.setType(INTEGER_TYPE);
        metadatColumns.add(metadataColumn);

        metadataColumn = new MetadataColumnTest();
        metadataColumn.setLabel("id_book");
        metadataColumn.setKey(true);
        metadataColumn.setType(INTEGER_TYPE);
        metadatColumns.add(metadataColumn);

        metadataColumn = new MetadataColumnTest();
        metadataColumn.setLabel("id_page_different");
        metadataColumn.setKey(true);
        metadataColumn.setType(STRING_TYPE);
        metadatColumns.add(metadataColumn);

        metadataTable.setListColumns(metadatColumns);

        inputConnectionList.add(connection);

        return inputConnectionList;
    }

    /**
     * DOC amaumont Comment method "generateRandomMetadataListIn".
     * 
     * @return
     */
    private List<IConnection> generateConstantConnectionListOut() {
        outputConnectionList = new ArrayList<IConnection>();

        Connection connection = new Connection();
        connection.setName("newBook");
        connection.setConnectionType(EConnectionType.FLOW_MAIN);

        MetadataTable metadataTable = new MetadataTable();
        metadataTable.setTableName("newBook");

        connection.setMetadataTable(metadataTable);

        ArrayList<IMetadataColumn> metadatColumns = new ArrayList<IMetadataColumn>();

        MetadataColumn metadataColumn = new MetadataColumnTest();
        metadataColumn.setLabel("newIdBook");
        metadataColumn.setKey(true);
        metadataColumn.setType(INTEGER_TYPE);
        metadatColumns.add(metadataColumn);

        metadataColumn = new MetadataColumnTest();
        metadataColumn.setLabel("name");
        metadataColumn.setKey(false);
        metadataColumn.setType(STRING_TYPE);
        metadatColumns.add(metadataColumn);

        metadataColumn = new MetadataColumnTest();
        metadataColumn.setLabel("nb_pages");
        metadataColumn.setKey(false);
        metadataColumn.setType(INTEGER_TYPE);
        metadatColumns.add(metadataColumn);

        metadataTable.setListColumns(metadatColumns);

        if (
                
                currentTest == TEST.THREE_TABLES_INPUT_WITH_ONE_INNER_JOIN_AND_NO_OUTPUT_TABLE_AND_NO_CONNECTION
                || currentTest == TEST.THREE_TABLES_INPUT_WITH_ONE_INNER_JOIN_AND_ONE_OUTPUT_TABLE_AND_NO_CONNECTION
                
        
        ) {

        } else {
            outputConnectionList.add(connection);
        }

        connection = new Connection();
        connection.setName("user");
        connection.setConnectionType(EConnectionType.FLOW_REF);

        metadataTable = new MetadataTable();
        metadataTable.setTableName("user");

        connection.setMetadataTable(metadataTable);

        metadatColumns = new ArrayList<IMetadataColumn>();

        metadataColumn = new MetadataColumnTest();
        metadataColumn.setLabel("idUser");
        metadataColumn.setType(INTEGER_TYPE);
        metadatColumns.add(metadataColumn);

        metadataColumn = new MetadataColumnTest();
        metadataColumn.setLabel("idBook");
        metadataColumn.setKey(true);
        metadataColumn.setType(INTEGER_TYPE);
        metadatColumns.add(metadataColumn);

        metadataColumn = new MetadataColumnTest();
        metadataColumn.setLabel("content");
        metadataColumn.setKey(true);
        metadataColumn.setType(STRING_TYPE);
        metadatColumns.add(metadataColumn);

        metadataTable.setListColumns(metadatColumns);

        if (
                currentTest == TEST.THREE_TABLES_INPUT_WITH_ONE_INNER_JOIN_AND_NO_OUTPUT_TABLE_AND_NO_CONNECTION
                || currentTest == TEST.THREE_TABLES_INPUT_WITH_ONE_INNER_JOIN_AND_ONE_OUTPUT_TABLE_AND_NO_CONNECTION
                
        
        ) {

        } else {
            outputConnectionList.add(connection);
        }

        connection = new Connection();
        connection.setName("newPageRejected");
        connection.setConnectionType(EConnectionType.FLOW_REF);

        metadataTable = new MetadataTable();
        metadataTable.setTableName("newPageRejected");

        connection.setMetadataTable(metadataTable);

        metadatColumns = new ArrayList<IMetadataColumn>();

        metadataColumn = new MetadataColumnTest();
        metadataColumn.setLabel("id");
        metadataColumn.setType(INTEGER_TYPE);
        metadatColumns.add(metadataColumn);

        metadataColumn = new MetadataColumnTest();
        metadataColumn.setLabel("id_book");
        metadataColumn.setKey(true);
        metadataColumn.setType(INTEGER_TYPE);
        metadatColumns.add(metadataColumn);

        metadataColumn = new MetadataColumnTest();
        metadataColumn.setLabel("content");
        metadataColumn.setKey(true);
        metadataColumn.setType(STRING_TYPE);
        metadatColumns.add(metadataColumn);

        metadataTable.setListColumns(metadatColumns);

        if (
                
                currentTest == TEST.THREE_TABLES_INPUT_WITH_ONE_INNER_JOIN_AND_NO_OUTPUT_TABLE_AND_NO_CONNECTION
                || currentTest == TEST.THREE_TABLES_INPUT_WITH_ONE_INNER_JOIN_AND_ONE_OUTPUT_TABLE_AND_NO_CONNECTION
                
        ) {

        } else {
            outputConnectionList.add(connection);
        }

        return outputConnectionList;
    }

    /**
     * DOC amaumont Comment method "generateMetadataListOut".
     * 
     * @return
     */
    public List<IMetadataTable> generateConstantMetadataListOut() {
        List<IMetadataTable> list = new ArrayList<IMetadataTable>();

        MetadataTable metadataTable = new MetadataTable();
        metadataTable.setTableName("newBook");

        ArrayList<IMetadataColumn> metadatColumns = new ArrayList<IMetadataColumn>();

        MetadataColumn metadataColumn = new MetadataColumnTest();
        metadataColumn.setLabel("newIdBook");
        metadataColumn.setKey(true);
        metadataColumn.setType(INTEGER_TYPE);
        metadatColumns.add(metadataColumn);

        metadataColumn = new MetadataColumnTest();
        metadataColumn.setLabel("name");
        metadataColumn.setKey(false);
        metadataColumn.setType(STRING_TYPE);
        metadatColumns.add(metadataColumn);

        metadataColumn = new MetadataColumnTest();
        metadataColumn.setLabel("nb_pages");
        metadataColumn.setKey(false);
        metadataColumn.setType(INTEGER_TYPE);
        metadatColumns.add(metadataColumn);

        metadataTable.setListColumns(metadatColumns);

        list.add(metadataTable);

        metadataTable = new MetadataTable();
        metadataTable.setTableName("user");

        metadatColumns = new ArrayList<IMetadataColumn>();

        metadataColumn = new MetadataColumnTest();
        metadataColumn.setLabel("idUser");
        metadataColumn.setKey(true);
        metadataColumn.setType(INTEGER_TYPE);
        metadatColumns.add(metadataColumn);

        metadataColumn = new MetadataColumnTest();
        metadataColumn.setLabel("idBook");
        metadataColumn.setKey(false);
        metadataColumn.setType(STRING_TYPE);
        metadatColumns.add(metadataColumn);

        metadataColumn = new MetadataColumnTest();
        metadataColumn.setLabel("content");
        metadataColumn.setKey(false);
        metadataColumn.setType(INTEGER_TYPE);
        metadatColumns.add(metadataColumn);

        metadataTable.setListColumns(metadatColumns);

        list.add(metadataTable);

        metadataTable = new MetadataTable();
        metadataTable.setTableName("newPageRejected");

        metadatColumns = new ArrayList<IMetadataColumn>();

        metadataColumn = new MetadataColumnTest();
        metadataColumn.setLabel("id");
        metadataColumn.setKey(true);
        metadataColumn.setType(INTEGER_TYPE);
        metadatColumns.add(metadataColumn);

        metadataColumn = new MetadataColumnTest();
        metadataColumn.setLabel("id_book");
        metadataColumn.setKey(false);
        metadataColumn.setType(INTEGER_TYPE);
        metadatColumns.add(metadataColumn);

        metadataColumn = new MetadataColumnTest();
        metadataColumn.setLabel("content");
        metadataColumn.setKey(false);
        metadataColumn.setType(STRING_TYPE);
        metadatColumns.add(metadataColumn);

        metadataTable.setListColumns(metadatColumns);

        list.add(metadataTable);

        metadataTable = new MetadataTable();
        metadataTable.setTableName("newPageRejected2");

        metadatColumns = new ArrayList<IMetadataColumn>();

        metadataColumn = new MetadataColumnTest();
        metadataColumn.setLabel("id");
        metadataColumn.setKey(true);
        metadataColumn.setType(INTEGER_TYPE);
        metadatColumns.add(metadataColumn);

        metadataColumn = new MetadataColumnTest();
        metadataColumn.setLabel("id_book");
        metadataColumn.setKey(false);
        metadataColumn.setType(INTEGER_TYPE);
        metadatColumns.add(metadataColumn);

        metadataColumn = new MetadataColumnTest();
        metadataColumn.setLabel("content");
        metadataColumn.setKey(false);
        metadataColumn.setType(STRING_TYPE);
        metadatColumns.add(metadataColumn);

        metadataTable.setListColumns(metadatColumns);

        list.add(metadataTable);

        return list;
    }

    /**
     * 
     * DOC amaumont MapperDataTestGenerator class global comment. Detailled comment <br/>
     * 
     * $Id$
     * 
     */
    class MetadataColumnTest extends MetadataColumn {

        public MetadataColumnTest() {
            super();
        }

        public MetadataColumnTest(IMetadataColumn metadataColumn) {
            super(metadataColumn);
        }

        @Override
        public String getTalendType() {
            return "";
        }

    }

    /**
     * 
     * DOC amaumont MapperDataTestGenerator class global comment. Detailled comment <br/>
     * 
     * $Id$
     * 
     */
    class Connection implements IConnection {

        String name;

        private IMetadataTable metadataTable;

        private EConnectionType connectionType;

        public EConnectionType getLineStyle() {
            return connectionType;
        }

        public IMetadataTable getMetadataTable() {
            return this.metadataTable;
        }

        public String getName() {
            return this.name;
        }

        public INode getSource() {
            // TODO Auto-generated method stub
            return null;
        }

        public INode getTarget() {
            // TODO Auto-generated method stub
            return null;
        }

        public boolean isActivate() {
            // TODO Auto-generated method stub
            return false;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setMetadataTable(IMetadataTable metadataTable) {
            this.metadataTable = metadataTable;
        }

        public void setConnectionType(EConnectionType connectionType) {
            this.connectionType = connectionType;
        }

        public String getCondition() {
            return null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.process.IElement#getElementParameters()
         */
        public List<? extends IElementParameter> getElementParameters() {
            return null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.process.IElement#setElementParameters(java.util.List)
         */
        public void setElementParameters(List<? extends IElementParameter> elementsParameters) {
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.process.IConnection#setTraceData(java.lang.String)
         */
        public void setTraceData(String traceData) {
            // TODO Auto-generated method stub

        }

        public boolean isReadOnly() {
            // TODO Auto-generated method stub
            return false;
        }

        public void setReadOnly(boolean readOnly) {
            // TODO Auto-generated method stub

        }

        public String getUniqueName() {
            return name;
        }

    };

    /**
     * 
     * DOC amaumont MapperDataTestGenerator class global comment. Detailled comment <br/>
     * 
     * $Id$
     * 
     */
    class Node implements INode {

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.process.INode#getComponentName()
         */
        public String getComponentName() {
            // TODO Auto-generated method stub
            return null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.process.INode#getElementParameters()
         */
        public List<? extends IElementParameter> getElementParameters() {
            // TODO Auto-generated method stub
            return null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.process.INode#getGeneratedCode()
         */
        public String getGeneratedCode() {
            // TODO Auto-generated method stub
            return null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.process.INode#getIncomingConnections()
         */
        public List<? extends IConnection> getIncomingConnections() {
            // TODO Auto-generated method stub
            return null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.process.INode#getMetadataList()
         */
        public List<IMetadataTable> getMetadataList() {
            // TODO Auto-generated method stub
            return null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.process.INode#getOutgoingConnections()
         */
        public List<? extends IConnection> getOutgoingConnections() {
            // TODO Auto-generated method stub
            return null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.process.INode#getPluginFullName()
         */
        public String getPluginFullName() {
            // TODO Auto-generated method stub
            return null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.process.INode#getUniqueName()
         */
        public String getUniqueName() {
            // TODO Auto-generated method stub
            return null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.process.INode#isActivate()
         */
        public boolean isActivate() {
            // TODO Auto-generated method stub
            return false;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.process.INode#isStart()
         */
        public boolean isStart() {
            // TODO Auto-generated method stub
            return false;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.process.INode#isSubProcessStart()
         */
        public boolean isSubProcessStart() {
            // TODO Auto-generated method stub
            return false;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.process.INode#setElementParameters(java.util.List)
         */
        public void setElementParameters(List<? extends IElementParameter> parameters) {
            // TODO Auto-generated method stub

        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.process.INode#setPerformanceData(java.lang.String)
         */
        public void setPerformanceData(String perfData) {
            // TODO Auto-generated method stub

        }

        public void setTraceData(String traceData) {
            // TODO Auto-generated method stub

        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.process.INode#getReturns()
         */
        public List<? extends INodeReturn> getReturns() {
            return new ArrayList<INodeReturn>();
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.process.INode#getProcess()
         */
        public IProcess getProcess() {
            // TODO Auto-generated method stub
            return null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.process.INode#setProcess(org.talend.core.model.process.IProcess)
         */
        public void setProcess(IProcess process) {
            // TODO Auto-generated method stub

        }

        public IComponent getComponent() {
            // TODO Auto-generated method stub
            return null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.process.INode#renameMetadataColumnName(java.lang.String, java.lang.String,
         * java.lang.String)
         */
        public void metadataInputChanged(IODataComponent dataComponent, String connectionToApply) {
            // TODO Auto-generated method stub
        }

        public void metadataOutputChanged(IODataComponent dataComponent, String connectionToApply) {
            // TODO Auto-generated method stub
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.process.INode#setComponent(org.talend.core.model.components.IComponent)
         */
        public void setComponent(IComponent component) {
            // TODO Auto-generated method stub

        }

        public boolean isReadOnly() {
            // TODO Auto-generated method stub
            return false;
        }

        public void setReadOnly(boolean readOnly) {
            // TODO Auto-generated method stub

        }

        public INode getSubProcessStartNode(boolean withConditions) {
            // TODO Auto-generated method stub
            return null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.process.INode#hasConditionalOutputs()
         */
        public Boolean hasConditionalOutputs() {
            // TODO Auto-generated method stub
            return null;
        }

    }

    public ArrayList<IConnection> getInputConnectionsList() {
        return this.inputConnectionList;
    }

    public ArrayList<IConnection> getOutputConnectionsList() {
        return this.outputConnectionList;
    }

    public ArrayList<IMetadataTable> getMetadataListOut() {
        return this.metadataListOut;
    }

    public ExternalMapperData getExternalData() {
        return this.externalData;
    };

}
