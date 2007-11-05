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
package org.talend.expressionbuilder.ui;

import org.eclipse.jface.dialogs.DialogTray;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.wst.common.snippets.internal.Logger;
import org.eclipse.wst.common.snippets.internal.SnippetsPlugin;
import org.eclipse.wst.common.snippets.internal.ui.SnippetsView;

/**
 * A subclass of DialogTray to provider the function of snippets. <br/>
 * 
 */
public class SnippetsDialogTray extends DialogTray {

    private IEditorPart editorPart;

    public SnippetsDialogTray() {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.DialogTray#createContents(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Control createContents(Composite parent) {

        Composite content = new Composite(parent, SWT.NONE);
        content.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        content.setLayout(new FillLayout());

        SnippetsView snippetsview = null;
        try {
            snippetsview = (SnippetsView) SnippetsPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage()
                    .findView(SnippetsPlugin.NAMES.VIEW_ID);
            if (snippetsview == null) {
                snippetsview = (SnippetsView) SnippetsPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow()
                        .getActivePage().showView(SnippetsPlugin.NAMES.VIEW_ID);
            }
            SnippetsDialogTrayView view = new SnippetsDialogTrayView();
            view.setEditor(editorPart);
            view.init(snippetsview.getViewSite());
            view.createPartControl(content);
        } catch (PartInitException e) {
            Logger.logException(e);
        }
        // PartSite partSite = (PartSite) snippetsview.getSite();
        return content;
    }

    /**
     * DOC bqian Comment method "setEditor".
     * 
     * @param editorPart
     */
    public void setEditor(IEditorPart editorPart) {
        this.editorPart = editorPart;
    }
}