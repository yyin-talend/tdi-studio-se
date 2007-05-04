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
package org.talend.repository.ui.wizards.metadata.connection.files.ldif;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingEnumeration;
import javax.naming.directory.Attributes;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.talend.commons.ui.swt.dialogs.ErrorDialogWidthDetailArea;
import org.talend.commons.ui.swt.extended.table.AbstractExtendedTableViewer;
import org.talend.commons.ui.swt.extended.table.ExtendedTableModel;
import org.talend.commons.ui.swt.formtools.Form;
import org.talend.commons.ui.swt.formtools.LabelledCheckboxCombo;
import org.talend.commons.ui.swt.formtools.UtilsButton;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreatorColumn;
import org.talend.commons.utils.data.bean.IBeanPropertyAccessors;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.utils.XmlArray;
import org.talend.repository.i18n.Messages;
import org.talend.repository.preview.ProcessDescription;
import org.talend.repository.ui.swt.preview.ShadowProcessPreview;
import org.talend.repository.ui.swt.utils.AbstractLdifFileStepForm;
import org.talend.repository.ui.utils.ShadowProcessHelper;

/**
 * @author cantoine
 * 
 */
public class LdifFileStep2Form extends AbstractLdifFileStepForm {

    private static Logger log = Logger.getLogger(LdifFileStep2Form.class);

    private static final String[] STRING_NUMBERS_DATA = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$ //$NON-NLS-12$ //$NON-NLS-13$ //$NON-NLS-14$ //$NON-NLS-15$
            "16", "17", "18", "19", "20" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

    /**
     * Fields use to preview.
     */
    private LabelledCheckboxCombo rowsToSkipLimitCheckboxCombo;

    private Group previewGroup;

    public static List<String> itemTableName;

    private Button previewButton;

    private Label previewInformationLabel;

    private ShadowProcessPreview ldifFilePreview;

    /**
     * Another.
     */

    private UtilsButton cancelButton;

    private boolean readOnly;

    private ExtendedTableModel<String> attributeModel;

    private AbstractExtendedTableViewer<String> tableEditorView;

    /**
     * Constructor to use by RCP Wizard.
     * 
     * @param Composite
     * @param Wizard
     * @param Style
     */
    public LdifFileStep2Form(Composite parent, ConnectionItem connectionItem) {
        super(parent, connectionItem);
        setupForm();
    }

    /**
     * 
     * Initialize value, forceFocus first field.
     */
    protected void initialize() {

        int i = getConnection().getLimitEntry();
        if (i > 0) {
            rowsToSkipLimitCheckboxCombo.setText("" + getConnection().getLimitEntry()); //$NON-NLS-1$
        }
        checkFieldsValue();
    }

    /**
     * DOC ocarbone Comment method "adaptFormToReadOnly".
     */
    protected void adaptFormToReadOnly() {
        readOnly = isReadOnly();
//        rowsToSkipLimitCheckboxCombo.setReadOnly(isReadOnly());
    }

