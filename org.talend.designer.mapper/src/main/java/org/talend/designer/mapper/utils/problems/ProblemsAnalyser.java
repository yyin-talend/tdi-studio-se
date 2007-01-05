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
package org.talend.designer.mapper.utils.problems;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.Problem;
import org.talend.core.model.process.Problem.ProblemStatus;
import org.talend.designer.mapper.MapperMain;
import org.talend.designer.mapper.external.connection.IOConnection;
import org.talend.designer.mapper.external.converter.ExternalDataConverter;
import org.talend.designer.mapper.external.data.ExternalMapperData;
import org.talend.designer.mapper.external.data.ExternalMapperTable;
import org.talend.designer.mapper.external.data.ExternalMapperTableEntry;
import org.talend.designer.mapper.language.ILanguage;
import org.talend.designer.mapper.language.LanguageProvider;
import org.talend.designer.mapper.language.generation.TableType;
import org.talend.designer.mapper.managers.MapperManager;
import org.talend.designer.mapper.model.table.InputTable;
import org.talend.designer.mapper.model.tableentry.IColumnEntry;
import org.talend.designer.mapper.model.tableentry.ITableEntry;
import org.talend.designer.mapper.model.tableentry.InputColumnTableEntry;
import org.talend.designer.mapper.model.tableentry.TableEntryLocation;
import org.talend.designer.mapper.utils.DataMapExpressionParser;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class ProblemsAnalyser {

    private MapperManager mapperManager;

    private List<Problem> problems = new ArrayList<Problem>();

    /**
     * DOC amaumont ProblemsAnalyser constructor comment.
     */
    public ProblemsAnalyser(MapperManager mapperManager) {
        super();
        this.mapperManager = mapperManager;
    }

    public List<Problem> checkProblems(ExternalMapperData externalData) {

        problems.clear();

        if (externalData != null) {

            List<ExternalMapperTable> extInputTables = new ArrayList<ExternalMapperTable>(externalData.getInputTables());
            List<ExternalMapperTable> extVarTables = new ArrayList<ExternalMapperTable>(externalData.getVarsTables());
            List<ExternalMapperTable> extOutputTables = new ArrayList<ExternalMapperTable>(externalData.getOutputTables());
            // loop on all tables
            checkExpressionSyntaxProblems(extInputTables);
            checkExpressionSyntaxProblems(extVarTables);
            checkExpressionSyntaxProblems(extOutputTables);

            List<? extends IConnection> incomingConnections = new ArrayList<IConnection>(this.mapperManager.getComponent()
                    .getIncomingConnections());
            ExternalDataConverter converter = new ExternalDataConverter();
            MapperMain mapperMain = mapperManager.getComponent().getMapperMain();
            ArrayList<IOConnection> inputsIOConnections = mapperMain.createIOConnections(incomingConnections);
            ArrayList<InputTable> inputTables = converter.prepareInputTables(inputsIOConnections, externalData);

            checkKeysProblems(inputTables);

        }

        return getProblems();
    }

    /**
     * DOC amaumont Comment method "checkKeysProblems".
     * 
     * @param incomingConnections
     * @param inputTables
     */
    private void checkKeysProblems(ArrayList<InputTable> inputTables) {

        ILanguage currentLanguage = LanguageProvider.getCurrentLanguage();
        for (InputTable table : inputTables) {
            if (table.isMainConnection()) {
                continue;
            }
            String tableName = table.getName();
            List<IColumnEntry> columnEntries = table.getColumnEntries();
            for (IColumnEntry entry : columnEntries) {
                InputColumnTableEntry inputEntry = (InputColumnTableEntry) entry;
                String columnName = entry.getName();
                if (mapperManager.checkEntryHasInvalidUncheckedKey(inputEntry)) {
                    String description = "Key of " + currentLanguage.getLocation(tableName, columnName) + " input entry should be checked";
                    addProblem(new Problem(null, description, ProblemStatus.WARNING));
                }
                if (mapperManager.checkEntryHasInvalidCheckedKey(inputEntry)) {
                    String description = "Key of " + currentLanguage.getLocation(tableName, columnName)
                            + " input entry should be unchecked";
                    addProblem(new Problem(null, description, ProblemStatus.WARNING));
                }
            }
        }

    }

    /**
     * DOC amaumont Comment method "checkExpressionSyntaxProblems".
     * 
     * @param tables
     */
    private void checkExpressionSyntaxProblems(List<ExternalMapperTable> tables) {
        for (ExternalMapperTable table : tables) {
            List<ExternalMapperTableEntry> metadataTableEntries = table.getMetadataTableEntries();
            // loop on all entries of current table
            if (metadataTableEntries != null) {
                for (ExternalMapperTableEntry entry : metadataTableEntries) {
                    addProblem(checkExpressionSyntax(entry.getExpression()));
                } // for (ExternalMapperTableEntry entry : metadataTableEntries) {
            }
            if (table.getConstraintTableEntries() != null) {
                for (ExternalMapperTableEntry entry : table.getConstraintTableEntries()) {
                    addProblem(checkExpressionSyntax(entry.getExpression()));
                }
            }
        } // for (ExternalMapperTable table : tables) {
    }

    /**
     * DOC amaumont Comment method "getProblems".
     * 
     * @return
     */
    public List<Problem> getProblems() {
        return new ArrayList<Problem>(problems);
    }

    /**
     * DOC amaumont Comment method "addProblem".
     * 
     * @param problem
     */
    private void addProblem(Problem problem) {
        if (problem != null) {
            problems.add(problem);
        }
    }

    /**
     * DOC amaumont Comment method "checkExpressionSyntax".
     * 
     * @param expression
     * @return
     */
    private Problem checkExpressionSyntax(String expression) {
        return mapperManager.checkExpressionSyntax(expression);
    }

    public void replaceLocation(TableEntryLocation oldLocation, TableEntryLocation newLocation, String newColumnName,
            ExternalMapperTableEntry entry, DataMapExpressionParser dataMapExpressionParser) {
        String currentExpression = entry.getExpression();
        if (currentExpression == null || currentExpression.length() == 0) {
            return;
        }
        TableEntryLocation[] tableEntryLocations = dataMapExpressionParser.parseTableEntryLocations(currentExpression);
        // loop on all locations of current expression
        for (int i = 0; i < tableEntryLocations.length; i++) {
            TableEntryLocation currentLocation = tableEntryLocations[i];
            if (currentLocation.equals(oldLocation)) {
                newLocation.columnName = newColumnName;
                currentExpression = dataMapExpressionParser.replaceLocation(currentExpression, currentLocation, newLocation);
            }
        } // for (int i = 0; i < tableEntryLocations.length; i++) {
        entry.setExpression(currentExpression);

    }

}
