// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.core.ui.views.properties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.bidimap.DualHashBidiMap;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CommandStackEvent;
import org.eclipse.gef.commands.CommandStackEventListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.threading.ExecutionLimiter;
import org.talend.commons.utils.time.TimeMeasure;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.builder.ConvertionHelper;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.DelimitedFileConnection;
import org.talend.core.model.metadata.builder.connection.GenericSchemaConnection;
import org.talend.core.model.metadata.builder.connection.LDAPSchemaConnection;
import org.talend.core.model.metadata.builder.connection.PositionalFileConnection;
import org.talend.core.model.metadata.builder.connection.QueriesConnection;
import org.talend.core.model.metadata.builder.connection.Query;
import org.talend.core.model.metadata.builder.connection.RegexpFileConnection;
import org.talend.core.model.metadata.builder.connection.WSDLSchemaConnection;
import org.talend.core.model.metadata.builder.connection.XmlFileConnection;
import org.talend.core.model.metadata.designerproperties.RepositoryToComponentProperty;
import org.talend.core.model.process.EComponentCategory;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.Element;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.core.model.utils.TalendTextUtils;
import org.talend.designer.core.DesignerPlugin;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.model.components.EmfComponent;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.designer.core.ui.AbstractMultiPageTalendEditor;
import org.talend.designer.core.ui.editor.cmd.ChangeMetadataCommand;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.process.Process;
import org.talend.designer.core.ui.editor.properties.DynamicTabbedPropertySection;
import org.talend.designer.core.ui.editor.properties.controllers.AbstractElementPropertySectionController;
import org.talend.designer.core.ui.editor.properties.controllers.GroupController;
import org.talend.designer.core.ui.editor.properties.controllers.generator.IDynamicProperty;
import org.talend.designer.runprocess.ProcessorUtilities;
import org.talend.repository.model.ERepositoryStatus;
import org.talend.repository.model.IProxyRepositoryFactory;

/**
 * yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: DynamicTabbedPropertySection.java 6579 2007-10-26 10:33:01Z ftang $
 * 
 */
public class DynamicComposite extends ScrolledComposite implements IDynamicProperty {

    protected AbstractMultiPageTalendEditor part;

    protected Element elem;

    protected Composite parent;

    protected BidiMap hashCurControls;

    protected String currentComponent;

    protected EComponentCategory section;

    protected int curRowSize;

    protected DynamicPropertyGenerator generator;

    private final Map<String, IMetadataTable> repositoryTableMap;

    private final Map<String, ConnectionItem> repositoryConnectionItemMap;

    private Map<String, IRepositoryObject> processMap;

    private final Map<String, Query> repositoryQueryStoreMap;

    private Map<String, String> tableIdAndDbTypeMap;

    private Map<String, String> tableIdAndDbSchemaMap;

    private boolean forceRedraw;

    private int lastCompositeSize = 0;

    private Process process;

    private boolean propertyResized;

    protected Composite composite;

    // private final String extraPropertyTypeName;
    //
    // private final String extraRepositoryPropertyTypeName;

    private final String updataComponentParamName;

    // /**
    // * ftang Comment method "updateProcessList".
    // */
    // private void updateProcessList_renamed() {
    // List<String> processNameList = new ArrayList<String>();
    // List<String> processValueList = new ArrayList<String>();
    // processMap = new HashMap<String, IRepositoryObject>();
    //
    // IProxyRepositoryFactory factory = DesignerPlugin.getDefault().getProxyRepositoryFactory();
    // try {
    // RootContainer<String, IRepositoryObject> processContainer = factory.getProcess();
    // ContentList<String, IRepositoryObject> processAbsoluteMembers = processContainer.getAbsoluteMembers();
    //
    // String currentProcess = process.getLabel();
    // for (Content<String, IRepositoryObject> object : processAbsoluteMembers.values()) {
    // IRepositoryObject process = object.getContent();
    // if (factory.getStatus(process) != ERepositoryStatus.DELETED && !currentProcess.equals(process.getLabel())) {
    // String path = object.getParent().getPath().toString();
    // String name;
    // if (path.equals("")) { //$NON-NLS-1$
    // name = IPath.SEPARATOR + process.getLabel();
    // } else {
    // name = IPath.SEPARATOR + path + IPath.SEPARATOR + process.getLabel();
    // }
    // processNameList.add(name);
    // processMap.put(name, process);
    // }
    // }
    // } catch (PersistenceException e) {
    // e.printStackTrace();
    // }
    //
    // List<String> tempFolderList = new ArrayList<String>();
    // List<String> tempProcessNameList = new ArrayList<String>();
    // tempProcessNameList.addAll(processNameList);
    //
    // for (String string : tempProcessNameList) {
    // // Put jobs which in a folder into a new list.s
    // if (string.lastIndexOf("/") != 0) {
    // tempFolderList.add(string);
    // processNameList.remove(string);
    // }
    // }
    //
    // sortList(processNameList);
    // sortList(tempFolderList);
    //
    // // Always put the jobs which in a folder on the top of the job list
    // tempFolderList.addAll(processNameList);
    //
    // processNameList = tempFolderList;
    //
    // for (String name : processNameList) {
    // IRepositoryObject process = processMap.get(name);
    // processValueList.add(process.getLabel()); //$NON-NLS-1$ //$NON-NLS-2$
    // }
    // String[] processTableNameList = processNameList.toArray(new String[0]);
    // String[] processTableValueList = processValueList.toArray(new String[0]);
    //
    // for (int i = 0; i < elem.getElementParameters().size(); i++) {
    // IElementParameter param = elem.getElementParameters().get(i);
    // if (param.getName().equals(EParameterName.PROCESS_TYPE_PROCESS.getName())) {
    // param.setListItemsDisplayName(processTableNameList);
    // param.setListItemsValue(processTableValueList);
    // if (elem instanceof Node) {
    // ((Node) elem).checkAndRefreshNode();
    // }
    // }
    // }
    // }

    /**
     * Sort the element order of the inputed list.
     * 
     * @param compareList
     */
    private void sortList(List<String> compareList) {
        Collections.sort(compareList, new Comparator<String>() {

            public int compare(String str1, String str2) {

                // For example: avoid job name "a_b_c" before "a_b" in the job
                // list.
                String newStr1 = str1.replaceAll("_", " ");
                String newStr2 = str2.replaceAll("_", " ");
                return newStr1.compareToIgnoreCase(newStr2);
            }
        });
    }

