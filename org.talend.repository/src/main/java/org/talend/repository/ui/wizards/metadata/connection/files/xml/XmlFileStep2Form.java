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
package org.talend.repository.ui.wizards.metadata.connection.files.xml;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.ProgressMonitorWrapper;
import org.eclipse.datatools.enablement.oda.xml.util.ui.ATreeNode;
import org.eclipse.datatools.enablement.oda.xml.util.ui.XPathPopulationUtil;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.internal.dialogs.EventLoopProgressMonitor;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.ui.command.CommandStackForComposite;
import org.talend.commons.ui.swt.dialogs.ErrorDialogWidthDetailArea;
import org.talend.commons.ui.swt.formtools.Form;
import org.talend.commons.ui.swt.formtools.LabelledCheckboxCombo;
import org.talend.commons.ui.swt.formtools.UtilsButton;
import org.talend.commons.ui.ws.WindowSystem;
import org.talend.commons.utils.data.list.IListenableListListener;
import org.talend.commons.utils.data.list.ListenableListEvent;
import org.talend.commons.utils.encoding.CharsetToolkit;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.XmlXPathLoopDescriptor;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.targetschema.editor.XmlExtractorFieldModel;
import org.talend.core.model.targetschema.editor.XmlExtractorLoopModel;
import org.talend.core.utils.CsvArray;
import org.talend.core.utils.XmlArray;
import org.talend.repository.i18n.Messages;
import org.talend.repository.preview.AsynchronousPreviewHandler;
import org.talend.repository.preview.IPreviewHandlerListener;
import org.talend.repository.preview.PreviewHandlerEvent;
import org.talend.repository.preview.ProcessDescription;
import org.talend.repository.preview.StoppablePreviewLoader;
import org.talend.repository.preview.PreviewHandlerEvent.TYPE;
import org.talend.repository.ui.swt.preview.ShadowProcessPreview;
import org.talend.repository.ui.swt.utils.AbstractXmlFileStepForm;
import org.talend.repository.ui.swt.utils.IRefreshable;
import org.talend.repository.ui.utils.ShadowProcessHelper;
import org.talend.repository.ui.wizards.metadata.connection.files.xml.extraction.ExtractionFieldsWithXPathEditorView;
import org.talend.repository.ui.wizards.metadata.connection.files.xml.extraction.ExtractionLoopWithXPathEditorView;
import org.talend.repository.ui.wizards.metadata.connection.files.xml.extraction.XmlToXPathLinker;

/**
 * @author ocarbone
 * 
 */
public class XmlFileStep2Form extends AbstractXmlFileStepForm implements IRefreshable {

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

    private IPreviewHandlerListener previewHandlerListener;

    private static Boolean firstTimeWizardOpened = null;

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

        if (firstTimeWizardOpened == null) {
            firstTimeWizardOpened = Boolean.TRUE;
        } else if (firstTimeWizardOpened.equals(Boolean.TRUE)) {
            firstTimeWizardOpened = Boolean.FALSE;
        }

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
            cancelButton = new UtilsButton(compositeBottomButton,
                    Messages.getString("CommonWizard.cancel"), WIDTH_BUTTON_PIXEL, //$NON-NLS-1$
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
        Group group = Form.createGroup(mainComposite, 1, Messages.getString("XmlFileStep1.sourceSchema"), height); //$NON-NLS-1$
        group.setBackground(null);

        availableXmlTree = new Tree(group, SWT.MULTI | SWT.BORDER);

        // availableXmlTree.setVisible(false);
        GridData gridData2 = new GridData(GridData.FILL_BOTH);
        availableXmlTree.setLayoutData(gridData2);
    }

