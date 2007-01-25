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
package org.talend.designer.mapper.model.table;

import java.util.List;

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
import org.talend.designer.mapper.external.data.ExternalMapperTable;
import org.talend.designer.mapper.external.data.ExternalMapperTableEntry;
import org.talend.designer.mapper.managers.MapperManager;
import org.talend.designer.mapper.model.tableentry.IColumnEntry;
import org.talend.designer.mapper.model.tableentry.ITableEntry;
import org.talend.designer.mapper.model.tableentry.VarTableEntry;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class VarsTable extends AbstractDataMapTable {

    private static final PatternCompiler COMPILER = new Perl5Compiler();

    public static final String PREFIX_VARS_TABLE_NAME = "Var";

    public static final String VALID_PATTERN_COLUMN_NAME = "^[a-zA-Z_][a-zA-Z_0-9]*$";

    public VarsTable(MapperManager mapperManager, String name) {
        super(mapperManager, name);
    }

    public void initFromExternalData(ExternalMapperTable externalMapperTable) {
        super.initFromExternalData(externalMapperTable);
        if (externalMapperTable != null) {
            List<ExternalMapperTableEntry> metadataTableEntries = externalMapperTable.getMetadataTableEntries();
            if (metadataTableEntries != null) {
                for (ExternalMapperTableEntry externalMapperTableEntry : metadataTableEntries) {
                    IColumnEntry dataMapTableEntry = new VarTableEntry(this, externalMapperTableEntry.getName(), externalMapperTableEntry
                            .getExpression(), externalMapperTableEntry.getType());
                    addColumnEntry(dataMapTableEntry);
                }
            }
        } else {
            setMinimized(externalMapperTable.isMinimized());
        }
    }

    /**
     * Manage to find a unique name with the given name.
     * 
     * @param titleName
     */
    public String findUniqueColumnName(String baseName) {
        if (baseName == null) {
            throw new IllegalArgumentException("baseName can't be null");
        }
        String uniqueName = baseName + 1;

        int counter = 1;
        boolean exists = true;
        while (exists) {
            exists = !checkValidColumnName(uniqueName);
            if (!exists) {
                break;
            }
            uniqueName = baseName + counter++;
        }
        return uniqueName;
    }

    /**
     * Check if the given name will be unique in the process. If another link already exists with that name, false will
     * be returned.
     * 
     * @param uniqueName
     * @return true if the name is unique
     */
    public boolean checkValidColumnName(String connectionName) {

        for (ITableEntry entry : dataMapTableEntries) {
            if (entry.getName().equals(connectionName)) {
                return false;
            }
        }

        Perl5Matcher matcher = new Perl5Matcher();
        Perl5Compiler compiler = new Perl5Compiler();
        Pattern pattern;

        try {
            pattern = compiler.compile("^[A-Za-z_][A-Za-z0-9_]*$");
            if (!matcher.matches(connectionName, pattern)) {
                return false;
            }
        } catch (MalformedPatternException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    /**
     * 
     * DOC amaumont Comment method "validateColumnName".
     * 
     * @param columnName
     * @return true if columnName has a valid value
     */
    public String validateColumnName(String columnName, int beanPosition) {
        if (columnName == null) {
            return "Error: Column name is null";
        }
        Pattern validPatternColumnNameRegexp = null;
        if (validPatternColumnNameRegexp == null) {
            try {
                validPatternColumnNameRegexp = COMPILER.compile(VALID_PATTERN_COLUMN_NAME);
            } catch (MalformedPatternException e) {
                throw new RuntimeException(e);
            }
        }
        Perl5Matcher matcher = new Perl5Matcher();
        boolean match = matcher.matches(columnName, validPatternColumnNameRegexp);
        // System.out.println(columnName + " -> "+ match);
        if (!match) {
            return "The column name '" + columnName + "' is invalid.";
        }

        int lstSize = dataMapTableEntries.size();
        for (int i = 0; i < lstSize; i++) {
            if (columnName.equals(dataMapTableEntries.get(i).getName()) && i != beanPosition) {
                return "The column name '" + columnName + "' already exists.";
            }

        }
        return null;
    }

}
