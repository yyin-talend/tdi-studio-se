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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.talend.core.model.general.User;
import org.talend.core.model.general.Version;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextListener;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.properties.Status;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.RepositoryObject;

/**
 * DOC mhirt class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 * @param <K>
 */
public class FileinToXmlProcess<K extends FileInputNode> extends RepositoryObject implements IProcess {

    private IContextManager contextManager;

    private K inNode;

    private FileOutputXmlNode outNode;

    /**
     * Constructs a new FileinToXmlProcess.
     */
    public FileinToXmlProcess(K inNode, FileOutputXmlNode outNode) {
        super();

        this.inNode = inNode;
        this.outNode = outNode;
        ShadowConnection cnx = new ShadowConnection(inNode, outNode);
        inNode.setOutCnx(cnx);
        outNode.setInCnx(cnx);

        contextManager = new EmptyContextManager();
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
     * @see org.talend.core.model.process.IProcess#getNodes()
     */
    public List<? extends INode> getGraphicalNodes() {
        return Arrays.asList(new INode[] { inNode, outNode });
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.IRepositoryProcess#getAuthor()
     */
    public User getAuthor() {
        return new User("Talend");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.IRepositoryProcess#getId()
     */
    public String getId() {
        return "";
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.IRepositoryProcess#getLabel()
     */
    public String getLabel() {
        return "Shadow FileInput to XmlOutput";
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.IRepositoryProcess#getStatus()
     */
    public Status getStatus() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.IRepositoryProcess#getVersion()
     */
    public Version getVersion() {
        return new Version();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.IRepositoryProcess#setAuthor(org.talend.core.model.general.User)
     */
    public void setAuthor(User author) {
        // Read only
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.IRepositoryProcess#setId(int)
     */
    public void setId(int id) {
        // Read only
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.IRepositoryProcess#setLabel(java.lang.String)
     */
    public void setLabel(String label) {
        // Read only
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.IRepositoryProcess#setStatus(org.talend.core.model.process.EProcessStatus)
     */
    public void setStatus(Status status) {
        // Read only
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.IRepositoryProcess#setVersion(org.talend.core.model.version.Version)
     */
    public void setVersion(Version version) {
        // Read only
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.temp.IXmlSerializable#getXmlStream()
     */
    public InputStream getXmlStream() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.temp.IXmlSerializable#setXmlStream(java.io.InputStream)
     */
    public void setXmlStream(InputStream xmlStream) {
        // Do nothing
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
     * @see org.talend.core.model.repository.IRepositoryObject#getComment()
     */
    public String getComment() {
        return "";
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.IRepositoryObject#setComment(java.lang.String)
     */
    public void setComment(String comment) {
        // Read-only
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.IRepositoryObject#getType()
     */
    public ERepositoryObjectType getType() {
        return ERepositoryObjectType.PROCESS;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IProcess#addUniqueConnectionName(java.lang.String)
     */
    public void addUniqueConnectionName(String uniqueConnectionName) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IProcess#checkValidConnectionName(java.lang.String)
     */
    public boolean checkValidConnectionName(String connectionName) {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IProcess#generateUniqueConnectionName()
     */
    public String generateUniqueConnectionName(String baseName) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IProcess#removeUniqueConnectionName(java.lang.String)
     */
    public void removeUniqueConnectionName(String uniqueConnectionName) {
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
     * @see org.talend.core.model.process.IElement#setElementParameters(java.util.List)
     */
    public void setElementParameters(List<? extends IElementParameter> elementsParameters) {
    }

    /**
     * Empty IContext implementation. <br/>
     * 
     * $Id$
     * 
     */
    private static class EmptyContext implements IContext, Cloneable {

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.process.IContext#getContextParameterList()
         */
        public List<IContextParameter> getContextParameterList() {
            return new ArrayList<IContextParameter>();
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.process.IContext#getName()
         */
        public String getName() {
            return "Shadow";
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.process.IContext#isConfirmationNeeded()
         */
        public boolean isConfirmationNeeded() {
            return false;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.process.IContext#setConfirmationNeeded(boolean)
         */
        public void setConfirmationNeeded(boolean confirmationNeeded) {
            // Read-only
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.process.IContext#setContextParameterList(java.util.List)
         */
        public void setContextParameterList(List<IContextParameter> contextParameterList) {
            // Read-only
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.process.IContext#setName(java.lang.String)
         */
        public void setName(String name) {
            // Read-only
        }

        @Override
        public IContext clone() {
            return this;
        }
    }

    /**
     * DOC nrousseau FileinDelimitedToXmlProcess class global comment. Detailled comment <br/>
     * 
     * $Id$
     * 
     */
    private static class EmptyContextManager implements IContextManager, Cloneable {

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.process.IContextManager#addContextListener(org.talend.core.model.process.IContextListener)
         */
        public void addContextListener(IContextListener listener) {
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.process.IContextManager#fireContextsChangedEvent()
         */
        public void fireContextsChangedEvent() {
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.process.IContextManager#getDefaultContext()
         */
        public IContext getDefaultContext() {
            return new EmptyContext();
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.process.IContextManager#getListContext()
         */
        public List<IContext> getListContext() {
            return Arrays.asList(new IContext[] { getDefaultContext() });
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.process.IContextManager#removeContextListener(org.talend.core.model.process.IContextListener)
         */
        public void removeContextListener(IContextListener listener) {
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.process.IContextManager#setDefaultContext(org.talend.core.model.process.IContext)
         */
        public void setDefaultContext(IContext context) {
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.process.IContextManager#setListContext(java.util.List)
         */
        public void setListContext(List<IContext> listContext) {
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.process.IContextManager#checkValidContextName(java.lang.String)
         */
        public boolean checkValidParameterName(String contextName) {
            return false;
        }
    }

    public boolean isReadOnly() {
        // TODO Auto-generated method stub
        return false;
    }

    public void setReadOnly(boolean readOnly) {
        // TODO Auto-generated method stub
        
    }
}
