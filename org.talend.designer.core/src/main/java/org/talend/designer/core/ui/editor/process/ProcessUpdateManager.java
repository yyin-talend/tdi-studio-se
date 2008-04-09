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
package org.talend.designer.core.ui.editor.process;

import java.beans.PropertyChangeEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Display;
import org.talend.commons.ui.swt.dialogs.ProgressDialog;
import org.talend.core.model.components.ComponentUtilities;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.context.ContextUtils;
import org.talend.core.model.context.JobContextManager;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.Query;
import org.talend.core.model.metadata.designerproperties.RepositoryToComponentProperty;
import org.talend.core.model.process.EComponentCategory;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.model.properties.DatabaseConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.core.model.update.EUpdateItemType;
import org.talend.core.model.update.EUpdateResult;
import org.talend.core.model.update.UpdateResult;
import org.talend.core.model.update.UpdatesConstants;
import org.talend.core.model.utils.TalendTextUtils;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.model.components.ElementParameter;
import org.talend.designer.core.model.components.EmfComponent;
import org.talend.designer.core.model.process.AbstractProcessProvider;
import org.talend.designer.core.model.utils.emf.talendfile.ContextParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.update.AbstractUpdateManager;
import org.talend.designer.core.ui.editor.update.UpdateCheckDialog;
import org.talend.designer.core.ui.editor.update.UpdateCheckResult;
import org.talend.designer.core.ui.editor.update.UpdateManagerHelper;
import org.talend.designer.core.ui.editor.update.cmd.UpdateContextParameterCommand;
import org.talend.designer.core.ui.editor.update.cmd.UpdateJobletNodeCommand;
import org.talend.designer.core.ui.editor.update.cmd.UpdateMainParameterCommand;
import org.talend.designer.core.ui.editor.update.cmd.UpdateNodeParameterCommand;
import org.talend.designer.core.ui.views.contexts.Contexts;
import org.talend.designer.core.ui.views.jobsettings.JobSettings;
import org.talend.designer.core.ui.views.properties.ComponentSettings;
import org.talend.repository.UpdateRepositoryUtils;

/**
 * ggu class global comment. Detailled comment
 */
public class ProcessUpdateManager extends AbstractUpdateManager {

    public ProcessUpdateManager(org.talend.designer.core.ui.editor.process.Process process) {
        super(process);

    }

    /*
     * check context.
     */
    private List<UpdateResult> checkContext() {
        List<UpdateResult> contextResults = new ArrayList<UpdateResult>();
        final IContextManager contextManager = getProcess().getContextManager();
        // record the unsame
        Map<ContextItem, Set<String>> unsameMap = new HashMap<ContextItem, Set<String>>();
        Set<String> builtInSet = new HashSet<String>();

        final List<ContextItem> allContextItem = ContextUtils.getAllContextItem();

        for (IContext context : contextManager.getListContext()) {
            for (IContextParameter param : context.getContextParameterList()) {
                if (!param.isBuiltIn()) {
                    final ContextItem contextItem = ContextUtils.getContextItemByName(allContextItem, param.getSource());
                    if (contextItem != null) {
                        boolean same = false;
                        final ContextType contextType = ContextUtils.getContextTypeByName(contextItem, context.getName(), true);
                        if (contextType != null) {
                            final ContextParameterType contextParameterType = ContextUtils.getContextParameterTypeByName(
                                    contextType, param.getName());
                            if (contextParameterType != null
                                    && ContextUtils.samePropertiesForContextParameter(param, contextParameterType)) {
                                same = true;
                            }
                        }
                        if (!same) {
                            Set<String> names = unsameMap.get(contextItem);
                            if (names == null) {
                                names = new HashSet<String>();
                                unsameMap.put(contextItem, names);
                            }
                            names.add(param.getName());
                        }
                    } else { // built in
                        builtInSet.add(param.getName());
                    }
                }
            }
        }
        // built-in

        if (contextManager instanceof JobContextManager) { // add the lost source for init process
            Set<String> lostParameters = ((JobContextManager) contextManager).getLostParameters();
            if (lostParameters != null && !lostParameters.isEmpty()) {
                builtInSet.addAll(lostParameters);
                lostParameters.clear();
            }
        }
        if (!builtInSet.isEmpty()) {
            UpdateCheckResult result = new UpdateCheckResult(builtInSet);
            result.setResult(EUpdateItemType.CONTEXT, EUpdateResult.BUIL_IN);
            contextResults.add(result);
        }
        // update
        if (!unsameMap.isEmpty()) {
            for (ContextItem item : unsameMap.keySet()) {
                Set<String> names = unsameMap.get(item);
                if (names != null && !names.isEmpty()) {
                    UpdateCheckResult result = new UpdateCheckResult(names);
                    result.setResult(EUpdateItemType.CONTEXT, EUpdateResult.UPDATE, item, UpdateRepositoryUtils
                            .getRepositorySourceName(item));
                    contextResults.add(result);
                }
            }
        }
        return contextResults;
    }

