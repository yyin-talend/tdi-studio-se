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
package org.talend.designer.core.model.components;

import org.talend.core.model.components.IMultipleComponentConnection;
import org.talend.core.model.components.IMultipleComponentItem;
import org.talend.core.model.process.EConnectionType;


/**
 * DOC nrousseau  class global comment. Detailled comment
 * <br/>
 *
 * $Id$
 *
 */
public class MultipleComponentConnection implements IMultipleComponentConnection {
    String nameTarget;
    IMultipleComponentItem source;
    IMultipleComponentItem target;
    EConnectionType connectionType;
    
    public MultipleComponentConnection (String cType, String targetName) {
        connectionType = EConnectionType.getTypeFromName(cType);
        nameTarget = targetName;
    }
    
    
    /* (non-Javadoc)
     * @see org.talend.designer.core.model.components.IMultipleComponentConnection#getConnectionType()
     */
    public EConnectionType getConnectionType() {
        return this.connectionType;
    }
    
    /* (non-Javadoc)
     * @see org.talend.designer.core.model.components.IMultipleComponentConnection#setConnectionType(org.talend.core.model.process.EConnectionType)
     */
    public void setConnectionType(EConnectionType connectionType) {
        this.connectionType = connectionType;
    }
    
    /* (non-Javadoc)
     * @see org.talend.designer.core.model.components.IMultipleComponentConnection#getNameTarget()
     */
    public String getNameTarget() {
        return this.nameTarget;
    }
    
    /* (non-Javadoc)
     * @see org.talend.designer.core.model.components.IMultipleComponentConnection#setNameTarget(java.lang.String)
     */
    public void setNameTarget(String nameTarget) {
        this.nameTarget = nameTarget;
    }
    
    /* (non-Javadoc)
     * @see org.talend.designer.core.model.components.IMultipleComponentConnection#getSource()
     */
    public IMultipleComponentItem getSource() {
        return this.source;
    }
    
    /* (non-Javadoc)
     * @see org.talend.designer.core.model.components.IMultipleComponentConnection#setSource(org.talend.core.model.components.IMultipleComponentItem)
     */
    public void setSource(IMultipleComponentItem source) {
        this.source = source;
    }
    
    /* (non-Javadoc)
     * @see org.talend.designer.core.model.components.IMultipleComponentConnection#getTarget()
     */
    public IMultipleComponentItem getTarget() {
        return this.target;
    }
    
    /* (non-Javadoc)
     * @see org.talend.designer.core.model.components.IMultipleComponentConnection#setTarget(org.talend.core.model.components.IMultipleComponentItem)
     */
    public void setTarget(IMultipleComponentItem target) {
        this.target = target;
    }
}
