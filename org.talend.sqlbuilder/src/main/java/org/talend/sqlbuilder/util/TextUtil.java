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
package org.talend.sqlbuilder.util;

/**
 * Text handling utility.
 * 
 * @author qzhang
 */
public class TextUtil {

    public static final int DEFAULT_WRAPLENGTH = 150;

    private static final String NEWLINE_SEPARATOR = "\n"; //$NON-NLS-1$

    private static final String NEWLINE_EXPR = "\\n"; //$NON-NLS-1$

    private static final String RETURN_EXPR = "\\r"; //$NON-NLS-1$

    private static final String TAB_EXPR = "\\t"; //$NON-NLS-1$

    /**
     * qzhang TextUtil constructor comment.
     */
    public TextUtil() {
    }

    /**
     * Clear all linebreaks and carriage returns from input text.
     * 
     * @return cleaned string
     */
    public static String removeLineBreaks(String input) {
        if (input == null) {
            return null;
        }
        String tmp = input.replaceAll(NEWLINE_EXPR, " "); //$NON-NLS-1$
        tmp = tmp.replaceAll(TAB_EXPR, " "); //$NON-NLS-1$
        return tmp.replaceAll(RETURN_EXPR, ""); //$NON-NLS-1$
    }

    /**
     * Return the text reformatted to have a max charwidth of maxWidth.
     * 
     * @param maxWidth number of chars that the text can be wide.
     */
    public static String getWrappedText(String input) {
        return getWrappedText(input, DEFAULT_WRAPLENGTH);
    }

    /**
     * Return the text reformatted to have a max charwidth of maxWidth.
     * 
     * @param maxWidth number of chars that the text can be wide.
     */
    public static String getWrappedText(String input, int maxWidth) {

        if (input == null) {
            return ""; //$NON-NLS-1$
        }

        String[] text = input.split(NEWLINE_EXPR);
        String wrappedText = ""; //$NON-NLS-1$

        for (int i = 0; i < text.length; i++) {

            text[i] = text[i].replaceAll(RETURN_EXPR, ""); //$NON-NLS-1$

            if (text[i].length() == 0) {
                continue;
            }

            if (text[i].length() <= maxWidth) {
                wrappedText += text[i];

                if (i < text.length - 1) {
                    wrappedText += NEWLINE_SEPARATOR;
                }
            } else {

                String tmp = text[i];

                while (tmp.length() > maxWidth) {

                    for (int j = tmp.length() - 1; j >= 0; j--) {

                        if (j < maxWidth) {

                            char c = text[i].charAt(j);
                            if (c == ',') {
                                wrappedText += tmp.substring(0, j + 1);
                                wrappedText += NEWLINE_SEPARATOR;
                                tmp = tmp.substring(j + 1);
                                break;
                            }
                            if (c == ' ') {
                                wrappedText += tmp.substring(0, j + 1);
                                wrappedText += NEWLINE_SEPARATOR;
                                tmp = tmp.substring(j + 1);
                                break;
                            }
                        }

                        if (j == 0) {
                            wrappedText += tmp.substring(0, maxWidth + 1);
                            tmp = ""; //$NON-NLS-1$
                            break;
                        }
                    }

                }

                wrappedText += tmp;
                wrappedText += NEWLINE_SEPARATOR;
            }

        }

        return wrappedText;
    }

    /**
     * Replace all occurrences of replaceFrom in inputString with replaceTo.
     * 
     * @param inputString string to update
     * @param replaceFrom occurrences to replace
     * @param replaceTo string that replaces occurrences
     * @return
     */
    public static String replaceChar(String inputString, char replaceFrom, String replaceTo) {

        if (inputString == null || inputString.length() == 0) {
            return inputString;
        }

        StringBuffer buffer = new StringBuffer();
        char[] input = inputString.toCharArray();

        for (int i = 0; i < input.length; i++) {

            if (input[i] == replaceFrom) {
                buffer.append(replaceTo);
            } else {
                buffer.append(input[i]);
            }
        }

        return buffer.toString();
    }

    public static String addSqlQuots(String dbType, String sql, String schema) {
        if (dbType.equals("PostgreSQL")) {
            if (schema != null && !"".equals(schema)) {
                sql = "\"" + schema + "\".\"" + sql + "\"";
            } else {
                sql = "\"" + sql + "\"";
            }

        } else {
            if (schema != null && !"".equals(schema)) {
                sql = schema + "." + sql;
            }
        }
        return sql;
    }

    public static String removeQuots(String query) {
        if (query == null) {
            return "";
        }
        if (ConnectionParameters.isJavaProject()) {
            if (query.startsWith("\"") && query.endsWith("\"") && query.length() > 1) {
                return query.substring(1, query.length() - 1);
            } else {
                return query;
            }
        } else {
            if (query.startsWith("\'") && query.endsWith("\'") && query.length() > 1) {
                return query.substring(1, query.length() - 1);
            } else {
                return query;
            }

        }
    }
}