    /*
     * check job settings parameters.
     */
    private List<UpdateResult> checkMainParameters(EUpdateItemType type) {
        List<UpdateResult> mainResults = new ArrayList<UpdateResult>();
        switch (type) {
        case JOB_PROPERTY_EXTRA:
            mainResults.addAll(checkJobSettingsParameters(EComponentCategory.EXTRA, type));
            break;
        case JOB_PROPERTY_STATS_LOGS:
            mainResults.addAll(checkJobSettingsParameters(EComponentCategory.STATSANDLOGS, type));
            break;
        default:
            return Collections.emptyList();
        }

        return mainResults;
    }

    private List<UpdateResult> checkJobSettingsParameters(EComponentCategory category, EUpdateItemType type) {
        List<UpdateResult> jobSettingsResults = new ArrayList<UpdateResult>();
        final IElementParameter propertyTypeParam = getProcess().getElementParameterFromField(EParameterFieldType.PROPERTY_TYPE,
                category);

        if (propertyTypeParam != null && propertyTypeParam.isShow(getProcess().getElementParameters())) {
            final Map<String, IElementParameter> childParameters = propertyTypeParam.getChildParameters();
            if (childParameters == null) {
                return Collections.emptyList();
            }
            IElementParameter elementParameter = childParameters.get(EParameterName.PROPERTY_TYPE.getName());
            // is repository
            if (elementParameter != null && EmfComponent.REPOSITORY.equals(elementParameter.getValue())) {
                IElementParameter repositoryParam = childParameters.get(EParameterName.REPOSITORY_PROPERTY_TYPE.getName());
                if (repositoryParam != null) {

                    // get the connection
                    Connection repositoryConnection = null;
                    String source = null;
                    IRepositoryObject lastVersion = UpdateRepositoryUtils.getRepositoryObjectById((String) repositoryParam
                            .getValue());
                    if (lastVersion != null) {
                        final Item item = lastVersion.getProperty().getItem();
                        if (item != null && item instanceof ConnectionItem) {
                            source = UpdateRepositoryUtils.getRepositorySourceName(item);
                            repositoryConnection = ((DatabaseConnectionItem) item).getConnection();
                        }
                    }
                    UpdateCheckResult result = new UpdateCheckResult(getProcess());

                    if (repositoryConnection != null) {
                        boolean sameValues = true;
                        for (IElementParameter param : getProcess().getElementParameters()) {
                            if (param.getCategory() == category) {
                                String repositoryValue = param.getRepositoryValue();
                                if (param.isShow(getProcess().getElementParameters()) && (repositoryValue != null)
                                        && !param.getName().equals(EParameterName.PROPERTY_TYPE.getName())) {
                                    Object repValue = RepositoryToComponentProperty.getValue(repositoryConnection,
                                            repositoryValue);
                                    if (repValue == null) {
                                        continue;
                                    }
                                    if (repositoryValue.equals(UpdatesConstants.TYPE)) { // datebase type
                                        boolean found = false;
                                        String[] list = param.getListRepositoryItems();
                                        for (int i = 0; (i < list.length) && (!found); i++) {
                                            if (repValue.equals(list[i])) {
                                                found = true;
                                            }
                                        }
                                        if (!found) {
                                            sameValues = false;
                                        }

                                    } else {
                                        // check the value
                                        if (!param.getValue().equals(repValue)) {
                                            sameValues = false;
                                        }
                                    }

                                }

                            }
                        }
                        if (!sameValues) {

                            result.setResult(type, EUpdateResult.UPDATE, repositoryConnection, source);

                        } else {
                            for (IElementParameter param : getProcess().getElementParameters()) {
                                String repositoryValue = param.getRepositoryValue();
                                if (param.isShow(getProcess().getElementParameters()) && (repositoryValue != null)
                                        && (!param.getName().equals(EParameterName.PROPERTY_TYPE.getName()))
                                        && param.getCategory() == category) {
                                    param.setRepositoryValueUsed(true);
                                    param.setReadOnly(true);
                                }
                            }
                        }
                    } else {
                        result.setResult(type, EUpdateResult.BUIL_IN);
                    }
                    if (result.getResultType() != null) {
                        jobSettingsResults.add(result);
                    }
                }
            }
        }
        return jobSettingsResults;
    }

