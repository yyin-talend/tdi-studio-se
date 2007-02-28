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
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.talend.commons.ui.image.EImage;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.commons.ui.swt.advanced.dataeditor.AbstractDataTableEditorView;
import org.talend.commons.ui.swt.advanced.dataeditor.ExtendedToolbarView;
import org.talend.commons.ui.swt.extended.table.ExtendedTableModel;
import org.talend.commons.ui.swt.tableviewer.CellEditorValueAdapterFactory;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreatorColumn;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator.CELL_EDITOR_STATE;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator.LAYOUT_MODE;
import org.talend.commons.ui.swt.tableviewer.behavior.CellEditorValueAdapter;
import org.talend.commons.ui.swt.tableviewer.behavior.IColumnImageProvider;
import org.talend.commons.ui.swt.tableviewer.celleditor.DialogErrorForCellEditorListener;
import org.talend.commons.ui.swt.tableviewer.tableeditor.CheckboxTableEditorContent;
import org.talend.commons.ui.swt.tableviewer.tableeditor.TableEditorContent;
import org.talend.commons.utils.data.bean.IBeanPropertyAccessors;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.MetadataColumn;
import org.talend.core.model.metadata.MetadataTalendType;
import org.talend.designer.rowgenerator.RowGeneratorComponent;
import org.talend.designer.rowgenerator.data.Function;
import org.talend.designer.rowgenerator.data.FunctionManager;
import org.talend.designer.rowgenerator.data.Parameter;
import org.talend.designer.rowgenerator.data.StringParameter;
import org.talend.designer.rowgenerator.i18n.Messages;
import org.talend.designer.rowgenerator.ui.RowGeneratorUI;

/**
 * qzhang class global comment. Detailled comment <br/>
 * 
 * $Id: RowGenTableEditor2.java,v 1.12 2007/01/31 10:31:05 pub Exp $
 * 
 */
public class RowGenTableEditor2 extends AbstractDataTableEditorView<IMetadataColumn> {

    public static final String NAME_ID_COLUMN = "ID_COLUMN_NAME"; //$NON-NLS-1$

    public static final String KEY_ID_COLUMN = "ID_COLUMN_KEY"; //$NON-NLS-1$

    public static final String TYPE_ID_COLUMN = "ID_COLUMN_TYPE"; //$NON-NLS-1$

    public static final String LENGTH_ID_COLUMN = "ID_COLUMN_LENGTH"; //$NON-NLS-1$

    public static final String PRECISION_ID_COLUMN = "ID_COLUMN_PRECISION"; //$NON-NLS-1$

    public static final String NULLABLE_ID_COLUMN = "ID_COLUMN_NULLABLE"; //$NON-NLS-1$

    public static final String COMMENT_ID_COLUMN = "ID_COLUMN_COMMENT"; //$NON-NLS-1$

    public static final String PARAMETER_ID_COLUMN = "ID_COLUMN_PARAMETER"; //$NON-NLS-1$

    public static final String FUNCTION_ID_COLUMN = "ID_COLUMN_FUNCTION"; //$NON-NLS-1$

    public static final String PREVIEW_ID_COLUMN = "ID_COLUMN_PREVIEW"; //$NON-NLS-1$

    private static final int TITLE_DEFAULT_HEIGHT = 25;

    private RowGeneratorComponent rGcomponent;

    // private Composite mainComposite;
    //
    // private int mainCompositeStyle;
    //
    // private Composite parentComposite;
    private Composite schCom;

    private Composite funCom;

    private Composite preCom;

    private Composite headerTable;

    private Label empty;

    private FunctionManager functionManager;

    /**
     * qzhang RowGenMetadataTableEditorExt constructor comment.
     * 
     * @param parentComposite
     * @param mainCompositeStyle
     * @param extendedTableModel
     * @param readOnly
     * @param toolbarVisible
     * @param functionManager
     */
    public RowGenTableEditor2(Composite parentComposite, int mainCompositeStyle,
            ExtendedTableModel<IMetadataColumn> extendedTableModel, boolean readOnly, boolean toolbarVisible,
            RowGeneratorComponent rGcomponent, FunctionManager functionManager) {
        super(parentComposite, mainCompositeStyle, extendedTableModel, readOnly, toolbarVisible, true);
        this.rGcomponent = rGcomponent;
        this.functionManager = functionManager;
    }