    /**
     * ftang Comment method "updateContextList".
     */
    private void updateContextList(IElementParameter jobParam) {
        List<String> contextNameList = new ArrayList<String>();
        List<String> contextValueList = new ArrayList<String>();

        IElementParameter jobNameParam = jobParam.getChildParameters().get(EParameterName.PROCESS_TYPE_PROCESS.getName());

        ProcessItem processItem = ProcessorUtilities.getProcessItem((String) jobNameParam.getValue());
        if (processItem != null) {
            for (Object o : processItem.getProcess().getContext()) {
                if (o instanceof ContextType) {
                    ContextType context = (ContextType) o;
                    contextNameList.add(context.getName());
                    contextValueList.add(context.getName());
                }
            }
        }
        jobNameParam.setLinkedRepositoryItem(processItem);

        String[] contextTableNameList = contextNameList.toArray(new String[0]);
        String[] contextTableValueList = contextValueList.toArray(new String[0]);

        IElementParameter contextParam = jobParam.getChildParameters().get(EParameterName.PROCESS_TYPE_CONTEXT.getName());
        contextParam.setListItemsDisplayName(contextTableNameList);
        contextParam.setListItemsValue(contextTableValueList);
        if (!contextValueList.contains(contextParam.getValue())) {
            if (contextTableNameList.length > 0) {
                elem.setPropertyValue(EParameterName.PROCESS_TYPE_CONTEXT.getName(), contextTableValueList[0]);
            }
        } else {
            // force to store the value again to activate the code
            // generation in Node.setPropertyValue
            elem.setPropertyValue(EParameterName.PROCESS_TYPE_CONTEXT.getName(), contextParam.getValue());
        }

        // for (int i = 0; i < elem.getElementParametersWithChildrens().size(); i++) {
        // IElementParameter param = elem.getElementParameters().get(i);
        // if (param.getName().equals(EParameterName.PROCESS_TYPE_CONTEXT.getName())) {
        // param.setListItemsDisplayName(contextTableNameList);
        // param.setListItemsValue(contextTableValueList);
        // if (!contextValueList.contains(param.getValue())) {
        // if (contextTableNameList.length > 0) {
        // elem.setPropertyValue(EParameterName.PROCESS_TYPE_CONTEXT.getName(), contextTableValueList[0]);
        // }
        // } else {
        // // force to store the value again to activate the code
        // // generation in Node.setPropertyValue
        // elem.setPropertyValue(EParameterName.PROCESS_TYPE_CONTEXT.getName(), param.getValue());
        // }
        // }
        // }

    }

    private String getRepositoryAliasName(ConnectionItem connectionItem) {
        ERepositoryObjectType repositoryObjectType = ERepositoryObjectType.getItemType(connectionItem);
        String aliasName = repositoryObjectType.getAlias();
        Connection connection = connectionItem.getConnection();
        if (connection instanceof DatabaseConnection) {
            String currentDbType = (String) RepositoryToComponentProperty.getValue(connection, "TYPE"); //$NON-NLS-1$
            aliasName += " (" + currentDbType + ")"; //$NON-NLS-1$ //$NON-NLS-2$
        }
        return aliasName;
    }

    private final Map<String, List<String>> tablesMap = new HashMap<String, List<String>>();

    private final Map<String, List<String>> queriesMap = new HashMap<String, List<String>>();

