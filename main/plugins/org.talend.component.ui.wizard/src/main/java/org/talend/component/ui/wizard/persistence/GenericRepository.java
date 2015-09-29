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
package org.talend.component.ui.wizard.persistence;

import org.talend.components.api.properties.ComponentProperties;
import org.talend.components.api.properties.Repository;
import org.talend.components.api.schema.Schema;

/**
 * created by ycbai on 2015年9月29日 Detailled comment
 *
 */
public class GenericRepository implements Repository {

    @Override
    public String storeComponentProperties(ComponentProperties properties, String name, String repositoryLocation, Schema schema) {
        return null; // TODO
    }

}
