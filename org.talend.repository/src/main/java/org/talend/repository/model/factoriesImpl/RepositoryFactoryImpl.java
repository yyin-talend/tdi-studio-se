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
package org.talend.repository.model.factoriesImpl;

import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IPath;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.data.container.RootContainer;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.general.Project;
import org.talend.core.model.general.User;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.Status;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.ERepositoryType;
import org.talend.core.model.repository.Folder;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.core.model.temp.ECodeLanguage;
import org.talend.repository.model.IRepositoryFactory;
import org.talend.repository.model.RepositoryConstants;

/**
 * Repository factory use by client. Based on implementation provide by extension point system. This class contains all
 * commons treatments done by repository whatever implementation.<br/>
 * 
 * $Id$
 * 
 */
public class RepositoryFactoryImpl implements IRepositoryFactory {

    private static Logger log = Logger.getLogger(RepositoryFactoryImpl.class);

    private IRepositoryFactory repositoryFactoryFromProvider;

    private RepositoryContext repositoryContext;

    public RepositoryFactoryImpl(IRepositoryFactory repositoryFactoryFromProvider) {
        this.repositoryFactoryFromProvider = repositoryFactoryFromProvider;
    }

    public void setRepositoryContext(RepositoryContext repositoryContext) {
        this.repositoryFactoryFromProvider.setRepositoryContext(repositoryContext);
        this.repositoryContext = repositoryContext;
    }

    private void checkFileName(String fileName, String pattern) {
        if (!Pattern.matches(pattern, fileName)) {
            throw new IllegalArgumentException("Label " + fileName + " does not match pattern " + pattern);
        }
    }

    private void checkFileNameAndPath(Item item, String pattern, IPath path, boolean folder) throws PersistenceException {
        String fileName = item.getProperty().getLabel();
        checkFileName(fileName, pattern);
        if (!this.repositoryFactoryFromProvider.isNameAvailable(item, null)) {
            throw new IllegalArgumentException("Label " + fileName + " is already in use");
        }
    }

    private void checkFileNameAndPath(String label, String pattern, ERepositoryObjectType type, IPath path, boolean folder)
            throws PersistenceException {
        String fileName = label;
        checkFileName(fileName, pattern);
        if (!this.repositoryFactoryFromProvider.isPathValid(type, path, label)) {
            throw new IllegalArgumentException("Label " + fileName + " is already in use");
        }
    }

    /**
     * @param project
     * @return
     * @throws PersistenceException
     * @see org.talend.repository.model.IMetadataFactory#getMetadataConnections(org.talend.core.model.general.Project)
     */
    public List<ConnectionItem> getMetadataConnectionsItem() throws PersistenceException {
        return this.repositoryFactoryFromProvider.getMetadataConnectionsItem();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryFactory#isValid(org.talend.core.model.general.Project,
     * org.talend.core.model.repository.ERepositoryObjectType, org.eclipse.core.runtime.IPath, java.lang.String)
     */
    public boolean isNameAvailable(Item item, String name) throws PersistenceException {
        return this.repositoryFactoryFromProvider.isNameAvailable(item, name);
    }

    public boolean isPathValid(ERepositoryObjectType type, IPath path, String label) throws PersistenceException {
        return this.repositoryFactoryFromProvider.isPathValid(type, path, label);
    }

    /**
     * @param label
     * @param description
     * @param language
     * @param author
     * @return
     * @throws PersistenceException
     * @see org.talend.core.model.repository.factories.IRepositoryFactory#createProject(java.lang.String,
     * java.lang.String, java.lang.String, org.talend.core.model.general.User)
     */
    public Project createProject(String label, String description, ECodeLanguage language, User author)
            throws PersistenceException {
        checkFileName(label, RepositoryConstants.PROJECT_PATTERN);
        return this.repositoryFactoryFromProvider.createProject(label, description, language, author);
    }

    /**
     * @param project
     * @param type
     * @param path
     * @param label
     * @throws PersistenceException
     * @see org.talend.core.model.repository.factories.IRepositoryFactory#createFolder(org.talend.core.model.general.Project,
     * org.talend.core.model.repository.ERepositoryObjectType, org.eclipse.core.runtime.IPath, java.lang.String)
     */
    public Folder createFolder(ERepositoryObjectType type, IPath path, String label) throws PersistenceException {
        checkFileNameAndPath(label, RepositoryConstants.FILE_PATTERN, type, path, true);
        return this.repositoryFactoryFromProvider.createFolder(type, path, label);
    }

