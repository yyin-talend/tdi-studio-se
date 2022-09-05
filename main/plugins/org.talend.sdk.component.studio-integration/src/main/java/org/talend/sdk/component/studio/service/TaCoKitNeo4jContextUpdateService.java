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


public class TaCoKitNeo4jContextUpdateService extends AbstractRepositoryContextUpdateService {

    @Override
	public boolean updateContextParameter(Connection conn, String oldValue, String newValue) {
		boolean isModified = false;
		if (conn.isContextMode()) {
			TaCoKitConfigurationModel taCoKitConfigurationModel = new TaCoKitConfigurationModel(conn);
			ValueModel connectionUri;
			ValueModel password;
			ValueModel username;
			try {
				connectionUri = taCoKitConfigurationModel.getValue("configuration.connectionUri");
				password = taCoKitConfigurationModel.getValue("configuration.password");
				username = taCoKitConfigurationModel.getValue("configuration.username");

				if (connectionUri != null && StringUtils.equals(oldValue, connectionUri.getValue())) {
					taCoKitConfigurationModel.setValue("configuration.connectionUri", connectionUri.getValue());
					isModified = true;
				} else if (password != null && StringUtils.equals(oldValue, password.getValue())) {
					taCoKitConfigurationModel.setValue("configuration.password", password.getValue());
					isModified = true;
				} else if (username != null && StringUtils.equals(oldValue, username.getValue())) {
					taCoKitConfigurationModel.setValue("configuration.username", username.getValue());
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
		return StringUtils.equals(name, "Neo4j");
    }
}
