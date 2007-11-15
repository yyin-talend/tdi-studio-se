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
package org.talend.repository.ui.wizards.metadata.connection.files.xml.extraction;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.talend.commons.ui.swt.advanced.dataeditor.AbstractDataTableEditorView;
import org.talend.commons.ui.swt.advanced.dataeditor.AbstractExtendedToolbar;
import org.talend.commons.ui.swt.advanced.dataeditor.ExtendedToolbarView;
import org.talend.commons.ui.swt.advanced.dataeditor.button.AddPushButtonForExtendedTable;
import org.talend.commons.ui.swt.advanced.dataeditor.button.PastePushButton;
import org.talend.commons.ui.swt.advanced.dataeditor.button.PastePushButtonForExtendedTable;
import org.talend.commons.ui.swt.advanced.dataeditor.commands.ExtendedTablePasteCommand;
import org.talend.commons.ui.swt.extended.table.ExtendedTableModel;
import org.talend.commons.ui.swt.proposal.TextCellEditorWithProposal;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreatorColumn;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator.CELL_EDITOR_STATE;
import org.talend.commons.ui.swt.tableviewer.behavior.CellEditorValueAdapter;
import org.talend.commons.ui.swt.tableviewer.celleditor.DialogErrorForCellEditorListener;
import org.talend.commons.utils.data.bean.IBeanPropertyAccessors;
import org.talend.commons.utils.data.list.ListenableListEvent;
import org.talend.core.model.metadata.builder.connection.SchemaTarget;
import org.talend.core.model.targetschema.editor.XmlExtractorFieldModel;
import org.talend.core.model.targetschema.editor.XmlExtractorLoopModel;
import org.talend.repository.i18n.Messages;

/**
 * DOC amaumont class global comment. Detailled comment <br/> TGU same purpose as TargetSchemaTableEditorView but uses
 * EMF model directly
 * 
 * $Id$
 * 
 */
public class ExtractionFieldsWithXPathEditorView extends AbstractDataTableEditorView<SchemaTarget> {

    public static final String ID_COLUMN_NAME = "ID_COLUMN_NAME"; //$NON-NLS-1$

    private TextCellEditorWithProposal xPathCellEditor;

    private TableViewerCreatorColumn xPathColumn;

    private XmlToXPathLinker linker;

    public ExtractionFieldsWithXPathEditorView(XmlExtractorFieldModel model, Composite parent, int styleChild) {
        this(model, parent, styleChild, false);
    }

    public ExtractionFieldsWithXPathEditorView(XmlExtractorFieldModel model, Composite parent) {
        this(model, parent, SWT.NONE, false);
    }

