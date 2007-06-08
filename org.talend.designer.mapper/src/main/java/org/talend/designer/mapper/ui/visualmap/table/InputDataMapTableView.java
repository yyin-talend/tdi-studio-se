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

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.talend.commons.ui.swt.proposal.ProposalUtils;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreatorColumn;
import org.talend.commons.ui.swt.tableviewer.behavior.IColumnImageProvider;
import org.talend.commons.ui.utils.ControlUtils;
import org.talend.commons.ui.ws.WindowSystem;
import org.talend.commons.utils.data.bean.IBeanPropertyAccessors;
import org.talend.core.language.ECodeLanguage;
import org.talend.designer.mapper.i18n.Messages;
import org.talend.designer.mapper.language.LanguageProvider;
import org.talend.designer.mapper.managers.MapperManager;
import org.talend.designer.mapper.model.table.ILookupType;
import org.talend.designer.mapper.model.table.InputTable;
import org.talend.designer.mapper.model.table.TMAP_MULTIPLE_MATCHING_MODE;
import org.talend.designer.mapper.model.tableentry.ExpressionFilterEntry;
import org.talend.designer.mapper.model.tableentry.IColumnEntry;
import org.talend.designer.mapper.model.tableentry.InputColumnTableEntry;
import org.talend.designer.mapper.ui.image.ImageInfo;
import org.talend.designer.mapper.ui.image.ImageProviderMapper;
import org.talend.designer.mapper.ui.proposal.expression.ExpressionProposalProvider;
import org.talend.designer.mapper.ui.visualmap.zone.Zone;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class InputDataMapTableView extends DataMapTableView {

    public static final String DEFAULT_EXPRESSION_FILTER = "< Type here an expression filter >";

    private ToolItem dropDownItem;

    private static final String EXPRESSION_FILTER_ENTRY = "EXPRESSION_FILTER_ENTRY";

    private StyledText expressionFilterText;

    private ExpressionProposalProvider expressionProposalProvider;

    private ToolItem uniqueCheck;

    public InputDataMapTableView(Composite parent, int style, InputTable inputTable, MapperManager mapperManager) {
        super(parent, style, inputTable, mapperManager);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.ui.visualmap.table.DataMapTableView#createContent()
     */
    @Override
    protected void createContent() {
        createTableForColumns();

        // ScrolledComposite scrolledComposite = new ScrolledComposite(getCenterComposite(), SWT.BORDER | SWT.V_SCROLL);
        // GridData gridData = new GridData(GridData.FILL_BOTH);
        // gridData.minimumHeight = 10;
        // // gridData.grabExcessVerticalSpace = true;
        // gridData.heightHint = 30;
        // scrolledComposite.setLayoutData(gridData);

        if (mapperManager.isAdvancedMap()) {

            // expressionFilterText = new Text(scrolledComposite, SWT.MULTI | SWT.WRAP | SWT.BORDER);
            expressionFilterText = new StyledText(getCenterComposite(), SWT.MULTI | SWT.WRAP | SWT.BORDER
                    | SWT.V_SCROLL);
            GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
            gridData.minimumHeight = 10;
            // gridData.grabExcessVerticalSpace = true;
            gridData.heightHint = 30;
            expressionFilterText.setLayoutData(gridData);

            // scrolledComposite.setExpandHorizontal(true);
            // scrolledComposite.setContent(expressionFilterText);

            // expressionFilterText.setLayoutData(new GridData(GridData.FILL_VERTICAL));
            // expressionFilterText.setLayoutData(new GridData());

            final String defaultText = DEFAULT_EXPRESSION_FILTER;
            String expressionFilter = getInputTable().getExpressionFilter();
            if (expressionFilter != null && !"".equals(expressionFilter.trim())) {
                expressionFilterText.setText(expressionFilter);
            } else {
                expressionFilterText.setText(defaultText);
            }
            expressionFilterText.addFocusListener(new FocusListener() {

                public void focusGained(FocusEvent e) {
                    Control text = (Control) e.getSource();
                    ;
                    if (defaultText.equals(ControlUtils.getText(text))) {
                        ControlUtils.setText(text, "");
                    }
                    getInputTable().setExpressionFilter(ControlUtils.getText(text));
                }

                public void focusLost(FocusEvent e) {
                    Control text = (Control) e.getSource();
                    if ("".equals(ControlUtils.getText(text).trim())) {
                        ControlUtils.setText(text, defaultText);
                    }
                    getInputTable().setExpressionFilter(ControlUtils.getText(text));
                }

            });
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
            column.setBeanPropertyAccessors(new IBeanPropertyAccessors<InputColumnTableEntry, String>() {

                public String get(InputColumnTableEntry bean) {
                    return bean.getExpression();
                }

                public void set(InputColumnTableEntry bean, String value) {
                    bean.setExpression(value);
                    mapperManager.getProblemsManager().checkProblemsForTableEntry(bean, true);
                }

            });
            column.setModifiable(!mapperManager.componentIsReadOnly());
            column.setDefaultInternalValue(""); //$NON-NLS-1$
            createExpressionCellEditor(tableViewerCreatorForColumns, column, new Zone[] { Zone.INPUTS }, false);
            column.setWeight(COLUMN_EXPRESSION_SIZE_WEIGHT);
            column.setImageProvider(new IColumnImageProvider<InputColumnTableEntry>() {

                public Image getImage(InputColumnTableEntry bean) {
                    if (bean.getExpression() != null && !bean.getExpression().trim().equals("")) {
                        if (LanguageProvider.getCurrentLanguage().getCodeLanguage() == ECodeLanguage.JAVA) {
                            if (mapperManager.isAdvancedMap()) {
                                return ImageProviderMapper.getImage(ImageInfo.LOOKUP_KEY_ICON);
                            } else {
                                return null;
                            }
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
    protected void initTableFilters() {
        // nothing
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

                // DROP DOWN
                dropDownItem = new ToolItem(toolBarActions, SWT.DROP_DOWN | SWT.BORDER);
                refreshLabelForJoinDropDown();
                dropDownItem.addSelectionListener(new DropDownSelectionListener());
                realToolbarSize.x += 70;

                // UNIQUE button
                uniqueCheck = new ToolItem(toolBarActions, SWT.CHECK);
                uniqueCheck.setEnabled(!mapperManager.componentIsReadOnly());
                realToolbarSize.x += 70;
                uniqueCheck.setToolTipText(Messages
                        .getString("InputDataMapTableView.widgetTooltip.uniqueLookupTooltip")); //$NON-NLS-1$
                boolean isUniqueLookup = getInputTable().isUniqueMatch();
                // Image image = ImageProviderMapper.getImage(isUniqueLookup ? ImageInfo.CHECKED_ICON :
                // ImageInfo.UNCHECKED_ICON);
                Image image = ImageProviderMapper.getImage(isUniqueLookup ? ImageInfo.CHECKED_ICON
                        : ImageInfo.UNCHECKED_ICON);
                if (WindowSystem.isGTK()) {
                    uniqueCheck.setImage(image);
                    uniqueCheck.setHotImage(image);
                } else {
                    uniqueCheck.setImage(ImageProviderMapper.getImage(ImageInfo.UNCHECKED_ICON));
                    uniqueCheck.setHotImage(image);
                }
                uniqueCheck.setSelection(isUniqueLookup);
                uniqueCheck.setText(Messages.getString("InputDataMapTableView.widgetTooltip.uniqueLabel")); //$NON-NLS-1$

                uniqueCheck.addSelectionListener(new SelectionListener() {

                    public void widgetDefaultSelected(SelectionEvent e) {
                    }

                    public void widgetSelected(SelectionEvent e) {
                        Image image = null;
                        if (uniqueCheck.getSelection()) {
                            getInputTable().setUniqueMatch(true);
                            dropDownItem.setEnabled(false);
                            dropDownItem.setText(TMAP_MULTIPLE_MATCHING_MODE.LAST_MATCH.getLabel());
                            image = ImageProviderMapper.getImage(ImageInfo.CHECKED_ICON);
                        } else {
                            getInputTable().setUniqueMatch(false);
                            dropDownItem.setText(getInputTable().getMultipleMatchingMode().getLabel());
                            dropDownItem.setEnabled(true);
                            image = ImageProviderMapper.getImage(ImageInfo.UNCHECKED_ICON);
                        }
                        if (WindowSystem.isGTK()) {
                            uniqueCheck.setImage(image);
                            uniqueCheck.setHotImage(image);
                        } else {
                            uniqueCheck.setHotImage(image);
                        }
                    }

                });
            }

            // Inner join button
            final ToolItem rejectConstraintCheck = new ToolItem(toolBarActions, SWT.CHECK);
            rejectConstraintCheck.setEnabled(!mapperManager.componentIsReadOnly());
            realToolbarSize.x += 70;
            rejectConstraintCheck.setToolTipText(Messages
                    .getString("InputDataMapTableView.widgetTooltip.rejectMainRow")); //$NON-NLS-1$
            boolean isInnerJoin = getInputTable().isInnerJoin();
            // Image image = ImageProviderMapper.getImage(isInnerJoin ? ImageInfo.CHECKED_ICON :
            // ImageInfo.UNCHECKED_ICON);
            Image image = ImageProviderMapper.getImage(isInnerJoin ? ImageInfo.CHECKED_ICON : ImageInfo.UNCHECKED_ICON);
            if (WindowSystem.isGTK()) {
                rejectConstraintCheck.setImage(image);
                rejectConstraintCheck.setHotImage(image);
            } else {
                rejectConstraintCheck.setImage(ImageProviderMapper.getImage(ImageInfo.UNCHECKED_ICON));
                rejectConstraintCheck.setHotImage(image);
            }
            rejectConstraintCheck.setSelection(isInnerJoin);
            rejectConstraintCheck.setText(Messages.getString("InputDataMapTableView.widgetTooltip.innerJoin")); //$NON-NLS-1$

            rejectConstraintCheck.addSelectionListener(new SelectionListener() {

                public void widgetDefaultSelected(SelectionEvent e) {
                }

                public void widgetSelected(SelectionEvent e) {
                    Image image = null;
                    if (rejectConstraintCheck.getSelection()) {
                        getInputTable().setInnerJoin(true);
                        image = ImageProviderMapper.getImage(ImageInfo.CHECKED_ICON);
                    } else {
                        getInputTable().setInnerJoin(false);
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
            return true;
        }
        return false;
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
     * DOC amaumont Comment method "registerProposalForExpressionFilter".
     */
    public void configureExpressionFilter() {
        if (this.expressionProposalProvider == null) {
            this.expressionProposalProvider = createExpressionProposalProvider();
        }
        ExpressionFilterEntry expressionFilterEntry = new ExpressionFilterEntry(abstractDataMapTable);
        expressionProposalProvider.init(getInputTable(), new Zone[] { Zone.INPUTS }, expressionFilterEntry);
        expressionFilterEntry.setName(EXPRESSION_FILTER_ENTRY);
        ProposalUtils.getCommonProposal(expressionFilterText, expressionProposalProvider);
    }

    /**
     * Listens to widgetSelected() events on SWT.DROP_DOWN type ToolItems and opens/closes a menu when appropriate.
     */
    class DropDownSelectionListener extends SelectionAdapter {

        private Menu menu = null;

        private boolean visible = false;

        public void widgetSelected(SelectionEvent event) {
            // Create the menu if it has not already been created
            if (menu == null) {
                // Lazy create the menu.
                menu = new Menu(getShell());

                ILookupType[] availableJoins = TMAP_MULTIPLE_MATCHING_MODE.values();

                for (int i = 0; i < availableJoins.length; ++i) {
                    final String text = availableJoins[i].getLabel();
                    if (text.length() != 0) {
                        MenuItem menuItem = new MenuItem(menu, SWT.NONE);
                        menuItem.setData(availableJoins[i]);
                        menuItem.setText(text);
                        if (availableJoins[i] == getInputTable().getMultipleMatchingMode()) {
                            menuItem.setImage(ImageProviderMapper.getImage(ImageInfo.CHECKED_ICON));
                        }

                        /*
                         * Add a menu selection listener so that the menu is hidden when the user selects an item from
                         * the drop down menu.
                         */
                        menuItem.addSelectionListener(new SelectionAdapter() {

                            public void widgetSelected(SelectionEvent e) {
                                MenuItem menuItem = (MenuItem) e.widget;
                                MenuItem[] menuItems = menu.getItems();
                                for (int j = 0; j < menuItems.length; j++) {
                                    menuItems[j].setImage(null);
                                }
                                menuItem.setImage(ImageProviderMapper.getImage(ImageInfo.CHECKED_ICON));
                                ILookupType lookupType = (ILookupType) menuItem.getData();
                                getInputTable().setMultipleMatchingMode(lookupType);
                                setMenuVisible(false);
                                refreshLabelForJoinDropDown();
                            }

                        });
                    } else {
                        new MenuItem(menu, SWT.SEPARATOR);
                    }
                }
            }

            /**
             * A selection event will be fired when a drop down tool item is selected in the main area and in the drop
             * down arrow. Examine the event detail to determine where the widget was selected.
             */

            /*
             * The drop down arrow was selected.
             */
            if (visible) {
                // Hide the menu to give the Arrow the appearance of being a toggle button.
                setMenuVisible(false);
            } else {
                // Position the menu below and vertically aligned with the the drop down tool button.
                final ToolItem toolItem = (ToolItem) event.widget;
                final ToolBar toolBar = toolItem.getParent();

                Rectangle toolItemBounds = toolItem.getBounds();
                Point point = toolBar.toDisplay(new Point(toolItemBounds.x, toolItemBounds.y));
                menu.setLocation(point.x, point.y + toolItemBounds.height);
                setMenuVisible(true);
            }
        }

        private void setMenuVisible(boolean visible) {
            menu.setVisible(visible);
            this.visible = visible;
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

    public void refreshLabelForJoinDropDown() {
        String text = getInputTable().getMultipleMatchingMode().getLabel();
        dropDownItem.setText(text);
        dropDownItem.setToolTipText(text);

        Point sizeToolBar = toolBarActions.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        GridData gridData = (GridData) toolBarActions.getLayoutData();
        if (gridData != null) {
            gridData.widthHint = sizeToolBar.x;
            // gridData.widthHint -= 60;
            toolBarActions.getParent().layout();
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.dbmap.ui.visualmap.table.DataMapTableView#checkChangementsAfterNewExpressionApplied()
     */
    @Override
    public void checkChangementsAfterEntryModifiedOrAdded() {
        super.checkChangementsAfterEntryModifiedOrAdded();
        if (!getInputTable().isMainConnection() && mapperManager.isAdvancedMap()) {
            List<IColumnEntry> columnEntries = getInputTable().getColumnEntries();
            boolean atLeastOneHashKey = false;
            for (IColumnEntry entry : columnEntries) {
                if (entry.getExpression() != null && !entry.getExpression().trim().equals("")) {
                    atLeastOneHashKey = true;
                    break;
                }
            }
            uniqueCheck.setEnabled(atLeastOneHashKey);
            if (atLeastOneHashKey) {
                if (uniqueCheck.getSelection()) {
                    dropDownItem.setText("");
                    dropDownItem.setToolTipText("");
                    dropDownItem.setEnabled(false);
                } else {
                    dropDownItem.setText(getInputTable().getMultipleMatchingMode().getLabel());
                    dropDownItem.setToolTipText(getInputTable().getMultipleMatchingMode().getLabel());
                    dropDownItem.setEnabled(true);
                    // dropDownItem.getControl().setVisible(true);
                }
            } else {
                // dropDownItem.getControl().setVisible(false);
                dropDownItem.setEnabled(false);
                dropDownItem.setText("All rows");
                dropDownItem.setToolTipText("All rows");
            }
            refreshLabelForJoinDropDown();
        }
    }

}
