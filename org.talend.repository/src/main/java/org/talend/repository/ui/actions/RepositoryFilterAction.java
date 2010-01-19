// ============================================================================
//
// Copyright (C) 2006-2009 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.ui.actions;

import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.jface.window.Window;
import org.talend.repository.ui.dialog.RepositoryFilterDialog;

/**
 * DOC wchen class global comment. Detailled comment
 */
public class RepositoryFilterAction extends RepositoryMenuAction {

    public RepositoryFilterAction() {
        this.setText("&Filters...");
        this.setToolTipText("Filters...");
        setImageDescriptor(JavaPluginImages.DESC_ELCL_FILTER);
        setDisabledImageDescriptor(JavaPluginImages.DESC_DLCL_FILTER);
    }

    @Override
    public void run() {
        RepositoryFilterDialog dialog = new RepositoryFilterDialog(view);
        if (dialog.open() == Window.OK) {
            view.getViewer().refresh();
        }
    }

}
