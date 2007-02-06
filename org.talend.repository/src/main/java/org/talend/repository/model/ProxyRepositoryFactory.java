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
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.util.EList;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.MessageBoxExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.data.container.RootContainer;
import org.talend.core.CorePlugin;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.general.Project;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.TableHelper;
import org.talend.core.model.migration.IMigrationToolService;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.FolderItem;
import org.talend.core.model.properties.FolderType;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.Status;
import org.talend.core.model.properties.User;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.Folder;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.core.model.temp.ECodeLanguage;
import org.talend.repository.exception.LoginException;
import org.talend.repository.i18n.Messages;
import org.talend.repository.ui.views.RepositoryContentProvider.MetadataTableRepositoryObject;

/**
 * Repository factory use by client. Based on implementation provide by extension point system. This class contains all
 * commons treatments done by repository whatever implementation.<br/>
 * 
 * $Id$
 * 
 */
public class ProxyRepositoryFactory implements IProxyRepositoryFactory {

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

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#getRepositoryContext()
     */
    public RepositoryContext getRepositoryContext() {
        Context ctx = CorePlugin.getContext();
        return (RepositoryContext) ctx.getProperty(Context.REPOSITORY_CONTEXT_KEY);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#getRepositoryFactoryFromProvider()
     */
    public IRepositoryFactory getRepositoryFactoryFromProvider() {
        return this.repositoryFactoryFromProvider;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#setRepositoryFactoryFromProvider(org.talend.repository.model.IRepositoryFactory)
     */
    public void setRepositoryFactoryFromProvider(IRepositoryFactory repositoryFactoryFromProvider) {
        this.repositoryFactoryFromProvider = repositoryFactoryFromProvider;
    }

    private void checkFileName(String fileName, String pattern) {
        if (!Pattern.matches(pattern, fileName)) {
            // i18n
            // throw new IllegalArgumentException("Label " + fileName + " does not match pattern " + pattern);
            throw new IllegalArgumentException(Messages.getString(
                    "ProxyRepositoryFactory.illegalArgumentException.labelNotMatchPattern", new String[] { fileName, pattern })); //$NON-NLS-1$
        }
    }

    private void checkFileNameAndPath(Item item, String pattern, IPath path, boolean folder) throws PersistenceException {
        String fileName = item.getProperty().getLabel();
        checkFileName(fileName, pattern);
        if (!this.repositoryFactoryFromProvider.isNameAvailable(item, null)) {
            // i18n
            // throw new IllegalArgumentException("Label " + fileName + " is already in use");
            throw new IllegalArgumentException(Messages.getString(
                    "ProxyRepositoryFactory.illegalArgumentException.labeAlreadyInUse", new String[] { fileName })); //$NON-NLS-1$
        }
    }

    private void checkFileNameAndPath(String label, String pattern, ERepositoryObjectType type, IPath path, boolean folder)
            throws PersistenceException {
        String fileName = label;
        checkFileName(fileName, pattern);
        if (!this.repositoryFactoryFromProvider.isPathValid(type, path, label)) {
            // i18n
            // throw new IllegalArgumentException("Label " + fileName + " is already in use");
            throw new IllegalArgumentException(Messages.getString(
                    "ProxyRepositoryFactory.illegalArgumentException.labeAlreadyInUse", new String[] { fileName })); //$NON-NLS-1$
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#getMetadataConnectionsItem()
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
    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#isNameAvailable(org.talend.core.model.properties.Item,
     * java.lang.String)
     */
    public boolean isNameAvailable(Item item, String name) throws PersistenceException {
        return this.repositoryFactoryFromProvider.isNameAvailable(item, name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#isPathValid(org.talend.core.model.repository.ERepositoryObjectType,
     * org.eclipse.core.runtime.IPath, java.lang.String)
     */
    public boolean isPathValid(ERepositoryObjectType type, IPath path, String label) throws PersistenceException {
        return this.repositoryFactoryFromProvider.isPathValid(type, path, label);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#createProject(java.lang.String, java.lang.String,
     * org.talend.core.model.temp.ECodeLanguage, org.talend.core.model.properties.User)
     */
    public Project createProject(String label, String description, ECodeLanguage language, User author)
            throws PersistenceException {
        checkFileName(label, RepositoryConstants.PROJECT_PATTERN);
        Project toReturn = this.repositoryFactoryFromProvider.createProject(label, description, language, author);

        IMigrationToolService service = (IMigrationToolService) GlobalServiceRegister.getDefault().getService(
                IMigrationToolService.class);
        service.initNewProjectTasks(toReturn);

        return toReturn;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#createFolder(org.talend.core.model.repository.ERepositoryObjectType,
     * org.eclipse.core.runtime.IPath, java.lang.String)
     */
    public Folder createFolder(ERepositoryObjectType type, IPath path, String label) throws PersistenceException {
        checkFileNameAndPath(label, RepositoryConstants.FOLDER_PATTERN, type, path, true);
        return this.repositoryFactoryFromProvider.createFolder(type, path, label);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#deleteFolder(org.talend.core.model.repository.ERepositoryObjectType,
     * org.eclipse.core.runtime.IPath)
     */
    public void deleteFolder(ERepositoryObjectType type, IPath path) throws PersistenceException {
        this.repositoryFactoryFromProvider.deleteFolder(type, path);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#moveFolder(org.talend.core.model.repository.ERepositoryObjectType,
     * org.eclipse.core.runtime.IPath, org.eclipse.core.runtime.IPath)
     */
    public void moveFolder(ERepositoryObjectType type, IPath sourcePath, IPath targetPath) throws PersistenceException {
        this.repositoryFactoryFromProvider.moveFolder(type, sourcePath, targetPath);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#getBusinessProcess()
     */
    public RootContainer<String, IRepositoryObject> getBusinessProcess() throws PersistenceException {
        return this.repositoryFactoryFromProvider.getBusinessProcess();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#getDocumentation()
     */
    public RootContainer<String, IRepositoryObject> getDocumentation() throws PersistenceException {
        return this.repositoryFactoryFromProvider.getDocumentation();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#getMetadataConnection()
     */
    public RootContainer<String, IRepositoryObject> getMetadataConnection() throws PersistenceException {
        RootContainer<String, IRepositoryObject> metadataConnection = this.repositoryFactoryFromProvider.getMetadataConnection();

        return metadataConnection;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#getMetadataFileDelimited()
     */
    public RootContainer<String, IRepositoryObject> getMetadataFileDelimited() throws PersistenceException {
        return this.repositoryFactoryFromProvider.getMetadataFileDelimited();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#getNextId()
     */
    public String getNextId() {
        String nextId = this.repositoryFactoryFromProvider.getNextId();

        // i18n
        // log.trace("New ID generated on project [" + getRepositoryContext().getProject() + "] = " + nextId);
        String str[] = new String[] { getRepositoryContext().getProject() + "", nextId + "" };//$NON-NLS-1$
        log.trace(Messages.getString("ProxyRepositoryFactory.log.newIdGenerated", str)); //$NON-NLS-1$
        return nextId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#getProcess()
     */
    public RootContainer<String, IRepositoryObject> getProcess() throws PersistenceException {
        return this.repositoryFactoryFromProvider.getProcess();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#getRoutine()
     */
    public RootContainer<String, IRepositoryObject> getRoutine() throws PersistenceException {
        return this.repositoryFactoryFromProvider.getRoutine();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#readProject()
     */
    public Project[] readProject() throws PersistenceException, BusinessException {
        return this.repositoryFactoryFromProvider.readProject();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#renameFolder(org.talend.core.model.repository.ERepositoryObjectType,
     * org.eclipse.core.runtime.IPath, java.lang.String)
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
    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#deleteObjectLogical(org.talend.core.model.repository.IRepositoryObject)
     */
    public void deleteObjectLogical(IRepositoryObject objToDelete) throws PersistenceException {
        this.repositoryFactoryFromProvider.deleteObjectLogical(objToDelete);
        // i18n
        // log.debug("Logical deletion [" + objToDelete + "] by " + getRepositoryContext().getUser() + ".");
        String str[] = new String[] { objToDelete + "", getRepositoryContext().getUser() + "" };//$NON-NLS-1$
        log.debug(Messages.getString("ProxyRepositoryFactory.log.logicalDeletion", str)); //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#deleteObjectPhysical(org.talend.core.model.repository.IRepositoryObject)
     */
    public void deleteObjectPhysical(IRepositoryObject objToDelete) throws PersistenceException {
        this.repositoryFactoryFromProvider.deleteObjectPhysical(objToDelete);
        // i18n
        // log.info("Physical deletion [" + objToDelete + "] by " + getRepositoryContext().getUser() + ".");
        String str[] = new String[] { objToDelete.toString(), getRepositoryContext().getUser().toString() };
        log.info(Messages.getString("ProxyRepositoryFactory.log.physicalDeletion", str)); //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#restoreObject(org.talend.core.model.repository.IRepositoryObject,
     * org.eclipse.core.runtime.IPath)
     */
    public void restoreObject(IRepositoryObject objToRestore, IPath path) throws PersistenceException {
        this.repositoryFactoryFromProvider.restoreObject(objToRestore, path);

        // i18n
        // log.debug("Restoration [" + objToRestore + "] by " + getRepositoryContext().getUser() + " to \"/" + path +
        // "\".");
        String str[] = new String[] { objToRestore + "", getRepositoryContext().getUser() + "", path + "" };//$NON-NLS-1$
        log.debug(Messages.getString("ProxyRepositoryFactory.log.Restoration", str)); //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#getOldPath(org.talend.core.model.repository.IRepositoryObject)
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
    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#moveObject(org.talend.core.model.repository.IRepositoryObject,
     * org.eclipse.core.runtime.IPath)
     */
    public void moveObject(IRepositoryObject objToMove, IPath path) throws PersistenceException, BusinessException {
        checkDisponibilite(objToMove);
        checkFileNameAndPath(objToMove.getProperty().getItem(), RepositoryConstants.getPattern(objToMove.getType()), path, false);
        this.repositoryFactoryFromProvider.moveObject(objToMove, path);

        // i18n
        // log.debug("Move [" + objToMove + "] to \"" + path + "\".");
        String str[] = new String[] { objToMove + "", path + "" };
        log.debug(Messages.getString("ProxyRepositoryFactory.log.move", str)); //$NON-NLS-1$
        unlock(getItem(objToMove));
    }

    // TODO SML Renommer et finir la m�thode et la plugger dans toutes les m�thodes
    private void checkDisponibilite(IRepositoryObject objToMove) throws BusinessException {
        if (!isEditableAndLockIfPossible(objToMove)) {
            throw new BusinessException(Messages.getString("ProxyRepositoryFactory.bussinessException.itemNonModifiable")); //$NON-NLS-1$
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#getMetadataFilePositional()
     */
    public RootContainer<String, IRepositoryObject> getMetadataFilePositional() throws PersistenceException {
        return this.repositoryFactoryFromProvider.getMetadataFilePositional();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#getMetadataFileRegexp()
     */
    public RootContainer<String, IRepositoryObject> getMetadataFileRegexp() throws PersistenceException {
        return this.repositoryFactoryFromProvider.getMetadataFileRegexp();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#getMetadataFileXml()
     */
    public RootContainer<String, IRepositoryObject> getMetadataFileXml() throws PersistenceException {
        return this.repositoryFactoryFromProvider.getMetadataFileXml();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#getMetadataFileLdif()
     */
    public RootContainer<String, IRepositoryObject> getMetadataFileLdif() throws PersistenceException {
        return this.repositoryFactoryFromProvider.getMetadataFileLdif();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#lock(org.talend.core.model.repository.IRepositoryObject)
     */
    public void lock(IRepositoryObject obj) throws PersistenceException, BusinessException {
        lock(getItem(obj));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#lock(org.talend.core.model.properties.Item)
     */
    public void lock(Item item) throws PersistenceException, BusinessException {
        if (getStatus(item).isPotentiallyEditable()) {
            this.repositoryFactoryFromProvider.lock(item);
            // i18n
            // log.debug("Lock [" + item + "] by \"" + getRepositoryContext().getUser() + "\".");
            String str[] = new String[] { item.toString(), getRepositoryContext().getUser().toString() };
            log.debug(Messages.getString("ProxyRepositoryFactory.log.lock", str)); //$NON-NLS-1$
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryFactory#getAllVersion(org.talend.core.model.general.Project, int)
     */
    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#getAllVersion(java.lang.String)
     */
    public List<IRepositoryObject> getAllVersion(String id) throws PersistenceException {
        return this.repositoryFactoryFromProvider.getAllVersion(id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryFactory#getLastVersion(org.talend.core.model.general.Project, int)
     */
    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#getLastVersion(java.lang.String)
     */
    public IRepositoryObject getLastVersion(String id) throws PersistenceException {
        return this.repositoryFactoryFromProvider.getLastVersion(id);
    }

    public List<IRepositoryObject> getAll(ERepositoryObjectType type) throws PersistenceException {
        return getAll(type, false);
    }

    public List<IRepositoryObject> getAll(ERepositoryObjectType type, boolean withDeleted) throws PersistenceException {
        return this.repositoryFactoryFromProvider.getAll(type, withDeleted);
    }

    public List<String> getFolders(ERepositoryObjectType type) throws PersistenceException {
        List<String> toReturn = new ArrayList<String>();
        Project project = getRepositoryContext().getProject();
        EList list = project.getEmfProject().getFolders();

        String[] split = ERepositoryObjectType.getFolderName(type).split("/"); //$NON-NLS-1$
        String labelType = split[split.length - 1];

        for (Object current : list) {
            FolderItem folderItem = (FolderItem) current;
            addChildren(toReturn, folderItem, labelType, ""); //$NON-NLS-1$
        }
        return toReturn;
    }

    private void addChildren(List<String> target, FolderItem source, String type, String path) {
        if (source.getType() == FolderType.FOLDER_LITERAL) {
            // FIXME mhelleboid Related to bug 364
            if (source.getProperty().getLabel().equals(".settings")) { //$NON-NLS-1$
                return;
            }
            target.add(path + source.getProperty().getLabel());

            for (Object current : source.getChildren()) {
                if (current instanceof FolderItem) {
                    addChildren(target, (FolderItem) current, type, path + source.getProperty().getLabel() + "/"); //$NON-NLS-1$
                }
            }
        }

        if (source.getType() == FolderType.SYSTEM_FOLDER_LITERAL || source.getType() == FolderType.STABLE_SYSTEM_FOLDER_LITERAL) {
            boolean match = source.getProperty().getLabel().equals(type);

            for (Object current : source.getChildren()) {
                if (current instanceof FolderItem) {
                    FolderItem currentChild = (FolderItem) current;
                    if (currentChild.getType() == FolderType.FOLDER_LITERAL && match) {
                        addChildren(target, currentChild, type, path);
                    } else if (currentChild.getType() == FolderType.SYSTEM_FOLDER_LITERAL
                            || currentChild.getType() == FolderType.STABLE_SYSTEM_FOLDER_LITERAL) {
                        addChildren(target, currentChild, type, path);
                    }
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#getDocumentationStatus()
     */
    public List<Status> getDocumentationStatus() throws PersistenceException {
        return this.repositoryFactoryFromProvider.getDocumentationStatus();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#getTechnicalStatus()
     */
    public List<Status> getTechnicalStatus() throws PersistenceException {
        return this.repositoryFactoryFromProvider.getTechnicalStatus();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#setDocumentationStatus(java.util.List)
     */
    public void setDocumentationStatus(List<Status> list) throws PersistenceException {
        this.repositoryFactoryFromProvider.setDocumentationStatus(list);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#setTechnicalStatus(java.util.List)
     */
    public void setTechnicalStatus(List<Status> list) throws PersistenceException {
        this.repositoryFactoryFromProvider.setTechnicalStatus(list);
    }

    public void setMigrationTasksDone(Project project, List<String> list) throws PersistenceException {
        this.repositoryFactoryFromProvider.setMigrationTasksDone(project, list);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryFactory#isServerValid()
     */
    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#isServerValid()
     */
    // public String isServerValid() {
    // return this.repositoryFactoryFromProvider.isServerValid();
    // }
    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#create(org.talend.core.model.properties.Item,
     * org.eclipse.core.runtime.IPath)
     */
    public void create(Item item, IPath path) throws PersistenceException {
        checkFileNameAndPath(item, RepositoryConstants.getPattern(ERepositoryObjectType.getItemType(item)), path, false);
        this.repositoryFactoryFromProvider.create(item, path);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#save(org.talend.core.model.properties.Item)
     */
    public void save(Item item) throws PersistenceException {
        this.repositoryFactoryFromProvider.save(item);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#save(org.talend.core.model.properties.Property)
     */
    public void save(Property property) throws PersistenceException {
        this.repositoryFactoryFromProvider.save(property);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#copy(org.talend.core.model.properties.Item,
     * org.eclipse.core.runtime.IPath)
     */
    public Item copy(Item item, IPath path) throws PersistenceException {
        return this.repositoryFactoryFromProvider.copy(item, path);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#reload(org.talend.core.model.properties.Property)
     */
    public Property reload(Property property) throws PersistenceException {
        return this.repositoryFactoryFromProvider.reload(property);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryFactory#unlock(org.talend.core.model.general.Project,
     * org.talend.core.model.repository.IRepositoryObject)
     */
    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#unlock(org.talend.core.model.repository.IRepositoryObject)
     */
    public void unlock(IRepositoryObject obj) throws PersistenceException {
        unlock(getItem(obj));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#unlock(org.talend.core.model.properties.Item)
     */
    public void unlock(Item obj) throws PersistenceException {
        if (getStatus(obj) == ERepositoryStatus.LOCK_BY_USER) {
            Date commitDate = obj.getState().getCommitDate();
            Date modificationDate = obj.getProperty().getModificationDate();
            if (modificationDate == null || commitDate == null || modificationDate.before(commitDate)) {
                this.repositoryFactoryFromProvider.unlock(obj);

                // i18n
                // log.debug("Unlock [" + obj + "] by \"" + getRepositoryContext().getUser() + "\".");
                String str[] = new String[] { obj.toString(), getRepositoryContext().getUser().toString() };
                log.debug(Messages.getString("ProxyRepositoryFactory.log.unlock", str)); //$NON-NLS-1$
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#findUser(org.talend.core.model.general.Project)
     */
    // public boolean doesLoggedUserExist() throws PersistenceException {
    // return this.repositoryFactoryFromProvider.doesLoggedUserExist();
    // }
    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#createUser(org.talend.core.model.general.Project)
     */
    // public void createUser() throws PersistenceException {
    // this.repositoryFactoryFromProvider.createUser();
    // }
    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#initialize()
     */
    public void initialize() {
        this.repositoryFactoryFromProvider.initialize();
    }

    /**
     * @param project
     * @throws PersistenceException
     * @throws LoginException
     * @see org.talend.repository.model.IRepositoryFactory#logOnProject(org.talend.core.model.general.Project)
     */
    public void logOnProject(Project project) throws PersistenceException, LoginException {
        getRepositoryContext().setProject(project);
        this.repositoryFactoryFromProvider.logOnProject(project);

        // i18n
        // log.info(getRepositoryContext().getUser() + " logged on " + getRepositoryContext().getProject());
        String str[] = new String[] { getRepositoryContext().getUser() + "", getRepositoryContext().getProject() + "" }; //$NON-NLS-1$        
        log.info(Messages.getString("ProxyRepositoryFactory.log.loggedOn", str)); //$NON-NLS-1$

        IMigrationToolService service = (IMigrationToolService) GlobalServiceRegister.getDefault().getService(
                IMigrationToolService.class);
        service.executeProjectTasks(project);

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryFactory#getStatus(org.talend.core.model.properties.Item)
     */
    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#getStatus(org.talend.core.model.repository.IRepositoryObject)
     */
    public ERepositoryStatus getStatus(IRepositoryObject obj) {
        if (obj instanceof MetadataTableRepositoryObject) {
            MetadataTableRepositoryObject metadataTableRepositoryObject = (MetadataTableRepositoryObject) obj;
            if (TableHelper.isDeleted(metadataTableRepositoryObject.getTable())) {
                return ERepositoryStatus.DELETED;
            }
        }
        return getStatus(getItem(obj));
    }

    @Deprecated
    public boolean isDeleted(MetadataTable table) {
        // TODO SML/MHE Remove when table are items
        if (TableHelper.isDeleted(table)) {
            return true;
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#getStatus(org.talend.core.model.properties.Item)
     */
    public ERepositoryStatus getStatus(Item item) {
        // PTODO SML [FOLDERS] temp code
        if (item instanceof FolderItem) {
            return ERepositoryStatus.EDITABLE;
        }

        return this.repositoryFactoryFromProvider.getStatus(item);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryFactory#getStatusAndLockIfPossible(org.talend.core.model.properties.Item)
     */
    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#isEditableAndLockIfPossible(org.talend.core.model.properties.Item)
     */
    public boolean isEditableAndLockIfPossible(Item item) {
        ERepositoryStatus status = getStatus(item);
        if (status.isPotentiallyEditable()) {
            try {
                lock(item);
            } catch (PersistenceException e) {
                MessageBoxExceptionHandler.process(e);
            } catch (BusinessException e) {
                // Nothing to do
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
    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#isEditableAndLockIfPossible(org.talend.core.model.repository.IRepositoryObject)
     */
    public boolean isEditableAndLockIfPossible(IRepositoryObject obj) {
        if (obj instanceof MetadataTableRepositoryObject) {
            MetadataTable table = ((MetadataTableRepositoryObject) obj).getTable();
            if (TableHelper.isDeleted(table)) {
                return false;
            } else {
                return isEditableAndLockIfPossible(getItem(obj));
            }
        } else {
            return isEditableAndLockIfPossible(getItem(obj));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryFactory#isPotentiallyEditable(org.talend.core.model.properties.Item)
     */
    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#isPotentiallyEditable(org.talend.core.model.properties.Item)
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
    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#isPotentiallyEditable(org.talend.core.model.repository.IRepositoryObject)
     */
    public boolean isPotentiallyEditable(IRepositoryObject obj) {
        if (obj instanceof MetadataTableRepositoryObject) {
            MetadataTable table = ((MetadataTableRepositoryObject) obj).getTable();
            if (TableHelper.isDeleted(table)) {
                return false;
            } else {
                return isPotentiallyEditable(getItem(obj));
            }
        } else {
            return isPotentiallyEditable(getItem(obj));
        }
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
