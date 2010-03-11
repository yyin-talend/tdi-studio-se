// ============================================================================
//
// Copyright (C) 2006-2010 Talend Inc. - www.talend.com
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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.talend.commons.CommonsPlugin;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.LoginException;
import org.talend.commons.exception.MessageBoxExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.exception.SystemException;
import org.talend.commons.utils.data.container.RootContainer;
import org.talend.commons.utils.generation.JavaUtils;
import org.talend.commons.utils.workbench.resources.ResourceUtils;
import org.talend.core.CorePlugin;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.PluginChecker;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.general.Project;
import org.talend.core.model.metadata.builder.connection.AbstractMetadataObject;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.SubItemHelper;
import org.talend.core.model.metadata.builder.connection.TableHelper;
import org.talend.core.model.migration.IMigrationToolService;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.model.properties.FolderItem;
import org.talend.core.model.properties.FolderType;
import org.talend.core.model.properties.InformationLevel;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.JobDocumentationItem;
import org.talend.core.model.properties.JobletDocumentationItem;
import org.talend.core.model.properties.JobletProcessItem;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.SpagoBiServer;
import org.talend.core.model.properties.Status;
import org.talend.core.model.properties.User;
import org.talend.core.model.properties.impl.FolderItemImpl;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.Folder;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.core.model.utils.JavaResourcesHelper;
import org.talend.core.model.utils.PerlResourcesHelper;
import org.talend.core.ui.IRulesProviderService;
import org.talend.designer.codegen.ICodeGeneratorService;
import org.talend.designer.core.IDesignerCoreService;
import org.talend.designer.joblet.ui.IJobCheckService;
import org.talend.designer.runprocess.IRunProcessService;
import org.talend.repository.ProjectManager;
import org.talend.repository.RepositoryPlugin;
import org.talend.repository.RepositoryWorkUnit;
import org.talend.repository.documentation.ERepositoryActionName;
import org.talend.repository.i18n.Messages;
import org.talend.repository.ui.wizards.exportjob.scriptsmanager.JobJavaScriptsManager;
import org.talend.repository.utils.RepositoryPathProvider;

/**
 * Repository factory use by client. Based on implementation provide by extension point system. This class contains all
 * commons treatments done by repository whatever implementation.<br/>
 * 
 * $Id$
 * 
 */
/**
 * DOC Administrator class global comment. Detailled comment
 */
public final class ProxyRepositoryFactory implements IProxyRepositoryFactory {

    private static final int MAX_TASKS = 7;

    private static Logger log = Logger.getLogger(ProxyRepositoryFactory.class);

    private IRepositoryFactory repositoryFactoryFromProvider;

    private static ProxyRepositoryFactory singleton = null;

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    private ProjectManager projectManager;

    public synchronized void addPropertyChangeListener(PropertyChangeListener l) {
        if (l == null) {
            throw new IllegalArgumentException();
        }
        support.addPropertyChangeListener(l);
    }

    public synchronized void removePropertyChangeListener(PropertyChangeListener l) {
        if (l != null) {
            support.removePropertyChangeListener(l);
        }
    }

    protected void fireRepositoryPropertyChange(String property, Object oldValue, Object newValue) {
        if (support.hasListeners(property)) {
            support.firePropertyChange(property, oldValue, newValue);
        }
    }

    /**
     * DOC smallet ProxyRepositoryFactory constructor comment.
     */
    private ProxyRepositoryFactory() {
        projectManager = ProjectManager.getInstance();
    }

