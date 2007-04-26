// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2007 Talend - www.talend.com
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
package org.talend.designer.core.ui.editor;

import org.eclipse.gef.EditPart;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.contentoutline.ContentOutline;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.talend.designer.core.ui.MultiPageTalendEditor;
import org.talend.designer.core.ui.editor.outline.NodeTreeEditPart;

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

    StructuredSelection oldSelection;

    @Override
    public void selectionChanged(IWorkbenchPart part, ISelection selection) {
        ISelection newSelection;
        if (part instanceof MultiPageTalendEditor) {
            MultiPageTalendEditor mpte = (MultiPageTalendEditor) part;
            newSelection = mpte.getTalendEditor().getViewer().getSelection();
            if (selection instanceof StructuredSelection) {
                StructuredSelection structSel = (StructuredSelection) newSelection;
                if (structSel.size() != 1) {
                    return;
                }
                if (structSel.getFirstElement() instanceof EditPart) {
                    if (structSel.equals(oldSelection)) {
                        if (getCurrentTab() != null) {
                            getCurrentTab().setInput(part, selection);
                        }
                    } else {
                        super.selectionChanged(part, selection);
                    }
                    oldSelection = structSel;
                }
            }
        } else if (part instanceof ContentOutline) {
            ContentOutline outline = (ContentOutline) part;
            newSelection = outline.getSelection();
            if (selection instanceof StructuredSelection) {
                StructuredSelection structSel = (StructuredSelection) newSelection;
                if (structSel.size() != 1) {
                    return;
                }
                if (structSel.getFirstElement() instanceof NodeTreeEditPart) {
                    if (structSel.equals(oldSelection)) {
                        this.getCurrentTab().setInput(part, selection);
                    } else {
                        super.selectionChanged(part, selection);
                    }
                    oldSelection = structSel;
                }
            }
        }
    }

    public StructuredSelection getOldSelection() {
        return oldSelection;
    }
}
