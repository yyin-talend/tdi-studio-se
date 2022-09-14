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

public class TaCoKitAzureContextUpdateService extends AbstractRepositoryContextUpdateService {

	@Override
	public boolean updateContextParameter(Connection conn, String oldValue, String newValue) {
		boolean isModified = false;
		if (conn.isContextMode()) {
			TaCoKitConfigurationModel taCoKitConfigurationModel = new TaCoKitConfigurationModel(conn);
			ValueModel endpointSuffixValue;
			ValueModel sharedKeyValue;
			ValueModel accountNameValue;
			ValueModel sasValue;
			ValueModel tenantIdValue;
			ValueModel clientSecretValue;
			ValueModel clientIdValue;

			try {
				endpointSuffixValue = taCoKitConfigurationModel.getValue("configuration.endpointSuffix");
				sharedKeyValue = taCoKitConfigurationModel.getValue("configuration.sharedKey");
				accountNameValue = taCoKitConfigurationModel.getValue("configuration.accountName");
				sasValue = taCoKitConfigurationModel.getValue("configuration.sas");
				tenantIdValue = taCoKitConfigurationModel.getValue("configuration.tenantId");
				clientSecretValue = taCoKitConfigurationModel.getValue("configuration.clientSecret");
				clientIdValue = taCoKitConfigurationModel.getValue("configuration.clientId");
				if (endpointSuffixValue != null && StringUtils.equals(oldValue, endpointSuffixValue.getValue())) {
					taCoKitConfigurationModel.setValue("configuration.endpointSuffix", endpointSuffixValue.getValue());
					isModified = true;
				} else if (sharedKeyValue != null && StringUtils.equals(oldValue, sharedKeyValue.getValue())) {
					taCoKitConfigurationModel.setValue("configuration.sharedKey", sharedKeyValue.getValue());
					isModified = true;
				} else if (accountNameValue != null && StringUtils.equals(oldValue, accountNameValue.getValue())) {
					taCoKitConfigurationModel.setValue("configuration.accountName", accountNameValue.getValue());
					isModified = true;
				} else if (sasValue != null && StringUtils.equals(oldValue, sasValue.getValue())) {
					taCoKitConfigurationModel.setValue("configuration.sas", sasValue.getValue());
					isModified = true;
				} else if (tenantIdValue != null && StringUtils.equals(oldValue, tenantIdValue.getValue())) {
					taCoKitConfigurationModel.setValue("configuration.tenantId", tenantIdValue.getValue());
					isModified = true;
				} else if (clientSecretValue != null && StringUtils.equals(oldValue, clientSecretValue.getValue())) {
					taCoKitConfigurationModel.setValue("configuration.clientSecret", clientSecretValue.getValue());
					isModified = true;
				} else if (clientIdValue != null && StringUtils.equals(oldValue, clientIdValue.getValue())) {
					taCoKitConfigurationModel.setValue("configuration.clientId", clientIdValue.getValue());
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
		return StringUtils.equals(name, "Azure Data Lake Storage Gen2");
	}
}
