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

import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.data.container.RootContainer;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.general.Project;
import org.talend.core.model.metadata.builder.connection.TableHelper;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.Status;
import org.talend.core.model.properties.User;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.Folder;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.core.model.temp.ECodeLanguage;
import org.talend.repository.ui.views.RepositoryContentProvider.MetadataTableRepositoryObject;

/**
 * Repository factory use by client. Based on implementation provide by extension point system. This class contains all
 * commons treatments done by repository whatever implementation.<br/>
 * 
 * $Id$
 * 
 */
public class ProxyRepositoryFactory {

    private static Logger log = Logger.getLogger(ProxyRepositoryFactory.class);

    private IRepositoryFactory repositoryFactoryFromProvider;

    private static ProxyRepositoryFactory singleton = null;

    /**
     * DOC smallet ProxyRepositoryFactory constructor comment.
     */
    public ProxyRepositoryFactory() {
        // TODO Auto-generated constructor stub
    }

    public static ProxyRepositoryFactory getInstance() {
        if (singleton == null) {
            singleton = new ProxyRepositoryFactory();
        }
        return singleton;
    }

    public RepositoryContext getRepositoryContext() {
        Context ctx = CorePlugin.getContext();
        return (RepositoryContext) ctx.getProperty(Context.REPOSITORY_CONTEXT_KEY);
    }

    /**
     * Getter for repositoryFactoryFromProvider.
     * 
     * @return the repositoryFactoryFromProvider
     */
    public IRepositoryFactory getRepositoryFactoryFromProvider() {
        return this.repositoryFactoryFromProvider;
    }

    /**
     * Sets the repositoryFactoryFromProvider.
     * 
     * @param repositoryFactoryFromProvider the repositoryFactoryFromProvider to set
     */
    public void setRepositoryFactoryFromProvider(IRepositoryFactory repositoryFactoryFromProvider) {
        this.repositoryFactoryFromProvider = repositoryFactoryFromProvider;
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
        log.trace("New ID generated on project [" + getRepositoryContext().getProject() + "] = " + nextId);
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
        log.debug("Logical deletion [" + objToDelete + "] by " + getRepositoryContext().getUser() + ".");
    }

    public void deleteObjectPhysical(IRepositoryObject objToDelete) throws PersistenceException {
        this.repositoryFactoryFromProvider.deleteObjectPhysical(objToDelete);
        log.info("Physical deletion [" + objToDelete + "] by " + getRepositoryContext().getUser() + ".");
    }

    public void restoreObject(IRepositoryObject objToRestore, IPath path) throws PersistenceException {
        this.repositoryFactoryFromProvider.restoreObject(objToRestore, path);
        log.debug("Restoration [" + objToRestore + "] by " + getRepositoryContext().getUser() + " to \"/" + path + "\".");
    }

    public String getOldPath(IRepositoryObject obj) throws PersistenceException {
        return this.repositoryFactoryFromProvider.getOldPath(obj);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryFactory#moveObject(org.talend.core.model.general.Project,
     * org.talend.core.model.repository.IRepositoryObject)
     */
    public void moveObject(IRepositoryObject objToMove, IPath path) throws PersistenceException, BusinessException {
        checkDisponibilite(objToMove);
        checkFileNameAndPath(objToMove.getProperty().getItem(), RepositoryConstants.FILE_PATTERN, path, false);
        this.repositoryFactoryFromProvider.moveObject(objToMove, path);
        log.debug("Move [" + objToMove + "] to \"" + path + "\".");
        unlock(getItem(objToMove));
    }