    /**
     * @param project
     * @param type
     * @param path
     * @throws PersistenceException
     * @see org.talend.core.model.repository.factories.IRepositoryFactory#deleteFolder(org.talend.core.model.general.Project,
     * org.talend.core.model.repository.ERepositoryObjectType, org.eclipse.core.runtime.IPath)
     */
    public void deleteFolder(ERepositoryObjectType type, IPath path) throws PersistenceException {
        this.repositoryFactoryFromProvider.deleteFolder(type, path);
    }

    /**
     * @param project
     * @param type
     * @param sourcePath
     * @param targetPath
     * @throws PersistenceException
     * @see org.talend.core.model.repository.factories.IRepositoryFactory#moveFolder(org.talend.core.model.general.Project,
     * org.talend.core.model.repository.ERepositoryObjectType, org.eclipse.core.runtime.IPath,
     * org.eclipse.core.runtime.IPath)
     */
    public void moveFolder(ERepositoryObjectType type, IPath sourcePath, IPath targetPath) throws PersistenceException {
        this.repositoryFactoryFromProvider.moveFolder(type, sourcePath, targetPath);
    }

    /**
     * @param project
     * @return
     * @throws PersistenceException
     * @see org.talend.core.model.repository.factories.IBusinessProcessFactory#getBusinessProcess(org.talend.core.model.general.Project)
     */
    public RootContainer<String, IRepositoryObject> getBusinessProcess() throws PersistenceException {
        return this.repositoryFactoryFromProvider.getBusinessProcess();
    }

    /**
     * @param project
     * @return
     * @throws PersistenceException
     * @see org.talend.core.model.repository.factories.IDocumentationFactory#getDocumentation(org.talend.core.model.general.Project)
     */
    public RootContainer<String, IRepositoryObject> getDocumentation() throws PersistenceException {
        return this.repositoryFactoryFromProvider.getDocumentation();
    }

    /**
     * @param project
     * @return
     * @throws PersistenceException
     * @see org.talend.core.model.repository.factories.IMetadataFactory#getMetadataConnection(org.talend.core.model.general.Project)
     */
    public RootContainer<String, IRepositoryObject> getMetadataConnection() throws PersistenceException {
        RootContainer<String, IRepositoryObject> metadataConnection = this.repositoryFactoryFromProvider.getMetadataConnection();

        return metadataConnection;
    }

    /**
     * @param project
     * @return
     * @throws PersistenceException
     * @see org.talend.core.model.repository.factories.IMetadataFactory#getMetadataFileDelimited(org.talend.core.model.general.Project)
     */
    public RootContainer<String, IRepositoryObject> getMetadataFileDelimited() throws PersistenceException {
        return this.repositoryFactoryFromProvider.getMetadataFileDelimited();
    }

    /**
     * @param project
     * @return
     * @throws PersistenceException
     * @see org.talend.core.model.repository.factories.IRepositoryFactory#getNextId(org.talend.core.model.general.Project)
     */
    public String getNextId() {
        String nextId = this.repositoryFactoryFromProvider.getNextId();
        log.trace("New ID generated on project [" + repositoryContext.getProject() + "] = " + nextId);
        return nextId;
    }

    /**
     * @param project
     * @return
     * @throws PersistenceException
     * @see org.talend.core.model.repository.factories.IProcessFactory#getProcess(org.talend.core.model.general.Project)
     */
    public RootContainer<String, IRepositoryObject> getProcess() throws PersistenceException {
        return this.repositoryFactoryFromProvider.getProcess();
    }

    /**
     * @param project
     * @return
     * @throws PersistenceException
     * @see org.talend.core.model.repository.factories.IRoutineFactory#getRoutine(org.talend.core.model.general.Project)
     */
    public RootContainer<String, IRepositoryObject> getRoutine() throws PersistenceException {
        return this.repositoryFactoryFromProvider.getRoutine();
    }

    /**
     * @param server
     * @param username
     * @param password
     * @return
     * @throws PersistenceException
     * @see org.talend.core.model.repository.factories.IRepositoryFactory#readProject(java.lang.String,
     * java.lang.String, java.lang.String)
     */
    public Project[] readProject() throws PersistenceException {
        return this.repositoryFactoryFromProvider.readProject();
    }

    /**
     * @param project
     * @param type
     * @param path
     * @param label
     * @throws PersistenceException
     * @see org.talend.core.model.repository.factories.IRepositoryFactory#renameFolder(org.talend.core.model.general.Project,
     * org.talend.core.model.repository.ERepositoryObjectType, org.eclipse.core.runtime.IPath, java.lang.String)
     */
    public void renameFolder(ERepositoryObjectType type, IPath path, String label) throws PersistenceException {
        this.repositoryFactoryFromProvider.renameFolder(type, path, label);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryFactory#deleteObject(org.talend.core.model.general.Project,
     * org.talend.core.model.repository.IRepositoryObject)
     */
    public void deleteObjectLogical(IRepositoryObject objToDelete) throws PersistenceException {
        this.repositoryFactoryFromProvider.deleteObjectLogical(objToDelete);
        log.debug("Logical deletion [" + objToDelete + "] by " + repositoryContext.getUser() + ".");
    }