    /*
     * check node parameters.
     */
    @SuppressWarnings("unchecked")//$NON-NLS-1$
    private List<UpdateResult> checkNodesParameters(EUpdateItemType type) {
        List<UpdateResult> nodesResults = new ArrayList<UpdateResult>();
        for (Node node : (List<Node>) getProcess().getGraphicalNodes()) {
            switch (type) {
            case NODE_SCHEMA:
                nodesResults.addAll(checkNodeSchemaFromRepository(node));
                break;
            case NODE_PROPERTY:
                nodesResults.addAll(checkNodePropertiesFromRepository(node));
                break;
            case NODE_QUERY:
                nodesResults.addAll(checkNodeQueryFromRepository(node));
                break;
            default:
                return Collections.emptyList();
            }
        }
        return nodesResults;
    }

    /**
     * 
     * nrousseau Comment method "checkNodeSchemaFromRepository".
     * 
     * @param nc
     * @param metadataTable
     * @return true if the data have been modified
     */
    private List<UpdateResult> checkNodeSchemaFromRepository(final Node node) {
        if (node == null) {
            return Collections.emptyList();
        }
        List<UpdateResult> schemaResults = new ArrayList<UpdateResult>();
        final String uniqueName = node.getUniqueName();

        // check the metadata from the repository to see if it's up to date.
        IElementParameter schemaTypeParam = node.getElementParameterFromField(EParameterFieldType.SCHEMA_TYPE);
        if (schemaTypeParam != null) {
            IElementParameter schemaParam = schemaTypeParam.getChildParameters().get(EParameterName.SCHEMA_TYPE.getName());
            if (schemaParam != null && ((ElementParameter) schemaTypeParam).isDisplayedByDefault()) {
                if (schemaParam.getValue().equals(EmfComponent.REPOSITORY)) {
                    String propertyValue = (String) schemaTypeParam.getChildParameters().get(
                            EParameterName.REPOSITORY_SCHEMA_TYPE.getName()).getValue();
                    ConnectionItem connectionItem = null;
                    String[] names = propertyValue.split(UpdatesConstants.SEGMENT_LINE); //$NON-NLS-1$
                    if (names.length == 2) {
                        connectionItem = UpdateRepositoryUtils.getConnectionItemByItemId(names[0]);
                    }

                    IMetadataTable repositoryMetadata = null;
                    String source = null;
                    if (connectionItem != null) {
                        IMetadataTable table = UpdateRepositoryUtils.getTableByName(connectionItem, names[1]);
                        if (table != null) {
                            source = UpdateRepositoryUtils.getRepositorySourceName(connectionItem)
                                    + UpdatesConstants.SEGMENT_LINE + table.getLabel();
                            repositoryMetadata = table;
                        }
                    }

                    UpdateCheckResult result = new UpdateCheckResult(node);

                    if (repositoryMetadata != null) {
                        final IMetadataTable copyOfrepositoryMetadata = repositoryMetadata.clone();
                        copyOfrepositoryMetadata.setTableName(uniqueName);
                        copyOfrepositoryMetadata.setAttachedConnector(schemaTypeParam.getContext());

                        IMetadataTable metadataTable = node.getMetadataFromConnector(schemaTypeParam.getContext());
                        if (!metadataTable.sameMetadataAs(copyOfrepositoryMetadata, IMetadataColumn.OPTIONS_NONE)) {

                            result.setResult(EUpdateItemType.NODE_SCHEMA, EUpdateResult.UPDATE, copyOfrepositoryMetadata, source);

                        }
                    } else {

                        result.setResult(EUpdateItemType.NODE_SCHEMA, EUpdateResult.BUIL_IN);
                        // if the repository connection doesn't exists then
                        // set to built-in
                    }

                    // add the check result to resultList, hold the value.
                    if (result.getResultType() != null) {
                        schemaResults.add(result);
                    }
                }
            }
        }
        return schemaResults;
    }

