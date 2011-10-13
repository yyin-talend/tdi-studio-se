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
package org.talend.designer.components.exchange.ui.htmlcontent;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import org.eclipse.emf.common.util.EList;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.intro.config.IIntroContentProviderSite;
import org.eclipse.ui.intro.config.IIntroXHTMLContentProvider;
import org.talend.designer.components.exchange.model.AvailableExtensionViewDetail;
import org.talend.designer.components.exchange.model.ComponentExtension;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * DOC talend class global comment. Detailled comment
 */
public class ExchangeContentProvider implements IIntroXHTMLContentProvider {

    private final String LEFT_NAME_PART = "LEFT_NAME_PART";

    private final String COMPONENT_DESCRIPTION = "COMPONENT_DESCRIPTION";

    private final String COMPONENT_IMANGE = "COMPONENT_IMANGE";

    private final String RATE_IMANGE = "RATE_IMANGE";

    private final String INSTALL_COMPONENT = "INSTALL_COMPONENT";

    private final String EXTENSION_REVIEWS = "EXTENSION_REVIEWS";

    private final String WRITE_REVIEWS = "WRITE_REVIEWS";

    protected DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    private ComponentExtension componentExtension;

    public void init(IIntroContentProviderSite site) {
    }

    public void createContent(String id, PrintWriter out) {
    }

    public void createContent(String id, Composite parent, FormToolkit toolkit) {
    }

    public void dispose() {
    }

    public void createContent(String id, Element parent) {
        if (componentExtension != null) {
            Document dom = parent.getOwnerDocument();
            if (LEFT_NAME_PART.equals(id)) {
                Element span = dom.createElement("span");
                span.setAttribute("class", "style_1 style_2 style_3");
                span.appendChild(dom.createTextNode(componentExtension.getLabel()));
                parent.appendChild(span);
                parent.appendChild(dom.createElement("br"));

                span = dom.createElement("span");
                span.appendChild(dom.createTextNode("Version " + componentExtension.getVersionExtension()));
                parent.appendChild(span);
                parent.appendChild(dom.createElement("br"));

                span = dom.createElement("span");
                span.appendChild(dom.createTextNode(formatter.format(componentExtension.getPublicationDate())));
                parent.appendChild(span);
            } else if (COMPONENT_DESCRIPTION.equals(id)) {
                parent.appendChild(dom.createTextNode(componentExtension.getDescription()));
            } else if (RATE_IMANGE.equals(id)) {
                String rateImage = getRateImage(componentExtension.getRate());
                Element imgElem = dom.createElement("img");
                imgElem.setAttribute("src", rateImage);
                parent.appendChild(imgElem);
            } else if (INSTALL_COMPONENT.equals(id)) {
                Element hyperlink = dom.createElement("a");
                String url = "http://org.eclipse.ui.intro/runAction?pluginId=org.talend.designer.components.exchange&"
                        + "class=org.talend.designer.components.exchange.ui.actions.DownloadComponenentsAction&"
                        + "id=org.talend.designer.components.exchange.ui.actions.DownloadComponenentsAction";
                hyperlink.setAttribute("href", url);

                Element imgElem = dom.createElement("img");
                imgElem.setAttribute("src", "imgs/install.png");
                hyperlink.appendChild(imgElem);

                parent.appendChild(hyperlink);
            } else if (EXTENSION_REVIEWS.equals(id)) {
                EList<AvailableExtensionViewDetail> reviews = componentExtension.getReviews();
                for (AvailableExtensionViewDetail detail : reviews) {
                    String desc = " " + detail.getTitle() + "\n\t" + detail.getComment();
                    String image = getRateImage(detail.getReviewrate());
                    Element imgElem = dom.createElement("img");
                    imgElem.setAttribute("src", image);
                    parent.appendChild(imgElem);
                    parent.appendChild(dom.createTextNode(desc));
                    parent.appendChild(dom.createElement("br"));
                }
            } else if (WRITE_REVIEWS.equals(id)) {
                Element hyperlink = dom.createElement("a");
                String url = "http://org.eclipse.ui.intro/runAction?pluginId=org.talend.designer.components.exchange&"
                        + "class=org.talend.designer.components.exchange.ui.htmlcontent.ExchangeUrlAction&"
                        + "id=org.talend.designer.components.exchange.ui.htmlcontent.ExchangeUrlAction&" + "type=EDIT_REVIEWS";
                hyperlink.setAttribute("href", url);
                hyperlink.appendChild(dom.createTextNode("write a review"));
                parent.appendChild(hyperlink);
            }
        }
    }

    public void setComponentExtension(ComponentExtension componentExtension) {
        this.componentExtension = componentExtension;
    }

    private String getRateImage(String rate) {
        int num = 0;
        if (rate != null && !"".equals(rate)) {
            double rates = Double.parseDouble(rate);
            DecimalFormat df = new DecimalFormat("#");
            num = Integer.parseInt(df.format(rates));
            switch (num) {
            case 0:
                return "imgs/star.jpg";
            case 1:
                return "imgs/star1.jpg";
            case 2:
                return "imgs/star2.jpg";
            case 3:
                return "imgs/star3.jpg";
            case 4:
                return "imgs/star4.jpg";
            case 5:
                return "imgs/star5.jpg";

            default:
                return "imgs/star.jpg";
            }
        }
        return "imgs/star.jpg";
    }
}
