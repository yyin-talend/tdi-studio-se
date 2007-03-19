// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
//
// ============================================================================
package org.talend.designer.core.ui.editor.properties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.bidimap.DualHashBidiMap;
import org.eclipse.core.runtime.IPath;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CommandStackEvent;
import org.eclipse.gef.commands.CommandStackEventListener;
import org.eclipse.jface.util.Assert;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.PropertySheet;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.swt.advanced.dataeditor.commands.IExtendedTableCommand;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreatorColumn;
import org.talend.commons.utils.data.container.Content;
import org.talend.commons.utils.data.container.ContentList;
import org.talend.commons.utils.data.container.RootContainer;
import org.talend.core.model.metadata.ColumnNameChanged;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.builder.ConvertionHelper;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.DelimitedFileConnection;
import org.talend.core.model.metadata.builder.connection.PositionalFileConnection;
import org.talend.core.model.metadata.builder.connection.QueriesConnection;
import org.talend.core.model.metadata.builder.connection.Query;
import org.talend.core.model.metadata.builder.connection.RegexpFileConnection;
import org.talend.core.model.metadata.builder.connection.XmlFileConnection;
import org.talend.core.model.metadata.designerproperties.RepositoryToComponentProperty;
import org.talend.core.model.process.EComponentCategory;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.Element;
import org.talend.core.model.process.IConnection;
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
import org.talend.designer.core.ui.MultiPageTalendEditor;
import org.talend.designer.core.ui.editor.TalendEditor;
import org.talend.designer.core.ui.editor.connections.ConnectionLabel;
import org.talend.designer.core.ui.editor.nodecontainer.NodeContainerPart;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.nodes.NodeLabel;
import org.talend.designer.core.ui.editor.outline.NodeReturnsTreeEditPart;
import org.talend.designer.core.ui.editor.outline.NodeTreeEditPart;
import org.talend.designer.core.ui.editor.properties.controllers.AbstractElementPropertySectionController;
import org.talend.repository.model.ERepositoryStatus;
import org.talend.repository.model.IProxyRepositoryFactory;

