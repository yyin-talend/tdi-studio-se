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
package org.talend.repository.ui.wizards.metadata.connection.files.excel;

import java.util.ArrayList;
import java.util.Iterator;

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
import org.talend.commons.ui.swt.dialogs.ErrorDialogWidthDetailArea;
import org.talend.commons.ui.swt.formtools.Form;
import org.talend.commons.ui.swt.formtools.LabelledCheckboxCombo;
import org.talend.commons.ui.swt.formtools.LabelledCombo;
import org.talend.commons.ui.swt.formtools.LabelledText;
import org.talend.commons.ui.swt.formtools.UtilsButton;
import org.talend.commons.ui.swt.thread.SWTUIThreadProcessor;
import org.talend.core.model.metadata.EMetadataEncoding;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.utils.TalendTextUtils;
import org.talend.core.utils.CsvArray;
import org.talend.repository.i18n.Messages;
import org.talend.repository.preview.ExcelSchemaBean;
import org.talend.repository.preview.ProcessDescription;
import org.talend.repository.ui.swt.preview.ShadowProcessPreview;
import org.talend.repository.ui.swt.utils.AbstractExcelFileStepForm;
import org.talend.repository.ui.swt.utils.IRefreshable;
import org.talend.repository.ui.utils.ShadowProcessHelper;

/**
 * @author xye
 * 
 */
public class ExcelFileStep2Form extends AbstractExcelFileStepForm implements IRefreshable {

    /**
     * DOC YeXiaowei ExcelFileStep2Form constructor comment.
     * 
     * @param parent
     * @param connectionItem
     */
    public ExcelFileStep2Form(Composite parent, ConnectionItem connectionItem) {
        super(parent, connectionItem);
        setupForm();
    }

    private static Logger log = Logger.getLogger(ExcelFileStep2Form.class);

    private static final String EMPTY_VALUE = Messages.getString("FileStep2.empty"); //$NON-NLS-1$

    private static final String[] ESCAPE_CHAR_DATA = { EMPTY_VALUE,
            TalendTextUtils.addQuotes("\""), TalendTextUtils.addQuotes("\'"), TalendTextUtils.addQuotes("\\\\") }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

    private static final String[] STRING_NUMBERS_DATA = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$ //$NON-NLS-12$ //$NON-NLS-13$
            "14", "15", "16", "17", "18", "19", "20" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$

    /**
     * Main Fields.
     */
    private LabelledCombo encodingCombo;

    private LabelledCheckboxCombo rowsToSkipHeaderCheckboxCombo;

    private LabelledCheckboxCombo rowsToSkipLimitCheckboxCombo;

    private LabelledCheckboxCombo rowsToSkipFooterCheckboxCombo;

    private Button emptyRowsToSkipCheckbox;

    private Button advanceSeparatorCheckbox;

    private LabelledText thousandSeparaotrText;

    private LabelledText decimalSeparatorText;

    private LabelledText firstColumnText;

    private LabelledText lastColumnText;

    /**
     * Fields use to preview.
     */

    private Group previewGroup;

    private Button firstRowIsCaptionCheckbox;

    private Button previewButton;

    private Label previewInformationLabel;

    private ShadowProcessPreview excelProcessPreview;

    SWTUIThreadProcessor processor = new PreviewProcessor();

    private UtilsButton cancelButton;

    private boolean readOnly;

