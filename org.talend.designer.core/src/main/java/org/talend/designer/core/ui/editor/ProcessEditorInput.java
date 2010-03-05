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
package org.talend.designer.core.ui.editor;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPersistableElement;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.workbench.resources.ResourceUtils;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.ProcessItem;
import org.talend.designer.core.ui.editor.process.Process;
import org.talend.designer.core.ui.projectsetting.ProjectSettingManager;
import org.talend.repository.editor.RepositoryEditorInput;
import org.talend.repository.model.RepositoryConstants;
import org.talend.repository.model.ResourceModelUtils;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class ProcessEditorInput extends RepositoryEditorInput {

    private static final String TEMP_DIRECTORY = RepositoryConstants.TEMP_DIRECTORY;

    public ProcessEditorInput(ProcessItem processItem, boolean load) throws PersistenceException {
        this(processItem, load, null);
    }

    public ProcessEditorInput(ProcessItem processItem, boolean load, Boolean readonly) throws PersistenceException {

        super(initFile(processItem.getProperty().getId()), processItem);

        loadedProcess = new Process(getProcessItem().getProperty());
        if (load) {
            loadProcess();
        } else {
            // use project setting true
            ProjectSettingManager.defaultUseProjectSetting(getLoadedProcess());
            saveProcess(null, null);
        }
        if (readonly == null) {
            checkReadOnly();
        } else {
            setReadOnly(readonly);
        }
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
        return (Process) loadedProcess;
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
}
