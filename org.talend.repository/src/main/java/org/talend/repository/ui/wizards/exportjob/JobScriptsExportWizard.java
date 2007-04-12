// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2007 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License.
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

package org.talend.repository.ui.wizards.exportjob;

import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
import org.eclipse.ui.internal.wizards.datatransfer.DataTransferMessages;
import org.eclipse.ui.internal.wizards.datatransfer.WizardFileSystemResourceExportPage1;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.repository.i18n.Messages;
import org.talend.repository.ui.wizards.ConfigExternalLib.ConfigExternalJarPage;
import org.talend.repository.ui.wizards.ConfigExternalLib.ConfigExternalPerlModulePage;

/**
 * Job scripts export wizard. <br/>
 * 
 * $Id: JobScriptsExportWizard.java 1 2006-12-13 下午03:13:18 bqian
 * 
 */
public class JobScriptsExportWizard extends Wizard implements IExportWizard {

    private IStructuredSelection selection;

    private WizardFileSystemResourceExportPage1 mainPage;

    /**
     * Creates a wizard for exporting workspace resources to a zip file.
     */
    public JobScriptsExportWizard() {
        AbstractUIPlugin plugin = (AbstractUIPlugin) Platform.getPlugin(PlatformUI.PLUGIN_ID);
        IDialogSettings workbenchSettings = plugin.getDialogSettings();
        IDialogSettings section = workbenchSettings.getSection("JobScriptsExportWizard"); //$NON-NLS-1$
        if (section == null) {
            section = workbenchSettings.addNewSection("JobScriptsExportWizard"); //$NON-NLS-1$
            section.put(JobScriptsExportWizardPage.STORE_SHELL_LAUNCHER_ID, true);
            section.put(JobScriptsExportWizardPage.STORE_SYSTEM_ROUTINE_ID, true);
            section.put(JobScriptsExportWizardPage.STORE_USER_ROUTINE_ID, true);
            section.put(JobScriptsExportWizardPage.STORE_MODEL_ID, true);
            section.put(JobScriptsExportWizardPage.STORE_JOB_ID, true);
            section.put(JobScriptsExportWizardPage.STORE_CONTEXT_ID, true);
            
            section.put(JavaJobScriptsExportWizardPage.STORE_SHELL_LAUNCHER_ID, true);
            section.put(JavaJobScriptsExportWizardPage.STORE_SYSTEM_ROUTINE_ID, true);
            section.put(JavaJobScriptsExportWizardPage.STORE_USER_ROUTINE_ID, true);
            section.put(JavaJobScriptsExportWizardPage.STORE_MODEL_ID, true);
            section.put(JavaJobScriptsExportWizardPage.STORE_JOB_ID, true);
            section.put(JavaJobScriptsExportWizardPage.STORE_CONTEXT_ID, true);
            
            
            
//            section.put(JobScriptsExportWizardPage.STORE_GENERATECODE_ID, true);
        }
        setDialogSettings(section);
    }

    /*
     * (non-Javadoc) Method declared on IWizard.
     */
    public void addPages() {
        super.addPages();
        
        switch (((RepositoryContext) CorePlugin.getContext().getProperty(Context.REPOSITORY_CONTEXT_KEY)).getProject()
                .getLanguage()) {
        case JAVA:
            mainPage = new JavaJobScriptsExportWizardPage(selection);
            break;
        case PERL:
            mainPage = new JobScriptsExportWizardPage(selection);
            break;
        }
        addPage(mainPage);
    }

    /*
     * (non-Javadoc) Method declared on IWorkbenchWizard.
     */
    public void init(IWorkbench workbench, IStructuredSelection currentSelection) {
        this.selection = currentSelection;
        List selectedResources = IDE.computeSelectedResources(currentSelection);
        if (!selectedResources.isEmpty()) {
            this.selection = new StructuredSelection(selectedResources);
        }

        setWindowTitle(Messages.getString("JobScriptsExportWizard.exportJob"));
        setDefaultPageImageDescriptor(IDEWorkbenchPlugin.getIDEImageDescriptor("wizban/exportzip_wiz.png"));//$NON-NLS-1$
        setNeedsProgressMonitor(true);
    }

    /*
     * (non-Javadoc) Method declared on IWizard.
     */
    public boolean performFinish() {
        return mainPage.finish();
    }
}
