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

import org.epic.perleditor.editors.PerlEditor;
import org.talend.designer.core.ISyntaxCheckableEditor;

/**
 * Perl editor with read only content. <br/>
 * 
 * $Id$
 * 
 */
public class TalendPerlEditor extends PerlEditor implements ISyntaxCheckableEditor {

    private boolean disposed = false;

    /**
     * Constructs a new TalendPerlEditor.
     */
    public TalendPerlEditor() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.editors.text.TextEditor#isSaveAsAllowed()
     */
    @Override
    public boolean isSaveAsAllowed() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.texteditor.AbstractTextEditor#isEditable()
     */
    @Override
    public boolean isEditable() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.ICheckable#checkCode()
     */
    public void validateSyntax() {
        revalidateSyntax();
    }

    /*
     * Return if this perl editor had been disposed.
     * 
     * @see org.talend.designer.core.ISyntaxCheckableEditor#isDisposed()
     */
    public boolean isDisposed() {
        return this.disposed;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.epic.perleditor.editors.PerlEditor#dispose()
     */
    @Override
    public void dispose() {
        this.disposed = true;
        super.dispose();
    }

}