    /**
     * 
     * nrousseau Comment method "checkNodePropertiesFromRepository".
     * 
     * @param node
     * @return true if the data have been modified
     */
    @SuppressWarnings("unchecked")//$NON-NLS-1$
    private List<UpdateResult> checkNodePropertiesFromRepository(final Node node) {
        if (node == null) {
            return Collections.emptyList();
        }
        List<UpdateResult> propertiesResults = new ArrayList<UpdateResult>();

        String propertyType = (String) node.getPropertyValue(EParameterName.PROPERTY_TYPE.getName());
        if (propertyType != null) {
            if (propertyType.equals(EmfComponent.REPOSITORY)) {
                String propertyValue = (String) node.getPropertyValue(EParameterName.REPOSITORY_PROPERTY_TYPE.getName());

                IRepositoryObject lastVersion = UpdateRepositoryUtils.getRepositoryObjectById(propertyValue);
                if (lastVersion == null) {
                    return Collections.emptyList();
                }
                Connection repositoryConnection = null;
                String source = null;
                final Item item = lastVersion.getProperty().getItem();
                if (item != null && item instanceof ConnectionItem) {
                    source = UpdateRepositoryUtils.getRepositorySourceName(item);
                    repositoryConnection = ((ConnectionItem) item).getConnection();
                }

                UpdateCheckResult result = new UpdateCheckResult(node);

                if (repositoryConnection != null) {
                    boolean sameValues = true;
                    // if the repository connection exists then test the values
                    for (IElementParameter param : node.getElementParameters()) {
                        String repositoryValue = param.getRepositoryValue();
                        if (param.isShow(node.getElementParameters()) && (repositoryValue != null)) {
                            Object objectValue = RepositoryToComponentProperty.getValue(repositoryConnection, repositoryValue);

                            if (objectValue != null) {
                                if ((param.getField().equals(EParameterFieldType.CLOSED_LIST) && UpdatesConstants.TYPE
                                        .equals(param.getRepositoryValue()))) {
                                    boolean found = false;
                                    String[] list = param.getListRepositoryItems();
                                    for (int i = 0; (i < list.length) && (!found); i++) {
                                        if (objectValue.equals(list[i])) {
                                            found = true;
                                        }
                                    }
                                    if (!found) {
                                        sameValues = false;
                                    }

                                } else {
                                    // check the value
                                    if (!param.getValue().equals(objectValue)) {
                                        sameValues = false;
                                    }
                                }
                            } else if (param.getField().equals(EParameterFieldType.TABLE)
                                    && UpdatesConstants.XML_MAPPING.equals(repositoryValue)) {
                                List<Map<String, Object>> newMaps = RepositoryToComponentProperty.getXMLMappingValue(
                                        repositoryConnection, node.getMetadataList().get(0));
                                if ((param.getValue() instanceof List) && newMaps != null) {
                                    List<Map<String, Object>> oldMaps = (List<Map<String, Object>>) param.getValue();
                                    // sameValues = oldMaps.size() == newMaps.size();
                                    for (int i = 0; i < newMaps.size() && sameValues; i++) {
                                        Map<String, Object> newmap = newMaps.get(i);
                                        Map<String, Object> oldmap = null; // oldMaps.get(i);
                                        if (i < oldMaps.size()) {
                                            oldmap = oldMaps.get(i);
                                        }
                                        if (oldmap != null && sameValues) {
                                            Object o = newmap.get(UpdatesConstants.QUERY);
                                            if (o != null) {
                                                sameValues = newmap.get(UpdatesConstants.QUERY).equals(
                                                        oldmap.get(UpdatesConstants.QUERY));
                                            } else {
                                                sameValues = oldmap.get(UpdatesConstants.QUERY) == null;
                                            }
                                        }
                                    }
                                    if (oldMaps.size() > newMaps.size()) {
                                        int size = newMaps.size();
                                        for (int i = size; i < oldMaps.size(); i++) {
                                            Map<String, Object> map = new HashMap<String, Object>();
                                            map.put(UpdatesConstants.QUERY, UpdatesConstants.EMPTY);
                                            newMaps.add(map);
                                        }
                                        sameValues = false;
                                    }
                                }
                            }
                        }
                    }
                    if (!sameValues) {

                        result.setResult(EUpdateItemType.NODE_PROPERTY, EUpdateResult.UPDATE, repositoryConnection, source);

                    } else {
                        for (IElementParameter param : node.getElementParameters()) {
                            String repositoryValue = param.getRepositoryValue();
                            if (param.isShow(node.getElementParameters()) && (repositoryValue != null)
                                    && (!param.getName().equals(EParameterName.PROPERTY_TYPE.getName()))
                                    && param.getField() != EParameterFieldType.MEMO_SQL) {
                                param.setRepositoryValueUsed(true);
                            }
                        }
                    }
                } else {

                    result.setResult(EUpdateItemType.NODE_PROPERTY, EUpdateResult.BUIL_IN);
                }

                // add the check result to resultList, hold the value.
                if (result.getResultType() != null) {
                    propertiesResults.add(result);
                }
            }
        }
        return propertiesResults;
    }

