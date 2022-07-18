// ============================================================================
//
// Copyright (C) 2006-2020 Talend Inc. - www.talend.com
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

import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.model.component_cache.ComponentInfo;
import org.talend.core.model.component_cache.ComponentsCache;
import org.talend.core.model.components.AbstractComponentsProvider;
// import org.talend.core.model.components.ComponentManager;
import org.talend.core.model.components.IComponent;
import org.talend.designer.core.model.components.EmfComponent;

// useful
// modified

/*
 * Created by bhe on Dec 31, 2020
 */
public class StigmaComponentsLoader {

    private static Logger LOGGER = Logger.getLogger(StigmaComponentsLoader.class);

    private static final StigmaComponentsLoader INSTANCE = new StigmaComponentsLoader();

    // private final AbstractComponentsProvider componentsProvider = new LocalComponentsProvider();

    private StigmaComponentsLoader() {
    }

    public static StigmaComponentsLoader getInstance() {
        return INSTANCE;
    }

    public void loadAllComponentsFromIndex(Set<IComponent> allSet) {
        int total = 0;
        for (ComponentsCache cache : StigmaComponentManager.getComponentCaches().values()) {
            Set<Entry<String, EList<ComponentInfo>>> entries = cache.getComponentEntryMap().entrySet();
            for (Entry<String, EList<ComponentInfo>> entry : entries) {
                for (ComponentInfo ci : entry.getValue()) {
                    try {
                        EmfComponent comp = new EmfComponent(entry.getKey(), ci, null);

                        if (skipLoadComponent(comp)) {
                            continue;
                        }

                        setComponentPaletteType(comp);

                        allSet.add(comp);
                        total++;

                        LOGGER.info("comp loaded: " + comp.getName() + ",palette type: " + comp.getPaletteType());

                    } catch (Exception e) {
                        ExceptionHandler.process(e);
                    }
                }
            }
        }
        LOGGER.info("allSet size: " + allSet.size() + ", total: " + total);
    }

    public static boolean skipLoadComponent(EmfComponent comp) {
        // if the component is not needed in the current branding
        // if (ComponentsFactoryProviderManager.getInstance()
        // .getProviders()
        // .stream()
        // .anyMatch(p -> !p.isAvailable(comp.getName()))) {
        // if NOT a specific component for code generation, just don't load it
        if (!comp.getOriginalFamilyName().contains("Technical") && !comp.isTechnical()) {
            return true;
        }
        // if IS a specific component for code generation, hide it
        comp.setVisible(false);
        comp.setTechnical(true);
        // }
        return false;
    }

    public static void setComponentPaletteType(EmfComponent comp) {
        // comp.setPaletteType(
        // comp.getProvider().getId().contains("Camel") ? ComponentCategory.CATEGORY_4_CAMEL.getName()
        // : comp.getType());
        comp.setPaletteType(comp.getType());
    }

    public static boolean isCachedProvider(AbstractComponentsProvider provider) {
        // FIXME skip mr and storm provider of TDM because they are not implemented yet
        return !provider.isCustom() && !provider.getId().equals("org.talend.transform.components.MRComponentsProvider")
                && !provider.getId().equals("org.talend.transform.components.StormComponentsProvider");
    }

}
