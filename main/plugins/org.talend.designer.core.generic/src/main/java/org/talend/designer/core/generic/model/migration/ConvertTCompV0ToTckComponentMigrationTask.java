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
package org.talend.designer.core.generic.model.migration;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.components.api.component.Connector;
import org.talend.components.api.properties.ComponentProperties;
import org.talend.core.model.components.ComponentCategory;
import org.talend.core.model.components.ComponentUtilities;
import org.talend.core.model.components.ModifyComponentsAction;
import org.talend.core.model.components.conversions.IComponentConversion;
import org.talend.core.model.components.filters.IComponentFilter;
import org.talend.core.model.components.filters.NameComponentFilter;
import org.talend.core.model.migration.AbstractJobMigrationTask;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.properties.Item;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.daikon.properties.Properties;
import org.talend.daikon.properties.property.Property;
import org.talend.designer.core.generic.utils.ParameterUtilTool;
import org.talend.designer.core.model.utils.emf.talendfile.ConnectionType;
import org.talend.designer.core.model.utils.emf.talendfile.ElementParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.ElementValueType;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;
import org.talend.designer.core.model.utils.emf.talendfile.TalendFileFactory;

public abstract class ConvertTCompV0ToTckComponentMigrationTask extends AbstractJobMigrationTask {

	static class TckMigrationModel {
		String oldComponentName;
		String newComponentName;
		
		String oldPath;
		String newPath;
		String fieldType;
	}
	
    @Override
    public ExecutionResult execute(final Item item) {
        final ProcessType processType = getProcessType(item);
        final ComponentCategory category = ComponentCategory.getComponentCategoryFromItem(item);
        final Map<String, List<TckMigrationModel>> props = getMigrationInfoFromFile();
        final IComponentConversion conversion = new IComponentConversion() {

            @Override
            public void transform(final NodeType nodeType) {
                if (nodeType == null || props == null) {
                    return;
                }
                boolean modified = false;
                
                final String currComponentName = nodeType.getComponentName();
                final List<TckMigrationModel> infos = props.get(currComponentName);
                final String newComponentName = infos.get(0).newComponentName;
                
                //update component node name to new one
                nodeType.setComponentName(newComponentName);
                
                //get the new tck component define model
                //final IComponent component = ComponentsFactoryProvider.getInstance().get(newComponentName, category.getName());
                
                //if use label format in current component instance, and label is a defined as a var like tcompv0 model "__sql__", 
                //the code below for that:
                String label = ParameterUtilTool.getParameterValue(nodeType, "LABEL");
                if (label != null) {
                    ElementParameterType paraType = ParameterUtilTool.findParameterType(nodeType, label.replaceAll("_", ""));
                    if (paraType != null) {
                        ParameterUtilTool.findParameterType(nodeType, "LABEL").setValue(paraType.getValue());
                        modified = true;
                    }
                }
                final ElementParameterType propertyParamType = getParameterType(nodeType, "PROPERTY:PROPERTY_TYPE");
                boolean isRepository = false;
                if (propertyParamType != null && "REPOSITORY".equals(propertyParamType.getValue())) {
                    isRepository = true;
                }
                final ElementParameterType sqlParamType = getParameterType(nodeType, "QUERYSTORE:QUERYSTORE_TYPE");
                boolean isSQLRepository = false;
                if (sqlParamType != null && "REPOSITORY".equals(sqlParamType.getValue())) {
                    isSQLRepository = true;
                }
                
                ElementParameterType tcompV0PropertiesElement = ParameterUtilTool.findParameterType(nodeType, "PROPERTIES");
                //final ComponentProperties compProperties = ComponentsUtils.getComponentProperties(currComponentName);
                final String jsonProperties = tcompV0PropertiesElement.getValue();
                //TODO make sure it go through tcompv0 self migration, but tcompv0 jdbc have two migrations which all no need to be used here, 
                //so may make sure it in future, not now
                final ComponentProperties compProperties = Properties.Helper.fromSerializedPersistent(jsonProperties, ComponentProperties.class).object;
                
                final TalendFileFactory fileFact = TalendFileFactory.eINSTANCE;
                
                for(TckMigrationModel model : infos) {
                	if(model.fieldType == null || model.fieldType.isEmpty()) {
                		//field="TECHNICAL", and oldPath is value directly, so use it directly
                		ElementParameterType ept = ParameterUtilTool.createParameterType("TECHNICAL", model.newPath, model.oldPath);
                		ParameterUtilTool.addParameterType(nodeType, ept);
                	} else {
                		if("COMPONENT_LIST".equals(model.fieldType)) {
                			final String enumName = Enum.class.cast(Property.class.cast(compProperties.getProperty(model.oldPath + ".referenceType")).getStoredValue()).name();
                			ComponentUtilities.addNodeProperty(nodeType, "USE_EXISTING_CONNECTION", "CHECK");
                			ComponentUtilities.addNodeProperty(nodeType, model.newPath, "COMPONENT_LIST");
                			if("THIS_COMPONENT".equals(enumName)) {
                    			ComponentUtilities.setNodeValue(nodeType, "USE_EXISTING_CONNECTION", "false");
                			} else {
                    			ComponentUtilities.setNodeValue(nodeType, "USE_EXISTING_CONNECTION", "true");
                    			final Object value = Property.class.cast(compProperties.getProperty(model.oldPath + ".componentInstanceId")).getStoredValue();
                    			ComponentUtilities.setNodeValue(nodeType, model.newPath, value == null ? null : String.valueOf(value));
                			}
                		} if("TABLE".equals(model.fieldType)) {
                			java.util.List<ElementValueType> elementValues = new ArrayList<ElementValueType>();
                			String[] tableColumnMappings = model.oldPath.split(";");
                			for(String mapping : tableColumnMappings) {
                				String[] oldPathAndNewPath = mapping.split("=");
                				String oldPath = oldPathAndNewPath[0];
                				String newPath = oldPathAndNewPath[1];
                				
                				Property property = Property.class.cast(compProperties.getProperty(oldPath));
                				List value = List.class.cast(property.getStoredValue());//no password, so ok
                				if(value == null) {
                					//any list is null, break directly as tcompv0 table store every column as list, their size should be the same
                					elementValues.clear();
                					break;
                				}
                				for(Object item : value) {
                					ElementValueType elementValue = fileFact.createElementValueType();
                					elementValue.setElementRef(model.newPath + "[]."+ newPath);
                					elementValue.setValue(item == null ? null : String.valueOf(item));
                					elementValues.add(elementValue);
                				}
                			}
                			ComponentUtilities.addNodeProperty(nodeType, model.newPath, "TABLE");
                            ComponentUtilities.setNodeProperty(nodeType, model.newPath, elementValues);
                		} else {
                			Property property = Property.class.cast(compProperties.getProperty(model.oldPath));
                			Object value = property.getStoredValue();//password works?
                			//enum's tostring to call name(), so ok
                			ElementParameterType ept = ParameterUtilTool.createParameterType(model.fieldType, model.newPath, value == null ? null : String.valueOf(value));
            				ParameterUtilTool.addParameterType(nodeType, ept);
                		}
                	}
                	modified = true;
                }
                
                if(modified) {
                	//remove tcompv0 PROPERTIES element
                	ParameterUtilTool.removeParameterType(nodeType, tcompV0PropertiesElement);
                }
                
                String uniqueName = ParameterUtilTool.getParameterValue(nodeType, "UNIQUE_NAME");

                for (Object connectionObj : processType.getConnection()) {
                    ConnectionType connection = (ConnectionType) connectionObj;
                    if (connection.getSource() != null && connection.getSource().equals(uniqueName)) {
                        if (EConnectionType.FLOW_MAIN.getName().equals(connection.getConnectorName())) {
                            connection.setConnectorName(Connector.MAIN_NAME);
                        }
                    }
                }
            }
        };

        if (processType != null) {
            boolean modified = false;
            for (Object obj : processType.getNode()) {
                if (obj != null && obj instanceof NodeType) {
                    String componentName = ((NodeType) obj).getComponentName();
                    if (props.get(componentName) == null) {
                        continue;
                    }
                    IComponentFilter filter = new NameComponentFilter(componentName);
                    modified = ModifyComponentsAction.searchAndModify((NodeType) obj, filter,
                            Arrays.<IComponentConversion> asList(conversion))
                            || modified;
                }
            }
            if (modified) {
                try {
                    ProxyRepositoryFactory.getInstance().save(item, true);
                    return ExecutionResult.SUCCESS_NO_ALERT;
                } catch (PersistenceException e) {
                    ExceptionHandler.process(e);
                    return ExecutionResult.FAILURE;
                }
            }
        }
        return ExecutionResult.NOTHING_TO_DO;
    }

