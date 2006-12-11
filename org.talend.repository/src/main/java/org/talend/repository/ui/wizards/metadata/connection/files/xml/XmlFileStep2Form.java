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
package org.talend.repository.ui.wizards.metadata.connection.files.xml;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.datatools.enablement.oda.xml.util.ui.ATreeNode;
import org.eclipse.datatools.enablement.oda.xml.util.ui.XPathPopulationUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.talend.commons.ui.command.CommandStackForComposite;
import org.talend.commons.ui.swt.dialogs.ErrorDialogWidthDetailArea;
import org.talend.commons.ui.swt.formtools.Form;
import org.talend.commons.ui.swt.formtools.LabelledCheckboxCombo;
import org.talend.commons.ui.swt.formtools.UtilsButton;
import org.talend.commons.utils.data.list.IListenableListListener;
import org.talend.commons.utils.data.list.ListenableListEvent;
import org.talend.commons.utils.encoding.CharsetToolkit;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.XmlXPathLoopDescriptor;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.targetschema.editor.XmlExtractorFieldModel;
import org.talend.core.model.targetschema.editor.XmlExtractorLoopModel;
import org.talend.core.utils.XmlArray;
import org.talend.repository.i18n.Messages;
import org.talend.repository.preview.ProcessDescription;
import org.talend.repository.ui.swt.preview.ShadowProcessPreview;
import org.talend.repository.ui.swt.utils.AbstractXmlFileStepForm;
import org.talend.repository.ui.utils.ShadowProcessHelper;

/**
 * @author ocarbone
 * 
 */
public class XmlFileStep2Form extends AbstractXmlFileStepForm {

    private static Logger log = Logger.getLogger(XmlFileStep2Form.class);

    /**
     * Main Fields.
     */

    private transient Tree availableXmlTree;

    private ATreeNode treeNode;

    private XmlExtractorFieldModel fieldsModel;

    private ExtractionLoopWithXPathEditorView loopTableEditorView;

    private ExtractionFieldsWithXPathEditorView fieldsTableEditorView;

    private Button previewButton;

    private Label previewInformationLabel;

    private ShadowProcessPreview xmlFilePreview;

    private Text fileXmlText;

    protected boolean filePathIsDone;

    private UtilsButton cancelButton;

    private boolean readOnly;

    private SashForm xmlToSchemaSash;

    private XmlToXPathLinker linker;

    private TreePopulator treePopulator;

    private XmlExtractorLoopModel loopModel;

    private XmlXPathLoopDescriptor xmlXPathLoopDescriptor;

    /**
     * Constructor to use by RCP Wizard.
     * 
     * @param Composite
     * @param Wizard
     * @param Style
     */
    public XmlFileStep2Form(Composite parent, ConnectionItem connectionItem) {
        super(parent, connectionItem);
        setupForm();
    }

    /**
     * 
     * Initialize value, forceFocus first field.
     */
    @Override
    protected void initialize() {

        this.treePopulator = new TreePopulator(availableXmlTree);

        checkFieldsValue();

        if (xmlXPathLoopDescriptor == null) {
            if (getConnection().getSchema() != null && !getConnection().getSchema().isEmpty()) {
                xmlXPathLoopDescriptor = (XmlXPathLoopDescriptor) getConnection().getSchema().get(0);
            } else {
                xmlXPathLoopDescriptor = ConnectionFactory.eINSTANCE.createXmlXPathLoopDescriptor();
                getConnection().getSchema().add(xmlXPathLoopDescriptor);
            }
        }
        loopModel.setXmlXPathLoopDescriptor(xmlXPathLoopDescriptor);

        fieldsModel.setXmlXPathLoopDescriptor(xmlXPathLoopDescriptor.getSchemaTargets());
        fieldsTableEditorView.getTableViewerCreator().layout();

    }

    /**
     * DOC ocarbone Comment method "adaptFormToReadOnly".
     */
    protected void adaptFormToReadOnly() {
        // readOnly = isReadOnly();
    }