    /*
     * check node query.
     */
    private List<UpdateResult> checkNodeQueryFromRepository(final Node node) {
        if (node == null) {
            return Collections.emptyList();
        }
        List<UpdateResult> queryResults = new ArrayList<UpdateResult>();

        String propertyType = (String) node.getPropertyValue(EParameterName.QUERYSTORE_TYPE.getName());
        if (propertyType != null) {
            if (propertyType.equals(EmfComponent.REPOSITORY)) {
                String propertyValue = (String) node.getPropertyValue(EParameterName.REPOSITORY_QUERYSTORE_TYPE.getName());

                ConnectionItem connectionItem = null;
                String[] names = propertyValue.split(UpdatesConstants.SEGMENT_LINE); //$NON-NLS-1$
                if (names.length == 2) {
                    connectionItem = UpdateRepositoryUtils.getConnectionItemByItemId(names[0]);
                }
                Query query = null;
                String source = null;
                if (connectionItem != null) {
                    query = UpdateRepositoryUtils.getQueryByName(connectionItem, names[1]);
                }
                String connectQuery = null;
                if (query != null) {
                    source = UpdateRepositoryUtils.getRepositorySourceName(connectionItem) + UpdatesConstants.SEGMENT_LINE
                            + query.getLabel();
                    connectQuery = query.getValue();
                }
                UpdateCheckResult result = new UpdateCheckResult(node);

                if (connectQuery != null) {
                    IElementParameter sqlParam = node.getElementParameterFromField(EParameterFieldType.MEMO_SQL);
                    if (sqlParam != null && UpdatesConstants.QUERY.equals(sqlParam.getName())) {
                        String paramValue = (String) sqlParam.getValue();

                        connectQuery = TalendTextUtils.addSQLQuotes(connectQuery);
                        if (!connectQuery.equals(paramValue)) {
                            result.setResult(EUpdateItemType.NODE_QUERY, EUpdateResult.UPDATE, query, source);
                        } else {
                            sqlParam.setReadOnly(true);
                            sqlParam.setRepositoryValueUsed(true);
                        }
                    }
                } else {
                    result.setResult(EUpdateItemType.NODE_QUERY, EUpdateResult.BUIL_IN);
                }
                if (result.getResultType() != null) {
                    queryResults.add(result);
                }
            }
        }

        return queryResults;
    }

