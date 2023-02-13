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
package org.talend.designer.core.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.talend.components.api.properties.ComponentProperties;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.components.IComponentsHandler;
import org.talend.core.model.components.IComponentsService;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.INodeConnector;
import org.talend.core.model.utils.IComponentName;
import org.talend.core.repository.RepositoryComponentSetting;
import org.talend.core.ui.component.ComponentsFactoryProvider;
import org.talend.core.utils.TalendQuoteUtils;
import org.talend.daikon.NamedThing;
import org.talend.daikon.properties.Properties;
import org.talend.daikon.properties.property.Property;
import org.talend.designer.core.IUnifiedComponentService;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.model.components.UnifiedJDBCBean;
import org.talend.designer.core.model.components.UnifiedJDBCConfigurationBean;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.views.properties.ComponentSettingsView;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * created by wchen on Dec 11, 2017 Detailled comment
 *
 */
public class UnifiedComponentUtil {

    private static Logger log = Logger.getLogger(UnifiedComponentUtil.class);

    public static List<String> JDBC_COMPONENT_BLACKLIST = Arrays
            .asList(new String[] { "tJDBCOutputBulk", "tJDBCOutputBulkExec", "tJDBCBulkExec" });

    private static Map<String, UnifiedJDBCBean> additionalJDBCCache = new HashMap<String, UnifiedJDBCBean>();

    public static IComponent getEmfComponent(Node node, IComponent component) {
        if (isDelegateComponent(component)) {
            IElementParameter elementParameter = node.getElementParameter(EParameterName.UNIFIED_COMPONENTS.name());
            if (elementParameter != null && elementParameter.getValue() != null) {
                String emfCompName = String.valueOf(elementParameter.getValue());
                String paletteType = component.getPaletteType();
                IComponentsService compService = GlobalServiceRegister.getDefault().getService(IComponentsService.class);
                IComponent emfComponent = compService.getComponentsFactory().get(emfCompName, paletteType);
                if (emfComponent != null) {
                    return emfComponent;
                } else {
                    log.error("Can't find component " + emfCompName);
                }
            }
        }
        return component;
    }

