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
package org.talend.designer.mapper.language.java;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.talend.commons.utils.generation.CodeGenerationUtils;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.core.model.process.AbstractExternalNode;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IExternalNode;
import org.talend.designer.codegen.config.CodeGeneratorArgument;
import org.talend.designer.mapper.MapperMain;
import org.talend.designer.mapper.external.data.ExternalMapperData;
import org.talend.designer.mapper.external.data.ExternalMapperTable;
import org.talend.designer.mapper.external.data.ExternalMapperTableEntry;
import org.talend.designer.mapper.language.ILanguage;
import org.talend.designer.mapper.language.LanguageProvider;
import org.talend.designer.mapper.language.generation.GenerationManagerFactory;
import org.talend.designer.mapper.language.generation.JavaGenerationManager;
import org.talend.designer.mapper.model.metadata.MapperDataTestGenerator;
import org.talend.designer.mapper.model.tableentry.TableEntryLocation;
import org.talend.designer.mapper.utils.DataMapExpressionParser;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id: TMapperMainPerljet.java 1275 2007-01-04 13:35:45Z amaumont $
 * 
 */
public class TMapperMainJavajet {

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        IExternalNode argument = null;

        CodeGeneratorArgument codeGenArgument = null;

        AbstractExternalNode node = (AbstractExternalNode) argument;

        // ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // start of code to copy in template

        String uniqueNameComponent = null;
        ILanguage currentLanguage = LanguageProvider.getJavaLanguage();
        List<IConnection> inputConnections;
        List<IConnection> outputConnections;
        ExternalMapperData data;
        boolean checkingSyntax = false;
        if (node != null) {
            // normal use
            inputConnections = (List<IConnection>) node.getIncomingConnections();
            outputConnections = (List<IConnection>) node.getOutgoingConnections();
            data = (ExternalMapperData) node.getExternalData();
            uniqueNameComponent = node.getUniqueName();
            checkingSyntax = codeGenArgument.isCheckingSyntax();
        } else {
            // Stand alone / tests
            MapperMain.setStandAloneMode(true);
            MapperDataTestGenerator testGenerator = new MapperDataTestGenerator(currentLanguage, false);
            inputConnections = testGenerator.getInputConnectionsList();
            outputConnections = testGenerator.getOutputConnectionsList();
            data = (ExternalMapperData) testGenerator.getExternalData();
            uniqueNameComponent = "testUniqueNameNode";
        }

        String cr = "\n";
        String rejected = "rejected";
        String rejectedInnerJoin = "rejectedInnerJoin";

        List<ExternalMapperTable> inputTables = data.getInputTables();
        List<ExternalMapperTable> varsTables = data.getVarsTables();
        List<ExternalMapperTable> outputTables = data.getOutputTables();

        int indent = 1;

        DataMapExpressionParser expressionParser = new DataMapExpressionParser(currentLanguage);

        JavaGenerationManager gm = (JavaGenerationManager) GenerationManagerFactory.getInstance().getGenerationManager(
                currentLanguage);

        StringBuilder sb = new StringBuilder();

        gm.setInputTables(inputTables);
        gm.setVarsTables(varsTables);

        // /////////////////////////////////////////////////////////////////////////////////////////////////////
        // /////////////////////////////////////////////////////////////////////////////////////////////////////
        // INPUTS : initialization of input arrays from expressions keys and hashes
        // 
        sb.append(cr + gm.indent(indent));
        sb.append(cr + gm.indent(indent) + "// ###############################");
        sb.append(cr + gm.indent(indent) + "// # Input tables (lookups)");

        HashMap<String, IConnection> hNameToConnection = new HashMap<String, IConnection>();
        for (IConnection connection : inputConnections) {
            hNameToConnection.put(connection.getName(), connection);
        }

        ArrayList<ExternalMapperTable> inputTablesWithInnerJoin = new ArrayList<ExternalMapperTable>();
        ArrayList<ExternalMapperTable> lookupTables = new ArrayList<ExternalMapperTable>();
        Set<ExternalMapperTable> validLookupTables = new HashSet<ExternalMapperTable>();

