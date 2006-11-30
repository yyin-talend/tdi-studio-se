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
package org.talend.repository.ui.wizards.metadata.connection.files.xml;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.talend.commons.ui.swt.advanced.dataeditor.AbstractDataTableEditorView;
import org.talend.commons.ui.swt.proposal.TextCellEditorWithProposal;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreatorColumn;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator.CELL_EDITOR_STATE;
import org.talend.commons.ui.swt.tableviewer.behavior.CellEditorValueAdapter;
import org.talend.commons.ui.swt.tableviewer.celleditor.DialogErrorForCellEditorListener;
import org.talend.commons.ui.swt.tableviewer.tableeditor.CheckboxTableEditorContent;
import org.talend.commons.utils.data.bean.IBeanPropertyAccessors;
import org.talend.commons.utils.data.list.ListenableListEvent;
import org.talend.commons.utils.data.list.ListenableListEvent.TYPE;
import org.talend.core.model.metadata.builder.connection.SchemaTarget;
import org.talend.core.model.targetschema.editor.XPathNodeSchemaModel;
import org.talend.core.ui.extended.ExtendedToolbarView;

/**
 * DOC amaumont class global comment. Detailled comment <br/> TGU same purpose as TargetSchemaTableEditorView but uses
 * EMF model directly
 * 
 * $Id$
 * 
 */
public class XPathNodeSchemaEditorView extends AbstractDataTableEditorView<SchemaTarget> {

    private TableViewerCreator<SchemaTarget> tableViewerCreator;

    private ExtendedToolbarView targetSchemaToolbarEditorView2;

    public static final String ID_COLUMN_NAME = "ID_COLUMN_NAME";

    private boolean showDbTypeColumn = false;

    private TextCellEditorWithProposal xPathCellEditor;

    private TableViewerCreatorColumn xPathColumn;

    private XmlToSchemaLinker linker;

    private IBeanPropertyAccessors<SchemaTarget, Boolean> keyAccesor;

    public XPathNodeSchemaEditorView(XPathNodeSchemaModel model, Composite parent, int styleChild) {
        this(model, parent, styleChild, false);
    }

    public XPathNodeSchemaEditorView(XPathNodeSchemaModel model, Composite parent) {
        this(model, parent, SWT.NONE, false);
    }
    
    /**
     * TargetSchemaTableEditorView2 constructor comment.
     * 
     * @param parent
     * @param styleChild
     * @param showDbTypeColumn
     */
    public XPathNodeSchemaEditorView(XPathNodeSchemaModel model, Composite parent, int styleChild, boolean showDbTypeColumn) {
        super(parent, styleChild, model);
        this.showDbTypeColumn = showDbTypeColumn;
    }

    /**
     * Getter for xPathCellEditor.
     * 
     * @return the xPathCellEditor
     */
    public TextCellEditorWithProposal getXPathCellEditor() {
        return this.xPathCellEditor;
    }

    
    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.swt.extended.macrotable.AbstractExtendedTableViewer#handleListenableListEvent(org.talend.commons.utils.data.list.ListenableListEvent)
     */
    @Override
    protected void handleAfterListenableListOperationEvent(ListenableListEvent event) {
        super.handleAfterListenableListOperationEvent(event);
        if (event.type == TYPE.REMOVED) {
            linker.updateBackground();
        }
    }
    
