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

import org.talend.core.model.components.IMultipleComponentItem;
import org.talend.core.model.process.EConnectionType;


/**
 * DOC nrousseau  class global comment. Detailled comment
 * <br/>
 *
 * $Id$
 *
 */
public class MultipleComponentItem implements IMultipleComponentItem {

    String name;

    boolean connectionExist;

    EConnectionType connectionType;

    IMultipleComponentItem linkFrom;

    IMultipleComponentItem linkTo;

    String nameLinkTo;

    /* (non-Javadoc)
     * @see org.talend.designer.core.model.components.IMultipleComponentItem#isConnectionExist()
     */
    public boolean isConnectionExist() {
        return this.connectionExist;
    }

    /* (non-Javadoc)
     * @see org.talend.designer.core.model.components.IMultipleComponentItem#setConnectionExist(boolean)
     */
    public void setConnectionExist(boolean connectionExist) {
        this.connectionExist = connectionExist;
    }

    /* (non-Javadoc)
     * @see org.talend.designer.core.model.components.IMultipleComponentItem#getConnectionType()
     */
    public EConnectionType getConnectionType() {
        return this.connectionType;
    }

    public void setConnectionType(EConnectionType connectionType) {
        this.connectionType = connectionType;
    }

    /* (non-Javadoc)
     * @see org.talend.designer.core.model.components.IMultipleComponentItem#getLinkFrom()
     */
    public IMultipleComponentItem getLinkFrom() {
        return this.linkFrom;
    }

    public void setLinkFrom(IMultipleComponentItem linkFrom) {
        this.linkFrom = linkFrom;
    }

    /* (non-Javadoc)
     * @see org.talend.designer.core.model.components.IMultipleComponentItem#getLinkTo()
     */
    public IMultipleComponentItem getLinkTo() {
        return this.linkTo;
    }

    public void setLinkTo(IMultipleComponentItem linkTo) {
        this.linkTo = linkTo;
    }

    /* (non-Javadoc)
     * @see org.talend.designer.core.model.components.IMultipleComponentItem#getName()
     */
    public String getName() {
        return this.name;
    }

    /* (non-Javadoc)
     * @see org.talend.designer.core.model.components.IMultipleComponentItem#setName(java.lang.String)
     */
    public void setName(String name) {
        this.name = name;
    }

    public String getNameLinkTo() {
        return this.nameLinkTo;
    }

    public void setNameLinkTo(String nameLinkTo) {
        this.nameLinkTo = nameLinkTo;
    }
}
