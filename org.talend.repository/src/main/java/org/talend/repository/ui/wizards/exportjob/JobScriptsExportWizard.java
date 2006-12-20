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
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * Job scripts export wizard.
 * <br/>
 *
 * $Id: JobScriptsExportWizard.java 1 2006-12-13 下午03:13:18 bqian
 *
 */
public class JobScriptsExportWizard extends Wizard implements IExportWizard {
    private IStructuredSelection selection;

    private JobScriptsExportWizardPage mainPage;

    /**
     * Creates a wizard for exporting workspace resources to a zip file.
     */
    public JobScriptsExportWizard() {
        AbstractUIPlugin plugin = (AbstractUIPlugin) Platform
                .getPlugin(PlatformUI.PLUGIN_ID);
        IDialogSettings workbenchSettings = plugin.getDialogSettings();
        IDialogSettings section = workbenchSettings
                .getSection("JobScriptsExportWizard");//$NON-NLS-1$
        if (section == null) {
            section = workbenchSettings.addNewSection("JobScriptsExportWizard");//$NON-NLS-1$
            section.put(JobScriptsExportWizardPage.STORE_SHELL_LAUNCHER_ID, true);
            section.put(JobScriptsExportWizardPage.STORE_SYSTEM_ROUTINE_ID, true);
            section.put(JobScriptsExportWizardPage.STORE_USER_ROUTINE_ID, true);
            section.put(JobScriptsExportWizardPage.STORE_MODEL_ID, true);
            section.put(JobScriptsExportWizardPage.STORE_JOB_ID,true);
            section.put(JobScriptsExportWizardPage.STORE_CONTEXT_ID, true);
        }
        setDialogSettings(section);
    }

    /* (non-Javadoc)
     * Method declared on IWizard.
     */
    public void addPages() {
        super.addPages();
        mainPage = new JobScriptsExportWizardPage(selection);
        addPage(mainPage);
    }

    /* (non-Javadoc)
     * Method declared on IWorkbenchWizard.
     */
    public void init(IWorkbench workbench, IStructuredSelection currentSelection) {
        this.selection = currentSelection;
        List selectedResources = IDE.computeSelectedResources(currentSelection);
        if (!selectedResources.isEmpty()) {
            this.selection = new StructuredSelection(selectedResources);
        }

        setWindowTitle(DataTransferMessages.DataTransfer_export);
        setDefaultPageImageDescriptor(IDEWorkbenchPlugin.getIDEImageDescriptor("wizban/exportzip_wiz.png"));//$NON-NLS-1$
        setNeedsProgressMonitor(true);
    }

    /* (non-Javadoc)
     * Method declared on IWizard.
     */
    public boolean performFinish() {
        return mainPage.finish();
    }
}
