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
package org.talend.designer.core.ui.editor.properties.macrowidgets.tableeditor;

import java.util.Map;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.talend.commons.ui.swt.advanced.dataeditor.AbstractDataTableEditorView;
import org.talend.commons.ui.swt.advanced.dataeditor.AbstractExtendedToolbar;
import org.talend.commons.ui.swt.proposal.TextCellEditorWithProposal;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreatorColumn;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator.LAYOUT_MODE;
import org.talend.commons.ui.swt.tableviewer.behavior.CellEditorValueAdapter;
import org.talend.commons.ui.swt.tableviewer.tableeditor.CheckboxTableEditorContent;
import org.talend.commons.utils.data.bean.IBeanPropertyAccessors;
import org.talend.core.model.process.Element;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.ui.proposal.ProcessProposalProvider;

/**
 * MetadataTableEditorView2 must be used.
 * 
 * $Id: MetadataTableEditorView.java 801 2006-11-30 16:28:36Z amaumont $
 * 
 * @param <B>
 */
public class PropertiesTableEditorView<B> extends AbstractDataTableEditorView<B> {

    /**
     * DOC amaumont MetadataTableEditorView constructor comment.
     * 
     * @param parent
     * @param style
     * @param model
     */
    public PropertiesTableEditorView(Composite parent, int style, PropertiesTableEditorModel model) {
        super(parent, style, model);
    }

    /**
     * DOC amaumont MetadataTableEditorView constructor comment.
     * 
     * @param parentComposite
     * @param mainCompositeStyle
     * @param readOnly
     * @param toolbarVisible
     * @param labelVisible TODO
     * @param extendedTableModel
     */
    public PropertiesTableEditorView(Composite parentComposite, int mainCompositeStyle, PropertiesTableEditorModel model, boolean readOnly,
            boolean toolbarVisible, boolean labelVisible) {
        super(parentComposite, mainCompositeStyle, model, readOnly, toolbarVisible, labelVisible);
    }

    /**
     * DOC amaumont MetadataTableEditorView constructor comment.
     * 
     * @param parentComposite
     * @param mainCompositeStyle
     */
    public PropertiesTableEditorView(Composite parentComposite, int mainCompositeStyle) {
        super(parentComposite, mainCompositeStyle);
    }

