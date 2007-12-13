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
package org.talend.componentdesigner.ui.wizard;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.List;

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
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.undo.CreateProjectOperation;
import org.eclipse.ui.ide.undo.WorkspaceUndoUtil;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
import org.eclipse.ui.statushandlers.StatusAdapter;
import org.eclipse.ui.statushandlers.StatusManager;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;
import org.talend.componentdesigner.ComponentDesigenerPlugin;
import org.talend.componentdesigner.PluginConstant;
import org.talend.componentdesigner.manager.ComponentProjectManager;
import org.talend.componentdesigner.model.ComponentProperty;
import org.talend.componentdesigner.model.enumtype.JetFileStamp;
import org.talend.componentdesigner.model.enumtype.LanguageType;

/**
 * Standard workbench wizard that creates a new component project resource in the workspace. Example:
 * 
 * <pre>
 * ComponentProjectWizard wizard = new ComponentProjectWizard();
 * wizard.init(workbench, selection);
 * WizardDialog dialog = new WizardDialog(shell, wizard);
 * dialog.open();
 * </pre>
 * 
 * During the call to <code>open</code>, the wizard dialog is presented to the user. When the user hits Finish, a
 * project resource with the user-specified name is created, the dialog closes, and the call to <code>open</code>
 * returns.
 */
public class ComponentProjectWizard extends BasicNewResourceWizard implements PropertyChangeListener {

    private WizardComponentProjectPage creatProjectPage;

    private WizardJetFilesChoosePage creatJetFilesPage;

    // cache of newly-created project
    private IProject newProject;

    private ComponentProperty componentProperty;

    private ComponentProjectManager manager;

    /**
     * Creates a wizard for creating a new project resource in the workspace.
     */
    public ComponentProjectWizard() {
        IDialogSettings workbenchSettings = ComponentDesigenerPlugin.getDefault().getDialogSettings();
        IDialogSettings section = workbenchSettings.getSection("BasicNewProjectResourceWizard"); //$NON-NLS-1$
        if (section == null) {
            section = workbenchSettings.addNewSection("BasicNewProjectResourceWizard"); //$NON-NLS-1$
        }
        setDialogSettings(section);
        componentProperty = new ComponentProperty();
    }

