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

import java.net.URL;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.Bundle;
import org.talend.commons.exception.CommonExceptionHandler;
import org.talend.presentation.onboarding.resource.i18n.Messages;
import org.talend.presentation.onboarding.resource.utils.OnBoardingResourceConstants;
import org.talend.presentation.onboarding.resource.utils.OnBoardingResourceUtil;
import org.talend.presentation.onboarding.resource.utils.TalendImportUtil;
import org.talend.repository.ui.dialog.AProgressMonitorDialogWithCancel;

public class ImportDemoJobHandler extends AbstractHandler {

    public static final String PARAM_PLUGIN_ID = "org.talend.presentation.onboarding.resource.importdemojob.pluginid"; //$NON-NLS-1$

    public static final String PARAM_ZIP_PATH = "org.talend.presentation.onboarding.resource.importdemojob.zippath"; //$NON-NLS-1$

    private Shell shell;

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        String pluginId = event.getParameter(PARAM_PLUGIN_ID);
        String zipPath = event.getParameter(PARAM_ZIP_PATH);
        if (pluginId == null || pluginId.isEmpty() || zipPath == null || zipPath.isEmpty()) {
            return null;
        }

        boolean alreadyImportDemoJob = OnBoardingResourceUtil.getPreferenceStore().getBoolean(
                OnBoardingResourceConstants.PREFERENCE_ALREADY_IMPORT_DEMO_JOB);
        if (alreadyImportDemoJob) {
            return null;
        }

        doImport(pluginId, zipPath);

        return null;
    }

    private void doImport(final String pluginId, final String zipPath) {
        Display.getDefault().syncExec(new Runnable() {

            @Override
            public void run() {
                shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
            }
        });
        AProgressMonitorDialogWithCancel<Boolean> dialog = new AProgressMonitorDialogWithCancel<Boolean>(shell) {

            @Override
            protected Boolean runWithCancel(IProgressMonitor monitor) throws Throwable {
                try {
                    Bundle bundle = Platform.getBundle(pluginId);
                    URL resourceURL = bundle.getEntry(zipPath);
                    boolean isSucceed = false;
                    try {
                        String demoZipPath = FileLocator.toFileURL(resourceURL).getPath();
                        isSucceed = TalendImportUtil.importItems(demoZipPath, monitor, false, true);
                    } catch (Throwable e) {
                        CommonExceptionHandler.process(e);
                    }
                    if (isSucceed) {
                        OnBoardingResourceUtil.getPreferenceStore().setValue(
                                OnBoardingResourceConstants.PREFERENCE_ALREADY_IMPORT_DEMO_JOB, true);
                    }
                } catch (Throwable e) {
                    CommonExceptionHandler.process(e);
                }
                return null;
            }
        };
        try {
            dialog.run(
                    Messages.getString("importDemoJobHandler.import.excuting"), //$NON-NLS-1$
                    Messages.getString("importDemoJobHandler.import.waitingFinish"), true, AProgressMonitorDialogWithCancel.ENDLESS_WAIT_TIME); //$NON-NLS-1$
        } catch (Throwable e) {
            CommonExceptionHandler.process(e);
        }
    }
}
