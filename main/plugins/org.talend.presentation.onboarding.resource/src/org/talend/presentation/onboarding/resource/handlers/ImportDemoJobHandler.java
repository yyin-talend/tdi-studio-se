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
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.widgets.Shell;
import org.osgi.framework.Bundle;
import org.talend.commons.exception.CommonExceptionHandler;
import org.talend.presentation.onboarding.resource.utils.OnBoardingResourceConstants;
import org.talend.presentation.onboarding.resource.utils.OnBoardingResourceUtil;
import org.talend.presentation.onboarding.resource.utils.TalendImportUtil;

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
        new Thread(new Runnable() {

            @Override
            public void run() {

                try {
                    Bundle bundle = Platform.getBundle(pluginId);
                    URL resourceURL = bundle.getEntry(zipPath);
                    boolean isSucceed = false;
                    try {
                        String demoZipPath = FileLocator.toFileURL(resourceURL).getPath();
                        isSucceed = TalendImportUtil.importItems(demoZipPath, new NullProgressMonitor(), false, true, false);
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
            }
        }).start();
    }
}
