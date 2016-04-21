// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.core.generic.model.migration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;

import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.components.api.properties.ComponentProperties;
import org.talend.components.api.properties.ComponentReferenceProperties;
import org.talend.core.model.components.ComponentCategory;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.components.ModifyComponentsAction;
import org.talend.core.model.components.conversions.IComponentConversion;
import org.talend.core.model.components.filters.IComponentFilter;
import org.talend.core.model.components.filters.NameComponentFilter;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.MetadataTable;
import org.talend.core.model.migration.AbstractJobMigrationTask;
import org.talend.core.model.process.AbstractNode;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.properties.Item;
import org.talend.core.ui.component.ComponentsFactoryProvider;
import org.talend.daikon.NamedThing;
import org.talend.daikon.properties.Property;
import org.talend.designer.core.generic.constants.IGenericConstants;
import org.talend.designer.core.generic.model.GenericElementParameter;
import org.talend.designer.core.generic.utils.ComponentsUtils;
import org.talend.designer.core.generic.utils.ParameterUtilTool;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.model.utils.emf.talendfile.ElementParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;

/**
 * created by hcyi on Nov 18, 2015 Detailled comment
 *
 */
public abstract class NewComponentFrameworkMigrationTask extends AbstractJobMigrationTask {

    @Override
    public ExecutionResult execute(final Item item) {
        final ProcessType processType = getProcessType(item);
        ComponentCategory category = ComponentCategory.getComponentCategoryFromItem(item);
        Properties props = getPropertiesFromFile();
        IComponentConversion conversion = new IComponentConversion() {

            @Override
            public void transform(NodeType nodeType) {
                if (nodeType == null || props == null) {
                    return;
                }
                boolean modified = false;
                String currComponentName = nodeType.getComponentName();
                String newComponentName = props.getProperty(currComponentName);
                nodeType.setComponentName(newComponentName);
                IComponent component = ComponentsFactoryProvider.getInstance().get(newComponentName, category.getName());
                ComponentProperties compProperties = ComponentsUtils.getComponentProperties(newComponentName);
                FakeNode fNode = new FakeNode(component);
                for (IElementParameter param : fNode.getElementParameters()) {
                    if (param instanceof GenericElementParameter) {
                        NamedThing currNamedThing = ComponentsUtils.getGenericSchemaElement(compProperties, param.getName());
                        // To update *.properties file
                        // System.out.println(currentComponentName + IGenericConstants.EXP_SEPARATOR +
                        // elementParameter.getName());
                        String oldParamName = props.getProperty(currComponentName + IGenericConstants.EXP_SEPARATOR
                                + param.getName());
                        if (oldParamName != null) {
                            oldParamName = oldParamName.trim();
                            ElementParameterType paramType = getParameterType(nodeType, oldParamName);
                            if (paramType != null) {
                            	if(currNamedThing instanceof ComponentReferenceProperties){
                            		ComponentReferenceProperties props = (ComponentReferenceProperties)currNamedThing;
                            		props.referenceType.setValue(ComponentReferenceProperties.ReferenceType.COMPONENT_INSTANCE);
                            		props.componentInstanceId.setValue(ParameterUtilTool.convertParameterValue(paramType));
                            		props.componentInstanceId.setTaggedValue(IGenericConstants.ADD_QUOTES, true);
                            	}else{
                            		((Property) currNamedThing).setValue(ParameterUtilTool.convertParameterValue(paramType));
                            	}
                                ParameterUtilTool.removeParameterType(nodeType, paramType);
                                modified = true;
                            }else{
                            	if(currNamedThing instanceof Property){
                            		if(((Property) currNamedThing).isRequired() && Property.Type.STRING.equals(((Property) currNamedThing).getType())){
                                		((Property) currNamedThing).setValue("\"\"");
                                		modified = true;
                                	}
                            	}
                            	
                            }
                            if (EParameterFieldType.SCHEMA_REFERENCE.equals(param.getFieldType())) {
                                String schemaTypeName = ":" + EParameterName.SCHEMA_TYPE.getName();//$NON-NLS-1$
                                String repSchemaTypeName = ":" + EParameterName.REPOSITORY_SCHEMA_TYPE.getName();//$NON-NLS-1$
                                paramType = getParameterType(nodeType, oldParamName + schemaTypeName);
                                if (paramType != null) {
                                    paramType.setName(param.getName() + schemaTypeName);
                                }
                                paramType = getParameterType(nodeType, oldParamName + repSchemaTypeName);
                                if (paramType != null) {
                                    paramType.setName(param.getName() + repSchemaTypeName);
                                }
                            }
                        }
                    }
                }
                if (modified) {
                    String serializedProperties = compProperties.toSerialized();
                    if (serializedProperties != null) {
                        ElementParameterType pType = ParameterUtilTool.createParameterType(null, "PROPERTIES", //$NON-NLS-1$
                                serializedProperties);
                        nodeType.getElementParameter().add(pType);
                    }
                }
            }
        };

        if (processType != null) {
            for (Object obj : processType.getNode()) {
                if (obj != null && obj instanceof NodeType) {
                    String componentName = ((NodeType) obj).getComponentName();
                    String newComponentName = props.getProperty(componentName);
                    if (newComponentName == null) {
                        continue;
                    }
                    IComponentFilter filter = new NameComponentFilter(componentName);
                    try {
                        ModifyComponentsAction.searchAndModify(item, processType, filter,
                                Arrays.<IComponentConversion> asList(conversion));
                    } catch (PersistenceException e) {
                        ExceptionHandler.process(e);
                        return ExecutionResult.FAILURE;
                    }
                }
            }
        }
        return ExecutionResult.SUCCESS_NO_ALERT;
    }

    protected void migrateComponent(String componentName) {
        // with default implementation
    }

    protected Properties getPropertiesFromFile() {
        // with default implementation
        return new Properties();
    }
    
    protected ElementParameterType getParameterType(NodeType node, String paramName) {
        return ParameterUtilTool.findParameterType(node, paramName);
    }

    @Override
    public Date getOrder() {
        GregorianCalendar gc = new GregorianCalendar(2015, 11, 18, 12, 0, 0);
        return gc.getTime();
    }

    public static class FakeNode extends AbstractNode {

        public FakeNode(IComponent component) {
            super();
            setComponentName(component.getName());
            List<IMetadataTable> metaList = new ArrayList<IMetadataTable>();
            IMetadataTable metaTable = new MetadataTable();
            metaTable.setTableName("TableName_1"); //$NON-NLS-1$
            metaList.add(metaTable);
            setMetadataList(metaList);
            setComponent(component);
            setElementParameters(component.createElementParameters(this));
            setListConnector(component.createConnectors(this));
            setUniqueName("UniqueName_1"); //$NON-NLS-1$
            setHasConditionalOutputs(component.hasConditionalOutputs());
            setIsMultiplyingOutputs(component.isMultiplyingOutputs());
        }
    }
}
