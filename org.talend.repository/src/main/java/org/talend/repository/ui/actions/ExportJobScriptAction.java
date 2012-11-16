// ============================================================================
//
// Copyright (C) 2006-2012 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.ui.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISaveablePart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.internal.EditorManager;
import org.eclipse.ui.internal.WorkbenchMessages;
import org.eclipse.ui.internal.WorkbenchPage;
import org.talend.commons.ui.runtime.image.EImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.repository.RepositoryPlugin;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.ERepositoryStatus;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.IRepositoryNode.EProperties;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.preference.ExportJobTokenCollector;
import org.talend.repository.ui.wizards.exportjob.JobScriptsExportWizard;

/**
 * Action used to export job scripts. <br/>
 * 
 * $Id: ExportJobScriptAction.java 1 2006-12-13 下午03:12:05 bqian
 * 
 */
public class ExportJobScriptAction extends AContextualAction {

    protected static final String EXPORTJOBSCRIPTS = Messages.getString("ExportJobScriptAction.actionLabel"); //$NON-NLS-1$

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.actions.ITreeContextualAction#init(org.eclipse.jface.viewers.TreeViewer,
     * org.eclipse.jface.viewers.IStructuredSelection)
     */
    public void init(TreeViewer viewer, IStructuredSelection selection) {
        boolean canWork = true;
        if (selection.isEmpty()) {
            setEnabled(false);
            return;
        }
        List<RepositoryNode> nodes = selection.toList();
        for (RepositoryNode node : nodes) {
            if (node.getProperties(EProperties.CONTENT_TYPE) != ERepositoryObjectType.PROCESS) {
                canWork = false;
                break;
            }
            if (node.getType() != ENodeType.REPOSITORY_ELEMENT && node.getChildren().isEmpty()) {
                canWork = false;
                break;
            }

            if (canWork && node.getObject() != null
                    && ProxyRepositoryFactory.getInstance().getStatus(node.getObject()) == ERepositoryStatus.DELETED) {
                canWork = false;
                break;
            }
        }
        setEnabled(canWork);
    }

    @Override
    public boolean isVisible() {
        return isEnabled();
    }

    public ExportJobScriptAction() {
        super();
        this.setText(EXPORTJOBSCRIPTS);
        this.setToolTipText(EXPORTJOBSCRIPTS);
        this.setImageDescriptor(ImageProvider.getImageDesc(EImage.EXPORT_JOB_ICON));
    }

    @Override
    protected void doRun() {
        JobScriptsExportWizard processWizard = new JobScriptsExportWizard();
        IWorkbench workbench = getWorkbench();
        processWizard.setWindowTitle(EXPORTJOBSCRIPTS);
        processWizard.init(workbench, (IStructuredSelection) this.getSelection());

        Shell activeShell = Display.getCurrent().getActiveShell();
        WizardDialog dialog = new WizardDialog(activeShell, processWizard);
        // workbench.saveAllEditors(true);

        if (saveExportEditors(workbench)) {
            dialog.setPageSize(830, 500);
            dialog.open();

            // collector
            IPreferenceStore preferenceStore = RepositoryPlugin.getDefault().getPreferenceStore();
            int num = preferenceStore.getInt(ExportJobTokenCollector.TOS_COUNT_JOB_EXPORTS.getPrefKey());
            preferenceStore.setValue(ExportJobTokenCollector.TOS_COUNT_JOB_EXPORTS.getPrefKey(), num + 1);
        }
    }

    /**
     * 
     * Save the export editors if it is dirty.
     * 
     * @param workbench
     * @return
     */
    private boolean saveExportEditors(final IWorkbench workbench) {
        final boolean[] result = new boolean[1];
        result[0] = true;

        SafeRunner.run(new SafeRunnable(WorkbenchMessages.ErrorClosing) {

            @SuppressWarnings("restriction")
            public void run() {
                // Collect dirtyParts
                ArrayList dirtyParts = new ArrayList();
                ArrayList dirtyEditorsInput = new ArrayList();
                IWorkbenchWindow[] windows = workbench.getWorkbenchWindows();
                for (int i = 0; i < windows.length; i++) {
                    IWorkbenchPage[] pages = windows[i].getPages();
                    for (int j = 0; j < pages.length; j++) {
                        WorkbenchPage page = (WorkbenchPage) pages[j];

                        ISaveablePart[] parts = page.getDirtyParts();

                        for (int k = 0; k < parts.length; k++) {
                            ISaveablePart part = parts[k];

                            if (part.isSaveOnCloseNeeded()) {
                                if (part instanceof IEditorPart) {
                                    IEditorPart editor = (IEditorPart) part;
                                    IRepositoryNode node = getNode();
                                    boolean isdirty = node.getLabel().equals("Job Designs")
                                            || editor.getEditorInput().getName().equals(node.getLabel());
                                    if (!dirtyEditorsInput.contains(editor.getEditorInput()) && isdirty) {
                                        dirtyParts.add(editor);
                                        dirtyEditorsInput.add(editor.getEditorInput());
                                    }
                                } else {
                                    dirtyParts.add(part);
                                }
                            }
                        }
                    }
                }
                IShellProvider shellProvider;
                IRunnableContext runnableContext;
                IWorkbenchWindow w = workbench.getActiveWorkbenchWindow();
                if (w == null && windows.length > 0) {
                    w = windows[0];
                }
                if (w != null) {
                    shellProvider = w;
                    runnableContext = w;
                } else {
                    shellProvider = new IShellProvider() {

                        public Shell getShell() {
                            return null;
                        }
                    };
                    runnableContext = new ProgressMonitorDialog(null);
                }
                result[0] = EditorManager.saveAll(dirtyParts, true, false, true, runnableContext, shellProvider);
            }
        });
        return result[0];
    }
}
