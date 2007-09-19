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
package org.talend.spagic.engines.client.ui.wizards;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.BooleanUtils;
import org.eclipse.core.runtime.IPath;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.model.genhtml.HTMLDocUtils;
import org.talend.core.model.properties.ProcessItem;
import org.talend.designer.runprocess.IProcessor;
import org.talend.designer.runprocess.ProcessorUtilities;
import org.talend.repository.ui.utils.JavaResourcesHelper;
import org.talend.repository.ui.wizards.exportjob.ExportFileResource;

/**
 * DOC qwei class global comment. Detailled comment <br/>
 * 
 */
public class SpagicJavaDeployManager extends org.talend.repository.ui.wizards.exportjob.scriptsmanager.JobJavaScriptsManager {

    public List<ExportFileResource> getExportResources(ExportFileResource[] process, Map<ExportChoice, Boolean> exportChoice,
            String contextName, String launcher, int statisticPort, int tracePort, String... codeOptions) {

        for (int i = 0; i < process.length; i++) {
            ProcessItem processItem = process[i].getProcess();

            String libPath = calculateLibraryPathFromDirectory(process[i].getDirectoryName());
            // use character @ as temporary classpath separator, this one will be replaced during the export.
            String standardJars = libPath + PATH_SEPARATOR + SYSTEMROUTINE_JAR + ProcessorUtilities.TEMP_JAVA_CLASSPATH_SEPARATOR
                    + libPath + PATH_SEPARATOR + USERROUTINE_JAR + ProcessorUtilities.TEMP_JAVA_CLASSPATH_SEPARATOR + ".";
            ProcessorUtilities.setExportConfig("java", standardJars, libPath);

            generateJobFiles(processItem, contextName, statisticPort != IProcessor.NO_STATISTICS,
                    tracePort != IProcessor.NO_TRACES, BooleanUtils.isTrue(exportChoice.get(ExportChoice.applyToChildren)));
            List<URL> resources = new ArrayList<URL>();
            resources.addAll(getLauncher(BooleanUtils.isTrue(exportChoice.get(ExportChoice.needLauncher)), processItem,
                    escapeSpace(contextName), escapeSpace(launcher), statisticPort, tracePort, codeOptions));

            List<URL> srcList = getSource(processItem, BooleanUtils.isTrue(exportChoice.get(ExportChoice.needSource)));
            process[i].addResources(JOB_SOURCE_FOLDER_NAME, srcList);

            resources.addAll(getJobScripts(processItem, BooleanUtils.isTrue(exportChoice.get(ExportChoice.needJob))));
            resources.addAll(getProperties(processItem, srcList));
            addContextScripts(process[i], BooleanUtils.isTrue(exportChoice.get(ExportChoice.needContext)));

            // add children jobs
            boolean needChildren = BooleanUtils.isTrue(exportChoice.get(ExportChoice.needJob))
                    && BooleanUtils.isTrue(exportChoice.get(ExportChoice.needContext));
            List<URL> childrenList = addChildrenResources(processItem, needChildren, process[i], exportChoice);
            resources.addAll(childrenList);
            process[i].addResources(resources);

            // Gets job designer resouce
            // List<URL> srcList = getSource(processItem, exportChoice.get(ExportChoice.needSource));
            // process[i].addResources(JOB_SOURCE_FOLDER_NAME, srcList);
        }

        // Exports the system libs
        List<ExportFileResource> list = new ArrayList<ExportFileResource>(Arrays.asList(process));
        // Add the java system libraries
        ExportFileResource rootResource = new ExportFileResource(null, LIBRARY_FOLDER_NAME);
        list.add(rootResource);
        // Gets system routines
        List<URL> systemRoutineList = getSystemRoutine(BooleanUtils.isTrue(exportChoice.get(ExportChoice.needSystemRoutine)));
        rootResource.addResources(systemRoutineList);
        // Gets user routines
        List<URL> userRoutineList = getUserRoutine(BooleanUtils.isTrue(exportChoice.get(ExportChoice.needUserRoutine)));
        rootResource.addResources(userRoutineList);

        // Gets talend libraries
        List<URL> talendLibraries = getExternalLibraries(BooleanUtils.isTrue(exportChoice.get(ExportChoice.needTalendLibraries)),
                process);
        rootResource.addResources(talendLibraries);

        return list;
    }

    private List<URL> addChildrenResources(ProcessItem process, boolean needChildren, ExportFileResource resource,
            Map<ExportChoice, Boolean> exportChoice) {
        List<String> list = new ArrayList<String>();
        if (needChildren) {
            String projectName = getCurrentProjectName();
            try {
                List<ProcessItem> processedJob = new ArrayList<ProcessItem>();
                getChildrenJobAndContextName(process.getProperty().getLabel(), list, process, projectName, processedJob,
                        resource, exportChoice);
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }

        }

        List<URL> allJobScripts = new ArrayList<URL>();
        for (Iterator<String> iter = list.iterator(); iter.hasNext();) {
            String jobName = iter.next();
            allJobScripts.addAll(getJobScripts(jobName, exportChoice.get(ExportChoice.needJob)));
            addContextScripts(jobName, resource, exportChoice.get(ExportChoice.needContext));
        }

        return allJobScripts;
    }

    public List<URL> getProperties(ProcessItem processItem, List<URL> srcList) {
        List<URL> list = new ArrayList<URL>();
        Properties p = new Properties();
        FileOutputStream out = null;
        String projectName = getCurrentProjectName();
        String jobName = processItem.getProperty().getLabel();
        String jobFolderName = JavaResourcesHelper.getJobFolderName(escapeFileNameSpace(processItem));
        try {

            IPath path = getSrcRootLocation();
            File file = new File(path + PATH_SEPARATOR + "spagic.properties");
            path = path.append(projectName).append(jobFolderName).append(jobName + ".java");
            BufferedReader buff = new BufferedReader(new FileReader(path.toPortableString()));
            int nbLine = 0;
            while (buff.readLine() != null) {
                nbLine++;
            }
            out = new FileOutputStream(file);
            PrintStream ps = new PrintStream(out);
            p.put("talendJobClassName", getCurrentProjectName() + "."
                    + JavaResourcesHelper.getJobFolderName(processItem.getProperty().getLabel()) + "."
                    + processItem.getProperty().getLabel());
            // p.put("talendJobClassDescription",
            // HTMLDocUtils.checkString(processItem.getProperty().getDescription()).replace("\n",
            // "\\n"));
            p.put("talendJobClassDescription", HTMLDocUtils.checkString(processItem.getProperty().getDescription()));
            p.put("rowNumber", Integer.toString(nbLine));
            p.put("host", "localhost");
            p.list(ps);
            ps.flush();
            list.add(file.toURI().toURL());
        } catch (Exception e) {
            ExceptionHandler.process(e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                }
            }
        }
        return list;
    }
}
