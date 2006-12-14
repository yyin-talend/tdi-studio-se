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

import java.util.List;
import java.util.Map;

/**
 * DOC cantoine class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class FileInputXmlNode extends FileInputNode {

    private List<Map<String, String>> mapping = null;
    
    /**
     * Constructs a new FileInputNode.
     */
    public FileInputXmlNode(String filename, String loopQuery, List<Map<String, String>> mapping) {
        super("tFileInputXML");
        String[] paramNames = new String[] { "FILENAME", "LOOP_QUERY", "MAPPING" };
        Object[] paramValues = new Object[] { filename, loopQuery, mapping };

        for (int i = 0; i < paramNames.length; i++) {
            if (paramValues[i] != null) {
                ObjectElementParameter param = new ObjectElementParameter(paramNames[i], paramValues[i]);
                addParameter(param);
            }
        }
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.shadow.ShadowNode#getMappingList()
     */
    @Override
    public List<Map<String, String>> getMappingList() {
        return mapping;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.shadow.ShadowNode#setMappingList(java.util.List)
     */
    @Override
    public void setMappingList(List<Map<String, String>> mapping) {
        this.mapping = mapping;
    }
    
}
