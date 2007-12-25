// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.core.ui.views.properties;

import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.model.process.Element;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class ComponentSettings {

    public static void switchToCurComponentSettingsView() {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IViewPart view = page.findView(ComponentSettingsView.ID);
        if (view == null) {
            try {
                view = page.showView(ComponentSettingsView.ID);
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }
        }
        if (view != null && view instanceof ComponentSettingsView) {
            ComponentSettingsView settingView = (ComponentSettingsView) view;
            Element element = settingView.getElement();
            if (element != null) {
                settingView.cleanDisplay();
                settingView.setElement(element);
            }
        }

    }
}
