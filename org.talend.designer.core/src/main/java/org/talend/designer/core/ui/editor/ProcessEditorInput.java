// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//   
// ============================================================================
package org.talend.designer.core.ui.editor;

import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPersistableElement;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.MessageBoxExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.data.container.Content;
import org.talend.commons.utils.data.container.ContentList;
import org.talend.commons.utils.data.container.RootContainer;
import org.talend.commons.utils.workbench.resources.ResourceUtils;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.designer.core.DesignerPlugin;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;
import org.talend.designer.core.ui.editor.process.Process;
import org.talend.repository.editor.RepositoryEditorInput;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IRepositoryService;
import org.talend.repository.model.RepositoryConstants;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.ResourceModelUtils;
import org.talend.repository.model.RepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryNode.EProperties;
import org.talend.repository.ui.views.IRepositoryView;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class ProcessEditorInput extends RepositoryEditorInput {

    private static final String TEMP_DIRECTORY = RepositoryConstants.TEMP_DIRECTORY;

    private Process loadedProcess;

    private IRepositoryView view;

    private RepositoryNode repositoryNode;

    public ProcessEditorInput(ProcessItem processItem, boolean load) throws PersistenceException {
        this(processItem, load, null);
    }

    public ProcessEditorInput(ProcessItem processItem, boolean load, Boolean readonly) throws PersistenceException {
        super(initFile(processItem.getProperty().getId()), processItem);

        loadedProcess = new Process(getProcessItem().getProperty());
        if (load) {
            loadProcess();
        } else {
            saveProcess(null, null);
        }
        if (readonly == null) {
            checkReadOnly();
        } else {
            setForceReadOnly(readonly);
        }
    }

    public boolean setForceReadOnly(boolean readonly) {
        if (readonly) {
            loadedProcess.setReadOnly(readonly);
            return true;
        } else {
            try {
                return checkReadOnly();
            } catch (PersistenceException e) {
                ExceptionHandler.process(e);
                return false;
            }
        }
    }

    /**
     * DOC mhelleboid Comment method "checkReadOnly".
     * 
     * @param processItem
     * @throws PersistenceException
     */
    private boolean checkReadOnly() throws PersistenceException {
        return loadedProcess.checkReadOnly();
    }

    private void loadProcess() throws PersistenceException {
        loadedProcess.loadXmlFile((ProcessType) getProcessItem().getProcess());
        loadedProcess.checkLoadNodes();
    }

    /**
     * DOC mhelleboid Comment method "initFile".
     * 
     * @throws PersistenceException
     */
    private static IFile initFile(String id) throws PersistenceException {
        Project project = ((RepositoryContext) CorePlugin.getContext().getProperty(Context.REPOSITORY_CONTEXT_KEY)).getProject();
        IProject fsProject = ResourceModelUtils.getProject(project);
        IFolder folder = ResourceUtils.getFolder(fsProject, TEMP_DIRECTORY, true);
        return ResourceUtils.getFile(folder, "tempProcess" + id, false); //$NON-NLS-1$
    }

    public Process getLoadedProcess() {
        return loadedProcess;
    }

    public boolean saveProcess(IProgressMonitor monitor, IPath path) {
        // PTODO SML Removed null test on monitor after assure that in can't be
        try {
            if (monitor != null) {
                monitor.beginTask(Messages.getString("ProcessEditorInput.monitorBeginText"), 100); //$NON-NLS-1$
            }
            ProcessType processType = loadedProcess.saveXmlFile(getFile());
            if (monitor != null) {
                monitor.worked(40);
            }

            getFile().refreshLocal(IResource.DEPTH_ONE, monitor);

            loadedProcess.setXmlStream(getFile().getContents());

            IRepositoryService service = DesignerPlugin.getDefault().getRepositoryService();
            IProxyRepositoryFactory factory = service.getProxyRepositoryFactory();

            if (path != null) {
                // factory.createProcess(project, loadedProcess, path);
            } else {
                getProcessItem().setProcess(processType);
                factory.save(getProcessItem());
                if (monitor != null) {
                    monitor.worked(50);
                }
            }

            refresh();
            if (monitor != null) {
                monitor.worked(10);
            }
            return true;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            if (monitor != null) {
                monitor.setCanceled(true);
            }
            return false;
        } catch (PersistenceException e) {
            MessageBoxExceptionHandler.process(e);
            if (monitor != null) {
                monitor.setCanceled(true);
            }
            return false;
        } catch (CoreException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            if (monitor != null) {
                monitor.setCanceled(true);
            }
            return false;
        } catch (Exception e) {
            ExceptionHandler.process(e);
            if (monitor != null) {
                monitor.setCanceled(true);
            }
            return false;
        } finally {
            if (monitor != null) {
                monitor.done();
            }
        }
    }

    /**
     * DOC smallet Comment method "refresh".
     */
    private void refresh() {
        if (view != null && repositoryNode != null) {
            view.refresh(repositoryNode);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IEditorInput#exists()
     */
    public boolean exists() {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IEditorInput#getImageDescriptor()
     */
    public ImageDescriptor getImageDescriptor() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IEditorInput#getName()
     */
    public String getName() {
        // TODO Auto-generated method stub
        return getProcessItem().getProperty().getLabel();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IEditorInput#getPersistable()
     */
    public IPersistableElement getPersistable() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IEditorInput#getToolTipText()
     */
    public String getToolTipText() {
        return this.getName();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
     */
    public Object getAdapter(Class adapter) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * DOC smallet Comment method "setView".
     * 
     * @param viewPart
     */
    public void setView(IRepositoryView viewPart) {
        this.view = viewPart;
    }

    /**
     * DOC smallet Comment method "setNode".
     * 
     * @param node
     */
    public void setRepositoryNode(RepositoryNode repositoryNode) {
        if (repositoryNode != null) {
        this.repositoryNode = repositoryNode;
        } else {
            IProxyRepositoryFactory factory = DesignerPlugin.getDefault().getProxyRepositoryFactory();
            IRepositoryObject repositoryObject = null;
            RepositoryNode parentNode = null;
            try {
                RootContainer<String, IRepositoryObject> processContainer = factory.getProcess();
                ContentList<String, IRepositoryObject> processAbsoluteMembers = processContainer.getAbsoluteMembers();

                for (Content<String, IRepositoryObject> object : processAbsoluteMembers.values()) {
                    IRepositoryObject process = (IRepositoryObject) object.getContent();
                    if (process.getLabel().equals(this.getProcessItem().getProperty().getLabel())) {
                        repositoryObject = process;
                    }
                }
            } catch (PersistenceException e) {
                e.printStackTrace();
            }
            ERepositoryObjectType itemType = ERepositoryObjectType.getItemType(this.getProcessItem());
            if (repositoryObject != null) {
                this.repositoryNode = new RepositoryNode(repositoryObject, parentNode,
                        ENodeType.REPOSITORY_ELEMENT);
                this.repositoryNode.setProperties(EProperties.CONTENT_TYPE, itemType);
            }

        }
    }

    /**
     * Getter for node.
     * 
     * @return the node
     */
    public RepositoryNode getRepositoryNode() {
        return this.repositoryNode;
    }

    private ProcessItem getProcessItem() {
        return (ProcessItem) getItem();
    }

}
