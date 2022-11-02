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
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.process.EParameterFieldType;
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
import org.talend.sdk.component.studio.model.parameter.ValueConverter;

public class TaCoKitContextHandler extends AbstractRepositoryContextHandler {

    public class TaCoKitParamName implements IConnParamName {

        private String name;

        private String value;

        private EParameterFieldType eParameterFieldType;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public TaCoKitParamName(String name, EParameterFieldType eParameterFieldType) {
            this.name = name;
            this.eParameterFieldType = eParameterFieldType;
        }

        public String getName() {
            return this.name;
        }

        public EParameterFieldType getType() {
            return this.eParameterFieldType;
        }
    }

    @Override
    public boolean isRepositoryConType(Connection connection) {
        boolean isTacokit = TaCoKitConfigurationModel.isTacokit(connection);

        return isTacokit;
    }

    public Set<IConnParamName> collectConParameters(Connection conn) {

        Set<IConnParamName> set = new HashSet<IConnParamName>();
        Set<IConnParamName> tableSet = new HashSet<IConnParamName>();
        TaCoKitConfigurationModel taCoKitConfigurationModel = new TaCoKitConfigurationModel(conn);
        Map<String, String> modelProperties = taCoKitConfigurationModel.getProperties();

        Set<Entry<String, String>> entrySet = modelProperties.entrySet();
        for (Entry<String, String> enty : entrySet) {

            String key = enty.getKey();
            String value = enty.getValue();
            boolean isNotENUMTypeParameter = taCoKitConfigurationModel.isNotENUMTypeParameter(key);
            if (isNotENUMTypeParameter) {
                
                EParameterFieldType eParameterFieldType = taCoKitConfigurationModel.getEParameterFieldType(key);
                
                TaCoKitParamName taCoKitParamName = new TaCoKitParamName(key, eParameterFieldType);
                
                taCoKitParamName.setValue(value);
                
                if (eParameterFieldType == EParameterFieldType.TABLE) {
                  tableSet.add(taCoKitParamName);
                }
                
                set.add(taCoKitParamName);
            }

        }


        for (IConnParamName cp : tableSet) {
            String name = ((TaCoKitParamName) cp).getName();
            String arryValue = modelProperties.get(name);
            List<Map<String, Object>> tableValue = ValueConverter.toTable((String) arryValue);

            for (int i = 0; i < tableValue.size(); i++) {
                Map<String, Object> map = tableValue.get(i);
                Set<Entry<String, Object>> entrySetTable = map.entrySet();
                //
                for (Entry<String, Object> entryTable : entrySetTable) {
                    String key = entryTable.getKey();
                    String newValue = (String) entryTable.getValue();
                    String replacePath = key.replace("[]", "[" + i + "]");
                    TaCoKitParamName taCoKitParamName = new TaCoKitParamName(replacePath, null);
                    taCoKitParamName.setValue(newValue);
                    set.add(taCoKitParamName);
                }

            }

        }
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
                if (taCoKitParamName.getType() == null) {
                    continue;// remove "configuration.connectionParametersList[1].parameterValue"
                }
                String name = taCoKitParamName.getName();// configuration.accout
                String substringName = StringUtils.substringAfter(name, ConnectionContextHelper.DOT);// accout
                if (StringUtils.isNoneBlank(substringName)) {

                    paramName = paramPrefix + substringName;
                } else {
                    paramName = paramPrefix + name;
                }
                String value = properties.get(name);
                EParameterFieldType eParameterFieldType = taCoKitConfigurationModel.getEParameterFieldType(name);
                if (eParameterFieldType == EParameterFieldType.TABLE) {
                    String arryValue = properties.get(name);

                    List<Map<String, Object>> tableValue = ValueConverter.toTable((String) arryValue);

                    for (int i = 0; i < tableValue.size(); i++) {
                        Map<String, Object> map = tableValue.get(i);
                        Set<Entry<String, Object>> entrySetTable = map.entrySet();
                        //
                        for (Entry<String, Object> entryTable : entrySetTable) {
                            String key = entryTable.getKey();
                            String keyAfter = StringUtils.substringAfterLast(key, ConnectionContextHelper.DOT);
                            String newValue = (String) entryTable.getValue();
                            String paramArry = StringUtils.substringAfter(name, ConnectionContextHelper.DOT);
                            String newParamName = paramPrefix + paramArry + "_" + i + "_" + keyAfter;
                            Object[] array = varList.stream().filter(v -> {
                                return v.getName().equals(newParamName);
                            }).toArray();
                            if (array == null || array.length == 0) {
                                ConnectionContextHelper.createParameters(varList, newParamName, newValue);
                            }

                        }

                    }
                }

                ConnectionContextHelper.createParameters(varList, paramName, value);

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
        TaCoKitConfigurationModel taCoKitConfigurationModel = new TaCoKitConfigurationModel(connection);
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

                String newScriptCode = ContextParameterUtils.getNewScriptCode(taCokitVariableName, LANGUAGE);

                taCoKitConfigurationModel.setValue(taCoKitParam.getName(), newScriptCode);

                if (taCoKitParam.getType() == EParameterFieldType.TABLE) {
                    String arryValue = taCoKitParam.getValue();
                    List<Map<String, Object>> tableValue = ValueConverter.toTable((String) arryValue);

                    for (int i = 0; i < tableValue.size(); i++) {
                        Map<String, Object> map = tableValue.get(i);
                        Set<Entry<String, Object>> entrySetTable = map.entrySet();
                        for (Entry<String, Object> entryTable : entrySetTable) {
                            String key = entryTable.getKey();

                            String newKey = key.replace("[]", "[" + i + "]");

                            String valueNoConfig = originalVariableName
                                    + StringUtils.substringAfter(newKey, ConnectionContextHelper.DOT);
                            String replaced = valueNoConfig.replace("[", "_");
                            if (StringUtils.isNoneBlank(replaced) && StringUtils.contains(replaced, "].")) {
                                String scriptCode = replaced.replace("].", "_");
                                String newValueScriptCode = ContextParameterUtils.getNewScriptCode(scriptCode, LANGUAGE);
                                taCoKitConfigurationModel.setValue(newKey, newValueScriptCode);

                                EParameterFieldType eParameterFieldType = taCoKitConfigurationModel
                                        .getEParameterFieldType(entryTable.getKey());
                                // avoid context for table parameter name,"CONNECTION TIMEOUT"
                                if (eParameterFieldType == EParameterFieldType.TEXT) {

                                    entryTable.setValue(newValueScriptCode);
                                }
                            }

                        }

                    }
                    if (tableValue != null && tableValue.size() > 0) {
                        String contextString = tableValue.toString();
                        taCoKitConfigurationModel.setValue(taCoKitParam.getName(), contextString);
                    } else {
                        taCoKitConfigurationModel.setValue(taCoKitParam.getName(), "");
                    }

                }
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
            EParameterFieldType eParameterFieldType = taCoKitConfigurationModel.getEParameterFieldType(key);

            if (valueModel != null) {
                if (eParameterFieldType == EParameterFieldType.TABLE) {
                    String tableValue = valueModel.getValue();
                    List<Map<String, Object>> tableValueList = ValueConverter.toTable((String) tableValue);
                    for (int i = 0; i < tableValueList.size(); i++) {
                        Map<String, Object> map = tableValueList.get(i);
                        Set<Entry<String, Object>> entrySetTable = map.entrySet();
                        for (Entry<String, Object> entryTable : entrySetTable) {
                            Object value = entryTable.getValue();
                            if (value instanceof String) {
                                String originalValue = TalendQuoteUtils
                                        .removeQuotes(ContextParameterUtils.getOriginalValue(contextType, value.toString()));
                                entryTable.setValue(originalValue);
                            }

                        }

                    }
                    String tableOriginalValue = tableValueList.toString();
                    taCoKitConfigurationModel.setValue(key, tableOriginalValue);
                    return;
                }
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