    // public RowGenTableEditor2(Composite parentComposite, int mainCompositeStyle) {
    // super(parentComposite, mainCompositeStyle);
    // }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.swt.advanced.dataeditor.AbstractDataTableEditorView#initGraphicComponents()
     */
    public void initGraphicComponents() {
        mainComposite = new Composite(parentComposite, SWT.NONE);
        if (parentComposite.getBackground() != null && !parentComposite.getBackground().equals(mainComposite.getBackground())) {
            mainComposite.setBackground(parentComposite.getBackground());
        }
        GridLayout layout = new GridLayout();
        layout.marginLeft = 0;
        layout.marginRight = 0;
        layout.horizontalSpacing = 0;
        layout.marginTop = 0;
        layout.marginBottom = 0;
        layout.verticalSpacing = 0;
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        mainComposite.setLayout(layout);

        GridData gridData = new GridData(GridData.FILL_BOTH);

        mainComposite.setLayoutData(gridData);

        createHeaderTable();

        initTable();

        getExtendedTableViewer().getTableViewerCreator().getTableViewer().setComparer(createElementComparer());

        getExtendedTableViewer().getTableViewerCreator().getTable().setLayoutData(new GridData(GridData.FILL_BOTH));

        initToolBar();

        addListeners();

        addTableColumnsListener();

        addHorizontalBarListener();
    }

    /**
     * qzhang Comment method "addHorizontalBarListener".
     */
    private void addHorizontalBarListener() {
        final ScrollBar horizontalBar = getTable().getHorizontalBar();
        horizontalBar.addSelectionListener(new SelectionAdapter() {

            /*
             * (non-Java)
             * 
             * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
             */
            public void widgetSelected(SelectionEvent e) {
                e.getSource();
                if (e.detail == SWT.DRAG) {
                    RowGenTableEditor2.this.attachLabelPosition();
                } else if (e.detail == SWT.NONE) {
                    RowGenTableEditor2.this.attachLabelPosition();
                }
            }
        });
    }

    private void addTableColumnsListener() {
        ControlListener controlListener = new ControlListener() {

            public void controlMoved(ControlEvent e) {
                RowGenTableEditor2.this.attachLabelPosition();
            }

            public void controlResized(ControlEvent e) {
                RowGenTableEditor2.this.attachLabelPosition();
            }

        };
        for (Iterator iter = getTableViewerCreator().getColumns().iterator(); iter.hasNext();) {
            TableViewerCreatorColumn element = (TableViewerCreatorColumn) iter.next();
            element.getTableColumn().addControlListener(controlListener);
        }
    }

    /**
     * qzhang Comment method "createHeader".
     */
    private void createHeaderTable() {
        headerTable = new Composite(mainComposite, SWT.NONE);

        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 4;
        gridLayout.marginLeft = 0;
        gridLayout.marginRight = 0;
        gridLayout.horizontalSpacing = 0;
        gridLayout.marginTop = 0;
        gridLayout.marginBottom = 0;
        gridLayout.verticalSpacing = 0;
        gridLayout.marginHeight = 0;
        gridLayout.marginWidth = 0;
        gridLayout.makeColumnsEqualWidth = false;

        headerTable.setLayout(gridLayout);
        final GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.heightHint = TITLE_DEFAULT_HEIGHT;
        gridData.verticalIndent = 0;
        gridData.horizontalIndent = 0;

        headerTable.setLayoutData(gridData);

        Label titleLabel = createSchemaComposite();

        Label funLabel = createFunctionComposite();

        Label previewLabel = createPreviewComposite();

        setHeaderText(titleLabel, funLabel, previewLabel);
        empty = new Label(headerTable, SWT.NONE);
        empty.setText("");
        empty.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    }

