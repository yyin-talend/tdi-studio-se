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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.internal.intro.impl.html.IIntroHTMLConstants;
import org.eclipse.ui.internal.intro.impl.model.IntroContentProvider;
import org.eclipse.ui.internal.intro.impl.model.loader.ContentProviderManager;
import org.eclipse.ui.internal.intro.impl.model.loader.IntroContentParser;
import org.eclipse.ui.internal.intro.impl.model.util.ModelUtil;
import org.eclipse.ui.intro.config.IIntroContentProviderSite;
import org.eclipse.ui.intro.config.IIntroXHTMLContentProvider;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;
import org.talend.commons.ui.html.TalendHtmlModelUtil;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.core.model.process.EComponentCategory;
import org.talend.designer.components.exchange.ExchangePlugin;
import org.talend.designer.components.exchange.model.Category;
import org.talend.designer.components.exchange.model.ComponentExtension;
import org.talend.designer.components.exchange.model.VersionRevision;
import org.talend.designer.components.exchange.ui.htmlcontent.HtmlContentUtil;
import org.talend.designer.components.exchange.ui.htmlcontent.MyExchangeContentProvider;
import org.talend.designer.components.exchange.ui.htmlcontent.MyExtensionLocationListener;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * DOC hcyi class global comment. Detailled comment
 */
public class MyExtensionsComposite extends ExchangeComposite {

    private ISelection selection;

    private IWizardPage wizardPage = null;

    private Table itemTable;

    private static final String[] FILE_EXPORT_MASK = { "*.zip", "*.*" }; //$NON-NLS-1$ //$NON-NLS-2$

    final StackLayout stackLayout;

    private Composite listMyExtensonsComp, formComp;

    private Text selectUploadFileText;

    private String previouslyBrowsedArchive = ""; //$NON-NLS-1$

    private List<VersionRevision> fVersionRevisions = new ArrayList<VersionRevision>();

    private List<Category> fCategorys = new ArrayList<Category>();

    private Map<Integer, Image> imageMaps = new HashMap<Integer, Image>();

    /**
     * DOC hcyi MyExtensionsComposite constructor comment.
     * 
     * @param parent
     * @param style
     */
    public MyExtensionsComposite(Composite parent, int styles, TabbedPropertySheetWidgetFactory factory,
            List<ComponentExtension> fExtensions, List<VersionRevision> fVersionRevisionsTemp, List<Category> fCategorysTemp) {
        super(parent, styles, factory);
        FormLayout layout = new FormLayout();
        setLayout(layout);
        FormData thisFormData = new FormData();
        thisFormData.left = new FormAttachment(0, 0);
        thisFormData.right = new FormAttachment(100, 0);
        thisFormData.top = new FormAttachment(0, 0);
        thisFormData.bottom = new FormAttachment(100, 0);
        setLayoutData(thisFormData);
        listMyExtensonsComp = new Composite(this, SWT.NONE);
        FormData compositeData = new FormData();
        compositeData.left = new FormAttachment(0, 0);
        compositeData.right = new FormAttachment(100, 0);
        compositeData.top = new FormAttachment(0, 0);
        compositeData.bottom = new FormAttachment(100, 0);
        listMyExtensonsComp.setLayoutData(compositeData);

        stackLayout = new StackLayout();
        listMyExtensonsComp.setLayout(stackLayout);

        if (fExtensions != null && !fExtensions.isEmpty()) {
            for (ComponentExtension ext : fExtensions) {
                fContributedExtensions.put(ext.getIdExtension(), ext);
            }
        }
        if (fVersionRevisionsTemp != null && !fVersionRevisionsTemp.isEmpty()) {
            fVersionRevisions = fVersionRevisionsTemp;
        }
        if (fCategorysTemp != null && !fCategorysTemp.isEmpty()) {
            fCategorys = fCategorysTemp;
        }

        createListMyExtensionComposite();
    }

    private void createFormComposite() {
        formComp = widgetFactory.createFlatFormComposite(listMyExtensonsComp);
        FormData data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(100, 0);
        data.top = new FormAttachment(0, 0);
        data.bottom = new FormAttachment(100, 0);
        formComp.setLayoutData(data);
        formComp.setLayout(new GridLayout());

    }

    public void createListMyExtensionComposite() {
        createFormComposite();
        Browser browser = new Browser(formComp, SWT.NONE);
        browser.addLocationListener(new MyExtensionLocationListener());

        browser.setLayoutData(new GridData(GridData.FILL_BOTH));
        try {
            String content = "";
            Document dom = HtmlContentUtil.readHtmlToDocument("content/listMyExtension.html");
            if (dom != null) {
                resolveDynamicContent(dom, null);
                // insert base meta-tag,
                ModelUtil.insertBase(dom, ModelUtil.getParentFolderOSString(content));

                // resolve all relative resources relative to content file. Do it before
                // inserting shared style to enable comparing fully qualified styles.
                TalendHtmlModelUtil.updateResourceAttributes(dom.getDocumentElement(), "/content/", ExchangePlugin.getDefault()
                        .getBundle());

                content = IntroContentParser.convertToString(dom);
                browser.setText(content);
                // String readHtmlContent = HtmlContentUtil.readHtmlContent("content/testaaa.html");
                // browser.setUrl(readHtmlContent);
            }
        } catch (IOException e1) {
            ExceptionHandler.process(e1);
        }
        stackLayout.topControl = formComp;
        listMyExtensonsComp.layout();
    }