    public static boolean isDelegateComponent(IComponent component) {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IUnifiedComponentService.class)) {
            IUnifiedComponentService service = GlobalServiceRegister.getDefault().getService(IUnifiedComponentService.class);
            if (service.isDelegateComponent(component)) {
                return true;
            }
        }
        return false;
    }

    public static IComponent getDelegateComponent(IComponent component) {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IUnifiedComponentService.class)) {
            IUnifiedComponentService service = GlobalServiceRegister.getDefault().getService(IUnifiedComponentService.class);
            return service.getDelegateComponent(component);
        }
        return component;
    }

    public static IComponent getDelegateComponent(String componentName, String paletteType) {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IUnifiedComponentService.class)) {
            IUnifiedComponentService service = GlobalServiceRegister.getDefault().getService(IUnifiedComponentService.class);
            return service.getDelegateComponent(componentName, paletteType);
        }
        return null;
    }

    public static void createParameters(INode node, List<IElementParameter> listParams, IComponent delegateComp,
            IComponent emfComp) {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IUnifiedComponentService.class)) {
            IUnifiedComponentService service = GlobalServiceRegister.getDefault().getService(IUnifiedComponentService.class);
            service.createParameters(node, listParams, delegateComp, emfComp);
        }
    }

    public static void switchComponent(INode node, IComponent delegateComponent, String oldEmfComponent,
            List<? extends IElementParameter> oldParms, List<IMetadataTable> oldMetadataTables,
            List<INodeConnector> oldConnectors) {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IUnifiedComponentService.class)) {
            IUnifiedComponentService service = GlobalServiceRegister.getDefault().getService(IUnifiedComponentService.class);
            service.switchComponent(node, delegateComponent, oldEmfComponent, oldParms, oldMetadataTables, oldConnectors);
        }

    }

    public static List<IComponent> filterUnifiedComponent(RepositoryComponentSetting setting, List<IComponent> componentList,
            String dbTypeName) {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IUnifiedComponentService.class)) {
            List<IComponent> filtedList = new ArrayList<IComponent>();
            IUnifiedComponentService service = GlobalServiceRegister.getDefault().getService(IUnifiedComponentService.class);
            IComponentsHandler componentsHandler = ComponentsFactoryProvider.getInstance().getComponentsHandler();
            // filter for additional JDBC
            for (IComponent component : componentList) {
                String databaseName = service.getUnifiedCompDisplayName(service.getDelegateComponent(component),
                        component.getName());
                if (("JDBC".equals(databaseName) || isAdditionalJDBC(databaseName)) && !dbTypeName.equals(databaseName)) {
                    continue;
                }
                //
                if ("NetSuite".equalsIgnoreCase(dbTypeName)) { //$NON-NLS-1$
                    continue;
                }

                if (isAdditionalJDBC(databaseName)) {
                    String compKey = StringUtils.deleteWhitespace(databaseName);
                    boolean unsupport = UnifiedComponentUtil.isUnsupportedComponent(
                            component.getName().replaceFirst(compKey, "JDBC"),
                            UnifiedComponentUtil.getAdditionalJDBC().get(databaseName));
                    if (component.getName().contains(compKey) && unsupport) {
                        // filter real component
                        continue;
                    }
                }

                filtedList.add(component);
            }

            for (IComponent component : componentList) {
                if (componentsHandler != null && componentsHandler.extractComponentsCategory() != null) {
                    if (!component.getPaletteType().equals(componentsHandler.extractComponentsCategory().getName())) {
                        continue;
                    }
                }
                if ("JDBC".equals(dbTypeName) || isAdditionalJDBC(dbTypeName)) {
                    String compDBType = service.getUnifiedCompDisplayName(service.getDelegateComponent(component),
                            component.getName());
                    if (!dbTypeName.equals(compDBType)) {
                        continue;
                    }

                }
                IComponent delegateComponent = service.getDelegateComponent(component);
                if (delegateComponent != null) {
                    if (!filtedList.contains(delegateComponent)) {
                        filtedList.add(delegateComponent);
                    }
                    if (component.getName().equals(setting.getInputComponent())) {
                        setting.setInputComponent(delegateComponent.getName());
                    }
                    if (component.getName().equals(setting.getOutputComponent())) {
                        setting.setOutputComponent(delegateComponent.getName());
                    }
                    if (component.getName().equals(setting.getDefaultComponent())) {
                        setting.setDefaultComponent(delegateComponent.getName());
                    }
                } else {
                    filtedList.add(component);
                }
            }
            return filtedList;
        }
        return componentList;
    }

    public static IComponent getEmfComponent(IComponentName setting, IComponent selectedComponent) {
        if (isDelegateComponent(selectedComponent)) {
            IUnifiedComponentService service = GlobalServiceRegister.getDefault().getService(IUnifiedComponentService.class);
            String paletteType = selectedComponent.getPaletteType();
            String emfCompName = service.getUnifiedComponetName4DndFromRepository(setting, selectedComponent);
            IComponentsService compService = GlobalServiceRegister.getDefault().getService(IComponentsService.class);
            IComponent emfComponent = compService.getComponentsFactory().get(emfCompName, paletteType);
            if (emfComponent != null) {
                return emfComponent;
            } else {
                log.error("Can't find component " + emfCompName);
            }
        }
        return selectedComponent;
    }

    public static String getUnifiedComponentDisplayName(IComponent delegateComponent, String emfComponent) {
        if (isDelegateComponent(delegateComponent)) {
            IUnifiedComponentService service = GlobalServiceRegister.getDefault().getService(IUnifiedComponentService.class);
            return service.getUnifiedCompDisplayName(delegateComponent, emfComponent);
        }
        return delegateComponent.getName();
    }

    public static void refreshComponentViewTitle() {
        if (!PlatformUI.isWorkbenchRunning()) {
            return;
        }
        final IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        if (activeWorkbenchWindow == null) {
            return;
        }
        final IWorkbenchPage activePage = activeWorkbenchWindow.getActivePage();
        if (activePage == null) {
            return;
        }
        ComponentSettingsView viewer = (ComponentSettingsView) activePage.findView(ComponentSettingsView.ID);
        if (viewer != null) {
            viewer.updatePropertiesViewerTitle();
        }
    }

    public static String getComponentDisplayNameForPalette(IComponent delegateComponent, String keyWord) {
        if (isDelegateComponent(delegateComponent)) {
            IUnifiedComponentService service = GlobalServiceRegister.getDefault().getService(IUnifiedComponentService.class);
            return service.getComponentDisplayNameForPalette(delegateComponent, keyWord);
        }
        return delegateComponent.getDisplayName();
    }

    public static IComponent getUnifiedComponentByFilter(IComponent delegateComponent, String filter) {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IUnifiedComponentService.class)) {
            IUnifiedComponentService service = GlobalServiceRegister.getDefault().getService(IUnifiedComponentService.class);
            return service.getUnifiedComponentByFilter(delegateComponent, filter);
        }
        return null;
    }

    public static void initComponentIfJDBC(Node node, IComponent delegateComponent) {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IUnifiedComponentService.class)) {
            IUnifiedComponentService service = GlobalServiceRegister.getDefault().getService(IUnifiedComponentService.class);
            UnifiedJDBCBean bean = service.getInitJDBCComponentProperties(node, delegateComponent);
            if (bean == null) {
                return;
            }
            for (UnifiedJDBCConfigurationBean configBean : bean.getParameterConfigurations()) {
                if (node.getComponent().getName().equals(configBean.getComponentName())) {
                    Map<String, Object> parameterMap = configBean.getParameters();
                    for (String parameName : parameterMap.keySet()) {
                        IElementParameter elementParameter = node.getElementParameter(parameName);
                        if (elementParameter != null) {
                            elementParameter.setValue(parameterMap.get(parameName));
                        }
                    }
                }
            }
            if (node.getElementParameter("connection.jdbcUrl") != null) {
                node.getElementParameter("connection.jdbcUrl").setValue(TalendQuoteUtils.addQuotes(bean.getUrl()));
            }
            if (node.getElementParameter("connection.driverClass") != null) {
                node.getElementParameter("connection.driverClass").setValue(TalendQuoteUtils.addQuotes(bean.getDriverClass()));
            }
            ComponentProperties componentProperties = node.getComponentProperties();
            if(componentProperties != null) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("jdbcUrl", TalendQuoteUtils.addQuotes(bean.getUrl()));
                map.put("driverClass", TalendQuoteUtils.addQuotes(bean.getDriverClass()));
                map.put("drivers", bean.getPaths());
                setCompPropertiesForJDBC(componentProperties, map);
            }
        }
    }

    public static void setCompPropertiesForJDBC(Properties componentProperties, Map<String, Object> map) {
        List<NamedThing> properties = componentProperties.getProperties();
        Properties connection = null;
        for (NamedThing namedThing : properties) {
            if ("connection".equals(namedThing.getName()) && namedThing instanceof Properties) {
                connection = (Properties) namedThing;
            }
        }
        if (connection == null) {
            return;
        }
        for (String key : map.keySet()) {
            NamedThing thing = null;
            if (connection.getProperty(key) != null) {
                thing = connection.getProperty(key);
            } else if ("drivers".equals(key)) {
                thing = connection.getProperties("driverTable").getProperty(key);
            }
            if (thing != null && thing instanceof Property) {
                Property property = (Property) thing;
                property.setValue(map.get(key));
            }
        }
    }

    public static Map<String, UnifiedJDBCBean> getAdditionalJDBC() {
        return additionalJDBCCache;
    }

    public static boolean isAdditionalJDBC(String dbType) {
        if (additionalJDBCCache.get(dbType) != null) {
            return true;
        }
        return false;
    }

    public static boolean isUnsupportedComponent(String compName, UnifiedJDBCBean unifiedJDBCBean) {
        List<String> definitionNames = unifiedJDBCBean.getExcludeDefinitions();
        if (!definitionNames.isEmpty() && definitionNames.contains(compName)) {
            return true;
        }
        return false;
    }

    public static Map<String, UnifiedJDBCBean> loadAdditionalJDBC(InputStream inputStream) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(new InputStreamReader(inputStream));
            for (JsonNode jo : jsonNode) {
                UnifiedJDBCBean bean = new UnifiedJDBCBean();
                bean.setDatabaseId(jo.get("id").asText());
                bean.setDisplayName(jo.get("displayName").asText());
                bean.setDriverClass(jo.get("class").asText());
                bean.setUrl(jo.get("url").asText());
                JsonNode paths = jo.get("paths");
                for (JsonNode path : paths) {
                    JsonNode jo_path = path;
                    bean.getPaths().add(jo_path.get("path").asText());
                }
                // optional setting excludes
                JsonNode excludes = jo.get("excludes");
                if (excludes != null) {
                    for (JsonNode exclude : excludes) {
                        JsonNode component = exclude.get("component");
                        if (component != null) {
                            bean.getExcludeDefinitions().add(component.asText());
                        }
                    }
                }
                // optional setting configuration
                JsonNode configuration = jo.get("configuration");
                if (configuration != null) {
                    for (JsonNode jo_config : configuration) {
                        UnifiedJDBCConfigurationBean configBean = new UnifiedJDBCConfigurationBean();
                        JsonNode component = jo_config.get("component");
                        if (component == null) {
                            continue;
                        }
                        configBean.setComponentName(component.asText());
                        Map<String, Object> parameters = configBean.getParameters();
                        JsonNode jo_parameters = jo_config.get("parameters");
                        if (jo_parameters != null) {
                            for (JsonNode jo_param : jo_parameters) {
                                JsonNode name = jo_param.get("name");
                                JsonNode value = jo_param.get("value");
                                if (name != null && value != null) {
                                    parameters.put(name.asText(), value.asText());
                                }
                            }
                        }
                        bean.getParameterConfigurations().add(configBean);
                    }
                }
                additionalJDBCCache.put(bean.getDisplayName(), bean);
            }
        } catch (Exception e) {
            log.error("failed to parse file to get additional databases : ", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("failed to close JDBC config file", e);
                }
            }
        }
        return additionalJDBCCache;
    }

}
