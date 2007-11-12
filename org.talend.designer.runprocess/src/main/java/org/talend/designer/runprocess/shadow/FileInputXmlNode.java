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
    public FileInputXmlNode(String filename, String loopQuery, List<Map<String, String>> mapping, Integer loopLimit,
            String encoding) {
        super("tFileInputXML", mapping.size()); //$NON-NLS-1$

        String limitLoop = "";
        if (loopLimit != null && loopLimit != 0) {
            limitLoop = Integer.toString(loopLimit);
        }

        String[] paramNames = new String[] { "FILENAME", "LOOP_QUERY", "MAPPING", "LIMIT", "ENCODING" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        Object[] paramValues = new Object[] { filename, loopQuery, mapping, limitLoop, encoding };

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
