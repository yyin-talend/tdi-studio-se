// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
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
package org.talend.repository.ui.wizards.metadata.connection.files.positional;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.talend.commons.ui.swt.formtools.Form;
import org.talend.commons.ui.swt.formtools.LabelledCombo;
import org.talend.commons.ui.swt.formtools.LabelledFileField;
import org.talend.commons.ui.swt.formtools.LabelledText;
import org.talend.commons.ui.swt.formtools.UtilsButton;
import org.talend.core.model.metadata.builder.connection.FileFormat;
import org.talend.core.model.metadata.builder.connection.RowSeparator;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.repository.i18n.Messages;
import org.talend.repository.ui.swt.filepositionalviewer.FilePositionalViewer;
import org.talend.repository.ui.swt.filepositionalviewer.GraphicRule;
import org.talend.repository.ui.swt.utils.AbstractPositionalFileStepForm;

/**
 * @author ocarbone
 * 
 */
public class FileStep1Form extends AbstractPositionalFileStepForm {

    private static Logger log = Logger.getLogger(FileStep1Form.class);

    /**
     * Settings.
     */
    private static final int WIDTH_GRIDDATA_PIXEL = 300;

    /**
     * Main Fields.
     */
    private LabelledCombo serverCombo;

    private LabelledFileField fileField;

    private LabelledCombo fileFormatCombo;

    /**
     * Another.
     */
    private boolean filePathIsDone;

    private static FilePositionalViewer filePositionalViewer;

    private UtilsButton cancelButton;

    private ScrolledComposite scrolledCompositeFileViewer;

    private GraphicRule graphicRule;

    private LabelledText fieldPositionText;

    private boolean readOnly;

    private static LabelledText fieldSeparatorText;

    /**
     * Constructor to use by RCP Wizard.
     * 
     * @param existingNames
     * 
     * @param Composite
     * @param Wizard
     * @param Style
     */
    public FileStep1Form(Composite parent, ConnectionItem connectionItem, String[] existingNames) {
        super(parent, connectionItem, existingNames);
        setupForm();
    }

    /**
     * 
     * Initialize value, forceFocus first field.
     */
    @Override
    protected void initialize() {

        // Initialize the fields
        String value = null;
        fileFormatCombo.setText(getConnection().getFormat().getName());
        fileFormatCombo.clearSelection();

        value = getConnection().getServer();
        if (value == null) {
            serverCombo.select(0);
            getConnection().setServer(serverCombo.getText());
        } else {
            serverCombo.setText(value);
        }
        serverCombo.clearSelection();

        fileField.setText(getConnection().getFilePath());

        value = getConnection().getFieldSeparatorValue();
        checkFilePathAndManageIt(false);

        // update the field Separator
        fieldSeparatorText.setText(value);

        // update the positionalViewer
        filePositionalViewer.setSeparatorValue(value, true);
        // update the field Position
        fieldPositionText.setText(filePositionalViewer.calculatePositionX());
    }

    /**
     * DOC ocarbone Comment method "adaptFormToReadOnly".
     */
    protected void adaptFormToReadOnly() {
        readOnly = isReadOnly();
        serverCombo.setReadOnly(isReadOnly());
        fieldSeparatorText.setReadOnly(isReadOnly());
        filePositionalViewer.setEnabled(!isReadOnly());
        fieldPositionText.setReadOnly(isReadOnly());
        fileField.setReadOnly(isReadOnly());
        fileFormatCombo.setReadOnly(isReadOnly());
    }