    /**
     * 
     * addGroupAttributes.
     */
    private void addGroupAttributes(final Composite mainComposite, final int width, final int height) {
        // Group Schema Viewer
        Group group = Form.createGroup(mainComposite, 1, Messages.getString("LdifFileStep2Form.group.listAttributes"), height); //$NON-NLS-1$

        attributeModel = new ExtendedTableModel<String>();
        attributeModel.registerDataList(itemTableName);
        tableEditorView = new AbstractExtendedTableViewer<String>(attributeModel, group) {

            /*
             * (non-Javadoc)
             * 
             * @see org.talend.commons.ui.swt.extended.macrotable.AbstractExtendedTableViewer#setTableViewerCreatorOptions(org.talend.commons.ui.swt.tableviewer.TableViewerCreator)
             */
            @Override
            protected void setTableViewerCreatorOptions(TableViewerCreator<String> newTableViewerCreator) {
                super.setTableViewerCreatorOptions(newTableViewerCreator);
                newTableViewerCreator.setFirstColumnMasked(false);
                newTableViewerCreator.setCheckboxInFirstColumn(true);
            }

            @Override
            protected void createColumns(TableViewerCreator<String> tableViewerCreator, Table table) {
                TableViewerCreatorColumn column = new TableViewerCreatorColumn(tableViewerCreator);
                column.setBeanPropertyAccessors(new IBeanPropertyAccessors<String, String>() {

                    public String get(String bean) {
                        return bean.toString();
                    }

                    public void set(String bean, String value) {
                    }

                });
                column.setTitle(Messages.getString("LdifFileStep2Form.columnTtitle.attributes")); //$NON-NLS-1$
                column.setWeight(100);

            }

        };
// uncomment this block for activate the ToolBar MoveUp and MoveDown Lines
//        new ExtendedTableToolbarView(group, SWT.NONE, tableEditorView) {
//
//            /*
//             * (non-Javadoc)
//             * 
//             * @see org.talend.core.ui.extended.ExtendedTableToolbarView#createComponents(org.eclipse.swt.widgets.Composite)
//             */
//            @Override
//            protected void createComponents(Composite parent) {
//                moveUpButton = createMoveUpPushButton();
//                moveDownButton = createMoveDownPushButton();
//            }
//
//        };

    }

    /**
     * populateLdifAttributes. method to populate the Table of Attributes to read the Ldif file
     * 
     */
    protected void populateLdifAttributes() throws IOException, Exception {

        itemTableName = new ArrayList<String>();

        String filename = new String(getConnection().getFilePath());
        Attributes entry = null;
        BufferedReader bufReader = null;

        bufReader = new BufferedReader(new FileReader(filename), 1024);
        LDIFReader ldif = new LDIFReader(bufReader);
        itemTableName = new ArrayList<String>();

        //EVOLUTION cantoine : if we would add a LIMIT of ENTRY read, implement this limit by report with Limit Entry
        while ((entry = ldif.getNext()) != null) {
            NamingEnumeration idsEnum = entry.getIDs();
            while (idsEnum.hasMore()) {
                String attributeId = (String) idsEnum.next();
                if (!itemTableName.contains(attributeId)) {
                    itemTableName.add(attributeId);
                }
            }
        }
    }

    /**
     * checkTheRightAttributes.
     * 
     * @param getConnection().getValue() Checked Attribute Checked in EMF model
     */
    protected void checkTheRightAttributes(List<String> attribute) {

        TableItem[] tableItems = tableEditorView.getTableViewerCreator().getTable().getItems();
        for (int j = 0; j < tableItems.length; j++) {
            TableItem tableItem = tableItems[j];
            for (int i = 0; i < attribute.size(); i++) {
                String attributeName = attribute.get(i);
                if (attributeName != null && !("").equals(attributeName)) { //$NON-NLS-1$
                    if (tableItem.getText().equals(attributeName)) {
                        tableItem.setChecked(true);
                        break;
                    }
                }
            }
        }
    }

    /**
     * add field to Group Limit.
     * 
     * @param mainComposite
     * @param form
     * @param width
     * @param height
     */
    private void addGroupLimit(final Composite mainComposite, final int width, final int height) {
        // Composite Limited rows
        Group group = Form.createGroup(mainComposite, 1, Messages.getString("FileStep2.groupLimitOfRows"), height); //$NON-NLS-1$
        Composite compositeLimit = Form.startNewDimensionnedGridLayout(group, 3, width, height);

        // Information Limit
        Label info = new Label(compositeLimit, SWT.NONE);
        GridData gridData = new GridData();
        gridData.horizontalSpan = 3;
        info.setLayoutData(gridData);
        info.setText(Messages.getString("FileStep2.groupLimitOfRowsTip")); //$NON-NLS-1$

        // Limit
        rowsToSkipLimitCheckboxCombo = new LabelledCheckboxCombo(compositeLimit, Messages.getString("FileStep2.limit"), Messages //$NON-NLS-1$
                .getString("FileStep2.limitTip"), STRING_NUMBERS_DATA, 1, true, SWT.NONE); //$NON-NLS-1$
    }

