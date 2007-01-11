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

import java.util.StringTokenizer;

import org.talend.core.model.components.IMultipleComponentParameter;


/**
 * DOC nrousseau  class global comment. Detailled comment
 * <br/>
 *
 * $Id: talend-code-templates.xml 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
 *
 */
public class MultipleComponentParameterValue implements IMultipleComponentParameter {

    String sourceComponent;

    String targetComponent;

    String sourceValue;

    String targetValue;

    public MultipleComponentParameterValue(String target, String value) {
        StringTokenizer token = new StringTokenizer(target, ".");
        targetComponent = token.nextToken();
        targetValue = token.nextToken();

        sourceComponent = null;
        sourceValue = value;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.model.components.IMultipleComponentParameter#getSourceComponent()
     */
    public String getSourceComponent() {
        return this.sourceComponent;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.model.components.IMultipleComponentParameter#getSourceValue()
     */
    public String getSourceValue() {
        return this.sourceValue;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.model.components.IMultipleComponentParameter#getTargetComponent()
     */
    public String getTargetComponent() {
        return this.targetComponent;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.model.components.IMultipleComponentParameter#getTargetValue()
     */
    public String getTargetValue() {
        return this.targetValue;
    }
}
