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
package org.talend.designer.mapper.ui.visualmap.table;

import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.fieldassist.IContentProposalProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellEditorListener;
import org.eclipse.jface.viewers.IOpenListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
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
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.talend.commons.ui.swt.proposal.ContentProposalAdapterExtended;
import org.talend.commons.ui.swt.proposal.TextCellEditorWithProposal;
import org.talend.commons.ui.swt.tableviewer.CellEditorValueAdapter;
import org.talend.commons.ui.swt.tableviewer.DefaultTableLabelProvider;
import org.talend.commons.ui.swt.tableviewer.ModifiedObjectInfo;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreatorColumn;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator.LAYOUT_MODE;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator.LINE_SELECTION;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator.SHOW_SELECTION;
import org.talend.commons.ui.ws.WindowSystem;
import org.talend.commons.utils.threading.ExecutionLimiter;
import org.talend.core.ui.ImageProvider.EImage;
import org.talend.core.ui.proposal.ProcessProposalProvider;
import org.talend.designer.mapper.MapperMain;
import org.talend.designer.mapper.managers.MapperManager;
import org.talend.designer.mapper.managers.UIManager;
import org.talend.designer.mapper.model.table.AbstractDataMapTable;
import org.talend.designer.mapper.model.table.AbstractInOutTable;
import org.talend.designer.mapper.model.table.OutputTable;
import org.talend.designer.mapper.model.table.VarsTable;
import org.talend.designer.mapper.model.tableentry.AbstractInOutTableEntry;
import org.talend.designer.mapper.model.tableentry.ConstraintTableEntry;
import org.talend.designer.mapper.model.tableentry.ITableEntry;
import org.talend.designer.mapper.ui.dnd.DragNDrop;
import org.talend.designer.mapper.ui.event.MousePositionAnalyser;
import org.talend.designer.mapper.ui.event.ResizeHelper;
import org.talend.designer.mapper.ui.event.ResizeHelper.RESIZE_MODE;
import org.talend.designer.mapper.ui.font.FontInfo;
import org.talend.designer.mapper.ui.font.FontProviderMapper;
import org.talend.designer.mapper.ui.image.ImageInfo;
import org.talend.designer.mapper.ui.image.ImageProviderMapper;
import org.talend.designer.mapper.ui.proposal.expression.ExpressionProposalProvider;
import org.talend.designer.mapper.ui.tabs.MapperColorStyledText;
import org.talend.designer.mapper.ui.tabs.StyledTextHandler;
import org.talend.designer.mapper.ui.visualmap.zone.Zone;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public abstract class DataMapTableView extends Composite {

    private Table table;

    public static final int WIDTH_BUTTON = 20;

    public static final int HEIGHT_BUTTON = 20;

    protected final DataMapTableView dataMapTableView;

    private final ResizeHelper resizeHelper = new ResizeHelper();

    protected MapperManager mapperManager;

    protected TableViewerCreator tableViewerCreatorForColumns;

    private AbstractDataMapTable abstractDataMapTable;

    private Composite headerComposite;

    private Label nameLabel;

    private ToolItem minimizeButton;

    protected int heightForRestore;

    protected Layout parentLayout;

    protected TableViewerCreator tableViewerCreatorForConstraints;

    protected Table tableForConstraints;

    private boolean executeSelectionEvent = true;

    private GridData tableForConstraintsGridData;

    private ToolBar toolBarActions;

    private ExpressionProposalProvider expressionProposalProvider;

    protected Point expressionEditorTextSelectionBeforeFocusLost;

    private Composite centerComposite;

    private Text columnExpressionTextEditor;

    private Text constraintExpressionTextEditor;

    protected ToolItem removeEntryItem;

    protected boolean dragDetected;

    private Cursor currentCursor;

    private static Image imageKey;

    private static Image imageEmpty;

    private static int constraintCounter = 0;

    private static final int MINIMUM_HEIGHT = 24;

    protected static final int TIME_BEFORE_NEW_REFRESH_BACKGROUND = 150;

    protected static final int OFFSET_HEIGHT_TRIGGER = 15;

    protected static final int COLUMN_EXPRESSION_SIZE_WEIGHT = 60;

    protected static final int COLUMN_NAME_SIZE_WEIGHT = 40;

    private static final int ADJUST_WIDTH_VALUE = -1;

    private static final int HEADER_HEIGHT = 22;

    public static final String ID_NAME_COLUMN = "ID_NAME_COLUMN";

    public static final String ID_EXPRESSION_COLUMN = "ID_EXPRESSION_COLUMN";

    public static final String COLUMN_NAME = "Column";
    
    /**
     * 
     * Call finalizeInitialization(...) after instanciate this class.
     * 
     * @param parent
     * @param style
     * @param abstractDataMapTable
     * @param mapperManager
     */
    public DataMapTableView(Composite parent, int style, AbstractDataMapTable abstractDataMapTable, MapperManager mapperManager) {
        super(parent, style);
        dataMapTableView = this;
        this.mapperManager = mapperManager;
        this.abstractDataMapTable = abstractDataMapTable;
        createComponents();
        addListeners();
        mapperManager.addTablePair(dataMapTableView, abstractDataMapTable);
    }

    private void createComponents() {
        final Display display = this.getDisplay();
        // final Color listForeground = display.getSystemColor(SWT.COLOR_WIDGET_FOREGROUND);
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
        headerGridData.heightHint = HEADER_HEIGHT;
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

        // Button check = new Button(headerComposite, SWT.CHECK);
        // check.setLayoutData(new GridData(SWT.BEGINNING));

        nameLabel = new Label(headerComposite, SWT.NONE);
        nameLabel.setFont(FontProviderMapper.getFont(FontInfo.FONT_SYSTEM_BOLD));
        nameLabel.setText(abstractDataMapTable.getName());
        nameLabel.setToolTipText(abstractDataMapTable.getName());
        nameLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER | GridData.FILL_HORIZONTAL));

        // coolBarActions = new CoolBar(headerComposite, SWT.FLAT);

        toolBarActions = new ToolBar(headerComposite, SWT.FLAT | SWT.RIGHT);

        ToolItem separator = null;

        addConstraintsActionsComponents();

        if (hasConstraintsActions() && hasEntriesActions()) {
            separator = new ToolItem(toolBarActions, SWT.SEPARATOR);
            separator.setWidth(10);
            separator.setControl(headerComposite);
        }

        addEntriesActionsComponents();

        if (hasConstraintsActions() || hasEntriesActions()) {
            separator = new ToolItem(toolBarActions, SWT.SEPARATOR);
            separator.setWidth(3);
        }

        minimizeButton = new ToolItem(toolBarActions, SWT.PUSH);

        Point sizeToolBar = toolBarActions.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_END);
        gridData.widthHint = sizeToolBar.x;
        if (hasConstraintsActions() && WindowSystem.isWIN32()) {
            // to correct invalid margin when SWT.RIGHT style set in ToolBar
            gridData.widthHint -= 34;
        }
        if (WindowSystem.isGTK()) {
            gridData.widthHint += 20;
            gridData.heightHint = 24;
        }
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

        initTableConstraints();

        tableViewerCreatorForColumns = new TableViewerCreator(centerComposite);
        tableViewerCreatorForColumns.setHeaderVisible(true);
        tableViewerCreatorForColumns.setAllColumnsResizable(true);
        tableViewerCreatorForColumns.setBorderVisible(false);
        tableViewerCreatorForColumns.setLinesVisible(true);
        tableViewerCreatorForColumns.setShowSelection(SHOW_SELECTION.FULL);
        tableViewerCreatorForColumns.setLineSelection(LINE_SELECTION.MULTI);
        tableViewerCreatorForColumns.setLayoutMode(LAYOUT_MODE.CONTINUOUS_CURRENT);
        tableViewerCreatorForColumns.setAdjustWidthValue(ADJUST_WIDTH_VALUE);
        tableViewerCreatorForColumns.maskFirstColumn(true);
        if (getDataMapTable() instanceof AbstractInOutTable) {

            if (imageKey == null) {
                imageKey = org.talend.core.ui.ImageProvider.getImage(EImage.KEY);
            }
            if (imageEmpty == null) {
                imageEmpty = org.talend.core.ui.ImageProvider.getImage(EImage.EMPTY16);
            }
            tableViewerCreatorForColumns.setLabelProvider(new DefaultTableLabelProvider(tableViewerCreatorForColumns) {

                @Override
                public Image getColumnImage(Object element, int columnIndex) {
                    AbstractInOutTableEntry entry = (AbstractInOutTableEntry) element;
                    TableViewerCreatorColumn column = (TableViewerCreatorColumn) tableViewerCreatorForColumns.getColumns().get(columnIndex);
                    if (column.getId().equals(ID_NAME_COLUMN)) {
                        if (entry.getMetadataColumn().isKey()) {
                            return imageKey;
                        } else {
                            return imageEmpty;
                        }
                    }
                    return null;
                }

            });
        }

        table = tableViewerCreatorForColumns.createTable();

        GridData tableEntriesGridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        // GridData tableEntriesGridData = new GridData(GridData.FILL_HORIZONTAL);
        tableEntriesGridData.grabExcessVerticalSpace = true;
        // tableEntriesGridData.heightHint = 50;
        // tableEntriesGridData.widthHint = 15;
        // tableEntriesGridData.heightHint = 20;
        // tableEntriesGridData. = 20;
        tableEntriesGridData.minimumHeight = table.getHeaderHeight() + table.getItemHeight();
        table.setLayoutData(tableEntriesGridData);

        final TableViewer tableViewerForEntries = tableViewerCreatorForColumns.getTableViewer();

        tableViewerForEntries.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                if (!dragDetected) { // useful when drag finished
                    onSelectedEntries(event.getSelection(), tableViewerForEntries.getTable().getSelectionIndices());
                }
                dragDetected = false;
            }

        });

        tableViewerCreatorForColumns.getTable().addListener(SWT.DragDetect, new Listener() {

            public void handleEvent(Event event) {
                dragDetected = true;
                onSelectedEntries(tableViewerForEntries.getSelection(), tableViewerForEntries.getTable().getSelectionIndices());
            }

        });

        // table.addListener(SWT.DragDetect, listener)

        new DragNDrop(mapperManager, table, true, true);

        initColumns();

        tableViewerCreatorForColumns.init(abstractDataMapTable.getColumnEntries());

        Composite footerComposite = new Composite(this, SWT.NONE);
        GridData footerGridData = new GridData(10, 2);
        // footerGridData.minimumHeight = 2;
        footerComposite.setLayoutData(footerGridData);

    }

    /**
     * DOC amaumont Comment method "addEntriesActionsComponents".
     */
    protected abstract boolean hasEntriesActions();

    /**
     * DOC amaumont Comment method "addEntriesActionsComponents".
     */
    protected abstract void addEntriesActionsComponents();

    /**
     * DOC amaumont Comment method "addConstraintsActionsButtons".
     */
    protected abstract void addConstraintsActionsComponents();

    /**
     * DOC amaumont Comment method "addConstraintsActionsButtons".
     */
    protected abstract boolean hasConstraintsActions();

    /**
     * DOC amaumont Comment method "initTableConstraints".
     */
    protected abstract void initTableConstraints();

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

            public void widgetSelected(SelectionEvent event) {
                uiManager.refreshBackground(true, false);
            }
        };

        table.getVerticalBar().addSelectionListener(scrollListener);

        // /////////////////////////////////////////////////////////////////

        this.addDisposeListener(new DisposeListener() {

            public void widgetDisposed(DisposeEvent e) {
                if (currentCursor != null) {
                    currentCursor.dispose();
                }
            }

        });

        Listener tableResizerListener = new Listener() {

            public void handleEvent(Event event) {

                MousePositionAnalyser mpa = new MousePositionAnalyser(dataMapTableView);
                Point eventPoint = new Point(event.x, event.y);

                boolean leftMouseButton = (event.stateMask & SWT.BUTTON1) != 0 || event.button == 1;

                switch (event.type) {
                case SWT.MouseMove:
                    if (resizeHelper.isDragging()) {
                        Point newPoint = convertToParentOrigin(dataMapTableView, eventPoint);
                        Point dragPoint = resizeHelper.getLastDragPoint();
                        Point diff = new Point(newPoint.x - dragPoint.x, newPoint.y - dragPoint.y);
                        if (mpa.isOnLeftBorder(eventPoint)) {
                            diff.x *= -1;
                        }
                        Rectangle rect = dataMapTableView.getClientArea();
                        rect.width += 2 * dataMapTableView.getBorderWidth();
                        rect.height += 2 * dataMapTableView.getBorderWidth();

                        RESIZE_MODE currentMode = resizeHelper.getCurrentMode();
                        int newWidth = (currentMode == RESIZE_MODE.HORIZONTAL || currentMode == RESIZE_MODE.BOTH) ? rect.width + diff.x * 2
                                : rect.width;
                        int newHeight = (currentMode == RESIZE_MODE.VERTICAL || currentMode == RESIZE_MODE.BOTH) ? rect.height + diff.y
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
                            // cursor = new Cursor(dataMapTableView.getDisplay(), org.eclipse.swt.SWT.CURSOR_SIZESW);
                            // resizeHelper.setCurrentMode(RESIZE_MODE.BOTH);
                        } else if (mpa.isOnRightBottomCorner(eventPoint)) {
                            // cursor = new Cursor(dataMapTableView.getDisplay(), org.eclipse.swt.SWT.CURSOR_SIZESE);
                            // resizeHelper.setCurrentMode(RESIZE_MODE.BOTH);
                        } else if (mpa.isOnLeftBorder(eventPoint)) {
                            // cursor = new Cursor(dataMapTableView.getDisplay(), org.eclipse.swt.SWT.CURSOR_SIZEE);
                            // resizeHelper.setCurrentMode(RESIZE_MODE.HORIZONTAL);
                        } else if (mpa.isOnRightBorder(eventPoint)) {
                            // cursor = new Cursor(dataMapTableView.getDisplay(), org.eclipse.swt.SWT.CURSOR_SIZEW);
                            // resizeHelper.setCurrentMode(RESIZE_MODE.HORIZONTAL);
                        } else if (mpa.isOnBottomBorder(eventPoint)) {
                            cursor = new Cursor(dataMapTableView.getDisplay(), org.eclipse.swt.SWT.CURSOR_SIZES);
                            resizeHelper.setCurrentMode(RESIZE_MODE.VERTICAL);
                        }
                        if (cursor != null) {
                            dataMapTableView.setCursor(cursor);
                        } else {
                            setDefaultCursor();
                            resizeHelper.setCurrentMode(RESIZE_MODE.NONE);
                        }
                    }

                    break;
                case SWT.MouseDown:
                    if (leftMouseButton) {
                        if (mpa.isOnLeftBorder(eventPoint) || mpa.isOnRightBorder(eventPoint) || mpa.isOnBottomBorder(eventPoint)) {
                            resizeHelper.startDrag(convertToParentOrigin(dataMapTableView, new Point(event.x, event.y)));
                        } else {
                            setDefaultCursor();
                        }
                        parentLayout = dataMapTableView.getParent().getLayout();
                        dataMapTableView.getParent().setLayout(null);
                    }
                    break;
                case SWT.MouseUp:
                    if (leftMouseButton) {
                        resizeHelper.stopDrag();
                        // gridData = (GridData) dataMapTableView.getLayoutData();
                        // gridData.exclude = false;
                        dataMapTableView.getParent().setLayout(parentLayout);
                        dataMapTableView.getParent().layout();
                        uiManager.resizeTablesZoneViewAtComputedSize(getZone());
                        uiManager.refreshBackground(true, false);
                    }
                    break;
                case SWT.MouseExit:
                    setDefaultCursor();
                    break;
                }

            }

        };

        this.addListener(SWT.MouseMove, tableResizerListener);
        this.addListener(SWT.MouseDown, tableResizerListener);
        this.addListener(SWT.MouseUp, tableResizerListener);
        // /////////////////////////////////////////////////////////////////

        // /////////////////////////////////////////////////////////////////
        Listener onSelectDataMapTableViewListener = new Listener() {

            public void handleEvent(Event event) {
                switch (event.type) {
                case SWT.MouseDown:
                    onSelectDataMapTableView();
                    break;
                }

            }
        };

        this.addListener(SWT.MouseDown, onSelectDataMapTableViewListener);
        headerComposite.addListener(SWT.MouseDown, onSelectDataMapTableViewListener);
        table.addListener(SWT.MouseDown, onSelectDataMapTableViewListener);
        if (tableForConstraints != null) {
            tableForConstraints.addListener(SWT.MouseDown, onSelectDataMapTableViewListener);
        }
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

    }

    /**
     * DOC amaumont Comment method "showTableConstraints".
     */
    protected void showTableConstraints(boolean visible) {

        if (visible) {
            tableForConstraintsGridData.exclude = false;
            tableForConstraints.setVisible(true);
        } else {
            tableForConstraintsGridData.exclude = true;
            tableForConstraints.setVisible(false);
        }
        tableViewerCreatorForConstraints.getTableViewer().refresh();
        resizeAtExpandedSize();
    }

    /**
     * DOC amaumont Comment method "onSelectDataMapTableView".
     */
    protected void onSelectDataMapTableView() {
        mapperManager.getUiManager().selectDataMapTableView(this);
    }

    /**
     * DOC amaumont Comment method "initClumns".
     */
    public abstract void initColumns();

    public TableViewerCreator getTableViewerCreatorForColumns() {
        return this.tableViewerCreatorForColumns;
    }

    public TableViewerCreator getTableViewerCreatorForConstraints() {
        return this.tableViewerCreatorForConstraints;
    }

    public Point convertToParentOrigin(Composite child, Point point) {
        Rectangle bounds = child.getBounds();
        return new Point(bounds.x + point.x, bounds.y + point.y);
    }

    private void setDefaultCursor() {
        Cursor cursor = new Cursor(dataMapTableView.getDisplay(), 0);
        dataMapTableView.setCursor(cursor);
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
        FormData formData = (FormData) dataMapTableView.getLayoutData();
        formData.width = newSize.x;
        formData.height = newSize.y;
        dataMapTableView.setSize(newSize);
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

    protected void createTableConstraints() {
        tableViewerCreatorForConstraints = new TableViewerCreator(centerComposite);
        tableViewerCreatorForConstraints.setHeaderVisible(true);
        tableViewerCreatorForConstraints.setAllColumnsResizable(true);
        tableViewerCreatorForConstraints.setBorderVisible(false);
        tableViewerCreatorForConstraints.setLinesVisible(true);
        tableViewerCreatorForConstraints.setShowSelection(SHOW_SELECTION.FULL);
        tableViewerCreatorForConstraints.setLineSelection(LINE_SELECTION.MULTI);
        tableViewerCreatorForConstraints.setLayoutMode(LAYOUT_MODE.CONTINUOUS_CURRENT);
        tableViewerCreatorForConstraints.setAdjustWidthValue(ADJUST_WIDTH_VALUE);
        tableViewerCreatorForConstraints.maskFirstColumn(true);

        tableForConstraints = tableViewerCreatorForConstraints.createTable();
        // tableForConstraintsGridData = new GridData(SWT.NONE, SWT.FILL, true, false);
        tableForConstraintsGridData = new GridData(GridData.FILL_HORIZONTAL);
        // tableForConstraintsGridData.grabExcessVerticalSpace = true;
        // tableForConstraintsGridData.widthHint = 15;
        // tableForConstraintsGridData.minimumHeight = tableForConstraints.getHeaderHeight() +
        // tableForConstraints.getItemHeight();
        // tableForConstraintsGridData.verticalAlignment = GridData.VERTICAL_ALIGN_FILL;
        // tableForConstraintsGridData.verticalIndent = 10;
        tableForConstraints.setLayoutData(tableForConstraintsGridData);

        boolean tableConstraintsVisible = false;
        if (abstractDataMapTable instanceof OutputTable) {
            tableConstraintsVisible = ((OutputTable) abstractDataMapTable).getConstraintEntries().size() > 0;
        }

        tableForConstraintsGridData.exclude = !tableConstraintsVisible;
        tableForConstraints.setVisible(tableConstraintsVisible);

        new DragNDrop(mapperManager, tableForConstraints, false, true);

        final TableViewer tableViewer = tableViewerCreatorForConstraints.getTableViewer();
        tableViewer.addOpenListener(new IOpenListener() {

            public void open(OpenEvent event) {
                // System.out.println("OpenEvent");

            }

        });

        tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                UIManager uiManager = mapperManager.getUiManager();
                uiManager.processSelectedMetadataTableEntries(dataMapTableView, uiManager.extractSelectedTableEntries(tableViewer
                        .getSelection()), true);
            }

        });

    }

    public void onSelectedEntries(ISelection selection, int[] selectionIndices) {
        if (executeSelectionEvent) {
            UIManager uiManager = mapperManager.getUiManager();
            uiManager.processSelectedMetadataTableEntries(dataMapTableView, uiManager.extractSelectedTableEntries(selection), false);
            uiManager.selectLinkedMetadataEditorEntries(this, selectionIndices);
        }
    }

    public AbstractDataMapTable getDataMapTable() {
        return this.abstractDataMapTable;
    }

    /**
     * DOC amaumont Comment method "setTableSelection".
     * 
     * @param selectionIndices
     */
    public void setTableSelection(int[] selectionIndices) {
        this.executeSelectionEvent = false;
        this.tableViewerCreatorForColumns.getTable().setSelection(selectionIndices);
        this.executeSelectionEvent = true;

    }

    protected void createConstraintActionButtons() {

        ToolItem addConstraintButton = new ToolItem(toolBarActions, SWT.PUSH);
        addConstraintButton.setToolTipText("Add constraint");
        addConstraintButton.setImage(ImageProviderMapper.getImage(ImageInfo.ADD_CONSTRAINT_ICON));

        ToolItem removeConstraintButton = new ToolItem(toolBarActions, SWT.PUSH);
        removeConstraintButton.setImage(ImageProviderMapper.getImage(ImageInfo.REMOVE_CONSTRAINT_ICON));
        removeConstraintButton.setToolTipText("Remove selected constraints");

        final ToolItem rejectConstraintCheck = new ToolItem(toolBarActions, SWT.CHECK);
        rejectConstraintCheck.setToolTipText("Active/unactive reject");
        boolean isReject = ((OutputTable) abstractDataMapTable).isReject();
        Image image = ImageProviderMapper.getImage(isReject ? ImageInfo.CHECKED_ICON : ImageInfo.UNCHECKED_ICON);
        if (WindowSystem.isGTK()) {
            rejectConstraintCheck.setImage(image);
            rejectConstraintCheck.setHotImage(image);
        } else {
            rejectConstraintCheck.setImage(ImageProviderMapper.getImage(ImageInfo.UNCHECKED_ICON));
            rejectConstraintCheck.setHotImage(image);
        }
        rejectConstraintCheck.setSelection(isReject);
        rejectConstraintCheck.setText("Reject");

        // /////////////////////////////////////////////////////////////////
        if (addConstraintButton != null) {

            addConstraintButton.addSelectionListener(new SelectionListener() {

                public void widgetDefaultSelected(SelectionEvent e) {
                }

                public void widgetSelected(SelectionEvent e) {

                    Table tableConstraints = tableViewerCreatorForConstraints.getTable();
                    int index = tableConstraints.getItemCount();
                    int[] selection = tableViewerCreatorForConstraints.getTable().getSelectionIndices();
                    if (selection.length > 0) {
                        index = selection[selection.length - 1] + 1;
                    }
                    mapperManager.addNewConstraintEntry(dataMapTableView, "newConstraint" + ++constraintCounter, index);
                    updateGridDataHeightForTableConstraints();
                    dataMapTableView.changeSize(dataMapTableView.getPreferredSize(false, true, true), true, true);
                    tableViewerCreatorForConstraints.getTableViewer().refresh();
                    mapperManager.getUiManager().refreshBackground(true, false);
                    showTableConstraints(true);
                    changeMinimizeState(false);
                }

            });
        }
        // /////////////////////////////////////////////////////////////////

        // /////////////////////////////////////////////////////////////////
        if (removeConstraintButton != null) {

            removeConstraintButton.addSelectionListener(new SelectionListener() {

                public void widgetDefaultSelected(SelectionEvent e) {
                }

                public void widgetSelected(SelectionEvent e) {

                    StructuredSelection selection = (StructuredSelection) tableViewerCreatorForConstraints.getTableViewer().getSelection();
                    List list = tableViewerCreatorForConstraints.getInputList();
                    int sizeBeforeRemoving = list.size();
                    Iterator iterator = selection.iterator();
                    for (; iterator.hasNext();) {
                        mapperManager.removeTableEntry((ITableEntry) iterator.next());
                    }
                    if (sizeBeforeRemoving != list.size()) {
                        tableViewerCreatorForConstraints.getTableViewer().refresh();
                        updateGridDataHeightForTableConstraints();
                        if (list != null && list.size() == 0) {
                            showTableConstraints(false);
                        } else {
                            showTableConstraints(true);
                        }
                    }
                }

            });
        }

        // /////////////////////////////////////////////////////////////////

        // /////////////////////////////////////////////////////////////////
        if (rejectConstraintCheck != null) {

            rejectConstraintCheck.addSelectionListener(new SelectionListener() {

                public void widgetDefaultSelected(SelectionEvent e) {
                }

                public void widgetSelected(SelectionEvent e) {
                    Image image = null;
                    if (rejectConstraintCheck.getSelection()) {
                        ((OutputTable) abstractDataMapTable).setReject(true);
                        image = ImageProviderMapper.getImage(ImageInfo.CHECKED_ICON);
                    } else {
                        ((OutputTable) abstractDataMapTable).setReject(false);
                        image = ImageProviderMapper.getImage(ImageInfo.UNCHECKED_ICON);
                    }
                    if (WindowSystem.isGTK()) {
                        rejectConstraintCheck.setImage(image);
                        rejectConstraintCheck.setHotImage(image);
                    } else {
                        rejectConstraintCheck.setHotImage(image);
                    }
                }

            });

        }

        // /////////////////////////////////////////////////////////////////

    }

    protected void createEntriesActionButtons() {

        ToolItem addEntryItem = new ToolItem(toolBarActions, SWT.PUSH);

        addEntryItem.setToolTipText("Add variable");
        addEntryItem.setImage(org.talend.core.ui.ImageProvider.getImage(org.talend.core.ui.ImageProvider.getImageDesc(EImage.ADD_ICON)));

        removeEntryItem = new ToolItem(toolBarActions, SWT.PUSH);
        removeEntryItem.setEnabled(false);
        removeEntryItem.setImage(org.talend.core.ui.ImageProvider
                .getImage(org.talend.core.ui.ImageProvider.getImageDesc(EImage.MINUS_ICON)));
        removeEntryItem.setToolTipText("Remove selected variable(s)");

        // /////////////////////////////////////////////////////////////////
        if (addEntryItem != null) {

            addEntryItem.addSelectionListener(new SelectionListener() {

                public void widgetDefaultSelected(SelectionEvent e) {
                }

                public void widgetSelected(SelectionEvent e) {
                    int[] indices = table.getSelectionIndices();
                    int indexInsert = table.getItemCount();
                    if (indices.length > 0) {
                        indexInsert = indices[indices.length - 1] + 1;
                    }
                    AbstractDataMapTable dataMapTable = dataMapTableView.getDataMapTable();
                    String varName = null;
                    if (dataMapTable instanceof VarsTable) {
                        varName = ((VarsTable) dataMapTable).findUniqueColumnName("var");
                    } else {
                        throw new UnsupportedOperationException("Can't create new column, case not found");
                    }
                    mapperManager.addNewColumnEntry(dataMapTableView, varName, indexInsert);
                    dataMapTableView.changeSize(dataMapTableView.getPreferredSize(true, true, false), true, true);
                    changeMinimizeState(false);
                    tableViewerCreatorForColumns.getTableViewer().refresh();
                    mapperManager.getUiManager().refreshBackground(true, false);
                    table.setSelection(indexInsert);
                    removeEntryItem.setEnabled(true);
                }

            });
        }
        // /////////////////////////////////////////////////////////////////

        // /////////////////////////////////////////////////////////////////
        if (removeEntryItem != null) {

            removeEntryItem.addSelectionListener(new SelectionListener() {

                public void widgetDefaultSelected(SelectionEvent e) {
                }

                public void widgetSelected(SelectionEvent e) {
                    TableItem[] tableItems = tableViewerCreatorForColumns.getTable().getSelection();
                    for (int i = 0; i < tableItems.length; i++) {
                        TableItem item = tableItems[i];
                        mapperManager.removeTableEntry((ITableEntry) item.getData());
                    }
                    if (tableItems.length > 0) {
                        tableViewerCreatorForColumns.getTableViewer().refresh();
                        mapperManager.getUiManager().refreshBackground(true, false);
                        dataMapTableView.resizeAtExpandedSize();
                    }
                    removeEntryItem.setEnabled(false);
                }

            });
        }

        // /////////////////////////////////////////////////////////////////

    }

    public void minimizeTable(boolean minimize) {
        Point size = dataMapTableView.getSize();
        if (minimize) {
            // System.out.println("store height before minimize"+size.y);
            this.heightForRestore = size.y - 4;
            changeSize(new Point(size.x, MINIMUM_HEIGHT), true, true);
            changeMinimizeState(true);
        } else {
            if (heightForRestore != MINIMUM_HEIGHT && heightForRestore > 0) {
                size.y = heightForRestore;
            } else {
                size = dataMapTableView.getPreferredSize(false, true, false);
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
            minimizeButton.setToolTipText("Restore");
        } else {
            abstractDataMapTable.setMinimized(false);
            minimizeButton.setSelection(false);
            minimizeButton.setImage(ImageProviderMapper.getImage(ImageInfo.MINIMIZE_ICON));
            minimizeButton.setToolTipText("Minimize");
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
    public Point getPreferredSize(boolean newTableEntryWillBeAdded, boolean expandedSize, boolean newConstraintEntryWillBeAdded) {
        int heightOffset = 0;
        int tableEntryItemHeight = tableViewerCreatorForColumns.getTable().getItemHeight();
        heightOffset += newTableEntryWillBeAdded ? tableEntryItemHeight : 0;
        heightOffset += newConstraintEntryWillBeAdded ? tableViewerCreatorForConstraints.getTable().getItemHeight() : 0;

        int newHeight = this.computeSize(SWT.DEFAULT, SWT.DEFAULT).y - tableEntryItemHeight + heightOffset;
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
            attachCellExpressionToStyledTextEditor(tableViewerCreatorForColumns, columnExpressionTextEditor, styledTextHandler);
        }
        if (constraintExpressionTextEditor != null) {
            attachCellExpressionToStyledTextEditor(tableViewerCreatorForConstraints, constraintExpressionTextEditor, styledTextHandler);
        }
    }

    /**
     * DOC amaumont Comment method "attachCellExpressionToStyledTextEditor".
     * 
     * @param tableViewerCreator TODO
     * @param styledTextHandler
     * @param expressionEditorText2
     */
    protected void attachCellExpressionToStyledTextEditor(final TableViewerCreator tableViewerCreator, final Text expressionTextEditor,
            final StyledTextHandler styledTextHandler) {
        expressionTextEditor.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                ITableEntry currentModifiedEntry = (ITableEntry) tableViewerCreator.getModifiedObjectInfo().getCurrentModifiedBean();
                styledTextHandler.setCurrentEntry(currentModifiedEntry);
                Text text = (Text) e.widget;
                if (Math.abs(text.getText().length() - styledTextHandler.getStyledText().getText().length()) > 1) {
                    styledTextHandler.setTextWithoutNotifyListeners(text.getText());
                    // highlightLineAndSetSelectionOfStyledText(expressionTextEditor);
                }
            }

        });

        expressionTextEditor.addKeyListener(new KeyListener() {

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
                    highlightLineAndSetSelectionOfStyledText(expressionTextEditor);
                } else {
                    MapperColorStyledText mapperColorStyledText = (MapperColorStyledText) styledTextHandler.getStyledText();
                    Point selection = expressionTextEditor.getSelection();
                    if (e.character == '\r' || e.character == '\u001b') {
                        e.doit = false;
                        styledTextHandler.setTextWithoutNotifyListeners(expressionTextEditor.getText());
                        highlightLineAndSetSelectionOfStyledText(expressionTextEditor);
                    } else {
                        if (e.character == SWT.BS || e.character == SWT.DEL) {
                            if (selection.x == selection.y) {

                                if (e.character == SWT.BS) {
                                    if (selection.x - 1 > 0) {
                                        char previousChar = mapperColorStyledText.getText().charAt(selection.x - 1);
                                        if (previousChar == '\n') {
                                            mapperColorStyledText.replaceTextRangeWithoutNotifyListeners(selection.x - 2, 2, "");
                                        } else {
                                            mapperColorStyledText.replaceTextRangeWithoutNotifyListeners(selection.x - 1, 1, "");
                                        }
                                    }
                                } else {
                                    if (selection.x < mapperColorStyledText.getText().length()) {
                                        char nextChar = mapperColorStyledText.getText().charAt(selection.x);
                                        if (nextChar == '\r') {
                                            mapperColorStyledText.replaceTextRangeWithoutNotifyListeners(selection.x, 2, "");
                                        } else {
                                            mapperColorStyledText.replaceTextRangeWithoutNotifyListeners(selection.x, 1, "");
                                        }
                                    }
                                }

                            } else {
                                mapperColorStyledText.replaceTextRangeWithoutNotifyListeners(selection.x, selection.y - selection.x, "");
                                highlightLineAndSetSelectionOfStyledText(expressionTextEditor);
                            }
                        } else {
                            // System.out.println("selection.x="+selection.x);
                            // System.out.println("selection.y="+selection.y);
                            // System.out.println("mapperColorStyledText.getText()="+mapperColorStyledText.getText().length());
                            mapperColorStyledText.replaceTextRangeWithoutNotifyListeners(selection.x, selection.y - selection.x, String
                                    .valueOf(e.character));
                            highlightLineAndSetSelectionOfStyledText(expressionTextEditor);
                        }
                    }

                }
            }

            public void keyReleased(KeyEvent e) {
                // highlightLineOfCursorPosition();
            }

        });

        expressionTextEditor.addListener(SWT.KeyDown, new Listener() {

            public void handleEvent(Event event) {
                // highlightLineOfCursorPosition();
            }

        });

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
        final StyledTextHandler styledTextHandler = mapperManager.getUiManager().getTabFolderEditors().getStyledTextHandler();
        new Thread() {

            @Override
            public void run() {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                styledTextHandler.getStyledText().getDisplay().syncExec(new Runnable() {

                    public void run() {

                        StyledText styledText = styledTextHandler.getStyledText();
                        if (styledText.isDisposed()) {
                            return;
                        }

                        String text = expressionTextEditor.getText();
                        Point selection = expressionTextEditor.getSelection();
                        if (selection.x - 1 > 0) {
                            while (expressionTextEditor.getLineDelimiter().equals(text.charAt(selection.x - 1))) {
                                selection.x++;
                            }
                        }
                        if (selection.y - 1 > 0) {
                            while (expressionTextEditor.getLineDelimiter().equals(text.charAt(selection.y - 1))) {
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
                });
            }

        }.start();
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
    protected void initExpressionProposals(final TextCellEditorWithProposal textCellEditor, Zone[] zones,
            final TableViewerCreator tableViewerCreator, ITableEntry currentModifiedEntry) {
        Control textControl = textCellEditor.getControl();
        if (this.expressionProposalProvider == null) {
            IContentProposalProvider[] contentProposalProviders = new IContentProposalProvider[0];
            if (!MapperMain.isStandAloneMode()) {
                contentProposalProviders = new IContentProposalProvider[] { new ProcessProposalProvider(mapperManager.getConnector()
                        .getProcess()) };
            }
            this.expressionProposalProvider = new ExpressionProposalProvider(mapperManager, contentProposalProviders);

            // this.expressionProposal.getContentProposalAdapter().setProposalAcceptanceStyle(ContentProposalAdapter.PROPOSAL_INSERT);
            // this.expressionProposal.addContentProposalListener(new IContentProposalListener() {
            //
            // public void proposalAccepted(IContentProposal proposal) {
            // Object previousModifiedBean = tableViewerCreator.getModifiedObjectInfo().getPreviousModifiedBean();
            // Point selection = null;
            // if (!textControl.isFocusControl()) {
            // selection = expressionEditorTextSelectionBeforeFocusLost;
            // tableViewerCreator.getTableViewer().editElement(previousModifiedBean, 1);
            // } else {
            // selection = textControl.getSelection();
            // }
            // final String insertedContent = proposal.getContent();
            // String beforeSelection = textControl.getText(0, selection.x - 1);
            // String afterSelection = textControl.getText(selection.y, textControl.getCharCount());
            // String newText = beforeSelection + insertedContent + afterSelection;
            // textControl.setText(newText);
            // int newCursorPosition = insertedContent.length() + selection.x;
            // textControl.setSelection(new Point(newCursorPosition, newCursorPosition));
            //
            // }
            //
            // });
        }
        this.expressionProposalProvider.init(abstractDataMapTable, zones, currentModifiedEntry);
        textCellEditor.setContentProposalProvider(this.expressionProposalProvider);

        StyledTextHandler styledTextHandler = mapperManager.getUiManager().getTabFolderEditors().getStyledTextHandler();
        styledTextHandler.getStyledText().setEnabled(true);

        ContentProposalAdapterExtended expressionProposalStyledText = styledTextHandler.getContentProposalAdapter();
        expressionProposalStyledText.setContentProposalProvider(this.expressionProposalProvider);

        // System.out.println("init expression proposal:"+this.expressionProposal);
    }

    protected TextCellEditor createExpressionCellEditor(final TableViewerCreator tableViewerCreator, TableViewerCreatorColumn column,
            final Zone[] zones, boolean isConstraintExpressionCellEditor) {
        final TextCellEditorWithProposal cellEditor = new TextCellEditorWithProposal(tableViewerCreator.getTable(), SWT.MULTI | SWT.BORDER,
                column);
        final Text expressionTextEditor = (Text) cellEditor.getControl();

        if (isConstraintExpressionCellEditor) {
            constraintExpressionTextEditor = expressionTextEditor;
        } else {
            columnExpressionTextEditor = expressionTextEditor;
        }

        cellEditor.addListener(new ICellEditorListener() {

            Text text = (Text) cellEditor.getControl();

            public void applyEditorValue() {
                ModifiedObjectInfo modifiedObjectInfo = tableViewerCreator.getModifiedObjectInfo();
                mapperManager.getUiManager()
                        .processNewExpression(text.getText(), (ITableEntry) modifiedObjectInfo.getCurrentModifiedBean());
            }

            public void cancelEditor() {
                ModifiedObjectInfo modifiedObjectInfo = tableViewerCreator.getModifiedObjectInfo();
                text.setText((String) modifiedObjectInfo.getOriginalPropertyBeanValue());
                ITableEntry tableEntry = (ITableEntry) (modifiedObjectInfo.getCurrentModifiedBean() != null ? modifiedObjectInfo
                        .getCurrentModifiedBean() : modifiedObjectInfo.getPreviousModifiedBean());
                String originalExpression = (String) modifiedObjectInfo.getOriginalPropertyBeanValue();
                mapperManager.getUiManager().processNewExpression(originalExpression, tableEntry);
            }

            public void editorValueChanged(boolean oldValidState, boolean newValidState) {

                if (expressionTextEditor.isFocusControl()) {
                    ModifiedObjectInfo modifiedObjectInfo = tableViewerCreator.getModifiedObjectInfo();
                    ITableEntry tableEntry = (ITableEntry) (modifiedObjectInfo.getCurrentModifiedBean() != null ? modifiedObjectInfo
                            .getCurrentModifiedBean() : modifiedObjectInfo.getPreviousModifiedBean());
                    mapperManager.getUiManager().processNewExpression(text.getText(), tableEntry);
                    resizeTextEditor(text, tableViewerCreator);
                }
            }

        });
        expressionTextEditor.addControlListener(new ControlListener() {

            ExecutionLimiter executionLimiter = null;

            public void controlMoved(ControlEvent e) {}

            public void controlResized(ControlEvent e) {
                if (executionLimiter == null) {
                    executionLimiter = new ExecutionLimiter(100, true) {

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
                ITableEntry currentModifiedEntry = (ITableEntry) tableViewerCreator.getModifiedObjectInfo().getCurrentModifiedBean();
                initExpressionProposals(cellEditor, zones, tableViewerCreator, currentModifiedEntry);
                resizeTextEditor(expressionTextEditor, tableViewerCreator);
                StyledTextHandler styledTextHandler = mapperManager.getUiManager().getTabFolderEditors().getStyledTextHandler();
                styledTextHandler.setCurrentEntry(currentModifiedEntry);
                styledTextHandler.getStyledText().setText(
                        currentModifiedEntry.getExpression() == null ? "" : currentModifiedEntry.getExpression());
            }

            public void focusLost(FocusEvent e) {
                expressionEditorTextSelectionBeforeFocusLost = expressionTextEditor.getSelection();
            }

        });
        column.setCellEditor(cellEditor, new CellEditorValueAdapter() {

            @Override
            public Object getCellEditorTypedValue(CellEditor cellEditor, Object originalTypedValue) {
                return super.getCellEditorTypedValue(cellEditor, originalTypedValue);
            }

            @Override
            public String getColumnText(CellEditor cellEditor, Object cellEditorTypedValue) {
                return super.getColumnText(cellEditor, cellEditorTypedValue).replaceAll("[\r\n\t]+", " ... ");
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
        if ((text.contains("\n") || text.contains("\r")) && currentBounds.y + maxHeight < currentTable.getBounds().height) {
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
        tableForConstraintsGridData.heightHint = ((OutputTable) abstractDataMapTable).getConstraintEntries().size()
                * tableForConstraints.getItemHeight() + tableForConstraints.getItemHeight() / 2;
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
        tableViewerCreatorForColumns.getTable().deselectAll();
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

}