    private void addGroupSchemaTarget(final Composite mainComposite, final int width, final int height) {
        // Group Schema Viewer
        final Group group = Form.createGroup(mainComposite, 1,
                Messages.getString("XmlFileStep1.groupSchemaTarget"), height); //$NON-NLS-1$

        // ///////////////////////////////////////////
        // to correct graphic bug under Linux-GTK when the wizard is opened the first time
        if (WindowSystem.isGTK() && firstTimeWizardOpened.equals(Boolean.TRUE)) {
            group.addListener(SWT.Paint, new Listener() {

                public void handleEvent(Event event) {
                    Point offsetPoint = event.display.map(linker.getBgDrawableComposite(), group, new Point(0, 0));
                    linker.setOffset(offsetPoint);
                    linker.drawBackground(event.gc);
                }

            });
        }
        // ///////////////////////////////////////////

        group.setBackgroundMode(SWT.INHERIT_FORCE);

        CommandStackForComposite commandStack = new CommandStackForComposite(group);

        loopModel = new XmlExtractorLoopModel("Xpath loop expression"); //$NON-NLS-1$

        loopTableEditorView = new ExtractionLoopWithXPathEditorView(loopModel, group);
        loopTableEditorView.getExtendedTableViewer().setCommandStack(commandStack);
        GridData data2 = new GridData(GridData.FILL_HORIZONTAL);
        data2.heightHint = 70;
        if (WindowSystem.isGTK()) {
            data2.heightHint = 90;
        }
        final Composite loopTableEditorComposite = loopTableEditorView.getMainComposite();
        loopTableEditorComposite.setLayoutData(data2);
        loopTableEditorComposite.setBackground(null);
        // ///////////////////////////////////////////
        // to correct graphic bug under Linux-GTK when the wizard is opened the first time
        if (WindowSystem.isGTK() && firstTimeWizardOpened.equals(Boolean.TRUE)) {
            loopTableEditorComposite.addListener(SWT.Paint, new Listener() {

                public void handleEvent(Event event) {
                    Point offsetPoint = event.display.map(linker.getBgDrawableComposite(), loopTableEditorComposite,
                            new Point(0, 0));
                    linker.setOffset(offsetPoint);
                    linker.drawBackground(event.gc);
                }

            });
        }
        // ///////////////////////////////////////////

        // Messages.getString("FileStep3.metadataDescription")
        fieldsModel = new XmlExtractorFieldModel("Fields to extract"); //$NON-NLS-1$
        fieldsTableEditorView = new ExtractionFieldsWithXPathEditorView(fieldsModel, group);
        fieldsTableEditorView.getExtendedTableViewer().setCommandStack(commandStack);
        final Composite fieldTableEditorComposite = fieldsTableEditorView.getMainComposite();
        fieldTableEditorComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
        fieldTableEditorComposite.setBackground(null);
        // ///////////////////////////////////////////
        // to correct graphic bug under Linux-GTK when the wizard is opened the first time
        if (WindowSystem.isGTK() && firstTimeWizardOpened.equals(Boolean.TRUE)) {
            fieldTableEditorComposite.addListener(SWT.Paint, new Listener() {

                public void handleEvent(Event event) {
                    Point offsetPoint = event.display.map(linker.getBgDrawableComposite(), fieldTableEditorComposite,
                            new Point(0, 0));
                    linker.setOffset(offsetPoint);
                    linker.drawBackground(event.gc);
                }

            });
        }
        // ///////////////////////////////////////////

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
        Group previewGroup = Form.createGroup(parent, 1, Messages.getString("FileStep2.groupPreview"), height); //$NON-NLS-1$
        // Composite compositeXmlFilePreviewButton = Form.startNewDimensionnedGridLayout(previewGroup, 4, width,
        // HEIGHT_BUTTON_PIXEL);
        // height = height - HEIGHT_BUTTON_PIXEL - 15;

        previewGroup.setLayout(new GridLayout());

        Composite preivewButtonPart = new Composite(previewGroup, SWT.NONE);
        preivewButtonPart.setLayout(new GridLayout(3, false));
        preivewButtonPart.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        // Preview Button
        previewButton = new Button(preivewButtonPart, SWT.NONE);
        previewButton.setText(Messages.getString("FileStep2.refreshPreview")); //$NON-NLS-1$
        previewButton.setSize(WIDTH_BUTTON_PIXEL, HEIGHT_BUTTON_PIXEL);

        Label limitLabel = new Label(preivewButtonPart, SWT.RIGHT);
        limitLabel.setText(Messages.getString("XmlFileStep2Form.limitOfRow")); //$NON-NLS-1$
        GridData labelGd = new GridData(GridData.FILL_HORIZONTAL);
        limitLabel.setLayoutData(labelGd);

        final Text limitText = new Text(preivewButtonPart, SWT.BORDER | SWT.RIGHT);
        GridData textGd = new GridData(30, SWT.DEFAULT);
        limitText.setLayoutData(textGd);
        XmlArray.setLimitToDefault();
        limitText.setText(String.valueOf(XmlArray.getRowLimit()));
        limitText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                String limitValue = limitText.getText();
                if (!limitValue.matches("\\d+")) {
                    limitText.setText(String.valueOf(XmlArray.getRowLimit()));
                } else {
                    int limit = Integer.valueOf(limitValue);
                    XmlArray.setRowLimit(limit);
                }
            }

        });

        limitText.addFocusListener(new FocusListener() {

            public void focusGained(FocusEvent e) {

            }

            public void focusLost(FocusEvent e) {
                limitText.setText(String.valueOf(XmlArray.getRowLimit()));
            }

        });

        previewInformationLabel = new Label(previewGroup, SWT.NONE);
        previewInformationLabel.setForeground(getDisplay().getSystemColor(SWT.COLOR_BLUE));

        // Xml File Preview
        xmlFilePreview = new ShadowProcessPreview(previewGroup, null, width, height - 10);
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
        Group group = Form.createGroup(parent, 1, Messages.getString("FileStep1.groupFileViewer"), height); //$NON-NLS-1$
        Composite compositeFileViewer = Form.startNewDimensionnedGridLayout(group, 1, width, HEIGHT_BUTTON_PIXEL);

        fileXmlText = new Text(compositeFileViewer, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
        GridData gridData = new GridData(GridData.FILL_BOTH);
        gridData.minimumWidth = width;
        gridData.minimumHeight = HEIGHT_BUTTON_PIXEL;
        fileXmlText.setLayoutData(gridData);
        fileXmlText
                .setToolTipText(Messages.getString("FileStep1.fileViewerTip1") + " " + TreePopulator.getMaximumRowsToPreview() + " " //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                        + Messages.getString("FileStep1.fileViewerTip2")); //$NON-NLS-1$
        fileXmlText.setEditable(false);
        fileXmlText.setText(Messages.getString("FileStep1.fileViewerAlert")); //$NON-NLS-1$
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
        xmlFilePreview.clearTablePreview();
    }

    /**
     * refreshPreview use ShadowProcess to refresh the preview.
     */
    void refreshPreview() {
        clearPreview();

        // if no file, the process don't be executed
        if (getConnection().getXmlFilePath() == null || getConnection().getXmlFilePath().equals("")) { //$NON-NLS-1$
            previewInformationLabel.setText("   " + Messages.getString("FileStep2.filePathIncomplete")); //$NON-NLS-1$ //$NON-NLS-2$
            return;
        }

        // if incomplete settings, , the process don't be executed
        if (!checkFieldsValue()) {
            previewInformationLabel.setText("   " + Messages.getString("FileStep2.settingsIncomplete")); //$NON-NLS-1$ //$NON-NLS-2$
            return;
        }

        previewInformationLabel.setText("   " + Messages.getString("FileStep2.previewProgress")); //$NON-NLS-1$ //$NON-NLS-2$

        AsynchronousPreviewHandler<CsvArray> previewHandler = null;
        try {
            previewHandler = (AsynchronousPreviewHandler<CsvArray>) ShadowProcessHelper.createPreviewHandler();
        } catch (CoreException e) {
            previewInError(e);
            return;
        }

        StoppablePreviewLoader previewLoader = new StoppablePreviewLoader<CsvArray>(previewHandler,
                previewInformationLabel) {

            /*
             * (non-Javadoc)
             * 
             * @see org.talend.repository.ui.wizards.metadata.connection.files.xml.StoppablePreviewLoader#previewEnded(java.lang.Object)
             */
            @Override
            protected void previewEnded(CsvArray result) {
                xmlFilePreview.refreshTablePreview(result, false, ((XmlXPathLoopDescriptor) getConnection().getSchema()
                        .get(0)).getSchemaTargets());
            }

            @Override
            public void previewInError(CoreException e) {
                XmlFileStep2Form.this.previewInError(e);
            }

        };

        previewLoader.load(getProcessDescription());

    }

    /**
     * DOC amaumont Comment method "previewInFileError".
     * 
     * @param e
     */
    protected void previewInError(CoreException e) {

        String errorMessage = null;
        if (e != null) {
            errorMessage = e.getMessage();
        }

        previewInformationLabel.setText("   " + Messages.getString("FileStep2.previewFailure")); //$NON-NLS-1$ //$NON-NLS-2$
        new ErrorDialogWidthDetailArea(previewInformationLabel.getShell(), PID, Messages
                .getString("FileStep2.previewFailure"), //$NON-NLS-1$
                errorMessage);
        log.error(Messages.getString("FileStep2.previewFailure") + " " + errorMessage); //$NON-NLS-1$ //$NON-NLS-2$

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
    }

    /**
     * get the standby XPath expression.
     * 
     * @return
     */
    protected List getSelectedXPath(TreeItem selected) {
        // TreeItem selected = this.selectedItem;
        String rootPath = ""; //$NON-NLS-1$
        if (selected.getData() instanceof ATreeNode) {
            ATreeNode node = (ATreeNode) selected.getData();
            rootPath = "/" + selected.getText(); //$NON-NLS-1$
        }

        while (selected.getParentItem() != null) {
            selected = selected.getParentItem();
            if (selected.getData() instanceof ATreeNode) {
                ATreeNode node = (ATreeNode) selected.getData();
                if (node.getType() == ATreeNode.ELEMENT_TYPE) {
                    rootPath = "/" + selected.getText() + rootPath; //$NON-NLS-1$
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
        previewInformationLabel.setText("   " + Messages.getString("FileStep2.settingsIncomplete")); //$NON-NLS-1$ //$NON-NLS-2$
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
                if (labelledCheckboxCombo.getText() == "") { //$NON-NLS-1$
                    updateStatus(IStatus.ERROR, labelledCheckboxCombo.getLabelText()
                            + Messages.getString("FileStep2.mustBePrecised")); //$NON-NLS-1$
                    return false;
                }
            }
        }

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
                    if (getConnection().getXmlFilePath() != null
                            && !getConnection().getXmlFilePath().equals("") //$NON-NLS-1$
                            && getConnection().getSchema() != null
                            && !getConnection().getSchema().isEmpty()
                            && ((XmlXPathLoopDescriptor) getConnection().getSchema().get(0)).getAbsoluteXPathQuery() != null
                            && !("").equals(((XmlXPathLoopDescriptor) getConnection().getSchema().get(0)).getAbsoluteXPathQuery()) //$NON-NLS-1$
                            && ((XmlXPathLoopDescriptor) getConnection().getSchema().get(0)).getSchemaTargets() != null
                            && !((XmlXPathLoopDescriptor) getConnection().getSchema().get(0)).getSchemaTargets()
                                    .isEmpty()) {
                        refreshPreview();
                    } else {
                        previewButton.setText(Messages.getString("FileStep2.refreshPreview")); //$NON-NLS-1$
                        if (!previewButton.getEnabled()) {
                            new ErrorDialogWidthDetailArea(getShell(), PID,
                                    Messages.getString("FileStep2.noresult"), Messages //$NON-NLS-1$
                                            .getString("FileStep2.noresultDetailMessage")); //$NON-NLS-1$
                            log.error(Messages.getString("FileStep2.noresult")); //$NON-NLS-1$
                            previewButton.setEnabled(true);
                        } else {
                            previewButton.setEnabled(false);
                        }
                    }
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
    }

    /**
     * checkFileFieldsValue active fileViewer if file exist.
     * 
     * @throws IOException
     */
    private void checkFilePathAndManageIt() {
        updateStatus(IStatus.OK, null);
        filePathIsDone = false;
        if (getConnection().getXmlFilePath() == "") { //$NON-NLS-1$
            fileXmlText
                    .setText(Messages.getString("FileStep1.fileViewerTip1") + " " + TreePopulator.getMaximumRowsToPreview() + " " //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                            + Messages.getString("FileStep1.fileViewerTip2")); //$NON-NLS-1$
        } else {
            fileXmlText.setText(Messages.getString("FileStep1.fileViewerProgress")); //$NON-NLS-1$

            StringBuilder previewRows = new StringBuilder();
            BufferedReader in = null;

            try {

                File file = new File(getConnection().getXmlFilePath());
                Charset guessedCharset = CharsetToolkit.guessEncoding(file, 4096);

                String str;
                in = new BufferedReader(new InputStreamReader(new FileInputStream(getConnection().getXmlFilePath()),
                        guessedCharset.displayName()));
                while ((str = in.readLine()) != null) {
                    previewRows.append(str + "\n"); //$NON-NLS-1$
                }

                // show lines
                fileXmlText.setText(new String(previewRows));
                filePathIsDone = true;

            } catch (Exception e) {
                String msgError = Messages.getString("FileStep1.filepath") + " \"" + fileXmlText.getText().replace("\\\\", "\\") + "\"\n"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
                if (e instanceof FileNotFoundException) {
                    msgError = msgError + Messages.getString("FileStep1.fileNotFoundException"); //$NON-NLS-1$
                } else if (e instanceof EOFException) {
                    msgError = msgError + Messages.getString("FileStep1.eofException"); //$NON-NLS-1$
                } else if (e instanceof IOException) {
                    msgError = msgError + Messages.getString("FileStep1.fileLocked"); //$NON-NLS-1$
                } else {
                    msgError = msgError + Messages.getString("FileStep1.noExist"); //$NON-NLS-1$
                }
                fileXmlText.setText(msgError);
                if (!isReadOnly()) {
                    updateStatus(IStatus.ERROR, msgError);
                }
                log.error(msgError + " " + e.getMessage()); //$NON-NLS-1$
            } finally {
                String msgError = Messages.getString("FileStep1.filepath") + " \"" + fileXmlText.getText().replace("\\\\", "\\") + "\"\n"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException e) {
                    msgError = msgError + Messages.getString("FileStep1.fileLocked"); //$NON-NLS-1$
                }
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
            if (getConnection().getXmlFilePath() != null
                    && !getConnection().getXmlFilePath().equals("") //$NON-NLS-1$
                    && getConnection().getSchema() != null && !getConnection().getSchema().isEmpty()
                    && ((XmlXPathLoopDescriptor) getConnection().getSchema().get(0)).getAbsoluteXPathQuery() != null
                    && ((XmlXPathLoopDescriptor) getConnection().getSchema().get(0)).getSchemaTargets() != null
                    && !((XmlXPathLoopDescriptor) getConnection().getSchema().get(0)).getSchemaTargets().isEmpty()) {
                // refreshPreview();
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
