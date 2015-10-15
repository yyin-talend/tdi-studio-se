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

import org.talend.component.ui.model.genericMetadata.GenericConnection;
import org.talend.components.api.properties.ComponentProperties;
import org.talend.components.api.properties.Repository;
import org.talend.components.api.schema.Schema;

/**
 * created by ycbai on 2015年9月29日 Detailled comment
 *
 */
public class GenericRepository implements Repository {

    private GenericConnection connection;

    public GenericRepository(GenericConnection connection) {
        this.connection = connection;
    }

    @Override
    public String storeComponentProperties(ComponentProperties properties, String name, String repositoryLocation, Schema schema) {
        connection.setCompProperties(properties.toSerialized());
        return properties.getName();
    }

	@Override
	public ComponentProperties getPropertiesForComponent(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
