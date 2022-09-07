package org.talend.sdk.component.studio.ui.wizard.page;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.model.utils.ContextParameterUtils;
import org.talend.core.ui.context.model.table.ConectionAdaptContextVariableModel;
import org.talend.core.utils.TalendQuoteUtils;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.metadata.managment.ui.model.IConnParamName;
import org.talend.metadata.managment.ui.utils.ConnectionContextHelper;
import org.talend.metadata.managment.ui.utils.TaCoKitConnectionContextUtils.ETaCoKitParamName;
import org.talend.metadata.managment.ui.wizard.context.AbstractRepositoryContextHandler;
import org.talend.sdk.component.server.front.model.ConfigTypeNode;
import org.talend.sdk.component.studio.metadata.model.TaCoKitConfigurationModel;
import org.talend.sdk.component.studio.metadata.model.TaCoKitConfigurationModel.ValueModel;
import org.talend.sdk.component.studio.model.parameter.ValueConverter;
import org.talend.utils.json.JSONArray;
import org.talend.utils.json.JSONException;
import org.talend.utils.json.JSONObject;

public class TaCoKitKuduContextHandler extends AbstractRepositoryContextHandler {

	private final static String KUDU_HOSTNAME = "configuration.masterAddresses[].hostname"; //$NON-NLS-1$

	private final static String KUDU_PORT = "configuration.masterAddresses[].port"; //$NON-NLS-1$

	@Override
	public boolean isRepositoryConType(Connection connection) {
		String name = null;
		boolean isTacokit = TaCoKitConfigurationModel.isTacokit(connection);
		if (isTacokit) {
			TaCoKitConfigurationModel taCoKitConfigurationModel = new TaCoKitConfigurationModel(connection);
			ConfigTypeNode configTypeNode = taCoKitConfigurationModel.getConfigTypeNode();
			name = configTypeNode.getDisplayName();
		}
		return StringUtils.equals(name, "Apache Kudu");
	}

	public Set<ETaCoKitParamName> collectConParameters() {

		Set<ETaCoKitParamName> set = new HashSet<ETaCoKitParamName>();
		set.add(ETaCoKitParamName.MasterAddresses);
		return set;
	}

	@Override
	public List<IContextParameter> createContextParameters(String prefixName, Connection connection,
			Set<IConnParamName> paramSet) {

		List<IContextParameter> varList = new ArrayList<IContextParameter>();
		TaCoKitConfigurationModel taCoKitConfigurationModel = new TaCoKitConfigurationModel(connection);
		Map<String, String> properties = taCoKitConfigurationModel.getProperties();
		String paramPrefix = prefixName + ConnectionContextHelper.LINE;
		String paramName = null;

		for (IConnParamName param : paramSet) {
			if (param instanceof ETaCoKitParamName) {
				ETaCoKitParamName hadoopParam = (ETaCoKitParamName) param;
				paramName = paramPrefix + hadoopParam;
				switch (hadoopParam) {

				case MasterAddresses:
					ConnectionContextHelper.createParameters(varList, paramName,
							properties.get("configuration.masterAddresses"));
					break;
				default:
				}
			}
		}
//		createKuduPropertiesContextVariable(prefixName, varList, properties.get("configuration.masterAddresses"));

		return varList;

	}

	@Override
	public void setPropertiesForContextMode(String prefixName, Connection connection, Set<IConnParamName> paramSet) {
		if (connection == null) {
			return;
		}

		String originalVariableName = prefixName + ConnectionContextHelper.LINE;
		String hadoopVariableName = null;
		for (IConnParamName param : paramSet) {
			if (param instanceof ETaCoKitParamName) {
				ETaCoKitParamName hadoopConnectionParam = (ETaCoKitParamName) param;
				originalVariableName = prefixName + ConnectionContextHelper.LINE;
				hadoopVariableName = originalVariableName + hadoopConnectionParam;
				matchContextForAttribues(connection, hadoopConnectionParam, hadoopVariableName);
			}
		}

//		TaCoKitConfigurationModel taCoKitConfigurationModel = new TaCoKitConfigurationModel(connection);
//
//		Map<String, String> properties = taCoKitConfigurationModel.getProperties();
//
//		String hadoopProperties = properties.get("configuration.masterAddresses");
//
//		List<Map<String, Object>> hadoopPropertiesList = getKuduPropertiesList(hadoopProperties);
//
//		List<Map<String, Object>> propertiesAfterContext = transformKuduPropertiesForContextMode(hadoopPropertiesList,
//				prefixName);
//
//		String hadoopPropertiesJsonStr = getKuduPropertiesJsonStr(propertiesAfterContext);
//
//		taCoKitConfigurationModel.setValue("configuration.masterAddresses", hadoopPropertiesJsonStr);

	}