    /**
     * ftang Comment method "updateRepositoryList".
     */
    @SuppressWarnings("unchecked")//$NON-NLS-1$
    private void updateRepositoryList() {
        IProxyRepositoryFactory factory = DesignerPlugin.getDefault().getProxyRepositoryFactory();
        tableIdAndDbTypeMap = new HashMap<String, String>();
        tableIdAndDbSchemaMap = new HashMap<String, String>();
        List<ConnectionItem> metadataConnectionsItem = null;
        String[] repositoryTableNameList = new String[] {};
        String[] repositoryTableValueList = new String[] {};
        String[] repositoryConnectionNameList = new String[] {};
        String[] repositoryConnectionValueList = new String[] {};
        String[] repositoryQueryNameList = new String[] {};
        String[] repositoryQueryValueList = new String[] {};
        try {
            metadataConnectionsItem = factory.getMetadataConnectionsItem();
        } catch (PersistenceException e) {
            throw new RuntimeException(e);
        }
        if (metadataConnectionsItem != null) {
            repositoryTableMap.clear();
            repositoryQueryStoreMap.clear();
            repositoryConnectionItemMap.clear();

            tablesMap.clear();
            queriesMap.clear();
            List<String> tableNamesList = new ArrayList<String>();
            List<String> tableValuesList = new ArrayList<String>();
            List<String> queryStoreNameList = new ArrayList<String>();
            List<String> queryStoreValuesList = new ArrayList<String>();
            for (ConnectionItem connectionItem : metadataConnectionsItem) {
                Connection connection = connectionItem.getConnection();
                if (!connection.isReadOnly()) {
                    repositoryConnectionItemMap.put(connectionItem.getProperty().getId() + "", connectionItem); //$NON-NLS-1$
                    for (Object tableObj : connection.getTables()) {
                        org.talend.core.model.metadata.builder.connection.MetadataTable table;

                        table = (org.talend.core.model.metadata.builder.connection.MetadataTable) tableObj;

                        if (factory.getStatus(connectionItem) != ERepositoryStatus.DELETED) {
                            if (!factory.isDeleted(table)) {
                                String name = getRepositoryAliasName(connectionItem) + ":" //$NON-NLS-1$
                                        + connectionItem.getProperty().getLabel() + " - " + table.getLabel(); //$NON-NLS-1$
                                String value = connectionItem.getProperty().getId() + " - " + table.getLabel(); //$NON-NLS-1$
                                IMetadataTable newTable = ConvertionHelper.convert(table);
                                repositoryTableMap.put(value, newTable);
                                if (connection instanceof DatabaseConnection) {
                                    String dbType = ((DatabaseConnection) connection).getDatabaseType();
                                    String schema = ((DatabaseConnection) connection).getSchema();
                                    tableIdAndDbTypeMap.put(newTable.getId(), dbType);
                                    if (schema != null && !schema.equals("")) {
                                        tableIdAndDbSchemaMap.put(newTable.getId(), schema);
                                    }
                                }
                                addOrderDisplayNames(tableValuesList, tableNamesList, value, name);
                                // tableNamesList.add(name);
                                // tableValuesList.add(value);
                            }
                        }
                    }
                }
                tablesMap.put(connectionItem.getProperty().getId(), tableValuesList);
                if (connection instanceof DatabaseConnection && !connection.isReadOnly()) {
                    DatabaseConnection dbConnection = (DatabaseConnection) connection;
                    QueriesConnection queriesConnection = dbConnection.getQueries();
                    if (queriesConnection != null) {
                        List<Query> qs = queriesConnection.getQuery();
                        for (Query query : qs) {
                            String name = getRepositoryAliasName(connectionItem) + ":" //$NON-NLS-1$
                                    + connectionItem.getProperty().getLabel() + " - " + query.getLabel(); //$NON-NLS-1$
                            String value = connectionItem.getProperty().getId() + " - " + query.getLabel(); //$NON-NLS-1$
                            repositoryQueryStoreMap.put(value, query);
                            addOrderDisplayNames(queryStoreValuesList, queryStoreNameList, value, name);
                            // queryStoreNameList.add(name);
                            // queryStoreValuesList.add(value);
                        }
                    }
                }
                queriesMap.put(connectionItem.getProperty().getId(), queryStoreValuesList);
            }
            repositoryTableNameList = tableNamesList.toArray(new String[0]);
            repositoryTableValueList = tableValuesList.toArray(new String[0]);
            repositoryQueryNameList = queryStoreNameList.toArray(new String[0]);
            repositoryQueryValueList = queryStoreValuesList.toArray(new String[0]);
        }
        initMaps();
        for (int i = 0; i < elem.getElementParameters().size(); i++) {
            IElementParameter param = elem.getElementParameters().get(i);
            if (param.getField().equals(EParameterFieldType.SCHEMA_TYPE)) {
                IElementParameter repositoryType = param.getChildParameters()
                        .get(EParameterName.REPOSITORY_SCHEMA_TYPE.getName());
                repositoryType.setListItemsDisplayName(repositoryTableNameList);
                repositoryType.setListItemsValue(repositoryTableValueList);
                if (!repositoryTableMap.keySet().contains(repositoryType.getValue())) {
                    List<String> list2 = tablesMap.get(elem.getPropertyValue(EParameterName.REPOSITORY_PROPERTY_TYPE.getName()));
                    boolean isNeeded = list2 != null && !list2.isEmpty();
                    if (repositoryTableNameList.length > 0 && repositoryConnectionValueList.length > 0 && isNeeded) {
                        repositoryType.setValue(getDefaultRepository(param, true, repositoryConnectionValueList[0]));
                        // elem.setPropertyValue(EParameterName.REPOSITORY_SCHEMA_TYPE.getName(),
                        // getDefaultRepository(
                        // true, repositoryConnectionValueList[0]));
                    }
                }
            }
            if (param.getField().equals(EParameterFieldType.QUERYSTORE_TYPE)) {
                IElementParameter repositoryType = param.getChildParameters().get(
                        EParameterName.REPOSITORY_QUERYSTORE_TYPE.getName());
                repositoryType.setListItemsDisplayName(repositoryQueryNameList);
                repositoryType.setListItemsValue(repositoryQueryValueList);
                if (!repositoryQueryStoreMap.keySet().contains(repositoryType.getValue())) {
                    List<String> list2 = queriesMap.get(elem.getPropertyValue(EParameterName.REPOSITORY_PROPERTY_TYPE.getName()));
                    boolean isNeeded = list2 != null && !list2.isEmpty();
                    if (repositoryQueryNameList.length > 0 && repositoryConnectionValueList.length > 0 && isNeeded) {
                        repositoryType.setValue(getDefaultRepository(elem
                                .getElementParameterFromField(EParameterFieldType.SCHEMA_TYPE), false,
                                repositoryConnectionValueList[0]));
                    }
                }
            }
            if (param.getField().equals(EParameterFieldType.PROPERTY_TYPE)) {
                IElementParameter repositoryType = param.getChildParameters().get(
                        EParameterName.REPOSITORY_PROPERTY_TYPE.getName());
                List<String> nameList = new ArrayList<String>();
                List<String> valueList = new ArrayList<String>();
                updateRepositoryListExtra(repositoryType, nameList, valueList, false);
                repositoryConnectionNameList = nameList.toArray(new String[0]);
                repositoryConnectionValueList = valueList.toArray(new String[0]);
            }
            // // for job settings extra (feature 2710)
            // if (param.getName().equals(extraRepositoryPropertyTypeName)) {
            // List<String> nameList = new ArrayList<String>();
            // List<String> valueList = new ArrayList<String>();
            // updateRepositoryListExtra(param, nameList, valueList, true);
            // repositoryConnectionNameList = nameList.toArray(new String[0]);
            // repositoryConnectionValueList = valueList.toArray(new String[0]);
            // }
        }
        updateQuery();
    }

