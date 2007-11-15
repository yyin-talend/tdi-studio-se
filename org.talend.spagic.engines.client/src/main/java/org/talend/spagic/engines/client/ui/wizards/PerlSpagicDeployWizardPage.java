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
package org.talend.spagic.engines.client.ui.wizards;

import java.util.List;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.talend.repository.ui.wizards.exportjob.ArchiveFileExportOperationFullPath;
import org.talend.repository.ui.wizards.exportjob.ExportFileResource;
import org.talend.repository.ui.wizards.exportjob.scriptsmanager.JobScriptsManager;

/**
 * Page of the Job Scripts Export Wizard. <br/>
 * 
 * @referto WizardArchiveFileResourceExportPage1 $Id: JobScriptsExportWizardPage.java 1 2006-12-13 涓嬪�?3:09:07 bqian
 * 
 */
public class PerlSpagicDeployWizardPage extends SpagicDeployWizardPage {

    // dialog store id constants
    public static final String STORE_SHELL_LAUNCHER_ID = "PerlSapgicDeployWizardPage.STORE_SHELL_LAUNCHER_ID"; //$NON-NLS-1$

    public static final String STORE_SYSTEM_ROUTINE_ID = "PerlSapgicDeployWizardPage.STORE_SYSTEM_ROUTINE_ID"; //$NON-NLS-1$

    public static final String STORE_USER_ROUTINE_ID = "PerlSapgicDeployWizardPage.STORE_USER_ROUTINE_ID"; //$NON-NLS-1$

    public static final String STORE_MODEL_ID = "PerlSapgicDeployWizardPage.STORE_MODEL_ID"; //$NON-NLS-1$

    public static final String STORE_JOB_ID = "PerlSapgicDeployWizardPage.STORE_JOB_ID"; //$NON-NLS-1$

    public static final String STORE_CONTEXT_ID = "PerlSapgicDeployWizardPage.STORE_CONTEXT_ID"; //$NON-NLS-1$

    public static final String APPLY_TO_CHILDREN_ID = "PerlSapgicDeployWizardPage.APPLY_TO_CHILDREN_ID"; //$NON-NLS-1$

    // public static final String STORE_GENERATECODE_ID = "PerlJobScriptsExportWizardPage.STORE_GENERATECODE_ID";
    // //$NON-NLS-1$

    public static final String STORE_SOURCE_ID = "PerlSapgicDeployWizardPage.STORE_SOURCE_ID"; //$NON-NLS-1$

    private static final String STORE_DESTINATION_NAMES_ID = "PerlSapgicDeployWizardPage.STORE_DESTINATION_NAMES_ID"; //$NON-NLS-1$

    protected JobScriptsManager createJobScriptsManager() {
        return new SpagicPerlDeployManager();
    }

    /**
     * Create an instance of this class.
     * 
     * @param selection the selection
     */
    public PerlSpagicDeployWizardPage(IStructuredSelection selection) {
        super("PerlSapgicDeployWizardPage1", selection); //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.wizards.exportjob.JobScriptsExportWizardPage#getExporterOperation(java.util.List)
     */
    @Override
    protected ArchiveFileExportOperationFullPath getExporterOperation(List<ExportFileResource> resourcesToExport) {
        ArchiveFileExportOperationFullPath operation = super.getExporterOperation(resourcesToExport);
        operation.setRegEx(".*.pl$|.*.pm$|.*.bat$|.*.sh$");
        return operation;
    }

    /**
     * Hook method for saving widget values for restoration by the next instance of this class.
     */
    protected void internalSaveWidgetValues() {
        // update directory names history
        IDialogSettings settings = getDialogSettings();
        if (settings != null) {
            String[] directoryNames = settings.getArray(STORE_DESTINATION_NAMES_ID);
            if (directoryNames == null) {
                directoryNames = new String[0];
            }

            directoryNames = addToHistory(directoryNames, getDestinationValue());
            settings.put(STORE_DESTINATION_NAMES_ID, directoryNames);
            settings.put(STORE_SHELL_LAUNCHER_ID, shellLauncherButton.getSelection());
            settings.put(STORE_SYSTEM_ROUTINE_ID, systemRoutineButton.getSelection());
            settings.put(STORE_USER_ROUTINE_ID, userRoutineButton.getSelection());
            settings.put(STORE_MODEL_ID, modelButton.getSelection());
            settings.put(STORE_JOB_ID, jobButton.getSelection());
            settings.put(STORE_SOURCE_ID, sourceButton.getSelection());
            settings.put(STORE_CONTEXT_ID, contextButton.getSelection());
            settings.put(APPLY_TO_CHILDREN_ID, applyToChildrenButton.getSelection());
            // settings.put(STORE_GENERATECODE_ID, genCodeButton.getSelection());
        }
    }

    /**
     * Hook method for restoring widget values to the values that they held last time this wizard was used to
     * completion.
     */
    protected void restoreWidgetValues() {
        IDialogSettings settings = getDialogSettings();
        if (settings != null) {
            String[] directoryNames = settings.getArray(STORE_DESTINATION_NAMES_ID);
            if (directoryNames != null) {
                // destination
                setDestinationValue(directoryNames[0]);
                for (int i = 0; i < directoryNames.length; i++) {
                    addDestinationItem(directoryNames[i]);
                }
            }
            shellLauncherButton.setSelection(settings.getBoolean(STORE_SHELL_LAUNCHER_ID));
            systemRoutineButton.setSelection(settings.getBoolean(STORE_SYSTEM_ROUTINE_ID));
            userRoutineButton.setSelection(settings.getBoolean(STORE_USER_ROUTINE_ID));
            modelButton.setSelection(settings.getBoolean(STORE_MODEL_ID));
            jobButton.setSelection(settings.getBoolean(STORE_JOB_ID));
            sourceButton.setSelection(settings.getBoolean(STORE_SOURCE_ID));
            contextButton.setSelection(settings.getBoolean(STORE_CONTEXT_ID));
            applyToChildrenButton.setSelection(settings.getBoolean(APPLY_TO_CHILDREN_ID));
            // genCodeButton.setSelection(settings.getBoolean(STORE_GENERATECODE_ID));
        }

        launcherCombo.setItems(manager.getLauncher());
        if (manager.getLauncher().length > 0) {
            launcherCombo.select(0);
        }
        if (process.length > 0) {
            List<String> contextNames = manager.getJobContexts(process[0].getProcess());
            contextCombo.setItems(contextNames.toArray(new String[contextNames.size()]));
            if (contextNames.size() > 0) {
                contextCombo.select(0);
            }
        }

    }
}
