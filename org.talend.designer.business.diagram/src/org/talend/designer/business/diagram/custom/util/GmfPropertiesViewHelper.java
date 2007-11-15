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
package org.talend.designer.business.diagram.custom.util;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gmf.runtime.common.ui.util.WorkbenchPartActivator;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewPart;

/**
 */
public class GmfPropertiesViewHelper {

    public void selectTargetEditPart(final EditPartViewer viewer, final EditPart targetEditPart) {
        Display.getCurrent().asyncExec(new Runnable() {

            public void run() {
                // show properties view
                IViewPart viewPart = WorkbenchPartActivator.showPropertySheet();

                // select target Edit Part to activate the view with the good input
                viewer.deselectAll();
                viewer.select(targetEditPart);
                viewer.getControl().forceFocus();
            }
        });
    }

}
