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
package org.talend.designer.core.model.components;

import org.talend.core.model.process.IElementParameterDefaultValue;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class ElementParameterDefaultValue implements IElementParameterDefaultValue {

    String defaultValue;

    String ifCondition;

    String notIfCondition;

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.model.components.IElementParameterDefaultValue#getDefaultValue()
     */
    public String getDefaultValue() {
        return this.defaultValue;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.model.components.IElementParameterDefaultValue#setDefaultValue(java.lang.String)
     */
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.model.components.IElementParameterDefaultValue#getIfCondition()
     */
    public String getIfCondition() {
        return this.ifCondition;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.model.components.IElementParameterDefaultValue#setIfCondition(java.lang.String)
     */
    public void setIfCondition(String ifCondition) {
        this.ifCondition = ifCondition;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.model.components.IElementParameterDefaultValue#getNotIfCondition()
     */
    public String getNotIfCondition() {
        return this.notIfCondition;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.model.components.IElementParameterDefaultValue#setNotIfCondition(java.lang.String)
     */
    public void setNotIfCondition(String notIfCondition) {
        this.notIfCondition = notIfCondition;
    }
}
