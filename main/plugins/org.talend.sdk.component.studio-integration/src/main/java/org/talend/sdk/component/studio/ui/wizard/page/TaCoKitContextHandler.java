/**
 * Copyright (C) 2006-2022 Talend Inc. - www.talend.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
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
import org.talend.metadata.managment.ui.wizard.context.AbstractRepositoryContextHandler;
import org.talend.sdk.component.studio.metadata.model.TaCoKitConfigurationModel;
import org.talend.sdk.component.studio.metadata.model.TaCoKitConfigurationModel.ValueModel;

public class TaCoKitContextHandler extends AbstractRepositoryContextHandler {

    public class TaCoKitParamName implements IConnParamName {

        private String name;

        public TaCoKitParamName(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    @Override
    public boolean isRepositoryConType(Connection connection) {
        boolean isTacokit = TaCoKitConfigurationModel.isTacokit(connection);

        return isTacokit;
    }

    public Set<IConnParamName> collectConParameters(Connection conn) {

        Set<IConnParamName> set = new HashSet<IConnParamName>();
        TaCoKitConfigurationModel taCoKitConfigurationModel = new TaCoKitConfigurationModel(conn);
        Map<String, String> properties = taCoKitConfigurationModel.getProperties();
        Set<String> keySet = properties.keySet();
        keySet.stream().forEach(k -> {
            boolean isNotENUMTypeParameter = taCoKitConfigurationModel.isNotENUMTypeParameter(k);
            if (isNotENUMTypeParameter) {
                TaCoKitParamName taCoKitParamName = new TaCoKitParamName(k);
                set.add(taCoKitParamName);
            }

        });

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
            if (param instanceof TaCoKitParamName) {
                TaCoKitParamName taCoKitParamName = (TaCoKitParamName) param;
                String name = taCoKitParamName.getName();// configuration.accout
                String substringName = StringUtils.substringAfter(name, ConnectionContextHelper.DOT);// accout
                if (StringUtils.isNoneBlank(substringName)) {

                    paramName = paramPrefix + substringName;
                } else {
                    paramName = paramPrefix + name;
                }

                ConnectionContextHelper.createParameters(varList, paramName, properties.get(name));

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
        String taCokitVariableName = null;
        for (IConnParamName param : paramSet) {
            if (param instanceof TaCoKitParamName) {
                TaCoKitParamName taCoKitParam = (TaCoKitParamName) param;
                originalVariableName = prefixName + ConnectionContextHelper.LINE;
                String name = taCoKitParam.getName();
                String substringName = StringUtils.substringAfter(name, ConnectionContextHelper.DOT);
                if (StringUtils.isNoneBlank(substringName)) {

                    taCokitVariableName = originalVariableName + substringName;
                } else {
                    taCokitVariableName = originalVariableName + name;
                }

                matchContextForAttribues(connection, taCoKitParam, taCokitVariableName);
            }
        }

    }

    @Override
    public void revertPropertiesForContextMode(Connection connection, ContextType contextType) {

        TaCoKitConfigurationModel taCoKitConfigurationModel = new TaCoKitConfigurationModel(connection);
        Map<String, String> properties = taCoKitConfigurationModel.getProperties();
        if (properties != null && properties.size() > 0) {
            Set<String> keySet = properties.keySet();
            keySet.stream().forEach(key -> {
                boolean isNotENUMTypeParameter = taCoKitConfigurationModel.isNotENUMTypeParameter(key);
                if (isNotENUMTypeParameter) {
                    revertProperties(taCoKitConfigurationModel, contextType, key);
                }

            });

        }
    }

    private void revertProperties(TaCoKitConfigurationModel taCoKitConfigurationModel, ContextType contextType, String key) {
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
    protected void matchContextForAttribues(Connection connection, IConnParamName param, String contextVariableName) {
        TaCoKitConfigurationModel taCoKitConfigurationModel = new TaCoKitConfigurationModel(connection);
        if (param instanceof TaCoKitParamName) {
            TaCoKitParamName taCoKitParamName = (TaCoKitParamName) param;
            taCoKitConfigurationModel.setValue(taCoKitParamName.getName(),
                    ContextParameterUtils.getNewScriptCode(contextVariableName, LANGUAGE));
        }

    }

    @Override
    public void setPropertiesForExistContextMode(Connection connection, Set<IConnParamName> paramSet,
            Map<ContextItem, List<ConectionAdaptContextVariableModel>> adaptMap) {
        // nothing to do

    }

    @Override
    public Set<String> getConAdditionPropertiesForContextMode(Connection conn) {
        return new HashSet<String>();
    }

    @Override
    protected void matchAdditionProperties(Connection conn, Map<ContextItem, List<ConectionAdaptContextVariableModel>> adaptMap) {
        // nothing to do

    }


}
