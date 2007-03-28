// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006 Talend – www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
//
// ============================================================================
package org.talend.designer.business.diagram.custom.util;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gmf.runtime.common.ui.util.WorkbenchPartActivator;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.views.properties.PropertySheet;
import org.talend.designer.business.diagram.custom.properties.BusinessPropertiesBrowserPage;

/**
 * DOC mhelleboid class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class GmfPropertiesViewHelper {

    /**
     * DOC mhelleboid Comment method "showTab".
     * 
     * @param tabId TODO
     * @param viewer TODO
     * @param targetEditPart TODO
     */
    public void showTab(final String tabId, final EditPartViewer viewer, final EditPart targetEditPart) {
        Display.getCurrent().asyncExec(new Runnable() {

            public void run() {
                // show properties view
                IViewPart viewPart = WorkbenchPartActivator.showPropertySheet();

                // select target Edit Part to activate the view with the good input
                viewer.deselectAll();
                viewer.select(targetEditPart);
                viewer.getControl().forceFocus();

                // show the good tab in the properties view
                if (viewPart instanceof PropertySheet) {
                    PropertySheet propertySheet = (PropertySheet) viewPart;
                    IPage currentPage = propertySheet.getCurrentPage();

                    if (currentPage instanceof BusinessPropertiesBrowserPage) {
                        BusinessPropertiesBrowserPage businessPropertiesBrowserPage = (BusinessPropertiesBrowserPage) currentPage;
                        businessPropertiesBrowserPage.showTab(tabId);
                    }
                }
            }
        });
    }

}
