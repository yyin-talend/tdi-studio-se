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
package org.talend.designer.core.ui.editor.update.cmd;

import java.util.List;
import java.util.Map;

import org.eclipse.gef.commands.Command;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.MetadataTool;
import org.talend.core.model.metadata.builder.connection.Query;
import org.talend.core.model.metadata.designerproperties.RepositoryToComponentProperty;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INodeConnector;
import org.talend.core.model.process.IProcess2;
import org.talend.core.model.update.EUpdateResult;
import org.talend.core.model.update.UpdateResult;
import org.talend.core.model.update.UpdatesConstants;
import org.talend.core.model.utils.TalendTextUtils;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.model.components.EmfComponent;
import org.talend.designer.core.ui.editor.cmd.PropertyChangeCommand;
import org.talend.designer.core.ui.editor.nodes.Node;

/**
 * ggu class global comment. Detailled comment
 * 
 */
public class UpdateNodeParameterCommand extends Command {

    private UpdateResult result;

    public UpdateNodeParameterCommand(UpdateResult result) {
        super();
        this.result = result;
    }

    @Override
    public void execute() {
        if (result == null) {
            return;
        }
        Object updateObject = result.getUpdateObject();
        if (updateObject == null) {
            return;
        }
        switch (result.getUpdateType()) {
        case NODE_PROPERTY:
            updateProperty();
            break;
        case NODE_SCHEMA:
            updateSchema();
            break;
        case NODE_QUERY:
            updateQuery();
            break;
        default:
            return;
        }

        if (updateObject instanceof Node) {
            Node node = (Node) updateObject;
            if (node.getProcess() instanceof IProcess2) {
                PropertyChangeCommand pcc = new PropertyChangeCommand(node, EParameterName.UPDATE_COMPONENTS.getName(),
                        Boolean.TRUE);
                ((IProcess2) node.getProcess()).getCommandStack().execute(pcc);
            }
        }
    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    private void updateProperty() {
        Object updateObject = result.getUpdateObject();
        if (updateObject == null) {
            return;
        }
        if (updateObject instanceof Node) { // opened job
            Node node = (Node) updateObject;

            boolean update = false;
            if (result.getResultType() == EUpdateResult.UPDATE) {
                // upgrade from repository
                if (result.isChecked()) {
                    for (IElementParameter param : node.getElementParameters()) {
                        String repositoryValue = param.getRepositoryValue();
                        if (param.isShow(node.getElementParameters()) && (repositoryValue != null)) {
                            if (param.getName().equals(EParameterName.PROPERTY_TYPE.getName())
                                    || param.getField() == EParameterFieldType.MEMO_SQL) {
                                continue;
                            }
                            Object objectValue = RepositoryToComponentProperty.getValue(
                                    (org.talend.core.model.metadata.builder.connection.Connection) result.getParameter(),
                                    repositoryValue);
                            if (objectValue != null) {
                                if (param.getField().equals(EParameterFieldType.CLOSED_LIST)
                                        && repositoryValue.equals(UpdatesConstants.TYPE)) {
                                    boolean found = false;
                                    String[] items = param.getListRepositoryItems();
                                    for (int i = 0; (i < items.length) && (!found); i++) {
                                        if (objectValue.equals(items[i])) {
                                            found = true;
                                            node.setPropertyValue(param.getName(), param.getListItemsValue()[i]);
                                        }
                                    }
                                } else {
                                    node.setPropertyValue(param.getName(), objectValue);
                                }
                            } else if (param.getField().equals(EParameterFieldType.TABLE)
                                    && UpdatesConstants.XML_MAPPING.equals(repositoryValue)) {
                                RepositoryToComponentProperty.getTableXMLMappingValue(
                                        (org.talend.core.model.metadata.builder.connection.Connection) result.getParameter(),
                                        (List<Map<String, Object>>) param.getValue(), node.getMetadataList().get(0));
                            }
                            param.setRepositoryValueUsed(true);
                            param.setReadOnly(true);
                            update = true;
                        }
                    }
                }
            }
            if (!update) { // bult-in
                node.setPropertyValue(EParameterName.PROPERTY_TYPE.getName(), EmfComponent.BUILTIN);
                for (IElementParameter param : node.getElementParameters()) {
                    String repositoryValue = param.getRepositoryValue();
                    if (param.isShow(node.getElementParameters()) && (repositoryValue != null)) {
                        if (param.getName().equals(EParameterName.PROPERTY_TYPE.getName())
                                || param.getField() == EParameterFieldType.MEMO_SQL) {
                            continue;
                        }
                        param.setRepositoryValueUsed(false);
                        param.setReadOnly(false);
                    }
                }
            }
        }
    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    private void updateSchema() {
        Object updateObject = result.getUpdateObject();
        if (updateObject == null) {
            return;
        }
        if (updateObject instanceof Node) { // opened job
            Node node = (Node) updateObject;

            boolean builtIn = true;

            if (result.getResultType() == EUpdateResult.UPDATE) {
                if (result.isChecked()) {
                    IMetadataTable newTable = (IMetadataTable) result.getParameter();
                    // node.getMetadataFromConnector(newTable.getAttachedConnector()).setListColumns(newTable.getListColumns());
                    if (newTable != null) {
                        for (INodeConnector nodeConnector : node.getListConnector()) {
                            if (nodeConnector.getBaseSchema().equals(newTable.getAttachedConnector())) {
                                MetadataTool.copyTable(newTable, node.getMetadataFromConnector(nodeConnector.getName()));
                            }
                        }
                        builtIn = false;
                    }
                }
            } else if (result.getResultType() == EUpdateResult.RENAME) {
                List<Object> parameter = (List<Object>) result.getParameter();
                if (parameter.size() >= 3) {
                    IMetadataTable newTable = (IMetadataTable) parameter.get(0);
                    String oldSourceId = (String) parameter.get(1);
                    String newSourceId = (String) parameter.get(2);

                    String schemaParamName = UpdatesConstants.SCHEMA + UpdatesConstants.COLON
                            + EParameterName.REPOSITORY_SCHEMA_TYPE.getName();
                    IElementParameter repositoryParam = node.getElementParameter(schemaParamName);
                    if (repositoryParam != null && oldSourceId.equals(repositoryParam.getValue())) {
                        node.setPropertyValue(schemaParamName, newSourceId);

                        if (newTable != null) {
                            for (INodeConnector nodeConnector : node.getListConnector()) {
                                if (nodeConnector.getBaseSchema().equals(newTable.getAttachedConnector())) {
                                    MetadataTool.copyTable(newTable, node.getMetadataFromConnector(nodeConnector.getName()));
                                }
                            }
                        }
                        builtIn = false;
                    }
                }
            }
            if (builtIn) { // built-in
                node.setPropertyValue(EParameterName.SCHEMA_TYPE.getName(), EmfComponent.BUILTIN);

            }
        }
    }

    private void updateQuery() {
        Object updateObject = result.getUpdateObject();
        if (updateObject == null) {
            return;
        }
        if (updateObject instanceof Node) { // opened job
            Node node = (Node) updateObject;

            boolean update = false;
            if (result.getResultType() == EUpdateResult.UPDATE) {
                if (result.isChecked()) {
                    Query query = (Query) result.getParameter();
                    if (query != null) {
                        for (IElementParameter param : node.getElementParameters()) {
                            if (param.getField() == EParameterFieldType.MEMO_SQL
                                    && UpdatesConstants.QUERY.equals(param.getName())) {
                                param.setValue(TalendTextUtils.addSQLQuotes(query.getValue()));
                                param.setRepositoryValueUsed(true);
                                param.setReadOnly(true);
                                update = true;
                            }
                        }
                    }
                }
            }
            if (!update) {
                node.setPropertyValue(EParameterName.QUERYSTORE_TYPE.getName(), EmfComponent.BUILTIN);
                IElementParameter sqlParam = node.getElementParameterFromField(EParameterFieldType.MEMO_SQL);
                if (sqlParam != null) {
                    sqlParam.setRepositoryValueUsed(false);
                    sqlParam.setReadOnly(false);
                }
            }
        }
    }

}
