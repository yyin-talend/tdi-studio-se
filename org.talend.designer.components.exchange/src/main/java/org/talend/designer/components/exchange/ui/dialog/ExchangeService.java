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

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.core.model.general.IExchangeService;
import org.talend.designer.components.exchange.i18n.Messages;
import org.talend.designer.components.exchange.ui.views.ExchangeEditorInput;
import org.talend.designer.components.exchange.util.ExchangeUtils;

/**
 * 
 * DOC hcyi class global comment. Detailled comment
 */
public class ExchangeService implements IExchangeService {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.general.IExchangeService#openExchangeDialog()
     */
    public String openExchangeDialog() {
        ImportExchangeDialog dialog = new ImportExchangeDialog(Display.getCurrent().getActiveShell());
        dialog.open();
        return dialog.getSelectFile();
    }

    public void openExchangeEditor() {
        IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        if (activePage != null) {
            if (!ExchangeUtils.checkUserAndPassword()) {
                MessageDialog.openError(null, "Exchange", Messages.getString("Exchange.logon.error"));
                return;
            }

            try {
                ExchangeEditorInput input = new ExchangeEditorInput();
                IEditorPart findEditor = activePage.findEditor(input);
                if (findEditor == null) {
                    activePage.openEditor(input, "org.talend.designer.components.exchange.ui.views.ExchangeView");
                } else {
                    activePage.activate(findEditor);
                }
            } catch (PartInitException e) {
                ExceptionHandler.process(e);
            }
        }

    }

}
