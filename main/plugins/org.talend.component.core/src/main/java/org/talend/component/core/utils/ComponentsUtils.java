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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.talend.commons.exception.BusinessException;
import org.talend.component.core.model.Component;
import org.talend.component.core.model.GenericElementParameter;
import org.talend.components.api.NamedThing;
import org.talend.components.api.component.ComponentDefinition;
import org.talend.components.api.properties.ComponentProperties;
import org.talend.components.api.properties.PresentationItem;
import org.talend.components.api.properties.presentation.Form;
import org.talend.components.api.properties.presentation.Widget;
import org.talend.components.api.schema.SchemaElement;
import org.talend.components.api.service.ComponentService;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.components.IComponentsFactory;
import org.talend.core.model.process.EComponentCategory;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.IElement;
import org.talend.core.ui.component.ComponentsFactoryProvider;
import org.talend.designer.core.model.components.ElementParameter;

/**
 * created by hcyi on Sep 11, 2015 Detailled comment
 *
 */
public class ComponentsUtils {

    public static final String DIR_SEPARATOR = "/";//$NON-NLS-1$ 

    public static final String EXP_SEPARATOR = ".";//$NON-NLS-1$ 

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

    public static ComponentProperties getComponentProperties(String compName) {
        return getComponentService().getComponentProperties(compName);
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
        }
    }

    /**
     * DOC ycbai Comment method "loadParametersFromForm".
     * <p>
     * Get element parameters of <code>element</code> from <code>form</code>.
     * 
     * @param element
     * @param category
     * @param form
     * @return parameters list
     */
    public static List<ElementParameter> getParametersFromForm(IElement element, EComponentCategory category, Form form,
            ElementParameter parentParam, AtomicInteger lastRowNum) {
        List<ElementParameter> elementParameters = new ArrayList<>();
        if (category == null) {
            category = EComponentCategory.BASIC;
        }
        if (lastRowNum == null) {
            lastRowNum = new AtomicInteger();
        }
        ComponentProperties compProperties = form.getProperties();
        // Have to initialize for the messages
        compProperties.getProperties();
        List<Widget> formWidgets = form.getWidgets();
        for (Widget widget : formWidgets) {
            ElementParameter param = new GenericElementParameter(element, compProperties, widget, getComponentService());
            if (parentParam != null) {
                param.setParentParameter(parentParam);
            }
            param.setCategory(category);
            NamedThing[] widgetProperties = widget.getProperties();
            NamedThing widgetProperty = widgetProperties[0];
            param.setName(widgetProperty.getName());
            param.setDisplayName(widgetProperty.getDisplayName());
            if (parentParam != null) {
                // param.setGroup(form.getName());
                // param.setGroupDisplayName(form.getDisplayName());
            }
            param.setShow(parentParam == null ? widget.isVisible() : parentParam.isShow(null) && widget.isVisible());
            int rowNum = 0;
            if (widget.getOrder() != 1) {
                rowNum = lastRowNum.get();
            } else {
                rowNum = widget.getRow();
                if (parentParam != null) {
                    rowNum += parentParam.getNumRow();
                }
                rowNum = rowNum + lastRowNum.get();
            }
            param.setNumRow(rowNum);
            lastRowNum.set(rowNum);
            if (widgetProperty instanceof Form) {
                elementParameters.addAll(getParametersFromForm(element, category, (Form) widgetProperty, param, lastRowNum));
                continue;
            }

            SchemaElement se = null;

            if (widgetProperty instanceof SchemaElement) {
                se = (SchemaElement) widgetProperties[0];
            }

            EParameterFieldType fieldType = null;
            switch (widget.getWidgetType()) {
            case DEFAULT:
                if (se == null) {
                    fieldType = EParameterFieldType.LABEL;
                    param.setValue(widgetProperty.getDisplayName());
                    break;
                }
                switch (se.getType()) {
                case BOOLEAN:
                    fieldType = EParameterFieldType.CHECK;
                    break;
                case BYTE_ARRAY:
                    fieldType = EParameterFieldType.TEXT;
                    break;
                case DATE:
                    fieldType = EParameterFieldType.DATE;
                    break;
                case DATETIME:
                    fieldType = EParameterFieldType.DATE;
                    break;
                case DECIMAL:
                    fieldType = EParameterFieldType.TEXT;
                    break;
                case DOUBLE:
                    fieldType = EParameterFieldType.TEXT;
                    break;
                case DYNAMIC:
                    fieldType = EParameterFieldType.TEXT;
                    break;
                case ENUM:
                    fieldType = EParameterFieldType.CLOSED_LIST;
                    break;
                case FLOAT:
                    fieldType = EParameterFieldType.TEXT;
                    break;
                case INT:
                    fieldType = EParameterFieldType.TEXT;
                    break;
                case SCHEMA:
                    fieldType = EParameterFieldType.SCHEMA_TYPE;
                    break;
                case STRING:
                    fieldType = EParameterFieldType.TEXT;
                    break;
                default:
                    fieldType = EParameterFieldType.TEXT;
                    break;
                }
                break;
            case BUTTON:
                fieldType = EParameterFieldType.BUTTON;
                break;
            case COMPONENT_REFERENCE:
                break;
            case NAME_SELECTION_AREA:
                fieldType = EParameterFieldType.NAME_SELECTION_AREA;
                break;
            case NAME_SELECTION_REFERENCE:
                break;
            case SCHEMA_EDITOR:
                break;
            case SCHEMA_REFERENCE:
                fieldType = EParameterFieldType.SCHEMA_TYPE;
                break;
            default:
                break;
            }
            param.setFieldType(fieldType != null ? fieldType : EParameterFieldType.TEXT);
            // FIXME - Column?
            if (se != null) {
                param.setRequired(se.isRequired());
                param.setValue(compProperties.getValue(se));
                Collection values = se.getPossibleValues();
                if (values != null) {
                    List<String> possVals = new ArrayList<>();
                    for (Object obj : values) {
                        possVals.add(String.valueOf(obj));
                    }
                    String[] valArray = possVals.toArray(new String[0]);
                    param.setListItemsDisplayName(valArray);
                    param.setListItemsDisplayCodeName(valArray);
                    param.setListItemsValue(valArray);
                }
            } else if (widgetProperty instanceof PresentationItem) {
                param.setValue(null);
            }

            // FIXME
            param.setReadOnly(false);
            elementParameters.add(param);
        }
        return elementParameters;
    }
}