    /**
     * for job settings extra (feature 2710).
     * 
     */
    private void updateRepositoryListExtra(IElementParameter param, List<String> repositoryConnectionNameList,
            List<String> repositoryConnectionValueList, boolean extra) {

        String repositoryValue = param.getParentParameter().getRepositoryValue();
        if (repositoryValue != null) {
            List<String> connectionNamesList = new ArrayList<String>();
            List<String> connectionValuesList = new ArrayList<String>();
            for (String key : repositoryConnectionItemMap.keySet()) {
                ConnectionItem connectionItem = repositoryConnectionItemMap.get(key);
                Connection connection = connectionItem.getConnection();
                String name = getRepositoryAliasName(connectionItem) + ":" //$NON-NLS-1$
                        + connectionItem.getProperty().getLabel();
                if ((connection instanceof DelimitedFileConnection) && (repositoryValue.equals("DELIMITED"))) { //$NON-NLS-1$
                    addOrderDisplayNames(connectionValuesList, connectionNamesList, key, name);
                }
                if ((connection instanceof PositionalFileConnection) && (repositoryValue.equals("POSITIONAL"))) { //$NON-NLS-1$
                    addOrderDisplayNames(connectionValuesList, connectionNamesList, key, name);
                }
                if ((connection instanceof RegexpFileConnection) && (repositoryValue.equals("REGEX"))) { //$NON-NLS-1$
                    addOrderDisplayNames(connectionValuesList, connectionNamesList, key, name);
                }
                if ((connection instanceof XmlFileConnection) && (repositoryValue.equals("XML"))) { //$NON-NLS-1$
                    addOrderDisplayNames(connectionValuesList, connectionNamesList, key, name);
                }
                if ((connection instanceof GenericSchemaConnection) && (repositoryValue.equals("GENERIC"))) { //$NON-NLS-1$
                    addOrderDisplayNames(connectionValuesList, connectionNamesList, key, name);
                }
                if ((connection instanceof LDAPSchemaConnection) && (repositoryValue.equals("LDAP"))) { //$NON-NLS-1$
                    addOrderDisplayNames(connectionValuesList, connectionNamesList, key, name);
                }
                if ((connection instanceof WSDLSchemaConnection) && (repositoryValue.equals("WSDL"))) { //$NON-NLS-1$
                    addOrderDisplayNames(connectionValuesList, connectionNamesList, key, name);
                }
                if ((connection instanceof DatabaseConnection) && (repositoryValue.startsWith("DATABASE"))) { //$NON-NLS-1$
                    String currentDbType = (String) RepositoryToComponentProperty.getValue(connection, "TYPE"); //$NON-NLS-1$
                    if (repositoryValue.contains(":")) { // database
                        // is
                        // specified
                        // //$NON-NLS-1$
                        String neededDbType = repositoryValue.substring(repositoryValue.indexOf(":") + 1); //$NON-NLS-1$
                        if (neededDbType.equals(currentDbType)) {
                            addOrderDisplayNames(connectionValuesList, connectionNamesList, key, name);
                        }
                    } else {
                        addOrderDisplayNames(connectionValuesList, connectionNamesList, key, name);
                    }
                }
            }

            repositoryConnectionNameList.addAll(connectionNamesList);
            repositoryConnectionValueList.addAll(connectionValuesList);
        } else {
            List<String> connectionValuesList = new ArrayList<String>();
            List<String> connectionStringList = new ArrayList<String>();
            for (String key : repositoryConnectionItemMap.keySet()) {
                ConnectionItem connectionItem = repositoryConnectionItemMap.get(key);
                String name = connectionItem.getProperty().getLabel();
                addOrderDisplayNames(connectionValuesList, connectionStringList, key, name);
            }
            repositoryConnectionNameList.addAll(connectionStringList);
            repositoryConnectionValueList.addAll(connectionValuesList);
        }
        param.setListItemsDisplayName(repositoryConnectionNameList.toArray(new String[0]));
        param.setListItemsValue(repositoryConnectionValueList.toArray(new String[0]));
        if (!repositoryConnectionItemMap.keySet().contains(param.getValue())) {
            if (repositoryConnectionNameList.size() > 0) {
                param.setValue(repositoryConnectionValueList.get(0));
            }
        }
    }

    /**
     * qzhang Comment method "addOrderDisplayNames".
     * 
     * @param connectionValuesList
     * @param connectionStringList
     * @param key
     * @param name
     */
    private void addOrderDisplayNames(List<String> connectionValuesList, List<String> connectionStringList, String key,
            String name) {
        int i = 0;

        for (; i < connectionStringList.size(); i++) {
            String string = connectionStringList.get(i);
            if (name.compareTo(string) < 0) {
                connectionStringList.add(i, name);
                connectionValuesList.add(i, key);
                break;
            }
        }
        if (connectionStringList.size() == 0 || i == connectionStringList.size()) {
            connectionStringList.add(name);
            connectionValuesList.add(key);
        }
    }

    /**
     * ftang Comment method "getElement".
     * 
     * @return an instance of Element
     */
    public Element getElement() {
        return elem;
    }

    /**
     * 
     */
    private boolean checkErrorsWhenViewRefreshed;

    public void addComponents(boolean forceRedraw) {
        addComponents(forceRedraw, true, 0);
    }

    /**
     * yzhang Comment method "addcomponents".
     * 
     * @param forceRedraw
     * @param reInitialize
     */
    public void addComponents(boolean forceRedraw, boolean reInitialize) {
        addComponents(forceRedraw, reInitialize, 0);
    }

    protected void disposeChildren() {
        if (composite != null && !composite.isDisposed()) {
            // Empty the composite before use (kinda refresh) :
            Control[] ct = composite.getChildren();
            for (int i = 0; i < ct.length; i++) {
                ct[i].dispose();
            }
        }
    }

    private static final int DEFAULT_GROUP_HEIGHT = 20;

