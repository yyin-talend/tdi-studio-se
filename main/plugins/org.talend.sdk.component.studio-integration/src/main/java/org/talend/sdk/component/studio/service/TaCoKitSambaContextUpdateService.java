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

public class TaCoKitSambaContextUpdateService extends AbstractRepositoryContextUpdateService {

	@Override
	public boolean updateContextParameter(Connection conn, String oldValue, String newValue) {
		boolean isModified = false;
		if (conn.isContextMode()) {
			TaCoKitConfigurationModel taCoKitConfigurationModel = new TaCoKitConfigurationModel(conn);
			ValueModel hostValue;
			ValueModel domainValue;
			ValueModel passwordValue;
			ValueModel usernameValue;

			try {
				hostValue = taCoKitConfigurationModel.getValue("configuration.host");
				domainValue = taCoKitConfigurationModel.getValue("configuration.domain");
				passwordValue = taCoKitConfigurationModel.getValue("configuration.password");
				usernameValue = taCoKitConfigurationModel.getValue("configuration.username");

				if (hostValue != null && StringUtils.equals(oldValue, hostValue.getValue())) {
					taCoKitConfigurationModel.setValue("configuration.host", hostValue.getValue());
					isModified = true;
				} else if (domainValue != null && StringUtils.equals(oldValue, domainValue.getValue())) {
					taCoKitConfigurationModel.setValue("configuration.domain", domainValue.getValue());
					isModified = true;
				} else if (passwordValue != null && StringUtils.equals(oldValue, passwordValue.getValue())) {
					taCoKitConfigurationModel.setValue("configuration.password", passwordValue.getValue());
					isModified = true;
				} else if (usernameValue != null && StringUtils.equals(oldValue, usernameValue.getValue())) {
					taCoKitConfigurationModel.setValue("configuration.username", usernameValue.getValue());
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
		return StringUtils.equals(name, "Samba");
	}
}
