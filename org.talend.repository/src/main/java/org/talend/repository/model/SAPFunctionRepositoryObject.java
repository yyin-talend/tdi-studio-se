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
import org.talend.core.model.metadata.builder.connection.SAPFunctionUnit;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.core.model.repository.RepositoryObject;

/**
 * DOC nrousseau class global comment. Detailled comment
 */
public class SAPFunctionRepositoryObject extends RepositoryObject implements ISubRepositoryObject {

    private final SAPFunctionUnit functionUnit;

    private final IRepositoryObject repObj;

    public SAPFunctionRepositoryObject(IRepositoryObject repObj, RepositoryNode functionNode, final SAPFunctionUnit functionUnit) {
        this.repObj = repObj;
        this.functionUnit = functionUnit;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.RepositoryObject#getAdapter(java.lang .Class)
     */
    @Override
    public Object getAdapter(Class adapter) {
        if (adapter == org.talend.core.model.metadata.builder.connection.MetadataTable.class) {
            return functionUnit.getMetadataTable();
        } else if (adapter == org.talend.core.model.metadata.builder.connection.InputSAPFunctionParameterTable.class) {
            return functionUnit.getInputParameterTable();
        } else if (adapter == org.talend.core.model.metadata.builder.connection.OutputSAPFunctionParameterTable.class) {
            return functionUnit.getOutputParameterTable();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.IRepositoryObject#getType()
     */
    @Override
    public ERepositoryObjectType getType() {
        return ERepositoryObjectType.METADATA_SAP_FUNCTION;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.RepositoryObject#setLabel(java.lang.String)
     */
    @Override
    public void setLabel(String value) {
        if (functionUnit.getLabel() == null) {
            functionUnit.setLabel(value);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.RepositoryObject#getLabel()
     */
    @Override
    public String getLabel() {
        return functionUnit.getLabel();
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
    public String getId() {
        return functionUnit.getId();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.ISubRepositoryObject#getAbstractMetadataObject ()
     */
    public AbstractMetadataObject getAbstractMetadataObject() {
        return this.functionUnit;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.ISubRepositoryObject#removeFromParent()
     */
    public void removeFromParent() {
        functionUnit.getConnection().getFuntions().remove(functionUnit);
    }

}
