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
package org.talend.designer.runprocess.shadow;

/**
 * DOC chuger class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class FileInputPositionalNode extends FileInputNode {

    /**
     * Constructs a new FileInputPositionalNode.
     */
    public FileInputPositionalNode(String filename, String rowSep, String pattern, int headerRows, int footerRows,
            int limitRows, boolean removeEmptyRow, String encoding) {
        super("tFileInputPositional", pattern.split(",").length); //$NON-NLS-1$

        String[] paramNames = new String[] {
                "FILENAME", "ROWSEPARATOR", "PATTERN", "HEADER", "FOOTER", "LIMIT", "REMOVE_EMPTY_ROW", "ENCODING" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
        String[] paramValues = new String[] { filename, rowSep, pattern, Integer.toString(headerRows),
                Integer.toString(footerRows), Integer.toString(limitRows), Boolean.toString(removeEmptyRow), encoding };

        for (int i = 0; i < paramNames.length; i++) {
            if (paramValues[i] != null) {
                TextElementParameter param = new TextElementParameter(paramNames[i], paramValues[i]);
                addParameter(param);
            }
        }
    }
}