    /**
     * add Field to Group File Viewer.
     * 
     * @param parent
     * @param form
     * @param width
     * @param height
     */
    private void addGroupFileViewer(final Composite parent, final int width, int height) {
        // composite Ldif File Preview
        previewGroup = Form.createGroup(parent, 1, Messages.getString("FileStep2.groupPreview"), height); //$NON-NLS-1$
        Composite compositeLdifFilePreviewButton = Form.startNewDimensionnedGridLayout(previewGroup, 4, width, HEIGHT_BUTTON_PIXEL);
        height = height - HEIGHT_BUTTON_PIXEL - 15;

        // Ldif File Preview Info
        previewButton = new Button(compositeLdifFilePreviewButton, SWT.NONE);
        previewButton.setText(Messages.getString("FileStep2.refreshPreview")); //$NON-NLS-1$
        previewButton.setSize(WIDTH_BUTTON_PIXEL, HEIGHT_BUTTON_PIXEL);

        // simple space
        new Label(compositeLdifFilePreviewButton, SWT.NONE);
        // Information Label
        previewInformationLabel = new Label(compositeLdifFilePreviewButton, SWT.NONE);
        previewInformationLabel
                .setText("                                                                                                                        "); //$NON-NLS-1$
        previewInformationLabel.setForeground(getDisplay().getSystemColor(SWT.COLOR_BLUE));

        Composite compositeLdifFilePreview = Form.startNewDimensionnedGridLayout(previewGroup, 1, width, height);

        // Ldif File Preview
        ldifFilePreview = new ShadowProcessPreview(compositeLdifFilePreview, null, width, height - 10);
        ldifFilePreview.newTablePreview();
    }

    protected void addFields() {

        // compositeFile Main Fields
        Composite mainComposite = Form.startNewGridLayout(this, 2);
        addGroupAttributes(mainComposite, 300, 115);
//        addGroupLimit(mainComposite, 300, 85);
        addGroupFileViewer(this, 700, 180);

        if (!isInWizard()) {
            // Bottom Button
            Composite compositeBottomButton = Form.startNewGridLayout(this, 2, false, SWT.CENTER, SWT.CENTER);
            // Button Cancel
            cancelButton = new UtilsButton(compositeBottomButton, Messages.getString("CommonWizard.cancel"), WIDTH_BUTTON_PIXEL, //$NON-NLS-1$
                    HEIGHT_BUTTON_PIXEL);
        }
        addUtilsButtonListeners();
    }

    /**
     * create ProcessDescription and set it.
     * 
     * WARNING ::field FieldSeparator, RowSeparator, EscapeChar and TextEnclosure are surround by double quote.
     * 
     * @param getConnection()
     * 
     * @return processDescription
     */
    private ProcessDescription getProcessDescription() {

        ProcessDescription processDescription = ShadowProcessHelper.getProcessDescription(getConnection());
        return processDescription;
    }

    /**
     * clear the table preview.
     */
    void clearPreview() {
        ldifFilePreview.clearTablePreview();
    }

