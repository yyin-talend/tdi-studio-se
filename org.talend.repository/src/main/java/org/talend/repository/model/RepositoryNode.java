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
package org.talend.repository.model;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.repository.IRepositoryObject;

/**
 * Node used to fill the repository view TreeViewer. Each node has a type defines in ENodeType enum. Object isn't stored
 * in the node but retrieve using the provider at each getObject call.<br/>
 * 
 * $Id$
 * 
 */
public class RepositoryNode {

    /**
     * Represents differents type of node.<br/>
     * 
     * $Id$
     * 
     */
    public enum ENodeType {
        SYSTEM_FOLDER,
        // Represents a folder created by the system (Process, Documentation). Thoses folders cannot be removed, moved
        // or rename by users.
        STABLE_SYSTEM_FOLDER,
        // Same as SYSTEM_FOLDER, but users cannot create sub-folders.
        SIMPLE_FOLDER,
        // Represents a folder created by a user. Those folders can be rename, moved or removed.
        REPOSITORY_ELEMENT;
        // Represents an object such as a process or a table.
    }

    /**
     * 
     * Represents available properties on a node.<br/>
     * 
     * $Id$
     * 
     */
    public enum EProperties {
        LABEL,
        CONTENT_TYPE;
    }

    private String id;

    private IRepositoryObject object;

    private RepositoryNode parent;

    private List<RepositoryNode> children = new ArrayList<RepositoryNode>();

    private ENodeType type;

    private Map<EProperties, Object> properties = new Hashtable<EProperties, Object>();

    /**
     * Constructor with an object.
     * 
     * TODO SML Will be removed
     * 
     * @param id
     * @param object
     * @param parent
     * @param type
     */
    public RepositoryNode(IRepositoryObject object, RepositoryNode parent, ENodeType type) {
        super();
        this.id = (object == null ? "-1" : object.getId());
        this.object = object;
        this.parent = parent;
        this.type = type;
    }

    public String toString() {
        switch (getType()) {
        case REPOSITORY_ELEMENT:
            return getObject().toString();
        case SIMPLE_FOLDER:
        case SYSTEM_FOLDER:
            return RepositoryNodeUtilities.getPath(this).toString();
        default:
            return getType() + "-" + getProperties(EProperties.LABEL);
        }
    }

    /**
     * Getter for children.
     * 
     * @return the children
     */
    public List<RepositoryNode> getChildren() {
        return this.children;
    }

    public boolean hasChildren() {
        return !getChildren().isEmpty();
    }

    /**
     * Getter for id.
     * 
     * @return the id
     */
    public String getId() {
        return this.id;
    }

    /**
     * Sets the id.
     * 
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Getter for object.
     * 
     * @return the object
     */
    public IRepositoryObject getObject() {
        if (this.object == null) {
            ProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
            try {
                return factory.getLastVersion(this.id);
            } catch (PersistenceException e) {
                e.printStackTrace();
            }
        }
        return this.object;
    }

    /**
     * Getter for parent.
     * 
     * @return the parent
     */
    public RepositoryNode getParent() {
        return this.parent;
    }

    /**
     * Getter for type.
     * 
     * @return the type
     */
    public ENodeType getType() {
        return this.type;
    }

    /**
     * Sets the type.
     * 
     * @param type the type to set
     */
    public void setType(ENodeType type) {
        this.type = type;
    }

    public void setProperties(EProperties key, Object value) {
        properties.put(key, value);
    }

    public Object getProperties(EProperties key) {
        return properties.get(key);
    }

    private static final int PRIME = 31;

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int result = 1;
        result = PRIME * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = PRIME * result + ((this.properties == null) ? 0 : this.properties.hashCode());
        result = PRIME * result + ((this.type == null) ? 0 : this.type.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RepositoryNode other = (RepositoryNode) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.properties == null) {
            if (other.properties != null) {
                return false;
            }
        } else if (!this.properties.equals(other.properties)) {
            return false;
        }
        if (this.type == null) {
            if (other.type != null) {
                return false;
            }
        } else if (!this.type.equals(other.type)) {
            return false;
        }
        if (this.parent == null) {
            if (other.parent != null) {
                return false;
            }
        } else if (!this.parent.equals(other.parent)) {
            return false;
        }
        return true;
    }

}
