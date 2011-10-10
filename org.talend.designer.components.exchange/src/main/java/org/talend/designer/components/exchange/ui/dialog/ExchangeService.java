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
package org.talend.designer.components.exchange.ui.dialog;

import org.eclipse.swt.widgets.Display;
import org.talend.core.model.general.IExchangeService;

/**
 * 
 * DOC hcyi class global comment. Detailled comment
 */
public class ExchangeService implements IExchangeService {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.general.IEcosystemService#openEcosystemDialog()
     */
    public String openExchangeDialog() {
        ImportExchangeDialog dialog = new ImportExchangeDialog(Display.getCurrent().getActiveShell());
        dialog.open();
        return dialog.getSelectFile();
    }

}