    protected void addFields() {

        // compositeFile Main Fields
        // Composite mainComposite = Form.startNewGridLayout(this, 1);
        SashForm mainComposite = new SashForm(this, SWT.VERTICAL | SWT.SMOOTH);
        mainComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

        // Splitter
        this.xmlToSchemaSash = new SashForm(mainComposite, SWT.HORIZONTAL | SWT.SMOOTH);
        xmlToSchemaSash.setLayoutData(new GridData(GridData.FILL_BOTH));
        xmlToSchemaSash.setBackgroundMode(SWT.INHERIT_FORCE);

        addGroupXmlFileSettings(xmlToSchemaSash, 400, 110);
        addGroupSchemaTarget(xmlToSchemaSash, 300, 110);
        xmlToSchemaSash.setWeights(new int[] { 40, 60 });

        SashForm sash2 = new SashForm(mainComposite, SWT.HORIZONTAL | SWT.SMOOTH);
        sash2.setLayoutData(new GridData(GridData.FILL_BOTH));

        addGroupFileViewer(sash2, 400, 210);
        addGroupXmlViewer(sash2, 300, 110);

        if (!isInWizard()) {
            // Bottom Button
            Composite compositeBottomButton = Form.startNewGridLayout(this, 2, false, SWT.CENTER, SWT.CENTER);
            // Button Cancel
            cancelButton = new UtilsButton(compositeBottomButton, Messages.getString("CommonWizard.cancel"), WIDTH_BUTTON_PIXEL,
                    HEIGHT_BUTTON_PIXEL);
        }
        addUtilsButtonListeners();
    }

    /**
     * add Field to Group Xml File Settings.
     * 
     * @param mainComposite
     * @param form
     * @param width
     * @param height
     */
    private void addGroupXmlFileSettings(final Composite mainComposite, final int width, final int height) {

        // Group Schema Viewer
        Group group = Form.createGroup(mainComposite, 1, Messages.getString("XmlFileStep1.sourceSchema"), height);
        availableXmlTree = new Tree(group, SWT.MULTI | SWT.BORDER);
        // availableXmlTree.setVisible(false);
        GridData gridData2 = new GridData(GridData.FILL_BOTH);
        availableXmlTree.setLayoutData(gridData2);
        availableXmlTree.setToolTipText(Messages.getString("FileStep1.fileViewerTip1") + " " + TreePopulator.MAXIMUM_ROWS_TO_PREVIEW + " "
                + Messages.getString("FileStep1.fileViewerTip2"));
    }

    private void addGroupSchemaTarget(final Composite mainComposite, final int width, final int height) {
        // Group Schema Viewer
        final Group group = Form.createGroup(mainComposite, 1, Messages.getString("XmlFileStep1.groupSchemaTarget"), height);

        CommandStackForComposite commandStack = new CommandStackForComposite(group);

        loopModel = new XmlExtractorLoopModel("Xpath loop expression");

        loopTableEditorView = new ExtractionLoopWithXPathEditorView(loopModel, group);
        loopTableEditorView.getExtendedTableViewer().setCommandStack(commandStack);
        GridData data2 = new GridData(GridData.FILL_BOTH);
        data2.minimumHeight = 70;
        loopTableEditorView.getMainComposite().setLayoutData(data2);

        // Messages.getString("FileStep3.metadataDescription")
        fieldsModel = new XmlExtractorFieldModel("Fields to extract");
        fieldsTableEditorView = new ExtractionFieldsWithXPathEditorView(fieldsModel, group);
        fieldsTableEditorView.getExtendedTableViewer().setCommandStack(commandStack);
        fieldsTableEditorView.getMainComposite().setLayoutData(new GridData(GridData.FILL_BOTH));

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
        // composite Xml File Preview
        Group previewGroup = Form.createGroup(parent, 1, Messages.getString("FileStep2.groupPreview"), height);
        Composite compositeXmlFilePreviewButton = Form.startNewDimensionnedGridLayout(previewGroup, 4, width, HEIGHT_BUTTON_PIXEL);
        height = height - HEIGHT_BUTTON_PIXEL - 15;

        // Preview Button
        previewButton = new Button(compositeXmlFilePreviewButton, SWT.NONE);
        previewButton.setText(Messages.getString("FileStep2.refreshPreview"));
        previewButton.setSize(WIDTH_BUTTON_PIXEL, HEIGHT_BUTTON_PIXEL);

        // simple space
        new Label(compositeXmlFilePreviewButton, SWT.NONE);
        // Information Label
        previewInformationLabel = new Label(compositeXmlFilePreviewButton, SWT.NONE);
        previewInformationLabel
                .setText("                                                                                                                        ");
        previewInformationLabel.setForeground(getDisplay().getSystemColor(SWT.COLOR_BLUE));

        Composite compositeXmlFilePreview = Form.startNewDimensionnedGridLayout(previewGroup, 1, width, height);

        // Xml File Preview
        xmlFilePreview = new ShadowProcessPreview(compositeXmlFilePreview, null, width, height - 10);
        xmlFilePreview.newTablePreview();
    }