    /*
     * (non-Javadoc) Method declared on IWizard.
     */
    public void addPages() {
        creatProjectPage = new WizardComponentProjectPage("componentNewProjectPage"); //$NON-NLS-1$
        creatProjectPage.setTitle("Component Project");
        creatProjectPage.setDescription("Create a new component project resource.");
        creatProjectPage.getPropertyChangeBean().addPropertyChangeListener(this);
        this.addPage(creatProjectPage);
        creatJetFilesPage = new WizardJetFilesChoosePage("creatJetFilesPage");
        creatJetFilesPage.setTitle("Creat jet files for the component project");
        creatJetFilesPage.getPropertyChangeBean().addPropertyChangeListener(this);
        this.addPage(creatJetFilesPage);

        // only add page if there are already projects in the workspace
        // if (ResourcesPlugin.getWorkspace().getRoot().getProjects().length > 0) {
        // referencePage = new WizardNewProjectReferencePage(
        // "basicReferenceProjectPage");//$NON-NLS-1$
        // referencePage.setTitle("Project References");
        // referencePage
        // .setDescription("Select referenced projects.");
        // this.addPage(referencePage);
        // }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    @SuppressWarnings("unchecked")
    public void propertyChange(PropertyChangeEvent event) {
        if (PluginConstant.NAME_PROPERTY.equals(event.getPropertyName())) {
            this.componentProperty.setName((String) event.getNewValue());
        } else if (PluginConstant.LANGUAGE_PROPERTY.equals(event.getPropertyName())) {
            this.componentProperty.setLanguageType((LanguageType) event.getNewValue());
        } else if (PluginConstant.JETFILETYPE_PROPERTY.equals(event.getPropertyName())) {
            this.componentProperty.setJetFileTypes((List<JetFileStamp>) event.getNewValue());
        } else if (PluginConstant.IMAGE_PROPERTY.equals(event.getPropertyName())) {
            this.componentProperty.setImageURL((String) event.getNewValue());
        } else if (PluginConstant.LIBRARY_PROPERTY.equals(event.getPropertyName())) {
            this.componentProperty.setLibFileURL((String) event.getNewValue());
        }
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
    private IProject createNewProject() {
        if (newProject != null) {
            return newProject;
        }

        // get a project handle
        final IProject newProjectHandle = creatProjectPage.getProjectHandle();

        // get a project descriptor
        URI location = null;
        if (!creatProjectPage.useDefaults()) {
            location = creatProjectPage.getLocationURI();
        }

        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        final IProjectDescription description = workspace.newProjectDescription(newProjectHandle.getName());
        description.setLocationURI(location);

        // update the referenced project if provided
        // if (referencePage != null) {
        // IProject[] refProjects = referencePage.getReferencedProjects();
        // if (refProjects.length > 0) {
        // description.setReferencedProjects(refProjects);
        // }
        // }

        // create the new project operation
        IRunnableWithProgress op = new IRunnableWithProgress() {

            public void run(IProgressMonitor monitor) throws InvocationTargetException {
                CreateProjectOperation op = new CreateProjectOperation(description, "New Project");
                try {
                    PlatformUI.getWorkbench().getOperationSupport().getOperationHistory().execute(op, monitor,
                            WorkspaceUndoUtil.getUIInfoAdapter(getShell()));
                } catch (ExecutionException e) {
                    throw new InvocationTargetException(e);
                }
            }
        };

        // run the new project creation operation
        try {
            getContainer().run(true, true, op);
        } catch (InterruptedException e) {
            return null;
        } catch (InvocationTargetException e) {
            Throwable t = e.getTargetException();
            if (t instanceof ExecutionException && t.getCause() instanceof CoreException) {
                CoreException cause = (CoreException) t.getCause();
                StatusAdapter status;
                if (cause.getStatus().getCode() == IResourceStatus.CASE_VARIANT_EXISTS) {
                    status = new StatusAdapter(

                    new Status(IStatus.WARNING, ComponentDesigenerPlugin.PLUGIN_ID, IStatus.WARNING,
                            "The underlying file system is case insensitive. There is an existing project which conflicts with"
                                    + newProjectHandle.getName(), cause));
                } else {
                    status = new StatusAdapter(new Status(cause.getStatus().getSeverity(), ComponentDesigenerPlugin.PLUGIN_ID,
                            cause.getStatus().getSeverity(), "Creation Problems", cause));
                }
                status.setProperty(StatusAdapter.TITLE_PROPERTY, "Creation Problems");
                StatusManager.getManager().handle(status, StatusManager.BLOCK);
            } else {
                StatusAdapter status = new StatusAdapter(new Status(IStatus.WARNING, ComponentDesigenerPlugin.PLUGIN_ID, 0,
                        "Internal error:" + t.getMessage(), t));
                status.setProperty(StatusAdapter.TITLE_PROPERTY, "Creation Problems");
                StatusManager.getManager().handle(status, StatusManager.LOG | StatusManager.BLOCK);
            }
            return null;
        }

        newProject = newProjectHandle;

        return newProject;
    }

    /**
     * Returns the newly created project.
     * 
     * @return the created project, or <code>null</code> if project not created
     */
    public IProject getNewProject() {
        return newProject;
    }

    /*
     * (non-Javadoc) Method declared on IWorkbenchWizard.
     */
    public void init(IWorkbench workbench, IStructuredSelection currentSelection) {
        super.init(workbench, currentSelection);
        setNeedsProgressMonitor(true);
        setWindowTitle("New Project");
    }

    /*
     * (non-Javadoc) Method declared on BasicNewResourceWizard.
     */
    protected void initializeDefaultPageImageDescriptor() {
        ImageDescriptor desc = IDEWorkbenchPlugin.getIDEImageDescriptor("wizban/newprj_wiz.png");//$NON-NLS-1$
        setDefaultPageImageDescriptor(desc);
    }

    /*
     * (non-Javadoc) Method declared on IWizard.
     */
    public boolean performFinish() {
        createNewProject();
        if (newProject == null) {
            return false;
        }
        selectAndReveal(newProject);
        manager = new ComponentProjectManager();
        try {
            manager.generateComponentContent(componentProperty, newProject);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (CoreException e) {
            e.printStackTrace();
        }
        return true;
    }

    public void dispose() {

    }
}