        HashMap<String, ExternalMapperTableEntry> hExternalInputTableEntries = new HashMap<String, ExternalMapperTableEntry>();
        for (ExternalMapperTable externalTable : inputTables) {
            String tableName = externalTable.getName();
            IConnection connection = hNameToConnection.get(tableName);
            if (connection == null) {
                continue;
            }
            EConnectionType connectionType = connection.getLineStyle();
            if (connectionType == EConnectionType.FLOW_MAIN) {
                continue;
            } else if (connectionType == EConnectionType.FLOW_REF) {

                IMetadataTable metadataTable = connection.getMetadataTable();
                if (externalTable != null) {
                    if (externalTable.isInnerJoin()) {
                        inputTablesWithInnerJoin.add(externalTable);
                    }
                    lookupTables.add(externalTable);
                    hExternalInputTableEntries.clear();
                    List<ExternalMapperTableEntry> metadataTableEntries = externalTable.getMetadataTableEntries();
                    if (metadataTableEntries == null) {
                        continue;
                    }
                    for (ExternalMapperTableEntry externalTableEntry : metadataTableEntries) {
                        hExternalInputTableEntries.put(externalTableEntry.getName(), externalTableEntry);
                    }
                    List<IMetadataColumn> listColumns = metadataTable.getListColumns();
                    ArrayList<String> keysNames = new ArrayList<String>();
                    ArrayList<String> keysValues = new ArrayList<String>();
                    for (IMetadataColumn column : listColumns) {
                        String columnName = column.getLabel();
                        ExternalMapperTableEntry externalInputTableEntry = hExternalInputTableEntries.get(columnName);
                        if (externalInputTableEntry != null) {
                            String expressionKey = externalInputTableEntry.getExpression();
                            if (column.isKey() && expressionKey != null && !"".equals(expressionKey.trim())) {
                                keysNames.add(columnName);
                                keysValues.add(expressionKey);
                            }
                        }
                    }
                    String[] aKeysNames = keysNames.toArray(new String[0]);
                    String[] aKeysValues = keysValues.toArray(new String[0]);
                    if (aKeysValues.length > 0 || checkingSyntax) {
                        sb.append(cr
                                + gm.buildLookupDataInstance(uniqueNameComponent, tableName, aKeysNames, aKeysValues,
                                        indent, checkingSyntax));
                        validLookupTables.add(externalTable);
                        sb.append(cr + gm.indent(indent) + tableName + "Struct " + tableName + " = ( " + tableName
                                + "FromHash == null )" + " ? " + tableName + "Default" + " : " + tableName
                                + "FromHash;");
                    } else {
                        sb.append(cr + gm.indent(indent) + tableName + "Struct " + tableName + " = " + tableName
                                + "Default;");
                    }

                } // if(externalTable != null) {
            } // else if(connectionType == EConnectionType.FLOW_REF) {
        } // for (IConnection connection : connections) {
        boolean atLeastOneInputTableWithInnerJoin = !inputTablesWithInnerJoin.isEmpty();

        sb.append(cr);

        sb.append(cr + gm.indent(indent) + "// ###############################");
        // /////////////////////////////////////////////////////////////////////////////////////////////////////
        // /////////////////////////////////////////////////////////////////////////////////////////////////////

