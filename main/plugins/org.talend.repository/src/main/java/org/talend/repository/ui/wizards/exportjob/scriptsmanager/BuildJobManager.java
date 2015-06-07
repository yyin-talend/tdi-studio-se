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
package org.talend.repository.ui.wizards.exportjob.scriptsmanager;

import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.runtime.process.IBuildJobHandler;
import org.talend.repository.i18n.Messages;
import org.talend.repository.ui.wizards.exportjob.JavaJobScriptsExportWSWizardPage.JobExportType;
import org.talend.repository.ui.wizards.exportjob.scriptsmanager.JobScriptsManager.ExportChoice;

/**
 * created by ycbai on 2015年5月15日 Detailled comment
 *
 */
public class BuildJobManager {

    private static BuildJobManager instance = null;

    private BuildJobManager() {
    }

    public static synchronized BuildJobManager getInstance() {
        if (instance == null) {
            instance = new BuildJobManager();
        }
        return instance;
    }

    public void buildJob(String destinationPath, ProcessItem processItem, String version, String context,
            Map<ExportChoice, Object> exportChoiceMap, JobExportType jobExportType, IProgressMonitor monitor) throws Exception {
        IProgressMonitor pMonitor = new NullProgressMonitor();
        if (monitor != null) {
            pMonitor = monitor;
        }
        int scale = 1000;
        int total = 3;
        pMonitor.beginTask(Messages.getString("JobScriptsExportWizardPage.newExportJobScript", jobExportType), scale * total); //$NON-NLS-1$

        IBuildJobHandler buildJobHandler = BuildJobFactory.createBuildJobHandler(processItem, context, version, exportChoiceMap,
                jobExportType);
        pMonitor.setTaskName("generating item files......");
        buildJobHandler.generateItemFiles(true, new SubProgressMonitor(pMonitor, scale));
        pMonitor.worked(scale);
        pMonitor.setTaskName("generating job files......");
        buildJobHandler.generateJobFiles(new SubProgressMonitor(pMonitor, scale));
        pMonitor.worked(scale);
        pMonitor.setTaskName("building......");
        buildJobHandler.build(destinationPath, new SubProgressMonitor(pMonitor, scale));
        pMonitor.worked(scale);
        pMonitor.done();
    }

}
