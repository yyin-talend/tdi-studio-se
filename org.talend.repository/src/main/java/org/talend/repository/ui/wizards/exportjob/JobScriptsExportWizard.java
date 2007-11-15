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
import org.eclipse.ui.internal.wizards.datatransfer.WizardFileSystemResourceExportPage1;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.designer.runprocess.ProcessorUtilities;
import org.talend.repository.i18n.Messages;

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
            section.put(PerlJobScriptsExportWizardPage.STORE_SHELL_LAUNCHER_ID, true);
            section.put(PerlJobScriptsExportWizardPage.STORE_SYSTEM_ROUTINE_ID, true);
            section.put(PerlJobScriptsExportWizardPage.STORE_USER_ROUTINE_ID, true);
            section.put(PerlJobScriptsExportWizardPage.STORE_MODEL_ID, true);
            section.put(PerlJobScriptsExportWizardPage.STORE_JOB_ID, true);
            section.put(PerlJobScriptsExportWizardPage.STORE_CONTEXT_ID, true);
            section.put(PerlJobScriptsExportWizardPage.APPLY_TO_CHILDREN_ID, true);

            section.put(JavaJobScriptsExportWizardPage.STORE_SHELL_LAUNCHER_ID, true);
            section.put(JavaJobScriptsExportWizardPage.STORE_SYSTEM_ROUTINE_ID, true);
            section.put(JavaJobScriptsExportWizardPage.STORE_USER_ROUTINE_ID, true);
            section.put(JavaJobScriptsExportWizardPage.STORE_MODEL_ID, true);
            section.put(JavaJobScriptsExportWizardPage.STORE_JOB_ID, true);
            section.put(JavaJobScriptsExportWizardPage.STORE_CONTEXT_ID, true);
            section.put(JavaJobScriptsExportWizardPage.APPLY_TO_CHILDREN_ID, true);
            section.put(JavaJobScriptsExportWSWizardPage.STORE_EXPORTTYPE_ID, "POJO");
            section.put(JavaJobScriptsExportWSWizardPage.STORE_WEBXML_ID, true);
            section.put(JavaJobScriptsExportWSWizardPage.STORE_CONFIGFILE_ID, true);
            section.put(JavaJobScriptsExportWSWizardPage.STORE_AXISLIB_ID, true);
            section.put(JavaJobScriptsExportWSWizardPage.STORE_WSDD_ID, true);
            section.put(JavaJobScriptsExportWSWizardPage.STORE_WSDL_ID, true);

            // section.put(JobScriptsExportWizardPage.STORE_GENERATECODE_ID, true);
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
            mainPage = new JavaJobScriptsExportWSWizardPage(selection);
            break;
        case PERL:
            mainPage = new PerlJobScriptsExportWizardPage(selection);
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

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.Wizard#performCancel()
     */
    @Override
    public boolean performCancel() {
        ProcessorUtilities.resetExportConfig();
        return true;
    }
}
