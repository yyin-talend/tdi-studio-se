// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2007 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
//
// ============================================================================
package org.talend.designer.runprocess;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.MessageBoxExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.designer.core.IDesignerCoreService;
import org.talend.designer.core.model.utils.emf.talendfile.JobType;
import org.talend.repository.model.IProxyRepositoryFactory;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 * $Id: talend-code-templates.xml 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
 * 
 */
public class ProcessorUtilities {

    private static String interpreter, codeLocation, libraryPath;

    private static boolean exportConfig = false;

    public static ProcessItem getProcessItem(String processName) {
        IProxyRepositoryFactory factory = CorePlugin.getDefault().getProxyRepositoryFactory();
        ProcessItem selectedProcessItem = null;

        try {
            List<IRepositoryObject> list = factory.getAll(ERepositoryObjectType.PROCESS);

            for (IRepositoryObject process : list) {
                if (processName.equals(process.getLabel())) {
                    if (process.getProperty().getItem() instanceof ProcessItem) {
                        selectedProcessItem = (ProcessItem) process.getProperty().getItem();
                        break;
                    }
                }
            }
        } catch (PersistenceException e) {
            ExceptionHandler.process(e);
        }
        return selectedProcessItem;
    }

    /**
     * Process need to be loaded to use this function.
     * 
     * @param process
     * @param selectedContext
     * @return
     */
    public static IContext getContext(IProcess process, String selectedContext) {
        return process.getContextManager().getContext(selectedContext);
    }

    public static void setExportConfig(String exportInterpreter, String exportCodeLocation, String exportLibraryPath) {
        interpreter = exportInterpreter;
        codeLocation = exportCodeLocation;
        libraryPath = exportLibraryPath;
        exportConfig = true;
    }

    public static boolean isExportConfig() {
        return exportConfig;
    }

    public static void resetExportConfig() {
        interpreter = null;
        codeLocation = null;
        libraryPath = null;
        exportConfig = false;
    }

    public static String getInterpreter() {
        return interpreter;
    }

    public static String getLibraryPath() {
        return libraryPath;
    }

    public static String getCodeLocation() {
        return codeLocation;
    }

    /**
     * Process need to be loaded first to use this function.
     * 
     * @param process
     * @param context
     * @return
     */
    public static IProcessor getProcessor(IProcess process, IContext context) {
        IProcessor processor = getProcessor(process);
        processor.setContext(context);
        return processor;
    }

    /**
     * Process need to be loaded first to use this function.
     * 
     * @param process
     * @param context
     * @return
     */
    public static IProcessor getProcessor(IProcess process) {
        if (process.getProcessor() == null) {
            IRunProcessService service = CorePlugin.getDefault().getRunProcessService();
            return service.createCodeProcessor(process, ((RepositoryContext) CorePlugin.getContext().getProperty(
                    Context.REPOSITORY_CONTEXT_KEY)).getProject().getLanguage(), true);
        } else {
            return process.getProcessor();
        }
    }

    private static boolean generateCode(JobInfo jobInfo, boolean statistics, boolean trace, boolean properties) {
        IProcess currentProcess = null;
        jobList.add(jobInfo);
        ProcessItem selectedProcessItem = getProcessItem(jobInfo.getJobName());
        if (selectedProcessItem == null) {
            return false;
        }
        if (jobInfo.getProcess() == null) {
            if (selectedProcessItem != null) {
                IDesignerCoreService service = CorePlugin.getDefault().getDesignerCoreService();
                currentProcess = service.getProcessFromProcessItem(selectedProcessItem);
            }
            if (currentProcess == null) {
                return false;
            }
        } else {
            currentProcess = jobInfo.getProcess();
        }
        IContext currentContext;
        if (jobInfo.getContext() == null) {
            currentContext = getContext(currentProcess, jobInfo.getContextName());
        } else {
            currentContext = jobInfo.getContext();
        }
        IProcessor processor = getProcessor(currentProcess, currentContext);
        try {
            processor.generateCode(currentContext, statistics, trace, properties); // main job will use stats / traces
        } catch (ProcessorException pe) {
            MessageBoxExceptionHandler.process(pe);
        }
        boolean toReturn = true;
        if (selectedProcessItem.getProcess().getRequired() != null) {
            EList emfJobList = selectedProcessItem.getProcess().getRequired().getJob();
            for (int j = 0; j < emfJobList.size() && toReturn; j++) {
                JobType jType = (JobType) emfJobList.get(j);
                JobInfo subJobInfo = new JobInfo(jType);
                if (!jobList.contains(subJobInfo)) {
                    toReturn = generateCode(subJobInfo, false, false, true); // children won't have stats / traces
                }
            }
        }
        return toReturn;
    }

