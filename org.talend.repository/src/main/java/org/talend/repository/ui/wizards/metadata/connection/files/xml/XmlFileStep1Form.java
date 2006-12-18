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
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.apache.log4j.Logger;
import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
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
import org.talend.commons.exception.BusinessException;
import org.talend.commons.ui.swt.dialogs.ErrorDialogWidthDetailArea;
import org.talend.commons.ui.swt.formtools.Form;
import org.talend.commons.ui.swt.formtools.LabelledCombo;
import org.talend.commons.ui.swt.formtools.LabelledFileField;
import org.talend.commons.ui.swt.formtools.LabelledText;
import org.talend.commons.ui.swt.formtools.UtilsButton;
import org.talend.commons.utils.encoding.CharsetToolkit;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.metadata.EMetadataEncoding;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.designer.codegen.perlmodule.IPerlModuleService;
import org.talend.designer.codegen.perlmodule.ModuleNeeded.ModuleStatus;
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

    private LabelledCombo encodingCombo;

    private String encoding;

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

        // add init of CheckBoxIsGuess and Determine the Initialize checkFileXsdorXml
        // if (getConnection().getXsdFilePath() != null) {
        // fileFieldXsd.setText(getConnection().getXsdFilePath().replace("\\\\", "\\"));
        // // init the fileViewer
        // this.treePopulator.populateTree(fileFieldXsd.getText(), treeNode);
        // checkFieldsValue();
        // }
        if (getConnection().getXmlFilePath() != null) {
            fileFieldXml.setText(getConnection().getXmlFilePath().replace("\\\\", "\\"));
            // init the fileViewer
            this.treePopulator.populateTree(fileFieldXml.getText(), treeNode);
            checkFieldsValue();
        }

        // Fields to the Group Delimited File Settings
        if (getConnection().getEncoding() != null && !getConnection().getEncoding().equals("")) {
            encodingCombo.setText(getConnection().getEncoding());
        } else {
            encodingCombo.select(0);
        }
        encodingCombo.clearSelection();

        // if (getConnection().getMaskXPattern() != null) {
        // fieldMaskXPattern.setText(getConnection().getMaskXPattern().replace("\\\\", "\\"));
        // }
    }

    /**
     * DOC ocarbone Comment method "adaptFormToReadOnly".
     * 
     */
    protected void adaptFormToReadOnly() {
        readOnly = isReadOnly();
        // fileFieldXsd.setReadOnly(isReadOnly());
        fileFieldXml.setReadOnly(isReadOnly());
        // fieldMaskXPattern.setReadOnly(isReadOnly());
        // checkBoxIsGuess.setReadOnly(isReadOnly());
        updateStatus(IStatus.OK, "");
    }

    protected void addFields() {

        // Group File Location
        Group group = Form.createGroup(this, 1, Messages.getString("FileStep2.groupDelimitedFileSettings"), 100);
        Composite compositeFileLocation = Form.startNewDimensionnedGridLayout(group, 3, WIDTH_GRIDDATA_PIXEL, 100);

        GridData gridDataFileLocation = new GridData(GridData.FILL_HORIZONTAL);
        gridDataFileLocation.minimumWidth = WIDTH_GRIDDATA_PIXEL;
        group.setLayoutData(gridDataFileLocation);

        // file Field XSD
        String[] xsdExtensions = { "*.xsd", "*.*", "*" };
        // fileFieldXsd = new LabelledFileField(compositeFileLocation, Messages.getString("XmlFileStep1.filepathXsd"),
        // xsdExtensions);

        // checkBox IsGuess
        // checkBoxIsGuess = new Button(compositeFileLocation, SWT.CHECK);
        // labelIsGuess = new Label(compositeFileLocation, SWT.LEFT);
        // GridData gridDataLabel = new GridData();
        // gridDataLabel.horizontalSpan = 2;
        // labelIsGuess.setLayoutData(gridDataLabel);
        // labelIsGuess.setText(Messages.getString("XmlFileStep1.checkBoxIsGuess"));

        // file Field XML
        String[] xmlExtensions = { "*.xml", "*.*", "*" };
        fileFieldXml = new LabelledFileField(compositeFileLocation, Messages.getString("XmlFileStep1.filepathXml"),
                xmlExtensions);

        EMetadataEncoding[] values = EMetadataEncoding.values();
        String[] encodingData = new String[values.length];
        for (int j = 0; j < values.length; j++) {
            encodingData[j] = values[j].getName();
        }

        encodingCombo = new LabelledCombo(compositeFileLocation, Messages.getString("FileStep2.encoding"), Messages
                .getString("FileStep2.encodingTip"), encodingData, 1, true, SWT.NONE);

        // field XmaskPattern
        // fieldMaskXPattern = new LabelledText(compositeFileLocation, Messages.getString("XmlFileStep1.maskXPattern"));

        // Group Schema Viewer
        group = Form.createGroup(this, 1, Messages.getString("XmlFileStep1.groupFileViewer"), 220);
        Composite compositeFileViewer = Form.startNewDimensionnedGridLayout(group, 1, WIDTH_GRIDDATA_PIXEL, 220);

        GridData gridData = new GridData(GridData.FILL_BOTH);
        gridData.minimumWidth = WIDTH_GRIDDATA_PIXEL;
        // gridData.minimumHeight = 150;

        availableXmlTree = new Tree(compositeFileViewer, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
        availableXmlTree.setLayoutData(gridData);
        availableXmlTree.setToolTipText(Messages.getString("FileStep1.fileViewerTip1") + " "
                + TreePopulator.MAXIMUM_ROWS_TO_PREVIEW + " " + Messages.getString("FileStep1.fileViewerTip2"));

        if (!isInWizard()) {
            // Composite BottomButton
            Composite compositeBottomButton = Form.startNewGridLayout(this, 2, false, SWT.CENTER, SWT.CENTER);

            // Button Cancel
            cancelButton = new UtilsButton(compositeBottomButton, Messages.getString("CommonWizard.cancel"),
                    WIDTH_BUTTON_PIXEL, HEIGHT_BUTTON_PIXEL);
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
        // fileFieldXsd.addModifyListener(new ModifyListener() {
        //
        // public void modifyText(final ModifyEvent e) {
        // getConnection().setXsdFilePath(fileFieldXsd.getText());
        // treePopulator.populateTree(fileFieldXsd.getText(), treeNode);
        // checkFieldsValue();
        // }
        // });

        // fileFieldXml : Event modifyText
        fileFieldXml.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                getConnection().setXmlFilePath(fileFieldXml.getText());

                try {
                    File file = new File(getConnection().getXmlFilePath());
                    Charset guessedCharset = CharsetToolkit.guessEncoding(file, 4096);

                    String str;
                    BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(getConnection()
                            .getXmlFilePath()), guessedCharset.displayName()));
                    while ((str = in.readLine()) != null) {
                        if (str.contains("encoding")) {
                            String regex = "^<\\?xml\\s*version=\\\"[^\\\"]*\\\"\\s*encoding=\\\"([^\\\"]*)\\\"\\?>$";

                            Perl5Compiler compiler = new Perl5Compiler();
                            Perl5Matcher matcher = new Perl5Matcher();
                            Pattern pattern = null;
                            try {
                                pattern = compiler.compile(regex);
                                if (matcher.contains(str, pattern)) {
                                    MatchResult matchResult = matcher.getMatch();
                                    if (matchResult != null) {
                                        encoding = matchResult.group(1);
                                    }
                                }
                            } catch (MalformedPatternException malE) {

                            }
                        }
                    }
                    in.close();
                } catch (Exception ex) {

                }
                getConnection().setEncoding(encoding);
                if (encoding != null && !("").equals(encoding)) {
                    encodingCombo.setText(encoding);
                } else {
                    encodingCombo.setText("UTF-8");
                }
                treePopulator.populateTree(fileFieldXml.getText(), treeNode);
                checkFieldsValue();
            }
        });

        // Event encodingCombo
        encodingCombo.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                getConnection().setEncoding(encodingCombo.getText());
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
        if (super.isVisible()) {
            // Fields to the Group Delimited File Settings
            if (getConnection().getEncoding() != null && !getConnection().getEncoding().equals("")) {
                encodingCombo.setText(getConnection().getEncoding());
            } else {
                encodingCombo.select(0);
            }

            // PTODO cantoine : make internationalisation for Perl Module not Installed, detail Message URL of PPM
            // module ??
            IPerlModuleService perlModuleService = (IPerlModuleService) GlobalServiceRegister.getDefault().getService(
                    IPerlModuleService.class);
            try {
                ModuleStatus status = perlModuleService.getModuleStatus("XML::LibXML");
                if (!("INSTALLED").equals(status.name())) {
                    new ErrorDialogWidthDetailArea(getShell(), PID, Messages.getString("FileStep.moduleFailure")+" XML::Lib "+Messages.getString("FileStep.moduleFailureEnd"),
                            Messages.getString("FileStep.moduleDetailMessage"));
                    log.error(Messages.getString("FileStep.moduleFailure")+" XML::Lib "+Messages.getString("FileStep.moduleFailureEnd"));
                }
            } catch (BusinessException e) {
                new ErrorDialogWidthDetailArea(getShell(), PID, Messages.getString("FileStep.moduleFailure")+" XML::Lib "+Messages.getString("FileStep.moduleFailureEnd"), e
                        .getMessage());
                log.error(Messages.getString("FileStep.moduleFailure")+" XML::Lib "+Messages.getString("FileStep.moduleFailureEnd"));
            }
        }
    }
}
