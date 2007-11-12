// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the  agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//   
// ============================================================================
package org.talend.designer.core.ui.editor.properties;

import java.util.regex.Matcher;

import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.utils.TalendTextUtils;
import org.talend.designer.core.ui.editor.nodes.Node;

/**
 * DOC ggu class global comment. Detailled comment <br/>
 * 
 */
public final class NodeQueryCheckUtil {

    public static final String SELECT = "SELECT"; //$NON-NLS-1$   

    public static final String FROM = "FROM"; //$NON-NLS-1$   

    private static final int REGX_FLAG = java.util.regex.Pattern.CANON_EQ | java.util.regex.Pattern.CASE_INSENSITIVE
            | java.util.regex.Pattern.UNICODE_CASE;

    private static final String NL_REGX_ONE = "(\\s)+"; //$NON-NLS-1$   

    private static final String QUOTE = LanguageManager.getCurrentLanguage() == ECodeLanguage.JAVA ? TalendTextUtils.QUOTATION_MARK
            : TalendTextUtils.SINGLE_QUOTE;

    // reg: "(\s)*(select)(\s)+(.*?)(\s)+(from)(\s)+(.*)"
    private static final String SQL_REGX = "^" + QUOTE + "(\\s)*(" + SELECT + ")" + NL_REGX_ONE + "(.*?)" + NL_REGX_ONE
            + "(" + FROM + ")" + NL_REGX_ONE + "(.*)" + QUOTE + "$";

    private static final String NL_REGX = "(\\s)*"; //$NON-NLS-1$   

    // reg:(.+?)(\s)*\((\s)*(.*)(\s)*\)
    private static final String SQL_FUNC_REGX = "(.+?)" + NL_REGX + "\\(" + NL_REGX + "(.*)" + NL_REGX + "\\)";

    /**
     * 
     * DOC ggu Comment method "checkQueryOK".
     * 
     * @param node
     * @param sql
     * @return
     */
    public static boolean checkQueryOK(Node node, String sql) {

        //
        if (sql == null) {
            return false;
        }
        // replace the new line char
        sql = sql.replaceAll("\r", " ");
        sql = sql.replaceAll("\n", " ");

        // empty
        sql = sql.trim();
        if ("".equals(sql)) {
            return false;
        }

        // match sql query: select x.a, x.b from x
        java.util.regex.Pattern sqlRegex = java.util.regex.Pattern.compile(SQL_REGX, REGX_FLAG);
        Matcher matcher = sqlRegex.matcher(sql);
        if (!matcher.find()) {
            return false;
        }

        // get the columns
        matcher.lookingAt();
        String columns = matcher.group(4).trim();
        if ("".equals(columns)) {
            return false;
        }
        //
        if ("*".equals(columns)) {
            return true;
        }

        // include function
        java.util.regex.Pattern columnRegex = java.util.regex.Pattern.compile(SQL_FUNC_REGX, REGX_FLAG);
        Matcher columnMatcher = columnRegex.matcher(columns);
        // no functions
        if (!columnMatcher.find()) {
            return compareNodeTableColumns(node, columns);

        }

        return true;
    }

    private static boolean compareNodeTableColumns(Node node, String columns) {
        if (node.getMetadataList().size() == 0) {
            return true;
        }
        IMetadataTable metaTable = node.getMetadataList().get(0);
        if (metaTable == null || metaTable.getListColumns() == null) {
            return true;
        }
        int originColumnSize = metaTable.getListColumns().size();
        String[] columnArray = columns.split(",");
        // columns not match
        if (columnArray.length != originColumnSize) {
            return false;
        }
        return true;
    }

}
