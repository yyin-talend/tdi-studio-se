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
package org.talend.repository.ui.wizards.exportjob;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.designer.runprocess.ProcessorException;
import org.talend.designer.runprocess.ProcessorUtilities;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.ResourceModelUtils;

/**
 * Manages the job scripts to be exported. <br/>
 * 
 * $Id: JobScriptsManager.java 1 2006-12-14 下午05:06:49 bqian
 * 
 */
public abstract class JobScriptsManager {

    protected static final String UNIX_LAUNCHER = "run.sh";

    protected static final String WINDOWS_LAUNCHER = "run.bat";

    protected static final String LIBRARY_FOLDER_NAME = "lib"; //$NON-NLS-1$

    private static final String ALL_PERL_INTERPRETERS = Messages.getString("JobPerlScriptsManager.allInterpreter"); //$NON-NLS-1$

    private static final String PERL_INTERPRETER_UNIX = "Unix"; //$NON-NLS-1$

    private static final String PERL_INTERPRETER_WINDOWS = "Windows"; //$NON-NLS-1$

    protected static final String JOB_SOURCE_FOLDER_NAME = "src"; //$NON-NLS-1$

    /**
     * 
     * DOC Represent exportchoice <br/>.
     * 
     * $Id: JobScriptsExportWizardPage.java 1 2007-1-31下午06:14:19 +0000 ylv $
     * 
     */
    enum ExportChoice {
        needLauncher,
        needSystemRoutine,
        needUserRoutine,
        needTalendLibraries,
        needJob,
        needSource,
        needContext
    }

    /**
     * qian Gets the export resources.
     * 
     * @param process
     * @param needLauncher
     * @param needSystemRoutine
     * @param needUserRoutine
     * @param needModel
     * @param needJob
     * @param needContext
     * @return
     */

    public abstract List<ExportFileResource> getExportResources(ExportFileResource[] process,
            Map<ExportChoice, Boolean> exportChoiceMap, String contextName, String launcher);

    protected String getTmpFolder() {
        String tmpFold = System.getProperty("user.dir"); //$NON-NLS-1$
        tmpFold = tmpFold + "/talendExporter"; //$NON-NLS-1$
        return tmpFold;
    }

    /**
     * Gets the perl launcher location.
     * 
     * @return
     */
    public String[] getLauncher() {
        String[] launchers = { ALL_PERL_INTERPRETERS, PERL_INTERPRETER_UNIX, PERL_INTERPRETER_WINDOWS };
        return launchers;
    }

    /**
     * Gets perl intepreter.
     * 
     * @param needLauncher
     * @param process
     * @return
     */
    protected List<URL> getLauncher(boolean needLauncher, ProcessItem process, String contextName, String launcher) {

        List<URL> list = new ArrayList<URL>();
        if (!needLauncher) {
            return list;
        }
        String cmd = getCommandByTalendJob(escapeFileNameSpace(process), contextName);
        String tmpFold = getTmpFolder();
        File fileTemp = new File(tmpFold);
        if (!fileTemp.exists()) {
            fileTemp.mkdir();
        }
        if (launcher.equals(ALL_PERL_INTERPRETERS)) {
            createLauncherFile(process, list, cmd, UNIX_LAUNCHER, tmpFold);
            createLauncherFile(process, list, cmd, WINDOWS_LAUNCHER, tmpFold);
        } else if (launcher.equals(PERL_INTERPRETER_UNIX)) {
            createLauncherFile(process, list, cmd, UNIX_LAUNCHER, tmpFold);
        } else if (launcher.equals(PERL_INTERPRETER_WINDOWS)) {
            createLauncherFile(process, list, cmd, WINDOWS_LAUNCHER, tmpFold);
        }
        return list;
    }

    protected String getCommandByTalendJob(String jobName, String context) {
        String[] cmd = new String[] {};
        try {
            cmd = ProcessorUtilities.getCommandLine(true, jobName, context, new String[] {});
        } catch (ProcessorException e) {
            ExceptionHandler.process(e);
        }
        StringBuffer sb = new StringBuffer();
        sb.append(""); //$NON-NLS-1$
        for (String s : cmd) {
            sb.append(s).append(' ');
        }
        return sb.toString();
    }

    /**
     * DOC Administrator Comment method "createLauncherFile".
     * 
     * @param process
     * @param list
     * @param cmdPrimary
     * @param cmdSecondary
     * @param tmpFold
     */
    private void createLauncherFile(ProcessItem process, List<URL> list, String cmdPrimary, String fileName, String tmpFold) {
        PrintWriter pw = null;
        try {

            File file = new File(tmpFold, process.getProperty().getLabel() + "_" + fileName); //$NON-NLS-1$
            file.createNewFile();
            pw = new PrintWriter(new FileOutputStream(file));
            pw.print(cmdPrimary);
            pw.flush();
            list.add(file.toURL());
            pw.close();
        } catch (Exception e) {
            ExceptionHandler.process(e);
        } finally {
            try {
                if (pw != null) {
                    pw.close();
                }
            } catch (Exception e) {
                // do nothing here
            }
        }
    }

