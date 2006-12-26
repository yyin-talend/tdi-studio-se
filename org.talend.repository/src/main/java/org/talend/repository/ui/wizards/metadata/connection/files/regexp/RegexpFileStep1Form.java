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
package org.talend.repository.ui.wizards.metadata.connection.files.regexp;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.talend.commons.ui.swt.formtools.Form;
import org.talend.commons.ui.swt.formtools.LabelledCombo;
import org.talend.commons.ui.swt.formtools.LabelledFileField;
import org.talend.commons.ui.swt.formtools.UtilsButton;
import org.talend.commons.utils.encoding.CharsetToolkit;
import org.talend.core.model.metadata.builder.connection.FileFormat;
import org.talend.core.model.metadata.builder.connection.RowSeparator;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.repository.i18n.Messages;
import org.talend.repository.ui.swt.utils.AbstractRegexpFileStepForm;

/**
 * @author ocarbone
 * 
 */
public class RegexpFileStep1Form extends AbstractRegexpFileStepForm {

    private static Logger log = Logger.getLogger(RegexpFileStep1Form.class);

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

    private Text fileViewerText;

    private UtilsButton cancelButton;

    private boolean readOnly;

    /**
     * Constructor to use by RCP Wizard.
     * 
     * @param existingNames
     * 
     * @param Composite
     * @param Wizard
     * @param Style
     */
    public RegexpFileStep1Form(Composite parent, ConnectionItem connectionItem, String[] existingNames) {
        super(parent, connectionItem, existingNames);
        setupForm();
    }

    /**
     * 
     * Initialize value, forceFocus first field.
     */
    @Override
    protected void initialize() {
        fileFormatCombo.setText(getConnection().getFormat().getName());
        fileFormatCombo.clearSelection();

        if (getConnection().getServer() == null) {
            serverCombo.select(0);
            getConnection().setServer(serverCombo.getText());
        } else {
            serverCombo.setText(getConnection().getServer());
        }
        serverCombo.clearSelection();

        if (getConnection().getFilePath() != null) {
            fileField.setText(getConnection().getFilePath().replace("\\\\", "\\"));
        }

        // init the fileViewer
        checkFilePathAndManageIt();

    }

    /**
     * DOC ocarbone Comment method "adaptFormToReadOnly".
     * 
     */
    protected void adaptFormToReadOnly() {
        readOnly = isReadOnly();
        serverCombo.setReadOnly(isReadOnly());
        fileField.setReadOnly(isReadOnly());
        fileFormatCombo.setReadOnly(isReadOnly());
        updateStatus(IStatus.OK, "");

    }

