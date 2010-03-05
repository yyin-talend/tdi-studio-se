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
package org.talend.componentdesigner.manager;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResourceStatus;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.undo.CreateProjectOperation;
import org.eclipse.ui.ide.undo.WorkspaceUndoUtil;
import org.eclipse.ui.statushandlers.StatusAdapter;
import org.eclipse.ui.statushandlers.StatusManager;
import org.talend.componentdesigner.ComponentDesigenerPlugin;
import org.talend.componentdesigner.PluginConstant;
import org.talend.componentdesigner.i18n.internal.Messages;
import org.talend.componentdesigner.ui.progress.ProgressUI;

/**
 * @author rli
 * 
 */
public final class ComponentProjectManager {

    // cache of newly-created project
    private IProject project;

    private final String projDir = PluginConstant.EMPTY_STRING;

    private static ComponentProjectManager manager = new ComponentProjectManager();

    public static ComponentProjectManager getInstance() {
        return manager;
    }

    private ComponentProjectManager() {

    }

    /**
     * Creates a new project resource with the selected name.
     * <p>
     * In normal usage, this method is invoked after the user has pressed Finish on the wizard; the enablement of the
     * Finish button implies that all controls on the pages currently contain valid values.
     * </p>
     * <p>
     * Note that this wizard caches the new project once it has been successfully created; subsequent invocations of
     * this method will answer the same project resource without attempting to create it again.
     * </p>
     * 
     * @return the created project resource, or <code>null</code> if the project was not created
     */
    public IProject createNewProject(String directroy, String projectName, Shell shell) {
        if (projDir.equals(directroy)) {
            return project;
        }

        final Shell currentShell = shell;

        // get a project handle
        final IProject newProjectHandle = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
        if (newProjectHandle.getRawLocation() != null) {
            if (newProjectHandle.getRawLocation().equals(directroy)) {
                return newProjectHandle;
            } else {
                try {
                    newProjectHandle.delete(false, true, null);
                } catch (CoreException e) {
                    // e.printStackTrace();
                    org.talend.componentdesigner.exception.ExceptionHandler.process(e);
                }
            }
        }

        // final IJavaProject javaProjHandle = JavaCore.create(newProjectHandle);
        // get a project descriptor
        URI location = null;
        if (directroy == null || directroy.equals(PluginConstant.EMPTY_STRING)) {
            return null;
        } else {
            location = new File(directroy).toURI();
        }

        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        final IProjectDescription description = workspace.newProjectDescription(newProjectHandle.getName());
        description.setLocationURI(location);

        // create the new project operation
        IRunnableWithProgress op = new IRunnableWithProgress() {

            public void run(IProgressMonitor monitor) throws InvocationTargetException {
                CreateProjectOperation op = new CreateProjectOperation(description, Messages
                        .getString("ComponentProjectManager.NewProject")); //$NON-NLS-1$
                try {
                    PlatformUI.getWorkbench().getOperationSupport().getOperationHistory().execute(op, monitor,
                            WorkspaceUndoUtil.getUIInfoAdapter(currentShell));
                } catch (ExecutionException e) {
                    throw new InvocationTargetException(e);
                }
            }
        };

        // run the new project creation o`peration
        try {
            ProgressUI.popProgressDialog(op, shell);
        } catch (InterruptedException e) {
            return null;
        } catch (InvocationTargetException e) {
            Throwable t = e.getTargetException();
            if (t instanceof ExecutionException && t.getCause() instanceof CoreException) {
                CoreException cause = (CoreException) t.getCause();
                StatusAdapter status;
                if (cause.getStatus().getCode() == IResourceStatus.CASE_VARIANT_EXISTS) {
                    status = new StatusAdapter(

                    new Status(IStatus.WARNING, ComponentDesigenerPlugin.PLUGIN_ID, IStatus.WARNING, Messages.getString(
                            "ComponentProjectManager.WarningMsg", newProjectHandle.getName()) //$NON-NLS-1$
                            , cause));
                } else {
                    status = new StatusAdapter(new Status(cause.getStatus().getSeverity(), ComponentDesigenerPlugin.PLUGIN_ID,
                            cause.getStatus().getSeverity(),
                            Messages.getString("ComponentProjectManager.CreationProblems"), cause)); //$NON-NLS-1$
                }
                status.setProperty(StatusAdapter.TITLE_PROPERTY, Messages.getString("ComponentProjectManager.CreationProblems")); //$NON-NLS-1$
                StatusManager.getManager().handle(status, StatusManager.BLOCK);
            } else {
                StatusAdapter status = new StatusAdapter(new Status(IStatus.WARNING, ComponentDesigenerPlugin.PLUGIN_ID, 0,
                        Messages.getString("ComponentProjectManager.InternalErrorMsg", t.getMessage()), t)); //$NON-NLS-1$
                status.setProperty(StatusAdapter.TITLE_PROPERTY, Messages.getString("ComponentProjectManager.CreationProblems")); //$NON-NLS-1$
                StatusManager.getManager().handle(status, StatusManager.LOG | StatusManager.BLOCK);
            }
            return null;
        }

        project = newProjectHandle;

        return project;
    }

    /**
     * Returns the newly created project.
     * 
     * @return the created project, or <code>null</code> if project not created
     */
    public IProject getProject() {
        return project;
    }
}
