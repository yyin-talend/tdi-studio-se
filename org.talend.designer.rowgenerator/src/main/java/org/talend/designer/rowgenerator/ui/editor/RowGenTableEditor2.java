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
package org.talend.designer.rowgenerator.ui.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.talend.commons.ui.image.EImage;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.commons.ui.swt.advanced.dataeditor.AbstractDataTableEditorView;
import org.talend.commons.ui.swt.advanced.dataeditor.ExtendedToolbarView;
import org.talend.commons.ui.swt.extended.table.ExtendedTableModel;
import org.talend.commons.ui.swt.tableviewer.CellEditorValueAdapterFactory;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreatorColumn;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator.CELL_EDITOR_STATE;
import org.talend.commons.ui.swt.tableviewer.behavior.CellEditorValueAdapter;
import org.talend.commons.ui.swt.tableviewer.behavior.IColumnImageProvider;
import org.talend.commons.ui.swt.tableviewer.celleditor.DialogErrorForCellEditorListener;
import org.talend.commons.ui.swt.tableviewer.tableeditor.CheckboxTableEditorContent;
import org.talend.commons.utils.data.bean.IBeanPropertyAccessors;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.MetadataColumn;
import org.talend.core.model.metadata.MetadataTalendType;
import org.talend.core.model.temp.ECodeLanguage;
import org.talend.designer.rowgenerator.RowGeneratorComponent;
import org.talend.designer.rowgenerator.data.Function;
import org.talend.designer.rowgenerator.data.FunctionManager;
import org.talend.designer.rowgenerator.data.Parameter;

/**
 * qzhang class global comment. Detailled comment <br/>
 * 
 * $Id: RowGenTableEditor2.java,v 1.12 2007/01/31 10:31:05 pub Exp $
 * 
 */
public class RowGenTableEditor2 extends AbstractDataTableEditorView<IMetadataColumn> {

    private static final String ID_COLUMN_NAME = "ID_COLUMN_NAME";

    private static final String ID_COLUMN_KEY = "ID_COLUMN_KEY";

    private RowGeneratorComponent rGcomponent;

    /**
     * qzhang RowGenMetadataTableEditorExt constructor comment.
     * 
     * @param parentComposite
     * @param mainCompositeStyle
     * @param extendedTableModel
     * @param readOnly
     * @param toolbarVisible
     */
    public RowGenTableEditor2(Composite parentComposite, int mainCompositeStyle,
            ExtendedTableModel<IMetadataColumn> extendedTableModel, boolean readOnly, boolean toolbarVisible,
            RowGeneratorComponent rGcomponent) {
        super(parentComposite, mainCompositeStyle, extendedTableModel, readOnly, toolbarVisible, true);
        this.rGcomponent = rGcomponent;
    }

    public RowGenTableEditor2(Composite parentComposite, int mainCompositeStyle) {
        super(parentComposite, mainCompositeStyle);
    }

    @SuppressWarnings("unchecked")
    public void saveOneColData(MetadataColumnExt bean) {
        if (bean != null && bean.getFunction() != null && rGcomponent != null) {
            String newValue = "sub{";
            newValue += bean.getFunction().getName() + "(";
            for (Parameter pa : (List<Parameter>) bean.getFunction().getParameters()) {
                newValue += pa.getValue() + ",";
            }
            newValue = newValue.substring(0, newValue.length() - 1);
            newValue += ")}";
            if (bean.getFunction().getName() == null || "".equals(bean.getFunction().getName())) {
                newValue = "";
            }
            rGcomponent.setColumnValue(bean.getLabel(), newValue);
        }
        
    }

