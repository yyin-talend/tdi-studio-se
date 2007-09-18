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
package org.talend.repository.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.data.container.RootContainer;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.model.repository.IRepositoryObject;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public abstract class AbstractRepositoryFactory implements IRepositoryFactory {

    private String name;

    private String id;

    private boolean authenticationNeeded;

    private List<DynamicFieldBean> fields = new ArrayList<DynamicFieldBean>();

    private List<DynamicButtonBean> buttons = new ArrayList<DynamicButtonBean>();

    public List<DynamicButtonBean> getButtons() {
        return buttons;
    }

    /**
     * Getter for authenticationNeeded.
     * 
     * @return the authenticationNeeded
     */
    public boolean isAuthenticationNeeded() {
        return this.authenticationNeeded;
    }

    /**
     * Sets the authenticationNeeded.
     * 
     * @param authenticationNeeded the authenticationNeeded to set
     */
    public void setAuthenticationNeeded(boolean authenticationNeeded) {
        this.authenticationNeeded = authenticationNeeded;
    }

    /**
     * Getter for name.
     * 
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the name.
     * 
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<DynamicFieldBean> getFields() {
        return this.fields;
    }

    public void setFields(List<DynamicFieldBean> fields) {
        this.fields = fields;
    }

    public RepositoryContext getRepositoryContext() {
        Context ctx = CorePlugin.getContext();
        return (RepositoryContext) ctx.getProperty(Context.REPOSITORY_CONTEXT_KEY);
    }

    @Override
    public String toString() {
        return getName();
    }

    /**
     * Generates the next id for serializable. If no serializable returns 0.
     * 
     * @param project the project to scan
     * 
     * @return the next id for the project
     * @throws PersistenceException
     * @throws PersistenceException if processes cannot be retrieved
     */
    public String getNextId() {
        return EcoreUtil.generateUUID();
    }

    private void collect(RootContainer<String, IRepositoryObject> rootContainer, List<ConnectionItem> result)
            throws PersistenceException {
        for (IRepositoryObject repositoryObject : rootContainer.getAbsoluteMembers().objects()) {
            ConnectionItem connectionItem = (ConnectionItem) repositoryObject.getProperty().getItem();
            if (getStatus(connectionItem) != ERepositoryStatus.DELETED) {
                result.add(connectionItem);
            }
        }
    }

    // gather all the metadata connections (file / db / etc ...)
    public List<ConnectionItem> getMetadataConnectionsItem() throws PersistenceException {
        List<ConnectionItem> result = new ArrayList<ConnectionItem>();

        collect(getMetadataFileDelimited(), result);
        collect(getMetadataFilePositional(), result);
        collect(getMetadataFileRegexp(), result);
        collect(getMetadataFileXml(), result);
        collect(getMetadataFileLdif(), result);
        collect(getMetadataConnection(), result);
        collect(getMetadataLDAPSchema(), result);
        collect(getMetadataGenericSchema(), result);

        return result;
    }

    // gather all the contexts
    public List<ContextItem> getContextItem() throws PersistenceException {
        List<ContextItem> result = new ArrayList<ContextItem>();

        for (IRepositoryObject repositoryObject : getContext().getAbsoluteMembers().objects()) {
            ContextItem contextItem = (ContextItem) repositoryObject.getProperty().getItem();
            if (getStatus(contextItem) != ERepositoryStatus.DELETED) {
                result.add(contextItem);
            }
        }

        return result;
    }

}
