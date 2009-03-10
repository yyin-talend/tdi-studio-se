// ============================================================================
//
// Copyright (C) 2006-2009 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.ui.wizards.exportjob;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.internal.wizards.datatransfer.DataTransferMessages;
import org.talend.commons.ui.swt.advanced.composite.FilteredCheckboxTree;
import org.talend.core.CorePlugin;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNode.ENodeType;
import org.talend.repository.ui.views.CheckboxRepositoryTreeViewer;
import org.talend.repository.ui.views.IRepositoryView;
import org.talend.repository.ui.views.RepositoryView;

/**
 * DOC achen class global comment. Detailled comment
 */
public class ExportTreeViewer {

    private FilteredCheckboxTree filteredCheckboxTree;

    private CheckboxRepositoryView exportItemsTreeViewer;

    private IRepositoryView repositoryView = RepositoryView.show();

    Collection<RepositoryNode> repositoryNodes = new ArrayList<RepositoryNode>();

    private IStructuredSelection selection;

    public ExportTreeViewer(IStructuredSelection selection) {
        this.selection = selection;
    }

    /**
     * 
     * @param workArea
     */
    public Composite createItemList(Composite workArea) {
        Group itemComposite = new Group(workArea, 0);
        GridLayoutFactory.swtDefaults().numColumns(2).applyTo(itemComposite);
        GridDataFactory.swtDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).hint(400, 200).applyTo(itemComposite);

        Label label = new Label(itemComposite, SWT.NONE);
        label.setText("Select the items to Export:"); //$NON-NLS-1$
        GridDataFactory.swtDefaults().span(2, 1).applyTo(label);

        createTreeViewer(itemComposite);

        createSelectionButton(itemComposite);

        exportItemsTreeViewer.refresh();
        // force loading all nodes
        TreeViewer viewer = exportItemsTreeViewer.getViewer();
        viewer.expandAll();
        viewer.collapseAll();
        // expand to level of metadata connection
        viewer.expandToLevel(4);

