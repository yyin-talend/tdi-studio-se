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

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.datatools.enablement.oda.xml.util.ui.ATreeNode;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.talend.commons.ui.swt.formtools.Form;
import org.talend.commons.ui.swt.formtools.LabelledFileField;
import org.talend.commons.ui.swt.formtools.LabelledText;
import org.talend.commons.ui.swt.formtools.UtilsButton;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.repository.i18n.Messages;
import org.talend.repository.ui.swt.utils.AbstractXmlFileStepForm;

/**
 * @author ocarbone
 * 
 */
public class XmlFileStep1Form extends AbstractXmlFileStepForm {

    private static Logger log = Logger.getLogger(XmlFileStep1Form.class);

    /**
     * Settings.
     */
    private static final int WIDTH_GRIDDATA_PIXEL = 300;

    /**
     * Main Fields.
     */
    private LabelledFileField fileFieldXsd;

    private LabelledFileField fileFieldXml;

    private LabelledText fieldMaskXPattern;

    private Label labelIsGuess;

    private Button checkBoxIsGuess;

    /**
     * Another.
     */
    private boolean filePathIsDone;

    private transient Tree availableXmlTree;

    private ATreeNode treeNode;

    private UtilsButton cancelButton;

    private boolean readOnly;

    private TreePopulator treePopulator;

    /**
     * Constructor to use by RCP Wizard.
     * 
     * @param existingNames
     * 
     * @param Composite
     * @param Wizard
     * @param Style
     */
    public XmlFileStep1Form(Composite parent, ConnectionItem connectionItem, String[] existingNames) {
        super(parent, connectionItem, existingNames);
        setupForm();
    }

    /**
     * 
     * Initialize value, forceFocus first field.
     */
    @Override
    protected void initialize() {

        this.treePopulator = new TreePopulator(availableXmlTree);

        //add init of CheckBoxIsGuess and Determine the Initialize checkFileXsdorXml
//        if (getConnection().getXsdFilePath() != null) {
//            fileFieldXsd.setText(getConnection().getXsdFilePath().replace("\\\\", "\\"));
//            // init the fileViewer
//            this.treePopulator.populateTree(fileFieldXsd.getText(), treeNode);
//            checkFieldsValue();
//        }
        if (getConnection().getXmlFilePath() != null) {
            fileFieldXml.setText(getConnection().getXmlFilePath().replace("\\\\", "\\"));
            // init the fileViewer
            this.treePopulator.populateTree(fileFieldXml.getText(), treeNode);
            checkFieldsValue();
        }
//        if (getConnection().getMaskXPattern() != null) {
//            fieldMaskXPattern.setText(getConnection().getMaskXPattern().replace("\\\\", "\\"));
//        }
    }

    /**
     * DOC ocarbone Comment method "adaptFormToReadOnly".
     * 
     */
    protected void adaptFormToReadOnly() {
        readOnly = isReadOnly();
//        fileFieldXsd.setReadOnly(isReadOnly());
        fileFieldXml.setReadOnly(isReadOnly());
//        fieldMaskXPattern.setReadOnly(isReadOnly());
        // checkBoxIsGuess.setReadOnly(isReadOnly());
        updateStatus(IStatus.OK, "");
    }

    protected void addFields() {
        // Group File Location
        Group group = Form.createGroup(this, 1, Messages.getString("FileStep2.groupDelimitedFileSettings"), 50);
        Composite compositeFileLocation = Form.startNewDimensionnedGridLayout(group, 3, WIDTH_GRIDDATA_PIXEL, 50);

        GridData gridDataFileLocation = new GridData(GridData.FILL_HORIZONTAL);
        gridDataFileLocation.minimumWidth = WIDTH_GRIDDATA_PIXEL;
        group.setLayoutData(gridDataFileLocation);

        // file Field XSD
        String[] xsdExtensions = { "*.xsd", "*.*", "*" };
//        fileFieldXsd = new LabelledFileField(compositeFileLocation, Messages.getString("XmlFileStep1.filepathXsd"), xsdExtensions);

        // checkBox IsGuess
//        checkBoxIsGuess = new Button(compositeFileLocation, SWT.CHECK);
//        labelIsGuess = new Label(compositeFileLocation, SWT.LEFT);
//        GridData gridDataLabel = new GridData();
//        gridDataLabel.horizontalSpan = 2;
//        labelIsGuess.setLayoutData(gridDataLabel);
//        labelIsGuess.setText(Messages.getString("XmlFileStep1.checkBoxIsGuess"));

        // file Field XML
        String[] xmlExtensions = { "*.xml", "*.*", "*" };
        fileFieldXml = new LabelledFileField(compositeFileLocation, Messages.getString("XmlFileStep1.filepathXml"), xmlExtensions);

        // field XmaskPattern
//        fieldMaskXPattern = new LabelledText(compositeFileLocation, Messages.getString("XmlFileStep1.maskXPattern"));

        // Group Schema Viewer
        group = Form.createGroup(this, 1, Messages.getString("XmlFileStep1.groupFileViewer"), 220);
        Composite compositeFileViewer = Form.startNewDimensionnedGridLayout(group, 1, WIDTH_GRIDDATA_PIXEL, 220);

        GridData gridData = new GridData(GridData.FILL_BOTH);
        gridData.minimumWidth = WIDTH_GRIDDATA_PIXEL;
//        gridData.minimumHeight = 150;

        availableXmlTree = new Tree(compositeFileViewer, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
        availableXmlTree.setLayoutData(gridData);
        availableXmlTree.setToolTipText(Messages.getString("FileStep1.fileViewerTip1") + " " + TreePopulator.MAXIMUM_ROWS_TO_PREVIEW + " "
                + Messages.getString("FileStep1.fileViewerTip2"));

        if (!isInWizard()) {
            // Composite BottomButton
            Composite compositeBottomButton = Form.startNewGridLayout(this, 2, false, SWT.CENTER, SWT.CENTER);

            // Button Cancel
            cancelButton = new UtilsButton(compositeBottomButton, Messages.getString("CommonWizard.cancel"), WIDTH_BUTTON_PIXEL,
                    HEIGHT_BUTTON_PIXEL);
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
        }
    }

    /**
     * Main Fields addControls.
     */
    protected void addFieldsListeners() {

        // fileFieldXsd : Event modifyText
//        fileFieldXsd.addModifyListener(new ModifyListener() {
//
//            public void modifyText(final ModifyEvent e) {
//                getConnection().setXsdFilePath(fileFieldXsd.getText());
//                treePopulator.populateTree(fileFieldXsd.getText(), treeNode);
//                checkFieldsValue();
//            }
//        });

        // fileFieldXml : Event modifyText
        fileFieldXml.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                getConnection().setXmlFilePath(fileFieldXml.getText());
                treePopulator.populateTree(fileFieldXml.getText(), treeNode);
                checkFieldsValue();
            }
        });
    }

    /**
     * Ensures that fields are set.
     * 
     * @return
     */
    protected boolean checkFieldsValue() {
        // The fields
        if (fileFieldXml.getText() == "") {
            updateStatus(IStatus.ERROR, Messages.getString("FileStep1.filepathAlert"));
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
