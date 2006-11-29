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

import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.data.container.RootContainer;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.Status;
import org.talend.core.model.properties.User;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.Folder;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.core.model.temp.ECodeLanguage;

/**
 * Defines all methods that a repository provider plug-in must provides to client.<br/>
 * 
 * $Id$
 * 
 */
public interface IRepositoryFactory {

    public String getName();

    public void setName(String name);

    public boolean isAuthenticationNeeded();

    public void setAuthenticationNeeded(boolean aBnthenticationNeeded);

    public void initialize();

    public String getNextId();

    public Project createProject(String label, String description, ECodeLanguage language, User author)
            throws PersistenceException;

    public boolean findUser(Project project) throws PersistenceException;

    public void createUser(Project project) throws PersistenceException;

    public Project[] readProject() throws PersistenceException;

    public Folder createFolder(ERepositoryObjectType type, IPath path, String label) throws PersistenceException;

    /**
     * Returns if the name is used by another item of the same type. Type, name and id of item are used to test label
     * availability.
     * 
     * Implementations should be updated when folder are treated as Items.
     * 
     * @param item
     * @param name TODO
     * @return <code>true</code> if the name is not used an so is available.
     * @throws PersistenceException
     */
    public boolean isNameAvailable(Item item, String name) throws PersistenceException;

    public boolean isPathValid(ERepositoryObjectType type, IPath path, String label) throws PersistenceException;

    public void deleteFolder(ERepositoryObjectType type, IPath path) throws PersistenceException;

    public void moveFolder(ERepositoryObjectType type, IPath sourcePath, IPath targetPath) throws PersistenceException;

    public void renameFolder(ERepositoryObjectType type, IPath path, String label) throws PersistenceException;

    /**
     * Returns all version of an object given its id.
     * 
     * @param project - the current project
     * @param id - the id to look for
     * @return a list (may be empty) of all version
     * @throws PersistenceException
     */
    public List<IRepositoryObject> getAllVersion(String id) throws PersistenceException;

    /**
     * Returns last version of an object given its id.
     * 
     * @param project - the current project
     * @param id - the id to look for
     * @return the most recent version of object with this id or <code>null</code> if no object with this id has been
     * found
     * @throws PersistenceException
     */
    public IRepositoryObject getLastVersion(String id) throws PersistenceException;

    /**
     * Returns all object of a given type.<br/>
     * 
     * Only the most recent version of each object is returned.
     * 
     * @param project - the current project
     * @param type - the type
     * @return a list of all objects of type <code>type</code> in the repository in the project
     * @throws PersistenceException
     */
    public List<IRepositoryObject> getAll(ERepositoryObjectType type) throws PersistenceException;

    /**
     * Deletes logically the given object. <code>isDeleted</code> on this object will now returned <code>true</code>.
     * 
     * @param project - the current project
     * @param objToDelete - the objet to delete
     * @param deletionAuthor - the user perfom the deletion (only for logging in this version)
     * @throws PersistenceException
     */
    public void deleteObjectLogical(IRepositoryObject objToDelete) throws PersistenceException;

    /**
     * Deletes physically the given object. Object cannot be retrieved.
     * 
     * @param project - the current project
     * @param objToDelete - the objet to delete
     * @param deletionAuthor - the user perfom the deletion (only for logging in this version)
     * @throws PersistenceException
     */
    public void deleteObjectPhysical(IRepositoryObject objToDelete) throws PersistenceException;

    public String getOldPath(IRepositoryObject obj) throws PersistenceException;

    /**
     * Restore a logically deleted object. <code>isDeleted</code> on this object will now returned <code>false</code>.
     * 
     * @param project - the current project
     * @param objToRestore - the object to restore
     * @param path - the path to restore the object. Cannot be null. Path is relative to root type folder.
     * @param restorationAuthor - the user perfom the restoration (only for logging in this version)
     * @throws PersistenceException
     */
    public void restoreObject(IRepositoryObject objToRestore, IPath path) throws PersistenceException;

    public void moveObject(IRepositoryObject objToMove, IPath newPath) throws PersistenceException;

    public void lock(Item item) throws PersistenceException;

    public void unlock(Item obj) throws PersistenceException;

    // TODO SML This method should not be in this interface but only in the remote/synchronizer => later
    public void commit(Item obj);

    public ERepositoryStatus getStatus(Item item);

    List<Status> getTechnicalStatus() throws PersistenceException;

    List<Status> getDocumentationStatus() throws PersistenceException;

    void setTechnicalStatus(List<Status> list) throws PersistenceException;

    void setDocumentationStatus(List<Status> list) throws PersistenceException;

    public String isServerValid();

    public void create(Item item, IPath path) throws PersistenceException;

    public void save(Item item) throws PersistenceException;

    public void save(Property property) throws PersistenceException;

    public Item copy(Item item, IPath path) throws PersistenceException;

    /**
     * DOC mhelleboid Comment method "cancel".
     * 
     * @param property
     */
    public void reload(Property property);

    public RootContainer<String, IRepositoryObject> getBusinessProcess() throws PersistenceException;

    public RootContainer<String, IRepositoryObject> getDocumentation() throws PersistenceException;

    public RootContainer<String, IRepositoryObject> getProcess() throws PersistenceException;

    public RootContainer<String, IRepositoryObject> getRoutine() throws PersistenceException;

    public RootContainer<String, IRepositoryObject> getMetadataConnection() throws PersistenceException;

    public RootContainer<String, IRepositoryObject> getMetadataFileDelimited() throws PersistenceException;

    public RootContainer<String, IRepositoryObject> getMetadataFilePositional() throws PersistenceException;

    public RootContainer<String, IRepositoryObject> getMetadataFileRegexp() throws PersistenceException;

    public RootContainer<String, IRepositoryObject> getMetadataFileXml() throws PersistenceException;

    public RootContainer<String, IRepositoryObject> getMetadataFileLdif() throws PersistenceException;

    /**
     * gather all the metadata connections (file / db / etc ...).
     */
    List<ConnectionItem> getMetadataConnectionsItem() throws PersistenceException;
}
