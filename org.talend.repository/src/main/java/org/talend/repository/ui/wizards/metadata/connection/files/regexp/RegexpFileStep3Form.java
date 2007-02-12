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

import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.talend.commons.ui.swt.dialogs.ErrorDialogWidthDetailArea;
import org.talend.commons.ui.swt.formtools.DataTypeHelper;
import org.talend.commons.ui.swt.formtools.Form;
import org.talend.commons.ui.swt.formtools.LabelledText;
import org.talend.commons.ui.swt.formtools.UtilsButton;
import org.talend.commons.utils.data.list.IListenableListListener;
import org.talend.commons.utils.data.list.ListenableListEvent;
import org.talend.core.model.metadata.MetadataTalendType;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.editor.MetadataEmfTableEditor;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.ui.metadata.editor.MetadataEmfTableEditorView;
import org.talend.core.utils.XmlArray;
import org.talend.core.utils.XmlField;
import org.talend.core.utils.XmlRow;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.RepositoryConstants;
import org.talend.repository.preview.ProcessDescription;
import org.talend.repository.ui.swt.utils.AbstractRegexpFileStepForm;
import org.talend.repository.ui.utils.ShadowProcessHelper;

/**
 * @author ocarbone
 * 
 */
public class RegexpFileStep3Form extends AbstractRegexpFileStepForm {

    private static Logger log = Logger.getLogger(RegexpFileStep3Form.class);

    private static final int WIDTH_GRIDDATA_PIXEL = 750;

    private UtilsButton cancelButton;

    private UtilsButton guessButton;

    private MetadataEmfTableEditor metadataEditor;

    private MetadataEmfTableEditorView tableEditorView;

    private Label informationLabel;

    private MetadataTable metadataTable;

    private LabelledText metadataNameText;

    private LabelledText metadataCommentText;

    private boolean readOnly;

    /**
     * Constructor to use by RCP Wizard.
     * 
     * @param Composite
     */
    public RegexpFileStep3Form(Composite parent, ConnectionItem connectionItem, MetadataTable metadataTable,
            String[] existingNames) {
        super(parent, connectionItem, metadataTable, existingNames);
        this.connectionItem = connectionItem;
        this.metadataTable = metadataTable;
        setupForm();
    }

    /**
     * 
     * Initialize value, forceFocus first field.
     */
    protected void initialize() {
        // init the metadata Table
        metadataNameText.setText(metadataTable.getLabel());
        metadataCommentText.setText(metadataTable.getComment());
        metadataEditor.setMetadataTable(metadataTable);
        tableEditorView.setMetadataEditor(metadataEditor);
        tableEditorView.getTableViewerCreator().layout();

        if (getConnection().isReadOnly()) {
            adaptFormToReadOnly();
        } else {
            updateStatus(IStatus.OK, null);
        }
    }

    /**
     * DOC ocarbone Comment method "adaptFormToReadOnly".
     * 
     */
    protected void adaptFormToReadOnly() {
        readOnly = isReadOnly();
        guessButton.setEnabled(!isReadOnly());
        metadataNameText.setReadOnly(isReadOnly());
        metadataCommentText.setReadOnly(isReadOnly());
        tableEditorView.setReadOnly(isReadOnly());
    }

