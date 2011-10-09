// ============================================================================
//
// Copyright (C) 2006-2011 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.components.exchange.ui;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;
import org.talend.commons.ui.swt.dialogs.ErrorDialogWidthDetailArea;
import org.talend.commons.ui.swt.tableviewer.behavior.AbstractElementFilter;
import org.talend.core.model.process.EComponentCategory;
import org.talend.designer.components.exchange.ExchangePlugin;
import org.talend.designer.components.exchange.i18n.Messages;
import org.talend.designer.components.exchange.model.AvailableExtensionViewDetail;
import org.talend.designer.components.exchange.model.ComponentExtension;
import org.talend.designer.components.exchange.ui.actions.DownloadComponenentsAction;
import org.talend.designer.components.exchange.util.ActionHelper;
import org.talend.designer.components.exchange.util.ExchangeUtils;
import org.talend.designer.components.exchange.util.ExchangeWebService;
import org.talend.designer.components.exchange.util.WebserviceStatus;

/**
 * DOC hcyi class global comment. Detailled comment
 */
public class AvailableExtensionsComposite extends ExchangeComposite {

    private ISelection selection;

    private IWizardPage wizardPage = null;

    private Table itemTable;

    private static final String ITEM_EDITOR_KEY = "ITEM_EDITOR_KEY"; //$NON-NLS-1$

    private List<ComponentExtension> fAvailableExtensions = new ArrayList<ComponentExtension>();

    final StackLayout stackLayout;

    private AbstractElementFilter<ComponentExtension> elementFilter;

    Composite addToolBarComp, listExtensonsComp, extensionViewDetailComp;

    private CCombo addTosVersionFilterCombo;

    private Map<Integer, Image> exchangeStarImageMap = new HashMap<Integer, Image>();

