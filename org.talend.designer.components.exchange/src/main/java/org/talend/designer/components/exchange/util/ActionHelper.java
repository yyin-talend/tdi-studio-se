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

import org.eclipse.ui.PlatformUI;
import org.talend.designer.components.exchange.ui.views.ExchangeView;

/**
 * 
 * DOC hcyi class global comment. Detailled comment
 */
public class ActionHelper {

    public static ExchangeView getExchangeView() {
        return (ExchangeView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                .findView("org.talend.designer.components.exchange.ui.views.ExchangeView"); //$NON-NLS-1$
    }
}
