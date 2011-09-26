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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;
import org.talend.core.model.process.EComponentCategory;
import org.talend.designer.components.exchange.i18n.Messages;
import org.talend.designer.components.exchange.model.ComponentExtension;
import org.talend.designer.components.exchange.ui.actions.DownloadComponenentsAction;
import org.talend.designer.components.exchange.util.ExchangeUtils;

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

    Composite addToolBarComp, listExtensonsComp, extensionViewDetailComp;

    private CCombo addTosVersionFilterCombo;

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
        FormLayout addToolBarCompLayout = new FormLayout();
        addToolBarComp.setLayout(addToolBarCompLayout);

        Text filterText = widgetFactory.createText(addToolBarComp, ""); //$NON-NLS-1$
        data = new FormData();
        data.left = new FormAttachment(80, 0);
        data.right = new FormAttachment(100, 0);
        data.top = new FormAttachment(0, ITabbedPropertyConstants.VSPACE);
        filterText.setLayoutData(data);

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
        itemColumn.setWidth(180);

        TableColumn versionColumn = new TableColumn(itemTable, SWT.CENTER);
        versionColumn.setText(Messages.getString("AvailableExtensionsComposite.Version")); //$NON-NLS-1$
        versionColumn.setWidth(180);

        TableColumn ratingColumn = new TableColumn(itemTable, SWT.CENTER);
        ratingColumn.setText(Messages.getString("AvailableExtensionsComposite.Rating")); //$NON-NLS-1$
        ratingColumn.setWidth(180);

        TableColumn authorColumn = new TableColumn(itemTable, SWT.CENTER);
        authorColumn.setText(Messages.getString("AvailableExtensionsComposite.Author")); //$NON-NLS-1$
        authorColumn.setWidth(180);

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

    }

    public void updateAvailableExtensions(List<ComponentExtension> extensions) {
        loadDownloadedExtensionsFromFile();
        fAvailableExtensions = extensions;
        // update status of Downloaded extensions
        checkDownloadedExtensions();
        removeItemElements(fAvailableExtensions);
        addItemElements(fAvailableExtensions);
        refresh();
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
            tableItem.setText(2, getRate(object.getRate()));
            tableItem.setText(3, object.getAuthor());

            final Composite operateComposit = new Composite(itemTable, SWT.NONE);
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
                    FormLayout layout = new FormLayout();
                    setLayout(layout);
                    FormData thisFormData = new FormData();
                    thisFormData.left = new FormAttachment(0, 0);
                    thisFormData.right = new FormAttachment(100, 0);
                    thisFormData.top = new FormAttachment(0, 0);
                    thisFormData.bottom = new FormAttachment(100, 0);
                    setLayoutData(thisFormData);
                    extensionViewDetailComp = widgetFactory.createFlatFormComposite(listExtensonsComp);
                    FormData compositeData = new FormData();
                    compositeData.left = new FormAttachment(0, 0);
                    compositeData.right = new FormAttachment(100, 0);
                    compositeData.top = new FormAttachment(0, 0);
                    compositeData.bottom = new FormAttachment(100, 0);
                    extensionViewDetailComp.setLayoutData(thisFormData);
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
        //
        FormLayout layout = new FormLayout();
        extensionViewDetailComp.setLayout(layout);
        FormData thisFormData = new FormData();
        thisFormData.left = new FormAttachment(0, 0);
        thisFormData.right = new FormAttachment(100, 0);
        thisFormData.top = new FormAttachment(0, 0);
        thisFormData.bottom = new FormAttachment(100, 0);
        setLayoutData(thisFormData);

        Composite composite = new Composite(extensionViewDetailComp, SWT.NONE);

        FormData fd_composite = new FormData();
        fd_composite.top = new FormAttachment(0);
        fd_composite.left = new FormAttachment(0);
        fd_composite.bottom = new FormAttachment(100);
        fd_composite.right = new FormAttachment(100);
        composite.setLayoutData(fd_composite);
        composite.setLayout(new FormLayout());

        Composite composite_1 = new Composite(composite, SWT.NONE);
        composite_1.setLayout(new FormLayout());
        FormData fd_composite_1 = new FormData();
        fd_composite_1.bottom = new FormAttachment(100, -12);
        fd_composite_1.right = new FormAttachment(0, 907);
        fd_composite_1.left = new FormAttachment(0, 10);
        composite_1.setLayoutData(fd_composite_1);

        Button btnNewButton = widgetFactory.createButton(composite, Messages.getString("MyExtensionsComposite.Form.Return"),
                SWT.CENTER);
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
        fd_lblNewLabel_4.right = new FormAttachment(text, 128, SWT.RIGHT);
        fd_lblNewLabel_4.left = new FormAttachment(text, 67);
        fd_lblNewLabel_4.top = new FormAttachment(lblNewLabel_3, 22);
        lblNewLabel_4.setLayoutData(fd_lblNewLabel_4);
        lblNewLabel_4.setText(getRate(componentExtension.getRate()));

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

        final Link link_1 = new Link(group_2, SWT.NONE);
        FormData fd_link_1 = new FormData();
        fd_link_1.top = new FormAttachment(0);
        fd_link_1.right = new FormAttachment(100, -10);
        link_1.setLayoutData(fd_link_1);
        link_1.setText("<a>Write a review</a>");

        Label lblNewLabel_5 = new Label(group_2, SWT.NONE);
        FormData fd_lblNewLabel_5 = new FormData();
        fd_lblNewLabel_5.top = new FormAttachment(0, 21);
        fd_lblNewLabel_5.left = new FormAttachment(0, 10);
        lblNewLabel_5.setLayoutData(fd_lblNewLabel_5);
        if (componentExtension.getReviews() != null && !componentExtension.getReviews().isEmpty()) {
            lblNewLabel_5.setText(getRate(componentExtension.getReviews().get(0).getReviewrate()));
        } else {
            lblNewLabel_5.setText(getRate("0"));
        }

        Label lblNewLabel_6 = new Label(group_2, SWT.V_SCROLL);
        FormData fd_lblNewLabel_6 = new FormData();
        fd_lblNewLabel_6.bottom = new FormAttachment(lblNewLabel_5, 64, SWT.BOTTOM);
        fd_lblNewLabel_6.right = new FormAttachment(link_1, 0, SWT.RIGHT);
        fd_lblNewLabel_6.top = new FormAttachment(lblNewLabel_5, 6);
        fd_lblNewLabel_6.left = new FormAttachment(lblNewLabel_5, 0, SWT.LEFT);
        lblNewLabel_6.setLayoutData(fd_lblNewLabel_6);
        if (componentExtension.getReviews() != null && !componentExtension.getReviews().isEmpty()) {
            lblNewLabel_6.setText(componentExtension.getReviews().get(0).getComment());
        } else {
            lblNewLabel_5.setText("");
        }

        FormData fd_btnNewButton = new FormData();
        fd_btnNewButton.bottom = new FormAttachment(composite_1, -6);
        fd_btnNewButton.left = new FormAttachment(0, 21);
        btnNewButton.setLayoutData(fd_btnNewButton);
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
                new WriteReviewDialog(link_1.getShell(), SWT.NONE).open();
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

    private static String getRate(String rate) {
        int num = 0;
        num = Integer.parseInt(rate.substring(0, 1));
        String star = "";
        for (int i = num; i > 0; i--) {
            star = star + ExchangeUtils.strRateTwo;
        }
        for (int j = 5 - num; j > 0; j--) {
            star = star + ExchangeUtils.strRateOne;
        }
        return star;
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
            default:
                return "";
            }
        }
        return temp;
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

