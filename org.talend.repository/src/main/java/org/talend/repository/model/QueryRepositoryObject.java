// ============================================================================
//
// Copyright (C) 2006-2010 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.model;

import org.talend.core.model.metadata.builder.connection.AbstractMetadataObject;
import org.talend.core.model.metadata.builder.connection.Query;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.IRepositoryObject;

/**
 * DOC nrousseau class global comment. Detailled comment
 */
public class QueryRepositoryObject extends org.talend.core.model.metadata.Query implements ISubRepositoryObject {

    private final IRepositoryObject repObj;

    private final Query query;

    @Override
    public Object getAdapter(Class adapter) {
        if (adapter == Query.class) {
            return query;
        }
        return null;
    }

    public QueryRepositoryObject(IRepositoryObject repObj, Query table) {
        this.repObj = repObj;
        this.query = table;
    }

    @Override
    public Property getProperty() {
        return repObj.getProperty();
    }

    @Override
    public void setProperty(Property property) {
        repObj.setProperty(property);
    }

    @Override
    public String getVersion() {
        return repObj.getVersion();
    }

    @Override
    public String getLabel() {
        return query.getLabel();
    }

    @Override
    public String getId() {
        return query.getId();
    }

    public Query getQuery() {
        return query;
    }

    public AbstractMetadataObject getAbstractMetadataObject() {
        return getQuery();
    }

    public void removeFromParent() {
        query.getQueries().getQuery().remove(query);
    }
}