        // /////////////////////////////////////////////////////////////////////////////////////////////////////
        // /////////////////////////////////////////////////////////////////////////////////////////////////////
        // VARIABLES
        // 
        sb.append(cr);
        sb.append(cr + gm.indent(indent) + "{ // start of Var scope");
        indent++;
        sb.append(cr);
        sb.append(cr + gm.indent(indent) + "// ###############################");
        sb.append(cr + gm.indent(indent) + "// # Vars tables");
        for (ExternalMapperTable varsTable : varsTables) {
            List<ExternalMapperTableEntry> varsTableEntries = varsTable.getMetadataTableEntries();
            if (varsTableEntries == null) {
                continue;
            }
            if (varsTableEntries.size() > 0) {
                // sb.append(cr + gm.indent(indent) + gm.buildNewArrayDeclaration(varsTable.getName(), indent));
            }
            String varsTableName = varsTable.getName();
            String instanceVarName = varsTableName + "__" + uniqueNameComponent;
            String className = instanceVarName + "__Struct";

            sb.append(cr + gm.indent(indent) + className + " " + varsTableName + " = " + instanceVarName + ";");
            for (ExternalMapperTableEntry varsTableEntry : varsTableEntries) {
                String varsColumnName = varsTableEntry.getName();
                String varExpression = varsTableEntry.getExpression();
                if (varExpression == null || varExpression.trim().length() == 0) {
                    varExpression = JavaTypesManager.getDefaultValueFromJavaIdType(varsTableEntry.getType(),
                            varsTableEntry.isNullable());
                }
                TableEntryLocation[] entryLocations = expressionParser.parseTableEntryLocations(varExpression);
                ArrayList<TableEntryLocation> listCoupleForAddTablePrefix = new ArrayList<TableEntryLocation>();
                for (TableEntryLocation location : entryLocations) {
                    if (gm.isInputTable(varExpression)) {
                        listCoupleForAddTablePrefix.add(location);
                    }
                }

                String key = CodeGenerationUtils.buildProblemKey(uniqueNameComponent,
                        JavaGenerationManager.PROBLEM_KEY_FIELD.METADATA_COLUMN.toString(), varsTableName,
                        varsColumnName);

                if (checkingSyntax) {
                    sb.append(cr).append(CodeGenerationUtils.buildJavaStartFieldKey(key));
                }

                String expression = gm.indent(indent)
                        + gm.getGeneratedCodeTableColumnVariable(varsTableName, varsColumnName) + " = " + varExpression
                        + ";";
                sb.append(cr).append(expression);

                if (checkingSyntax) {
                    sb.append(cr).append(CodeGenerationUtils.buildJavaEndFieldKey(key));
                }

            }
        }
        sb.append(cr + gm.indent(indent) + "// ###############################");
        // /////////////////////////////////////////////////////////////////////////////////////////////////////
        // /////////////////////////////////////////////////////////////////////////////////////////////////////

        // /////////////////////////////////////////////////////////////////////////////////////////////////////
        // /////////////////////////////////////////////////////////////////////////////////////////////////////
        // OUTPUTS
        // 
        sb.append(cr + gm.indent(indent));
        sb.append(cr + gm.indent(indent) + "// ###############################");
        sb.append(cr + gm.indent(indent) + "// # Output tables");

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

        Map<String, IConnection> nameToOutputConnection = new HashMap<String, IConnection>();
        for (IConnection connection : outputConnections) {
            nameToOutputConnection.put(connection.getName(), connection);
        }

        int lstSizeOutputs = outputTablesSortedByReject.size();
        // ///////////////////////////////////////////////////////////////////
        // init of allNotRejectTablesHaveFilter and atLeastOneReject
        for (int i = 0; i < lstSizeOutputs; i++) {
            ExternalMapperTable outputTable = (ExternalMapperTable) outputTablesSortedByReject.get(i);

            String outputTableName = outputTable.getName();

            if (outputTable.isRejectInnerJoin()) {
                atLeastOneRejectInnerJoin = true;
            }
            List<ExternalMapperTableEntry> columnsEntries = outputTable.getMetadataTableEntries();
            List<ExternalMapperTableEntry> filters = outputTable.getConstraintTableEntries();
            boolean hasFilter = filters != null && filters.size() > 0 && !gm.checkFiltersAreEmpty(outputTable);
            if (columnsEntries != null && columnsEntries.size() > 0) {
                if (!hasFilter && !(outputTable.isReject() || outputTable.isRejectInnerJoin())) {
                    allNotRejectTablesHaveFilter = false;
                }
                if (outputTable.isReject()) {
                    atLeastOneReject = true;
                }
            }

            if (nameToOutputConnection.get(outputTableName) != null) {
                sb.append(cr + gm.indent(indent) + outputTableName + " = null;");
            }

        }
        // ///////////////////////////////////////////////////////////////////

        sb.append(cr);

