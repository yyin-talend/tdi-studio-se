// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.preference;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.talend.commons.exception.LoginException;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.core.CorePlugin;
import org.talend.core.PluginChecker;
import org.talend.core.model.general.Project;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.repository.ProjectManager;
import org.talend.repository.RepositoryWorkUnit;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.IProxyRepositoryFactory;


/**
 * DOC wzhang class global comment. Detailled comment
 */
public class SecurityProjectSettingPage extends ProjectSettingPage {

    private static final String MIGRATION_TOKEN_KEY = "force_import_unsupported_job";

    private Button button;

    private Button forceRegenSignatureBtn;

    private Boolean hasMigrationToken;

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Control createContents(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        FormLayout layout = new FormLayout();
        composite.setLayout(layout);

        button = new Button(composite, SWT.CHECK | SWT.TOP);
        button.setText(Messages.getString("SecurityProjectSettingPage.hidePass")); //$NON-NLS-1$
        FormData layoutData = new FormData();
        layoutData.top = new FormAttachment(0);
        layoutData.left = new FormAttachment(0);
        button.setLayoutData(layoutData);

        button.setSelection(pro.getEmfProject().isHidePassword());
        IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
        if (factory.isUserReadOnlyOnCurrentProject()) {
            composite.setEnabled(false);
        }

        if (PluginChecker.isTIS() && hasMigrationToken()) {
            layoutForceRegenSignature(composite);
        }
        return composite;
    }

    private boolean hasMigrationToken() {
        if (hasMigrationToken != null) {
            return hasMigrationToken;
        }
        try {
            hasMigrationToken = StringUtils.isNotBlank(System.getProperty(MIGRATION_TOKEN_KEY));
        } catch (Exception e) {
            hasMigrationToken = false;
            ExceptionHandler.process(e);
        }
        return hasMigrationToken;
    }

