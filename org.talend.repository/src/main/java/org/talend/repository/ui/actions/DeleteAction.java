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
package org.talend.repository.ui.actions;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.map.MultiKeyMap;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.MessageBoxExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.image.EImage;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.components.ComponentUtilities;
import org.talend.core.model.general.Project;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.SubscriberTable;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.properties.FolderItem;
import org.talend.core.model.properties.FolderType;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.JobletProcessItem;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.ProjectReference;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.repository.RepositoryManager;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.expressionbuilder.ExpressionPersistance;
import org.talend.repository.ProjectManager;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.ERepositoryStatus;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.JobletReferenceBean;
import org.talend.repository.model.MetadataTableRepositoryObject;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.RepositoryConstants;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNodeUtilities;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.IRepositoryNode.EProperties;
import org.talend.repository.ui.actions.metadata.DeleteTableAction;
import org.talend.repository.ui.dialog.JobletReferenceDialog;

/**
 * Action used to delete object from repository. This action manages logical and physical deletions.<br/>
 * 
 * $Id$
 * 
 */
public class DeleteAction extends AContextualAction {

    private static DeleteAction singleton;

    private static final String DELETE_LOGICAL_TITLE = Messages.getString("DeleteAction.action.logicalTitle"); //$NON-NLS-1$

    private static final String DELETE_FOREVER_TITLE = Messages.getString("DeleteAction.action.foreverTitle"); //$NON-NLS-1$

    private static final String DELETE_LOGICAL_TOOLTIP = Messages.getString("DeleteAction.action.logicalToolTipText"); //$NON-NLS-1$

    private static final String DELETE_FOREVER_TOOLTIP = Messages.getString("DeleteAction.action.logicalToolTipText"); //$NON-NLS-1$

    public DeleteAction() {
        super();
        setId(ActionFactory.DELETE.getId());
        this.setImageDescriptor(ImageProvider.getImageDesc(EImage.DELETE_ICON));
        //        this.setActionDefinitionId("deleteItem"); //$NON-NLS-1$
        singleton = this;
    }

    public static DeleteAction getInstance() {
        return singleton;
    }

    @Override
    protected void doRun() {
        ISelection selection = getSelection();
        IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
        final DeleteActionCache deleteActionCache = DeleteActionCache.getInstance();
        deleteActionCache.setGetAlways(false);
        deleteActionCache.setDocRefresh(false);
        deleteActionCache.createRecords();

        boolean needToUpdataPalette = false;
        final Set<ERepositoryObjectType> types = new HashSet<ERepositoryObjectType>();
        for (Object obj : ((IStructuredSelection) selection).toArray()) {
            if (obj instanceof RepositoryNode) {
                RepositoryNode node = (RepositoryNode) obj;
                try {

                    if (isForbidNode(node)) {
                        continue;
                    }

                    if (node.getType() == ENodeType.REPOSITORY_ELEMENT) {
                        boolean needReturn = deleteElements(factory, deleteActionCache, node);
                        if (node.getProperties(EProperties.CONTENT_TYPE) == ERepositoryObjectType.JOBLET) {
                            needToUpdataPalette = true;
                        }
                        if (needReturn) {
                            return;
                        }
                        types.add(node.getObjectType());
                    } else if (node.getType() == ENodeType.SIMPLE_FOLDER) {

                        types.add(node.getContentType());
                        // fixed for the documentation deleted
                        if (node.getContentType() == ERepositoryObjectType.PROCESS
                                || node.getContentType() == ERepositoryObjectType.JOBLET) {
                            types.add(ERepositoryObjectType.DOCUMENTATION);
                        }

                        deleteFolder(node, factory, deleteActionCache);
                    }
                } catch (PersistenceException e) {
                    MessageBoxExceptionHandler.process(e);
                } catch (BusinessException e) {
                    MessageBoxExceptionHandler.process(e);
                }
            }
        }
        try {
            factory.saveProject(ProjectManager.getInstance().getCurrentProject());
        } catch (PersistenceException e) {
            ExceptionHandler.process(e);
        }

        final boolean updatePalette = needToUpdataPalette;
        Display.getCurrent().syncExec(new Runnable() {

            public void run() {
                if (updatePalette) {
                    ComponentUtilities.updatePalette();
                }
                RepositoryManager.getRepositoryView().refresh();
                IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
                for (IEditorReference editors : page.getEditorReferences()) {
                    CorePlugin.getDefault().getDiagramModelService().refreshBusinessModel(editors);
                }

                if (!deleteActionCache.isDocRefresh()) { // not refresh in JobDeleteListener
                    RepositoryManager.refreshCreatedNode(ERepositoryObjectType.DOCUMENTATION);
                }
                deleteActionCache.revertParameters();
            }
        });

    }

