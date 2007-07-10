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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.talend.core.model.metadata.ColumnNameChanged;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.MetadataTable;
import org.talend.core.model.metadata.MetadataTool;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.ui.editor.cmd.PropertyChangeCommand;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.properties.DynamicTabbedPropertySection;

/**
 * DOC yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: ColumnListController.java 1 2006-12-12 下午02:04:32 +0000 (下午02:04:32) yzhang $
 * 
 */
public class ColumnListController extends AbstractElementPropertySectionController {

    private boolean updateColumnListFlag;

    private Map<String, IMetadataTable> repositoryTableMap;

    private Map<String, ConnectionItem> repositoryConnectionItemMap;

    /**
     * DOC dev ColumnListController constructor comment.
     * 
     * @param parameterBean
     */
    public ColumnListController(DynamicTabbedPropertySection dtp) {
        super(dtp);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties2.editors.AbstractElementPropertySectionController#createCommand()
     */
    public Command createCommand(SelectionEvent selectionEvent) {
        repositoryTableMap = dynamicTabbedPropertySection.getRepositoryTableMap();
        repositoryConnectionItemMap = dynamicTabbedPropertySection.getRepositoryConnectionItemMap();

        Set<String> elementsName;
        Control ctrl;
        IMetadataTable repositoryMetadata = new MetadataTable();
        Connection repositoryConnection = null;

        elementsName = hashCurControls.keySet();
        for (String name : elementsName) {
            Object o = hashCurControls.get(name);
            if (o instanceof Control) {
                ctrl = (Control) o;
                if (ctrl == null) {
                    hashCurControls.remove(name);
                    return null;
                }

                if (ctrl.equals(selectionEvent.getSource()) && ctrl instanceof CCombo) {
                    boolean isDisposed = ((CCombo) ctrl).isDisposed();
                    if (!isDisposed && (!elem.getPropertyValue(name).equals(((CCombo) ctrl).getText()))) {

                        String value = new String(""); //$NON-NLS-1$
                        for (int i = 0; i < elem.getElementParameters().size(); i++) {
                            IElementParameter param = elem.getElementParameters().get(i);
                            if (param.getName().equals(name)) {
                                for (int j = 0; j < param.getListItemsValue().length; j++) {
                                    if (((CCombo) ctrl).getText().equals(param.getListItemsDisplayName()[j])) {
                                        value = (String) param.getListItemsValue()[j];
                                    }
                                }
                            }
                        }
                        if (value.equals(elem.getPropertyValue(name))) { // same value so no need to do anything
                            return null;
                        }

                        return new PropertyChangeCommand(elem, name, value);
                    }
                }

            }
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties2.editors.AbstractElementPropertySectionController#createControl()
     */
    @Override
    public Control createControl(final Composite subComposite, final IElementParameter param, final int numInRow,
            final int nbInRow, final int top, final Control lastControl) {
        if (param.getDisplayName().startsWith("!!")) {
            if (param.getField() == EParameterFieldType.COLUMN_LIST) {
                param.setDisplayName(EParameterName.COLUMN_LIST.getDisplayName());
            } else if (param.getField() == EParameterFieldType.PREV_COLUMN_LIST) {
                param.setDisplayName(EParameterName.PREV_COLUMN_LIST.getDisplayName());
            } else if (param.getField() == EParameterFieldType.LOOKUP_COLUMN_LIST) {
                param.setDisplayName(EParameterName.LOOKUP_COLUMN_LIST.getDisplayName());
            }
        }

        // Button refreshBtn;
        // refreshBtn = getWidgetFactory().createButton(subComposite, "", SWT.PUSH); //$NON-NLS-1$
        // refreshBtn.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        //
        // // refreshBtn.setImage(CorePlugin.getImageDescriptor(REFRESH_BUTTON).createImage());
        //
        // refreshBtn.addSelectionListener(listenerSelection);
        // refreshBtn.setData(NAME, COLUMN);
        // refreshBtn.setData(PROPERTY, param.getName());
        // refreshBtn.setEnabled(!param.isReadOnly());

        IControlCreator cbCtrl = new IControlCreator() {

            public Control createControl(final Composite parent, final int style) {
                CCombo cb = new CCombo(parent, style);
                return cb;
            }
        };
        DecoratedField dField = new DecoratedField(subComposite, SWT.BORDER, cbCtrl);
        if (param.isRequired()) {
            FieldDecoration decoration = FieldDecorationRegistry.getDefault().getFieldDecoration(
                    FieldDecorationRegistry.DEC_REQUIRED);
            dField.addFieldDecoration(decoration, SWT.RIGHT | SWT.TOP, false);
        }

        Control cLayout = dField.getLayoutControl();
        CCombo combo = (CCombo) dField.getControl();
        FormData data;
        combo.setEditable(false);
        cLayout.setBackground(subComposite.getBackground());
        combo.setEnabled(!param.isReadOnly());
        combo.addSelectionListener(listenerSelection);
        if (elem instanceof Node) {
            combo.setToolTipText(VARIABLE_TOOLTIP + param.getVariableName());
        }

        CLabel labelLabel = getWidgetFactory().createCLabel(subComposite, param.getDisplayName());
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
        Point labelSize = gc.stringExtent(param.getDisplayName());
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
        cLayout.setLayoutData(data);
        Point initialSize = dField.getLayoutControl().computeSize(SWT.DEFAULT, SWT.DEFAULT);

        // data = new FormData();
        // data.left = new FormAttachment(cLayout, 0, SWT.RIGHT);
        // data.top = new FormAttachment(0, top);
        // data.height = initialSize.y;
        //
        // refreshBtn.setLayoutData(data);

        // **********************
        hashCurControls.put(param.getName(), combo);
        updateData();
        // this.dynamicTabbedPropertySection.updateColumnList(null);

        dynamicTabbedPropertySection.setCurRowSize(initialSize.y + ITabbedPropertyConstants.VSPACE);
        return cLayout;
    }

    private void updateData() {
        if (elem instanceof Node) {
            updateColumnList((Node) elem, null);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent evt) {
        // TODO Auto-generated method stub
    }

    SelectionListener listenerSelection = new SelectionAdapter() {

        public void widgetSelected(SelectionEvent event) {
            // updateRepositoryList();
            dynamicTabbedPropertySection.updateRepositoryList();
            Command cmd = createCommand(event);
            if (cmd != null) {
                getCommandStack().execute(cmd);
                if (updateColumnListFlag) {
                    updateData();
                }
            }
        }
    };

    @Override
    public void refresh(IElementParameter param, boolean check) {
        updateData();

        String[] curColumnNameList = param.getListItemsDisplayName();
        String[] curColumnValueList = (String[]) param.getListItemsValue();

        Object value = param.getValue();
        boolean listContainValue = false;
        int numValue = 0;
        for (int i = 0; i < curColumnValueList.length && !listContainValue; i++) {
            if (curColumnValueList[i].equals(value)) {
                listContainValue = true;
                numValue = i;
            }
        }

        CCombo combo = (CCombo) hashCurControls.get(param.getName());

        combo.setItems(curColumnNameList);
        if (!listContainValue) {
            if (curColumnNameList.length > 0) {
                elem.setPropertyValue(param.getName(), curColumnValueList[0]);
                combo.setText(curColumnNameList[0]);
            }
        } else {
            combo.setText(curColumnNameList[numValue]);
        }
    }
    
    public static void updateColumnList(INode node, List<ColumnNameChanged> columnsChanged) {
        List<String> columnList = getColumnList(node);
        List<String> prevColumnList = getPrevColumnList(node);
        Map<IConnection, List<String>> refColumnLists = getRefColumnLists(node);

        String[] columnNameList = (String[]) columnList.toArray(new String[0]);
        String[] prevColumnNameList = (String[]) prevColumnList.toArray(new String[0]);
        String[] curColumnNameList = null;
        String[] curColumnValueList = null;

        List<String> refColumnListNamesTmp = new ArrayList<String>();
        List<String> refColumnListValuesTmp = new ArrayList<String>();
        for (IConnection connection : refColumnLists.keySet()) {
            String name = connection.getName() + ".";
            String value = connection.getSource().getUniqueName() + ".";
            for (String column : refColumnLists.get(connection)) {
                refColumnListNamesTmp.add(name + column);
                refColumnListValuesTmp.add(value + column);
            }
        }
        String[] refColumnListNames = (String[]) refColumnListNamesTmp.toArray(new String[0]);
        String[] refColumnListValues = (String[]) refColumnListValuesTmp.toArray(new String[0]);
        for (int i = 0; i < node.getElementParameters().size(); i++) {
            IElementParameter param = node.getElementParameters().get(i);
            if (param.getField() == EParameterFieldType.COLUMN_LIST) {
                curColumnNameList = columnNameList;
                curColumnValueList = columnNameList;
            }
            if (param.getField() == EParameterFieldType.PREV_COLUMN_LIST) {
                curColumnNameList = prevColumnNameList;
                curColumnValueList = prevColumnNameList;
            }
            if (param.getField() == EParameterFieldType.LOOKUP_COLUMN_LIST) {
                curColumnNameList = refColumnListNames;
                curColumnValueList = refColumnListValues;
            }
            if (param.getField() == EParameterFieldType.COLUMN_LIST || param.getField() == EParameterFieldType.PREV_COLUMN_LIST
                    || param.getField() == EParameterFieldType.LOOKUP_COLUMN_LIST) {
                param.setListItemsDisplayName(curColumnNameList);
                param.setListItemsValue(curColumnValueList);
            }
            if (param.getField() == EParameterFieldType.TABLE) {
                Object[] itemsValue = (Object[]) param.getListItemsValue();
                for (int j = 0; j < itemsValue.length; j++) {
                    if (itemsValue[j] instanceof IElementParameter) {
                        IElementParameter tmpParam = (IElementParameter) itemsValue[j];
                        if (tmpParam.getField() == EParameterFieldType.COLUMN_LIST) {
                            curColumnNameList = columnNameList;
                            curColumnValueList = columnNameList;
                        }
                        if (tmpParam.getField() == EParameterFieldType.PREV_COLUMN_LIST) {
                            curColumnNameList = prevColumnNameList;
                            curColumnValueList = prevColumnNameList;
                        }
                        if (tmpParam.getField() == EParameterFieldType.LOOKUP_COLUMN_LIST) {
                            curColumnNameList = refColumnListNames;
                            curColumnValueList = refColumnListValues;
                        }
                        if (tmpParam.getField() == EParameterFieldType.COLUMN_LIST
                                || tmpParam.getField() == EParameterFieldType.PREV_COLUMN_LIST
                                || tmpParam.getField() == EParameterFieldType.LOOKUP_COLUMN_LIST) {
                            tmpParam.setListItemsDisplayCodeName(curColumnNameList);
                            tmpParam.setListItemsDisplayName(curColumnNameList);
                            tmpParam.setListItemsValue(curColumnValueList);
                            if (curColumnValueList.length > 0) {
                                tmpParam.setDefaultClosedListValue(curColumnValueList[0]);
                            } else {
                                tmpParam.setDefaultClosedListValue(""); //$NON-NLS-1$
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
                        newLine = TableController.createNewLine(param);
                        newLine.put(codes[0], columnName);
                    }
                    newParamValues.add(j, newLine);
                }
                paramValues.clear();
                paramValues.addAll(newParamValues);

            }
        }
    }
    
    private static List<String> getColumnList(INode node) {
        List<String> columnList = new ArrayList<String>();

        if (node.getMetadataList().size() > 0) {
            IMetadataTable table = node.getMetadataList().get(0);
            for (IMetadataColumn column : table.getListColumns()) {
                columnList.add(column.getLabel());
            }
        }

        return columnList;
    }

    private static List<String> getPrevColumnList(INode node) {
        List<String> columnList = new ArrayList<String>();

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

        return columnList;
    }

    private static Map<IConnection, List<String>> getRefColumnLists(INode node) {
        Map<IConnection, List<String>> refColumnLists = new HashMap<IConnection, List<String>>();

        List<IConnection> refConnections = new ArrayList<IConnection>();
        for (int i = 0; i < node.getIncomingConnections().size(); i++) {
            IConnection curConnec = node.getIncomingConnections().get(i);
            if (curConnec.getLineStyle() == EConnectionType.FLOW_REF) {
                refConnections.add(curConnec);
            }
        }
        for (IConnection connection : refConnections) {
            List<String> columnList = new ArrayList<String>();
            IMetadataTable table = connection.getMetadataTable();
            for (IMetadataColumn column : table.getListColumns()) {
                columnList.add(column.getLabel());
            }
            refColumnLists.put(connection, columnList);
        }
        return refColumnLists;
    }

}
