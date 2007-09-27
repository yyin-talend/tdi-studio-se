// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2007 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
//
// ============================================================================
package org.talend.designer.core.ui.editor.process;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.dialogs.SelectionDialog;
import org.eclipse.ui.internal.WorkbenchMessages;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreatorColumn;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator.LAYOUT_MODE;
import org.talend.commons.ui.swt.tableviewer.behavior.DefaultCellModifier;
import org.talend.commons.ui.swt.tableviewer.tableeditor.CheckboxTableEditorContent;
import org.talend.commons.utils.data.bean.IBeanPropertyAccessors;
import org.talend.designer.core.i18n.Messages;

/**
 * This dialog show the check results when the metadata update.<br/>
 * 
 */
public class MetadataUpdateCheckDialog extends SelectionDialog {

    // sizing constants
    private static final int SIZING_SELECTION_WIDGET_HEIGHT = 300;

    private static final int SIZING_SELECTION_WIDGET_WIDTH = 450;

    private static final int SIZING_COLUMN_WIDTH = 20;

    // the root element to populate the viewer with
    private List<MetadataUpdateCheckResult> inputElement;

    TableViewerCreator<MetadataUpdateCheckResult> tableViewerCreator;

    /**
     * Creates a list selection dialog.<br/>
     * 
     * @param parentShell the parent shell
     * @param input the root element to populate this dialog with
     * @param message the message to be displayed at the top of this dialog, or <code>null</code> to display a default
     * message
     */
    public MetadataUpdateCheckDialog(Shell parentShell, List<MetadataUpdateCheckResult> input, String message) {
        super(parentShell);
        setTitle(""); //$NON-NLS-1$
        setShellStyle(SWT.TITLE | SWT.RESIZE | SWT.APPLICATION_MODAL | getDefaultOrientation());
        inputElement = input;
        if (message != null) {
            setMessage(message);
        } else {
            setMessage(""); //$NON-NLS-1$
        }
    }

