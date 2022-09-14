// ============================================================================
//
// Copyright (C) 2006-2022 Talend Inc. - www.talend.com
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

public class TaCoKitRabbitMQContextUpdateService extends AbstractRepositoryContextUpdateService {

	@Override
	public boolean updateContextParameter(Connection conn, String oldValue, String newValue) {
		boolean isModified = false;
		if (conn.isContextMode()) {
			TaCoKitConfigurationModel taCoKitConfigurationModel = new TaCoKitConfigurationModel(conn);
			ValueModel portValue;
			ValueModel hostnameValue;
			ValueModel passwordValue;
			ValueModel userNameValue;
			try {
				portValue = taCoKitConfigurationModel.getValue("configuration.port");
				hostnameValue = taCoKitConfigurationModel.getValue("configuration.hostname");
				passwordValue = taCoKitConfigurationModel.getValue("configuration.password");
				userNameValue = taCoKitConfigurationModel.getValue("configuration.userName");
				if (portValue != null && StringUtils.equals(oldValue, portValue.getValue())) {
					taCoKitConfigurationModel.setValue("configuration.port", portValue.getValue());
					isModified = true;
				} else if (hostnameValue != null && StringUtils.equals(oldValue, hostnameValue.getValue())) {
					taCoKitConfigurationModel.setValue("configuration.hostname", hostnameValue.getValue());
					isModified = true;
				} else if (passwordValue != null && StringUtils.equals(oldValue, passwordValue.getValue())) {
					taCoKitConfigurationModel.setValue("configuration.password", passwordValue.getValue());
					isModified = true;
				} else if (userNameValue != null && StringUtils.equals(oldValue, userNameValue.getValue())) {
					taCoKitConfigurationModel.setValue("configuration.userName", userNameValue.getValue());
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
		return StringUtils.equals(name, "RabbitMQ");
	}
}