    /**
     * qzhang Comment method "setHeaderText".
     * 
     * @param titleLabel
     * @param funLabel
     * @param previewLabel
     * @param fds
     */
    private void setHeaderText(Label titleLabel, Label funLabel, Label previewLabel) {
        FontData[] fds = titleLabel.getFont().getFontData();
        for (FontData fd : fds) {
            fd.setHeight(fd.getHeight() + 2);
            fd.setStyle(fd.getStyle() | SWT.BOLD);
        }
        Font titleFont = new Font(titleLabel.getDisplay(), fds);
        titleLabel.setFont(titleFont);
        funLabel.setFont(titleFont);
        previewLabel.setFont(titleFont);

        titleLabel.setText(Messages.getString("RowGeneratorUI.SchemaTitle.Text")); //$NON-NLS-1$
        funLabel.setText(Messages.getString("RowGenTableEditor2.Fuctions.TitleText")); //$NON-NLS-1$
        previewLabel.setText(Messages.getString("RowGenTableEditor2.Preview.TitleText")); //$NON-NLS-1$
    }

    /**
     * qzhang Comment method "createPreviewComposite".
     * 
     * @param headerTable
     * @param gridLayout
     * @param gridData
     * @return
     */
    private Label createPreviewComposite() {
        preCom = new Composite(headerTable, SWT.NONE);
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 3;
        gridLayout.marginLeft = 0;
        gridLayout.marginRight = 0;
        gridLayout.horizontalSpacing = 10;
        gridLayout.marginTop = 0;
        gridLayout.marginBottom = 0;
        gridLayout.verticalSpacing = 0;
        gridLayout.marginHeight = 0;
        gridLayout.marginWidth = 0;

        final GridData gridData = new GridData(GridData.FILL_BOTH);
        gridData.heightHint = TITLE_DEFAULT_HEIGHT;
        // gridData.horizontalAlignment = SWT.LEFT;
        // gridData.verticalAlignment = SWT.CENTER;

        preCom.setLayout(gridLayout);
        preCom.setLayoutData(gridData);

        Label separator = new Label(preCom, SWT.SEPARATOR | SWT.VERTICAL);
        separator.setLayoutData(new GridData(GridData.FILL_VERTICAL));

        Label previewLabel = new Label(preCom, SWT.NONE);
        previewLabel.setLayoutData(new GridData(GridData.FILL_VERTICAL));
        if (parentComposite.getBackground() != null && !parentComposite.getBackground().equals(previewLabel.getBackground())) {
            previewLabel.setBackground(parentComposite.getBackground());
        }
        Button refresh = new Button(preCom, SWT.NONE);
        refresh.setImage(ImageProvider.getImageDesc(EImage.REFRESH_ICON).createImage());
        GridData gridData2 = new GridData(GridData.FILL_VERTICAL);
        gridData2.horizontalAlignment = GridData.BEGINNING;
        refresh.setLayoutData(gridData2);
        refresh.addSelectionListener(new SelectionAdapter() {

            /*
             * (non-Java)
             * 
             * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
             */
            @Override
            public void widgetSelected(SelectionEvent e) {
                Table table = generatorUI.getTabFolderEditors().getProcessPreview().getTable();

                if (table != null && table.getItemCount() > 1) {
                    TableItem item = table.getItems()[0];
                    for (int i = 1; i < table.getColumnCount(); i++) {
                        MetadataColumnExt ext = (MetadataColumnExt) getTable().getItem(i - 1).getData();
                        ext.setPreview(item.getText(i));
                    }
                    getTableViewerCreator().getTableViewer().refresh();

                }
            }
        });
        return previewLabel;
    }

