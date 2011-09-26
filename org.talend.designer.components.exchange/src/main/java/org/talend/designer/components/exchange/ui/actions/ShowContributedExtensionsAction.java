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
import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Display;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.designer.components.exchange.jobs.ShowContributedExtensionsJob;
import org.talend.designer.components.exchange.model.ComponentExtension;
import org.talend.designer.components.exchange.ui.views.ExchangeView;
import org.talend.designer.components.exchange.util.ExchangeUtils;

/**
 * DOC hcyi class global comment. Detailled comment
 */
public class ShowContributedExtensionsAction extends Action {

    public void run() {
        try {
            final ShowContributedExtensionsJob showContributedJob = new ShowContributedExtensionsJob();
            showContributedJob.addJobChangeListener(new JobChangeAdapter() {

                @Override
                public void done(final IJobChangeEvent event) {

                    Display.getDefault().syncExec(new Runnable() {

                        public void run() {
                            updateUI(showContributedJob, event);
                        }
                    });
                }
            });
            ExchangeUtils.scheduleUserJob(showContributedJob);

        } catch (Exception e) {
            ExceptionHandler.process(e);

        }
    }

    /**
     * Update ui after job finished.
     * 
     * @param action
     * @param job
     * @param event
     */
    private void updateUI(final ShowContributedExtensionsJob showContributedJob, final IJobChangeEvent event) {

        if (event.getResult().isOK()) {
            // update exchange view
            List<ComponentExtension> extensions = showContributedJob.getfContributedExtensions();
            if (ExchangeUtils.getExchangeView() != null) {
                ExchangeUtils.getExchangeView().setfContributedExtensions(extensions);
            }
        }
    }
}
