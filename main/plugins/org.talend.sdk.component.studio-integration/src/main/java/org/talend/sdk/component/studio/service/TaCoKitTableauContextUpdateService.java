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
package org.talend.sdk.component.studio.service;

import org.apache.commons.lang3.StringUtils;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.AbstractRepositoryContextUpdateService;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.sdk.component.server.front.model.ConfigTypeNode;
import org.talend.sdk.component.studio.metadata.model.TaCoKitConfigurationModel;
import org.talend.sdk.component.studio.metadata.model.TaCoKitConfigurationModel.ValueModel;

public class TaCoKitTableauContextUpdateService extends AbstractRepositoryContextUpdateService {

	@Override
	public boolean updateContextParameter(Connection conn, String oldValue, String newValue) {
		boolean isModified = false;
		if (conn.isContextMode()) {
			TaCoKitConfigurationModel taCoKitConfigurationModel = new TaCoKitConfigurationModel(conn);
			ValueModel siteValue;
			ValueModel serverUrlValue;
			ValueModel basicAuthUsernameValue;
			ValueModel basicAuthPasswordValue;
			ValueModel accessTokenAuthTokenSecretValue;
			ValueModel accessTokenAuthTokenNameValue;
			try {
				siteValue = taCoKitConfigurationModel.getValue("configuration.site");
				serverUrlValue = taCoKitConfigurationModel.getValue("configuration.serverUrl");
				basicAuthUsernameValue = taCoKitConfigurationModel.getValue("configuration.basicAuth.username");
				basicAuthPasswordValue = taCoKitConfigurationModel.getValue("configuration.basicAuth.password");
				accessTokenAuthTokenSecretValue = taCoKitConfigurationModel
						.getValue("configuration.accessTokenAuth.tokenSecret");
				accessTokenAuthTokenNameValue = taCoKitConfigurationModel
						.getValue("configuration.accessTokenAuth.tokenName");

				if (siteValue != null && StringUtils.equals(oldValue, siteValue.getValue())) {
					taCoKitConfigurationModel.setValue("configuration.site", siteValue.getValue());
					isModified = true;
				} else if (serverUrlValue != null && StringUtils.equals(oldValue, serverUrlValue.getValue())) {
					taCoKitConfigurationModel.setValue("configuration.serverUrl", serverUrlValue.getValue());
					isModified = true;
				} else if (basicAuthUsernameValue != null
						&& StringUtils.equals(oldValue, basicAuthUsernameValue.getValue())) {
					taCoKitConfigurationModel.setValue("configuration.basicAuth.username",
							basicAuthUsernameValue.getValue());
					isModified = true;
				} else if (basicAuthPasswordValue != null
						&& StringUtils.equals(oldValue, basicAuthPasswordValue.getValue())) {
					taCoKitConfigurationModel.setValue("configuration.basicAuth.password",
							basicAuthPasswordValue.getValue());
					isModified = true;
				} else if (accessTokenAuthTokenSecretValue != null
						&& StringUtils.equals(oldValue, accessTokenAuthTokenSecretValue.getValue())) {
					taCoKitConfigurationModel.setValue("configuration.accessTokenAuth.tokenSecret",
							accessTokenAuthTokenSecretValue.getValue());
					isModified = true;
				} else if (accessTokenAuthTokenNameValue != null
						&& StringUtils.equals(oldValue, accessTokenAuthTokenNameValue.getValue())) {
					taCoKitConfigurationModel.setValue("configuration.accessTokenAuth.tokenName",
							accessTokenAuthTokenNameValue.getValue());
					isModified = true;
				}
			} catch (Exception e) {
				ExceptionHandler.process(e);
			}

		}
		return isModified;
	}

	@Override
	public boolean accept(Connection connection) {
		String name = null;
		boolean isTacokit = TaCoKitConfigurationModel.isTacokit(connection);
		if (isTacokit) {
			TaCoKitConfigurationModel taCoKitConfigurationModel = new TaCoKitConfigurationModel(connection);
			ConfigTypeNode configTypeNode = taCoKitConfigurationModel.getConfigTypeNode();
			name = configTypeNode.getDisplayName();
		}
		return StringUtils.equals(name, "Tableau");
	}
}