    static List<JobInfo> jobList = new ArrayList<JobInfo>();

    /**
     * This function will generate the code of the process and all of this sub process.
     * 
     * @param processName
     * @param contextName
     */
    public static boolean generateCode(String processName, String contextName, boolean statistics, boolean trace) {
        jobList.clear();
        JobInfo jobInfo = new JobInfo(processName, contextName);
        return generateCode(jobInfo, statistics, trace, true);
    }

    public static boolean generateCode(IProcess process, IContext context, boolean statistics, boolean trace,
            boolean properties) {
        jobList.clear();
        JobInfo jobInfo = new JobInfo(process.getName(), context.getName());
        jobInfo.setProcess(process);
        jobInfo.setContext(context);
        return generateCode(jobInfo, statistics, trace, properties);
    }

    /**
     * 
     * Get the command line to launch the job.
     * 
     * WARNING : this method is used in jet templates, don't modify method signature
     * 
     * @param externalUse if true, will add "" around path and change \ to /
     * @param processName
     * @param contextName
     * @param codeOptions
     * @return
     * @throws ProcessorException
     */
    public static String[] getCommandLine(boolean externalUse, String processName, String contextName,
             String[] codeOptions) throws ProcessorException {
        return getCommandLine(externalUse, processName, contextName, codeOptions);
    }
    /**
     * 
     * Get the command line to launch the job.
     * 
     * @param externalUse if true, will add "" around path and change \ to /
     * @param processName
     * @param contextName
     * @param codeOptions
     * @return
     * @throws ProcessorException
     */
    public static String[] getCommandLine(boolean externalUse, String processName, String contextName,
             int statisticPort, int tracePort, String ... codeOptions) throws ProcessorException {
        IProcess currentProcess = null;
        ProcessItem selectedProcessItem = getProcessItem(processName);
        if (selectedProcessItem != null) {
            IDesignerCoreService service = CorePlugin.getDefault().getDesignerCoreService();
            currentProcess = service.getProcessFromProcessItem(selectedProcessItem);
        }
        if (currentProcess == null) {
            return new String[] {};
        }
        IContext currentContext = getContext(currentProcess, contextName);
        IProcessor processor = getProcessor(currentProcess, currentContext);
        return processor.getCommandLine(externalUse, statisticPort, tracePort, codeOptions);
    }

    /**
     * DOC nrousseau ProcessController class global comment. Detailled comment <br/>
     * 
     * $Id: talend-code-templates.xml 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
     * 
     */
    private static class JobInfo {

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((contextName == null) ? 0 : contextName.hashCode());
            result = prime * result + ((jobName == null) ? 0 : jobName.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            final JobInfo other = (JobInfo) obj;
            if (contextName == null) {
                if (other.contextName != null) {
                    return false;
                }
            } else if (!contextName.equals(other.contextName)) {
                return false;
            }
            if (jobName == null) {
                if (other.jobName != null) {
                    return false;
                }
            } else if (!jobName.equals(other.jobName)) {
                return false;
            }
            if (context == null) {
                if (other.context != null) {
                    return false;
                }
            } else if (!context.equals(other.context)) {
                return false;
            }
            if (process == null) {
                if (other.process != null) {
                    return false;
                }
            } else if (!process.equals(other.process)) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return "job:" + jobName + " / context:" + contextName; //$NON-NLS-1$ //$NON-NLS-2$
        }

        String jobName, contextName;

        IProcess process;

        IContext context;

        JobInfo(String jobName, String contextName) {
            this.jobName = jobName;
            this.contextName = contextName;
        }

        JobInfo(JobType jobType) {
            this.jobName = jobType.getName();
            this.contextName = jobType.getContext();
        }

        public String getContextName() {
            return contextName;
        }

        public void setContextName(String contextName) {
            this.contextName = contextName;
        }

        public String getJobName() {
            return jobName;
        }

        public void setJobName(String processName) {
            this.jobName = processName;
        }

        public IProcess getProcess() {
            return process;
        }

        public void setProcess(IProcess process) {
            this.process = process;
        }

        public IContext getContext() {
            return context;
        }

        public void setContext(IContext context) {
            this.context = context;
        }
    }
}
