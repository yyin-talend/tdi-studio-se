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
