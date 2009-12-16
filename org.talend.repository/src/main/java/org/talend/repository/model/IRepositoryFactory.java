// ============================================================================
//
// Copyright (C) 2006-2009 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.model;

import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.LoginException;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.data.container.RootContainer;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.SpagoBiServer;
import org.talend.core.model.properties.Status;
import org.talend.core.model.properties.User;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.Folder;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.repository.RepositoryWorkUnit;

/**
 * Defines all methods that a repository provider plug-in must provides to client.<br/>
 * 
 * $Id$
 * 
 */
public interface IRepositoryFactory {

    public String getId();

    public void setId(String id);

    public String getName();

    public void setName(String name);

    public List<DynamicFieldBean> getFields();

    public void setFields(List<DynamicFieldBean> fields);

    public List<DynamicButtonBean> getButtons();

    public List<DynamicChoiceBean> getChoices();

    public boolean isAuthenticationNeeded();

    public void setAuthenticationNeeded(boolean aBnthenticationNeeded);

    public void initialize() throws PersistenceException;

    public void logOnProject(Project project) throws LoginException, PersistenceException;

    public String getNextId();

    public Project createProject(String label, String description, ECodeLanguage language, User author)
            throws PersistenceException;

    public void saveProject(Project project) throws PersistenceException;

    // public boolean doesLoggedUserExist() throws PersistenceException;

    // public void createUser() throws PersistenceException;

    public Project[] readProject() throws PersistenceException, BusinessException;

    public Folder createFolder(Project project, ERepositoryObjectType type, IPath path, String label) throws PersistenceException;

    /**
     * Returns if the name is used by another item of the same type. Type, name and id of item are used to test label
     * availability.
     * 
     * Implementations should be updated when folder are treated as Items.
     * 
     * This function will take some time as it will retrieve all items from the repository. It is possible also to give
     * in parameter the list of items, to avoid to have this function too slow.
     * 
     * @param item
     * @param name TODO
     * @return <code>true</code> if the name is not used an so is available.
     * @throws PersistenceException
     */
    public boolean isNameAvailable(Project project, Item item, String name, List<IRepositoryObject>... givenList)
            throws PersistenceException;

    public boolean isPathValid(Project project, ERepositoryObjectType type, IPath path, String label) throws PersistenceException;

    public void deleteFolder(Project project, ERepositoryObjectType type, IPath path) throws PersistenceException;

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
    public List<IRepositoryObject> getAllVersion(Project project, String id) throws PersistenceException;

    public List<IRepositoryObject> getAllVersion(Project project, String id, String relativeFolder, ERepositoryObjectType type)
            throws PersistenceException;

    /**
     * Returns last version of an object given its id.
     * 
     * @param project - the current project
     * @param id - the id to look for
     * @return the most recent version of object with this id or <code>null</code> if no object with this id has been
     * found
     * @throws PersistenceException
     */
    public IRepositoryObject getLastVersion(Project project, String id) throws PersistenceException;

    /**
     * Returns last version of an object given its id. If folder and repository type is given it can be faster (mostly
     * usefull for imports)
     * 
     * @param project - the current project
     * @param id - the id to look for
     * @return the most recent version of object with this id or <code>null</code> if no object with this id has been
     * found
     * @throws PersistenceException
     */
    public IRepositoryObject getLastVersion(Project project, String id, String folderPath, ERepositoryObjectType type)
            throws PersistenceException;

    /**
     * Returns all object of a given type.<br/>
     * 
     * @param project - the current project
     * @param type - the type
     * @param withDeleted - with deleted items
     * @param allVersions - all versions for one item
     * @return a list of all objects of type <code>type</code> in the repository in the project
     * @throws PersistenceException
     */
    public List<IRepositoryObject> getAll(Project project, ERepositoryObjectType type, boolean withDeleted, boolean allVersions)
            throws PersistenceException;

    /**
     * Deletes logically the given object. <code>isDeleted</code> on this object will now returned <code>true</code>.
     * 
     * @param project - the current project
     * @param objToDelete - the objet to delete
     * @param deletionAuthor - the user perfom the deletion (only for logging in this version)
     * @throws PersistenceException
     */
    public void deleteObjectLogical(Project project, IRepositoryObject objToDelete) throws PersistenceException;

    /**
     * Deletes physically the given object. Object cannot be retrieved.
     * 
     * @param project - the current project
     * @param objToDelete - the objet to delete
     * @param deletionAuthor - the user perfom the deletion (only for logging in this version)
     * @throws PersistenceException
     */
    public void deleteObjectPhysical(Project project, IRepositoryObject objToDelete) throws PersistenceException;

    public void deleteObjectPhysical(Project project, IRepositoryObject objToDelete, String version) throws PersistenceException;

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

    public ERepositoryStatus getStatus(Item item);

    List<Status> getTechnicalStatus() throws PersistenceException;

    List<Status> getDocumentationStatus() throws PersistenceException;

    // TODO SML Remove this method
    List<SpagoBiServer> getSpagoBiServer() throws PersistenceException;

    void setTechnicalStatus(List<Status> list) throws PersistenceException;

    void setDocumentationStatus(List<Status> list) throws PersistenceException;

