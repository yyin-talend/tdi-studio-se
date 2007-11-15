// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.dbmap.language.generation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.utils.StringUtils;
import org.talend.commons.utils.data.text.StringHelper;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.designer.dbmap.DbMapComponent;
import org.talend.designer.dbmap.external.data.ExternalDbMapEntry;
import org.talend.designer.dbmap.external.data.ExternalDbMapTable;
import org.talend.designer.dbmap.language.IDbLanguage;
import org.talend.designer.dbmap.language.IDbOperatorManager;
import org.talend.designer.dbmap.model.tableentry.TableEntryLocation;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public abstract class DbGenerationManager {

    private Map<String, ExternalDbMapTable> nameToInputTable;

    private HashMap<String, ExternalDbMapTable> nameToVarsTable;

    protected IDbLanguage language;

    /**
     * DOC amaumont GenerationManager constructor comment.
     * 
     * @param language2
     */
    public DbGenerationManager(IDbLanguage language) {
        super();
        this.language = language;
    }

    /**
     * DOC amaumont Comment method "setInputTables".
     * 
     * @param inputTables
     */
    public void setInputTables(List<ExternalDbMapTable> inputTables) {
        // this.inputTables = inputTables;
        nameToInputTable = new HashMap<String, ExternalDbMapTable>(inputTables.size());
        for (ExternalDbMapTable table : inputTables) {
            nameToInputTable.put(table.getName(), table);
        }
    }

    /**
     * DOC amaumont Comment method "setInputTables".
     * 
     * @param varsTables
     */
    public void setVarsTables(List<ExternalDbMapTable> varsTables) {
        // this.inputTables = varsTables;
        nameToVarsTable = new HashMap<String, ExternalDbMapTable>(varsTables.size());
        for (ExternalDbMapTable table : varsTables) {
            nameToVarsTable.put(table.getName(), table);
        }
    }

    public ExternalDbMapTable getInputTable(String tableName) {
        return nameToInputTable.get(tableName);
    }

    public ExternalDbMapTable getVarsTable(String tableName) {
        return nameToVarsTable.get(tableName);
    }

    public boolean isInputTable(String tableName) {
        return getInputTable(tableName) != null;
    }

    public boolean isVarsTable(String tableName) {
        return getVarsTable(tableName) != null;
    }

    public String getTableColumnVariable(String tableName, String columnName) {
        return StringHelper.replacePrms(this.language.getTemplateTableColumnVariable(), new Object[] { tableName,
                columnName });
    }

    public String getGeneratedCodeTableColumnVariable(String tableName, String columnName) {
        return StringHelper.replacePrms(this.language.getTemplateGeneratedCodeTableColumnVariable(), new Object[] {
                tableName, columnName });
    }

    public String getTableColumnVariable(TableEntryLocation location) {
        return this.language.getLocation(location.tableName, location.columnName);
    }

    public String getVarsColumnVariable(String columnName) {
        return StringHelper.replacePrms(this.language.getTemplateVarsColumnVariable(), new Object[] { columnName });
    }

    public String indent(final int i) {
        return StringUtils.repeat("  ", i); //$NON-NLS-1$
    }

    /**
     * DOC amaumont Comment method "ckeckConstraintsAreEmpty".
     * 
     * @param ExternalDbMapTable
     * @return
     */
    public boolean checkFiltersAreEmpty(ExternalDbMapTable outputTable) {
        List<ExternalDbMapEntry> constraints = outputTable.getCustomConditionsEntries();
        int lstSize = constraints.size();
        boolean oneConstraintIsNotEmpty = false;
        for (int i = 0; i < lstSize; i++) {

            String constraintExpression = ((ExternalDbMapEntry) constraints.get(i)).getExpression();
            if (constraintExpression != null && constraintExpression.trim().length() > 0) {
                oneConstraintIsNotEmpty = true;
                break;
            }
        }
        return !oneConstraintIsNotEmpty;
    }

    /**
     * Getter for language.
     * 
     * @return the language
     */
    public IDbLanguage getLanguage() {
        return this.language;
    }

    public IDbOperatorManager getOperatorsManager() {
        return this.language.getOperatorsManager();
    }

    public abstract String buildSqlSelect(DbMapComponent component, String tableName);

    /**
     * DOC amaumont Comment method "removeUnmatchingEntriesWithColumnsOfMetadataTable".
     * 
     * @param outputTable
     * @param metadataTable
     */
    protected ExternalDbMapTable removeUnmatchingEntriesWithColumnsOfMetadataTable(
            ExternalDbMapTable externalDbMapTable, IMetadataTable metadataTable) {
        ExternalDbMapTable clonedTable = null;
        try {
            clonedTable = (ExternalDbMapTable) externalDbMapTable.clone();
        } catch (CloneNotSupportedException e) {
            ExceptionHandler.process(e);
        }

        List<ExternalDbMapEntry> metadataTableEntries = clonedTable.getMetadataTableEntries();

        if (metadataTableEntries != null) {

            HashMap<String, IMetadataColumn> hNameToColumn = new HashMap<String, IMetadataColumn>();

            List<IMetadataColumn> listColumns = metadataTable.getListColumns();
            for (IMetadataColumn column : listColumns) {
                hNameToColumn.put(column.getLabel(), column);
            }
            List<ExternalDbMapEntry> dbMapEntriesToRemove = new ArrayList<ExternalDbMapEntry>();
            for (ExternalDbMapEntry externalTableEntry : metadataTableEntries) {
                String entryName = externalTableEntry.getName();
                IMetadataColumn column = hNameToColumn.get(entryName);
                if (column == null) {
                    dbMapEntriesToRemove.add(externalTableEntry);
                }
            }
            metadataTableEntries.removeAll(dbMapEntriesToRemove);
        }
        return clonedTable;

    }

}
