// ============================================================================
//
// Copyright (C) 2006-2008 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.filemultischemas.ui;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.internal.IWorkbenchGraphicConstants;
import org.eclipse.ui.internal.WorkbenchImages;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.ui.swt.formtools.Form;
import org.talend.commons.ui.swt.formtools.LabelledFileField;
import org.talend.commons.ui.swt.formtools.LabelledText;
import org.talend.commons.ui.utils.PathUtils;
import org.talend.core.CorePlugin;
import org.talend.core.model.utils.TalendTextUtils;
import org.talend.core.prefs.ITalendCorePrefConstants;
import org.talend.core.utils.CsvArray;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.filemultischemas.MultiSchemasComponent;
import org.talend.designer.filemultischemas.data.ExternalMultiSchemasUIProperties;
import org.talend.designer.filemultischemas.data.SchemasKeyData;
import org.talend.designer.filemultischemas.managers.MultiSchemasManager;
import org.talend.designer.filemultischemas.managers.UIManager;
import org.talend.designer.filemultischemas.ui.dialog.MultiSchemaEventListener;
import org.talend.designer.filemultischemas.ui.dnd.SchemasTreeDnD;
import org.talend.designer.filemultischemas.ui.preview.MultiSchemasShadowProcessPreview;
import org.talend.designer.filemultischemas.ui.preview.MultiSchemasUIThreadProcessor;
import org.talend.designer.filemultischemas.ui.provider.SchemasTreeContentProvider;
import org.talend.designer.filemultischemas.ui.provider.SchemasTreeLabelProvider;
import org.talend.repository.i18n.Messages;
import org.talend.repository.preview.ProcessDescription;
import org.talend.repository.ui.swt.preview.ShadowProcessPreview;

/**
 * cLi class global comment. Detailled comment
 */
public class MultiSchemasUI {

    protected int maximumRowsToPreview = CorePlugin.getDefault().getPreferenceStore().getInt(
            ITalendCorePrefConstants.PREVIEW_LIMIT);

    private MultiSchemasManager multiSchemaManager;

    /**
     * for schema details viewer.
     */

    private SchameDetailsPropertyAction propertyAction;

    private SchameDetailsColumnAction columnAction;

    /**
     * 
     */
    private Composite uiParent;

    private LabelledFileField fileField;

    private LabelledText rowSeparatorText;

    private LabelledText fieldSeparatorText;

    private Button previewBtn;

    private Button fetchBtn;

    private TreeViewer schemaTreeViewer;

    private TreeViewer schemaDetailsViewer;

    private Label previewInformationLabel;

    private CTabFolder tabFolder;

    private CTabItem previewTabItem;

    private CTabItem outputTabItem;

    private Composite outputComposite;

    private SashForm allContentForm;

    private SashForm headerSashForm;

    /**
     * 
     */
    private MultiSchemasShadowProcessPreview multiSchemasFilePreview;

    private MultiSchemasUIThreadProcessor processor;

    private MultiSchemaEventListener listener;

    public MultiSchemasUI(Composite uiParent, MultiSchemasManager multiSchemaManager) {
        super();
        this.uiParent = uiParent;
        this.multiSchemaManager = multiSchemaManager;
        this.processor = new MultiSchemasUIThreadProcessor(this);
    }

    /**
     * Sets the listener.
     * 
     * @param listener the listener to set
     */
    public void addListener(MultiSchemaEventListener listener) {
        this.listener = listener;
    }

    private Shell getShell() {
        return this.uiParent.getShell();
    }

