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
package org.talend.designer.mapper.language.generation;

import java.util.List;

import org.talend.commons.utils.generation.CodeGenerationUtils;
import org.talend.designer.mapper.external.data.ExternalMapperTableEntry;
import org.talend.designer.mapper.language.ILanguage;
import org.talend.designer.mapper.language.LanguageProvider;
import org.talend.designer.mapper.utils.DataMapExpressionParser;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id: GenerationManager.java 1299 2007-01-05 14:53:10Z amaumont $
 * 
 */
public class JavaGenerationManager extends GenerationManager {

    public JavaGenerationManager(ILanguage language) {
        super(language);
    }

    /**
     * DOC amaumont Comment method "buildLookupDataInstance".
     * 
     * @param name
     * 
     * @param tableName
     * @param keysValues
     * @param keysValues2
     * @param indent
     * @return
     */
    public String buildLookupDataInstance(String uniqueNameComponent, String name, String[] keysNames,
            String[] keysValues, int indent, boolean writeCommentedFieldKeys) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keysNames.length; i++) {

            String key = CodeGenerationUtils.buildProblemKey(uniqueNameComponent,
                    JavaGenerationManager.PROBLEM_KEY_FIELD.METADATA_COLUMN.toString(), name, keysNames[i]);
            if (writeCommentedFieldKeys) {
                sb.append("\n").append(CodeGenerationUtils.buildJavaStartFieldKey(key)); //$NON-NLS-1$
            }

            String expression = indent(indent) + name + "HashKey." + keysNames[i] + " = " + keysValues[i] + ";"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            sb.append("\n").append(expression); //$NON-NLS-1$

            if (writeCommentedFieldKeys) {
                sb.append("\n").append(CodeGenerationUtils.buildJavaEndFieldKey(key)); //$NON-NLS-1$
            }
        }
        sb.append("\n" + indent(indent) + name + "HashKey.hashCodeDirty = true;"); //$NON-NLS-1$ //$NON-NLS-2$
        String className = name + "Struct"; //$NON-NLS-1$
        sb
                .append("\n" + indent(indent) + className + " " + name + "FromHash = (" + className + ") tHash_" + name + ".get( " + name //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
                        + "HashKey );"); //$NON-NLS-1$
        sb.append("\n"); //$NON-NLS-1$
        return sb.toString();
    }

    /**
     * DOC amaumont Comment method "buildConditions".
     * 
     * @param gm
     * 
     * @param constraintTableEntries
     * @param expressionParser
     * @return
     */
    public String buildConditions(List<ExternalMapperTableEntry> constraintTableEntries,
            DataMapExpressionParser expressionParser) {
        int lstSize = constraintTableEntries.size();
        StringBuilder stringBuilder = new StringBuilder();
        String and = null;
        for (int i = 0; i < lstSize; i++) {

            String constraintExpression = ((ExternalMapperTableEntry) constraintTableEntries.get(i)).getExpression();
            if (constraintExpression == null) {
                continue;
            }
            if (and != null && constraintExpression.trim().length() > 0) {
                stringBuilder.append(and);
            }

            if (lstSize > 1) {
                stringBuilder.append(" ( " + constraintExpression + " ) "); //$NON-NLS-1$ //$NON-NLS-2$
            } else {
                stringBuilder.append(constraintExpression);
            }

            if (and == null) {
                and = language.getAndCondition();
            }
        }
        return stringBuilder.toString();
    }

    public static void main(String[] args) {

        JavaGenerationManager manager = new JavaGenerationManager(LanguageProvider.getJavaLanguage());
        // System.out.println(manager.insertFieldKey("table:column", "\nligne1\nligne2\nligne3"));
    }

    /**
     * 
     * DOC amaumont MapperManager class global comment. Detailled comment <br/>
     * 
     * $Id$
     * 
     */
    public enum PROBLEM_KEY_FIELD {
        METADATA_COLUMN,
        FILTER,
    }

}