        if (allNotRejectTablesHaveFilter && atLeastOneReject) {
            // write rejected = false;
            sb.append(cr + gm.indent(indent) + "boolean " + rejected + " = true;");
        }
        if (atLeastOneInputTableWithInnerJoin && atLeastOneRejectInnerJoin) {
            // write rejectedInnerJoin = false;
            sb.append(cr + gm.indent(indent) + "boolean " + rejectedInnerJoin + " = true;");
        }

        // write conditions for inner join reject
        if (validLookupTables.size() > 0 && lstSizeOutputs > 0 && atLeastOneInputTableWithInnerJoin) {
            sb.append(cr + gm.indent(indent) + "if(");
            String and = null;
            for (ExternalMapperTable validLookupTable : validLookupTables) {
                if (validLookupTable.isInnerJoin()) {
                    if (and == null) {
                        and = "";
                    } else {
                        and = " &&";
                    }
                    sb.append(and + " " + validLookupTable.getName() + "FromHash != null");
                }
            }
            sb.append(" ) {");
            closeTestInnerJoinConditionsBracket = true;
            indent++;
            if (atLeastOneInputTableWithInnerJoin && atLeastOneRejectInnerJoin) {
                sb.append(cr + gm.indent(indent) + rejectedInnerJoin + " = false;");
            }

        }

