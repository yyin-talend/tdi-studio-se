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
package org.talend.designer.runprocess.shadow;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * DOC chuger class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class FileInputRegExpNode extends FileInputNode {

    /**
     * Constructs a new FileInputNode.
     */
    public FileInputRegExpNode(String filename, String rowSep, String regex, int limitRows, int headerRows,
            int footerRows, boolean removeEmptyRow, String encoding) {
        super("tFileInputRegex"); //$NON-NLS-1$
        if(regex.length() > 1){
            String reg = regex.substring(1, regex.length()-1);
            Pattern pattern = Pattern.compile(reg);
            Matcher matcher = pattern.matcher("");
            int columnCount = matcher.groupCount();
            if(columnCount > 0){
                this.setColumnNumber(columnCount);
            }
        }

        String[] paramNames = new String[] { "FILENAME", "ROWSEPARATOR", "REGEX", "LIMIT", "HEADER", "FOOTER", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
                "REMOVE_EMPTY_ROW", "ENCODING" }; //$NON-NLS-1$
        String[] paramValues = new String[] { filename, rowSep, regex, Integer.toString(limitRows),
                Integer.toString(headerRows), Integer.toString(footerRows), Boolean.toString(removeEmptyRow), encoding };

        for (int i = 0; i < paramNames.length; i++) {
            if (paramValues[i] != null) {
                TextElementParameter param = new TextElementParameter(paramNames[i], paramValues[i]);
                addParameter(param);
            }
        }
    }

}