    protected void addFields() {

        // Header Fields
        Composite mainComposite = Form.startNewDimensionnedGridLayout(this, 2, WIDTH_GRIDDATA_PIXEL, 60);
        metadataNameText = new LabelledText(mainComposite, Messages.getString("FileStep3.metadataName")); //$NON-NLS-1$
        metadataCommentText = new LabelledText(mainComposite, Messages.getString("FileStep3.metadataComment")); //$NON-NLS-1$

        // Group MetaData
        Group groupMetaData = Form.createGroup(this, 1, Messages.getString("FileStep3.groupMetadata"), 280); //$NON-NLS-1$
        Composite compositeMetaData = Form.startNewGridLayout(groupMetaData, 1);

        // Composite Guess
        Composite compositeGuessButton = Form.startNewDimensionnedGridLayout(compositeMetaData, 2, WIDTH_GRIDDATA_PIXEL, 40);
        informationLabel = new Label(compositeGuessButton, SWT.NONE);
        informationLabel.setText(Messages.getString("FileStep3.informationLabel") + "                                                  "); //$NON-NLS-1$ //$NON-NLS-2$
        informationLabel.setSize(500, HEIGHT_BUTTON_PIXEL);

        guessButton = new UtilsButton(compositeGuessButton, Messages.getString("FileStep3.guess"), WIDTH_BUTTON_PIXEL, //$NON-NLS-1$
                HEIGHT_BUTTON_PIXEL);
        guessButton.setToolTipText(Messages.getString("FileStep3.guessTip")); //$NON-NLS-1$

        // Composite MetadataTableEditorView
        Composite compositeTable = Form.startNewDimensionnedGridLayout(compositeMetaData, 1, WIDTH_GRIDDATA_PIXEL, 200);
        compositeTable.setLayout(new FillLayout());
        metadataEditor = new MetadataEmfTableEditor(Messages.getString("FileStep3.metadataDescription")); //$NON-NLS-1$
        tableEditorView = new MetadataEmfTableEditorView(compositeTable, SWT.NONE, false);

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
     * Main Fields addControls.
     */
    protected void addFieldsListeners() {
        // metadataNameText : Event modifyText
        metadataNameText.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                metadataTable.setLabel(metadataNameText.getText());
                checkFieldsValue();
            }
        });
        // metadataNameText : Event KeyListener
        metadataNameText.addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                if ((!Character.isIdentifierIgnorable(e.character))
                        && (!Pattern.matches(RepositoryConstants.REPOSITORY_ITEM_PATTERN, "" + e.character))) { //$NON-NLS-1$
                    e.doit = false;
                }
            }
        });

        // metadataCommentText : Event modifyText
        metadataCommentText.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                metadataTable.setComment(metadataCommentText.getText());
            }
        });

        // add listener to tableMetadata (listen the event of the toolbars)
        tableEditorView.getMetadataEditor().addAfterOperationListListener(new IListenableListListener() {

            public void handleEvent(ListenableListEvent event) {
                checkFieldsValue();
            }
        });
    }

    /**
     * addButtonControls.
     * 
     * @param cancelButton
     */
    protected void addUtilsButtonListeners() {

        // Event guessButton
        guessButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                if (tableEditorView.getMetadataEditor().getBeanCount() > 0) {

                    if (!guessButton.getEnabled()) {
                        guessButton.setEnabled(true);
                        if (MessageDialog.openConfirm(getShell(), Messages.getString("FileStep3.guessConfirmation"), Messages //$NON-NLS-1$
                                .getString("FileStep3.guessConfirmationMessage"))) { //$NON-NLS-1$
                            runShadowProcess();
                        }
                    } else {
                        guessButton.setEnabled(false);
                    }

                } else {

                    if (!guessButton.getEnabled()) {
                        guessButton.setEnabled(true);
                        runShadowProcess();
                    } else {
                        guessButton.setEnabled(false);
                    }
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

    }

    /**
     * create ProcessDescription and set it.
     * 
     * WARNING ::field FieldSeparator, RowSeparator, EscapeChar and TextEnclosure are surround by double quote.
     * 
     * 
     * @return processDescription
     */
    private ProcessDescription getProcessDescription() {

        ProcessDescription processDescription = ShadowProcessHelper.getProcessDescription(getConnection());

        // Adapt Header width firstRowIsCaption to preview the first line on caption or not
        if (getConnection().isFirstLineCaption()) {
            processDescription.setHeaderRow(getConnection().getHeaderValue() - 1);
        }

        // adapt the limit to the extract sames rows of preview
        processDescription.setLimitRows(MAXIMUM_ROWS_TO_PREVIEW);
        if (getConnection().isUseLimit()) {
            Integer i = getConnection().getLimitValue();
            if (getConnection().isFirstLineCaption()) {
                i++;
            }
            if (i < MAXIMUM_ROWS_TO_PREVIEW) {
                processDescription.setLimitRows(i);
            }
        }
        return processDescription;
    }

    /**
     * run a ShadowProcess to determined the Metadata.
     */
    protected void runShadowProcess() {

        // if no file, the process don't be executed
        if (getConnection().getFilePath() == null || getConnection().getFilePath().equals("")) { //$NON-NLS-1$
            informationLabel.setText("   " + Messages.getString("FileStep3.filepathAlert") //$NON-NLS-1$ //$NON-NLS-2$
                    + "                                                                              "); //$NON-NLS-1$
            return;
        }

        try {
            informationLabel.setText("   " + Messages.getString("FileStep3.guessProgress")); //$NON-NLS-1$ //$NON-NLS-2$

            XmlArray xmlArray = ShadowProcessHelper.getXmlArray(getProcessDescription(), "FILE_REGEXP"); //$NON-NLS-1$
            if (xmlArray == null) {
                informationLabel.setText("   " + Messages.getString("FileStep3.guessFailure")); //$NON-NLS-1$ //$NON-NLS-2$

            } else {
                refreshMetaDataTable(xmlArray);
            }

        } catch (CoreException e) {
            if (getParent().getChildren().length == 1) {
                new ErrorDialogWidthDetailArea(getShell(), PID, Messages.getString("FileStep3.guessFailureTip") + "\n" //$NON-NLS-1$ //$NON-NLS-2$
                        + Messages.getString("FileStep3.guessFailureTip2"), e.getMessage()); //$NON-NLS-1$
            } else {
                new ErrorDialogWidthDetailArea(getShell(), PID, Messages.getString("FileStep3.guessFailureTip"), e.getMessage()); //$NON-NLS-1$
            }
            log.error(Messages.getString("FileStep3.guessFailure") + " " + e.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$
        }
        checkFieldsValue();
    }

    /**
     * DOC ocarbone Comment method "refreshMetaData".
     * 
     * @param xmlArray
     */
    public void refreshMetaDataTable(final XmlArray xmlArray) {
        informationLabel.setText("   " + Messages.getString("FileStep3.guessIsDone")); //$NON-NLS-1$ //$NON-NLS-2$

        // clear all items
        tableEditorView.getMetadataEditor().removeAll();

        if (xmlArray == null) {
            return;
        } else {

            List<XmlRow> xmlRows = xmlArray.getRows();
            List<XmlField> fields = xmlRows.get(0).getFields();
            int numberOfCol = fields.size();

            // define the label to the metadata width the content of the first row
            int firstRowToExtractMetadata = 0;
            if (getConnection().isFirstLineCaption()) {
                firstRowToExtractMetadata = 1;
            }

            // the first rows is used to define the label of any metadata
            String[] label = new String[numberOfCol];
            for (int i = 0; i < numberOfCol; i++) {
                label[i] = Messages.getString("FileStep3.column") + i; //$NON-NLS-1$
                if (firstRowToExtractMetadata == 1) {
                    String value = fields.get(i).getValue();
                    if (value != null && !value.equals("")) { //$NON-NLS-1$
                        label[i] = value.trim().replaceAll(" ", "_");
                    }
                }
            }

            for (int i = 0; i < numberOfCol; i++) {
                // define the first currentType and assimile it to globalType
                String globalType = null;
                int lengthValue = 0;
                int precisionValue = 0;

                int current = firstRowToExtractMetadata;
                while (globalType == null) {
                    if (i >= xmlRows.get(current).getFields().size()) {
                        globalType = "String"; //$NON-NLS-1$
                    } else {
                        globalType = DataTypeHelper.getTalendTypeOfValue(xmlRows.get(current).getFields().get(i).getValue());
                        current++;
                        if (current == xmlRows.size()) {
                            globalType = "String"; //$NON-NLS-1$
                        }
                    }
                }
                // for another lines
                for (int f = firstRowToExtractMetadata; f < xmlRows.size(); f++) {
                    fields = xmlRows.get(f).getFields();
                    if (fields.size() > i) {
                        String value = fields.get(i).getValue();
                        if (!value.equals("")) { //$NON-NLS-1$
                            if (!DataTypeHelper.getTalendTypeOfValue(value).equals(globalType)) {
                                globalType = DataTypeHelper.getCommonType(globalType, DataTypeHelper.getTalendTypeOfValue(value));
                            }
                            if (lengthValue < value.length()) {
                                lengthValue = value.length();                                
                            }
                            int positionDecimal = 0;
                            if (value.indexOf(',') > -1) {
                                positionDecimal = value.lastIndexOf(',');
                                precisionValue = lengthValue - positionDecimal;
                            } else if (value.indexOf('.') > -1) {
                                positionDecimal = value.lastIndexOf('.');
                                precisionValue = lengthValue - positionDecimal;
                            }
                        }
                    }
                }

                // define the metadataColumn to field i
                MetadataColumn metadataColumn = ConnectionFactory.eINSTANCE.createMetadataColumn();
                // Convert javaType to TalendType
                String talendType = MetadataTalendType.loadTalendType(globalType, "TALENDDEFAULT", false); //$NON-NLS-1$
                metadataColumn.setTalendType(talendType);
                metadataColumn.setLength(lengthValue);
                if (globalType.equals("FLOAT") || globalType.equals("DOUBLE")) { //$NON-NLS-1$ //$NON-NLS-2$
                    metadataColumn.setPrecision(precisionValue);
                } else {
                    metadataColumn.setPrecision(0);
                }
                // Check the label and add it to the table
                metadataColumn.setLabel(tableEditorView.getMetadataEditor().getNextGeneratedColumnName(label[i]));
                tableEditorView.getMetadataEditor().add(metadataColumn, i);
            }
        }
        checkFieldsValue();
        tableEditorView.getTableViewerCreator().layout();
        informationLabel.setText(Messages.getString("FileStep3.guessTip")); //$NON-NLS-1$
    }

    /**
     * Ensures that fields are set. Update checkEnable / use to checkConnection().
     * 
     * @return
     */
    protected boolean checkFieldsValue() {
        if (metadataNameText.getCharCount() == 0) {
            metadataNameText.forceFocus();
            updateStatus(IStatus.ERROR, Messages.getString("FileStep1.nameAlert")); //$NON-NLS-1$
            return false;
        } else if (!Pattern.matches(RepositoryConstants.REPOSITORY_ITEM_PATTERN, metadataNameText.getText())) {
            metadataNameText.forceFocus();
            updateStatus(IStatus.ERROR, Messages.getString("FileStep1.nameAlertIllegalChar")); //$NON-NLS-1$
            return false;
        } else if (isNameAllowed(metadataNameText.getText())) {
            updateStatus(IStatus.ERROR, Messages.getString("CommonWizard.nameAlreadyExist")); //$NON-NLS-1$
            return false;
        }

        if (tableEditorView.getMetadataEditor().getBeanCount() > 0) {
            updateStatus(IStatus.OK, null);
            return true;
        }
        updateStatus(IStatus.ERROR, Messages.getString("FileStep3.itemAlert")); //$NON-NLS-1$
        return false;
    }

    public void saveMetaData() {
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
            if (getConnection().getFilePath() != null && (!getConnection().getFilePath().equals("")) //$NON-NLS-1$
                    && (tableEditorView.getMetadataEditor().getBeanCount() <= 0)) {
                runShadowProcess();
            }

            if (isReadOnly() != readOnly) {
                adaptFormToReadOnly();
            }
        }
    }

}