    void setSpagoBiServer(List<SpagoBiServer> list) throws PersistenceException;

    void setMigrationTasksDone(Project project, List<String> list) throws PersistenceException;

    public String isServerValid() throws BusinessException;

    public void create(Project project, Item item, IPath path) throws PersistenceException;

    public void save(Project project, Item item) throws PersistenceException;

    public void save(Project project, Property property) throws PersistenceException;

    public Item copy(Item item, IPath path) throws PersistenceException, BusinessException;

    public Item copy(Item item, IPath path, boolean changeLabelWithCopyPrefix) throws PersistenceException, BusinessException;

    /**
     * DOC mhelleboid Comment method "cancel".
     * 
     * @param property
     * @throws PersistenceException
     */
    public Property reload(Property property) throws PersistenceException;

    public RootContainer<String, IRepositoryObject> getBusinessProcess(Project project) throws PersistenceException;

    public RootContainer<String, IRepositoryObject> getSVGBusinessProcess(Project project) throws PersistenceException;

    public RootContainer<String, IRepositoryObject> getDocumentation(Project project) throws PersistenceException;

    public RootContainer<String, IRepositoryObject> getProcess(Project project) throws PersistenceException;

    public RootContainer<String, IRepositoryObject> getContext(Project project) throws PersistenceException;

    public RootContainer<String, IRepositoryObject> getSnippets(Project project) throws PersistenceException;

    public RootContainer<String, IRepositoryObject> getRoutine(Project project) throws PersistenceException;

    public RootContainer<String, IRepositoryObject> getMetadataConnection(Project project) throws PersistenceException;

    public RootContainer<String, IRepositoryObject> getMetadataSAPConnection(Project project) throws PersistenceException;

    public RootContainer<String, IRepositoryObject> getMetadataEBCDIC(Project project) throws PersistenceException;

    public RootContainer<String, IRepositoryObject> getMetadataMDM(Project project) throws PersistenceException;

    // feature 0006484
    public RootContainer<String, IRepositoryObject> getMetadataRules(Project project) throws PersistenceException;

    public RootContainer<String, IRepositoryObject> getMetadataSQLPattern(Project project) throws PersistenceException;

    public RootContainer<String, IRepositoryObject> getMetadataFileDelimited(Project project) throws PersistenceException;

    public RootContainer<String, IRepositoryObject> getMetadataFilePositional(Project project) throws PersistenceException;

    public RootContainer<String, IRepositoryObject> getMetadataFileRegexp(Project project) throws PersistenceException;

    public RootContainer<String, IRepositoryObject> getMetadataFileXml(Project project) throws PersistenceException;

    public RootContainer<String, IRepositoryObject> getMetadataFileExcel(Project project) throws PersistenceException;

    public RootContainer<String, IRepositoryObject> getMetadataSalesforceSchema(Project project) throws PersistenceException;

    public RootContainer<String, IRepositoryObject> getMetadataFileLdif(Project project) throws PersistenceException;

    public RootContainer<String, IRepositoryObject> getMetadataLDAPSchema(Project project) throws PersistenceException;

    public RootContainer<String, IRepositoryObject> getMetadataGenericSchema(Project project) throws PersistenceException;

    public RootContainer<String, IRepositoryObject> getMetadataWSDLSchema(Project project) throws PersistenceException;

    public List<IRepositoryObject> getRecycleBinItems(Project project) throws PersistenceException;

    /**
     * gather all the metadata connections (file / db / etc ...).
     */
    List<ConnectionItem> getMetadataConnectionsItem(Project project) throws PersistenceException;

    /**
     * get all context items.
     * 
     * @return
     * @throws PersistenceException
     */
    List<ContextItem> getContextItem(Project project) throws PersistenceException;

    public List<org.talend.core.model.properties.Project> getReferencedProjects();

    public Boolean hasChildren(Object parent);

    public void setDisplayToUser(boolean bool);

    public boolean isDisplayToUser();

    public List<ModuleNeeded> getModulesNeededForJobs() throws PersistenceException;

    /**
     * DOC qzhang Comment method "getJoblets".
     * 
     * @return
     */
    public RootContainer<String, IRepositoryObject> getJoblets(Project project) throws PersistenceException;

    public RootContainer<String, IRepositoryObject> getRoutineFromProject(Project project) throws PersistenceException;

    public void updateItemsPath(ERepositoryObjectType type, IPath targetPath) throws PersistenceException;

    public boolean setAuthorByLogin(Item item, String login) throws PersistenceException;

    public Property getUptodateProperty(Project project, Property property) throws PersistenceException;

    public void beforeLogon(Project project) throws PersistenceException, LoginException;

    public boolean isUserReadOnlyOnCurrentProject();

    public void checkAvailability() throws PersistenceException;

    @SuppressWarnings("unchecked")
    public void executeRepositoryWorkUnit(RepositoryWorkUnit workUnit);

    public void logOffProject();

    public void unloadResources();

    public void unloadResources(Property property);

    /**
     * 
     * DOC mzhao feature 9207 unload and remove the specification resource from the resource set.
     * 
     * @param uriString the uri sting of resource.
     */
    public void unloadResources(String uriString);
}
