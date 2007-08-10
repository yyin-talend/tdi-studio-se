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

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreatorColumn;
import org.talend.commons.utils.data.list.IListenableListListener;
import org.talend.commons.utils.data.list.ListenableListEvent;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.utils.TalendTextUtils;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.ui.editor.connections.Connection;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.process.Process;
import org.talend.designer.core.ui.editor.properties.DynamicTabbedPropertySection;
import org.talend.designer.core.ui.editor.properties.macrowidgets.tableeditor.PropertiesTableEditorModel;
import org.talend.designer.core.ui.editor.properties.macrowidgets.tableeditor.PropertiesTableEditorView;
import org.talend.designer.runprocess.ProcessorUtilities;

/**
 * DOC yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: TableController.java 1 2006-12-14 下午05:44:30 +0000 (下午05:44:30) yzhang $
 * 
 */
public class TableController extends AbstractElementPropertySectionController {

    /**
     * 
     */
    private static final int MIN_NUMBER_ROWS = 8;

    /**
     * DOC yzhang TableController constructor comment.
     * 
     * @param dtp
     */
    public TableController(DynamicTabbedPropertySection dtp) {
        super(dtp);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties2.editors.AbstractElementPropertySectionController#
     * createControl(org.eclipse.swt.widgets.Composite, org.talend.core.model.process.IElementParameter, int, int, int,
     * org.eclipse.swt.widgets.Control)
     */
    @Override
    public Control createControl(final Composite parentComposite, final IElementParameter param, final int numInRow,
            final int nbInRow, int top, final Control lastControlPrm) {

        final Composite container = parentComposite;

        PropertiesTableEditorModel<Map<String, Object>> tableEditorModel = new PropertiesTableEditorModel<Map<String, Object>>();

        updateTableValues(param);

        tableEditorModel.setData(elem, param, part.getTalendEditor().getProcess());
        PropertiesTableEditorView<Map<String, Object>> tableEditorView = new PropertiesTableEditorView<Map<String, Object>>(
                parentComposite, SWT.NONE, tableEditorModel, !param.isBasedOnSchema(), false);
        tableEditorView.getExtendedTableViewer().setCommandStack(getCommandStack());
        tableEditorView.setReadOnly(param.isReadOnly());

        final Table table = tableEditorView.getTable();

        table.setToolTipText(VARIABLE_TOOLTIP + param.getVariableName());

        // add listener to tableMetadata (listen the event of the toolbars)
        tableEditorView.getExtendedTableModel().addAfterOperationListListener(new IListenableListListener() {

            public void handleEvent(ListenableListEvent event) {
                if (elem instanceof Node) {
                    Node node = (Node) elem;
                    node.checkAndRefreshNode();
                }
            }
        });
        final Composite mainComposite = tableEditorView.getMainComposite();

        CLabel labelLabel2 = getWidgetFactory().createCLabel(container, param.getDisplayName());
        FormData formData = new FormData();
        if (lastControlPrm != null) {
            formData.left = new FormAttachment(lastControlPrm, 0);
        } else {
            formData.left = new FormAttachment((((numInRow - 1) * MAX_PERCENT) / nbInRow), 0);
        }
        formData.top = new FormAttachment(0, top);
        labelLabel2.setLayoutData(formData);
        if (numInRow != 1) {
            labelLabel2.setAlignment(SWT.RIGHT);
        }
        // *********************
        formData = new FormData();
        int currentLabelWidth2 = STANDARD_LABEL_WIDTH;
        GC gc2 = new GC(labelLabel2);
        Point labelSize2 = gc2.stringExtent(param.getDisplayName());
        gc2.dispose();

        if ((labelSize2.x + ITabbedPropertyConstants.HSPACE) > currentLabelWidth2) {
            currentLabelWidth2 = labelSize2.x + ITabbedPropertyConstants.HSPACE;
        }

        int tableHorizontalOffset = -5;
        if (numInRow == 1) {
            if (lastControlPrm != null) {
                formData.left = new FormAttachment(lastControlPrm, currentLabelWidth2 + tableHorizontalOffset);
            } else {
                formData.left = new FormAttachment(0, currentLabelWidth2 + tableHorizontalOffset);
            }
        } else {
            formData.left = new FormAttachment(labelLabel2, 0 + tableHorizontalOffset, SWT.RIGHT);
        }
        formData.right = new FormAttachment((numInRow * MAX_PERCENT) / nbInRow, 0);
        formData.top = new FormAttachment(0, top);

        int currentHeightEditor = table.getHeaderHeight() + ((List) param.getValue()).size() * table.getItemHeight()
                + table.getItemHeight() + 50;
        int minHeightEditor = table.getHeaderHeight() + MIN_NUMBER_ROWS * table.getItemHeight() + table.getItemHeight()
                + 50;
        int ySize2 = Math.max(currentHeightEditor, minHeightEditor);

        formData.bottom = new FormAttachment(0, top + ySize2);
        mainComposite.setLayoutData(formData);

        hashCurControls.put(param.getName(), tableEditorView.getExtendedTableViewer().getTableViewerCreator());

        this.dynamicTabbedPropertySection.setCurRowSize(ySize2 + ITabbedPropertyConstants.VSPACE);

        top += this.dynamicTabbedPropertySection.getCurRowSize();

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.properties.controllers.AbstractElementPropertySectionController#estimateRowSize(org.eclipse.swt.widgets.Composite,
     * org.talend.core.model.process.IElementParameter)
     */
    @Override
    public int estimateRowSize(Composite subComposite, IElementParameter param) {
        PropertiesTableEditorModel<Map<String, Object>> tableEditorModel = new PropertiesTableEditorModel<Map<String, Object>>();

        updateTableValues(param);

        tableEditorModel.setData(elem, param, part.getTalendEditor().getProcess());
        PropertiesTableEditorView<Map<String, Object>> tableEditorView = new PropertiesTableEditorView<Map<String, Object>>(
                subComposite, SWT.NONE, tableEditorModel, !param.isBasedOnSchema(), false);
        tableEditorView.getExtendedTableViewer().setCommandStack(getCommandStack());
        tableEditorView.setReadOnly(param.isReadOnly());

        final Table table = tableEditorView.getTable();
        int currentHeightEditor = table.getHeaderHeight() + ((List) param.getValue()).size() * table.getItemHeight()
                + table.getItemHeight() + 50;
        int minHeightEditor = table.getHeaderHeight() + MIN_NUMBER_ROWS * table.getItemHeight() + table.getItemHeight()
                + 50;

        tableEditorView.getMainComposite().dispose();

        int ySize2 = Math.max(currentHeightEditor, minHeightEditor);
        return ySize2 + ITabbedPropertyConstants.VSPACE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void refresh(IElementParameter param, boolean check) {
        TableViewerCreator tableViewerCreator = (TableViewerCreator) hashCurControls.get(param.getName());
        Object value = param.getValue();
        if (value instanceof List) {
            updateTableValues(param);
            if (tableViewerCreator != null) {
                if (!tableViewerCreator.getInputList().equals(value)) {
                    tableViewerCreator.init((List) value);
                }
                tableViewerCreator.getTableViewer().refresh();
            }
        }
    }

    private void updateTableValues(IElementParameter param) {
        if (elem instanceof Node) {
            DbTypeListController.updateDbTypeList((Node) elem, null);
        } else if (elem instanceof Connection) {
            DbTypeListController.updateDbTypeList(((Connection) elem).getSource(), null);
        }
        updateColumnList(param);
        updateContextList(param);
        updateConnectionList(param);
        updateComponentList(param);
    }

    private void updateColumnList(IElementParameter param) {
        if (elem instanceof Node) {
            ColumnListController.updateColumnList((Node) elem, null);
        } else if (elem instanceof Connection) {
            ColumnListController.updateColumnList(((Connection) elem).getSource(), null);
        }

        TableViewerCreator tableViewerCreator = (TableViewerCreator) hashCurControls.get(param.getName());
        Object[] itemsValue = (Object[]) param.getListItemsValue();
        if (tableViewerCreator != null) {
            List colList = tableViewerCreator.getColumns();
            for (int j = 0; j < itemsValue.length; j++) {
                if (itemsValue[j] instanceof IElementParameter) {
                    IElementParameter tmpParam = (IElementParameter) itemsValue[j];
                    if (tmpParam.getField() == EParameterFieldType.COLUMN_LIST
                            || tmpParam.getField() == EParameterFieldType.PREV_COLUMN_LIST
                            || tmpParam.getField() == EParameterFieldType.LOOKUP_COLUMN_LIST) {
                        TableViewerCreatorColumn column = (TableViewerCreatorColumn) colList.get(j + 1);

                        CCombo combo = (CCombo) column.getCellEditor().getControl();
                        String[] oldItems = combo.getItems();
                        combo.setItems(tmpParam.getListItemsDisplayName());

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
        }
    }

    private void updateConnectionList(IElementParameter param) {
        // update table values
        TableViewerCreator tableViewerCreator = (TableViewerCreator) hashCurControls.get(param.getName());
        Object[] itemsValue = (Object[]) param.getListItemsValue();
        if (tableViewerCreator != null) {
            List colList = tableViewerCreator.getColumns();
            for (int j = 0; j < itemsValue.length; j++) {
                if (itemsValue[j] instanceof IElementParameter) {
                    IElementParameter tmpParam = (IElementParameter) itemsValue[j];
                    if (tmpParam.getField() == EParameterFieldType.CONNECTION_LIST) {
                        String[] contextParameterNames = null;

                        ConnectionListController.updateConnectionList(elem, tmpParam, tmpParam.getFilter());
                        contextParameterNames = tmpParam.getListItemsDisplayName();
                        tmpParam.setListItemsDisplayCodeName(contextParameterNames);
                        // tmpParam.setListItemsDisplayName(contextParameterNames);
                        // tmpParam.setListItemsValue(contextParameterNames);
                        if (contextParameterNames.length > 0) {
                            tmpParam.setDefaultClosedListValue(contextParameterNames[0]);
                        } else {
                            tmpParam.setDefaultClosedListValue(""); //$NON-NLS-1$
                        }
                        // j + 1 because first column is masked
                        TableViewerCreatorColumn column = (TableViewerCreatorColumn) colList.get(j + 1);

                        CCombo combo = (CCombo) column.getCellEditor().getControl();
                        String[] oldItems = combo.getItems();
                        combo.setItems(contextParameterNames);

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
        }
    }

    private void updateComponentList(IElementParameter param) {
        // update table values
        TableViewerCreator tableViewerCreator = (TableViewerCreator) hashCurControls.get(param.getName());
        Object[] itemsValue = (Object[]) param.getListItemsValue();
        if (tableViewerCreator != null) {
            List colList = tableViewerCreator.getColumns();
            for (int j = 0; j < itemsValue.length; j++) {
                if (itemsValue[j] instanceof IElementParameter) {
                    IElementParameter tmpParam = (IElementParameter) itemsValue[j];
                    if (tmpParam.getField() == EParameterFieldType.COMPONENT_LIST) {
                        String[] contextParameterNames = null;
                        ComponentListController.updateComponentList(elem, tmpParam);
                        contextParameterNames = tmpParam.getListItemsDisplayName();
                        tmpParam.setListItemsDisplayCodeName(contextParameterNames);
                        // tmpParam.setListItemsDisplayName(contextParameterNames);
                        // tmpParam.setListItemsValue(contextParameterNames);
                        if (contextParameterNames.length > 0) {
                            tmpParam.setDefaultClosedListValue(contextParameterNames[0]);
                        } else {
                            tmpParam.setDefaultClosedListValue(""); //$NON-NLS-1$
                        }
                        // j + 1 because first column is masked
                        TableViewerCreatorColumn column = (TableViewerCreatorColumn) colList.get(j + 1);

                        CCombo combo = (CCombo) column.getCellEditor().getControl();
                        String[] oldItems = combo.getItems();
                        combo.setItems(contextParameterNames);

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
        }
    }

    private void updateContextList(IElementParameter param) {
        List<String> contextParameterNamesList = new ArrayList<String>();

        // get context list
        String processName = (String) elem.getPropertyValue(EParameterName.PROCESS_TYPE_PROCESS.getName());
        String contextName = (String) elem.getPropertyValue(EParameterName.PROCESS_TYPE_CONTEXT.getName());

        if (processName == null || contextName == null) {
            return;
        }

        processName = processName.replaceAll("'", "");
        contextName = contextName.replaceAll("'", "");

        ProcessItem processItem = ProcessorUtilities.getProcessItem(processName);
        Process process = null;
        String[] contextParameterNames = null;
        if (processItem != null) {
            process = new Process(processItem.getProperty());
            process.loadXmlFile(processItem.getProcess());
            IContext context = process.getContextManager().getContext(contextName);

            for (IContextParameter contextParam : context.getContextParameterList()) {
                contextParameterNamesList.add(contextParam.getName());
            }

            contextParameterNames = contextParameterNamesList.toArray(new String[0]);
        } else {
            contextParameterNames = new String[0];
        }
        // update table values
        TableViewerCreator tableViewerCreator = (TableViewerCreator) hashCurControls.get(param.getName());
        Object[] itemsValue = (Object[]) param.getListItemsValue();
        if (tableViewerCreator != null) {
            List colList = tableViewerCreator.getColumns();
            for (int j = 0; j < itemsValue.length; j++) {
                if (itemsValue[j] instanceof IElementParameter) {
                    IElementParameter tmpParam = (IElementParameter) itemsValue[j];
                    if (tmpParam.getField() == EParameterFieldType.CONTEXT_PARAM_NAME_LIST) {
                        tmpParam.setListItemsDisplayCodeName(contextParameterNames);
                        tmpParam.setListItemsDisplayName(contextParameterNames);
                        tmpParam.setListItemsValue(contextParameterNames);
                        if (contextParameterNames.length > 0) {
                            tmpParam.setDefaultClosedListValue(contextParameterNames[0]);
                        } else {
                            tmpParam.setDefaultClosedListValue(""); //$NON-NLS-1$
                        }
                        // j + 1 because first column is masked
                        TableViewerCreatorColumn column = (TableViewerCreatorColumn) colList.get(j + 1);

                        CCombo combo = (CCombo) column.getCellEditor().getControl();
                        String[] oldItems = combo.getItems();
                        combo.setItems(contextParameterNames);

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
        }
    }

    public static Map<String, Object> createNewLine(IElementParameter param) {
        Map<String, Object> line = new HashMap<String, Object>();
        String[] items = (String[]) param.getListItemsDisplayCodeName();
        Object[] itemsValue = (Object[]) param.getListItemsValue();
        IElementParameter tmpParam;

        tmpParam = (IElementParameter) itemsValue[0];
        switch (tmpParam.getField()) {
        case CONTEXT_PARAM_NAME_LIST:
        case CLOSED_LIST:
        case COLUMN_LIST:
        case COMPONENT_LIST:
        case CONNECTION_LIST:
        case DBTYPE_LIST:
        case LOOKUP_COLUMN_LIST:
        case PREV_COLUMN_LIST:
            line.put(items[0], new Integer(tmpParam.getIndexOfItemFromList((String) tmpParam
                    .getDefaultClosedListValue())));
            break;
        case COLOR:
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
            case CONTEXT_PARAM_NAME_LIST:
            case CLOSED_LIST:
            case DBTYPE_LIST:
            case COLUMN_LIST:
            case COMPONENT_LIST:
            case CONNECTION_LIST:
            case LOOKUP_COLUMN_LIST:
            case PREV_COLUMN_LIST:
                line.put(items[i], new Integer(tmpParam.getIndexOfItemFromList((String) tmpParam
                        .getDefaultClosedListValue())));
                break;
            default: // TEXT or CHECK or COLOR (means String or Boolean)
                line.put(items[i], tmpParam.getValue());
            }
        }
        return line;
    }
}
