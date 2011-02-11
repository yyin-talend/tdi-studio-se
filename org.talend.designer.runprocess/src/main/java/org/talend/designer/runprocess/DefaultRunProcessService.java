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
package org.talend.designer.runprocess;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Level;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.ICodeProblemsChecker;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.process.IProcess2;
import org.talend.core.model.properties.Property;
import org.talend.core.service.IDesignerPerlService;
import org.talend.designer.runprocess.i18n.Messages;
import org.talend.designer.runprocess.java.JavaProcessor;
import org.talend.designer.runprocess.java.JavaProcessorUtilities;
import org.talend.designer.runprocess.language.SyntaxCheckerFactory;
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

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IRunProcessFactory#getSyntaxChecker(org.talend.core.model.temp.ECodeLanguage)
     */
    public ICodeProblemsChecker getSyntaxChecker(ECodeLanguage codeLanguage) {
        return SyntaxCheckerFactory.getInstance().getSyntaxChecker(codeLanguage);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IRunProcessFactory#setActiveProcess(org.talend.core.model.process.IProcess)
     */
    public void setActiveProcess(IProcess2 activeProcess) {
        RunProcessPlugin.getDefault().getRunProcessContextManager().setActiveProcess(activeProcess);
    }

    public void setActiveProcess(IProcess2 activeProcess, boolean refreshUI) {
        RunProcessPlugin.getDefault().getRunProcessContextManager().setActiveProcess(activeProcess, refreshUI);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IRunProcessService#getSelectedContext()
     */
    public IContext getSelectedContext() {
        return RunProcessPlugin.getDefault().getRunProcessContextManager().getActiveContext().getSelectedContext();
    }

    /**
     * DOC qian Removes IProcess.
     * 
     * @param activeProcess IProcess
     */
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
    public int perlExec(StringBuffer out, StringBuffer err, IPath absCodePath, String contextName, Level level,
            String perlInterpreterLibOption, String perlModuleDirectoryOption, int statOption, int traceOption,
            String... codeOptions) throws ProcessorException {
        int i = 0;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IDesignerPerlService.class)) {
            IDesignerPerlService service = (IDesignerPerlService) GlobalServiceRegister.getDefault().getService(
                    IDesignerPerlService.class);
            i = service.perlExec(out, err, absCodePath, contextName, level, perlInterpreterLibOption, perlModuleDirectoryOption,
                    statOption, traceOption, codeOptions);
        }
        return i;

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.designer.runprocess.IRunProcessFactory#createCodeProcessor(org.talend.core.model.process.IProcess,
     * boolean)
     */
    public IProcessor createCodeProcessor(IProcess process, Property property, ECodeLanguage language, boolean filenameFromLabel) {
        switch (language) {
        case PERL:
            return createPerlProcessor(process, property, filenameFromLabel);
        case JAVA:
            return createJavaProcessor(process, property, filenameFromLabel);
        default:
            return createJavaProcessor(process, property, filenameFromLabel);
        }
    }

    /**
     * DOC xue Comment method "createPerlProcessor".
     * 
     * @param process
     * @param filenameFromLabel
     * @return
     */
    protected IProcessor createPerlProcessor(IProcess process, Property property, boolean filenameFromLabel) {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IDesignerPerlService.class)) {
            IDesignerPerlService service = (IDesignerPerlService) GlobalServiceRegister.getDefault().getService(
                    IDesignerPerlService.class);
            return service.createPerlProcessor(process, property, filenameFromLabel);
        }
        return null;
    }

    /**
     * DOC xue Comment method "createJavaProcessor".
     * 
     * @param process
     * @param filenameFromLabel
     * @return
     */
    protected IProcessor createJavaProcessor(IProcess process, Property property, boolean filenameFromLabel) {
        return new JavaProcessor(process, property, filenameFromLabel);
    }

    public IPerformanceData createPerformanceData(String data) {
        return new PerformanceData(data);
    }

    public String getRoutineFilenameExt() {
        return ROUTINE_FILENAME_EXT;
    }

    public IProject getProject(ECodeLanguage language) throws CoreException {
        switch (language) {
        case PERL:
            if (GlobalServiceRegister.getDefault().isServiceRegistered(IDesignerPerlService.class)) {
                IDesignerPerlService service = (IDesignerPerlService) GlobalServiceRegister.getDefault().getService(
                        IDesignerPerlService.class);
                return service.getProject();
            }
        case JAVA:
            return JavaProcessorUtilities.getProcessorProject();
        default:
            if (GlobalServiceRegister.getDefault().isServiceRegistered(IDesignerPerlService.class)) {
                IDesignerPerlService service = (IDesignerPerlService) GlobalServiceRegister.getDefault().getService(
                        IDesignerPerlService.class);
                return service.getProject();
            }
            return null;
        }
    }

    public IJavaProject getJavaProject() throws CoreException {
        return JavaProcessorUtilities.getJavaProject();
    }

    /*
     * (non-Javadoc)
     * 
     * @seeorg.talend.designer.runprocess.IRunProcessService#setDelegateService(org.talend.designer.runprocess.
     * IRunProcessService)
     */
    public void setDelegateService(IRunProcessService delegateService) {
        throw new UnsupportedOperationException(Messages.getString("DefaultRunProcessService.methodCalledError")); //$NON-NLS-1$
    }

    public void updateLibraries(Set<String> jobModuleList, IProcess process) {
        switch (LanguageManager.getCurrentLanguage()) {
        case JAVA:
            JavaProcessorUtilities.computeLibrariesPath(new HashSet<String>(), null);
        default:
            // nothing
        }
    }

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
    public int getPauseTime() {
        return RunProcessPlugin.getDefault().getPreferenceStore().getInt(RunProcessPrefsConstants.STRACESTIME);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IRunProcessService#needDeleteAllJobs()
     */
    public boolean needDeleteAllJobs() {
        return !DeleteAllJobWhenStartUp.executed;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IRunProcessService#deleteAllJobx(boolean)
     */
    public void deleteAllJobs(boolean fromPluginModel) {
        new DeleteAllJobWhenStartUp().startup(fromPluginModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IRunProcessService#getRunProcessAction()
     */
    public IAction getRunProcessAction() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IRunProcessService#enableTraceForActiveRunProcess()
     */
    public boolean enableTraceForActiveRunProcess() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IRunProcessService#saveJobBeforeRun(org.talend.core.model.process.IProcess)
     */
    public void saveJobBeforeRun(IProcess activeProcess) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IRunProcessService#getPreferenceStore()
     */
    public IPreferenceStore getPreferenceStore() {
        // TODO Auto-generated method stub
        return null;
    }

    public IProcess getActiveProcess() {
        if (RunProcessPlugin.getDefault().getRunProcessContextManager().getActiveContext() == null) {
            return null;
        }

        return RunProcessPlugin.getDefault().getRunProcessContextManager().getActiveContext().getProcess();
    }

    public boolean checkExportProcess(IStructuredSelection selection) {
        return JobErrorsChecker.checkExportErrors(selection);
    }
}
