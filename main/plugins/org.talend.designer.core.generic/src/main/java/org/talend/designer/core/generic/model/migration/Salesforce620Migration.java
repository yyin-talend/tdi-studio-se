// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.components.api.properties.ComponentProperties;
import org.talend.components.common.UserPasswordProperties;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.model.components.ComponentUtilities;
import org.talend.core.model.components.ModifyComponentsAction;
import org.talend.core.model.components.conversions.IComponentConversion;
import org.talend.core.model.components.filters.IComponentFilter;
import org.talend.core.model.components.filters.NameComponentFilter;
import org.talend.core.model.migration.AbstractJobMigrationTask;
import org.talend.core.model.properties.Item;
import org.talend.core.model.utils.ContextParameterUtils;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.runtime.util.GenericTypeUtils;
import org.talend.core.utils.TalendQuoteUtils;
import org.talend.daikon.NamedThing;
import org.talend.daikon.properties.Properties;
import org.talend.daikon.properties.property.EnumProperty;
import org.talend.daikon.properties.property.Property;
import org.talend.designer.core.generic.utils.ComponentsUtils;
import org.talend.designer.core.model.utils.emf.talendfile.ElementParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * created by nrousseau on May 25, 2016 Detailled comment
 *
 */
public class Salesforce620Migration extends AbstractJobMigrationTask {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.migration.IMigrationTask#getOrder()
     */
    @Override
    public Date getOrder() {
        GregorianCalendar gc = new GregorianCalendar(2016, 05, 25, 12, 0, 0);
        return gc.getTime();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.migration.AbstractItemMigrationTask#execute(org.talend.core.model.properties.Item)
     */
    @Override
    public ExecutionResult execute(Item item) {
        ProcessType processType = getProcessType(item);
        if (getProject().getLanguage() != ECodeLanguage.JAVA || processType == null) {
            return ExecutionResult.NOTHING_TO_DO;
        }

        String[] componentsName = new String[] {
                "tSalesforceConnection", "tSalesforceBulkExec", "tSalesforceGetDeleted", "tSalesforceGetUpdated", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                "tSalesforceInput", "tSalesforceOutput", "tSalesforceOutputBulk", "tSalesforceOutputBulkExec" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        IComponentConversion changeJDBCDriverJarType = new IComponentConversion() {

            @Override
            public void transform(NodeType node) {
                ElementParameterType elemParamType = ComponentUtilities.getNodeProperty(node, "PROPERTIES");
                String propertiesString = elemParamType.getValue();
                
//				Map propertiesMap = new HashMap();
				// extractProperties(propertiesMap,propertiesString);
				ObjectMapper mapper = new ObjectMapper();
				ComponentProperties newProperties = ComponentsUtils.getComponentProperties(node.getComponentName());
				
				
				try {
					JsonNode propertiesJson = mapper.readTree(propertiesString);
					copyProperties(newProperties, propertiesJson);
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


				System.out.println("###END");
                
                
                NamedThing nt = newProperties.getProperty("module.moduleName");
                if (nt != null && nt instanceof Property) {
                    Property moduleNameProperty = (Property) nt;
                    String moduleName = (String) moduleNameProperty.getValue();
                    if (ContextParameterUtils.isContainContextParam(moduleName)) {
                        moduleName = TalendQuoteUtils.removeQuotes(moduleName);
                    } else {
                        moduleName = TalendQuoteUtils.addPairQuotesIfNotExist(moduleName);
                    }
                    moduleNameProperty.setStoredValue(moduleName);
                }
                nt = newProperties.getProperty("upsertRelationTable.columnName");
                if (nt != null && nt instanceof Property) {
                    Property moduleNameProperty = (Property) nt;
                    if (moduleNameProperty.getPossibleValues() == null || moduleNameProperty.getPossibleValues().isEmpty()) {
                        List<String> columns = new ArrayList<String>();
                        if (moduleNameProperty.getValue() instanceof String) {
                            String column = (String) moduleNameProperty.getValue();
                            columns.add(column);
                        } else if (moduleNameProperty.getValue() instanceof List) {
                            columns.addAll((Collection<? extends String>) moduleNameProperty.getValue());
                        }
                        moduleNameProperty.setPossibleValues(columns);
                    }
                }
                elemParamType.setValue(newProperties.toSerialized());
            }
        };

        boolean modified = false;
        for (Object obj : processType.getNode()) {
            if (obj != null && obj instanceof NodeType) {
                String componentName = ((NodeType) obj).getComponentName();
                if (ArrayUtils.contains(componentsName, componentName)) {
                    IComponentFilter filter = new NameComponentFilter(componentName);
                    modified = ModifyComponentsAction.searchAndModify((NodeType) obj, filter,
                            Arrays.<IComponentConversion> asList(changeJDBCDriverJarType))
                            || modified;
                }
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
        return ExecutionResult.SUCCESS_WITH_ALERT;

    }
    
	private Object extractProperties(String name, JsonNode propertiesJson) {
		if (!propertiesJson.isMissingNode()) {
			if (!propertiesJson.path("type").isMissingNode()) {
				String type = propertiesJson.path("type").path("name").asText();


				JsonNode storedValue = propertiesJson.path("storedValue");
				//
				System.out.println(type+": "+storedValue);
				
				switch (type) {
				case "STRING":
					return storedValue.asText();
				case "BOOLEAN":
					return storedValue.path("value").asBoolean(false);
				case "INT":
					return storedValue.asInt();
				case "SCHEMA":
					// todo
					break;
				default://ENUM type
					return storedValue.asText();
				}
			}
		}

		return null;  
    	
    }
	
	private void deseralized(){
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);  
		mapper.disable(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS);  
		mapper.disable(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE);
		mapper.disable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY);
//		try {
////			Class type = Class.forName(className)
////			ComponentProperties cp = mapper.readValue(propertiesString, newProperties.getClass());
//			
////			System.out.println(cp.getName());
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
	}
    
    private void copyProperties(Properties newProperties,JsonNode json) throws JsonProcessingException, IOException{
		if (newProperties == null || json.isMissingNode()) {
			return;
		}
        List<NamedThing> Propertlist = newProperties.getProperties();
        for (NamedThing property : Propertlist) {
        	String propertyName = property.getName();
        	System.out.println("propertyName: "+propertyName);

        	if (property instanceof Property) {
        		//

                
                JsonNode jsonNode = json.path(propertyName);
                Object storeValue = extractProperties(property.getName(),jsonNode);

                evaluate((Property)property,storeValue);
                	
        	}else if (property instanceof ComponentProperties) {
        		copyProperties((Properties)property,json.path(propertyName));
        		
            }else if (property instanceof UserPasswordProperties){
            	copyProperties((Properties)property,json.path(propertyName));
            }
		}
    	
    }
    
    
    private void evaluate(Property property, Object storedValue) {
    	if(storedValue==null){
    		return ;
    	}
        if (storedValue instanceof String) {
        	String parentName = property.getName();
        	if("securityKey".equals(parentName)||"password".equals(parentName)){
                String encrypt = (String)storedValue;
                if (ContextParameterUtils.isContainContextParam(encrypt)) {
                	encrypt = TalendQuoteUtils.removeQuotes(encrypt);
                } else {
                	encrypt = TalendQuoteUtils.addPairQuotesIfNotExist(encrypt);
                }
                property.setStoredValue(encrypt);
                return;
        		
        	}
            if (GenericTypeUtils.isEnumType(property)) {
                    List<?> propertyPossibleValues = property.getPossibleValues();
                    if (propertyPossibleValues != null) {
                        for (Object possibleValue : propertyPossibleValues) {
                            if (possibleValue.toString().equals(storedValue)) {
                                property.setStoredValue(possibleValue);
                                return ;
                            }
                        }
                    }
            
            }
        }
        System.out.println("Value: "+storedValue);
        property.setStoredValue(storedValue);
    }

    /**
     * DOC nrousseau Comment method "updateSubProperties".
     * 
     * @param properties
     * @param newProperties
     */
    private void updateSubProperties(ComponentProperties properties, ComponentProperties newProperties) {
        if (newProperties == null) {
            return;
        }
        for (NamedThing nt : properties.getProperties()) {
            if (nt instanceof Property) {
                Property property = (Property) nt;
                Object storedValue = property.getStoredValue();
                if (storedValue instanceof String) {
                    String stringValue = (String) storedValue;
                    if (ContextParameterUtils.isContainContextParam(stringValue)) {
                        continue;
                    }
                    Property newProperty = (Property) newProperties.getProperty(property.getName());
                    if (newProperty != null) {
                        if (GenericTypeUtils.isBooleanType(newProperty)) {
                            if (stringValue.isEmpty()) {
                                property.setValue(Boolean.FALSE);
                            } else {
                                property.setValue(new Boolean(stringValue));
                            }
                        } else if (GenericTypeUtils.isEnumType(newProperty) && (!(newProperty instanceof EnumProperty))) {
                            property.setStoredValue(TalendQuoteUtils.removeQuotes(stringValue));
                        } else if (GenericTypeUtils.isEnumType(newProperty)) {
                            List<?> propertyPossibleValues = ((Property<?>) newProperty).getPossibleValues();
                            if (propertyPossibleValues != null) {
                                for (Object possibleValue : propertyPossibleValues) {
                                    if (possibleValue.toString().equals(storedValue)) {
                                        property.setStoredValue(possibleValue);
                                        break;
                                    }
                                }
                            }
                        }

                    }
                }
            } else if (nt instanceof ComponentProperties) {
                updateSubProperties((ComponentProperties) nt, (ComponentProperties) newProperties.getProperty(nt.getName()));
            }
        }
    }

}
