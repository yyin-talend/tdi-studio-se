// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.model.migration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URL;
import java.util.List;

import org.apache.commons.codec.binary.StringUtils;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.designer.core.model.utils.emf.talendfile.ElementParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;
import org.talend.model.bridge.ReponsitoryContextBridge;
import org.talend.repository.ProjectManager;
import org.talend.repository.items.importexport.handlers.ImportExportHandlersManager;
import org.talend.repository.items.importexport.handlers.model.ImportItem;
import org.talend.repository.items.importexport.manager.ResourcesManager;
import org.talend.repository.items.importexport.ui.managers.FileResourcesUnityManager;
import org.talend.repository.items.importexport.ui.managers.ResourcesManagerFactory;
import org.talend.repository.model.IProxyRepositoryFactory;

public class SaveDefaultClostdListValueForTckMigrationTaskTest {

    private static List<ImportItem> importedItems = null;

    @BeforeClass
    public static void setUp() throws Exception {

        ReponsitoryContextBridge.setProject(ProjectManager.getInstance().getCurrentProject().getEmfProject());

        ImportExportHandlersManager importManager = new ImportExportHandlersManager();
        URL testJobURL = FileLocator.find(Platform.getBundle("org.talend.repository.test"), new Path("/resources/tck801.zip"), null);
        if (testJobURL != null) {
            testJobURL = FileLocator.toFileURL(testJobURL);
        }
        File srcFile = new File(testJobURL.getFile());
        FileResourcesUnityManager fileUnityManager = ResourcesManagerFactory.getInstance().createFileUnityManager(srcFile);
        ResourcesManager resManager = fileUnityManager.doUnify();
        importedItems = importManager.populateImportingItems(resManager, true, new NullProgressMonitor());
        assertNotNull(importedItems);
        assertTrue(importedItems.size() > 0);
        importManager.importItemRecords(new NullProgressMonitor(), resManager, importedItems, true, importedItems.toArray(new ImportItem[0]), null);

    }

    @AfterClass
    public static void tearDown() throws Exception {
        if (importedItems != null && !importedItems.isEmpty()) {
            for (ImportItem item : importedItems) {
                IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
                IRepositoryViewObject repObj = factory.getLastVersion(item.getItemId());
                if (repObj != null) {
                    factory.deleteObjectPhysical(repObj);
                }
            }
            importedItems = null;
        }
    }

    @Test
    public void checkTck() throws Exception {
        List<IRepositoryViewObject> objects = getAll(ERepositoryObjectType.PROCESS);
        assertNotNull(objects);
        boolean needCheck = false;
        ProcessType processType = null;
        for (IRepositoryViewObject object : objects) {
            ProcessItem processItem = (ProcessItem) object.getProperty().getItem();

            if (StringUtils.equals(processItem.getProperty().getDisplayName(), "tck")) {
                processType = processItem.getProcess();
                for (Object nodeObject : processType.getNode()) {
                    NodeType nodeType = (NodeType) nodeObject;
                    for (Object paramObjectType : nodeType.getElementParameter()) {
                        ElementParameterType param = (ElementParameterType) paramObjectType;
                        if (param.getField() != null) {
                            if (EParameterFieldType.TECHNICAL.getName().equals(param.getField()) && param.getName().equals("TACOKIT_COMPONENT_ID")) {
                                needCheck = true;
                                break;
                            }
                        }
                    }
                }
            }
        }

        assertTrue(needCheck);

        boolean findApiVersion = false;
        boolean findLoginType = false;
        for (Object nodeObject : processType.getNode()) {
            NodeType nodeType = (NodeType) nodeObject;
            for (Object paramObjectType : nodeType.getElementParameter()) {
                ElementParameterType param = (ElementParameterType) paramObjectType;
                if (param.getField() != null) {
                    if (EParameterFieldType.CLOSED_LIST.getName().equals(param.getField()) && param.getName().equals("configuration.dataSet.dataStore.apiVersion")) {
                        findApiVersion = true;
                        assertEquals(param.getValue(), "V2019_2");
                    }
                    if (EParameterFieldType.CLOSED_LIST.getName().equals(param.getField()) && param.getName().equals("configuration.dataSet.dataStore.loginType")) {
                        findLoginType = true;
                        assertEquals(param.getValue(), "BASIC");
                    }
                }
            }
        }
        assertTrue(findApiVersion);
        assertTrue(findLoginType);
    }

    private List<IRepositoryViewObject> getAll(ERepositoryObjectType type) throws PersistenceException {
        return ProxyRepositoryFactory.getInstance().getAll(type);
    }

}
