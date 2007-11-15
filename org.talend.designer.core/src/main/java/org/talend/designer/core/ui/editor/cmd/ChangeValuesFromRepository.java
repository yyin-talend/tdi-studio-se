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
package org.talend.designer.core.ui.editor.cmd;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.talend.core.model.components.IODataComponent;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.designerproperties.RepositoryToComponentProperty;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.Element;
import org.talend.core.model.process.IConnectionCategory;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.model.components.EmfComponent;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.process.Process;
import org.talend.designer.core.ui.views.statsandlogs.StatsAndLogsView;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class ChangeValuesFromRepository extends ChangeMetadataCommand {

    private final Map<String, Object> oldValues;

    private final Element elem;

    private final Connection connection;

    private final String value;

    private final String propertyName;

    private String oldMetadata;

    private Map<String, List<String>> tablesmap;

    private Map<String, List<String>> queriesmap;

    private Map<String, IMetadataTable> repositoryTableMap;

    public ChangeValuesFromRepository(Element elem, Connection connection, String propertyName, String value) {
        this.elem = elem;
        this.connection = connection;
        this.value = value;
        this.propertyName = propertyName;
        oldValues = new HashMap<String, Object>();

        setLabel(Messages.getString("PropertyChangeCommand.Label")); //$NON-NLS-1$
    }

    @Override
    public void execute() {
        // Force redraw of Commponents propoerties
        // elem.setPropertyValue(EParameterName.UPDATE_COMPONENTS.getName(), new
        // Boolean(true));

        boolean allowAutoSwitch = true;
        IElementParameter elemParam = elem.getElementParameter(EParameterName.REPOSITORY_ALLOW_AUTO_SWITCH.getName());
        if (elemParam != null) {
            allowAutoSwitch = (Boolean) elemParam.getValue();
        }
        if (!allowAutoSwitch && (elem instanceof Node)) {
            // force the autoSwitch to true if the schema is empty and if the
            // query is not set.
            Node node = (Node) elem;
            boolean isSchemaEmpty = node.getMetadataList().get(0).getListColumns().size() == 0;
            boolean isQueryEmpty = false;
            for (IElementParameter curParam : node.getElementParameters()) {
                if (curParam.getField().equals(EParameterFieldType.MEMO_SQL)) {
                    String defaultValue = "";
                    if (curParam.getDefaultValues().size() > 0) {
                        defaultValue = (String) curParam.getDefaultValues().get(0).getDefaultValue();
                    }
                    String paramValue = (String) curParam.getValue();
                    isQueryEmpty = paramValue.equals("") || paramValue.equals("''") || paramValue.equals("\"\"")
                            || paramValue.equals(defaultValue);
                }
            }
            if (isSchemaEmpty) {
                allowAutoSwitch = true;
            }
        }
        if (propertyName.equals(EParameterName.PROPERTY_TYPE.getName())) {
            elem.setPropertyValue(EParameterName.PROPERTY_TYPE.getName(), value);
            if (allowAutoSwitch) {
                setOtherProperties();
            }
        } else {
            oldMetadata = (String) elem.getPropertyValue(EParameterName.REPOSITORY_PROPERTY_TYPE.getName());
            elem.setPropertyValue(EParameterName.REPOSITORY_PROPERTY_TYPE.getName(), value);
            if (allowAutoSwitch) {
                setOtherProperties();
            }
        }

        // Node node = (Node) elem;
        // String schemTypeModel = (String) node.getElementParameter(
        // EParameterName.SCHEMA_TYPE.getName()).getValue();
        // boolean repositoryModel = true;
        // if (EmfComponent.BUILTIN.equals(schemTypeModel) &&
        // EmfComponent.BUILTIN.equals(value)) {
        // repositoryModel = false;
        // }
        if (propertyName.equals(EParameterName.PROPERTY_TYPE.getName()) && (EmfComponent.BUILTIN.equals(value))) {
            for (IElementParameter param : elem.getElementParameters()) {
                param.setRepositoryValueUsed(false);
            }
        } else {
            oldValues.clear();
            for (IElementParameter param : elem.getElementParameters()) {
                String repositoryValue = param.getRepositoryValue();
                if (param.isShow(elem.getElementParameters()) && (repositoryValue != null)
                        && (!param.getName().equals(EParameterName.PROPERTY_TYPE.getName()))) {
                    Object objectValue = RepositoryToComponentProperty.getValue(connection, repositoryValue);
                    if (objectValue != null) {
                        oldValues.put(param.getName(), param.getValue());

                        if (param.getField().equals(EParameterFieldType.CLOSED_LIST) && param.getRepositoryValue().equals("TYPE")) {
                            boolean found = false;
                            String[] list = param.getListRepositoryItems();
                            for (int i = 0; (i < list.length) && (!found); i++) {
                                if (objectValue.equals(list[i])) {
                                    found = true;
                                    elem.setPropertyValue(param.getName(), param.getListItemsValue()[i]);
                                }
                            }
                        } else {
                            if (repositoryValue.equals("ENCODING")) {
                                IElementParameter paramEncoding = param.getChildParameters().get(
                                        EParameterName.ENCODING_TYPE.getName());
                                paramEncoding.setValue(EmfComponent.ENCODING_TYPE_CUSTOM);
                            }
                            elem.setPropertyValue(param.getName(), objectValue);
                        }
                        param.setRepositoryValueUsed(true);
                    } else if (param.getField().equals(EParameterFieldType.TABLE)
                            && param.getRepositoryValue().equals("XML_MAPPING")) { //$NON-NLS-1$

                        List<Map<String, Object>> table = (List<Map<String, Object>>) elem.getPropertyValue(param.getName());
                        IMetadataTable metaTable = ((Node) elem).getMetadataList().get(0);
                        RepositoryToComponentProperty.getTableXmlFileValue(connection, "XML_MAPPING", param, //$NON-NLS-1$
                                table, metaTable);
                        param.setRepositoryValueUsed(true);
                    }

                }
            }
        }

        refreshPropertyView();
        refreshStatsAndLogsView();
        
        if (elem instanceof Node) {
            ((Process) ((Node) elem).getProcess()).checkProcess();
        }
    }
    
    /**
     * ftang Comment method "refreshStatsAndLogsView".
     */
    private void refreshStatsAndLogsView() {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IViewPart view = page.findView(StatsAndLogsView.ID); 
       if (view != null) {
            StatsAndLogsView statsAndLogsView = (StatsAndLogsView) view;
            statsAndLogsView.refreshView();
        }
    }

    private String getFirstRepositoryTable(IElementParameter schemaParam, String repository) {
        IElementParameter repositorySchemaTypeParameter = schemaParam.getChildParameters().get(
                EParameterName.REPOSITORY_SCHEMA_TYPE.getName());
        String[] listId = (String[]) repositorySchemaTypeParameter.getListItemsValue();
        for (int i = 0; i < listId.length; i++) {
            if (listId[i].startsWith(repository)) {
                return listId[i];
            }
        }
        return "";
    }

    /**
     * qzhang Comment method "setOtherProperties".
     */
    private void setOtherProperties() {
        boolean metadataInput = false;
        if (elem instanceof Node) {
            Node node = (Node) elem;
            if (node.getCurrentActiveLinksNbInput(EConnectionType.FLOW_MAIN) > 0
                    || node.getCurrentActiveLinksNbInput(EConnectionType.FLOW_REF) > 0
                    || node.getCurrentActiveLinksNbInput(EConnectionType.TABLE) > 0) {
                metadataInput = true;
            }

            boolean hasSchema = elem.getElementParameterFromField(EParameterFieldType.SCHEMA_TYPE) != null;
            if (value.equals(EmfComponent.BUILTIN)) {
                if (!metadataInput && hasSchema) {
                    elem.setPropertyValue(EParameterName.SCHEMA_TYPE.getName(), value);
                }
                elem.setPropertyValue(EParameterName.QUERYSTORE_TYPE.getName(), value);
            } else {
                if (hasSchema) {
                    for (IElementParameter param : elem.getElementParameters()) {
                        if (param.getField().equals(EParameterFieldType.SCHEMA_TYPE)) {
                            if (!metadataInput) {
                                IElementParameter repositorySchemaTypeParameter = param.getChildParameters().get(
                                        EParameterName.REPOSITORY_SCHEMA_TYPE.getName());
                                String repositoryTable;
                                if (propertyName.equals(EParameterName.PROPERTY_TYPE.getName())) {
                                    repositoryTable = (String) repositorySchemaTypeParameter.getValue();
                                } else {
                                    repositoryTable = getFirstRepositoryTable(param, value);
                                    repositorySchemaTypeParameter.setValue(repositoryTable);
                                }
                                if (!"".equals(repositoryTable)) {
                                    param.getChildParameters().get(EParameterName.SCHEMA_TYPE.getName()).setValue(
                                            EmfComponent.REPOSITORY);

                                    IMetadataTable table = repositoryTableMap.get(repositoryTable);
                                    if (table != null) {
                                        table = table.clone();
                                        setDBTableFieldValue(node, table.getTableName(), null);
                                        table.setTableName(node.getMetadataList().get(0).getTableName());
                                        if (!table.sameMetadataAs(node.getMetadataList().get(0))) {
                                            ChangeMetadataCommand cmd = new ChangeMetadataCommand(node, param, null, table);
                                            cmd.setRepositoryMode(true);
                                            cmd.execute(true);
                                        }
                                    }
                                }
                            } else {
                                Node sourceNode = getRealSourceNode((INode) elem);
                                if (sourceNode != null) {
                                    IMetadataTable sourceMetadataTable = sourceNode.getMetadataList().get(0);
                                    Object sourceSchema = sourceNode.getPropertyValue(EParameterName.SCHEMA_TYPE.getName());
                                    boolean isTake = !sourceNode.isExternalNode() && sourceSchema != null
                                            && elem.getPropertyValue(EParameterName.SCHEMA_TYPE.getName()) != null;
                                    if (isTake && getTake()) {
                                        ChangeMetadataCommand cmd = new ChangeMetadataCommand((Node) elem, param, null,
                                                sourceMetadataTable);
                                        cmd.execute(true);
                                        elem.setPropertyValue(EParameterName.SCHEMA_TYPE.getName(), sourceSchema);
                                        if (sourceSchema.equals(EmfComponent.REPOSITORY)) {
                                            elem.setPropertyValue(EParameterName.REPOSITORY_SCHEMA_TYPE.getName(), sourceNode
                                                    .getPropertyValue(EParameterName.REPOSITORY_SCHEMA_TYPE.getName()));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (propertyName.equals(EParameterName.PROPERTY_TYPE.getName())) {
                if (queriesmap != null
                        && !queriesmap.get(elem.getPropertyValue(EParameterName.REPOSITORY_PROPERTY_TYPE.getName())).isEmpty()) {
                    elem.setPropertyValue(EParameterName.QUERYSTORE_TYPE.getName(), value);
                }
            } else {
                elem.setPropertyValue(EParameterName.QUERYSTORE_TYPE.getName(), EmfComponent.REPOSITORY);
                if (queriesmap == null || queriesmap.get(value).isEmpty()) {
                    elem.setPropertyValue(EParameterName.QUERYSTORE_TYPE.getName(), EmfComponent.BUILTIN);
                }
                if (tablesmap == null || tablesmap.get(value).isEmpty()) {
                    elem.setPropertyValue(EParameterName.SCHEMA_TYPE.getName(), EmfComponent.BUILTIN);
                }
            }

        }
    }

    @SuppressWarnings("unchecked")
    protected Node getRealSourceNode(INode target) {
        Node sourceNode = null;
        IODataComponent input = null;
        List<org.talend.designer.core.ui.editor.connections.Connection> incomingConnections = null;
        incomingConnections = (List<org.talend.designer.core.ui.editor.connections.Connection>) target.getIncomingConnections();
        for (org.talend.designer.core.ui.editor.connections.Connection connec : incomingConnections) {
            if (connec.isActivate() && connec.getLineStyle().hasConnectionCategory(IConnectionCategory.DATA)) {
                input = new IODataComponent(connec);
            }
        }
        if (input != null) {
            INode source = input.getSource();
            if (source instanceof Node) {
                sourceNode = (Node) source;
                // final IExternalNode externalNode =
                // sourceNode.getExternalNode();
                // if (sourceNode.getPluginFullName() != null &&
                // !"".equals(sourceNode.getPluginFullName())
                // && sourceNode.getExternalNode() != null) {
                // return getRealSourceNode(externalNode);
                // }
            }
        }
        return sourceNode;
    }

    /**
     * qzhang Comment method "getTake".
     * 
     * @return
     */
    private Boolean take = null;

    private boolean getTake() {
        if (take == null) {
            take = MessageDialog.openQuestion(new Shell(), "", Messages
                    .getString("ChangeValuesFromRepository.messageDialog.takeMessage"));
        }
        return take;
    }

    @Override
    public void undo() {
        // Force redraw of Commponents propoerties
        elem.setPropertyValue(EParameterName.UPDATE_COMPONENTS.getName(), new Boolean(true));

        if (propertyName.equals(EParameterName.PROPERTY_TYPE.getName()) && (EmfComponent.BUILTIN.equals(value))) {
            for (IElementParameter param : elem.getElementParameters()) {
                String repositoryValue = param.getRepositoryValue();
                if (param.isShow(elem.getElementParameters()) && (repositoryValue != null)
                        && (!param.getName().equals(EParameterName.PROPERTY_TYPE.getName()))) {
                    param.setRepositoryValueUsed(true);
                }
            }
        } else {
            for (IElementParameter param : elem.getElementParameters()) {
                String repositoryValue = param.getRepositoryValue();
                if (param.isShow(elem.getElementParameters()) && (repositoryValue != null)) {
                    Object objectValue = RepositoryToComponentProperty.getValue(connection, repositoryValue);
                    if (objectValue != null) {
                        elem.setPropertyValue(param.getName(), oldValues.get(param.getName()));
                        param.setRepositoryValueUsed(false);
                    }
                }
            }
        }
        if (propertyName.equals(EParameterName.PROPERTY_TYPE.getName())) {
            if (value.equals(EmfComponent.BUILTIN)) {
                elem.setPropertyValue(EParameterName.PROPERTY_TYPE.getName(), EmfComponent.REPOSITORY);
            } else {
                elem.setPropertyValue(EParameterName.PROPERTY_TYPE.getName(), EmfComponent.BUILTIN);
                elem.setPropertyValue(EParameterName.SCHEMA_TYPE.getName(), EmfComponent.BUILTIN);
                elem.setPropertyValue(EParameterName.QUERYSTORE_TYPE.getName(), EmfComponent.BUILTIN);
            }
        } else {
            elem.setPropertyValue(EParameterName.REPOSITORY_PROPERTY_TYPE.getName(), oldMetadata);
        }

        refreshPropertyView();
    }

    /**
     * Sets a sets of maps.
     * 
     * @param tablesmap
     * @param queriesmap
     * @param repositoryTableMap
     */
    public void setMaps(Map<String, List<String>> tablesmap, Map<String, List<String>> queriesmap,
            Map<String, IMetadataTable> repositoryTableMap) {
        this.tablesmap = tablesmap;
        this.queriesmap = queriesmap;
        this.repositoryTableMap = repositoryTableMap;
    }
}
