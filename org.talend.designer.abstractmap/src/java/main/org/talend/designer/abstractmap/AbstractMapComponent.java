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
package org.talend.designer.abstractmap;

import java.util.List;

import org.talend.core.model.process.AbstractExternalNode;
import org.talend.core.model.process.IElementParameter;
import org.talend.designer.abstractmap.ui.prefs.MapPrefsConstants;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 */
public abstract class AbstractMapComponent extends AbstractExternalNode {

    /**
     * DOC amaumont AbstractMapComponent constructor comment.
     */
    public AbstractMapComponent() {
        super();
    }

    
    
    /* (non-Javadoc)
     * @see org.talend.core.model.process.IExternalNode#initialize()
     */
    public void initialize() {
        initElementParameters();
    }



    /**
     * DOC amaumont Comment method "initElementParameters".
     */
    private void initElementParameters() {
        IElementParameter elementParameter = getElementParameter(MapPrefsConstants.LINK_STYLE);
        if (elementParameter != null && ((String) elementParameter.getValue()).trim().equals("")) {
            elementParameter.setValue(MapPlugin.getDefault().getPreferenceStore().getString(
                    MapPrefsConstants.LINK_STYLE));
        }
    }

    /**
     * DOC amaumont Comment method "refreshMapperConnectorData".
     */
    public void refreshMapperConnectorData() {
    }


    
}