    public static synchronized ProxyRepositoryFactory getInstance() {
        if (singleton == null) {
            singleton = new ProxyRepositoryFactory();
        }
        return singleton;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#refreshJobPictureFolder()
     */
    public void refreshJobPictureFolder(String picFolder) {
        IFolder folder = RepositoryPathProvider.getFolder(picFolder);
        try {
            folder.refreshLocal(1, null);
        } catch (Exception e) {
            // e.printStackTrace();
            ExceptionHandler.process(e);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#refreshJobPictureFolder()
     */
    public void refreshDocumentationFolder(String docFolder) {
        IFolder folder = RepositoryPathProvider.getFolder(docFolder);
        if (folder != null) {
            try {
                folder.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
            } catch (Exception e) {
                // e.printStackTrace();
                ExceptionHandler.process(e);
            }
        }
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
     * @see
     * org.talend.repository.model.IProxyRepositoryFactory#setRepositoryFactoryFromProvider(org.talend.repository.model
     * .IRepositoryFactory)
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

    private void checkFileNameAndPath(Project project, Item item, String pattern, IPath path, boolean folder,
            boolean... isImportItem) throws PersistenceException {
        String fileName = item.getProperty().getLabel();
        checkFileName(fileName, pattern);

        // if the check comes from create item when import item, then no need to check the name availability
        // since we already checked before.
        if ((isImportItem.length == 0) && !this.repositoryFactoryFromProvider.isNameAvailable(project, item, null)) {
            // i18n
            // throw new IllegalArgumentException("Label " + fileName + " is already in use");
            // throw new IllegalArgumentException(Messages.getString(
            //                    "ProxyRepositoryFactory.illegalArgumentException.labeAlreadyInUse", new String[] { fileName })); //$NON-NLS-1$

            MessageBox box = new MessageBox(Display.getCurrent().getActiveShell(), SWT.ICON_ERROR | SWT.OK | SWT.CANCEL);
            box.setText(Messages.getString("ProxyRepositoryFactory.JobNameErroe")); //$NON-NLS-1$
            box
                    .setMessage(Messages.getString("ProxyRepositoryFactory.Label") + fileName + Messages.getString("ProxyRepositoryFactory.ReplaceJob")); //$NON-NLS-1$ //$NON-NLS-2$
            if (box.open() == SWT.OK) {
                return;
            } else {
                throw new IllegalArgumentException(Messages.getString(
                        "ProxyRepositoryFactory.illegalArgumentException.labeAlreadyInUse", new String[] { fileName })); //$NON-NLS-1$
            }
        }
    }

    private void checkFileNameAndPath(Project proejct, String label, String pattern, ERepositoryObjectType type, IPath path,
            boolean folder) throws PersistenceException {
        String fileName = label;
        checkFileName(fileName, pattern);

        if (!this.repositoryFactoryFromProvider.isPathValid(proejct, type, path, label)) {
            // i18n
            // throw new IllegalArgumentException("Label " + fileName + " is already in use");
            if (!type.isResourceItem()) {
                throw new IllegalArgumentException(Messages.getString(
                        "ProxyRepositoryFactory.illegalArgumentException.labeAlreadyInUse", new String[] { fileName })); //$NON-NLS-1$
            }

        }
    }

    public List<ConnectionItem> getMetadataConnectionsItem(Project project) throws PersistenceException {
        return this.repositoryFactoryFromProvider.getMetadataConnectionsItem(project);
    }

    public List<ConnectionItem> getMetadataConnectionsItem() throws PersistenceException {
        return getMetadataConnectionsItem(projectManager.getCurrentProject());
    }

    public List<ContextItem> getContextItem(Project project) throws PersistenceException {
        return this.repositoryFactoryFromProvider.getContextItem(project);
    }

    public List<ContextItem> getContextItem() throws PersistenceException {
        List<ContextItem> contextItems = getContextItem(projectManager.getCurrentProject());
        if (contextItems == null) {
            contextItems = new ArrayList<ContextItem>();
        }
        for (Project p : projectManager.getAllReferencedProjects()) {
            List<ContextItem> rContextItems = getContextItem(p);
            if (rContextItems != null) {
                contextItems.addAll(rContextItems);
            }
        }
        return contextItems;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#isNameAvailable(org.talend.core.model.properties.Item,
     * java.lang.String)
     */
    public boolean isNameAvailable(Item item, String name, List<IRepositoryObject>... givenList) throws PersistenceException {
        return isNameAvailable(projectManager.getCurrentProject(), item, name, givenList);
    }

    public boolean isNameAvailable(Project project, Item item, String name, List<IRepositoryObject>... givenList)
            throws PersistenceException {
        return this.repositoryFactoryFromProvider.isNameAvailable(project, item, name, givenList);
    }

    /*
     * (non-Javadoc)
     * 
     * @seeorg.talend.repository.model.IProxyRepositoryFactory#isPathValid(org.talend.core.model.repository.
     * ERepositoryObjectType, org.eclipse.core.runtime.IPath, java.lang.String)
     */
    public boolean isPathValid(ERepositoryObjectType type, IPath path, String label) throws PersistenceException {
        return isPathValid(projectManager.getCurrentProject(), type, path, label);
    }

    public boolean isPathValid(Project proejct, ERepositoryObjectType type, IPath path, String label) throws PersistenceException {
        return this.repositoryFactoryFromProvider.isPathValid(proejct, type, path, label);
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

    public void saveProject(Project project) throws PersistenceException {
        repositoryFactoryFromProvider.saveProject(project);
    }

    /*
     * (non-Javadoc)
     * 
     * @seeorg.talend.repository.model.IProxyRepositoryFactory#createFolder(org.talend.core.model.repository.
     * ERepositoryObjectType, org.eclipse.core.runtime.IPath, java.lang.String)
     */
    public Folder createFolder(ERepositoryObjectType type, IPath path, String label) throws PersistenceException {
        return createFolder(projectManager.getCurrentProject(), type, path, label);
    }

    public Folder createFolder(Project project, ERepositoryObjectType type, IPath path, String label) throws PersistenceException {
        checkFileNameAndPath(project, label, RepositoryConstants.FOLDER_PATTERN, type, path, true);
        Folder createFolder = this.repositoryFactoryFromProvider.createFolder(project, type, path, label);
        if (type == ERepositoryObjectType.PROCESS || type == ERepositoryObjectType.JOBLET) {
            fireRepositoryPropertyChange(ERepositoryActionName.FOLDER_CREATE.getName(), path, new Object[] { createFolder, type });
        }
        return createFolder;
    }

    /*
     * (non-Javadoc)
     * 
     * @seeorg.talend.repository.model.IProxyRepositoryFactory#deleteFolder(org.talend.core.model.repository.
     * ERepositoryObjectType, org.eclipse.core.runtime.IPath)
     */
    public synchronized void deleteFolder(ERepositoryObjectType type, IPath path) throws PersistenceException {
        deleteFolder(projectManager.getCurrentProject(), type, path);
    }

    public synchronized void deleteFolder(Project project, ERepositoryObjectType type, IPath path) throws PersistenceException {
        this.repositoryFactoryFromProvider.deleteFolder(project, type, path);
        if (type == ERepositoryObjectType.PROCESS) {
            fireRepositoryPropertyChange(ERepositoryActionName.FOLDER_DELETE.getName(), path, type);
        }
        if (type == ERepositoryObjectType.JOBLET) {
            fireRepositoryPropertyChange(ERepositoryActionName.JOBLET_FOLDER_DELETE.getName(), path, type);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.model.IProxyRepositoryFactory#moveFolder(org.talend.core.model.repository.ERepositoryObjectType
     * , org.eclipse.core.runtime.IPath, org.eclipse.core.runtime.IPath)
     */
    public void moveFolder(ERepositoryObjectType type, IPath sourcePath, IPath targetPath) throws PersistenceException {
        this.repositoryFactoryFromProvider.moveFolder(type, sourcePath, targetPath);
        if (type == ERepositoryObjectType.PROCESS) {
            fireRepositoryPropertyChange(ERepositoryActionName.FOLDER_MOVE.getName(), sourcePath, targetPath);
        }
        if (type == ERepositoryObjectType.JOBLET) {
            fireRepositoryPropertyChange(ERepositoryActionName.JOBLET_FOLDER_MOVE.getName(), sourcePath, targetPath);
        }
        this.repositoryFactoryFromProvider.updateItemsPath(type, targetPath.append(sourcePath.lastSegment()));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#getBusinessProcess()
     */
    public RootContainer<String, IRepositoryObject> getBusinessProcess() throws PersistenceException {
        return getBusinessProcess(projectManager.getCurrentProject());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#getDocumentation()
     */
    public RootContainer<String, IRepositoryObject> getDocumentation() throws PersistenceException {
        return getDocumentation(projectManager.getCurrentProject());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#getMetadataConnection()
     */
    public RootContainer<String, IRepositoryObject> getMetadataConnection() throws PersistenceException {
        return getMetadataConnection(projectManager.getCurrentProject());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#getMetadataFileDelimited()
     */
    public RootContainer<String, IRepositoryObject> getMetadataFileDelimited() throws PersistenceException {
        return getMetadataFileDelimited(projectManager.getCurrentProject());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#getNextId()
     */
    public String getNextId() {
        String nextId = this.repositoryFactoryFromProvider.getNextId();

        // i18n
        // log.trace("New ID generated on project [" + projectManager.getCurrentProject() + "] = " + nextId);
        String str[] = new String[] { projectManager.getCurrentProject() + "", nextId + "" };//$NON-NLS-1$ //$NON-NLS-2$
        log.trace(Messages.getString("ProxyRepositoryFactory.log.newIdGenerated", str)); //$NON-NLS-1$
        return nextId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#getProcess()
     */
    public RootContainer<String, IRepositoryObject> getProcess() throws PersistenceException {
        return getProcess(projectManager.getCurrentProject());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#getProcess()
     */
    public RootContainer<String, IRepositoryObject> getContext() throws PersistenceException {
        return getContext(projectManager.getCurrentProject());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#getRoutine()
     */
    public RootContainer<String, IRepositoryObject> getRoutine() throws PersistenceException {
        return getRoutine(projectManager.getCurrentProject());
    }

    public RootContainer<String, IRepositoryObject> getMetadataSQLPattern() throws PersistenceException {
        return getMetadataSQLPattern(projectManager.getCurrentProject());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#getSnippets()
     */
    public RootContainer<String, IRepositoryObject> getSnippets() throws PersistenceException {
        return getSnippets(projectManager.getCurrentProject());
    }

    public RootContainer<String, IRepositoryObject> getRoutineFromProject(Project project) throws PersistenceException {
        return this.repositoryFactoryFromProvider.getRoutineFromProject(project);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#getRecycleBinItems()
     */
    public List<IRepositoryObject> getRecycleBinItems() throws PersistenceException {
        return getRecycleBinItems(projectManager.getCurrentProject());
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
     * @seeorg.talend.repository.model.IProxyRepositoryFactory#renameFolder(org.talend.core.model.repository.
     * ERepositoryObjectType, org.eclipse.core.runtime.IPath, java.lang.String)
     */
    public void renameFolder(ERepositoryObjectType type, IPath path, String label) throws PersistenceException {
        this.repositoryFactoryFromProvider.renameFolder(type, path, label);
        if (type == ERepositoryObjectType.PROCESS) {
            fireRepositoryPropertyChange(ERepositoryActionName.FOLDER_RENAME.getName(), path, label);
        }
        if (type == ERepositoryObjectType.JOBLET) {
            fireRepositoryPropertyChange(ERepositoryActionName.JOBLET_FOLDER_RENAME.getName(), path, label);
        }
        this.repositoryFactoryFromProvider.updateItemsPath(type, path.removeLastSegments(1).append(label));
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
     * @seeorg.talend.repository.model.IProxyRepositoryFactory#deleteObjectLogical(org.talend.core.model.repository.
     * IRepositoryObject)
     */
    public void deleteObjectLogical(IRepositoryObject objToDelete) throws PersistenceException, BusinessException {
        deleteObjectLogical(projectManager.getCurrentProject(), objToDelete);
    }

    public void deleteObjectLogical(Project project, IRepositoryObject objToDelete) throws PersistenceException,
            BusinessException {
        checkAvailability(objToDelete);
        this.repositoryFactoryFromProvider.deleteObjectLogical(project, objToDelete);
        unlock(objToDelete);
        // i18n
        // log.debug("Logical deletion [" + objToDelete + "] by " + getRepositoryContext().getUser() + ".");
        String str[] = new String[] { objToDelete + "", getRepositoryContext().getUser() + "" };//$NON-NLS-1$ //$NON-NLS-2$
        log.debug(Messages.getString("ProxyRepositoryFactory.log.logicalDeletion", str)); //$NON-NLS-1$

        // TODO this need to be refactered after M2.
        if (objToDelete.getType() == ERepositoryObjectType.PROCESS || objToDelete.getType() == ERepositoryObjectType.JOBLET
                || objToDelete.getType() == ERepositoryObjectType.ROUTINES) {
            fireRepositoryPropertyChange(ERepositoryActionName.JOB_DELETE_TO_RECYCLE_BIN.getName(), null, objToDelete);
        }

        if (objToDelete.getType() == ERepositoryObjectType.BUSINESS_PROCESS) {
            fireRepositoryPropertyChange(ERepositoryActionName.BUSINESS_DELETE_TO_RECYCLE_BIN.getName(), null, objToDelete);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @seeorg.talend.repository.model.IProxyRepositoryFactory#deleteObjectPhysical(org.talend.core.model.repository.
     * IRepositoryObject)
     */
    public void forceDeleteObjectPhysical(IRepositoryObject objToDelete, String version) throws PersistenceException {
        this.repositoryFactoryFromProvider.deleteObjectPhysical(projectManager.getCurrentProject(), objToDelete, version);
        // i18n
        // log.info("Physical deletion [" + objToDelete + "] by " + getRepositoryContext().getUser() + ".");
        String str[] = new String[] { objToDelete.toString(), getRepositoryContext().getUser().toString() };
        log.info(Messages.getString("ProxyRepositoryFactory.log.physicalDeletion", str)); //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @seeorg.talend.repository.model.IProxyRepositoryFactory#deleteObjectPhysical(org.talend.core.model.repository.
     * IRepositoryObject)
     */
    public void deleteObjectPhysical(IRepositoryObject objToDelete) throws PersistenceException {
        deleteObjectPhysical(objToDelete, null);
    }

    public void deleteObjectPhysical(IRepositoryObject objToDelete, String version) throws PersistenceException {
        deleteObjectPhysical(projectManager.getCurrentProject(), objToDelete, version);
    }

    public void deleteObjectPhysical(Project project, IRepositoryObject objToDelete) throws PersistenceException {
        deleteObjectPhysical(project, objToDelete, null);
    }

    public void deleteObjectPhysical(Project project, IRepositoryObject objToDelete, String version) throws PersistenceException {
        if (project == null || objToDelete == null) {
            return;
        }
        this.repositoryFactoryFromProvider.deleteObjectPhysical(project, objToDelete, version);
        // i18n
        // log.info("Physical deletion [" + objToDelete + "] by " + getRepositoryContext().getUser() + ".");
        String str[] = new String[] { objToDelete.toString(), getRepositoryContext().getUser().toString() };
        log.info(Messages.getString("ProxyRepositoryFactory.log.physicalDeletion", str)); //$NON-NLS-1$

        if (objToDelete.getType() == ERepositoryObjectType.PROCESS || objToDelete.getType() == ERepositoryObjectType.JOBLET
                || objToDelete.getType() == ERepositoryObjectType.ROUTINES) {
            fireRepositoryPropertyChange(ERepositoryActionName.JOB_DELETE_FOREVER.getName(), null, objToDelete);
            if (objToDelete.getType() == ERepositoryObjectType.PROCESS) {
                // delete the job launch, for bug 8878
                IDesignerCoreService designerCoreService = RepositoryPlugin.getDefault().getDesignerCoreService();
                if (designerCoreService != null) {
                    designerCoreService.removeJobLaunch(objToDelete);
                }
            }
        }
        if (objToDelete.getType() == ERepositoryObjectType.BUSINESS_PROCESS) {
            fireRepositoryPropertyChange(ERepositoryActionName.BUSINESS_DELETE_FOREVER.getName(), null, objToDelete);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.model.IProxyRepositoryFactory#restoreObject(org.talend.core.model.repository.IRepositoryObject
     * , org.eclipse.core.runtime.IPath)
     */
    public void restoreObject(IRepositoryObject objToRestore, IPath path) throws PersistenceException, BusinessException {
        if (ProxyRepositoryFactory.getInstance().isUserReadOnlyOnCurrentProject()) {
            throw new BusinessException(Messages.getString("ProxyRepositoryFactory.bussinessException.itemNonModifiable")); //$NON-NLS-1$
        }
        this.repositoryFactoryFromProvider.restoreObject(objToRestore, path);
        unlock(objToRestore);
        // i18n
        // log.debug("Restoration [" + objToRestore + "] by " + getRepositoryContext().getUser() + " to \"/" + path +
        // "\".");
        String str[] = new String[] { objToRestore + "", getRepositoryContext().getUser() + "", path + "" };//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        log.debug(Messages.getString("ProxyRepositoryFactory.log.Restoration", str)); //$NON-NLS-1$
        if (objToRestore.getType() == ERepositoryObjectType.PROCESS || objToRestore.getType() == ERepositoryObjectType.JOBLET
                || objToRestore.getType() == ERepositoryObjectType.ROUTINES) {
            fireRepositoryPropertyChange(ERepositoryActionName.JOB_RESTORE.getName(), null, objToRestore);
        }
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
     * @see
     * org.talend.repository.model.IProxyRepositoryFactory#moveObject(org.talend.core.model.repository.IRepositoryObject
     * , org.eclipse.core.runtime.IPath)
     */
    public void moveObject(IRepositoryObject objToMove, IPath targetPath, IPath... sourcePath) throws PersistenceException,
            BusinessException {
        checkAvailability(objToMove);
        // avoid to check the name, since it's only one object moved, from one folder to another one, of course the name
        // don't exist already.

        // Item item = objToMove.getProperty().getItem();
        // Project project = new Project(projectManager.getProject(item));
        // checkFileNameAndPath(project, item, RepositoryConstants.getPattern(objToMove.getType()), targetPath, false);
        this.repositoryFactoryFromProvider.moveObject(objToMove, targetPath);
        // i18n
        // log.debug("Move [" + objToMove + "] to \"" + path + "\".");
        String str[] = new String[] { objToMove + "", targetPath + "" }; //$NON-NLS-1$ //$NON-NLS-2$
        log.debug(Messages.getString("ProxyRepositoryFactory.log.move", str)); //$NON-NLS-1$
        unlock(getItem(objToMove));
        if (objToMove.getType() == ERepositoryObjectType.PROCESS) {
            if (sourcePath != null && sourcePath.length == 1) {
                fireRepositoryPropertyChange(ERepositoryActionName.JOB_MOVE.getName(), objToMove, new IPath[] { sourcePath[0],
                        targetPath });
            }
        }
        if (objToMove.getType() == ERepositoryObjectType.JOBLET) {
            if (sourcePath != null && sourcePath.length == 1) {
                fireRepositoryPropertyChange(ERepositoryActionName.JOBLET_MOVE.getName(), objToMove, new IPath[] { sourcePath[0],
                        targetPath });
            }
        }

    }

    // TODO SML Renommer et finir la m�thode et la plugger dans toutes les m�thodes
    private void checkAvailability(IRepositoryObject objToMove) throws BusinessException {
        if (!isEditableAndLockIfPossible(objToMove) || ProxyRepositoryFactory.getInstance().isUserReadOnlyOnCurrentProject()) {
            throw new BusinessException(Messages.getString("ProxyRepositoryFactory.bussinessException.itemNonModifiable")); //$NON-NLS-1$
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#getMetadataFilePositional()
     */
    public RootContainer<String, IRepositoryObject> getMetadataFilePositional() throws PersistenceException {
        return getMetadataFilePositional(projectManager.getCurrentProject());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#getMetadataFileRegexp()
     */
    public RootContainer<String, IRepositoryObject> getMetadataFileRegexp() throws PersistenceException {
        return getMetadataFileRegexp(projectManager.getCurrentProject());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#getMetadataFileXml()
     */
    public RootContainer<String, IRepositoryObject> getMetadataFileXml() throws PersistenceException {
        return getMetadataFileXml(projectManager.getCurrentProject());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#getMetadataFileLdif()
     */
    public RootContainer<String, IRepositoryObject> getMetadataFileLdif() throws PersistenceException {
        return getMetadataFileLdif(projectManager.getCurrentProject());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#getMetadataFileExcel()
     */
    public RootContainer<String, IRepositoryObject> getMetadataFileExcel() throws PersistenceException {
        return getMetadataFileExcel(projectManager.getCurrentProject());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#getMetadataSalesforceSchema()
     */
    public RootContainer<String, IRepositoryObject> getMetadataSalesforceSchema() throws PersistenceException {
        return getMetadataSalesforceSchema(projectManager.getCurrentProject());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#lock(org.talend.core.model.repository.IRepositoryObject)
     */
    public void lock(IRepositoryObject obj) throws PersistenceException {
        lock(getItem(obj));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#lock(org.talend.core.model.properties.Item)
     */
    public void lock(Item item) throws PersistenceException {
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
     * @see org.talend.repository.model.IProxyRepositoryFactory#getAllVersion(java.lang.String)
     */
    public List<IRepositoryObject> getAllVersion(String id) throws PersistenceException {
        List<IRepositoryObject> allVersion = getAllRefVersion(projectManager.getCurrentProject(), id);
        return allVersion;
    }

    public List<IRepositoryObject> getAllVersion(String id, String folderPath, ERepositoryObjectType type)
            throws PersistenceException {
        List<IRepositoryObject> allVersion = getAllRefVersion(projectManager.getCurrentProject(), id, folderPath, type);
        return allVersion;
    }

    public List<IRepositoryObject> getAllRefVersion(Project project, String id) throws PersistenceException {
        List<IRepositoryObject> allVersion = getAllVersion(project, id);
        if (allVersion.isEmpty()) {
            for (Project p : projectManager.getReferencedProjects(project)) {
                allVersion = getAllRefVersion(p, id);
                if (!allVersion.isEmpty()) {
                    break;
                }
            }
        }
        return allVersion;
    }

    public List<IRepositoryObject> getAllRefVersion(Project project, String id, String folderPath, ERepositoryObjectType type)
            throws PersistenceException {
        List<IRepositoryObject> allVersion = getAllVersion(project, id, folderPath, type);
        if (allVersion.isEmpty()) {
            for (Project p : projectManager.getReferencedProjects(project)) {
                allVersion = getAllRefVersion(p, id, folderPath, type);
                if (!allVersion.isEmpty()) {
                    break;
                }
            }
        }
        return allVersion;
    }

    public List<IRepositoryObject> getAllVersion(Project project, String id) throws PersistenceException {
        return this.repositoryFactoryFromProvider.getAllVersion(project, id);
    }

    public List<IRepositoryObject> getAllVersion(Project project, String id, String folderPath, ERepositoryObjectType type)
            throws PersistenceException {
        return this.repositoryFactoryFromProvider.getAllVersion(project, id, folderPath, type);
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
    public IRepositoryObject getLastVersion(Project project, String id) throws PersistenceException {
        return this.repositoryFactoryFromProvider.getLastVersion(project, id);
    }

    public IRepositoryObject getLastVersion(Project project, String id, String folderPath, ERepositoryObjectType type)
            throws PersistenceException {
        return this.repositoryFactoryFromProvider.getLastVersion(project, id, folderPath, type);
    }

    public IRepositoryObject getLastVersion(String id, String folderPath, ERepositoryObjectType type) throws PersistenceException {
        return this.repositoryFactoryFromProvider.getLastVersion(projectManager.getCurrentProject(), id, folderPath, type);
    }

    public IRepositoryObject getLastVersion(String id) throws PersistenceException {
        IRepositoryObject lastRefVersion = getLastRefVersion(projectManager.getCurrentProject(), id);
        return lastRefVersion;

    }

    public IRepositoryObject getLastRefVersion(Project project, String id) throws PersistenceException {
        IRepositoryObject lastVersion = getLastVersion(project, id);
        if (lastVersion == null) {
            for (Project p : projectManager.getReferencedProjects(project)) {
                lastVersion = getLastRefVersion(p, id);
                if (lastVersion != null) {
                    break;
                }
            }
        }
        return lastVersion;
    }

    public List<IRepositoryObject> getAll(Project project, ERepositoryObjectType type) throws PersistenceException {
        return getAll(project, type, false);
    }

    public List<IRepositoryObject> getAll(Project project, ERepositoryObjectType type, boolean withDeleted)
            throws PersistenceException {
        return this.repositoryFactoryFromProvider.getAll(project, type, withDeleted, false);
    }

    public List<IRepositoryObject> getAll(Project project, ERepositoryObjectType type, boolean withDeleted, boolean allVersions)
            throws PersistenceException {
        return this.repositoryFactoryFromProvider.getAll(project, type, withDeleted, allVersions);
    }

    public List<IRepositoryObject> getAll(ERepositoryObjectType type) throws PersistenceException {
        return getAll(projectManager.getCurrentProject(), type, false);
    }

    public List<IRepositoryObject> getAll(ERepositoryObjectType type, boolean withDeleted) throws PersistenceException {
        return this.repositoryFactoryFromProvider.getAll(projectManager.getCurrentProject(), type, withDeleted, false);
    }

    public List<IRepositoryObject> getAll(ERepositoryObjectType type, boolean withDeleted, boolean allVersions)
            throws PersistenceException {
        return this.repositoryFactoryFromProvider.getAll(projectManager.getCurrentProject(), type, withDeleted, allVersions);
    }

    public List<String> getFolders(ERepositoryObjectType type) throws PersistenceException {
        return getFolders(projectManager.getCurrentProject(), type);
    }

    public List<String> getFolders(Project project, ERepositoryObjectType type) throws PersistenceException {
        List<String> toReturn = new ArrayList<String>();
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
            // for bug 9352: .svnlog folder should not be visible in wizards
            EObject obj = source.eContainer();
            if (obj != null && obj instanceof FolderItemImpl) {
                target.add(path + source.getProperty().getLabel());
                for (Object current : source.getChildren()) {
                    if (current instanceof FolderItem) {
                        addChildren(target, (FolderItem) current, type, path + source.getProperty().getLabel() + "/"); //$NON-NLS-1$
                    }
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
     * @see org.talend.repository.model.IProxyRepositoryFactory#getTechnicalStatus()
     */
    // public List<SpagoBiServer> getSpagoBiServer() throws PersistenceException {
    // return this.repositoryFactoryFromProvider.getSpagoBiServer();
    // }
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
     * @see org.talend.repository.model.IProxyRepositoryFactory#forceCreate(org.talend.core.model.properties.Item,
     * org.eclipse.core.runtime.IPath)
     */
    public void forceCreate(Item item, IPath path) throws PersistenceException {
        forceCreate(projectManager.getCurrentProject(), item, path);
        // if (item instanceof ProcessItem) {
        // fireRepositoryPropertyChange(ERepositoryActionName.JOB_CREATE.getName(), null, item);
        // }
    }

    public void forceCreate(Project project, Item item, IPath path) throws PersistenceException {
        this.repositoryFactoryFromProvider.create(project, item, path);
    }

    public void createParentFoldersRecursively(ERepositoryObjectType itemType, IPath path) throws PersistenceException {
        createParentFoldersRecursively(projectManager.getCurrentProject(), itemType, path);
    }

    public void createParentFoldersRecursively(Project project, ERepositoryObjectType itemType, IPath path)
            throws PersistenceException {
        List<String> folders = getFolders(project, itemType);

        for (int i = 0; i < path.segmentCount(); i++) {
            IPath parentPath = path.removeLastSegments(path.segmentCount() - i);
            String folderLabel = path.segment(i);

            String folderName = parentPath.append(folderLabel).toString();
            if (!folders.contains(folderName)) {
                createFolder(project, itemType, parentPath, folderLabel);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#setTechnicalStatus(java.util.List)
     */
    public void setTechnicalStatus(List<Status> list) throws PersistenceException {
        this.repositoryFactoryFromProvider.setTechnicalStatus(list);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#setSpagoBiServer(java.util.List)
     */
    public void setSpagoBiServer(List<SpagoBiServer> list) throws PersistenceException {
        this.repositoryFactoryFromProvider.setSpagoBiServer(list);
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
    public void create(Item item, IPath path, boolean... isImportItem) throws PersistenceException {
        create(projectManager.getCurrentProject(), item, path, isImportItem);
    }

    public void create(Project project, Item item, IPath path, boolean... isImportItem) throws PersistenceException {
        if (item instanceof ProcessItem) {
            try {
                IJobCheckService jobCheckService = (IJobCheckService) GlobalServiceRegister.getDefault().getService(
                        IJobCheckService.class);
                if (jobCheckService != null) {
                    jobCheckService.checkJob(item.getProperty().getLabel());
                }
            } catch (BusinessException e) {
                throw new PersistenceException(e);
            } catch (RuntimeException e) {
                // don't do anything
            }
        }
        checkFileNameAndPath(project, item, RepositoryConstants.getPattern(ERepositoryObjectType.getItemType(item)), path, false,
                isImportItem);
        this.repositoryFactoryFromProvider.create(project, item, path);
        if ((item instanceof ProcessItem || item instanceof JobletProcessItem) && (isImportItem.length == 0)) {
            fireRepositoryPropertyChange(ERepositoryActionName.JOB_CREATE.getName(), null, item);
        }
        if (isImportItem.length == 1) {
            this.repositoryFactoryFromProvider.unloadResources(item.getProperty());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#save(org.talend.core.model.properties.Item)
     */
    public void save(Item item, boolean... isMigrationTask) throws PersistenceException {
        save(projectManager.getCurrentProject(), item, isMigrationTask);
    }

    public void save(Project project, Item item, boolean... isMigrationTask) throws PersistenceException {
        this.repositoryFactoryFromProvider.save(project, item);
        if ((item instanceof ProcessItem || item instanceof JobletProcessItem)
                && (isMigrationTask == null || isMigrationTask.length == 0)) {
            fireRepositoryPropertyChange(ERepositoryActionName.JOB_SAVE.getName(), null, item);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#save(org.talend.core.model.properties.Property)
     */
    public void save(Property property, String... originalNameAndVersion) throws PersistenceException {
        save(projectManager.getCurrentProject(), property, originalNameAndVersion);
    }

    public void save(Project project, Property property, String... originalNameAndVersion) throws PersistenceException {
        this.repositoryFactoryFromProvider.save(project, property);
        if (property.getItem() instanceof ProcessItem || property.getItem() instanceof JobletProcessItem) {
            fireRepositoryPropertyChange(ERepositoryActionName.JOB_PROPERTIES_CHANGE.getName(), originalNameAndVersion, property);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#copy(org.talend.core.model.properties.Item,
     * org.eclipse.core.runtime.IPath)
     */
    public Item copy(Item sourceItem, IPath targetPath) throws PersistenceException, BusinessException {
        if (sourceItem instanceof ProcessItem) {
            try {
                IJobCheckService jobCheckService = (IJobCheckService) GlobalServiceRegister.getDefault().getService(
                        IJobCheckService.class);
                if (jobCheckService != null) {
                    jobCheckService.checkJob(sourceItem.getProperty().getLabel());
                }
            } catch (BusinessException e) {
                throw new PersistenceException(e);
            } catch (RuntimeException e) {
                // don't do anything
            }
        }

        Item targetItem = this.repositoryFactoryFromProvider.copy(sourceItem, targetPath);

        if (sourceItem instanceof ProcessItem || sourceItem instanceof JobletProcessItem) {
            fireRepositoryPropertyChange(ERepositoryActionName.JOB_COPY.getName(), sourceItem, targetItem);
        }
        return targetItem;

    }

    public Item copy(Item sourceItem, IPath targetPath, boolean changeLabelWithCopyPrefix) throws PersistenceException,
            BusinessException {

        if (sourceItem instanceof ProcessItem) {
            try {
                IJobCheckService jobCheckService = (IJobCheckService) GlobalServiceRegister.getDefault().getService(
                        IJobCheckService.class);
                if (jobCheckService != null) {
                    jobCheckService.checkJob(sourceItem.getProperty().getLabel());
                }
            } catch (BusinessException e) {
                throw new PersistenceException(e);
            } catch (RuntimeException e) {
                // don't do anything
            }
        }
        Item targetItem = this.repositoryFactoryFromProvider.copy(sourceItem, targetPath, changeLabelWithCopyPrefix);
        // if ((sourceItem instanceof ProcessItem || sourceItem instanceof JobletProcessItem)) {
        // fireRepositoryPropertyChange(ERepositoryActionName.JOB_COPY.getName(), sourceItem, targetItem);
        // }
        return targetItem;

    }

    public void saveCopy(Item sourceItem, Item targetItem) {
        fireRepositoryPropertyChange(ERepositoryActionName.JOB_COPY.getName(), sourceItem, targetItem);
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
     * @see
     * org.talend.repository.model.IProxyRepositoryFactory#unlock(org.talend.core.model.repository.IRepositoryObject)
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
        if (getStatus(obj) == ERepositoryStatus.LOCK_BY_USER || obj instanceof JobletDocumentationItem
                || obj instanceof JobDocumentationItem) {
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
    public void initialize() throws PersistenceException {
        this.repositoryFactoryFromProvider.initialize();
    }

    /**
     * DOC smallet Comment method "emptyTempFolder".
     * 
     * @param project
     * @throws PersistenceException
     */
    private void emptyTempFolder(Project project) throws PersistenceException {
        long start = System.currentTimeMillis();
        IProject fsProject = ResourceModelUtils.getProject(project);
        IFolder folder = ResourceUtils.getFolder(fsProject, RepositoryConstants.TEMP_DIRECTORY, true);
        int nbResourcesDeleted = ResourceUtils.emptyFolder(folder);
        long elapsedTime = System.currentTimeMillis() - start;
        log.trace(Messages.getString("ProxyRepositoryFactory.log.tempFolderEmptied", nbResourcesDeleted, elapsedTime)); //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryFactory#getStatus(org.talend.core.model.properties.Item)
     */
    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.model.IProxyRepositoryFactory#getStatus(org.talend.core.model.repository.IRepositoryObject)
     */
    public ERepositoryStatus getStatus(IRepositoryObject obj) {
        if (obj instanceof ISubRepositoryObject) {
            ISubRepositoryObject subRepositoryObject = (ISubRepositoryObject) obj;
            if (SubItemHelper.isDeleted(subRepositoryObject.getAbstractMetadataObject())) {
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

    public boolean isUserReadOnlyOnCurrentProject() {
        return this.repositoryFactoryFromProvider.isUserReadOnlyOnCurrentProject();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#getStatus(org.talend.core.model.properties.Item)
     */
    public ERepositoryStatus getStatus(Item item) {
        // PTODO SML [FOLDERS] temp code
        ERepositoryStatus toReturn;
        if (item instanceof FolderItem) {
            toReturn = ERepositoryStatus.EDITABLE;
        } else {
            toReturn = this.repositoryFactoryFromProvider.getStatus(item);
        }

        if (toReturn != ERepositoryStatus.DELETED
                && (isUserReadOnlyOnCurrentProject() || !projectManager.isInCurrentMainProject(item))) {
            return ERepositoryStatus.READ_ONLY;
        }

        return toReturn;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.model.IProxyRepositoryFactory#getStatus(org.talend.core.model.properties.InformationLevel)
     */
    public ERepositoryStatus getStatus(InformationLevel level) {

        if (level.getValue() == InformationLevel.WARN) {
            return ERepositoryStatus.WARN;
        } else if (level.getValue() == InformationLevel.ERROR) {
            return ERepositoryStatus.ERROR;
        }
        return ERepositoryStatus.DEFAULT;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.model.IRepositoryFactory#getStatusAndLockIfPossible(org.talend.core.model.properties.Item)
     */
    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.model.IProxyRepositoryFactory#isEditableAndLockIfPossible(org.talend.core.model.properties
     * .Item)
     */
    public boolean isEditableAndLockIfPossible(Item item) {
        if (!projectManager.isInCurrentMainProject(item)) {
            return false;
        }

        ERepositoryStatus status = getStatus(item);
        if (status.isPotentiallyEditable()) {
            try {
                lock(item);
            } catch (PersistenceException e) {
                MessageBoxExceptionHandler.process(e);
            }
            status = getStatus(item);
        }

        return status.isEditable();
    }

    // public boolean isLastVersion(Item item) {
    // if (item.getProperty() != null) {
    // try {
    // List<IRepositoryObject> allVersion = ProxyRepositoryFactory.getInstance().getAllVersion(
    // item.getProperty().getId());
    // if (allVersion != null && !allVersion.isEmpty()) {
    // if (allVersion.get(allVersion.size() - 1).getVersion().equals(item.getProperty().getVersion())) {
    // return true;
    // }
    // }
    // } catch (PersistenceException e) {
    // ExceptionHandler.process(e);
    // }
    // }
    // return false;
    // }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.model.IRepositoryFactory#isEditable(org.talend.core.model.repository.IRepositoryObject)
     */
    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.model.IProxyRepositoryFactory#isEditableAndLockIfPossible(org.talend.core.model.repository
     * .IRepositoryObject)
     */
    public boolean isEditableAndLockIfPossible(IRepositoryObject obj) {
        if (obj instanceof ISubRepositoryObject) {
            AbstractMetadataObject abstractMetadataObject = ((ISubRepositoryObject) obj).getAbstractMetadataObject();
            if (SubItemHelper.isDeleted(abstractMetadataObject)) {
                return false;
            } else {
                return isEditableAndLockIfPossible(getItem(obj));
            }
        } else {
            return isEditableAndLockIfPossible(getItem(obj));
        }
    }

    public org.talend.core.model.properties.Project getProject(Item item) {
        EObject object = EcoreUtil.getRootContainer(item);
        if (object != null && object instanceof org.talend.core.model.properties.Project) {
            return (org.talend.core.model.properties.Project) object;
        }
        return projectManager.getCurrentProject().getEmfProject();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryFactory#isPotentiallyEditable(org.talend.core.model.properties.Item)
     */
    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.model.IProxyRepositoryFactory#isPotentiallyEditable(org.talend.core.model.properties.Item)
     */
    private boolean isPotentiallyEditable(Item item) {
        if (!projectManager.isInCurrentMainProject(item)) {
            return false;
        }

        ERepositoryStatus status = getStatus(item);
        return status.isPotentiallyEditable();
    }

    /*
     * (non-Javadoc)
     * 
     * @seeorg.talend.repository.model.IRepositoryFactory#isPotentiallyEditable(org.talend.core.model.repository.
     * IRepositoryObject)
     */
    /*
     * (non-Javadoc)
     * 
     * @seeorg.talend.repository.model.IProxyRepositoryFactory#isPotentiallyEditable(org.talend.core.model.repository.
     * IRepositoryObject)
     */
    public boolean isPotentiallyEditable(IRepositoryObject obj) {
        if (obj instanceof ISubRepositoryObject) {
            AbstractMetadataObject abstractMetadataObject = ((ISubRepositoryObject) obj).getAbstractMetadataObject();
            if (SubItemHelper.isDeleted(abstractMetadataObject)) {
                return false;
            } else {
                return isPotentiallyEditable(getItem(obj));
            }
        } else {
            return isPotentiallyEditable(getItem(obj));
        }
    }

    private Item getItem(IRepositoryObject obj) {
        return obj.getProperty().getItem();
    }

    public List<org.talend.core.model.properties.Project> getReferencedProjects() {
        return this.repositoryFactoryFromProvider.getReferencedProjects();
    }

    public void removeContextFiles(IProcess process, IContext context) throws Exception {
        IResource resource = getContextResource(process, context);
        if (resource != null) {
            resource.delete(true, null);
        }
    }

    /**
     * Gets the context file resource according to the project type.
     * 
     * @param process
     * @param context
     * @return
     */
    private IResource getContextResource(IProcess process, IContext context) throws Exception {
        switch (ProjectManager.getInstance().getCurrentProject().getLanguage()) {
        case JAVA:
            IPath path = new Path(JavaUtils.JAVA_SRC_DIRECTORY).append(
                    JavaResourcesHelper.getProjectFolderName(process.getProperty().getItem())).append(
                    JavaResourcesHelper.getJobFolderName(process.getName(), process.getVersion())).append(
                    JobJavaScriptsManager.JOB_CONTEXT_FOLDER).append(context.getName() + JavaUtils.JAVA_CONTEXT_EXTENSION);
            return JavaResourcesHelper.getSpecificResourceInJavaProject(path);
        case PERL:
            String rootProjectName = PerlResourcesHelper.getRootProjectName(process.getProperty().getItem());
            String contextFullName = PerlResourcesHelper.getContextFileName(rootProjectName, process.getName(), process
                    .getVersion(), context.getName());
            return PerlResourcesHelper.getSpecificResourceInPerlProject(new Path(contextFullName));
        }
        return null;
    }

    public Boolean hasChildren(Object parent) {
        return repositoryFactoryFromProvider.hasChildren(parent);
    }

    public RootContainer<String, IRepositoryObject> getMetadataGenericSchema() throws PersistenceException {
        return getMetadataGenericSchema(projectManager.getCurrentProject());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#getMetadataLDAPSchema()
     */
    public RootContainer<String, IRepositoryObject> getMetadataLDAPSchema() throws PersistenceException {
        return getMetadataLDAPSchema(projectManager.getCurrentProject());
    }

    public synchronized List<ModuleNeeded> getModulesNeededForJobs() throws PersistenceException {
        return this.repositoryFactoryFromProvider.getModulesNeededForJobs();
    }

    public RootContainer<String, IRepositoryObject> getJoblets() throws PersistenceException {
        return getJoblets(projectManager.getCurrentProject());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#getMetadataWSDLSchema()
     */
    public RootContainer<String, IRepositoryObject> getMetadataWSDLSchema() throws PersistenceException {
        return getMetadataWSDLSchema(projectManager.getCurrentProject());
    }

    /**
     * DOC tang Comment method "logOnProject".
     * 
     * @param project
     * @param monitorWrap
     * @throws PersistenceException
     * @throws LoginException
     */
    public void logOnProject(Project project, IProgressMonitor monitorWrap) throws LoginException, PersistenceException {
        try {
            monitorWrap.beginTask(Messages.getString("ProxyRepositoryFactory.logonInProgress"), MAX_TASKS); //$NON-NLS-1$
            LanguageManager.reset();
            getRepositoryContext().setProject(project);

            monitorWrap.subTask(Messages.getString("ProxyRepositoryFactory.initializeProjectConnection")); //$NON-NLS-1$
            this.repositoryFactoryFromProvider.beforeLogon(project);
            monitorWrap.worked(1);

            monitorWrap.subTask("Execute before logon migrations tasks"); //$NON-NLS-1$
            IMigrationToolService service = (IMigrationToolService) GlobalServiceRegister.getDefault().getService(
                    IMigrationToolService.class);
            service.executeProjectTasks(project, true, monitorWrap);
            monitorWrap.worked(1);

            monitorWrap.subTask(Messages.getString("ProxyRepositoryFactory.logonInProgress")); //$NON-NLS-1$
            this.repositoryFactoryFromProvider.logOnProject(project);
            monitorWrap.worked(1);

            emptyTempFolder(project);

            // i18n
            // log.info(getRepositoryContext().getUser() + " logged on " + projectManager.getCurrentProject());
            String str[] = new String[] { getRepositoryContext().getUser() + "", projectManager.getCurrentProject() + "" }; //$NON-NLS-1$ //$NON-NLS-2$        
            log.info(Messages.getString("ProxyRepositoryFactory.log.loggedOn", str)); //$NON-NLS-1$

            monitorWrap.subTask("Load Components..."); //$NON-NLS-1$
            ComponentsFactoryProvider.getInstance().reset();
            ComponentsFactoryProvider.getInstance().initializeComponents(monitorWrap);
            monitorWrap.worked(1);

            monitorWrap.subTask("Execute migrations tasks"); //$NON-NLS-1$
            service.executeProjectTasks(project, false, monitorWrap);
            monitorWrap.worked(1);

            // clean workspace
            IRunProcessService runProcessService = (IRunProcessService) GlobalServiceRegister.getDefault().getService(
                    IRunProcessService.class);
            runProcessService.deleteAllJobs(false);

            monitorWrap.subTask(Messages.getString("ProxyRepositoryFactory.synchronizeLibraries")); //$NON-NLS-1$
            CorePlugin.getDefault().getLibrariesService().syncLibraries(monitorWrap);
            monitorWrap.worked(1);

            monitorWrap.subTask("Synchronize repository items"); //$NON-NLS-1$
            ICodeGeneratorService codeGenService = (ICodeGeneratorService) GlobalServiceRegister.getDefault().getService(
                    ICodeGeneratorService.class);
            try {
                codeGenService.createRoutineSynchronizer().syncAllRoutines();
            } catch (SystemException e1) {
                //
            }
            // rules
            if (PluginChecker.isRulesPluginLoaded()) {
                IRulesProviderService rulesService = (IRulesProviderService) GlobalServiceRegister.getDefault().getService(
                        IRulesProviderService.class);
                if (rulesService != null) {
                    rulesService.syncAllRules();
                }
            }
            monitorWrap.worked(1);
            if (!CommonsPlugin.isHeadless()) {
                CorePlugin.getDefault().getCodeGeneratorService().initializeTemplates();
            }

            // remove the auto-build to enhance the build speed and application's use
            IWorkspace workspace = ResourcesPlugin.getWorkspace();
            IWorkspaceDescription description = workspace.getDescription();
            description.setAutoBuilding(false);
            try {
                workspace.setDescription(description);
            } catch (CoreException e) {
                // do nothing
            }
            IDesignerCoreService designerCoreService = CorePlugin.getDefault().getDesignerCoreService();
            if (designerCoreService != null) {
                designerCoreService.createStatsLogAndImplicitParamter(project);
            }

        } catch (LoginException e) {
            logOffProject();
            throw e;
        } catch (PersistenceException e) {
            logOffProject();
            throw e;
        } catch (RuntimeException e) {
            logOffProject();
            throw e;
        }
    }

    public void logOffProject() {
        // getRepositoryContext().setProject(null);
        repositoryFactoryFromProvider.logOffProject();
    }

    public boolean setAuthorByLogin(Item item, String login) throws PersistenceException {
        return repositoryFactoryFromProvider.setAuthorByLogin(item, login);
    }

    public Property getUptodateProperty(Project project, Property property) throws PersistenceException {
        return repositoryFactoryFromProvider.getUptodateProperty(project, property);
    }

    public Property getUptodateProperty(Property property) throws PersistenceException {
        return getUptodateProperty(projectManager.getCurrentProject(), property);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.model.IProxyRepositoryFactory#getBusinessProcess(org.talend.core.model.general.Project)
     */
    public RootContainer<String, IRepositoryObject> getBusinessProcess(Project project) throws PersistenceException {
        return this.repositoryFactoryFromProvider.getBusinessProcess(project);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#getContext(org.talend.core.model.general.Project)
     */
    public RootContainer<String, IRepositoryObject> getContext(Project project) throws PersistenceException {
        return this.repositoryFactoryFromProvider.getContext(project);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#getDocumentation(org.talend.core.model.general.Project)
     */
    public RootContainer<String, IRepositoryObject> getDocumentation(Project project) throws PersistenceException {
        return this.repositoryFactoryFromProvider.getDocumentation(project);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#getJoblets(org.talend.core.model.general.Project)
     */
    public RootContainer<String, IRepositoryObject> getJoblets(Project project) throws PersistenceException {
        return this.repositoryFactoryFromProvider.getJoblets(project);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.model.IProxyRepositoryFactory#getMetadataConnection(org.talend.core.model.general.Project)
     */
    public RootContainer<String, IRepositoryObject> getMetadataConnection(Project project) throws PersistenceException {
        return this.repositoryFactoryFromProvider.getMetadataConnection(project);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.model.IProxyRepositoryFactory#getMetadataFileDelimited(org.talend.core.model.general.Project
     * )
     */
    public RootContainer<String, IRepositoryObject> getMetadataFileDelimited(Project project) throws PersistenceException {
        return this.repositoryFactoryFromProvider.getMetadataFileDelimited(project);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.model.IProxyRepositoryFactory#getMetadataFileExcel(org.talend.core.model.general.Project)
     */
    public RootContainer<String, IRepositoryObject> getMetadataFileExcel(Project project) throws PersistenceException {
        return this.repositoryFactoryFromProvider.getMetadataFileExcel(project);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.model.IProxyRepositoryFactory#getMetadataFileLdif(org.talend.core.model.general.Project)
     */
    public RootContainer<String, IRepositoryObject> getMetadataFileLdif(Project project) throws PersistenceException {
        return this.repositoryFactoryFromProvider.getMetadataFileLdif(project);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.model.IProxyRepositoryFactory#getMetadataFilePositional(org.talend.core.model.general.Project
     * )
     */
    public RootContainer<String, IRepositoryObject> getMetadataFilePositional(Project project) throws PersistenceException {
        return this.repositoryFactoryFromProvider.getMetadataFilePositional(project);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.model.IProxyRepositoryFactory#getMetadataFileRegexp(org.talend.core.model.general.Project)
     */
    public RootContainer<String, IRepositoryObject> getMetadataFileRegexp(Project project) throws PersistenceException {
        return this.repositoryFactoryFromProvider.getMetadataFileRegexp(project);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.model.IProxyRepositoryFactory#getMetadataFileXml(org.talend.core.model.general.Project)
     */
    public RootContainer<String, IRepositoryObject> getMetadataFileXml(Project project) throws PersistenceException {
        return this.repositoryFactoryFromProvider.getMetadataFileXml(project);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.model.IProxyRepositoryFactory#getMetadataGenericSchema(org.talend.core.model.general.Project
     * )
     */
    public RootContainer<String, IRepositoryObject> getMetadataGenericSchema(Project project) throws PersistenceException {
        return this.repositoryFactoryFromProvider.getMetadataGenericSchema(project);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.model.IProxyRepositoryFactory#getMetadataLDAPSchema(org.talend.core.model.general.Project)
     */
    public RootContainer<String, IRepositoryObject> getMetadataLDAPSchema(Project project) throws PersistenceException {
        return this.repositoryFactoryFromProvider.getMetadataLDAPSchema(project);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.model.IProxyRepositoryFactory#getMetadataSQLPattern(org.talend.core.model.general.Project)
     */
    public RootContainer<String, IRepositoryObject> getMetadataSQLPattern(Project project) throws PersistenceException {
        return this.repositoryFactoryFromProvider.getMetadataSQLPattern(project);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.model.IProxyRepositoryFactory#getMetadataSalesforceSchema(org.talend.core.model.general
     * .Project)
     */
    public RootContainer<String, IRepositoryObject> getMetadataSalesforceSchema(Project project) throws PersistenceException {
        return this.repositoryFactoryFromProvider.getMetadataSalesforceSchema(project);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.model.IProxyRepositoryFactory#getMetadataWSDLSchema(org.talend.core.model.general.Project)
     */
    public RootContainer<String, IRepositoryObject> getMetadataWSDLSchema(Project project) throws PersistenceException {
        return this.repositoryFactoryFromProvider.getMetadataWSDLSchema(project);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#getProcess(org.talend.core.model.general.Project)
     */
    public RootContainer<String, IRepositoryObject> getProcess(Project project) throws PersistenceException {
        return this.repositoryFactoryFromProvider.getProcess(project);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.model.IProxyRepositoryFactory#getRecycleBinItems(org.talend.core.model.general.Project)
     */
    public List<IRepositoryObject> getRecycleBinItems(Project project) throws PersistenceException {
        return this.repositoryFactoryFromProvider.getRecycleBinItems(project);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#getRoutine(org.talend.core.model.general.Project)
     */
    public RootContainer<String, IRepositoryObject> getRoutine(Project project) throws PersistenceException {
        return this.repositoryFactoryFromProvider.getRoutine(project);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#getSnippets(org.talend.core.model.general.Project)
     */
    public RootContainer<String, IRepositoryObject> getSnippets(Project project) throws PersistenceException {
        return this.repositoryFactoryFromProvider.getSnippets(project);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.model.IProxyRepositoryFactory#getSpecificVersion(org.talend.core.model.general.Project,
     * java.lang.String, java.lang.String)
     */
    public IRepositoryObject getSpecificVersion(Project project, String id, String version) throws PersistenceException {
        List<IRepositoryObject> objList = getAllVersion(project, id);
        for (IRepositoryObject obj : objList) {
            if (obj.getVersion().equals(version)) {
                return obj;
            }
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#getSpecificVersion(java.lang.String, java.lang.String)
     */
    public IRepositoryObject getSpecificVersion(String id, String version) throws PersistenceException {
        return getSpecificVersion(projectManager.getCurrentProject(), id, version);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.model.IProxyRepositoryFactory#getMetadataSAPConnection(org.talend.core.model.general.Project
     * )
     */
    public RootContainer<String, IRepositoryObject> getMetadataSAPConnection(Project project) throws PersistenceException {
        // TODO Auto-generated method stub
        return this.repositoryFactoryFromProvider.getMetadataSAPConnection(project);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#getMetadataSAPConnection()
     */
    public RootContainer<String, IRepositoryObject> getMetadataSAPConnection() throws PersistenceException {
        return getMetadataSAPConnection(projectManager.getCurrentProject());
    }

    public void checkAvailability() throws PersistenceException {
        this.repositoryFactoryFromProvider.checkAvailability();
    }

    @SuppressWarnings("unchecked")
    public void executeRepositoryWorkUnit(RepositoryWorkUnit workUnit) {
        this.repositoryFactoryFromProvider.executeRepositoryWorkUnit(workUnit);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.model.IProxyRepositoryFactory#getMetadataEbcdicConnection(org.talend.core.model.general
     * .Project)
     */
    public RootContainer<String, IRepositoryObject> getMetadataEBCDIC(Project project) throws PersistenceException {
        return this.repositoryFactoryFromProvider.getMetadataEBCDIC(project);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#getMetadataEbcdicConnection()
     */
    public RootContainer<String, IRepositoryObject> getMetadataEBCDIC() throws PersistenceException {
        return getMetadataEBCDIC(projectManager.getCurrentProject());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#getMetadataRules(org.talend.core.model.general.Project)
     */
    public RootContainer<String, IRepositoryObject> getMetadataRules(Project project) throws PersistenceException {
        return repositoryFactoryFromProvider.getMetadataRules(project);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.model.IProxyRepositoryFactory#getSVGBusinessProcess(org.talend.core.model.general.Project)
     */
    public RootContainer<String, IRepositoryObject> getSVGBusinessProcess(Project project) throws PersistenceException {
        // TODO Auto-generated method stub
        return this.repositoryFactoryFromProvider.getSVGBusinessProcess(project);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#getSVGBusinessProcess()
     */
    public RootContainer<String, IRepositoryObject> getSVGBusinessProcess() throws PersistenceException {
        // TODO Auto-generated method stub
        return getSVGBusinessProcess(projectManager.getCurrentProject());
    }

    public void unloadResources(Property property) throws PersistenceException {
        repositoryFactoryFromProvider.unloadResources(property);
    }

    /**
     * 
     * DOC mzhao Comment method "unloadResources".
     * 
     * @param uriString
     * @throws PersistenceException
     */
    public void unloadResources(String uriString) throws PersistenceException {
        repositoryFactoryFromProvider.unloadResources(uriString);
    }

    public void unloadResources() throws PersistenceException {
        repositoryFactoryFromProvider.unloadResources();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#getMetadataMDM()
     */
    public RootContainer<String, IRepositoryObject> getMetadataMDM() throws PersistenceException {
        return getMetadataMDM(projectManager.getCurrentProject());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#getMetadataMDM(org.talend.core.model.general.Project)
     */
    public RootContainer<String, IRepositoryObject> getMetadataMDM(Project project) throws PersistenceException {
        return this.repositoryFactoryFromProvider.getMetadataMDM(project);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#getMetadataHL7(org.talend.core.model.general.Project)
     */
    public RootContainer<String, IRepositoryObject> getMetadataHL7(Project project) throws PersistenceException {
        return this.repositoryFactoryFromProvider.getMetadataHL7(project);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#getMetadataHL7()
     */
    public RootContainer<String, IRepositoryObject> getMetadataHL7() throws PersistenceException {
        return getMetadataHL7(projectManager.getCurrentProject());
    }
}
