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

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.talend.commons.CommonsPlugin;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.utils.generation.JavaUtils;
import org.talend.commons.utils.resource.FileExtensions;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.hadoop.version.EHadoopDistributions;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.utils.JavaResourcesHelper;
import org.talend.designer.core.IDesignerCoreService;
import org.talend.designer.core.model.utils.emf.talendfile.ElementParameterType;
import org.talend.designer.runprocess.IRunProcessService;
import org.talend.repository.ui.utils.ZipToFile;
import org.talend.repository.ui.wizards.exportjob.JavaJobExportReArchieveCreator;
import org.talend.utils.io.FilesUtils;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class BDJobReArchieveCreator {

    private ProcessItem processItem;

    private Boolean isMRWithHDInsight, isStormJob;

    public BDJobReArchieveCreator(ProcessItem processItem) {
        this.processItem = processItem;
    }

    /**
     * copied from MapReduceJobJavaScriptsManager.
     */
    public boolean isMRWithHDInsight() {
        if (isMRWithHDInsight == null && processItem != null) {
            isMRWithHDInsight = false;
            EList<ElementParameterType> parameters = processItem.getProcess().getParameters().getElementParameter();
            for (ElementParameterType pt : parameters) {
                if (pt.getName().equals("DISTRIBUTION")
                        && EHadoopDistributions.MICROSOFT_HD_INSIGHT.getName().equals(pt.getValue())) {
                    isMRWithHDInsight = true;
                    break;
                }
            }
        }
        return isMRWithHDInsight;
    }

    public boolean isStormJob() {
        if (isStormJob == null && processItem != null) {
            isStormJob = ERepositoryObjectType.getItemType(processItem).equals(ERepositoryObjectType.PROCESS_STORM);
        }
        return isStormJob;
    }

    public void create(File zipFile) {
        if (zipFile == null || !zipFile.exists() || !zipFile.isFile()) {
            return;
        }

        // check
        if (!isMRWithHDInsight() && !isStormJob()) {
            return;
        }
        Property property = processItem.getProperty();
        String label = property.getLabel();
        String version = property.getVersion();

        JavaJobExportReArchieveCreator creator = new JavaJobExportReArchieveCreator(zipFile.getAbsolutePath(), label);
        try {
            // create temp folders.
            creator.deleteTempFiles(); // clean temp folder
            File zipTmpFolder = new File(creator.getTmpFolder(), "zip-" + label + "_" + version);
            File jarTmpFolder = new File(creator.getTmpFolder(), "jar-" + label + "_" + version);
            zipTmpFolder.mkdirs();
            jarTmpFolder.mkdirs();

            // unzip the files.
            FilesUtils.unzip(zipFile.getAbsolutePath(), zipTmpFolder.getAbsolutePath());

            String jobJarName = JavaResourcesHelper.getJobJarName(property.getLabel(), property.getVersion())
                    + FileExtensions.JAR_FILE_SUFFIX;
            File originalJarFile = new File(zipTmpFolder, label.toLowerCase() + '/' + jobJarName);
            if (!originalJarFile.exists()) { // can't find the job jar.
                return;
            }
            FilesUtils.unzip(originalJarFile.getAbsolutePath(), jarTmpFolder.getAbsolutePath());
            // re-build the job jar with lib.
            File newJarFile = new File(creator.getTmpFolder(), jobJarName);
            FilesUtils.copyFile(originalJarFile, newJarFile); // make sure enable to package the libs.
            JarBuilder jarbuilder = new JarBuilder(jarTmpFolder, newJarFile);
            String jobClassPackageFolder = JavaResourcesHelper.getJobClassPackageFolder(property.getItem());
            jarbuilder.setIncludeDir(Collections.singleton(jobClassPackageFolder));
            jarbuilder.setExcludeDir(null);
            if (isMRWithHDInsight()) {
                jarbuilder.setLibPath(getLibPath(zipTmpFolder, true));
            } else if (isStormJob()) {
                jarbuilder.setLibPath(getLibPath(zipTmpFolder, false));
                jarbuilder.setStorm(true);
            }
            jarbuilder.buildJar();

            // use new jar to overwrite old one.
            if (originalJarFile.exists()) {
                originalJarFile.delete();
            }
            FilesUtils.copyFile(newJarFile, originalJarFile);

            // zip
            ZipToFile.zipFile(zipTmpFolder.getAbsolutePath(), zipFile.getAbsolutePath());
        } catch (Exception e) {
            ExceptionHandler.process(e);
        } finally {
            // when debug, keep the files first. because when build again. will do clean also.
            if (!CommonsPlugin.isDebugMode()) {
                creator.deleteTempFiles();
            }
        }
    }

    /**
     * 
     * maybe, should be same result with JobJavaScriptsManager.getLibPath
     */
    public List<File> getLibPath(File zipTmpFolder, boolean isSpecialMR) {
        List<File> neededLibFiles = new ArrayList<File>();
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IRunProcessService.class)
                && GlobalServiceRegister.getDefault().isServiceRegistered(IDesignerCoreService.class)) {
            IRunProcessService processService = (IRunProcessService) GlobalServiceRegister.getDefault().getService(
                    IRunProcessService.class);
            IDesignerCoreService designerCoreService = (IDesignerCoreService) GlobalServiceRegister.getDefault().getService(
                    IDesignerCoreService.class);
            IProcess process = designerCoreService.getProcessFromProcessItem(processItem);
            File libFolder = new File(zipTmpFolder, JavaUtils.JAVA_LIB_DIRECTORY);
            File[] listFiles = libFolder.listFiles();
            if (process != null && listFiles != null) {
                Set<String> libJarsForBD = processService.getLibJarsForBD(process);
                for (File libFile : listFiles) {
                    if (libJarsForBD.contains(libFile.getName())) {
                        neededLibFiles.add(libFile);
                    }
                }
            }
        }
        return neededLibFiles;
    }

}
