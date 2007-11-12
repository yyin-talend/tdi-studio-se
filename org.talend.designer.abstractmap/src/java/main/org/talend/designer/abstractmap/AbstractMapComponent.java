// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the  agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
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