    /**
     * 
     * ggu Comment method "checkJobletNodesContext".
     * 
     * check and update, the result only record the operation.
     */
    private List<UpdateResult> checkJobletNodesContext() {
        if (getProcess().isReadOnly()) { // not readonly
            return Collections.emptyList();
        }
        List<AbstractProcessProvider> findAllProcessProviders = AbstractProcessProvider.findAllProcessProviders();
        List<String> labelList = new ArrayList<String>();
        for (AbstractProcessProvider abstractProcessProvider : findAllProcessProviders) {
            if (abstractProcessProvider != null) {
                List<String> tmpList = abstractProcessProvider.updateProcessContexts(getProcess());
                if (tmpList != null && !tmpList.isEmpty()) {
                    labelList.addAll(tmpList);
                }
            }
        }
        // source to variables list map
        Map<String, Set<String>> reformMap = new HashMap<String, Set<String>>();
        for (String label : labelList) {
            String[] str = label.split(UpdatesConstants.SPACE);
            if (str.length == 2) {
                String var = str[0].trim();
                String source = removeBrackets(str[1]);
                if (IContextParameter.BUILT_IN.equals(source)) {
                    source = str[1];
                }
                Set<String> set = reformMap.get(source);
                if (set == null) {
                    set = new HashSet<String>();
                    reformMap.put(source, set);
                }
                if (!set.contains(var)) {
                    set.add(var);
                }
            }
        }
        List<UpdateResult> contextResults = new ArrayList<UpdateResult>();
        for (String source : reformMap.keySet()) {
            Set<String> set = reformMap.get(source);
            if (set != null && !set.isEmpty()) {
                UpdateCheckResult result = new UpdateCheckResult(set);
                result.setResult(EUpdateItemType.JOBLET_CONTEXT, EUpdateResult.JOBLET_UPDATE, null, UpdatesConstants.CONTEXT
                        + UpdatesConstants.COLON + source);
                contextResults.add(result);
            }
        }

        return contextResults;

    }

    private String removeBrackets(String str) {
        if (str == null) {
            return UpdatesConstants.EMPTY;
        }
        final String prefix = "\\"; //$NON-NLS-1$
        str = str.trim();

        str = str.replaceAll(prefix + UpdatesConstants.LEFT_BRACKETS, UpdatesConstants.EMPTY);
        str = str.replaceAll(prefix + UpdatesConstants.RIGHT_BRACKETS, UpdatesConstants.EMPTY);
        return str.trim();
    }

