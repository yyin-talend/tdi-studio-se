// ============================================================================
//
// Copyright (C) 2006-2010 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.mapper.ui.visualmap.table;

import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ArmEvent;
import org.eclipse.swt.events.ArmListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.talend.commons.ui.image.EImage;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.commons.ui.swt.advanced.dataeditor.commands.ExtendedTableRemoveCommand;
import org.talend.commons.ui.swt.extended.table.AbstractExtendedTableViewer;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreatorColumn;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator.LAYOUT_MODE;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator.SHOW_ROW_SELECTION;
import org.talend.commons.ui.swt.tableviewer.behavior.DefaultHeaderColumnSelectionListener;
import org.talend.commons.ui.swt.tableviewer.behavior.DefaultTableLabelProvider;
import org.talend.commons.ui.swt.tableviewer.behavior.IColumnImageProvider;
import org.talend.commons.ui.swt.tableviewer.behavior.ITableCellValueModifiedListener;
import org.talend.commons.ui.swt.tableviewer.behavior.TableCellValueModifiedEvent;
import org.talend.commons.ui.swt.tableviewer.celleditor.ExtendedTextCellEditor;
import org.talend.commons.ui.swt.tableviewer.tableeditor.ButtonPushImageTableEditorContent;
import org.talend.commons.ui.ws.WindowSystem;
import org.talend.commons.utils.data.bean.IBeanPropertyAccessors;
import org.talend.commons.utils.image.ImageUtils;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.designer.abstractmap.model.tableentry.ITableEntry;
import org.talend.designer.mapper.i18n.Messages;
import org.talend.designer.mapper.managers.MapperManager;
import org.talend.designer.mapper.managers.UIManager;
import org.talend.designer.mapper.model.table.IUILookupMode;
import org.talend.designer.mapper.model.table.IUIMatchingMode;
import org.talend.designer.mapper.model.table.IUIMenuOption;
import org.talend.designer.mapper.model.table.InputTable;
import org.talend.designer.mapper.model.table.TMAP_LOOKUP_MODE;
import org.talend.designer.mapper.model.table.TMAP_MATCHING_MODE;
import org.talend.designer.mapper.model.tableentry.GlobalMapEntry;
import org.talend.designer.mapper.model.tableentry.InputColumnTableEntry;
import org.talend.designer.mapper.ui.dnd.DragNDrop;
import org.talend.designer.mapper.ui.footer.StatusBar.STATUS;
import org.talend.designer.mapper.ui.image.ImageInfo;
import org.talend.designer.mapper.ui.image.ImageProviderMapper;
import org.talend.designer.mapper.ui.visualmap.zone.Zone;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class InputDataMapTableView extends DataMapTableView {

    private ToolItem dropDownMatchingModeItem;

    private ToolItem dropDownLookupModeItem;

    private Menu menuMatchingMode = null;

    private Menu menuLookupMode = null;

    private IUIMatchingMode previousMatchingModeSelected = getInputTable().getMatchingMode() != null
            && getInputTable().getMatchingMode() != TMAP_MATCHING_MODE.ALL_ROWS ? getInputTable().getMatchingMode()
            : TMAP_MATCHING_MODE.UNIQUE_MATCH;

    private boolean previousStateAtLeastOneHashKey;

    private ToolItem innerJoinCheck;

    private boolean previousInnerJoinSelection;

    private static boolean replaceLabelsByImagesForLookupMode = true;

    private static boolean useImagesForMatching = true;

    private static boolean useTextForMatching = true;

    public InputDataMapTableView(Composite parent, int style, InputTable inputTable, MapperManager mapperManager) {
        super(parent, style, inputTable, mapperManager);
        previousValidPersistentMode = inputTable.isPersistent();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.ui.visualmap.table.DataMapTableView#createContent()
     */
    @Override
    protected void createContent() {
        createTableForColumns();

        createExpressionFilter(DEFAULT_POST_MATCHING_EXPRESSION_FILTER);

    }

    private ExtendedTextCellEditor expressionCellEditor;

    protected ToolItem activatePersistentCheck;

    protected boolean previousStatePersistentCheckFilter;

    protected boolean previousValidPersistentMode;

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.ui.visualmap.table.DataMapTableView#notifyFocusLost()
     */
    @Override
    public void notifyFocusLost() {
        if (expressionCellEditor != null) {
            expressionCellEditor.focusLost();
        }
    }

    @Override
    public void initColumnsOfTableColumns(TableViewerCreator tableViewerCreatorForColumns) {
        boolean isMainConnection = ((InputTable) getDataMapTable()).isMainConnection();
        TableViewerCreatorColumn column = null;
        if (!isMainConnection) {

            column = new TableViewerCreatorColumn(tableViewerCreatorForColumns);
            column.setTitle(Messages.getString("InputDataMapTableView.columnTitle.Expr")); //$NON-NLS-1$
            column.setId(DataMapTableView.ID_EXPRESSION_COLUMN);
            expressionCellEditor = createExpressionCellEditor(tableViewerCreatorForColumns, column, new Zone[] { Zone.INPUTS },
                    false);
            column.setBeanPropertyAccessors(new IBeanPropertyAccessors<InputColumnTableEntry, String>() {

                public String get(InputColumnTableEntry bean) {
                    return bean.getExpression();
                }

                public void set(InputColumnTableEntry bean, String value) {
                    // System.out.println("value='" + value + "'");
                    bean.setExpression(value);
                    mapperManager.getProblemsManager().checkProblemsForTableEntry(bean, true);
                }

            });
            column.setModifiable(!mapperManager.componentIsReadOnly());
            column.setDefaultInternalValue(""); //$NON-NLS-1$
            column.setWeight(COLUMN_EXPRESSION_SIZE_WEIGHT);
            column.setImageProvider(new IColumnImageProvider<InputColumnTableEntry>() {

                public Image getImage(InputColumnTableEntry bean) {
                    if (bean.getExpression() != null && !bean.getExpression().trim().equals("")) { //$NON-NLS-1$
                        if (mapperManager.isAdvancedMap()) {
                            return ImageProviderMapper.getImage(ImageInfo.LOOKUP_KEY_ICON);
                        } else {
                            return null;
                        }
                    } else {
                        return null;
                    }
                }

            });
        }

        column = new TableViewerCreatorColumn(tableViewerCreatorForColumns);
        column.setTitle(DataMapTableView.COLUMN_NAME);
        column.setId(DataMapTableView.ID_NAME_COLUMN);
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<InputColumnTableEntry, String>() {

            public String get(InputColumnTableEntry bean) {
                return bean.getMetadataColumn().getLabel();
            }

            public void set(InputColumnTableEntry bean, String value) {
                bean.getMetadataColumn().setLabel(value);
            }

        });
        column.setWeight(COLUMN_NAME_SIZE_WEIGHT);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.ui.visualmap.table.DataMapTableView#initTableConstraints()
     */
    @Override
    protected void initExtraTable() {

        createGlobalMapTable();

        List<GlobalMapEntry> entries = ((InputTable) getDataMapTable()).getGlobalMapEntries();

        // correct partially layout problem with GTK when cell editor value is
        // applied
        tableViewerCreatorForGlobalMap.setAdjustWidthValue(WindowSystem.isGTK() ? -20 : ADJUST_WIDTH_VALUE);

        tableViewerCreatorForGlobalMap.init(entries);
        updateGridDataHeightForTableGlobalMap();
        //
        // if (WindowSystem.isGTK()) {
        // tableViewerCreatorForGlobalMap.layout();
        // }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.ui.visualmap.table.DataMapTableView#addEntriesActionsComponents()
     */
    @Override
    protected boolean addToolItems() {

        Point realToolbarSize = getRealToolbarSize();

        if (!getInputTable().isMainConnection()) {

            if (mapperManager.isAdvancedMap()) {

                if (LanguageManager.getCurrentLanguage() == ECodeLanguage.JAVA) {
                    // LOOKUP MODE
                    dropDownLookupModeItem = new ToolItem(toolBarActions, SWT.DROP_DOWN | SWT.BORDER);
                    dropDownLookupModeItem.setEnabled(!mapperManager.componentIsReadOnly());
                    refreshLabelForLookupModeDropDown();
                    dropDownLookupModeItem.addSelectionListener(new DropDownLookupModeSelectionListener());
                    realToolbarSize.x += 70;
                }

                // MATCHING MODE
                dropDownMatchingModeItem = new ToolItem(toolBarActions, SWT.DROP_DOWN | SWT.BORDER);
                dropDownMatchingModeItem.setEnabled(!mapperManager.componentIsReadOnly());
                refreshLabelForMatchingModeDropDown();
                dropDownMatchingModeItem.addSelectionListener(new DropDownMatchingModeSelectionListener());
                realToolbarSize.x += 70;
            }

            // Inner join button
            innerJoinCheck = new ToolItem(toolBarActions, SWT.CHECK);
            innerJoinCheck.setEnabled(!mapperManager.componentIsReadOnly());
            realToolbarSize.x += 70;
            innerJoinCheck.setToolTipText(Messages.getString("InputDataMapTableView.widgetTooltip.rejectMainRow")); //$NON-NLS-1$
            boolean isInnerJoin = getInputTable().isInnerJoin();
            Image image = ImageProviderMapper.getImage(isInnerJoin ? ImageInfo.CHECKED_ICON : ImageInfo.UNCHECKED_ICON);
            if (WindowSystem.isGTK()) {
                innerJoinCheck.setImage(image);
                innerJoinCheck.setHotImage(image);
            } else {
                innerJoinCheck.setImage(ImageProviderMapper.getImage(ImageInfo.UNCHECKED_ICON));
                innerJoinCheck.setHotImage(image);
            }
            innerJoinCheck.setSelection(isInnerJoin);
            innerJoinCheck.setText(Messages.getString("InputDataMapTableView.widgetTooltip.innerJoin")); //$NON-NLS-1$

            innerJoinCheck.addSelectionListener(new SelectionListener() {

                public void widgetDefaultSelected(SelectionEvent e) {
                }

                public void widgetSelected(SelectionEvent e) {
                    Image image = null;
                    if (innerJoinCheck.getSelection()) {
                        getInputTable().setInnerJoin(true);
                        image = ImageProviderMapper.getImage(ImageInfo.CHECKED_ICON);
                    } else {
                        getInputTable().setInnerJoin(false);
                        image = ImageProviderMapper.getImage(ImageInfo.UNCHECKED_ICON);
                    }
                    if (WindowSystem.isGTK()) {
                        innerJoinCheck.setImage(image);
                        innerJoinCheck.setHotImage(image);
                    } else {
                        innerJoinCheck.setHotImage(image);
                    }
                    updateViewAfterChangeInnerJoinCheck();
                }

            });

            if (mapperManager.isPersistentMap()) {
                final InputTable table = getInputTable();
                TMAP_LOOKUP_MODE lookupMode = (TMAP_LOOKUP_MODE) table.getLookupMode();
                activatePersistentCheck = new ToolItem(toolBarActions, SWT.CHECK);
                activatePersistentCheck.setEnabled(!mapperManager.componentIsReadOnly());
                // previousStatePersistentCheckFilter = table.isPersistent();
                activatePersistentCheck.setSelection(table.isPersistent());
                activatePersistentCheck.setToolTipText(Messages
                        .getString("InputDataMapTableView.buttonTooltip.activatePersistentCheck")); //$NON-NLS-1$
                activatePersistentCheck.setImage(ImageProviderMapper.getImage(ImageInfo.ACTIVATE_PERSISTENT_ICON));

                previousValidPersistentMode = lookupMode != TMAP_LOOKUP_MODE.CACHE_OR_RELOAD && table.isPersistent();

                // /////////////////////////////////////////////////////////////////
                if (activatePersistentCheck != null) {

                    activatePersistentCheck.addSelectionListener(new SelectionListener() {

                        public void widgetDefaultSelected(SelectionEvent e) {
                        }

                        public void widgetSelected(SelectionEvent e) {
                            // updateExepressionFilterTextAndLayout(true);
                            // previousStatePersistentCheckFilter = activatePersistentCheck.getSelection();

                            table.setPersistent(activatePersistentCheck.getSelection());
                            previousValidPersistentMode = activatePersistentCheck.getSelection();
                        }

                    });
                }
                // /////////////////////////////////////////////////////////////////

                enableDisablePersistentMode(lookupMode);

            }
        }

        createActivateFilterCheck();

        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.ui.visualmap.table.DataMapTableView#getZone()
     */
    @Override
    public Zone getZone() {
        return Zone.INPUTS;
    }

    public InputTable getInputTable() {
        return (InputTable) abstractDataMapTable;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.ui.visualmap.table.DataMapTableView#toolbarNeededToBeRightStyle()
     */
    @Override
    public boolean toolbarNeedToHaveRightStyle() {
        return !getInputTable().isMainConnection();
    }

    /**
     * Listens to widgetSelected() events on SWT.DROP_DOWN type ToolItems and opens/closes a menu when appropriate.
     */
    class DropDownMatchingModeSelectionListener implements SelectionListener {

        public void widgetSelected(SelectionEvent event) {
            // Create the menu if it has not already been created
            if (menuMatchingMode == null) {
                // Lazy create the menu.
                menuMatchingMode = new Menu(getShell(), SWT.POP_UP);

                IUIMatchingMode[] availableJoins = TMAP_MATCHING_MODE.values();

                for (int i = 0; i < availableJoins.length; ++i) {
                    IUIMatchingMode matchingMode = availableJoins[i];
                    final String text = matchingMode.getLabel();

                    if (matchingMode == TMAP_MATCHING_MODE.LAST_MATCH) {
                        continue;
                    }

                    if (text.length() != 0) {

                        MenuItem menuItem = new MenuItem(menuMatchingMode, SWT.NONE);
                        menuItem.setData(matchingMode);
                        menuItem.setText(text);

                        addMenuItemListener(menuItem);

                        enableMenuItemMatchingMode(menuItem);

                        boolean selected = matchingMode == getInputTable().getMatchingMode();
                        changeMenuItem(matchingMode.getImageInfo(), menuItem, selected);

                        /*
                         * Add a menu selection listener so that the menu is hidden when the user selects an item from
                         * the drop down menu.
                         */
                        menuItem.addSelectionListener(new SelectionAdapter() {

                            @Override
                            public void widgetSelected(SelectionEvent e) {
                                MenuItem menuItem = (MenuItem) e.widget;
                                IUIMatchingMode matchingMode = (IUIMatchingMode) menuItem.getData();

                                IUILookupMode lookupMode = getInputTable().getLookupMode();

                                boolean invalidState = lookupMode == TMAP_LOOKUP_MODE.CACHE_OR_RELOAD
                                        && matchingMode == TMAP_MATCHING_MODE.ALL_ROWS;
                                setMenuMatchingModeVisible(false);
                                if (!invalidState) {
                                    selectMatchingModeItem(menuItem);
                                    getInputTable().setMatchingMode(matchingMode);
                                    refreshLabelForMatchingModeDropDown();
                                }
                            }

                        });
                    } else {
                        new MenuItem(menuMatchingMode, SWT.SEPARATOR);
                    }
                }
            }

            /**
             * A selection event will be fired when a drop down tool item is selected in the main area and in the drop
             * down arrow. Examine the event detail to determine where the widget was selected.
             */

            final ToolItem toolItem = (ToolItem) event.widget;
            final ToolBar toolBar = toolItem.getParent();

            Rectangle toolItemBounds = toolItem.getBounds();
            Point point = toolBar.toDisplay(new Point(toolItemBounds.x, toolItemBounds.y));
            menuMatchingMode.setLocation(point.x, point.y + toolItemBounds.height);
            MenuItem[] menuItems = menuMatchingMode.getItems();
            for (int i = 0; i < menuItems.length; i++) {
                MenuItem menuItem = menuItems[i];
                enableMenuItemMatchingMode(menuItem);
            }
            setMenuMatchingModeVisible(true);
        }

        private void setMenuMatchingModeVisible(boolean visible) {
            menuMatchingMode.setVisible(visible);
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
         */
        public void widgetDefaultSelected(SelectionEvent e) {
            System.out.println("widgetDefaultSelected"); //$NON-NLS-1$
        }
    }

    /**
     * Listens to widgetSelected() events on SWT.DROP_DOWN type ToolItems and opens/closes a menu when appropriate.
     */
    class DropDownLookupModeSelectionListener implements SelectionListener {

        private boolean visible;

        public void widgetSelected(SelectionEvent event) {
            // Create the menu if it has not already been created
            if (menuLookupMode == null) {
                // Lazy create the menu.
                menuLookupMode = new Menu(getShell());

                // IUILookupMode[] availableJoins = TMAP_LOOKUP_MODE.values();
                IUILookupMode[] availableJoins = { TMAP_LOOKUP_MODE.LOAD_ONCE, TMAP_LOOKUP_MODE.RELOAD,
                        TMAP_LOOKUP_MODE.CACHE_OR_RELOAD, };

                for (int i = 0; i < availableJoins.length; ++i) {
                    IUILookupMode lookupMode = availableJoins[i];
                    final String text = lookupMode.getLabel();
                    if (text.length() != 0) {
                        MenuItem menuItem = new MenuItem(menuLookupMode, SWT.NONE);
                        menuItem.setData(lookupMode);
                        menuItem.setText(text);

                        boolean selected = lookupMode == getInputTable().getLookupMode();
                        changeMenuItem(lookupMode.getImageInfo(), menuItem, selected);

                        addMenuItemListener(menuItem);

                        /*
                         * Add a menu selection listener so that the menu is hidden when the user selects an item from
                         * the drop down menu.
                         */
                        menuItem.addSelectionListener(new SelectionAdapter() {

                            @Override
                            public void widgetSelected(SelectionEvent e) {
                                MenuItem menuItem = (MenuItem) e.widget;
                                selectLookupModeItem(menuItem);
                                IUILookupMode lookupMode = (IUILookupMode) menuItem.getData();
                                getInputTable().setLookupMode(lookupMode);
                                if (lookupMode == TMAP_LOOKUP_MODE.RELOAD || lookupMode == TMAP_LOOKUP_MODE.CACHE_OR_RELOAD) {
                                    showTableGlobalMap(true);
                                } else {
                                    showTableGlobalMap(false);
                                }
                                setMenuVisible(false);
                                refreshLabelForLookupModeDropDown();
                                checkChangementsAfterEntryModifiedOrAdded(true);
                            }

                        });
                    } else {
                        new MenuItem(menuLookupMode, SWT.SEPARATOR);
                    }
                }
            }

            /**
             * A selection event will be fired when a drop down tool item is selected in the main area and in the drop
             * down arrow. Examine the event detail to determine where the widget was selected.
             */

            final ToolItem toolItem = (ToolItem) event.widget;
            final ToolBar toolBar = toolItem.getParent();

            Rectangle toolItemBounds = toolItem.getBounds();
            Point point = toolBar.toDisplay(new Point(toolItemBounds.x, toolItemBounds.y));
            menuLookupMode.setLocation(point.x, point.y + toolItemBounds.height);
            MenuItem[] menuItems = menuLookupMode.getItems();
            for (int i = 0; i < menuItems.length; i++) {
                MenuItem menuItem = menuItems[i];
                enableMenuItemLoookupMode(menuItem);
            }
            setMenuVisible(true);
        }

        private void setMenuVisible(boolean visible) {
            menuLookupMode.setVisible(visible);
            this.visible = visible;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
         */
        public void widgetDefaultSelected(SelectionEvent e) {
            System.out.println("widgetDefaultSelected"); //$NON-NLS-1$
        }
    }

    private void changeMenuItem(ImageInfo imageInfo, MenuItem menuItem, boolean selected) {

        boolean isMatchingMode = menuItem.getData() instanceof IUIMatchingMode;

        if (replaceLabelsByImagesForLookupMode || isMatchingMode && useImagesForMatching) {
            if (selected) {
                menuItem.setImage(ImageProviderMapper.getImage(imageInfo));
            } else {
                Image image = ImageProviderMapper.getImage(imageInfo);
                Image modifiedImage = ImageUtils.changeAlpha(image, 100);
                menuItem.setImage(modifiedImage);
            }
        } else {
            if (selected) {
                menuItem.setImage(ImageProviderMapper.getImage(ImageInfo.CHECKED_ICON));
            } else {
                menuItem.setImage(null);
            }

        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.dbmap.ui.visualmap.table.DataMapTableView#hasDropDownToolBarItem()
     */
    @Override
    public boolean hasDropDownToolBarItem() {
        return !getInputTable().isMainConnection() && mapperManager.isAdvancedMap();
    }

    public void refreshLabelForMatchingModeDropDown() {
        IUIMatchingMode matchingMode = getInputTable().getMatchingMode();
        if (matchingMode == TMAP_MATCHING_MODE.ALL_ROWS) {
            if (innerJoinCheck != null) {
                innerJoinCheck.setEnabled(false);
            }
        } else {
            previousMatchingModeSelected = matchingMode;
            previousInnerJoinSelection = getInputTable().isInnerJoin();
            if (innerJoinCheck != null) {
                innerJoinCheck.setEnabled(true);
            }
        }
        if (innerJoinCheck != null) {
            updateViewAfterChangeInnerJoinCheck();
        }

        if (useImagesForMatching) {
            dropDownMatchingModeItem.setImage(ImageProviderMapper.getImage(matchingMode.getImageInfo()));
        }

        if (useTextForMatching) {
            dropDownMatchingModeItem.setText(matchingMode.getLabel());
        } else {
            dropDownMatchingModeItem.setText(""); //$NON-NLS-1$
        }

        dropDownMatchingModeItem.setToolTipText(matchingMode.getLabel());

        layoutToolBar();

    }

    private void layoutToolBar() {
        Point sizeToolBar = toolBarActions.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        GridData gridData = (GridData) toolBarActions.getLayoutData();
        if (gridData != null) {
            gridData.widthHint = sizeToolBar.x;
            // gridData.widthHint -= 60;
            toolBarActions.getParent().layout();
        }
    }

    public void refreshLabelForLookupModeDropDown() {
        IUILookupMode lookupMode = getInputTable().getLookupMode();

        if (replaceLabelsByImagesForLookupMode) {
            dropDownLookupModeItem.setImage(ImageProviderMapper.getImage(lookupMode.getImageInfo()));
            dropDownLookupModeItem.setText(""); //$NON-NLS-1$
        } else {
            dropDownLookupModeItem.setText(lookupMode.getLabel());
        }

        dropDownLookupModeItem.setToolTipText(lookupMode.getTooltipText());

        // if (activatePersistentCheck != null) {
        // if (lookupMode == TMAP_LOOKUP_MODE.CACHE_OR_RELOAD) {
        // activatePersistentCheck.setEnabled(false);
        // } else {
        // activatePersistentCheck.setEnabled(previousValidPersistentMode);
        // }
        // }

        layoutToolBar();

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.dbmap.ui.visualmap.table.DataMapTableView#checkChangementsAfterNewExpressionApplied()
     */
    @Override
    public void checkChangementsAfterEntryModifiedOrAdded(boolean forceEvaluation) {
        super.checkChangementsAfterEntryModifiedOrAdded(forceEvaluation);
        if (!getInputTable().isMainConnection() && mapperManager.isAdvancedMap()) {
            boolean stateAtLeastOneHashKey = mapperManager.isTableHasAtLeastOneHashKey(getInputTable());
            if (forceEvaluation || previousStateAtLeastOneHashKey != stateAtLeastOneHashKey) {
                if (stateAtLeastOneHashKey) {
                    if (useImagesForMatching) {
                        dropDownMatchingModeItem.setImage(ImageProviderMapper.getImage(previousMatchingModeSelected
                                .getImageInfo()));
                    }
                    if (useTextForMatching) {
                        dropDownMatchingModeItem.setText(previousMatchingModeSelected.getLabel());
                    } else {
                        dropDownMatchingModeItem.setText(""); //$NON-NLS-1$
                    }
                    dropDownMatchingModeItem.setToolTipText(previousMatchingModeSelected.getLabel());
                    getInputTable().setMatchingMode(previousMatchingModeSelected);
                    selectMatchingModeMenuItem(previousMatchingModeSelected);
                    getInputTable().setInnerJoin(previousInnerJoinSelection);
                    innerJoinCheck.setEnabled(true);
                    updateViewAfterChangeInnerJoinCheck();
                } else {

                    TMAP_MATCHING_MODE matchingMode = TMAP_MATCHING_MODE.ALL_ROWS;

                    if (getInputTable().getLookupMode() == TMAP_LOOKUP_MODE.CACHE_OR_RELOAD) {
                        String errorMessage = Messages.getString("InputDataMapTableView.invalidConfiguration", getInputTable() //$NON-NLS-1$
                                .getName());
                        mapperManager.getUiManager().setStatusBarValues(STATUS.ERROR, errorMessage);
                        dropDownMatchingModeItem.setText(Messages.getString("InputDataMapTableView.invalid")); //$NON-NLS-1$
                        if (useImagesForMatching) {
                            dropDownMatchingModeItem.setImage(ImageProviderMapper.getImage(matchingMode.getImageInfo()));
                        }
                        dropDownMatchingModeItem.setToolTipText(errorMessage);
                    } else {
                        mapperManager.getUiManager().setStatusBarValues(STATUS.EMPTY, null);
                        if (useImagesForMatching) {
                            dropDownMatchingModeItem.setImage(ImageProviderMapper.getImage(matchingMode.getImageInfo()));
                        }
                        if (useTextForMatching) {
                            dropDownMatchingModeItem.setText(matchingMode.getLabel());
                        } else {
                            dropDownMatchingModeItem.setText(""); //$NON-NLS-1$
                        }
                        dropDownMatchingModeItem.setToolTipText(matchingMode.getLabel());
                        selectMatchingModeMenuItem(matchingMode);
                    }
                    getInputTable().setMatchingMode(matchingMode);

                    getInputTable().setInnerJoin(false);
                    innerJoinCheck.setEnabled(false);
                }

                previousStateAtLeastOneHashKey = stateAtLeastOneHashKey;
            }
            layoutToolBar();

        }

    }

    /**
     * DOC amaumont Comment method "updateViewAfterChangeInnerJoinCheck".
     */
    private void updateViewAfterChangeInnerJoinCheck() {

        // if (LanguageProvider.getCurrentLanguage() instanceof PerlLanguage) {
        // if (innerJoinCheck.getSelection() || getInputTable().getMatchingMode() == TMAP_MATCHING_MODE.ALL_ROWS) {
        // getActivateFilterCheck().setSelection(isPreviousStateCheckFilter());
        // getActivateFilterCheck().setEnabled(true);
        // } else {
        // getActivateFilterCheck().setSelection(false);
        // getActivateFilterCheck().setEnabled(false);
        // }
        // }
        updateExepressionFilterTextAndLayout(false);

        // enableDisablePersistentMode((TMAP_MATCHING_MODE) getInputTable().getMatchingMode());
    }

    /**
     * DOC amaumont Comment method "enableMenuItemMatchingMode".
     * 
     * @param availableJoins
     * @param i
     * @param menuItem
     */
    private void enableMenuItemMatchingMode(MenuItem menuItem) {
        IUIMatchingMode matchingMode = (IUIMatchingMode) menuItem.getData();
        if (matchingMode == TMAP_MATCHING_MODE.ALL_ROWS && getInputTable().getMatchingMode() != TMAP_MATCHING_MODE.ALL_ROWS
                || matchingMode != TMAP_MATCHING_MODE.ALL_ROWS
                && getInputTable().getMatchingMode() == TMAP_MATCHING_MODE.ALL_ROWS) {
            menuItem.setEnabled(false);
        } else {
            menuItem.setEnabled(true);
        }
    }

    /**
     * DOC amaumont Comment method "enableMenuItemMatchingMode".
     * 
     * @param availableJoins
     * @param i
     * @param menuItem
     */
    private void enableMenuItemLoookupMode(MenuItem menuItem) {
        // IUILookupMode lookupMode = (IUILookupMode) menuItem.getData();
        menuItem.setEnabled(true);
    }

    /**
     * DOC amaumont Comment method "selectMenuItem".
     * 
     * @param menuItem
     */
    private void selectMatchingModeItem(MenuItem menuItem) {
        if (menuMatchingMode != null) {
            MenuItem[] menuItems = menuMatchingMode.getItems();
            for (int j = 0; j < menuItems.length; j++) {
                MenuItem currentMenuItem = menuItems[j];
                TMAP_MATCHING_MODE matchingMode = (TMAP_MATCHING_MODE) currentMenuItem.getData();
                changeMenuItem(matchingMode.getImageInfo(), currentMenuItem, false);
            }
            TMAP_MATCHING_MODE matchingMode = (TMAP_MATCHING_MODE) menuItem.getData();
            changeMenuItem(matchingMode.getImageInfo(), menuItem, true);
        }
    }

    /**
     * DOC amaumont Comment method "selectMenuItem".
     * 
     * @param menuItem
     */
    private void selectLookupModeItem(MenuItem menuItem) {
        if (menuLookupMode != null) {
            MenuItem[] menuItems = menuLookupMode.getItems();
            for (int j = 0; j < menuItems.length; j++) {
                MenuItem currentMenuItem = menuItems[j];
                TMAP_LOOKUP_MODE lookupMode = (TMAP_LOOKUP_MODE) currentMenuItem.getData();
                changeMenuItem(lookupMode.getImageInfo(), currentMenuItem, false);
            }
            TMAP_LOOKUP_MODE lookupMode = (TMAP_LOOKUP_MODE) menuItem.getData();
            changeMenuItem(lookupMode.getImageInfo(), menuItem, true);

            enableDisablePersistentMode(lookupMode);
        }
    }

    private void enableDisablePersistentMode(TMAP_LOOKUP_MODE lookupMode) {
        if (mapperManager.isPersistentMap()) {
            String baseMessage = Messages.getString("InputDataMapTableView.buttonTooltip.activatePersistentCheck"); //$NON-NLS-1$
            activatePersistentCheck.setSelection(previousValidPersistentMode);
            getInputTable().setPersistent(previousValidPersistentMode);
            switch (lookupMode) {
            case LOAD_ONCE:
            case LOAD_ONCE_AND_UPDATE:
            case RELOAD:
                activatePersistentCheck.setEnabled(true);
                getInputTable().setPersistent(previousValidPersistentMode);
                activatePersistentCheck.setToolTipText(baseMessage); //$NON-NLS-1$
                break;
            case CACHE_OR_RELOAD:
                activatePersistentCheck.setEnabled(false);
                activatePersistentCheck.setSelection(false);
                getInputTable().setPersistent(false);
                String explanation = Messages.getString(
                        "InputDataMapTableView.buttonTooltip.activatePersistentCheck.disabled", lookupMode.getLabel()); //$NON-NLS-1$
                activatePersistentCheck.setToolTipText(baseMessage + explanation); //$NON-NLS-1$
                break;
            default:
                break;
            }
        }
    }

    /**
     * DOC amaumont Comment method "selectMenuItem".
     * 
     * @param menuItem
     */
    private void selectMatchingModeMenuItem(IUIMatchingMode lookupType) {
        if (menuMatchingMode != null) {
            MenuItem[] menuItems = menuMatchingMode.getItems();
            for (int j = 0; j < menuItems.length; j++) {
                if (menuItems[j].getData() == lookupType) {
                    selectMatchingModeItem(menuItems[j]);
                    break;
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.ui.visualmap.table.DataMapTableView#getValidZonesForExpressionFilterField()
     */
    @Override
    protected Zone[] getValidZonesForExpressionFilterField() {
        return new Zone[] { Zone.INPUTS };
    }

    /**
     * DOC amaumont Comment method "init".
     */
    @Override
    public void loaded() {

        super.loaded();

        if (mapperManager.isAdvancedMap()) {
            configureExpressionFilter();
            if (!getInputTable().isMainConnection()) {
                checkChangementsAfterEntryModifiedOrAdded(true);
            }
        }

    }

    protected void createGlobalMapTable() {

        this.extendedTableViewerForGlobalMap = new AbstractExtendedTableViewer<GlobalMapEntry>(
                ((InputTable) abstractDataMapTable).getTableGlobalMapEntriesModel(), centerComposite) {

            @Override
            protected void createColumns(TableViewerCreator<GlobalMapEntry> tableViewerCreator, Table table) {
                createGlobalMapColumns(tableViewerCreator);
            }

            /*
             * (non-Javadoc)
             * 
             * @see
             * org.talend.commons.ui.swt.extended.macrotable.AbstractExtendedTableViewer#setTableViewerCreatorOptions
             * (org.talend.commons.ui.swt.tableviewer.TableViewerCreator)
             */
            @Override
            protected void setTableViewerCreatorOptions(TableViewerCreator<GlobalMapEntry> newTableViewerCreator) {
                super.setTableViewerCreatorOptions(newTableViewerCreator);
                newTableViewerCreator.setColumnsResizableByDefault(true);
                newTableViewerCreator.setShowLineSelection(SHOW_ROW_SELECTION.FULL);
                newTableViewerCreator.setBorderVisible(false);
                newTableViewerCreator.setLayoutMode(LAYOUT_MODE.FILL_HORIZONTAL);
                newTableViewerCreator.setKeyboardManagementForCellEdition(true);
                newTableViewerCreator.setHorizontalScroll(false);
                newTableViewerCreator.setVerticalScroll(false);
                // tableViewerCreatorForColumns.setUseCustomItemColoring(this.getDataMapTable()
                // instanceof
                // AbstractInOutTable);
                newTableViewerCreator.setFirstColumnMasked(true);

            }

        };
        tableViewerCreatorForGlobalMap = this.extendedTableViewerForGlobalMap.getTableViewerCreator();
        this.extendedTableViewerForGlobalMap.setCommandStack(mapperManager.getCommandStack());

        tableForGlobalMap = tableViewerCreatorForGlobalMap.getTable();
        tableForGlobalMapGridData = new GridData(GridData.FILL_HORIZONTAL);
        tableForGlobalMap.setLayoutData(tableForGlobalMapGridData);

        boolean tableGlobalMapVisible = false;
        if (abstractDataMapTable instanceof InputTable) {
            IUILookupMode lookupMode = ((InputTable) abstractDataMapTable).getLookupMode();
            tableGlobalMapVisible = lookupMode == TMAP_LOOKUP_MODE.RELOAD || lookupMode == TMAP_LOOKUP_MODE.CACHE_OR_RELOAD;
        }

        tableForGlobalMapGridData.exclude = !tableGlobalMapVisible;
        tableForGlobalMap.setVisible(tableGlobalMapVisible);

        if (!mapperManager.componentIsReadOnly()) {
            new DragNDrop(mapperManager, tableForGlobalMap, false, true);
        }

        tableViewerCreatorForGlobalMap.addCellValueModifiedListener(new ITableCellValueModifiedListener() {

            public void cellValueModified(TableCellValueModifiedEvent e) {
                unselectAllEntriesIfErrorDetected(e);
            }
        });

        final TableViewer tableViewer = tableViewerCreatorForGlobalMap.getTableViewer();

        tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                ISelection selection = event.getSelection();
                selectThisDataMapTableView();
                UIManager uiManager = mapperManager.getUiManager();
                uiManager.selectLinks(InputDataMapTableView.this, uiManager.extractSelectedTableEntries(selection), false, false);
            }

        });

        tableForGlobalMap.addListener(SWT.KeyDown, new Listener() {

            public void handleEvent(Event event) {
                processEnterKeyDown(tableViewerCreatorForGlobalMap, event);
            }

        });

        tableViewerCreatorForGlobalMap.setLabelProvider(new DefaultTableLabelProvider(tableViewerCreatorForGlobalMap) {

            @Override
            public Color getBackground(Object element, int columnIndex) {
                return getBackgroundCellColor(tableViewerCreator, element, columnIndex);
            }

            @Override
            public Color getForeground(Object element, int columnIndex) {
                return getForegroundCellColor(tableViewerCreator, element, columnIndex);
            }

        });

        initShowMessageErrorListener(tableForGlobalMap);
    }

    public void createGlobalMapColumns(final TableViewerCreator<GlobalMapEntry> tableViewerCreatorForGlobalMap) {
        TableViewerCreatorColumn<GlobalMapEntry, String> column = new TableViewerCreatorColumn<GlobalMapEntry, String>(
                tableViewerCreatorForGlobalMap);
        //        column.setTitle(Messages.getString("InputDataMapTableView.columnTitle.globalMapVar")); //$NON-NLS-1$
        column.setTitle("Expr."); //$NON-NLS-1$
        column.setId(DataMapTableView.ID_EXPRESSION_COLUMN);
        final ExtendedTextCellEditor expressionCellEditor = createExpressionCellEditor(tableViewerCreatorForGlobalMap, column,
                new Zone[] { Zone.INPUTS }, false);
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<GlobalMapEntry, String>() {

            public String get(GlobalMapEntry bean) {
                return bean.getExpression();
            }

            public void set(GlobalMapEntry bean, String value) {
                bean.setExpression(value);
                mapperManager.getProblemsManager().checkProblemsForTableEntry(bean, true);
            }

        });
        column.setModifiable(true);
        column.setDefaultInternalValue(""); //$NON-NLS-1$
        column.setWeight(COLUMN_EXPRESSION_SIZE_WEIGHT);
        column.setMoveable(false);
        column.setResizable(true);

        column = new TableViewerCreatorColumn<GlobalMapEntry, String>(tableViewerCreatorForGlobalMap);
        column.setTitle(Messages.getString("InputDataMapTableView.globalMapKey")); //$NON-NLS-1$
        column.setId(DataMapTableView.ID_NAME_COLUMN);
        column.setModifiable(true);
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<GlobalMapEntry, String>() {

            public String get(GlobalMapEntry bean) {
                return bean.getName();
            }

            public void set(GlobalMapEntry bean, String value) {
                bean.setName(value);
            }

        });
        column.setWeight(COLUMN_NAME_SIZE_WEIGHT);
        final TextCellEditor cellEditor = new TextCellEditor(tableViewerCreatorForGlobalMap.getTable());
        column.setCellEditor(cellEditor);

        // Column with remove button
        column = new TableViewerCreatorColumn(tableViewerCreatorForGlobalMap);
        column.setTitle(""); //$NON-NLS-1$
        column.setDefaultDisplayedValue(""); //$NON-NLS-1$
        column.setToolTipHeader(Messages.getString("InputDataMapTableView.addNewGlobalMapVar.tooltip")); //$NON-NLS-1$
        // column.setWeight(10);
        column.setWidth(25);
        column.setMoveable(false);
        column.setResizable(true);
        column.setImageHeader(org.talend.commons.ui.image.ImageProvider.getImage(org.talend.commons.ui.image.ImageProvider
                .getImageDesc(EImage.ADD_ICON)));
        column.setTableColumnSelectionListener(new DefaultHeaderColumnSelectionListener(column, tableViewerCreatorForGlobalMap) {

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
             */
            public void widgetSelected(SelectionEvent arg0) {

                getInputTable().addGlobalMapEntry(new GlobalMapEntry(getInputTable(), "\"myKey\"", "")); //$NON-NLS-1$ //$NON-NLS-2$

                updateGridDataHeightForTableGlobalMap();
                resizeAtExpandedSize();

            }

        });
        ButtonPushImageTableEditorContent buttonImage = new ButtonPushImageTableEditorContent() {

            /*
             * (non-Javadoc)
             * 
             * @see
             * org.talend.commons.ui.swt.tableviewer.tableeditor.ButtonImageTableEditorContent#selectionEvent(org.talend
             * .commons.ui.swt.tableviewer.TableViewerCreatorColumn, java.lang.Object)
             */
            @Override
            protected void selectionEvent(TableViewerCreatorColumn column, Object bean) {

                ITableEntry tableEntry = (ITableEntry) bean;

                boolean removeEntry = MessageDialog.openConfirm(getShell(), Messages
                        .getString("InputDataMapTableView.removeGlobalMapVar.Title"), //$NON-NLS-1$
                        Messages.getString("InputDataMapTableView.removeGlobalMapVar.Message", tableEntry.getName())); //$NON-NLS-1$
                if (removeEntry) {
                    ExtendedTableRemoveCommand removeCommand = new ExtendedTableRemoveCommand(bean,
                            extendedTableViewerForGlobalMap.getExtendedTableModel());
                    mapperManager.removeTableEntry((ITableEntry) bean);
                    mapperManager.executeCommand(removeCommand);
                    tableViewerCreatorForGlobalMap.getTableViewer().refresh();
                    List list = tableViewerCreatorForGlobalMap.getInputList();
                    updateGridDataHeightForTableGlobalMap();
                    resizeAtExpandedSize();
                }

            }

        };
        buttonImage.setImage(ImageProvider.getImage(EImage.MINUS_ICON));
        column.setTableEditorContent(buttonImage);

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.ui.visualmap.table.DataMapTableView#unselectAllGlobalMapEntries()
     */
    @Override
    public void unselectAllGlobalMapEntries() {
        tableViewerCreatorForGlobalMap.getSelectionHelper().deselectAll();
    }

    private void addMenuItemListener(final MenuItem menuItem) {

        menuItem.addArmListener(new ArmListener() {

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.events.ArmListener#widgetArmed(org.eclipse.swt.events.ArmEvent)
             */
            public void widgetArmed(ArmEvent e) {
                IUIMenuOption data = (IUIMenuOption) menuItem.getData();
                mapperManager.getUiManager().setStatusBarValues(STATUS.INFO, data.getTooltipText());
            }

        });

    }

}
