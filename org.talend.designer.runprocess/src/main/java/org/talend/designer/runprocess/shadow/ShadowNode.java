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
package org.talend.designer.runprocess.shadow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.talend.commons.exception.SystemException;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.components.IComponentsFactory;
import org.talend.core.model.components.IODataComponent;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.MetadataColumn;
import org.talend.core.model.metadata.MetadataTable;
import org.talend.core.model.process.AbstractNode;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INodeReturn;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.temp.ECodePart;
import org.talend.designer.codegen.ICodeGeneratorService;
import org.talend.designer.runprocess.RunProcessPlugin;

/**
 * DOC chuger class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public abstract class ShadowNode extends AbstractNode {

    private String componentName;

    private IComponent component;

    private IConnection inCnx;

    private IConnection outCnx;

    private List<IElementParameter> parameters;

    private String pluginFullName;

    private IProcess process;

    private List<IMetadataTable> metadatas;

    private int columnNumber = 256;

    /**
     * DOC chuger ShadowNode constructor comment.
     */
    public ShadowNode(String componentName) {
        super();

        this.componentName = componentName;
        parameters = new ArrayList<IElementParameter>();

        IComponentsFactory compFac = RunProcessPlugin.getDefault().getRepositoryService().getComponentsFactory();
        setComponent(compFac.get(componentName));
    }

    public ShadowNode(String componentName, int nbColumn) {
        super();

        this.componentName = componentName;
        parameters = new ArrayList<IElementParameter>();

        IComponentsFactory compFac = RunProcessPlugin.getDefault().getRepositoryService().getComponentsFactory();
        setComponent(compFac.get(componentName));

        this.columnNumber = nbColumn;
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
    @SuppressWarnings("unchecked")//$NON-NLS-1$
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
            ICodeGeneratorService service = RunProcessPlugin.getDefault().getCodeGeneratorService();
            generatedCode = service.createCodeGenerator().generateComponentCode(this, ECodePart.MAIN);
        } catch (SystemException e) {
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
        List<IMetadataTable> metadatas = new ArrayList<IMetadataTable>();
        MetadataTable metadata = new MetadataTable();
        metadata.setTableName(this.getUniqueName());
        List<IMetadataColumn> columns = new ArrayList<IMetadataColumn>();
        for (int i = 0; i < columnNumber; i++) {
            MetadataColumn col = new MetadataColumn();
            col.setLabel("row" + i);
            col.setTalendType("id_String");
            col.setType("String");
            columns.add(col);
        }
        metadata.setListColumns(columns);
        metadatas.add(metadata);
        return metadatas;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#getMappingList()
     */
    public List<Map<String, String>> getMappingList() {
        return null;
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
        this.metadatas = metadataList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#setMappingList(java.util.List)
     */
    public void setMappingList(List<Map<String, String>> mapping) {
        // TODO Auto-generated method stub
    }

    public Boolean hasConditionalOutputs() {
        return component.hasConditionalOutputs();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#isMultiplyingOutputs()
     */
    public Boolean isMultiplyingOutputs() {
        return component.isMultiplyingOutputs();
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

    /**
     * Getter for process.
     * 
     * @return the process
     */
    public IProcess getProcess() {
        return this.process;
    }

    /**
     * Sets the process.
     * 
     * @param process the process to set
     */
    public void setProcess(IProcess process) {
        this.process = process;
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

    public boolean isReadOnly() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#getComponent()
     */
    public IComponent getComponent() {
        return component;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#setComponent(org.talend.core.model.components.IComponent)
     */
    public void setComponent(IComponent component) {
        this.component = component;

    }

    public int getColumnNumber() {
        return columnNumber;
    }

    public void setColumnNumber(int columnNumber) {
        this.columnNumber = columnNumber;
    }
}