    /**
     * DOC hcyi AvailableExtensionsComposite constructor comment.
     * 
     * @param parent
     * @param style
     */
    public AvailableExtensionsComposite(Composite parent, int styles, TabbedPropertySheetWidgetFactory factory,
            List<ComponentExtension> fExtensions) {
        super(parent, styles, factory);
        FormLayout layout = new FormLayout();
        setLayout(layout);
        FormData thisFormData = new FormData();
        thisFormData.left = new FormAttachment(0, 0);
        thisFormData.right = new FormAttachment(100, 0);
        thisFormData.top = new FormAttachment(0, 0);
        thisFormData.bottom = new FormAttachment(100, 0);
        setLayoutData(thisFormData);

        //
        addToolBarComp = widgetFactory.createFlatFormComposite(this);
        FormData data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(100, 0);
        data.top = new FormAttachment(0, 0);
        addToolBarComp.setLayoutData(data);
        addToolBarComp.setLayout(new FormLayout());

        final Text filterText = widgetFactory.createText(addToolBarComp, ""); //$NON-NLS-1$
        data = new FormData();
        data.left = new FormAttachment(80, 0);
        data.right = new FormAttachment(100, 0);
        data.top = new FormAttachment(0, ITabbedPropertyConstants.VSPACE);
        filterText.setLayoutData(data);
        filterText.addKeyListener(new KeyAdapter() {

            public void keyReleased(KeyEvent ke) {
                elementFilter.setSearchText(filterText.getText());
                updateItems();
            }
        });

        CLabel filterTitle = widgetFactory.createCLabel(addToolBarComp,
                Messages.getString("AvailableExtensionsComposite.FilterTitle")); //$NON-NLS-1$
        data = new FormData();
        data.left = new FormAttachment(75, 0);
        data.right = new FormAttachment(filterText, -ITabbedPropertyConstants.HSPACE);
        data.top = new FormAttachment(filterText, 0, SWT.CENTER);
        filterTitle.setLayoutData(data);

        //
        listExtensonsComp = widgetFactory.createFlatFormComposite(this);
        data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(100, 0);
        data.top = new FormAttachment(addToolBarComp, 0);
        data.bottom = new FormAttachment(100, 0);
        listExtensonsComp.setLayoutData(data);

        stackLayout = new StackLayout();
        listExtensonsComp.setLayout(stackLayout);

        itemTable = new Table(listExtensonsComp, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
        TableLayout tableLayout = new TableLayout();
        itemTable.setLayout(tableLayout);
        itemTable.setHeaderVisible(true);
        itemTable.setLinesVisible(true);

        //
        TableColumn itemColumn = new TableColumn(itemTable, SWT.CENTER);
        itemColumn.setText(Messages.getString("AvailableExtensionsComposite.ExtensionName")); //$NON-NLS-1$
        itemColumn.setWidth(280);

        TableColumn versionColumn = new TableColumn(itemTable, SWT.CENTER);
        versionColumn.setText(Messages.getString("AvailableExtensionsComposite.Version")); //$NON-NLS-1$
        versionColumn.setWidth(180);

        TableColumn ratingColumn = new TableColumn(itemTable, SWT.CENTER);
        ratingColumn.setText(Messages.getString("AvailableExtensionsComposite.Rating")); //$NON-NLS-1$
        ratingColumn.setWidth(120);

        TableColumn authorColumn = new TableColumn(itemTable, SWT.CENTER);
        authorColumn.setText(Messages.getString("AvailableExtensionsComposite.Author")); //$NON-NLS-1$
        authorColumn.setWidth(150);

        final TableColumn operateColumn = new TableColumn(itemTable, SWT.CENTER);
        operateColumn.setText(""); //$NON-NLS-1$
        operateColumn.setWidth(150);
        operateColumn.setResizable(false);

        Object layoutData = listExtensonsComp.getLayoutData();
        if (layoutData instanceof GridData) {
            GridData gridData = (GridData) layoutData;
            gridData.grabExcessVerticalSpace = true;
            gridData.verticalAlignment = SWT.FILL;
        }

        FormData formData = new FormData();
        formData.left = new FormAttachment(0);
        formData.top = new FormAttachment(0);
        formData.right = new FormAttachment(100);
        formData.bottom = new FormAttachment(100);
        itemTable.setLayoutData(formData);

        elementFilter = new AbstractElementFilter<ComponentExtension>() {

            protected boolean select(ComponentExtension element) {
                ComponentExtension ce = (ComponentExtension) element;
                if (ce.getLabel() != null && ce.getLabel().toUpperCase().matches(searchString)) {
                    return true;
                }
                if (ce.getAuthor() != null && ce.getAuthor().toUpperCase().matches(searchString)) {
                    return true;
                }

                return false;
            }

        };

        operateColumn.addControlListener(new ControlListener() {

            public void controlMoved(ControlEvent e) {
            }

            public void controlResized(ControlEvent e) {
            }
        });
        itemTable.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
            }
        });
        //
        if (fExtensions != null && !fExtensions.isEmpty()) {
            updateAvailableExtensions(fExtensions);
        }
        stackLayout.topControl = itemTable;
        listExtensonsComp.layout();

        Display.getDefault().asyncExec(new Runnable() {

            public void run() {
                IAction action1 = ActionHelper.getRefreshComponenentsAction();
                if (action1 != null) {
                    action1.setEnabled(true);
                }
                IAction action2 = ActionHelper.getShowInstalledExtensionsAction();
                if (action2 != null) {
                    action2.setEnabled(false);
                }
            }
        });
    }

    public void updateAvailableExtensions(List<ComponentExtension> extensions) {
        loadDownloadedExtensionsFromFile();
        fAvailableExtensions = extensions;
        // update status of Downloaded extensions
        checkDownloadedExtensions();
        removeItemElements(fAvailableExtensions);
        addItemElements(fAvailableExtensions);
        elementFilter.setElements(fAvailableExtensions);
        refresh();
    }

    private void updateItems() {
        clearTableContents(itemTable);
        addItemElements(elementFilter.filter());
    }

    private void clearTableContents(Table table) {
        if (table == null)
            return;
        table.removeAll();
        Control[] children = table.getChildren();
        if (children != null && children.length > 0) {
            for (Control child : children) {
                if (child != null) {
                    child.dispose();
                }
            }
        }
    }

    private void addItemElements(List<ComponentExtension> elements) {
        if (elements == null || elements.isEmpty()) {
            return;
        }
        itemTable.setRedraw(false);

        for (final ComponentExtension object : elements) {
            final TableItem tableItem = new TableItem(itemTable, SWT.NONE);
            tableItem.setData(object);

            //
            tableItem.setText(0, object.getLabel());
            tableItem.setText(1, object.getVersionExtension());
            tableItem.setImage(2, getRateImage(object.getRate()));
            tableItem.setText(3, object.getAuthor());

            final Composite operateComposit = new Composite(itemTable, SWT.NONE);
            operateComposit.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
            GridLayout layout = new GridLayout(5, false);
            layout.horizontalSpacing = 1;
            layout.verticalSpacing = 0;
            layout.marginHeight = 0;
            layout.marginWidth = 0;
            operateComposit.setLayout(layout);

            //
            final Link linkView = new Link(operateComposit, SWT.RIGHT);
            GridData data = new GridData(GridData.FILL_HORIZONTAL);
            data.horizontalAlignment = SWT.RIGHT;
            linkView.setLayoutData(data);
            linkView.setText("<A>view</A> /");
            linkView.setData(tableItem);
            linkView.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent event) {
                    //
                    if (((Link) event.widget).getData() != null && ((Link) event.widget).getData() instanceof TableItem) {
                        TableItem tableItem = (TableItem) ((Link) event.widget).getData();
                        if (tableItem != null && tableItem.getData() != null && tableItem.getData() instanceof ComponentExtension) {
                            setSelectedExtension((ComponentExtension) tableItem.getData());
                        }
                    }
                    //
                    extensionViewDetailComp = widgetFactory.createFlatFormComposite(listExtensonsComp);
                    FormData compositeData = new FormData();
                    compositeData.left = new FormAttachment(0, 0);
                    compositeData.right = new FormAttachment(100, 0);
                    compositeData.top = new FormAttachment(0, 0);
                    compositeData.bottom = new FormAttachment(100, 0);
                    extensionViewDetailComp.setLayoutData(compositeData);
                    extensionViewDetailComp.setLayout(new FormLayout());
                    //
                    createExtensionViewDetailControl(getSelectedExtension());
                    stackLayout.topControl = extensionViewDetailComp;
                    listExtensonsComp.layout();
                }

            });

            final Link linkDownload = new Link(operateComposit, SWT.LEFT);
            linkDownload.setText("<A>download</A>");
            linkDownload.setData(tableItem);
            linkDownload.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent event) {
                    //
                    if (((Link) event.widget).getData() != null && ((Link) event.widget).getData() instanceof TableItem) {
                        TableItem tableItem = (TableItem) ((Link) event.widget).getData();
                        if (tableItem != null && tableItem.getData() != null && tableItem.getData() instanceof ComponentExtension) {
                            setSelectedExtension((ComponentExtension) tableItem.getData());
                        }
                    }
                    //
                    DownloadComponenentsAction downloadAction = new DownloadComponenentsAction();
                    if (downloadAction != null) {
                        downloadAction.run();
                    }
                    // removeItemElement(selected);
                    itemTable.layout();
                    refresh();
                }
            });

            TableEditor versionEditor = new TableEditor(itemTable);
            versionEditor.minimumWidth = itemTable.getColumn(4).getWidth();
            versionEditor.setEditor(operateComposit, tableItem, 4);
            versionEditor.setItem(tableItem);
            tableItem.setData(ITEM_EDITOR_KEY, new TableEditor[] { versionEditor });
        }
        itemTable.setRedraw(true);
        refresh();
    }

    /**
     * 
     * DOC hcyi Comment method "createExtensionViewDetailControl".
     * 
     * @param componentExtension
     */
    private void createExtensionViewDetailControl(final ComponentExtension componentExtension) {

        ScrolledComposite execScrolls = new ScrolledComposite(extensionViewDetailComp, SWT.V_SCROLL | SWT.H_SCROLL);
        execScrolls.setExpandHorizontal(true);
        execScrolls.setExpandVertical(true);
        FormData data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(100, 0);
        data.top = new FormAttachment(0, 0);
        data.bottom = new FormAttachment(100, 0);
        execScrolls.setLayoutData(data);

        Composite viewDetailComposite = new Composite(execScrolls, SWT.NONE);
        execScrolls.setContent(viewDetailComposite);
        execScrolls.setLayout(new FormLayout());
        data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(100, 0);
        data.top = new FormAttachment(0, 0);
        data.bottom = new FormAttachment(100, 0);
        viewDetailComposite.setLayoutData(data);
        viewDetailComposite.setLayout(new FormLayout());

        Composite composite_1 = new Composite(viewDetailComposite, SWT.NONE);
        composite_1.setLayout(new FormLayout());
        FormData fd_composite_1 = new FormData();
        fd_composite_1.bottom = new FormAttachment(100, -12);
        fd_composite_1.right = new FormAttachment(0, 907);
        fd_composite_1.left = new FormAttachment(0, 10);
        composite_1.setLayoutData(fd_composite_1);

        Button btnNewButton = widgetFactory.createButton(viewDetailComposite,
                Messages.getString("MyExtensionsComposite.Form.Return"), SWT.CENTER);
        fd_composite_1.top = new FormAttachment(0, 33);

        Group group = new Group(composite_1, SWT.NONE);
        group.setLayout(new FormLayout());
        FormData fd_group = new FormData();
        fd_group.left = new FormAttachment(0, 10);
        fd_group.bottom = new FormAttachment(0, 420);
        fd_group.top = new FormAttachment(0);
        group.setLayoutData(fd_group);

        final Label lblNewLabel = new Label(group, SWT.WRAP | SWT.CENTER);
        FontData newFontData = lblNewLabel.getFont().getFontData()[0];
        newFontData.setStyle(SWT.BOLD);
        newFontData.setHeight(10);
        Font newFont = new Font(this.getShell().getDisplay(), newFontData);
        lblNewLabel.setFont(newFont);
        FormData fd_lblNewLabel = new FormData();
        fd_lblNewLabel.top = new FormAttachment(0, 6);
        fd_lblNewLabel.left = new FormAttachment(0, 18);
        lblNewLabel.setLayoutData(fd_lblNewLabel);
        // toFormaStr
        String cEName = toFormaStr(1, componentExtension.getLabel());
        lblNewLabel.setText(cEName != null ? cEName : "");

        Label lblNewLabel_1 = new Label(group, SWT.NONE);
        FormData fd_lblNewLabel_1 = new FormData();
        fd_lblNewLabel_1.top = new FormAttachment(lblNewLabel, 22);
        fd_lblNewLabel_1.left = new FormAttachment(lblNewLabel, 0, SWT.LEFT);
        lblNewLabel_1.setLayoutData(fd_lblNewLabel_1);
        lblNewLabel_1.setText("Version " + componentExtension.getVersionExtension());

        Label lblNewLabel_2 = new Label(group, SWT.NONE);
        FormData fd_lblNewLabel_2 = new FormData();
        fd_lblNewLabel_2.top = new FormAttachment(lblNewLabel_1, 6);
        fd_lblNewLabel_2.right = new FormAttachment(lblNewLabel_1, 0, SWT.RIGHT);
        lblNewLabel_2.setLayoutData(fd_lblNewLabel_2);
        lblNewLabel_2.setText(formatter.format(componentExtension.getPublicationDate()));

        Group group_1 = new Group(composite_1, SWT.NONE);
        fd_group.right = new FormAttachment(100, -742);
        group_1.setLayout(new FormLayout());
        FormData fd_group_1 = new FormData();
        fd_group_1.left = new FormAttachment(group, 6);
        fd_group_1.top = new FormAttachment(0);
        fd_group_1.right = new FormAttachment(100, -10);
        group_1.setLayoutData(fd_group_1);

        Group group_2 = new Group(composite_1, SWT.NONE);
        fd_group_1.bottom = new FormAttachment(group_2, -6);

        final Text text = new Text(group_1, SWT.H_SCROLL | SWT.V_SCROLL);
        FormData fd_text = new FormData();
        fd_text.top = new FormAttachment(0);
        fd_text.left = new FormAttachment(0);
        fd_text.bottom = new FormAttachment(100, -29);
        fd_text.right = new FormAttachment(0, 528);
        text.setLayoutData(fd_text);
        // toFormaStr
        String cEDescription = toFormaStr(2, componentExtension.getDescription());
        text.setText(cEDescription != null ? cEDescription : "");

        Label lblNewLabel_3 = new Label(group_1, SWT.NONE);
        FormData fd_lblNewLabel_3 = new FormData();
        fd_lblNewLabel_3.top = new FormAttachment(0, 10);
        fd_lblNewLabel_3.left = new FormAttachment(text, 67);
        lblNewLabel_3.setLayoutData(fd_lblNewLabel_3);
        lblNewLabel_3.setImage(exchangeImageMissing);

        Label lblNewLabel_4 = new Label(group_1, SWT.NONE);
        FormData fd_lblNewLabel_4 = new FormData();
        fd_lblNewLabel_4.right = new FormAttachment(text, 158, SWT.RIGHT);
        fd_lblNewLabel_4.left = new FormAttachment(text, 50);
        fd_lblNewLabel_4.top = new FormAttachment(lblNewLabel_3, 22);
        lblNewLabel_4.setLayoutData(fd_lblNewLabel_4);
        lblNewLabel_4.setImage(getRateImage(componentExtension.getRate()));

        final Button btnNewButton_1 = widgetFactory.createButton(group_1,
                Messages.getString("AvailableExtensionsComposite.ViewDetail.installOperateStatus"), SWT.CENTER);
        FormData fd_btnNewButton_1 = new FormData();
        fd_btnNewButton_1.top = new FormAttachment(lblNewLabel_4, 33);
        fd_btnNewButton_1.right = new FormAttachment(text, 141, SWT.RIGHT);
        fd_btnNewButton_1.left = new FormAttachment(text, 61);
        btnNewButton_1.setLayoutData(fd_btnNewButton_1);
        btnNewButton_1.setText("Install");

        Link link = new Link(group_1, SWT.NONE);
        FormData fd_link = new FormData();
        fd_link.top = new FormAttachment(text, 5);
        fd_link.left = new FormAttachment(text, 10, SWT.LEFT);
        link.setLayoutData(fd_link);
        link.setText("<a>More...</a>");

        group_2.setLayout(new FormLayout());
        FormData fd_group_2 = new FormData();
        fd_group_2.bottom = new FormAttachment(100, -10);
        fd_group_2.top = new FormAttachment(0, 195);
        fd_group_2.left = new FormAttachment(group, 6);
        fd_group_2.right = new FormAttachment(100, -10);
        group_2.setLayoutData(fd_group_2);
        group_2.setText("User Reviews");

        ScrolledComposite execScroll = new ScrolledComposite(group_2, SWT.V_SCROLL | SWT.H_SCROLL);
        execScroll.setExpandHorizontal(true);
        execScroll.setExpandVertical(true);
        data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(100, 0);
        data.top = new FormAttachment(0, 0);
        data.bottom = new FormAttachment(100, 0);
        execScroll.setLayoutData(data);

        Composite reviewsComposite = new Composite(execScroll, SWT.NONE);
        execScroll.setContent(reviewsComposite);
        execScroll.setLayout(new FormLayout());
        data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(100, 0);
        data.top = new FormAttachment(0, 0);
        data.bottom = new FormAttachment(100, 0);
        reviewsComposite.setLayoutData(data);
        reviewsComposite.setLayout(new FormLayout());

        final Link link_1 = new Link(reviewsComposite, SWT.NONE);
        FormData fd_link_1 = new FormData();
        fd_link_1.top = new FormAttachment(0);
        fd_link_1.right = new FormAttachment(100, -10);
        link_1.setLayoutData(fd_link_1);
        link_1.setText("<a>Write a review</a>");

        // review
        Map<Integer, AvailableExtensionViewDetail> fViewDetails = new HashMap<Integer, AvailableExtensionViewDetail>();
        Map<Integer, CLabel> fLabels = new HashMap<Integer, CLabel>();
        EList<AvailableExtensionViewDetail> reviews = componentExtension.getReviews();
        if (reviews != null && reviews.size() > 0) {
            for (int i = 1; i <= reviews.size(); i++) {
                fLabels.put(i, new CLabel(reviewsComposite, SWT.NONE));
                fViewDetails.put(i, reviews.get(i - 1));
            }
        }
        if (fLabels != null && !fLabels.isEmpty()) {

            Iterator ite = fLabels.entrySet().iterator();
            while (ite.hasNext()) {
                Map.Entry<Integer, CLabel> entry = (Entry<Integer, CLabel>) ite.next();
                int j = entry.getKey();
                CLabel objectLabel = entry.getValue();
                AvailableExtensionViewDetail viewDetail = fViewDetails.get(j);
                String tstr = " " + viewDetail.getTitle() + "\n\t" + toFormaStr(3, viewDetail.getComment());
                objectLabel.setImage(getRateImage(viewDetail.getReviewrate()));
                objectLabel.setText(tstr);
                data = new FormData();
                if (j == 1) {
                    data.left = new FormAttachment(0, 10);
                    data.right = new FormAttachment(link_1, 0);
                    data.top = new FormAttachment(0, 10);
                    objectLabel.setLayoutData(data);
                } else {
                    data.left = new FormAttachment(0, 10);
                    data.right = new FormAttachment(100, -AbstractPropertySection.STANDARD_LABEL_WIDTH + 30);
                    data.top = new FormAttachment(fLabels.get(j - 1), ITabbedPropertyConstants.VSPACE + 5);
                    objectLabel.setLayoutData(data);
                }
            }
        }

        execScroll.setMinSize(group_2.computeSize(SWT.DEFAULT, SWT.DEFAULT));

        FormData fd_btnNewButton = new FormData();
        fd_btnNewButton.bottom = new FormAttachment(composite_1, -6);
        fd_btnNewButton.left = new FormAttachment(0, 21);
        btnNewButton.setLayoutData(fd_btnNewButton);
        execScrolls.setMinSize(extensionViewDetailComp.computeSize(SWT.DEFAULT, SWT.DEFAULT));

        // event
        btnNewButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                stackLayout.topControl = itemTable;
                listExtensonsComp.layout();
            }
        });

        btnNewButton_1.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                DownloadComponenentsAction downloadAction = new DownloadComponenentsAction();
                if (downloadAction != null) {
                    downloadAction.run();
                }
                btnNewButton_1.setEnabled(false);
            }
        });

        link.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent event) {
                // toFormaStr
                String cEDescription = componentExtension.getDescription();
                text.setText(cEDescription != null ? cEDescription : "");
                extensionViewDetailComp.layout();
            }
        });

        link_1.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent event) {
                ReviewComponentDialog reviewDialog = new ReviewComponentDialog(link_1.getShell());
                if (reviewDialog.open() == IDialogConstants.OK_ID) {
                    final String title = reviewDialog.getTitle();
                    final String rate = reviewDialog.getRating() + "";
                    final String review = reviewDialog.getReview();
                    Display.getDefault().asyncExec(new Runnable() {

                        public void run() {
                            if (ExchangeUtils.checkUserAndPassword()) {
                                WebserviceStatus ws = ExchangeWebService.insertReviewService(componentExtension.getIdExtension(),
                                        ExchangeUtils.TYPEEXTENSION, ExchangeUtils.getUserName(),
                                        ExchangeUtils.getPasswordHash(), title, review, rate);
                                if (ws.isResult()) {
                                    MessageDialog.openInformation(
                                            link_1.getShell(),
                                            Messages.getString("AvailableExtensionsComposite.ViewDetail.WriteReview"), ws.getMessageException()); //$NON-NLS-1$
                                } else {
                                    String mainMsg = Messages
                                            .getString("AvailableExtensionsComposite.ViewDetail.InsertionReviewFailure")
                                            + " "
                                            + Messages
                                                    .getString("AvailableExtensionsComposite.ViewDetail.InsertionReviewFailureTip");
                                    new ErrorDialogWidthDetailArea(link_1.getShell(), ExchangePlugin.PLUGIN_ID, mainMsg, ws
                                            .getMessageException());
                                }
                            } else {
                                MessageDialog.openInformation(
                                        link_1.getShell(),
                                        Messages.getString("AvailableExtensionsComposite.ViewDetail.WriteReview"), Messages.getString("MyExtensionsComposite.Form.checkUserAndPassword")); //$NON-NLS-1$
                            }
                        }
                    });
                }
            }
        });
    }

    private void removeItemElements(List<ComponentExtension> objects) {
        itemTable.setRedraw(false);
        TableItem[] items = itemTable.getItems();
        for (TableItem item : items) {
            if (item.getData() != null && item.getData() instanceof ComponentExtension) {
                ComponentExtension itemObject = (ComponentExtension) item.getData();
                for (ComponentExtension object : objects) {
                    if (itemObject != null && itemObject.getIdExtension().equals(object.getIdExtension())) {
                        removeTableItem(item);
                    }
                }
            }
        }
        itemTable.setRedraw(true);
    }

    private void removeItemElement(ComponentExtension object) {
        itemTable.setRedraw(false);
        TableItem[] items = itemTable.getItems();
        for (TableItem item : items) {
            if (item.getData() != null && item.getData() instanceof ComponentExtension) {
                ComponentExtension itemObject = (ComponentExtension) item.getData();
                if (itemObject != null && itemObject.getIdExtension().equals(object.getIdExtension())) {
                    removeTableItem(item);
                }
            }
        }
        itemTable.setRedraw(true);
    }

    private void removeTableItem(TableItem item) {
        if (item == null) {
            return;
        }
        TableEditor[] editors = (TableEditor[]) item.getData(ITEM_EDITOR_KEY);
        if (editors != null) {
            for (int j = 0; j < editors.length; j++) {
                editors[j].getEditor().dispose();
                editors[j].dispose();
            }
        }
        item.dispose();
    }

    private void checkDownloadedExtensions() {
        if (fDownloadedExtensions == null || fAvailableExtensions == null) {
            return;
        }

        List<ComponentExtension> compatible = new ArrayList<ComponentExtension>();
        for (ComponentExtension available : fAvailableExtensions) {
            ComponentExtension downloaded = fDownloadedExtensions.get(available.getIdExtension());
            if (downloaded == null) {
                compatible.add(available);
            }
        }
        fAvailableExtensions = compatible;
    }

    public void removeDownloadedExtension(ComponentExtension extension) {
        fDownloadedExtensions.remove(extension.getIdExtension());

        fAvailableExtensions.add(extension);
    }

    public Boolean addDownloadedExtension(ComponentExtension extension) {
        if (fDownloadedExtensions.containsKey(extension.getIdExtension())
                && extension.getIdExtension().equals(fDownloadedExtensions.get(extension.getIdExtension()).getVersionExtension())) {
            return false;
        } else {
            fDownloadedExtensions.put(extension.getIdExtension(), extension);
            //
            saveDownloadedExtensionsToFile();
            for (int i = 0; i < fAvailableExtensions.size(); i++) {
                if (fAvailableExtensions.get(i).getIdExtension().equals(extension.getIdExtension())) {
                    fAvailableExtensions.remove(i);
                    break;
                }
            }
            return true;
        }
    }

    private Image getRateImage(String rate) {
        initStarImages();
        int num = 0;
        String star = "";
        if (rate != null && !"".equals(rate)) {
            double rates = Double.parseDouble(rate);
            DecimalFormat df = new DecimalFormat("#");
            num = Integer.parseInt(df.format(rates));
            return exchangeStarImageMap.get(num);

        }
        return exchangeStarImageMap.get(0);
    }

    private String toFormaStr(int num, String strTemp) {
        String temp = strTemp;
        if (strTemp != null && !strTemp.equals("")) {

            switch (num) {
            case 1:
                if (strTemp.length() > 13) {
                    StringBuffer sb = new StringBuffer(strTemp);
                    sb.insert(13, "-\n");
                    return sb.toString();
                }
            case 2:
                if (strTemp.length() > 80) {
                    temp = strTemp.substring(0, 80);
                }
                break;
            case 3:
                StringBuffer sb = new StringBuffer();
                if (strTemp.length() > 90) {
                    int len = strTemp.length() / 90;
                    for (int i = 0; i < len; i++) {
                        sb.append(temp.substring(0, 90));
                        sb.append("-\n");
                        temp = temp.substring(90, temp.length());

                    }
                    sb.append(strTemp.substring(90 * len, strTemp.length()));
                    temp = sb.toString();
                }
                break;
            default:
                return "";
            }
        }
        return temp;
    }

    private void initStarImages() {
        exchangeStarImageMap.put(0, ExchangePlugin.getImageDescriptor("icons/exchangeStar/star.jpg").createImage());
        exchangeStarImageMap.put(1, ExchangePlugin.getImageDescriptor("icons/exchangeStar/star1.jpg").createImage());
        exchangeStarImageMap.put(2, ExchangePlugin.getImageDescriptor("icons/exchangeStar/star2.jpg").createImage());
        exchangeStarImageMap.put(3, ExchangePlugin.getImageDescriptor("icons/exchangeStar/star3.jpg").createImage());
        exchangeStarImageMap.put(4, ExchangePlugin.getImageDescriptor("icons/exchangeStar/star4.jpg").createImage());
        exchangeStarImageMap.put(5, ExchangePlugin.getImageDescriptor("icons/exchangeStar/star5.jpg").createImage());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.properties.tab.IDynamicProperty#getSection()
     */
    public EComponentCategory getSection() {
        return EComponentCategory.AVAILABLEEXTENSIONS;
    }

    public ISelection getSelection() {
        return this.selection;
    }

    public IWizardPage getParentWizard() {
        return this.wizardPage;
    }

    public void setParentWizard(IWizardPage parentWizard) {
        this.wizardPage = parentWizard;
    }
}
