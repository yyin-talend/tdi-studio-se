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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;

import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.components.api.properties.ComponentProperties;
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
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.properties.Item;
import org.talend.core.ui.component.ComponentsFactoryProvider;
import org.talend.daikon.NamedThing;
import org.talend.daikon.properties.Property;
import org.talend.designer.core.generic.constants.IGenericConstants;
import org.talend.designer.core.generic.model.GenericElementParameter;
import org.talend.designer.core.generic.utils.ComponentsUtils;
import org.talend.designer.core.generic.utils.ParameterUtilTool;
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
                String currentComponentName = nodeType.getComponentName();
                String newComponentName = props.getProperty(currentComponentName);
                nodeType.setComponentName(newComponentName);
                IComponent component = ComponentsFactoryProvider.getInstance().get(newComponentName, category.getName());
                ComponentProperties componentProperties = ComponentsUtils.getComponentProperties(newComponentName);
                FakeNode fNode = new FakeNode(component);
                for (IElementParameter elementParameter : fNode.getElementParameters()) {
                    if (elementParameter instanceof GenericElementParameter) {
                        NamedThing currentNamedThing = ComponentsUtils.getGenericSchemaElement(componentProperties,
                                elementParameter.getName());
                        // To update *.properties file
                        // System.out.println(currentComponentName + IGenericConstants.EXP_SEPARATOR +
                        // elementParameter.getName());
                        String oldParameterName = props.getProperty(currentComponentName + IGenericConstants.EXP_SEPARATOR
                                + elementParameter.getName());
                        ElementParameterType paramType = ParameterUtilTool.findParameterType(nodeType, oldParameterName);
                        if (paramType != null) {
                            ((Property) currentNamedThing).setValue(ParameterUtilTool.convertParameterValue(paramType));
                            ParameterUtilTool.removeParameterType(nodeType, paramType);
                            modified = true;
                        }
                    }
                }
                if (modified) {
                    String serializedProperties = componentProperties.toSerialized();
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
        Properties props = new Properties();
        InputStream in = getClass().getResourceAsStream("NewSalesforceMigrationTask.properties");//$NON-NLS-1$
        try {
            props.load(in);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return props;
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
