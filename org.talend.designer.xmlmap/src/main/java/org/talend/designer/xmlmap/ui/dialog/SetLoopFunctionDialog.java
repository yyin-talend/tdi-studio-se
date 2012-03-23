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
package org.talend.designer.xmlmap.ui.dialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Widget;
import org.talend.designer.xmlmap.model.emf.xmlmap.InputLoopNodesTable;
import org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode;
import org.talend.designer.xmlmap.ui.resource.ImageInfo;
import org.talend.designer.xmlmap.ui.resource.ImageProviderMapper;

/**
 * DOC hcyi class global comment. Detailled comment
 */
public class SetLoopFunctionDialog extends Dialog {

    private TableViewer viewer;

    private Button upButton;

    private Button downButton;

    private SelectionListener selectionListener;

    private InputLoopNodesTable inputLoopNodesTable;

    List<String> listData = new ArrayList<String>();

    int sequence = 0;

    public SetLoopFunctionDialog(Shell parentShell, InputLoopNodesTable inputLoopNodesTable) {
        super(parentShell);
        this.inputLoopNodesTable = inputLoopNodesTable;
        initData();
    }

    public Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        composite.setLayout(layout);
        GridData data = new GridData(GridData.FILL_BOTH);
        data.horizontalSpan = 2;
        composite.setLayoutData(data);
        createTableControl(composite);

        Composite barComposite = (Composite) super.createDialogArea(parent);
        layout = new GridLayout();
        layout.numColumns = 2;
        barComposite.setLayout(layout);
        data = new GridData(GridData.BEGINNING);
        data.horizontalSpan = 1;
        barComposite.setLayoutData(data);
        createButtons(barComposite);

        return parent;
    }

    private void initData() {
        if (inputLoopNodesTable != null) {
            for (int i = 0; i < inputLoopNodesTable.getInputloopnodes().size(); i++) {
                TreeNode treeNode = inputLoopNodesTable.getInputloopnodes().get(i);
                listData.add(i, treeNode.getXpath());
            }
        }
    }

    private IStructuredContentProvider createContentProvider() {
        return new IStructuredContentProvider() {

            public Object[] getElements(Object inputElement) {
                return ((List) inputElement).toArray();
            }

            public void dispose() {
            }

            public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            }

        };
    }

    private ITableLabelProvider createLabelProvider() {

        return new ITableLabelProvider() {

            public Image getColumnImage(Object element, int columnIndex) {
                return null;
            }

            public String getColumnText(Object element, int columnIndex) {
                String value = ((String) element);
                if (columnIndex == 0) {
                    sequence++;
                    return sequence + "";
                }
                if (columnIndex == 1) {
                    return value;
                }
                throw new IllegalStateException();
            }

            public void addListener(ILabelProviderListener listener) {
            }

            public void dispose() {
            }

            public boolean isLabelProperty(Object element, String property) {
                return false;
            }

            public void removeListener(ILabelProviderListener listener) {
            }

        };
    }

    private void createButtons(Composite box) {
        upButton = createPushButton(box, "ListEditor.up"); //$NON-NLS-1$
        upButton.setImage(ImageProviderMapper.getImage(ImageInfo.SORT_UP));
        downButton = createPushButton(box, "ListEditor.down"); //$NON-NLS-1$
        downButton.setImage(ImageProviderMapper.getImage(ImageInfo.SORT_DOWN));
    }

    private Button createPushButton(Composite parent, String key) {
        Button button = new Button(parent, SWT.PUSH);
        button.setText(JFaceResources.getString(key));
        button.setFont(parent.getFont());
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.widthHint = 30;
        button.setLayoutData(data);
        button.addSelectionListener(getSelectionListener());
        return button;
    }

    public void createSelectionListener() {
        selectionListener = new SelectionAdapter() {

            public void widgetSelected(SelectionEvent event) {
                Widget widget = event.widget;
                if (widget == upButton) {
                    upPressed();
                } else if (widget == downButton) {
                    downPressed();
                }
            }
        };
    }

    private void downPressed() {
        swap(false);
    }

    private TableViewer createTableControl(Composite parent) {
        if (viewer == null) {
            Table table = createTable(parent);
            viewer = new TableViewer(table);
            viewer.setContentProvider(createContentProvider());
            viewer.setLabelProvider(createLabelProvider());
            viewer.setInput(listData);
            table.setFont(parent.getFont());
            viewer.addSelectionChangedListener(new ISelectionChangedListener() {

                public void selectionChanged(SelectionChangedEvent event) {
                }
            });
            viewer.addDoubleClickListener(new IDoubleClickListener() {

                public void doubleClick(DoubleClickEvent event) {
                }
            });
        }
        return viewer;
    }

    private Table createTable(Composite parent) {
        Table contextTable = new Table(parent, SWT.BORDER | SWT.FULL_SELECTION);
        contextTable.setLinesVisible(true);
        contextTable.setHeaderVisible(true);

        TableColumn colName = new TableColumn(contextTable, SWT.NONE);
        colName.setText("Sequence"); //$NON-NLS-1$
        colName.setWidth(100);
        TableColumn colValue = new TableColumn(contextTable, SWT.NONE);
        colValue.setText("input Loop Nodes"); //$NON-NLS-1$
        colValue.setWidth(250);
        return contextTable;
    }

    public int getNumberOfControls() {
        return 2;
    }

    private SelectionListener getSelectionListener() {
        if (selectionListener == null) {
            createSelectionListener();
        }
        return selectionListener;
    }

    protected void selectionChanged() {
        int index = viewer.getTable().getSelectionIndex();
        int size = viewer.getTable().getItemCount();
        setControlEnable(upButton, size > 1 && index > 0);
        setControlEnable(downButton, size > 1 && index >= 0 && index < size - 1);
    }

    public void setFocus() {
        if (viewer != null) {
            viewer.getTable().setFocus();
        }
    }

    private void swap(boolean up) {
        int index = viewer.getTable().getSelectionIndex();
        int target = up ? index - 1 : index + 1;

        if (index >= 0 && (target >= 0 && target < viewer.getTable().getItemCount())) {
            Collections.swap(listData, index, target);
            inputLoopNodesTable.getInputloopnodes().move(index, target);
        }
        sequence = 0;
        viewer.refresh();
        selectionChanged();
    }

    private void upPressed() {
        swap(true);
    }

    private void setEnabled(boolean enabled, Composite parent) {
        setControlEnable(createTableControl(parent).getTable(), enabled);
        setControlEnable(upButton, enabled);
        setControlEnable(downButton, enabled);
    }

    private void setControlEnable(Control control, boolean enable) {
        if (control != null && !control.isDisposed()) {
            control.setEnabled(enable);
        }
    }

    public List<String> getList() {
        return this.listData;
    }
}