    /**
     * qzhang Comment method "createPreviewComposite".
     * 
     * @param headerTable
     * @param gridLayout
     * @param gridData
     * @return
     */
    private Label createFunctionComposite() {
        funCom = new Composite(headerTable, SWT.NONE);
        final GridLayout gridLayout = new GridLayout();

        gridLayout.numColumns = 2;
        gridLayout.marginLeft = 0;
        gridLayout.marginRight = 0;
        gridLayout.horizontalSpacing = 10;
        gridLayout.marginTop = 0;
        gridLayout.marginBottom = 0;
        gridLayout.verticalSpacing = 0;
        gridLayout.marginHeight = 0;
        gridLayout.marginWidth = 0;

        funCom.setLayout(gridLayout);

        final GridData gridData = new GridData(GridData.FILL_BOTH);
        gridData.heightHint = TITLE_DEFAULT_HEIGHT;
        gridData.verticalIndent = 0;
        gridData.horizontalIndent = 0;
        funCom.setLayoutData(gridData);

        Label separator = new Label(funCom, SWT.SEPARATOR | SWT.VERTICAL);
        separator.setLayoutData(new GridData(GridData.FILL_VERTICAL));

        Label funLabel = new Label(funCom, SWT.NONE);
        GridData funLabelGd = new GridData(GridData.FILL_BOTH);
        funLabel.setLayoutData(funLabelGd);

        if (parentComposite.getBackground() != null && !parentComposite.getBackground().equals(funLabel.getBackground())) {
            funLabel.setBackground(parentComposite.getBackground());
        }
        return funLabel;
    }

    /**
     * qzhang Comment method "createSchemaComposite".
     * 
     * @param headerTable
     * @param gridLayout
     * @param gridData
     * @return
     */
    private Label createSchemaComposite() {

        schCom = new Composite(headerTable, SWT.NONE);
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        gridLayout.marginLeft = 0;
        gridLayout.marginRight = 0;
        gridLayout.horizontalSpacing = 10;
        gridLayout.marginTop = 0;
        gridLayout.marginBottom = 0;
        gridLayout.verticalSpacing = 0;
        gridLayout.marginHeight = 0;
        gridLayout.marginWidth = 0;
        schCom.setLayout(gridLayout);
        final GridData gridData = new GridData(GridData.FILL_BOTH);
        gridData.heightHint = TITLE_DEFAULT_HEIGHT;
        gridData.verticalIndent = 0;
        gridData.horizontalIndent = 0;
        schCom.setLayoutData(gridData);
        new Label(schCom, SWT.NONE);

        Label titleLabel = new Label(schCom, SWT.NONE);
        titleLabel.setLayoutData(gridData);
        if (parentComposite.getBackground() != null && !parentComposite.getBackground().equals(titleLabel.getBackground())) {
            titleLabel.setBackground(parentComposite.getBackground());
        }
        return titleLabel;
    }

    /**
     * qzhang Comment method "attachLabelListener".
     */
    public void attachLabelPosition() {
        int funwidth = 0;
        int prewidth = 0;
        int w = 0;
        for (Iterator iter = getTableViewerCreator().getColumns().iterator(); iter.hasNext();) {
            TableViewerCreatorColumn element = (TableViewerCreatorColumn) iter.next();
            w += element.getTableColumn().getWidth();
        }
        prewidth = w - getTableViewerCreator().getColumn(PREVIEW_ID_COLUMN).getTableColumn().getWidth();

        funwidth = prewidth - getTableViewerCreator().getColumn(FUNCTION_ID_COLUMN).getTableColumn().getWidth()
                - getTableViewerCreator().getColumn(PARAMETER_ID_COLUMN).getTableColumn().getWidth();

        schCom.setSize(funwidth, TITLE_DEFAULT_HEIGHT);
        funCom.setLocation(schCom.getBounds().x + funwidth, schCom.getBounds().y);
        funCom.setSize(prewidth - funwidth, TITLE_DEFAULT_HEIGHT);
        preCom.setLocation(schCom.getBounds().x + prewidth, schCom.getBounds().y);
        // preCom.setSize(w - prewidth, TITLE_DEFAULT_HEIGHT);
        // getTableViewerCreator().getColumn(PREVIEW_ID_COLUMN).getTableColumn().setWidth(preCom.getSize().x);
        empty.setLocation(schCom.getBounds().x + w, schCom.getBounds().y);
    }