    public void setMetadataTableEditor(PropertiesTableEditorModel model) {
        setExtendedTableModel(model);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.swt.advanced.dataeditor.AbstractDataTableEditorView#initToolBar()
     */
    @Override
    protected AbstractExtendedToolbar initToolBar() {
        return new PropertiesTableToolbarEditorView(getMainComposite(), SWT.NONE, this.getExtendedTableViewer());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.swt.advanced.dataeditor.AbstractDataTableEditorView#setTableViewerCreatorOptions(org.talend.commons.ui.swt.tableviewer.TableViewerCreator)
     */
    @Override
    protected void setTableViewerCreatorOptions(TableViewerCreator<B> newTableViewerCreator) {
        super.setTableViewerCreatorOptions(newTableViewerCreator);
        newTableViewerCreator.setLayoutMode(LAYOUT_MODE.DEFAULT);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.swt.extended.macrotable.AbstractExtendedTableViewer#createColumns(org.talend.commons.ui.swt.tableviewer.TableViewerCreator,
     * org.eclipse.swt.widgets.Table)
     */
    @Override
    protected void createColumns(final TableViewerCreator<B> tableViewerCreator, final Table table) {

        // there's two lists of values, one that will be in the table
        // and the other will be stored as the current value in the property
        // there is two lists because of the undo / redo capabilities
        PropertiesTableEditorModel model = getModel();
        ProcessProposalProvider processProposalProvider = new ProcessProposalProvider(model.getProcess());
        String[] titles = model.getTitles();
        final Object[] itemsValue = model.getItemsValue();
        final String[] items = model.getItems();
        final Element elem = model.getElement();
        final IElementParameter param = model.getElemParameter();
        for (int i = 0; i < titles.length; i++) {
            if (param.isShow(model.getItemsShowIf()[i], model.getItemsNotShowIf()[i], model.getElement().getElementParameters())) {
                TableViewerCreatorColumn column = new TableViewerCreatorColumn(tableViewerCreator);
                column.setTitle(titles[i]);
                column.setModifiable(true);
                column.setMinimumWidth(100);
                column.setWeight(20);

                IElementParameter tmpParam = (IElementParameter) itemsValue[i];
                switch (tmpParam.getField()) {
                case CLOSED_LIST:
                case COLUMN_LIST:
                case PREV_COLUMN_LIST:
                    ComboBoxCellEditor cellEditor = new ComboBoxCellEditor(table, tmpParam.getListItemsDisplayName());
                    ((CCombo) cellEditor.getControl()).setEditable(false);
                    column.setCellEditor(cellEditor, new CellEditorValueAdapter() {

                        public String getColumnText(CellEditor cellEditor, Object cellEditorValue) {
                            return (String) cellEditorValue;
                        }

                        public Object getOriginalTypedValue(CellEditor cellEditor, Object cellEditorTypedValue) {
                            if (cellEditorTypedValue instanceof Integer) {
                                return cellEditorTypedValue;
                            }
                            return new Integer(0);
                        };

                        public Object getCellEditorTypedValue(CellEditor cellEditor, Object originalTypedValue) {
                            boolean found = false;
                            int nb = 0;

                            String[] namesSet = ((CCombo) cellEditor.getControl()).getItems();

                            for (int j = 0; j < namesSet.length && !found; j++) {
                                if (namesSet[j].equals(originalTypedValue)) {
                                    found = true;
                                    nb = j;
                                }
                            }
                            return new Integer(nb);
                        };
                    });
                    break;
                case CHECK:
                    column.setTableEditorContent(new CheckboxTableEditorContent(false));
                    column.setDisplayedValue("");
                    break;
                default: // TEXT
                    TextCellEditorWithProposal textCellEditor = new TextCellEditorWithProposal(table, column);
                    textCellEditor.setContentProposalProvider(processProposalProvider);
                    if ((i == 0) && (param.isBasedOnSchema())) {
                        Text text = (Text) textCellEditor.getControl();
                        text.setEditable(false);
                    }
                    column.setCellEditor(textCellEditor);
                }
                final int curCol = i;
                column.setBeanPropertyAccessors(new IBeanPropertyAccessors<B, Object>() {

                    public Object get(B bean) {
                        Object value = ((Map<String, Object>) bean).get(items[curCol]);
                        if (value == null) {
                            return "";
                        }
                        if (itemsValue[curCol] instanceof IElementParameter) {
                            IElementParameter tmpParam = (IElementParameter) itemsValue[curCol];
                            switch (tmpParam.getField()) {
                            case CLOSED_LIST:
                            case COLUMN_LIST:
                            case PREV_COLUMN_LIST:
                                String[] namesSet = tmpParam.getListItemsDisplayName();
                                if (namesSet.length == 0) {
                                    return (String) tmpParam.getDefaultClosedListValue();
                                }
                                if (value instanceof String) {
                                    boolean found = false;
                                    int index = 0;
                                    Object[] items = ((IElementParameter) itemsValue[curCol]).getListItemsValue();
                                    for (int j = 0; j < items.length && !found; j++) {
                                        if (items[j].equals(value)) {
                                            found = true;
                                            index = j;
                                        }
                                    }
                                    value = new Integer(index);
                                }
                                if (value != null && ((Integer) value) >= 0) {
                                    return namesSet[(Integer) value];
                                }
                                return null;
                            case CHECK:
                                if (value instanceof String) {
                                    return new Boolean((String) value);
                                }
                                return value;
                            default: // TEXT
                                return (String) value;
                            }
                        }
                        return (String) value;
                    }

                    public void set(B bean, Object value) {
                        ((Map<String, Object>) bean).put(items[curCol], value);
                        // List<Map<String, Object>> tableValues = new ArrayList<Map<String, Object>>();
                        // List<Map<String, Object>> paramValues = (List<Map<String, Object>>) param.getValue();
                        // int currentBeanIndex = paramValues.indexOf(bean);
                        // for (int currentIndex = 0; currentIndex < paramValues.size(); currentIndex++) {
                        // Map<String, Object> currentLine = paramValues.get(currentIndex);
                        //
                        // Map<String, Object> newLine = new HashMap<String, Object>();
                        // for (int i = 0; i < items.length; i++) {
                        // newLine.put(items[i], currentLine.get(items[i]));
                        // }
                        //
                        // if (currentIndex == currentBeanIndex) {
                        // newLine.put(items[curCol], value);
                        // }
                        // tableValues.add(newLine);
                        // }
                        // Command cmd = new PropertyChangeCommand(elem, param.getName(), tableValues);
                        // getCommandStack().execute(cmd);
                        // refresh();
                    }
                });
            }
        }
        // for (int i = 0; i < titles.length; i++) {
        // if (param.isShow(
        // model.getItemsShowIf()[i],
        // model.getItemsNotShowIf()[i],
        // model.getElement().getElementParameters()
        // )
        // ) {
        // TableViewerCreatorColumn column = new TableViewerCreatorColumn(tableViewerCreator);
        // column.setTitle(titles[i]);
        // column.setModifiable(true);
        // column.setMinimumWidth(100);
        // column.setWeight(20);
        //
        // if (itemsValue[i] instanceof IElementParameter) {
        // IElementParameter tmpParam = (IElementParameter) itemsValue[i];
        //
        // ComboBoxCellEditor cellEditor = new ComboBoxCellEditor(table, tmpParam.getListItemsDisplayName());
        // ((CCombo) cellEditor.getControl()).setEditable(false);
        // column.setCellEditor(cellEditor, new CellEditorValueAdapter() {
        //
        // public String getColumnText(CellEditor cellEditor, Object cellEditorValue) {
        // return (String) cellEditorValue;
        // }
        //
        // public Object getOriginalTypedValue(CellEditor cellEditor, Object cellEditorTypedValue) {
        // if (cellEditorTypedValue instanceof Integer) {
        // return cellEditorTypedValue;
        // }
        // return new Integer(0);
        // };
        //
        // public Object getCellEditorTypedValue(CellEditor cellEditor, Object originalTypedValue) {
        // boolean found = false;
        // int nb = 0;
        //
        // String[] namesSet = ((CCombo) cellEditor.getControl()).getItems();
        //
        // for (int j = 0; j < namesSet.length && !found; j++) {
        // if (namesSet[j].equals(originalTypedValue)) {
        // found = true;
        // nb = j;
        // }
        // }
        // return new Integer(nb);
        // };
        // });
        // } else {
        // TextCellEditorWithProposal textCellEditor = new TextCellEditorWithProposal(table, column);
        // textCellEditor.setContentProposalProvider(processProposalProvider);
        // if ((i == 0) && (param.isBasedOnSchema())) {
        // Text text = (Text) textCellEditor.getControl();
        // text.setEditable(false);
        // }
        // column.setCellEditor(textCellEditor);
        // }
        // final int curCol = i;
        // column.setBeanPropertyAccessors(new IBeanPropertyAccessors<ElemParamValueWrapper, Object>() {
        //
        // public String get(ElemParamValueWrapper bean) {
        // Object value = bean.getValue().get(items[curCol]);
        // if (value == null) {
        // return "";
        // }
        // if (itemsValue[curCol] instanceof IElementParameter) {
        // IElementParameter tmpParam = (IElementParameter) itemsValue[curCol];
        // String[] namesSet = tmpParam.getListItemsDisplayName();
        // if (namesSet.length == 0) {
        // return (String) tmpParam.getDefaultClosedListValue();
        // }
        // if (value instanceof String) {
        // boolean found = false;
        // int index = 0;
        // Object[] items = ((IElementParameter) itemsValue[curCol]).getListItemsValue();
        // for (int j = 0; j < items.length && !found; j++) {
        // if (items[j].equals(value)) {
        // found = true;
        // index = j;
        // }
        // }
        // value = new Integer(index);
        // }
        // return namesSet[(Integer) value];
        // }
        // return (String) value;
        // }
        //
        // public void set(ElemParamValueWrapper bean, Object value) {
        // List<Map<String, Object>> tableValues = new ArrayList<Map<String, Object>>();
        // List<Map<String, Object>> paramValues = (List<Map<String, Object>>) param.getValue();
        // int currentBeanIndex = table.getSelectionIndex();
        // for (int currentIndex = 0; currentIndex < paramValues.size(); currentIndex++) {
        // Map<String, Object> currentLine = paramValues.get(currentIndex);
        //
        // Map<String, Object> newLine = new HashMap<String, Object>();
        // for (int i = 0; i < items.length; i++) {
        // newLine.put(items[i], currentLine.get(items[i]));
        // }
        //
        // if (currentIndex == currentBeanIndex) {
        // newLine.put(items[curCol], value);
        // }
        // tableValues.add(newLine);
        // }
        // // Command cmd = new PropertyChangeCommand(elem, param.getName(), tableValues);
        // //
        // // CommandStack commandStack = (CommandStack)
        // getExtendedTableViewer().getCommandStackAdapter().getCommandStack();
        // // commandStack.execute(cmd);
        // // tableViewerCreator.getTableViewer().refresh();
        // }
        // });
        // }
    }

    public PropertiesTableToolbarEditorView getToolBar() {
        return (PropertiesTableToolbarEditorView) getExtendedToolbar();
    }

    public PropertiesTableEditorModel getModel() {
        return (PropertiesTableEditorModel) getExtendedTableModel();
    }

}