	private void createKuduPropertiesContextVariable(String prefixName, List<IContextParameter> varList,
			String hadoopProperties) {
		List<Map<String, Object>> hadoopPropertiesList = getKuduPropertiesList(hadoopProperties);
		for (Map<String, Object> propertyMap : hadoopPropertiesList) {
			String propertyKey = (String) propertyMap.get(KUDU_HOSTNAME);
			String propertyValue = (String) propertyMap.get(KUDU_PORT);
			String keyWithPrefix = prefixName + ConnectionContextHelper.LINE
					+ ContextParameterUtils.getValidParameterName(propertyKey);
			ConnectionContextHelper.createParameters(varList, keyWithPrefix, propertyValue);
		}
	}

	private List<Map<String, Object>> getKuduPropertiesList(String propertiesJsonStr) {
		List<Map<String, Object>> properties = new ArrayList<>();
		try {
			if (StringUtils.isNotEmpty(propertiesJsonStr)) {
				JSONArray jsonArr = new JSONArray(propertiesJsonStr);
				for (int i = 0; i < jsonArr.length(); i++) {
					HashMap<String, Object> map = new HashMap<>();
					JSONObject object = jsonArr.getJSONObject(i);
					Iterator<String> it = object.keys();
					while (it.hasNext()) {
						String key = it.next();
						String value = String.valueOf(object.get(key));
						value = TalendQuoteUtils.removeQuotesIfExist(value);
						map.put(key, value);
					}
					properties.add(map);
				}
			}
		} catch (JSONException e) {
			ExceptionHandler.process(e);
		}

		return properties;
	}

	private List<Map<String, Object>> transformKuduPropertiesForContextMode(
			List<Map<String, Object>> hadoopPropertiesList, String prefixName) {
		for (Map<String, Object> propertyMap : hadoopPropertiesList) {
			String propertyKey = (String) propertyMap.get(KUDU_HOSTNAME);
			propertyMap.put(KUDU_PORT, ContextParameterUtils.getNewScriptCode(prefixName + ConnectionContextHelper.LINE
					+ ContextParameterUtils.getValidParameterName(propertyKey), LANGUAGE));
		}
		return hadoopPropertiesList;
	}

	public static String getKuduPropertiesJsonStr(List<Map<String, Object>> properties) {
		JSONArray jsonArr = null;
		try {
			jsonArr = new JSONArray();
			if (properties != null && properties.size() > 0) {
				for (Map<String, Object> map : properties) {
					JSONObject object = new JSONObject();
					Iterator<String> it = map.keySet().iterator();
					while (it.hasNext()) {
						String key = it.next();
						object.put(key, map.get(key));
					}
					jsonArr.put(object);
				}
			}
		} catch (JSONException e) {
			ExceptionHandler.process(e);
		}

		return jsonArr.toString();
	}

	@Override
	public void setPropertiesForExistContextMode(Connection connection, Set<IConnParamName> paramSet,
			Map<ContextItem, List<ConectionAdaptContextVariableModel>> adaptMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void revertPropertiesForContextMode(Connection connection, ContextType contextType) {

		TaCoKitConfigurationModel taCoKitConfigurationModel = new TaCoKitConfigurationModel(connection);
		revertProperties(taCoKitConfigurationModel, contextType, "configuration.masterAddresses");
	}

	private void revertProperties(TaCoKitConfigurationModel taCoKitConfigurationModel, ContextType contextType,
			String key) {
		try {
			ValueModel valueModel = taCoKitConfigurationModel.getValue(key);
			if (valueModel != null) {// context.aaaa_MasterAddresses
				String contetValue = valueModel.getValue();
				final List<Map<String, Object>> tableValue = ValueConverter.toTable((String) contetValue);
				if (tableValue != null && tableValue.size() > 0) {
					Map<String, Object> map = tableValue.get(0);

					Collection<Object> values = tableValue.get(0).values();
					if (values != null && values.size() > 0) {
						Object value = values.toArray()[0];
						if (value instanceof String) {
							String applicationId = TalendQuoteUtils
									.removeQuotes(ContextParameterUtils.getOriginalValue(contextType, (String) value));
							taCoKitConfigurationModel.setValue(key, applicationId);
						}

					}

				}

			}

		} catch (Exception e) {
			ExceptionHandler.process(e);
		}
	}

	@Override
	public Set<String> getConAdditionPropertiesForContextMode(Connection conn) {
		// TODO Auto-generated method stub
		return new HashSet<String>();
	}

	@Override
	protected void matchContextForAttribues(Connection connection, IConnParamName param, String contextVariableName) {

		TaCoKitConfigurationModel taCoKitConfigurationModel = new TaCoKitConfigurationModel(connection);

		String paramName = null;
		if (param instanceof ETaCoKitParamName) {
			ETaCoKitParamName hadoopParam = (ETaCoKitParamName) param;
			switch (hadoopParam) {
			case MasterAddresses:
				taCoKitConfigurationModel.setValue("configuration.masterAddresses",
						ContextParameterUtils.getNewScriptCode(contextVariableName, LANGUAGE));
				break;
			default:
			}
		}

	}

	@Override
	protected void matchAdditionProperties(Connection conn,
			Map<ContextItem, List<ConectionAdaptContextVariableModel>> adaptMap) {

	}

}
