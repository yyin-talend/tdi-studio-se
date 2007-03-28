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
package org.talend.designer.mapper.external.connection;

import java.util.List;

import org.talend.core.model.components.IODataComponent;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class IOConnection {

    private IConnection connection;

    private IMetadataTable table;
    
    
    /**
     * DOC amaumont MapperConnection constructor comment.
     * 
     * @param connection
     * @param ioDataComponent
     */
    public IOConnection(IConnection connection) {
        super();
        this.connection = connection;
        this.table = connection.getMetadataTable();
    }

    /**
     * DOC amaumont MapperConnection constructor comment.
     * 
     * @param connection
     * @param ioDataComponent
     */
    public IOConnection(IODataComponent ioDataComponent) {
        super();
        connection = ioDataComponent.getConnection();
        table = ioDataComponent.getTable();
    }

    public String getName() {
        return connection.getName();
    }

    public EConnectionType getConnectionType() {
        return connection.getLineStyle();
    }

    public IMetadataTable getTable() {
        return table;
    }

    /**
     * @return
     * @see org.talend.core.model.process.IConnection#getCondition()
     */
    public String getCondition() {
        return this.connection.getCondition();
    }

    /**
     * @return
     * @see org.talend.core.model.process.IElement#getElementParameters()
     */
    public List<? extends IElementParameter> getElementParameters() {
        return this.connection.getElementParameters();
    }

    /**
     * @return
     * @see org.talend.core.model.process.IConnection#getSource()
     */
    public INode getSource() {
        return this.connection.getSource();
    }

    /**
     * @return
     * @see org.talend.core.model.process.IConnection#getTarget()
     */
    public INode getTarget() {
        return this.connection.getTarget();
    }

    /**
     * @return
     * @see org.talend.core.model.process.IConnection#getUniqueName()
     */
    public String getUniqueName() {
        return this.connection.getUniqueName();
    }

    /**
     * @return
     * @see org.talend.core.model.process.IConnection#isActivate()
     */
    public boolean isActivate() {
        return this.connection.isActivate();
    }

    /**
     * @return
     * @see org.talend.core.model.process.IElement#isReadOnly()
     */
    public boolean isReadOnly() {
        return this.connection.isReadOnly();
    }

    
    
}
