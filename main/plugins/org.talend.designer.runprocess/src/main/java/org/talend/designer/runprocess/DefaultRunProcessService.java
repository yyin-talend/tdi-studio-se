// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.runprocess;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Level;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.osgi.framework.Bundle;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.utils.generation.JavaUtils;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.ICodeProblemsChecker;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.components.ComponentCategory;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.process.IProcess2;
import org.talend.core.model.properties.Property;
import org.talend.designer.runprocess.i18n.Messages;
import org.talend.designer.runprocess.java.JavaProcessor;
import org.talend.designer.runprocess.java.JavaProcessorUtilities;
import org.talend.designer.runprocess.language.SyntaxCheckerFactory;
import org.talend.designer.runprocess.mapreduce.MapReduceJavaProcessor;
import org.talend.designer.runprocess.prefs.RunProcessPrefsConstants;
import org.talend.designer.runprocess.ui.views.ProcessView;
import org.talend.runprocess.data.PerformanceData;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id: talend-code-templates.xml 1 2006-09-29 17:06:40Z nrousseau $
 * 
 */
public class DefaultRunProcessService implements IRunProcessService {

    private static final String ROUTINE_FILENAME_EXT = ".pm"; //$NON-NLS-1$

    private static final String RESOURCE_FILE_PATH = "resources/"; //$NON-NLS-1$

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IRunProcessFactory#getSyntaxChecker(org.talend.core.model.temp.ECodeLanguage)
     */
    @Override
    public ICodeProblemsChecker getSyntaxChecker(ECodeLanguage codeLanguage) {
        return SyntaxCheckerFactory.getInstance().getSyntaxChecker(codeLanguage);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IRunProcessFactory#setActiveProcess(org.talend.core.model.process.IProcess)
     */
    @Override
    public void setActiveProcess(IProcess2 activeProcess) {
        RunProcessPlugin.getDefault().getRunProcessContextManager().setActiveProcess(activeProcess);
    }

    @Override
    public void setActiveProcess(IProcess2 activeProcess, boolean refreshUI) {
        RunProcessPlugin.getDefault().getRunProcessContextManager().setActiveProcess(activeProcess, refreshUI);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IRunProcessService#getSelectedContext()
     */
    @Override
    public IContext getSelectedContext() {
        return RunProcessPlugin.getDefault().getRunProcessContextManager().getActiveContext().getSelectedContext();
    }

    /**
     * DOC qian Removes IProcess.
     * 
     * @param activeProcess IProcess
     */
    @Override
    public void removeProcess(IProcess activeProcess) {
        RunProcessPlugin.getDefault().getRunProcessContextManager().removeProcess(activeProcess);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IRunProcessFactory#exec(java.lang.StringBuffer, java.lang.StringBuffer,
     * org.eclipse.core.runtime.IPath, java.lang.String, org.apache.log4j.Level, java.lang.String, int, int,
     * java.lang.String)
     */
    @Override
    public int perlExec(StringBuffer out, StringBuffer err, IPath absCodePath, String contextName, Level level,
            String perlInterpreterLibOption, String perlModuleDirectoryOption, int statOption, int traceOption,
            String... codeOptions) throws ProcessorException {
        int i = 0;
        return i;

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.designer.runprocess.IRunProcessFactory#createCodeProcessor(org.talend.core.model.process.IProcess,
     * boolean)
     */
    @Override
    public IProcessor createCodeProcessor(IProcess process, Property property, ECodeLanguage language, boolean filenameFromLabel) {
        switch (language) {
        case JAVA:
            return createJavaProcessor(process, property, filenameFromLabel);
        default:
            return createJavaProcessor(process, property, filenameFromLabel);
        }
    }

    /**
     * DOC xue Comment method "createJavaProcessor".
     * 
     * @param process
     * @param filenameFromLabel
     * @return
     */
    protected IProcessor createJavaProcessor(IProcess process, Property property, boolean filenameFromLabel) {
        if (ComponentCategory.CATEGORY_4_MAPREDUCE.getName().equals(process.getComponentsType())) {
            return new MapReduceJavaProcessor(process, property, filenameFromLabel);
        } else {
            return new JavaProcessor(process, property, filenameFromLabel);
        }
    }

    @Override
    public IPerformanceData createPerformanceData(String data) {
        return new PerformanceData(data);
    }

    @Override
    public String getRoutineFilenameExt() {
        return ROUTINE_FILENAME_EXT;
    }

    @Override
    public IProject getProject(ECodeLanguage language) throws CoreException {
        return JavaProcessorUtilities.getProcessorProject();
    }

    @Override
    public IJavaProject getJavaProject() throws CoreException {
        return JavaProcessorUtilities.getJavaProject();
    }

    /*
     * (non-Javadoc)
     * 
     * @seeorg.talend.designer.runprocess.IRunProcessService#setDelegateService(org.talend.designer.runprocess.
     * IRunProcessService)
     */
    @Override
    public void setDelegateService(IRunProcessService delegateService) {
        throw new UnsupportedOperationException(Messages.getString("DefaultRunProcessService.methodCalledError")); //$NON-NLS-1$
    }

    @Override
    public void updateLibraries(Set<String> jobModuleList, IProcess process) {
        switch (LanguageManager.getCurrentLanguage()) {
        case JAVA:
            JavaProcessorUtilities.computeLibrariesPath(new HashSet<String>(jobModuleList), null);
        default:
            // nothing
        }
    }

    @Override
    public void refreshView() {
        ProcessView view = ProcessView.findProcessView();
        if (view != null) {
            view.refresh();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IRunProcessService#getPauseTime()
     */
    @Override
    public int getPauseTime() {
        return RunProcessPlugin.getDefault().getPreferenceStore().getInt(RunProcessPrefsConstants.STRACESTIME);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IRunProcessService#needDeleteAllJobs()
     */
    @Override
    public boolean needDeleteAllJobs() {
        return !DeleteAllJobWhenStartUp.executed;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IRunProcessService#deleteAllJobx(boolean)
     */
    @Override
    public void deleteAllJobs(boolean fromPluginModel) {
        new DeleteAllJobWhenStartUp().startup(fromPluginModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IRunProcessService#getRunProcessAction()
     */
    @Override
    public IAction getRunProcessAction() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IRunProcessService#enableTraceForActiveRunProcess()
     */
    @Override
    public boolean enableTraceForActiveRunProcess() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IRunProcessService#saveJobBeforeRun(org.talend.core.model.process.IProcess)
     */
    @Override
    public void saveJobBeforeRun(IProcess activeProcess) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IRunProcessService#getPreferenceStore()
     */
    @Override
    public IPreferenceStore getPreferenceStore() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IProcess getActiveProcess() {
        if (RunProcessPlugin.getDefault().getRunProcessContextManager().getActiveContext() == null) {
            return null;
        }

        return RunProcessPlugin.getDefault().getRunProcessContextManager().getActiveContext().getProcess();
    }

    @Override
    public boolean checkExportProcess(IStructuredSelection selection, boolean isJob) {
        return JobErrorsChecker.checkExportErrors(selection, isJob);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IRunProcessService#getResourceFile(java.lang.String)
     */
    @Override
    public String getResourceFilePath(String filePath) {
        Bundle b = Platform.getBundle(RunProcessPlugin.PLUGIN_ID);
        URL url = null;
        try {
            url = FileLocator.toFileURL(FileLocator.find(b, new Path(RESOURCE_FILE_PATH + filePath), null));
        } catch (IOException e) {
            ExceptionHandler.process(e);
        }

        if (url != null) {
            return url.getFile();
        } else {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IRunProcessService#getTemplateStrFromPreferenceStore(java.lang.String)
     */
    @Override
    public String getTemplateStrFromPreferenceStore(String templateType) {
        return RunProcessPlugin.getDefault().getPreferenceStore().getString(templateType);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IRunProcessService#updateLogFiles(org.eclipse.core.resources.IProject)
     */
    @Override
    public void updateLogFiles(IProject project) {
        if (project == null) {
            return;
        }
        try {
            Path path = new Path(JavaUtils.JAVA_SRC_DIRECTORY);
            IFolder srcFolder = project.getFolder(path);
            if (srcFolder == null) {
                return;
            }
            IFile commonLogFile = srcFolder.getFile("common-logging.properties"); //$NON-NLS-1$
            String commonLogStr = getTemplateStrFromPreferenceStore(RunProcessPrefsConstants.COMMON_LOGGING_PROPERTIES_TEMPLATE);
            if (commonLogStr != null) {
                File clFile = new File(commonLogFile.getLocation().toOSString());
                if (!clFile.exists()) {// not support modify common-logging.properties template now.
                    FileOutputStream clFileFileOutputStream = null;
                    try {
                        clFileFileOutputStream = new FileOutputStream(clFile);
                        clFileFileOutputStream.write(commonLogStr.getBytes());
                    } finally {
                        clFileFileOutputStream.close();
                    }
                }
            }
            IFile log4jFile = srcFolder.getFile("log4j.properties"); //$NON-NLS-1$
            String log4jStr = getTemplateStrFromPreferenceStore(RunProcessPrefsConstants.LOG4J_PROPERTIES_TEMPLATE);
            if (log4jStr != null) {
                File ljFile = new File(log4jFile.getLocation().toOSString());
                FileOutputStream ljFileOutputStream = null;
                try {
                    ljFileOutputStream = new FileOutputStream(ljFile);
                    ljFileOutputStream.write(log4jStr.getBytes());
                } finally {
                    ljFileOutputStream.close();
                }
            }
            srcFolder.refreshLocal(IResource.DEPTH_ONE, null);
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IRunProcessService#buildJavaProject()
     */
    @Override
    public void buildJavaProject() {
        try {
            getJavaProject().getProject().build(IncrementalProjectBuilder.AUTO_BUILD, null);
        } catch (CoreException e) {
            ExceptionHandler.process(e);
        }
    }

}
