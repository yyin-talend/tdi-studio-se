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
package org.talend.designer.codegen.stigma;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMLParserPoolImpl;
import org.talend.core.model.component_cache.ComponentCacheFactory;
import org.talend.core.model.component_cache.ComponentCachePackage;
import org.talend.core.model.component_cache.ComponentsCache;
import org.talend.core.model.component_cache.util.ComponentCacheResourceFactoryImpl;
import org.talend.designer.codegen.components.model.ComponentsFactory;

// useful

/**
 * DOC zwzhao class global comment. Detailled comment
 */
public class StigmaComponentManager {

    private static final String TALEND_COMPONENTS_CACHE_FILE = "components.cache"; //$NON-NLS-1$

    private static Map<String, ComponentsCache> caches = new HashMap<>();

    private static Set<File> providerInstallationFolders;

    private static Set<String> modified = new HashSet<>();

    public static Map<String, ComponentsCache> getComponentCaches() {
        if (caches.isEmpty()) {
            caches.put(ComponentsFactory.APPLICATION_PATH + "/components",
                    ComponentCacheFactory.eINSTANCE.createComponentsCache());
        }
        return caches;
    }

    public static void saveResource() {
        if (!modified.isEmpty()) {
            caches.entrySet().stream().filter(entry -> modified.contains(entry.getKey())).forEach(entry -> {
                try {
                    Resource resource = createComponentCacheResource(entry.getKey());
                    resource.getContents().add(entry.getValue());
                    // EmfHelper.saveResource(entry.getValue().eResource());

                    URI uri = URI.createFileURI(entry.getKey()).appendSegment(TALEND_COMPONENTS_CACHE_FILE);
                    resource.save(new FileOutputStream(uri.toFileString()),
                            Collections.singletonMap(XMLResource.OPTION_ENCODING, "UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            modified.clear();
        }
    }

    public static boolean hasComponentFile(String installLocation) {
        File file = new File(installLocation, TALEND_COMPONENTS_CACHE_FILE);
        return file != null && file.exists();
    }

    // public static void loadComponentResource() throws IOException {
    // caches.clear();
    // for (File installationFolder : getProviderInstallationFolders()) {
    // ComponentsCache loadedComponentCache = loadComponentResource(installationFolder);
    // if (loadedComponentCache != null) {
    // caches.put(installationFolder.getAbsolutePath(), loadedComponentCache);
    // }
    // }
    // }

    public static ComponentsCache loadComponentResource(File installationFolder) {

        ComponentCachePackage.eINSTANCE.eClass();
        URI uri = URI.createFileURI(installationFolder.getAbsolutePath()).appendSegment(TALEND_COMPONENTS_CACHE_FILE);
        ComponentCacheResourceFactoryImpl compFact = new ComponentCacheResourceFactoryImpl();
        Resource resource = compFact.createResource(uri);
        Map<Object, Object> optionMap = new HashMap<>();
        optionMap.put(XMLResource.OPTION_DEFER_ATTACHMENT, Boolean.TRUE);
        optionMap.put(XMLResource.OPTION_DEFER_IDREF_RESOLUTION, Boolean.TRUE);
        optionMap.put(XMLResource.OPTION_USE_PARSER_POOL, new XMLParserPoolImpl());
        optionMap.put(XMLResource.OPTION_USE_XML_NAME_TO_FEATURE_MAP, new HashMap<>());
        optionMap.put(XMLResource.OPTION_USE_DEPRECATED_METHODS, Boolean.FALSE);

        try {
            resource.load(optionMap);

            ComponentsCache loadedComponentCache = (ComponentsCache) EcoreUtil.getObjectByType(resource.getContents(),
                    ComponentCachePackage.eINSTANCE.getComponentsCache());

            if (loadedComponentCache != null) {
                caches.put(installationFolder.getAbsolutePath(), loadedComponentCache);
            }
            return loadedComponentCache;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void clearAllCaches() {
        caches.clear();
    }

    public static void setModified(String installationFolder) {
        modified.add(installationFolder);
    }

    private static Resource createComponentCacheResource(String installLocation) {
        URI uri = URI.createFileURI(installLocation).appendSegment(TALEND_COMPONENTS_CACHE_FILE);
        ComponentCacheResourceFactoryImpl compFact = new ComponentCacheResourceFactoryImpl();
        return compFact.createResource(uri);
    }
}
