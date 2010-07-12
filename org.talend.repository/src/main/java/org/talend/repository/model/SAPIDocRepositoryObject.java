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
import org.talend.core.model.metadata.builder.connection.SAPIDocUnit;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.repository.RepositoryObject;

/**
 * DOC zli class global comment. Detailled comment
 */
public class SAPIDocRepositoryObject extends RepositoryObject implements ISubRepositoryObject {

    private final SAPIDocUnit iDocUnit;

    private final IRepositoryViewObject repObj;

    public SAPIDocRepositoryObject(IRepositoryViewObject repObj, RepositoryNode functionNode, final SAPIDocUnit functionUnit) {
        this.repObj = repObj;
        this.iDocUnit = functionUnit;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.RepositoryObject#getAdapter(java.lang .Class)
     */
    @Override
    public Object getAdapter(Class adapter) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.IRepositoryObject#getType()
     */
    @Override
    public ERepositoryObjectType getType() {
        return ERepositoryObjectType.METADATA_SAP_IDOC;
    }

    public void setLabel(String value) {
        if (iDocUnit.getLabel() == null) {
            iDocUnit.setLabel(value);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.RepositoryObject#getLabel()
     */
    @Override
    public String getLabel() {
        return iDocUnit.getLabel();
    }

    @Override
    public Property getProperty() {
        return repObj.getProperty();
    }

    @Override
    public String getVersion() {
        return repObj.getVersion();
    }

    @Override
    public String getId() {
        return iDocUnit.getId();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.ISubRepositoryObject#getAbstractMetadataObject ()
     */
    public AbstractMetadataObject getAbstractMetadataObject() {
        return this.iDocUnit;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.ISubRepositoryObject#removeFromParent()
     */
    public void removeFromParent() {
        iDocUnit.getConnection().getIDocs().remove(iDocUnit);
    }

}