    /**
     * Deletes the temporary files.
     */
    public void deleteTempFiles() {
        String tmpFold = getTmpFolder();
        deleteDirectory(new File(tmpFold));
    }

    public void deleteDirectory(File dir) {
        File[] entries = dir.listFiles();
        int sz = entries.length;
        for (int i = 0; i < sz; i++) {
            if (entries[i].isDirectory()) {
                deleteDirectory(entries[i]);
            } else {
                entries[i].delete();
            }
        }
        dir.delete();
    }

    public boolean existTempFile() {
        String tmpFold = getTmpFolder();
        File f = new File(tmpFold);
        return f.exists();
    }

    /**
     * 
     * Gets the set of current job's context.
     * 
     * @return a List of context names.
     * 
     */
    public abstract List<String> getJobContexts(ProcessItem processItem);

    /**
     * ftang Comment method "escapeFileNameSpace".
     * 
     * @param processItem
     * @return
     */
    protected String escapeFileNameSpace(ProcessItem processItem) {
        String jobName = processItem.getProperty().getLabel();
        return escapeSpace(jobName);
    }

    /**
     * ftang Comment method "escapeSpace".
     * 
     * @param name
     * @return
     */
    protected String escapeSpace(String name) {
        return name != null ? name.replace(" ", "") : ""; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

    /**
     * Generates the perl files.
     * 
     * @param needGenerateCode
     * @param contextName
     * @param process
     */
    protected void generateJobFiles(ProcessItem process, String contextName) {
        ProcessorUtilities.generateCode(process.getProperty().getLabel(), contextName); //$NON-NLS-1$
    }

    protected IResource[] sourceResouces = null;

    protected IResource[] getAllSourceFiles() {
        if (sourceResouces == null) {
            try {
                List<IResource> sourceFile = new ArrayList<IResource>();
                Project project = ((RepositoryContext) CorePlugin.getContext().getProperty(Context.REPOSITORY_CONTEXT_KEY))
                        .getProject();
                IProject prj = ResourceModelUtils.getProject(project);
                IFolder folder = prj.getFolder(ERepositoryObjectType.getFolderName(ERepositoryObjectType.PROCESS));
                addNodeToResource(folder.members(), sourceFile);
                sourceResouces = sourceFile.toArray(new IResource[sourceFile.size()]);
            } catch (Exception e) {
                ExceptionHandler.process(e);
                sourceResouces = new IResource[0];
            }
        }
        return sourceResouces;
    }

    protected void addNodeToResource(IResource[] resources, List<IResource> sourceFile) throws CoreException {

        for (IResource resource : resources) {
            if (resource instanceof IFolder) {
                IFolder folder = (IFolder) resource;
                addNodeToResource(folder.members(), sourceFile);
            }
            if (resource instanceof IFile) {
                sourceFile.add(resource);
            }

        }
    }

    protected List<URL> getSource(ProcessItem processItem, boolean needChoice) {
        List<String> list = new ArrayList<String>();
        if (needChoice) {
            try {
                String name = processItem.getProperty().getLabel() + "_" + processItem.getProperty().getVersion(); //$NON-NLS-1$
                name = name != null ? name.replace(" ", "") : ""; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                list.add(name + ".item"); //$NON-NLS-1$
                list.add(name + ".properties"); //$NON-NLS-1$

            } catch (Exception e) {
                ExceptionHandler.process(e);
            }

        }

        IResource[] resources = this.getAllSourceFiles();
        return getResourcesURL(resources, list);
    }

    /**
     * Gets resources' URL.
     * 
     * @param resources
     * @param fileNames
     * @return
     */
    protected List<URL> getResourcesURL(IResource[] resources, List<String> fileNames) {
        List<URL> list = new ArrayList<URL>();

        for (Iterator<String> iter = fileNames.iterator(); iter.hasNext();) {
            String name = iter.next();
            for (int i = 0; i < resources.length; i++) {
                IResource resource = resources[i];
                if (resource.getFullPath().toOSString().endsWith(name)) {
                    try {
                        URL url = resource.getLocation().toFile().toURL();
                        list.add(FileLocator.toFileURL(url));
                        break;
                    } catch (Exception e) {
                        ExceptionHandler.process(e);
                    }
                }
            }
        }
        return list;
    }

    protected void addToList(List<String> list, String o) {
        if (!list.contains(o)) {
            list.add(o);
        }
    }

    protected ProcessItem findProcess(String name) {
        return ProcessorUtilities.getProcessItem(name);
    }
}
