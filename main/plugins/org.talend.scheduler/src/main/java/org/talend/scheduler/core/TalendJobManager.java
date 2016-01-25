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
package org.talend.scheduler.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IPath;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.data.container.Content;
import org.talend.commons.utils.data.container.ContentList;
import org.talend.commons.utils.data.container.RootContainer;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.general.Project;
import org.talend.core.model.process.JobInfo;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.designer.runprocess.IProcessor;
import org.talend.designer.runprocess.ProcessorException;
import org.talend.designer.runprocess.ProcessorUtilities;
import org.talend.repository.model.ERepositoryStatus;
import org.talend.scheduler.SchedulerPlugin;

/**
 * This class is responsible for keeping synchronous with project,job and context in comboxes, according to these 3
 * arameters, generate corresponding command.
 * 
 * <br/>
 * $Id$
 * 
 */
public class TalendJobManager {

    private RepositoryContext repositoryContext;

    private ProxyRepositoryFactory factory;

    // private RootContainer<Integer, IRepositoryObject> processContainer;
    private RootContainer<String, IRepositoryViewObject> processContainer;

    // private ContentList<Integer, IRepositoryObject> processAbsoluteMembers;
    private ContentList<String, IRepositoryViewObject> processAbsoluteMembers;

    private Map<String, List<JobInfo>> runjobInfosByName;

    private Map<String, JobInfo> mainJobInfoByName;

    private Map<String, String> processNameList = new HashMap<String, String>();

    /**
     * 
     * Default constructor.
     */
    public TalendJobManager() {

        try {
            repositoryContext = (RepositoryContext) CorePlugin.getContext().getProperty(Context.REPOSITORY_CONTEXT_KEY);

            factory = ProxyRepositoryFactory.getInstance();

            processContainer = factory.getMetadata(ERepositoryObjectType.PROCESS);

            processAbsoluteMembers = processContainer.getAbsoluteMembers();

            runjobInfosByName = new HashMap<String, List<JobInfo>>();

            mainJobInfoByName = new HashMap<String, JobInfo>();

        } catch (Exception exception) {
            SchedulerPlugin.log(exception);

        }
    }

    /**
     * 
     * Gets current project.
     * 
     * @return an instance of Project.
     */
    public Project getCurrentProject() {

        Project project = repositoryContext.getProject();

        return project;
    }

    /**
     * 
     * Gets all prjects' names.
     * 
     * @return an array of project names.
     */
    // public String[] getAllProjectNames() {
    // List<String> names = new ArrayList<String>();
    //
    // for (Project project : this.getAllProjects()) {
    // names.add(project.getLabel());
    // }
    // return names.toArray(new String[names.size()]);
    // }
    /**
     * 
     * Gets all projects.
     * 
     * @return an ArrayList of Projects.
     */
    // public List<Project> getAllProjects() {
    // List<Project> list = new ArrayList<Project>();
    //
    // Project[] projects = null;
    // try {
    // String server = checkString(repositoryContext.getServer());
    // String port = checkString(repositoryContext.getPort() + "");
    // // String user = checkString(repositoryContext.getUser().getName());
    // User user = repositoryContext.getUser();
    // // String password = checkString(repositoryContext.getPassword());
    //
    // projects = factory.readProject(server, port, user);
    // } catch (PersistenceException e) {
    // SchedulerPlugin.log(e);
    // }
    //
    // if (projects == null) {
    // return new ArrayList<Project>();
    // }
    //
    // for (Project project : projects) {
    // list.add(project);
    // }
    //
    // return list;
    // }
    /**
     * 
     * Gets the set of current project's job.
     * 
     * @return a List of jobs
     * @exception
     */
    public Map<String, String> getCurrentProjectJobs() {
        processNameList.clear();

        for (String string : runjobInfosByName.keySet()) {
            runjobInfosByName.remove(string);
        }

        for (Content<String, IRepositoryViewObject> object : processAbsoluteMembers.values()) {
            IRepositoryObject process = (IRepositoryObject) object.getContent();
            if (factory.getStatus(process) != ERepositoryStatus.DELETED) {
                String path = object.getParent().getPath().toString();
                String name;
                if (path.equals("")) { //$NON-NLS-1$
                    name = IPath.SEPARATOR + process.getLabel();
                } else {
                    name = IPath.SEPARATOR + path + IPath.SEPARATOR + process.getLabel();
                }
                Item item = process.getProperty().getItem();
                if (item instanceof ProcessItem) {
                    List<JobInfo> jobInfos = new ArrayList<JobInfo>();
                    JobInfo mainJobInfo = new JobInfo((ProcessItem) item, ((ProcessItem) item).getProcess().getDefaultContext());
                    // jobInfos.add(mainJobInfo);
                    try {
                        jobInfos.addAll(ProcessorUtilities.getChildrenJobInfo(((ProcessItem) ProxyRepositoryFactory.getInstance()
                                .getUptodateProperty(((ProcessItem) item).getProperty()).getItem())));
                    } catch (PersistenceException e) {
                        e.printStackTrace();
                    }
                    mainJobInfoByName.put(process.getLabel(), mainJobInfo);
                    runjobInfosByName.put(process.getLabel(), jobInfos);
                }
                processNameList.put(process.getLabel(), name);
            }
        }
        return processNameList;
    }

