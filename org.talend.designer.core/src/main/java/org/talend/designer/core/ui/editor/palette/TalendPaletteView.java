// ============================================================================
//
// Copyright (C) 2006-2010 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.core.ui.editor.palette;

import org.eclipse.gef.ui.views.palette.PaletteView;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.widgets.Composite;
import org.talend.repository.ui.actions.ShowFavoriteAction;
import org.talend.repository.ui.actions.ShowStandardAction;

/**
 * DOC hwang class global comment. Detailled comment
 */
public class TalendPaletteView extends PaletteView {

    public static final String ID = "org.eclipse.gef.ui.palette_view"; //$NON-NLS-1$

    @Override
    public void createPartControl(Composite parent) {
        super.createPartControl(parent);
        IToolBarManager toolMana = getViewSite().getActionBars().getToolBarManager();
        ShowStandardAction showSt = ShowStandardAction.getInstance();
        ShowFavoriteAction showSf = ShowFavoriteAction.getInstance();
        showSt.setShowF(showSf);
        showSf.setShowS(showSt);
        toolMana.add(showSt);
        toolMana.add(showSf);
        if (showSf.state == true) {
            showSt.doSetEnable();
        }
    }

}
