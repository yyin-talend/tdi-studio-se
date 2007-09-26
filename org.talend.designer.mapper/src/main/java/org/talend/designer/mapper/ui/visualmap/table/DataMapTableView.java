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
package org.talend.designer.mapper.ui.visualmap.table;

import java.util.Arrays;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalListener;
import org.eclipse.jface.fieldassist.IContentProposalProvider;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellEditorListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ExtendedModifyEvent;
import org.eclipse.swt.custom.ExtendedModifyListener;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.ui.image.EImage;
import org.talend.commons.ui.swt.colorstyledtext.ColorManager;
import org.talend.commons.ui.swt.colorstyledtext.UnnotifiableColorStyledText;
import org.talend.commons.ui.swt.extended.table.AbstractExtendedTableViewer;
import org.talend.commons.ui.swt.proposal.ContentProposalAdapterExtended;
import org.talend.commons.ui.swt.proposal.ExtendedTextCellEditorWithProposal;
import org.talend.commons.ui.swt.proposal.ProposalUtils;
import org.talend.commons.ui.swt.tableviewer.IModifiedBeanListener;
import org.talend.commons.ui.swt.tableviewer.ModifiedBeanEvent;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreatorColumn;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator.LAYOUT_MODE;
import org.talend.commons.ui.swt.tableviewer.behavior.CellEditorValueAdapter;
import org.talend.commons.ui.swt.tableviewer.behavior.DefaultTableLabelProvider;
import org.talend.commons.ui.swt.tableviewer.behavior.ITableCellValueModifiedListener;
import org.talend.commons.ui.swt.tableviewer.behavior.TableCellValueModifiedEvent;
import org.talend.commons.ui.swt.tableviewer.celleditor.CellEditorDialogBehavior;
import org.talend.commons.ui.swt.tableviewer.celleditor.ExtendedTextCellEditor;
import org.talend.commons.ui.swt.tableviewer.data.ModifiedObjectInfo;
import org.talend.commons.ui.swt.tableviewer.selection.ILineSelectionListener;
import org.talend.commons.ui.swt.tableviewer.selection.LineSelectionEvent;
import org.talend.commons.ui.utils.ControlUtils;
import org.talend.commons.ui.utils.TableUtils;
import org.talend.commons.ui.ws.WindowSystem;
import org.talend.commons.utils.data.list.IListenableListListener;
import org.talend.commons.utils.data.list.ListenableListEvent;
import org.talend.commons.utils.threading.AsynchronousThreading;
import org.talend.commons.utils.threading.ExecutionLimiter;
import org.talend.core.CorePlugin;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.IService;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.Problem;
import org.talend.core.ui.proposal.ProcessProposalProvider;
import org.talend.designer.abstractmap.model.table.IDataMapTable;
import org.talend.designer.abstractmap.model.tableentry.IColumnEntry;
import org.talend.designer.abstractmap.model.tableentry.ITableEntry;
import org.talend.designer.core.ui.MultiPageTalendEditor;
import org.talend.designer.mapper.MapperMain;
import org.talend.designer.mapper.i18n.Messages;
import org.talend.designer.mapper.managers.MapperManager;
import org.talend.designer.mapper.managers.UIManager;
import org.talend.designer.mapper.model.table.AbstractInOutTable;
import org.talend.designer.mapper.model.table.OutputTable;
import org.talend.designer.mapper.model.tableentry.AbstractInOutTableEntry;
import org.talend.designer.mapper.model.tableentry.ExpressionFilterEntry;
import org.talend.designer.mapper.model.tableentry.FilterTableEntry;
import org.talend.designer.mapper.ui.color.ColorInfo;
import org.talend.designer.mapper.ui.color.ColorProviderMapper;
import org.talend.designer.mapper.ui.dnd.DragNDrop;
import org.talend.designer.mapper.ui.event.MousePositionAnalyser;
import org.talend.designer.mapper.ui.event.ResizeHelper;
import org.talend.designer.mapper.ui.event.ResizeHelper.RESIZE_MODE;
import org.talend.designer.mapper.ui.font.FontInfo;
import org.talend.designer.mapper.ui.font.FontProviderMapper;
import org.talend.designer.mapper.ui.image.ImageInfo;
import org.talend.designer.mapper.ui.image.ImageProviderMapper;
import org.talend.designer.mapper.ui.proposal.expression.ExpressionProposalProvider;
import org.talend.designer.mapper.ui.tabs.StyledTextHandler;
import org.talend.designer.mapper.ui.visualmap.zone.Zone;
import org.talend.expressionbuilder.IExpressionBuilderDialogService;
import org.talend.expressionbuilder.ui.IExpressionBuilderDialogController;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public abstract class DataMapTableView extends Composite {

    private final Point realToolbarSize = new Point(0, 0);

    private Table tableForEntries;

    private final ResizeHelper resizeHelper = new ResizeHelper();

    protected MapperManager mapperManager;

    protected TableViewerCreator tableViewerCreatorForColumns;

    protected IDataMapTable abstractDataMapTable;

    private Composite headerComposite;

    private Label nameLabel;

    private ToolItem minimizeButton;

    protected int heightForRestore;

    protected Layout parentLayout;

    protected TableViewerCreator tableViewerCreatorForFilters;

    protected Table tableForConstraints;

    private boolean executeSelectionEvent = true;

    protected ToolBar toolBarActions;

    private ExpressionProposalProvider expressionProposalProvider;

    protected Point expressionEditorTextSelectionBeforeFocusLost;

    protected Composite centerComposite;

    private Text columnExpressionTextEditor;

    private Text constraintExpressionTextEditor;

    private Cursor currentCursor;

    private final ExpressionColorProvider expressionColorProvider;

    private Listener showErrorMessageListener;

    protected boolean forceExecuteSelectionEvent;

    private AbstractExtendedTableViewer<IColumnEntry> extendedTableViewerForColumns;

    protected AbstractExtendedTableViewer<FilterTableEntry> extendedTableViewerForFilters;

    private static Image imageKey;

    private static Image imageEmpty;

    private static int constraintCounter = 0;

    private static final int MINIMUM_HEIGHT = 24;

    protected static final int TIME_BEFORE_NEW_REFRESH_BACKGROUND = 150;

    protected static final int OFFSET_HEIGHT_TRIGGER = 15;

    protected static final int COLUMN_EXPRESSION_SIZE_WEIGHT = 60;

    protected static final int COLUMN_NAME_SIZE_WEIGHT = 40;

    protected static final int ADJUST_WIDTH_VALUE = 0;

    private static final int HEADER_HEIGHT = 23;

    public static final String ID_NAME_COLUMN = "ID_NAME_COLUMN"; //$NON-NLS-1$

    public static final String ID_EXPRESSION_COLUMN = "ID_EXPRESSION_COLUMN"; //$NON-NLS-1$

    public static final String COLUMN_NAME = "Column"; //$NON-NLS-1$

    protected GridData tableForConstraintsGridData;

    private ExpressionProposalProvider expressionProposalProviderForExpressionFilter;

    private UnnotifiableColorStyledText expressionFilterText;

    public static final String DEFAULT_EXPRESSION_FILTER = "<Type your filter expression>";

    private static final String EXPRESSION_FILTER_ENTRY = "EXPRESSION_FILTER_ENTRY";

    private String previousTextForExpressionFilter;

    private ContentProposalAdapterExtended proposalForExpressionFilterText;

    private ExecutionLimiter executionLimiterForCheckProblemsExpressionFilter;

    private ExecutionLimiter executionLimiterForExpressionFilterSetText = null;

    private ContentProposalAdapterExtended expressionProposalStyledText;

    private ToolItem activateFilterCheck;

    private boolean previousStateCheckFilter;

    private IExpressionBuilderDialogController dialog;

    /**
     * 
     * Call loaded() method after instanciate this class.
     * 
     * @param parent
     * @param style
     * @param abstractDataMapTable
     * @param mapperManager
     */
    public DataMapTableView(Composite parent, int style, IDataMapTable abstractDataMapTable, MapperManager mapperManager) {
        super(parent, style);
        this.mapperManager = mapperManager;
        this.abstractDataMapTable = abstractDataMapTable;
        expressionColorProvider = new ExpressionColorProvider();
        createComponents();
        addListeners();
        mapperManager.addTablePair(DataMapTableView.this, abstractDataMapTable);
    }

    private void createComponents() {

        final Display display = this.getDisplay();
        // final Color listForeground =
        // display.getSystemColor(SWT.COLOR_WIDGET_FOREGROUND);
        final Color listBackground = display.getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW);

        this.setBackground(listBackground);

        GridLayout mainLayout = new GridLayout();
        int marginMainLayout = 0;
        mainLayout.marginLeft = marginMainLayout;
        mainLayout.marginRight = marginMainLayout;
        mainLayout.marginTop = marginMainLayout;
        mainLayout.marginBottom = marginMainLayout;
        mainLayout.marginWidth = marginMainLayout;
        mainLayout.marginHeight = marginMainLayout;
        int spacingMainLayout = 2;
        mainLayout.horizontalSpacing = spacingMainLayout;
        mainLayout.verticalSpacing = spacingMainLayout;
        setLayout(mainLayout);

        headerComposite = new Composite(this, SWT.NONE);
        GridData headerGridData = new GridData(GridData.FILL_HORIZONTAL);
        headerGridData.heightHint = getHeaderHeight();
        headerComposite.setLayoutData(headerGridData);
        GridLayout headerLayout = new GridLayout();

        int margin = 0;
        headerLayout.marginLeft = 3;
        headerLayout.marginRight = margin;
        headerLayout.marginTop = margin;
        headerLayout.marginBottom = margin;
        headerLayout.marginWidth = margin;
        headerLayout.marginHeight = margin;
        int spacing = 2;
        headerLayout.horizontalSpacing = spacing;
        headerLayout.verticalSpacing = spacing;
        headerComposite.setLayout(headerLayout);

        nameLabel = new Label(headerComposite, SWT.NONE);
        nameLabel.setFont(FontProviderMapper.getFont(FontInfo.FONT_SYSTEM_BOLD));
        nameLabel.setText(abstractDataMapTable.getName());
        nameLabel.setToolTipText(abstractDataMapTable.getName());
        GridData dataNameLabel = new GridData(GridData.FILL_HORIZONTAL);
        dataNameLabel.minimumWidth = nameLabel.getText().length() * 8;

        nameLabel.setLayoutData(dataNameLabel);
        // nameLabel.setBackground(nameLabel.getDisplay().getSystemColor(SWT.COLOR_RED));

        int rightStyle = toolbarNeedToHaveRightStyle() ? SWT.RIGHT : SWT.NONE;
        toolBarActions = new ToolBar(headerComposite, SWT.FLAT | rightStyle | SWT.NONE);
        // toolBarActions.setBackground(nameLabel.getDisplay().getSystemColor(SWT.COLOR_BLUE));

        if (addToolItems()) {
            addToolItemSeparator();
        }

        minimizeButton = new ToolItem(toolBarActions, SWT.PUSH);
        realToolbarSize.x += 45;

        Point sizeToolBar = toolBarActions.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        Rectangle trim = toolBarActions.computeTrim(0, 0, 0, 0);
        // System.out.println(getDataMapTable().getName());
        // System.out.println("sizeToolBar:" + sizeToolBar);

        GridData gridData = new GridData();

        // gridData.grabExcessHorizontalSpace = true;
        // gridData.horizontalAlignment = SWT.END;
        gridData.heightHint = sizeToolBar.y;
        if (toolbarNeedToHaveRightStyle() && WindowSystem.isWIN32()) {
            if (realToolbarSize != null) {
                gridData.widthHint = realToolbarSize.x;
                // System.out.println("realToolbarSize:" + realToolbarSize);
            }
            // to correct invalid margin when SWT.RIGHT style set in ToolBar
            // gridData.widthHint -= 48;
        }
        if (WindowSystem.isGTK()) {
            gridData.heightHint = 26;
        }
        // gridData.widthHint = 50;
        toolBarActions.setLayoutData(gridData);

        headerLayout.numColumns = headerComposite.getChildren().length;

        centerComposite = new Composite(this, SWT.NONE);
        GridData centerData = new GridData(GridData.FILL_BOTH);
        centerComposite.setLayoutData(centerData);

        GridLayout centerLayout = new GridLayout();
        int marginCenterLayout = 0;
        centerLayout.marginLeft = marginCenterLayout;
        centerLayout.marginRight = marginCenterLayout;
        centerLayout.marginTop = marginCenterLayout;
        centerLayout.marginBottom = marginCenterLayout;
        centerLayout.marginWidth = marginCenterLayout;
        centerLayout.marginHeight = marginCenterLayout;
        int spacingCenterLayout = 2;
        centerLayout.horizontalSpacing = spacingCenterLayout;
        centerLayout.verticalSpacing = spacingCenterLayout;
        centerComposite.setLayout(centerLayout);

        if (mapperManager.isAdvancedMap() && this instanceof OutputDataMapTableView) {
            createExpressionFilter();
            initTableFilters();
        } else {
            initTableFilters();
        }

        createContent();

        if (!mapperManager.componentIsReadOnly()) {
            new DragNDrop(mapperManager, tableForEntries, true, true);
        }

        Composite footerComposite = new Composite(this, SWT.NONE);
        GridData footerGridData = new GridData(10, 2);
        footerComposite.setLayoutData(footerGridData);

    }

    /**
     * DOC amaumont Comment method "createContent".
     */
    protected abstract void createContent();

    /**
     * DOC amaumont Comment method "addToolItemSeparator".
     */
    protected void addToolItemSeparator() {
        ToolItem separator = new ToolItem(toolBarActions, SWT.SEPARATOR);
        separator.setWidth(10);
        // separator.setControl(headerComposite);
    }

    /**
     * DOC amaumont Comment method "createTableForColumns".
     */
    protected void createTableForColumns() {
        this.extendedTableViewerForColumns = new AbstractExtendedTableViewer<IColumnEntry>(abstractDataMapTable
                .getTableColumnsEntriesModel(), centerComposite) {

            @Override
            protected void createColumns(TableViewerCreator<IColumnEntry> tableViewerCreator, Table table) {
                initColumnsOfTableColumns(tableViewerCreator);
            }

            /*
             * (non-Javadoc)
             * 
             * @see org.talend.commons.ui.swt.extended.table.AbstractExtendedTableViewer#initTableListeners()
             */
            @Override
            protected void initTableListeners() {
                super.initTableListeners();
                getTableViewerCreator().addCellValueModifiedListener(new ITableCellValueModifiedListener() {

                    public void cellValueModified(TableCellValueModifiedEvent e) {
                        getTableViewerCreator().getTable().deselectAll();
                    }

                });
            }

            /*
             * (non-Javadoc)
             * 
             * @see org.talend.commons.ui.swt.extended.macrotable.AbstractExtendedTableViewer#setTableViewerCreatorOptions(org.talend.commons.ui.swt.tableviewer.TableViewerCreator)
             */
            @Override
            protected void setTableViewerCreatorOptions(final TableViewerCreator<IColumnEntry> newTableViewerCreator) {
                super.setTableViewerCreatorOptions(newTableViewerCreator);
                newTableViewerCreator.setColumnsResizableByDefault(true);
                newTableViewerCreator.setBorderVisible(false);
                newTableViewerCreator.setLayoutMode(LAYOUT_MODE.FILL_HORIZONTAL);
                newTableViewerCreator.setKeyboardManagementForCellEdition(false);
                // tableViewerCreatorForColumns.setUseCustomItemColoring(this.getDataMapTable()
                // instanceof
                // AbstractInOutTable);
                newTableViewerCreator.setFirstColumnMasked(true);

                if (getDataMapTable() instanceof AbstractInOutTable) {

                    if (imageKey == null) {
                        imageKey = org.talend.commons.ui.image.ImageProvider.getImage(EImage.KEY_ICON);
                    }
                    if (imageEmpty == null) {
                        imageEmpty = org.talend.commons.ui.image.ImageProvider.getImage(EImage.EMPTY);
                    }
                }
                newTableViewerCreator.setLabelProvider(new DefaultTableLabelProvider(newTableViewerCreator) {

                    @Override
                    public Color getBackground(Object element, int columnIndex) {
                        return getBackgroundCellColor(newTableViewerCreator, element, columnIndex);
                    }

                    @Override
                    public Color getForeground(Object element, int columnIndex) {
                        return getForegroundCellColor(newTableViewerCreator, element, columnIndex);
                    }

                    @Override
                    public Image getColumnImage(Object element, int columnIndex) {
                        return getColumnImageExecute(element, columnIndex);
                    }

                    /**
                     * DOC amaumont Comment method "getColumnImageExecute".
                     * 
                     * @param element
                     * @param columnIndex
                     * @return
                     */
                    private Image getColumnImageExecute(Object element, int columnIndex) {
                        TableViewerCreatorColumn column = newTableViewerCreator.getColumns().get(columnIndex);
                        if (getDataMapTable() instanceof AbstractInOutTable) {
                            AbstractInOutTableEntry entry = (AbstractInOutTableEntry) element;
                            if (column.getId().equals(ID_NAME_COLUMN)) {
                                if (entry.getMetadataColumn().isKey()) {
                                    return imageKey;
                                } else {
                                    return imageEmpty;
                                }
                            }
                        }
                        if (column.getImageProvider() != null) {
                            return column.getImageProvider().getImage(element);
                        }
                        return null;
                    }

                });

            }

        };
        tableViewerCreatorForColumns = this.extendedTableViewerForColumns.getTableViewerCreator();
        this.extendedTableViewerForColumns.setCommandStack(mapperManager.getCommandStack());

        tableForEntries = tableViewerCreatorForColumns.getTable();

        GridData tableEntriesGridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        tableEntriesGridData.grabExcessVerticalSpace = true;
        tableEntriesGridData.minimumHeight = tableForEntries.getHeaderHeight() + tableForEntries.getItemHeight();
        tableForEntries.setLayoutData(tableEntriesGridData);

        addTableForColumnsListeners();

    }

    /**
     * DOC amaumont Comment method "addTableForColumnsListeners".
     */
    private void addTableForColumnsListeners() {
        tableViewerCreatorForColumns.addCellValueModifiedListener(new ITableCellValueModifiedListener() {

            public void cellValueModified(TableCellValueModifiedEvent e) {
                unselectAllEntriesIfErrorDetected(e);
            }
        });

        final TableViewer tableViewerForEntries = tableViewerCreatorForColumns.getTableViewer();

        tableViewerForEntries.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                int[] selectionIndices = tableViewerForEntries.getTable().getSelectionIndices();
                if (selectionIndices.length > 0) {
                    selectThisDataMapTableView();
                    onSelectedEntries(event.getSelection(), selectionIndices);
                }
            }

        });

        tableForEntries.addListener(SWT.DragDetect, new Listener() {

            public void handleEvent(Event event) {
                onSelectedEntries(tableViewerForEntries.getSelection(), tableViewerForEntries.getTable()
                        .getSelectionIndices());
            }

        });

        tableViewerCreatorForColumns.getSelectionHelper().addAfterSelectionListener(new ILineSelectionListener() {

            public void handle(LineSelectionEvent e) {
                if (forceExecuteSelectionEvent) {
                    forceExecuteSelectionEvent = false;
                    onSelectedEntries(tableViewerForEntries.getSelection(), tableViewerForEntries.getTable()
                            .getSelectionIndices());
                }
            }

        });
        tableForEntries.addListener(SWT.KeyDown, new Listener() {

            public void handleEvent(Event event) {
                processEnterKeyDown(tableViewerCreatorForColumns, event);
            }

        });

        abstractDataMapTable.getTableColumnsEntriesModel().addModifiedBeanListener(
                new IModifiedBeanListener<IColumnEntry>() {

                    public void handleEvent(ModifiedBeanEvent<IColumnEntry> event) {

                        TableViewerCreator tableViewerCreator = tableViewerCreatorForColumns;
                        ITableEntry tableEntry = event.bean;

                        parseExpression(event, tableViewerCreator, tableEntry);
                    }

                });

        if (abstractDataMapTable instanceof OutputTable) {
            OutputTable outputTable = (OutputTable) abstractDataMapTable;
            outputTable.getTableFiltersEntriesModel().addAfterOperationListListener(
                    new IListenableListListener<FilterTableEntry>() {

                        public void handleEvent(ListenableListEvent<FilterTableEntry> event) {

                            resizeAtExpandedSize();

                        }

                    });
            outputTable.getTableFiltersEntriesModel().addModifiedBeanListener(
                    new IModifiedBeanListener<FilterTableEntry>() {

                        public void handleEvent(ModifiedBeanEvent<FilterTableEntry> event) {

                            TableViewerCreator tableViewerCreator = tableViewerCreatorForFilters;
                            ITableEntry tableEntry = event.bean;

                            parseExpression(event, tableViewerCreator, tableEntry);
                        }

                    });
        }
    }

    /**
     * DOC amaumont Comment method "initShowMessageErrorListener".
     * 
     * @param table
     */
    protected void initShowMessageErrorListener(final Table table) {
        showErrorMessageListener = new Listener() {

            public void handleEvent(Event event) {

                switch (event.type) {
                case SWT.MouseMove:

                    // System.out.println("ToolTipText:" +
                    // table.getToolTipText());

                    Point cursorPositionFromTableOrigin = TableUtils.getCursorPositionFromTableOrigin(table, event);
                    TableColumn tableColumn = TableUtils.getTableColumn(table, cursorPositionFromTableOrigin);
                    if (tableColumn == null) {
                        setTableToolTipText(table, null, null, null);
                        return;
                    }
                    TableItem tableItem = TableUtils.getTableItem(table, cursorPositionFromTableOrigin);

                    if (tableItem == null) {
                        setTableToolTipText(table, tableColumn, null, null);
                        return;
                    }
                    ITableEntry tableEntry = (ITableEntry) tableItem.getData();
                    String toolTip = null;
                    if (tableEntry.getProblems() != null) {
                        List<Problem> problems = tableEntry.getProblems();
                        toolTip = createErrorContentForTooltip(problems);
                    }

                    String tableToolTip = table.getToolTipText();
                    if (!WindowSystem.isGTK()
                            || WindowSystem.isGTK()
                            && ((tableToolTip == null || tableToolTip.equals("")) && toolTip != null || tableToolTip != null //$NON-NLS-1$
                                    && toolTip == null || toolTip != null && !toolTip.equals(tableToolTip))) {
                        setTableToolTipText(table, tableColumn, tableEntry, toolTip);
                    }
                    break;
                default:
                }
            }

            /**
             * DOC amaumont Comment method "setTableToolTipText".
             * 
             * @param table
             * @param tableColumn
             * @param text
             */
            private void setTableToolTipText(final Table table, TableColumn tableColumn, ITableEntry tableEntry,
                    String text) {
                table.setToolTipText(text);
            }

        };
        table.addListener(SWT.MouseMove, showErrorMessageListener);
    }

    /**
     * DOC amaumont Comment method "addEntriesActionsComponents".
     * 
     * @return true if one or more ToolItem has been added
     */
    protected abstract boolean addToolItems();

    /**
     * DOC amaumont Comment method "initTableConstraints".
     */
    protected abstract void initTableFilters();

    /**
     * DOC amaumont Comment method "addListeners".
     */
    protected void addListeners() {

        final UIManager uiManager = mapperManager.getUiManager();

        MouseTrackListener mouseTracker = new MouseTrackListener() {

            public void mouseEnter(MouseEvent e) {
            }

            public void mouseExit(MouseEvent e) {
                setDefaultCursor();
                resizeHelper.stopDrag();
            }

            public void mouseHover(MouseEvent e) {
            }

        };
        this.addMouseTrackListener(mouseTracker);

        SelectionListener scrollListener = new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent event) {
                uiManager.refreshBackground(true, false);
            }
        };

        tableForEntries.getVerticalBar().addSelectionListener(scrollListener);

        // /////////////////////////////////////////////////////////////////

        addManualTableResizeListeners(uiManager);
        // /////////////////////////////////////////////////////////////////

        // /////////////////////////////////////////////////////////////////
        Listener onSelectDataMapTableViewListener = new Listener() {

            public void handleEvent(Event event) {
                switch (event.type) {
                case SWT.MouseDown:
                    onSelectDataMapTableView();
                    break;
                default:
                }

            }
        };

        this.addListener(SWT.MouseDown, onSelectDataMapTableViewListener);
        headerComposite.addListener(SWT.MouseDown, onSelectDataMapTableViewListener);
        nameLabel.addListener(SWT.MouseDown, onSelectDataMapTableViewListener);
        // /////////////////////////////////////////////////////////////////

        // /////////////////////////////////////////////////////////////////
        minimizeButton.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e) {
                // TODO Auto-generated method stub

            }

            public void widgetSelected(SelectionEvent e) {
                minimizeTable(!abstractDataMapTable.isMinimized());
            }

        });

        // /////////////////////////////////////////////////////////////////

        initShowMessageErrorListener(tableForEntries);

    }

    /**
     * DOC amaumont Comment method "addManualTableResizeListeners".
     * 
     * @param uiManager
     */
    private void addManualTableResizeListeners(final UIManager uiManager) {
        this.addDisposeListener(new DisposeListener() {

            public void widgetDisposed(DisposeEvent e) {
                if (currentCursor != null) {
                    currentCursor.dispose();
                }
            }

        });

        Listener tableResizerListener = new Listener() {

            public void handleEvent(Event event) {

                MousePositionAnalyser mpa = new MousePositionAnalyser(DataMapTableView.this);
                Point eventPoint = new Point(event.x, event.y);

                boolean leftMouseButton = (event.stateMask & SWT.BUTTON1) != 0 || event.button == 1;

                switch (event.type) {
                case SWT.MouseMove:
                    if (resizeHelper.isDragging()) {
                        Point newPoint = convertToParentOrigin(DataMapTableView.this, eventPoint);
                        Point dragPoint = resizeHelper.getLastDragPoint();
                        Point diff = new Point(newPoint.x - dragPoint.x, newPoint.y - dragPoint.y);
                        if (mpa.isOnLeftBorder(eventPoint)) {
                            diff.x *= -1;
                        }
                        Rectangle rect = DataMapTableView.this.getClientArea();
                        rect.width += 2 * DataMapTableView.this.getBorderWidth();
                        rect.height += 2 * DataMapTableView.this.getBorderWidth();

                        RESIZE_MODE currentMode = resizeHelper.getCurrentMode();
                        int newWidth = (currentMode == RESIZE_MODE.HORIZONTAL || currentMode == RESIZE_MODE.BOTH) ? rect.width
                                + diff.x * 2
                                : rect.width;
                        int newHeight = (currentMode == RESIZE_MODE.VERTICAL || currentMode == RESIZE_MODE.BOTH) ? rect.height
                                + diff.y
                                : rect.height;

                        if (newHeight < MINIMUM_HEIGHT + OFFSET_HEIGHT_TRIGGER && diff.y < 0) {
                            changeMinimizeState(true);
                            newHeight = MINIMUM_HEIGHT;
                        } else if (newHeight > MINIMUM_HEIGHT + OFFSET_HEIGHT_TRIGGER && diff.y > 0) {
                            changeMinimizeState(false);
                        }
                        changeSize(new Point(newWidth, newHeight), false, true);
                        resizeHelper.setLastDragPoint(newPoint);
                    } else if (!resizeHelper.isDragging()) {
                        Cursor cursor = null;
                        if (mpa.isOnLeftBottomCorner(eventPoint)) {
                            // cursor = new
                            // Cursor(dataMapTableView.getDisplay(),
                            // org.eclipse.swt.SWT.CURSOR_SIZESW);
                            // resizeHelper.setCurrentMode(RESIZE_MODE.BOTH);
                        } else if (mpa.isOnRightBottomCorner(eventPoint)) {
                            // cursor = new
                            // Cursor(dataMapTableView.getDisplay(),
                            // org.eclipse.swt.SWT.CURSOR_SIZESE);
                            // resizeHelper.setCurrentMode(RESIZE_MODE.BOTH);
                        } else if (mpa.isOnLeftBorder(eventPoint)) {
                            // cursor = new
                            // Cursor(dataMapTableView.getDisplay(),
                            // org.eclipse.swt.SWT.CURSOR_SIZEE);
                            // resizeHelper.setCurrentMode(RESIZE_MODE.HORIZONTAL);
                        } else if (mpa.isOnRightBorder(eventPoint)) {
                            // cursor = new
                            // Cursor(dataMapTableView.getDisplay(),
                            // org.eclipse.swt.SWT.CURSOR_SIZEW);
                            // resizeHelper.setCurrentMode(RESIZE_MODE.HORIZONTAL);
                        } else if (mpa.isOnBottomBorder(eventPoint)) {
                            cursor = new Cursor(DataMapTableView.this.getDisplay(), org.eclipse.swt.SWT.CURSOR_SIZES);
                            resizeHelper.setCurrentMode(RESIZE_MODE.VERTICAL);
                        }
                        if (cursor != null) {
                            DataMapTableView.this.setCursor(cursor);
                        } else {
                            setDefaultCursor();
                            resizeHelper.setCurrentMode(RESIZE_MODE.NONE);
                        }
                    }

                    break;
                case SWT.MouseDown:
                    if (leftMouseButton) {
                        if (mpa.isOnLeftBorder(eventPoint) || mpa.isOnRightBorder(eventPoint)
                                || mpa.isOnBottomBorder(eventPoint)) {
                            resizeHelper.startDrag(convertToParentOrigin(DataMapTableView.this, new Point(event.x,
                                    event.y)));
                        } else {
                            setDefaultCursor();
                        }
                        parentLayout = DataMapTableView.this.getParent().getLayout();
                        DataMapTableView.this.getParent().setLayout(null);
                    }
                    break;
                case SWT.MouseUp:
                    if (leftMouseButton) {
                        resizeHelper.stopDrag();
                        // gridData = (GridData)
                        // dataMapTableView.getLayoutData();
                        // gridData.exclude = false;
                        DataMapTableView.this.getParent().setLayout(parentLayout);
                        DataMapTableView.this.getParent().layout();
                        uiManager.resizeTablesZoneViewAtComputedSize(getZone());
                        uiManager.refreshBackground(true, false);
                    }
                    break;
                case SWT.MouseExit:
                    setDefaultCursor();
                    break;
                default:
                }

            }

        };

        this.addListener(SWT.MouseMove, tableResizerListener);
        this.addListener(SWT.MouseDown, tableResizerListener);
        this.addListener(SWT.MouseUp, tableResizerListener);
    }

    /**
     * DOC amaumont Comment method "showTableConstraints".
     */
    protected void showTableConstraints(boolean visible) {

        if (visible) {
            tableForConstraintsGridData.exclude = false;
            tableForConstraints.setVisible(true);
            if (WindowSystem.isGTK()) {
                updateGridDataHeightForTableConstraints();
            }
        } else {
            tableForConstraintsGridData.exclude = true;
            tableForConstraints.setVisible(false);
        }
        tableViewerCreatorForFilters.getTableViewer().refresh();
        resizeAtExpandedSize();
    }

    /**
     * DOC amaumont Comment method "onSelectDataMapTableView".
     */
    protected void onSelectDataMapTableView() {
        mapperManager.getUiManager().selectDataMapTableView(this, true, true);
    }

    /**
     * DOC amaumont Comment method "initClumns".
     */
    public abstract void initColumnsOfTableColumns(TableViewerCreator tableViewerCreator);

    public TableViewerCreator getTableViewerCreatorForColumns() {
        return this.tableViewerCreatorForColumns;
    }

    public TableViewerCreator getTableViewerCreatorForFilters() {
        return this.tableViewerCreatorForFilters;
    }

    public Point convertToParentOrigin(Composite child, Point point) {
        Rectangle bounds = child.getBounds();
        return new Point(bounds.x + point.x, bounds.y + point.y);
    }

    private void setDefaultCursor() {
        Cursor cursor = new Cursor(DataMapTableView.this.getDisplay(), 0);
        DataMapTableView.this.setCursor(cursor);
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    public MapperManager getMapperManager() {
        return this.mapperManager;
    }

    public abstract Zone getZone();

    /**
     * DOC amaumont Comment method "resizeWithPreferredSize".
     */
    public void resizeAtExpandedSize() {
        changeSize(getPreferredSize(false, true, false), true, true);
    }

    /**
     * 
     * Change size of the DataMapTableView.
     * 
     * @param newWidth
     * @param newHeight
     * @param refreshNow refresh background with links if true
     * @param recalculateParentSize
     */
    public void changeSize(Point newSize, boolean refreshNow, boolean recalculateParentSize) {
        if (newSize.y < MINIMUM_HEIGHT) {
            newSize.y = MINIMUM_HEIGHT;
        }
        FormData formData = (FormData) DataMapTableView.this.getLayoutData();
        formData.width = newSize.x;
        formData.height = newSize.y;
        DataMapTableView.this.setSize(newSize);
        UIManager uiManager = mapperManager.getUiManager();
        if (recalculateParentSize) {
            uiManager.resizeTablesZoneViewAtComputedSize(getZone());
        }
        if (refreshNow) {
            uiManager.refreshBackground(true, false);
        } else {
            uiManager.refreshBackground(true, TIME_BEFORE_NEW_REFRESH_BACKGROUND, true);
        }
        // dataMapTableView.redraw();
        // dataMapTableView.layout(true, true);
    }

    public void onSelectedEntries(ISelection selection, int[] selectionIndices) {
        UIManager uiManager = mapperManager.getUiManager();
        List<ITableEntry> selectionEntries = uiManager.extractSelectedTableEntries(selection);
        if (executeSelectionEvent) {
            uiManager.selectLinks(DataMapTableView.this, selectionEntries, false, false);
            uiManager.selectLinkedMetadataEditorEntries(this, selectionIndices);
        }
        if (selectionIndices.length == 1 && mapperManager.componentIsReadOnly()) {
            ITableEntry tableEntry = selectionEntries.get(0);
            StyledText styledText = mapperManager.getUiManager().getTabFolderEditors().getStyledTextHandler()
                    .getStyledText();
            styledText.setEnabled(true);
            styledText.setEditable(false);
            styledText.setText(tableEntry.getExpression() == null ? "" : tableEntry.getExpression());
        }
    }

    public IDataMapTable getDataMapTable() {
        return this.abstractDataMapTable;
    }

    /**
     * DOC amaumont Comment method "setTableSelection".
     * 
     * @param selectionIndices
     */
    public void setTableSelection(int[] selectionIndices) {
        this.executeSelectionEvent = false;
        this.tableViewerCreatorForColumns.getSelectionHelper().setSelection(selectionIndices);
        this.executeSelectionEvent = true;

    }

    protected void createFiltersToolItems() {

        if (mapperManager.isAdvancedMap()) {

            createActivateFilterCheck();

        } else {

            ToolItem addFilterButton = new ToolItem(toolBarActions, SWT.PUSH);
            addFilterButton.setEnabled(!mapperManager.componentIsReadOnly());
            addFilterButton.setToolTipText(Messages.getString("DataMapTableView.buttonTooltip.addFilterRow")); //$NON-NLS-1$
            addFilterButton.setImage(ImageProviderMapper.getImage(ImageInfo.ADD_FILTER_ICON));

            // /////////////////////////////////////////////////////////////////
            if (addFilterButton != null) {

                addFilterButton.addSelectionListener(new SelectionListener() {

                    public void widgetDefaultSelected(SelectionEvent e) {
                    }

                    public void widgetSelected(SelectionEvent e) {

                        Table tableConstraints = tableViewerCreatorForFilters.getTable();
                        int index = tableConstraints.getItemCount();
                        int[] selection = tableViewerCreatorForFilters.getTable().getSelectionIndices();
                        if (selection.length > 0) {
                            index = selection[selection.length - 1] + 1;
                        }
                        mapperManager
                                .addNewFilterEntry(DataMapTableView.this, "newFilter" + ++constraintCounter, index); //$NON-NLS-1$
                        updateGridDataHeightForTableConstraints();
                        DataMapTableView.this.changeSize(DataMapTableView.this.getPreferredSize(false, true, true),
                                true, true);
                        tableViewerCreatorForFilters.getTableViewer().refresh();
                        mapperManager.getUiManager().refreshBackground(true, false);
                        showTableConstraints(true);
                        changeMinimizeState(false);
                        tableViewerCreatorForFilters.layout();
                    }

                });
            }
            // /////////////////////////////////////////////////////////////////
        }

        final ToolItem rejectFilterCheck = new ToolItem(toolBarActions, SWT.CHECK);
        rejectFilterCheck.setEnabled(!mapperManager.componentIsReadOnly());
        rejectFilterCheck.setToolTipText(Messages.getString("DataMapTableView.widgetTooltip.enableOutputReject")); //$NON-NLS-1$
        boolean isReject = ((OutputTable) abstractDataMapTable).isReject();
        // Image image = ImageProviderMapper.getImage(isReject ?
        // ImageInfo.CHECKED_ICON : ImageInfo.UNCHECKED_ICON);
        Image image = ImageProviderMapper.getImage(isReject ? ImageInfo.REJECT_FILTER_ICON
                : ImageInfo.REJECT_FILTER_ICON);
        if (WindowSystem.isGTK()) {
            rejectFilterCheck.setImage(image);
            rejectFilterCheck.setHotImage(image);
        } else {
            rejectFilterCheck.setImage(ImageProviderMapper.getImage(ImageInfo.REJECT_FILTER_ICON));
            rejectFilterCheck.setHotImage(image);
        }
        rejectFilterCheck.setSelection(isReject);

        // /////////////////////////////////////////////////////////////////
        if (rejectFilterCheck != null) {

            rejectFilterCheck.addSelectionListener(new SelectionListener() {

                public void widgetDefaultSelected(SelectionEvent e) {
                }

                public void widgetSelected(SelectionEvent e) {
                    Image image = null;
                    if (rejectFilterCheck.getSelection()) {
                        ((OutputTable) abstractDataMapTable).setReject(true);
                        image = ImageProviderMapper.getImage(ImageInfo.REJECT_FILTER_ICON);
                    } else {
                        ((OutputTable) abstractDataMapTable).setReject(false);
                        image = ImageProviderMapper.getImage(ImageInfo.REJECT_FILTER_ICON);
                    }
                    if (WindowSystem.isGTK()) {
                        rejectFilterCheck.setImage(image);
                        rejectFilterCheck.setHotImage(image);
                    } else {
                        rejectFilterCheck.setHotImage(image);
                    }
                }

            });

        }

        // // /////////////////////////////////////////////////////////////////
        final ToolItem rejectInnerJoinFilterCheck = new ToolItem(toolBarActions, SWT.CHECK);
        rejectInnerJoinFilterCheck.setEnabled(!mapperManager.componentIsReadOnly());
        rejectInnerJoinFilterCheck.setToolTipText(Messages
                .getString("DataMapTableView.widgetTooltip.enableLookupInnerJoin")); //$NON-NLS-1$
        boolean isRejectInnerJoin = ((OutputTable) abstractDataMapTable).isRejectInnerJoin();
        // image = ImageProviderMapper.getImage(isRejectInnerJoin ?
        // ImageInfo.CHECKED_ICON : ImageInfo.UNCHECKED_ICON);
        image = ImageProviderMapper.getImage(isRejectInnerJoin ? ImageInfo.REJECT_LOOKUP_ICON
                : ImageInfo.REJECT_LOOKUP_ICON);
        if (WindowSystem.isGTK()) {
            rejectInnerJoinFilterCheck.setImage(image);
            rejectInnerJoinFilterCheck.setHotImage(image);
        } else {
            rejectInnerJoinFilterCheck.setImage(ImageProviderMapper.getImage(ImageInfo.REJECT_LOOKUP_ICON));
            // rejectInnerJoinFilterCheck.setImage(ImageProviderMapper.getImage(ImageInfo.UNCHECKED_ICON));
            rejectInnerJoinFilterCheck.setHotImage(image);
        }
        rejectInnerJoinFilterCheck.setSelection(isRejectInnerJoin);
        // rejectInnerJoinFilterCheck.setText("Reject2");
        // /////////////////////////////////////////////////////////////////
        if (rejectInnerJoinFilterCheck != null) {

            rejectInnerJoinFilterCheck.addSelectionListener(new SelectionListener() {

                public void widgetDefaultSelected(SelectionEvent e) {
                }

                public void widgetSelected(SelectionEvent e) {
                    Image image = null;
                    if (rejectInnerJoinFilterCheck.getSelection()) {
                        ((OutputTable) abstractDataMapTable).setRejectInnerJoin(true);
                        image = ImageProviderMapper.getImage(ImageInfo.REJECT_LOOKUP_ICON);
                        // image =
                        // ImageProviderMapper.getImage(ImageInfo.CHECKED_ICON);
                    } else {
                        ((OutputTable) abstractDataMapTable).setRejectInnerJoin(false);
                        image = ImageProviderMapper.getImage(ImageInfo.REJECT_LOOKUP_ICON);
                        // image =
                        // ImageProviderMapper.getImage(ImageInfo.UNCHECKED_ICON);
                    }
                    if (WindowSystem.isGTK()) {
                        rejectInnerJoinFilterCheck.setImage(image);
                        rejectInnerJoinFilterCheck.setHotImage(image);
                    } else {
                        rejectInnerJoinFilterCheck.setHotImage(image);
                    }
                }

            });

        }

        // /////////////////////////////////////////////////////////////////

    }

    /**
     * DOC amaumont Comment method "createActivateFilterCheck".
     */
    protected void createActivateFilterCheck() {
        AbstractInOutTable table = (AbstractInOutTable) getDataMapTable();
        activateFilterCheck = new ToolItem(toolBarActions, SWT.CHECK);
        activateFilterCheck.setEnabled(!mapperManager.componentIsReadOnly());
        previousStateCheckFilter = table.isActivateExpressionFilter();
        activateFilterCheck.setSelection(table.isActivateExpressionFilter());
        activateFilterCheck.setToolTipText(Messages
                .getString("DataMapTableView.buttonTooltip.activateExpressionFilter")); //$NON-NLS-1$
        activateFilterCheck.setImage(ImageProviderMapper.getImage(ImageInfo.ACTIVATE_FILTER_ICON));

        // /////////////////////////////////////////////////////////////////
        if (activateFilterCheck != null) {

            activateFilterCheck.addSelectionListener(new SelectionListener() {

                public void widgetDefaultSelected(SelectionEvent e) {
                }

                public void widgetSelected(SelectionEvent e) {
                    updateViewAfterChangingFilterCheck();
                    previousStateCheckFilter = activateFilterCheck.getSelection();
                }

            });
        }
        // /////////////////////////////////////////////////////////////////
    }

    protected void createToolItems() {

    }

    public void minimizeTable(boolean minimize) {
        Point size = DataMapTableView.this.getSize();
        if (minimize) {
            // System.out.println("store height before minimize"+size.y);
            this.heightForRestore = size.y - 4;
            changeSize(new Point(size.x, getHeaderHeight()), true, true);
            changeMinimizeState(true);
        } else {
            if (heightForRestore != getHeaderHeight() && heightForRestore > 0) {
                size.y = heightForRestore;
            } else {
                size = DataMapTableView.this.getPreferredSize(false, true, false);
            }
            changeSize(size, true, true);
            changeMinimizeState(false);
        }
    }

    public void changeMinimizeState(boolean newStateIsMinimized) {
        if (newStateIsMinimized) {
            abstractDataMapTable.setMinimized(true);
            minimizeButton.setSelection(true);
            minimizeButton.setImage(ImageProviderMapper.getImage(ImageInfo.RESTORE_ICON));
            minimizeButton.setToolTipText(Messages.getString("DataMapTableView.buttonTooltip.restore")); //$NON-NLS-1$
        } else {
            abstractDataMapTable.setMinimized(false);
            minimizeButton.setSelection(false);
            minimizeButton.setImage(ImageProviderMapper.getImage(ImageInfo.MINIMIZE_ICON));
            minimizeButton.setToolTipText(Messages.getString("DataMapTableView.buttonTooltip.minimize")); //$NON-NLS-1$
        }
    }

    /**
     * 
     * Return current size of DataMapTableView.
     * 
     * @return
     */
    public Point getPreferredSize() {
        return getPreferredSize(false, false, false);
    }

    /**
     * 
     * DOC amaumont Comment method "getPreferredSize".
     * 
     * @param newTableEntryWillBeAdded
     * @param expandedSize
     * @param newConstraintEntryWillBeAdded
     * @return
     */
    public Point getPreferredSize(boolean newTableEntryWillBeAdded, boolean expandedSize,
            boolean newConstraintEntryWillBeAdded) {
        int heightOffset = 0;
        int tableEntryItemHeight = tableViewerCreatorForColumns.getTable().getItemHeight();
        // heightOffset += newTableEntryWillBeAdded ? tableEntryItemHeight : 0;
        heightOffset += newConstraintEntryWillBeAdded ? tableViewerCreatorForFilters.getTable().getItemHeight() : 0;

        int newHeight = this.computeSize(SWT.DEFAULT, SWT.DEFAULT).y - tableEntryItemHeight + heightOffset;
        if (WindowSystem.isGTK()) {
            newHeight += tableEntryItemHeight;
        }
        if (tableViewerCreatorForColumns.getInputList().size() == 0) {
            newHeight += tableEntryItemHeight;
        } else {
            newHeight += 5;
        }

        if (abstractDataMapTable.isMinimized() && !expandedSize) {
            newHeight = MINIMUM_HEIGHT;
        }
        return new Point(this.getSize().x, newHeight);
    }

    public void registerStyledExpressionEditor(final StyledTextHandler styledTextHandler) {
        if (columnExpressionTextEditor != null) {
            attachCellExpressionToStyledTextEditor(tableViewerCreatorForColumns, columnExpressionTextEditor,
                    styledTextHandler);
        }
        if (constraintExpressionTextEditor != null) {
            attachCellExpressionToStyledTextEditor(tableViewerCreatorForFilters, constraintExpressionTextEditor,
                    styledTextHandler);
        }
    }

    /**
     * DOC amaumont Comment method "attachCellExpressionToStyledTextEditor".
     * 
     * @param tableViewerCreator TODO
     * @param styledTextHandler
     * @param expressionEditorText2
     */
    protected void attachCellExpressionToStyledTextEditor(final TableViewerCreator tableViewerCreator,
            final Text expressionTextEditor, final StyledTextHandler styledTextHandler) {
        expressionTextEditor.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                ITableEntry currentModifiedEntry = (ITableEntry) tableViewerCreator.getModifiedObjectInfo()
                        .getCurrentModifiedBean();
                styledTextHandler.setCurrentEntry(currentModifiedEntry);
                Text text = (Text) e.widget;
                if (Math.abs(text.getText().length() - styledTextHandler.getStyledText().getText().length()) > 1) {
                    styledTextHandler.setTextWithoutNotifyListeners(text.getText());
                    // highlightLineAndSetSelectionOfStyledText(expressionTextEditor);
                }
            }

        });

        expressionTextEditor.addKeyListener(new TextCellEditorToMapperStyledTextKeyListener(expressionTextEditor,
                styledTextHandler));

        expressionTextEditor.addMouseListener(new MouseListener() {

            public void mouseDoubleClick(MouseEvent e) {
                highlightLineAndSetSelectionOfStyledText(expressionTextEditor);
            }

            public void mouseDown(MouseEvent e) {
                highlightLineAndSetSelectionOfStyledText(expressionTextEditor);
            }

            public void mouseUp(MouseEvent e) {

            }

        });

    }

    private void highlightLineAndSetSelectionOfStyledText(final Text expressionTextEditor) {
        highlightLineAndSetSelectionOfStyledTextFromTextControl(expressionTextEditor);

    }

    /**
     * DOC amaumont Comment method "highlightLineAndSetSelectionOfStyledText".
     * 
     * @param expressionTextEditor
     */
    protected void highlightLineAndSetSelectionOfStyledTextFromTextControl(final Control textWidget) {
        final StyledTextHandler styledTextHandler = mapperManager.getUiManager().getTabFolderEditors()
                .getStyledTextHandler();

        Runnable runnable = new Runnable() {

            public void run() {

                StyledText styledText = styledTextHandler.getStyledText();
                if (styledText.isDisposed()) {
                    return;
                }

                String text = ControlUtils.getText(textWidget);
                Point selection = ControlUtils.getSelection(textWidget);
                String lineDelimiter = ControlUtils.getLineDelimiter(textWidget);
                if (selection.x - 1 > 0) {
                    while (lineDelimiter.equals(text.charAt(selection.x - 1))) {
                        selection.x++;
                    }
                }
                if (selection.y - 1 > 0) {
                    while (lineDelimiter.equals(text.charAt(selection.y - 1))) {
                        selection.y++;
                    }
                }
                int length = styledText.getText().length();
                if (selection.x < 0) {
                    selection.x = 0;
                }
                if (selection.x > length) {
                    selection.x = length;
                }
                if (selection.y < 0) {
                    selection.y = 0;
                }
                if (selection.y > length) {
                    selection.y = length;
                }
                styledText.setSelection(selection);
                styledTextHandler.highlightLineOfCursorPosition(selection);

            }
        };
        new AsynchronousThreading(50, true, DataMapTableView.this.getDisplay(), runnable).start();
    }

    /**
     * 
     * Init proposals of Text control, and StyledText in tab "Expression editor".
     * 
     * @param textControl
     * @param zones
     * @param tableViewerCreator
     * @param currentModifiedEntry
     */
    protected void initExpressionProposals(final ExtendedTextCellEditorWithProposal cellEditor, Zone[] zones,
            final TableViewerCreator tableViewerCreator, ITableEntry currentModifiedEntry) {
        if (this.expressionProposalProvider == null) {
            this.expressionProposalProvider = createExpressionProposalProvider();
        }
        this.expressionProposalProvider.init(abstractDataMapTable, zones, currentModifiedEntry);
        cellEditor.setContentProposalProvider(this.expressionProposalProvider);
        cellEditor.setData(expressionProposalProvider.getVariables());

        StringBuffer id = new StringBuffer();

        IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        Object obj = ((MultiPageTalendEditor) editor).getTalendEditor().getViewer().getSelectedEditParts().get(0);
        EditPart editorPart = (EditPart) obj;
        id.append(((INode) editorPart.getModel()).getLabel() + "=>");

        TableItem[] items = tableViewerCreator.getTable().getSelection();
        if (items.length == 1) {
            TableItem item = items[0];
            AbstractInOutTableEntry entry = (AbstractInOutTableEntry) item.getData();
            id.append(entry.getParent().getName()+"=>");
            id.append(entry.getMetadataColumn().getLabel());
            
        }

        cellEditor.setOwnerId(id.toString());

        StyledTextHandler styledTextHandler = mapperManager.getUiManager().getTabFolderEditors().getStyledTextHandler();

        styledTextHandler.getStyledText().setEnabled(true);

        ContentProposalAdapterExtended expressionProposalStyledText = styledTextHandler.getContentProposalAdapter();
        expressionProposalStyledText.setContentProposalProvider(this.expressionProposalProvider);
        // System.out.println("init expression
        // proposal:"+this.expressionProposal);
    }

    /**
     * DOC amaumont Comment method "initExpressionProposalProvider".
     */
    protected ExpressionProposalProvider createExpressionProposalProvider() {
        IContentProposalProvider[] contentProposalProviders = new IContentProposalProvider[0];
        if (!MapperMain.isStandAloneMode()) {
            contentProposalProviders = new IContentProposalProvider[] { new ProcessProposalProvider(mapperManager
                    .getAbstractMapComponent().getProcess()) };
        }
        return new ExpressionProposalProvider(mapperManager, contentProposalProviders);
    }

    protected ExtendedTextCellEditor createExpressionCellEditor(final TableViewerCreator tableViewerCreator,
            TableViewerCreatorColumn column, final Zone[] zones, boolean isConstraintExpressionCellEditor) {
        IService expressionBuilderDialogService = GlobalServiceRegister.getDefault().getService(
                IExpressionBuilderDialogService.class);

        final ExtendedTextCellEditorWithProposal cellEditor = new ExtendedTextCellEditorWithProposal(tableViewerCreator
                .getTable(), SWT.MULTI | SWT.BORDER, column);

        dialog = ((IExpressionBuilderDialogService) expressionBuilderDialogService).getExpressionBuilderInstance(
                tableViewerCreator.getCompositeParent(), cellEditor);

        CellEditorDialogBehavior behavior = new CellEditorDialogBehavior(cellEditor);
        behavior.setCellEditorDialog(dialog);
        cellEditor.setCellEditorBehavior(behavior);
        cellEditor.init();

        final Text expressionTextEditor = cellEditor.getTextControl();

        if (isConstraintExpressionCellEditor) {
            constraintExpressionTextEditor = expressionTextEditor;
        } else {
            columnExpressionTextEditor = expressionTextEditor;
        }

        cellEditor.addListener(new ICellEditorListener() {

            Text text = expressionTextEditor;

            public void applyEditorValue() {
                ModifiedObjectInfo modifiedObjectInfo = tableViewerCreator.getModifiedObjectInfo();
                mapperManager.getUiManager().parseNewExpression(text.getText(),
                        (ITableEntry) modifiedObjectInfo.getCurrentModifiedBean(), true);
            }

            public void cancelEditor() {
                ModifiedObjectInfo modifiedObjectInfo = tableViewerCreator.getModifiedObjectInfo();
                text.setText((String) modifiedObjectInfo.getOriginalPropertyBeanValue());
                ITableEntry tableEntry = (ITableEntry) (modifiedObjectInfo.getCurrentModifiedBean() != null ? modifiedObjectInfo
                        .getCurrentModifiedBean()
                        : modifiedObjectInfo.getPreviousModifiedBean());
                String originalExpression = (String) modifiedObjectInfo.getOriginalPropertyBeanValue();
                mapperManager.getUiManager().parseNewExpression(originalExpression, tableEntry, true);
            }

            public void editorValueChanged(boolean oldValidState, boolean newValidState) {

                if (expressionTextEditor.isFocusControl()) {
                    ModifiedObjectInfo modifiedObjectInfo = tableViewerCreator.getModifiedObjectInfo();
                    ITableEntry tableEntry = (ITableEntry) (modifiedObjectInfo.getCurrentModifiedBean() != null ? modifiedObjectInfo
                            .getCurrentModifiedBean()
                            : modifiedObjectInfo.getPreviousModifiedBean());
                    mapperManager.getUiManager().parseNewExpression(text.getText(), tableEntry, false);
                    resizeTextEditor(text, tableViewerCreator);
                }
            }

        });
        expressionTextEditor.addControlListener(new ControlListener() {

            ExecutionLimiter executionLimiter = null;

            public void controlMoved(ControlEvent e) {
            }

            public void controlResized(ControlEvent e) {
                if (executionLimiter == null) {
                    executionLimiter = new ExecutionLimiter(50, true) {

                        @Override
                        public void execute(boolean isFinalExecution) {

                            if (isFinalExecution) {
                                expressionTextEditor.getDisplay().syncExec(new Runnable() {

                                    public void run() {
                                        if (expressionTextEditor.isDisposed()) {
                                            return;
                                        }
                                        resizeTextEditor(expressionTextEditor, tableViewerCreator);
                                    }

                                });
                            }
                        }

                    };
                }
                executionLimiter.startIfExecutable();
            }

        });
        expressionTextEditor.addFocusListener(new FocusListener() {

            public void focusGained(FocusEvent e) {
                ITableEntry currentModifiedEntry = (ITableEntry) tableViewerCreator.getModifiedObjectInfo()
                        .getCurrentModifiedBean();
                initExpressionProposals(cellEditor, zones, tableViewerCreator, currentModifiedEntry);
                resizeTextEditor(expressionTextEditor, tableViewerCreator);
                StyledTextHandler styledTextHandler = mapperManager.getUiManager().getTabFolderEditors()
                        .getStyledTextHandler();
                styledTextHandler.setCurrentEntry(currentModifiedEntry);
                styledTextHandler
                        .setTextWithoutNotifyListeners(currentModifiedEntry.getExpression() == null ? "" : currentModifiedEntry.getExpression()); //$NON-NLS-1$
            }

            public void focusLost(FocusEvent e) {
                expressionEditorTextSelectionBeforeFocusLost = expressionTextEditor.getSelection();
                checkChangementsAfterEntryModifiedOrAdded(false);
                if (WindowSystem.isGTK()) {

                    new AsynchronousThreading(50, false, expressionTextEditor.getDisplay(), new Runnable() {

                        public void run() {

                            tableViewerCreator.layout();

                        }
                    }).start();

                }
            }

        });
        column.setCellEditor(cellEditor, new CellEditorValueAdapter() {

            @Override
            public Object getCellEditorTypedValue(CellEditor cellEditor, Object originalTypedValue) {
                return super.getCellEditorTypedValue(cellEditor, originalTypedValue);
            }

            @Override
            public String getColumnText(CellEditor cellEditor, Object bean, Object cellEditorTypedValue) {
                return super.getColumnText(cellEditor, bean, cellEditorTypedValue).replaceAll("[\r\n\t]+", " ... "); //$NON-NLS-1$ //$NON-NLS-2$
            }

            @Override
            public Object getOriginalTypedValue(CellEditor cellEditor, Object cellEditorTypedValue) {
                return super.getOriginalTypedValue(cellEditor, cellEditorTypedValue);
            }

        });
        return cellEditor;
    }

    private void resizeTextEditor(Text textEditor, TableViewerCreator tableViewerCreator) {

        Point currentSize = textEditor.getSize();
        Rectangle currentBounds = textEditor.getBounds();
        String text = textEditor.getText();
        Table currentTable = tableViewerCreator.getTable();
        int itemHeight = currentTable.getItemHeight();
        int minHeight = itemHeight + 3;
        int maxHeight = 2 * itemHeight + 4;
        int newHeight = 0;
        if ((text.contains("\n") || text.contains("\r")) && currentBounds.y + maxHeight < currentTable.getBounds().height) { //$NON-NLS-1$ //$NON-NLS-2$
            newHeight = maxHeight;
        } else {
            newHeight = minHeight;
        }
        if (currentSize.y != newHeight) {
            textEditor.setSize(textEditor.getSize().x, newHeight);
        }
    }

    /**
     * DOC amaumont Comment method "updateGridDataHeightForTableConstraints".
     */
    public void updateGridDataHeightForTableConstraints() {

        int moreSpace = WindowSystem.isGTK() ? tableForConstraints.getItemHeight() : 0;
        tableForConstraintsGridData.heightHint = ((OutputTable) abstractDataMapTable).getFilterEntries().size()
                * tableForConstraints.getItemHeight() + tableForConstraints.getItemHeight() / 2 + moreSpace;
    }

    /**
     * DOC amaumont Comment method "unselectAllEntries".
     */
    public void unselectAllEntries() {
        unselectAllColumnEntries();
        unselectAllConstraintEntries();
    }

    /**
     * DOC amaumont Comment method "unselectAllConstraintEntries".
     */
    public void unselectAllConstraintEntries() {
        // nothing by default, override if necessary
    }

    /**
     * DOC amaumont Comment method "unselectAllColumnEntries".
     */
    public void unselectAllColumnEntries() {
        tableViewerCreatorForColumns.getSelectionHelper().deselectAll();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.widgets.Control#setCursor(org.eclipse.swt.graphics.Cursor)
     */
    @Override
    public void setCursor(Cursor cursor) {
        if (this.currentCursor != null) {
            this.currentCursor.dispose();
        }
        this.currentCursor = cursor;
        super.setCursor(cursor);
    }

    /**
     * DOC amaumont Comment method "selectThisDataMapTableView".
     */
    protected void selectThisDataMapTableView() {
        UIManager uiManager = mapperManager.getUiManager();
        if (uiManager.getCurrentSelectedInputTableView() != DataMapTableView.this
                && uiManager.getCurrentSelectedOutputTableView() != DataMapTableView.this) {

            uiManager.selectDataMapTableView(DataMapTableView.this, true, false);
        }
    }

    /**
     * 
     * Provide a color provider for Constraints table and dataMap table.
     * 
     * 
     * <br/>
     * 
     * $Id$
     * 
     */
    class ExpressionColorProvider {

        /**
         * DOC amaumont Comment method "getBackgroundColor".
         * 
         * @param expression
         */
        public Color getBackgroundColor(boolean validCell) {

            if (validCell) {
                return null;
            } else {
                return ColorProviderMapper.getColor(ColorInfo.COLOR_BACKGROUND_ERROR_EXPRESSION_CELL);
            }

        }

        /**
         * DOC amaumont Comment method "getBackgroundColor".
         * 
         * @param expression
         */
        public Color getForegroundColor(boolean validCell) {
            if (validCell) {
                return null;
            } else {
                return ColorProviderMapper.getColor(ColorInfo.COLOR_FOREGROUND_ERROR_EXPRESSION_CELL);
            }

        }

    }

    protected ExpressionColorProvider getExpressionColorProvider() {
        return this.expressionColorProvider;
    }

    /**
     * DOC amaumont Comment method "getCommonBackgroundColor".
     * 
     * @param element
     * @param columnIndex
     * @return
     */
    protected Color getBackgroundCellColor(TableViewerCreator tableViewerCreator, Object element, int columnIndex) {
        ITableEntry entry = (ITableEntry) element;
        TableViewerCreatorColumn column = (TableViewerCreatorColumn) tableViewerCreator.getColumns().get(columnIndex);
        if (column.getId().equals(ID_EXPRESSION_COLUMN)) {
            return expressionColorProvider.getBackgroundColor(entry.getProblems() == null ? true : false);
        }
        return null;
    }

    /**
     * DOC amaumont Comment method "getCommonBackgroundColor".
     * 
     * @param element
     * @param columnIndex
     * @return
     */
    protected Color getForegroundCellColor(TableViewerCreator tableViewerCreator, Object element, int columnIndex) {
        ITableEntry entry = (ITableEntry) element;
        TableViewerCreatorColumn column = (TableViewerCreatorColumn) tableViewerCreator.getColumns().get(columnIndex);
        if (column.getId().equals(ID_EXPRESSION_COLUMN)) {
            return expressionColorProvider.getForegroundColor(entry.getProblems() == null ? true : false);
        }
        return null;
    }

    /**
     * DOC amaumont Comment method "unselectAllEntriesIfErrorDetected".
     * 
     * @param e
     */
    protected void unselectAllEntriesIfErrorDetected(TableCellValueModifiedEvent e) {
        if (e.column.getId().equals(ID_EXPRESSION_COLUMN)) {
            ITableEntry currentEntry = (ITableEntry) e.bean;
            TableViewer tableViewer = null;
            if (currentEntry instanceof IColumnEntry) {
                tableViewer = DataMapTableView.this.getTableViewerCreatorForColumns().getTableViewer();
            } else if (currentEntry instanceof FilterTableEntry) {
                tableViewer = DataMapTableView.this.getTableViewerCreatorForFilters().getTableViewer();
            }
            if (currentEntry.getProblems() != null) {
                tableViewer.getTable().deselectAll();
            }
        }
    }

    /**
     * DOC amaumont Comment method "processEnterKeyDown".
     * 
     * @param tableViewer
     * @param event
     */
    protected void processEnterKeyDown(final TableViewerCreator tableViewerCreator, Event event) {
        if (event.character == '\r') {
            Object element = null;
            if (tableViewerCreator.getTable().getSelectionCount() == 1) {
                element = tableViewerCreator.getTable().getSelection()[0].getData();
            } else {
                element = tableViewerCreator.getModifiedObjectInfo().getPreviousModifiedBean();
            }
            if (element != null) {
                int indexColumn = tableViewerCreator.getColumn(ID_EXPRESSION_COLUMN).getIndex();
                tableViewerCreator.getTableViewer().editElement(element, indexColumn);
            }
        }
    }

    public abstract boolean toolbarNeedToHaveRightStyle();

    public abstract boolean hasDropDownToolBarItem();

    /**
     * DOC amaumont Comment method "parseExpression".
     * 
     * @param event
     * @param tableViewerCreator
     * @param tableEntry
     */
    private void parseExpression(ModifiedBeanEvent event, TableViewerCreator tableViewerCreator, ITableEntry tableEntry) {
        if (event.column == tableViewerCreator.getColumn(DataMapTableView.ID_EXPRESSION_COLUMN)) {
            mapperManager.getUiManager().parseExpression(tableEntry.getExpression(), tableEntry, false, false, false);
            mapperManager.getUiManager().refreshBackground(false, false);
        }
    }

    /**
     * Getter for extendedTableViewerForColumns.
     * 
     * @return the extendedTableViewerForColumns
     */
    public AbstractExtendedTableViewer<IColumnEntry> getExtendedTableViewerForColumns() {
        return this.extendedTableViewerForColumns;
    }

    /**
     * Getter for extendedTableViewerForFilters.
     * 
     * @return the extendedTableViewerForFilters
     */
    public AbstractExtendedTableViewer<FilterTableEntry> getExtendedTableViewerForFilters() {
        return this.extendedTableViewerForFilters;
    }

    protected Composite getCenterComposite() {
        return this.centerComposite;
    }

    protected ExpressionProposalProvider getExpressionProposalProvider() {
        return this.expressionProposalProvider;
    }

    public int getHeaderHeight() {
        return HEADER_HEIGHT + (hasDropDownToolBarItem() ? 8 : 0);
    }

    public void checkChangementsAfterEntryModifiedOrAdded(boolean forceEvaluation) {

    }

    public Point getRealToolbarSize() {
        return realToolbarSize;
    }

    /**
     * DOC amaumont Comment method "createExpressionFilter".
     */
    protected void createExpressionFilter() {
        if (mapperManager.isAdvancedMap() && getDataMapTable() instanceof AbstractInOutTable) {

            final AbstractInOutTable table = (AbstractInOutTable) getDataMapTable();

            IPreferenceStore preferenceStore = CorePlugin.getDefault().getPreferenceStore();
            ColorManager colorManager = new ColorManager(preferenceStore);

            // expressionFilterText = new Text(scrolledComposite, SWT.MULTI |
            // SWT.WRAP | SWT.BORDER);
            expressionFilterText = new UnnotifiableColorStyledText(getCenterComposite(), SWT.MULTI | SWT.WRAP
                    | SWT.BORDER | SWT.V_SCROLL, colorManager, LanguageManager.getCurrentLanguage().getName());
            GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
            gridData.minimumHeight = 10;
            // gridData.grabExcessVerticalSpace = true;
            gridData.heightHint = 32;
            gridData.minimumWidth = 25;
            gridData.widthHint = 50;
            expressionFilterText.setLayoutData(gridData);

            final String defaultText = DEFAULT_EXPRESSION_FILTER;
            String expressionFilter = table.getExpressionFilter().getExpression();
            if (expressionFilter != null && !"".equals(expressionFilter.trim())) {
                expressionFilterText.setText(expressionFilter);
            } else {
                expressionFilterText.setText(defaultText);
            }

            new DragNDrop(mapperManager, expressionFilterText, false, true);

            expressionFilterText.setVisible(table.isActivateExpressionFilter());
            gridData.exclude = !table.isActivateExpressionFilter();

            expressionFilterText.addFocusListener(new FocusListener() {

                public void focusGained(FocusEvent e) {
                    redrawExpressionFilter();
                    Control text = (Control) e.getSource();
                    if (defaultText.equals(ControlUtils.getText(text))) {
                        ControlUtils.setText(text, "");
                    }

                    ExpressionFilterEntry currentExpressionFilterEntry = table.getExpressionFilter();
                    // currentExpressionFilterEntry.setExpression(ControlUtils.getText(text));

                    mapperManager.getUiManager().selectLinks(DataMapTableView.this,
                            Arrays.<ITableEntry> asList(currentExpressionFilterEntry), true, false);
                    expressionFilterText.setBackground(null);
                    expressionFilterText.setForeground(null);
                    StyledTextHandler styledTextHandler = mapperManager.getUiManager().getTabFolderEditors()
                            .getStyledTextHandler();
                    styledTextHandler.setCurrentEntry(currentExpressionFilterEntry);
                    previousTextForExpressionFilter = currentExpressionFilterEntry.getExpression() == null ? "" : currentExpressionFilterEntry.getExpression(); //$NON-NLS-1$
                    styledTextHandler.getStyledText().setText(previousTextForExpressionFilter);
                    expressionFilterText.setToolTipText(null);
                    styledTextHandler.getStyledText().setEnabled(true);
                    styledTextHandler.getStyledText().setEditable(true);
                    expressionProposalStyledText = styledTextHandler.getContentProposalAdapter();
                    expressionProposalProviderForExpressionFilter.init(table, getValidZonesForExpressionFilterField(),
                            table.getExpressionFilter());
                    expressionProposalStyledText
                            .setContentProposalProvider(expressionProposalProviderForExpressionFilter);
                    mapperManager.getUiManager().selectLinks(DataMapTableView.this,
                            Arrays.<ITableEntry> asList(currentExpressionFilterEntry), true, false);
                    colorExpressionFilterFromProblems(table, false);
                }

                public void focusLost(FocusEvent e) {
                    Control text = (Control) e.getSource();
                    if ("".equals(ControlUtils.getText(text).trim())) {
                        ControlUtils.setText(text, defaultText);
                    }
                    setExpressionFilterFromStyledText(table, text);
                    checkProblemsForExpressionFilter(false, true);
                }

            });

            Listener showTooltipErrorListener = new Listener() {

                public void handleEvent(Event event) {

                    switch (event.type) {
                    case SWT.MouseMove:
                        if (table.getExpressionFilter().getProblems() != null && !expressionFilterText.isFocusControl()) {
                            String tooltip = createErrorContentForTooltip(table.getExpressionFilter().getProblems());
                            expressionFilterText.setToolTipText(tooltip);
                        } else {
                            expressionFilterText.setToolTipText(null);
                        }

                        break;
                    default:
                    }
                }

            };
            expressionFilterText.addListener(SWT.MouseMove, showTooltipErrorListener);

            if (executionLimiterForExpressionFilterSetText == null) {
                executionLimiterForExpressionFilterSetText = new ExecutionLimiter(50, true) {

                    @Override
                    public void execute(boolean isFinalExecution) {

                        if (isFinalExecution) {
                            expressionFilterText.getDisplay().syncExec(new Runnable() {

                                public void run() {
                                    if (expressionFilterText.isDisposed()) {
                                        return;
                                    }
                                    // to correct bug of CR when content
                                    // is multiline
                                    if (expressionFilterText.getText() != null) {
                                        expressionFilterText.setText(expressionFilterText.getText());
                                    }
                                }

                            });
                        }
                    }

                };
            }

            expressionFilterText.addControlListener(new ControlListener() {

                public void controlMoved(ControlEvent e) {
                    redrawExpressionFilter();
                }

                public void controlResized(ControlEvent e) {
                    redrawExpressionFilter();
                }

            });

            ControlListener listenerForCorrectWrapBug = new ControlListener() {

                public void controlMoved(ControlEvent e) {
                    redrawExpressionFilter();
                }

                public void controlResized(ControlEvent e) {
                    correctAsynchStyledTextWrapBug();
                }
            };

            ScrolledComposite scrolledCompositeView = null;
            if (getZone() == Zone.INPUTS) {
                scrolledCompositeView = getMapperManager().getUiManager().getScrolledCompositeViewInputs();
            } else if (getZone() == Zone.OUTPUTS) {
                scrolledCompositeView = getMapperManager().getUiManager().getScrolledCompositeViewOutputs();
            }
            scrolledCompositeView.addControlListener(listenerForCorrectWrapBug);

            SelectionListener selectionListenerToCorrectWrapBug = new SelectionListener() {

                public void widgetDefaultSelected(SelectionEvent e) {
                }

                public void widgetSelected(SelectionEvent e) {
                    redrawExpressionFilter();
                }

            };

            scrolledCompositeView.getVerticalBar().addSelectionListener(selectionListenerToCorrectWrapBug);

            expressionFilterText.getVerticalBar().addSelectionListener(selectionListenerToCorrectWrapBug);

            // expressionFilterText.addExtendedModifyListener(new
            // ExtendedModifyListener() {
            //
            // public void modifyText(ExtendedModifyEvent event) {
            // StyledTextHandler styledTextHandler =
            // mapperManager.getUiManager().getTabFolderEditors().getStyledTextHandler();
            // styledTextHandler.getStyledText().replaceTextRange(event.start,
            // event.length, event.replacedText);
            // }
            //                
            // });

            ExpressionEditorToMapperStyledTextKeyListener keyAndModifyListener = new ExpressionEditorToMapperStyledTextKeyListener(
                    expressionFilterText, mapperManager.getUiManager().getTabFolderEditors().getStyledTextHandler());
            expressionFilterText.addExtendedModifyListener(keyAndModifyListener);
            expressionFilterText.addKeyListener(keyAndModifyListener);

            // expressionFilterText.addKeyListener(new KeyListener() {
            //
            // /*
            // * (non-Javadoc)
            // *
            // * @see
            // org.eclipse.swt.events.KeyListener#keyPressed(org.eclipse.swt.events.KeyEvent)
            // */
            // public void keyPressed(KeyEvent e) {
            //
            // Control text = (Control) e.getSource();
            // setExpressionFilterFromStyledText(table, text);
            // StyledTextHandler styledTextHandler =
            // mapperManager.getUiManager().getTabFolderEditors()
            // .getStyledTextHandler();
            // styledTextHandler.getStyledText().setText(expressionFilterText.getText());
            //
            // }
            //
            // public void keyReleased(KeyEvent e) {
            //
            // mapperManager.getUiManager().parseNewExpression(expressionFilterText.getText(),
            // table.getExpressionFilter(), false);
            //
            // }
            // });

        }
    }

    /**
     * DOC amaumont Comment method "onExpressionFilterTextResized".
     */
    private void redrawExpressionFilter() {
        // System.out.println("Filter text resized" +
        // System.currentTimeMillis());
        if (!expressionFilterText.isDisposed()) {
            expressionFilterText.redraw();
        }
    }

    /**
     * DOC amaumont Comment method "checkProblemsForExpressionFilter".
     */
    public void checkProblemsForExpressionFilterWithDelay() {
        if (this.executionLimiterForCheckProblemsExpressionFilter == null) {

            this.executionLimiterForCheckProblemsExpressionFilter = new ExecutionLimiter(2000, true) {

                /*
                 * (non-Javadoc)
                 * 
                 * @see org.talend.commons.utils.threading.ExecutionLimiter#execute(boolean)
                 */
                @Override
                protected void execute(boolean isFinalExecution) {
                    if (!expressionFilterText.isDisposed()) {
                        expressionFilterText.getDisplay().asyncExec(new Runnable() {

                            public void run() {
                                checkProblemsForExpressionFilter(true, true);
                            }

                        });
                    }

                }

            };
        }
        executionLimiterForCheckProblemsExpressionFilter.resetTimer();
        executionLimiterForCheckProblemsExpressionFilter.startIfExecutable(true);

    }

    public void checkProblemsForExpressionFilter(boolean forceRecompile, boolean colorAllowed) {
        if (this.getDataMapTable() instanceof AbstractInOutTable) {
            AbstractInOutTable table = (AbstractInOutTable) this.getDataMapTable();
            if (table.isActivateExpressionFilter() && !DEFAULT_EXPRESSION_FILTER.equals(expressionFilterText.getText())) {
                String nextText = expressionFilterText.getText();
                if (nextText != null && previousTextForExpressionFilter != null
                        && !nextText.trim().equals(previousTextForExpressionFilter.trim()) || forceRecompile) {
                    mapperManager.getProblemsManager().checkProblemsForTableEntry(table.getExpressionFilter(), true);
                } else {
                    mapperManager.getProblemsManager().checkProblemsForTableEntry(table.getExpressionFilter(), false);
                }
            } else {
                table.getExpressionFilter().setProblems(null);
            }
            colorExpressionFilterFromProblems(table, colorAllowed);

        }

    }

    /**
     * DOC amaumont Comment method "colorExpressionFilterFromProblems".
     * 
     * @param table
     * @param colorAllowed TODO
     */
    private void colorExpressionFilterFromProblems(AbstractInOutTable table, boolean colorAllowed) {
        List<Problem> problems = table.getExpressionFilter().getProblems();
        if (problems != null && colorAllowed) {
            expressionFilterText.setColoring(false);
            expressionFilterText.setBackground(ColorProviderMapper
                    .getColor(ColorInfo.COLOR_BACKGROUND_ERROR_EXPRESSION_CELL));
            expressionFilterText.setForeground(ColorProviderMapper
                    .getColor(ColorInfo.COLOR_FOREGROUND_ERROR_EXPRESSION_CELL));
        } else {
            expressionFilterText.setColoring(true);
            expressionFilterText.setBackground(ColorProviderMapper
                    .getColor(ColorInfo.COLOR_BACKGROUND_VALID_EXPRESSION_CELL));
            expressionFilterText.setForeground(ColorProviderMapper
                    .getColor(ColorInfo.COLOR_FOREGROUND_VALID_EXPRESSION_CELL));
        }
    }

    /**
     * DOC amaumont Comment method "registerProposalForExpressionFilter".
     */
    public void configureExpressionFilter() {
        if (mapperManager.isAdvancedMap() && getDataMapTable() instanceof AbstractInOutTable) {
            final AbstractInOutTable table = (AbstractInOutTable) getDataMapTable();
            if (this.expressionProposalProviderForExpressionFilter == null) {
                this.expressionProposalProviderForExpressionFilter = createExpressionProposalProvider();
            }
            expressionProposalProviderForExpressionFilter.init(table, getValidZonesForExpressionFilterField(), table
                    .getExpressionFilter());
            table.getExpressionFilter().setName(EXPRESSION_FILTER_ENTRY);
            this.proposalForExpressionFilterText = ProposalUtils.getCommonProposal(expressionFilterText,
                    expressionProposalProviderForExpressionFilter);
            this.proposalForExpressionFilterText.addContentProposalListener(new IContentProposalListener() {

                public void proposalAccepted(IContentProposal proposal) {
                    new AsynchronousThreading(50, false, expressionFilterText.getDisplay(), new Runnable() {

                        /*
                         * (non-Javadoc)
                         * 
                         * @see java.lang.Runnable#run()
                         */
                        public void run() {
                            if (!expressionFilterText.isDisposed()) {
                                mapperManager.getUiManager().parseNewExpression(expressionFilterText.getText(),
                                        table.getExpressionFilter(), false);
                            }
                        }

                    }).start();
                }

            });
            checkProblemsForExpressionFilter(false, true);
        }
    }

    /**
     * DOC amaumont Comment method "getValidZonesForExpressionFilterField".
     * 
     * @return
     */
    protected abstract Zone[] getValidZonesForExpressionFilterField();

    /**
     * DOC amaumont Comment method "createErrorContentForTooltip".
     * 
     * @param problems
     * @return
     */
    private String createErrorContentForTooltip(List<Problem> problems) {
        String toolTip;
        toolTip = ""; //$NON-NLS-1$
        for (Problem problem : problems) {
            String description = problem.getDescription().replaceAll("[\r\n\t]", ""); //$NON-NLS-1$ //$NON-NLS-2$
            toolTip += description + "\n"; //$NON-NLS-1$
        }
        return toolTip;
    }

    /**
     * This method must be called when all widgets has been created.
     */
    public void loaded() {

    }

    /**
     * Getter for expressionFilterText.
     * 
     * @return the expressionFilterText
     */
    public UnnotifiableColorStyledText getExpressionFilterText() {
        return this.expressionFilterText;
    }

    /**
     * DOC amaumont Comment method "correctAsynchStyledTextWrapBug".
     */
    private void correctAsynchStyledTextWrapBug() {
        new AsynchronousThreading(100, false, expressionFilterText.getDisplay(), new Runnable() {

            /*
             * (non-Javadoc)
             * 
             * @see java.lang.Runnable#run()
             */
            public void run() {
                // System.out.println("scrolledCompositeView.addControlListener(new
                // ControlListener()"
                // + System.currentTimeMillis());
                redrawExpressionFilter();
            }

        }).start();
    }

    /**
     * DOC amaumont Comment method "setExpressionFilterFromStyledText".
     * 
     * @param table
     * @param text
     */
    private void setExpressionFilterFromStyledText(final AbstractInOutTable table, Control text) {
        String currentContent = ControlUtils.getText(text);
        if (DEFAULT_EXPRESSION_FILTER.equals(currentContent)) {
            table.getExpressionFilter().setExpression(null);
        } else {
            table.getExpressionFilter().setExpression(currentContent);
        }
    }

    /**
     * DOC amaumont Comment method "updateViewAfterChangingFilterCheck".
     * 
     * @param activateFilterCheck
     */
    protected void updateViewAfterChangingFilterCheck() {
        final AbstractInOutTable table = (AbstractInOutTable) getDataMapTable();

        GridData gridData = (GridData) expressionFilterText.getLayoutData();

        if (activateFilterCheck.getSelection()) {
            expressionFilterText.setVisible(true);
            gridData.exclude = false;
            table.setActivateExpressionFilter(true);
            mapperManager.getUiManager().parseExpression(expressionFilterText.getText(), table.getExpressionFilter(),
                    false, false, false);
        } else {
            expressionFilterText.setVisible(false);
            gridData.exclude = true;
            table.setActivateExpressionFilter(false);
            mapperManager.removeTableEntry(table.getExpressionFilter());
        }
        // updateGridDataHeightForTableConstraints();
        DataMapTableView.this.changeSize(DataMapTableView.this.getPreferredSize(false, true, false), true, true);
        DataMapTableView.this.layout();

        mapperManager.getUiManager().refreshBackground(true, false);

        if (expressionFilterText.isVisible()) {
            new AsynchronousThreading(50, false, mapperManager.getUiManager().getDisplay(), new Runnable() {

                public void run() {
                    checkProblemsForExpressionFilter(expressionFilterText.isFocusControl(), false);
                }

            }).start();

            expressionFilterText.setFocus();
            if (DataMapTableView.this.getZone() == Zone.INPUTS) {
                ScrolledComposite scrolledZoneView = mapperManager.getUiManager().getScrolledCompositeViewInputs();
                Point point = expressionFilterText.getDisplay().map(expressionFilterText,
                        mapperManager.getUiManager().getTablesZoneViewInputs(), new Point(0, 0));
                mapperManager.getUiManager().setPositionOfVerticalScrollBarZone(scrolledZoneView, point.y - 20);
            }
        }

        correctAsynchStyledTextWrapBug();
    }

    /**
     * 
     * DOC amaumont InputDataMapTableView class global comment. Detailled comment <br/>
     * 
     */
    class ExpressionEditorToMapperStyledTextKeyListener implements ExtendedModifyListener, KeyListener {

        private final Control textWidget;

        private final StyledTextHandler textTarget;

        private boolean modifyListenerAllowed;

        /**
         * DOC amaumont TextKeyListener constructor comment.
         */
        public ExpressionEditorToMapperStyledTextKeyListener(StyledText textWidgetSrc, StyledTextHandler textTarget) {
            super();
            this.textWidget = textWidgetSrc;
            this.textTarget = textTarget;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.swt.events.KeyListener#keyPressed(org.eclipse.swt.events.KeyEvent)
         */
        public void keyPressed(KeyEvent e) {
            // System.out.println("e.character=" + e.character);
            // System.out.println("keyCode=" + e.keyCode);

            // boolean ctrl = (e.stateMask & SWT.CTRL) != 0;
            // boolean altgr = (e.stateMask & SWT.ALT) != 0;
            // if (e.character == '\0' || ctrl && !altgr) {
            // modifyListenerAllowed = true;
            // highlightLineAndSetSelectionOfStyledTextFromTextControl(textWidget);
            // } else {
            // modifyListenerAllowed = false;
            // UnnotifiableColorStyledText mapperColorStyledText =
            // (UnnotifiableColorStyledText) textTarget
            // .getStyledText();
            // Point selection = ControlUtils.getSelection(textWidget);
            // if (e.character == '\r' || e.character == '\u001b') {
            // textTarget.setTextWithoutNotifyListeners(ControlUtils.getText(textWidget));
            // highlightLineAndSetSelectionOfStyledTextFromTextControl(textWidget);
            // } else {
            // if (e.character == SWT.BS || e.character == SWT.DEL) {
            // if (selection.x == selection.y) {
            //
            // if (e.character == SWT.BS) {
            // if (selection.x + 1 < mapperColorStyledText.getText().length()
            // && mapperColorStyledText.getText().charAt(selection.x + 1) ==
            // '\n') {
            // mapperColorStyledText.replaceTextRangeWithoutNotifyListeners(selection.x,
            // 2, ""); //$NON-NLS-1$
            // } else {
            // mapperColorStyledText.replaceTextRangeWithoutNotifyListeners(selection.x,
            // 1, ""); //$NON-NLS-1$
            // }
            // } else {
            // if (selection.x < mapperColorStyledText.getText().length()) {
            // char nextChar =
            // mapperColorStyledText.getText().charAt(selection.x);
            // if (nextChar == '\r') {
            // mapperColorStyledText
            // .replaceTextRangeWithoutNotifyListeners(selection.x, 2, "");
            // //$NON-NLS-1$
            // } else {
            // mapperColorStyledText
            // .replaceTextRangeWithoutNotifyListeners(selection.x, 1, "");
            // //$NON-NLS-1$
            // }
            // }
            // }
            //
            // } else {
            // mapperColorStyledText.replaceTextRangeWithoutNotifyListeners(selection.x,
            // selection.y
            // - selection.x, ""); //$NON-NLS-1$
            // highlightLineAndSetSelectionOfStyledTextFromTextControl(textWidget);
            // }
            // } else {
            // // System.out.println("selection.x="+selection.x);
            // // System.out.println("selection.y="+selection.y);
            // //
            // System.out.println("mapperColorStyledText.getText()="+mapperColorStyledText.getText().length());
            // mapperColorStyledText.replaceTextRangeWithoutNotifyListeners(selection.x
            // - 1, selection.y
            // - selection.x, String.valueOf(e.character));
            // highlightLineAndSetSelectionOfStyledTextFromTextControl(textWidget);
            // }
            // }
            //
            // }
        }

        public void keyReleased(KeyEvent e) {
            // highlightLineOfCursorPosition();
            mapperManager.getUiManager().parseNewExpression(textTarget.getStyledText().getText(),
                    textTarget.getCurrentEntry(), false);

        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.swt.custom.ExtendedModifyListener#modifyText(org.eclipse.swt.custom.ExtendedModifyEvent)
         */
        public void modifyText(ExtendedModifyEvent event) {
            // if (modifyListenerAllowed) {
            if (DEFAULT_EXPRESSION_FILTER.equals(ControlUtils.getText(textWidget))) {
                textTarget.setTextWithoutNotifyListeners("");
            } else {
                textTarget.setTextWithoutNotifyListeners(ControlUtils.getText(textWidget));
            }
            highlightLineAndSetSelectionOfStyledTextFromTextControl(textWidget);
            // }
        }

    }

    /**
     * 
     * DOC amaumont InputDataMapTableView class global comment. Detailled comment <br/>
     * 
     */
    class TextCellEditorToMapperStyledTextKeyListener implements KeyListener {

        private final Control textWidget;

        private final StyledTextHandler textTarget;

        /**
         * DOC amaumont TextKeyListener constructor comment.
         */
        public TextCellEditorToMapperStyledTextKeyListener(Text textWidgetSrc, StyledTextHandler textTarget) {
            super();
            this.textWidget = textWidgetSrc;
            this.textTarget = textTarget;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.swt.events.KeyListener#keyPressed(org.eclipse.swt.events.KeyEvent)
         */
        public void keyPressed(KeyEvent e) {
            // System.out.println("e.character=" + e.character);
            // System.out.println("keyCode=" + e.keyCode);

            boolean ctrl = (e.stateMask & SWT.CTRL) != 0;
            boolean altgr = (e.stateMask & SWT.ALT) != 0;
            if (e.character == '\0' || ctrl && !altgr) {
                highlightLineAndSetSelectionOfStyledTextFromTextControl(textWidget);
            } else {
                UnnotifiableColorStyledText mapperColorStyledText = (UnnotifiableColorStyledText) textTarget
                        .getStyledText();
                Point selection = ControlUtils.getSelection(textWidget);
                if (e.character == '\r' || e.character == '\u001b') {
                    e.doit = false;
                    textTarget.setTextWithoutNotifyListeners(ControlUtils.getText(textWidget));
                    highlightLineAndSetSelectionOfStyledTextFromTextControl(textWidget);
                } else {
                    if (e.character == SWT.BS || e.character == SWT.DEL) {
                        if (selection.x == selection.y) {

                            if (e.character == SWT.BS) {
                                if (selection.x - 1 > 0) {
                                    char previousChar = mapperColorStyledText.getText().charAt(selection.x - 1);
                                    if (previousChar == '\n') {
                                        mapperColorStyledText.replaceTextRangeWithoutNotifyListeners(selection.x - 2,
                                                2, ""); //$NON-NLS-1$
                                    } else {
                                        mapperColorStyledText.replaceTextRangeWithoutNotifyListeners(selection.x - 1,
                                                1, ""); //$NON-NLS-1$
                                    }
                                }
                            } else {
                                if (selection.x < mapperColorStyledText.getText().length()) {
                                    char nextChar = mapperColorStyledText.getText().charAt(selection.x);
                                    if (nextChar == '\r') {
                                        mapperColorStyledText
                                                .replaceTextRangeWithoutNotifyListeners(selection.x, 2, ""); //$NON-NLS-1$
                                    } else {
                                        mapperColorStyledText
                                                .replaceTextRangeWithoutNotifyListeners(selection.x, 1, ""); //$NON-NLS-1$
                                    }
                                }
                            }

                        } else {
                            mapperColorStyledText.replaceTextRangeWithoutNotifyListeners(selection.x, selection.y
                                    - selection.x, ""); //$NON-NLS-1$
                            highlightLineAndSetSelectionOfStyledTextFromTextControl(textWidget);
                        }
                    } else {
                        // System.out.println("selection.x="+selection.x);
                        // System.out.println("selection.y="+selection.y);
                        // System.out.println("mapperColorStyledText.getText()="+mapperColorStyledText.getText().length());
                        mapperColorStyledText.replaceTextRangeWithoutNotifyListeners(selection.x, selection.y
                                - selection.x, String.valueOf(e.character));
                        highlightLineAndSetSelectionOfStyledTextFromTextControl(textWidget);
                    }
                }

            }
        }

        public void keyReleased(KeyEvent e) {
            // highlightLineOfCursorPosition();
        }

    }

    /**
     * Getter for activateFilterCheck.
     * 
     * @return the activateFilterCheck
     */
    protected ToolItem getActivateFilterCheck() {
        return this.activateFilterCheck;
    }

    /**
     * Getter for previousStateCheckFilter.
     * 
     * @return the previousStateCheckFilter
     */
    protected boolean isPreviousStateCheckFilter() {
        return this.previousStateCheckFilter;
    }

    /**
     * Sets the previousStateCheckFilter.
     * 
     * @param previousStateCheckFilter the previousStateCheckFilter to set
     */
    protected void setPreviousStateCheckFilter(boolean previousStateCheckFilter) {
        this.previousStateCheckFilter = previousStateCheckFilter;
    }

}
