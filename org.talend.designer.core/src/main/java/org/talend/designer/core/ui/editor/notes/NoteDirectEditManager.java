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
package org.talend.designer.core.ui.editor.notes;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

/**
 */
public class NoteDirectEditManager extends DirectEditManager {

    public NoteDirectEditManager(GraphicalEditPart source, Class editorType, CellEditorLocator locator) {
        super(source, editorType, locator);
    }

    protected CellEditor createCellEditorOn(Composite composite) {
        return new TextCellEditor(composite, SWT.MULTI | SWT.WRAP);
    }

    @Override
    protected void initCellEditor() {
        NoteFigure noteFigure = (NoteFigure) getEditPart().getFigure();
        getCellEditor().setValue(noteFigure.getText());
    }

    @Override
    public void show() {
        super.show();

        // unselect text in celleditor's control
        if (getCellEditor().getControl() instanceof Text) {
            Text text = (Text) getCellEditor().getControl();
            text.setSelection(text.getText().length(), text.getText().length());
        }
    }
}