    /**
     * Initialize all components for the defined section for this node.
     */
    public void addComponents(boolean forceRedraw, boolean reInitialize, int height) {
        checkErrorsWhenViewRefreshed = true;
        int heightSize = 0, maxRowSize = 0, nbInRow, numInRow;
        int maxRow;

        Map<String, Integer> groupPosition = new HashMap<String, Integer>();
        List<? extends IElementParameter> listParam = elem.getElementParametersWithChildrens();

        // updateMainParameters();

        if (!forceRedraw) {
            boolean needRedraw = isNeedRedraw();
            if (!needRedraw) {
                // System.out.println("no need redraw");
                return;
            }
        }

        Control lastControl = null;
        if (reInitialize) {
            if (currentComponent != null) {
                disposeChildren();
            }
        } else {
            heightSize = height;
        }

        hashCurControls = new DualHashBidiMap();

        maxRow = 0;
        for (int i = 0; i < listParam.size(); i++) {
            if (listParam.get(i).getCategory() == section) {
                if (listParam.get(i).getNumRow() > maxRow && listParam.get(i).isShow(listParam)) {
                    maxRow = listParam.get(i).getNumRow();
                }
            }
        }

        IElementParameter synchronizeSchemaParam = elem.getElementParameter(EParameterName.NOT_SYNCHRONIZED_SCHEMA.getName());

        if (synchronizeSchemaParam != null) {
            // if the node don't contains a schema type and accept an input flow and is not synchronized
            // display a schema on the first line just the type to synchronize the schema
            synchronizeSchemaParam.setShow(!((Node) elem).isSchemaSynchronized());
        }

        generator.initController(this);

        // System.out.println("********************** NEW ADDCOMPONENTS
        // **********************");
        // TabbedPropertyComposite tabbedPropertyComposite = this.getTabbedPropertyComposite();
        int additionalHeightSize = 0;
        boolean hasDynamicRow = false;
        for (int i = 0; i < listParam.size(); i++) {
            IElementParameter curParam = listParam.get(i);
            if (curParam.getCategory() == section) {
                if (curParam.getField() != EParameterFieldType.TECHNICAL) {
                    if (curParam.isShow(listParam)) {
                        AbstractElementPropertySectionController controller = generator.getController(curParam.getField(), this);

                        if (controller == null) {
                            continue;
                        }
                        if (controller.hasDynamicRowSize()) {
                            hasDynamicRow = true;
                            break;
                        }
                    }
                }
            }
        }
        if (hasDynamicRow) {
            additionalHeightSize = estimatePropertyHeightSize(maxRow, listParam);
        }

        long lastTime = TimeMeasure.timeSinceBegin("DC:refresh:" + getCurrentComponent());
        for (int curRow = 1; curRow <= maxRow; curRow++) {
            maxRowSize = 0;
            nbInRow = 0;
            for (int i = 0; i < listParam.size(); i++) {
                IElementParameter curParam = listParam.get(i);
                if (curParam.getCategory() == section) {
                    if (curParam.getNumRow() == curRow && curParam.isShow(listParam)
                            && (curParam.getField() != EParameterFieldType.TECHNICAL)) {
                        nbInRow++;
                    }
                }
            }
            numInRow = 0;
            lastControl = null;
            curRowSize = 0;
            for (int i = 0; i < listParam.size(); i++) {
                IElementParameter curParam = listParam.get(i);
                if (curParam.getCategory() == section) {
                    if (curParam.getNumRow() == curRow && (curParam.getField() != EParameterFieldType.TECHNICAL)) {
                        // System.out.println("test:" + curParam.getName() + "
                        // field:"+curParam.getField());
                        if (curParam.isShow(listParam)) {
                            // System.out.println("show:" + curParam.getName()+
                            // " field:"+curParam.getField());
                            numInRow++;
                            AbstractElementPropertySectionController controller = generator.getController(curParam.getField(),
                                    this);

                            if (controller == null) {
                                continue;
                            }
                            if (controller.hasDynamicRowSize()) {
                                controller.setAdditionalHeightSize(additionalHeightSize);
                            }

                            String groupName = curParam.getGroup();
                            Composite subComposite = null;

                            if (groupName != null) {
                                if (!hashCurControls.containsKey(groupName)) {
                                    if (groupPosition.size() > 0) {
                                        heightSize += DEFAULT_GROUP_HEIGHT;
                                    }
                                    new GroupController(this).createControl(composite, curParam, numInRow, nbInRow, heightSize,
                                            lastControl);
                                    groupPosition.put(groupName, heightSize);
                                }
                                subComposite = (Composite) hashCurControls.get(groupName);
                                int h2 = heightSize - groupPosition.get(groupName);
                                lastControl = controller
                                        .createControl(subComposite, curParam, numInRow, nbInRow, h2, lastControl);

                            } else {
                                int h3 = DEFAULT_GROUP_HEIGHT * (groupPosition.size() > 0 ? 1 : 0) + heightSize;
                                lastControl = controller.createControl(composite, curParam, numInRow, nbInRow, h3, lastControl);
                            }

                            lastTime = TimeMeasure.timeSinceBegin("DC:refresh:" + getCurrentComponent()) - lastTime;
                            if (DynamicTabbedPropertySection.DEBUG_TIME) {
                                System.out.println("DC;create:" + curParam.getField().getName() + ";" + getCurrentComponent()
                                        + ";" + lastTime);
                            }

                            // System.out.println("param:" + curParam.getName()
                            // + " - curRowSize:" + curRowSize);

                            if (curRowSize > maxRowSize) {
                                maxRowSize = curRowSize;
                            }
                        }
                    }
                }
            }
            heightSize += maxRowSize;

        }
        if (synchronizeSchemaParam != null) {
            synchronizeSchemaParam.setShow(false);
        }

        resizeScrolledComposite();
    }

    // /**
    // * DOC Administrator Comment method "updateMainParameters".
    // */
    // protected void updateMainParameters() {
    // oldQueryStoreType = (String) elem.getPropertyValue(EParameterName.QUERYSTORE_TYPE.getName());
    // if (oldQueryStoreType != null) {
    // if (oldQueryStoreType.equals(EmfComponent.REPOSITORY)) {
    // showQueryStoreRepositoryList(true);
    // updateRepositoryList();
    // } else {
    // showQueryStoreRepositoryList(false);
    // }
    // }
    //
    // IElementParameter param = elem.getElementParameter(EParameterName.PROPERTY_TYPE.getName());
    // if (param != null) {
    // oldPropertyType = (String) param.getValue();
    // if (param.isShow(elem.getElementParameters())) {
    // if (oldPropertyType.equals(EmfComponent.REPOSITORY)) {
    // showPropertyRepositoryList(true, false);
    // updateRepositoryList();
    // } else {
    // showPropertyRepositoryList(false, false);
    // }
    // } else {
    // showPropertyRepositoryList(false, false);
    // }
    // }
    // // for job settings extra (feature 2710)
    // param = elem.getElementParameter(extraPropertyTypeName);
    // if (param != null) {
    // oldPropertyType = (String) param.getValue();
    // if (param.isShow(elem.getElementParameters())) {
    // if (oldPropertyType.equals(EmfComponent.REPOSITORY)) {
    // showPropertyRepositoryList(true, true);
    // updateRepositoryList();
    // } else {
    // showPropertyRepositoryList(false, true);
    // }
    // } else {
    // showPropertyRepositoryList(false, true);
    // }
    // }
    // oldProcessType = (String) elem.getPropertyValue(EParameterName.PROCESS_TYPE_PROCESS.getName());
    // if (oldProcessType != null) {
    // String[] list =
    // elem.getElementParameter(EParameterName.PROCESS_TYPE_PROCESS.getName()).getListItemsDisplayName();
    // if ((oldProcessType.equals("NO_PROCESS") || (list.length == 0))) { //$NON-NLS-1$
    // updateProcessList();
    // updateContextList();
    // if (elem instanceof Node) {
    // ((Node) elem).checkAndRefreshNode();
    // }
    // }
    // }
    // }