    /**
     * Add the selection and deselection buttons to the dialog.<br/>
     * 
     * @param composite org.eclipse.swt.widgets.Composite
     */
    private void addSelectionButtons(Composite composite) {
        Composite buttonComposite = new Composite(composite, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 0;
        layout.marginWidth = 0;
        layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
        buttonComposite.setLayout(layout);
        buttonComposite.setLayoutData(new GridData(SWT.END, SWT.TOP, true, false));

        // "select all" button
        Button selectButton = createButton(buttonComposite, IDialogConstants.SELECT_ALL_ID,
                WorkbenchMessages.SelectionDialog_selectLabel, false);

        selectButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                selectAll(true);
            }
        });

        // "deselect all" button
        Button deselectButton = createButton(buttonComposite, IDialogConstants.DESELECT_ALL_ID,
                WorkbenchMessages.SelectionDialog_deselectLabel, false);

        deselectButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                selectAll(false);
            }

        });
    }

    /**
     * whether select all the items or not.
     * 
     * @param b Select all items if input true here, otherwise deselect all.
     */
    private void selectAll(boolean b) {
        Table table = tableViewerCreator.getTable();
        TableItem[] items = table.getItems();
        for (int i = 0; i < items.length; i++) {
            MetadataUpdateCheckResult result = (MetadataUpdateCheckResult) items[i].getData();
            if (result.getResultType() == MetadataUpdateCheckResult.ResultType.change) {
                result.setChecked(b);
            }
            tableViewerCreator.refreshTableEditorControls();
        }
    }

    /**
     * Visually checks the previously-specified elements in this dialog's list viewer.<br/>
     */
    private void checkInitialSelections() {
        Iterator itemsToCheck = getInitialElementSelections().iterator();

        while (itemsToCheck.hasNext()) {
            MetadataUpdateCheckResult result = (MetadataUpdateCheckResult) itemsToCheck.next();
            result.setChecked(true);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
     */
    @Override
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        // shell.setSize(SIZING_SELECTION_WIDGET_WIDTH, SIZING_SELECTION_WIDGET_HEIGHT);
    }

    /*
     * (non-Javadoc) Method declared on Dialog.
     */
    @Override
    protected Control createDialogArea(Composite parent) {
        // page group
        Composite composite = (Composite) super.createDialogArea(parent);

        initializeDialogUnits(composite);

        Label messageLabel = createMessageArea(composite);

        GridData data = new GridData(GridData.FILL_BOTH);
        data.heightHint = SIZING_SELECTION_WIDGET_HEIGHT;
        data.widthHint = SIZING_SELECTION_WIDGET_WIDTH;

        tableViewerCreator = new TableViewerCreator<MetadataUpdateCheckResult>(composite);
        tableViewerCreator.setHeaderVisible(true);
        tableViewerCreator.setLinesVisible(true);
        tableViewerCreator.setBorderVisible(true);
        tableViewerCreator.setColumnsMoveableByDefault(true);
        tableViewerCreator.setColumnsResizableByDefault(true);
        tableViewerCreator.setLayoutMode(LAYOUT_MODE.DEFAULT);
        tableViewerCreator.setCellModifier(new CheckCellModifier(tableViewerCreator));

        tableViewerCreator.createTable().setLayoutData(data);

        createColumns(tableViewerCreator);

        // initialize page
        if (!getInitialElementSelections().isEmpty()) {
            checkInitialSelections();
        }

        // sort the list to implement the disable checkbox to divide from the enable
        Collections.sort(inputElement, new Comparator<MetadataUpdateCheckResult>() {

            public int compare(MetadataUpdateCheckResult o1, MetadataUpdateCheckResult o2) {

                return o1.toString().compareTo(o2.toString());

            }
        });

        tableViewerCreator.init(inputElement);

        addSelectionButtons(composite);

        Dialog.applyDialogFont(composite);

        return composite;
    }

    /**
     * create columns".
     * 
     * @param tableViewerCreator
     */
    protected void createColumns(TableViewerCreator<MetadataUpdateCheckResult> tableViewerCreator) {

        // the column "checkbox"
        TableViewerCreatorColumn column;
        column = new TableViewerCreatorColumn(tableViewerCreator);
        column.setTitle(""); //$NON-NLS-1$
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<MetadataUpdateCheckResult, Object>() {

            public Boolean get(MetadataUpdateCheckResult bean) {
                return bean.isChecked();
            }

            public void set(MetadataUpdateCheckResult bean, Object value) {

                bean.setChecked(((Boolean) value).booleanValue());
            }

        });
        column.setTableEditorContent(new CheckboxTableEditorContent());
        column.setCellEditor(new TextCellEditor(tableViewerCreator.getTable()));
        column.setModifiable(true);
        column.setDefaultDisplayedValue(""); //$NON-NLS-1$
        column.setWidth(SIZING_COLUMN_WIDTH);

        // the column "Node"
        column = new TableViewerCreatorColumn(tableViewerCreator);
        column.setTitle(Messages.getString("MetadataUpdateCheckDialog.column.nodeName")); //$NON-NLS-1$
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<MetadataUpdateCheckResult, Object>() {

            public String get(MetadataUpdateCheckResult bean) {
                return bean.getNode().getUniqueName();
            }

            public void set(MetadataUpdateCheckResult bean, Object value) {

            }

        });
        column.setSortable(true);
        column.setModifiable(false);
        column.setWidth(SIZING_COLUMN_WIDTH * 7);

        // the column "RepositoryType"
        column = new TableViewerCreatorColumn(tableViewerCreator);
        column.setTitle(Messages.getString("MetadataUpdateCheckDialog.column.repositoryType")); //$NON-NLS-1$
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<MetadataUpdateCheckResult, Object>() {

            public String get(MetadataUpdateCheckResult bean) {
                return bean.getRepositoryType().name();
            }

            public void set(MetadataUpdateCheckResult bean, Object value) {

            }

        });
        column.setSortable(true);
        column.setModifiable(false);
        column.setWidth(SIZING_COLUMN_WIDTH * 7);

        // the column "ResultType"
        column = new TableViewerCreatorColumn(tableViewerCreator);
        column.setTitle(Messages.getString("MetadataUpdateCheckDialog.column.resultType")); //$NON-NLS-1$
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<MetadataUpdateCheckResult, Object>() {

            public String get(MetadataUpdateCheckResult bean) {
                return bean.getResultType().getDisplayName();
            }

            public void set(MetadataUpdateCheckResult bean, Object value) {

            }

        });
        column.setSortable(true);
        column.setModifiable(false);
        column.setWidth(SIZING_COLUMN_WIDTH * 10);

    }

    /**
     * The <code>ListSelectionDialog</code> implementation of this <code>Dialog</code> method builds a list of the
     * selected elements for later retrieval by the client and closes this dialog.
     */
    @Override
    protected void okPressed() {

        List<MetadataUpdateCheckResult> list = new ArrayList<MetadataUpdateCheckResult>();

        Table table = tableViewerCreator.getTable();
        if (table.isDisposed()) {
            return;
        }

        TableItem[] items = table.getItems();
        for (int i = 0; i < items.length; i++) {
            MetadataUpdateCheckResult result = (MetadataUpdateCheckResult) items[i].getData();
            list.add(result);
        }

        setResult(list);

        super.okPressed();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.window.Window#canHandleShellCloseEvent()
     */
    @Override
    protected boolean canHandleShellCloseEvent() {
        return false;
    }

    /**
     * Sets the inputElement.
     * 
     * @param inputElement the inputElement to set
     */
    public void setInputElement(List<MetadataUpdateCheckResult> inputElement) {
        this.inputElement = inputElement;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.dialogs.SelectionDialog#createButtonsForButtonBar(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
    }
}

/**
 * This class provide for the TableEditor. <br/>
 * 
 */
class CheckCellModifier extends DefaultCellModifier {

    /**
     * CheckCellModifier constructor comment.<br/>
     * 
     * @param tableViewerCreator
     */
    public CheckCellModifier(TableViewerCreator tableViewerCreator) {
        super(tableViewerCreator);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.swt.tableviewer.behavior.DefaultCellModifier#canModify(java.lang.Object,
     * java.lang.String)
     */
    @Override
    public boolean canModify(Object element, String property) {
        MetadataUpdateCheckResult result = (MetadataUpdateCheckResult) element;
        // here only the type of ResultType.change can modify in the checkbox
        if (result.getResultType() == MetadataUpdateCheckResult.ResultType.change) {
            return true;
        }
        return false;
    }
}
