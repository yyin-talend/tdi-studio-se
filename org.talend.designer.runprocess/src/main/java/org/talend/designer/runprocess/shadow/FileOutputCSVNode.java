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
package org.talend.designer.runprocess.shadow;

import org.talend.core.model.utils.TalendTextUtils;

/**
 * DOC chuger class global comment. Detailled comment <br/>
 * 
 * $Id: FileOutputXmlNode.java 4749 2007-07-24 05:37:22Z ftang $
 * 
 */
public class FileOutputCSVNode extends ShadowNode {

    /**
     * Constructs a new FileOutputXmlNode.
     */
    public FileOutputCSVNode(String filename, String encoding) {
        super("tFileOutputDelimited"); //$NON-NLS-1$

        TextElementParameter param0 = new TextElementParameter("CSV_OPTION", "true"); //$NON-NLS-1$
        TextElementParameter param1 = new TextElementParameter("FILENAME", filename); //$NON-NLS-1$
        TextElementParameter param2 = new TextElementParameter("ENCODING", encoding); //$NON-NLS-1$
        TextElementParameter param3 = new TextElementParameter("ROWSEPARATOR", "\"\\n\""); //$NON-NLS-1$
        TextElementParameter param4 = new TextElementParameter("FIELDSEPARATOR", "\";\""); //$NON-NLS-1$ //$NON-NLS-2$
        TextElementParameter param5 = new TextElementParameter("LIMIT", ""); //$NON-NLS-1$ //$NON-NLS-2$
        TextElementParameter param6 = new TextElementParameter("HEADER", ""); //$NON-NLS-1$ //$NON-NLS-2$
        TextElementParameter param7 = new TextElementParameter("FOOTER", ""); //$NON-NLS-1$ //$NON-NLS-2$
        TextElementParameter param8 = new TextElementParameter("ESCAPE_CHAR", TalendTextUtils.addQuotes("\"")); //$NON-NLS-1$ //$NON-NLS-2$
        TextElementParameter param9 = new TextElementParameter("TEXT_ENCLOSURE", TalendTextUtils.addQuotes("\""));
        TextElementParameter param10 = new TextElementParameter("REMOVE_EMPTY_ROW", "true");
        TextElementParameter param11 = new TextElementParameter("SPLIT", "false");
        TextElementParameter param12 = new TextElementParameter("SPLIT_EVERY", "1000");

        addParameter(param1);
        addParameter(param2);
        addParameter(param3);
        addParameter(param4);
        addParameter(param5);
        addParameter(param6);
        addParameter(param7);
        addParameter(param8);
        addParameter(param9);
        addParameter(param10);
        addParameter(param11);
        addParameter(param12);

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#isStart()
     */
    public boolean isStart() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#isThereLinkWithHash()
     */
    public boolean isThereLinkWithHash() {
        // TODO Auto-generated method stub
        return false;
    }

}
