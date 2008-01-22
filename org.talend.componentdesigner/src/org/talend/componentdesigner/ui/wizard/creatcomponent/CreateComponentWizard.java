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
package org.talend.componentdesigner.ui.wizard.creatcomponent;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;
import org.talend.componentdesigner.ComponentDesigenerPlugin;
import org.talend.componentdesigner.ImageLib;
import org.talend.componentdesigner.PluginConstant;
import org.talend.componentdesigner.manager.ComponentFolderManager;
import org.talend.componentdesigner.model.componentpref.ComponentPref;
import org.talend.componentdesigner.model.componentpref.ComponentPrefCollection;

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
public class CreateComponentWizard extends BasicNewResourceWizard {

    private WizardComponentFolderPage creatProjectPage;

    private WizardJetFilesChoosePage creatJetFilesPage;

    private ComponentPref componentPref;

    private ComponentFolderManager manager;

    /**
     * Creates a wizard for creating a new project resource in the workspace.
     */
    public CreateComponentWizard() {
        IDialogSettings workbenchSettings = ComponentDesigenerPlugin.getDefault().getDialogSettings();
        IDialogSettings section = workbenchSettings.getSection("BasicNewProjectResourceWizard"); //$NON-NLS-1$
        if (section == null) {
            section = workbenchSettings.addNewSection("BasicNewProjectResourceWizard"); //$NON-NLS-1$
        }
        setDialogSettings(section);
        componentPref = new ComponentPref();
    }
    
    /**
     * Creates a wizard for creating a new project resource in the workspace.
     */
    public CreateComponentWizard(String selectedComponentName) {
        IDialogSettings workbenchSettings = ComponentDesigenerPlugin.getDefault().getDialogSettings();
        IDialogSettings section = workbenchSettings.getSection("BasicNewProjectResourceWizard"); //$NON-NLS-1$
        if (section == null) {
            section = workbenchSettings.addNewSection("BasicNewProjectResourceWizard"); //$NON-NLS-1$
        }
        setDialogSettings(section);
        componentPref = ComponentPrefCollection.getInstance().getComponentPref(selectedComponentName);
    }

    /*
     * (non-Javadoc) Method declared on IWizard.
     */
    public void addPages() {
		creatProjectPage = new WizardComponentFolderPage(
				"componentNewProjectPage", componentPref); //$NON-NLS-1$
		creatProjectPage.setTitle("Component");
		creatProjectPage
				.setDescription("Create a new component folder resource.");
//		creatProjectPage.getPropertyChangeBean()
//				.addPropertyChangeListener(this);
		this.addPage(creatProjectPage);
		creatJetFilesPage = new WizardJetFilesChoosePage("creatJetFilesPage",   //$NON-NLS-1$
				componentPref);
		creatJetFilesPage.setTitle("Create jet files for the component.");
//		creatJetFilesPage.getPropertyChangeBean().addPropertyChangeListener(
//				this);
		this.addPage(creatJetFilesPage);
		
		WizardXMLConfigPage xmlConfigPage = new WizardXMLConfigPage(
				"xmlConfigPage", componentPref);
		xmlConfigPage.setTitle("Create the xml configuration file for current component.");
		this.addPage(xmlConfigPage);		
	}

    /*
	 * (non-Javadoc)
	 * 
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
    @SuppressWarnings("unchecked")
//    public void propertyChange(PropertyChangeEvent event) {
//		if (PluginConstant.NAME_PROPERTY.equals(event.getPropertyName())) {
//			this.componentPref.setName((String) event.getNewValue());
//		} else if (PluginConstant.LANGUAGE_PROPERTY.equals(event
//				.getPropertyName())) {
//			this.componentPref.setLanguageType((LanguageType) event
//					.getNewValue());
//		} else if (PluginConstant.RESOURCETYPE_PROPERTY.equals(event
//				.getPropertyName())) {
//			this.componentPref
//					.setResourceLanguageTypes((List<ResourceLanguageType>) event
//							.getNewValue());
//		} else if (PluginConstant.JETFILETYPE_PROPERTY.equals(event
//				.getPropertyName())) {
//			this.componentPref.setJetFileStamps((List<JetFileStamp>) event
//					.getNewValue());
//		} else if (PluginConstant.IMAGE_PROPERTY
//				.equals(event.getPropertyName())) {
//			this.componentPref.setImageURL((String) event.getNewValue());
//		} else if (PluginConstant.LIBRARY_PROPERTY.equals(event
//				.getPropertyName())) {
//			this.componentPref.setLibEntries((ILibEntry[]) event
//					.getNewValue());
//		}
//	}

    /*
	 * (non-Javadoc) Method declared on IWorkbenchWizard.
	 */
    public void init(IWorkbench workbench, IStructuredSelection currentSelection) {
        super.init(workbench, currentSelection);
        setNeedsProgressMonitor(true);
        setWindowTitle("New Component");
    }

    /*
     * (non-Javadoc) Method declared on BasicNewResourceWizard.
     */
    protected void initializeDefaultPageImageDescriptor() {
        ImageDescriptor desc = ImageLib.getImageDescriptor(ImageLib.NEWCOMPONENT_WIZARD); //$NON-NLS-1$
        setDefaultPageImageDescriptor(desc);
    }

    /*
	 * (non-Javadoc) Method declared on IWizard.
	 */
	public boolean performFinish() {
		manager = new ComponentFolderManager();
		try {
			manager.generateComponentContent(componentPref, ResourcesPlugin
					.getWorkspace().getRoot().getProject(PluginConstant.PROJECTNAME_DEFAULT));
			ComponentPrefCollection.getInstance().save(componentPref);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (CoreException e) {
			e.printStackTrace();
		} catch (IOException ioe) {
            ioe.printStackTrace();
        }
		return true;
	}

    public void dispose() {

    }
}
