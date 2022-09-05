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

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.AbstractRepositoryContextUpdateService;
import org.talend.core.hadoop.repository.HadoopRepositoryUtil;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.sdk.component.server.front.model.ConfigTypeNode;
import org.talend.sdk.component.studio.metadata.model.TaCoKitConfigurationModel;
import org.talend.sdk.component.studio.metadata.model.TaCoKitConfigurationModel.ValueModel;


public class TaCoKitWorkdayContextUpdateService extends AbstractRepositoryContextUpdateService {

    @Override
	public boolean updateContextParameter(Connection conn, String oldValue, String newValue) {
		boolean isModified = false;
		if (conn.isContextMode()) {
			TaCoKitConfigurationModel taCoKitConfigurationModel = new TaCoKitConfigurationModel(conn);
			ValueModel clientIdentifier;
			ValueModel clientSecret;
			ValueModel tenantAlias;
			ValueModel endpoint;
			ValueModel authEndpoint;
			try {
				clientIdentifier = taCoKitConfigurationModel.getValue("configuration.clientId");
				clientSecret = taCoKitConfigurationModel.getValue("configuration.clientSecret");
				tenantAlias = taCoKitConfigurationModel.getValue("configuration.tenantAlias");
				endpoint = taCoKitConfigurationModel.getValue("configuration.endpoint");
				authEndpoint = taCoKitConfigurationModel.getValue("configuration.authEndpoint");

				if (clientIdentifier != null && StringUtils.equals(oldValue, clientIdentifier.getValue())) {
					taCoKitConfigurationModel.setValue("configuration.clientId", clientIdentifier.getValue());
					isModified = true;
				} else if (clientSecret != null && StringUtils.equals(oldValue, clientSecret.getValue())) {
					taCoKitConfigurationModel.setValue("configuration.clientSecret", clientSecret.getValue());
					isModified = true;
				} else if (tenantAlias != null && StringUtils.equals(oldValue, tenantAlias.getValue())) {
					taCoKitConfigurationModel.setValue("configuration.tenantAlias", tenantAlias.getValue());
					isModified = true;
				}  else if (endpoint != null && StringUtils.equals(oldValue, endpoint.getValue())) {
					taCoKitConfigurationModel.setValue("configuration.endpoint", endpoint.getValue());
					isModified = true;
				} else if (authEndpoint != null && StringUtils.equals(oldValue, authEndpoint.getValue())) {
					taCoKitConfigurationModel.setValue("configuration.authEndpoint", authEndpoint.getValue());
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
		if(isTacokit) {
			TaCoKitConfigurationModel taCoKitConfigurationModel = new TaCoKitConfigurationModel(connection);
			ConfigTypeNode configTypeNode = taCoKitConfigurationModel.getConfigTypeNode();
		    name = configTypeNode.getDisplayName();
		}
		return StringUtils.equals(name, "Workday");
    }
}