    protected void addFields() {
        int heightViewer = 150;

        // Group File Location
        Group groupFileViewer = Form.createGroup(this, 1, Messages.getString("FileStep1.groupFileLocationSettings"), 95);
        Composite compositeFileLocation = Form.startNewDimensionnedGridLayout(groupFileViewer, 3, WIDTH_GRIDDATA_PIXEL, 95);

        // server Combo
        String[] serverLocation = { "Localhost 127.0.0.1" };
        serverCombo = new LabelledCombo(compositeFileLocation, Messages.getString("FileStep1.server"), Messages
                .getString("FileStep1.serverTip"), serverLocation, 2, true, SWT.NONE);

        // file Field
        String[] extensions = { "*.txt", "*.*", "*" };
        fileField = new LabelledFileField(compositeFileLocation, Messages.getString("FileStep1.filepath"), extensions);

        // file format Combo
        String[] fileFormat = { FileFormat.WINDOWS_LITERAL.getName(), FileFormat.UNIX_LITERAL.getName(),
                FileFormat.MAC_LITERAL.getName() };
        fileFormatCombo = new LabelledCombo(compositeFileLocation, Messages.getString("FileStep1.format"), Messages
                .getString("FileStep1.formatTip"), fileFormat, 2);

        // Group File Viewer
        groupFileViewer = Form.createGroup(this, 1, Messages.getString("FileStep1.groupFileViewer"), heightViewer + 80);

        Composite compositeBorderFileViewer = new Composite(groupFileViewer, SWT.BORDER);
        compositeBorderFileViewer.setLayout(new GridLayout());
        GridData gridData = new GridData(GridData.FILL_BOTH);
        gridData.minimumHeight = heightViewer + 25;
        gridData.heightHint = heightViewer + 25;
        compositeBorderFileViewer.setLayoutData(gridData);

        graphicRule = new GraphicRule(compositeBorderFileViewer, SWT.NONE);

        scrolledCompositeFileViewer = new ScrolledComposite(compositeBorderFileViewer, SWT.H_SCROLL | SWT.V_SCROLL | SWT.NONE);
        scrolledCompositeFileViewer.setExpandHorizontal(true);
        scrolledCompositeFileViewer.setExpandVertical(true);
        GridData gridData1 = new GridData(GridData.FILL_BOTH);
        gridData1.widthHint = WIDTH_GRIDDATA_PIXEL;
        gridData1.heightHint = heightViewer;
        gridData1.horizontalSpan = 2;
        scrolledCompositeFileViewer.setLayoutData(gridData1);

        filePositionalViewer = new FilePositionalViewer(scrolledCompositeFileViewer, SWT.LEFT);
        graphicRule.moveAbove(filePositionalViewer);
        graphicRule.setPositionalViewer(filePositionalViewer);

        scrolledCompositeFileViewer.setContent(filePositionalViewer);
        scrolledCompositeFileViewer.setMinSize(filePositionalViewer.computeSize(SWT.DEFAULT, SWT.DEFAULT));

        // Field fieldSeparatorText
        Composite compositeFieldSeparator = Form.startNewDimensionnedGridLayout(groupFileViewer, 2, WIDTH_GRIDDATA_PIXEL, 50);
        fieldSeparatorText = new LabelledText(compositeFieldSeparator, Messages.getString("FileStep2.fieldSeparator"), 1, true,
                SWT.RIGHT);
        fieldSeparatorText.setToolTipText(Messages.getString("FileStep2.fieldSeparatorPositionalTip"));

        fieldPositionText = new LabelledText(compositeFieldSeparator, Messages.getString("FileStep2.fieldPosition"), 1, true,
                SWT.RIGHT);
        fieldPositionText.setToolTipText(Messages.getString("FileStep2.fieldPositionTip"));

        if (!isInWizard()) {
            // Composite BottomButton
            Composite compositeBottomButton = Form.startNewGridLayout(this, 2, false, SWT.CENTER, SWT.CENTER);
            // Button Cancel
            cancelButton = new UtilsButton(compositeBottomButton, Messages.getString("CommonWizard.Cancel"), WIDTH_BUTTON_PIXEL,
                    HEIGHT_BUTTON_PIXEL);
            // nextButton = new UtilsButton(compositeBottomButton, "Next", WIDTH_BUTTON_PIXEL, HEIGHT_BUTTON_PIXEL);
        }
        addUtilsButtonListeners();
    }