    abstract protected String getMigrationFile();
    
    private Pattern pattern = Pattern.compile("(t\\w++)(\\.([a-zA-Z_.]++))?=([^#]++)(#([a-zA-Z_]++))?");

    private Map<String, List<TckMigrationModel>> getMigrationInfoFromFile() {
    	Map<String, List<TckMigrationModel>> result = new HashMap<>();
    	try(InputStream in = getClass().getResourceAsStream(getMigrationFile());BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF8"))) {
    		List<TckMigrationModel> modelList = new ArrayList<>();
    		
    		String oldComponentName = null;
    		
    		String line = null;
			while((line = br.readLine())!=null) {
				if(line.trim().isEmpty()) {//next component
					result.put(oldComponentName, modelList);
					modelList = new ArrayList<>();
					continue;
				}
				
				Matcher matcher = pattern.matcher(line);
				if(matcher.find()) {
					final TckMigrationModel model = new TckMigrationModel();
					model.newComponentName = matcher.group(1);
					model.newPath = matcher.group(3);
					if(model.newPath == null || model.newPath.isEmpty()) {
						oldComponentName = matcher.group(4);
						model.oldComponentName = oldComponentName;
					} else {
						model.oldPath = matcher.group(4);
						model.fieldType = matcher.group(6);
						
						modelList.add(model);
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
        return result;
    }

    private ElementParameterType getParameterType(NodeType node, String paramName) {
        return ParameterUtilTool.findParameterType(node, paramName);
    }

}