    // TODO SML Renommer et finir la méthode et la plugger dans toutes les méthodes
    private void checkDisponibilite(IRepositoryObject objToMove) throws BusinessException {
        if (!isEditableAndLockIfPossible(objToMove)) {
            throw new BusinessException("Item non modifiable par vous !!");
        }
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

    /**
     * @param project
     * @return
     * @throws PersistenceException
     * @see org.talend.core.model.repository.factories.IMetadataFactory#getMetadataFileLdif(org.talend.core.model.general.Project)
     */
    public RootContainer<String, IRepositoryObject> getMetadataFileLdif() throws PersistenceException {
        return this.repositoryFactoryFromProvider.getMetadataFileLdif();
    }

    public void lock(IRepositoryObject obj) throws PersistenceException {
        lock(getItem(obj));
    }

    /**
     * @param item
     * @throws PersistenceException
     * @see org.talend.repository.model.IRepositoryFactory#lock(org.talend.core.model.properties.Item)
     */
    public void lock(Item item) throws PersistenceException {
        if (getStatus(item).isPotentiallyEditable()) {
            this.repositoryFactoryFromProvider.lock(item);
            log.debug("Lock [" + item + "] by \"" + getRepositoryContext().getUser() + "\".");
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

    public void reload(Property property) {
        this.repositoryFactoryFromProvider.reload(property);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryFactory#unlock(org.talend.core.model.general.Project,
     * org.talend.core.model.repository.IRepositoryObject)
     */
    public void unlock(IRepositoryObject obj) throws PersistenceException {
        unlock(getItem(obj));
    }

    /**
     * @param obj
     * @throws PersistenceException
     * @see org.talend.repository.model.IRepositoryFactory#unlock(org.talend.core.model.properties.Item)
     */
    public void unlock(Item obj) throws PersistenceException {
        if (getStatus(obj) == ERepositoryStatus.LOCK_BY_USER) {
            Date commitDate = obj.getState().getCommitDate();
            Date modificationDate = obj.getProperty().getModificationDate();
            if (modificationDate == null || commitDate == null || modificationDate.before(commitDate)) {
                this.repositoryFactoryFromProvider.unlock(obj);
                log.debug("Unlock [" + obj + "] by \"" + getRepositoryContext().getUser() + "\".");
            }
        }
    }

    public void commit(Item obj) throws PersistenceException {
        if (getStatus(obj) == ERepositoryStatus.LOCK_BY_USER) {
            this.repositoryFactoryFromProvider.commit(obj);
            this.repositoryFactoryFromProvider.unlock(obj);
            log.debug("Unlock [" + obj + "] by \"" + getRepositoryContext().getUser() + "\".");
        }
    }

    public boolean findUser(Project project) throws PersistenceException {
        return this.repositoryFactoryFromProvider.findUser(project);
    }

    public void createUser(Project project) throws PersistenceException {
        this.repositoryFactoryFromProvider.createUser(project);
    }

    public void initialize() {
        this.repositoryFactoryFromProvider.initialize();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryFactory#getStatus(org.talend.core.model.properties.Item)
     */
    public ERepositoryStatus getStatus(IRepositoryObject obj) {
        if (obj instanceof Folder) {
        // PTODO SML temporary implementation
            return ERepositoryStatus.LOCK_BY_USER;
        }
        
        if (obj instanceof MetadataTableRepositoryObject) {
            MetadataTableRepositoryObject metadataTableRepositoryObject = (MetadataTableRepositoryObject) obj;
            if (TableHelper.isDeleted(metadataTableRepositoryObject.getTable())) {
                return ERepositoryStatus.DELETED;
            }
        }
        return getStatus(getItem(obj));
    }

    public ERepositoryStatus getStatus(Item item) {
        return this.repositoryFactoryFromProvider.getStatus(item);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryFactory#getStatusAndLockIfPossible(org.talend.core.model.properties.Item)
     */
    public boolean isEditableAndLockIfPossible(Item item) {
        ERepositoryStatus status = getStatus(item);
        if (status.isPotentiallyEditable()) {
            try {
                lock(item);
            } catch (PersistenceException e) {
                e.printStackTrace();
            }
            status = getStatus(item);
        }

        return status.isEditable();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryFactory#isEditable(org.talend.core.model.repository.IRepositoryObject)
     */
    public boolean isEditableAndLockIfPossible(IRepositoryObject obj) {
        return isEditableAndLockIfPossible(getItem(obj));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryFactory#isPotentiallyEditable(org.talend.core.model.properties.Item)
     */
    public boolean isPotentiallyEditable(Item item) {
        ERepositoryStatus status = getStatus(item);
        return status.isPotentiallyEditable();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryFactory#isPotentiallyEditable(org.talend.core.model.repository.IRepositoryObject)
     */
    public boolean isPotentiallyEditable(IRepositoryObject obj) {
        return isPotentiallyEditable(getItem(obj));
    }

    private Item getItem(IRepositoryObject obj) {
        Item item;
        if (obj instanceof MetadataTableRepositoryObject) {
            item = ((MetadataTableRepositoryObject) obj).getProperty().getItem();
        } else {
            item = obj.getProperty().getItem();
        }
        return item;
    }
}
