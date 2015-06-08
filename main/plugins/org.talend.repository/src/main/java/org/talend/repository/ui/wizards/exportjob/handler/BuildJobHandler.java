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
package org.talend.repository.ui.wizards.exportjob.handler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ITDQItemService;
import org.talend.core.PluginChecker;
import org.talend.core.model.process.ProcessUtils;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.Project;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.runprocess.LastGenerationInfo;
import org.talend.core.model.utils.JavaResourcesHelper;
import org.talend.core.repository.constants.FileConstants;
import org.talend.core.service.ITransformService;
import org.talend.core.ui.ITestContainerProviderService;
import org.talend.designer.maven.model.TalendMavenConstants;
import org.talend.designer.runprocess.ProcessorUtilities;
import org.talend.model.bridge.ReponsitoryContextBridge;
import org.talend.repository.local.ExportItemUtil;
import org.talend.repository.model.RepositoryConstants;
import org.talend.repository.ui.wizards.exportjob.scriptsmanager.JobScriptsManager.ExportChoice;
import org.talend.utils.io.FilesUtils;

/**
 * created by ycbai on 2015年5月13日 Detailled comment
 *
 */
public class BuildJobHandler extends AbstractBuildJobHandler {

    public BuildJobHandler(ProcessItem processItem, String version, String contextName, Map<ExportChoice, Object> exportChoiceMap) {
        super(processItem, version, contextName, exportChoiceMap);
    }

    @Override
    public void generateJobFiles(IProgressMonitor monitor) throws Exception {
        generateJobFiles(monitor, false, false);
    }

    @Override
    public void generateJobFiles(IProgressMonitor monitor, boolean stats, boolean trace) throws Exception {
        LastGenerationInfo.getInstance().getUseDynamicMap().clear();
        int generationOption = (isOptionChoosed(ExportChoice.includeTestSource) || isOptionChoosed(ExportChoice.executeTests)) ? ProcessorUtilities.GENERATE_ALL_CHILDS
                | ProcessorUtilities.GENERATE_TESTS
                : ProcessorUtilities.GENERATE_ALL_CHILDS;

        ProcessorUtilities.generateCode(processItem, contextName, version, stats, false,
                isOptionChoosed(ExportChoice.applyToChildren), isOptionChoosed(ExportChoice.needContext), generationOption,
                monitor);
    }

