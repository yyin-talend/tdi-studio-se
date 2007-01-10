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
package org.talend.designer.mapper.language.generation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.talend.commons.utils.data.text.StringHelper;
import org.talend.designer.mapper.external.data.ExternalMapperTable;
import org.talend.designer.mapper.external.data.ExternalMapperTableEntry;
import org.talend.designer.mapper.language.ILanguage;
import org.talend.designer.mapper.model.tableentry.TableEntryLocation;


/**
 * DOC amaumont  class global comment. Detailled comment
 * <br/>
 *
 * $Id$
 *
 */
public class GenerationManager {

    private Map<String, ExternalMapperTable> nameToInputTable;

    private HashMap<String, ExternalMapperTable> nameToVarsTable;


    protected ILanguage language;
    
    /**
     * DOC amaumont GenerationManager constructor comment.
     * @param language2
     */
    public GenerationManager(ILanguage language) {
        super();
        this.language = language;
    }

    /**
     * DOC amaumont Comment method "setInputTables".
     * 
     * @param inputTables
     */
    public void setInputTables(List<ExternalMapperTable> inputTables) {
        // this.inputTables = inputTables;
        nameToInputTable = new HashMap<String, ExternalMapperTable>(inputTables.size());
        for (ExternalMapperTable table : inputTables) {
            nameToInputTable.put(table.getName(), table);
        }
    }

    /**
     * DOC amaumont Comment method "setInputTables".
     * 
     * @param varsTables
     */
    public void setVarsTables(List<ExternalMapperTable> varsTables) {
        // this.inputTables = varsTables;
        nameToVarsTable = new HashMap<String, ExternalMapperTable>(varsTables.size());
        for (ExternalMapperTable table : varsTables) {
            nameToVarsTable.put(table.getName(), table);
        }
    }

    public ExternalMapperTable getInputTable(String tableName) {
        return nameToInputTable.get(tableName);
    }

    public ExternalMapperTable getVarsTable(String tableName) {
        return nameToVarsTable.get(tableName);
    }

    public boolean isInputTable(String tableName) {
        return getInputTable(tableName) != null;
    }

    public boolean isVarsTable(String tableName) {
        return getVarsTable(tableName) != null;
    }

    public String getTableColumnVariable(String tableName, String columnName) {
        return StringHelper.replacePrms(this.language.getTemplateTableColumnVariable(), new Object[] { tableName, columnName });
    }

    public String getGeneratedCodeTableColumnVariable(String tableName, String columnName) {
        return StringHelper
                .replacePrms(this.language.getTemplateGeneratedCodeTableColumnVariable(), new Object[] { tableName, columnName });
    }

    public String getTableColumnVariable(TableEntryLocation location) {
        return this.language.getLocation(location.tableName, location.columnName);
    }

    public String getVarsColumnVariable(String columnName) {
        return StringHelper.replacePrms(this.language.getTemplateVarsColumnVariable(), new Object[] { columnName });
    }

    public String indent(final int i) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int j = 0; j < i; j++) {
            stringBuilder.append("  ");
        }
        return stringBuilder.toString();
    }

    /**
     * DOC amaumont Comment method "ckeckConstraintsAreEmpty".
     * 
     * @param ExternalMapperTable
     * @return
     */
    public boolean checkFiltersAreEmpty(ExternalMapperTable outputTable) {
        List<ExternalMapperTableEntry> constraints = outputTable.getConstraintTableEntries();
        int lstSize = constraints.size();
        boolean oneConstraintIsNotEmpty = false;
        for (int i = 0; i < lstSize; i++) {

            String constraintExpression = ((ExternalMapperTableEntry) constraints.get(i)).getExpression();
            if (constraintExpression != null && constraintExpression.trim().length() > 0) {
                oneConstraintIsNotEmpty = true;
                break;
            }
        }
        return !oneConstraintIsNotEmpty;
    }

    
}

