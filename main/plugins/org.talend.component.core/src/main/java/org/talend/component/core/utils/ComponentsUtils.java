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
package org.talend.component.core.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.talend.commons.exception.BusinessException;
import org.talend.component.core.model.Component;
import org.talend.components.api.properties.ComponentDefinition;
import org.talend.components.api.properties.ComponentProperties;
import org.talend.components.api.service.ComponentService;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.components.IComponentsFactory;
import org.talend.core.ui.component.ComponentsFactoryProvider;

/**
 * created by hcyi on Sep 11, 2015 Detailled comment
 *
 */
public class ComponentsUtils {

    private static List<IComponent> components = null;

    public static ComponentService getComponentService() {
        ComponentService compService = null;
        BundleContext bundleContext = FrameworkUtil.getBundle(ComponentsUtils.class).getBundleContext();
        ServiceReference<ComponentService> compServiceRef = bundleContext.getServiceReference(ComponentService.class);
        if (compServiceRef != null) {
            compService = bundleContext.getService(compServiceRef);
        }
        return compService;
    }

    public static void loadComponents(ComponentService service) {
        IComponentsFactory componentsFactory = null;
        if (componentsFactory == null) {
            componentsFactory = ComponentsFactoryProvider.getInstance();
        }
        Set<IComponent> componentsList = componentsFactory.getComponents();
        if (components == null) {
            components = new ArrayList<IComponent>();
        } else {
            componentsList.removeAll(components);
        }
        Map<String, IComponent> existComponents = new HashMap<String, IComponent>();

        for (IComponent component : componentsList) {
            existComponents.put(component.getName(), component);
        }

        // Load components from service
        Set<ComponentDefinition> componentDefinitions = service.getAllComponents();
        for (ComponentDefinition componentDefinition : componentDefinitions) {
            try {
                Component currentComponent = new Component(componentDefinition);
                componentsList.add(currentComponent);
            } catch (BusinessException e) {
                e.printStackTrace();
            }
        }
        // TODO Try to get all component properties
        Set<String> componentNames = service.getAllComponentNames();
        for (String componentName : componentNames) {
            ComponentProperties componentProperty = service.getComponentProperties(componentName);
            // System.out.println("Loading:" + componentName);
        }
    }
}
