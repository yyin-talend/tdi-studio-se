// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//   
// ============================================================================
package org.talend.expressionbuilder.ui;

import org.eclipse.jface.dialogs.DialogTray;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
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
            final SnippetsDialogTrayView view = new SnippetsDialogTrayView();
            view.setEditor(editorPart);
            view.init(snippetsview.getViewSite());
            view.createPartControl(content);
            content.addDisposeListener(new DisposeListener() {

                public void widgetDisposed(DisposeEvent e) {
                    view.dispose();
                }
            });
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