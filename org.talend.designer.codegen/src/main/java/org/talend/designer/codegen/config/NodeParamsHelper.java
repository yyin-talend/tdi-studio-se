// ============================================================================
//
// Copyright (C) 2006-2013 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.codegen.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.ElementParameterParser;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.RulesItem;
import org.talend.core.model.relationship.RelationshipItemBuilder;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.utils.JavaResourcesHelper;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.utils.TalendQuoteUtils;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.designer.runprocess.ItemCacheManager;

/**
 * Use for wrap a {@link INode} to provide faster and convenient methods to get
 * parameters.
 * 
 * @author GaoZone
 * @update 2013-9-12 [TESB-11028]
 */
public class NodeParamsHelper {

    /** The node use to wrap. */
    private final INode node;

    /** The parameter map use to cache params by name. */
    private final HashMap<String, IElementParameter> parameterMap;

    /**
     * Use for method return property(K-V) entries in list/map.
     */
    private class PropEntry implements Map.Entry<String, String> {

        /** The key. */
        private final String key;

        /** The value. */
        private final String value;

        /**
         * Instantiates a new prop entry.
         * 
         * @param key
         *            the key
         * @param value
         *            the value
         */
        public PropEntry(String key, String value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String getKey() {
            return key;
        }

        @Override
        public String getValue() {
            return value;
        }

        @Override
        public String setValue(String value) {
            throw new UnsupportedOperationException();
        }

    }

    /**
     * Instantiates a new node params helper.
     * 
     * @param node
     *            the node
     */
    public NodeParamsHelper(INode node) {
        this.node = node;

        // cache ParameterMap
        parameterMap = new HashMap<String, IElementParameter>();
        for (IElementParameter parameter : node.getElementParametersWithChildrens()) {
            parameterMap.put(parameter.getVariableName(), parameter);
        }
    }

    /**
     * Gets the parameter from cached name map.
     * 
     * @param key
     *            the key
     * @return the parameter
     */
    private IElementParameter getParameter(String key) {
        return parameterMap.get(key);
    }

    /**
     * Gets the boolean parameter by {@code key}. If no {@code key} provided
     * 
     * @param key
     *            the key
     * @return the boolean parameter
     */
    public boolean getBoolParam(String key) {
        return "true".equals(getStringParam(key));
    }