    /**
     * 
     * Initialize value, forceFocus first field.
     */
    @Override
    protected void initialize() {

        // Fields to the Group Delimited File Settings
        if (getConnection().getEncoding() != null && !getConnection().getEncoding().equals("")) { //$NON-NLS-1$
            encodingCombo.setText(getConnection().getEncoding());
        } else {
            encodingCombo.select(0);
            getConnection().setEncoding(encodingCombo.getText());
        }

        // Fields to the Group Rows To Skip
        int i = getConnection().getHeaderValue();
        if (i > 0) {
            rowsToSkipHeaderCheckboxCombo.setText("" + getConnection().getHeaderValue()); //$NON-NLS-1$
        }
        rowsToSkipHeaderCheckboxCombo.getCombo().setEnabled(i > 0);
        i = getConnection().getFooterValue();
        if (i > 0) {
            rowsToSkipFooterCheckboxCombo.setText("" + getConnection().getFooterValue()); //$NON-NLS-1$
        }
        rowsToSkipFooterCheckboxCombo.getCombo().setEnabled(i > 0);
        i = getConnection().getLimitValue();
        if (i > 0) {
            rowsToSkipLimitCheckboxCombo.setText("" + getConnection().getLimitValue()); //$NON-NLS-1$
        }
        rowsToSkipLimitCheckboxCombo.getCombo().setEnabled(i > 0);

        emptyRowsToSkipCheckbox.setSelection(getConnection().isRemoveEmptyRow());
        firstRowIsCaptionCheckbox.setSelection(getConnection().isFirstLineCaption());

        emptyRowsToSkipCheckbox.setSelection(getConnection().isRemoveEmptyRow());

        int firstColumn = stringToInteger(getConnection().getFirstColumn());
        firstColumnText.setText(firstColumn < 0 ? "1" : getConnection().getFirstColumn());

        int lastColumn = stringToInteger(getConnection().getLastColumn());
        lastColumnText.setText(lastColumn < 0 ? "" : getConnection().getLastColumn());

        advanceSeparatorCheckbox.setSelection(getConnection().isAdvancedSpearator());

        String ts = this.getConnection().getThousandSeparator();
        if (ts == null || ts.equals("")) {
            thousandSeparaotrText.setText("\',\'");
        } else {
            thousandSeparaotrText.setText(ts);
        }

        String ds = this.getConnection().getDecimalSeparator();
        if (ds == null || ds.equals("")) {
            decimalSeparatorText.setText("\'.\'");
        } else {
            decimalSeparatorText.setText(ds);
        }

        checkFieldsValue();
    }

    private int stringToInteger(String intString) {
        if (intString == null || intString.equals("")) {
            return -1;
        }
        try {
            return Integer.parseInt(intString);
        } catch (Exception e) {
            return -1;
        }

    }

    /**
     * DOC ocarbone Comment method "adaptFormToReadOnly".
     */
    @Override
    protected void adaptFormToReadOnly() {
        readOnly = isReadOnly();
        encodingCombo.setReadOnly(isReadOnly());
        rowsToSkipHeaderCheckboxCombo.setReadOnly(isReadOnly());
        rowsToSkipFooterCheckboxCombo.setReadOnly(isReadOnly());
        rowsToSkipLimitCheckboxCombo.setReadOnly(isReadOnly());
        emptyRowsToSkipCheckbox.setEnabled(!isReadOnly());
        firstRowIsCaptionCheckbox.setEnabled(!isReadOnly());
    }

    /**
     * add Field to Group Delimited File Settings.
     * 
     * @param mainComposite
     * @param form
     * @param width
     * @param height
     */
    private void addGroupDelimitedFileSettings(final Composite mainComposite, final int width, final int height) {
        Group group = Form.createGroup(mainComposite, 2, Messages.getString("FileStep2.groupDelimitedFileSettings"), height); //$NON-NLS-1$
        Composite compositeFileDelimitor = Form.startNewDimensionnedGridLayout(group, 4, width, height);

        EMetadataEncoding[] values = EMetadataEncoding.values();
        String[] encodingData = new String[values.length];
        for (int j = 0; j < values.length; j++) {
            encodingData[j] = values[j].getName();
        }

        encodingCombo = new LabelledCombo(compositeFileDelimitor, Messages.getString("FileStep2.encoding"), Messages //$NON-NLS-1$
                .getString("FileStep2.encodingTip"), encodingData, 3, true, SWT.NONE); //$NON-NLS-1$

        advanceSeparatorCheckbox = new Button(compositeFileDelimitor, SWT.CHECK);
        advanceSeparatorCheckbox.setText("Advanced separator(for number)");

        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.horizontalSpan = 4;
        advanceSeparatorCheckbox.setLayoutData(data);

        thousandSeparaotrText = new LabelledText(compositeFileDelimitor, "Thousands separator:", 3);

        decimalSeparatorText = new LabelledText(compositeFileDelimitor, "Decimal separator:", 3);

    }

