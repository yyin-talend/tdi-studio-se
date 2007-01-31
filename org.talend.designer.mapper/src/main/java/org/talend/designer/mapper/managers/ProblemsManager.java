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
package org.talend.designer.mapper.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.talend.core.language.ICodeProblemsChecker;
import org.talend.core.model.components.IODataComponent;
import org.talend.core.model.components.IODataComponentContainer;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.process.AbstractConnection;
import org.talend.core.model.process.AbstractNode;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IExternalNode;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.Problem;
import org.talend.core.model.process.Problem.ProblemStatus;
import org.talend.core.model.temp.ECodeLanguage;
import org.talend.designer.codegen.IAloneProcessNodeConfigurer;
import org.talend.designer.mapper.external.data.ExternalMapperData;
import org.talend.designer.mapper.language.ILanguage;
import org.talend.designer.mapper.language.LanguageProvider;
import org.talend.designer.mapper.language.generation.JavaGenerationManager;
import org.talend.designer.mapper.language.generation.JavaGenerationManager.PROBLEM_KEY_FIELD;
import org.talend.designer.mapper.model.tableentry.FilterTableEntry;
import org.talend.designer.mapper.model.tableentry.IColumnEntry;
import org.talend.designer.mapper.model.tableentry.ITableEntry;
import org.talend.designer.mapper.ui.visualmap.table.DataMapTableView;
import org.talend.designer.mapper.ui.visualmap.zone.Zone;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class ProblemsManager {

    private static final String EMPTY_STRING = "";

    private MapperManager mapperManager;

    private ICodeProblemsChecker codeChecker;

    private ECodeLanguage codeLanguage;

    private IAloneProcessNodeConfigurer nodeConfigurer;

    /**
     * DOC amaumont ProblemsManager constructor comment.
     */
    private ProblemsManager() {
        super();
    }

    /**
     * DOC amaumont ProblemsManager constructor comment.
     * 
     * @param manager
     */
    public ProblemsManager(final MapperManager mapperManager) {
        this.mapperManager = mapperManager;
        ILanguage currentLanguage = LanguageProvider.getCurrentLanguage();
        codeLanguage = currentLanguage.getCodeLanguage();
        codeChecker = currentLanguage.getCodeChecker();
        this.nodeConfigurer = new IAloneProcessNodeConfigurer() {

            public void configure(INode processNode) {

                IExternalNode mapperNode = mapperManager.getComponent();
                if (processNode.getUniqueName().equals(mapperNode.getUniqueName())) {

                    IExternalNode processExternalNode = (IExternalNode) processNode;
                    processExternalNode.setExternalData(mapperNode.getExternalData());

                    IODataComponentContainer dataComponents = mapperNode.getIODataComponents();

                    List<IODataComponent> mapperInputsDataComponent = (List<IODataComponent>) dataComponents.getInputs();
                    HashMap<String, IMetadataTable> connectionNameToInputMetadataTable = new HashMap<String, IMetadataTable>();
                    for (IODataComponent dataComponent : mapperInputsDataComponent) {
                        connectionNameToInputMetadataTable.put(dataComponent.getConnection().getName(), dataComponent.getTable());
                    }
                    List<IConnection> processIncomingConnections = (List<IConnection>) processExternalNode.getIncomingConnections();
                    for (IConnection connection : processIncomingConnections) {
                        if (connection instanceof AbstractConnection) {
                            IMetadataTable metadataTable = connectionNameToInputMetadataTable.get(connection.getName());
                            ((AbstractConnection) connection).setMetadataTable(metadataTable);
                        }
                    }

                    List<IMetadataTable> metadataListOut = new ArrayList<IMetadataTable>();

                    List<IODataComponent> mapperOutputsDataComponent = (List<IODataComponent>) dataComponents.getOuputs();
                    HashMap<String, IMetadataTable> connectionNameToOutputMetadataTable = new HashMap<String, IMetadataTable>();
                    for (IODataComponent dataComponent : mapperOutputsDataComponent) {
                        connectionNameToOutputMetadataTable.put(dataComponent.getConnection().getName(), dataComponent.getTable());
                    }
                    List<IConnection> processOutgoingConnections = (List<IConnection>) processExternalNode.getOutgoingConnections();
                    for (IConnection connection : processOutgoingConnections) {
                        if (connection instanceof AbstractConnection) {
                            IMetadataTable metadataTable = connectionNameToOutputMetadataTable.get(connection.getName());
                            ((AbstractConnection) connection).setMetadataTable(metadataTable);
                            metadataListOut.add(metadataTable);
                        }
                    }
                    processExternalNode.setMetadataList(metadataListOut);

                } else {
                    throw new IllegalArgumentException("Should be same instance...");
                }

            }

        };
    }

    /**
     * 
     * Check all problems and save in cache for Java only.
     */
    public void checkProblems() {
        if (codeLanguage == ECodeLanguage.JAVA) {
            codeChecker.checkProblems(nodeConfigurer);
        }
    }

    /**
     * DOC amaumont Comment method "checkJavaProblemsForEntry".
     * 
     * @param codeChecker
     * @param problemKeyField
     * @param tableName
     * @param entryName
     * @param forceGenerateJavaCode
     * @return
     */
    private List<Problem> checkJavaProblemsForEntry(PROBLEM_KEY_FIELD problemKeyField, String tableName, String entryName,
            boolean forceGenerateJavaCode) {
        String key = mapperManager.buildProblemKey(problemKeyField, tableName, entryName);
        if (forceGenerateJavaCode) {
            return codeChecker.checkProblemsFromKey(key, nodeConfigurer);
        } else {
            return codeChecker.getProblemsFromKey(key);
        }
    }

    /**
     * DOC amaumont Comment method "buildProblemKey".
     * 
     * @param uniqueName
     * @param problemKeyField
     * @param tableName
     * @param entryName
     */
    public String buildProblemKey(String uniqueName, PROBLEM_KEY_FIELD problemKeyField, String tableName, String entryName) {
        return JavaGenerationManager.buildProblemKey(uniqueName, problemKeyField, tableName, entryName);
    }

    /**
     * DOC amaumont Comment method "checkExpressionSyntax".
     * 
     * @param expression
     */
    public List<Problem> checkExpressionSyntax(String expression) {
        ICodeProblemsChecker codeChecker = LanguageProvider.getCurrentLanguage().getCodeChecker();
        return codeChecker.checkProblemsForExpression(expression);
    }

    /**
     * DOC amaumont Comment method "checkProblemsForAllEntries".
     * 
     * @param forceRefreshData TODO
     */
    public void checkProblemsForAllEntriesOfAllTables(boolean forceRefreshData) {
        List<DataMapTableView> tablesView = mapperManager.getInputsTablesView();
        tablesView.addAll(mapperManager.getVarsTablesView());
        tablesView.addAll(mapperManager.getOutputsTablesView());
        if (forceRefreshData) {
            mapperManager.getComponent().refreshMapperConnectorData();
            checkProblems();
        }
        for (DataMapTableView view : tablesView) {
            checkProblemsForAllEntries(view, false);
        }
    }

    /**
     * DOC amaumont Comment method "processAllExpressions".
     * 
     * @param forceRefreshData TODO
     */
    @SuppressWarnings("unchecked")
    public void checkProblemsForAllEntries(DataMapTableView dataMapTableView, boolean forceRefreshData) {
        if (forceRefreshData) {
            mapperManager.getComponent().refreshMapperConnectorData();
            checkProblems();
        }
        List<IColumnEntry> columnsEntriesList = dataMapTableView.getDataMapTable().getColumnEntries();
        if (checkProblemsForAllEntries(columnsEntriesList)) {
            dataMapTableView.getTableViewerCreatorForColumns().getTableViewer().refresh(true);
        }
        if (dataMapTableView.getZone() == Zone.OUTPUTS) {
            List<ITableEntry> constraintEntriesList = dataMapTableView.getTableViewerCreatorForFilters().getInputList();
            if (checkProblemsForAllEntries(constraintEntriesList)) {
                dataMapTableView.getTableViewerCreatorForFilters().getTableViewer().refresh(true);
            }
        }
    }

    private boolean checkProblemsForAllEntries(List<? extends ITableEntry> entriesList) {
        boolean errorsHasChanged = false;
        for (ITableEntry entry : entriesList) {
            boolean haveProblemsBefore = entry.getProblems() != null;
            mapperManager.getProblemsManager().checkProblemsForTableEntry(entry, false);
            boolean haveProblemsAfter = entry.getProblems() != null;
            if (haveProblemsBefore != haveProblemsAfter) {
                errorsHasChanged = true;
            }
        }
        return errorsHasChanged;
    }

    public void checkProblemsForTableEntry(ITableEntry tableEntry, boolean forceRefreshData) {

        if (forceRefreshData) {
            mapperManager.getComponent().refreshMapperConnectorData();
            checkProblems();
        }

        String expression = tableEntry.getExpression();
        List<Problem> problems = null;
        if (expression == null || EMPTY_STRING.equals(expression.trim())) {
            problems = null;
        } else {
            // System.out.println("check=" + expression);
            if (codeLanguage == ECodeLanguage.PERL) {
                problems = codeChecker.checkProblemsForExpression(expression);
            } else if (codeLanguage == ECodeLanguage.JAVA) {
                PROBLEM_KEY_FIELD problemKeyField = JavaGenerationManager.PROBLEM_KEY_FIELD.METADATA_COLUMN;
                String entryName = tableEntry.getName();
                if (tableEntry instanceof FilterTableEntry) {
                    problemKeyField = JavaGenerationManager.PROBLEM_KEY_FIELD.FILTER;
                    entryName = null;
                }
                problems = checkJavaProblemsForEntry(problemKeyField, tableEntry.getParent().getName(), entryName, forceRefreshData);
            }
            if (problems != null) {
                for (Iterator iter = problems.iterator(); iter.hasNext();) {
                    Problem problem = (Problem) iter.next();
                    ProblemStatus status = problem.getStatus();
                    if (status != ProblemStatus.ERROR) {
                        iter.remove();
                    }

                }

            } else {
                problems = null;
            }

            tableEntry.setProblems(problems);
        }

    }

}
