// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.mapper.ui.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Slider;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.MetadataColumn;
import org.talend.core.prefs.ITalendCorePrefConstants;
import org.talend.core.ui.CoreUIPlugin;
import org.talend.designer.abstractmap.model.table.IDataMapTable;
import org.talend.designer.abstractmap.model.tableentry.IColumnEntry;
import org.talend.designer.mapper.language.ILanguage;
import org.talend.designer.mapper.language.LanguageProvider;
import org.talend.designer.mapper.managers.MapperManager;
import org.talend.designer.mapper.model.MapperModel;
import org.talend.designer.mapper.model.table.AbstractDataMapTable;
import org.talend.designer.mapper.model.table.InputTable;
import org.talend.designer.mapper.model.table.OutputTable;
import org.talend.designer.mapper.model.tableentry.InputColumnTableEntry;
import org.talend.designer.mapper.model.tableentry.OutputColumnTableEntry;
import org.talend.designer.mapper.ui.visualmap.zone.scrollable.InputTablesZoneView;
import org.talend.designer.mapper.ui.visualmap.zone.scrollable.OutputTablesZoneView;
import org.talend.utils.string.Jaccard;
import org.talend.utils.string.Levenshtein;

/**
 * DOC xwen  class global comment. Detailled comment
 */
public class AutoMappingDialog extends Dialog {

    private IPreferenceStore weightStore;

    private MapperManager mapperManager;

    private MapperModel mapperModel;

    private Slider levenshteinSlider;

    private Slider jaccardSlider;

    private int levenshteinWeight = 0;

    private Integer jaccardWeight = 0;

    private Label levenshteinWeightLabel;

    private Label jaccardWeightLabel;

    private Group autoMapGroup;

    private Button previewButton;

    private Button resetButton;

    private SashForm datasFlowViewSashForm;

    private Composite inputsZone;

    private Composite outputZone;

    private Composite container;

    private SashForm mainSash;


    private ScrolledComposite sc2;

    private InputTablesZoneView inputTablesZoneView;

    private OutputTablesZoneView outputTablesZoneView;

    private List<InputTable> inputTables;
    
    private List<List<IColumnEntry>> cacheInputTables = new ArrayList<List<IColumnEntry>>();

    private List<List<IColumnEntry>> cacheOutputTables = new ArrayList<List<IColumnEntry>>();

    private List<List<IColumnEntry>> resetOutTables = new ArrayList<List<IColumnEntry>>();

    private List<List<IColumnEntry>> resetInTables = new ArrayList<List<IColumnEntry>>();

    private List<IColumnEntry> currentInputList = new ArrayList<IColumnEntry>();

    private List<OutputTable> outputTables;

    private List<IColumnEntry> currentOutputList = new ArrayList<IColumnEntry>();

    private static final boolean SHOW_BORDERS = false;

    private TableViewer tableViewerOut;

    private TableViewer tableViewerIn;

    private Combo inCombo;

    private Combo outCombo;

    /**
     * DOC xwen AutoMappingDialog constructor comment.
     * 
     * @param parentShell
     */
    public AutoMappingDialog(Shell parentShell, MapperManager mapperManager) {
        super(parentShell);

        this.mapperManager = mapperManager;
    }