    /**
     * TargetSchemaTableEditorView2 constructor comment.
     * 
     * @param parent
     * @param styleChild
     * @param showDbTypeColumn
     */
    public ExtractionFieldsWithXPathEditorView(XmlExtractorFieldModel model, Composite parent, int styleChild, boolean showDbTypeColumn) {
        super(parent, styleChild, model);
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
     * @see org.talend.commons.ui.swt.advanced.dataeditor.AbstractDataTableEditorView#handleBeforeListenableListOperationEvent(org.talend.commons.utils.data.list.ListenableListEvent)
     */
    @Override
    protected void handleBeforeListenableListOperationEvent(ListenableListEvent<SchemaTarget> event) {
        super.handleBeforeListenableListOperationEvent(event);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.swt.extended.macrotable.AbstractExtendedTableViewer#handleListenableListEvent(org.talend.commons.utils.data.list.ListenableListEvent)
     */
    @Override
    protected void handleAfterListenableListOperationEvent(ListenableListEvent<SchemaTarget> event) {
        super.handleAfterListenableListOperationEvent(event);
        // if (event.type == TYPE.REMOVED) {
        if (linker != null) {
            linker.getBackgroundRefresher().refreshBackground();
        }
        // }
    }

    /*
     * (non-Javadoc)
     * 
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
    protected void createColumns(TableViewerCreator<SchemaTarget> tableViewerCreator, final Table table) {
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
                return ""; //$NON-NLS-1$
            }
        };

        // //////////////////////////////////////////////////////////////////////////////////////

        // column for mouse selection
        TableViewerCreatorColumn column = new TableViewerCreatorColumn(tableViewerCreator);
        column.setTitle(""); //$NON-NLS-1$
        column.setDefaultInternalValue(""); //$NON-NLS-1$
        column.setWidth(15);

        // //////////////////////////////////////////////////////////////////////////////////////
        // X Path Query

        column = new TableViewerCreatorColumn(tableViewerCreator);
        xPathColumn = column;
        column.setTitle(Messages.getString("ExtractionFieldsWithXPathEditorView.columnTitle.xPath")); //$NON-NLS-1$
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<SchemaTarget, String>() {

            public String get(SchemaTarget bean) {
                return bean.getRelativeXPathQuery();
            }

            public void set(SchemaTarget bean, String value) {
                bean.setRelativeXPathQuery(value);
            }
        });
        xPathCellEditor = new TextCellEditorWithProposal(tableViewerCreator.getTable(), SWT.NONE, column);
        column.setCellEditor(xPathCellEditor);
        xPathCellEditor.addListener(new DialogErrorForCellEditorListener(xPathCellEditor, column) {

            @Override
            public void newValidValueTyped(int itemIndex, Object previousValue, Object newValue, CELL_EDITOR_STATE state) {
                if (state == CELL_EDITOR_STATE.EDITING) {
                    linker.onXPathValueChanged(table, newValue.toString(), itemIndex);
                }

            }

            @Override
            public String validateValue(String newValue, int beanPosition) {
                String currentLoopXPath = linker.getCurrentLoopXPath();
                String value = null;
                if (newValue.trim().length() == 0) {
                    return null;
                } else if (newValue.trim().startsWith("/")) { //$NON-NLS-1$
                    value = newValue;
                } else {
                    value = currentLoopXPath + "/" + newValue; //$NON-NLS-1$
                }
                return linker.validateXPathExpression(value);
            }

        });
        column.setModifiable(true);
        column.setWeight(30);
        column.setMinimumWidth(50);
        column.setDefaultInternalValue(""); //$NON-NLS-1$
        // //////////////////////////////////////////////////////////////////////////////////////

        // //////////////////////////////////////////////////////////////////////////////////////
        // Tag Name
        column = new TableViewerCreatorColumn(tableViewerCreator);
        column.setTitle(Messages.getString("ExtractionFieldsWithXPathEditorView.columnTitle.columnName")); //$NON-NLS-1$
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
        column.setDefaultInternalValue(""); //$NON-NLS-1$

    }

    public XmlExtractorFieldModel getModel() {
        return (XmlExtractorFieldModel) getExtendedTableModel();
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
    public void setLinker(XmlToXPathLinker linker) {
        this.linker = linker;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.swt.advanced.dataeditor.AbstractDataTableEditorView#initToolBar()
     */
    @Override
    protected ExtendedToolbarView initToolBar() {
        return new ExtendedToolbarView(getMainComposite(), SWT.NONE, getExtendedTableViewer()) {

            @Override
            protected AddPushButtonForExtendedTable createAddPushButton() {
                return new AddPushButtonForExtendedTable(this.toolbar, getExtendedTableViewer()) {

                    @Override
                    protected Object getObjectToAdd() {
                        return getModel().createNewSchemaTarget();
                    }

                };
            }

            /* (non-Javadoc)
             * @see org.talend.core.ui.extended.ExtendedToolbarView#createPastePushButton()
             */
            @Override
            protected PastePushButton createPastePushButton() {
                return new PastePushButtonForExtendedTable(toolbar, extendedTableViewer) {

                    @Override
                    protected Command getCommandToExecute(ExtendedTableModel extendedTableModel, Integer indexWhereInsert) {
                         return new ExtendedTablePasteCommand(extendedTableModel, indexWhereInsert) {

                            @Override
                            public List createPastableBeansList(ExtendedTableModel extendedTableModel, List copiedObjectsList) {
                                ArrayList list = new ArrayList();
                                XmlExtractorFieldModel fieldsModel = (XmlExtractorFieldModel) extendedTableModel;
                                for (Object current : copiedObjectsList) {
                                    if (current instanceof SchemaTarget) {
                                        SchemaTarget original = (SchemaTarget) current;
                                        SchemaTarget copy = fieldsModel.createNewSchemaTarget();
                                        copy.setRelativeXPathQuery(original.getRelativeXPathQuery());
                                        copy.setTagName(original.getTagName());
                                        list.add(copy);
                                    }
                                }
                                return list;
                            }
                         };
                    }

                };
            }

            
            
        };

    }

}
