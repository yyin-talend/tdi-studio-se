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
package org.talend.component.ui.wizard.ui.dnd;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.talend.commons.exception.ExceptionHandler;
import org.talend.component.core.dnd.AbstractComponentDragAndDropHandler;
import org.talend.component.core.utils.ComponentsUtils;
import org.talend.component.ui.model.genericMetadata.GenericConnection;
import org.talend.component.ui.model.genericMetadata.GenericConnectionItem;
import org.talend.components.api.properties.ComponentProperties;
import org.talend.components.api.properties.ComponentProperties.Deserialized;
import org.talend.components.api.schema.SchemaElement;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.components.EComponentType;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.components.IComponentsService;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.process.IElement;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.utils.IComponentName;
import org.talend.core.repository.RepositoryComponentSetting;
import org.talend.designer.core.model.components.AbstractComponent;
import org.talend.repository.model.RepositoryNode;

/**
 * 
 * created by hcyi on Oct 26, 2015 Detailled comment
 *
 */
public class GenericDragAndDropHandler extends AbstractComponentDragAndDropHandler {

    private static final String SALESFORCE = "SALESFORCE"; //$NON-NLS-1$

    public static final String COMPONENT_T_SALSEFORCE_CONNECTION = "tSalesforceConnection"; //$NON-NLS-1$

    public static final String COMPONENT_T_SALSEFORCE_WAVE_BULK_EXEC = "tSalesforceWaveBulkExec"; //$NON-NLS-1$

    public static final String COMPONENT_T_SALSEFORCE_WAVE_OUTPUT_BULK_EXEC = "tSalesforceWaveOutputBulkExec"; //$NON-NLS-1$

    public static final String COMPONENT_T_SALSEFORCE_INPUT = "tSalesforceInput"; //$NON-NLS-1$

    public static final String COMPONENT_T_SALSEFORCE_OUTPUT = "tSalesforceOutput"; //$NON-NLS-1$

    @Override
    public boolean canHandle(Connection connection) {
        return connection instanceof GenericConnection;
    }

    @Override
    public Object getComponentValue(Connection connection, String value, IMetadataTable table, String targetComponent) {
        if (value != null && canHandle(connection)) {
            return getGenericRepositoryValue((GenericConnection) connection, value, table);
        }
        return null;
    }

    private Object getGenericRepositoryValue(GenericConnection connection, String value, IMetadataTable table) {
        if (connection == null) {
            return null;
        }
        String compPropertiesStr = connection.getCompProperties();
        if (compPropertiesStr != null) {
            Deserialized fromSerialized = ComponentProperties.fromSerialized(compPropertiesStr);
            if (fromSerialized != null) {
                ComponentProperties componentProperties = fromSerialized.properties;
                SchemaElement ses = ComponentsUtils.getGenericSchemaElement(componentProperties, value);
                if (ses != null) {
                    return componentProperties.getValue(ses);
                }
            }
        }
        return null;
    }

    @Override
    public List<IComponent> filterNeededComponents(Item item, RepositoryNode seletetedNode, ERepositoryObjectType type) {
        List<IComponent> neededComponents = new ArrayList<IComponent>();
        if (!(item instanceof GenericConnectionItem)) {
            return neededComponents;
        }
        IComponentsService service = (IComponentsService) GlobalServiceRegister.getDefault().getService(IComponentsService.class);
        Set<IComponent> components = service.getComponentsFactory().getComponents();
        for (IComponent component : components) {
            if (EComponentType.GENERIC.equals(component.getComponentType())) {
                if (isValid(item, type, seletetedNode, component, SALESFORCE) && !neededComponents.contains(component)) {
                    neededComponents.add(component);
                }
            }
        }
        return neededComponents;
    }

    private boolean isValid(Item item, ERepositoryObjectType type, RepositoryNode seletetedNode, IComponent component,
            String repositoryType) {
        if (component == null || repositoryType == null) {
            return false;
        }
        String componentProductname = component.getRepositoryType();
        if (componentProductname != null && componentProductname.contains(repositoryType)) {
            String componentName = component.getName();
            if (ERepositoryObjectType.METADATA_SALESFORCE_MODULE == type || ERepositoryObjectType.METADATA_CON_TABLE == type
                    || ERepositoryObjectType.METADATA_CON_COLUMN == type) {
                if (COMPONENT_T_SALSEFORCE_INPUT.equals(componentName) || COMPONENT_T_SALSEFORCE_OUTPUT.equals(componentName)) {
                    return true;
                }
            } else {
                if (COMPONENT_T_SALSEFORCE_CONNECTION.equals(componentName)
                        || COMPONENT_T_SALSEFORCE_WAVE_BULK_EXEC.equals(componentName)
                        || COMPONENT_T_SALSEFORCE_WAVE_OUTPUT_BULK_EXEC.equals(componentName)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public IComponentName getCorrespondingComponentName(Item item, ERepositoryObjectType type) {
        RepositoryComponentSetting setting = null;
        if (item instanceof GenericConnectionItem) {
            setting = new RepositoryComponentSetting();
            setting.setName(SALESFORCE);
            setting.setRepositoryType(SALESFORCE);
            setting.setWithSchema(true);
            setting.setInputComponent(INPUT);
            setting.setOutputComponent(OUTPUT);
            List<Class<Item>> list = new ArrayList<Class<Item>>();
            Class clazz = null;
            try {
                clazz = Class.forName(GenericConnectionItem.class.getName());
            } catch (ClassNotFoundException e) {
                ExceptionHandler.process(e);
            }
            list.add(clazz);
            setting.setClasses(list.toArray(new Class[0]));
        }
        return setting;
    }

    private void setGenericRepositoryValue(GenericConnection connection, INode node, IElementParameter param) {
        if (connection != null) {
            IComponent component = node.getComponent();
            if (component != null && component instanceof AbstractComponent) {
                AbstractComponent comp = (AbstractComponent) component;
                String compProperties = comp.genericToSerialized(param);
                connection.setCompProperties(compProperties);
            }
        }
    }

    @Override
    public ERepositoryObjectType getType(String repositoryType) {
        if (SALESFORCE.equals(repositoryType)) {
            return ERepositoryObjectType.METADATA_SALESFORCE_SCHEMA;
        }
        return null;
    }

    @Override
    public void handleTableRelevantParameters(Connection connection, IElement ele, IMetadataTable metadataTable) {
        // TODO
        if (ele == null || metadataTable == null) {
            return;
        }
    }

    @Override
    public void setComponentValue(Connection connection, INode node, IElementParameter param) {
        if (node != null && canHandle(connection)) {
            setGenericRepositoryValue((GenericConnection) connection, node, param);
        }
    }
}