        // ///////////////////////////////////////////////////////////////////
        // run through output tables list for generating intilization of outputs arrays
        int dummyVarCounter = 0;
        for (int indexCurrentTable = 0; indexCurrentTable < lstSizeOutputs; indexCurrentTable++) {
            ExternalMapperTable outputTable = (ExternalMapperTable) outputTablesSortedByReject.get(indexCurrentTable);
            List<ExternalMapperTableEntry> outputTableEntries = outputTable.getMetadataTableEntries();
            String outputTableName = outputTable.getName();
            boolean connectionExists = true;
            if (outputTableEntries == null || nameToOutputConnection.get(outputTableName) == null) {
                connectionExists = false;
            }

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
                    if (atLeastOneReject && allNotRejectTablesHaveFilter) {
                        sb.append(" else {");
                        indent++;
                        sb.append(cr + gm.indent(indent) + rejected + " = false;");
                        indent--;
                        sb.append(cr + gm.indent(indent) + "}");
                    }
                    closeTestInnerJoinConditionsBracket = false;
                }
            }

            // No connection matching and no checking errors
            if (!connectionExists && !checkingSyntax) {
                continue;
            }
            if (rejectValueHasJustChanged) {
                sb.append(cr + gm.indent(indent) + "// ###### START REJECTS ##### ");
            }

            // write filters conditions and code to execute
            if (!currentIsReject && !currentIsRejectInnerJoin || rejectValueHasJustChanged
                    && oneFilterForNotRejectTable || currentIsReject && allNotRejectTablesHaveFilter
                    || currentIsRejectInnerJoin && atLeastOneInputTableWithInnerJoin || checkingSyntax) {

                boolean closeFilterOrRejectBracket = false;
                if (currentIsReject || currentIsRejectInnerJoin) {
                    sb.append(cr + cr + gm.indent(indent) + "// # Output reject table: '" + outputTableName + "'");
                } else {
                    sb.append(cr + cr + gm.indent(indent) + "// # Output table: '" + outputTableName + "'");
                }
                if (hasFilters || currentIsReject || currentIsRejectInnerJoin && atLeastOneInputTableWithInnerJoin) {
                    sb.append(cr + gm.indent(indent) + "// # Filter conditions ");

                    String key = CodeGenerationUtils.buildProblemKey(uniqueNameComponent,
                            JavaGenerationManager.PROBLEM_KEY_FIELD.FILTER.toString(), outputTableName, null);
                    if (checkingSyntax) {
                        sb.append("\n").append(CodeGenerationUtils.buildJavaStartFieldKey(key));
                    }

                    String ifConditions = gm.indent(indent) + "if( ";

                    String rejectedTests = null;
                    if (allNotRejectTablesHaveFilter && atLeastOneReject && currentIsReject && currentIsRejectInnerJoin
                            && atLeastOneInputTableWithInnerJoin) {
                        rejectedTests = rejected + " || " + rejectedInnerJoin;
                        if (hasFilters) {
                            rejectedTests = "(" + rejectedTests + ")";
                        }
                    } else if (allNotRejectTablesHaveFilter && atLeastOneReject && currentIsReject) {
                        rejectedTests = rejected;
                    } else if (currentIsRejectInnerJoin && atLeastOneInputTableWithInnerJoin) {
                        rejectedTests = rejectedInnerJoin;
                    }
                    if (hasFilters) {
                        String filtersConditions = gm.buildConditions(filters, expressionParser);
                        if (rejectedTests == null) {
                            ifConditions += filtersConditions;
                        } else {
                            ifConditions += rejectedTests + " && (" + filtersConditions + ")";
                        }
                    } else {
                        ifConditions += rejectedTests;
                    }
                    ifConditions += " ) {";

                    sb.append(cr).append(ifConditions);

                    if (checkingSyntax) {
                        sb.append("\n").append(CodeGenerationUtils.buildJavaEndFieldKey(key));
                    }

                    indent++;
                    closeFilterOrRejectBracket = true;
                    if (allNotRejectTablesHaveFilter && !(currentIsReject || currentIsRejectInnerJoin)
                            && atLeastOneReject) {
                        sb.append(cr + gm.indent(indent) + rejected + " = false;");
                    }
                }

                if (!currentIsReject && !currentIsRejectInnerJoin || currentIsReject || currentIsRejectInnerJoin
                        && atLeastOneInputTableWithInnerJoin || checkingSyntax) {
                    for (ExternalMapperTableEntry outputTableEntry : outputTableEntries) {
                        String outputColumnName = outputTableEntry.getName();
                        String outputExpression = outputTableEntry.getExpression();
                        if (outputExpression == null || outputExpression.trim().length() == 0) {
                            outputExpression = JavaTypesManager.getDefaultValueFromJavaIdType(outputTableEntry
                                    .getType(), outputTableEntry.isNullable());
                        }

                        String key = CodeGenerationUtils.buildProblemKey(uniqueNameComponent,
                                JavaGenerationManager.PROBLEM_KEY_FIELD.METADATA_COLUMN.toString(), outputTableName,
                                outputColumnName);
                        if (checkingSyntax) {
                            sb.append("\n").append(CodeGenerationUtils.buildJavaStartFieldKey(key));
                        }

                        String assignationVar = null;
                        if (connectionExists) {
                            assignationVar = gm.getGeneratedCodeTableColumnVariable(outputTableName + "_tmp",
                                    outputColumnName);
                        } else {
                            assignationVar = JavaTypesManager.getTypeToGenerate(outputTableEntry.getType(),
                                    outputTableEntry.isNullable())
                                    + " dummyVar" + (dummyVarCounter++);
                        }
                        String expression = gm.indent(indent) + assignationVar + " = " + outputExpression + ";";

                        sb.append(cr).append(expression);

                        if (checkingSyntax) {
                            sb.append("\n").append(CodeGenerationUtils.buildJavaEndFieldKey(key));
                        }

                    } // for entries

                    if (connectionExists) {
                        sb.append(cr + gm.indent(indent) + outputTableName + " = " + outputTableName + "_tmp;");
                    }

                }
                if (closeFilterOrRejectBracket) {
                    indent--;
                    sb.append(cr + gm.indent(indent) + "}");
                }

            }
            lastValueReject = currentIsReject || currentIsRejectInnerJoin;

            boolean isLastTable = indexCurrentTable == lstSizeOutputs - 1;
            if (closeTestInnerJoinConditionsBracket && isLastTable) {
                indent--;
                sb.append(cr + gm.indent(indent) + "}");
                closeTestInnerJoinConditionsBracket = false;
            }

        } // for output tables

        sb.append(cr + gm.indent(indent) + "// ###############################");

        sb.append(cr);
        indent--;
        sb.append(cr + gm.indent(indent) + "} // end of Var scope");
        sb.append(cr);

        // end of code to copy in template
        // ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        System.out.println(sb);
    }

}
