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
package org.talend.sqlbuilder.actions;

import org.eclipse.jface.action.Action;
import org.talend.sqlbuilder.ui.editor.ISQLEditor;


/**
 * Abstract implementation for a sql editor actions.
 * Extend this class to add new actions to the sql editor.
 * 
 * @author Davy Vanherbergen
 *
 */
public abstract class AbstractEditorAction extends Action {
   
    public abstract String getText();
    
    public String getToolTipText() {
        return getText();
    }
    
    public abstract void run();
    
    protected ISQLEditor editor;
    
    
    /**
     * Sets the editor.
     * @param editor the editor to set
     */
    public final void setEditor(ISQLEditor editor) {
        this.editor = editor;
    }

    /**
     * Getter for editor.
     * @return the editor
     */
    public ISQLEditor getEditor() {
        return this.editor;
    }

    public boolean isDisabled() {

        boolean active = editor.getSessionTreeNode() != null;
        return !active;
    }
    
}