    /**
     * 
     * ggu Comment method "checkNodesPropertyChanger".
     * 
     * If this is not relational joblet node to update. filter it.
     */
    private List<UpdateResult> checkJobletNodesPropertyChanger() {
        if (getProcess() == null || getNodePropertyChanger() == null) {
            return Collections.emptyList();
        }
        List<UpdateResult> nodeResults = new ArrayList<UpdateResult>();
        for (PropertyChangeEvent event : getNodePropertyChanger()) {
            UpdateCheckResult result = null;

            String propertyName = event.getPropertyName();
            if (propertyName.equals(ComponentUtilities.NORMAL)) {
                Object object = event.getSource();
                if (object instanceof IProcess) {
                    IProcess process2 = (IProcess) object;
                    // avoid reload self
                    if (!getProcess().getId().equals(process2.getId())) {
                        Set<String> nodesName = findRelatedJobletNode(getProcess(), process2.getLabel(), null);
                        if (nodesName != null && !nodesName.isEmpty()) {
                            String source = UpdatesConstants.JOBLET + UpdatesConstants.COLON + process2.getLabel();
                            result = new UpdateCheckResult(nodesName);
                            result.setResult(EUpdateItemType.RELOAD, EUpdateResult.RELOAD, event, source);
                        }
                    }
                } else {
                    result = new UpdateCheckResult(UpdatesConstants.COMPONENT);
                    result.setResult(EUpdateItemType.RELOAD, EUpdateResult.RELOAD, event);
                }

            } else if (propertyName.equals(ComponentUtilities.JOBLET_NAME_CHANGED)) {
                String oldName = (String) event.getOldValue();
                String newName = (String) event.getNewValue();
                Set<String> nodesName = findRelatedJobletNode(getProcess(), oldName, newName);

                if (nodesName != null && !nodesName.isEmpty()) {
                    String source = UpdatesConstants.JOBLET + UpdatesConstants.COLON + newName;

                    result = new UpdateCheckResult(nodesName);
                    result.setResult(EUpdateItemType.JOBLET_RENAMED, EUpdateResult.JOBLET_UPDATE, event, source);
                }
            } else if (propertyName.equals(ComponentUtilities.JOBLET_SCHEMA_CHANGED)) {
                Object object = event.getSource();
                if (object instanceof IProcess) {
                    String oldName = ((IProcess) object).getName();
                    Set<String> nodesName = findRelatedJobletNode(getProcess(), oldName, null);
                    if (nodesName != null && !nodesName.isEmpty()) {
                        String source = UpdatesConstants.JOBLET + UpdatesConstants.COLON + ((IProcess) object).getLabel();

                        result = new UpdateCheckResult(nodesName);
                        result.setResult(EUpdateItemType.JOBLET_SCHEMA, EUpdateResult.JOBLET_UPDATE, event, source);
                    }
                }
            }
            if (result != null) {
                nodeResults.add(result);
            }
        }
        // clear
        getNodePropertyChanger().clear();
        return nodeResults;
    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    private Set<String> findRelatedJobletNode(Process process, String oldjobletName, String newJobletName) {
        if (oldjobletName == null || process == null) {
            return null;
        }
        if (newJobletName == null) {
            newJobletName = oldjobletName;
        }
        Set<String> nodesName = new HashSet<String>();
        for (Node node : (List<Node>) process.getGraphicalNodes()) {
            if (node.getComponent().getName().equals(newJobletName)) {
                IComponent newComponent = UpdateManagerHelper.getComponent(process, newJobletName);
                if (newComponent == null) {
                    continue;
                }
                if (node.getUniqueName().equals(node.getLabel())) {
                    nodesName.add(node.getUniqueName());
                } else {
                    nodesName.add(node.getLabel() + UpdatesConstants.SPACE + UpdatesConstants.LEFT_BRACKETS
                            + node.getUniqueName() + UpdatesConstants.RIGHT_BRACKETS);
                }
            }
        }
        return nodesName;
    }

    private List<UpdateResult> checkJobletNodeSchema() {
        AbstractProcessProvider processProvider = AbstractProcessProvider.findProcessProviderFromPID(IComponent.JOBLET_PID);
        if (processProvider != null) {
            return processProvider.checkJobletNodeSchema(getProcess());
        }
        return null;
    }

    public List<UpdateResult> getUpdatesNeeded(EUpdateItemType type) {
        if (type == null) {
            return null;
        }
        List<UpdateResult> tmpResults = new ArrayList<UpdateResult>();
        switch (type) {
        case NODE_PROPERTY:
        case NODE_SCHEMA:
        case NODE_QUERY:
            tmpResults = checkNodesParameters(type);
            break;
        case JOB_PROPERTY_EXTRA:
        case JOB_PROPERTY_STATS_LOGS:
            tmpResults = checkMainParameters(type);
            break;
        case CONTEXT:
            tmpResults = checkContext();
            break;
        case JOBLET_SCHEMA:
            tmpResults = checkJobletNodeSchema();
            break;
        case JOBLET_RENAMED:
        case RELOAD:
            tmpResults = checkJobletNodesPropertyChanger();
            break;
        case JOBLET_CONTEXT:
            tmpResults = checkJobletNodesContext();
            break;
        default:
        }
        return tmpResults;
    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    public boolean executeUpdates(List<UpdateResult> results) {
        if (results == null || results.isEmpty()) {
            return false;
        }
        try {
            UpdateCheckDialog checkDialog = new UpdateCheckDialog(Display.getCurrent().getActiveShell(), results);

            if (checkDialog.open() == IDialogConstants.OK_ID) {
                final List<UpdateResult> selectResult = Arrays.asList((UpdateResult[]) checkDialog.getResult());
                ProgressDialog progress = new ProgressDialog(Display.getCurrent().getActiveShell()) {

                    @Override
                    public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                        monitor.setCanceled(false);
                        monitor.beginTask("Progress...", selectResult.size() + 1); //$NON-NLS-1$
                        // execute
                        executeUpdates(selectResult, monitor);
                        // refresh
                        refreshRelatedViewers(selectResult);
                        monitor.worked(1);

                        monitor.done();
                    }

                };
                try {
                    progress.executeProcess();
                } catch (InvocationTargetException e) {
                    // 
                } catch (InterruptedException e) {
                    // 
                }
                return !selectResult.isEmpty();
            }
        } finally {
            results.clear();
        }
        return false;
    }

    /**
     * 
     * ggu Comment method "refreshViewers".
     */
    @SuppressWarnings("unchecked")
    private void refreshRelatedViewers(List<UpdateResult> results) {
        boolean context = false;
        boolean jobSetting = false;
        boolean componentSettings = false;
        boolean palette = false;

        for (UpdateResult result : results) {
            switch (result.getUpdateType()) {
            case CONTEXT:
            case JOBLET_CONTEXT:
                context = true;
                break;
            case JOB_PROPERTY_EXTRA:
            case JOB_PROPERTY_STATS_LOGS:
                jobSetting = true;
                break;
            case NODE_PROPERTY:
            case NODE_QUERY:
            case NODE_SCHEMA:
                componentSettings = true;
                break;
            case RELOAD:
            case JOBLET_RENAMED:
            case JOBLET_SCHEMA:
                palette = true;
                break;
            default:
                break;
            }
        }
        if (context) {
            Contexts.switchToCurContextsView();
        }
        if (jobSetting) {
            JobSettings.switchToCurJobSettingsView();
        }
        if (componentSettings) {
            ComponentSettings.switchToCurComponentSettingsView();
        }
        if (palette) {
            ComponentUtilities.updatePalette();
        }
    }

    /**
     * 
     * ggu Comment method "executeUpdates".
     * 
     * can override the is method.
     */
    @SuppressWarnings("unchecked")//$NON-NLS-1$
    private void executeUpdates(List<UpdateResult> selectResult, IProgressMonitor monitor) {
        Command command = null;
        for (UpdateResult result : selectResult) {
            switch (result.getUpdateType()) {
            case NODE_PROPERTY:
            case NODE_SCHEMA:
            case NODE_QUERY:
                command = executeNodeUpdates(result);
                break;
            case JOB_PROPERTY_EXTRA:
            case JOB_PROPERTY_STATS_LOGS:
                command = executeMainUpdates(result);
                break;
            case CONTEXT:
                command = executeContextUpdates(result);
                break;
            case JOBLET_RENAMED:
            case JOBLET_SCHEMA:
            case RELOAD:
                command = executeJobletNodesUpdates(result);
                break;
            case JOBLET_CONTEXT:
                command = executeJobletContextUpdates(result);
                break;
            default:
                break;
            }
            if (command != null) {
                getCommandStack().execute(command);
                monitor.worked(1);
            }
        }

    }

    /*
     * node
     */
    private Command executeNodeUpdates(UpdateResult result) {
        if (result == null) {
            return null;
        }
        Object element = result.getUpdateObject();
        if (element instanceof Node) {
            Node node = (Node) element;
            return new UpdateNodeParameterCommand(node, result);
        }
        return null;
    }

    /*
     * main
     */
    private Command executeMainUpdates(UpdateResult result) {
        if (result == null) {
            return null;
        }
        return new UpdateMainParameterCommand(getProcess(), result);
    }

    /*
     * context
     */
    @SuppressWarnings("unchecked")//$NON-NLS-1$
    private Command executeContextUpdates(UpdateResult result) {
        if (result == null) {
            return null;
        }
        Object object = result.getUpdateObject();
        if (object instanceof Set) {
            return new UpdateContextParameterCommand(getProcess(), result);
        }
        return null;
    }

    /*
     * joblet
     */
    private Command executeJobletNodesUpdates(UpdateResult result) {
        if (result == null) {
            return null;
        }
        Object parameter = result.getParameter();
        if (parameter instanceof PropertyChangeEvent) {
            return new UpdateJobletNodeCommand(getProcess(), (PropertyChangeEvent) parameter);
        }
        return null;
    }

    private Command executeJobletContextUpdates(UpdateResult result) {
        if (result == null) {
            return null;
        }
        // have updated in method "checkJobletNodesContext".
        return new Command() {
        };
    }

}