    private void addGroupDieOnErrorSettings(final Composite mainComposite, final int width, final int height) {
        Group group = Form.createGroup(mainComposite, 2, "Ext setting");
        Composite compositeExt = Form.startNewDimensionnedGridLayout(group, 4, width, height);

        firstColumnText = new LabelledText(compositeExt, "First column:", 3);
        firstColumnText.setText("1"); // Default 1

        lastColumnText = new LabelledText(compositeExt, "Last column:", 3); // Default no value
    }

    private void addGroupRowsToSkip(final Composite mainComposite, final int width, final int height) {
        // compositerowsToSkip Main Fields
        Group group = Form.createGroup(mainComposite, 1, Messages.getString("FileStep2.groupRowsToSkip"), height); //$NON-NLS-1$
        Composite compositeRowsToSkip = Form.startNewDimensionnedGridLayout(group, 3, width - 100, height);

        // Information rowsToSkip
        Label info = new Label(compositeRowsToSkip, SWT.NONE);
        GridData gridData = new GridData();
        gridData.horizontalSpan = 3;
        info.setLayoutData(gridData);
        info.setText(Messages.getString("FileStep2.rowsToSkipTip")); //$NON-NLS-1$

        // Header
        rowsToSkipHeaderCheckboxCombo = new LabelledCheckboxCombo(compositeRowsToSkip, Messages.getString("FileStep2.header"), //$NON-NLS-1$
                Messages.getString("FileStep2.headerTip"), STRING_NUMBERS_DATA, 1, true, SWT.NONE); //$NON-NLS-1$

        // Footer
        rowsToSkipFooterCheckboxCombo = new LabelledCheckboxCombo(compositeRowsToSkip, Messages.getString("FileStep2.footer"), //$NON-NLS-1$
                Messages.getString("FileStep2.footerTip"), STRING_NUMBERS_DATA, 1, true, SWT.NONE); //$NON-NLS-1$

        // Empty row
        emptyRowsToSkipCheckbox = new Button(compositeRowsToSkip, SWT.CHECK);
        emptyRowsToSkipCheckbox.setText(Messages.getString("FileStep2.removeEmptyRow")); //$NON-NLS-1$
        emptyRowsToSkipCheckbox.setAlignment(SWT.LEFT);
        gridData = new GridData(SWT.FILL, SWT.BOTTOM, true, false);
        gridData.horizontalSpan = 3;
        emptyRowsToSkipCheckbox.setLayoutData(gridData);

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
        Group group = Form.createGroup(mainComposite, 2, Messages.getString("FileStep2.groupLimitOfRows"), height); //$NON-NLS-1$
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
        // composite Delimited File Preview
        previewGroup = Form.createGroup(parent, 1, Messages.getString("FileStep2.groupPreview"), height); //$NON-NLS-1$
        Composite compositeDelimitedFilePreviewButton = Form.startNewDimensionnedGridLayout(previewGroup, 4, width,
                HEIGHT_BUTTON_PIXEL);
        height = height - HEIGHT_BUTTON_PIXEL - 15;

        // Delimited File Preview Info
        firstRowIsCaptionCheckbox = new Button(compositeDelimitedFilePreviewButton, SWT.CHECK);
        firstRowIsCaptionCheckbox.setText(Messages.getString("FileStep2.firstRowsIsCaption")); //$NON-NLS-1$
        firstRowIsCaptionCheckbox.setAlignment(SWT.LEFT);

        previewButton = new Button(compositeDelimitedFilePreviewButton, SWT.NONE);
        previewButton.setText(Messages.getString("FileStep2.refreshPreview")); //$NON-NLS-1$
        previewButton.setSize(WIDTH_BUTTON_PIXEL, HEIGHT_BUTTON_PIXEL);

        // simple space
        new Label(compositeDelimitedFilePreviewButton, SWT.NONE);
        // Information Label
        previewInformationLabel = new Label(compositeDelimitedFilePreviewButton, SWT.NONE);
        previewInformationLabel
                .setText("                                                                                                                        "); //$NON-NLS-1$
        previewInformationLabel.setForeground(getDisplay().getSystemColor(SWT.COLOR_BLUE));

        Composite compositeDelimitedFilePreview = Form.startNewDimensionnedGridLayout(previewGroup, 1, width, height);

        // Delimited File Preview
        excelProcessPreview = new ShadowProcessPreview(compositeDelimitedFilePreview, null, width, height - 10);
        excelProcessPreview.newTablePreview();
    }

