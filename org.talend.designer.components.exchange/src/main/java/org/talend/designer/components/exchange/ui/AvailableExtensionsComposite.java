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
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.internal.intro.impl.html.IIntroHTMLConstants;
import org.eclipse.ui.internal.intro.impl.model.IntroContentProvider;
import org.eclipse.ui.internal.intro.impl.model.loader.ContentProviderManager;
import org.eclipse.ui.internal.intro.impl.model.loader.IntroContentParser;
import org.eclipse.ui.internal.intro.impl.model.util.ModelUtil;
import org.eclipse.ui.intro.config.IIntroContentProviderSite;
import org.eclipse.ui.intro.config.IIntroXHTMLContentProvider;
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
import org.talend.designer.components.exchange.ui.htmlcontent.AvailableCompositeProvider;
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

    private List<ComponentExtension> fAvailableExtensionsFilter = new ArrayList<ComponentExtension>();

    final StackLayout stackLayout;

    private AbstractElementFilter<ComponentExtension> elementFilter;

    Composite addToolBarComp, listExtensonsComp, extensionViewDetailComp, tableComp;

    private CCombo addTosVersionFilterCombo;

    private Map<Integer, Image> exchangeStarImageMap = new HashMap<Integer, Image>();

    private Browser tableBrowser;

    private Browser toolbarBrowser;

    private ComponentExtension selectComponentExtension;

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
        FormData data = new FormData();
        listExtensonsComp = new Composite(this, SWT.NONE);
        data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(100, 0);
        data.top = new FormAttachment(addToolBarComp, 0);
        data.bottom = new FormAttachment(100, 0);
        listExtensonsComp.setLayoutData(data);

        stackLayout = new StackLayout();
        listExtensonsComp.setLayout(stackLayout);

        if (fExtensions != null && !fExtensions.isEmpty()) {
            updateAvailableExtensions(fExtensions);
        }
        Display.getDefault().asyncExec(new Runnable() {

            public void run() {
                IAction action1 = ActionHelper.getRefreshComponenentsAction();
                if (action1 != null) {
                    action1.setEnabled(true);
                }
            }
        });
        createAvailableComposite(null);
    }

    public void updateAvailableExtensions(List<ComponentExtension> extensions) {
        loadDownloadedExtensionsFromFile();
        fAvailableExtensions = extensions;
        createAvailableComposite(null);
        refresh();
    }

    public void updateItems(String filter) {
        createAvailableComposite(filter);
    }

    public void createToolBarComposite() {
        try {
            toolbarBrowser = new Browser(listExtensonsComp, SWT.NONE);
            toolbarBrowser.addLocationListener(new BrowserDynamicPartLocationListener());
            toolbarBrowser.setLayoutData(new GridData(GridData.FILL_BOTH));
            String content = "";
            URL entry = ExchangePlugin.getDefault().getBundle().getEntry("content/AvailableComposite.html");
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
                    resolveDynamicContent(dom, null, null, null);
                    // insert base meta-tag,
                    ModelUtil.insertBase(dom, ModelUtil.getParentFolderOSString(content));

                    // resolve all relative resources relative to content file. Do it before
                    // inserting shared style to enable comparing fully qualified styles.
                    TalendHtmlModelUtil.updateResourceAttributes(dom.getDocumentElement(), "/content/", ExchangePlugin
                            .getDefault().getBundle());

                    content = IntroContentParser.convertToString(dom);
                }
            }
            toolbarBrowser.setText(content);
        } catch (IOException e) {
            ExceptionHandler.process(e);
        }
    }

    public void createAvailableComposite(String filter) {
        try {
            tableBrowser = new Browser(listExtensonsComp, SWT.NONE);
            tableBrowser.addLocationListener(new BrowserDynamicPartLocationListener());
            tableBrowser.setLayoutData(new GridData(GridData.FILL_BOTH));
            String content = "";
            URL entry = ExchangePlugin.getDefault().getBundle().getEntry("content/AvailableComposite.html");
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
                    resolveDynamicContent(dom, null, null, filter);
                    // insert base meta-tag,
                    ModelUtil.insertBase(dom, ModelUtil.getParentFolderOSString(content));

                    // resolve all relative resources relative to content file. Do it before
                    // inserting shared style to enable comparing fully qualified styles.
                    TalendHtmlModelUtil.updateResourceAttributes(dom.getDocumentElement(), "/content/", ExchangePlugin
                            .getDefault().getBundle());

                    content = IntroContentParser.convertToString(dom);
                }
            }
            tableBrowser.setText(content);
            stackLayout.topControl = tableBrowser;
            listExtensonsComp.layout();
        } catch (IOException e) {
            ExceptionHandler.process(e);
        }
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
                    resolveDynamicContent(dom, null, componentExtension, null);
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

    private Document resolveDynamicContent(Document dom, IIntroContentProviderSite site,
            final ComponentExtension componentExtension, String filter) {
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
                    if (componentExtension != null) {
                        ((ExchangeContentProvider) providerClass).setComponentExtension(componentExtension);
                    } else {
                        ((ExchangeContentProvider) providerClass).setComponentExtension(getSelectedExtension());
                    }
                }
                if (providerClass instanceof AvailableCompositeProvider) {
                    if (filter == null) {
                        ((AvailableCompositeProvider) providerClass).setfAvailableExtensions(fAvailableExtensions);
                    } else {
                        fAvailableExtensionsFilter.clear();
                        for (ComponentExtension Extension : fAvailableExtensions) {
                            if (Extension.getLabel().toUpperCase().contains(filter.toUpperCase())) {
                                fAvailableExtensionsFilter.add(Extension);
                            }
                        }
                        ((AvailableCompositeProvider) providerClass).setfAvailableExtensions(fAvailableExtensionsFilter);
                    }
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
        stackLayout.topControl = tableBrowser;
        listExtensonsComp.layout();
    }

    public void openDetailPage(ComponentExtension extension) {
        setSelectedExtension(extension);
        extensionViewDetailComp = widgetFactory.createFlatFormComposite(listExtensonsComp);
        FormData compositeData = new FormData();
        compositeData.left = new FormAttachment(0, 0);
        compositeData.right = new FormAttachment(100, 0);
        compositeData.top = new FormAttachment(0, 0);
        compositeData.bottom = new FormAttachment(100, 0);
        extensionViewDetailComp.setLayoutData(compositeData);
        extensionViewDetailComp.setLayout(new GridLayout());
        createExtensionViewDetailControl(extension);
        stackLayout.topControl = extensionViewDetailComp;
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
