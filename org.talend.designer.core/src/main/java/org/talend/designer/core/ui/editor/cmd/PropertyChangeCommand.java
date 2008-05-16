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
import java.util.Map;

import org.eclipse.gef.commands.Command;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.Element;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.IElementParameterDefaultValue;
import org.talend.core.model.properties.ProcessItem;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.model.components.EmfComponent;
import org.talend.designer.core.model.process.jobsettings.JobSettingsConstants;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.views.CodeView;
import org.talend.designer.runprocess.ProcessorUtilities;

/**
 * Command that changes a given property. It will call the set or get property value in an element. This element can be
 * either a node, a connection or a process. <br/>
 * 
 * $Id$
 * 
 */
public class PropertyChangeCommand extends Command {

    private final Element elem;

    private final String propName;

    private Object newValue;

    private Object oldValue;

    private boolean repositoryValueWasUsed;

    private boolean toUpdate;

    private final Map<IElementParameter, Object> oldElementValues;

    private ChangeMetadataCommand changeMetadataCommand;

    private String propertyTypeName;

    private final String updataComponentParamName;

    /**
     * The property is defined in an element, which can be either a node or a connection.
     * 
     * @param elem
     * @param propName
     * @param propValue
     */
    public PropertyChangeCommand(Element elem, String propName, Object propValue) {
        this.elem = elem;
        this.propName = propName;
        newValue = propValue;
        toUpdate = false;
        oldElementValues = new HashMap<IElementParameter, Object>();
        setLabel(Messages.getString("PropertyChangeCommand.Label")); //$NON-NLS-1$
        // for job settings extra (feature 2710)
        // if (JobSettingsConstants.isExtraParameter(propName) ||
        // propName.equals(EParameterName.IMPLICIT_TCONTEXTLOAD.getName())) {
        // propertyTypeName = JobSettingsConstants.getExtraParameterName(EParameterName.PROPERTY_TYPE.getName());
        // updataComponentParamName =
        // JobSettingsConstants.getExtraParameterName(EParameterName.UPDATE_COMPONENTS.getName());
        // } else {

        IElementParameter currentParam = elem.getElementParameter(propName);
        propertyTypeName = EParameterName.PROPERTY_TYPE.getName();
        for (IElementParameter param : elem.getElementParameters()) {
            if (param.getField().equals(EParameterFieldType.PROPERTY_TYPE)
                    && param.getCategory().equals(currentParam.getCategory())) {
                propertyTypeName = param.getName() + ":" + EParameterName.PROPERTY_TYPE.getName();
                break;
            }
        }
        updataComponentParamName = EParameterName.UPDATE_COMPONENTS.getName();
        // }
    }

