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

import java.util.Properties;

import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.intro.IIntroSite;
import org.eclipse.ui.intro.config.IIntroAction;
import org.talend.designer.components.exchange.ui.views.ExchangeView;

/**
 * DOC talend class global comment. Detailled comment
 */
public class ExchangeUrlAction implements IIntroAction {

    public static final String RETURN_TO_FIRST_PAGE = "RETURN_TO_FIRST_PAGE";

    public static final String EDIT_REVIEWS = "EDIT_REVIEWS";

    public static final String KEY_TYPE = "type";

    public void run(IIntroSite site, Properties params) {
        if (params != null) {
            IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
            if (page != null) {
                IViewPart view = page.findView(ExchangeView.ID);
                if (view != null) {
                    ExchangeView exchangeView = (ExchangeView) view;
                    if (RETURN_TO_FIRST_PAGE.equals(params.get(KEY_TYPE))) {
                        exchangeView.returnAvailableExtensionsCompositeToFirstPage();
                    } else if (EDIT_REVIEWS.equals(params.get(KEY_TYPE))) {
                        exchangeView.editAvailableExtensionReviews();
                    }
                }

            }
        }

    }

}
