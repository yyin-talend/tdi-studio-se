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
package org.talend.repository.json.ui.dnd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.EList;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.components.IComponentsService;
import org.talend.core.model.metadata.EMetadataEncoding;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.process.IElement;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.utils.ContextParameterUtils;
import org.talend.core.model.utils.IComponentName;
import org.talend.core.model.utils.IDragAndDropServiceHandler;
import org.talend.core.repository.RepositoryComponentSetting;
import org.talend.core.utils.TalendQuoteUtils;
import org.talend.repository.json.node.JSONRepositoryNodeType;
import org.talend.repository.json.util.EJSONRepositoryToComponent;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.json.JSONFileConnection;
import org.talend.repository.model.json.JSONFileConnectionItem;
import org.talend.repository.model.json.JSONFileNode;
import org.talend.repository.model.json.JSONXPathLoopDescriptor;

/**
 * DOC wanghong class global comment. Detailled comment
 */
public class JSONDragAndDropHandler implements IDragAndDropServiceHandler {

    private static final String JSON = "JSON"; //$NON-NLS-1$

    private static final String MAP = "MAP"; //$NON-NLS-1$

    private static final String INPUT = "tFileInputJSON"; //$NON-NLS-1$

    private static final String OUTPUT = "tExtractJSONFields"; //$NON-NLS-1$

    @Override
    public boolean canHandle(Connection connection) {
        return connection instanceof JSONFileConnection;
    }

    @Override
    public Object getComponentValue(Connection connection, String value, IMetadataTable table) {
        if (value != null && canHandle(connection)) {
            return getJSONRepositoryValue((JSONFileConnection) connection, value, table);
        }
        return null;
    }

    private Object getJSONRepositoryValue(JSONFileConnection connection, String value, IMetadataTable table) {
        boolean isInputModel = connection.isInputModel();
        EList list;
        JSONXPathLoopDescriptor xmlDesc = null;
        if (isInputModel) {
            list = connection.getSchema();
            xmlDesc = (JSONXPathLoopDescriptor) list.get(0);
        }
        if (value.equals("FILE_PATH")) { //$NON-NLS-1$//USE_URL  JSON_MAPPING JSON   URL_PATH VALIDATIONRULES
            if (isContextMode(connection, connection.getJSONFilePath())) {
                return connection.getJSONFilePath();
            } else {
                Path p = new Path(connection.getJSONFilePath());
                return TalendQuoteUtils.addQuotes(p.toPortableString());
            }
        }
        if (value.equals("OUT_FILE_PATH")) {
            if (connection.getOutputFilePath() == null) {
                return "";
            }
            if (isContextMode(connection, connection.getOutputFilePath())) {
                return connection.getOutputFilePath();
            } else {
                Path p = new Path(connection.getOutputFilePath());
                return TalendQuoteUtils.addQuotes(p.toPortableString());
            }
        }
        if (value.equals("LIMIT")) { //$NON-NLS-1$
            if ((xmlDesc == null) || (xmlDesc.getLimitBoucle() == null)) {
                return ""; //$NON-NLS-1$
            } else {
                return xmlDesc.getLimitBoucle().toString();
            }
        }
        if (value.equals("XPATH_QUERY")) { //$NON-NLS-1$
            if (xmlDesc == null) {
                return ""; //$NON-NLS-1$
            } else {
                if (isContextMode(connection, xmlDesc.getAbsoluteXPathQuery())) {
                    return xmlDesc.getAbsoluteXPathQuery();
                } else {
                    return TalendQuoteUtils.addQuotes(xmlDesc.getAbsoluteXPathQuery());
                }
            }
        }
        if (value.equals("ENCODING")) { //$NON-NLS-1$
            if (isContextMode(connection, connection.getEncoding())) {
                return connection.getEncoding();
            } else {
                if (connection.getEncoding() == null) {
                    // get the default encoding
                    return TalendQuoteUtils.addQuotes(EMetadataEncoding.getMetadataEncoding("").getName()); //$NON-NLS-1$
                } else {
                    return TalendQuoteUtils.addQuotes(connection.getEncoding());
                }
            }
        }
        if (value.equals("ROOT")) {
            return getOutputXmlValue(connection.getRoot());
        }
        if (value.equals("GROUP")) {
            return getOutputXmlValue(connection.getGroup());
        }
        if (value.equals("LOOP")) {
            return getOutputXmlValue(connection.getLoop());
        }

        return null;
    }