    public String getJobPathString(String jobLable) {
        if (jobLable != null) {
            return processNameList.get(jobLable);
        }
        return null;
    }

    /**
     * 
     * Gets the set of current job's context.
     * 
     * @return a List of context names.
     * 
     */
    public List<String> getCurrentJobContexts() {
        List<String> contextNameList = new ArrayList<String>();
        try {
            for (Content<String, IRepositoryViewObject> object : processAbsoluteMembers.values()) {
                IRepositoryObject process = (IRepositoryObject) object.getContent();
                if (process.getProperty().getItem() instanceof ProcessItem) {
                    ProcessItem processItem = (ProcessItem) process.getProperty().getItem();
                    for (Object o : processItem.getProcess().getContext()) {
                        if (o instanceof ContextType) {
                            ContextType context = (ContextType) o;
                            if (contextNameList.contains(context.getName())) {
                                continue;
                            }
                            contextNameList.add(context.getName());
                        }
                    }
                }
            }
        } catch (Exception e) {
            SchedulerPlugin.log(e);
        }
        return contextNameList;
    }

    /**
     * 
     * Updates context list according to selected job name.
     * 
     * @param selectedJobName
     * 
     * @return a List of context name.
     */
    public List<String> updateContextList(String selectedJobName) {

        List<String> contextNameList = new ArrayList<String>();

        String selectedProcess = selectedJobName;

        try {
            for (Content<String, IRepositoryViewObject> object : processAbsoluteMembers.values()) {
                IRepositoryObject process = (IRepositoryObject) object.getContent();
                String id = process.getLabel();

                if (selectedProcess.equals(id)) {
                    if (process.getProperty().getItem() instanceof ProcessItem) {

                        ProcessItem processItem = (ProcessItem) process.getProperty().getItem();
                        for (Object o : processItem.getProcess().getContext()) {
                            if (o instanceof ContextType) {
                                ContextType context = (ContextType) o;
                                if (contextNameList.contains(context.getName())) {
                                    continue;
                                }
                                contextNameList.add(context.getName());
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {

            SchedulerPlugin.log(e);
        }

        return contextNameList;
    }

    /**
     * 
     * DOC dev Comment method "getCommandByTalendJob".
     * 
     * @param project
     * @param jobName
     * @param context
     * @return
     * @throws ProcessorException
     * @throws ProcessorException
     */
    public String getCommandByTalendJob(String project, String jobName, String context) throws ProcessorException {
        project = project.replace("/", ""); //$NON-NLS-1$ //$NON-NLS-2$
        if (jobName.split("/").length > 2) { //$NON-NLS-1$
            jobName = jobName.substring(jobName.lastIndexOf("/") + 1, jobName.length()); //$NON-NLS-1$
        } else {
            jobName = jobName.replace("/", ""); //$NON-NLS-1$ //$NON-NLS-2$
        }
        context = context.replace("/", ""); //$NON-NLS-1$ //$NON-NLS-2$

        String[] cmd = ProcessorUtilities.getCommandLine(true, mainJobInfoByName.get(jobName).getJobId(), context,
                IProcessor.NO_STATISTICS, IProcessor.NO_TRACES);

        StringBuffer sb = new StringBuffer();
        sb.append(""); //$NON-NLS-1$
        for (String s : cmd) {
            sb.append(' ').append(s);
        }
        return sb.toString();
    }

    /**
     * 
     * Checks if a string is null or empty, translate it to "".
     * 
     * @param str input string
     * @return a string
     */
    private String checkString(String str) {
        if (str == null || str.length() == 0) {
            return ""; //$NON-NLS-1$
        }
        return str;
    }

    /**
     * Get tRunJob within current job.
     * 
     * yzhang Comment method "getRunJobs".
     * 
     * @param id
     * @return
     */
    public List<JobInfo> getRunJobs(String name) {
        return runjobInfosByName.get(name);
    }

    public JobInfo getMainJobInfoByName(String name) {
        return mainJobInfoByName.get(name);
    }
}
