// ============================================================================
//
// Copyright (C) 2006-2011 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.core.repository;

import org.talend.core.model.metadata.builder.connection.WSDLSchemaConnection;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.WSDLSchemaConnectionItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.repository.DefaultRepositoryComponentDndFilter;

/**
 * ggu class global comment. Detailled comment
 */
public class WebServiceRepositoryComponentDndFilter extends DefaultRepositoryComponentDndFilter {

    public WebServiceRepositoryComponentDndFilter() {
    }

    @Override
    public String getRepositoryType(Item item, ERepositoryObjectType type) {
        // only process for webservice, others will be processed by default
        if (item instanceof WSDLSchemaConnectionItem) {
            WSDLSchemaConnection connection = (WSDLSchemaConnection) ((WSDLSchemaConnectionItem) item).getConnection();
            if (!connection.isIsInputModel()) {
                return "WEBSERVICE"; //$NON-NLS-1$
            }
        }
        return null;
    }

}
