// ============================================================================
//
// Copyright (C) 2006-2014 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.runprocess.java;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.talend.commons.utils.io.FilesUtils;
import org.talend.core.CorePlugin;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.Property;
import org.talend.core.prefs.ITalendCorePrefConstants;
import org.talend.designer.core.model.utils.emf.talendfile.ContextParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.designer.core.runprocess.Processor;
import org.talend.designer.runprocess.IProcessor;
import org.talend.designer.runprocess.ProcessorException;
import org.talend.designer.runprocess.ProcessorUtilities;
import org.talend.repository.documentation.ArchiveFileExportOperationFullPath;
import org.talend.repository.documentation.ExportFileResource;
import org.talend.repository.ui.wizards.exportjob.JavaJobScriptsExportWSWizardPage.JobExportType;
import org.talend.repository.ui.wizards.exportjob.scriptsmanager.JobScriptsManager;
import org.talend.repository.ui.wizards.exportjob.scriptsmanager.JobScriptsManager.ExportChoice;
import org.talend.repository.ui.wizards.exportjob.scriptsmanager.JobScriptsManagerFactory;

/**
 * created by Administrator on 2013-3-15 Detailled comment
 * 
 */
public class ExportProcessorHelper {

    protected String processName = null;

    public String exportJob(Processor processor, int statisticsPort, int tracePort, String watchParam,
            final IProgressMonitor progressMonitor) throws ProcessorException {
        Map<ExportChoice, Object> exportChoiceMap = JobScriptsManagerFactory.getDefaultExportChoiceMap();
        ProcessItem processItem = (ProcessItem) processor.getProperty().getItem();
        processName = processor.getProperty().getLabel();
        ExportFileResource fileResource = new ExportFileResource(processItem, processName);
        ExportFileResource[] exportFileResources = new ExportFileResource[] { fileResource };

        if (progressMonitor.isCanceled()) {
            throw new ProcessorException(new InterruptedException());
        }
        // update directly the .item (without save it) in case of prompt
        // then the generation will be correct automatically in the .properties
        IContext context = processor.getContext();
        if (context != null) {
            List<IContextParameter> contextParameterList = context.getContextParameterList();
            if (contextParameterList != null && contextParameterList.size() > 0) {
                for (IContextParameter contextParameter : contextParameterList) {
                    if (!contextParameter.isPromptNeeded()) {
                        continue;
                    }
                    for (Object curCType : processItem.getProcess().getContext()) {
                        ContextType cType = (ContextType) curCType;
                        if (context.getName().equals(cType.getName())) {
                            for (Object curParam : cType.getContextParameter()) {
                                ContextParameterType cParamType = (ContextParameterType) curParam;
                                if (contextParameter.getName().equals(cParamType.getName())) {
                                    cParamType.setRawValue(contextParameter.getValue());
                                }
                            }
                        }
                    }
                }
            }
        }
        JobScriptsManager jobScriptsManager = JobScriptsManagerFactory.createManagerInstance(exportChoiceMap, processor
                .getContext().getName(), JobScriptsManager.ALL_ENVIRONMENTS, statisticsPort, tracePort, JobExportType.POJO);
        List<ExportFileResource> exportResources = jobScriptsManager.getExportResources(exportFileResources, watchParam);

        if (progressMonitor.isCanceled()) {
            throw new ProcessorException(new InterruptedException());
        }

        String archiveFilePath = Path.fromOSString(CorePlugin.getDefault().getPreferenceStore()
                .getString(ITalendCorePrefConstants.FILE_PATH_TEMP))
                + "/export.zip"; //$NON-NLS-1$ 
        final ArchiveFileExportOperationFullPath exporterOperation = new ArchiveFileExportOperationFullPath(exportResources,
                archiveFilePath);
        exporterOperation.setCreateLeadupStructure(true);
        exporterOperation.setUseCompression(true);

        final IProgressMonitor subProgressMonitor = new SubProgressMonitor(progressMonitor, 1);

        if (progressMonitor.isCanceled()) {
            throw new ProcessorException(new InterruptedException());
        }

        try {
            exporterOperation.run(subProgressMonitor);
        } catch (InvocationTargetException e) {
            throw new ProcessorException(e);
        } catch (InterruptedException e) {
            throw new ProcessorException(e);
        }

        // path can like name/name
        jobScriptsManager.deleteTempFiles();
        ProcessorUtilities.resetExportConfig();
        return archiveFilePath;
    }

    public void exportPigudf(IProcessor processor, Property property, int statisticsPort, int tracePort)
            throws ProcessorException {
        // build java project
        try {
            CorePlugin.getDefault().getRunProcessService().getJavaProject().getProject()
                    .build(IncrementalProjectBuilder.AUTO_BUILD, null);
        } catch (CoreException e) {
            throw new ProcessorException(e);
        }

        Map<ExportChoice, Object> exportChoiceMap = new EnumMap<ExportChoice, Object>(ExportChoice.class);
        exportChoiceMap.put(ExportChoice.needPigudf, true);
        ProcessItem processItem = (ProcessItem) property.getItem();
        ExportFileResource fileResource = new ExportFileResource(processItem, property.getLabel());
        ExportFileResource[] exportFileResources = new ExportFileResource[] { fileResource };

        JobScriptsManager jobScriptsManager = JobScriptsManagerFactory.createManagerInstance(exportChoiceMap, processor
                .getContext().getName(), JobScriptsManager.ALL_ENVIRONMENTS, statisticsPort, tracePort, JobExportType.POJO);
        URL url = jobScriptsManager.getExportPigudfResources(exportFileResources);

        if (url == null) {
            return;
        }
        File file = new File(url.getFile());
        File target = new File(JavaProcessorUtilities.getJavaProjectLibFolder().getAbsolutePath() + property.getLabel() + "_"
                + property.getVersion() + "_" + file.getName());
        try {
            FilesUtils.copyFile(file, target);
        } catch (IOException e) {
            throw new ProcessorException(e.getMessage());
        }

    }
}