    @Override
    protected int getShellStyle() {

        return super.getShellStyle() | SWT.RESIZE | SWT.MAX;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Control createDialogArea(Composite parent) {
        container = new Composite(parent, SWT.RESIZE);
        // container = (Composite) super.createDialogArea(parent);
        final GridLayout gridLayout = new GridLayout(1, false);
        gridLayout.marginLeft = 10;
        gridLayout.marginTop = 10;
        gridLayout.marginHeight = 10;
        container.setLayout(gridLayout);

        autoMapGroup = new Group(container, SWT.NONE);
        autoMapGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        autoMapGroup.setText("Auto Map");

        GridLayout AutogridLayout = new GridLayout(3, false);
        AutogridLayout.horizontalSpacing = 10;
        AutogridLayout.marginRight = 100;

        autoMapGroup.setLayout(AutogridLayout);
        Label levenshteinLabel = new Label(autoMapGroup, SWT.NONE);
        levenshteinLabel.setText("Levenshtein");
        levenshteinSlider = new Slider(autoMapGroup, SWT.HORIZONTAL);
        levenshteinSlider.setSize(200, 25);
        levenshteinSlider.setMaximum(101);
        levenshteinSlider.setMinimum(0);
        levenshteinSlider.setThumb(1);
        levenshteinWeightLabel = new Label(autoMapGroup, SWT.NONE);

        Label jaccardLabel = new Label(autoMapGroup, SWT.NONE);
        jaccardLabel.setText("Jaccard");
        jaccardSlider = new Slider(autoMapGroup, SWT.HORIZONTAL);
        jaccardSlider.setSize(200, 25);
        jaccardSlider.setMaximum(101);
        jaccardSlider.setMinimum(0);
        jaccardSlider.setThumb(1);
        jaccardWeightLabel = new Label(autoMapGroup, SWT.NONE);

        Composite buttonComposite = new Composite(container, SWT.NONE);
        FormLayout layout = new FormLayout();
        FormData data = new FormData();
        data.left = new FormAttachment(90, 20);

        previewButton = new Button(buttonComposite, SWT.NONE);
        previewButton.setText("Preview");
        resetButton = new Button(buttonComposite, SWT.NONE);
        resetButton.setText("  Reset  ");
        resetButton.setLayoutData(data);

        buttonComposite.setLayout(layout);

        mainSash = new SashForm(container, SWT.SMOOTH | SWT.HORIZONTAL | SWT.BORDER);


        init();
        initTables();
        addListener();

        return container;
    }


    /**
     * DOC xwen Comment method "initTables".
     */
    private void initTables() {
        createInputZone();
        createOutputZone();
    }

    /**
     * DOC xwen Comment method "createOutputZone".
     */
    private void createOutputZone() {
        outputZone = new Composite(mainSash, SWT.NONE);
        GridLayout grid = new GridLayout(1, true);
        outputZone.setLayout(grid);

        Composite comboComp = new Composite(outputZone, SWT.NONE);
        GridLayout layout = new GridLayout(2, true);
        Label outLabel = new Label(comboComp, SWT.NONE);
        outLabel.setText("Output Tables");
        comboComp.setLayout(layout);

        outCombo = new Combo(comboComp, SWT.READ_ONLY);

        sc2 = new ScrolledComposite(outputZone, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        GridData sc2GridData = new GridData(GridData.FILL_BOTH);
        sc2.setLayoutData(sc2GridData);
        sc2.setBackgroundMode(SWT.INHERIT_DEFAULT);
        sc2.setLayout(new GridLayout(1, false));

        outputTablesZoneView = new OutputTablesZoneView(sc2, getBorder(), mapperManager);
        outputTablesZoneView.setBackgroundMode(SWT.INHERIT_DEFAULT);
        sc2.setContent(outputTablesZoneView);
        GridLayout grid1 = new GridLayout();
        outputZone.setLayout(grid1);
        outputTablesZoneView.setLayout(grid1);
        sc2.setExpandHorizontal(true);
        outputTablesZoneView.setSize(200, 200);
        sc2.layout();

        tableViewerOut = new TableViewer(outputTablesZoneView, SWT.BORDER | SWT.FULL_SELECTION);
        Table table = tableViewerOut.getTable();
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        TableColumn col1 = new TableColumn(table, SWT.NONE);
        col1.setText("Expression");
        col1.setWidth(150);
        TableColumn col2 = new TableColumn(table, SWT.NONE);
        col2.setText("Column");
        col2.setWidth(150);

        TableViewerContentProvider provider = new TableViewerContentProvider();
        OutTableViewerLabelProvider labelProvider = new OutTableViewerLabelProvider();

        tableViewerOut.setContentProvider(provider);
        tableViewerOut.setLabelProvider(labelProvider);

        // Object data = null;
        OutputTable firstTable = null;
        if (!outputTables.isEmpty()) {
            firstTable = outputTables.get(0);
        }
        if (firstTable != null) {
            List<IColumnEntry> outputColumnEntries = firstTable.getColumnEntries();
            // data = outputColumnEntries;

            for (IColumnEntry outputEntry : outputColumnEntries) {
                IMetadataColumn metadataColumn = new MetadataColumn();
                OutputColumnTableEntry entry = new OutputColumnTableEntry(metadataColumn);
                entry.setName(outputEntry.getName());
                entry.setExpression(outputEntry.getExpression());
                entry.setParentName(outputEntry.getParent().getName());
                currentOutputList.add(entry);
            }
        }
        tableViewerOut.setUseHashlookup(true);

        // cache all output tables
        initOutputTables();
        outCombo.select(0);
        tableViewerOut.setInput(cacheOutputTables.get(0));
        outputTablesZoneView.setSize(outputTablesZoneView.computeSize(SWT.DEFAULT, SWT.DEFAULT));

    }

    /**
     * DOC xwen Comment method "createInputZone".
     */
    private void createInputZone() {
        inputsZone = new Composite(mainSash, SWT.NONE);
        GridLayout grid = new GridLayout(1, true);
        inputsZone.setLayout(grid);

        Composite comboComp = new Composite(inputsZone, SWT.NONE);
        GridLayout layout = new GridLayout(2, true);
        Label inLabel = new Label(comboComp, SWT.NONE);
        inLabel.setText("Intput Tables");
        comboComp.setLayout(layout);

        inCombo = new Combo(comboComp, SWT.READ_ONLY);

        ScrolledComposite sc1 = new ScrolledComposite(inputsZone, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        GridData sc1GridData = new GridData(GridData.FILL_BOTH);
        sc1.setLayoutData(sc1GridData);
        // sc1.setLayout(new GridLayout(1, false));
        sc1.setBackgroundMode(SWT.INHERIT_DEFAULT);

        inputTablesZoneView = new InputTablesZoneView(sc1, getBorder(), mapperManager);
        inputTablesZoneView.setBackgroundMode(SWT.INHERIT_DEFAULT);
        inputTablesZoneView.setSize(100, 100);

        sc1.setExpandHorizontal(true);
        sc1.setExpandVertical(true);
        sc1.setMinWidth(100);
        sc1.setMinHeight(100);
        sc1.setBounds(50, 50, 300, 300);
        sc1.setContent(inputTablesZoneView);
        // sc1.setSize(100, 100);

        tableViewerIn = new TableViewer(inputTablesZoneView, SWT.BORDER | SWT.FULL_SELECTION);
        Table table = tableViewerIn.getTable();
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        
        TableColumn col = new TableColumn(table, SWT.NONE);
        col.setText("Column");
        col.setWidth(150);

        TableViewerContentProvider provider = new TableViewerContentProvider();
        InTableViewerLabelProvider labelProvider = new InTableViewerLabelProvider();

        tableViewerIn.setContentProvider(provider);
        tableViewerIn.setLabelProvider(labelProvider);


        tableViewerIn.setUseHashlookup(true);
        
        //cache all input tables
        initInputTables();
        inCombo.select(0);

        tableViewerIn.setInput(cacheInputTables.get(0));

        inputTablesZoneView.setSize(inputTablesZoneView.computeSize(SWT.DEFAULT, SWT.DEFAULT));
    }

    private void initInputTables() {
        if (cacheInputTables.isEmpty()) {
        for (InputTable inputTable : inputTables) {

            List<IColumnEntry> inputColumnEntries = inputTable.getColumnEntries();
            List<IColumnEntry> cacheInputTable = new ArrayList<IColumnEntry>();

            for (IColumnEntry inputEntry : inputColumnEntries) {
                IMetadataColumn metadataColumn = new MetadataColumn();
                InputColumnTableEntry entry = new InputColumnTableEntry(metadataColumn);
                entry.setName(inputEntry.getName());
                entry.setParentName(inputEntry.getParent().getName());
                    cacheInputTable.add(entry);// put entry into table
            }

            cacheInputTables.add(cacheInputTable);
            inCombo.add(inputTable.getName());
        }
        } else {
            cacheInputTables = new ArrayList<List<IColumnEntry>>();
            for (InputTable inputTable : inputTables) {

                List<IColumnEntry> inputColumnEntries = inputTable.getColumnEntries();
                List<IColumnEntry> cacheInputTable = new ArrayList<IColumnEntry>();

                for (IColumnEntry inputEntry : inputColumnEntries) {
                    IMetadataColumn metadataColumn = new MetadataColumn();
                    InputColumnTableEntry entry = new InputColumnTableEntry(metadataColumn);
                    entry.setName(inputEntry.getName());
                    entry.setParentName(inputEntry.getParent().getName());
                    cacheInputTable.add(entry);// put entry into table
                }

                cacheInputTables.add(cacheInputTable);

            }
        }
    }

    private void initOutputTables() {
        if (cacheOutputTables.isEmpty()) {
            for (OutputTable outputTable : outputTables) {

                List<IColumnEntry> outputColumnEntries = outputTable.getColumnEntries();
                List<IColumnEntry> cacheOutputTable = new ArrayList<IColumnEntry>();

                for (IColumnEntry outputEntry : outputColumnEntries) {
                    IMetadataColumn metadataColumn = new MetadataColumn();
                    OutputColumnTableEntry entry = new OutputColumnTableEntry(metadataColumn);
                    entry.setName(outputEntry.getName());
                    entry.setExpression(outputEntry.getExpression());
                    entry.setParentName(outputEntry.getParent().getName());
                    cacheOutputTable.add(entry);// put entry into table
                }
                cacheOutputTables.add(cacheOutputTable);
                outCombo.add(outputTable.getName());
            }
        } else {
            cacheOutputTables = new ArrayList<List<IColumnEntry>>();
            for (OutputTable outputTable : outputTables) {

                List<IColumnEntry> outputColumnEntries = outputTable.getColumnEntries();
                List<IColumnEntry> cacheOutputTable = new ArrayList<IColumnEntry>();

                for (IColumnEntry outputEntry : outputColumnEntries) {
                    IMetadataColumn metadataColumn = new MetadataColumn();
                    OutputColumnTableEntry entry = new OutputColumnTableEntry(metadataColumn);
                    entry.setName(outputEntry.getName());
                    entry.setExpression(outputEntry.getExpression());
                    entry.setParentName(outputEntry.getParent().getName());
                    cacheOutputTable.add(entry);// put entry into table
                }
                cacheOutputTables.add(cacheOutputTable);
            }
        }
    }

    /**
     * DOC xwen Comment method "getMinimizedButtonState".
     * 
     * @param tables
     * @return
     */
    private Boolean getMinimizedButtonState(List<? extends AbstractDataMapTable> tables) {
        boolean allTablesAreMinimized = true;
        boolean allTablesAreNotMinimized = true;

        for (IDataMapTable table : tables) {
            if (table.isMinimized()) {
                allTablesAreNotMinimized = false;
            } else {
                allTablesAreMinimized = false;
            }
        }

        if (allTablesAreMinimized) {
            return new Boolean(true);
        } else if (allTablesAreNotMinimized) {
            return new Boolean(false);
        }

        return new Boolean(false);
    }

    /**
     * DOC xwen Comment method "init".
     */
    private void init() {
        weightStore = CoreUIPlugin.getDefault().getPreferenceStore();
        levenshteinWeightLabel.setText(String.valueOf(weightStore.getInt(ITalendCorePrefConstants.LEVENSHTEIN_WEIGHT)));
        levenshteinSlider.setSelection(weightStore.getInt(ITalendCorePrefConstants.LEVENSHTEIN_WEIGHT));
        jaccardWeightLabel.setText(String.valueOf(weightStore.getInt(ITalendCorePrefConstants.JACCARD_WEIGHT)));
        jaccardSlider.setSelection(weightStore.getInt(ITalendCorePrefConstants.JACCARD_WEIGHT));

        this.mapperModel = new MapperModel(mapperManager.getInputTables(), mapperManager.getOutputTables(),
                mapperManager.getVarsTables());

        inputTables = mapperManager.getInputTables();
        outputTables = mapperManager.getOutputTables();


    }

    private void addListener() {
        levenshteinSlider.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                updateAutoMap();
            }
        });

        jaccardSlider.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                updateAutoMap();
            }
        });

        previewButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {

                // IPreferenceStore weightStore = CoreUIPlugin.getDefault().getPreferenceStore();
                int levenshteinWeight = levenshteinSlider.getSelection();
                int jaccardWeight = jaccardSlider.getSelection();
                map(levenshteinWeight, jaccardWeight);
            }
        });

        resetButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                initInputTables();
                initOutputTables();

                tableViewerIn.setInput(cacheInputTables.get(inCombo.getSelectionIndex()));
                tableViewerOut.setInput(cacheOutputTables.get(outCombo.getSelectionIndex()));
            }
        });

        inCombo.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                tableViewerIn.setInput(cacheInputTables.get(inCombo.getSelectionIndex()));
            };
        });

        outCombo.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                tableViewerOut.setInput(cacheOutputTables.get(outCombo.getSelectionIndex()));
            }
        });
    }

    protected void map(int levenshteinWeight, int jaccardWeight) {
        int paramL = levenshteinWeight;
        int paramJ = jaccardWeight;

        ILanguage currentLanguage = LanguageProvider.getCurrentLanguage();

        for (List<IColumnEntry> OutputList : cacheOutputTables) {
            for (IColumnEntry outputEntry : OutputList) {

                if (outputEntry.getExpression() == null || outputEntry.getExpression().trim().length() == 0) {
                    String outputColumnName = outputEntry.getName().toLowerCase();
                    String jaccardOutput = Jaccard.tokenize(outputEntry.getName());
                    HashMap<IColumnEntry, Double> finalMap = new HashMap<IColumnEntry, Double>();

                    for (List<IColumnEntry> InputList : cacheInputTables) {
                        for (IColumnEntry inputEntry : InputList) {
                            String inputStr = inputEntry.getName().toLowerCase();
                            double LevenshteinScore = Levenshtein.getLevenshteinScore(inputStr, outputColumnName);
                            // Jaccard
                            String jaccardIutput = Jaccard.tokenize(inputEntry.getName());
                            double JaccardScore = Jaccard.JaccardCompare(jaccardIutput, jaccardOutput);
                            double finalScore = LevenshteinScore * paramL + JaccardScore * paramJ;

                            finalMap.put(inputEntry, finalScore);
                        }
                    }
                    IColumnEntry bestEntry = getMaxStr(finalMap);
                    if (bestEntry == null) {
                        continue;
                    }
                    if (finalMap.get(bestEntry) < 30) {
                        continue;
                    } else {
                        outputEntry.setExpression(currentLanguage.getLocation(bestEntry.getTableName(), bestEntry.getName()));
                    }
                }
            }
        }
        int index = outCombo.getSelectionIndex();
        tableViewerOut.setInput(cacheOutputTables.get(index));
    }

    public static IColumnEntry getMaxStr(HashMap<IColumnEntry, Double> map) {
        Double max = 0.0;
        IColumnEntry result = null;

        for (Entry<IColumnEntry, Double> entry : map.entrySet()) {
            if (entry.getValue() > max) {
                result = entry.getKey();
                if (result != null)
                    max = entry.getValue();
            }

        }
        return result;
    }

    private void updateAutoMap() {
        levenshteinWeight = levenshteinSlider.getSelection();
        jaccardWeight = jaccardSlider.getSelection();
        levenshteinWeightLabel.setText(String.valueOf(levenshteinWeight));
        jaccardWeightLabel.setText(String.valueOf(jaccardWeight));
        autoMapGroup.layout();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#okPressed()
     */
    @Override
    protected void okPressed() {
        IPreferenceStore weightStore = CoreUIPlugin.getDefault().getPreferenceStore();
        levenshteinWeight = levenshteinSlider.getSelection();
        jaccardWeight = jaccardSlider.getSelection();
        weightStore.setValue(ITalendCorePrefConstants.LEVENSHTEIN_WEIGHT, levenshteinWeight);
        weightStore.setValue(ITalendCorePrefConstants.JACCARD_WEIGHT, jaccardWeight);

        // List<DataMapTableView> dataMapTableViews = mapperManager.getUiManager().getInputsTablesView();
        // for (DataMapTableView dataMapTableView : dataMapTableViews) {
        // dataMapTableView.setParent(oldParent);
        // }

        super.okPressed();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#cancelPressed()
     */
    @Override
    protected void cancelPressed() {
        // List<DataMapTableView> dataMapTableViews = mapperManager.getUiManager().getInputsTablesView();
        // for (DataMapTableView dataMapTableView : dataMapTableViews) {
        // dataMapTableView.setParent(oldParent);
        // }
        super.cancelPressed();
    }


    private int getBorder() {
        return SHOW_BORDERS ? SWT.BORDER : SWT.NONE;
    }

}