        // if user has select some items in repository view, mark them as checked
        if (!selection.isEmpty()) {
            repositoryNodes.addAll(selection.toList());
            ((CheckboxTreeViewer) viewer).setCheckedElements(repositoryNodes.toArray());
            for (RepositoryNode node : repositoryNodes) {
                expandParent(viewer, node);
                exportItemsTreeViewer.refresh(node);
            }
        }
        return itemComposite;
    }

    private void expandParent(TreeViewer viewer, RepositoryNode node) {
        RepositoryNode parent = node.getParent();
        if (parent != null) {
            expandParent(viewer, parent);
            viewer.setExpandedState(parent, true);
        }
    }

    public RepositoryNode[] getCheckNodes() {
        CheckboxTreeViewer viewer = (CheckboxTreeViewer) exportItemsTreeViewer.getViewer();
        List<RepositoryNode> ret = new ArrayList<RepositoryNode>();
        for (int i = 0; i < viewer.getCheckedElements().length; i++) {
            RepositoryNode node = (RepositoryNode) viewer.getCheckedElements()[i];
            if (node.getType() == ENodeType.REPOSITORY_ELEMENT) {
                ret.add(node);
            }
        }
        return (RepositoryNode[]) ret.toArray(new RepositoryNode[0]);
    }

    private void createTreeViewer(Composite itemComposite) {
        filteredCheckboxTree = new FilteredCheckboxTree(itemComposite, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI) {

            @Override
            protected CheckboxTreeViewer doCreateTreeViewer(Composite parent, int style) {
                exportItemsTreeViewer = new CheckboxRepositoryView();
                try {
                    exportItemsTreeViewer.init(repositoryView.getViewSite());
                } catch (PartInitException e) {
                    e.printStackTrace();
                }
                exportItemsTreeViewer.createPartControl(parent);

                return (CheckboxTreeViewer) exportItemsTreeViewer.getViewer();
            }

            @Override
            protected void refreshCompleted() {
                getViewer().expandToLevel(3);
                restoreCheckedElements();
            }

            @Override
            protected boolean isNodeCollectable(TreeItem item) {
                Object obj = item.getData();
                if (obj instanceof RepositoryNode) {
                    RepositoryNode node = (RepositoryNode) obj;
                    if (node.getObjectType() == ERepositoryObjectType.METADATA_CONNECTIONS) {
                        return true;
                    }
                }
                return false;
            }
        };
        exportItemsTreeViewer.getViewer().addFilter(new ViewerFilter() {

            @Override
            public boolean select(Viewer viewer, Object parentElement, Object element) {
                RepositoryNode node = (RepositoryNode) element;
                return filterRepositoryNode(node);
            }
        });
    }

    private boolean filterRepositoryNode(RepositoryNode node) {
        if (node == null) {
            return false;
        }
        if (node.isBin()) {
            return false;
        }

        ERepositoryObjectType contentType = node.getContentType();
        // System.out.println("contentType---" + contentType + " nodetype--" + node.getType());
        if (contentType != null) {
            switch (contentType) {
            case PROCESS: // referenced project.
                return true;
            default:
                return false;
            }
        } else {
            if (node.getType() == ENodeType.REPOSITORY_ELEMENT) {
                return true;
            }
        }

        return false;
    }

    /**
     * DOC hcw Comment method "createSelectionButton".
     * 
     * @param itemComposite
     */
    private void createSelectionButton(Composite itemComposite) {
        Composite buttonComposite = new Composite(itemComposite, SWT.NONE);
        GridLayoutFactory.swtDefaults().margins(0, 25).applyTo(buttonComposite);
        GridDataFactory.swtDefaults().align(SWT.FILL, SWT.BEGINNING).applyTo(buttonComposite);
        buttonComposite.setLayout(new RowLayout(SWT.VERTICAL));

        Button hide = new Button(buttonComposite, SWT.PUSH);
        hide.setVisible(false);
        Button selectAll = new Button(buttonComposite, SWT.PUSH);
        selectAll.setText(DataTransferMessages.DataTransfer_selectAll);
        selectAll.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                ((CheckboxTreeViewer) exportItemsTreeViewer.getViewer()).setAllChecked(true);
            }
        });

        Button deselectAll = new Button(buttonComposite, SWT.PUSH);
        deselectAll.setText(DataTransferMessages.DataTransfer_deselectAll);
        deselectAll.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                ((CheckboxTreeViewer) exportItemsTreeViewer.getViewer()).setAllChecked(false);
            }
        });

        // setButtonLayoutData(deselectAll);

        Button expandBtn = new Button(buttonComposite, SWT.PUSH);
        expandBtn.setText("Expand All"); //$NON-NLS-1$
        expandBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                exportItemsTreeViewer.getViewer().expandAll();
            }
        });
        // setButtonLayoutData(expandBtn);

        Button collapseBtn = new Button(buttonComposite, SWT.PUSH);
        collapseBtn.setText("Collapse All"); //$NON-NLS-1$
        collapseBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                exportItemsTreeViewer.getViewer().collapseAll();
            }
        });
        // setButtonLayoutData(collapseBtn);
    }

    /**
     * 
     * A repository view with checkbox on the left.
     */
    class CheckboxRepositoryView extends RepositoryView {

        @Override
        protected TreeViewer createTreeViewer(Composite parent) {
            return new CheckboxRepositoryTreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.repository.ui.views.RepositoryView#createPartControl(org.eclipse.swt.widgets.Composite)
         */
        @Override
        public void createPartControl(Composite parent) {
            super.createPartControl(parent);
            CorePlugin.getDefault().getRepositoryService().removeRepositoryChangedListener(this);
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.repository.ui.views.RepositoryView#refresh(java.lang.Object)
         */
        @Override
        public void refresh(Object object) {
            refresh();
            if (object != null) {
                getViewer().expandToLevel(object, AbstractTreeViewer.ALL_LEVELS);
            }
        }

        @Override
        protected void makeActions() {
        }

        @Override
        protected void hookContextMenu() {
        }

        @Override
        protected void contributeToActionBars() {
        }

        @Override
        protected void initDragAndDrop() {
        }

        @Override
        protected void hookDoubleClickAction() {
        }

    }
}