    @Override
    protected void createColumns(TableViewerCreator<IMetadataColumn> tableViewerCreator, Table table) {
        CellEditorValueAdapter comboValueAdapter = null;
        TableViewerCreatorColumn column;
        orignAllColumns(tableViewerCreator);

        // ///////////////////////////////////////////////////////////////////////
        comboValueAdapter = CellEditorValueAdapterFactory.getComboAdapter("String");
        column = new TableViewerCreatorColumn(tableViewerCreator);
        final ComboBoxCellEditor functComboBox = new ComboBoxCellEditor();
        functComboBox.create(tableViewerCreator.getTable());
        column.setCellEditor(functComboBox, comboValueAdapter);
        column.setTitle("Functions");
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<MetadataColumnExt, String>() {

            public String get(MetadataColumnExt bean) {
                if (bean.getFunction() != null) {
                    functComboBox.setItems(bean.getArrayFunctions());
                    saveOneColData(bean);
                    return bean.getFunction().getName();
                }
                return "";
            }

            public void set(MetadataColumnExt bean, String value) {
                bean.setFunction(RowGenTableEditor2.this.getFunnctionByName(bean.getTalendType(), value));
                saveOneColData(bean);
            }

        });
        column.setModifiable(true);
        column.setWeight(10);
        column.setMinimumWidth(30);
        // ////////////////////////////////////////////////////////////////////////////
        column = new TableViewerCreatorColumn(tableViewerCreator);
        column.setTitle("Parameters");
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<MetadataColumnExt, String>() {

            public String get(MetadataColumnExt bean) {
                if (bean.getFunction() != null) {
                    saveOneColData(bean);
                }
                return bean.getParameter();
            }

            public void set(MetadataColumnExt bean, String value) {
                saveOneColData(bean);
            }

        });
        column.setModifiable(true);
        column.setWeight(10);
        column.setMinimumWidth(30);
        // //////////////////////////////////////////////////////////////////////////
        // column = new TableViewerCreatorColumn(tableViewerCreator);
        // column.setTitle("Summary");
        // column.setBeanPropertyAccessors(new IBeanPropertyAccessors<IMetadataColumn, String>() {
        //
        // public String get(IMetadataColumn bean) {
        // return bean.getTalendType();
        // }
        //
        // public void set(IMetadataColumn bean, String value) {
        // bean.setTalendType(value);
        // }
        //
        // });
        // column.setModifiable(true);
        // column.setWeight(10);
        // column.setMinimumWidth(30);

        // ////////////////////////////////////////////////////////////////////////////
        column = new TableViewerCreatorColumn(tableViewerCreator);
        column.setTitle("Preview");
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<MetadataColumnExt, String>() {

            public String get(MetadataColumnExt bean) {
                if (bean.getFunction() == null) {
                    return "";
                }
                return bean.getFunction().getPreview();
            }

            public void set(MetadataColumnExt bean, String value) {
            }

        });
        column.setModifiable(true);
        column.setWeight(10);
        column.setMinimumWidth(30);
        // ////////////////////////////////////////////////////////////////////////

    }

    /**
     * qzhang Comment method "getFunnctionByName".
     * 
     * @param talendType
     * @param value
     * @return
     */
    @SuppressWarnings("unchecked")
    protected Function getFunnctionByName(String talendType, String value) {
        Function func = null;
        for (Function fun : FunctionManager.getInstance().getFunctionByName(talendType)) {
            if (fun.getName().equals(value)) {
                func = (Function) fun.clone();
            }
        }
        return func;
    }

    /**
     * qzhang Comment method "createOrignColumns".
     * 
     * @param tableViewerCreator
     */
    private void orignAllColumns(TableViewerCreator<IMetadataColumn> tableViewerCreator) {
        CellEditorValueAdapter comboValueAdapter;
        String dbms = null;
        RepositoryContext repositoryContext = (RepositoryContext) CorePlugin.getContext().getProperty(
                Context.REPOSITORY_CONTEXT_KEY);
        ECodeLanguage codeLanguage = repositoryContext.getProject().getLanguage();
        if (codeLanguage == ECodeLanguage.JAVA) {
            comboValueAdapter = CellEditorValueAdapterFactory.getComboAdapter("String");
            dbms = MetadataTalendType.LANGUAGE_JAVA;
        } else {
            comboValueAdapter = CellEditorValueAdapterFactory.getComboAdapter();
            dbms = MetadataTalendType.TALENDDEFAULT;
        }

        String[] arrayTalendTypes = new String[0];
        try {
            arrayTalendTypes = MetadataTalendType.loadTalendTypes(dbms, false);
        } catch (NoClassDefFoundError e) {
            // shouln't be happend
            e.printStackTrace();
        } catch (ExceptionInInitializerError e) {
            // shouln't be happend
            e.printStackTrace();
        }

        CellEditorValueAdapter positiveIntValueAdapter = CellEditorValueAdapterFactory.getPositiveIntAdapter();

        // //////////////////////////////////////////////////////////////////////////////////////

        TableViewerCreatorColumn column = new TableViewerCreatorColumn(tableViewerCreator);
        column.setTitle("");
        column.setDefaultInternalValue("");
        column.setWidth(15);

        // //////////////////////////////////////////////////////////////////////////////////////

        column = new TableViewerCreatorColumn(tableViewerCreator);
        column.setId(ID_COLUMN_NAME);
        column.setTitle("Column");
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<IMetadataColumn, String>() {

            public String get(IMetadataColumn bean) {
                return bean.getLabel();
            }

            public void set(IMetadataColumn bean, String value) {
                bean.setLabel(value);
            }

        });
        final Image imageKey = ImageProvider.getImage(EImage.KEY_ICON);
        final Image imageEmpty = org.talend.commons.ui.image.ImageProvider.getImage(EImage.EMPTY);
        column.setImageProvider(new IColumnImageProvider() {

            public Image getImage(Object element) {
                IMetadataColumn metadataColumn = (MetadataColumn) element;
                if (metadataColumn.isKey()) {
                    return imageKey;
                } else {
                    return imageEmpty;
                }
            }

        });
        column.setWeight(25);
        column.setModifiable(!isReadOnly());
        column.setMinimumWidth(45);
        final TextCellEditor cellEditor = new TextCellEditor(tableViewerCreator.getTable());
        cellEditor.addListener(new DialogErrorForCellEditorListener(cellEditor, column) {

            @Override
            public void newValidValueTyped(int itemIndex, Object previousValue, Object newValue, CELL_EDITOR_STATE state) {
            }

            @Override
            public String validateValue(String newValue, int beanPosition) {
                return getMetadataTableEditor().validateColumnName(newValue, beanPosition);
            }

        });
        column.setCellEditor(cellEditor);

        // //////////////////////////////////////////////////////////////////////////////////////

        column = new TableViewerCreatorColumn(tableViewerCreator);
        column.setTitle("Key");
        column.setId(ID_COLUMN_KEY);
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<IMetadataColumn, Boolean>() {

            public Boolean get(IMetadataColumn bean) {
                return new Boolean(bean.isKey());
            }

            public void set(IMetadataColumn bean, Boolean value) {
                bean.setKey(value);
            }

        });
        column.setWidth(35);
        column.setDisplayedValue("");
        CheckboxTableEditorContent checkboxTableEditorContent = new CheckboxTableEditorContent(isReadOnly());
        column.setTableEditorContent(checkboxTableEditorContent);

        // //////////////////////////////////////////////////////////////////////////////////////

        column = new TableViewerCreatorColumn(tableViewerCreator);
        column.setTitle("Type");
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<MetadataColumnExt, String>() {

            public String get(MetadataColumnExt bean) {
                return bean.getTalendType();
            }

            public void set(MetadataColumnExt bean, String value) {
                bean.setTalendType(value);
                bean.setFunction(RowGenTableEditor2.this.getFunction(bean, value));
                saveOneColData(bean);
            }

        });
        column.setModifiable(!isReadOnly());
        column.setWeight(10);
        column.setMinimumWidth(30);
        column.setCellEditor(new ComboBoxCellEditor(tableViewerCreator.getTable(), arrayTalendTypes), comboValueAdapter);

        // //////////////////////////////////////////////////////////////////////////////////////

        orignColumns(tableViewerCreator, positiveIntValueAdapter);
    }

    /**
     * qzhang Comment method "orignColumns".
     * 
     * @param tableViewerCreator
     * @param positiveIntValueAdapter
     */
    private void orignColumns(TableViewerCreator<IMetadataColumn> tableViewerCreator,
            CellEditorValueAdapter positiveIntValueAdapter) {
        TableViewerCreatorColumn column;
        column = new TableViewerCreatorColumn(tableViewerCreator);
        column.setTitle("Length");
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<IMetadataColumn, Integer>() {

            public Integer get(IMetadataColumn bean) {
                return bean.getLength();
            }

            public void set(IMetadataColumn bean, Integer value) {
                bean.setLength(value);
            }

        });
        column.setModifiable(!isReadOnly());
        column.setWidth(55);
        column.setCellEditor(new TextCellEditor(tableViewerCreator.getTable()), positiveIntValueAdapter);

        // //////////////////////////////////////////////////////////////////////////////////////

        column = new TableViewerCreatorColumn(tableViewerCreator);
        column.setTitle("Precision");
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<IMetadataColumn, Integer>() {

            public Integer get(IMetadataColumn bean) {
                return bean.getPrecision();
            }

            public void set(IMetadataColumn bean, Integer value) {
                bean.setPrecision(value);
            }

        });
        column.setModifiable(!isReadOnly());
        column.setWidth(60);
        column.setCellEditor(new TextCellEditor(tableViewerCreator.getTable()), positiveIntValueAdapter);

        // //////////////////////////////////////////////////////////////////////////////////////

        column = new TableViewerCreatorColumn(tableViewerCreator);
        column.setTitle("Nullable");
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<IMetadataColumn, Boolean>() {

            public Boolean get(IMetadataColumn bean) {
                return new Boolean(bean.isNullable());
            }

            public void set(IMetadataColumn bean, Boolean value) {
                bean.setNullable(value);
            }

        });
        column.setModifiable(!isReadOnly());
        column.setWidth(56);
        column.setDisplayedValue("");
        column.setTableEditorContent(new CheckboxTableEditorContent(isReadOnly()));

        // //////////////////////////////////////////////////////////////////////////////////////

        column = new TableViewerCreatorColumn(tableViewerCreator);
        column.setTitle("Comment");
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<IMetadataColumn, String>() {

            public String get(IMetadataColumn bean) {
                return bean.getComment();
            }

            public void set(IMetadataColumn bean, String value) {
                bean.setComment(value);
            }

        });
        column.setWeight(10);
        column.setModifiable(!isReadOnly());
        column.setMinimumWidth(20);
        column.setCellEditor(new TextCellEditor(tableViewerCreator.getTable()));
    }

    @SuppressWarnings("unchecked")
    private Function getFunction(MetadataColumnExt bean, String talendType) {
        Function currentFun = new Function();
        List<Function> functions = FunctionManager.getInstance().getFunctionByName(talendType);
        String[] arrayTalendFunctions2 = new String[functions.size()];
        if (functions.isEmpty()) {
            currentFun.setDescription("");
            currentFun.setPreview("");
            currentFun.setParameters(new ArrayList<Parameter>());
            bean.setArrayFunctions(arrayTalendFunctions2);
        } else {
            for (int i = 0; i < functions.size(); i++) {
                arrayTalendFunctions2[i] = functions.get(i).getName();
            }
            currentFun = (Function) functions.get(0).clone();
            bean.setArrayFunctions(arrayTalendFunctions2);
        }

        return currentFun;
    }

    public MetadataTableEditorExt getMetadataTableEditor() {
        return (MetadataTableEditorExt) getExtendedTableModel();
    }

    public void setMetadataTableEditor(MetadataTableEditorExt metadataTableEditor) {
        setExtendedTableModel(metadataTableEditor);
    }

    /*
     * (non-Java)
     * 
     * @see org.talend.commons.ui.swt.advanced.dataeditor.AbstractDataTableEditorView#initToolBar()
     */
    @Override
    protected ExtendedToolbarView initToolBar() {
        return new MetadataToolbarEditorViewExt(getMainComposite(), SWT.NONE, this.getExtendedTableViewer());
    }

    public void setRGcomponent(RowGeneratorComponent gcomponent) {
        this.rGcomponent = gcomponent;
    }

    public RowGeneratorComponent getRGcomponent() {
        return this.rGcomponent;
    }
}
