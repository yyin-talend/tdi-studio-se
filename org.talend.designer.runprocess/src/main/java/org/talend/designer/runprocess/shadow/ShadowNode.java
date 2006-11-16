// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
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
package org.talend.designer.runprocess.shadow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.talend.core.model.components.IComponent;
import org.talend.core.model.components.IODataComponent;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.MetadataTable;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.IExternalNode;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.INodeReturn;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.temp.ECodePart;
import org.talend.designer.codegen.CodeGenerator;
import org.talend.designer.codegen.exception.CodeGeneratorException;

/**
 * DOC chuger class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public abstract class ShadowNode implements INode {

    private String componentName;

    private IConnection inCnx;

    private IConnection outCnx;

    private List<IElementParameter> parameters;

    private String pluginFullName;

    /**
     * DOC chuger ShadowNode constructor comment.
     */
    public ShadowNode(String componentName) {
        super();

        this.componentName = componentName;
        parameters = new ArrayList<IElementParameter>();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#getComponentName()
     */
    public String getComponentName() {
        return componentName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#getElementParameters()
     */
    public List<? extends IElementParameter> getElementParameters() {
        return parameters;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#setElementParameters(java.util.List)
     */
    @SuppressWarnings("unchecked")
    public void setElementParameters(List<? extends IElementParameter> newParameters) {
        this.parameters = (List<IElementParameter>) newParameters;
    }

    protected void addParameter(IElementParameter param) {
        parameters.add(param);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#getGeneratedCode()
     */
    public String getGeneratedCode() {
        String generatedCode;
        try {
            generatedCode = new CodeGenerator().generateComponentCode(this, ECodePart.MAIN);
        } catch (CodeGeneratorException e) {
            generatedCode = null;
        }
        return generatedCode;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#getIncomingConnections()
     */
    public List<? extends IConnection> getIncomingConnections() {
        return (List<? extends IConnection>) Arrays.asList(inCnx != null ? new IConnection[] { inCnx } : new IConnection[0]);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#getMetaDataList()
     */
    public List<IMetadataTable> getMetadataList() {
        IMetadataTable meta = new MetadataTable();
        meta.setTableName(getUniqueName());
        return Arrays.asList(new IMetadataTable[] { meta });
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#getOutgoingConnections()
     */
    public List<? extends IConnection> getOutgoingConnections() {
        return (List<? extends IConnection>) Arrays.asList(outCnx != null ? new IConnection[] { outCnx } : new IConnection[0]);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#getPluginFullName()
     */
    public String getPluginFullName() {
        return this.pluginFullName;
        // return "org.talend.designer.core";
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#getPluginFullName()
     */
    public void setPluginFullName(String pluginFullName) {
        this.pluginFullName = pluginFullName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#getUniqueName()
     */
    public String getUniqueName() {
        return getComponentName();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#isActivate()
     */
    public boolean isActivate() {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#isSubProcessStart()
     */
    public boolean isSubProcessStart() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#setPerformanceData(java.lang.String)
     */
    public void setPerformanceData(String perfData) {
        // Do nothing
    }

    /**
     * Getter for inCnx.
     * 
     * @return the inCnx
     */
    public IConnection getInCnx() {
        return this.inCnx;
    }

    /**
     * Sets the inCnx.
     * 
     * @param inCnx the inCnx to set
     */
    public void setInCnx(IConnection inCnx) {
        this.inCnx = inCnx;
    }

    /**
     * Getter for outCnx.
     * 
     * @return the outCnx
     */
    public IConnection getOutCnx() {
        return this.outCnx;
    }

    /**
     * Sets the outCnx.
     * 
     * @param outCnx the outCnx to set
     */
    public void setOutCnx(IConnection outCnx) {
        this.outCnx = outCnx;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#setMetadataList(java.util.List)
     */
    public void setMetadataList(List<IMetadataTable> metadataList) {
        // TODO Auto-generated method stub

    }

    public Boolean isMultipleMethods() {
        return new Boolean(true);
    }

    public IExternalNode getExternalNode() {
        return null;
    }

    public boolean isExternalNode() {
        return false;
    }

    public boolean isStart() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#getReturns()
     */
    public List<? extends INodeReturn> getReturns() {
        return new ArrayList<INodeReturn>();
    }

    public IProcess getProcess() {
        return null;
    }

    public void setProcess(IProcess process) {
    }

    public IComponent getComponent() {
        return null;
    }

    public void setComponent(IComponent component) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#renameMetadataColumnName(java.lang.String, java.lang.String,
     * java.lang.String)
     */
    public void metadataInputChanged(IODataComponent dataComponent, String connectionToApply) {
        // Nothing to do as it's shadow node
    }

    public void metadataOutputChanged(IODataComponent dataComponent, String connectionToApply) {
        // Nothing to do as it's shadow node
    }
}
