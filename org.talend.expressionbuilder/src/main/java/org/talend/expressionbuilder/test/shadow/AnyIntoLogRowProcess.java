// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.expressionbuilder.test.shadow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.User;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.designer.rowgenerator.shadow.LogRowNode;
import org.talend.designer.rowgenerator.shadow.RowGenContextManager;
import org.talend.designer.runprocess.IProcessor;
import org.talend.designer.runprocess.shadow.ShadowConnection;
import org.talend.designer.runprocess.shadow.ShadowNode;

/**
 * yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: AnyIntoLogRowProcess.java 下午04:13:24 2007-8-20 +0000 (2007-8-20) yzhang $
 * 
 * @param <K>
 */
public class AnyIntoLogRowProcess<K extends ShadowNode> implements IProcess {

    private String name = "IntoLogRow";

    private K inNode;

    private LogRowNode outNode;

    private IContextManager contextManager;

    /**
     * yzhang PerlLogRowProcess constructor comment.
     */
    public AnyIntoLogRowProcess(K inNode, LogRowNode outNode) {

        this.inNode = inNode;
        this.outNode = outNode;

        ShadowConnection cnx = new ShadowConnection(inNode, outNode);
        inNode.setOutCnx(cnx);
        outNode.setInCnx(cnx);

        inNode.setProcess(this);
        outNode.setProcess(this);

        contextManager = new RowGenContextManager();

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IProcess#addUniqueConnectionName(java.lang.String)
     */
    public void addUniqueConnectionName(String uniqueConnectionName) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IProcess#checkValidConnectionName(java.lang.String)
     */
    public boolean checkValidConnectionName(String connectionName) {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IProcess#checkValidConnectionName(java.lang.String, boolean)
     */
    public boolean checkValidConnectionName(String connectionName, boolean checkExists) {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IProcess#generateUniqueConnectionName(java.lang.String)
     */
    public String generateUniqueConnectionName(String baseName) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IProcess#getAllConnections(java.lang.String)
     */
    public IConnection[] getAllConnections(String filter) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IProcess#getContextManager()
     */
    public IContextManager getContextManager() {
        return contextManager;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IProcess#getGeneratingNodes()
     */
    public List<? extends INode> getGeneratingNodes() {
        return getGraphicalNodes();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IProcess#getGraphicalNodes()
     */
    public List<? extends INode> getGraphicalNodes() {

        return Arrays.asList(new INode[] { inNode, outNode });

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IProcess#getName()
     */
    public String getName() {
        return name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IProcess#getNeededLibraries(boolean)
     */
    public Set<String> getNeededLibraries(boolean withChildrens) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IProcess#getNodesOfType(java.lang.String)
     */
    public List<? extends INode> getNodesOfType(String componentName) {
        List<INode> matchingNodes = new ArrayList<INode>();
        if ((inNode != null) && (inNode.getComponentName() != null) && (inNode.getComponentName().compareTo(componentName) == 0)) {
            matchingNodes.add(inNode);
        }
        if ((outNode != null) && (outNode.getComponentName() != null)
                && (outNode.getComponentName().compareTo(componentName) == 0)) {
            matchingNodes.add(outNode);
        }
        return matchingNodes;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IProcess#getProcessor()
     */
    public IProcessor getProcessor() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IProcess#removeUniqueConnectionName(java.lang.String)
     */
    public void removeUniqueConnectionName(String uniqueConnectionName) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IProcess#setProcessor(org.talend.designer.runprocess.IProcessor)
     */
    public void setProcessor(IProcessor processor) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.IRepositoryObject#getAuthor()
     */
    public User getAuthor() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.IRepositoryObject#getChildren()
     */
    public List<IRepositoryObject> getChildren() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.IRepositoryObject#getCreationDate()
     */
    public Date getCreationDate() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.IRepositoryObject#getDescription()
     */
    public String getDescription() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.IRepositoryObject#getId()
     */
    public String getId() {
        return "";
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.IRepositoryObject#getLabel()
     */
    public String getLabel() {
        return name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.IRepositoryObject#getModificationDate()
     */
    public Date getModificationDate() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.IRepositoryObject#getProperty()
     */
    public Property getProperty() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.IRepositoryObject#getPurpose()
     */
    public String getPurpose() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.IRepositoryObject#getStatusCode()
     */
    public String getStatusCode() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.IRepositoryObject#getType()
     */
    public ERepositoryObjectType getType() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.IRepositoryObject#getVersion()
     */
    public String getVersion() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.IRepositoryObject#setAuthor(org.talend.core.model.properties.User)
     */
    public void setAuthor(User author) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.IRepositoryObject#setCreationDate(java.util.Date)
     */
    public void setCreationDate(Date value) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.IRepositoryObject#setDescription(java.lang.String)
     */
    public void setDescription(String value) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.IRepositoryObject#setId(java.lang.String)
     */
    public void setId(String id) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.IRepositoryObject#setLabel(java.lang.String)
     */
    public void setLabel(String label) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.IRepositoryObject#setModificationDate(java.util.Date)
     */
    public void setModificationDate(Date value) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.IRepositoryObject#setProperty(org.talend.core.model.properties.Property)
     */
    public void setProperty(Property property) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.IRepositoryObject#setPurpose(java.lang.String)
     */
    public void setPurpose(String value) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.IRepositoryObject#setStatusCode(java.lang.String)
     */
    public void setStatusCode(String statusCode) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.IRepositoryObject#setVersion(java.lang.String)
     */
    public void setVersion(String version) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElement#getElementParameter(java.lang.String)
     */
    public IElementParameter getElementParameter(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElement#getElementParameters()
     */
    public List<? extends IElementParameter> getElementParameters() {
        return new ArrayList<IElementParameter>();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElement#isReadOnly()
     */
    public boolean isReadOnly() {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElement#setElementParameters(java.util.List)
     */
    public void setElementParameters(List<? extends IElementParameter> elementsParameters) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElement#setReadOnly(boolean)
     */
    public void setReadOnly(boolean readOnly) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IProcess#getMergelinkOrder(org.talend.core.model.process.INode)
     */
    public int getMergelinkOrder(INode node) {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IProcess#isThereLinkWithHash(org.talend.core.model.process.INode)
     */
    public boolean isThereLinkWithHash(INode node) {
        // TODO Auto-generated method stub
        return false;
    }

}