    /**
     * refreshPreview use ShadowProcess to refresh the preview.
     */
    void refreshPreview() {
        clearPreview();

        // if no file, the process don't be executed
        if (getConnection().getFilePath() == null || getConnection().getFilePath().equals("")) { //$NON-NLS-1$
            previewInformationLabel.setText("   " + Messages.getString("FileStep2.filePathIncomplete")); //$NON-NLS-1$ //$NON-NLS-2$
            return;
        }

        // if incomplete settings, , the process don't be executed
        if (!checkFieldsValue()) {
            previewInformationLabel.setText("   " + Messages.getString("FileStep2.settingsIncomplete")); //$NON-NLS-1$ //$NON-NLS-2$
            return;
        }

        previewInformationLabel.setText("   " + Messages.getString("FileStep2.previewProgress")); //$NON-NLS-1$ //$NON-NLS-2$

        // get the XmlArray width an adapt ProcessDescription
        try {

            ProcessDescription processDescription = getProcessDescription();

            XmlArray xmlArray = ShadowProcessHelper.getXmlArray(processDescription, "FILE_LDIF"); //$NON-NLS-1$
            if (xmlArray == null) {
                previewInformationLabel.setText("   " + Messages.getString("FileStep2.previewFailure")); //$NON-NLS-1$ //$NON-NLS-2$
            } else {
                previewInformationLabel.setText("   " + Messages.getString("FileStep2.previewIsDone")); //$NON-NLS-1$ //$NON-NLS-2$

                // refresh TablePreview on this step
                ldifFilePreview.refreshTablePreview(xmlArray, false, processDescription);
                previewInformationLabel.setText(""); //$NON-NLS-1$
            }

        } catch (CoreException e) {
            previewInformationLabel.setText("   " + Messages.getString("FileStep2.previewFailure")); //$NON-NLS-1$ //$NON-NLS-2$
            new ErrorDialogWidthDetailArea(getShell(), PID, Messages.getString("FileStep2.previewFailure"), e.getMessage()); //$NON-NLS-1$
            log.error(Messages.getString("FileStep2.previewFailure") + " " + e.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$
        }
    }

    /**
     * Main Fields addControls.
     */
    protected void addFieldsListeners() {
//        addFieldsListenersGroupsRowToSkipAndLimit();
    }

    /**
     * DOC ocarbone Comment method "setCharFlag".
     * 
     * @param escapeCharFlag2
     * @param text
     */
    protected void setCharFlag(Label label, String string) {
        label.setText(string);
    }

    /**
     * add Controls to group Row To Skip and Limit.
     */
    private void addFieldsListenersGroupsRowToSkipAndLimit() {
        // Event modify
        ArrayList<LabelledCheckboxCombo> labelledCheckboxCombo2Control = new ArrayList<LabelledCheckboxCombo>();
        labelledCheckboxCombo2Control.add(rowsToSkipLimitCheckboxCombo);

        Iterator<LabelledCheckboxCombo> iCheckboxCombo;
        LabelledCheckboxCombo labelledCheckboxCombo;

        // Event : keyPressed
        for (iCheckboxCombo = labelledCheckboxCombo2Control.iterator(); iCheckboxCombo.hasNext();) {
            labelledCheckboxCombo = iCheckboxCombo.next();

            // Event Key (numeric value only)
            labelledCheckboxCombo.addKeyListener(new KeyAdapter() {

                public void keyPressed(KeyEvent e) {
                    if (Character.getNumericValue(e.character) >= 10) {
                        e.doit = false;
                    }
                }
            });
        }

        rowsToSkipLimitCheckboxCombo.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                if (!rowsToSkipLimitCheckboxCombo.isEmpty()) {
                    if (!rowsToSkipLimitCheckboxCombo.isInteger() || rowsToSkipLimitCheckboxCombo.getText().equals("0")) { //$NON-NLS-1$
                        rowsToSkipLimitCheckboxCombo.deselectAll();
                        getConnection().setUseLimit(rowsToSkipLimitCheckboxCombo.isChecked());
                        getConnection().setLimitEntry(0);
                    } else {
                        getConnection().setLimitEntry(new Integer(rowsToSkipLimitCheckboxCombo.getText()));
                    }
                } else {
                    getConnection().setUseLimit(rowsToSkipLimitCheckboxCombo.isChecked());
                    getConnection().setLimitEntry(0);
                }
                checkFieldsValue();
            }
        });
    }

    /**
     * Ensures that fields are set. Update checkEnable / use to checkConnection().
     * 
     * @return
     */
    protected boolean checkFieldsValue() {
        previewInformationLabel.setText("   " + Messages.getString("FileStep2.settingsIncomplete")); //$NON-NLS-1$ //$NON-NLS-2$
        updateStatus(IStatus.OK, null);
        previewButton.setEnabled(false);

        // Labelled Checkbox Combo (Row to Skip and Limit)
//        ArrayList<LabelledCheckboxCombo> labelledCheckboxCombo2Control = new ArrayList<LabelledCheckboxCombo>();
//        labelledCheckboxCombo2Control.add(rowsToSkipLimitCheckboxCombo);
//
//        Iterator<LabelledCheckboxCombo> iCheckboxCombo;
//        LabelledCheckboxCombo labelledCheckboxCombo;
//
//        for (iCheckboxCombo = labelledCheckboxCombo2Control.iterator(); iCheckboxCombo.hasNext();) {
//            labelledCheckboxCombo = iCheckboxCombo.next();
//            // if the checkbox is checked, check Numeric value
//            if (labelledCheckboxCombo.getCheckbox().getSelection()) {
//                if (labelledCheckboxCombo.getText() == "") {
//                    updateStatus(IStatus.ERROR, labelledCheckboxCombo.getLabelText() + Messages.getString("FileStep2.mustBePrecised"));
//                    return false;
//                }
//            }
//        }
        previewInformationLabel.setText(""); //$NON-NLS-1$
        previewButton.setEnabled(true);
        updateStatus(IStatus.OK, null);
        return true;
    }

    /**
     * addButtonControls.
     * 
     * @param cancelButton
     */
    protected void addUtilsButtonListeners() {

        // Event PreviewButton
        previewButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                if (!previewButton.getText().equals(Messages.getString("FileStep2.wait"))) { //$NON-NLS-1$
                    previewButton.setText(Messages.getString("FileStep2.wait")); //$NON-NLS-1$
                    refreshPreview();
                } else {
                    previewButton.setText(Messages.getString("FileStep2.refreshPreview")); //$NON-NLS-1$
                }
            }
        });

        if (cancelButton != null) {
            // Event CancelButton
            cancelButton.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(final SelectionEvent e) {
                    getShell().close();
                }
            });
        }

        // Event checkBox action
        final Table table = tableEditorView.getTableViewerCreator().getTable();
        table.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                if (!table.getEnabled()) {
                    table.setEnabled(true);
                    if (e.detail == SWT.CHECK) {
                        TableItem tableItem = (TableItem) e.item;
                        boolean promptNeeded = tableItem.getChecked();
                        if (promptNeeded) {
                            getConnection().getValue().add(tableItem.getText());
                        } else {
                            getConnection().getValue().remove(tableItem.getText());
                        }
                    }
                } else {
                    table.setEnabled(false);
                }
                checkFieldsValue();
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.widgets.Control#setVisible(boolean)
     * 
     */
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (super.isVisible()) {
            
            try {
                populateLdifAttributes();
            } catch (Exception e) {
                new ErrorDialogWidthDetailArea(getShell(), PID, Messages.getString("LdifFileStep2.previewFailure"), e.getMessage()); //$NON-NLS-1$
                log.error(Messages.getString("LdifFileStep2.previewFailure") + " " + e.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$
                updateStatus(IStatus.ERROR, Messages.getString("LdifFileStep2.previewFailure")); //$NON-NLS-1$
            }
            attributeModel.registerDataList(itemTableName);
            
            // Refresh the preview width the adapted rowSeparator
            // If metadata exist, refreshMetadata
            if (getConnection().getFilePath() != null && !("").equals(getConnection().getFilePath()) //$NON-NLS-1$
                    && getConnection().getValue() != null && !getConnection().getValue().isEmpty()) {
                 refreshPreview();
            }
            if (getConnection().getValue() != null && !getConnection().getValue().isEmpty()) {
                checkTheRightAttributes(getConnection().getValue());
            }
            if (isReadOnly() != readOnly) {
                adaptFormToReadOnly();
            }
        }
    }
}
