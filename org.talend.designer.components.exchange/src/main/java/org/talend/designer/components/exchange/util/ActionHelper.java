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
package org.talend.designer.components.exchange.util;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.talend.designer.components.exchange.ui.actions.RefreshComponenentsAction;
import org.talend.designer.components.exchange.ui.views.ExchangeView;

/**
 * 
 * DOC hcyi class global comment. Detailled comment
 */
public class ActionHelper {

    public static IAction getRefreshComponenentsAction() {
        ExchangeView exchangeView = ExchangeUtils.getExchangeView();
        if (exchangeView != null) {
            IContributionItem item = exchangeView.getViewSite().getActionBars().getToolBarManager()
                    .find(RefreshComponenentsAction.ID);
            if (item != null && item instanceof IContributionItem) {
                IAction action = ((ActionContributionItem) item).getAction();
                return action;
            }
        }
        return null;
    }
}