    protected void addFields() {
        // Group File Location
        Group group = Form.createGroup(this, 1, Messages.getString("FileStep2.groupDelimitedFileSettings"), 120);
        Composite compositeFileLocation = Form.startNewDimensionnedGridLayout(group, 3, WIDTH_GRIDDATA_PIXEL, 120);

        // server Combo
        String[] serverLocation = { "Localhost 127.0.0.1" };
        serverCombo = new LabelledCombo(compositeFileLocation, Messages.getString("FileStep1.server"), Messages
                .getString("FileStep1.serverTip"), serverLocation, 2, true, SWT.NONE);

        // file Field
        String[] extensions = { "*.*", "*.csv", "*.txt", "*" };
        fileField = new LabelledFileField(compositeFileLocation, Messages.getString("FileStep1.filepath"), extensions);

        // file format Combo
        String[] fileFormat = { FileFormat.WINDOWS_LITERAL.getName(), FileFormat.UNIX_LITERAL.getName(),
                FileFormat.MAC_LITERAL.getName() };
        fileFormatCombo = new LabelledCombo(compositeFileLocation, Messages.getString("FileStep1.format"), Messages
                .getString("FileStep1.formatTip"), fileFormat, 2);

        // Group File Viewer
        group = Form.createGroup(this, 1, Messages.getString("FileStep1.groupFileViewer"), 150);
        Composite compositeFileViewer = Form.startNewDimensionnedGridLayout(group, 1, WIDTH_GRIDDATA_PIXEL, 150);

        fileViewerText = new Text(compositeFileViewer, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
        GridData gridData = new GridData(GridData.FILL_BOTH);
        gridData.minimumWidth = WIDTH_GRIDDATA_PIXEL;
        gridData.minimumHeight = 150;
        fileViewerText.setLayoutData(gridData);
        fileViewerText.setToolTipText(Messages.getString("FileStep1.fileViewerTip1") + " " + MAXIMUM_ROWS_TO_PREVIEW + " "
                + Messages.getString("FileStep1.fileViewerTip2"));
        fileViewerText.setEditable(false);
        fileViewerText.setText(Messages.getString("FileStep1.fileViewerAlert"));

        if (!isInWizard()) {
            // Composite BottomButton
            Composite compositeBottomButton = Form.startNewGridLayout(this, 2, false, SWT.CENTER, SWT.CENTER);

            // Button Cancel
            cancelButton = new UtilsButton(compositeBottomButton, Messages.getString("CommonWizard.cancel"), WIDTH_BUTTON_PIXEL,
                    HEIGHT_BUTTON_PIXEL);
            // nextButton = new UtilsButton(compositeBottomButton, Messages.getString("CommonWizard.next"),
            // WIDTH_BUTTON_PIXEL, HEIGHT_BUTTON_PIXEL);
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
            //
            // public void widgetSelected(final SelectionEvent e) {
            // }
            // });
        }

    }

    /**
     * Main Fields addControls.
     */
    protected void addFieldsListeners() {
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
                fileViewerText.setText(Messages.getString("FileStep1.fileViewerProgress"));
                checkFilePathAndManageIt();
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
                fileViewerText.setText(Messages.getString("FileStep1.fileViewerProgress"));
                checkFilePathAndManageIt();
            }
        });
    }

    /**
     * checkFileFieldsValue active fileViewer if file exist.
     */
    private void checkFilePathAndManageIt() {
        updateStatus(IStatus.OK, null);
        filePathIsDone = false;
        if (fileField.getText() == "") {
            fileViewerText.setText(Messages.getString("FileStep1.fileViewerTip1") + " " + MAXIMUM_ROWS_TO_PREVIEW + " "
                    + Messages.getString("FileStep1.fileViewerTip2"));
        } else {
            fileViewerText.setText(Messages.getString("FileStep1.fileViewerProgress"));

            StringBuffer previewRows = new StringBuffer("");
            BufferedReader in = null;
            
            try {
                
                File file = new File(fileField.getText());
                Charset guessedCharset = CharsetToolkit.guessEncoding(file, 4096);
                getConnection().setEncoding(guessedCharset.displayName());
                
                String str;
                int numberLine = 0;
                // read the file width the limit : MAXIMUM_ROWS_TO_PREVIEW
                in = 
                    new BufferedReader(
                        new InputStreamReader(new FileInputStream(fileField.getText()),
                                guessedCharset.displayName()));
                while (((str = in.readLine()) != null) && (numberLine <= MAXIMUM_ROWS_TO_PREVIEW)) {
                    numberLine++;
                    previewRows.append(str + "\n");
                }

                // show lines
                fileViewerText.setText(new String(previewRows));
                filePathIsDone = true;
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
                    msgError = msgError + Messages.getString("FileStep1.noExist");
                }
                fileViewerText.setText(msgError);
                if (!isReadOnly()) {
                    updateStatus(IStatus.ERROR, msgError);
                }
                log.error(msgError + " " + e.getMessage());
            } finally {
                String msgError = Messages.getString("FileStep1.filepath") + " \"" + fileViewerText.getText().replace("\\\\", "\\") + "\"\n";
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException e) {
                    msgError = msgError + Messages.getString("FileStep1.fileLocked");
                }
            }
            checkFieldsValue();
        }
    }

    /**
     * Ensures that fields are set.
     * 
     * @return
     */
    protected boolean checkFieldsValue() {
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
        if (isReadOnly() != readOnly) {
            adaptFormToReadOnly();
        }
    }
}
