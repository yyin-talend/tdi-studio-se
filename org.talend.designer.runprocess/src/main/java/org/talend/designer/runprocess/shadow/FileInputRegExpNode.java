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
package org.talend.designer.runprocess.shadow;

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