    public void deleteObjectPhysical(IRepositoryObject objToDelete) throws PersistenceException {
        this.repositoryFactoryFromProvider.deleteObjectPhysical(objToDelete);
        log.info("Physical deletion [" + objToDelete + "] by " + repositoryContext.getUser() + ".");
    }

    public void restoreObject(IRepositoryObject objToRestore, IPath path) throws PersistenceException {
        this.repositoryFactoryFromProvider.restoreObject(objToRestore, path);
        log.debug("Restoration [" + objToRestore + "] by " + repositoryContext.getUser() + " to \"/" + path + "\".");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryFactory#isDeleted(org.talend.core.model.general.Project,
     * org.talend.core.model.repository.IRepositoryObject)
     */
    public boolean isDeleted(IRepositoryObject obj) throws PersistenceException {
        return this.repositoryFactoryFromProvider.isDeleted(obj);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryFactory#getOldPath(org.talend.core.model.general.Project,
     * org.talend.core.model.repository.IRepositoryObject)
     */
    public String getOldPath(IRepositoryObject obj) throws PersistenceException {
        return this.repositoryFactoryFromProvider.getOldPath(obj);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryFactory#moveObject(org.talend.core.model.general.Project,
     * org.talend.core.model.repository.IRepositoryObject)
     */
    public void moveObject(IRepositoryObject objToMove, IPath path) throws PersistenceException {
        checkFileNameAndPath(objToMove.getProperty().getItem(), RepositoryConstants.FILE_PATTERN, path, false);
        this.repositoryFactoryFromProvider.moveObject(objToMove, path);
        log.debug("Move [" + objToMove + "] to \"" + path + "\".");
    }

    /**
     * @param project
     * @return
     * @throws PersistenceException
     * @see org.talend.core.model.repository.factories.IMetadataFactory#getMetadataFilePositional(org.talend.core.model.general.Project)
     */
    public RootContainer<String, IRepositoryObject> getMetadataFilePositional() throws PersistenceException {
        return this.repositoryFactoryFromProvider.getMetadataFilePositional();
    }

    /**
     * @param project
     * @return
     * @throws PersistenceException
     * @see org.talend.core.model.repository.factories.IMetadataFactory#getMetadataFileRegexp(org.talend.core.model.general.Project)
     */
    public RootContainer<String, IRepositoryObject> getMetadataFileRegexp() throws PersistenceException {
        return this.repositoryFactoryFromProvider.getMetadataFileRegexp();
    }