    @Override
    public void generateTestReports(IProgressMonitor monitor) throws Exception {
        if (!isOptionChoosed(ExportChoice.includeTestSource)) {
            return;
        }
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ITestContainerProviderService.class)) {
            ITestContainerProviderService testContainerService = (ITestContainerProviderService) GlobalServiceRegister
                    .getDefault().getService(ITestContainerProviderService.class);
            if (testContainerService != null) {
                List<IFile> reports = new ArrayList<IFile>();
                List<ProcessItem> testsItems = testContainerService.getAllTestContainers(processItem);
                for (ProcessItem testItem : testsItems) {
                    List<IFile> testReportFiles = testContainerService.getTestReportFiles(testItem);
                    if (testReportFiles.size() > 0) {
                        reports.add(testReportFiles.get(0));
                    }
                }
                IFolder testsFolder = talendProcessJavaProject.getTestsFolder();
                talendProcessJavaProject.cleanFolder(monitor, testsFolder);
                IFolder parentFolder = talendProcessJavaProject.createSubFolder(monitor, testsFolder, processItem.getProperty()
                        .getLabel());
                for (IFile report : reports) {
                    report.copy(parentFolder.getFile(report.getName()).getFullPath(), true, monitor);
                }
            }
        }
    }

    @Override
    public void generateItemFiles(boolean withDependencies, IProgressMonitor monitor) throws Exception {
        if (!isOptionChoosed(ExportChoice.needJobItem)) {
            return;
        }
        IFolder baseFolder = talendProcessJavaProject.getItemsFolder();
        baseFolder = baseFolder.getFolder(JavaResourcesHelper.getJobFolderName(processItem.getProperty().getLabel(), processItem
                .getProperty().getVersion()));
        if (baseFolder.exists()) {
            talendProcessJavaProject.cleanFolder(monitor, baseFolder);
        } else {
            baseFolder.create(true, true, null);
        }
        IFolder itemsFolder = baseFolder.getFolder("items");
        itemsFolder.create(true, true, monitor);
        List<Item> items = new ArrayList<Item>();
        items.add(processItem);
        ExportItemUtil exportItemUtil = new ExportItemUtil();
        if (withDependencies) {
            Collection<IRepositoryViewObject> allProcessDependencies = ProcessUtils.getAllProcessDependencies(items);
            for (IRepositoryViewObject repositoryObject : allProcessDependencies) {
                items.add(repositoryObject.getProperty().getItem());
            }
        }
        File destination = new File(itemsFolder.getLocation().toFile().getAbsolutePath());
        exportItemUtil.setProjectNameAsLowerCase(true);
        exportItemUtil.exportItems(destination, new ArrayList<Item>(items), false, new NullProgressMonitor());
        addDQDependencies(itemsFolder, items);
        addTDMDependencies(baseFolder, items);
    }

    /**
     * DOC nrousseau Comment method "addTDMDependencies".
     * 
     * @param items
     * @param itemsFolder
     */
    private void addTDMDependencies(IFolder baseFolder, List<Item> items) {
        ITransformService tdmService = null;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ITransformService.class)) {
            tdmService = (ITransformService) GlobalServiceRegister.getDefault().getService(ITransformService.class);
        }
        try {
            for (Item item : items) {
                // add .settings/com.oaklandsw.base.projectProps for tdm
                if (tdmService != null && !tdmService.isTransformItem(item)) {
                    String itemProjectFolder = getProject(item).getTechnicalLabel().toLowerCase();
                    IFolder targetProjectFolder = baseFolder.getFolder(itemProjectFolder);
                    IFolder targetSettingsFolder = null;
                    if (!targetProjectFolder.exists()) {
                        targetProjectFolder.create(true, true, new NullProgressMonitor());
                        targetSettingsFolder = targetProjectFolder.getFolder(RepositoryConstants.SETTING_DIRECTORY);
                        if (!targetSettingsFolder.exists()) {
                            targetSettingsFolder.create(true, true, new NullProgressMonitor());
                        }
                    }
                    IProject sourceProject = getCorrespondingProjectRootFolder(item);

                    if (sourceProject.exists()) {
                        IFolder settingsFolder = sourceProject.getFolder(RepositoryConstants.SETTING_DIRECTORY);
                        if (settingsFolder.exists()) {
                            IFile settingsFile = settingsFolder.getFile(FileConstants.TDM_PROPS);
                            if (settingsFile.exists()) {
                                if (targetSettingsFolder != null && targetSettingsFolder.exists()) {
                                    settingsFile.copy(targetSettingsFolder.getFullPath(), true, null);
                                }
                            }
                        }
                    }
                }
            }
        } catch (CoreException e) {
            // don't block all the export if got exception here
            ExceptionHandler.process(e);
        }
    }

    protected IProject getCorrespondingProjectRootFolder(Item item) throws CoreException {
        // for bug 17685
        org.talend.core.model.properties.Project p = getProject(item);
        IProject project = null;
        if (p != null) {
            project = ResourcesPlugin.getWorkspace().getRoot().getProject(p.getTechnicalLabel().toUpperCase());
            if (project != null) {
                return project;
            }
        }
        return null;
    }

    private void addDQDependencies(IFolder parentFolder, List<Item> items) throws IOException {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ITDQItemService.class)) {
            ITDQItemService tdqItemService = (ITDQItemService) GlobalServiceRegister.getDefault().getService(
                    ITDQItemService.class);
            for (Item item : items) {
                if (tdqItemService != null && tdqItemService.hasProcessItemDependencies(Arrays.asList(new Item[] { item }))) {
                    // add .Talend.definition file
                    String defIdxFolderName = "TDQ_Libraries"; //$NON-NLS-1$
                    String defIdxFileName = ".Talend.definition"; //$NON-NLS-1$
                    Project pro = getProject(processItem);
                    IFolder itemsProjectFolder = parentFolder.getFolder(pro.getTechnicalLabel().toLowerCase());
                    File itemsFolderDir = new File(parentFolder.getLocation().toFile().getAbsolutePath());
                    IProject project = ReponsitoryContextBridge.getRootProject();
                    String defIdxRelativePath = defIdxFolderName + PATH_SEPARATOR + defIdxFileName;
                    IFile defIdxFile = project.getFile(defIdxRelativePath);
                    if (defIdxFile.exists()) {
                        File defIdxFileSource = new File(project.getLocation().makeAbsolute().append(defIdxFolderName)
                                .append(defIdxFileName).toFile().toURI());
                        File defIdxFileTarget = new File(itemsProjectFolder.getFile(defIdxRelativePath).getLocation().toFile()
                                .getAbsolutePath());
                        FilesUtils.copyFile(defIdxFileSource, defIdxFileTarget);
                    }
                    // add report header image & template files
                    String reportingBundlePath = PluginChecker.getBundlePath("org.talend.dataquality.reporting"); //$NON-NLS-1$
                    File imageFolder = new File(reportingBundlePath + PATH_SEPARATOR + "images"); //$NON-NLS-1$
                    if (imageFolder.exists()) {
                        FilesUtils.copyDirectory(imageFolder, itemsFolderDir);
                    }
                    File templateFolder = new File(reportingBundlePath + PATH_SEPARATOR + "reports"); //$NON-NLS-1$ 
                    if (templateFolder.exists() && templateFolder.isDirectory()) {
                        FilesUtils.copyDirectory(templateFolder, itemsFolderDir);
                    }
                }
            }
        }
    }

    @Override
    public void build(String destinationPath, IProgressMonitor monitor) throws Exception {
        talendProcessJavaProject.buildModules(TalendMavenConstants.GOAL_PACKAGE, null, getProgramArgs(), monitor);
        IFile jobTargetFile = getJobTargetFile();
        if (jobTargetFile.exists()) {
            File jobFileSource = new File(jobTargetFile.getLocation().toFile().getAbsolutePath());
            File jobFileTarget = new File(destinationPath);
            if (jobFileTarget.isDirectory()) {
                jobFileTarget = new File(destinationPath, jobFileSource.getName());
            }
            FilesUtils.copyFile(jobFileSource, jobFileTarget);
        }
    }

}
