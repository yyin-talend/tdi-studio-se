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
			ValueModel accountValue;
			ValueModel emailValue;
			ValueModel passwordValue;
			ValueModel roleValue;
			ValueModel applicationIdValue;
			try {
				accountValue = taCoKitConfigurationModel.getValue("configuration.account");
				emailValue = taCoKitConfigurationModel.getValue("configuration.email");
				passwordValue = taCoKitConfigurationModel.getValue("configuration.password");
				roleValue = taCoKitConfigurationModel.getValue("configuration.role");
				applicationIdValue = taCoKitConfigurationModel.getValue("configuration.applicationId");

				if (accountValue != null && StringUtils.equals(oldValue, accountValue.getValue())) {
					taCoKitConfigurationModel.setValue("configuration.account", accountValue.getValue());
					isModified = true;
				} else if (emailValue != null && StringUtils.equals(oldValue, emailValue.getValue())) {
					taCoKitConfigurationModel.setValue("configuration.email", emailValue.getValue());
					isModified = true;
				} else if (passwordValue != null && StringUtils.equals(oldValue, passwordValue.getValue())) {
					taCoKitConfigurationModel.setValue("configuration.password", passwordValue.getValue());
					isModified = true;
				} else if (roleValue != null && StringUtils.equals(oldValue, roleValue.getValue())) {
					taCoKitConfigurationModel.setValue("configuration.role", roleValue.getValue());
					isModified = true;
				} else if (applicationIdValue != null && StringUtils.equals(oldValue, applicationIdValue.getValue())) {
					taCoKitConfigurationModel.setValue("configuration.applicationId", applicationIdValue.getValue());
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
		return StringUtils.equals(name, "NetSuite");
    }
}
