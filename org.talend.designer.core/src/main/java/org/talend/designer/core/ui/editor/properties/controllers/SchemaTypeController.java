// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2007 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License.
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
package org.talend.designer.core.ui.editor.properties.controllers;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.fieldassist.DecoratedField;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.fieldassist.IControlCreator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.talend.core.CorePlugin;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.MetadataTable;
import org.talend.core.model.metadata.MetadataTool;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.INodeConnector;
import org.talend.core.ui.metadata.dialog.MetadataDialog;
import org.talend.core.ui.metadata.dialog.MetadataDialogForMerge;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.model.components.EmfComponent;
import org.talend.designer.core.ui.editor.cmd.ChangeMetadataCommand;
import org.talend.designer.core.ui.editor.cmd.RepositoryChangeMetadataCommand;
import org.talend.designer.core.ui.editor.connections.Connection;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.properties.DynamicTabbedPropertySection;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 * $Id: talend-code-templates.xml 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
 * 
 */
public class SchemaTypeController extends AbstractElementPropertySectionController {

    private static final String RESET_COLUMNS = "RESET_COLUMNS"; //$NON-NLS-1$

    private static final String SCHEMA = "SCHEMA"; //$NON-NLS-1$

    public SchemaTypeController(DynamicTabbedPropertySection dtp) {
        super(dtp);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties.controllers.AbstractElementPropertySectionController#createCommand()
     */
    private Command createCommand(SelectionEvent selectionEvent) {
        if (selectionEvent.getSource() instanceof Button) {
            return createButtonCommand((Button) selectionEvent.getSource());
        }
        if (selectionEvent.getSource() instanceof CCombo) {
            return createComboCommand((CCombo) selectionEvent.getSource());
        }
        return null;
    }

    private Command createComboCommand(CCombo combo) {
        IMetadataTable repositoryMetadata;
        Map<String, IMetadataTable> repositoryTableMap = dynamicTabbedPropertySection.getRepositoryTableMap();

        // String paramName;

        // IElementParameter repositorySchemaTypeParameter = elem
        // .getElementParameter(EParameterName.REPOSITORY_SCHEMA_TYPE.getName());
        // Object repositoryControl = hashCurControls.get(repositorySchemaTypeParameter.getName());

        String fullParamName = (String) combo.getData(PARAMETER_NAME);
        // if (combo.equals(repositoryControl)) {
        // paramName = EParameterName.REPOSITORY_SCHEMA_TYPE.getName();
        // } else {
        // paramName = EParameterName.SCHEMA_TYPE.getName();
        // }
        IElementParameter switchParam = elem.getElementParameter(EParameterName.REPOSITORY_ALLOW_AUTO_SWITCH.getName());

        String value = new String(""); //$NON-NLS-1$

        IElementParameter param = elem.getElementParameter(fullParamName);
        for (int j = 0; j < param.getListItemsValue().length; j++) {
            if (combo.getText().equals(param.getListItemsDisplayName()[j])) {
                value = (String) param.getListItemsValue()[j];
            }
        }

        // for (int i = 0; i < elem.getElementParameters().size(); i++) {
        // IElementParameter param = elem.getElementParameters().get(i);
        // if (param.getName().equals(paramName)) {
        // for (int j = 0; j < param.getListItemsValue().length; j++) {
        // if (combo.getText().equals(param.getListItemsDisplayName()[j])) {
        // value = (String) param.getListItemsValue()[j];
        // }
        // }
        // }
        // }
        org.talend.core.model.metadata.builder.connection.Connection connection = null;
        if (fullParamName.contains(EParameterName.REPOSITORY_SCHEMA_TYPE.getName())) {
            if (elem instanceof Node) {
                this.dynamicTabbedPropertySection.updateRepositoryList();
                if (repositoryTableMap.containsKey(value)) {
                    repositoryMetadata = repositoryTableMap.get(value);
                    IElementParameter property = ((Node) elem).getElementParameter(EParameterName.PROPERTY_TYPE
                            .getName());
                    if ((property != null) && EmfComponent.REPOSITORY.equals(property.getValue())) {
                        String propertySelected = (String) ((Node) elem).getElementParameter(
                                EParameterName.REPOSITORY_PROPERTY_TYPE.getName()).getValue();
                        connection = dynamicTabbedPropertySection.getRepositoryConnectionItemMap()
                                .get(propertySelected).getConnection();
                    }
                } else {
                    repositoryMetadata = new MetadataTable();
                }
                if (switchParam != null) {
                    switchParam.setValue(Boolean.FALSE);
                }
                RepositoryChangeMetadataCommand changeMetadataCommand = new RepositoryChangeMetadataCommand(
                        (Node) elem, fullParamName, value, repositoryMetadata);
                changeMetadataCommand.setConnection(connection);
                // changeMetadataCommand.setMaps(this.dynamicTabbedPropertySection.getTableIdAndDbTypeMap(),
                // this.dynamicTabbedPropertySection.getTableIdAndDbSchemaMap(), this.dynamicTabbedPropertySection
                // .getRepositoryTableMap());

                return changeMetadataCommand;

            }
        }
        // Schema Type combo was selected.
        else {
            if (elem instanceof Node) {
                Node node = (Node) elem;
                boolean isReadOnly = false;
                if (node.getMetadataFromConnector(param.getContext()) != null) {
                    isReadOnly = node.getMetadataFromConnector(param.getContext()).isReadOnly();
                }
                if (value.equals(EmfComponent.BUILTIN) && isReadOnly) {
                    boolean hasMetadataInput = false;
                    if (node.getCurrentActiveLinksNbInput(EConnectionType.FLOW_MAIN) > 0
                            || node.getCurrentActiveLinksNbInput(EConnectionType.TABLE) > 0) {
                        hasMetadataInput = true;
                    }
                    repositoryMetadata = new MetadataTable();
                    if (hasMetadataInput) {
                        for (Connection connec : (List<Connection>) node.getIncomingConnections()) {
                            if (connec.isActivate()
                                    && (connec.getLineStyle().equals(EConnectionType.FLOW_MAIN) || connec
                                            .getLineStyle().equals(EConnectionType.TABLE))) {
                                repositoryMetadata = connec.getMetadataTable().clone();
                            }
                        }

                    }
                } else {
                    this.dynamicTabbedPropertySection.updateRepositoryList();
                    IElementParameter property = ((Node) elem).getElementParameter(EParameterName.PROPERTY_TYPE
                            .getName());
                    if ((property != null) && EmfComponent.REPOSITORY.equals(property.getValue())) {
                        String propertySelected = (String) ((Node) elem).getElementParameter(
                                EParameterName.REPOSITORY_PROPERTY_TYPE.getName()).getValue();
                        connection = dynamicTabbedPropertySection.getRepositoryConnectionItemMap()
                                .get(propertySelected).getConnection();
                    }

                    String schemaSelected = (String) param.getParentParameter().getChildParameters().get(
                            EParameterName.REPOSITORY_SCHEMA_TYPE.getName()).getValue();
                    if (repositoryTableMap.containsKey(schemaSelected)) {
                        repositoryMetadata = repositoryTableMap.get(schemaSelected);
                    } else {
                        if (repositoryTableMap.keySet().size() == 0) {
                            repositoryMetadata = new MetadataTable();
                        } else {
                            // Gets the schema of the first item in repository schema type combo.
                            repositoryMetadata = repositoryTableMap.get(repositoryTableMap.keySet().iterator().next());

                        }
                    }
                }
                if (switchParam != null) {
                    switchParam.setValue(Boolean.FALSE);
                }

                RepositoryChangeMetadataCommand changeMetadataCommand = new RepositoryChangeMetadataCommand(
                        (Node) elem, fullParamName, value, repositoryMetadata);
                changeMetadataCommand.setConnection(connection);
                // changeMetadataCommand.setMaps(this.dynamicTabbedPropertySection.getTableIdAndDbTypeMap(),
                // this.dynamicTabbedPropertySection.getTableIdAndDbSchemaMap(), this.dynamicTabbedPropertySection
                // .getRepositoryTableMap());

                return changeMetadataCommand;
            }
        }

        return null;
    }

    private Command createButtonCommand(Button button) {
        Button inputButton = button;
        IElementParameter switchParam = elem.getElementParameter(EParameterName.REPOSITORY_ALLOW_AUTO_SWITCH.getName());

        if (inputButton.getData(NAME).equals(SCHEMA)) {
            // this map wil hold the all input connection for the tUnite component
            Map<INode, Map<IMetadataTable, Boolean>> inputInfos = new HashMap<INode, Map<IMetadataTable, Boolean>>();

            Node node;
            if (elem instanceof Node) {
                node = (Node) elem;
            } else { // else instanceof Connection
                node = ((Connection) elem).getSource();
            }

            IMetadataTable inputMetadata = null, inputMetaCopy = null;
            Connection inputConec = null;

            boolean inputReadOnly = false, outputReadOnly = false, inputReadOnlyNode = false, inputReadOnlyParam = false;
            for (Connection connec : (List<Connection>) node.getIncomingConnections()) {
                if (connec.isActivate()
                        && (connec.getLineStyle().equals(EConnectionType.FLOW_MAIN)
                                || connec.getLineStyle().equals(EConnectionType.TABLE) || connec.getLineStyle().equals(
                                EConnectionType.FLOW_MERGE))) {
                    inputMetadata = connec.getMetadataTable();
                    inputMetaCopy = inputMetadata.clone();
                    inputConec = connec;

                    if (connec.getSource().isReadOnly()) {
                        inputReadOnlyNode = true;
                    } else {
                        for (IElementParameter param : connec.getSource().getElementParameters()) {
                            if (param.getField() == EParameterFieldType.SCHEMA_TYPE) {
                                if (param.isReadOnly()) {
                                    inputReadOnlyParam = true;
                                }
                            }
                        }
                    }

                    // check if the inputMetadata is readonly
                    if (inputMetadata != null) {
                        for (IMetadataColumn column : inputMetadata.getListColumns()) {
                            IMetadataColumn columnCopied = inputMetaCopy.getColumn(column.getLabel());
                            columnCopied.setCustom(column.isCustom());
                            columnCopied.setReadOnly(column.isReadOnly());
                        }
                        inputMetaCopy.setReadOnly(inputMetadata.isReadOnly());
                        inputReadOnly = prepareReadOnlyTable(inputMetaCopy, inputReadOnlyParam, inputReadOnlyNode);
                    }

                    // store the value for Dialog
                    Map<IMetadataTable, Boolean> oneInput = new HashMap<IMetadataTable, Boolean>();
                    oneInput.put(inputMetaCopy, inputReadOnly);
                    inputInfos.put(connec.getSource(), oneInput);
                }
            }

            // check if the outputMetadata is readonly
            String propertyName = (String) inputButton.getData(PARAMETER_NAME);
            IElementParameter param = node.getElementParameter(propertyName);
            IMetadataTable originaleOutputTable = node.getMetadataFromConnector(param.getContext());
            IMetadataTable outputMetaCopy = originaleOutputTable.clone();

            for (IMetadataColumn column : originaleOutputTable.getListColumns()) {
                IMetadataColumn columnCopied = outputMetaCopy.getColumn(column.getLabel());
                columnCopied.setCustom(column.isCustom());
                columnCopied.setReadOnly(column.isReadOnly());
                setColumnLength(node, param, columnCopied);
            }
            outputMetaCopy.setReadOnly(originaleOutputTable.isReadOnly());
            outputReadOnly = prepareReadOnlyTable(outputMetaCopy, param.isReadOnly(), node.isReadOnly());

            // create the MetadataDialog
            MetadataDialog metaDialog = null;
            if (inputMetadata != null) {
                if (inputInfos != null && node.getComponent().useMerge() && inputInfos.size() > 1) {
                    MetadataDialogForMerge metaDialogForMerge = new MetadataDialogForMerge(composite.getShell(),
                            inputInfos, outputMetaCopy, node, getCommandStack());
                    metaDialogForMerge.setText(Messages.getString("SchemaController.schemaOf") + node.getLabel()); //$NON-NLS-1$
                    metaDialogForMerge.setInputReadOnly(inputReadOnly);
                    metaDialogForMerge.setOutputReadOnly(outputReadOnly);
                    if (metaDialogForMerge.open() == MetadataDialogForMerge.OK) {
                        // inputMetaCopy = metaDialog.getInputMetaData();
                        outputMetaCopy = metaDialogForMerge.getOutputMetaData();

                        // check if the metadata is modified
                        boolean modified = false;
                        if (!outputMetaCopy.sameMetadataAs(originaleOutputTable, IMetadataColumn.OPTIONS_NONE)) {
                            modified = true;
                        } else {
                            if (inputMetadata != null) {

                                // Notice: the Map inputInfos maybe is modified by the dialog.
                                Set<INode> inputNodes = inputInfos.keySet();
                                for (INode inputNode : inputNodes) {
                                    Map<IMetadataTable, Boolean> oneInput = inputInfos.get(inputNode);
                                    inputMetaCopy = (IMetadataTable) oneInput.keySet().toArray()[0];
                                    if (!inputMetaCopy.sameMetadataAs(inputNode.getMetadataList().get(0),
                                            IMetadataColumn.OPTIONS_NONE)) {
                                        modified = true;
                                    }
                                }

                            }
                        }

                        // create the changeMetadataCommand
                        if (modified) {
                            if (switchParam != null) {
                                switchParam.setValue(Boolean.FALSE);
                            }

                            Command changeMetadataCommand = null;

                            // only output, no input
                            if (inputInfos.isEmpty()) {
                                changeMetadataCommand = new ChangeMetadataCommand(node, param, null, null, null,
                                        originaleOutputTable, outputMetaCopy);

                            } else {
                                Set<INode> inputNodes = inputInfos.keySet();
                                int count = 0;
                                for (INode inputNode : inputNodes) {
                                    Map<IMetadataTable, Boolean> oneInput = inputInfos.get(inputNode);
                                    inputMetaCopy = (IMetadataTable) oneInput.keySet().toArray()[0];
                                    if (count == 0) {
                                        changeMetadataCommand = new ChangeMetadataCommand(node, param,
                                                (Node) inputNode, inputNode.getMetadataList().get(0), inputMetaCopy,
                                                originaleOutputTable, outputMetaCopy);
                                    } else {
                                        changeMetadataCommand = changeMetadataCommand.chain(new ChangeMetadataCommand(
                                                node, param, (Node) inputNode, inputNode.getMetadataList().get(0),
                                                inputMetaCopy, originaleOutputTable, outputMetaCopy));
                                    }
                                    count++;
                                }
                            }
                            return changeMetadataCommand;

                        }
                    }

                } else {
                    Node inputNode = (inputConec.getSource());
                    if (inputMetaCopy.getAttachedConnector() == null) {
                        INodeConnector mainConnector;
                        if (inputNode.isELTComponent()) {
                            mainConnector = inputNode.getConnectorFromType(EConnectionType.TABLE);
                        } else {
                            mainConnector = inputNode.getConnectorFromType(EConnectionType.FLOW_MAIN);
                        }
                        inputMetaCopy.setAttachedConnector(mainConnector.getName());
                    }

                    metaDialog = new MetadataDialog(composite.getShell(), inputMetaCopy, inputNode, outputMetaCopy,
                            node, getCommandStack());
                }

            } else {
                metaDialog = new MetadataDialog(composite.getShell(), outputMetaCopy, node, getCommandStack());
            }

            if (metaDialog != null) {
                metaDialog.setText(Messages.getString("SchemaController.schemaOf") + node.getLabel()); //$NON-NLS-1$
                metaDialog.setInputReadOnly(inputReadOnly);
                metaDialog.setOutputReadOnly(outputReadOnly);

                if (metaDialog.open() == MetadataDialog.OK) {
                    inputMetaCopy = metaDialog.getInputMetaData();
                    outputMetaCopy = metaDialog.getOutputMetaData();
                    boolean modified = false;
                    if (!outputMetaCopy.sameMetadataAs(originaleOutputTable, IMetadataColumn.OPTIONS_NONE)) {
                        modified = true;
                    } else {
                        if (inputMetadata != null) {
                            if (!inputMetaCopy.sameMetadataAs(inputMetadata, IMetadataColumn.OPTIONS_NONE)) {
                                modified = true;
                            }
                        }
                    }

                    if (modified) {
                        if (switchParam != null) {
                            switchParam.setValue(Boolean.FALSE);
                        }
                        Node inputNode = null;
                        if (inputConec != null) {
                            inputNode = inputConec.getSource();
                        }
                        ChangeMetadataCommand changeMetadataCommand = new ChangeMetadataCommand(node, param, inputNode,
                                inputMetadata, inputMetaCopy, originaleOutputTable, outputMetaCopy);

                        return changeMetadataCommand;

                    }
                }

            }
        } else if (inputButton.getData(NAME).equals(RESET_COLUMNS)) {
            Node node = (Node) elem;

            String propertyName = (String) inputButton.getData(PARAMETER_NAME);
            IElementParameter param = node.getElementParameter(propertyName);

            IMetadataTable meta = node.getMetadataFromConnector(param.getContext());
            IMetadataTable metaCopy = meta.clone(true);

            boolean inputFound = false;
            for (Connection connec : (List<Connection>) node.getIncomingConnections()) {
                if (connec.isActivate() && connec.getLineStyle().equals(EConnectionType.FLOW_MAIN)
                        || connec.getLineStyle().equals(EConnectionType.TABLE)
                        || connec.getLineStyle().equals(EConnectionType.FLOW_MERGE)) {
                    if (connec.getLineStyle().equals(EConnectionType.FLOW_MERGE)) {
                        if (connec.getInputId() == 1) {
                            MetadataTool.copyTable(connec.getMetadataTable().clone(), metaCopy);
                            inputFound = true;
                            break;
                        }
                    } else {
                        MetadataTool.copyTable(connec.getMetadataTable().clone(), metaCopy);
                        inputFound = true;
                    }
                }
            }
            if (!inputFound) {
                List<IMetadataColumn> columnsToRemove = new ArrayList<IMetadataColumn>();
                for (IMetadataColumn column : metaCopy.getListColumns()) {
                    if (!column.isCustom()) {
                        columnsToRemove.add(column);
                    }
                }
                metaCopy.getListColumns().removeAll(columnsToRemove);
            }

            if (switchParam != null) {
                switchParam.setValue(Boolean.FALSE);
            }

            return new ChangeMetadataCommand(node, param, meta, metaCopy);
        }
        return null;
    }

    /**
     * DOC bqian Comment method "setColumnLength".
     * 
     * @param node
     * @param param
     * @param columnCopied
     */
    private void setColumnLength(Node node, IElementParameter param, IMetadataColumn columnCopied) {
        for (int i = 0; i < node.getElementParameters().size(); i++) {
            IElementParameter parameter = node.getElementParameters().get(i);
            if (parameter.getField() == EParameterFieldType.TABLE) {
                if (parameter.isBasedOnSchema()) {
                    List<Map<String, Object>> paramValues = (List<Map<String, Object>>) parameter.getValue();
                    for (Map<String, Object> columnInfo : paramValues) {
                        if (columnInfo.get("SCHEMA_COLUMN").equals(columnCopied.getLabel())) {
                            if (!ColumnListController.needSynchronizeSize(parameter)) {
                                return;
                            }
                            // codes[1] is SIZE;
                            String size = (String) columnInfo.get("SIZE");
                            int tmpSize = 0;
                            if (size == null || size.equals("")) {
                                tmpSize = -1;
                            } else {
                                tmpSize = Integer.parseInt(size);
                            }
                            columnCopied.setLength(tmpSize);
                            break;
                        }
                    }
                }
            }
        }
    }

    private boolean prepareReadOnlyTable(IMetadataTable table, boolean readOnlyParam, boolean readOnlyElement) {
        boolean isCustom = false;
        for (IMetadataColumn column : table.getListColumns()) {
            if (column.isCustom() && !column.isReadOnly()) {
                isCustom = true;
            }
        }
        if (!isCustom) {
            return readOnlyParam || readOnlyElement;
        }
        for (IMetadataColumn column : table.getListColumns()) {
            if (!column.isCustom()) {
                column.setReadOnly(table.isReadOnly());
            }
        }
        return readOnlyElement;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties.controllers.AbstractElementPropertySectionController#createControl(org.eclipse.swt.widgets.Composite,
     * org.talend.core.model.process.IElementParameter, int, int, int, org.eclipse.swt.widgets.Control)
     */
    @Override
    public Control createControl(Composite subComposite, IElementParameter param, int numInRow, int nbInRow, int top,
            Control lastControl) {
        CCombo combo;

        Control lastControlUsed = lastControl;

        if (elem instanceof Node) {
            combo = new CCombo(subComposite, SWT.BORDER);
            IElementParameter schemaTypeParameter = param.getChildParameters()
                    .get(EParameterName.SCHEMA_TYPE.getName());
            // elem.getElementParameter(EParameterName.SCHEMA_TYPE.getName());
            FormData data;
            String[] originalList = schemaTypeParameter.getListItemsDisplayName();
            List<String> stringToDisplay = new ArrayList<String>();
            for (int i = 0; i < originalList.length; i++) {
                stringToDisplay.add(originalList[i]);
            }
            combo.setItems(stringToDisplay.toArray(new String[0]));
            combo.setEditable(false);
            combo.setEnabled(!schemaTypeParameter.isReadOnly());
            // combo.addSelectionListener(listenerSelection);
            if (elem instanceof Node) {
                combo.setToolTipText(VARIABLE_TOOLTIP + schemaTypeParameter.getVariableName());
            }

            CLabel labelLabel = getWidgetFactory().createCLabel(subComposite, schemaTypeParameter.getDisplayName());
            data = new FormData();
            if (lastControl != null) {
                data.left = new FormAttachment(lastControl, 0);
            } else {
                data.left = new FormAttachment((((numInRow - 1) * MAX_PERCENT) / nbInRow), 0);
            }
            data.top = new FormAttachment(0, top);
            labelLabel.setLayoutData(data);
            if (numInRow != 1) {
                labelLabel.setAlignment(SWT.RIGHT);
            }
            // *********************
            data = new FormData();
            int currentLabelWidth = STANDARD_LABEL_WIDTH;
            GC gc = new GC(labelLabel);
            Point labelSize = gc.stringExtent(schemaTypeParameter.getDisplayName());
            gc.dispose();

            if ((labelSize.x + ITabbedPropertyConstants.HSPACE) > currentLabelWidth) {
                currentLabelWidth = labelSize.x + ITabbedPropertyConstants.HSPACE;
            }

            if (numInRow == 1) {
                if (lastControl != null) {
                    data.left = new FormAttachment(lastControl, currentLabelWidth);
                } else {
                    data.left = new FormAttachment(0, currentLabelWidth);
                }

            } else {
                data.left = new FormAttachment(labelLabel, 0, SWT.RIGHT);
            }
            data.top = new FormAttachment(0, top);
            combo.setLayoutData(data);
            combo.addSelectionListener(listenerSelection);
            combo.setData(PARAMETER_NAME, param.getName() + ":" + schemaTypeParameter.getName());
            lastControlUsed = combo;

            String schemaType = (String) schemaTypeParameter.getValue();
            if (schemaType != null && schemaType.equals(EmfComponent.REPOSITORY)) {
                lastControlUsed = addRepositoryCombo(subComposite, lastControlUsed, numInRow, top, param);
            }

            // **********************
            hashCurControls.put(param.getName() + ":" + schemaTypeParameter.getName(), combo);

            Point initialSize = combo.computeSize(SWT.DEFAULT, SWT.DEFAULT);
            dynamicTabbedPropertySection.setCurRowSize(initialSize.y + ITabbedPropertyConstants.VSPACE);
        }
        lastControlUsed = addButton(subComposite, param, lastControlUsed, numInRow, top);
        return lastControlUsed;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties.controllers.AbstractElementPropertySectionController#estimateRowSize(org.eclipse.swt.widgets.Composite,
     * org.talend.core.model.process.IElementParameter)
     */
    @Override
    public int estimateRowSize(Composite subComposite, IElementParameter param) {
        int comboSize, buttonSize;

        CCombo combo = new CCombo(subComposite, SWT.BORDER);
        IElementParameter schemaTypeParameter = param.getChildParameters().get(EParameterName.SCHEMA_TYPE.getName());
        // elem.getElementParameter(EParameterName.SCHEMA_TYPE.getName());
        String[] originalList = schemaTypeParameter.getListItemsDisplayName();
        combo.setItems(originalList);
        comboSize = combo.computeSize(SWT.DEFAULT, SWT.DEFAULT).y;
        combo.dispose();

        Button btn = getWidgetFactory().createButton(subComposite, "", SWT.PUSH); //$NON-NLS-1$
        buttonSize = btn.computeSize(SWT.DEFAULT, SWT.DEFAULT).y;
        btn.dispose();
        return Math.max(comboSize, buttonSize) + ITabbedPropertyConstants.VSPACE;
    }

    private Control addButton(Composite subComposite, IElementParameter param, Control lastControl, int numInRow,
            int top) {
        Button btn;
        Button resetBtn = null;
        Control lastControlUsed = lastControl;
        Point btnSize;
        FormData data;

        btn = getWidgetFactory().createButton(subComposite, "", SWT.PUSH); //$NON-NLS-1$
        btnSize = btn.computeSize(SWT.DEFAULT, SWT.DEFAULT);

        btn.setImage(CorePlugin.getImageDescriptor(DOTS_BUTTON).createImage());

        btn.addSelectionListener(listenerSelection);
        btn.setData(NAME, SCHEMA);
        btn.setData(PARAMETER_NAME, param.getName());

        lastControlUsed = btn;

        if (elem instanceof Node) {
            Node node = (Node) elem;
            boolean flowMainInput = false;
            for (IConnection connec : node.getIncomingConnections()) {
                if (connec.getLineStyle().equals(EConnectionType.FLOW_MAIN)
                        || connec.getLineStyle().equals(EConnectionType.TABLE)
                        || connec.getLineStyle().equals(EConnectionType.FLOW_MERGE)) {
                    flowMainInput = true;
                }
            }
            if (flowMainInput) {
                resetBtn = getWidgetFactory().createButton(subComposite,
                        Messages.getString("SchemaController.syncColumns"), SWT.PUSH); //$NON-NLS-1$
                resetBtn.setToolTipText(Messages.getString("SchemaController.resetButton.tooltip")); //$NON-NLS-1$

                Point resetBtnSize = resetBtn.computeSize(SWT.DEFAULT, SWT.DEFAULT);

                resetBtn.addSelectionListener(listenerSelection);
                data = new FormData();
                data.left = new FormAttachment(btn, 0);
                data.right = new FormAttachment(btn, resetBtnSize.x + ITabbedPropertyConstants.HSPACE, SWT.RIGHT);
                data.top = new FormAttachment(0, top);
                data.height = resetBtnSize.y;
                resetBtn.setLayoutData(data);
                resetBtn.setData(NAME, RESET_COLUMNS);
                resetBtn.setData(PARAMETER_NAME, param.getName());
                resetBtn.setEnabled(!param.isReadOnly());

                if (resetBtnSize.y > btnSize.y) {
                    btnSize.y = resetBtnSize.y;
                }

                lastControlUsed = resetBtn;
            }
        }

        CLabel labelLabel = getWidgetFactory().createCLabel(subComposite,
                Messages.getString("SchemaController.editSchema")); //$NON-NLS-1$
        data = new FormData();
        data.left = new FormAttachment(lastControl, 0);
        data.right = new FormAttachment(lastControl, labelLabel.computeSize(SWT.DEFAULT, SWT.DEFAULT).x
                + (ITabbedPropertyConstants.HSPACE * 2), SWT.RIGHT);
        if (resetBtn != null) {
            data.top = new FormAttachment(resetBtn, 0, SWT.CENTER);
        } else {
            data.top = new FormAttachment(0, top);
        }
        labelLabel.setLayoutData(data);
        if (numInRow != 1) {
            labelLabel.setAlignment(SWT.RIGHT);
        }

        data = new FormData();
        data.left = new FormAttachment(labelLabel, 0);
        data.right = new FormAttachment(labelLabel, STANDARD_BUTTON_WIDTH, SWT.RIGHT);
        if (resetBtn != null) {
            data.top = new FormAttachment(resetBtn, 0, SWT.CENTER);
        } else {
            data.top = new FormAttachment(0, top);
        }
        data.height = STANDARD_HEIGHT - 2;
        btn.setLayoutData(data);

        // curRowSize = btnSize.y + ITabbedPropertyConstants.VSPACE;
        dynamicTabbedPropertySection.setCurRowSize(btnSize.y + ITabbedPropertyConstants.VSPACE);
        return lastControlUsed;
    }

    private Control addRepositoryCombo(Composite subComposite, Control lastControl, int numInRow, int top,
            IElementParameter param) {
        IControlCreator cbCtrl;
        DecoratedField dField;
        Control cLayout;

        cbCtrl = new IControlCreator() {

            public Control createControl(final Composite parent, final int style) {
                CCombo cb = new CCombo(parent, style);
                return cb;
            }
        };
        FieldDecoration decoration = FieldDecorationRegistry.getDefault().getFieldDecoration(
                FieldDecorationRegistry.DEC_REQUIRED);
        dField = new DecoratedField(subComposite, SWT.BORDER, cbCtrl);
        dField.addFieldDecoration(decoration, SWT.RIGHT | SWT.TOP, false);
        cLayout = dField.getLayoutControl();
        ((CCombo) dField.getControl()).addSelectionListener(listenerSelection);
        ((CCombo) dField.getControl()).setEditable(false);
        cLayout.setBackground(subComposite.getBackground());

        FormData data = new FormData();
        data.left = new FormAttachment(lastControl, 0);
        data.top = new FormAttachment(0, top);
        cLayout.setLayoutData(data);
        CCombo combo = (CCombo) dField.getControl();
        IElementParameter repositorySchemaTypeParameter = param.getChildParameters().get(
                EParameterName.REPOSITORY_SCHEMA_TYPE.getName());
        // elem.getElementParameter(EParameterName.REPOSITORY_SCHEMA_TYPE.getName());
        combo.setData(PARAMETER_NAME, param.getName() + ":" + repositorySchemaTypeParameter.getName());
        hashCurControls.put(param.getName() + ":" + repositorySchemaTypeParameter.getName(), dField.getControl());
        dynamicTabbedPropertySection.updateRepositoryList();
        String[] paramItems = repositorySchemaTypeParameter.getListItemsDisplayName();
        // ControlUtils.setSortedValuesForCombo(combo, paramItems);
        combo.setItems(paramItems);

        return cLayout;
    }

    SelectionListener listenerSelection = new SelectionListener() {

        public void widgetDefaultSelected(SelectionEvent e) {
            // do nothing.
        }

        public void widgetSelected(SelectionEvent e) {
            Command cmd = createCommand(e);
            if (cmd != null) {
                getCommandStack().execute(cmd);
            }
        }
    };

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties.controllers.AbstractElementPropertySectionController#refresh(org.talend.core.model.process.IElementParameter,
     * boolean)
     */
    @Override
    public void refresh(IElementParameter param, boolean check) {
        IElementParameter schemaTypeParameter = param.getChildParameters().get(EParameterName.SCHEMA_TYPE.getName());
        // elem.getElementParameter(EParameterName.SCHEMA_TYPE.getName());
        CCombo combo = (CCombo) hashCurControls.get(param.getName() + ":" + schemaTypeParameter.getName());

        if (combo == null || combo.isDisposed()) {
            return;
        }
        Object value = schemaTypeParameter.getValue();

        if (value instanceof String) {
            String strValue = ""; //$NON-NLS-1$
            int nbInList = 0, nbMax = schemaTypeParameter.getListItemsValue().length;
            String name = (String) value;
            while (strValue.equals(new String("")) && nbInList < nbMax) { //$NON-NLS-1$
                if (name.equals(schemaTypeParameter.getListItemsValue()[nbInList])) {
                    strValue = schemaTypeParameter.getListItemsDisplayName()[nbInList];
                }
                nbInList++;
            }
            String[] paramItems = schemaTypeParameter.getListItemsDisplayName();
            String[] comboItems = combo.getItems();
            if (!paramItems.equals(comboItems)) {
                combo.setItems(paramItems);
            }
            combo.setText(strValue);
        }

        IElementParameter repositorySchemaTypeParameter = param.getChildParameters().get(
                EParameterName.REPOSITORY_SCHEMA_TYPE.getName());
        // elem.getElementParameter(EParameterName.REPOSITORY_SCHEMA_TYPE.getName());
        combo = (CCombo) hashCurControls.get(param.getName() + ":" + repositorySchemaTypeParameter.getName());

        if (combo == null) {
            return;
        }
        value = repositorySchemaTypeParameter.getValue();

        if (value instanceof String) {
            dynamicTabbedPropertySection.updateRepositoryList();
            String strValue = ""; //$NON-NLS-1$
            int nbInList = 0, nbMax = repositorySchemaTypeParameter.getListItemsValue().length;
            String name = (String) value;
            while (strValue.equals(new String("")) && nbInList < nbMax) { //$NON-NLS-1$
                if (name.equals(repositorySchemaTypeParameter.getListItemsValue()[nbInList])) {
                    strValue = repositorySchemaTypeParameter.getListItemsDisplayName()[nbInList];
                }
                nbInList++;
            }
            String[] paramItems = repositorySchemaTypeParameter.getListItemsDisplayName();
            String[] comboItems = combo.getItems();
            if (!Arrays.equals(paramItems, comboItems)) {
                combo.setItems(paramItems);
                // ControlUtils.setSortedValuesForCombo(combo, paramItems);
            }
            combo.setText(strValue);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent arg0) {
        // TODO Auto-generated method stub

    }

}