    /**
     * DOC qwei Comment method "deleteFolder".
     * 
     * @param deleteActionCache
     */
    private void deleteFolder(final RepositoryNode node, final IProxyRepositoryFactory factory,
            final DeleteActionCache deleteActionCache) {
        FolderItem folderItem = (FolderItem) node.getObject().getProperty().getItem();
        try {
            if (folderItem.getState().isDeleted()) {
                // if folder has been deleted already
                deleteElements(factory, deleteActionCache, node);
                return;
            }
            IRunnableWithProgress op = new IRunnableWithProgress() {

                public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                    try {
                        monitor.beginTask("Delete Running", 100); //$NON-NLS-1$
                        IPath path = RepositoryNodeUtilities.getPath(node);
                        ERepositoryObjectType objectType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
                        List<IRepositoryNode> repositoryList = node.getChildren();
                        monitor.worked(10);
                        int taskTotal = repositoryList.size();
                        for (IRepositoryNode repositoryNode : repositoryList) {
                            deleteRepositoryNode(repositoryNode, factory);
                            monitor.worked(1 * 100 / taskTotal);
                        }

                        FolderItem folderItem = factory.getFolderItem(ProjectManager.getInstance().getCurrentProject(),
                                objectType, path);
                        folderItem.getState().setDeleted(true);

                        String fullPath = "";
                        FolderItem curItem = folderItem;

                        while (curItem.getParent() instanceof FolderItem
                                && ((Item) curItem.getParent()).getParent() instanceof FolderItem
                                && ((FolderItem) ((Item) curItem.getParent()).getParent()).getType().getValue() == FolderType.FOLDER) {
                            FolderItem parentFolder = (FolderItem) curItem.getParent();
                            if ("".equals(fullPath)) {
                                fullPath = parentFolder.getProperty().getLabel() + fullPath;
                            } else {
                                fullPath = parentFolder.getProperty().getLabel() + "/" + fullPath;
                            }
                            curItem = parentFolder;
                        }
                        if (!objectType.getKey().toString().startsWith("repository.metadata")
                                && objectType != ERepositoryObjectType.SQLPATTERNS
                                && objectType != ERepositoryObjectType.ROUTINES && curItem.getParent() instanceof FolderItem
                                && ((Item) curItem.getParent()).getParent() instanceof FolderItem) {
                            FolderItem parentFolder = (FolderItem) curItem.getParent();
                            if ("".equals(fullPath)) {
                                fullPath = parentFolder.getProperty().getLabel() + fullPath;
                            } else {
                                fullPath = parentFolder.getProperty().getLabel() + "/" + fullPath;
                            }
                            curItem = parentFolder;
                        }
                        if (objectType.getKey().toString().startsWith("repository.metadata")) {
                            while (((FolderItem) curItem.getParent()).getType().getValue() != FolderType.SYSTEM_FOLDER) {
                                if ("".equals(fullPath)) {
                                    fullPath = ((FolderItem) curItem.getParent()).getProperty().getLabel() + fullPath;
                                } else {
                                    fullPath = ((FolderItem) curItem.getParent()).getProperty().getLabel() + "/" + fullPath;
                                }
                                curItem = (FolderItem) curItem.getParent();
                            }
                        }
                        if (objectType == ERepositoryObjectType.ROUTINES) {
                            while (((FolderItem) curItem.getParent()).getType().getValue() != FolderType.SYSTEM_FOLDER) {
                                if ("".equals(fullPath)) {
                                    fullPath = ((FolderItem) curItem.getParent()).getProperty().getLabel() + fullPath;
                                } else {
                                    fullPath = ((FolderItem) curItem.getParent()).getProperty().getLabel() + "/" + fullPath;
                                }
                                curItem = (FolderItem) curItem.getParent();
                            }
                        }
                        if (objectType == ERepositoryObjectType.SQLPATTERNS) {
                            while (((FolderItem) curItem.getParent()).getType().getValue() != FolderType.SYSTEM_FOLDER) {
                                if ("".equals(fullPath)) {
                                    fullPath = ((FolderItem) curItem.getParent()).getProperty().getLabel() + fullPath;
                                } else {
                                    fullPath = ((FolderItem) curItem.getParent()).getProperty().getLabel() + "/" + fullPath;
                                }
                                curItem = (FolderItem) curItem.getParent();
                            }
                            while (!((FolderItem) curItem.getParent()).getProperty().getLabel().equals("sqlPatterns")) {
                                fullPath = ((FolderItem) curItem.getParent()).getProperty().getLabel() + "/" + fullPath;
                                curItem = (FolderItem) curItem.getParent();
                            }
                        }
                        folderItem.getState().setPath(fullPath);
                        this.setChildFolderPath(folderItem);
                    } catch (Exception e) {
                        ExceptionHandler.process(e);
                    }
                    monitor.done();
                }

                private void setChildFolderPath(FolderItem folderItem) {
                    EList childFoderList = folderItem.getChildren();
                    for (Object o : childFoderList) {
                        if (o instanceof FolderItem) {
                            String parentPath = ((FolderItem) ((FolderItem) o).getParent()).getState().getPath();
                            String parentName = ((FolderItem) ((FolderItem) o).getParent()).getProperty().getLabel();
                            ((FolderItem) o).getState().setPath(parentPath + File.separator + parentName);
                            setChildFolderPath((FolderItem) o);
                        }
                    }
                }
            };
            PlatformUI.getWorkbench().getProgressService().run(true, true, op);
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }

    }

    private void deleteRepositoryNode(IRepositoryNode repositoryNode, IProxyRepositoryFactory factory)
            throws PersistenceException, BusinessException {
        if (repositoryNode.getType() == ENodeType.SIMPLE_FOLDER) {
            IPath path = RepositoryNodeUtilities.getPath((RepositoryNode) repositoryNode);
            ERepositoryObjectType objectType = (ERepositoryObjectType) repositoryNode.getProperties(EProperties.CONTENT_TYPE);
            List<IRepositoryNode> repositoryList = repositoryNode.getChildren();
            for (IRepositoryNode repositoryNode2 : repositoryList) {
                deleteRepositoryNode(repositoryNode2, factory);
            }

            FolderItem folderItem = factory.getFolderItem(ProjectManager.getInstance().getCurrentProject(), objectType, path);
            folderItem.getState().setDeleted(true);

            String fullPath = "";
            FolderItem curItem = folderItem;
            while (curItem.getParent() instanceof FolderItem && ((Item) curItem.getParent()).getParent() instanceof FolderItem) {
                FolderItem parentFolder = (FolderItem) curItem.getParent();
                if ("".equals(fullPath)) {
                    fullPath = parentFolder.getProperty().getLabel() + fullPath;
                } else {
                    fullPath = parentFolder.getProperty().getLabel() + "/" + fullPath;
                }
                curItem = parentFolder;
            }
            folderItem.getState().setPath(fullPath);

        } else {
            IRepositoryViewObject objToDelete = repositoryNode.getObject();
            factory.deleteObjectLogical(objToDelete);
        }
    }

    /**
     * DOC qzhang Comment method "checkRepository".
     * 
     * @param factory
     * @param currentJobNode
     * @return
     */

    public static IEditorReference[] getEditors() {
        final List<IEditorReference> list = new ArrayList<IEditorReference>();
        Display.getDefault().syncExec(new Runnable() {

            public void run() {
                IEditorReference[] reference = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                        .getEditorReferences();
                list.addAll(Arrays.asList(reference));
            }
        });
        return list.toArray(new IEditorReference[0]);
    }

    private static boolean isOpenedItem(Item openedItem, MultiKeyMap openProcessMap) {
        if (openedItem == null) {
            return false;
        }
        Property property = openedItem.getProperty();
        return (openProcessMap.get(property.getId(), property.getLabel(), property.getVersion()) != null);
    }

    /**
     * 
     * wzhang Comment method "calcParentProjects".
     * 
     * @param curProject
     * @param parentProject
     * @param refParentProjects
     * @return
     */
    private static boolean calcParentProjects(Project curProject, Project parentProject, Set<Project> refParentProjects) {
        boolean found = false;
        if (curProject != null && parentProject != null) {
            Context ctx = CorePlugin.getContext();
            if (ctx == null) {
                return false;
            }
            RepositoryContext repositoryContext = (RepositoryContext) ctx.getProperty(Context.REPOSITORY_CONTEXT_KEY);
            String parentBranch = repositoryContext.getFields().get(
                    IProxyRepositoryFactory.BRANCH_SELECTION + "_" + parentProject.getTechnicalLabel());

            EList referencedProjects = parentProject.getEmfProject().getReferencedProjects();
            for (ProjectReference pRef : (List<ProjectReference>) referencedProjects) {
                if (pRef.getBranch() != null && !parentBranch.equals(pRef.getBranch())) {
                    continue;
                }
                final String technicalLabel = pRef.getReferencedProject().getTechnicalLabel();
                if (technicalLabel != null) {
                    final Project project = new Project(pRef.getReferencedProject());
                    final Project paProject = new Project(pRef.getProject());
                    if (technicalLabel.equals(curProject.getTechnicalLabel())
                            || calcParentProjects(curProject, project, refParentProjects)) {
                        found = true;
                        if (!refParentProjects.contains(project)) {
                            refParentProjects.add(project);
                        }
                        if (!refParentProjects.contains(paProject)) {
                            refParentProjects.add(paProject);
                        }
                    }
                }
            }
        }
        return found;
    }

    public static List<JobletReferenceBean> checkRepositoryNodeFromProcess(IProxyRepositoryFactory factory,
            DeleteActionCache deleteActionCache, RepositoryNode currentJobNode) {
        IRepositoryViewObject object = currentJobNode.getObject();
        Item nodeItem = object.getProperty().getItem(); // hywang add
        boolean needCheckJobletIfUsedInProcess = false;
        if (nodeItem instanceof JobletProcessItem) {
            needCheckJobletIfUsedInProcess = true;
        }
        List<JobletReferenceBean> list = new ArrayList<JobletReferenceBean>();

        if (deleteActionCache == null) {
            deleteActionCache = DeleteActionCache.getInstance();
            deleteActionCache.createRecords();
        }
        if (object != null && needCheckJobletIfUsedInProcess) {
            Property property = object.getProperty();
            if (property != null) {
                String label = property.getLabel();
                String version = property.getVersion();
                Item item = property.getItem();
                if (!(item instanceof JobletProcessItem)) {
                    return list;
                }
                EList nodesList = null;
                // wzhang added to fix bug 10050
                Set<Project> refParentProjects = new HashSet<Project>();
                try {
                    refParentProjects.add(ProjectManager.getInstance().getCurrentProject());
                    refParentProjects.addAll(ProjectManager.getInstance().getReferencedProjects());
                    // if (currentProject != null) {
                    // final Project[] readProject = factory.readProject();
                    // for (Project p : readProject) {
                    // if (p.equals(currentProject)) {
                    // continue;
                    // }
                    // calcParentProjects(currentProject, p, refParentProjects);
                    // }
                    // refParentProjects.add(currentProject); // contain current project
                    // }
                    for (Project refP : refParentProjects) {
                        List<IRepositoryViewObject> processes = factory.getAll(refP, ERepositoryObjectType.PROCESS);
                        List<IRepositoryViewObject> jobletes = factory.getAll(refP, ERepositoryObjectType.JOBLET);
                        processes.addAll(jobletes);
                        deleteActionCache.setProcessList(processes);
                        for (IRepositoryViewObject process : deleteActionCache.getProcessList()) {
                            // node = (EList) process.getGraphicalNodes();item

                            Property property2 = process.getProperty();

                            boolean isDelete = factory.getStatus(process) == ERepositoryStatus.DELETED;
                            boolean isJob = true;

                            Item item2 = property2.getItem();
                            if (item == item2) {
                                continue;
                            }
                            if (!isOpenedItem(item2, deleteActionCache.getOpenProcessMap())) {
                                if (item2 instanceof ProcessItem) {
                                    nodesList = ((ProcessItem) item2).getProcess().getNode();
                                } else if (item2 instanceof JobletProcessItem) {
                                    nodesList = ((JobletProcessItem) item2).getJobletProcess().getNode();
                                }
                            }
                            if (nodesList != null) {
                                // isExtensionComponent(node);
                                for (Object object2 : nodesList) {
                                    if (object2 instanceof NodeType) {
                                        NodeType nodeType = (NodeType) object2;
                                        nodeType.getElementParameter();
                                        boolean equals = nodeType.getComponentName().equals(label)
                                                && nodeType.getComponentVersion().equals(version);
                                        if (equals) {
                                            String path = item2.getState().getPath();

                                            boolean found = false;
                                            JobletReferenceBean bean = new JobletReferenceBean(property2.getLabel(), property2
                                                    .getVersion(), path, refP.getLabel());
                                            bean.setJobFlag(isJob, isDelete);

                                            for (JobletReferenceBean b : list) {
                                                if (b.toString().equals(bean.toString())) {
                                                    found = true;
                                                    b.addNodeNum();
                                                    break;
                                                }
                                            }
                                            if (!found) {
                                                list.add(bean);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        for (IProcess openedProcess : deleteActionCache.getOpenedProcessList()) {
                            for (INode node : openedProcess.getGraphicalNodes()) {
                                boolean equals = node.getComponent().getName().equals(label)
                                        && node.getComponent().getVersion().equals(version);

                                boolean isDelete = factory.getStatus(openedProcess) == ERepositoryStatus.DELETED;
                                boolean isJob = true;
                                Property property2 = openedProcess.getProperty();
                                Item item2 = property2.getItem();
                                String path = item2.getState().getPath();

                                if (equals) {

                                    boolean found = false;
                                    JobletReferenceBean bean = new JobletReferenceBean(property2.getLabel(), property2
                                            .getVersion(), path, refP.getLabel());
                                    bean.setJobFlag(isJob, isDelete);

                                    for (JobletReferenceBean b : list) {
                                        if (b.toString().equals(bean.toString())) {
                                            found = true;
                                            b.addNodeNum();
                                            break;
                                        }
                                    }
                                    if (!found) {
                                        list.add(bean);
                                    }
                                }

                            }
                        }

                    }

                } catch (PersistenceException e) {
                    ExceptionHandler.process(e);
                }

            }

        }

        return list;
    }

    /**
     * ftang Comment method "isForbbidNode".
     * 
     * @param node
     * @return
     */
    private boolean isForbidNode(RepositoryNode node) {

        IRepositoryViewObject nodeObject = node.getObject();
        // Avoid to delete node which is locked.
        if (nodeObject != null
                && (nodeObject.getProperty().getItem().getState().isLocked() || RepositoryManager
                        .isOpenedItemInEditor(nodeObject)) && !(DELETE_FOREVER_TITLE.equals(getText()))) {

            String title = Messages.getString("DeleteAction.error.title"); //$NON-NLS-1$
            String message = Messages.getString("DeleteAction.error.lockedOrOpenedObject.message");//$NON-NLS-1$
            MessageDialog dialog = new MessageDialog(new Shell(), title, null, message, MessageDialog.ERROR,
                    new String[] { IDialogConstants.YES_LABEL }, 0);//$NON-NLS-1$
            dialog.open();

            return true;
        }

        // Avoid to delete all related documentation node by click Key "Delete" from keyboard.
        if (node.getContentType() == ERepositoryObjectType.JOB_DOC) {
            return true;
        }

        if (node.getProperties(EProperties.CONTENT_TYPE) == ERepositoryObjectType.JOB_DOC) {
            return true;
        }

        if (node.getContentType() == ERepositoryObjectType.JOBLET_DOC) {
            return true;
        }

        if (node.getProperties(EProperties.CONTENT_TYPE) == ERepositoryObjectType.JOBLET_DOC) {
            return true;
        }

        if (node.getContentType() == ERepositoryObjectType.JOBS) {
            return true;
        }
        if (node.getContentType() == ERepositoryObjectType.GENERATED) {
            return true;
        }
        if (node.getProperties(EProperties.CONTENT_TYPE) == ERepositoryObjectType.METADATA_CON_CDC) {
            return true;
        }
        if (node.getProperties(EProperties.CONTENT_TYPE) == ERepositoryObjectType.METADATA_CON_TABLE) {
            final IRepositoryViewObject object = nodeObject;
            if (object != null && object instanceof MetadataTableRepositoryObject) {
                final MetadataTable table = ((MetadataTableRepositoryObject) object).getTable();
                if (table != null && table instanceof SubscriberTable) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean deleteElements(IProxyRepositoryFactory factory, DeleteActionCache deleteActionCache,
            RepositoryNode currentJobNode) throws PersistenceException, BusinessException {
        return deleteElements(factory, deleteActionCache, currentJobNode, null);
    }

    private boolean deleteElements(IProxyRepositoryFactory factory, DeleteActionCache deleteActionCache,
            RepositoryNode currentJobNode, Boolean confirm) throws PersistenceException, BusinessException {
        boolean needReturn = false;
        IRepositoryViewObject objToDelete = currentJobNode.getObject();

        List<JobletReferenceBean> checkRepository = checkRepositoryNodeFromProcess(factory, deleteActionCache, currentJobNode);
        if (checkRepository.size() > 0) {
            JobletReferenceDialog dialog = new JobletReferenceDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow()
                    .getShell(), objToDelete, checkRepository);
            dialog.open();
            return true;
        }
        // To manage case of we have a subitem. This is possible using 'DEL' shortcut:
        ERepositoryObjectType nodeType = (ERepositoryObjectType) currentJobNode.getProperties(EProperties.CONTENT_TYPE);
        if (nodeType.isSubItem()) {
            final DeleteTableAction deleteTableAction = new DeleteTableAction();
            deleteTableAction.setWorkbenchPart(getWorkbenchPart());
            deleteTableAction.run();
            needReturn = true;
        } else {
            if (factory.getStatus(objToDelete) == ERepositoryStatus.DELETED) {
                if (confirm == null) {
                    String title = Messages.getString("DeleteAction.dialog.title"); //$NON-NLS-1$
                    String message = currentJobNode.getProperties(EProperties.LABEL)
                            + " " + Messages.getString("DeleteAction.dialog.message0") + "\n" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                            + Messages.getString("DeleteAction.dialog.message2"); //$NON-NLS-1$
                    confirm = (MessageDialog.openQuestion(new Shell(), title, message));
                }
                if (confirm) {

                    deleteActionCache.closeOpenedEditor(objToDelete);
                    if (currentJobNode.getType() == ENodeType.SIMPLE_FOLDER) {
                        for (IRepositoryNode curNode : currentJobNode.getChildren()) {
                            deleteElements(factory, deleteActionCache, (RepositoryNode) curNode, confirm);
                        }
                        factory.deleteFolder(currentJobNode.getContentType(), new Path(currentJobNode.getObject().getProperty()
                                .getItem().getState().getPath()
                                + "/" + currentJobNode.getObject().getProperty().getLabel()));
                    } else {
                        factory.deleteObjectPhysical(objToDelete);
                        ExpressionPersistance.getInstance().jobDeleted(objToDelete.getLabel());
                    }
                }
            } else {
                factory.deleteObjectLogical(objToDelete);
            }
        }

        return needReturn;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.actions.ITreeContextualAction#init(org.eclipse.jface.viewers.TreeViewer,
     * org.eclipse.jface.viewers.IStructuredSelection)
     */
    public void init(TreeViewer viewer, IStructuredSelection selection) {
        visible = !selection.isEmpty();
        if (selection.isEmpty()) {
            setEnabled(false);
            return;
        }

        boolean enabled = true;
        this.setText(null);
        IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
        if (factory.isUserReadOnlyOnCurrentProject()) {
            visible = false;
        }
        for (Object o : (selection).toArray()) {
            if (visible) {
                RepositoryNode node = (RepositoryNode) o;
                if (!ProjectManager.getInstance().isInCurrentMainProject(node)) {
                    visible = false;
                    break;
                }
                switch (node.getType()) {
                case STABLE_SYSTEM_FOLDER:
                    visible = false;
                case SYSTEM_FOLDER:
                    visible = false;
                    break;
                case SIMPLE_FOLDER:
                    Object obj = node.getProperties(EProperties.LABEL);
                    String label = null;
                    if (obj instanceof String) {
                        label = (String) obj;
                    }
                    if (node.getContentType() == ERepositoryObjectType.JOB_DOC
                            || node.getContentType() == ERepositoryObjectType.JOBLET_DOC
                            || RepositoryConstants.USER_DEFINED.equals(label)) {
                        visible = false;
                    } else {
                        this.setText(DELETE_LOGICAL_TITLE);
                        this.setToolTipText(DELETE_LOGICAL_TOOLTIP);
                        if (node.hasChildren()) {
                            visible = true;
                            enabled = true;
                        }
                    }

                    // 1. the select node should belong to the SQL Patterns
                    // 2. the select node is the father node of the SQL Patterns
                    // 3. the select node do not has father node(means do not contain "/")
                    String selectName = selection.getFirstElement().toString();
                    if (node.getContentType() == ERepositoryObjectType.SQLPATTERNS && selectName.equals(label)
                            && !selectName.contains("/")) { //$NON-NLS-1$
                        visible = false;
                    }
                    break;
                case REPOSITORY_ELEMENT:
                    if (node.getProperties(EProperties.CONTENT_TYPE) == ERepositoryObjectType.JOB_DOC
                            || node.getProperties(EProperties.CONTENT_TYPE) == ERepositoryObjectType.JOBLET_DOC) {
                        visible = false;
                        break;
                    }
                    if (node.getProperties(EProperties.CONTENT_TYPE) == ERepositoryObjectType.METADATA_CON_CDC) {
                        enabled = false;
                        visible = false;
                        break;
                    }
                    IRepositoryViewObject repObj = node.getObject();
                    IProxyRepositoryFactory repFactory = ProxyRepositoryFactory.getInstance();

                    ERepositoryStatus status = repFactory.getStatus(repObj);
                    boolean isEditable = status.isPotentiallyEditable() || status.isEditable();
                    boolean isDeleted = repFactory.getStatus(repObj) == ERepositoryStatus.DELETED;

                    if (isDeleted) {
                        ERepositoryObjectType nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
                        if (ERepositoryObjectType.METADATA_CON_TABLE.equals(nodeType)) {
                            visible = false;
                            break;
                        }
                        if (ERepositoryObjectType.METADATA_CON_QUERY.equals(nodeType)) {
                            visible = false;
                            break;
                        }

                        if (getText() == null || DELETE_FOREVER_TITLE.equals(getText())) {
                            this.setText(DELETE_FOREVER_TITLE);
                            this.setToolTipText(DELETE_FOREVER_TOOLTIP);
                        } else {
                            visible = false;
                        }
                    } else {
                        switch (repObj.getType()) {
                        case METADATA_CON_TABLE:
                        case METADATA_CON_QUERY:
                            visible = false;
                            break;
                        default:
                            if (getText() == null || DELETE_LOGICAL_TITLE.equals(getText())) {
                                this.setText(DELETE_LOGICAL_TITLE);
                                this.setToolTipText(DELETE_LOGICAL_TOOLTIP);

                                if (!isEditable) {
                                    visible = true;
                                    enabled = false;
                                }
                            } else {
                                visible = false;
                            }
                            break;
                        }
                    }
                    break;
                default:
                    // Nothing to do
                    break;
                }
            }
        }
        setEnabled(enabled);
    }

    private boolean visible;

    /**
     * Getter for visible.
     * 
     * @return the visible
     */
    @Override
    public boolean isVisible() {
        return this.visible;
    }

    /**
     * Sets the visible.
     * 
     * @param visible the visible to set
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
