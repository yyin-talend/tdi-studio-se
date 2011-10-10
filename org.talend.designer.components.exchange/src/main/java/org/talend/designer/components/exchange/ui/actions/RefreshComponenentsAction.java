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

import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.designer.components.exchange.jobs.RefreshJob;
import org.talend.designer.components.exchange.jobs.ShowInstalledExtensionsJob;
import org.talend.designer.components.exchange.model.ComponentExtension;
import org.talend.designer.components.exchange.ui.views.ExchangeView;
import org.talend.designer.components.exchange.util.ExchangeUtils;

/**
 * DOC hcyi class global comment. Detailled comment
 */
public class RefreshComponenentsAction implements IViewActionDelegate {

    public static final String ID = "org.talend.designer.components.exchange.ui.actions.RefreshComponenentsAction"; //$NON-NLS-1$

    private ExchangeView fView;

    public void init(IViewPart view) {
        fView = (ExchangeView) view;
    }

    public void run(final IAction action) {
        // avoid starting multiple action at the same time
        action.setEnabled(false);
        try {
            final RefreshJob job = new RefreshJob();
            job.addJobChangeListener(new JobChangeAdapter() {

                @Override
                public void done(final IJobChangeEvent event) {

                    Display.getDefault().syncExec(new Runnable() {

                        public void run() {
                            updateUI(action, job, event);
                        }
                    });
                }
            });
            ExchangeUtils.scheduleUserJob(job);

        } catch (Exception e) {
            ExceptionHandler.process(e);

        }

        try {
            final ShowInstalledExtensionsJob showInstalledJob = new ShowInstalledExtensionsJob();
            showInstalledJob.addJobChangeListener(new JobChangeAdapter() {

                @Override
                public void done(final IJobChangeEvent event) {

                    Display.getDefault().syncExec(new Runnable() {

                        public void run() {
                            updateInstalledUI(action, showInstalledJob, event);
                        }
                    });
                }
            });
            ExchangeUtils.scheduleUserJob(showInstalledJob);

        } catch (Exception e) {
            ExceptionHandler.process(e);

        }

    }

    public void selectionChanged(IAction action, ISelection selection) {
    }

    /**
     * Update ui after job finished.
     * 
     * @param action
     * @param job
     * @param event
     */
    private void updateUI(final IAction action, final RefreshJob job, final IJobChangeEvent event) {
        // activate action again after job finished
        action.setEnabled(true);

        if (event.getResult().isOK()) {
            // update exchange view
            List<ComponentExtension> extensions = job.getAvailableExtensions();
            fView.updateAvailableExtensions(extensions);
            fView.refresh();
        }
    }

    /**
     * Update ui after job finished.
     * 
     * @param action
     * @param job
     * @param event
     */
    private void updateInstalledUI(final IAction action, final ShowInstalledExtensionsJob showInstalledJob,
            final IJobChangeEvent event) {
        // activate action again after job finished
        action.setEnabled(true);

        if (event.getResult().isOK()) {
            // update exchange view
            List<ComponentExtension> extensions = showInstalledJob.getfInstalledExtensions();
            fView.updateInstalledExtensions(extensions);
            fView.refresh();
        }
    }
}
