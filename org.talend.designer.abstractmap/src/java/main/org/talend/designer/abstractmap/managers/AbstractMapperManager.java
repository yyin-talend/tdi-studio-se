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
package org.talend.designer.abstractmap.managers;

import java.util.List;

import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.IExternalData;
import org.talend.designer.abstractmap.AbstractMapComponent;


/**
 * DOC amaumont  class global comment. Detailled comment
 * <br/>
 *
 */
public abstract class AbstractMapperManager {

    private AbstractMapComponent mapperComponent;
    private boolean mapperChanged;
    
    private IExternalData data;


    /**
     * DOC amaumont AbstractMapperManager constructor comment.
     * @param mapperComponent 
     */
    public AbstractMapperManager(AbstractMapComponent mapperComponent) {
        super();
        this.mapperComponent = mapperComponent;
    }

    public Object getElementParameterValue(String parameterName) {
        List<? extends IElementParameter> elementParameters = mapperComponent.getElementParameters();
        for (IElementParameter parameter : elementParameters) {
            if (parameterName.equals(parameter.getName())) {
                return parameter.getValue();
            }
        }
        return null;
    }


   
    public AbstractMapComponent getAbstractMapComponent() {
        return this.mapperComponent;
    }

    public abstract ILinkManager getLinkManager();

    /**
     * Getter for mapperChanged.
     * @return the mapperChanged
     */
    public abstract boolean isDataChanged();
    
    /**
     * DOC amaumont Comment method "setOriginalExternalData".
     * @param data
     */
    public void setOriginalExternalData(IExternalData data) {
        this.data = data;
    }

    public IExternalData getOriginalExternalData() {
        return this.data;
    }
    
    
    
}
