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
package org.talend.designer.core.ui.editor.process;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.dnd.TemplateTransfer;
import org.eclipse.gef.dnd.TemplateTransferDropTargetListener;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.CreationFactory;
import org.talend.designer.core.ui.editor.ElementFactory;

/**
 * Performs a native Drop using the {@link TemplateTransfer}. The Drop is performed by using a {@link CreateRequest} to
 * obtain a <code>Command</code> from the targeted <code>EditPart</code>. <br/>
 * 
 * $Id$
 * 
 */
public class ProcessTemplateTransferDropTargetListener extends TemplateTransferDropTargetListener {

    public ProcessTemplateTransferDropTargetListener(EditPartViewer viewer) {
        super(viewer);
    }

    // ------------------------------------------------------------------------
    // Abstract methods from TemplateTransferDropTargetListener

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.dnd.TemplateTransferDropTargetListener#getFactory(java.lang.Object)
     */
    protected CreationFactory getFactory(Object template) {
        return new ElementFactory(template);
    }
}
