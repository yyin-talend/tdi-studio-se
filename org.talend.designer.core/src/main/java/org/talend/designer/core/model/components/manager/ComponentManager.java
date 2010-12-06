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
package org.talend.designer.core.model.components.manager;

import java.io.File;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.osgi.service.datalocation.Location;
import org.talend.commons.emf.EmfHelper;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.component_cache.ComponentCacheFactory;
import org.talend.core.model.component_cache.ComponentsCache;
import org.talend.core.model.component_cache.util.ComponentCacheResourceFactoryImpl;

/**
 * DOC guanglong.du class global comment. Detailled comment
 */
public class ComponentManager {

    private static ComponentsCache cache;

    private static final String TALEND_JAVA_COMPONENT_CACHE = "ComponentsCache.javacache";

    private static final String TALEND_PERL_COMPONENT_CACHE = "ComponentsCache.perlcache";

    private static final String METADATA_NAME = ".metadata";

    public static ComponentsCache getInstance() {
        if (cache == null) {
            cache = ComponentCacheFactory.eINSTANCE.createComponentsCache();
        }
        return cache;
    }

    public static void saveResource() {
        Location instanceLoc = Platform.getInstanceLocation();
        File file = new File(instanceLoc.getURL().getFile(), ComponentManager.METADATA_NAME);
        String installLocation = file.getAbsolutePath();
        try {
            Resource resource = createComponentCacheResource(installLocation);
            resource.getContents().add(cache);
            EmfHelper.saveResource(cache.eResource());
        } catch (PersistenceException e1) {
            // TODO Auto-generated catch block
            ExceptionHandler.process(e1);
        }
    }

    public static Resource createComponentCacheResource(String installLocation) {
        URI uri = URI.createFileURI(installLocation);
        if (LanguageManager.getCurrentLanguage() == ECodeLanguage.PERL) {
            uri = uri.appendSegment(ComponentManager.TALEND_PERL_COMPONENT_CACHE);
        } else {
            uri = uri.appendSegment(ComponentManager.TALEND_JAVA_COMPONENT_CACHE);
        }
        ComponentCacheResourceFactoryImpl compFact = new ComponentCacheResourceFactoryImpl();
        return compFact.createResource(uri);
    }
}