    /**
     * qzhang Comment method "updateHeader".
     */
    public void updateHeader(String id, String currTitle, boolean isHide) {
        final TableViewerCreatorColumn funColumn = getTableViewerCreator().getColumn(id);
        if (isHide) {
            final TableEditorContent tableEditorContent = funColumn.getTableEditorContent();
            if (tableEditorContent != null && tableEditorContent instanceof CheckboxTableEditorContent) {
                funColumn.setTableEditorContent(null);
            }
            funColumn.getTableColumn().setText("");
            funColumn.getTableColumn().setWidth(0);
            funColumn.setWidth(0);
            funColumn.getTableColumn().setResizable(false);
            funColumn.setMoveable(false);
            if ((id.equals(FUNCTION_ID_COLUMN) && getTableViewerCreator().getColumn(PARAMETER_ID_COLUMN).getTableColumn()
                    .getWidth() == 0)
                    || ((id.equals(PARAMETER_ID_COLUMN)) && getTableViewerCreator().getColumn(FUNCTION_ID_COLUMN)
                            .getTableColumn().getWidth() == 0)) {
                funCom.setVisible(false);
            }
            if (id.equals(PREVIEW_ID_COLUMN)) {
                preCom.setVisible(false);
            }
            getTableViewerCreator().refreshTableEditorControls();
        } else {

            final TableEditorContent tableEditorContent = funColumn.getTableEditorContent();
            if (tableEditorContent == null
                    && (funColumn.getId().equals(KEY_ID_COLUMN) || funColumn.getId().equals(NULLABLE_ID_COLUMN))) {
                funColumn.setTableEditorContent(new CheckboxTableEditorContent(isReadOnly()));
            }
            funColumn.setMinimumWidth(35);
            funColumn.getTableColumn().setWidth(65);
            funColumn.getTableColumn().setText(currTitle);
            funColumn.getTableColumn().setResizable(true);
            funColumn.setTitle(currTitle);
            funColumn.setMoveable(true);
            if (id.equals(FUNCTION_ID_COLUMN) || id.equals(PARAMETER_ID_COLUMN)) {
                funCom.setVisible(true);
            }
            if (id.equals(PREVIEW_ID_COLUMN)) {
                preCom.setVisible(true);
            }
            getTableViewerCreator().refreshTableEditorControls();
        }
        attachLabelPosition();
    }

