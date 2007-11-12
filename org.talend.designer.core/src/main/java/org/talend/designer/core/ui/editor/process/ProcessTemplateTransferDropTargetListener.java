// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the  agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
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
