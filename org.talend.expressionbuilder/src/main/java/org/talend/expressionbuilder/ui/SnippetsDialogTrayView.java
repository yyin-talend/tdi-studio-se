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

import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNode.EProperties;
import org.talend.repository.ui.views.RepositoryContentProvider;
import org.talend.repository.ui.views.RepositoryView;

/**
 * DOC bqian class global comment. Detailled comment <br/>
 * 
 */
public class SnippetsDialogTrayView extends RepositoryView {

    private IEditorPart editorPart;

    ViewerFilter filter = new ViewerFilter() {

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
    public SnippetsDialogTrayView() {
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
        getViewer().addFilter(filter);
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
        getViewer().setInput(this.getViewSite());
        RepositoryContentProvider contentProvider = (RepositoryContentProvider) getViewer().getContentProvider();
        RepositoryNode snippetNode = contentProvider.getCodeNode();
        getViewer().setInput(snippetNode);

    }

    public void dragFinished() {
        LocalSelectionTransfer.getTransfer().setSelection(null);
        LocalSelectionTransfer.getTransfer().setSelectionSetTime(0);
    }
}
