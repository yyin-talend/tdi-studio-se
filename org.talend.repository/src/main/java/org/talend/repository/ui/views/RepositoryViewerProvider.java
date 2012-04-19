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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.utils.RepositoryManagerHelper;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.ProjectRepositoryNode;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.nodes.IProjectRepositoryNode;
import org.talend.repository.ui.utils.RecombineRepositoryNodeUtil;

/**
 * ggu class global comment. Detailled comment
 */
public class RepositoryViewerProvider {

    private IRepositoryView realRepView;

    public RepositoryViewerProvider() {
    }

    protected int getStyle() {
        return SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL;
    }

    protected IRepositoryView getRepView() {
        if (realRepView == null) {
            realRepView = RepositoryManagerHelper.findRepositoryView();
        }
        return realRepView;
    }

    protected IStructuredContentProvider getContextProvider() {
        // can't reuse the content provider
        // final StructuredViewer viewer = getRepView().getViewer();
        // return (IStructuredContentProvider) viewer.getContentProvider();
        return new RepositoryContentProvider(getRepView());
    }

    protected ILabelProvider getLabelProvider() {
        // can't reuse the label provider
        // final StructuredViewer viewer = getRepView().getViewer();
        // return (ILabelProvider) viewer.getLabelProvider();
        return new RepositoryLabelProvider(getRepView());
    }

    protected ERepositoryObjectType getCheckingType() {
        return null;
    }

    protected List<ERepositoryObjectType> getCheckingTypes() {
        final ERepositoryObjectType checkingType = getCheckingType();
        List<ERepositoryObjectType> checkingTypes = new ArrayList<ERepositoryObjectType>();
        if (checkingType != null) {
            checkingTypes.add(checkingType);
        }
        return checkingTypes;
    }

    public TreeViewer createViewer(final Composite parent) {
        TreeViewer treeViewer = createTreeViewer(parent, getStyle());
        treeViewer.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));

        addProviders(treeViewer);

        checkSorter(treeViewer);

        treeViewer.setInput(getInputRoot(getProjectRepositoryNode()));

        return treeViewer;
    }

    protected void addProviders(TreeViewer treeViewer) {
        final IStructuredContentProvider contentProvider = getContextProvider();
        if (contentProvider != null) {
            treeViewer.setContentProvider(contentProvider);
        }
        final ILabelProvider labelProvider = getLabelProvider();
        if (labelProvider != null) {
            treeViewer.setLabelProvider(labelProvider);
        }
    }

    protected void checkSorter(TreeViewer treeViewer) {
        ViewerSorter sorter = null;
        if (getRepView() != null) {
            final StructuredViewer viewer = getRepView().getViewer();
            sorter = viewer.getSorter();
        }
        if (sorter == null) {
            // when the repository view is not opened, so used this one by default.
            sorter = new RepositoryNameSorter();
        }
        if (sorter != null) {
            final ViewerSorter viewerSorter = sorter;
            // TDI-20528
            // treeViewer.setSorter(sorter);
            treeViewer.setSorter(new ViewerSorter() {

                @Override
                public int compare(Viewer viewer, Object e1, Object e2) {
                    if (e1 instanceof RepositoryNode && e2 instanceof RepositoryNode) {
                        final RepositoryNode node1 = (RepositoryNode) e1;
                        final RepositoryNode node2 = (RepositoryNode) e2;
                        if (node1.isBin()) { // recycle bin is always in bottom
                            return 1;
                        }
                        if (node2.isBin()) { // recycle bin is always in bottom
                            return -1;
                        }
                        // do special for simple folder， TDI-20528
                        if (node1.getType() == IRepositoryNode.ENodeType.SIMPLE_FOLDER
                                || node2.getType() == IRepositoryNode.ENodeType.SIMPLE_FOLDER) {
                            return e1.toString().compareTo(e2.toString());
                        } else {
                            return viewerSorter.compare(viewer, e1, e2);
                        }
                    }
                    return super.compare(viewer, e1, e2);

                }

            });
        }
    }

    public IProjectRepositoryNode getProjectRepositoryNode() {
        if (getRepView() != null) { // in fact, they are same
            return getRepView().getRoot();
        } else {
            return ProjectRepositoryNode.getInstance();
        }
    }

    protected TreeViewer createTreeViewer(final Composite parent, final int style) {
        return new CheckboxRepositoryTreeViewer(parent, style);
    }

    protected IRepositoryNode getInputRoot(final IProjectRepositoryNode projectRepoNode) {
        return RecombineRepositoryNodeUtil.getFixingTypesInputRoot(projectRepoNode, getCheckingTypes());
    }

}
