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
package org.talend.designer.mapper.language.perl;

import org.talend.core.model.process.Problem;
import org.talend.core.model.temp.ECodeLanguage;
import org.talend.designer.core.language.perl.ICodeSyntaxChecker;
import org.talend.designer.mapper.language.AbstractLanguage;
import org.talend.designer.runprocess.language.perl.PerlExpressionSyntaxChecker;
import org.talend.designer.runprocess.language.perl.SyntaxCheckerFactory;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class PerlLanguage extends AbstractLanguage {

    private static final String DOUBLE_ESCAPE = "\\";

    private static final String PREFIX_VARIABLE_NAME = "$";

    private static final String AND_CONDITION = " && ";

    private static final String PREFIX_FIELD_NAME = "[";

    private static final String SUFFIX_FIELD_NAME = "]";

    private static final String PREFIX_TABLE_NAME = PREFIX_VARIABLE_NAME;

    private static final String PREFIX_TABLE_NAME_REGEXP = DOUBLE_ESCAPE + PREFIX_TABLE_NAME;

    private static final String SUFFIX_TABLE_NAME = "";

    private static final String SUFFIX_TABLE_NAME_REGEXP = SUFFIX_TABLE_NAME;

    private static final String PREFIX_FIELD_NAME_REGEXP = DOUBLE_ESCAPE + PREFIX_FIELD_NAME;

    private static final String SUFFIX_FIELD_NAME_REGEXP = DOUBLE_ESCAPE + SUFFIX_FIELD_NAME;

    private static final String LOCATION_PATTERN = PREFIX_TABLE_NAME_REGEXP + "\\s*(\\w+)\\s*" + PREFIX_FIELD_NAME_REGEXP
            + "\\s*(\\w+)\\s*" + SUFFIX_FIELD_NAME_REGEXP;

    /**
     * {0} and {1} must be replaced respectively by the table name and the column name.
     */
    private static final String SUBST_PATTERN_FOR_PREFIX_COLUMN_NAME = PREFIX_TABLE_NAME_REGEXP + "\\s*({0})\\s*"
            + PREFIX_FIELD_NAME_REGEXP + "\\s*({1})\\s*" + SUFFIX_FIELD_NAME_REGEXP;

    /**
     * {0} and {1} must be replaced respectively by the table name and the column name.
     */
    private static final String SUBST_PATTERN_FOR_REPLACE_LOCATION = PREFIX_TABLE_NAME_REGEXP + "(\\s*){0}(\\s*)"
            + SUFFIX_TABLE_NAME_REGEXP + "(\\s*)" + PREFIX_FIELD_NAME_REGEXP + "(\\s*){1}(\\s*)" + SUFFIX_FIELD_NAME_REGEXP;

    /**
     * {0} and {1} must be replaced respectively by the table name and the column name.
     */
    private static final String TEMPLATE_TABLE_VARIABLE = PREFIX_TABLE_NAME + "{0}";

    /**
     * {0} and {1} must be replaced respectively by the table name and the column name.
     */
    private static final String TEMPLATE_TABLE_COLUMN_VARIABLE = TEMPLATE_TABLE_VARIABLE + PREFIX_FIELD_NAME + "{1}" + SUFFIX_FIELD_NAME;

    /**
     * {0} and {1} must be replaced respectively by the table name and the column name.
     */
    private static final String TEMPLATE_GENERATED_CODE_TABLE_COLUMN_VARIABLE = PREFIX_TABLE_NAME + "{0}" + PREFIX_FIELD_NAME + "{0}__{1}"
            + SUFFIX_FIELD_NAME;

    /**
     * {0} and {1} must be replaced respectively by the table name and the column name.
     */
    private static final String TEMPLATE_VARS_COLUMN_VARIABLE = PREFIX_VARIABLE_NAME + "{0}";

    private ICodeSyntaxChecker syntaxChecker;

    /**
     * DOC amaumont PerlLanguage constructor comment.
     */
    public PerlLanguage() {
        super();
        this.syntaxChecker = SyntaxCheckerFactory.getInstance().getSyntaxChecker(ECodeLanguage.PERL);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.model.language.ILanguage#getCouplePattern()
     */
    public String getLocationPattern() {
        return LOCATION_PATTERN;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.model.language.ILanguage#getPREFIX_FIELD_NAME_REGEXP()
     */
    public String getPrefixFieldRegexp() {
        return PREFIX_FIELD_NAME_REGEXP;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.model.language.ILanguage#getPREFIX_TABLE_NAME_REGEXP()
     */
    public String getPrefixTableRegexp() {
        return PREFIX_TABLE_NAME_REGEXP;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.model.language.ILanguage#getSUFFIX_FIELD_NAME_REGEXP()
     */
    public String getSuffixFieldRegexp() {
        return SUFFIX_FIELD_NAME_REGEXP;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.model.language.ILanguage#getSUFFIX_TABLE_NAME_REGEXP()
     */
    public String getSuffixTableRegexp() {
        return SUFFIX_TABLE_NAME_REGEXP;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.model.language.ILanguage#getSubstPatternForPrefixColumnName()
     */
    public String getSubstPatternForPrefixColumnName() {
        return SUBST_PATTERN_FOR_PREFIX_COLUMN_NAME;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.model.language.ILanguage#getSubstPatternForReplaceLocation()
     */
    public String getSubstPatternForReplaceLocation() {
        return SUBST_PATTERN_FOR_REPLACE_LOCATION;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.model.language.ILanguage#getTEMPLATE_TABLE_COLUMN_VARIABLE()
     */
    public String getTemplateTableColumnVariable() {
        return TEMPLATE_TABLE_COLUMN_VARIABLE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.language.ILanguage#getTEMPLATE_PROCESS_COLUMN_VARIABLE()
     */
    public String getTemplateVarsColumnVariable() {
        return TEMPLATE_VARS_COLUMN_VARIABLE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.language.ILanguage#getTEMPLATE_GENERATED_CODE_TABLE_COLUMN_VARIABLE()
     */
    public String getTemplateGeneratedCodeTableColumnVariable() {
        return TEMPLATE_GENERATED_CODE_TABLE_COLUMN_VARIABLE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.language.ILanguage#getAndCondition()
     */
    public String getAndCondition() {
        return AND_CONDITION;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.language.ILanguage#getPrefixField()
     */
    public String getPrefixField() {
        return PREFIX_FIELD_NAME;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.language.ILanguage#getPrefixTable()
     */
    public String getPrefixTable() {
        return PREFIX_TABLE_NAME;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.language.ILanguage#getSuffixField()
     */
    public String getSuffixField() {
        // TODO Auto-generated method stub
        return SUFFIX_FIELD_NAME;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.language.ILanguage#getSuffixTable()
     */
    public String getSuffixTable() {
        return SUFFIX_TABLE_NAME;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.language.ILanguage#getTemplateTableVariable()
     */
    public String getTemplateTableVariable() {
        return TEMPLATE_TABLE_VARIABLE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.language.ILanguage#checkExpressionSyntax(java.lang.String)
     */
    public Problem checkExpressionSyntax(String expression) {
        return syntaxChecker.checkSyntax(expression);
    }

}
