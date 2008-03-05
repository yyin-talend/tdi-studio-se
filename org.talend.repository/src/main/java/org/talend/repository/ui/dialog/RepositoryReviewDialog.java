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
package org.talend.repository.ui.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNode.EProperties;
import org.talend.repository.ui.views.IRepositoryView;
import org.talend.repository.ui.views.RepositoryView;

/**
 * bqian check the content of the repository view. <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ææäº, 29 ä¹æ 2006) nrousseau $
 * 
 */
public class RepositoryReviewDialog extends Dialog {

    /**
     * DOC bqian RepositoryReviewDialog constructor comment.
     * 
     * @param parentShell
     */
    public RepositoryReviewDialog(Shell parentShell) {
        super(parentShell);
        setShellStyle(SWT.SHELL_TRIM | SWT.APPLICATION_MODAL | getDefaultOrientation());

    }

    /**
     * Configures the shell
     * 
     * @param shell the shell
     */
    @Override
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        // Set the title bar text and the size
        shell.setText("Repository Content");
        shell.setSize(500, 400);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Control createDialogArea(Composite parent) {
        Composite content = (Composite) super.createDialogArea(parent);

        IRepositoryView view = RepositoryView.show();
        // TreeViewer viewer = (TreeViewer) view.getViewer();
        final FakeRepositoryView repositoryView = new FakeRepositoryView();
        try {
            repositoryView.init(view.getViewSite());
        } catch (PartInitException e) {
            e.printStackTrace();
        }

        repositoryView.createPartControl(content);
        repositoryView.refresh();

        return content;
    }

}

/**
 * DOC bqian class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ææäº, 29 ä¹æ 2006) nrousseau $
 * 
 */
class FakeRepositoryView extends RepositoryView {

    private IEditorPart editorPart;

    ViewerFilter filter = new ViewerFilter() {

        @Override
        public boolean isFilterProperty(Object element, String property) {
            return false;
        }

        /**
         * Returns whether the given element makes it through this filter.
         * 
         * @param viewer the viewer
         * @param parentElement the parent element
         * @param element the element
         * @return <code>true</code> if element is included in the filtered set, and <code>false</code> if excluded
         */
        @Override
        public boolean select(Viewer viewer, Object parentElement, Object element) {
            RepositoryNode node = (RepositoryNode) element;
            if (node.getProperties(EProperties.CONTENT_TYPE) != null) {
                if (node.getProperties(EProperties.CONTENT_TYPE).equals(ERepositoryObjectType.SNIPPETS)) {
                    return true;
                }
            }

            return false;
        }

    };

    /**
     * DOC bqian SnippetsDialogTrayView constructor comment.
     */
    public FakeRepositoryView() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.views.RepositoryView#createPartControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void createPartControl(Composite parent) {
        super.createPartControl(parent);
        // getViewer().addFilter(filter);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.views.RepositoryView#refresh(java.lang.Object)
     */
    @Override
    public void refresh(Object object) {
        refresh();
        // viewer.refresh(object);
        if (object != null) {
            // getViewer().setExpandedState(object, true);
            getViewer().expandToLevel(object, AbstractTreeViewer.ALL_LEVELS);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.views.RepositoryView#refresh()
     */
    @Override
    public void refresh() {
        super.refresh();
        // getViewer().setInput(this.getViewSite());
        // RepositoryContentProvider contentProvider = (RepositoryContentProvider) getViewer().getContentProvider();
        // RepositoryNode snippetNode = contentProvider.getCodeNode();
        // getViewer().setInput(snippetNode);

    }

    @Override
    public void dragFinished() {
        LocalSelectionTransfer.getTransfer().setSelection(null);
        LocalSelectionTransfer.getTransfer().setSelectionSetTime(0);
    }
}
