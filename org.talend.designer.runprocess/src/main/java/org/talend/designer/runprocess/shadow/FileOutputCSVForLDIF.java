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
 * yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: FileOutputCSVForLDIF.java 下午03:30:59 2007-8-15 +0000 (2007-8-15) yzhang $
 * 
 */
public class FileOutputCSVForLDIF extends FileOutputCSVNode {

    List<IMetadataTable> metadataList;

    /**
     * yzhang FileOutputCSVForLDIF constructor comment.
     * 
     * @param filename
     * @param encoding
     */
    public FileOutputCSVForLDIF(String filename, String encoding) {
        super(filename, encoding);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.shadow.ShadowNode#getMetadataList()
     */
    @Override
    public List<IMetadataTable> getMetadataList() {
        return metadataList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.shadow.ShadowNode#setMetadataList(java.util.List)
     */
    @Override
    public void setMetadataList(List<IMetadataTable> metadataList) {
        this.metadataList = metadataList;
    }

}
