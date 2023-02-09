/**
 * Copyright (C) 2006-2021 Talend Inc. - www.talend.com
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.talend.sdk.component.studio;

import java.util.List;
import java.util.Optional;

import org.eclipse.jface.resource.ImageDescriptor;
import org.talend.core.model.process.EComponentCategory;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.properties.JobletProcessItem;
import org.talend.core.model.properties.ProcessItem;
import org.talend.designer.core.model.components.ElementParameter;
import org.talend.designer.runprocess.ItemCacheManager;
import org.talend.sdk.component.server.front.model.ComponentDetail;
import org.talend.sdk.component.server.front.model.ComponentIndex;
import org.talend.sdk.component.server.front.model.ConfigTypeNode;
import org.talend.sdk.component.server.front.model.ConfigTypeNodes;
import org.talend.sdk.component.studio.model.parameter.ElementParameterCreator;
import org.talend.sdk.component.studio.util.TaCoKitConst;
import org.talend.sdk.component.studio.util.TaCoKitUtil;

public class VirtualComponentModel extends ComponentModel {

    private VirtualComponentModelType modelType;

    public VirtualComponentModel(ComponentIndex index, ComponentDetail detail, ConfigTypeNodes configTypeNodes,
                                 ImageDescriptor image32, String reportPath, boolean isCatcherAvailable, VirtualComponentModelType modelType) {
        super(index, detail, configTypeNodes, image32, reportPath, isCatcherAvailable);
        this.modelType = modelType;
    }

    @Override
    public String getName() {
        final Optional<String> optionalCreateConnectionActionName = Lookups.taCoKitCache().getActionList(this.index.getId().getFamily()).getItems().stream().filter(e -> TaCoKitConst.CREATE_CONNECTION_ATCION_NAME.equals(e.getType())).map(e -> e.getName()).findFirst();
        String main = optionalCreateConnectionActionName.orElse(index.getId().getFamily());
        if (TaCoKitConst.UNSET_CONNECTION_NAME.equals(main)) {
            main = index.getId().getFamily();
        }
        if (isMadeByTalend()) {
            if (isNetSuiteComponent(index)) {
                return TaCoKitConst.COMPONENT_NAME_PREFIX + TaCoKitUtil.getFullComponentName(getProcessedNetSuiteFamilyName(index), modelType.getDisplayName());
            }
            return TaCoKitConst.COMPONENT_NAME_PREFIX + TaCoKitUtil.getFullComponentName(main, modelType.getDisplayName());
        }
        return TaCoKitUtil.getFullComponentName(main, modelType.getDisplayName());
    }

    @Override
    public String getDisplayName() {
        return getName();
    }

    /**
     * Returns long component name, aka title (e.g. "Salesforce Input"). It is i18n title. In v0 component it is
     * specified by "component.{compName}.title" message key
     *
     * @return long component name, aka title (e.g. "") or translated
     */
    @Override
    public String getLongName() {
        return getName();
    }

    @Override
    public List<? extends IElementParameter> createElementParameters(final INode node) {
        if (isNeedMigration() && node.getProcess() != null) {
            IProcess process = node.getProcess();
            ProcessItem processItem = ItemCacheManager.getProcessItem(process.getId());
            if (processItem != null) {
                manager.checkNodeMigration(processItem, getName(), process.getComponentsType());
            }
            JobletProcessItem jpi = ItemCacheManager.getJobletProcessItem(process.getId());
            if (jpi != null) {
                manager.checkNodeItemMigration(jpi, getName(), process.getComponentsType());
            }
        }
        ConfigTypeNode configTypeNode = Lookups.taCoKitCache().findDatastoreConfigTypeNodeByName(detail.getId().getFamily());
        ElementParameterCreator creator = new ElementParameterCreator(this, detail,
                configTypeNode == null ? null : configTypeNode.getProperties(), node, reportPath, isCatcherAvailable);
        List<IElementParameter> parameters = (List<IElementParameter>) creator.createParameters();
        // connection and close runtime need plugin name to fetch plugin object in runtime
        parameters.add(createPluginNameParameter(node));
        return parameters;
    }

    /**
     * Create {@link TaCoKitConst#TACOKIT_COMPONENT_PLUGIN_NAME} parameter. This parameter is used during code generation to know
     * which component runtime to use
     */
    private ElementParameter createPluginNameParameter(final INode node) {
        final ElementParameter parameter = new ElementParameter(node);
        parameter.setName(TaCoKitConst.TACOKIT_COMPONENT_PLUGIN_NAME);
        parameter.setValue(this.getPluginName());
        parameter.setFieldType(EParameterFieldType.TEXT);
        parameter.setCategory(EComponentCategory.TECHNICAL);
        parameter.setReadOnly(true);
        parameter.setRequired(false);
        parameter.setShow(false);
        return parameter;
    }

    public boolean isShowPropertyParameter() {
        if (modelType == VirtualComponentModelType.CLOSE) {
            return false;
        }
        return true;
    }

    @Override
    public String getTemplateFolder() {
        return "tacokit/jet_stub/generic/" + getForder();
    }

    private String getForder() {
        switch (modelType) {
            case CONNECTION:
                return "connection";
            case CLOSE:
                return "close";
            default:
                return null;
        }
    }

    public VirtualComponentModelType getModelType() {
        return modelType;
    }

    @Override
    public String getTemplateNamePrefix() {
        return getForder();
    }

    public String getComponentName() {
        return getName();
    }

    public String getComponentId() {
        return detail.getId().getId() + this.modelType.getDisplayName();
    }

    public static String getDefaultConnectionName(ComponentIndex index) {

        final Optional<String> optionalCreateConnectionActionName = Lookups.taCoKitCache().getActionList(index.getId().getFamily()).getItems().stream().filter(e -> TaCoKitConst.CREATE_CONNECTION_ATCION_NAME.equals(e.getType())).map(e -> e.getName()).findFirst();
        String main = optionalCreateConnectionActionName.orElse(index.getId().getFamily());
        if (TaCoKitConst.UNSET_CONNECTION_NAME.equals(main)) {
            main = index.getId().getFamily();
        }

        if (TaCoKitUtil.isTaCoKitComponentMadeByTalend(index)) {
            if (isNetSuiteComponent(index)) {
                return TaCoKitConst.COMPONENT_NAME_PREFIX + TaCoKitUtil.getFullComponentName(getProcessedNetSuiteFamilyName(index),
                        VirtualComponentModelType.CONNECTION.getDisplayName());
            }
            return TaCoKitConst.COMPONENT_NAME_PREFIX + TaCoKitUtil.getFullComponentName(main,
                    VirtualComponentModelType.CONNECTION.getDisplayName());
        }
        return TaCoKitUtil.getFullComponentName(main, VirtualComponentModelType.CONNECTION.getDisplayName());
    }

    private static boolean isNetSuiteComponent(ComponentIndex index) {
        if ("NetSuite".equals(index.getId().getFamily())) {
            return true;
        }
        return false;
    }

    private static String getProcessedNetSuiteFamilyName(ComponentIndex index) {
        StringBuffer sb = new StringBuffer();
        sb.append(index.getId().getFamily());
        String name = index.getId().getName();
        if (name.startsWith("V2019")) {
            sb.append("V2019");
        }
        return sb.toString();
    }

    public enum VirtualComponentModelType {
        CONNECTION("Connection"),
        CLOSE("Close");

        String displayName;

        VirtualComponentModelType(String displayName) {
            this.displayName = displayName;
        }

        /**
         * Getter for displayName.
         *
         * @return the displayName
         */
        public String getDisplayName() {
            return this.displayName;
        }

    }
}
