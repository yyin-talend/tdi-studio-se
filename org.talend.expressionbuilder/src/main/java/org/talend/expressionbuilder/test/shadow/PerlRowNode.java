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
package org.talend.expressionbuilder.test.shadow;

import java.util.ArrayList;
import java.util.List;

import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.MetadataTable;
import org.talend.designer.rowgenerator.shadow.TextElementParameter;
import org.talend.designer.rowgenerator.ui.editor.MetadataColumnExt;
import org.talend.designer.runprocess.shadow.ShadowNode;

/**
 * yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: TPerlRowNode.java 下午03:16:33 2007-8-20 +0000 (2007-8-20) yzhang $
 * 
 */
public class PerlRowNode extends ShadowNode {

    private List<IMetadataTable> metadatas;

    /**
     * yzhang PerlRowNode constructor comment.
     * 
     * @param componentName
     */
    public PerlRowNode(String expression) {
        super("tPerlRow");

        TextElementParameter parameter = new TextElementParameter("CODE", "$output_row[id] ="+expression+";");
        addParameter(parameter);

        IMetadataTable metadataTable = new MetadataTable();
        List<IMetadataColumn> listColumns = new ArrayList<IMetadataColumn>();
        VirtualMetadataColumn column = new VirtualMetadataColumn();
        listColumns.add(column);
        metadataTable.setListColumns(listColumns);
        metadatas = new ArrayList<IMetadataTable>();
        metadatas.add(metadataTable);

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

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.shadow.ShadowNode#isStart()
     */
    @Override
    public boolean isStart() {
        return true;

    }

}
