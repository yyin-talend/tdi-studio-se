// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
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
package org.talend.designer.core.ui.editor;

import org.eclipse.gef.EditPart;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

/**
 * The TabbedPropertySheetPage will only change when an EditPart is selected. This class was created due to a problem
 * with the default properties and the displayed ISection. In some cases it was possible to see some sections even
 * without select an EditPart.
 * 
 * $Id$
 * 
 */
public class TalendTabbedPropertySheetPage extends TabbedPropertySheetPage {

    public TalendTabbedPropertySheetPage(ITabbedPropertySheetPageContributor tabbedPropertySheetPageContributor) {
        super(tabbedPropertySheetPageContributor);
    }

    @Override
    public void selectionChanged(IWorkbenchPart part, ISelection selection) {
        if (selection instanceof StructuredSelection) {
            StructuredSelection structSel = (StructuredSelection) selection;
            if (structSel.size() != 1) {
                return;
            }
            if (structSel.getFirstElement() instanceof EditPart) {
                super.selectionChanged(part, selection);
            }
        }
    }
}