    /**
     * DOC Administrator Comment method "isNeedRedraw".
     * 
     * @return
     */
    protected boolean isNeedRedraw() {
        boolean needRedraw = false;
        for (IElementParameter elementParameter : elem.getElementParametersWithChildrens()) {
            if (elementParameter.getCategory().equals(section)
                    && (elementParameter.getField() != EParameterFieldType.SCHEMA_TYPE)
                    && (elementParameter.getField() != EParameterFieldType.QUERYSTORE_TYPE)) {
                // if the component must be displayed, then check if the
                // control exists or not.
                boolean show = elementParameter.isShow(elem.getElementParameters());
                Object control;
                if (elementParameter.getParentParameter() == null) {
                    control = hashCurControls.get(elementParameter.getName());
                } else {
                    control = hashCurControls.get(elementParameter.getParentParameter().getName() + ":"
                            + elementParameter.getName());
                }
                if ((control == null && show) || (control != null && !show)) {
                    needRedraw = true;
                    break;

                }
            }
        }
        return needRedraw;
    }

    /**
     * DOC nrousseau Comment method "estimatePropertyHeightSize".
     * 
     * @param maxRow
     * @param listParam
     * @param tabbedPropertyComposite
     */
    private int estimatePropertyHeightSize(int maxRow, List<? extends IElementParameter> listParam) {
        int estimatedHeightSize = 0, estimatedMaxRowSize = 0;
        int additionalHeightSize = 0;
        int compositeHeight = getParent().getBounds().height;

        // System.out.println("size composite:" + compositeHeight);

        int nbDynamic = 0;
        for (int curRow = 1; curRow <= maxRow; curRow++) {
            estimatedMaxRowSize = 0;
            for (int i = 0; i < listParam.size(); i++) {
                IElementParameter curParam = listParam.get(i);
                if (curParam.getCategory() == section) {
                    if (curParam.getNumRow() == curRow && (curParam.getField() != EParameterFieldType.TECHNICAL)) {
                        // System.out.println("test:" + curParam.getName() + "
                        // field:"+curParam.getField());
                        if (curParam.isShow(listParam)) {
                            // System.out.println("show:" + curParam.getName()+
                            // " field:"+curParam.getField());
                            AbstractElementPropertySectionController controller = generator.getController(curParam.getField(),
                                    this);

                            if (controller == null) {
                                break;
                            }
                            int estimatedSize = controller.estimateRowSize(composite, curParam);
                            if (controller.hasDynamicRowSize()) {
                                nbDynamic++;
                            }
                            // System.out.println("param:" + curParam.getName()
                            // + " - estimatedSize:" + estimatedSize);

                            if (estimatedSize > estimatedMaxRowSize) {
                                estimatedMaxRowSize = estimatedSize;
                            }
                        }
                    }
                }
            }
            estimatedHeightSize += estimatedMaxRowSize;
        }
        // System.out.println("*** ESTIMATED SIZE:" + estimatedHeightSize + "
        // ***");
        int emptySpace = compositeHeight - estimatedHeightSize;
        // System.out.println("--- EMPTY SPACE:" + emptySpace);
        if (emptySpace > 0 && nbDynamic > 0) {
            additionalHeightSize = emptySpace / nbDynamic;
            // System.out.println("--- DIVIDED ADDITIONAL HEIGHT (for each
            // dynamic):" + additionalHeightSize);
        }
        return additionalHeightSize;
    }

    private void resizeScrolledComposite() {
        // Point compositeSize = composite.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        //
        // Point parentSize = getParent().getSize();
        //
        // System.out.println("compositeSize:" + compositeSize + " / parentSize:" + parentSize);

        lastCompositeSize = getParent().getClientArea().height;

        // setMinSize(compositeSize);
        propertyResized = true;
    }

    public void refresh() {
        TimeMeasure.display = false;
        TimeMeasure.measureActive = true;
        TimeMeasure.begin("DC:refresh:" + getCurrentComponent());

        if (elem == null) {
            return;
        }
        List<? extends IElementParameter> listParam = elem.getElementParameters();

        IElementParameter jobParam = elem.getElementParameterFromField(EParameterFieldType.PROCESS_TYPE);
        if (jobParam != null) {
            // updateProcessList();
            updateContextList(jobParam);
            if (elem instanceof Node) {
                ((Node) elem).checkAndRefreshNode();
            }
        }

        Boolean updateNeeded = (Boolean) elem.getPropertyValue(updataComponentParamName);

        if (updateNeeded != null) {
            if (updateNeeded) {
                addComponents(forceRedraw);
                elem.setPropertyValue(updataComponentParamName, new Boolean(false));
            }
        }
        forceRedraw = false;

        for (int i = 0; i < listParam.size(); i++) {
            if (listParam.get(i).getCategory() == section) {
                if (listParam.get(i).isShow(listParam)) {
                    AbstractElementPropertySectionController controller = generator.getController(listParam.get(i).getField(),
                            this);
                    if (controller != null) {
                        controller.refresh(listParam.get(i), checkErrorsWhenViewRefreshed);
                    }
                }
            }
        }
        if (propertyResized) {
            removeListener(SWT.Resize, resizeListener);
            composite.pack();
            getParent().layout();
            propertyResized = false;
            addListener(SWT.Resize, resizeListener);
        }
        checkErrorsWhenViewRefreshed = false;
        long time = TimeMeasure.timeSinceBegin("DC:refresh:" + getCurrentComponent());
        TimeMeasure.end("DC:refresh:" + getCurrentComponent());
        if (DynamicTabbedPropertySection.DEBUG_TIME) {
            System.out.println("DC;total;" + getCurrentComponent() + ";" + time);
        }
    }

