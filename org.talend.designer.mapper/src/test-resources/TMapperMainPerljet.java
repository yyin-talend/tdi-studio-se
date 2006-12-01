// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.process.AbstractExternalNode;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IExternalNode;
import org.talend.designer.mapper.external.data.ExternalMapperData;
import org.talend.designer.mapper.external.data.ExternalMapperTable;
import org.talend.designer.mapper.external.data.ExternalMapperTableEntry;
import org.talend.designer.mapper.language.ILanguage;
import org.talend.designer.mapper.language.LanguageProvider;
import org.talend.designer.mapper.language.generation.GenerationManager;
import org.talend.designer.mapper.language.generation.TableType;
import org.talend.designer.mapper.model.metadata.MapperDataTestGenerator;
import org.talend.designer.mapper.model.tableentry.TableEntryLocation;
import org.talend.designer.mapper.utils.DataMapExpressionParser;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class TMapperMainPerljet {

    public static void main(String[] args) {
        IExternalNode argument = null;

        AbstractExternalNode node = (AbstractExternalNode) argument;

        // ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // start of code to copy in template

        List<IConnection> connections;
        ExternalMapperData data;
        if (node != null) {
            // normal use
            connections = (List<IConnection>) node.getIncomingConnections();
            data = (ExternalMapperData) node.getExternalData();
        } else {
            // Stand alone / tests
            org.talend.designer.mapper.MapperMain.setStandAloneMode(true);
            MapperDataTestGenerator testGenerator = new MapperDataTestGenerator(LanguageProvider.getCurrentLanguage(), false);
            connections = testGenerator.getConnectionList();
            data = (ExternalMapperData) testGenerator.getExternalData();
        }

        String cr = "\n";
        String rejected = "rejected";
        String rejectedInnerJoin = "rejectedInnerJoin";

        List<ExternalMapperTable> inputTables = data.getInputTables();
        List<ExternalMapperTable> varsTables = data.getVarsTables();
        List<ExternalMapperTable> outputTables = data.getOutputTables();

        int indent = 1;

        ILanguage currentLanguage = LanguageProvider.getCurrentLanguage();

        DataMapExpressionParser expressionParser = new DataMapExpressionParser(currentLanguage);

        GenerationManager gm = new GenerationManager(currentLanguage);

        StringBuilder sb = new StringBuilder();

        gm.setInputTables(inputTables);
        gm.setVarsTables(varsTables);

        // /////////////////////////////////////////////////////////////////////////////////////////////////////
        // /////////////////////////////////////////////////////////////////////////////////////////////////////
        // INPUTS : initialization of input arrays from expressions keys and hashes
        // 
        sb.append(cr + gm.indent(indent));
        sb.append(cr + gm.indent(indent) + "###############################");
        sb.append(cr + gm.indent(indent) + "# Input tables ");

        HashMap<String, ExternalMapperTable> hExternalInpuTables = new HashMap<String, ExternalMapperTable>();
        for (ExternalMapperTable inputTable : inputTables) {
            hExternalInpuTables.put(inputTable.getName(), inputTable);
        }

        ArrayList<ExternalMapperTable> inputTablesWithInnerJoin = new ArrayList<ExternalMapperTable>();

        HashMap<String, ExternalMapperTableEntry> hExternalInputTableEntries = new HashMap<String, ExternalMapperTableEntry>();
        for (IConnection connection : connections) {
            EConnectionType connectionType = connection.getLineStyle();
            if (connectionType == EConnectionType.FLOW_MAIN) {
                continue;
            } else if (connectionType == EConnectionType.FLOW_REF) {

                IMetadataTable metadataTable = connection.getMetadataTable();
                String tableName = connection.getName();
                ExternalMapperTable externalTable = hExternalInpuTables.get(tableName);
                if (externalTable != null) {
                    if (externalTable.isInnerJoin()) {
                        inputTablesWithInnerJoin.add(externalTable);
                    }
                    hExternalInputTableEntries.clear();
                    List<ExternalMapperTableEntry> metadataTableEntries = externalTable.getMetadataTableEntries();
                    if (metadataTableEntries == null) {
                        continue;
                    }
                    for (ExternalMapperTableEntry externalTableEntry : metadataTableEntries) {
                        hExternalInputTableEntries.put(externalTableEntry.getName(), externalTableEntry);
                    }
                    List<IMetadataColumn> listColumns = metadataTable.getListColumns();
                    ArrayList<String> keysValues = new ArrayList<String>();
                    for (IMetadataColumn column : listColumns) {
                        String columnName = column.getLabel();
                        ExternalMapperTableEntry externalInputTableEntry = hExternalInputTableEntries.get(columnName);
                        if (externalInputTableEntry != null) {
                            String expressionKey = externalInputTableEntry.getExpression();
                            if (column.isKey() && expressionKey != null && !"".equals(expressionKey.trim())) {
                                String outputExpressionKeyToWrite = gm.prefixEntryLocationsForOutputExpression(expressionKey,
                                        expressionParser, new TableType[] { TableType.INPUT });

                                keysValues.add(outputExpressionKeyToWrite);
                            }
                        }
                    }
                    String[] aKeysValues = keysValues.toArray(new String[0]);
                    if (aKeysValues.length > 0) {
                        sb.append(cr + gm.indent(indent) + gm.buildNewArrayDeclarationWithKeyValue(tableName, aKeysValues, indent));
                    }

                } // if(externalTable != null) {
            } // else if(connectionType == EConnectionType.FLOW_REF) {
        } // for (IConnection connection : connections) {
        boolean atLeastOneInputTableWithInnerJoin = !inputTablesWithInnerJoin.isEmpty();

        sb.append(cr + gm.indent(indent) + "###############################");
        // /////////////////////////////////////////////////////////////////////////////////////////////////////
        // /////////////////////////////////////////////////////////////////////////////////////////////////////

        // /////////////////////////////////////////////////////////////////////////////////////////////////////
        // /////////////////////////////////////////////////////////////////////////////////////////////////////
        // VARIABLES
        // 
        sb.append(cr + gm.indent(indent));
        sb.append(cr + gm.indent(indent) + "###############################");
        sb.append(cr + gm.indent(indent) + "# Vars tables");
        for (ExternalMapperTable varsTable : varsTables) {
            List<ExternalMapperTableEntry> varsTableEntries = varsTable.getMetadataTableEntries();
            if (varsTableEntries == null) {
                continue;
            }
            if (varsTableEntries.size() > 0) {
                sb.append(cr + gm.indent(indent) + gm.buildNewArrayDeclaration(varsTable.getName(), indent));
            }
            String varsTableName = varsTable.getName();
            for (ExternalMapperTableEntry varsTableEntry : varsTableEntries) {
                String varsColumnName = varsTableEntry.getName();
                String varExpression = varsTableEntry.getExpression();
                if (varExpression == null || varExpression.trim().length() == 0) {
                    continue;
                }
                TableEntryLocation[] entryLocations = expressionParser.parseTableEntryLocations(varExpression);
                ArrayList<TableEntryLocation> listCoupleForAddTablePrefix = new ArrayList<TableEntryLocation>();
                for (TableEntryLocation location : entryLocations) {
                    if (gm.isInputTable(varExpression)) {
                        listCoupleForAddTablePrefix.add(location);
                    }
                }
                String varExpressionWithPrefixs = expressionParser.addTablePrefixToColumnName(varExpression, entryLocations);

                sb.append(cr + gm.indent(indent) + "" + gm.getGeneratedCodeTableColumnVariable(varsTableName, varsColumnName) + " = "
                        + varExpressionWithPrefixs + ";");

            }
        }
        sb.append(cr + gm.indent(indent) + "###############################");
        // /////////////////////////////////////////////////////////////////////////////////////////////////////
        // /////////////////////////////////////////////////////////////////////////////////////////////////////

        // /////////////////////////////////////////////////////////////////////////////////////////////////////
        // /////////////////////////////////////////////////////////////////////////////////////////////////////
        // OUTPUTS
        // 
        sb.append(cr + gm.indent(indent));
        sb.append(cr + gm.indent(indent) + "###############################");
        sb.append(cr + gm.indent(indent) + "# Output tables");

        ArrayList<ExternalMapperTable> outputTablesSortedByReject = new ArrayList<ExternalMapperTable>(outputTables);
        // sorting outputs : rejects tables after not rejects table
        Collections.sort(outputTablesSortedByReject, new Comparator<ExternalMapperTable>() {

            public int compare(ExternalMapperTable o1, ExternalMapperTable o2) {
                if (o1.isReject() != o2.isReject()) {
                    if (o1.isReject()) {
                        return 1;
                    } else {
                        return -1;
                    }
                }
                if (o1.isRejectInnerJoin() != o2.isRejectInnerJoin()) {
                    if (o1.isRejectInnerJoin()) {
                        return 1;
                    } else {
                        return -1;
                    }
                }
                return 0;
            }

        });

        boolean lastValueReject = false;
        boolean oneFilterForNotRejectTable = false;
        boolean allNotRejectTablesHaveFilter = true;
        boolean atLeastOneReject = false;
        boolean atLeastOneRejectInnerJoin = false;
        boolean closeTestInnerJoinConditionsBracket = false;

        int lstSize = outputTablesSortedByReject.size();
        // ///////////////////////////////////////////////////////////////////
        // init of allNotRejectTablesHaveFilter and atLeastOneReject
        for (int i = 0; i < lstSize; i++) {
            ExternalMapperTable outputTable = (ExternalMapperTable) outputTablesSortedByReject.get(i);
            List<ExternalMapperTableEntry> columnsEntries = outputTable.getMetadataTableEntries();
            List<ExternalMapperTableEntry> filters = outputTable.getConstraintTableEntries();
            boolean hasFilter = filters != null && filters.size() > 0 && !gm.checkFiltersAreEmpty(outputTable);
            if (columnsEntries != null && columnsEntries.size() > 0) {
                if (!hasFilter && !(outputTable.isReject() || outputTable.isRejectInnerJoin())) {
                    allNotRejectTablesHaveFilter = false;
                }
                if (outputTable.isReject() || outputTable.isRejectInnerJoin()) {
                    atLeastOneReject = true;
                }
            }
            if (outputTable.isRejectInnerJoin()) {
                atLeastOneRejectInnerJoin = true;
            }
        }
        // ///////////////////////////////////////////////////////////////////

        if (allNotRejectTablesHaveFilter && atLeastOneReject) {
            // write $oneNotRejectFilterValidated = false;
            sb.append(cr + gm.indent(indent) + "$" + rejected + " = true;");
        }
        if (atLeastOneInputTableWithInnerJoin && atLeastOneRejectInnerJoin) {
            // write $oneNotRejectFilterValidated = false;
            sb.append(cr + gm.indent(indent) + "$" + rejectedInnerJoin + " = true;");
        }

        // write outputs arrays initialization with empty list for NOT reject tables
        for (int indexReject = 0; indexReject < lstSize; indexReject++) {
            ExternalMapperTable outputNormalTable = (ExternalMapperTable) outputTablesSortedByReject.get(indexReject);
            if (outputNormalTable.isReject() || outputNormalTable.isRejectInnerJoin()) {
                break;
            }
            List<ExternalMapperTableEntry> metadataTableEntries = outputNormalTable.getMetadataTableEntries();
            if (metadataTableEntries != null && metadataTableEntries.size() > 0) {
                sb.append(cr + gm.indent(indent) + "# Output table: '" + outputNormalTable.getName() + "'");
                sb.append(cr + gm.indent(indent) + gm.buildNewArrayDeclaration(outputNormalTable.getName(), indent));
            }
        }

        // write conditions for inner join reject
        if (inputTablesWithInnerJoin.size() > 0) {
            sb.append(cr + gm.indent(indent) + "if(");
            String and = null;
            for (ExternalMapperTable inputTable : inputTablesWithInnerJoin) {
                if (and == null) {
                    and = "";
                } else {
                    and = " &&";
                }
                sb.append(and + " scalar( @" + inputTable.getName() + ")");
            }
            sb.append(" ) {");
            closeTestInnerJoinConditionsBracket = true;
            indent++;
            sb.append(cr + gm.indent(indent) + "$" + rejectedInnerJoin + " = false;");
        }

        // ///////////////////////////////////////////////////////////////////
        // run through output tables list for generating intilization of outputs arrays
        for (int indexCurrentTable = 0; indexCurrentTable < lstSize; indexCurrentTable++) {
            ExternalMapperTable outputTable = (ExternalMapperTable) outputTablesSortedByReject.get(indexCurrentTable);
            List<ExternalMapperTableEntry> outputTableEntries = outputTable.getMetadataTableEntries();
            if (outputTableEntries == null) {
                continue;
            }
            String outputTableName = outputTable.getName();

            List<ExternalMapperTableEntry> filters = outputTable.getConstraintTableEntries();

            boolean currentIsReject = outputTable.isReject();
            boolean currentIsRejectInnerJoin = outputTable.isRejectInnerJoin();

            boolean hasFilters = filters != null && filters.size() > 0 && !gm.checkFiltersAreEmpty(outputTable);

            boolean rejectValueHasJustChanged = lastValueReject != (currentIsReject || currentIsRejectInnerJoin);

            oneFilterForNotRejectTable = !(currentIsReject || currentIsRejectInnerJoin) && hasFilters;

            if (rejectValueHasJustChanged) {

                if (closeTestInnerJoinConditionsBracket) {
                    indent--;
                    sb.append(cr + gm.indent(indent) + "}");
                    closeTestInnerJoinConditionsBracket = false;
                }

                sb.append(cr + gm.indent(indent) + "###### START REJECTS ##### ");
                // write outputs arrays initialization with empty list for reject tables
                for (int indexReject = indexCurrentTable; indexReject < lstSize; indexReject++) {
                    ExternalMapperTable outputRejectTable = (ExternalMapperTable) outputTablesSortedByReject.get(indexReject);
                    List<ExternalMapperTableEntry> metadataTableEntries = outputRejectTable.getMetadataTableEntries();
                    List<ExternalMapperTableEntry> filtersForReject = outputRejectTable.getConstraintTableEntries();
                    boolean hasFIlterForReject = filtersForReject != null && filtersForReject.size() > 0
                            && !gm.checkFiltersAreEmpty(outputRejectTable);
                    if (metadataTableEntries != null && metadataTableEntries.size() > 0 && hasFIlterForReject
                            && outputRejectTable.isReject() && hasFilters || outputRejectTable.isRejectInnerJoin()) {
                        sb.append(cr + gm.indent(indent) + "# Output reject table: '" + outputRejectTable.getName() + "'");
                        sb.append(cr + gm.indent(indent) + gm.buildNewArrayDeclaration(outputRejectTable.getName(), indent));
                    }
                }
            }

            // write filters conditions and code to execute
            if (!currentIsReject || rejectValueHasJustChanged && oneFilterForNotRejectTable || currentIsReject || currentIsRejectInnerJoin
                    && atLeastOneInputTableWithInnerJoin) {

                boolean closeFilterOrRejectBracket = false;
                if (hasFilters || currentIsReject || currentIsRejectInnerJoin && atLeastOneInputTableWithInnerJoin) {
                    sb.append(cr + gm.indent(indent) + "# Filter condition ");
                    sb.append(cr + gm.indent(indent) + "if( ");

                    String rejectedTests = null;
                    if (allNotRejectTablesHaveFilter && atLeastOneReject && currentIsReject && currentIsRejectInnerJoin
                            && atLeastOneInputTableWithInnerJoin) {
                        rejectedTests = "$" + rejected + " || $" + rejectedInnerJoin;
                        if (hasFilters) {
                            rejectedTests = "(" + rejectedTests + ")";
                        }
                    } else if (allNotRejectTablesHaveFilter && atLeastOneReject && currentIsReject) {
                        rejectedTests = "$" + rejected;
                    } else if (currentIsRejectInnerJoin && atLeastOneInputTableWithInnerJoin) {
                        rejectedTests = "$" + rejectedInnerJoin;
                    }
                    if (hasFilters) {
                        String filtersConditions = gm.buildConditions(filters, expressionParser);
                        if (rejectedTests == null) {
                            sb.append(filtersConditions);
                        } else {
                            sb.append(rejectedTests + " && (" + filtersConditions + ")");
                        }
                    } else {
                        sb.append(rejectedTests);
                    }
                    sb.append(" ) {");
                    indent++;
                    closeFilterOrRejectBracket = true;
                    if (allNotRejectTablesHaveFilter && !(currentIsReject || currentIsRejectInnerJoin) && atLeastOneReject) {
                        sb.append(cr + gm.indent(indent) + "$" + rejected + " = false;");
                    }
                }

                if (!currentIsReject && !currentIsRejectInnerJoin || currentIsReject || currentIsRejectInnerJoin
                        && atLeastOneInputTableWithInnerJoin) {
                    for (ExternalMapperTableEntry outputTableEntry : outputTableEntries) {
                        String outputColumnName = outputTableEntry.getName();
                        String outputExpression = outputTableEntry.getExpression();
                        if (outputExpression != null && outputExpression.trim().length() != 0) {

                            String outputExpressionToWrite = gm.prefixEntryLocationsForOutputExpression(outputExpression, expressionParser,
                                    new TableType[] { TableType.INPUT, TableType.VARS });

                            sb.append(cr + gm.indent(indent) + gm.getGeneratedCodeTableColumnVariable(outputTableName, outputColumnName)
                                    + " = " + outputExpressionToWrite + ";");

                        }

                    } // for entries
                }
                if (closeFilterOrRejectBracket) {
                    indent--;
                    sb.append(cr + gm.indent(indent) + "}");
                }

            }
            lastValueReject = currentIsReject || currentIsRejectInnerJoin;

            boolean isLastTable = indexCurrentTable == lstSize - 1;
            if (closeTestInnerJoinConditionsBracket && isLastTable) {
                indent--;
                sb.append(cr + gm.indent(indent) + "}");
                closeTestInnerJoinConditionsBracket = false;
            }

        } // for output tables

        sb.append(cr + gm.indent(indent) + "###############################");

        // end of code to copy in template
        // ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        System.out.println(sb);
    }

}
