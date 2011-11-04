// ============================================================================
//
// Copyright (C) 2006-2011 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.components.exchange.ui.actions;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Display;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.designer.components.exchange.jobs.RefreshJob;
import org.talend.designer.components.exchange.jobs.ShowContributedExtensionsJob;
import org.talend.designer.components.exchange.jobs.ShowInstalledExtensionsJob;
import org.talend.designer.components.exchange.model.ComponentExtension;
import org.talend.designer.components.exchange.ui.htmlcontent.ContentConstants;
import org.talend.designer.components.exchange.ui.views.ExchangeManager;
import org.talend.designer.components.exchange.util.ExchangeUtils;
import org.talend.designer.components.exchange.util.ExchangeWebService;

/**
 * DOC hcyi class global comment. Detailled comment
 */
public class RefreshComponenentsAction extends Action {

    public static final String REFRESH_ALL = "refresh_all";

    public static final String REFRESH_AVAILABLES = "refresh_availables";

    public static final String REFRESH_INSTALLED = "refresh_installed";

    public static final String REFRESH_MY_EXTENSIONS = "refresh_my_extensions";

    public void run(String refreshType) {
        if (refreshType == null || "".equals(REFRESH_ALL)) {
            refreshType = REFRESH_ALL;
        }

        if (REFRESH_ALL.equals(refreshType) || REFRESH_AVAILABLES.equals(refreshType)) {
            try {
                final RefreshJob job = new RefreshJob();
                job.addJobChangeListener(new JobChangeAdapter() {

                    @Override
                    public void done(final IJobChangeEvent event) {

                        Display.getDefault().syncExec(new Runnable() {

                            public void run() {
                                updateUI(job, event);
                                ExchangeManager.getInstance().generateXHTMLPage(ContentConstants.UL_LIST_AVAILABLE_EXTENSIONS,
                                        new String[] { ContentConstants.INSERT_DATA });
                            }
                        });
                    }
                });
                ExchangeUtils.scheduleUserJob(job);

            } catch (Exception e) {
                ExceptionHandler.process(e);

            }
        }

        // Show Installed Extensions
        if (REFRESH_ALL.equals(refreshType) || REFRESH_INSTALLED.equals(refreshType)) {
            try {
                final ShowInstalledExtensionsJob showInstalledJob = new ShowInstalledExtensionsJob();
                showInstalledJob.addJobChangeListener(new JobChangeAdapter() {

                    @Override
                    public void done(final IJobChangeEvent event) {

                        Display.getDefault().syncExec(new Runnable() {

                            public void run() {
                                updateInstalledUI(showInstalledJob, event);
                                ExchangeManager.getInstance().generateXHTMLPage(null,
                                        new String[] { ContentConstants.DOWNLOADEXTENSION_DATA });
                            }
                        });
                    }
                });
                ExchangeUtils.scheduleUserJob(showInstalledJob);

            } catch (Exception e) {
                ExceptionHandler.process(e);

            }
        }

        // Show Contributed Extensions
        if (REFRESH_ALL.equals(refreshType) || REFRESH_MY_EXTENSIONS.equals(refreshType)) {
            try {
                final ShowContributedExtensionsJob showContributedJob = new ShowContributedExtensionsJob();
                showContributedJob.addJobChangeListener(new JobChangeAdapter() {

                    @Override
                    public void done(final IJobChangeEvent event) {

                        Display.getDefault().syncExec(new Runnable() {

                            public void run() {
                                updateContributedUI(showContributedJob, event);
                                ExchangeManager.getInstance().generateXHTMLPage(null,
                                        new String[] { ContentConstants.LIST_MY_EXTENSION });
                            }
                        });
                    }
                });
                ExchangeUtils.scheduleUserJob(showContributedJob);

            } catch (Exception e) {
                ExceptionHandler.process(e);

            }
        }

        //
        getVersionRevisionsAndCategorys();
        // refreshHTML();
    }

    public void selectionChanged(IAction action, ISelection selection) {
    }

    private void getVersionRevisionsAndCategorys() {
        Display.getDefault().syncExec(new Runnable() {

            public void run() {
                ExchangeManager.getInstance().updateVersionRevisionsAndCategorys(
                        ExchangeWebService.searchVersionRevisionJSONArray(ExchangeUtils.TYPEEXTENSION),
                        ExchangeWebService.searchCategoryExtensionJSONArray(ExchangeUtils.TYPEEXTENSION));
            }
        });
    }

    private void refreshHTML() {
        Job job = new Job("generatehtml") {

            @Override
            protected IStatus run(IProgressMonitor monitor) {
                Display.getDefault().syncExec(new Runnable() {

                    public void run() {
                        ExchangeManager.getInstance().generateXHTMLPage();
                    }
                });
                return Status.OK_STATUS;
            }

        };
        ExchangeUtils.scheduleUserJob(job);

    }

    /**
     * Update ui after job finished.
     * 
     * @param action
     * @param job
     * @param event
     */
    private void updateUI(final RefreshJob job, final IJobChangeEvent event) {
        // activate action again after job finished

        if (event.getResult().isOK()) {
            // update exchange view
            List<ComponentExtension> extensions = job.getAvailableExtensions();
            ExchangeManager.getInstance().updateAvailableExtensions(extensions);
        }
    }

    /**
     * Update ui after job finished.
     * 
     * @param action
     * @param job
     * @param event
     */
    private void updateInstalledUI(final ShowInstalledExtensionsJob showInstalledJob, final IJobChangeEvent event) {
        if (event.getResult().isOK()) {
            // update exchange view
            List<ComponentExtension> extensions = showInstalledJob.getfInstalledExtensions();
            ExchangeManager.getInstance().updateInstalledExtensions(extensions);
        }
    }

    /**
     * 
     * @param action
     * @param showInstalledJob
     * @param event
     */
    private void updateContributedUI(final ShowContributedExtensionsJob showContributedJob, final IJobChangeEvent event) {
        if (event.getResult().isOK()) {
            // update exchange view
            List<ComponentExtension> extensions = showContributedJob.getfContributedExtensions();
            ExchangeManager.getInstance().updateContributedExtensions(extensions);
        }
    }
}