    protected void addUtilsButtonListeners() {

        if (!isInWizard()) {
            // Event cancelButton
            cancelButton.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(final SelectionEvent e) {
                    getShell().close();
                }
            });
            // // Event nextButton
            // nextButton.addSelectionListener(new SelectionAdapter() {
            // public void widgetSelected(final SelectionEvent e) {
            // }
            // });
        }
    }

    /**
     * Main Fields addControls.
     */
    protected void addFieldsListeners() {

        // Synchronise the graphicRule width the scrolledCompositeFileViewer
        scrolledCompositeFileViewer.getHorizontalBar().addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event e) {
                int hSelection = scrolledCompositeFileViewer.getHorizontalBar().getSelection();
                graphicRule.setBounds(5 - hSelection, 5, filePositionalViewer.getText().getSize().x, graphicRule.getSize().y);
            }
        });
        scrolledCompositeFileViewer.addControlListener(new ControlListener() {

            public void controlMoved(ControlEvent e) {
            }

            public void controlResized(ControlEvent e) {
                int compositeXsize = filePositionalViewer.getText().getSize().x;
                int compositeYsize = graphicRule.getSize().y;
                if (compositeXsize <= 10000) {
                    graphicRule.setBounds(5, 5, 500, 0);
                    graphicRule.setSize(10000, compositeYsize);
                } else {
                    graphicRule.setBounds(5, 5, 500, 0);
                    graphicRule.setSize(compositeXsize, compositeYsize);
                }
            }
        });

        // Event serverCombo
        serverCombo.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                getConnection().setServer(serverCombo.getText());
                checkFieldsValue();
            }
        });

        // fileField : Event modifyText
        fileField.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                getConnection().setFilePath(fileField.getText());
                checkFilePathAndManageIt(true);
            }
        });

        // Event fileFormatCombo
        fileFormatCombo.addModifyListener(new ModifyListener() {

            // Event Modify
            public void modifyText(final ModifyEvent e) {
                getConnection().setFormat(FileFormat.getByName(fileFormatCombo.getText()));
                // if necessary, adapt the rowSeparator to the file format
                if (getConnection().getRowSeparatorType() == RowSeparator.STANDART_EOL_LITERAL) {
                    if (getConnection().getFormat().toString().equals(FileFormat.MAC_LITERAL.getName())) {
                        getConnection().setRowSeparatorValue("\\r");
                    } else {
                        getConnection().setRowSeparatorValue("\\n");
                    }
                }
                checkFilePathAndManageIt(false);
            }
        });

        // when positionalViewer is modified : synchronise the field SeparatorText
        filePositionalViewer.getFieldSeparatorValue().addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {

                String value = filePositionalViewer.getFieldSeparatorValue().getText();
                String valueToField = value;
                if (fieldSeparatorText.getText().contains("*")) {
                    if (value.equals("")) {
                        valueToField = "*";
                    } else {
                        valueToField = value + ",*";
                    }
                }

                if (!fieldSeparatorText.getText().equals(valueToField)) {
                    // update the field separator Text
                    fieldSeparatorText.setEditable(false);
                    fieldSeparatorText.setText(valueToField);
                    fieldSeparatorText.setEditable(true);
                    // update the field position Text
                    fieldPositionText.setEditable(false);
                    fieldPositionText.setText(filePositionalViewer.calculatePositionX());
                    fieldPositionText.setEditable(true);
                    checkFieldsValue();
                }
            }
        });

        // Separator Text : check the value and synchronise the positionalViewer
        fieldSeparatorText.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                // update the connection
                getConnection().setFieldSeparatorValue(fieldSeparatorText.getText());

                if (fieldSeparatorText.getEditable()
                        && getConnection().getFieldSeparatorValue().equals(fieldSeparatorText.getText())) {

                    // check the value and display the status Error is needed
                    if (!checkFieldSeparatorValue()) {
                        // the value isn't correct => clean markers of the positionalViewer
                        fieldPositionText.setEditable(false);
                        filePositionalViewer.cleanAllMarkers();
                        filePositionalViewer.setEnabled(false);
                        graphicRule.setEnabled(false);
                    } else {
                        String value = getValidateFieldSeparator(fieldSeparatorText.getText());
                        Point selection = fieldSeparatorText.getSelection();
                        if ((!value.equals(getConnection().getFieldSeparatorValue()))) {
                            // the value isn't correct => clean markers of the positionalViewer
                            fieldPositionText.setEditable(false);
                            filePositionalViewer.setEnabled(false);
                            graphicRule.setEnabled(false);
                            filePositionalViewer.cleanAllMarkers();
                        } else {
                            // the value is correct
                            filePositionalViewer.setEnabled(true);
                            graphicRule.setEnabled(true);

                            // update the positionalViewer
                            filePositionalViewer.setSeparatorValue(value, true);
                            // update the field position Text
                            String newPosition = filePositionalViewer.calculatePositionX();
                            if (!fieldPositionText.getText().equals(newPosition)) {
                                fieldPositionText.setEditable(false);
                                fieldPositionText.setText(newPosition);
                            }
                            fieldPositionText.setEditable(true);
                        }
                        fieldSeparatorText.setSelection(selection.x);
                    }
                }
            }
        });

        // Separator Text : check Key Listener
        fieldSeparatorText.addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                e.doit = charIsAcceptedOnFieldSeparator(fieldSeparatorText.getText(), e.character, fieldSeparatorText
                        .getSelection().x);
            }
        });

        // Position Text : check the value and synchronise positionalViewer
        fieldPositionText.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                if (fieldPositionText.getEditable()) {
                    if (!checkFieldPositionValue()) {
                        // the value isn't correct => clean markers of the positionalViewer
                        fieldSeparatorText.setEditable(false);
                        filePositionalViewer.setEnabled(false);
                        graphicRule.setEnabled(false);
                        filePositionalViewer.cleanAllMarkers();
                    } else {
                        filePositionalViewer.setEnabled(true);
                        graphicRule.setEnabled(true);
                        String value = getValidateFieldPosition(fieldPositionText.getText());
                        Point selection = fieldPositionText.getSelection();
                        // the value is correct
                        filePositionalViewer.setPositionValue(value);
                        fieldSeparatorText.setEditable(true);
                        value = filePositionalViewer.getSeparatorValue();
                        if (fieldSeparatorText.getText().equals("")) {
                            fieldSeparatorText.setText("*");
                        } else if (fieldSeparatorText.getText().contains("*")) {
                            fieldSeparatorText.setText(value + ",*");
                        } else {
                            fieldSeparatorText.setText(value);
                        }
                        fieldPositionText.setSelection(selection.x);
                    }
                }
            }
        });

        // Position Text : Key Listener
        fieldPositionText.addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                e.doit = charIsAcceptedOnFieldPosition(fieldPositionText.getText(), e.character,
                        fieldPositionText.getSelection().x);
            }
        });

    }

    /**
     * checkFileFieldsValue active fileViewer if file exist.
     */
    private void checkFilePathAndManageIt(boolean isNewFile) {

        filePathIsDone = false;
        if (fileField.getText() == "") {
            filePositionalViewer.setText("\n" + Messages.getString("FileStep1.fileViewerTip1") + " " + MAXIMUM_ROWS_TO_PREVIEW
                    + " " + Messages.getString("FileStep1.fileViewerTip2"));
        } else {
            filePositionalViewer.setText("\n" + Messages.getString("FileStep1.fileViewerProgress"));

            String previewRows = "";
            try {
                String str;
                int numberLine = 0;
                // read the file
                BufferedReader in = new BufferedReader(new FileReader(fileField.getText()));
                while (((str = in.readLine()) != null) && (numberLine <= MAXIMUM_ROWS_TO_PREVIEW)) {
                    numberLine++;
                    previewRows = previewRows + str + "\n";
                }
                in.close();

                // replace Tabulation by a CaretChar
                previewRows = previewRows.replaceAll("\t", "\u25A1");
                // show lines
                filePositionalViewer.setText("\n" + previewRows);
                filePathIsDone = true;
                if (isNewFile) {
                    fieldSeparatorText.setText("*");
                    filePositionalViewer.setSeparatorValue("*", true);
                    getConnection().setFieldSeparatorValue("*");
                }

            } catch (Exception e) {
                String msgError = Messages.getString("FileStep1.filepath") + " \"" + fileField.getText().replace("\\\\", "\\")
                        + "\"\n";
                if (e instanceof FileNotFoundException) {
                    msgError = msgError + Messages.getString("FileStep1.fileNotFoundException");
                } else if (e instanceof EOFException) {
                    msgError = msgError + Messages.getString("FileStep1.eofException");
                } else if (e instanceof IOException) {
                    msgError = msgError + Messages.getString("FileStep1.fileLocked");
                } else {
                    msgError = Messages.getString("FileStep1.filepath") + " \"" + fileField.getText().replace("\\\\", "\\")
                            + "\" " + Messages.getString("FileStep1.noExist");
                }
                filePositionalViewer.setText("\n" + msgError);
                updateStatus(IStatus.ERROR, msgError);
                log.error(msgError + " " + e.getMessage());
            }

            // resize the composite text
            filePositionalViewer.layout();
            scrolledCompositeFileViewer.setMinSize(filePositionalViewer.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        }

        checkFieldsValue();
    }

    /**
     * Ensures that fields are set.
     * 
     * @return
     */
    protected boolean checkFieldsValue() {
        return checkFieldsValue(true);
    }

    /**
     * Ensures that fields are set.
     * 
     * @return
     */
    protected boolean checkFieldsValue(boolean checkViewerField) {
        // The fields
        serverCombo.setEnabled(true);

        if (serverCombo.getText() == "") {
            fileField.setEditable(false);
            fileFormatCombo.setEnabled(false);
            updateStatus(IStatus.ERROR, Messages.getString("FileStep1.serverAlert"));
            return false;
        } else {
            fileField.setEditable(true);
            fileFormatCombo.setEnabled(true);
        }

        if (fileField.getText() == "") {
            updateStatus(IStatus.ERROR, Messages.getString("FileStep1.filepathAlert"));
            return false;
        }

        if (!filePathIsDone) {
            updateStatus(IStatus.ERROR, Messages.getString("FileStep1.fileIncomplete"));
            return false;
        } else if (fileFormatCombo.getSelectionIndex() < 0) {
            updateStatus(IStatus.ERROR, Messages.getString("FileStep1.formatAlert"));
            return false;
        }

        updateStatus(IStatus.OK, null);
        if (checkViewerField) {
            if (checkFieldSeparatorValue()) {
                return checkFieldPositionValue();
            }
        }
        return true;
    }

    /**
     * Ensures that field Separator are set.
     * 
     * @return
     */
    protected boolean checkFieldSeparatorValue() {
        if (fieldSeparatorText.getText().length() <= 1) {
            // fieldSeparatorText can't be empty
            if (fieldSeparatorText.getText().equals("") || fieldSeparatorText.getText().equals("0")) {
                updateStatus(IStatus.ERROR, Messages.getString("FileStep2.fieldSeparatorAlert"));
                return false;
            } else if (fieldSeparatorText.getText().equals(",")) {
                // fieldSeparatorText can't be just a comma
                updateStatus(IStatus.ERROR, Messages.getString("FileStep2.fieldSeparatorNotFinishByComma"));
                return false;
            }
        } else if ((fieldSeparatorText.getText().substring(fieldSeparatorText.getText().length() - 1,
                fieldSeparatorText.getText().length()).equals(","))) {
            // fieldSeparatorText can't finish by comma
            updateStatus(IStatus.ERROR, Messages.getString("FileStep2.fieldSeparatorNotFinishByComma"));
            return false;
        } else if (!fieldSeparatorText.getText().equals(getValidateFieldSeparator(fieldSeparatorText.getText()))) {
            // fieldSeparatorText is not valide
            updateStatus(IStatus.ERROR, Messages.getString("FileStep2.fieldSeparatorAlert"));
            return false;
        }

        checkFieldsValue(false);
        return true;
    }

    /**
     * Ensures that field Separator are set.
     * 
     * @return
     */
    protected boolean checkFieldPositionValue() {
        if (fieldPositionText.getText().length() <= 1) {
            // fieldPositionText can't be empty
            if (fieldPositionText.getText().equals("")) {
                updateStatus(IStatus.ERROR, Messages.getString("FileStep2.fieldPositionAlert"));
                return false;
            } else if (fieldPositionText.getText().equals(",")) {
                // fieldPositionText can't be just a comma
                updateStatus(IStatus.ERROR, Messages.getString("FileStep2.fieldPositionNotFinishByComma"));
                return false;
            }
        } else if (fieldPositionText.getText().substring(fieldPositionText.getText().length() - 1,
                fieldPositionText.getText().length()).equals(",")) {
            // fieldPositionText can't finish by comma
            updateStatus(IStatus.ERROR, Messages.getString("FileStep2.fieldPositionNotFinishByComma"));
            return false;
        }

        if (!fieldPositionText.getText().equals(getValidateFieldPosition(fieldPositionText.getText()))) {
            // fieldPositionText is not valide
            updateStatus(IStatus.ERROR, Messages.getString("FileStep2.fieldPositionNotValidate"));
            return false;
        }

        checkFieldsValue(false);
        return true;
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
            // Adapt the UI fieldSeparator and Position to step1
            String value = getConnection().getFieldSeparatorValue();
            fieldSeparatorText.setText(value);
            filePositionalViewer.setSeparatorValue(value, true);
            String newPosition = filePositionalViewer.calculatePositionX();
            if (!fieldPositionText.getText().equals(newPosition)) {
                fieldPositionText.setText(newPosition);
            }

            if (isReadOnly() != readOnly) {
                adaptFormToReadOnly();
            }
        }
    }

}