    @Override
    public void execute() {
        IElementParameter currentParam = elem.getElementParameter(propName);
        oldElementValues.clear();

        if (currentParam == null) {
            return;
        }

        if (currentParam.isRepositoryValueUsed()) {
            if (currentParam.getField() == EParameterFieldType.MEMO_SQL) {
                Object queryStoreValue = elem.getPropertyValue(EParameterName.QUERYSTORE_TYPE.getName());
                if (!EmfComponent.BUILTIN.equals(queryStoreValue)) {
                    elem.setPropertyValue(EParameterName.QUERYSTORE_TYPE.getName(), EmfComponent.BUILTIN);
                }
                currentParam.setRepositoryValueUsed(false);
            } else {
                toUpdate = true;
                elem.setPropertyValue(propertyTypeName, EmfComponent.BUILTIN);
                for (IElementParameter param : elem.getElementParameters()) {
                    if (param.getCategory().equals(currentParam.getCategory())) {
                        param.setRepositoryValueUsed(false);
                    }
                }
            }

            repositoryValueWasUsed = true;
        } else {
            repositoryValueWasUsed = false;
        }

        oldValue = elem.getPropertyValue(propName);
        elem.setPropertyValue(propName, newValue);
        if (propName.contains(EParameterName.PROCESS_TYPE_PROCESS.getName())) {
            // newValue is the id of the job
            ProcessItem processItem = ProcessorUtilities.getProcessItem((String) newValue);
            if (processItem != null) {
                currentParam.setLinkedRepositoryItem(processItem);
                currentParam.getParentParameter().setValue(processItem.getProperty().getLabel());
            }
        }
        if (propName.contains(EParameterName.PROCESS_TYPE_VERSION.getName())) {
            // newValue is the id of the job
            IElementParameter processIdParam = currentParam.getParentParameter().getChildParameters().get(
                    EParameterName.PROCESS_TYPE_PROCESS.getName());
            ProcessItem processItem = ProcessorUtilities.getProcessItem((String) processIdParam.getValue(), (String) newValue);
            if (processItem != null) {
                processIdParam.setLinkedRepositoryItem(processItem);
                currentParam.getParentParameter().setValue(processItem.getProperty().getLabel());
            }
        }

        if (!toUpdate
                && (currentParam.getField().equals(EParameterFieldType.RADIO)
                        || currentParam.getField().equals(EParameterFieldType.CLOSED_LIST)
                        || currentParam.getField().equals(EParameterFieldType.CHECK) || currentParam.getField().equals(
                        EParameterFieldType.AS400_CHECK))) {
            toUpdate = false;
            for (int i = 0; i < elem.getElementParameters().size(); i++) {
                IElementParameter testedParam = elem.getElementParameters().get(i);

                String showIf = testedParam.getShowIf();
                String notShowIf = testedParam.getNotShowIf();

                if (showIf != null) {
                    if (showIf.contains(currentParam.getName())) {
                        toUpdate = true;
                    }
                } else {
                    if (notShowIf != null) {
                        if (notShowIf.contains(currentParam.getName())) {
                            toUpdate = true;
                        }
                    }
                }
                if (currentParam.getField().equals(EParameterFieldType.CLOSED_LIST)) {
                    if (testedParam.getListItemsShowIf() != null) {
                        for (int j = 0; j < testedParam.getListItemsShowIf().length && !toUpdate; j++) {
                            showIf = testedParam.getListItemsShowIf()[j];
                            notShowIf = testedParam.getListItemsNotShowIf()[j];
                            if (showIf != null) {
                                if (showIf.contains(currentParam.getName())) {
                                    toUpdate = true;
                                }
                            } else {
                                if (notShowIf != null) {
                                    if (notShowIf.contains(currentParam.getName())) {
                                        toUpdate = true;
                                    }
                                }
                            }
                        }
                    }
                }

                setDefaultValues(currentParam, testedParam);
            }
        }

        if (currentParam.getName().equals(EParameterName.PROCESS_TYPE_PROCESS.getName())) {
            toUpdate = true;
        }

        if (toUpdate) {
            elem.setPropertyValue(updataComponentParamName, new Boolean(true));
        }
        CodeView.refreshCodeView(elem);

        if (elem instanceof Node) {
            ((Node) elem).checkAndRefreshNode();
        }
    }
    
    public void setUpdate(boolean update){
        toUpdate=update;
    }
    
    /**
     * Set the values to default if needed.
     * 
     * @param currentParam Current parameter that has been modified in the interface
     * @param testedParam Tested parameter, to know if there is a link for the default values between this parameter and
     * the current.
     */
    private void setDefaultValues(IElementParameter currentParam, IElementParameter testedParam) {
        boolean contains = false;
        for (IElementParameterDefaultValue value : testedParam.getDefaultValues()) {
            if (value.getIfCondition() != null) {
                if (value.getIfCondition().contains(currentParam.getName())) {
                    contains = true;
                    break;
                }
            }
            if (value.getNotIfCondition() != null) {
                if (value.getNotIfCondition().contains(currentParam.getName())) {
                    contains = true;
                    break;
                }
            }
        }

        if (testedParam.getDefaultValues().size() > 0 && contains) {
            oldElementValues.put(testedParam, testedParam.getValue());

            // if the field is not a schema type, then use standard "set value".
            if (!testedParam.getField().equals(EParameterFieldType.SCHEMA_TYPE)) {
                String oldMapping = "";
                if (!testedParam.getField().equals(EParameterFieldType.CHECK)
                        && !testedParam.getField().equals(EParameterFieldType.RADIO)) {
                    oldMapping = (String) testedParam.getValue();
                }
                testedParam.setValueToDefault(elem.getElementParameters());
                if (testedParam.getField().equals(EParameterFieldType.MAPPING_TYPE)) {
                    String newMapping = (String) testedParam.getValue();
                    if (!oldMapping.equals(newMapping)) {
                        Node node = (Node) elem;
                        if (node.getMetadataList().size() > 0) {
                            // to change with:
                            // IMetadataTable metadataTable = node.getMetadataFromConnector(testedParam.getContext());
                            IMetadataTable metadataTable = node.getMetadataList().get(0);
                            metadataTable.setDbms(newMapping);
                        }
                    }
                }
            } else {
                // See issue 975, update the schema.
                Node node = (Node) elem;
                if (node.getMetadataList().size() > 0) {
                    IMetadataTable metadataTable = node.getMetadataFromConnector(testedParam.getContext());
                    testedParam.setValueToDefault(node.getElementParameters());
                    IMetadataTable newMetadataTable = (IMetadataTable) testedParam.getValue();
                    newMetadataTable.setTableName(metadataTable.getTableName());
                    newMetadataTable.setAttachedConnector(metadataTable.getAttachedConnector());

                    metadataTable.setListColumns(newMetadataTable.clone(true).getListColumns());

                    changeMetadataCommand = new ChangeMetadataCommand(node, null, metadataTable, newMetadataTable);
                    changeMetadataCommand.execute(true);
                }
            }
        }
    }

