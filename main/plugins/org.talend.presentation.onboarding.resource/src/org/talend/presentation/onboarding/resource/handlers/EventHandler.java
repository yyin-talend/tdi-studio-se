// ============================================================================
//
// Copyright (C) 2006-2015 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.presentation.onboarding.resource.handlers;

import org.apache.log4j.Priority;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.talend.commons.exception.CommonExceptionHandler;
import org.talend.presentation.onboarding.resource.utils.OnBoardingResourceUtil;
import org.talend.updates.runtime.ui.ShowWizardHandler;

public class EventHandler extends AbstractHandler {

    public static final String EVENT_TYPE = "org.talend.presentation.onboarding.resource.handleevent.type"; //$NON-NLS-1$

    public static final String EVENT_TYPE_ON_OPEN = "ON_OPEN"; //$NON-NLS-1$

    public static final String EVENT_TYPE_ON_CLOSE = "ON_CLOSE"; //$NON-NLS-1$

    private static final String PREFERENCE_ONBOARDING_RESOURCE_NOT_FIRSTTIME_SHOW = "ONBOARDING_RESOURCE_NOT_FIRSTTIME_SHOW"; //$NON-NLS-1$

    /**
     * used to prevent the update dialog to popup during the presentation; the update will popup after the presentation
     * closed
     */
    private static Thread lockThread = createLockThread();

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {

        String eventType = event.getParameter(EVENT_TYPE);
        boolean isNotFirstTimeShow = OnBoardingResourceUtil.getPreferenceStore().getBoolean(
                PREFERENCE_ONBOARDING_RESOURCE_NOT_FIRSTTIME_SHOW);
        if (!isNotFirstTimeShow) {
            OnBoardingResourceUtil.getPreferenceStore().setValue(PREFERENCE_ONBOARDING_RESOURCE_NOT_FIRSTTIME_SHOW, true);
            if (EVENT_TYPE_ON_OPEN.equals(eventType)) {
                synchronized (lockThread) {
                    if (lockThread.isAlive()) {
                        CommonExceptionHandler.process(new Throwable(
                                "thread should already be interrupted while it is still alive.")); //$NON-NLS-1$
                    } else {
                        // in case it will be used many times
                        lockThread = createLockThread();
                        lockThread.start();
                    }
                }
            }
        }
        if (EVENT_TYPE_ON_CLOSE.equals(eventType)) {
            synchronized (lockThread) {
                lockThread.interrupt();
            }
        }
        return null;
    }

    private static Thread createLockThread() {
        return new Thread(new Runnable() {

            @Override
            public void run() {
                synchronized (ShowWizardHandler.showWizardLock) {
                    while (true) {
                        if (Thread.currentThread().isInterrupted()) {
                            break;
                        }
                        try {
                            Thread.sleep(10000000);
                        } catch (InterruptedException e) {
                            CommonExceptionHandler.process(e, Priority.INFO);
                            break;
                        }
                    }
                }
            }
        });
    }
}