    private final Listener resizeListener = new Listener() {

        public void handleEvent(Event event) {
            resizeLimiter.resetTimer();
            resizeLimiter.startIfExecutable(true);
        }
    };

    private final ExecutionLimiter resizeLimiter = new ExecutionLimiter(250, true) {

        @Override
        public void execute(final boolean isFinalExecution) {
            if (!isDisposed()) {
                getDisplay().asyncExec(new Runnable() {

                    public void run() {
                        if (!isDisposed() && !getParent().isDisposed()) {
                            int currentSize = getParent().getClientArea().height;
                            if (getLastCompositeSize() != currentSize) {
                                addComponents(true);
                                refresh();
                            }
                        }
                    }

                });
            }
        }
    };

    /**
     * Set the section of the tabbed property.
     * 
     * @param section
     */
    public DynamicComposite(Composite parentComposite, int styles, final EComponentCategory section, Element element) {
        super(parentComposite, styles);
        // for job settings extra (feature 2710)
        // if (section == EComponentCategory.EXTRA) {
        // updataComponentParamName =
        // JobSettingsConstants.getExtraParameterName(EParameterName.UPDATE_COMPONENTS.getName());
        // } else {
        updataComponentParamName = EParameterName.UPDATE_COMPONENTS.getName();
        // }
        FormData d = new FormData();
        d.left = new FormAttachment(0, 0);
        d.right = new FormAttachment(100, 0);
        d.top = new FormAttachment(0, 0);
        d.bottom = new FormAttachment(100, 0);
        setLayoutData(d);

        setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));

        setExpandHorizontal(true);
        // setExpandVertical(true);

        composite = new WidgetFactory().createComposite(this, SWT.NO_FOCUS);
        setContent(composite);

        generator = new DynamicPropertyGenerator();
        this.section = section;
        this.elem = element;
        if (elem instanceof Node) {
            process = (Process) ((Node) elem).getProcess();
        }
        if (elem instanceof org.talend.designer.core.ui.editor.connections.Connection) {
            org.talend.designer.core.ui.editor.connections.Connection connection;
            connection = (org.talend.designer.core.ui.editor.connections.Connection) elem;
            process = (Process) connection.getSource().getProcess();
        }
        if (elem instanceof Process) {
            process = (Process) elem;
        }
        if (process != null) {
            part = process.getEditor();
        }
        FormLayout layout = new FormLayout();
        layout.marginWidth = ITabbedPropertyConstants.HSPACE + 2;
        layout.marginHeight = ITabbedPropertyConstants.VSPACE;
        layout.spacing = ITabbedPropertyConstants.VMARGIN + 1;
        composite.setLayout(layout);

        repositoryQueryStoreMap = new HashMap<String, Query>();
        repositoryConnectionItemMap = new HashMap<String, ConnectionItem>();
        repositoryTableMap = new HashMap<String, IMetadataTable>();
        hashCurControls = new DualHashBidiMap();

        if ((currentComponent == null) || (!currentComponent.equals(elem.getElementName()))) {
            forceRedraw = true;
            elem.setPropertyValue(updataComponentParamName, Boolean.TRUE);

        }
        currentComponent = elem.getElementName();

        addListener(SWT.Resize, resizeListener);
        addListener(SWT.FocusOut, new Listener() {

            public void handleEvent(Event event) {
                // if the focus is lost reinitialise all information from repository
                repositoryTableMap.clear();
                repositoryQueryStoreMap.clear();
                repositoryConnectionItemMap.clear();
            }

        });

        if (getCommandStack() != null) {
            getCommandStack().addCommandStackEventListener(commandStackEventListener);
        }
        // for job settings extra (feature 2710)
        // extraPropertyTypeName = JobSettingsConstants.getExtraParameterName(EParameterName.PROPERTY_TYPE.getName());
        // extraRepositoryPropertyTypeName =
        // JobSettingsConstants.getExtraParameterName(EParameterName.REPOSITORY_PROPERTY_TYPE
        // .getName());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.widgets.Widget#dispose()
     */
    @Override
    public void dispose() {
        CommandStack cmdStack = getCommandStack();
        if (cmdStack != null) {
            cmdStack.removeCommandStackEventListener(commandStackEventListener);
        }
        super.dispose();
        process = null;
        elem = null;
        part = null;
        generator.dispose();
        generator = null;
        hashCurControls.clear();
    }

    CommandStackEventListener commandStackEventListener = new CommandStackEventListener() {

        public void stackChanged(CommandStackEvent event) {
            int detail = event.getDetail();
            if ((getElement() instanceof org.talend.designer.core.ui.editor.connections.Connection)
                    && (event.getCommand() instanceof ChangeMetadataCommand)
                    && (0 != (detail & CommandStack.POST_EXECUTE) || 0 != (detail & CommandStack.POST_REDO) // 
                    || 0 != (detail & CommandStack.POST_REDO))) {
                addComponents(true);
                refresh();
            }
            if (0 != (detail & CommandStack.POST_EXECUTE) || 0 != (detail & CommandStack.POST_UNDO)
                    || 0 != (detail & CommandStack.POST_REDO)) {
                Boolean updateNeeded = (Boolean) elem.getPropertyValue(updataComponentParamName);
                // System.out.println("elem:" + elem.getElementName() + "(" + section + ") --- update needed:" +
                // updateNeeded);
                if (updateNeeded) {
                    refresh();
                }
            }
        }
    };

    /**
     * yzhang Comment method "setCurRowSize" Sets the curRowSize.
     * 
     * @param curRowSize int
     */
    public void setCurRowSize(int curRowSize) {
        this.curRowSize = curRowSize;
    }

    /**
     * dev Comment method "getRepositoryTableMap".
     * 
     * @return Map
     */
    public Map<String, IMetadataTable> getRepositoryTableMap() {
        if (this.repositoryTableMap.keySet().isEmpty()) {
            updateRepositoryList();
        }
        return this.repositoryTableMap;
    }

    /**
     * dev Comment method "getRepositoryConnectionItemMap".
     * 
     * @return Map
     */
    public Map<String, ConnectionItem> getRepositoryConnectionItemMap() {
        if (this.repositoryConnectionItemMap.keySet().isEmpty()) {
            updateRepositoryList();
        }
        return this.repositoryConnectionItemMap;
    }

    /**
     * Getter for currentComponent.
     * 
     * @return the currentComponent
     */
    public String getCurrentComponent() {
        return this.currentComponent;
    }

    /**
     * Getter for curRowSize.
     * 
     * @return the curRowSize
     */
    public int getCurRowSize() {
        return this.curRowSize;
    }

    /**
     * Getter for hashCurControls.
     * 
     * @return the hashCurControls
     */
    public BidiMap getHashCurControls() {
        return this.hashCurControls;
    }

    /**
     * Getter for part.
     * 
     * @return the part
     */
    public AbstractMultiPageTalendEditor getPart() {
        return this.part;
    }

    /**
     * Getter for section.
     * 
     * @return the section
     */
    public EComponentCategory getSection() {
        return this.section;
    }

    /**
     * Getter for repositoryQueryStoreMap.
     * 
     * @return the repositoryQueryStoreMap
     */
    public Map<String, Query> getRepositoryQueryStoreMap() {
        if (this.repositoryQueryStoreMap.keySet().isEmpty()) {
            updateRepositoryList();
        }
        return repositoryQueryStoreMap;
    }

    /**
     * Get the command stack of the Gef editor.
     * 
     * @return
     */
    protected CommandStack getCommandStack() {
        if (part != null && part.getTalendEditor() != null) {
            Object adapter = part.getTalendEditor().getAdapter(CommandStack.class);
            return (CommandStack) adapter;
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private void updateQuery() {
        Object propertyValue = elem.getPropertyValue(EParameterName.REPOSITORY_QUERYSTORE_TYPE.getName());
        if (propertyValue == null || !(propertyValue instanceof String) || "".equals(propertyValue)
                || elem.getPropertyValue(EParameterName.QUERYSTORE_TYPE.getName()).equals(EmfComponent.BUILTIN)) {
            return;
        }
        if (repositoryQueryStoreMap.containsKey(propertyValue)) {
            Query query = repositoryQueryStoreMap.get(propertyValue);
            for (IElementParameter param : (List<IElementParameter>) elem.getElementParameters()) {
                if (param.getField() == EParameterFieldType.MEMO_SQL) {
                    elem.setPropertyValue(param.getName(), convertSQL(query.getValue()));
                    param.setRepositoryValueUsed(true);
                }
            }
        }
    }

    private String convertSQL(String sql) {

        if (sql.startsWith("'") || sql.startsWith("\"")) { //$NON-NLS-1$
            return sql;
        }
        return TalendTextUtils.addQuotes(sql); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * qzhang Comment method "getDefaultRepository".
     * 
     * @return
     */
    private String getDefaultRepository(IElementParameter baseParam, boolean istable, String defaultPropertyValue) {
        boolean metadataInput = false;
        if (((Node) elem).getCurrentActiveLinksNbInput(EConnectionType.FLOW_MAIN) > 0
                || ((Node) elem).getCurrentActiveLinksNbInput(EConnectionType.FLOW_REF) > 0
                || ((Node) elem).getCurrentActiveLinksNbInput(EConnectionType.TABLE) > 0) {
            metadataInput = true;
        }

        if (metadataInput && istable) {
            return (String) baseParam.getChildParameters().get(EParameterName.REPOSITORY_SCHEMA_TYPE.getName()).getValue();
        }
        Object propertyValue = elem.getPropertyValue(EParameterName.REPOSITORY_PROPERTY_TYPE.getName());
        if ((propertyValue == null || !(propertyValue instanceof String)) && defaultPropertyValue != null) {
            propertyValue = defaultPropertyValue;
        }
        if (propertyValue == null || propertyValue.equals("")) {
            return "";
        }
        if (istable) {
            List<String> list = tablesMap.get(propertyValue);
            if (list != null) {
                if (list.size() > 0) {
                    return tablesMap.get(propertyValue).get(0);
                }
            }
        } else {
            List<String> list = queriesMap.get(propertyValue);
            if (list != null) {
                if (queriesMap.get(propertyValue).size() > 0) {
                    return queriesMap.get(propertyValue).get(0);
                }
            }
        }
        return "";
    }

    /**
     * qzhang Comment method "initMaps".
     */
    private void initMaps() {
        for (String key : tablesMap.keySet()) {
            List<String> tablesName = new ArrayList<String>();
            List<String> queriesName = new ArrayList<String>();
            queriesName.addAll(queriesMap.get(key));
            tablesName.addAll(tablesMap.get(key));
            for (String string : tablesMap.get(key)) {
                if (!string.startsWith(key)) {
                    tablesName.remove(string);
                }
            }

            for (String string : queriesMap.get(key)) {
                if (!string.startsWith(key)) {
                    queriesName.remove(string);
                }
            }
            tablesMap.put(key, tablesName);
            queriesMap.put(key, queriesName);
        }
    }

    /**
     * Getter for tablesMap.
     * 
     * @return the tablesMap
     */
    public Map<String, List<String>> getTablesMap() {
        initMaps();
        return this.tablesMap;
    }

    /**
     * Getter for queriesMap.
     * 
     * @return the queriesMap
     */
    public Map<String, List<String>> getQueriesMap() {
        initMaps();
        return this.queriesMap;
    }

    /**
     * Getter for tableIdAndDbTypeMap.
     * 
     * @return the tableIdAndDbTypeMap
     */
    public Map<String, String> getTableIdAndDbTypeMap() {
        return this.tableIdAndDbTypeMap;
    }

    /**
     * Getter for tableIdAndDbSchemaMap.
     * 
     * @return the tableIdAndDbSchemaMap
     */
    public Map<String, String> getTableIdAndDbSchemaMap() {
        return this.tableIdAndDbSchemaMap;
    }

    /**
     * Getter for lastCompositeSize.
     * 
     * @return the lastCompositeSize
     */
    public int getLastCompositeSize() {
        return this.lastCompositeSize;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties.controllers.generator.IDynamicProperty#getComposite()
     */
    public Composite getComposite() {
        return composite;
    }
}
