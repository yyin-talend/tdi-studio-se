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
package org.talend.designer.components.model;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.talend.core.model.components.AbstractComponentsProvider;

/***/
public final class ComponentsProviderManager {

    private static Logger log = Logger.getLogger(ComponentsProviderManager.class);

    private static final ComponentsProviderManager INSTANCE = new ComponentsProviderManager();

    private Collection<AbstractComponentsProvider> providers;

    private ComponentsProviderManager() {
    }

    public static ComponentsProviderManager getInstance() {
        return INSTANCE;
    }

    public Collection<AbstractComponentsProvider> getProviders() {
        if (providers == null) {
            loadComponentsProvidersFromExtension();
        }
        return providers;
    }

    private void loadComponentsProvidersFromExtension() {
        providers = new ArrayList<AbstractComponentsProvider>();

        IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
        IExtensionPoint extensionPoint = extensionRegistry.getExtensionPoint("org.talend.core.components_provider");
        IExtension[] extensions = extensionPoint.getExtensions();
        for (IExtension extension : extensions) {
            IConfigurationElement[] configurationElements = extension.getConfigurationElements();
            for (IConfigurationElement configurationElement : configurationElements) {
                String id = configurationElement.getAttribute("id");
                String folderName = configurationElement.getAttribute("folderName");
                try {
                    AbstractComponentsProvider componentsProvider = (AbstractComponentsProvider) configurationElement
                            .createExecutableExtension("class");
                    componentsProvider.setId(id);
                    componentsProvider.setFolderName(folderName);
                    providers.add(componentsProvider);
                } catch (CoreException e) {
                    log.error("unable to load component provider " + id, e);
                }
            }
        }
    }
}