    @Override
    protected void addFields() {

        Composite mainComposite = Form.startNewGridLayout(this, 2);

        addGroupDelimitedFileSettings(mainComposite, 280, 100);
        addGroupRowsToSkip(mainComposite, 300, 100);
        addGroupDieOnErrorSettings(mainComposite, 280, 100);
        addGroupLimit(mainComposite, 300, 100);

        addGroupFileViewer(this, 700, 210);

        if (!isInWizard()) {
            // Bottom Button
            Composite compositeBottomButton = Form.startNewGridLayout(this, 2, false, SWT.CENTER, SWT.CENTER);

            // Button Cancel
            cancelButton = new UtilsButton(compositeBottomButton, Messages.getString("CommonWizard.cancel"), WIDTH_BUTTON_PIXEL, //$NON-NLS-1$
                    HEIGHT_BUTTON_PIXEL);
        }
    }

    /**
     * Set field advanced separator, decimal separator , thousand separator DOC YeXiaowei Comment method
     * "getProcessDescription".
     * 
     * @return
     */
    private ProcessDescription getProcessDescription() {

        ProcessDescription processDescription = (ProcessDescription) ShadowProcessHelper.getProcessDescription(getConnection());

        // Adapt Header width firstRowIsCaption to preview the first line on caption or not
        Integer i = 0;
        if (rowsToSkipHeaderCheckboxCombo.isInteger()) {
            i = new Integer(rowsToSkipHeaderCheckboxCombo.getText());
        }
        if (firstRowIsCaptionCheckbox.getSelection()) {
            i--;
        }
        processDescription.setHeaderRow(i);

        // adapt the limit to the preview
        processDescription.setLimitRows(maximumRowsToPreview);
        if (rowsToSkipLimitCheckboxCombo.isInteger()) {
            i = new Integer(rowsToSkipLimitCheckboxCombo.getText());
            if (firstRowIsCaptionCheckbox.getSelection()) {
                i++;
            }
            if (i < maximumRowsToPreview) {
                processDescription.setLimitRows(i);
            }
        }

        processDescription.setEncoding(TalendTextUtils.addQuotes(encodingCombo.getText()));

        ExcelSchemaBean bean = new ExcelSchemaBean();

        bean.setSheetName(getConnection().getSheetName());
        bean.setFirstColumn(firstColumnText.getText());
        bean.setLastColumn(lastColumnText.getText());

        bean.setAdvancedSeparator(advanceSeparatorCheckbox.getSelection());
        bean.setThousandSeparator(thousandSeparaotrText.getText());
        bean.setDecimalSeparator(decimalSeparatorText.getText());

        processDescription.setExcelSchemaBean(bean);

        return processDescription;
    }

    /**
     * clear the table preview.
     */
    void clearPreview() {
        excelProcessPreview.clearTablePreview();
    }

    /**
     * refreshPreview use ShadowProcess to refresh the preview.
     */
    void refreshPreview() {
        processor.execute();
    }

    /**
     * Main Fields addControls.
     */
    @Override
    protected void addFieldsListeners() {
        addFieldsListenersGroupDelimitedFileSettings();
        addFieldsListenersGroupsRowToSkipAndLimit();
        addFieldsListenersDieOnError();
        addFieldsListenersGroupsFileViewer();
    }

