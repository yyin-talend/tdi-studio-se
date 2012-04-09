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
            realRepView = RepositoryManagerHelper.getRepositoryView();
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

        final IStructuredContentProvider contentProvider = getContextProvider();
        treeViewer.setContentProvider(contentProvider);
        treeViewer.setLabelProvider(getLabelProvider());

        final StructuredViewer viewer = getRepView().getViewer();
        final ViewerSorter sorter = viewer.getSorter();
        if (sorter != null) {
            // TDI-20528
            // treeViewer.setSorter(sorter);
            treeViewer.setSorter(new ViewerSorter() {

                @Override
                public int compare(Viewer viewer, Object e1, Object e2) {
                    if (e1 instanceof RepositoryNode && e2 instanceof RepositoryNode) {
                        final RepositoryNode node1 = (RepositoryNode) e1;
                        final RepositoryNode node2 = (RepositoryNode) e2;
                        // do special for simple folder
                        if (node1.getType() == IRepositoryNode.ENodeType.SIMPLE_FOLDER
                                || node2.getType() == IRepositoryNode.ENodeType.SIMPLE_FOLDER) {
                            return e1.toString().compareTo(e2.toString());
                        } else {
                            return sorter.compare(viewer, e1, e2);
                        }
                    }

                    return super.compare(viewer, e1, e2);
                }

            });
        }

        treeViewer.setInput(getInputRoot(getRepView().getRoot()));

        return treeViewer;
    }

    protected TreeViewer createTreeViewer(final Composite parent, final int style) {
        return new CheckboxRepositoryTreeViewer(parent, style);
    }

    protected IRepositoryNode getInputRoot(final IProjectRepositoryNode projectRepoNode) {
        return RecombineRepositoryNodeUtil.getFixingTypesInputRoot(projectRepoNode, getCheckingTypes());
    }
}
