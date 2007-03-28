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
package org.talend.designer.business.diagram.custom.properties;

import org.eclipse.gmf.runtime.diagram.ui.properties.views.PropertiesBrowserPage;
import org.eclipse.ui.internal.views.properties.tabbed.view.TabDescriptor;
import org.eclipse.ui.internal.views.properties.tabbed.view.TabbedPropertyComposite;
import org.eclipse.ui.internal.views.properties.tabbed.view.TabbedPropertyList;
import org.eclipse.ui.internal.views.properties.tabbed.view.TabbedPropertyList.ListElement;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;

/**
 * DOC mhelleboid class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class BusinessPropertiesBrowserPage extends PropertiesBrowserPage {

    private TabDescriptor[] tabDescriptors;

    /**
     * DOC mhelleboid BusinessPropertiesBrowserPage constructor comment.
     * 
     * @param contributor
     */
    public BusinessPropertiesBrowserPage(ITabbedPropertySheetPageContributor contributor) {
        super(contributor);
    }

    /**
     * DOC mhelleboid Comment method "test".
     * 
     * @param tabId TODO
     */
    public void showTab(String tabId) {
        TabDescriptor tabDescriptor = getTabDescriptor(tabId);
        if (tabDescriptor == null)
            return;

        TabbedPropertyComposite tabbedPropertyComposite = (TabbedPropertyComposite) getControl();
        TabbedPropertyList tabbedPropertyList = tabbedPropertyComposite.getList();

        for (int i = 0; true; i++) {
            Object element = tabbedPropertyList.getElementAt(i);

            if (element == null)
                return;

            if (element instanceof ListElement) {
                ListElement listElement = (ListElement) element;

                if (listElement != null && listElement.getText().equals(tabDescriptor.getText()))
                    tabbedPropertyList.select(i, false);
            }
        }
    }

    /**
     * DOC mhelleboid Comment method "getTabDescriptor".
     * 
     * @param tabId TODO
     * @return
     */
    private TabDescriptor getTabDescriptor(String tabId) {
        for (int i = 0; i < tabDescriptors.length; i++) {
            TabDescriptor tabDescriptor = tabDescriptors[i];
            if (tabDescriptor.getId().equals(tabId))
                return tabDescriptor;
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage#updateTabs(org.eclipse.ui.internal.views.properties.tabbed.view.TabDescriptor[])
     */
    @Override
    protected void updateTabs(TabDescriptor[] descriptors) {
        tabDescriptors = descriptors;
        super.updateTabs(descriptors);
    }
}