    @Override
    public void undo() {
        IElementParameter currentParam = elem.getElementParameter(propName);
        if (repositoryValueWasUsed) {
            if (currentParam.getField() == EParameterFieldType.MEMO_SQL) {
                elem.setPropertyValue(EParameterName.QUERYSTORE_TYPE.getName(), EmfComponent.REPOSITORY);
            } else {
                elem.setPropertyValue(propertyTypeName, EmfComponent.REPOSITORY);
            }
            for (IElementParameter param : elem.getElementParameters()) {
                String repositoryValue = param.getRepositoryValue();
                if (param.isShow(elem.getElementParameters()) && (repositoryValue != null)
                        && param.getCategory().equals(currentParam.getCategory())) {
                    param.setRepositoryValueUsed(true);
                }
            }
        }
        elem.setPropertyValue(propName, oldValue);
        if (propName.contains(EParameterName.PROCESS_TYPE_PROCESS.getName())) {
            // oldValue is the id of the job
            ProcessItem processItem = ProcessorUtilities.getProcessItem((String) oldValue);
            if (processItem != null) {
                currentParam.setLinkedRepositoryItem(processItem);
                currentParam.getParentParameter().setValue(processItem.getProperty().getLabel());
            }
        }

        for (IElementParameter param : oldElementValues.keySet()) {
            param.setValue(oldElementValues.get(param));
        }

        if (toUpdate) {
            elem.setPropertyValue(updataComponentParamName, new Boolean(true));
        }
        if (changeMetadataCommand != null) {
            changeMetadataCommand.undo();
        }
        CodeView.refreshCodeView(elem);
        if (elem instanceof Node) {
            ((Node) elem).checkAndRefreshNode();
        }
    }

    @Override
    public void redo() {
        IElementParameter currentParam = elem.getElementParameter(propName);
        if (repositoryValueWasUsed) {
            if (currentParam.getField() == EParameterFieldType.MEMO_SQL) {
                elem.setPropertyValue(EParameterName.QUERYSTORE_TYPE.getName(), EmfComponent.BUILTIN);
            } else {
                elem.setPropertyValue(propertyTypeName, EmfComponent.BUILTIN);
            }

            for (IElementParameter param : elem.getElementParameters()) {
                boolean paramFlag = JobSettingsConstants.isExtraParameter(param.getName());
                boolean extraFlag = JobSettingsConstants.isExtraParameter(propertyTypeName);
                if (paramFlag == extraFlag) {
                    // for job settings extra.(feature 2710)
                    param.setRepositoryValueUsed(false);
                }
            }
        }

        elem.setPropertyValue(propName, newValue);
        if (propName.contains(EParameterName.PROCESS_TYPE_PROCESS.getName())) {
            // newValue is the id of the job
            ProcessItem processItem = ProcessorUtilities.getProcessItem((String) newValue);
            if (processItem != null) {
                currentParam.setLinkedRepositoryItem(processItem);
                currentParam.getParentParameter().setValue(processItem.getProperty().getLabel());
            }
        }

        if (currentParam.getField().equals(EParameterFieldType.CLOSED_LIST)) {
            for (int i = 0; i < elem.getElementParameters().size(); i++) {
                IElementParameter param = elem.getElementParameters().get(i);
                if (param.getDefaultValues().size() > 0) {
                    param.setValueToDefault(elem.getElementParameters());
                }
            }
        }

        if (toUpdate) {
            elem.setPropertyValue(updataComponentParamName, new Boolean(true));
        }

        if (changeMetadataCommand != null) {
            changeMetadataCommand.redo();
        }
        CodeView.refreshCodeView(elem);
        if (elem instanceof Node) {
            ((Node) elem).checkAndRefreshNode();
        }
    }

    public String getPropName() {
        return this.propName;
    }

    public Element getElement() {
        return this.elem;
    }

    public Object getOldValue() {
        return this.oldValue;
    }

    public Object getNewValue() {
        return this.newValue;
    }

}
