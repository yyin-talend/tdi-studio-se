// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2007 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License.
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
package org.talend.designer.rowgenerator.shadow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.draw2d.geometry.Point;
import org.talend.commons.exception.SystemException;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.components.IComponentsFactory;
import org.talend.core.model.components.IODataComponent;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.MetadataTable;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.IExternalNode;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.INodeReturn;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.temp.ECodePart;
import org.talend.designer.codegen.ICodeGeneratorService;
import org.talend.designer.rowgenerator.RowGeneratorPlugin;
import org.talend.designer.rowgenerator.managers.UIManager;

/**
 * qzhang class global comment. Detailled comment <br/>
 * 
 * $Id: LogRowNode.java,v 1.4 2007/02/02 07:59:31 pub Exp $
 * 
 */
public class LogRowNode implements INode {

    private String componentName;

    private IComponent component;

    private IConnection inCnx;

    private IConnection outCnx;

    private List<IElementParameter> parameters;

    private String pluginFullName;

    private IProcess process;

    private IMetadataTable metadataTable;
    /**
     * qzhang LogRowNode constructor comment.
     */
    public LogRowNode(String componentName, IMetadataTable list) {
        super();
        this.metadataTable = list;
        this.componentName = componentName;
        parameters = new ArrayList<IElementParameter>();

        IComponentsFactory compFac = RowGeneratorPlugin.getDefault().getRepositoryService().getComponentsFactory();
        setComponent(compFac.get(componentName));
        TextElementParameter param = null;
        if (UIManager.isJavaProject()) {
            param = new TextElementParameter("FIELDSEPARATOR", "\"|\"");
        } else {
            param = new TextElementParameter("FIELDSEPARATOR", "\'|\'"); //$NON-NLS-1$ //$NON-NLS-2$
        }
        this.addParameter(param);
    }

    /*
     * (non-Java)
     * 
     * @see org.talend.core.model.process.INode#getIncomingConnections()
     */
    public List<? extends IConnection> getIncomingConnections() {
        return (List<? extends IConnection>) Arrays.asList(inCnx != null ? new IConnection[] { inCnx } : new IConnection[0]);
    }

    /*
     * (non-Java)
     * 
     * @see org.talend.core.model.process.INode#getMetadataList()
     */
    public List<IMetadataTable> getMetadataList() {
        List<IMetadataTable> metadatas = new ArrayList<IMetadataTable>();
        MetadataTable metadata = new MetadataTable();
        metadata.setTableName(this.getUniqueName());
        if (UIManager.isJavaProject()) {
            List<IMetadataColumn> columns = new ArrayList<IMetadataColumn>();
            columns.addAll(metadataTable.getListColumns());
            metadata.setListColumns(columns);
        }
        metadatas.add(metadata);
        return metadatas;
    }

    /*
     * (non-Java)
     * 
     * @see org.talend.core.model.process.INode#getOutgoingConnections()
     */
    public List<? extends IConnection> getOutgoingConnections() {
        return (List<? extends IConnection>) Arrays.asList(outCnx != null ? new IConnection[] { outCnx } : new IConnection[0]);
    }

    /*
     * (non-Java)
     * 
     * @see org.talend.core.model.process.INode#getPluginFullName()
     */
    public String getPluginFullName() {
        return this.pluginFullName;
    }

    /*
     * (non-Java)
     * 
     * @see org.talend.core.model.process.INode#getPluginFullName()
     */
    public void setPluginFullName(String pluginFullName) {
        this.pluginFullName = pluginFullName;
    }

    /*
     * (non-Java)
     * 
     * @see org.talend.core.model.process.INode#getUniqueName()
     */
    public String getUniqueName() {
        return getComponentName();
    }

    /*
     * (non-Java)
     * 
     * @see org.talend.core.model.process.INode#isActivate()
     */
    public boolean isActivate() {
        return true;
    }

    /*
     * (non-Java)
     * 
     * @see org.talend.core.model.process.INode#isStart()
     */
    public boolean isStart() {
        return false;
    }

    /*
     * (non-Java)
     * 
     * @see org.talend.core.model.process.INode#isSubProcessStart()
     */
    public boolean isSubProcessStart() {
        return true;
    }

    /*
     * (non-Java)
     * 
     * @see org.talend.core.model.process.INode#setPerformanceData(java.lang.String)
     */
    public void setPerformanceData(String perfData) {
        // do nothings

    }

    /*
     * (non-Java)
     * 
     * @see org.talend.core.model.process.IElement#getElementParameters()
     */
    public List<? extends IElementParameter> getElementParameters() {
        return parameters;
    }

    /*
     * (non-Java)
     * 
     * @see org.talend.core.model.process.IElement#setElementParameters(java.util.List)
     */
    @SuppressWarnings("unchecked")//$NON-NLS-1$
    public void setElementParameters(List<? extends IElementParameter> elementsParameters) {
        this.parameters = (List<IElementParameter>) elementsParameters;
    }

    protected void addParameter(IElementParameter param) {
        parameters.add(param);
    }

    /*
     * (non-Java)
     * 
     * @see org.talend.core.model.process.INode#getGeneratedCode()
     */
    public String getGeneratedCode() {
        String generatedCode;
        try {
            ICodeGeneratorService service = RowGeneratorPlugin.getDefault().getCodeGeneratorService();
            generatedCode = service.createCodeGenerator().generateComponentCode(this, ECodePart.MAIN);
        } catch (SystemException e) {
            generatedCode = null;
        }
        return generatedCode;
    }

    public String getComponentName() {
        return this.componentName;
    }

    public Boolean hasConditionalOutputs() {
        return component.hasConditionalOutputs();
    }

    /*
     * (non-Java)
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
     * (non-Java)
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

    public void setReadOnly(boolean readOnly) {
    }

    /*
     * (non-Java)
     * 
     * @see org.talend.core.model.process.INode#getComponent()
     */
    public IComponent getComponent() {
        return component;
    }

    /*
     * (non-Java)
     * 
     * @see org.talend.core.model.process.INode#setComponent(org.talend.core.model.components.IComponent)
     */
    public void setComponent(IComponent component) {
        this.component = component;

    }

    public INode getSubProcessStartNode(boolean withConditions) {
        return null;
    }

    public IConnection getInCnx() {
        return this.inCnx;
    }

    public void setInCnx(IConnection inCnx) {
        this.inCnx = inCnx;
    }

    public IConnection getOutCnx() {
        return this.outCnx;
    }

    public void setOutCnx(IConnection outCnx) {
        this.outCnx = outCnx;
    }
    
    /* (non-Javadoc)
     * @see org.talend.core.model.process.INode#getLocation()
     */
    public Point getLocation()
    {
        return null;
    }

    public IExternalNode getExternalNode() {
        // TODO Auto-generated method stub
        return null;
    }
    
        public boolean isThereLinkWithHash() {
        // TODO Auto-generated method stub
        return false;
    }

}
