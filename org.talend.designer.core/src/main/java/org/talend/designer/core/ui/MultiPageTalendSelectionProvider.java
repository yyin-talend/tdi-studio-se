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
package org.talend.designer.core.ui;

import org.eclipse.gef.EditPart;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.part.MultiPageSelectionProvider;

/**
 * Extension of the standard MultiPageSelectionProvider. This class avoid to send all events to the TalendEditor (Gef
 * part) <br/>
 * 
 * $Id$
 * 
 */
public class MultiPageTalendSelectionProvider extends MultiPageSelectionProvider implements ISelectionProvider {

    public void fireSelectionChanged(SelectionChangedEvent event) {
        ISelection sel = event.getSelection();
        if (sel instanceof StructuredSelection) {
            StructuredSelection structSel = (StructuredSelection) sel;
            if (structSel.getFirstElement() instanceof EditPart) {
                super.fireSelectionChanged(event);
            }
        }
    }

    public MultiPageTalendSelectionProvider(MultiPageEditorPart multiPageEditor) {
        super(multiPageEditor);
    }

}
