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
import java.util.List;

import org.talend.designer.mapper.external.data.ExternalMapperData;
import org.talend.designer.mapper.external.data.ExternalMapperTable;
import org.talend.designer.mapper.external.data.ExternalMapperTableEntry;
import org.talend.designer.mapper.language.LanguageProvider;
import org.talend.designer.mapper.managers.MapperManager;
import org.talend.designer.mapper.model.tableentry.ITableEntry;
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

    /**
     * DOC amaumont ProblemsAnalyser constructor comment.
     */
    public ProblemsAnalyser(MapperManager mapperManager) {
        super();
        this.mapperManager = mapperManager;
    }

    public void checkSyntaxForAllExpressions(ExternalMapperData externalData) {

        if (externalData != null) {

            // replace old location by new location for all expressions in mapper
            List<ExternalMapperTable> tables = new ArrayList<ExternalMapperTable>(externalData.getInputTables());
            tables.addAll(new ArrayList<ExternalMapperTable>(externalData.getVarsTables()));
            tables.addAll(new ArrayList<ExternalMapperTable>(externalData.getOutputTables()));
            DataMapExpressionParser dataMapExpressionParser = new DataMapExpressionParser(LanguageProvider.getCurrentLanguage());
            // TableEntryLocation oldLocation = new TableEntryLocation(conectionName, oldColumnName);
            // TableEntryLocation newLocation = new TableEntryLocation();
            // newLocation.tableName = conectionName;
            // loop on all tables
            for (ExternalMapperTable table : tables) {
                List<ExternalMapperTableEntry> metadataTableEntries = table.getMetadataTableEntries();
                // loop on all entries of current table
                for (ExternalMapperTableEntry entry : metadataTableEntries) {
                    checkEntryValidity(entry);
                } // for (ExternalMapperTableEntry entry : metadataTableEntries) {
                if (table.getConstraintTableEntries() != null) {
                    for (ExternalMapperTableEntry entry : table.getConstraintTableEntries()) {
                        checkEntryValidity(entry);
                    }
                }
            } // for (ExternalMapperTable table : tables) {

        }
    }

    /**
     * DOC amaumont Comment method "checkEntryValidity".
     * 
     * @param entry
     */
    private void checkEntryValidity(ExternalMapperTableEntry entry) {
        String errorMessage = checkExpressionSyntax(entry.getExpression());

    }

    /**
     * DOC amaumont Comment method "checkEntryValidity".
     * 
     * @param entry
     */
    private void checkEntryValidity(ITableEntry entry) {
        String errorMessage = checkExpressionSyntax(entry.getExpression());

    }

    /**
     * DOC amaumont Comment method "checkExpressionSyntax".
     * 
     * @param expression
     * @return
     */
    private String checkExpressionSyntax(String expression) {
        if (expression == null) {
            return null;
        }

        return null;
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