    public void init() {
        uiParent.setLayout(new GridLayout());

        Composite composite = new Composite(uiParent, SWT.NONE);
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));
        composite.setLayout(new FormLayout());

        Composite fileGroup = new Composite(composite, SWT.NONE);
        FormData formData = new FormData();
        formData.top = new FormAttachment(0, 5);
        formData.left = new FormAttachment(0, 5);
        formData.right = new FormAttachment(100, -10);
        fileGroup.setLayoutData(formData);
        createFileGroup(fileGroup);

        allContentForm = new SashForm(composite, SWT.NONE);
        formData = new FormData();
        formData.top = new FormAttachment(fileGroup, 10);
        formData.left = new FormAttachment(0, 5);
        formData.right = new FormAttachment(100, -5);
        formData.bottom = new FormAttachment(100, -5);
        allContentForm.setLayoutData(formData);
        createViewers(allContentForm);

        initFieldValues();
        // listener
        addFieldListeners();
        addButtonListeners();

        // preview
        refreshPreview();
        checkDialog();
        // reload
        reloadSchemaDataFromNode();
    }

    private void reloadSchemaDataFromNode() {
        SchemasKeyData rootData = this.getMultiSchemaManager().retrievePropertiesFromNode();
        schemaTreeViewer.setInput(rootData);
        schemaTreeViewer.refresh();
    }

    protected MultiSchemasComponent getMultiSchemasComponent() {
        return getMultiSchemaManager().getMultiSchemasComponent();
    }

    protected MultiSchemasManager getMultiSchemaManager() {
        return this.multiSchemaManager;
    }

    private void initFieldValues() {
        fileField.setText(getMultiSchemaManager().getParameterValue(EParameterName.FILENAME));
        rowSeparatorText.setText(getMultiSchemaManager().getParameterValue(EParameterName.ROWSEPARATOR));
        fieldSeparatorText.setText(getMultiSchemaManager().getParameterValue(EParameterName.FIELDSEPARATOR));
    }

    private void createFileGroup(Composite fileGroup) {
        fileGroup.setLayout(new GridLayout(3, false));

        fileField = new LabelledFileField(fileGroup, ExternalMultiSchemasUIProperties.FILE_LABEL,
                ExternalMultiSchemasUIProperties.FILE_EXTENSIONS, 1, SWT.BORDER) {

            @Override
            protected void setFileFieldValue(String result) {
                if (result != null) {
                    getTextControl().setText(TalendTextUtils.addQuotes(PathUtils.getPortablePath(result)));
                }
            }

        };
        // fileField.getTextControl().setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));

        GridData layoutData;
        rowSeparatorText = new LabelledText(fileGroup, Messages.getString("FileStep2.rowSeparator"), 2);
        layoutData = new GridData();
        layoutData.horizontalSpan = 2;
        layoutData.widthHint = ExternalMultiSchemasUIProperties.SEPERATOR_TEXT_WIDTH;
        rowSeparatorText.setLayoutData(layoutData);
        rowSeparatorText.setText(ExternalMultiSchemasUIProperties.DEFAULT_ROW_SEPERATOR);

        fieldSeparatorText = new LabelledText(fileGroup, Messages.getString("FileStep2.fieldSeparator"), 2);
        layoutData = new GridData();
        layoutData.horizontalSpan = 2;
        layoutData.widthHint = ExternalMultiSchemasUIProperties.SEPERATOR_TEXT_WIDTH;
        fieldSeparatorText.setLayoutData(layoutData);
        fieldSeparatorText.setText(ExternalMultiSchemasUIProperties.DEFAULT_FIELD_SEPERATOR);
    }

    private void createViewers(SashForm allContentForm) {
        allContentForm.setLayout(new FillLayout());
        allContentForm.setOrientation(SWT.VERTICAL);
        allContentForm.setSashWidth(ExternalMultiSchemasUIProperties.SASHFORM_WIDTH);

        createHeader(allContentForm);
        creatButtom(allContentForm);

        allContentForm.setWeights(ExternalMultiSchemasUIProperties.getAllSashformWeights());

    }

    private void creatButtom(SashForm allContentForm) {
        //
        Composite composite = new Composite(allContentForm, SWT.NONE);
        composite.setLayout(new GridLayout(2, false));

        schemaDetailsViewer = new TreeViewer(composite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
        final Tree tree = schemaDetailsViewer.getTree();
        tree.setHeaderVisible(true);
        tree.setLinesVisible(true);
        tree.setLayoutData(new GridData(GridData.FILL_BOTH));

        final ToolBar menuBar = new ToolBar(composite, SWT.FLAT | SWT.NO_BACKGROUND);
        GridDataFactory.swtDefaults().align(SWT.RIGHT, SWT.TOP).applyTo(menuBar);
        createMenuBar(menuBar);

        getUIManager().changeSchemasDetailView(schemaDetailsViewer, getSchemaDetailModel());
    }

    @SuppressWarnings("restriction")
    private void createMenuBar(final ToolBar menuBar) {
        ToolItem pullDownButton = new ToolItem(menuBar, SWT.PUSH);
        Image hoverImage = WorkbenchImages.getImage(IWorkbenchGraphicConstants.IMG_LCL_RENDERED_VIEW_MENU);
        pullDownButton.setDisabledImage(hoverImage);
        pullDownButton.setImage(hoverImage);
        pullDownButton.setToolTipText("Menu");
        pullDownButton.setWidth(5);

        MenuManager menuManager = new MenuManager("MultiSchema");//$NON-NLS-1$

        columnAction = new SchameDetailsColumnAction(schemaDetailsViewer);
        columnAction.setChecked(!ExternalMultiSchemasUIProperties.isSchemaDetailsModel());
        menuManager.add(columnAction);

        propertyAction = new SchameDetailsPropertyAction(schemaDetailsViewer);
        propertyAction.setChecked(ExternalMultiSchemasUIProperties.isSchemaDetailsModel());
        menuManager.add(propertyAction);

        final Menu aMenu = menuManager.createContextMenu(menuBar.getParent());

        pullDownButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                Point toolbarSize = menuBar.getSize();
                toolbarSize = menuBar.toDisplay(0, toolbarSize.y);
                aMenu.setLocation(toolbarSize);
                aMenu.setVisible(true);
            }
        });
    }

    private void createHeader(SashForm allContentForm) {
        //
        tabFolder = new CTabFolder(allContentForm, SWT.NULL);
        tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));

        previewTabItem = new CTabItem(tabFolder, SWT.NONE);
        previewTabItem.setText(ExternalMultiSchemasUIProperties.PREVIEW_LABEL);
        tabFolder.setSelection(previewTabItem);

        outputTabItem = new CTabItem(tabFolder, SWT.BORDER);
        outputTabItem.setText(ExternalMultiSchemasUIProperties.OUTPUT_LABEL);
        outputComposite = Form.startNewGridLayout(tabFolder, 1);
        outputTabItem.setControl(outputComposite);

        Composite headerComposite = new Composite(tabFolder, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        headerComposite.setLayout(layout);
        previewTabItem.setControl(headerComposite);

        createHeaderStatus(headerComposite);

        createHeaderSashForm(headerComposite);
    }

    private void createHeaderStatus(Composite headerComposite) {
        Composite previewComposite = new Composite(headerComposite, SWT.NONE);
        GridData layoutData = new GridData(GridData.FILL_HORIZONTAL);
        layoutData.verticalIndent = 10;
        previewComposite.setLayoutData(layoutData);
        previewComposite.setLayout(new GridLayout(2, false));

        previewBtn = new Button(previewComposite, SWT.PUSH);
        previewBtn.setText(ExternalMultiSchemasUIProperties.PREVIEW_STRING);
        layoutData = new GridData();
        layoutData.horizontalIndent = 50;
        previewBtn.setLayoutData(layoutData);

        previewInformationLabel = new Label(previewComposite, SWT.NONE);
        layoutData = new GridData(GridData.FILL_HORIZONTAL);
        layoutData.horizontalIndent = 20;
        previewInformationLabel.setLayoutData(layoutData);
        previewInformationLabel.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));
    }

    private void createHeaderSashForm(Composite headerComposite) {
        GridLayout layout;
        headerSashForm = new SashForm(headerComposite, SWT.NONE);
        headerSashForm.setLayout(new FillLayout());
        headerSashForm.setLayoutData(new GridData(GridData.FILL_BOTH));
        headerSashForm.setSashWidth(ExternalMultiSchemasUIProperties.SASHFORM_WIDTH);
        //
        multiSchemasFilePreview = new MultiSchemasShadowProcessPreview(headerSashForm);
        multiSchemasFilePreview.newTablePreview();

        Composite struComp = new Composite(headerSashForm, SWT.NONE);
        layout = new GridLayout();
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        struComp.setLayout(layout);
        headerSashForm.setWeights(ExternalMultiSchemasUIProperties.getHeaderSashformWeights());
        //
        fetchBtn = new Button(struComp, SWT.PUSH);
        fetchBtn.setText(ExternalMultiSchemasUIProperties.FETCH_LABEL);
        fetchBtn.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));

        //
        schemaTreeViewer = new TreeViewer(struComp, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        schemaTreeViewer.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));

        SchemasTreeDnD dnd = new SchemasTreeDnD(schemaTreeViewer);
        dnd.addDragAndDrop();

        schemaTreeViewer.setContentProvider(new SchemasTreeContentProvider());
        schemaTreeViewer.setLabelProvider(new SchemasTreeLabelProvider());
        schemaTreeViewer.setSorter(new ViewerSorter() {

            @SuppressWarnings("unchecked")
            @Override
            public int compare(Viewer viewer, Object e1, Object e2) {
                if (e1 instanceof SchemasKeyData && e2 instanceof SchemasKeyData) {
                    return getComparator().compare(((SchemasKeyData) e1).getKeyName(), ((SchemasKeyData) e2).getKeyName());
                }
                return super.compare(viewer, e1, e2);
            }

        });
        schemaTreeViewer.setColumnProperties(ExternalMultiSchemasUIProperties.SCHEMAS_TREE_COLUMN_PROPERTY);

    }

    private void addFieldListeners() {
        ModifyListener modifyListener = new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                previewBtn.setEnabled(checkFieldsValue());
                clearPreview();
                // clearSchemaTree();
                // clearSchemaDetail();
                // setFetchButtonStatus(false);
            }
        };
        fileField.addModifyListener(modifyListener);
        rowSeparatorText.addModifyListener(modifyListener);
        fieldSeparatorText.addModifyListener(modifyListener);

        schemaTreeViewer.getTree().addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                getUIManager().refreshSchemasDetailView(schemaTreeViewer, schemaDetailsViewer, getSchemaDetailModel());
            }
        });
    }

    protected void addButtonListeners() {

        // Event PreviewButton
        previewBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                refreshPreview();
            }
        });
        fetchBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                fetchCodes();
            }
        });
    }

    @SuppressWarnings("restriction")
    private void fetchCodes() {
        try {
            final ProgressMonitorDialog dialog = new ProgressMonitorDialog(getShell());
            dialog.run(true, false, new IRunnableWithProgress() {

                public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                    monitor.beginTask("Fetch...", IProgressMonitor.UNKNOWN);
                    monitor.setCanceled(false);

                    final CsvArray csvArray = processor.getCsvArray();

                    Display.getDefault().syncExec(new Runnable() {

                        public void run() {
                            SchemasKeyData schemasModel = null;
                            boolean checked = (csvArray != null && csvArray.getRows().size() > 0);
                            final CsvArray uniqueCsvArray = getMultiSchemaManager().retrieveCsvArrayInUniqueModel(
                                    getProcessDescription(), checked);

                            schemasModel = getMultiSchemaManager().createSchemasTree(uniqueCsvArray);
                            schemaTreeViewer.setInput(schemasModel);
                            clearSchemaDetail();
                            checkDialog();
                        }
                    });
                    monitor.done();
                }
            });

        } catch (InvocationTargetException e) {
            ExceptionHandler.process(e);
        } catch (InterruptedException e) {
            ExceptionHandler.process(e);
        }

    }

    /**
     * clear the table preview.
     */
    void clearPreview() {
        multiSchemasFilePreview.removePreviewContent();
        this.processor.clearCsvArray();
    }

    void clearSchemaTree() {
        schemaTreeViewer.setInput(null);
    }

    void clearSchemaDetail() {
        schemaDetailsViewer.setInput(null);
        // dipose other columns, except first.
        // getUIManager().updateColumns(schemaDetailsViewer.getTree(), 0);
    }

    private UIManager getUIManager() {
        return getMultiSchemaManager().getUIManager();
    }

    /**
     * refreshPreview use ShadowProcess to refresh the preview.
     */
    void refreshPreview() {
        Display.getCurrent().asyncExec(new Runnable() {

            public void run() {
                if (checkFieldsValue()) {
                    processor.execute();
                    setFetchButtonStatus(true);
                }
            }
        });
    }

    private String getFile() {
        return this.multiSchemaManager.getOriginalValue(this.fileField.getText());
    }

    private String getRowSeperator() {
        return this.multiSchemaManager.getOriginalValue(this.rowSeparatorText.getText());
    }

    private String getFieldSeperator() {
        return this.multiSchemaManager.getOriginalValue(this.fieldSeparatorText.getText());
    }

    private boolean checkString(String value) {
        if (value != null && !"".equals(value)) {
            return true;
        }
        return false;
    }

    protected boolean checkFieldsValue() {
        if (!checkString(getFile())) {
            previewInformationLabel.setText(Messages.getString("FileStep2.filePathIncomplete")); //$NON-NLS-1$ 
            return false;
        } else {
            String file = TalendTextUtils.removeQuotes(getFile());
            if (!new File(file).exists()) {
                previewInformationLabel.setText("File not found");
                return false;
            }
        }
        if (!checkString(getRowSeperator()) || !checkString(getFieldSeperator())) {
            previewInformationLabel.setText(Messages.getString("FileStep2.settingsIncomplete")); //$NON-NLS-1$ //$NON-NLS-2$
            return false;
        }
        previewInformationLabel.setText(""); //$NON-NLS-1$
        return true;
    }

    public ProcessDescription getProcessDescription() {
        ProcessDescription processDescription = new ProcessDescription();

        processDescription.setFilepath(getFile());
        processDescription.setRowSeparator(getRowSeperator());
        processDescription.setFieldSeparator(getFieldSeperator());
        processDescription.setPattern(processDescription.getFieldSeparator());

        processDescription.setEncoding(TalendTextUtils.addQuotes("UTF-8"));

        processDescription.setHeaderRow(-1);
        processDescription.setFooterRow(0);
        processDescription.setLimitRows(maximumRowsToPreview);

        return processDescription;
    }

    public Label getPreviewInformationLabel() {
        return this.previewInformationLabel;
    }

    public ShadowProcessPreview getMultiSchemasFilePreview() {
        return this.multiSchemasFilePreview;
    }

    public CTabFolder getTabFolder() {
        return this.tabFolder;
    }

    public CTabItem getOutputTabItem() {
        return this.outputTabItem;
    }

    public Composite getOutputComposite() {
        return this.outputComposite;
    }

    /**
     * 
     * cLi Comment method "preCheckProcessStart".
     * 
     * for processor
     */
    public boolean preCheckProcessStart() {
        previewBtn.setText(Messages.getString("FileStep2.stop"));

        clearPreview();
        if (!checkFieldsValue()) {
            return false;
        }

        previewInformationLabel.setText(Messages.getString("FileStep2.previewProgress")); //$NON-NLS-1$ //$NON-NLS-2$
        return true;
    }

    /**
     * 
     * cLi Comment method "updateUIInThreadIfThreadFinally".
     * 
     * for processor
     */
    public void updateUIInThreadIfThreadFinally() {
        if (!previewBtn.isDisposed()) {
            previewBtn.setText(ExternalMultiSchemasUIProperties.PREVIEW_STRING);
            previewBtn.setEnabled(true);

        }
    }

    /**
     * 
     * cLi Comment method "postProcessCancle".
     * 
     * for processor
     */
    public void postProcessCancle() {
        previewBtn.setEnabled(false);
    }

    public void setFetchButtonStatus(final boolean status) {
        fetchBtn.setEnabled(status);
    }

    public void checkDialog() {
        if (this.listener != null) {
            this.listener.checkPerformed(canFinished());
        }
    }

    public boolean canFinished() {
        final Object input = schemaTreeViewer.getInput();
        if (input != null && input instanceof SchemasKeyData) {
            return true;
        }
        return false;
    }

    /**
     * 
     * cLi Comment method "saveProperties".
     * 
     * 
     */
    public void saveProperties() {
        final Object input = schemaTreeViewer.getInput();
        if (input != null) {
            getMultiSchemaManager().savePropertiesToComponent((SchemasKeyData) input, getFile(), getRowSeperator(),
                    getFieldSeperator());
        }
    }

    public void prepareClosing(int dialogResponse) {
        multiSchemaManager.getUIManager().setDialogResponse(dialogResponse);
        if (dialogResponse == SWT.OK) {
            ExternalMultiSchemasUIProperties.setShellMaximized(getShell().getMaximized());
            ExternalMultiSchemasUIProperties.setBoundsMapper(getShell().getBounds());
            ExternalMultiSchemasUIProperties.setAllSashformWeights(this.allContentForm.getWeights());
            ExternalMultiSchemasUIProperties.setHeaderSashformWeights(this.headerSashForm.getWeights());
            ExternalMultiSchemasUIProperties.setSchemaDetailsModel(getSchemaDetailModel());
        }

    }

    private boolean getSchemaDetailModel() {
        if (propertyAction != null) {
            return propertyAction.isChecked();
        }
        return ExternalMultiSchemasUIProperties.isSchemaDetailsModel(); // default
    }

    class SchameDetailsPropertyAction extends Action {

        private final TreeViewer schemaDetailsViewer;

        public SchameDetailsPropertyAction(final TreeViewer schemaDetailsViewer) {
            super("Property model", IAction.AS_RADIO_BUTTON);
            this.schemaDetailsViewer = schemaDetailsViewer;
        }

        @Override
        public void run() {
            if (isChecked()) {
                changeSchemaDetailsView();
            }
        }

    }

    class SchameDetailsColumnAction extends Action {

        private final TreeViewer schemaDetailsViewer;

        public SchameDetailsColumnAction(final TreeViewer schemaDetailsViewer) {
            super("Column model", IAction.AS_RADIO_BUTTON);
            this.schemaDetailsViewer = schemaDetailsViewer;
        }

        @Override
        public void run() {
            if (isChecked()) {
                changeSchemaDetailsView();
            }
        }
    }

    private void changeSchemaDetailsView() {
        final Tree tree = schemaDetailsViewer.getTree();
        for (TreeItem item : tree.getItems()) {
            item.dispose();
        }
        schemaDetailsViewer.setInput(null);

        getUIManager().changeSchemasDetailView(schemaDetailsViewer, getSchemaDetailModel());
        getUIManager().refreshSchemasDetailView(schemaTreeViewer, schemaDetailsViewer, getSchemaDetailModel());
    }
}
