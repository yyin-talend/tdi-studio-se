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

        String CR = "\n";
        String ONE_NOT_REJECT_CONSTRAINT_VALIDATED_VAR_NAME = "oneNotRejectConstraintValidated";

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

        ///////////////////////////////////////////////////////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////////////////////////////////////
        // INPUTS : initialization of input arrays from expressions keys and hashes 
        // 
        sb.append(CR + gm.indent(indent));
        sb.append(CR + gm.indent(indent) + "###############################");
        sb.append(CR + gm.indent(indent) + "# Input tables ");

        HashMap<String, ExternalMapperTable> hExternalInpuTables = new HashMap<String, ExternalMapperTable>();
        for (ExternalMapperTable inputTable : inputTables) {
            hExternalInpuTables.put(inputTable.getName(), inputTable);
        }

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
                        sb.append(CR + gm.indent(indent) + gm.buildNewArrayDeclarationWithKeyValue(tableName, aKeysValues, indent));
                    }

                } // if(externalTable != null) {
            } // else if(connectionType == EConnectionType.FLOW_REF) {
        } // for (IConnection connection : connections) {

        sb.append(CR + gm.indent(indent) + "###############################");
        ///////////////////////////////////////////////////////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////////////////////////////////////

        
        ///////////////////////////////////////////////////////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////////////////////////////////////
        // VARIABLES
        // 
        sb.append(CR + gm.indent(indent));
        sb.append(CR + gm.indent(indent) + "###############################");
        sb.append(CR + gm.indent(indent) + "# Vars tables");
        for (ExternalMapperTable varsTable : varsTables) {
            List<ExternalMapperTableEntry> varsTableEntries = varsTable.getMetadataTableEntries();
            if (varsTableEntries == null) {
                continue;
            }
            if (varsTableEntries.size() > 0) {
                sb.append(CR + gm.indent(indent) + gm.buildNewArrayDeclaration(varsTable.getName(), indent));
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

                sb.append(CR + gm.indent(indent) + "" + gm.getGeneratedCodeTableColumnVariable(varsTableName, varsColumnName) + " = "
                        + varExpressionWithPrefixs + ";");

            }
        }
        sb.append(CR + gm.indent(indent) + "###############################");
        ///////////////////////////////////////////////////////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////////////////////////////////////
        
        
        ///////////////////////////////////////////////////////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////////////////////////////////////
        // OUTPPUTS
        // 
        sb.append(CR + gm.indent(indent));
        sb.append(CR + gm.indent(indent) + "###############################");

        ArrayList<ExternalMapperTable> outputTablesSortedByReject = new ArrayList<ExternalMapperTable>(outputTables);
        // sorting outputs : not rejects first, rejects after
        Collections.sort(outputTablesSortedByReject, new Comparator<ExternalMapperTable>() {

            public int compare(ExternalMapperTable o1, ExternalMapperTable o2) {
                if (o1.isReject() != o2.isReject()) {
                    if (o1.isReject()) {
                        return 1;
                    } else {
                        return -1;
                    }
                }
                return 0;
            }

        });

        boolean lastValueReject = false;
        boolean oneConstraintForNotRejectTable = false;
        boolean serialRejectCanBeWrite = false;
        boolean allNotRejectTablesHaveConstraint = true;
        boolean atLeastOneReject = false;
        boolean closeTestRejectConditionsBracket = false;
        
        int lstSize = outputTablesSortedByReject.size();
        /////////////////////////////////////////////////////////////////////
        // init of allNotRejectTablesHaveConstraint and atLeastOneReject
        for (int i = 0; i < lstSize; i++) {
            ExternalMapperTable outputTable = (ExternalMapperTable) outputTablesSortedByReject.get(i);
            List<ExternalMapperTableEntry> columnsEntries = outputTable.getMetadataTableEntries();
            List<ExternalMapperTableEntry> constraints = outputTable.getConstraintTableEntries();
            boolean hasConstraint = constraints != null && constraints.size() > 0 && !gm.checkConstraintsAreEmpty(outputTable);
            if (columnsEntries != null && columnsEntries.size() > 0) {
                if (!hasConstraint && !outputTable.isReject()) {
                    allNotRejectTablesHaveConstraint = false;
                }
                if (outputTable.isReject()) {
                    atLeastOneReject = true;
                }
            }
        }
        /////////////////////////////////////////////////////////////////////

        if (allNotRejectTablesHaveConstraint && atLeastOneReject) {
            // write $oneNotRejectConstraintValidated = false;
            sb.append(CR + gm.indent(indent) + "$" + ONE_NOT_REJECT_CONSTRAINT_VALIDATED_VAR_NAME + " = false;");
        }

        /////////////////////////////////////////////////////////////////////
        // run through output tables list for generating intilization of outputs arrays
        for (int indexCurrentTable = 0; indexCurrentTable < lstSize; indexCurrentTable++) {
            ExternalMapperTable outputTable = (ExternalMapperTable) outputTablesSortedByReject.get(indexCurrentTable);
            List<ExternalMapperTableEntry> outputTableEntries = outputTable.getMetadataTableEntries();
            if (outputTableEntries == null) {
                continue;
            }
            String outputTableName = outputTable.getName();

            List<ExternalMapperTableEntry> constraints = outputTable.getConstraintTableEntries();

            boolean currentIsReject = outputTable.isReject();

            boolean hasConstraint = constraints != null && constraints.size() > 0 && !gm.checkConstraintsAreEmpty(outputTable);

            boolean rejectValueHasJustChanged = lastValueReject != currentIsReject;

            oneConstraintForNotRejectTable = !currentIsReject && hasConstraint;

            if (!currentIsReject && outputTableEntries.size() > 0) {
                sb.append(CR + gm.indent(indent) + "# Output table: '" + outputTableName + "'");
                // write output array initialization with empty list
                sb.append(CR + gm.indent(indent) + gm.buildNewArrayDeclaration(outputTableName, indent));

            } else if (rejectValueHasJustChanged) {
                sb.append(CR + gm.indent(indent) + "###### START REJECTS ##### ");
                // write outputs arrays initialization with empty list for reject tables
                for (int indexReject = indexCurrentTable; indexReject < lstSize; indexReject++) {
                    ExternalMapperTable outputRejectTable = (ExternalMapperTable) outputTablesSortedByReject.get(indexReject);
                    List<ExternalMapperTableEntry> metadataTableEntries = outputRejectTable.getMetadataTableEntries();
                    if (metadataTableEntries != null  && metadataTableEntries.size() > 0) {
                        sb.append(CR + gm.indent(indent) + "# Output reject table: '" + outputRejectTable.getName() + "'");
                        sb.append(CR + gm.indent(indent) + gm.buildNewArrayDeclaration(outputRejectTable.getName(), indent));
                    }
                }
            }

            // write condition of rejects code execution  
            if (rejectValueHasJustChanged && atLeastOneReject) {
                serialRejectCanBeWrite = allNotRejectTablesHaveConstraint;
                if (currentIsReject && serialRejectCanBeWrite) {
                    sb.append(CR + gm.indent(indent) + "if( ! $" + ONE_NOT_REJECT_CONSTRAINT_VALIDATED_VAR_NAME + " ) {");
                    closeTestRejectConditionsBracket = true;
                    indent++;
                }

            }

            // write condition of constraints and code to execute
            if (!currentIsReject || rejectValueHasJustChanged && oneConstraintForNotRejectTable || serialRejectCanBeWrite
                    && currentIsReject) {

                if (hasConstraint) {
                    sb.append(CR + gm.indent(indent) + "# Constraint condition ");
                    sb.append(CR + gm.indent(indent) + "if( ");
                    sb.append(gm.buildConditions(constraints, expressionParser));
                    sb.append("  ) {");
                    indent++;
                    if (allNotRejectTablesHaveConstraint && !currentIsReject && atLeastOneReject) {
                        sb.append(CR + gm.indent(indent) + "$" + ONE_NOT_REJECT_CONSTRAINT_VALIDATED_VAR_NAME + " = true;");
                    }
                }

                for (ExternalMapperTableEntry outputTableEntry : outputTableEntries) {
                    String outputColumnName = outputTableEntry.getName();
                    String outputExpression = outputTableEntry.getExpression();
                    if (outputExpression != null && outputExpression.trim().length() != 0) {

                        String outputExpressionToWrite = gm.prefixEntryLocationsForOutputExpression(outputExpression, expressionParser,
                                new TableType[] { TableType.INPUT, TableType.VARS });

                        sb.append(CR + gm.indent(indent) + gm.getGeneratedCodeTableColumnVariable(outputTableName, outputColumnName)
                                + " = " + outputExpressionToWrite + ";");

                    }

                } // for entries
                if (hasConstraint) {
                    indent--;
                    sb.append(CR + gm.indent(indent) + "}");
                }

            }

            lastValueReject = outputTable.isReject();
        } // for output tables
        if (closeTestRejectConditionsBracket) {
            indent--;
            sb.append(CR + gm.indent(indent) + "}");
        }
        sb.append(CR + gm.indent(indent) + "###############################");
        ///////////////////////////////////////////////////////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////////////////////////////////////

        // end of code to copy in template
        // ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        System.out.println(sb);
    }

}
