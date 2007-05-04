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

import java.util.List;

import org.talend.core.model.metadata.IMetadataTable;

/**
 * DOC chuger class global comment. Detailled comment <br/>
 * 
 * $Id: FileInputDelimitedNode.java 93 2006-10-04 10:02:12 +0000 (mer., 04 oct. 2006) mhirt $
 * 
 */
public class FileInputLdifNode extends FileInputNode {

    private List<IMetadataTable> metadatas = null;

    /**
     * Constructs a new FileInputNode.
     */
    public FileInputLdifNode(String filename, List<IMetadataTable> metadatas, String encoding) {
        super("tFileInputLDIF"); //$NON-NLS-1$

        String[] paramNames = new String[] { "FILENAME", "ENCODING" }; //$NON-NLS-1$
        String[] paramValues = new String[] { filename, encoding };

        for (int i = 0; i < paramNames.length; i++) {
            if (paramValues[i] != null) {
                TextElementParameter param = new TextElementParameter(paramNames[i], paramValues[i]);
                addParameter(param);
            }
        }
        setMetadataList(metadatas);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.shadow.ShadowNode#getMetadataList()
     */
    @Override
    public List<IMetadataTable> getMetadataList() {
        return metadatas;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.shadow.ShadowNode#setMetadataList(java.util.List)
     */
    @Override
    public void setMetadataList(List<IMetadataTable> metadataList) {
        this.metadatas = metadataList;
    }
}
