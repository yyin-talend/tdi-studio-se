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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * DOC chuger class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class FileOutputXmlNode extends ShadowNode {

    /**
     * Constructs a new FileOutputXmlNode.
     */
    public FileOutputXmlNode(String filename, String encoding) {
        super("tFileOutputXML"); //$NON-NLS-1$
        
        List<Map<String, String>> rootTags = new ArrayList<Map<String, String>>();

        TextElementParameter param1 = new TextElementParameter("FILENAME", filename); //$NON-NLS-1$
        TextElementParameter param2 = new TextElementParameter("ENCODING", encoding); //$NON-NLS-1$
        ObjectElementParameter param3 = new ObjectElementParameter("ROOT_TAGS", rootTags); //$NON-NLS-1$
        TextElementParameter param4 = new TextElementParameter("ROW_TAG", "row"); //$NON-NLS-1$ //$NON-NLS-2$
        TextElementParameter param5 = new TextElementParameter("COLNAME_AS_TAGNAME", "false"); //$NON-NLS-1$ //$NON-NLS-2$
        TextElementParameter param6 = new TextElementParameter("FIELD_TAG", "field"); //$NON-NLS-1$ //$NON-NLS-2$
        TextElementParameter param7 = new TextElementParameter("SPLIT", "false"); //$NON-NLS-1$ //$NON-NLS-2$
        TextElementParameter param8 = new TextElementParameter("SPLIT_EVERY", "1000"); //$NON-NLS-1$ //$NON-NLS-2$
        //TextElementParameter param9 = new TextElementParameter("SCHEMA", null);
        addParameter(param1);
        addParameter(param2);
        addParameter(param3);
        addParameter(param4);
        addParameter(param5);
        addParameter(param6);
        addParameter(param7);
        addParameter(param8);
        //addParameter(param9);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#isStart()
     */
    public boolean isStart() {
        return false;
    }

}
