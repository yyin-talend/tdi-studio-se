// ============================================================================
//
// Copyright (C) 2006-2011 Talend Inc. - www.talend.com
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

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.repository.model.RepositoryNode;
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
            realRepView = RepositoryView.show();
        }
        return realRepView;
    }

    protected RepositoryContentProvider getContextProvider() {
        return new RepositoryContentProvider(getRepView());
    }

    protected RepositoryLabelProvider getLabelProvider() {
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

        final RepositoryContentProvider contentProvider = getContextProvider();
        treeViewer.setContentProvider(contentProvider);
        treeViewer.setLabelProvider(getLabelProvider());

        ViewerSorter sorter = getRepView().getViewer().getSorter();
        if (sorter != null) {
            treeViewer.setSorter(sorter);
        }

        treeViewer.setInput(getInputRoot(contentProvider));

        return treeViewer;
    }

    protected TreeViewer createTreeViewer(final Composite parent, final int style) {
        return new CheckboxRepositoryTreeViewer(parent, style);
    }

    protected RepositoryNode getInputRoot(final RepositoryContentProvider contentProvider) {
        return RecombineRepositoryNodeUtil.getFixingTypesInputRoot(contentProvider, getCheckingTypes());
    }
}