    private List<Map<String, String>> getOutputXmlValue(EList list) {
        List<Map<String, String>> newList = new ArrayList<Map<String, String>>();
        for (int i = 0; i < list.size(); i++) {
            Map<String, String> map = new HashMap<String, String>();
            JSONFileNode node = (JSONFileNode) list.get(i);
            String defaultValue = node.getDefaultValue();
            if (defaultValue == null) {
                defaultValue = ""; //$NON-NLS-1$
            }
            map.put("VALUE", defaultValue);
            map.put("ORDER", String.valueOf(node.getOrder()));
            map.put("PATH", node.getJSONPath());
            map.put("ATTRIBUTE", node.getAttribute());
            map.put("COLUMN", node.getRelatedColumn());
            newList.add(map);
        }
        return newList;

    }

    @Override
    public List<IComponent> filterNeededComponents(Item item, RepositoryNode seletetedNode, ERepositoryObjectType type) {
        List<IComponent> neededComponents = new ArrayList<IComponent>();
        if (!(item instanceof JSONFileConnectionItem)) {
            return neededComponents;
        }
        IComponentsService service = (IComponentsService) GlobalServiceRegister.getDefault().getService(IComponentsService.class);
        Set<IComponent> components = service.getComponentsFactory().getComponents();
        for (IComponent component : components) {
            if (isValid(item, type, seletetedNode, component, JSON) && !neededComponents.contains(component)) {
                neededComponents.add(component);
            }
        }

        return neededComponents;
    }

    private boolean isValid(Item item, ERepositoryObjectType type, RepositoryNode seletetedNode, IComponent component,
            String repositoryType) {
        if (component == null || repositoryType == null) {
            return false;
        }
        String componentProductname = component.getRepositoryType();
        if (componentProductname != null && repositoryType.contains(componentProductname)
                && isSubValid(item, type, seletetedNode, component, repositoryType)) {
            return true;
        } else if (component.getName() != null && component.getName().contains(repositoryType)) {
            return true;
        }
        return false;
    }

    protected boolean isSubValid(Item item, ERepositoryObjectType type, RepositoryNode seletetedNode, IComponent component,
            String repositoryType) {
        boolean tableWithMap = true;
        if (type == ERepositoryObjectType.METADATA_CON_TABLE) {
            if (component.getName().toUpperCase().endsWith(MAP)) {
                tableWithMap = false;

            }
        }
        return tableWithMap;
    }

    @Override
    public IComponentName getCorrespondingComponentName(Item item, ERepositoryObjectType type) {
        RepositoryComponentSetting setting = null;
        if (item instanceof JSONFileConnectionItem) {
            setting = new RepositoryComponentSetting();
            setting.setName(JSON);
            setting.setRepositoryType(JSON);
            setting.setWithSchema(true);
            setting.setInputComponent(INPUT);
            setting.setOutputComponent(OUTPUT);
            List<Class<Item>> list = new ArrayList<Class<Item>>();
            Class clazz = null;
            try {
                clazz = Class.forName(JSONFileConnectionItem.class.getName());
            } catch (ClassNotFoundException e) {
                ExceptionHandler.process(e);
            }
            list.add(clazz);
            setting.setClasses(list.toArray(new Class[0]));
        }

        return setting;
    }

    private void setJSONRepositoryValue(JSONFileConnection connection, INode node, String repositoryValue) {
    }

    @Override
    public ERepositoryObjectType getType(String repositoryType) {
        if (JSON.equals(repositoryType)) {
            return JSONRepositoryNodeType.JSON;
        }
        return null;
    }

    @Override
    public void handleTableRelevantParameters(IElement ele, IMetadataTable metadataTable) {
        if (ele == null || metadataTable == null) {
            return;
        }
        IElementParameter fileNameParameter = ele.getElementParameter(EJSONRepositoryToComponent.FILENAME.getParameterName());
        if (fileNameParameter != null) {
            String JSONPath = "";
            // metadataTable.getAdditionalProperties().get(JSONConstants.JSON_PATH);
            if (JSONPath != null) {
                fileNameParameter.setValue(TalendQuoteUtils.addQuotesIfNotExist(JSONPath));
            }
        }
    }

    @Override
    public void setComponentValue(Connection connection, INode node, String repositoryValue) {
        if (node != null && canHandle(connection)) {
            setJSONRepositoryValue((JSONFileConnection) connection, node, repositoryValue);
        }
    }

    private boolean isContextMode(Connection connection, String value) {
        if (connection == null || value == null) {
            return false;
        }

        if (connection.isContextMode() && ContextParameterUtils.isContainContextParam(value)) {
            return true;
        }
        return false;
    }
}