/**
 * 
 * DOC hcyi class global comment. Detailled comment
 */
class WriteReviewDialog extends Dialog {

    protected Object result;

    protected Shell shell;

    public final static String strRateOne = "☆";

    public final static String strRateTwo = "★";

    /**
     * Create the dialog.
     * 
     * @param parent
     * @param style
     */
    public WriteReviewDialog(Shell parent, int style) {
        super(parent, style);
        setText("Write an Review");
    }

    /**
     * Open the dialog.
     * 
     * @return the result
     */
    public Object open() {
        createContents();
        shell.open();
        shell.layout();
        Display display = getParent().getDisplay();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        return result;
    }

    /**
     * Create contents of the dialog.
     */
    private void createContents() {
        shell = new Shell(getParent(), SWT.DIALOG_TRIM);
        shell.setSize(450, 300);
        shell.setText(getText());

        // title
        Text titleText = new Text(shell, SWT.BORDER);
        FormData data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(10, 0);
        data.top = new FormAttachment(0, ITabbedPropertyConstants.VSPACE);
        titleText.setLayoutData(data);

        Label titleLabel = new Label(shell, SWT.NONE);
        data = new FormData();
        data.left = new FormAttachment(0, AbstractPropertySection.STANDARD_LABEL_WIDTH);
        data.right = new FormAttachment(50, 0);
        data.top = new FormAttachment(titleText, ITabbedPropertyConstants.VSPACE);
        titleLabel.setLayoutData(data);
        titleLabel.setText("Title");

        // Rate
        Label rateLabel1 = new Label(shell, SWT.NONE);
        data = new FormData();
        data.left = new FormAttachment(0, AbstractPropertySection.STANDARD_LABEL_WIDTH);
        data.right = new FormAttachment(100, 0);
        data.top = new FormAttachment(titleText, ITabbedPropertyConstants.VSPACE);
        rateLabel1.setLayoutData(data);
        rateLabel1.setText("☆");

        Label rateLabel2 = new Label(shell, SWT.NONE);
        data = new FormData();
        data.left = new FormAttachment(0, AbstractPropertySection.STANDARD_LABEL_WIDTH);
        data.right = new FormAttachment(100, 0);
        data.top = new FormAttachment(rateLabel1, ITabbedPropertyConstants.VSPACE);
        rateLabel2.setLayoutData(data);
        rateLabel2.setText("☆");

        Label rateLabel3 = new Label(shell, SWT.NONE);
        data = new FormData();
        data.left = new FormAttachment(0, AbstractPropertySection.STANDARD_LABEL_WIDTH);
        data.right = new FormAttachment(100, 0);
        data.top = new FormAttachment(rateLabel2, ITabbedPropertyConstants.VSPACE);
        rateLabel3.setLayoutData(data);
        rateLabel3.setText("☆");

        Label rateLabel4 = new Label(shell, SWT.NONE);
        data = new FormData();
        data.left = new FormAttachment(0, AbstractPropertySection.STANDARD_LABEL_WIDTH);
        data.right = new FormAttachment(100, 0);
        data.top = new FormAttachment(rateLabel3, ITabbedPropertyConstants.VSPACE);
        rateLabel4.setLayoutData(data);
        rateLabel4.setText("☆");

        Label rateLabel5 = new Label(shell, SWT.NONE);
        data = new FormData();
        data.left = new FormAttachment(0, AbstractPropertySection.STANDARD_LABEL_WIDTH);
        data.right = new FormAttachment(100, 0);
        data.top = new FormAttachment(rateLabel4, ITabbedPropertyConstants.VSPACE);
        rateLabel5.setLayoutData(data);
        rateLabel5.setText("☆");

        // Description
        Text descriptionText = new Text(shell, SWT.MULTI | SWT.V_SCROLL);
        data = new FormData();
        data.left = new FormAttachment(0, AbstractPropertySection.STANDARD_LABEL_WIDTH);
        data.right = new FormAttachment(100, 0);
        data.top = new FormAttachment(rateLabel5, ITabbedPropertyConstants.VSPACE);
        data.height = 4 * descriptionText.getLineHeight();
        descriptionText.setLayoutData(data);

        Label descriptionLabel = new Label(shell, SWT.NONE);
        data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(descriptionText, -ITabbedPropertyConstants.HSPACE);
        data.top = new FormAttachment(descriptionText, 0, SWT.TOP);
        descriptionLabel.setLayoutData(data);
        descriptionLabel.setText("Description");

        Button btnNewButton = new Button(shell, SWT.NONE);
        data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(descriptionText, -ITabbedPropertyConstants.HSPACE);
        data.top = new FormAttachment(descriptionText, 0, SWT.TOP);
        descriptionLabel.setLayoutData(data);
        btnNewButton.setText("OK");
    }
}
