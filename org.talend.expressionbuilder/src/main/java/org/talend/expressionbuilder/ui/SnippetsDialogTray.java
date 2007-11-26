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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.talend.commons.ui.image.EImage;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.repository.ui.views.IRepositoryView;
import org.talend.repository.ui.views.RepositoryView;

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
        content.setLayout(new GridLayout());

        Button refreshButton = new Button(content, SWT.NONE);
        refreshButton.setText("Refresh");
        refreshButton.setImage(ImageProvider.getImage(EImage.REFRESH_ICON));

        IRepositoryView view = RepositoryView.show();

        // TreeViewer viewer = (TreeViewer) view.getViewer();
        final SnippetsDialogTrayView repositoryView = new SnippetsDialogTrayView();
        try {
            repositoryView.init(view.getViewSite());
        } catch (PartInitException e) {
            e.printStackTrace();
        }

        repositoryView.createPartControl(content);
        repositoryView.refresh();

        refreshButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                repositoryView.refresh();
            }
        });

        // WorkbenchPage page = (WorkbenchPage) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        // page.activate(repositoryView);
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