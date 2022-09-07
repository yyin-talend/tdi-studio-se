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

public class TaCoKitTableauContextHandler extends AbstractRepositoryContextHandler {

	@Override
	public boolean isRepositoryConType(Connection connection) {
		String name = null;
		boolean isTacokit = TaCoKitConfigurationModel.isTacokit(connection);
		if (isTacokit) {
			TaCoKitConfigurationModel taCoKitConfigurationModel = new TaCoKitConfigurationModel(connection);
			ConfigTypeNode configTypeNode = taCoKitConfigurationModel.getConfigTypeNode();
			name = configTypeNode.getDisplayName();
		}
		return StringUtils.equals(name, "Tableau datastore");
	}

	public Set<ETaCoKitParamName> collectConParameters() {

		Set<ETaCoKitParamName> set = new HashSet<ETaCoKitParamName>();
		set.add(ETaCoKitParamName.Site);
		set.add(ETaCoKitParamName.ServerUrl);
		set.add(ETaCoKitParamName.BasicAuthUsername);
		set.add(ETaCoKitParamName.BasicAuthPassword);
		set.add(ETaCoKitParamName.AccessTokenAuthTokenSecret);
		set.add(ETaCoKitParamName.AccessTokenAuthTokenName);

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

				case Site:
					ConnectionContextHelper.createParameters(varList, paramName, properties.get("configuration.site"));
					break;
				case ServerUrl:
					ConnectionContextHelper.createParameters(varList, paramName,
							properties.get("configuration.serverUrl"));
					break;
				case BasicAuthUsername:
					ConnectionContextHelper.createParameters(varList, paramName,
							properties.get("configuration.basicAuth.username"));
					break;
				case BasicAuthPassword:
					ConnectionContextHelper.createParameters(varList, paramName,
							properties.get("configuration.basicAuth.password"));
					break;
				case AccessTokenAuthTokenSecret:
					ConnectionContextHelper.createParameters(varList, paramName,
							properties.get("configuration.accessTokenAuth.tokenSecret"));
					break;
				case AccessTokenAuthTokenName:
					ConnectionContextHelper.createParameters(varList, paramName,
							properties.get("configuration.accessTokenAuth.tokenName"));
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
		revertProperties(taCoKitConfigurationModel, contextType, "configuration.site");
		revertProperties(taCoKitConfigurationModel, contextType, "configuration.serverUrl");
		revertProperties(taCoKitConfigurationModel, contextType, "configuration.basicAuth.username");
		revertProperties(taCoKitConfigurationModel, contextType, "configuration.basicAuth.password");
		revertProperties(taCoKitConfigurationModel, contextType, "configuration.accessTokenAuth.tokenSecret");
		revertProperties(taCoKitConfigurationModel, contextType, "configuration.accessTokenAuth.tokenName");
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
			case Site:
				taCoKitConfigurationModel.setValue("configuration.site",
						ContextParameterUtils.getNewScriptCode(contextVariableName, LANGUAGE));
				break;
			case ServerUrl:
				taCoKitConfigurationModel.setValue("configuration.serverUrl",
						ContextParameterUtils.getNewScriptCode(contextVariableName, LANGUAGE));
				break;
			case BasicAuthUsername:
				taCoKitConfigurationModel.setValue("configuration.basicAuth.username",
						ContextParameterUtils.getNewScriptCode(contextVariableName, LANGUAGE));
				break;
			case BasicAuthPassword:
				taCoKitConfigurationModel.setValue("configuration.basicAuth.password",
						ContextParameterUtils.getNewScriptCode(contextVariableName, LANGUAGE));
				break;
			case AccessTokenAuthTokenSecret:
				taCoKitConfigurationModel.setValue("configuration.accessTokenAuth.tokenSecret",
						ContextParameterUtils.getNewScriptCode(contextVariableName, LANGUAGE));
				break;
			case AccessTokenAuthTokenName:
				taCoKitConfigurationModel.setValue("configuration.accessTokenAuth.tokenName",
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
