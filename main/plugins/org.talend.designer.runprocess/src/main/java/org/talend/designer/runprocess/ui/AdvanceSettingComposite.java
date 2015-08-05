// ============================================================================
//
// Copyright (C) 2006-2015 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.runprocess.ui;

import java.util.Map;

import org.apache.commons.collections.BidiMap;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.components.ComponentCategory;
import org.talend.core.model.process.EComponentCategory;
import org.talend.core.model.process.Element;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.properties.tab.IDynamicProperty;
import org.talend.designer.core.IMultiPageTalendEditor;
import org.talend.designer.runprocess.RunProcessContext;
import org.talend.designer.runprocess.RunProcessPlugin;
import org.talend.designer.runprocess.i18n.Messages;
import org.talend.designer.runprocess.prefs.RunProcessPrefsConstants;

/**
 * DOC Administrator class global comment. Detailled comment
 */
public class AdvanceSettingComposite extends ScrolledComposite implements IDynamicProperty {

    private Button perfBtn;

    private Button saveJobBeforeRunButton;

    private Button watchBtn;

    private Button clearBeforeExec;

    private RunProcessContext processContext;

    private Composite argumentsComposite;

    private JobVMArgumentsComposite argumentsViewer;

    private ProcessManager processManager;

    /**
     * DOC Administrator AdvanceSettingComposite constructor comment.
     * 
     * @param parent
     * @param style
     */
    public AdvanceSettingComposite(Composite parent, int style) {
        super(parent, style);
        // TODO Auto-generated constructor stub
        setExpandHorizontal(true);
        setExpandVertical(true);
        this.setLayout(new FormLayout());
        processManager = ProcessManager.getInstance();
        FormData layoutData = new FormData();
        layoutData.left = new FormAttachment(0, 0);
        layoutData.right = new FormAttachment(100, 0);
        layoutData.top = new FormAttachment(0, 0);
        layoutData.bottom = new FormAttachment(100, 0);
        setLayoutData(layoutData);
        setLayout(new FormLayout());
        final Composite panel = new Composite(this, SWT.NONE);
        setContent(panel);
        // panel.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_DARK_RED));
        FormLayout layout = new FormLayout();
        layout.marginWidth = 5 + 2;
        layout.marginHeight = 4;
        layout.spacing = 6 + 1;
        panel.setLayout(layout);

        perfBtn = new Button(panel, SWT.CHECK);
        FormData layouData = new FormData();
        layouData.left = new FormAttachment(0, 10);
        layouData.right = new FormAttachment(0, 100);
        layouData.top = new FormAttachment(0, 10);
        layouData.bottom = new FormAttachment(0, 30);
        perfBtn.setLayoutData(layouData);
        if (ProcessManager.getProcessContext() != null && ProcessManager.getProcessContext().getProcess() != null) {
            perfBtn.setVisible(!ProcessManager.getProcessContext().getProcess().getComponentsType()
                    .equals(ComponentCategory.CATEGORY_4_MAPREDUCE.getName()));
        }
        perfBtn.setText(Messages.getString("ProcessComposite.stat"));
        perfBtn.setToolTipText(Messages.getString("ProcessComposite.statHint")); //$NON-NLS-1$

        perfBtn.setEnabled(false);
        saveJobBeforeRunButton = new Button(panel, SWT.CHECK);
        FormData layouDatac = new FormData();
        if (perfBtn.isVisible()) {
            layouDatac.left = new FormAttachment(perfBtn, 0, SWT.RIGHT);
        } else {
            layouDatac.left = new FormAttachment(0, 10);
        }
        layouDatac.right = new FormAttachment(0, 300);
        layouDatac.top = new FormAttachment(0, 10);
        layouDatac.bottom = new FormAttachment(0, 30);
        saveJobBeforeRunButton.setLayoutData(layouDatac);
        saveJobBeforeRunButton.setText(Messages.getString("ProcessComposite.saveBeforeRun"));
        saveJobBeforeRunButton.setToolTipText(Messages.getString("ProcessComposite.saveBeforeRunHint")); //$NON-NLS-1$
        saveJobBeforeRunButton.setEnabled(false);

        watchBtn = new Button(panel, SWT.CHECK);
        FormData layouDatad = new FormData();
        layouDatad.left = new FormAttachment(0, 10);
        layouDatad.right = new FormAttachment(0, 100);
        layouDatad.top = new FormAttachment(0, 30);
        layouDatad.bottom = new FormAttachment(0, 50);
        watchBtn.setLayoutData(layouDatad);
        watchBtn.setText(Messages.getString("ProcessComposite.execTime"));
        watchBtn.setToolTipText(Messages.getString("ProcessComposite.execTimeHint"));

        watchBtn.setEnabled(false);
        clearBeforeExec = new Button(panel, SWT.CHECK);
        clearBeforeExec.setText(Messages.getString("ProcessComposite.clearBefore"));
        clearBeforeExec.setToolTipText(Messages.getString("ProcessComposite.clearBeforeHint"));

        FormData layouDatacb = new FormData();
        layouDatacb.left = new FormAttachment(watchBtn, 0, SWT.RIGHT);
        layouDatacb.right = new FormAttachment(0, 300);
        layouDatacb.top = new FormAttachment(0, 30);
        layouDatacb.bottom = new FormAttachment(0, 50);
        clearBeforeExec.setLayoutData(layouDatacb);
        clearBeforeExec.setEnabled(false);
        Group execGroup = new Group(panel, SWT.NONE);
        execGroup.setText("JVM Setting"); //$NON-NLS-1$
        GridLayout layoutg = new GridLayout();
        layoutg.marginHeight = 0;
        layoutg.marginWidth = 0;
        execGroup.setLayout(layoutg);
        FormData layouDatag = new FormData();
        layouDatag.left = new FormAttachment(0, 10);
        layouDatag.right = new FormAttachment(100, 0);
        layouDatag.top = new FormAttachment(0, 55);
        layouDatag.bottom = new FormAttachment(100, 0);
        execGroup.setLayoutData(layouDatag);

        ScrolledComposite execScroll = new ScrolledComposite(execGroup, SWT.V_SCROLL | SWT.H_SCROLL);
        execScroll.setExpandHorizontal(true);
        execScroll.setExpandVertical(true);
        execScroll.setLayoutData(new GridData(GridData.FILL_BOTH));

        if (LanguageManager.getCurrentLanguage() == ECodeLanguage.JAVA) {
            argumentsComposite = new Composite(execScroll, SWT.NONE);
            execScroll.setContent(argumentsComposite);
            execScroll.setLayout(new GridLayout());
            argumentsComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
            GridLayout gridLayoutArguments = new GridLayout(1, false);
            argumentsComposite.setLayout(gridLayoutArguments);
            argumentsViewer = new JobVMArgumentsComposite("vmarguments",
                    Messages.getString("RunProcessPreferencePage.vmArgument"), //$NON-NLS-1$
                    argumentsComposite);
            execScroll.setMinSize(execGroup.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        }
        addListeners();

        clearBeforeExec.setSelection(RunProcessPlugin.getDefault().getPreferenceStore()
                .getBoolean(RunProcessPrefsConstants.ISCLEARBEFORERUN));
        watchBtn.setSelection(RunProcessPlugin.getDefault().getPreferenceStore()
                .getBoolean(RunProcessPrefsConstants.ISEXECTIMERUN));
        perfBtn.setSelection(RunProcessPlugin.getDefault().getPreferenceStore()
                .getBoolean(RunProcessPrefsConstants.ISSTATISTICSRUN));
    }

    /**
     * DOC Administrator Comment method "addListeners".
     */
    private void addListeners() {
        // TODO Auto-generated method stub
        watchBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                processContext.setWatchAllowed(watchBtn.getSelection());
                processManager.setExecTime(watchBtn.getSelection());
            }

        });
        saveJobBeforeRunButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                processContext.setSaveBeforeRun(saveJobBeforeRunButton.getSelection());
                processManager.setSaveJobBeforeRun(saveJobBeforeRunButton.getSelection());
            }
        });
        perfBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                processContext.setMonitorPerf(perfBtn.getSelection());
                processManager.setStat(perfBtn.getSelection());
            }
        });
        clearBeforeExec.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                processContext.setClearBeforeExec(clearBeforeExec.getSelection());
                processManager.setClearBeforeExec(clearBeforeExec.getSelection());
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.properties.tab.IDynamicProperty#getComposite()
     */
    @Override
    public Composite getComposite() {
        // TODO Auto-generated method stub
        return null;
    }

    public void setProcessContext(RunProcessContext processContext) {
        if (argumentsViewer != null) {
            argumentsViewer.setProcessContext(processContext);
        }
        this.processContext = processContext;
        watchBtn.setSelection(processContext != null && processContext.isWatchAllowed());
        perfBtn.setSelection(processContext != null && processContext.isMonitorPerf());
        boolean disableAll = false;
        if (processContext != null) {
            disableAll = processContext.getProcess().disableRunJobView();
        }
        /*
         * perfBtn.setSelection(RunProcessPlugin.getDefault().getPreferenceStore().getBoolean(
         * RunProcessPrefsConstants.ISSTATISTICSRUN) && !disableAll);
         * watchBtn.setSelection(RunProcessPlugin.getDefault().getPreferenceStore().getBoolean(
         * RunProcessPrefsConstants.ISEXECTIMERUN) && !disableAll);
         * saveJobBeforeRunButton.setSelection(RunProcessPlugin.getDefault().getPreferenceStore().getBoolean(
         * RunProcessPrefsConstants.ISSAVEBEFORERUN) && !disableAll);
         */
        if (this.processContext == null) {
            // this.processContext.setMonitorTrace(traceBtn.getSelection());
            processManager.setBooleanTrace(false);
        }
        saveJobBeforeRunButton.setSelection(processContext != null && processContext.isSaveBeforeRun());
        clearBeforeExec.setEnabled(processContext != null);
        clearBeforeExec.setSelection(processContext != null && processContext.isClearBeforeExec());
        setRunnable(processContext != null && !processContext.isRunning() && !disableAll);
    }

    /**
     * DOC Administrator Comment method "setRunnable".
     * 
     * @param b
     */
    private void setRunnable(boolean runnable) {
        // TODO Auto-generated method stub
        if (argumentsComposite != null) {
            argumentsComposite.setEnabled(runnable);
        }
        perfBtn.setEnabled(runnable);
        saveJobBeforeRunButton.setEnabled(runnable);
        watchBtn.setEnabled(runnable);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.properties.tab.IDynamicProperty#getCurRowSize()
     */
    @Override
    public int getCurRowSize() {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.properties.tab.IDynamicProperty#getElement()
     */
    @Override
    public Element getElement() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.properties.tab.IDynamicProperty#getHashCurControls()
     */
    @Override
    public BidiMap getHashCurControls() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.properties.tab.IDynamicProperty#getPart()
     */
    @Override
    public IMultiPageTalendEditor getPart() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @seeorg.talend.core.properties.tab.IDynamicProperty#getRepositoryAliasName(org.talend.core.model.properties.
     * ConnectionItem)
     */
    @Override
    public String getRepositoryAliasName(ConnectionItem connectionItem) {
        // TODO Auto-generated method stub
        return null;
    }

    /* 16969 */
    // /*
    // * (non-Javadoc)
    // *
    // * @see org.talend.core.properties.tab.IDynamicProperty#getRepositoryConnectionItemMap()
    // */
    // public Map<String, ConnectionItem> getRepositoryConnectionItemMap() {
    // // TODO Auto-generated method stub
    // return null;
    // }

    // /*
    // * (non-Javadoc)
    // *
    // * @see org.talend.core.properties.tab.IDynamicProperty#getRepositoryQueryStoreMap()
    // */
    // public Map<String, Query> getRepositoryQueryStoreMap() {
    // // TODO Auto-generated method stub
    // return null;
    // }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.properties.tab.IDynamicProperty#getRepositoryTableMap()
     */
    // public Map<String, IMetadataTable> getRepositoryTableMap() {
    // // TODO Auto-generated method stub
    // return null;
    // }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.properties.tab.IDynamicProperty#getSection()
     */
    @Override
    public EComponentCategory getSection() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.properties.tab.IDynamicProperty#getTableIdAndDbSchemaMap()
     */
    @Override
    public Map<String, String> getTableIdAndDbSchemaMap() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.properties.tab.IDynamicProperty#getTableIdAndDbTypeMap()
     */
    @Override
    public Map<String, String> getTableIdAndDbTypeMap() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.properties.tab.IDynamicProperty#refresh()
     */
    @Override
    public void refresh() {
        // TODO Auto-generated method stub
        if (!isDisposed()) {
            getParent().layout();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.properties.tab.IDynamicProperty#setCurRowSize(int)
     */
    @Override
    public void setCurRowSize(int i) {
        // TODO Auto-generated method stub

    }

}
