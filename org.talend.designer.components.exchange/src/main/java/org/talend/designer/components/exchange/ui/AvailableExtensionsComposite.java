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

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.internal.intro.impl.html.IIntroHTMLConstants;
import org.eclipse.ui.internal.intro.impl.model.IntroContentProvider;
import org.eclipse.ui.internal.intro.impl.model.loader.ContentProviderManager;
import org.eclipse.ui.internal.intro.impl.model.loader.IntroContentParser;
import org.eclipse.ui.internal.intro.impl.model.util.ModelUtil;
import org.eclipse.ui.intro.config.IIntroContentProviderSite;
import org.eclipse.ui.intro.config.IIntroXHTMLContentProvider;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;
import org.talend.commons.ui.html.BrowserDynamicPartLocationListener;
import org.talend.commons.ui.html.TalendHtmlModelUtil;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.ui.swt.dialogs.ErrorDialogWidthDetailArea;
import org.talend.commons.ui.swt.tableviewer.behavior.AbstractElementFilter;
import org.talend.core.model.process.EComponentCategory;
import org.talend.designer.components.exchange.ExchangePlugin;
import org.talend.designer.components.exchange.i18n.Messages;
import org.talend.designer.components.exchange.model.ComponentExtension;
import org.talend.designer.components.exchange.ui.actions.DownloadComponenentsAction;
import org.talend.designer.components.exchange.ui.htmlcontent.ExchangeContentProvider;
import org.talend.designer.components.exchange.util.ActionHelper;
import org.talend.designer.components.exchange.util.ExchangeUtils;
import org.talend.designer.components.exchange.util.ExchangeWebService;
import org.talend.designer.components.exchange.util.WebserviceStatus;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
            }
        });
    }

    public void updateAvailableExtensions(List<ComponentExtension> extensions) {
        loadDownloadedExtensionsFromFile();
        fAvailableExtensions = extensions;
        // update status of Downloaded extensions
        // checkDownloadedExtensions();
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
            // tableItem.setImage(2, getRateImage(object.getRate()));
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
            linkView.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
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
                    extensionViewDetailComp.setLayout(new GridLayout());
                    //
                    createExtensionViewDetailControl(getSelectedExtension());
                    stackLayout.topControl = extensionViewDetailComp;
                    listExtensonsComp.layout();
                }

            });

            final Link linkDownload = new Link(operateComposit, SWT.LEFT);
            linkDownload.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
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

            //
            Composite operateRateComposit = new Composite(itemTable, SWT.CENTER);
            operateRateComposit.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
            FormData operFormData = new FormData();
            operFormData.left = new FormAttachment(0, 0);
            operFormData.right = new FormAttachment(100, 0);
            operFormData.top = new FormAttachment(0, 0);
            operFormData.bottom = new FormAttachment(100, 0);
            operateRateComposit.setLayoutData(operFormData);
            operateRateComposit.setLayout(new FormLayout());

            CLabel operateRateCLabel = new CLabel(operateRateComposit, SWT.CENTER);
            operateRateCLabel.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
            operateRateCLabel.setImage(getRateImage(object.getRate()));
            operateRateCLabel.setLayoutData(operFormData);
            operateRateCLabel.setData(tableItem);

            TableEditor rateEditor = new TableEditor(itemTable);
            rateEditor.minimumWidth = itemTable.getColumn(2).getWidth();
            rateEditor.setEditor(operateRateComposit, tableItem, 2);
            tableItem.setData(ITEM_EDITOR_KEY, new TableEditor[] { rateEditor });
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
        try {
            Browser browser = new Browser(extensionViewDetailComp, SWT.NONE);
            browser.addLocationListener(new BrowserDynamicPartLocationListener());
            browser.setLayoutData(new GridData(GridData.FILL_BOTH));
            String content = "";
            URL entry = ExchangePlugin.getDefault().getBundle().getEntry("content/exchange.html");
            if (entry != null) {
                entry = FileLocator.toFileURL(entry);
                String result = entry.toExternalForm();
                if (result.startsWith("file:/")) { //$NON-NLS-1$
                    if (result.startsWith("file:///") == false) { //$NON-NLS-1$
                        result = "file:///" + result.substring(6); //$NON-NLS-1$
                    }
                }

                IntroContentParser parser = new IntroContentParser(result);
                Document dom = parser.getDocument();
                if (dom != null) {
                    resolveDynamicContent(dom, null);
                    // insert base meta-tag,
                    ModelUtil.insertBase(dom, ModelUtil.getParentFolderOSString(content));

                    // resolve all relative resources relative to content file. Do it before
                    // inserting shared style to enable comparing fully qualified styles.
                    TalendHtmlModelUtil.updateResourceAttributes(dom.getDocumentElement(), "/content/", ExchangePlugin
                            .getDefault().getBundle());

                    content = IntroContentParser.convertToString(dom);
                }
            }
            browser.setText(content);
        } catch (IOException e) {
            ExceptionHandler.process(e);
        }

    }

    private Document resolveDynamicContent(Document dom, IIntroContentProviderSite site) {
        NodeList contentProviders = dom.getElementsByTagNameNS("*", //$NON-NLS-1$
                IntroContentProvider.TAG_CONTENT_PROVIDER);

        Node[] nodes = ModelUtil.getArray(contentProviders);
        for (int i = 0; i < nodes.length; i++) {
            Element contentProviderElement = (Element) nodes[i];
            IntroContentProvider provider = new IntroContentProvider(contentProviderElement, ExchangePlugin.getDefault()
                    .getBundle());
            IIntroXHTMLContentProvider providerClass = (IIntroXHTMLContentProvider) ContentProviderManager.getInst()
                    .getContentProvider(provider);
            if (providerClass == null)
                providerClass = (IIntroXHTMLContentProvider) ContentProviderManager.getInst().createContentProvider(provider,
                        site);

            if (providerClass != null) {
                if (providerClass instanceof ExchangeContentProvider) {
                    ((ExchangeContentProvider) providerClass).setComponentExtension(getSelectedExtension());
                }

                Properties att = new Properties();
                att.setProperty(IIntroHTMLConstants.ATTRIBUTE_ID, provider.getId());
                Element contentDiv = ModelUtil.createElement(dom, ModelUtil.TAG_DIV, att);
                providerClass.createContent(provider.getId(), contentDiv);
                contentProviderElement.getParentNode().replaceChild(contentDiv, contentProviderElement);

            } else {
                // we couldn't load the content provider, so add any alternate
                // text content if there is any.
                // INTRO: do it. 3.0 intro content style uses text element as
                // alt text. We can load XHTML content here.
            }
        }
        return dom;
    }

    public void returnToFirstPage() {
        stackLayout.topControl = itemTable;
        listExtensonsComp.layout();
    }

    public void editReviews() {
        if (getSelectedExtension() != null) {
            ReviewComponentDialog reviewDialog = new ReviewComponentDialog(getShell());
            if (reviewDialog.open() == IDialogConstants.OK_ID) {
                final String title = reviewDialog.getTitle();
                final String rate = reviewDialog.getRating() + "";
                final String review = reviewDialog.getReview();
                Display.getDefault().asyncExec(new Runnable() {

                    public void run() {
                        if (ExchangeUtils.checkUserAndPassword()) {
                            WebserviceStatus ws = ExchangeWebService.insertReviewService(getSelectedExtension().getIdExtension(),
                                    ExchangeUtils.TYPEEXTENSION, ExchangeUtils.getUserName(), ExchangeUtils.getPasswordHash(),
                                    title, review, rate);
                            if (ws.isResult()) {
                                MessageDialog.openInformation(
                                        getShell(),
                                        Messages.getString("AvailableExtensionsComposite.ViewDetail.WriteReview"), ws.getMessageException()); //$NON-NLS-1$
                            } else {
                                String mainMsg = Messages
                                        .getString("AvailableExtensionsComposite.ViewDetail.InsertionReviewFailure")
                                        + " "
                                        + Messages.getString("AvailableExtensionsComposite.ViewDetail.InsertionReviewFailureTip");
                                new ErrorDialogWidthDetailArea(getShell(), ExchangePlugin.PLUGIN_ID, mainMsg, ws
                                        .getMessageException());
                            }
                        } else {
                            MessageDialog.openInformation(
                                    getShell(),
                                    Messages.getString("AvailableExtensionsComposite.ViewDetail.WriteReview"), Messages.getString("MyExtensionsComposite.Form.checkUserAndPassword")); //$NON-NLS-1$
                        }
                    }
                });
            }
        }

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
