// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.model.properties.ProcessItem;
import org.talend.designer.runprocess.ProcessorException;
import org.talend.designer.runprocess.ProcessorUtilities;
import org.talend.repository.i18n.Messages;
import org.talend.repository.local.ExportItemUtil;
import org.talend.repository.ui.utils.PerlResourcesHelper;
import org.talend.repository.ui.wizards.exportjob.ExportFileResource;

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

    protected static final String PATH_SEPARATOR = "/";

    public static final String ALL_ENVIRONMENTS = Messages.getString("JobPerlScriptsManager.allInterpreter"); //$NON-NLS-1$

    public static final String UNIX_ENVIRONMENT = "Unix"; //$NON-NLS-1$

    public static final String WINDOWS_ENVIRONMENT = "Windows"; //$NON-NLS-1$

    protected static final String JOB_SOURCE_FOLDER_NAME = "src"; //$NON-NLS-1$

    public Map<ExportChoice, Boolean> getDefaultExportChoiseMap() {
        Map<ExportChoice, Boolean> exportChoiceMap = new EnumMap<ExportChoice, Boolean>(ExportChoice.class);
        exportChoiceMap.put(ExportChoice.needLauncher, true);
        exportChoiceMap.put(ExportChoice.needSystemRoutine, true);
        exportChoiceMap.put(ExportChoice.needUserRoutine, true);
        exportChoiceMap.put(ExportChoice.needTalendLibraries, true);
        exportChoiceMap.put(ExportChoice.needJob, true);
        exportChoiceMap.put(ExportChoice.needSource, true);
        exportChoiceMap.put(ExportChoice.needContext, true);
        exportChoiceMap.put(ExportChoice.applyToChildren, true);
        exportChoiceMap.put(ExportChoice.doNotCompileCode, false);
        return exportChoiceMap;
    }

    /**
     * 
     * DOC Represent exportchoice <br/>.
     * 
     * $Id: JobScriptsExportWizardPage.java 1 2007-1-31下午06:14:19 +0000 ylv $
     * 
     */
    public enum ExportChoice {
        needMetaInfo,
        needWEBXML,
        needCONFIGFILE,
        needAXISLIB,
        needWSDD,
        needWSDL,
        needLauncher,
        needSystemRoutine,
        needUserRoutine,
        needTalendLibraries,
        needJob,
        needSource,
        needContext,
        applyToChildren,
        doNotCompileCode
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
            Map<ExportChoice, Boolean> exportChoiceMap, String contextName, String launcher, int statisticPort,
            int tracePort, String... codeOptions);

    protected String getTmpFolder() {
        String tmpFold = getTmpFolderPath();
        File f = new File(tmpFold);
        if (!f.exists()) {
            f.mkdir();
        }
        return tmpFold;
    }

    private String getTmpFolderPath() {
        String tmpFolder = System.getProperty("user.dir"); //$NON-NLS-1$
        tmpFolder = tmpFolder + "/talendExporter"; //$NON-NLS-1$
        return tmpFolder;
    }

    /**
     * Gets the perl launcher location.
     * 
     * @return
     */
    public String[] getLauncher() {
        String[] launchers = { ALL_ENVIRONMENTS, UNIX_ENVIRONMENT, WINDOWS_ENVIRONMENT };
        return launchers;
    }

    /**
     * 
     * Create launcher(s) and get url(s).
     * 
     * @param needLauncher
     * @param process
     * @param contextName
     * @param environment use JobScriptsManager.ALL_ENVIRONMENTS, JobScriptsManager.UNIX_ENVIRONMENT or
     * JobScriptsManager.WINDOWS_ENVIRONMENT
     * @param statisticPort TODO
     * @param tracePort TODO
     * @param codeOptions TODO
     * @return
     */
    protected List<URL> getLauncher(boolean needLauncher, ProcessItem process, String contextName, String environment,
            int statisticPort, int tracePort, String... codeOptions) {

        List<URL> list = new ArrayList<URL>();
        if (!needLauncher) {
            return list;
        }
        String windowsCmd = getCommandByTalendJob(Platform.OS_WIN32, escapeFileNameSpace(process), contextName,
                statisticPort, tracePort, codeOptions);
        String unixCmd = getCommandByTalendJob(Platform.OS_LINUX, escapeFileNameSpace(process), contextName,
                statisticPort, tracePort, codeOptions);
        String tmpFold = getTmpFolder();

        if (environment.equals(ALL_ENVIRONMENTS)) {
            createLauncherFile(process, list, unixCmd, UNIX_LAUNCHER, tmpFold);
            createLauncherFile(process, list, windowsCmd, WINDOWS_LAUNCHER, tmpFold);
        } else if (environment.equals(UNIX_ENVIRONMENT)) {
            createLauncherFile(process, list, unixCmd, UNIX_LAUNCHER, tmpFold);
        } else if (environment.equals(WINDOWS_ENVIRONMENT)) {
            createLauncherFile(process, list, windowsCmd, WINDOWS_LAUNCHER, tmpFold);
        }

        return list;
    }

    protected String getCommandByTalendJob(String targetPlatform, String jobName, String context, int statisticPort,
            int tracePort, String... codeOptions) {
        String[] cmd = new String[] {};
        try {
            cmd = ProcessorUtilities.getCommandLine(targetPlatform, true, jobName, context, statisticPort, tracePort,
                    codeOptions);
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
    private void createLauncherFile(ProcessItem process, List<URL> list, String cmdPrimary, String fileName,
            String tmpFold) {
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
        String tmpFold = getTmpFolderPath();
        File file = new File(tmpFold);
        if (!file.exists() && !file.isDirectory()) {
            return;
        }
        deleteDirectory(file);
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
    public String escapeSpace(String name) {
        return PerlResourcesHelper.escapeSpace(name);
    }

    /**
     * Generates the perl files.
     * 
     * @param needGenerateCode
     * @param contextName
     * @param process
     */
    protected void generateJobFiles(ProcessItem process, String contextName, boolean statistics, boolean trace,
            boolean applyContextToChildren) {
        ProcessorUtilities.generateCode(process.getProperty().getLabel(), contextName, statistics, trace,
                applyContextToChildren);
    }

    protected IResource[] sourceResouces = null;

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
        List<URL> list = new ArrayList<URL>();
        if (needChoice) {
            try {
                Set<File> files = new ExportItemUtil().createLocalResources(new File(getTmpFolder()), processItem);
                for (File file : files) {
                    list.add(file.toURI().toURL());
                }
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }
        }

        return list;
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

                if (resource.getName().equals(name)) {
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