    /**
     * add Controls to group File Viewer.
     */
    private void addFieldsListenersGroupsFileViewer() {
        // Manage rowsToSkipHeader when firstRowIsCaption is checked
        firstRowIsCaptionCheckbox.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                getConnection().setFirstLineCaption(firstRowIsCaptionCheckbox.getSelection());

                if (firstRowIsCaptionCheckbox.getSelection()) {
                    // when firstRowIsCaption is checked
                    if (rowsToSkipHeaderCheckboxCombo.isEmpty()) {
                        // at least, rowsToSkipHeader = 1
                        rowsToSkipHeaderCheckboxCombo.setText("1"); //$NON-NLS-1$
                        getConnection().setHeaderValue(1);
                    } else {
                        // rowsToSkipHeader ++
                        int value = new Integer(rowsToSkipHeaderCheckboxCombo.getText());
                        value++;
                        String newValue = new String("" + value); //$NON-NLS-1$
                        rowsToSkipHeaderCheckboxCombo.setText(newValue);
                        getConnection().setHeaderValue(new Integer(rowsToSkipHeaderCheckboxCombo.getText()));
                    }
                } else {
                    // when firstRowIsCaption isn't checked
                    if (rowsToSkipHeaderCheckboxCombo.getText().equals("1")) { //$NON-NLS-1$
                        // rowsToSkipHeader is unusable
                        rowsToSkipHeaderCheckboxCombo.deselectAll();
                        getConnection().setHeaderValue(0);
                    } else {
                        // rowsToSkipHeader --
                        int value = new Integer(rowsToSkipHeaderCheckboxCombo.getText());
                        value--;
                        String newValue = new String("" + value); //$NON-NLS-1$
                        rowsToSkipHeaderCheckboxCombo.setText(newValue);
                        getConnection().setHeaderValue(new Integer(rowsToSkipHeaderCheckboxCombo.getText()));
                    }
                }
                checkFieldsValue();
            }
        });
    }

    private void addFieldsListenersDieOnError() {
        firstColumnText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                getConnection().setFirstColumn(firstColumnText.getText());
                checkFieldsValue();
            }

        });

        lastColumnText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                getConnection().setLastColumn(lastColumnText.getText());
                checkFieldsValue();
            }

        });
    }

    /**
     * DOC xye Comment method "setCharFlag".
     * 
     * @param escapeCharFlag2
     * @param text
     */
    protected void setCharFlag(Label label, String string) {
        // PTODO OCA : when is utils in the Flag ?
        label.setText(string);
    }

    /**
     * add Controls to group Row To Skip and Limit.
     */
    private void addFieldsListenersGroupsRowToSkipAndLimit() {
        // Event modify
        ArrayList<LabelledCheckboxCombo> labelledCheckboxCombo2Control = new ArrayList<LabelledCheckboxCombo>();
        labelledCheckboxCombo2Control.add(rowsToSkipHeaderCheckboxCombo);
        labelledCheckboxCombo2Control.add(rowsToSkipLimitCheckboxCombo);
        labelledCheckboxCombo2Control.add(rowsToSkipFooterCheckboxCombo);

        Iterator<LabelledCheckboxCombo> iCheckboxCombo;
        LabelledCheckboxCombo labelledCheckboxCombo;

        // Event : keyPressed
        for (iCheckboxCombo = labelledCheckboxCombo2Control.iterator(); iCheckboxCombo.hasNext();) {
            labelledCheckboxCombo = iCheckboxCombo.next();

            labelledCheckboxCombo.addKeyListener(new KeyAdapter() {

                @Override
                public void keyPressed(KeyEvent e) {

                    String string = String.valueOf(e.character);
                    // Check if input is number, backspace key and delete key of keyboard.
                    if (!(string.matches("[0-9]*")) && e.keyCode != 8 && e.keyCode != SWT.DEL) {
                        e.doit = false;
                    }
                }
            });
        }

        // Event : Modify (to control the use of Ctrl V)
        rowsToSkipHeaderCheckboxCombo.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                if (!rowsToSkipHeaderCheckboxCombo.isEmpty()) {
                    if (!rowsToSkipHeaderCheckboxCombo.isInteger() || rowsToSkipHeaderCheckboxCombo.getText().trim().equals("0")) { //$NON-NLS-1$
                        rowsToSkipHeaderCheckboxCombo.deselectAll();
                        getConnection().setUseHeader(rowsToSkipHeaderCheckboxCombo.isChecked());
                        getConnection().setHeaderValue(0);

                        updateStatus(IStatus.ERROR, "Number allowed only.");
                        rowsToSkipHeaderCheckboxCombo.getCombo().setFocus();

                        // if rowsHeaderToSkip isn't integer or is equals to 0, the firstRowIsCaptionCheckbox is
                        // unusable.
                        firstRowIsCaptionCheckbox.setSelection(false);
                        getConnection().setFirstLineCaption(false);
                        return;
                    } else {
                        getConnection().setHeaderValue(new Integer(rowsToSkipHeaderCheckboxCombo.getText().trim()));
                        getConnection().setUseHeader(rowsToSkipHeaderCheckboxCombo.isChecked());
                        checkFieldsValue();
                    }
                } else {
                    getConnection().setUseHeader(rowsToSkipHeaderCheckboxCombo.isChecked());
                    getConnection().setHeaderValue(0);
                    checkFieldsValue();
                }
            }
        });

        rowsToSkipFooterCheckboxCombo.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                if (!rowsToSkipFooterCheckboxCombo.isEmpty()) {
                    if (!rowsToSkipFooterCheckboxCombo.isInteger() || rowsToSkipFooterCheckboxCombo.getText().trim().equals("0")) { //$NON-NLS-1$
                        rowsToSkipFooterCheckboxCombo.deselectAll();
                        getConnection().setUseFooter(rowsToSkipFooterCheckboxCombo.isChecked());
                        getConnection().setFooterValue(0);

                        updateStatus(IStatus.ERROR, "Number allowed only.");
                        rowsToSkipFooterCheckboxCombo.getCombo().setFocus();
                    } else {
                        getConnection().setFooterValue(new Integer(rowsToSkipFooterCheckboxCombo.getText().trim()));
                    }
                } else {
                    getConnection().setUseFooter(rowsToSkipFooterCheckboxCombo.isChecked());
                    getConnection().setFooterValue(0);
                }
                checkFieldsValue();
            }
        });

        rowsToSkipLimitCheckboxCombo.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                if (!rowsToSkipLimitCheckboxCombo.isEmpty()) {
                    if (!rowsToSkipLimitCheckboxCombo.isInteger() || rowsToSkipLimitCheckboxCombo.getText().trim().equals("0")) { //$NON-NLS-1$
                        rowsToSkipLimitCheckboxCombo.deselectAll();
                        getConnection().setUseLimit(rowsToSkipLimitCheckboxCombo.isChecked());
                        getConnection().setLimitValue(0);

                        updateStatus(IStatus.ERROR, "Number allowed only.");
                        rowsToSkipLimitCheckboxCombo.getCombo().setFocus();
                    } else {
                        getConnection().setLimitValue(new Integer(rowsToSkipLimitCheckboxCombo.getText()));
                    }
                } else {
                    getConnection().setUseLimit(rowsToSkipLimitCheckboxCombo.isChecked());
                    getConnection().setLimitValue(0);
                }
                checkFieldsValue();
            }
        });

        // If nothing in rowsToSkipHeader, the firstRowIsCaption mustn't be checked
        rowsToSkipHeaderCheckboxCombo.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                if ((!rowsToSkipHeaderCheckboxCombo.isChecked()) || rowsToSkipHeaderCheckboxCombo.getText().trim().equals("0")) { //$NON-NLS-1$
                    firstRowIsCaptionCheckbox.setSelection(false);
                    getConnection().setFirstLineCaption(false);
                }
            }
        });

        // empty Rows To Skip
        emptyRowsToSkipCheckbox.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                getConnection().setRemoveEmptyRow(emptyRowsToSkipCheckbox.getSelection());
            }
        });
    }

    /**
     * add Controls of Group Delimited File Settings.
     */
    private void addFieldsListenersGroupDelimitedFileSettings() {
        // Event encodingCombo
        encodingCombo.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                getConnection().setEncoding(encodingCombo.getText());
                checkFieldsValue();
            }
        });

        advanceSeparatorCheckbox.addSelectionListener(new SelectionAdapter() {

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
             */
            @Override
            public void widgetSelected(SelectionEvent e) {
                boolean select = advanceSeparatorCheckbox.getSelection();
                decimalSeparatorText.setEditable(select);
                thousandSeparaotrText.setEditable(select);
                getConnection().setAdvancedSpearator(select);
                getConnection().setThousandSeparator(thousandSeparaotrText.getText());
                getConnection().setDecimalSeparator(decimalSeparatorText.getText());
                checkFieldsValue();
            }
        });

        decimalSeparatorText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                getConnection().setDecimalSeparator(decimalSeparatorText.getText());
                checkFieldsValue();
            }

        });

        thousandSeparaotrText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                getConnection().setThousandSeparator(thousandSeparaotrText.getText());
                checkFieldsValue();
            }

        });
    }

    /**
     * Ensures that fields are set. Update checkEnable / use to checkConnection().
     * 
     * @return
     */
    @Override
    protected boolean checkFieldsValue() {

        previewInformationLabel.setText("   " + Messages.getString("FileStep2.settingsIncomplete")); //$NON-NLS-1$ //$NON-NLS-2$
        updateStatus(IStatus.OK, null);
        previewButton.setEnabled(false);

        // Labelled Checkbox Combo (Row to Skip and Limit)
        ArrayList<LabelledCheckboxCombo> labelledCheckboxCombo2Control = new ArrayList<LabelledCheckboxCombo>();
        labelledCheckboxCombo2Control.add(rowsToSkipHeaderCheckboxCombo);
        labelledCheckboxCombo2Control.add(rowsToSkipLimitCheckboxCombo);
        labelledCheckboxCombo2Control.add(rowsToSkipFooterCheckboxCombo);

        Iterator<LabelledCheckboxCombo> iCheckboxCombo;
        LabelledCheckboxCombo labelledCheckboxCombo;

        for (iCheckboxCombo = labelledCheckboxCombo2Control.iterator(); iCheckboxCombo.hasNext();) {
            labelledCheckboxCombo = iCheckboxCombo.next();
            // if the checkbox is checked, check Numeric value
            if (labelledCheckboxCombo.getCheckbox().getSelection()) {
                if (labelledCheckboxCombo.getText() == "") { //$NON-NLS-1$
                    updateStatus(IStatus.ERROR, labelledCheckboxCombo.getLabelText()
                            + " " + Messages.getString("FileStep2.mustBePrecised")); //$NON-NLS-1$
                    return false;
                }
            }
        }

        if (!checkAdvancedSetting()) {
            return false;
        }

        if (!checkFristAndLastColumn()) {
            return false;
        }

        previewInformationLabel.setText(""); //$NON-NLS-1$
        previewButton.setEnabled(true);
        updateStatus(IStatus.OK, null);
        return true;
    }

    private int getIntFromString(String s) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return -1;
        }
    }

    private boolean checkFristAndLastColumn() {
        String firstS = firstColumnText.getText();
        if (!firstS.equals("") && getIntFromString(firstS) < 0) {
            updateStatus(IStatus.ERROR, "First column value error"); //$NON-NLS-1$
            return false;
        }

        String lastS = lastColumnText.getText();
        if (!lastS.equals("") && getIntFromString(lastS) < 0) {
            updateStatus(IStatus.ERROR, "Last column value error"); //$NON-NLS-1$
            return false;
        }
        return true;

    }

    /**
     * DOC YeXiaowei Comment method "checkAdvancedSetting".
     */
    private boolean checkAdvancedSetting() {

        String thouandsSeparator = thousandSeparaotrText.getText();
        if (thouandsSeparator == null) {
            updateStatus(IStatus.ERROR, "Thousand Separator" + " " + Messages.getString("FileStep2.mustBePrecised")); //$NON-NLS-1$
            return false;
        }

        String decimalSeparator = decimalSeparatorText.getText();
        if (decimalSeparator == null) {
            updateStatus(IStatus.ERROR, "Decimal Separator" + " " + Messages.getString("FileStep2.mustBePrecised")); //$NON-NLS-1$
            return false;
        }

        return true;
    }

    /**
     * Subclass of SWTUIThreadProcessor to process the preview event. <br/>
     * 
     * $Id: DelimitedFileStep2Form.java 8214 2008-01-19 02:15:27Z qwei $
     * 
     */
    class PreviewProcessor extends SWTUIThreadProcessor {

        String previewInformationLabelMsg = null;

        CsvArray csvArray = null;

        ProcessDescription processDescription = null;

        boolean firstRowIsCatption = false;

        public boolean preProcessStart() {
            previewButton.setText(Messages.getString("FileStep2.stop"));

            clearPreview();

            // if no file, the process don't be executed
            if (getConnection().getFilePath() == null || getConnection().getFilePath().equals("")) { //$NON-NLS-1$
                previewInformationLabel.setText("   " + Messages.getString("FileStep2.filePathIncomplete")); //$NON-NLS-1$ //$NON-NLS-2$
                return false;
            }

            // if incomplete settings, , the process don't be executed
            if (!checkFieldsValue()) {
                previewInformationLabel.setText("   " + Messages.getString("FileStep2.settingsIncomplete")); //$NON-NLS-1$ //$NON-NLS-2$
                return false;
            }

            previewInformationLabel.setText("   " + Messages.getString("FileStep2.previewProgress")); //$NON-NLS-1$ //$NON-NLS-2$
            firstRowIsCatption = firstRowIsCaptionCheckbox.getSelection();
            processDescription = getProcessDescription();
            return true;
        }

        public void nonUIProcessInThread() {
            // get the XmlArray width an adapt ProcessDescription
            try {
                csvArray = ShadowProcessHelper.getCsvArray(processDescription, "FILE_EXCEL"); //$NON-NLS-1$
                if (csvArray == null) {
                    previewInformationLabelMsg = "   " + Messages.getString("FileStep2.previewFailure"); //$NON-NLS-1$ //$NON-NLS-2$
                } else {
                    previewInformationLabelMsg = "   " + Messages.getString("FileStep2.previewIsDone"); //$NON-NLS-1$ //$NON-NLS-2$

                    // refresh TablePreview on this step
                    previewInformationLabelMsg = ""; //$NON-NLS-1$
                }
            } catch (CoreException ex) {
                setException(ex);
                previewInformationLabelMsg = "   " + Messages.getString("FileStep2.previewFailure"); //$NON-NLS-1$ //$NON-NLS-2$
                log.error(Messages.getString("FileStep2.previewFailure") + " " + ex.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$
            }
        }

        public void updateUIInThreadIfThreadIsCanceled() {
            if (!previewInformationLabel.isDisposed()) {
                previewInformationLabel.setText("");
            }
        }

        public void updateUIInThreadIfThreadIsNotCanceled() {
            if (previewInformationLabel.isDisposed()) {
                return;
            }
            previewInformationLabel.setText(previewInformationLabelMsg);
            if (getException() != null) {
                new ErrorDialogWidthDetailArea(getShell(), PID,
                        Messages.getString("FileStep2.previewFailure"), getException().getMessage()); //$NON-NLS-1$
            }
            if (csvArray != null) {
                excelProcessPreview.refreshTablePreview(csvArray, firstRowIsCatption);
            }
        }

        public void updateUIInThreadIfThreadFinally() {
            if (!previewButton.isDisposed()) {
                previewButton.setText(Messages.getString("FileStep2.refreshPreview"));
                previewButton.setEnabled(true);

            }
        }

        public void postProcessCancle() {
            previewButton.setEnabled(false);
        }
    }

    /**
     * addButtonControls.
     * 
     * @param cancelButton
     */
    @Override
    protected void addUtilsButtonListeners() {

        // Event PreviewButton
        previewButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                processor.execute();
            }
        });

        if (cancelButton != null) {
            // Event CancelButton
            cancelButton.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(final SelectionEvent e) {
                    getShell().close();
                }
            });
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.widgets.Control#setVisible(boolean)
     * 
     */
    @Override
    public void setVisible(boolean visible) {

        super.setVisible(visible);

        if (super.isVisible()) {

            // Fields to the Group Delimited File Settings
            if (getConnection().getEncoding() != null && !getConnection().getEncoding().equals("")) { //$NON-NLS-1$
                encodingCombo.setText(getConnection().getEncoding());
            } else {
                encodingCombo.select(0);
            }

            // Refresh the preview width the adapted rowSeparator
            // If metadata exist, refreshMetadata
            if ((!"".equals(getConnection().getFilePath())) && (getConnection().getFilePath() != null)) { //$NON-NLS-1$
                refreshPreview();
            }
            if (isReadOnly() != readOnly) {
                adaptFormToReadOnly();
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.IRefreshable#refresh()
     */
    public void refresh() {
        refreshPreview();
    }
}
