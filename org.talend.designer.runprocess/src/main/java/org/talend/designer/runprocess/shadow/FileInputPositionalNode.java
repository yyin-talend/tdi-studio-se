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
public class FileInputPositionalNode extends FileInputNode {

    /**
     * Constructs a new FileInputPositionalNode.
     */
    public FileInputPositionalNode(String filename, String rowSep, String pattern, int headerRows, int footerRows,
            int limitRows) {
        super("tFileInputPositional");

        String[] paramNames = new String[] { "FILENAME", "ROWSEPARATOR", "PATTERN", "HEADER", "FOOTER", "LIMIT" };
        String[] paramValues = new String[] { filename, rowSep, pattern, Integer.toString(headerRows),
                Integer.toString(footerRows), Integer.toString(limitRows) };

        for (int i = 0; i < paramNames.length; i++) {
            if (paramValues[i] != null) {
                TextElementParameter param = new TextElementParameter(paramNames[i], paramValues[i]);
                addParameter(param);
            }
        }
    }
}
