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
import org.talend.commons.utils.workbench.resources.ResourceUtils;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.ProcessItem;
import org.talend.designer.core.DesignerPlugin;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;
import org.talend.designer.core.ui.editor.process.Process;
import org.talend.repository.editor.RepositoryEditorInput;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IRepositoryService;
import org.talend.repository.model.RepositoryConstants;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.ResourceModelUtils;
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
            setReadOnly(readonly);
        }
    }

    public boolean setReadOnly(boolean readonly) {
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
        return ResourceUtils.getFile(folder, "tempProcess" + id, false);
    }

    public Process getLoadedProcess() {
        return loadedProcess;
    }

    public boolean saveProcess(IProgressMonitor monitor, IPath path) {
        // PTODO SML Removed null test on monitor after assure that in can't be
        try {
            if (monitor != null) {
                monitor.beginTask("Saving process ...", 100);
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

    private static final int PRIME = 31;

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int result = 1;
        result = PRIME * result + ((this.loadedProcess == null) ? 0 : this.loadedProcess.hashCode());
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
        final ProcessEditorInput other = (ProcessEditorInput) obj;
        if (this.loadedProcess == null) {
            if (other.loadedProcess != null) {
                return false;
            }
        } else if (!this.loadedProcess.equals(other.loadedProcess)) {
            return false;
        }
        return true;
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
        this.repositoryNode = repositoryNode;
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
