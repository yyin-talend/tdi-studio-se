// ============================================================================
//
// Copyright (C) 2006-2012 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.ui.views;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.navigator.CommonViewerSorter;
import org.eclipse.ui.navigator.INavigatorContentService;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.nodes.IProjectRepositoryNode;
import org.talend.repository.viewer.filter.PerspectiveFilterActionProvider;
import org.talend.repository.viewer.filter.PerspectiveFilterHelper;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class RepoCommonViewerProvider extends RepositoryViewerProvider {

    public static final RepoCommonViewerProvider NORMAL = new RepoCommonViewerProvider();

    public static final RepoCommonViewerProvider CHECKBOX = new RepoCommonViewerProvider() {

        @Override
        protected TreeViewer createTreeViewer(Composite parent, int style) {
            return new CheckboxRepoCommonViewer(getViewId(), parent, style);
        }

    };

    @Override
    public TreeViewer createViewer(Composite parent) {
        TreeViewer treeViewer = super.createViewer(parent);
        doFilterForCommonViewer(treeViewer);

        // Maybe, no need filter in dialog, because it's not for view
        // if (treeViewer instanceof IRepoNavigatorContentService) {
        //
        // INavigatorContentService navigatorContentService = ((IRepoNavigatorContentService) treeViewer)
        // .getNavigatorContentService();
        // INavigatorFilterService filterService = navigatorContentService.getFilterService();
        // ViewerFilter[] visibleFilters = filterService.getVisibleFilters(true);
        // for (int i = 0; i < visibleFilters.length; i++) {
        // treeViewer.addFilter(visibleFilters[i]);
        // }
        //
        // }

        treeViewer.setSorter(new CommonViewerSorter());

        return treeViewer;
    }

    protected TreeViewer createTreeViewer(final Composite parent, final int style) {
        return new RepoCommonViewer(getViewId(), parent, style);
    }

    @Override
    protected void addProviders(TreeViewer treeViewer) {
        // super.addProviders(treeViewer); will use common viewer to manage
    }

    protected void checkSorter(TreeViewer treeViewer) {
        // super.checkSorter(treeViewer); will use common viewer to manage
    }

    protected void doFilterForCommonViewer(TreeViewer treeViewer) {
        if (treeViewer instanceof IRepoNavigatorContentService) {
            INavigatorContentService navigatorContentService = ((IRepoNavigatorContentService) treeViewer)
                    .getNavigatorContentService();

            IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
            if (activeWorkbenchWindow != null) {
                IWorkbenchPage activePage = activeWorkbenchWindow.getActivePage();
                if (activePage != null) {
                    PerspectiveFilterHelper helper = new PerspectiveFilterHelper();
                    helper.setTreeViewer(treeViewer);
                    helper.setNavigatorContentService(navigatorContentService);
                    helper.setActionProviderId(PerspectiveFilterActionProvider.ID);

                    String perspectiveId = activePage.getPerspective().getId();
                    helper.doFiltering(perspectiveId, isPerspectiveFilter(), false);
                }
            }
        }
    }

    protected String getViewId() {
        return IRepositoryView.VIEW_ID;
    }

    protected boolean isPerspectiveFilter() {
        return true; // FIXME true first.
    }

    protected IRepositoryNode getInputRoot(final IProjectRepositoryNode projectRepoNode) {
        if (projectRepoNode instanceof IRepositoryNode) {
            return (IRepositoryNode) projectRepoNode;
        }
        return null;
    }

}