/**
 * yzhang class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class DynamicTabbedPropertySection extends AbstractPropertySection {

    protected MultiPageTalendEditor part;

    protected Element elem;

    protected Composite composite;

    protected BidiMap hashCurControls;

    protected String currentComponent;

    protected EComponentCategory section;

    protected int curRowSize;

    protected DynamicTabbedPropertyGenerator generator = DynamicTabbedPropertyGenerator.getDefault(this);;

    private String oldProcessType;

    private Map<String, IMetadataTable> repositoryTableMap;

    private Map<String, ConnectionItem> repositoryConnectionItemMap;

    private Map<String, IRepositoryObject> processMap;

    private String oldPropertyType;

    private Map<String, Query> repositoryQueryStoreMap;

    private String oldQueryStoreType;

    private static CommandStackEventListener commandStackEventListener;

    /**
     * ftang Comment method "showSchemaRepositoryList".
     * 
     * @param show boolean
     */
    private void showSchemaRepositoryList(boolean show) {
        for (int i = 0; i < elem.getElementParameters().size(); i++) {
            IElementParameter param = elem.getElementParameters().get(i);
            if (param.getName().equals(EParameterName.REPOSITORY_SCHEMA_TYPE.getName())) {
                param.setShow(show);
            }
        }
    }

    /**
     * ftang Comment method "showQueryStoreRepositoryList".
     * 
     * @param show
     */
    private void showQueryStoreRepositoryList(boolean show) {
        for (int i = 0; i < elem.getElementParameters().size(); i++) {
            IElementParameter param = elem.getElementParameters().get(i);
            if (param.getName().equals(EParameterName.REPOSITORY_QUERYSTORE_TYPE.getName())) {
                param.setShow(show);
            }
        }
    }

    /**
     * ftang Comment method "showPropertyRepositoryList".
     * 
     * @param show boolean
     */
    private void showPropertyRepositoryList(boolean show) {
        for (int i = 0; i < elem.getElementParameters().size(); i++) {
            IElementParameter param = elem.getElementParameters().get(i);
            if (param.getName().equals(EParameterName.REPOSITORY_PROPERTY_TYPE.getName())) {
                param.setShow(show);
            }
        }
    }

    /**
     * ftang Comment method "updateProcessList".
     */
    private void updateProcessList() {
        List<String> processNameList = new ArrayList<String>();
        List<String> processValueList = new ArrayList<String>();
        processMap = new HashMap<String, IRepositoryObject>();
        IProxyRepositoryFactory factory = DesignerPlugin.getDefault().getProxyRepositoryFactory();
        try {
            RootContainer<String, IRepositoryObject> processContainer = factory.getProcess();
            ContentList<String, IRepositoryObject> processAbsoluteMembers = processContainer.getAbsoluteMembers();

            for (Content<String, IRepositoryObject> object : processAbsoluteMembers.values()) {
                IRepositoryObject process = (IRepositoryObject) object.getContent();
                if (factory.getStatus(process) != ERepositoryStatus.DELETED) {
                    String path = object.getParent().getPath().toString();
                    String name;
                    if (path.equals("")) { //$NON-NLS-1$
                        name = IPath.SEPARATOR + process.getLabel();
                    } else {
                        name = IPath.SEPARATOR + path + IPath.SEPARATOR + process.getLabel();
                    }

                    processNameList.add(name);
                    processMap.put(name, process);
                    processValueList.add("'" + process.getLabel() + "'"); //$NON-NLS-1$ //$NON-NLS-2$
                }
            }
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
        String[] processTableNameList = (String[]) processNameList.toArray(new String[0]);
        String[] processTableValueList = (String[]) processValueList.toArray(new String[0]);

        for (int i = 0; i < elem.getElementParameters().size(); i++) {
            IElementParameter param = elem.getElementParameters().get(i);
            if (param.getName().equals(EParameterName.PROCESS_TYPE_PROCESS.getName())) {
                param.setListItemsDisplayName(processTableNameList);
                param.setListItemsValue(processTableValueList);
                if (param.getValue().equals("NO_PROCESS")) { //$NON-NLS-1$
                    if (!processMap.keySet().contains(param.getValue())) {
                        if (processTableNameList.length > 0) {
                            elem.setPropertyValue(EParameterName.PROCESS_TYPE_PROCESS.getName(), processTableValueList[0]);
                        }
                    }
                }
                if (elem instanceof Node) {
                    ((Node) elem).checkAndRefreshNode();
                }
            }
        }
    }

    /**
     * ftang Comment method "updateContextList".
     */
    private void updateContextList() {
        List<String> contextNameList = new ArrayList<String>();
        List<String> contextValueList = new ArrayList<String>();
        IProxyRepositoryFactory factory = DesignerPlugin.getDefault().getProxyRepositoryFactory();

        String selectedProcess = null;

        for (int i = 0; (i < elem.getElementParameters().size()) && (selectedProcess == null); i++) {
            IElementParameter param = elem.getElementParameters().get(i);
            if (param.getName().equals(EParameterName.PROCESS_TYPE_PROCESS.getName())) {
                selectedProcess = (String) param.getValue();
            }
        }

        if (selectedProcess == null) {
            return;
        }

        try {
            List<IRepositoryObject> list = factory.getAll(ERepositoryObjectType.PROCESS);

            for (IRepositoryObject process : list) {
                String id = "'" + process.getLabel() + "'"; //$NON-NLS-1$ //$NON-NLS-2$
                if (selectedProcess.equals(id)) {
                    if (process.getProperty().getItem() instanceof ProcessItem) {
                        ProcessItem processItem = (ProcessItem) process.getProperty().getItem();

                        for (Object o : processItem.getProcess().getContext()) {
                            if (o instanceof ContextType) {
                                ContextType context = (ContextType) o;
                                contextNameList.add(context.getName());
                                contextValueList.add("'" + context.getName() + "'"); //$NON-NLS-1$ //$NON-NLS-2$
                            }
                        }
                    }
                }
            }
        } catch (PersistenceException e) {
            ExceptionHandler.process(e);
        }

        String[] contextTableNameList = (String[]) contextNameList.toArray(new String[0]);
        String[] contextTableValueList = (String[]) contextValueList.toArray(new String[0]);

        for (int i = 0; i < elem.getElementParameters().size(); i++) {
            IElementParameter param = elem.getElementParameters().get(i);
            if (param.getName().equals(EParameterName.PROCESS_TYPE_CONTEXT.getName())) {
                param.setListItemsDisplayName(contextTableNameList);
                param.setListItemsValue(contextTableValueList);
                if (!contextValueList.contains(param.getValue())) {
                    if (contextTableNameList.length > 0) {
                        elem.setPropertyValue(EParameterName.PROCESS_TYPE_CONTEXT.getName(), contextTableValueList[0]);
                    }
                }
            }
        }

    }

    private String getRepositoryAliasName(ConnectionItem connectionItem) {
        ERepositoryObjectType repositoryObjectType = ERepositoryObjectType.getItemType(connectionItem);
        String aliasName = repositoryObjectType.getAlias();
        Connection connection = (Connection) connectionItem.getConnection();
        if (connection instanceof DatabaseConnection) {
            String currentDbType = (String) RepositoryToComponentProperty.getValue(connection, "TYPE"); //$NON-NLS-1$
            aliasName += " (" + currentDbType + ")"; //$NON-NLS-1$ //$NON-NLS-2$
        }
        return aliasName;
    }

    private Map<String, List<String>> tablesMap = new HashMap<String, List<String>>();

    private Map<String, List<String>> queriesMap = new HashMap<String, List<String>>();

    /**
     * ftang Comment method "updateRepositoryList".
     */
    @SuppressWarnings("unchecked")//$NON-NLS-1$
    public void updateRepositoryList() {
        IProxyRepositoryFactory factory = DesignerPlugin.getDefault().getProxyRepositoryFactory();
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
                Connection connection = (Connection) connectionItem.getConnection();
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
                                repositoryTableMap.put(value, ConvertionHelper.convert(table));
                                tableNamesList.add(name);
                                tableValuesList.add(value);
                            }
                        }
                    }
                }
                tablesMap.put(connectionItem.getProperty().getId(), tableValuesList);
                if (connection instanceof DatabaseConnection && !connection.isReadOnly()) {
                    DatabaseConnection dbConnection = (DatabaseConnection) connection;
                    List<QueriesConnection> qcs = dbConnection.getQueries();
                    if (!qcs.isEmpty()) {
                        QueriesConnection connection2 = qcs.get(0);
                        List<Query> qs = connection2.getQuery();
                        for (Query query : qs) {
                            String name = getRepositoryAliasName(connectionItem) + ":" //$NON-NLS-1$
                                    + connectionItem.getProperty().getLabel() + " - " + query.getLabel(); //$NON-NLS-1$
                            String value = connectionItem.getProperty().getId() + " - " + query.getLabel(); //$NON-NLS-1$
                            repositoryQueryStoreMap.put(value, query);
                            queryStoreNameList.add(name);
                            queryStoreValuesList.add(value);
                        }
                    }
                }
                queriesMap.put(connectionItem.getProperty().getId(), queryStoreValuesList);
            }
            repositoryTableNameList = (String[]) tableNamesList.toArray(new String[0]);
            repositoryTableValueList = (String[]) tableValuesList.toArray(new String[0]);
            repositoryQueryNameList = (String[]) queryStoreNameList.toArray(new String[0]);
            repositoryQueryValueList = (String[]) queryStoreValuesList.toArray(new String[0]);
        }
        initMaps();
        for (int i = 0; i < elem.getElementParameters().size(); i++) {
            IElementParameter param = elem.getElementParameters().get(i);
            if (param.getName().equals(EParameterName.REPOSITORY_SCHEMA_TYPE.getName())) {
                param.setListItemsDisplayName(repositoryTableNameList);
                param.setListItemsValue(repositoryTableValueList);
                if (!repositoryTableMap.keySet().contains(param.getValue())) {
                    List<String> list2 = tablesMap.get(elem.getPropertyValue(EParameterName.REPOSITORY_PROPERTY_TYPE.getName()));
                    boolean isNeeded = list2 != null && !list2.isEmpty();
                    if (repositoryTableNameList.length > 0 && repositoryConnectionValueList.length > 0 && isNeeded) {
                        elem.setPropertyValue(EParameterName.REPOSITORY_SCHEMA_TYPE.getName(), getDefaultRepository(true,
                                repositoryConnectionValueList[0]));
                    }
                }
            }
            if (param.getName().equals(EParameterName.REPOSITORY_QUERYSTORE_TYPE.getName())) {
                param.setListItemsDisplayName(repositoryQueryNameList);
                param.setListItemsValue(repositoryQueryValueList);
                if (!repositoryQueryStoreMap.keySet().contains(param.getValue())) {
                    List<String> list2 = queriesMap.get(elem.getPropertyValue(EParameterName.REPOSITORY_PROPERTY_TYPE.getName()));
                    boolean isNeeded = list2 != null && !list2.isEmpty();
                    if (repositoryQueryNameList.length > 0 && repositoryConnectionValueList.length > 0 && isNeeded) {
                        elem.setPropertyValue(EParameterName.REPOSITORY_QUERYSTORE_TYPE.getName(), getDefaultRepository(false,
                                repositoryConnectionValueList[0]));
                    }
                }
            }
            if (param.getName().equals(EParameterName.REPOSITORY_PROPERTY_TYPE.getName())) {
                String repositoryValue = elem.getElementParameter(EParameterName.PROPERTY_TYPE.getName()).getRepositoryValue();
                if (repositoryValue != null) {
                    List<String> connectionNamesList = new ArrayList<String>();
                    List<String> connectionValuesList = new ArrayList<String>();
                    for (String key : repositoryConnectionItemMap.keySet()) {
                        ConnectionItem connectionItem = repositoryConnectionItemMap.get(key);
                        Connection connection = (Connection) connectionItem.getConnection();
                        String name = getRepositoryAliasName(connectionItem) + ":" //$NON-NLS-1$
                                + connectionItem.getProperty().getLabel();
                        if ((connection instanceof DelimitedFileConnection) && (repositoryValue.equals("DELIMITED"))) { //$NON-NLS-1$
                            connectionNamesList.add(name);
                            connectionValuesList.add(key);
                        }
                        if ((connection instanceof PositionalFileConnection) && (repositoryValue.equals("POSITIONAL"))) { //$NON-NLS-1$
                            connectionNamesList.add(name);
                            connectionValuesList.add(key);
                        }
                        if ((connection instanceof RegexpFileConnection) && (repositoryValue.equals("REGEX"))) { //$NON-NLS-1$
                            connectionNamesList.add(name);
                            connectionValuesList.add(key);
                        }
                        if ((connection instanceof XmlFileConnection) && (repositoryValue.equals("XML"))) { //$NON-NLS-1$
                            connectionNamesList.add(name);
                            connectionValuesList.add(key);
                        }
                        if ((connection instanceof DatabaseConnection) && (repositoryValue.startsWith("DATABASE"))) { //$NON-NLS-1$
                            String currentDbType = (String) RepositoryToComponentProperty.getValue(connection, "TYPE"); //$NON-NLS-1$
                            if (repositoryValue.contains(":")) { // database is specified //$NON-NLS-1$
                                String neededDbType = repositoryValue.substring(repositoryValue.indexOf(":") + 1); //$NON-NLS-1$
                                if (neededDbType.equals(currentDbType)) {
                                    connectionNamesList.add(name);
                                    connectionValuesList.add(key);
                                }
                            } else {
                                connectionNamesList.add(name);
                                connectionValuesList.add(key);
                            }
                        }
                    }
                    if (!repositoryPropType.equals(elem.getPropertyValue(EParameterName.REPOSITORY_PROPERTY_TYPE.getName()))) {
                        if (repositoryTableNameList.length > 0) {
                            List<String> list2 = tablesMap.get(elem.getPropertyValue(EParameterName.REPOSITORY_PROPERTY_TYPE
                                    .getName()));
                            if (list2 != null && !list2.isEmpty()) {
                                elem.setPropertyValue(EParameterName.REPOSITORY_SCHEMA_TYPE.getName(), getDefaultRepository(true,
                                        null));
                            }
                        }
                        if (repositoryQueryNameList.length > 0) {
                            List<String> list2 = queriesMap.get(elem.getPropertyValue(EParameterName.REPOSITORY_PROPERTY_TYPE
                                    .getName()));
                            if (list2 != null && !list2.isEmpty()) {
                                elem.setPropertyValue(EParameterName.REPOSITORY_QUERYSTORE_TYPE.getName(), getDefaultRepository(
                                        false, null));
                            }
                        }
                    }

                    repositoryConnectionNameList = (String[]) connectionNamesList.toArray(new String[0]);
                    repositoryConnectionValueList = (String[]) connectionValuesList.toArray(new String[0]);
                } else {
                    List<String> connectionValuesList = new ArrayList<String>();
                    List<String> connectionStringList = new ArrayList<String>();
                    for (String key : repositoryConnectionItemMap.keySet()) {
                        ConnectionItem connectionItem = repositoryConnectionItemMap.get(key);
                        connectionStringList.add(connectionItem.getProperty().getLabel());
                        connectionValuesList.add(key);
                    }
                    repositoryConnectionNameList = (String[]) connectionStringList.toArray(new String[0]);
                    repositoryConnectionValueList = (String[]) connectionValuesList.toArray(new String[0]);
                }
                param.setListItemsDisplayName(repositoryConnectionNameList);
                param.setListItemsValue(repositoryConnectionValueList);
                if (!repositoryConnectionItemMap.keySet().contains(param.getValue())) {
                    if (repositoryConnectionNameList.length > 0) {
                        elem
                                .setPropertyValue(EParameterName.REPOSITORY_PROPERTY_TYPE.getName(),
                                        repositoryConnectionValueList[0]);
                    }
                }
            }
        }
        updateQuery();
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

    /**
     * Initialize all components for the defined section for this node.
     */
    public void addComponents() {
        registerListenerForRefreshingSection();
        checkErrorsWhenViewRefreshed = true;

        int heightSize = 0, maxRowSize = 0, nbInRow, numInRow;
        int maxRow;
        List<? extends IElementParameter> listParam = elem.getElementParameters();

        // oldSchemaType = (String) elem.getPropertyValue(EParameterName.SCHEMA_TYPE.getName());
        // if (oldSchemaType != null) {
        // if (oldSchemaType.equals(EmfComponent.REPOSITORY)) {
        // showSchemaRepositoryList(true);
        // updateRepositoryList();
        // } else {
        // showSchemaRepositoryList(false);
        // }
        // }

        oldQueryStoreType = (String) elem.getPropertyValue(EParameterName.QUERYSTORE_TYPE.getName());
        if (oldQueryStoreType != null) {
            if (oldQueryStoreType.equals(EmfComponent.REPOSITORY)) {
                showQueryStoreRepositoryList(true);
                updateRepositoryList();
            } else {
                showQueryStoreRepositoryList(false);
            }
        }

        oldPropertyType = (String) elem.getPropertyValue(EParameterName.PROPERTY_TYPE.getName());
        if (oldPropertyType != null) {
            if (oldPropertyType.equals(EmfComponent.REPOSITORY)) {
                showPropertyRepositoryList(true);
                updateRepositoryList();
            } else {
                showPropertyRepositoryList(false);
            }
        }

        oldProcessType = (String) elem.getPropertyValue(EParameterName.PROCESS_TYPE_PROCESS.getName());
        if (oldProcessType != null) {
            String[] list = elem.getElementParameter(EParameterName.PROCESS_TYPE_PROCESS.getName()).getListItemsDisplayName();
            if ((oldProcessType.equals("NO_PROCESS") || (list.length == 0))) { //$NON-NLS-1$
                updateProcessList();
                updateContextList();
                if (elem instanceof Node) {
                    ((Node) elem).checkAndRefreshNode();
                }
            }
        }

        Control lastControl = null;

        if (currentComponent != null) {
            Control[] ct = composite.getChildren();
            for (int i = 0; i < ct.length; i++) {
                ct[i].dispose();
            }
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

        curRowSize = 0;
        for (int curRow = 1; curRow <= maxRow; curRow++) {
            maxRowSize = 0;
            nbInRow = 0;
            for (int i = 0; i < listParam.size(); i++) {
                if (listParam.get(i).getCategory() == section) {
                    if (listParam.get(i).getNumRow() == curRow && listParam.get(i).isShow(listParam)) {
                        nbInRow++;
                    }
                }
            }
            numInRow = 0;
            lastControl = null;
            for (int i = 0; i < listParam.size(); i++) {
                if (listParam.get(i).getCategory() == section) {
                    if (listParam.get(i).getNumRow() == curRow && listParam.get(i).isShow(listParam)) {
                        numInRow++;

                        generator.initController();
                        AbstractElementPropertySectionController controller = generator.getController(
                                listParam.get(i).getField(), this);

                        if (controller == null) {
                            break;
                        }
                        lastControl = controller.createControl(composite, listParam.get(i), numInRow, nbInRow, heightSize,
                                lastControl);

                        if (curRowSize > maxRowSize) {
                            maxRowSize = curRowSize;
                        }
                    }
                }
            }
            heightSize += maxRowSize;
        }
        composite.pack();
    }

    /*
     * (non-Java)
     * 
     * @see org.eclipse.ui.views.properties.tabbed.AbstractPropertySection#
     * createControls(org.eclipse.swt.widgets.Composite, org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage)
     */
    public void createControls(final Composite parent, final TabbedPropertySheetPage aTabbedPropertySheetPage) {
        super.createControls(parent, aTabbedPropertySheetPage);

        composite = parent;

        FormLayout layout = new FormLayout();
        layout.marginWidth = ITabbedPropertyConstants.HSPACE + 2;
        layout.marginHeight = ITabbedPropertyConstants.VSPACE;
        layout.spacing = ITabbedPropertyConstants.VMARGIN + 1;
        composite.setLayout(layout);
    }

    public void refresh() {
        if (elem == null) {
            return;
        }
        List<? extends IElementParameter> listParam = elem.getElementParameters();

        if (oldProcessType != null) {
            String newProcessType = (String) elem.getPropertyValue(EParameterName.PROCESS_TYPE_PROCESS.getName());
            if (!oldProcessType.equals(newProcessType)) {
                updateProcessList();
                updateContextList();
                if (elem instanceof Node) {
                    ((Node) elem).checkAndRefreshNode();
                }
            }
        }

        Boolean updateNeeded = (Boolean) elem.getPropertyValue(EParameterName.UPDATE_COMPONENTS.getName());
        if (updateNeeded != null) {
            if (updateNeeded) {
                addComponents();
                elem.setPropertyValue(EParameterName.UPDATE_COMPONENTS.getName(), new Boolean(false));
            }
        }

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
        composite.getParent().layout(true, true);
        checkErrorsWhenViewRefreshed = false;
    }

    // private ISelection lastSelection;
    /*
     * @Override (non-Java)
     * 
     * @see org.eclipse.ui.views.properties.tabbed.AbstractPropertySection# setInput(org.eclipse.ui.IWorkbenchPart,
     * org.eclipse.jface.viewers.ISelection)
     */
    public void setInput(final IWorkbenchPart workbenchPart, final ISelection selection) {
        if (!(selection instanceof IStructuredSelection)) {
            return;
        }
        // if (lastSelection != null) { // added to correct a bug of the TabbedProperties
        // if (lastSelection.equals(selection)) {
        // return;
        // }
        // }
        // lastSelection = selection;

        if (workbenchPart instanceof MultiPageTalendEditor) {
            part = (MultiPageTalendEditor) workbenchPart;
        } else {
            part = (MultiPageTalendEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        }
        super.setInput(part, selection);
        Assert.isTrue(selection instanceof IStructuredSelection);
        Object input = ((IStructuredSelection) selection).getFirstElement();
        if (input instanceof NodeContainerPart) {
            NodeContainerPart nContainer = (NodeContainerPart) input;
            elem = (Element) nContainer.getParent().getModel();
        } else if (input instanceof NodeTreeEditPart) {
            NodeTreeEditPart nTreePart = (NodeTreeEditPart) input;
            elem = (Element) nTreePart.getModel();
        } else if (!(input instanceof NodeReturnsTreeEditPart)) {
            Assert.isTrue(input instanceof EditPart);
            EditPart editPart = (EditPart) input;
            elem = (Element) editPart.getModel();
        }
        if (elem instanceof NodeLabel) {
            elem = ((NodeLabel) elem).getNode();
        }

        if (elem instanceof ConnectionLabel) {
            elem = ((ConnectionLabel) elem).getConnection();
        }

        if (currentComponent == null || elem instanceof org.talend.designer.core.ui.editor.connections.Connection) {
            addComponents();
        } else {
            if (!currentComponent.equals(elem.getElementName())) {
                addComponents();
            } else {
                updateRepositoryList();
                updateProcessList();
                updateContextList();
                refresh();
            }
        }
        currentComponent = elem.getElementName();
    }

    /**
     * Set the section of the tabbed property.
     * 
     * @param section
     */
    public DynamicTabbedPropertySection(final EComponentCategory section) {
        super();
        this.section = section;
        // currentLanguage = ((RepositoryContext) CorePlugin.getContext().getProperty(Context.REPOSITORY_CONTEXT_KEY))
        // .getProject().getLanguage();
        repositoryQueryStoreMap = new HashMap<String, Query>();
        repositoryConnectionItemMap = new HashMap<String, ConnectionItem>();
        repositoryTableMap = new HashMap<String, IMetadataTable>();
    }

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
        return this.repositoryConnectionItemMap;
    }

    /**
     * Getter for composite.
     * 
     * @return the composite
     */
    public Composite getComposite() {
        return this.composite;
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
    public MultiPageTalendEditor getPart() {
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
        return repositoryQueryStoreMap;
    }

    /**
     * yzhang Comment method "updateColumnList".
     */
    @SuppressWarnings("unchecked")//$NON-NLS-1$
    public void updateColumnList(List<ColumnNameChanged> columnsChanged) {
        List<String> columnList = getColumnList();
        List<String> prevColumnList = getPrevColumnList();
        List<String> curColumnList = null;

        String[] columnNameList = (String[]) columnList.toArray(new String[0]);
        String[] prevColumnNameList = (String[]) prevColumnList.toArray(new String[0]);
        String[] curColumnNameList = null;
        for (int i = 0; i < elem.getElementParameters().size(); i++) {
            IElementParameter param = elem.getElementParameters().get(i);
            if (param.getField() == EParameterFieldType.COLUMN_LIST) {
                curColumnList = columnList;
                curColumnNameList = columnNameList;
            }
            if (param.getField() == EParameterFieldType.PREV_COLUMN_LIST) {
                curColumnList = prevColumnList;
                curColumnNameList = prevColumnNameList;
            }
            if (param.getField() == EParameterFieldType.COLUMN_LIST || param.getField() == EParameterFieldType.PREV_COLUMN_LIST) {
                param.setListItemsDisplayName(curColumnNameList);
                param.setListItemsValue(curColumnNameList);
                CCombo combo = (CCombo) hashCurControls.get(param.getName());
                combo.setItems(curColumnNameList);
                if (!curColumnList.contains(param.getValue())) {
                    if (curColumnNameList.length > 0) {
                        elem.setPropertyValue(param.getName(), curColumnNameList[0]);
                        combo.setText(curColumnNameList[0]);
                    }
                } else {
                    combo.setText((String) param.getValue());
                }
            }
            if (param.getField() == EParameterFieldType.TABLE) {
                TableViewerCreator tableViewerCreator = (TableViewerCreator) hashCurControls.get(param.getName());
                Object[] itemsValue = (Object[]) param.getListItemsValue();
                List colList = tableViewerCreator.getColumns();
                for (int j = 0; j < itemsValue.length; j++) {
                    if (itemsValue[j] instanceof IElementParameter) {
                        IElementParameter tmpParam = (IElementParameter) itemsValue[j];
                        if (tmpParam.getField() == EParameterFieldType.COLUMN_LIST) {
                            curColumnList = columnList;
                            curColumnNameList = columnNameList;
                        }
                        if (tmpParam.getField() == EParameterFieldType.PREV_COLUMN_LIST) {
                            curColumnList = prevColumnList;
                            curColumnNameList = prevColumnNameList;
                        }
                        if (tmpParam.getField() == EParameterFieldType.COLUMN_LIST
                                || tmpParam.getField() == EParameterFieldType.PREV_COLUMN_LIST) {
                            tmpParam.setListItemsDisplayCodeName(curColumnNameList);
                            tmpParam.setListItemsDisplayName(curColumnNameList);
                            tmpParam.setListItemsValue(curColumnNameList);
                            if (curColumnNameList.length > 0) {
                                tmpParam.setDefaultClosedListValue(curColumnNameList[0]);
                            } else {
                                tmpParam.setDefaultClosedListValue(""); //$NON-NLS-1$
                            }
                            // j + 1 because first column is masked
                            TableViewerCreatorColumn column = (TableViewerCreatorColumn) colList.get(j + 1);

                            CCombo combo = (CCombo) column.getCellEditor().getControl();
                            String[] oldItems = combo.getItems();
                            combo.setItems(curColumnNameList);

                            List<Map<String, Object>> paramValues = (List<Map<String, Object>>) param.getValue();
                            String[] items = param.getListItemsDisplayCodeName();

                            for (int currentIndex = 0; currentIndex < paramValues.size(); currentIndex++) {
                                Map<String, Object> currentLine = paramValues.get(currentIndex);
                                Object o = currentLine.get(items[j]);
                                if (o instanceof Integer) {
                                    Integer nb = (Integer) o;
                                    if ((nb >= oldItems.length) || (nb == -1)) {
                                        nb = new Integer(tmpParam.getIndexOfItemFromList((String) tmpParam
                                                .getDefaultClosedListValue()));
                                        currentLine.put(items[j], nb);
                                    } else {
                                        nb = new Integer(tmpParam.getIndexOfItemFromList(oldItems[nb]));
                                        currentLine.put(items[j], nb);
                                    }
                                }
                            }
                        }
                    }
                }
                if (param.isBasedOnSchema()) {
                    List<Map<String, Object>> paramValues = (List<Map<String, Object>>) param.getValue();
                    List<Map<String, Object>> newParamValues = new ArrayList<Map<String, Object>>();
                    for (int j = 0; j < columnNameList.length; j++) {
                        String columnName = columnNameList[j];
                        String[] codes = param.getListItemsDisplayCodeName();

                        Map<String, Object> newLine = null;
                        boolean found = false;
                        ColumnNameChanged colChanged = null;
                        if (columnsChanged != null) {
                            for (int k = 0; k < columnsChanged.size() && !found; k++) {
                                colChanged = columnsChanged.get(k);
                                if (colChanged.getNewName().equals(columnName)) {
                                    found = true;
                                }
                            }
                        }
                        if (found) {
                            found = false;
                            for (int k = 0; k < paramValues.size() && !found; k++) {
                                Map<String, Object> currentLine = (Map<String, Object>) paramValues.get(k);
                                if (currentLine.get(codes[0]).equals(colChanged.getOldName())) {
                                    currentLine.put(codes[0], colChanged.getNewName());
                                    found = true;
                                }
                            }
                        }
                        found = false;
                        for (int k = 0; k < paramValues.size() && !found; k++) {
                            Map<String, Object> currentLine = (Map<String, Object>) paramValues.get(k);
                            if (currentLine.get(codes[0]) == null) {
                                currentLine.put(codes[0], columnName);
                            }
                            if (currentLine.get(codes[0]).equals(columnName)) {
                                found = true;
                                newLine = currentLine;
                            }
                        }
                        if (!found) {
                            newLine = createNewLine(param);
                            newLine.put(codes[0], columnName);
                        }
                        newParamValues.add(j, newLine);
                    }
                    paramValues.clear();
                    paramValues.addAll(newParamValues);

                    if (param.isRepositoryValueUsed() && param.getRepositoryValue().equals("XML_MAPPING")) { //$NON-NLS-1$
                        String connectionSelected = (String) elem.getPropertyValue(EParameterName.REPOSITORY_PROPERTY_TYPE
                                .getName());
                        List<Map<String, Object>> table = (List<Map<String, Object>>) elem.getPropertyValue(param.getName());
                        IMetadataTable metaTable = ((Node) elem).getMetadataList().get(0);
                        RepositoryToComponentProperty.getTableXmlFileValue(

                        this.repositoryConnectionItemMap.get(connectionSelected).getConnection(), "XML_MAPPING", param, //$NON-NLS-1$
                                table, metaTable);
                    }
                }
                tableViewerCreator.getTableViewer().refresh();
            }
        }
    }

    /**
     * yzhang Comment method "getColumnList".
     * 
     * @return
     */
    public List<String> getColumnList() {
        List<String> columnList = new ArrayList<String>();

        if (elem instanceof Node) {
            Node node = (Node) elem;
            IMetadataTable table = node.getMetadataList().get(0);
            for (IMetadataColumn column : table.getListColumns()) {
                columnList.add(column.getLabel());
            }
        }

        return columnList;
    }

    /**
     * yzhang Comment method "getPrevColumnList".
     * 
     * @return
     */
    public List<String> getPrevColumnList() {
        List<String> columnList = new ArrayList<String>();

        if (elem instanceof Node) {
            Node node = (Node) elem;
            IConnection connection = null;
            boolean found = false;
            for (int i = 0; i < node.getIncomingConnections().size() && !found; i++) {
                IConnection curConnec = node.getIncomingConnections().get(i);
                if (curConnec.getLineStyle() == EConnectionType.FLOW_MAIN) {
                    connection = curConnec;
                    found = true;
                }
            }
            if (connection != null) {
                IMetadataTable table = connection.getMetadataTable();
                for (IMetadataColumn column : table.getListColumns()) {
                    columnList.add(column.getLabel());
                }
            }
        }

        return columnList;
    }

    /**
     * yzhang Comment method "createNewLine".
     * 
     * @param param
     * @return
     */
    protected Map<String, Object> createNewLine(IElementParameter param) {
        Map<String, Object> line = new HashMap<String, Object>();
        String[] items = (String[]) param.getListItemsDisplayCodeName();
        Object[] itemsValue = (Object[]) param.getListItemsValue();
        IElementParameter tmpParam;

        tmpParam = (IElementParameter) itemsValue[0];
        switch (tmpParam.getField()) {
        case CLOSED_LIST:
        case COLUMN_LIST:
        case PREV_COLUMN_LIST:
            line.put(items[0], new Integer(tmpParam.getIndexOfItemFromList((String) tmpParam.getDefaultClosedListValue())));
            break;
        case CHECK:
            line.put(items[0], tmpParam.getValue());
            break;
        default: // TEXT
            if ((tmpParam.getValue() == null) || (tmpParam.getValue().equals(""))) { //$NON-NLS-1$
                line.put(items[0], new String(TalendTextUtils.addQuotes("newLine"))); //$NON-NLS-1$
            } else {
                line.put(items[0], tmpParam.getValue());
            }
        }

        for (int i = 1; i < items.length; i++) {
            tmpParam = (IElementParameter) itemsValue[i];
            switch (tmpParam.getField()) {
            case CLOSED_LIST:
            case COLUMN_LIST:
            case PREV_COLUMN_LIST:
                line.put(items[i], new Integer(tmpParam.getIndexOfItemFromList((String) tmpParam.getDefaultClosedListValue())));
                break;
            default: // TEXT or CHECK (means String or Boolean)
                line.put(items[i], tmpParam.getValue());
            }
        }
        return line;
    }

    /**
     * Get the command stack of the Gef editor.
     * 
     * @return
     */
    protected CommandStack getCommandStack() {
        TalendEditor talendEditor = part.getTalendEditor();
        Object adapter = talendEditor.getAdapter(CommandStack.class);
        return (CommandStack) adapter;
    }

    /**
     * amaumont Comment method "registerListenerForRefreshingSection".
     */
    private void registerListenerForRefreshingSection() {
        if (commandStackEventListener == null) {

            DynamicTabbedPropertySection.commandStackEventListener = new CommandStackEventListener() {

                public void stackChanged(CommandStackEvent event) {
                    int detail = event.getDetail();
                    if (0 != (detail & CommandStack.POST_UNDO) || 0 != (detail & CommandStack.POST_REDO)) {
                        if (event.getCommand() instanceof IExtendedTableCommand) {

                            IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
                            IViewPart view = page.findView("org.eclipse.ui.views.PropertySheet"); //$NON-NLS-1$
                            PropertySheet sheet = (PropertySheet) view;
                            TabbedPropertySheetPage tabbedPropertySheetPage = (TabbedPropertySheetPage) sheet.getCurrentPage();
                            tabbedPropertySheetPage.refresh();

                        }
                    }

                }

            };
            getCommandStack().addCommandStackEventListener(DynamicTabbedPropertySection.commandStackEventListener);
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

    private String repositoryPropType = "";

    /**
     * qzhang Comment method "getDefaultRepository".
     * 
     * @return
     */
    private String getDefaultRepository(boolean istable, String defaultPropertyValue) {
        Object propertyValue = elem.getPropertyValue(EParameterName.REPOSITORY_PROPERTY_TYPE.getName());
        if ((propertyValue == null || !(propertyValue instanceof String)) && defaultPropertyValue != null) {
            propertyValue = defaultPropertyValue;
        }
        if (propertyValue == null || propertyValue.equals("")) {
            return "";
        }
        repositoryPropType = (String) propertyValue;
        if (istable) {
            if (tablesMap.get(propertyValue).size() > 0) {
                return tablesMap.get(propertyValue).get(0);
            }
        } else {
            if (queriesMap.get(propertyValue).size() > 0) {
                return queriesMap.get(propertyValue).get(0);
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

}