    /* (non-Javadoc)
     * @see org.talend.commons.ui.swt.extended.macrotable.AbstractExtendedTableViewer#setTableViewerCreatorOptions(org.talend.commons.ui.swt.tableviewer.TableViewerCreator)
     */
    @Override
    protected void setTableViewerCreatorOptions(TableViewerCreator<SchemaTarget> newTableViewerCreator) {
        super.setTableViewerCreatorOptions(newTableViewerCreator);
        newTableViewerCreator.setFirstVisibleColumnIsSelection(true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.swt.advanced.macrotable.AbstractExtendedTableViewer#createColumns(org.talend.commons.ui.swt.tableviewer.TableViewerCreator,
     * org.eclipse.swt.widgets.Table)
     */
    @Override
    protected void createColumns(TableViewerCreator<SchemaTarget> tableViewerCreator, Table table) {
        CellEditorValueAdapter intValueAdapter = new CellEditorValueAdapter() {

            public Object getOriginalTypedValue(final CellEditor cellEditor, Object value) {
                try {
                    return new Integer(value.toString());
                } catch (Exception ex) {
                    return null;
                }
            }

            public Object getCellEditorTypedValue(final CellEditor cellEditor, Object value) {
                if (value != null) {
                    return String.valueOf(value);
                }
                return "";
            }
        };

        // //////////////////////////////////////////////////////////////////////////////////////

        // column for mouse selection
        TableViewerCreatorColumn column = new TableViewerCreatorColumn(tableViewerCreator);
        column.setTitle("");
        column.setDefaultInternalValue("");
        column.setWidth(15);

        // //////////////////////////////////////////////////////////////////////////////////////
        // X Path Query

        column = new TableViewerCreatorColumn(tableViewerCreator);
        xPathColumn = column;
        column.setTitle("XPath expression");
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<SchemaTarget, String>() {

            public String get(SchemaTarget bean) {
                return bean.getXPathQuery();
            }

            public void set(SchemaTarget bean, String value) {
                bean.setXPathQuery(value);
            }
        });
        xPathCellEditor = new TextCellEditorWithProposal(tableViewerCreator.getTable(), SWT.NONE, column);
        column.setCellEditor(xPathCellEditor);
        xPathCellEditor.addListener(new DialogErrorForCellEditorListener(xPathCellEditor, column) {

            @Override
            public void newValidValueTyped(int itemIndex, Object previousValue, Object newValue, CELL_EDITOR_STATE state) {
                if (state == CELL_EDITOR_STATE.EDITING) {
                    linker.onXPathValueChanged(previousValue.toString(), newValue.toString(), itemIndex);
                }

            }

            @Override
            public String validateValue(String newValue, int beanPosition) {
                XPathFactory xpf = XPathFactory.newInstance();
                XPath xpath = xpf.newXPath();
                try {
                    xpath.compile(newValue);
                } catch (Exception e) {
                    return e.getMessage();
                }
                return null;
            }

        });
        column.setModifiable(true);
        column.setWeight(30);
        column.setMinimumWidth(50);
        column.setDefaultInternalValue("");
        // //////////////////////////////////////////////////////////////////////////////////////

        // //////////////////////////////////////////////////////////////////////////////////////
        // Tag Name
        column = new TableViewerCreatorColumn(tableViewerCreator);
        column.setTitle("Tag Name");
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<SchemaTarget, String>() {

            public String get(SchemaTarget bean) {
                return bean.getTagName();
            }

            public void set(SchemaTarget bean, String value) {
                bean.setTagName(value);
            }

        });
        column.setModifiable(true);
        column.setWeight(10);
        column.setMinimumWidth(50);
        column.setCellEditor(new TextCellEditor(table));
        column.setDefaultInternalValue("");

        // //////////////////////////////////////////////////////////////////////////////////////
        // Loop
        column = new TableViewerCreatorColumn(tableViewerCreator);
        column.setTitle("Loop");
        this.keyAccesor = new IBeanPropertyAccessors<SchemaTarget, Boolean>() {

            public Boolean get(SchemaTarget bean) {
                return new Boolean(bean.isBoucle());
            }

            public void set(SchemaTarget bean, Boolean value) {
                bean.setBoucle(value);
            }

        };
        column.setBeanPropertyAccessors(this.keyAccesor);
        column.setDisplayedValue("");
        column.setWidth(38);
        CheckboxTableEditorContent checkboxTableEditorContent = new CheckboxTableEditorContent();
        column.setTableEditorContent(checkboxTableEditorContent);

        // //////////////////////////////////////////////////////////////////////////////////////
        // Loop limit
        column = new TableViewerCreatorColumn(tableViewerCreator);
        column.setTitle("Loop limit");
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<SchemaTarget, Integer>() {

            public Integer get(SchemaTarget bean) {
                return bean.getLimitBoucle();
            }

            public void set(SchemaTarget bean, Integer value) {
                bean.setLimitBoucle(value.intValue());
            }

        });
        column.setModifiable(true);
        column.setWidth(59);
        column.setCellEditor(new TextCellEditor(table), intValueAdapter);

    }

    public XPathNodeSchemaModel getXpathNodeSchemaModel() {
        return (XPathNodeSchemaModel) getExtendedTableModel();
    }

    /**
     * Getter for xPathColumn.
     * 
     * @return the xPathColumn
     */
    public TableViewerCreatorColumn getXPathColumn() {
        return this.xPathColumn;
    }

    /**
     * DOC amaumont Comment method "setLinker".
     * 
     * @param linker
     */
    public void setLinker(XmlToSchemaLinker linker) {
        this.linker = linker;
    }

}
