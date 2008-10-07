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
package org.talend.designer.core.utils;

import java.util.List;
import java.util.Map;

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
import org.apache.oro.text.regex.Perl5Substitution;
import org.apache.oro.text.regex.Util;
import org.talend.core.model.context.UpdateContextVariablesHelper;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.Element;
import org.talend.core.model.process.IElementParameter;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.ui.editor.nodes.Node;

/**
 * DOC xye class global comment. Detailled comment
 */
public final class UpgradeElementHelper {

    public static boolean isUseData(final Element element, final String name) {
        if (element == null || element.getElementParameters() == null) {
            return false;
        }
        for (IElementParameter param : element.getElementParameters()) {
            if (param.getName().equals(EParameterName.UNIQUE_NAME.getName())) {
                continue;
            }
            if (param.getValue() instanceof String) { // for TEXT / MEMO etc..
                String value = (String) param.getValue();
                if (valueContains(value, name)) {
                    return true;
                }
            } else if (param.getValue() instanceof List) { // for TABLE
                List<Map<String, Object>> tableValues = (List<Map<String, Object>>) param.getValue();
                for (Map<String, Object> line : tableValues) {
                    for (String key : line.keySet()) {
                        Object cellValue = line.get(key);
                        if (cellValue instanceof String) { // cell is text so
                            // test data
                            if (valueContains((String) cellValue, name)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public static void renameData(final Element element, final String oldName, final String newName) {

        if (element == null) {
            return;
        }

        if (element.getElementParameters() == null || element.getElementParameters().isEmpty()) {
            return;
        }

        if (oldName.equals(newName)) {
            return;
        }

        // see bug 4733 & 5167
        for (IElementParameter param : element.getElementParameters()) {
            if (param.getName().equals(EParameterName.UNIQUE_NAME.getName()) || isSQLQueryParameter(param)
                    || isJavaRowCodeParameter(element, param)) {
                continue;
            }
            if (param.getValue() instanceof String) { // for TEXT / MEMO etc..
                String value = (String) param.getValue();
                if (value.contains(oldName)) {
                    // param.setValue(value.replaceAll(oldName, newName));
                    String newValue = renameValues(value, oldName, newName);
                    if (!value.equals(newValue)) {
                        param.setValue(newValue);
                    }
                }
            } else if (param.getValue() instanceof List) { // for TABLE
                List<Map<String, Object>> tableValues = (List<Map<String, Object>>) param.getValue();
                for (Map<String, Object> line : tableValues) {
                    for (String key : line.keySet()) {
                        Object cellValue = line.get(key);
                        if (cellValue instanceof String) { // cell is text so
                            // rename data if
                            // needed
                            String value = (String) cellValue;
                            if (value.contains(oldName)) {
                                // line.put(key, value.replaceAll(oldName, newName));
                                String newValue = renameValues(value, oldName, newName);
                                if (!value.equals(newValue)) {
                                    line.put(key, newValue);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 
     * DOC ggu Comment method "renameValues".
     * 
     */
    private static String renameValues(final String value, final String oldName, final String newName) {
        if (value == null || oldName == null || newName == null) {
            return value; // keep original value
        }

        PatternCompiler compiler = new Perl5Compiler();
        Perl5Matcher matcher = new Perl5Matcher();
        matcher.setMultiline(true);
        Perl5Substitution substitution = new Perl5Substitution(newName + "$2", Perl5Substitution.INTERPOLATE_ALL);

        Pattern pattern;
        try {
            pattern = compiler.compile("\\b(" + UpdateContextVariablesHelper.replaceSpecialChar(oldName) + ")(\\b|\\_)");
        } catch (MalformedPatternException e) {
            return value; // keep original value
        }

        if (matcher.contains(value, pattern)) {
            // replace
            String returnValue = Util.substitute(matcher, pattern, substitution, value, Util.SUBSTITUTE_ALL);
            return returnValue;

        }
        return value; // keep original value

    }

    /**
     * 
     * DOC YeXiaowei Comment method "isSQLQueryParameter".
     * 
     * @param parameter
     * @return
     */
    private static boolean isSQLQueryParameter(final IElementParameter parameter) {
        return parameter.getField().equals(EParameterFieldType.MEMO_SQL) && parameter.getName().equals("QUERY");
    }

    /**
     * 
     * DOC xye Comment method "iJavaRowCodeParameter".
     * <p>
     * 
     * @see bug 5167
     * @param node
     * @param parameter
     * @return
     */
    private static boolean isJavaRowCodeParameter(final Element node, final IElementParameter parameter) {
        if (node instanceof Node) {
            if (((Node) node).getUniqueName().contains("tJavaRow")) {
                return parameter.getField().equals(EParameterFieldType.MEMO_JAVA) && parameter.getName().equals("CODE");
            }
        }
        return false;
    }

    private static boolean valueContains(String value, String toTest) {
        if (value.contains(toTest)) {
            Perl5Matcher matcher = new Perl5Matcher();
            Perl5Compiler compiler = new Perl5Compiler();
            Pattern pattern;

            try {
                pattern = compiler.compile("\\b(" + UpdateContextVariablesHelper.replaceSpecialChar(toTest) + ")(\\b|\\_)"); //$NON-NLS-1$
                if (matcher.contains(value, pattern)) {
                    return true;
                }
            } catch (MalformedPatternException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }
}
