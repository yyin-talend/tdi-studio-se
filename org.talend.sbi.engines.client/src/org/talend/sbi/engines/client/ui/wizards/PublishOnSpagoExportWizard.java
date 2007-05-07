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

package org.talend.sbi.engines.client.ui.wizards;

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
import org.eclipse.ui.internal.wizards.datatransfer.WizardFileSystemResourceExportPage1;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.sbi.engines.client.i18n.Messages;

/**
 * Publish SpagoBI export wizard. <br/>
 * 
 * $Id: PublishOnSpagoExportWizard.java 1 2007-04-27 11:30:00 cantoine
 * 
 */
public class PublishOnSpagoExportWizard extends Wizard implements IExportWizard {

    private IStructuredSelection selection;

    private WizardFileSystemResourceExportPage1 mainPage;

    /**
     * Creates a wizard for exporting workspace resources to a zip file.
     */
    public PublishOnSpagoExportWizard() {
        AbstractUIPlugin plugin = (AbstractUIPlugin) Platform.getPlugin(PlatformUI.PLUGIN_ID);
        IDialogSettings workbenchSettings = plugin.getDialogSettings();
        IDialogSettings section = workbenchSettings.getSection("PublishOnSpagoExportWizard"); //$NON-NLS-1$
        if (section == null) {
            section = workbenchSettings.addNewSection("PublishOnSpagoExportWizard"); //$NON-NLS-1$
            section.put(PerlPublishOnSpagoExportWizardPage.STORE_SHELL_LAUNCHER_ID, true);
            section.put(PerlPublishOnSpagoExportWizardPage.STORE_SYSTEM_ROUTINE_ID, true);
            section.put(PerlPublishOnSpagoExportWizardPage.STORE_USER_ROUTINE_ID, true);
            section.put(PerlPublishOnSpagoExportWizardPage.STORE_MODEL_ID, true);
            section.put(PerlPublishOnSpagoExportWizardPage.STORE_JOB_ID, true);
            section.put(PerlPublishOnSpagoExportWizardPage.STORE_CONTEXT_ID, true);
            
            section.put(JavaPublishOnSpagoExportWizardPage.STORE_SHELL_LAUNCHER_ID, true);
            section.put(JavaPublishOnSpagoExportWizardPage.STORE_SYSTEM_ROUTINE_ID, true);
            section.put(JavaPublishOnSpagoExportWizardPage.STORE_USER_ROUTINE_ID, true);
            section.put(JavaPublishOnSpagoExportWizardPage.STORE_MODEL_ID, true);
            section.put(JavaPublishOnSpagoExportWizardPage.STORE_JOB_ID, true);
            section.put(JavaPublishOnSpagoExportWizardPage.STORE_CONTEXT_ID, true);
            
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
            mainPage = new JavaPublishOnSpagoExportWizardPage(selection);
            break;
        case PERL:
            mainPage = new PerlPublishOnSpagoExportWizardPage(selection);
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

        setWindowTitle(Messages.getString("PublishOnSpagoExportWizard.publishJob"));
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