    private void addItemElements(Map<String, ComponentExtension> elements) {
        if (elements == null || elements.isEmpty()) {
            return;
        }
        itemTable.setRedraw(false);

        Iterator ite = elements.entrySet().iterator();
        while (ite.hasNext()) {
            Map.Entry<String, ComponentExtension> entry = (Entry<String, ComponentExtension>) ite.next();
            ComponentExtension object = entry.getValue();

            final TableItem tableItem = new TableItem(itemTable, SWT.NONE);
            tableItem.setData(object);

            // Extension Name
            tableItem.setText(0, object.getLabel());
            tableItem.setText(1, object.getLastVersionAvailable());
            tableItem.setText(2, formatter.format(object.getPublicationDate()));
        }
        itemTable.setRedraw(true);
    }

    public void getFormComposite(int operateStatus, ComponentExtension componentExtension) {
        //
        if (operateStatus == 0) {
            createUpdateFormComposite(operateStatus, componentExtension);
        } else if (operateStatus == 1) {
            createModifyFormComposite(operateStatus, componentExtension);
        }
    }

    /**
     * 
     * DOC hcyi Comment method "createUpLoadComposite".
     */
    public void createUpLoadFormComposite() {
        createFormComposite();
        Browser browser = new Browser(formComp, SWT.NONE);
        browser.addLocationListener(new MyExtensionLocationListener());

        browser.setLayoutData(new GridData(GridData.FILL_BOTH));
        try {
            String content = "";
            Document dom = HtmlContentUtil.readHtmlToDocument("content/upload.html");
            if (dom != null) {
                // insert base meta-tag,
                ModelUtil.insertBase(dom, ModelUtil.getParentFolderOSString(content));

                // resolve all relative resources relative to content file. Do it before
                // inserting shared style to enable comparing fully qualified styles.
                TalendHtmlModelUtil.updateResourceAttributes(dom.getDocumentElement(), "/content/", ExchangePlugin.getDefault()
                        .getBundle());

                content = IntroContentParser.convertToString(dom);
                browser.setText(content);
                // String readHtmlContent = HtmlContentUtil.readHtmlContent("content/testaaa.html");
                // browser.setUrl(readHtmlContent);
            }
        } catch (IOException e1) {
            ExceptionHandler.process(e1);
        }
        stackLayout.topControl = formComp;
        listMyExtensonsComp.layout();
    }

    /**
     * 
     * DOC hcyi Comment method "createUpdateFormComposite".
     */
    public void createUpdateFormComposite(int operateStatus, final ComponentExtension componentExtension) {
        createFormComposite();
        Browser browser = new Browser(formComp, SWT.NONE);
        browser.addLocationListener(new MyExtensionLocationListener());

        browser.setLayoutData(new GridData(GridData.FILL_BOTH));
        try {
            String content = "";
            Document dom = HtmlContentUtil.readHtmlToDocument("content/update.html");
            if (dom != null) {
                resolveDynamicContent(dom, null);

                // insert base meta-tag,
                ModelUtil.insertBase(dom, ModelUtil.getParentFolderOSString(content));

                // resolve all relative resources relative to content file. Do it before
                // inserting shared style to enable comparing fully qualified styles.
                TalendHtmlModelUtil.updateResourceAttributes(dom.getDocumentElement(), "/content/", ExchangePlugin.getDefault()
                        .getBundle());

                content = IntroContentParser.convertToString(dom);
                browser.setText(content);
                // String readHtmlContent = HtmlContentUtil.readHtmlContent("content/testaaa.html");
                // browser.setUrl(readHtmlContent);
            }
        } catch (IOException e1) {
            ExceptionHandler.process(e1);
        }
        stackLayout.topControl = formComp;
        listMyExtensonsComp.layout();
    }

    /**
     * 
     * DOC hcyi Comment method "createModifyFormComposite".
     */
    public void createModifyFormComposite(int operateStatus, final ComponentExtension componentExtension) {
        createFormComposite();
        Browser browser = new Browser(formComp, SWT.NONE);
        browser.addLocationListener(new MyExtensionLocationListener());

        browser.setLayoutData(new GridData(GridData.FILL_BOTH));
        try {
            String content = "";
            Document dom = HtmlContentUtil.readHtmlToDocument("content/modify.html");
            if (dom != null) {
                resolveDynamicContent(dom, null);

                // insert base meta-tag,
                ModelUtil.insertBase(dom, ModelUtil.getParentFolderOSString(content));

                // resolve all relative resources relative to content file. Do it before
                // inserting shared style to enable comparing fully qualified styles.
                TalendHtmlModelUtil.updateResourceAttributes(dom.getDocumentElement(), "/content/", ExchangePlugin.getDefault()
                        .getBundle());

                content = IntroContentParser.convertToString(dom);
                browser.setText(content);
                // String readHtmlContent = HtmlContentUtil.readHtmlContent("content/testaaa.html");
                // browser.setUrl(readHtmlContent);
            }
        } catch (IOException e1) {
            ExceptionHandler.process(e1);
        }
        stackLayout.topControl = formComp;
        listMyExtensonsComp.layout();
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
                if (providerClass instanceof MyExchangeContentProvider) {
                    ((MyExchangeContentProvider) providerClass).setComponentExtension(getSelectedExtension());
                    ((MyExchangeContentProvider) providerClass).setfContributeExtensions(new ArrayList<ComponentExtension>(
                            fContributedExtensions.values()));
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

    public void returnMyExtensionsComposite() {
        // stackLayout.topControl = itemTable;
        // listMyExtensonsComp.layout();
        // addNewExtensonBtn.setEnabled(true);

        createListMyExtensionComposite();
    }

    public void myExtensionModifyOrUploadVersion(int operateStatus, ComponentExtension componentExtension) {
        getFormComposite(operateStatus, componentExtension);
    }

    public void myExtensionAddNew() {
        createUpLoadFormComposite();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.properties.tab.IDynamicProperty#getSection()
     */
    public EComponentCategory getSection() {
        return EComponentCategory.MYEXTENSIONS;
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
