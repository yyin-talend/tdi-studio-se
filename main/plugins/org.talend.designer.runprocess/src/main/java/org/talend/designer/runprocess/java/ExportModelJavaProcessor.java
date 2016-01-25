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
package org.talend.designer.runprocess.java;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.eclipse.core.runtime.IProgressMonitor;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.properties.Property;
import org.talend.designer.runprocess.IProcessMessageManager;
import org.talend.designer.runprocess.ProcessorException;
import org.talend.repository.ui.utils.ZipToFile;

/**
 * Created by wchen on Mar 5, 2013.
 */
public class ExportModelJavaProcessor extends JavaProcessor {

    private String unzipFolder;

    /**
     * DOC wchen ExportModelJavaProcessor constructor comment.
     * 
     * @param process
     * @param property
     * @param filenameFromLabel
     */
    public ExportModelJavaProcessor(IProcess process, Property property, boolean filenameFromLabel) {
        super(process, property, filenameFromLabel);
    }

    @Override
    public Process run(int statisticsPort, int tracePort, String watchParam, IProgressMonitor monitor,
            IProcessMessageManager processMessageManager) throws ProcessorException {
        ExportProcessorHelper helper = new ExportProcessorHelper();

        // export job
        String archive = helper.exportJob(this, statisticsPort, tracePort, watchParam, monitor);
        unzipFolder = unzipAndDeploy(process, archive);

        Process process = super.execFrom(unzipFolder + File.separatorChar + this.process.getName(), Level.INFO, statisticsPort,
                tracePort, new String[] { watchParam });

        // delete tempfiles after exeute ??
        // FilesUtils.deleteFile(new File(archive), true);
        // FilesUtils.deleteFile(new File(unzipFolder), true);
        return process;

    }

    @Override
    protected String getLibFolderInWorkingDir() {
        return unzipFolder + File.separator + "lib";
    }

    private String unzipAndDeploy(IProcess process, String archiveZipFileStr) {
        String unzipFolder = unzipProcess(process, archiveZipFileStr);
        return unzipFolder;
    }

    private String unzipProcess(IProcess process, String archiveZipFileStr) {
        // throws OozieJobDeployException {
        String jobName = process.getName();
        String tempFolder = null;
        if (archiveZipFileStr != null && !"".equals(archiveZipFileStr)) {
            File file = new File(archiveZipFileStr);
            tempFolder = file.getParentFile().getPath() + File.separator + jobName;
            try {
                ZipToFile.unZipFile(archiveZipFileStr, tempFolder);
            } catch (Exception e) {
                // throw new OozieJobDeployException("Can not unzip a file!", e);
            }
        }
        return tempFolder;
    }

    @Override
    public String[] getCommandLine() throws ProcessorException {
        return super.getCommandLine();
    }

    protected List<String> extractJobJar() {
        List<String> list = new ArrayList<String>();
        String jobFolderString = unzipFolder + File.separator + process.getName();
        File jobFolder = new File(jobFolderString);
        File[] jarFiles = jobFolder.listFiles(new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                if (pathname.getName().endsWith(".jar")) {//$NON-NLS-1$
                    return true;
                }
                return false;
            }
        });
        if (jarFiles != null && jarFiles.length > 0) {
            for (File tmpFile : jarFiles) {
                list.add(tmpFile.getAbsolutePath() + extractClassPathSeparator());
            }
        }
        return list;
    }
}
