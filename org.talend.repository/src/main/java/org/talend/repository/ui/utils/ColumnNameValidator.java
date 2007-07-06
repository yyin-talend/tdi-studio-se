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
package org.talend.repository.ui.utils;

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;

/**
 * This class is used for column name validation.
 * 
 * @author ftang, 2007-06-13 <br/>
 * 
 */
public class ColumnNameValidator {

    private static final String VALIDATE_PATTERN_COLUMN_NAME = "^[a-zA-Z_][a-zA-Z_0-9]*$"; //$NON-NLS-1$

    /**
     * Validates column name to avoid special characters.
     * 
     * @param columnName
     * @param index
     */
    public static String validateColumnNameFormat(String columnName, int index) {
        org.apache.oro.text.regex.Pattern validPatternColumnNameRegexp = null;
        PatternCompiler compiler = new Perl5Compiler();

        if (validPatternColumnNameRegexp == null) {
            try {
                validPatternColumnNameRegexp = compiler.compile(VALIDATE_PATTERN_COLUMN_NAME);
            } catch (MalformedPatternException e) {
                throw new RuntimeException(e);
            }
        }
        Perl5Matcher matcher = new Perl5Matcher();
        boolean match = matcher.matches(columnName, validPatternColumnNameRegexp);
        if (!match) {
            columnName = "Column" + index;
        }
        return columnName;
    }
}