    @Override
    protected void createColumns(TableViewerCreator<IMetadataColumn> tableViewerCreator, Table table) {
        CellEditorValueAdapter comboValueAdapter = null;
        TableViewerCreatorColumn column;
        orignAllColumns(tableViewerCreator);

        // ///////////////////////////////////////////////////////////////////////
        comboValueAdapter = CellEditorValueAdapterFactory.getComboAdapter("String"); //$NON-NLS-1$
        column = new TableViewerCreatorColumn(tableViewerCreator);
        final ComboBoxCellEditor functComboBox = new ComboBoxCellEditor();
        functComboBox.create(tableViewerCreator.getTable());
        column.setCellEditor(functComboBox, comboValueAdapter);
        column.setId(FUNCTION_ID_COLUMN);
        column.setTitle(Messages.getString("RowGenTableEditor2.Fuctions.TitleText")); //$NON-NLS-1$
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<MetadataColumnExt, String>() {

            public String get(MetadataColumnExt bean) {
                if (bean.getFunction() != null) {
                    functComboBox.setItems(bean.getArrayFunctions());
                    return bean.getFunction().getName();
                }
                return ""; //$NON-NLS-1$
            }

            public void set(MetadataColumnExt bean, String value) {
                bean.setFunction(RowGenTableEditor2.this.getFunnctionByName(bean.getTalendType(), value));
            }

        });
        column.setModifiable(true);
        column.setWeight(10);
        column.setWidth(45);
        // ////////////////////////////////////////////////////////////////////////////
        column = new TableViewerCreatorColumn(tableViewerCreator);
        column.setId(PARAMETER_ID_COLUMN);
        column.setTitle(Messages.getString("RowGenTableEditor2.Parameters.TitleText")); //$NON-NLS-1$
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<MetadataColumnExt, String>() {

            public String get(MetadataColumnExt bean) {
                if (bean.getFunction() != null && bean.getFunction().getName().equals(FunctionManager.PURE_PERL_NAME)) {
                    return ((StringParameter) bean.getFunction().getParameters().get(0)).getValue();
                }
                return bean.getParameter();
            }

            public void set(MetadataColumnExt bean, String value) {
                if (bean.getFunction() != null && bean.getFunction().getName().equals(FunctionManager.PURE_PERL_NAME)) {
                    ((StringParameter) bean.getFunction().getParameters().get(0)).setValue(value);
                }
            }

        });
        column.setModifiable(true);
        column.setWeight(10);
        column.setWidth(45);
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
        column.setId(PREVIEW_ID_COLUMN);
        column.setTitle(Messages.getString("RowGenTableEditor2.Preview.TitleText")); //$NON-NLS-1$
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<MetadataColumnExt, String>() {

            public String get(MetadataColumnExt bean) {
                if (bean.getFunction() == null) {
                    return ""; //$NON-NLS-1$
                }
                return bean.getFunction().getPreview();
            }

            public void set(MetadataColumnExt bean, String value) {
                bean.getFunction().setPreview(value);
                bean.setPreview(value);
            }

        });
        column.setModifiable(false);
        column.setWeight(10);
        column.setMinimumWidth(110);
        column.setWidth(120);
        // ////////////////////////////////////////////////////////////////////////

    }

    /**
     * qzhang Comment method "getFunnctionByName".
     * 
     * @param talendType
     * @param value
     * @return
     */
    @SuppressWarnings("unchecked")//$NON-NLS-1$
    protected Function getFunnctionByName(String talendType, String value) {
        Function func = null;
        for (Function fun : functionManager.getFunctionByName(talendType)) {
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
    @SuppressWarnings("deprecation")
    private void orignAllColumns(TableViewerCreator<IMetadataColumn> tableViewerCreator) {
        CellEditorValueAdapter comboValueAdapter;
        String dbms = null;
        RepositoryContext repositoryContext = (RepositoryContext) CorePlugin.getContext().getProperty(
                Context.REPOSITORY_CONTEXT_KEY);
        ECodeLanguage codeLanguage = repositoryContext.getProject().getLanguage();
        if (codeLanguage == ECodeLanguage.JAVA) {
            comboValueAdapter = CellEditorValueAdapterFactory.getComboAdapter("String"); //$NON-NLS-1$
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
        column.setTitle(""); //$NON-NLS-1$
        column.setDefaultInternalValue(""); //$NON-NLS-1$
        column.setWidth(15);

        // //////////////////////////////////////////////////////////////////////////////////////

        column = new TableViewerCreatorColumn(tableViewerCreator);
        column.setId(NAME_ID_COLUMN);
        column.setTitle(Messages.getString("RowGenTableEditor2.Column.TitleText")); //$NON-NLS-1$
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<IMetadataColumn, String>() {

            public String get(IMetadataColumn bean) {
                return bean.getLabel();
            }

            public void set(IMetadataColumn bean, String value) {
                getMetadataTableEditor().getRowGenUI().getTabFolderEditors().getProcessPreview().renameColumn(bean, value);
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
        column.setTitle(Messages.getString("RowGenTableEditor2.Key.TitleText")); //$NON-NLS-1$
        column.setId(KEY_ID_COLUMN);
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<IMetadataColumn, Boolean>() {

            public Boolean get(IMetadataColumn bean) {
                return new Boolean(bean.isKey());
            }

            public void set(IMetadataColumn bean, Boolean value) {
                bean.setKey(value);
            }

        });
        column.setWidth(35);
        column.setDisplayedValue(""); //$NON-NLS-1$
        CheckboxTableEditorContent checkboxTableEditorContent = new CheckboxTableEditorContent(isReadOnly());
        column.setTableEditorContent(checkboxTableEditorContent);

        // //////////////////////////////////////////////////////////////////////////////////////

        column = new TableViewerCreatorColumn(tableViewerCreator);
        column.setId(TYPE_ID_COLUMN);
        column.setTitle(Messages.getString("RowGenTableEditor2.Type.TitleText")); //$NON-NLS-1$
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<MetadataColumnExt, String>() {

            public String get(MetadataColumnExt bean) {
                return bean.getTalendType();
            }

            public void set(MetadataColumnExt bean, String value) {
                bean.setTalendType(value);
                bean.setFunction(RowGenTableEditor2.this.getFunction(bean, value));
                // saveOneColData(bean);
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
        column.setId(LENGTH_ID_COLUMN);
        column.setTitle(Messages.getString("RowGenTableEditor2.Length.TitleText")); //$NON-NLS-1$
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
        column.setId(PRECISION_ID_COLUMN);
        column.setTitle(Messages.getString("RowGenTableEditor2.Precision.TitleText")); //$NON-NLS-1$
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
        column.setId(NULLABLE_ID_COLUMN);
        column.setTitle(Messages.getString("RowGenTableEditor2.Nullable.TitleText")); //$NON-NLS-1$
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
        column.setDisplayedValue(""); //$NON-NLS-1$
        column.setTableEditorContent(new CheckboxTableEditorContent(isReadOnly()));

        // //////////////////////////////////////////////////////////////////////////////////////

        column = new TableViewerCreatorColumn(tableViewerCreator);
        column.setId(COMMENT_ID_COLUMN);
        column.setTitle(Messages.getString("RowGenTableEditor2.Comment.TitleText")); //$NON-NLS-1$
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

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    private Function getFunction(MetadataColumnExt bean, String talendType) {
        Function currentFun = new Function();
        List<Function> functions = functionManager.getFunctionByName(talendType);
        String[] arrayTalendFunctions2 = new String[functions.size()];
        if (functions.isEmpty()) {
            currentFun.setDescription(""); //$NON-NLS-1$
            currentFun.setPreview(""); //$NON-NLS-1$
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

    private MetadataToolbarEditorViewExt extendedToolbar;

    /*
     * (non-Java)
     * 
     * @see org.talend.commons.ui.swt.advanced.dataeditor.AbstractDataTableEditorView#initToolBar()
     */
    @Override
    protected ExtendedToolbarView initToolBar() {
        extendedToolbar = new MetadataToolbarEditorViewExt(getMainComposite(), SWT.NONE, this.getExtendedTableViewer(), this);
        return extendedToolbar;
    }

    public void setRGcomponent(RowGeneratorComponent gcomponent) {
        this.rGcomponent = gcomponent;
    }

    public RowGeneratorComponent getRGcomponent() {
        return this.rGcomponent;
    }

    /*
     * (non-Java)
     * 
     * @see org.talend.commons.ui.swt.advanced.dataeditor.AbstractDataTableEditorView#setTableViewerCreatorOptions(org.talend.commons.ui.swt.tableviewer.TableViewerCreator)
     */
    @Override
    protected void setTableViewerCreatorOptions(TableViewerCreator<IMetadataColumn> newTableViewerCreator) {
        super.setTableViewerCreatorOptions(newTableViewerCreator);
        newTableViewerCreator.setLayoutMode(LAYOUT_MODE.DEFAULT);
    }

    public MetadataToolbarEditorViewExt getExtendedToolbar() {
        return this.extendedToolbar;
    }

    /**
     * qzhang Comment method "updateHeader".
     * 
     * @param showColumnsList
     */
    public void updateHeader(String[] hideColumnsList) {
        if (hideColumnsList == null || hideColumnsList.length == 0) {
            attachLabelPosition();
            return;
        }
        for (String string : hideColumnsList) {
            updateHeader(string, null, true);
        }
        extendedToolbar.updateColumnsList(hideColumnsList);
    }

    private RowGeneratorUI generatorUI;

    public RowGeneratorUI getGeneratorUI() {
        return this.generatorUI;
    }

    public void setGeneratorUI(RowGeneratorUI generatorUI) {
        this.generatorUI = generatorUI;
    }

}
