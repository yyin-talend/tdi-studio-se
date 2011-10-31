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
import java.util.List;
import java.util.Properties;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
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
import org.talend.core.model.process.EComponentCategory;
import org.talend.designer.components.exchange.ExchangePlugin;
import org.talend.designer.components.exchange.i18n.Messages;
import org.talend.designer.components.exchange.model.ComponentExtension;
import org.talend.designer.components.exchange.ui.htmlcontent.DownloadExtensionProvider;
import org.talend.designer.components.exchange.util.ActionHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * DOC hcyi class global comment. Detailled comment
 */
public class DownloadedExtensionsComposite extends ExchangeComposite {

    private ISelection selection;

    private IWizardPage wizardPage = null;

    private Table itemTable;

    private static final String ITEM_EDITOR_KEY = "ITEM_EDITOR_KEY"; //$NON-NLS-1$

    Button operateStatusBtn;

    Composite Comp;

    final StackLayout stackLayout;

    /**
     * DOC hcyi DownloadedExtensionsComposite constructor comment.
     * 
     * @param parent
     * @param style
     */
    public DownloadedExtensionsComposite(Composite parent, int styles, TabbedPropertySheetWidgetFactory factory,
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

        Comp = new Composite(this, SWT.NONE);
        FormData data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(100, 0);
        data.top = new FormAttachment(0, 0);
        data.bottom = new FormAttachment(100, 0);
        Comp.setLayoutData(data);
        stackLayout = new StackLayout();
        Comp.setLayout(stackLayout);

        //
        if (fExtensions != null && !fExtensions.isEmpty()) {
            updateInstalledExtensions(fExtensions);
        }

        Display.getDefault().asyncExec(new Runnable() {

            public void run() {
                IAction action1 = ActionHelper.getRefreshComponenentsAction();
                if (action1 != null) {
                    action1.setEnabled(false);
                }
            }
        });

        createDownloadExtensionComposite();
    }

    public void review() {
        createDownloadExtensionComposite();
    }

    public void createDownloadExtensionComposite() {
        try {
            Browser tableBrowser = new Browser(Comp, SWT.NONE);
            tableBrowser.addLocationListener(new BrowserDynamicPartLocationListener());
            tableBrowser.setLayoutData(new GridData(GridData.FILL_BOTH));
            String content = "";
            URL entry = ExchangePlugin.getDefault().getBundle().getEntry("content/DownloadExtensionComposite.html");
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
            tableBrowser.setText(content);
            stackLayout.topControl = tableBrowser;
            Comp.layout();
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
                if (providerClass instanceof DownloadExtensionProvider) {
                    ((DownloadExtensionProvider) providerClass).setfInstalledExtensions(fInstalledExtensions);
                    ((DownloadExtensionProvider) providerClass).setfDownloadedExtensions(fDownloadedExtensions);
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

    public void updateInstalledExtensions(List<ComponentExtension> extensions) {
        for (ComponentExtension ext : extensions) {
            fInstalledExtensions.put(ext.getIdExtension(), ext);
        }
        loadDownloadedExtensionsFromFile();
        // removeItemElements(fInstalledExtensions);
        // addItemElements(fInstalledExtensions);
        refresh();
    }

    private String getOperateStatus(ComponentExtension object) {
        //
        if (object != null && fDownloadedExtensions.containsKey(object.getIdExtension())) {
            if (object.getVersionExtension().equals(object.getDownloadedVersion())) {
                operateStatusBtn.setEnabled(false);
                return Messages.getString("DownloadedExtensionsComposite.installedOperateStatus");
            } else {
                return "  " + Messages.getString("DownloadedExtensionsComposite.updateOperateStatus") + "  ";
            }
        }
        return "  " + Messages.getString("DownloadedExtensionsComposite.installOperateStatus") + "  ";
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.properties.tab.IDynamicProperty#getSection()
     */
    public EComponentCategory getSection() {
        return EComponentCategory.DOWNLOADEDEXTENSIONS;
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
