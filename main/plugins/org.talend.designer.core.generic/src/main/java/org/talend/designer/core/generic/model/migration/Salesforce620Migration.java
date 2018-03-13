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
import java.util.List;

import org.apache.avro.Schema;
import org.apache.commons.lang.ArrayUtils;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.components.api.properties.ComponentProperties;
import org.talend.components.common.ProxyProperties;
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
import org.talend.daikon.properties.property.Property;
import org.talend.daikon.properties.property.Property.Flags;
import org.talend.designer.core.generic.utils.ComponentsUtils;
import org.talend.designer.core.model.utils.emf.talendfile.ElementParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;

import com.fasterxml.jackson.core.JsonProcessingException;
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
	 * @see
	 * org.talend.core.model.migration.AbstractItemMigrationTask#execute(org.
	 * talend.core.model.properties.Item)
	 */
	@Override
	public ExecutionResult execute(Item item) {
		ProcessType processType = getProcessType(item);
		if (getProject().getLanguage() != ECodeLanguage.JAVA || processType == null) {
			return ExecutionResult.NOTHING_TO_DO;
		}

		String[] componentsName = new String[] { "tSalesforceConnection", "tSalesforceBulkExec", //$NON-NLS-1$ //$NON-NLS-2$
				"tSalesforceGetDeleted", "tSalesforceGetUpdated", //$NON-NLS-1$ //$NON-NLS-2$
				"tSalesforceInput", "tSalesforceOutput", "tSalesforceOutputBulk", "tSalesforceOutputBulkExec" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		IComponentConversion changeJDBCDriverJarType = new IComponentConversion() {

			@Override
			public void transform(NodeType node) {
				ElementParameterType elemParamType = ComponentUtilities.getNodeProperty(node, "PROPERTIES");
				String propertiesString = elemParamType.getValue();

				// Map propertiesMap = new HashMap();
				// extractProperties(propertiesMap,propertiesString);
				ObjectMapper mapper = new ObjectMapper();
				ComponentProperties newProperties = ComponentsUtils.getComponentProperties(node.getComponentName());

				try {
					JsonNode propertiesJson = mapper.readTree(propertiesString);
					copyProperties(newProperties, propertiesJson);
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

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
					if (moduleNameProperty.getPossibleValues() == null
							|| moduleNameProperty.getPossibleValues().isEmpty()) {
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
							Arrays.<IComponentConversion>asList(changeJDBCDriverJarType)) || modified;
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
				if (!storedValue.isMissingNode()) {
					switch (type) {
					case "STRING":
						return storedValue.asText();
					case "BOOLEAN":
						return storedValue.path("value").asBoolean(false);
					case "INT":
						return storedValue.path("value").asInt();
					default:// ENUM type
						return storedValue.asText();
					}
				}
			}
		}

		return null;

	}


	private void extractSchema(Property property, JsonNode json) {
		String schema = TalendQuoteUtils.removeQuotes(json.path("storedValue").toString().replaceAll("\\\\\"", "\""));
		Schema.Parser parser = new Schema.Parser();
		Schema newSchema = parser.parse(schema);
		property.setValue(newSchema);

	}

	private void copyProperties(Properties newProperties, JsonNode json) throws JsonProcessingException, IOException {
		if (newProperties == null || json.isMissingNode()) {
			return;
		}
		List<NamedThing> Propertlist = newProperties.getProperties();
		for (NamedThing property : Propertlist) {
			String propertyName = property.getName();

			if (property instanceof Property) {
				JsonNode jsonNode = json.findPath(propertyName);
				if ("schema".equals(propertyName)) {

					extractSchema((Property) property,jsonNode);
					continue;

				}

				Object storeValue = extractProperties(property.getName(), jsonNode);

				evaluate((Property) property, storeValue);

			} else if (property instanceof ProxyProperties) {
				copyProperties((Properties) property, json.findPath(propertyName));

			} else if (property instanceof UserPasswordProperties) {
				JsonNode UserPassword = json.path(propertyName);
				if (UserPassword.isMissingNode()) {
					copyProperties((Properties) property, json.findPath(propertyName));// scan child nodes
				} else {
					copyProperties((Properties) property, UserPassword);
				}
			} else if (property instanceof Properties) {
				copyProperties((Properties) property, json.path(propertyName));
			}
		}

	}

	private void evaluate(Property property, Object storedValue) {
		if (storedValue == null || "null".equals(storedValue)) {
			return;
		}
		if (storedValue instanceof String) {
			String parentName = property.getName();
			if ("securityKey".equals(parentName) || "password".equals(parentName)) {

				String encrypt = (String) storedValue;
				if (ContextParameterUtils.isContainContextParam(encrypt)) {
					encrypt = TalendQuoteUtils.removeQuotes(encrypt);
				} else {
					encrypt = TalendQuoteUtils.addPairQuotesIfNotExist(encrypt);
				}
				property.setStoredValue(encrypt);
				if (property.isFlag(Flags.ENCRYPT)) {
					property.encryptStoredValue(false);
					property.encryptStoredValue(false);
				} // else not an encrypted property
				return;

			}
			if (GenericTypeUtils.isEnumType(property)) {
				List<?> propertyPossibleValues = property.getPossibleValues();
				if (propertyPossibleValues != null) {
					for (Object possibleValue : propertyPossibleValues) {
						if (possibleValue.toString().equals(storedValue)) {
							property.setStoredValue(possibleValue);
							return;
						}
					}
				}

			}
		}
		property.setStoredValue(storedValue);
	}
}
