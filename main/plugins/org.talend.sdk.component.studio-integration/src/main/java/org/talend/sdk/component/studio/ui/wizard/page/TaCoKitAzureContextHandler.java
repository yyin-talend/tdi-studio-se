package org.talend.sdk.component.studio.ui.wizard.page;

import java.util.ArrayList;
import java.util.HashSet;
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

public class TaCoKitAzureContextHandler extends AbstractRepositoryContextHandler {

	@Override
	public boolean isRepositoryConType(Connection connection) {
		String name = null;
		boolean isTacokit = TaCoKitConfigurationModel.isTacokit(connection);
		if (isTacokit) {
			TaCoKitConfigurationModel taCoKitConfigurationModel = new TaCoKitConfigurationModel(connection);
			ConfigTypeNode configTypeNode = taCoKitConfigurationModel.getConfigTypeNode();
			name = configTypeNode.getDisplayName();
		}
		return StringUtils.equals(name, "Azure");
	}

	@Override
	public Set<ETaCoKitParamName> collectConParameters() {

		Set<ETaCoKitParamName> set = new HashSet<ETaCoKitParamName>();
		set.add(ETaCoKitParamName.EndpointSuffix);
		set.add(ETaCoKitParamName.SharedKey);
		set.add(ETaCoKitParamName.AccountName);
		set.add(ETaCoKitParamName.Sas);
		set.add(ETaCoKitParamName.TenantId);
		set.add(ETaCoKitParamName.ClientSecret);
		set.add(ETaCoKitParamName.ClientId);

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

				case EndpointSuffix:
					ConnectionContextHelper.createParameters(varList, paramName,
							properties.get("configuration.endpointSuffix"));
					break;
				case SharedKey:
					ConnectionContextHelper.createParameters(varList, paramName,
							properties.get("configuration.sharedKey"));
					break;
				case AccountName:
					ConnectionContextHelper.createParameters(varList, paramName,
							properties.get("configuration.accountName"));
					break;
				case Sas:
					ConnectionContextHelper.createParameters(varList, paramName, properties.get("configuration.sas"));
					break;
				case TenantId:
					ConnectionContextHelper.createParameters(varList, paramName,
							properties.get("configuration.tenantId"));
					break;
				case ClientSecret:
					ConnectionContextHelper.createParameters(varList, paramName,
							properties.get("configuration.clientSecret"));
					break;
				case ClientId:
					ConnectionContextHelper.createParameters(varList, paramName,
							properties.get("configuration.clientId"));
					break;
				default:
				}
			}
		}

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

	}

	@Override
	public void setPropertiesForExistContextMode(Connection connection, Set<IConnParamName> paramSet,
			Map<ContextItem, List<ConectionAdaptContextVariableModel>> adaptMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void revertPropertiesForContextMode(Connection connection, ContextType contextType) {

		TaCoKitConfigurationModel taCoKitConfigurationModel = new TaCoKitConfigurationModel(connection);
		revertProperties(taCoKitConfigurationModel, contextType, "configuration.endpointSuffix");
		revertProperties(taCoKitConfigurationModel, contextType, "configuration.sharedKey");
		revertProperties(taCoKitConfigurationModel, contextType, "configuration.accountName");
		revertProperties(taCoKitConfigurationModel, contextType, "configuration.sas");
		revertProperties(taCoKitConfigurationModel, contextType, "configuration.tenantId");
		revertProperties(taCoKitConfigurationModel, contextType, "configuration.clientSecret");
		revertProperties(taCoKitConfigurationModel, contextType, "configuration.clientId");
	}

	private void revertProperties(TaCoKitConfigurationModel taCoKitConfigurationModel, ContextType contextType,
			String key) {
		try {
			ValueModel valueModel = taCoKitConfigurationModel.getValue(key);
			if (valueModel != null) {
				String applicationId = TalendQuoteUtils
						.removeQuotes(ContextParameterUtils.getOriginalValue(contextType, valueModel.getValue()));
				taCoKitConfigurationModel.setValue(key, applicationId);

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
		// TODO Auto-generated method stub
		TaCoKitConfigurationModel taCoKitConfigurationModel = new TaCoKitConfigurationModel(connection);
		Map<String, String> properties = taCoKitConfigurationModel.getProperties();
		String paramName = null;
		if (param instanceof ETaCoKitParamName) {
			ETaCoKitParamName hadoopParam = (ETaCoKitParamName) param;
			switch (hadoopParam) {
			case EndpointSuffix:
				taCoKitConfigurationModel.setValue("configuration.endpointSuffix",
						ContextParameterUtils.getNewScriptCode(contextVariableName, LANGUAGE));
				break;
			case SharedKey:
				taCoKitConfigurationModel.setValue("configuration.sharedKey",
						ContextParameterUtils.getNewScriptCode(contextVariableName, LANGUAGE));
				break;
			case AccountName:
				taCoKitConfigurationModel.setValue("configuration.accountName",
						ContextParameterUtils.getNewScriptCode(contextVariableName, LANGUAGE));
				break;
			case Sas:
				taCoKitConfigurationModel.setValue("configuration.sas",
						ContextParameterUtils.getNewScriptCode(contextVariableName, LANGUAGE));
				break;
			case TenantId:
				taCoKitConfigurationModel.setValue("configuration.tenantId",
						ContextParameterUtils.getNewScriptCode(contextVariableName, LANGUAGE));
				break;
			case ClientSecret:
				taCoKitConfigurationModel.setValue("configuration.clientSecret",
						ContextParameterUtils.getNewScriptCode(contextVariableName, LANGUAGE));
				break;
			case ClientId:
				taCoKitConfigurationModel.setValue("configuration.clientId",
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
