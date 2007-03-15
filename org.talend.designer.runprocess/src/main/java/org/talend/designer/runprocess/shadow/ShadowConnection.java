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

import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;

/**
 * DOC chuger class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class ShadowConnection implements IConnection {

    private INode source;

    private INode target;

    /**
     * Constructs a new ShadowConnection.
     */
    public ShadowConnection(INode source, INode target) {
        super();

        this.source = source;
        this.target = target;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IConnection#getLineStyle()
     */
    public EConnectionType getLineStyle() {
        return EConnectionType.FLOW_MAIN;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IConnection#getMetadataTable()
     */
    public IMetadataTable getMetadataTable() {
        if (source != null) {
            List<IMetadataTable> metadataList = source.getMetadataList();
            for (int i = 0; i < metadataList.size(); i++) {
                if (metadataList.get(i).getTableName().equals(source.getUniqueName())) {
                    return metadataList.get(i);
                }
            }
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IConnection#getName()
     */
    public String getName() {
        return "Row"; //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IConnection#getSource()
     */
    public INode getSource() {
        return source;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IConnection#getTarget()
     */
    public INode getTarget() {
        return target;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IConnection#isActivate()
     */
    public boolean isActivate() {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IConnection#getCondition()
     */
    public String getCondition() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElement#getElementParameters()
     */
    public List<? extends IElementParameter> getElementParameters() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElement#setElementParameters(java.util.List)
     */
    public void setElementParameters(List<? extends IElementParameter> elementsParameters) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IConnection#setTraceData(java.lang.String)
     */
    public void setTraceData(String traceData) {
    }

    public boolean isReadOnly() {
        // TODO Auto-generated method stub
        return false;
    }

    public void setReadOnly(boolean readOnly) {
        // TODO Auto-generated method stub

    }

    public String getUniqueName() {
        return getName();
    }
}
