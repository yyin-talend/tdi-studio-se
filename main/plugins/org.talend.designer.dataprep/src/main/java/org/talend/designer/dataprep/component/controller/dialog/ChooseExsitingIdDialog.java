// ============================================================================
//
// Copyright (C) 2006-2014 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.dataprep.component.controller.dialog;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * created by wchen on 2015年9月18日 Detailled comment
 *
 */
public class ChooseExsitingIdDialog extends Dialog {

    List<ExistingIdObject> existingIds;

    private String selectedId;

    /**
     * DOC wchen ChooseExsitingIdDialog constructor comment.
     * 
     * @param parentShell
     */
    public ChooseExsitingIdDialog(Shell parentShell, List<ExistingIdObject> existingIds) {
        super(parentShell);
        setShellStyle(getShellStyle() | SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
        this.existingIds = existingIds;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
     */
    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText("Select an exsisting preperation");
    }

    @Override
    protected void initializeBounds() {
        // super.initializeBounds();
        getShell().setSize(800, 400);
        Point location = getInitialLocation(getShell().getSize());
        getShell().setLocation(location.x, location.y);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Control createDialogArea(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));
        TableColumnLayout existIdColumnLayout = new TableColumnLayout();
        composite.setLayout(existIdColumnLayout);

        final CheckboxTableViewer tableViwer = CheckboxTableViewer.newCheckList(composite, SWT.BORDER | SWT.FULL_SELECTION);
        final Table table = tableViwer.getTable();
        table.setLayoutData(new GridData(GridData.FILL_BOTH));
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        TableColumn nameColumn = new TableColumn(table, SWT.NONE);
        nameColumn.setText("Name");
        existIdColumnLayout.setColumnData(nameColumn, new ColumnWeightData(3, true));

        TableColumn authorColumn = new TableColumn(table, SWT.NONE);
        authorColumn.setText("Author");
        existIdColumnLayout.setColumnData(authorColumn, new ColumnWeightData(2, true));

        TableColumn lastModificationColumn = new TableColumn(table, SWT.NONE);
        lastModificationColumn.setText("Last Modification");
        existIdColumnLayout.setColumnData(lastModificationColumn, new ColumnWeightData(2, true));

        tableViwer.setContentProvider(new IStructuredContentProvider() {

            @Override
            public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            }

            @Override
            public void dispose() {

            }

            @Override
            public Object[] getElements(Object inputElement) {
                if (inputElement instanceof List) {
                    return ((List) inputElement).toArray();
                }
                return null;
            }
        });
        tableViwer.setLabelProvider(new ITableLabelProvider() {

            @Override
            public void removeListener(ILabelProviderListener listener) {
            }

            @Override
            public boolean isLabelProperty(Object element, String property) {
                return false;
            }

            @Override
            public void dispose() {

            }

            @Override
            public void addListener(ILabelProviderListener listener) {

            }

            @Override
            public String getColumnText(Object element, int columnIndex) {
                if (element instanceof ExistingIdObject) {
                    ExistingIdObject idObject = (ExistingIdObject) element;
                    switch (columnIndex) {
                    case 0:
                        return idObject.getName();
                    case 1:
                        return idObject.getAuthor();
                    case 2:
                        return DateFormat.getInstance().format(new Date(new Long(idObject.getLastModificationDate())));
                    }
                }

                return "";
            }

            @Override
            public Image getColumnImage(Object element, int columnIndex) {
                return null;
            }
        });
        tableViwer.addCheckStateListener(new ICheckStateListener() {

            @Override
            public void checkStateChanged(CheckStateChangedEvent event) {
                if (event.getChecked()) {
                    tableViwer.setCheckedElements(new Object[] { event.getElement() });
                    selectedId = ((ExistingIdObject) event.getElement()).getId();
                    getButton(IDialogConstants.OK_ID).setEnabled(true);
                } else {
                    getButton(IDialogConstants.OK_ID).setEnabled(false);
                }
            }

        });
        tableViwer.setInput(existingIds);

        return composite;
    }

    /**
     * Getter for selectedId.
     * 
     * @return the selectedId
     */
    public String getSelectedId() {
        return this.selectedId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#createButtonsForButtonBar(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        // TODO Auto-generated method stub
        super.createButtonsForButtonBar(parent);
        getButton(IDialogConstants.OK_ID).setEnabled(false);
    }
}
