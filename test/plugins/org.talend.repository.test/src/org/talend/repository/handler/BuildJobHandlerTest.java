package org.talend.repository.handler;

import static org.junit.Assert.*;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Assert;
import org.junit.Test;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.repository.items.importexport.handlers.ImportExportHandlersManager;
import org.talend.repository.items.importexport.handlers.model.ImportItem;
import org.talend.repository.items.importexport.manager.ResourcesManager;
import org.talend.repository.items.importexport.ui.managers.FileResourcesUnityManager;
import org.talend.repository.items.importexport.ui.managers.ResourcesManagerFactory;
import org.talend.repository.ui.wizards.exportjob.handler.BuildJobHandler;
import org.talend.repository.ui.wizards.exportjob.scriptsmanager.JobScriptsManager.ExportChoice;

public class BuildJobHandlerTest {

    @Test
    public void testBuildJobDenpendenciesCheck() throws Exception {
        ProcessItem processItem = null;
        ImportExportHandlersManager importManager = new ImportExportHandlersManager();
        // job with tHmap.
        File srcFile = new File("resources/testTDMJob.zip");
        FileResourcesUnityManager fileUnityManager = ResourcesManagerFactory.getInstance().createFileUnityManager(srcFile);
        ResourcesManager resManager = fileUnityManager.doUnify();
        List<ImportItem> projectRecords = importManager.populateImportingItems(resManager, true, new NullProgressMonitor());
        Assert.assertTrue(projectRecords.size() > 0);
        importManager.importItemRecords(new NullProgressMonitor(), resManager, projectRecords, true,
                projectRecords.toArray(new ImportItem[0]), null);

        IRepositoryViewObject obj = ProxyRepositoryFactory.getInstance().getLastVersion("_EtK5gPAJEeWJrK20z22A8Q");
        Item item = obj.getProperty().getItem();
        assertTrue(item instanceof ProcessItem);
        processItem = (ProcessItem) item;
        Map<ExportChoice, Object> exportChoiceMap = new HashMap<ExportChoice, Object>();
        exportChoiceMap.put(ExportChoice.needLauncher, true);
        exportChoiceMap.put(ExportChoice.needSystemRoutine, true);
        exportChoiceMap.put(ExportChoice.needUserRoutine, true);
        exportChoiceMap.put(ExportChoice.needTalendLibraries, true);
        exportChoiceMap.put(ExportChoice.needJobItem, false);
        exportChoiceMap.put(ExportChoice.needJobScript, true);
        exportChoiceMap.put(ExportChoice.needSourceCode, true);
        exportChoiceMap.put(ExportChoice.includeLibs, true);
        exportChoiceMap.put(ExportChoice.includeTestSource, false);
        exportChoiceMap.put(ExportChoice.executeTests, false);
        exportChoiceMap.put(ExportChoice.binaries, true);
        exportChoiceMap.put(ExportChoice.needContext, true);
        exportChoiceMap.put(ExportChoice.contextName, "Default");
        exportChoiceMap.put(ExportChoice.applyToChildren, false);
        exportChoiceMap.put(ExportChoice.needLog4jLevel, false);
        exportChoiceMap.put(ExportChoice.log4jLevel, null);
        exportChoiceMap.put(ExportChoice.needDependencies, true);
        exportChoiceMap.put(ExportChoice.needParameterValues, false);
        BuildJobHandler handler = new BuildJobHandler(processItem, "0.1", "Default", exportChoiceMap);
        handler.generateItemFiles(true, new NullProgressMonitor());
        boolean hasItemDependencies = handler.hasItemDependencies();
        assertFalse(hasItemDependencies);
    }

}