    private void layoutForceRegenSignature(Composite parent) {
        FormData layoutData = new FormData();
        forceRegenSignatureBtn = new Button(parent, SWT.PUSH);
        forceRegenSignatureBtn.setText(Messages.getString("SecurityProjectSettingPage.forceRegenSignature")); //$NON-NLS-1$
        layoutData = new FormData();
        layoutData.top = new FormAttachment(button, 15, SWT.BOTTOM);
        layoutData.left = new FormAttachment(button, 0, SWT.LEFT);
        forceRegenSignatureBtn.setLayoutData(layoutData);
        forceRegenSignatureBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                onForceRegenSignatureBtnSelected(e);
            }

        });

        Label note = new Label(parent, SWT.NONE);
        note.setText(Messages.getString("SecurityProjectSettingPage.forceRegenSignature.warn")); //$NON-NLS-1$
        note.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
        layoutData = new FormData();
        layoutData.top = new FormAttachment(forceRegenSignatureBtn, 5, SWT.BOTTOM);
        layoutData.left = new FormAttachment(forceRegenSignatureBtn, 0, SWT.LEFT);
        note.setLayoutData(layoutData);
    }

    private void onForceRegenSignatureBtnSelected(SelectionEvent event) {
        final Job forceRegenSignatureJob[] = new Job[1];
        final IRunnableWithProgress forceRegenSignatureProgress = new IRunnableWithProgress() {

            @Override
            public void run(IProgressMonitor progressMonitor) throws InvocationTargetException, InterruptedException {
                forceRegenSignatureJob[0] = new Job(Messages.getString("SecurityProjectSettingPage.forceRegenSignature")) { //$NON-NLS-1$

                    @Override
                    protected IStatus run(IProgressMonitor m) {
                        regenerateSignature(progressMonitor);
                        return Status.OK_STATUS;
                    }

                    @Override
                    protected void canceling() {
                        Thread thread = getThread();
                        if (thread != null) {
                            thread.interrupt();
                        }
                        super.canceling();
                    }

                };
                forceRegenSignatureJob[0].setUser(false);
                forceRegenSignatureJob[0].setSystem(true);
                forceRegenSignatureJob[0].schedule();
                forceRegenSignatureJob[0].join();
            }
        };

        ProgressMonitorDialog dialog = new ProgressMonitorDialog(Display.getDefault().getActiveShell()) {

            @Override
            protected void cancelPressed() {
                super.cancelPressed();
                if (forceRegenSignatureJob[0] != null) {
                    forceRegenSignatureJob[0].cancel();
                }
            }

        };
        try {
            dialog.run(true, true, forceRegenSignatureProgress);
        } catch (Throwable e) {
            ExceptionHandler.process(e);
        }

    }

    private void regenerateSignature(IProgressMonitor monitor) {
        IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
        Project currentProject = ProjectManager.getInstance().getCurrentProject();
        RepositoryWorkUnit rwu = new RepositoryWorkUnit(currentProject,
                Messages.getString("SecurityProjectSettingPage.forceRegenSignature")) { //$NON-NLS-1$

            @Override
            protected void run() throws LoginException, PersistenceException {

                final IWorkspaceRunnable op = new IWorkspaceRunnable() {

                    public void run(IProgressMonitor monitor) throws CoreException {
                        Thread currentThread = Thread.currentThread();
                        List<ERepositoryObjectType> repoObjTypes = Arrays
                                .asList((ERepositoryObjectType[]) ERepositoryObjectType.values());
                        int repoObjTypeSize = repoObjTypes.size();
                        monitor.beginTask(Messages.getString("SecurityProjectSettingPage.forceRegenSignature.progress.begin"), //$NON-NLS-1$
                                repoObjTypeSize);
                        for (ERepositoryObjectType repoObjType : repoObjTypes) {
                            if (monitor.isCanceled() || currentThread.isInterrupted()) {
                                ExceptionHandler.log(
                                        Messages.getString("SecurityProjectSettingPage.forceRegenSignature.progress.cancel")); //$NON-NLS-1$
                                return;
                            }
                            monitor.setTaskName(
                                    Messages.getString("SecurityProjectSettingPage.forceRegenSignature.progress.curRepoType", //$NON-NLS-1$
                                            repoObjType.getLabel()));
                            try {
                                monitor.subTask(
                                        Messages.getString("SecurityProjectSettingPage.forceRegenSignature.progress.collect", //$NON-NLS-1$
                                                repoObjType.getLabel()));
                                List<IRepositoryViewObject> repoObjs = factory.getAll(currentProject, repoObjType, true, true);
                                if (monitor.isCanceled() || currentThread.isInterrupted()) {
                                    ExceptionHandler.log(
                                            Messages.getString("SecurityProjectSettingPage.forceRegenSignature.progress.cancel")); //$NON-NLS-1$
                                    return;
                                }
                                if (repoObjs != null) {
                                    int repoObjSize = repoObjs.size();
                                    Iterator<IRepositoryViewObject> iterator = repoObjs.iterator();
                                    for (int i = 0; iterator.hasNext(); i++) {
                                        if (monitor.isCanceled() || currentThread.isInterrupted()) {
                                            ExceptionHandler.log(Messages
                                                    .getString("SecurityProjectSettingPage.forceRegenSignature.progress.cancel")); //$NON-NLS-1$
                                            return;
                                        }
                                        IRepositoryViewObject next = iterator.next();
                                        if (next == null) {
                                            continue;
                                        }
                                        monitor.subTask(
                                                Messages.getString("SecurityProjectSettingPage.forceRegenSignature.progress.save", //$NON-NLS-1$
                                                        repoObjType, i + "/" + repoObjSize, next.getLabel()));
                                        try {
                                            factory.save(next.getProperty().getItem());
                                        } catch (Exception e) {
                                            ExceptionHandler.process(e);
                                        }
                                    }
                                }
                            } catch (PersistenceException e) {
                                ExceptionHandler.process(e);
                            } finally {
                                monitor.worked(1);
                            }
                        }
                    }
                };

                IWorkspace workspace = ResourcesPlugin.getWorkspace();
                ISchedulingRule refreshRule = workspace.getRuleFactory()
                        .refreshRule(ProjectManager.getInstance().getResourceProject(currentProject.getEmfProject()));
                try {
                    workspace.run(op, refreshRule, IWorkspace.AVOID_UPDATE, monitor);
                } catch (CoreException e) {
                    throw new PersistenceException(e);
                }

            }
        };
        factory.executeRepositoryWorkUnit(rwu);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.PreferencePage#performApply()
     */
    @Override
    protected void performApply() {
        super.performApply();
        apply();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.PreferencePage#performOk()
     */
    @Override
    public boolean performOk() {
        apply();
        return super.performOk();
    }

    protected void apply() {
        if (button != null) {
            if (button.getSelection() == pro.getEmfProject().isHidePassword()) {
                return;
            }
            pro.getEmfProject().setHidePassword(button.getSelection());
        }
        IProxyRepositoryFactory prf = CorePlugin.getDefault().getProxyRepositoryFactory();

        try {
            prf.saveProject(pro);
        } catch (Exception ex) {
            ExceptionHandler.process(ex);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.preference.ProjectSettingPage#refresh()
     */
    @Override
    public void refresh() {
        if (button != null && !button.isDisposed()) {
            button.setSelection(pro.getEmfProject().isHidePassword());
        }
    }

    @Override
    protected void performDefaults() {
        super.performDefaults();
        button.setSelection(false);
    }
}
