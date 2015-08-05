// ============================================================================
//
// Copyright (C) 2006-2015 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.xmlmap;

import org.talend.core.model.process.IExternalNode;
import org.talend.core.service.IXmlMapService;

/**
 * DOC talend class global comment. Detailled comment
 */
public class XmlMapService implements IXmlMapService {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.service.IXmlMapService#isXmlMapComponent(org.talend.core.model.process.IExternalNode)
     */
    public boolean isXmlMapComponent(IExternalNode node) {
        if (node instanceof XmlMapComponent) {
            return true;
        }
        return false;
    }

}
