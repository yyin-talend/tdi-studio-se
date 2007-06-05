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

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolItem;
import org.talend.commons.ui.swt.proposal.ContentProposalAdapterExtended;
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
import org.talend.designer.mapper.model.table.InputTable;
import org.talend.designer.mapper.model.tableentry.ExpressionFilterEntry;
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

    private static final String EXPRESSION_FILTER_ENTRY = "EXPRESSION_FILTER_ENTRY";

    private StyledText expressionFilterText;

    private ExpressionProposalProvider expressionProposalProvider;

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

            final String defaultText = "< Type here an expression filter >";
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
        if (!getInputTable().isMainConnection()) {

            final ToolItem rejectConstraintCheck = new ToolItem(toolBarActions, SWT.CHECK);
            rejectConstraintCheck.setEnabled(!mapperManager.componentIsReadOnly());
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
    public boolean toolbarNeededToBeRightStyle() {
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
}
