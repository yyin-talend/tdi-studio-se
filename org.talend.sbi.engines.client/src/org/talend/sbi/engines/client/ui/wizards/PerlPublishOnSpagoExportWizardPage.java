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
package org.talend.sbi.engines.client.ui.wizards;

import java.util.List;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.talend.repository.ui.wizards.exportjob.ArchiveFileExportOperationFullPath;
import org.talend.repository.ui.wizards.exportjob.ExportFileResource;
import org.talend.repository.ui.wizards.exportjob.scriptsmanager.JobScriptsManager;

/**
 * Page of the Job Publish SpagoBI Export Wizard. <br/>
 * 
 * @referto WizardArchiveFileResourceExportPage1 $Id: PerlPublishOnSpagoExportWizardPage.java 1 2007-04-26 14:22:07 cantoine
 * 
 */
public class PerlPublishOnSpagoExportWizardPage extends PublishOnSpagoExportWizardPage {

    // dialog store id constants
    public static final String STORE_SHELL_LAUNCHER_ID = "PerlJobScriptsExportWizardPage.STORE_SHELL_LAUNCHER_ID"; //$NON-NLS-1$

    public static final String STORE_SYSTEM_ROUTINE_ID = "PerlJobScriptsExportWizardPage.STORE_SYSTEM_ROUTINE_ID"; //$NON-NLS-1$

    public static final String STORE_USER_ROUTINE_ID = "PerlJobScriptsExportWizardPage.STORE_USER_ROUTINE_ID"; //$NON-NLS-1$

    public static final String STORE_MODEL_ID = "PerlJobScriptsExportWizardPage.STORE_MODEL_ID"; //$NON-NLS-1$

    public static final String STORE_JOB_ID = "PerlJobScriptsExportWizardPage.STORE_JOB_ID"; //$NON-NLS-1$

    public static final String STORE_CONTEXT_ID = "PerlJobScriptsExportWizardPage.STORE_CONTEXT_ID"; //$NON-NLS-1$

    // public static final String STORE_GENERATECODE_ID = "PerlJobScriptsExportWizardPage.STORE_GENERATECODE_ID";
    // //$NON-NLS-1$

    public static final String STORE_SOURCE_ID = "PerlJobScriptsExportWizardPage.STORE_SOURCE_ID"; //$NON-NLS-1$

    private static final String STORE_DESTINATION_NAMES_ID = "PerlJobScriptsExportWizardPage.STORE_DESTINATION_NAMES_ID"; //$NON-NLS-1$

    /**
     * Create an instance of this class.
     * 
     * @param selection the selection
     */
    public PerlPublishOnSpagoExportWizardPage(IStructuredSelection selection) {
        super("PerlPublishOnSpagoExportPage1", selection); //$NON-NLS-1$
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
            settings.put(STORE_SHELL_LAUNCHER_ID, true);
            settings.put(STORE_SYSTEM_ROUTINE_ID, true);
            settings.put(STORE_USER_ROUTINE_ID, true);
            settings.put(STORE_MODEL_ID, true);
            settings.put(STORE_JOB_ID, true);
            settings.put(STORE_SOURCE_ID, false);
            settings.put(STORE_CONTEXT_ID, contextButton.getSelection());
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
//            shellLauncherButton.setSelection(settings.getBoolean(STORE_SHELL_LAUNCHER_ID));
//            systemRoutineButton.setSelection(settings.getBoolean(STORE_SYSTEM_ROUTINE_ID));
//            userRoutineButton.setSelection(settings.getBoolean(STORE_USER_ROUTINE_ID));
//            modelButton.setSelection(settings.getBoolean(STORE_MODEL_ID));
//            jobButton.setSelection(settings.getBoolean(STORE_JOB_ID));
//            sourceButton.setSelection(settings.getBoolean(STORE_SOURCE_ID));
            contextButton.setSelection(settings.getBoolean(STORE_CONTEXT_ID));
            // genCodeButton.setSelection(settings.getBoolean(STORE_GENERATECODE_ID));
        }

//        launcherCombo.setItems(manager.getLauncher());
//        if (manager.getLauncher().length > 0) {
//            launcherCombo.select(0);
//        }
        if (process.length > 0) {
            List<String> contextNames = manager.getJobContexts(process[0].getProcess());
            contextCombo.setItems(contextNames.toArray(new String[contextNames.size()]));
            if (contextNames.size() > 0) {
                contextCombo.select(0);
            }
        }
    }
}