    /**
     * Gets the string parameter by {@code key}.
     * 
     * @param key
     *            the key
     * @return the parameter value in String
     */
    public String getStringParam(String key) {
        // return ElementParameterParser.getValue(node, key);
        try {
            IElementParameter param = getParameter(key);
            return getDisplayValue(param);
        } catch (Throwable e) {
            // ignore and return "";
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Gets string parameter by {@code key}. Especially, if the parameter's
     * controller is not visible, then also return {@code ""}.
     * 
     * @param key
     *            the key
     * @return the parameter value
     */
    public String getVisibleStringParam(String key) {
        String value = getStringParam(key);
        if (!("").equals(value)) {
            IElementParameter param = getParameter(key);
            if (param.isShow(node.getElementParametersWithChildrens())) {
                return value;
            }
        }
        return "";
    }

    /**
     * Gets the object param. </br>//TODO Not used name map yet, and be improve
     * by expose methods from {@link ElementParameterParser}
     * 
     * @param <T>
     *            the generic type
     * @param key
     *            the key
     * @return the object param
     */
    @SuppressWarnings("unchecked")
    public <T> T getObjectParam(String key) {
        return (T) ElementParameterParser.getObjectValue(node, key);
    }


    /**
     * Gets boolean parameter by {@code key}. Especially, if the parameter's
     * controller is not visible, then also return {@code false}.
     * 
     * @param key
     *            the key
     * @return the parameter value
     */
    public boolean getVisibleBoolParam(final String key) {
        if (key == null) {
            return false;
        }
        if (getBoolParam(key)) {
            // value is true, then check visible
            // no need null check, has true value must not null.
            IElementParameter param = getParameter(key);
            return param.isShow(node.getElementParametersWithChildrens());
        }
        return false;
    }

    /**
     * Gets the properties pram.
     * 
     * @param key
     *            the key
     * @return the properties pram
     */
    public List<Entry<String, String>> getPropertiesPram(String key) {
        try {
            List<Map<String, String>> paramValue = getObjectParam(key);
            List<Entry<String, String>> properties = new ArrayList<Entry<String, String>>(paramValue.size());
            for (Map<String, String> map : paramValue) {
                properties.add(new PropEntry(map.get("PROP_NAME"), map.get("PROP_VALUE")));
            }
            return properties;
        } catch (Throwable e) {
            // ignore and return empty list.
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    /**
     * Gets the display value. Copied from
     * 
     * @param param
     *            the param
     * @return the display value
     *         {@code ElementParameterParser.getDisplayValue(final IElementParameter param)}
     */
    @SuppressWarnings("unchecked")
    private static String getDisplayValue(final IElementParameter param) {
        if (param == null) {
            return "";
        }
        Object value = param.getValue();

        if (value instanceof String) {

            if (param.getName().equals("PROCESS_TYPE_VERSION") && value.equals(RelationshipItemBuilder.LATEST_VERSION)) { //$NON-NLS-1$
                String jobId = (String) param.getParentParameter().getChildParameters()
                        .get("PROCESS_TYPE_PROCESS").getValue(); //$NON-NLS-1$
                ProcessItem processItem = ItemCacheManager.getProcessItem(jobId);
                if (processItem == null) {
                    return ""; //$NON-NLS-1$
                }
                return processItem.getProperty().getVersion();
            }
            if (param.getName().equals("PROCESS_TYPE_CONTEXT")) { //$NON-NLS-1$
                String jobId = (String) param.getParentParameter().getChildParameters()
                        .get("PROCESS_TYPE_PROCESS").getValue(); //$NON-NLS-1$
                ProcessItem processItem = ItemCacheManager.getProcessItem(jobId);
                if (processItem == null) {
                    return ""; //$NON-NLS-1$
                }
                // check if the selected context exists, if not, use the default
                // context of the job.
                boolean contextExists = false;
                for (Object object : processItem.getProcess().getContext()) {
                    if (object instanceof ContextType) {
                        if (((ContextType) object).getName() != null && ((ContextType) object).getName().equals(value)) {
                            contextExists = true;
                            continue;
                        }
                    }
                }
                if (!contextExists) {
                    return processItem.getProcess().getDefaultContext();
                }
                return (String) value;
            }

            if (param.getName().equals("SELECTED_JOB_NAME")) {
                String jobId = (String) param.getChildParameters().get("PROCESS_TYPE_PROCESS").getValue(); //$NON-NLS-1$
                ProcessItem processItem = ItemCacheManager.getProcessItem(jobId);
                if (processItem == null) {
                    return ""; //$NON-NLS-1$
                }
                return processItem.getProperty().getLabel();
            }
            // hywang add for 6484
            if ("SELECTED_FILE".equals(param.getRepositoryValue())) { //$NON-NLS-1$
                IElementParameter propertyParam = param.getElement().getElementParameter(
                        "PROPERTY:REPOSITORY_PROPERTY_TYPE"); //$NON-NLS-1$
                if (propertyParam != null && propertyParam.getValue() != null && !propertyParam.getValue().equals("")) { //$NON-NLS-1$
                    try {
                        IRepositoryViewObject object = CoreRuntimePlugin.getInstance().getProxyRepositoryFactory()
                                .getLastVersion((String) propertyParam.getValue());
                        if (object != null) {
                            Item item = object.getProperty().getItem();
                            String extension = null;

                            String rule = ""; //$NON-NLS-1$
                            String processLabelAndVersion = null;
                            if (item instanceof RulesItem) {
                                RulesItem rulesItem = (RulesItem) item;
                                extension = rulesItem.getExtension();
                                if (param.getElement() instanceof INode) {
                                    INode node = (INode) param.getElement();
                                    IProcess process = node.getProcess();
                                    String jobLabel = process.getName();
                                    String jobVersion = process.getVersion();
                                    processLabelAndVersion = JavaResourcesHelper.getJobFolderName(jobLabel, jobVersion);
                                }

                                rule = "rules/final/" + processLabelAndVersion + "/" + rulesItem.getProperty().getLabel() //$NON-NLS-1$ //$NON-NLS-2$
                                        + rulesItem.getProperty().getVersion() + extension;
                            }
                            return TalendQuoteUtils.addQuotes(rule);
                        } else {
                            return param.getValue().toString();
                        }
                    } catch (Exception e) {
                        ExceptionHandler.process(e);
                    }

                }
            }

            return (String) value;
        }
        if (param.getFieldType() == EParameterFieldType.RADIO || param.getFieldType() == EParameterFieldType.CHECK
                || param.getFieldType() == EParameterFieldType.AS400_CHECK) {
            if (value instanceof Boolean) {
                return ((Boolean) param.getValue()).toString();
            } else {
                return Boolean.FALSE.toString();
            }
        }

        if (param.getFieldType() == EParameterFieldType.TABLE) {
            List<Map<String, Object>> tableValues = (List<Map<String, Object>>) param.getValue();
            String[] items = param.getListItemsDisplayCodeName();
            String stringValues = "{"; //$NON-NLS-1$
            for (int i = 0; i < tableValues.size(); i++) {
                Map<String, Object> lineValues = tableValues.get(i);
                stringValues += "["; //$NON-NLS-1$
                for (int j = 0; j < items.length; j++) {

                    Object currentValue = lineValues.get(items[j]);
                    if (currentValue instanceof Integer) {
                        IElementParameter tmpParam = (IElementParameter) param.getListItemsValue()[j];
                        if (tmpParam.getListItemsDisplayName().length != 0) {
                            stringValues += tmpParam.getListItemsDisplayName()[(Integer) currentValue];
                        }
                    } else {
                        stringValues += currentValue;
                    }

                    if (j != (items.length - 1)) {
                        stringValues += ","; //$NON-NLS-1$
                    }
                }
                stringValues += "]"; //$NON-NLS-1$
                if (i != (tableValues.size() - 1)) {
                    stringValues += ","; //$NON-NLS-1$
                }
            }
            stringValues += "}"; //$NON-NLS-1$
            return stringValues;
        }

        return new String(""); //$NON-NLS-1$
    }

}