    /**
     * @param project
     * @return
     * @throws PersistenceException
     * @see org.talend.core.model.repository.factories.IMetadataFactory#getMetadataFileXml(org.talend.core.model.general.Project)
     */
    public RootContainer<String, IRepositoryObject> getMetadataFileXml() throws PersistenceException {
        return this.repositoryFactoryFromProvider.getMetadataFileXml();
    }

    
    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryFactory#getLockDate(org.talend.core.model.general.Project,
     * org.talend.core.model.repository.IRepositoryObject)
     */
    public Date getLockDate(IRepositoryObject obj) throws PersistenceException {
        return this.repositoryFactoryFromProvider.getLockDate(obj);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryFactory#getLocker(org.talend.core.model.general.Project,
     * org.talend.core.model.repository.IRepositoryObject)
     */
    public User getLocker(IRepositoryObject obj) throws PersistenceException {
        return this.repositoryFactoryFromProvider.getLocker(obj);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryFactory#isLocked(org.talend.core.model.general.Project,
     * org.talend.core.model.repository.IRepositoryObject)
     */
    public boolean isLocked(IRepositoryObject obj) throws PersistenceException {
        return this.repositoryFactoryFromProvider.isLocked(obj);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryFactory#lock(org.talend.core.model.general.Project,
     * org.talend.core.model.repository.IRepositoryObject)
     */
    public void lock(IRepositoryObject obj) throws PersistenceException {
        if (!isLocked(obj)) {
            this.repositoryFactoryFromProvider.lock(obj);
            log.debug("Lock [" + obj + "] by \"" + repositoryContext.getUser() + "\".");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryFactory#unlock(org.talend.core.model.general.Project,
     * org.talend.core.model.repository.IRepositoryObject)
     */
    public void unlock(IRepositoryObject obj) throws PersistenceException {
        if (isLocked(obj)) {
            this.repositoryFactoryFromProvider.unlock(obj);
            log.debug("Unlock [" + obj + "] by \"" + repositoryContext.getUser() + "\".");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryFactory#getAllVersion(org.talend.core.model.general.Project, int)
     */
    public List<IRepositoryObject> getAllVersion(String id) throws PersistenceException {
        return this.repositoryFactoryFromProvider.getAllVersion(id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryFactory#getLastVersion(org.talend.core.model.general.Project, int)
     */
    public IRepositoryObject getLastVersion(String id) throws PersistenceException {
        return this.repositoryFactoryFromProvider.getLastVersion(id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryFactory#getAll(org.talend.core.model.general.Project,
     * org.talend.core.model.repository.ERepositoryObjectType)
     */
    public List<IRepositoryObject> getAll(ERepositoryObjectType type) throws PersistenceException {
        return this.repositoryFactoryFromProvider.getAll(type);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryFactory#getType()
     */
    public ERepositoryType getType() {
        return this.repositoryFactoryFromProvider.getType();
    }

    /**
     * @return
     * @see org.talend.repository.model.IRepositoryFactory#getDocumentationStatus()
     */
    public List<Status> getDocumentationStatus() throws PersistenceException {
        return this.repositoryFactoryFromProvider.getDocumentationStatus();
    }

    /**
     * @return
     * @see org.talend.repository.model.IRepositoryFactory#getTechnicalStatus()
     */
    public List<Status> getTechnicalStatus() throws PersistenceException {
        return this.repositoryFactoryFromProvider.getTechnicalStatus();
    }

    /**
     * @param list
     * @see org.talend.repository.model.IRepositoryFactory#setDocumentationStatus(java.util.List)
     */
    public void setDocumentationStatus(List<Status> list) throws PersistenceException {
        this.repositoryFactoryFromProvider.setDocumentationStatus(list);
    }

    /**
     * @param list
     * @see org.talend.repository.model.IRepositoryFactory#setTechnicalStatus(java.util.List)
     */
    public void setTechnicalStatus(List<Status> list) throws PersistenceException {
        this.repositoryFactoryFromProvider.setTechnicalStatus(list);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryFactory#isServerValid()
     */
    public String isServerValid() {
        return this.repositoryFactoryFromProvider.isServerValid();
    }

    public void create(Item item, IPath path) throws PersistenceException {
        checkFileNameAndPath(item, RepositoryConstants.FILE_PATTERN, path, false);
        this.repositoryFactoryFromProvider.create(item, path);
    }

    public void save(Item item) throws PersistenceException {
        this.repositoryFactoryFromProvider.save(item);
    }

    public void save(Property property) throws PersistenceException {
        this.repositoryFactoryFromProvider.save(property);
    }

    public Item copy(Item item, IPath path) throws PersistenceException {
        return this.repositoryFactoryFromProvider.copy(item, path);
    }

    public boolean isDeleted(Item item) throws PersistenceException {
        return this.repositoryFactoryFromProvider.isDeleted(item);
    }

    public boolean isLocked(Item item) throws PersistenceException {
        return this.repositoryFactoryFromProvider.isLocked(item);
    }

    public void reload(Property property) {
        this.repositoryFactoryFromProvider.reload(property);
    }

    /**
     * @param item
     * @throws PersistenceException
     * @see org.talend.repository.model.IRepositoryFactory#lock(org.talend.core.model.properties.Item)
     */
    public void lock(Item item) throws PersistenceException {
        if (!isLocked(item)) {
            this.repositoryFactoryFromProvider.lock(item);
            log.debug("Lock [" + item + "] by \"" + repositoryContext.getUser() + "\".");
        }
    }

    /**
     * @param obj
     * @throws PersistenceException
     * @see org.talend.repository.model.IRepositoryFactory#unlock(org.talend.core.model.properties.Item)
     */
    public void unlock(Item obj) throws PersistenceException {
        if (isLocked(obj)) {
            this.repositoryFactoryFromProvider.unlock(obj);
            log.debug("Unlock [" + obj + "] by \"" + repositoryContext.getUser() + "\".");
        }
    }

    public boolean findUser(Project project, RepositoryContext repositoryContext) throws PersistenceException {
        return this.repositoryFactoryFromProvider.findUser(project, repositoryContext);
    }

    public void createUser(Project project, RepositoryContext repositoryContext) throws PersistenceException {
        this.repositoryFactoryFromProvider.createUser(project, repositoryContext);
    }

    public void initialize() {
        this.repositoryFactoryFromProvider.initialize();
    }
}