    /**
     * add Field to Group File Viewer.
     * 
     * @param parent
     * @param form
     * @param width
     * @param height
     */
    private void addGroupXmlViewer(final Composite parent, final int width, int height) {
        // Group File Viewer
        Group group = Form.createGroup(parent, 1, Messages.getString("FileStep1.groupFileViewer"), height);
        Composite compositeFileViewer = Form.startNewDimensionnedGridLayout(group, 1, width, HEIGHT_BUTTON_PIXEL);

        fileXmlText = new Text(compositeFileViewer, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
        GridData gridData = new GridData(GridData.FILL_BOTH);
        gridData.minimumWidth = width;
        gridData.minimumHeight = HEIGHT_BUTTON_PIXEL;
        fileXmlText.setLayoutData(gridData);
        fileXmlText.setToolTipText(Messages.getString("FileStep1.fileViewerTip1") + " " + TreePopulator.MAXIMUM_ROWS_TO_PREVIEW + " "
                + Messages.getString("FileStep1.fileViewerTip2"));
        fileXmlText.setEditable(false);
        fileXmlText.setText(Messages.getString("FileStep1.fileViewerAlert"));
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

        // adapt the limit to the preview
        processDescription.setLimitRows(TreePopulator.MAXIMUM_ROWS_TO_PREVIEW);
        return processDescription;
    }

    /**
     * clear the table preview.
     */
    void clearPreview() {
        xmlFilePreview.clearTablePreview();
    }

    /**
     * refreshPreview use ShadowProcess to refresh the preview.
     */
    void refreshPreview() {
        clearPreview();

        // if no file, the process don't be executed
        if (getConnection().getXsdFilePath() != null && !getConnection().getXsdFilePath().equals("")
                && getConnection().getXmlFilePath() != null && !getConnection().getXmlFilePath().equals("")) {
            previewInformationLabel.setText("   " + Messages.getString("FileStep2.filePathIncomplete"));
            return;
        }

        // if incomplete settings, , the process don't be executed
        if (!checkFieldsValue()) {
            previewInformationLabel.setText("   " + Messages.getString("FileStep2.settingsIncomplete"));
            return;
        }

        previewInformationLabel.setText("   " + Messages.getString("FileStep2.previewProgress"));

        // get the XmlArray width an adapt ProcessDescription
        try {
            ProcessDescription processDescription = getProcessDescription();
            XmlArray xmlArray = ShadowProcessHelper.getXmlArray(processDescription, "FILE_XML");
            if (xmlArray == null) {
                previewInformationLabel.setText("   " + Messages.getString("FileStep2.previewFailure"));
            } else {
                previewInformationLabel.setText("   " + Messages.getString("FileStep2.previewIsDone"));

                // refresh TablePreview on this step
                xmlFilePreview.refreshTablePreview(xmlArray, false);
                previewInformationLabel.setText("");
            }
        } catch (CoreException e) {
            previewInformationLabel.setText("   " + Messages.getString("FileStep2.previewFailure"));
            new ErrorDialogWidthDetailArea(getShell(), PID, Messages.getString("FileStep2.previewFailure"), e.getMessage());
            log.error(Messages.getString("FileStep2.previewFailure") + " " + e.getMessage());
        }
    }

    /**
     * Main Fields addControls.
     */
    protected void addFieldsListeners() {
        // add listener to tableMetadata (listen the event of the toolbars)
        fieldsTableEditorView.getExtendedTableModel().addAfterOperationListListener(new IListenableListListener() {

            public void handleEvent(ListenableListEvent event) {
                checkFieldsValue();
            }
        });

        // availableXmlTree.addSelectionListener(new SelectionAdapter() {
        //
        // /*
        // * (non-Javadoc)
        // *
        // * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
        // */
        // public void widgetSelected(SelectionEvent e) {
        // List xpathList = getSelectedXPath(availableXmlTree.getSelection()[0]);
        // if (xpathList != null) {
        // System.out.println("Node : " + availableXmlTree.getSelection()[0].getText() + " Xpath : " +
        // xpathList.get(0));
        // }
        // }
        // });
    }

    /**
     * get the standby XPath expression.
     * 
     * @return
     */
    protected List getSelectedXPath(TreeItem selected) {
        // TreeItem selected = this.selectedItem;
        String rootPath = "";
        if (selected.getData() instanceof ATreeNode) {
            ATreeNode node = (ATreeNode) selected.getData();
            // if (node.getType() == ATreeNode.ATTRIBUTE_TYPE) {
            // return null;
            // } else {
            rootPath = "/" + selected.getText();
            // }
        }

        while (selected.getParentItem() != null) {
            selected = selected.getParentItem();
            if (selected.getData() instanceof ATreeNode) {
                ATreeNode node = (ATreeNode) selected.getData();
                if (node.getType() == ATreeNode.ELEMENT_TYPE) {
                    rootPath = "/" + selected.getText() + rootPath;
                }
            }
        }
        return XPathPopulationUtil.populateRootPath(rootPath);

    }

    /**
     * Ensures that fields are set. Update checkEnable / use to checkConnection().
     * 
     * @return
     */
    protected boolean checkFieldsValue() {
        previewInformationLabel.setText("   " + Messages.getString("FileStep2.settingsIncomplete"));
        updateStatus(IStatus.OK, null);
        previewButton.setEnabled(false);

        // Labelled Checkbox Combo (Row to Skip and Limit)
        ArrayList<LabelledCheckboxCombo> labelledCheckboxCombo2Control = new ArrayList<LabelledCheckboxCombo>();

        Iterator<LabelledCheckboxCombo> iCheckboxCombo;
        LabelledCheckboxCombo labelledCheckboxCombo;

        for (iCheckboxCombo = labelledCheckboxCombo2Control.iterator(); iCheckboxCombo.hasNext();) {
            labelledCheckboxCombo = iCheckboxCombo.next();
            // if the checkbox is checked, check Numeric value
            if (labelledCheckboxCombo.getCheckbox().getSelection()) {
                if (labelledCheckboxCombo.getText() == "") {
                    updateStatus(IStatus.ERROR, labelledCheckboxCombo.getLabelText() + Messages.getString("FileStep2.mustBePrecised"));
                    return false;
                }
            }
        }

        previewInformationLabel.setText("");
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
                if (!previewButton.getText().equals(Messages.getString("FileStep2.wait"))) {
                    previewButton.setText(Messages.getString("FileStep2.wait"));
                    refreshPreview();
                } else {
                    previewButton.setText(Messages.getString("FileStep2.refreshPreview"));
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
     * checkFileFieldsValue active fileViewer if file exist.
     */
    private void checkFilePathAndManageIt() {
        updateStatus(IStatus.OK, null);
        filePathIsDone = false;
        if (getConnection().getXmlFilePath() == "") {
            fileXmlText.setText(Messages.getString("FileStep1.fileViewerTip1") + " " + TreePopulator.MAXIMUM_ROWS_TO_PREVIEW + " "
                    + Messages.getString("FileStep1.fileViewerTip2"));
        } else {
            fileXmlText.setText(Messages.getString("FileStep1.fileViewerProgress"));

            String previewRows = "";
            try {

                File file = new File(getConnection().getXmlFilePath());
                Charset guessedCharset = CharsetToolkit.guessEncoding(file, 4096);

                String str;
                int numberLine = 0;
                // read the file width the limit : MAXIMUM_ROWS_TO_PREVIEW
                BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(getConnection().getXmlFilePath()),
                        guessedCharset.displayName()));
                while (((str = in.readLine()) != null) && (numberLine <= TreePopulator.MAXIMUM_ROWS_TO_PREVIEW)) {
                    numberLine++;
                    previewRows = previewRows + str + "\n";
                }
                in.close();

                // show lines
                fileXmlText.setText(previewRows);
                filePathIsDone = true;

            } catch (Exception e) {
                String msgError = Messages.getString("FileStep1.filepath") + " \"" + fileXmlText.getText().replace("\\\\", "\\") + "\"\n";
                if (e instanceof FileNotFoundException) {
                    msgError = msgError + Messages.getString("FileStep1.fileNotFoundException");
                } else if (e instanceof EOFException) {
                    msgError = msgError + Messages.getString("FileStep1.eofException");
                } else if (e instanceof IOException) {
                    msgError = msgError + Messages.getString("FileStep1.fileLocked");
                } else {
                    msgError = msgError + Messages.getString("FileStep1.noExist");
                }
                fileXmlText.setText(msgError);
                if (!isReadOnly()) {
                    updateStatus(IStatus.ERROR, msgError);
                }
                log.error(msgError + " " + e.getMessage());
            }

            checkFieldsValue();
        }
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
            if (this.linker != null) {
                this.linker.removeAllLinks();
            }
            this.treePopulator.populateTree(getConnection().getXmlFilePath(), treeNode);
            ScrollBar verticalBar = availableXmlTree.getVerticalBar();
            if (verticalBar != null) {
                verticalBar.setSelection(0);
            }

            if (this.linker == null) {
                this.linker = new XmlToXPathLinker(this.xmlToSchemaSash);
                this.linker.init(availableXmlTree, loopTableEditorView, fieldsTableEditorView, treePopulator);
                loopTableEditorView.setLinker(this.linker);
                fieldsTableEditorView.setLinker(this.linker);
            } else {
                this.linker.createLinks();
            }
            checkFilePathAndManageIt();
            // Refresh the preview width the adapted rowSeparator
            // If metadata exist, refreshMetadata
            if (getConnection().getXsdFilePath() != null && !getConnection().getXsdFilePath().equals("")
                    && getConnection().getXmlFilePath() != null && !getConnection().getXmlFilePath().equals("")) {
                refreshPreview();
            }
            // if (isReadOnly() != readOnly) {
            // adaptFormToReadOnly();
            // }
        }
    }

}
