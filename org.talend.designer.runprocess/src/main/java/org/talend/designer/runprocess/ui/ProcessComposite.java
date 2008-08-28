// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.IStreamListener;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.core.model.IStreamMonitor;
import org.eclipse.debug.internal.ui.DebugUIPlugin;
import org.eclipse.debug.internal.ui.preferences.IDebugPreferenceConstants;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressService;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.core.CorePlugin;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.process.IContext;
import org.talend.designer.core.DesignerPlugin;
import org.talend.designer.core.debug.JobLaunchShortcutManager;
import org.talend.designer.runprocess.IProcessMessage;
import org.talend.designer.runprocess.IProcessor;
import org.talend.designer.runprocess.ProcessMessage;
import org.talend.designer.runprocess.ProcessMessageManager;
import org.talend.designer.runprocess.ProcessorException;
import org.talend.designer.runprocess.ProcessorUtilities;
import org.talend.designer.runprocess.RunProcessContext;
import org.talend.designer.runprocess.RunProcessPlugin;
import org.talend.designer.runprocess.RunprocessConstants;
import org.talend.designer.runprocess.ProcessMessage.MsgType;
import org.talend.designer.runprocess.i18n.Messages;
import org.talend.designer.runprocess.prefs.RunProcessPrefsConstants;
import org.talend.designer.runprocess.ui.actions.ClearPerformanceAction;
import org.talend.designer.runprocess.ui.actions.ClearTraceAction;
import org.talend.designer.runprocess.ui.actions.SaveJobBeforeRunAction;
import org.talend.designer.runprocess.ui.views.ProcessView;

/**
 * DOC chuger class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 * 
 */
public class ProcessComposite extends Composite {

    private static final int BUTTON_SIZE = 120;

    private static final int H_WEIGHT = 5;

    private static final int MINIMUM_HEIGHT = 65;

    private static final int MINIMUM_WIDTH = 530;

    private RunProcessContext processContext;

    /** Context composite. */
    private ProcessContextComposite contextComposite;

    /** Performance button. */
    private Button perfBtn;

    /** Trace button. */
    private Button traceBtn;

    /** Clear trace & performance button. */
    private Button clearTracePerfBtn;

    private Button saveJobBeforeRunButton;

    /** Clear log button. */
    // private Button clearLogBtn;
    private Button clearBeforeExec;

    /** Show time button. */
    private Button watchBtn;

    /** Exec button. */
    private Button execBtn;

    /** Kill button. */
    private Button killBtn;

    /** Debug button. */
    private Button debugBtn;

    /** Execution console. */
    private StyledText consoleText;

    /** RunProcessContext property change listener. */
    private PropertyChangeListener pcl;

    private CTabItem targetExecutionTabItem;

    private CTabFolder leftTabFolder;

    private IStreamListener streamListener;

    private boolean isAddedStreamListener;

    private boolean hideConsoleLine = false;

    private Button enableLineLimitButton;

    private Text lineLimitText;

    /**
     * DOC chuger ProcessComposite2 constructor comment.
     * 
     * @param parent Parent composite.
     * @param style Style bits.
     */
    public ProcessComposite(Composite parent, int style) {
        super(parent, style);
        initGraphicComponents(parent);
    }

    /**
     * DOC amaumont Comment method "initGraphicComponents".
     * 
     * @param parent
     */
    private void initGraphicComponents(Composite parent) {
        GridData data;
        GridLayout layout = new GridLayout();
        setLayout(layout);

        // Splitter
        SashForm sash = new SashForm(this, SWT.HORIZONTAL | SWT.SMOOTH);
        sash.setLayoutData(new GridData(GridData.FILL_BOTH));

        layout = new GridLayout();
        sash.setLayout(layout);

        leftTabFolder = new CTabFolder(sash, SWT.BORDER);
        leftTabFolder.setSimple(false);

        leftTabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));

        // Group context

        CTabItem contextTabItem = new CTabItem(leftTabFolder, SWT.BORDER);
        contextTabItem.setText(Messages.getString("ProcessComposite.contextTab")); //$NON-NLS-1$
        contextComposite = new ProcessContextComposite(leftTabFolder, SWT.NONE);
        contextComposite.setBackground(leftTabFolder.getDisplay().getSystemColor(SWT.COLOR_WHITE));
        contextTabItem.setControl(contextComposite);

        Composite targetExecutionComposite = createTargetExecutionComposite(leftTabFolder);
        targetExecutionComposite.setBackground(leftTabFolder.getDisplay().getSystemColor(SWT.COLOR_WHITE));

        targetExecutionTabItem = new CTabItem(leftTabFolder, SWT.BORDER);
        targetExecutionTabItem.setText(Messages.getString("ProcessComposite.targetExecutionTab")); //$NON-NLS-1$
        //targetExecutionTabItem.setToolTipText(Messages.getString("ProcessComposite.targetExecutionTabTooltipAvailable"
        // ));
        targetExecutionTabItem.setControl(targetExecutionComposite);

        // Group execution
        Group execGroup = new Group(sash, SWT.NONE);
        execGroup.setText(Messages.getString("ProcessComposite.execGroup")); //$NON-NLS-1$
        layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        execGroup.setLayout(layout);

        ScrolledComposite execScroll = new ScrolledComposite(execGroup, SWT.V_SCROLL | SWT.H_SCROLL);
        execScroll.setExpandHorizontal(true);
        execScroll.setExpandVertical(true);
        execScroll.setLayoutData(new GridData(GridData.FILL_BOTH));

        Composite execContent = new Composite(execScroll, SWT.NONE);
        layout = new GridLayout();
        execContent.setLayout(layout);
        execScroll.setContent(execContent);

        Composite execHeader = new Composite(execContent, SWT.NONE);
        FormLayout formLayout = new FormLayout();
        formLayout.marginWidth = 7;
        formLayout.marginHeight = 4;
        formLayout.spacing = 7;
        execHeader.setLayout(formLayout);
        execHeader.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        debugBtn = new Button(execHeader, SWT.PUSH);
        debugBtn.setText(Messages.getString("ProcessDebugDialog.debugBtn")); //$NON-NLS-1$
        debugBtn.setToolTipText(Messages.getString("ProcessComposite.debugHint")); //$NON-NLS-1$
        debugBtn.setImage(ImageProvider.getImage(RunProcessPlugin.imageDescriptorFromPlugin(RunProcessPlugin.PLUGIN_ID,
                "icons/process_debug.gif"))); //$NON-NLS-1$
        FormData formData = new FormData();
        formData.top = new FormAttachment(0, 15);
        formData.left = new FormAttachment(0);
        formData.right = new FormAttachment(0, BUTTON_SIZE);
        debugBtn.setLayoutData(formData);

        execBtn = new Button(execHeader, SWT.PUSH);
        execBtn.setText(Messages.getString("ProcessComposite.exec")); //$NON-NLS-1$
        execBtn.setToolTipText(Messages.getString("ProcessComposite.execHint")); //$NON-NLS-1$
        execBtn.setImage(ImageProvider.getImage(ERunprocessImages.RUN_PROCESS_ACTION));
        execBtn.setData(ProcessView.EXEC_ID);
        execBtn.setEnabled(false);
        formData = new FormData();
        formData.top = new FormAttachment(debugBtn, 0, SWT.TOP);
        formData.left = new FormAttachment(debugBtn, 0, SWT.RIGHT);
        formData.right = new FormAttachment(debugBtn, BUTTON_SIZE, SWT.RIGHT);
        execBtn.setLayoutData(formData);

        killBtn = new Button(execHeader, SWT.PUSH);
        killBtn.setText(Messages.getString("ProcessComposite.kill")); //$NON-NLS-1$
        killBtn.setToolTipText(Messages.getString("ProcessComposite.killHint")); //$NON-NLS-1$
        killBtn.setImage(ImageProvider.getImage(RunProcessPlugin.imageDescriptorFromPlugin(RunProcessPlugin.PLUGIN_ID,
                "icons/process_kill.gif"))); //$NON-NLS-1$
        setButtonLayoutData(killBtn);
        killBtn.setEnabled(false);
        formData = new FormData();
        formData.top = new FormAttachment(debugBtn, 0, SWT.TOP);
        formData.left = new FormAttachment(execBtn, 0, SWT.RIGHT);
        formData.right = new FormAttachment(execBtn, BUTTON_SIZE, SWT.RIGHT);
        killBtn.setLayoutData(formData);

        saveJobBeforeRunButton = new Button(execHeader, SWT.CHECK);
        saveJobBeforeRunButton.setText(Messages.getString("ProcessComposite.saveBeforeRun")); //$NON-NLS-1$
        saveJobBeforeRunButton.setToolTipText(Messages.getString("ProcessComposite.saveBeforeRunHint")); //$NON-NLS-1$
        saveJobBeforeRunButton.setEnabled(false);
        saveJobBeforeRunButton.setSelection(RunProcessPlugin.getDefault().getPreferenceStore().getBoolean(
                RunProcessPrefsConstants.ISSAVEBEFORERUN));
        data = new GridData();
        data.horizontalSpan = 2;
        data.horizontalAlignment = SWT.END;
        saveJobBeforeRunButton.setLayoutData(data);
        formData = new FormData();
        formData.top = new FormAttachment(execBtn, 0, SWT.BOTTOM);
        formData.left = new FormAttachment(debugBtn, 0, SWT.LEFT);
        saveJobBeforeRunButton.setLayoutData(formData);

        clearBeforeExec = new Button(execHeader, SWT.CHECK);
        clearBeforeExec.setText(Messages.getString("ProcessComposite.clearBefore")); //$NON-NLS-1$
        clearBeforeExec.setToolTipText(Messages.getString("ProcessComposite.clearBeforeHint")); //$NON-NLS-1$
        // clearBeforeExec.setEnabled(false);
        clearBeforeExec.setSelection(RunProcessPlugin.getDefault().getPreferenceStore().getBoolean(
                RunProcessPrefsConstants.ISCLEARBEFORERUN));
        data = new GridData();
        data.horizontalSpan = 2;
        data.horizontalAlignment = SWT.END;
        clearBeforeExec.setLayoutData(data);
        formData = new FormData();
        formData.top = new FormAttachment(execBtn, 0, SWT.BOTTOM);
        formData.left = new FormAttachment(saveJobBeforeRunButton, 0, SWT.RIGHT);
        clearBeforeExec.setLayoutData(formData);

        watchBtn = new Button(execHeader, SWT.CHECK);
        watchBtn.setText(Messages.getString("ProcessComposite.execTime")); //$NON-NLS-1$
        watchBtn.setToolTipText(Messages.getString("ProcessComposite.execTimeHint")); //$NON-NLS-1$
        watchBtn.setEnabled(false);
        watchBtn.setSelection(RunProcessPlugin.getDefault().getPreferenceStore().getBoolean(
                RunProcessPrefsConstants.ISEXECTIMERUN));
        data = new GridData();
        data.horizontalSpan = 2;
        data.horizontalAlignment = SWT.END;
        watchBtn.setLayoutData(data);
        formData = new FormData();
        formData.top = new FormAttachment(killBtn, 0, SWT.BOTTOM);
        formData.left = new FormAttachment(clearBeforeExec, 0, SWT.RIGHT);
        watchBtn.setLayoutData(formData);

        Group statisticsComposite = new Group(execHeader, SWT.NONE);
        statisticsComposite.setText(Messages.getString("ProcessComposite2.statsComposite")); //$NON-NLS-1$
        layout = new GridLayout(3, false);
        layout.marginWidth = 0;
        statisticsComposite.setLayout(layout);
        formData = new FormData();
        formData.right = new FormAttachment(100, 0);
        statisticsComposite.setLayoutData(formData);

        Composite statisticsButtonComposite = new Composite(statisticsComposite, SWT.NONE);
        layout = new GridLayout(1, false);
        layout.marginWidth = 0;
        statisticsButtonComposite.setLayout(layout);
        statisticsButtonComposite.setLayoutData(new GridData(GridData.FILL_VERTICAL));

        perfBtn = new Button(statisticsButtonComposite, SWT.CHECK);
        perfBtn.setText(Messages.getString("ProcessComposite.stat")); //$NON-NLS-1$
        perfBtn.setToolTipText(Messages.getString("ProcessComposite.statHint")); //$NON-NLS-1$
        perfBtn.setEnabled(false);
        perfBtn.setSelection(RunProcessPlugin.getDefault().getPreferenceStore().getBoolean(
                RunProcessPrefsConstants.ISSTATISTICSRUN));
        traceBtn = new Button(statisticsButtonComposite, SWT.CHECK);
        traceBtn.setText(Messages.getString("ProcessComposite.trace")); //$NON-NLS-1$
        traceBtn.setToolTipText(Messages.getString("ProcessComposite.traceHint")); //$NON-NLS-1$
        traceBtn.setEnabled(false);
        traceBtn
                .setSelection(RunProcessPlugin.getDefault().getPreferenceStore().getBoolean(RunProcessPrefsConstants.ISTRACESRUN));

        clearTracePerfBtn = new Button(statisticsComposite, SWT.PUSH);
        clearTracePerfBtn.setText(Messages.getString("ProcessComposite.clear")); //$NON-NLS-1$
        clearTracePerfBtn.setToolTipText(Messages.getString("ProcessComposite.clearHint")); //$NON-NLS-1$
        clearTracePerfBtn.setImage(ImageProvider.getImage(RunProcessPlugin.imageDescriptorFromPlugin(RunProcessPlugin.PLUGIN_ID,
                "icons/process_stat_clear.gif"))); //$NON-NLS-1$
        clearTracePerfBtn.setEnabled(false);

        consoleText = new StyledText(execContent, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.READ_ONLY);
        data = new GridData(GridData.FILL_BOTH);
        data.horizontalSpan = 2;
        data.minimumHeight = MINIMUM_HEIGHT;
        data.minimumWidth = MINIMUM_WIDTH;
        consoleText.setLayoutData(data);

        // see feature 0004895: Font size of the output console are very small
        if (!setConsoleFont()) {
            Font font = new Font(parent.getDisplay(), "courier", 8, SWT.NONE); //$NON-NLS-1$
            consoleText.setFont(font);
        }

        execScroll.setMinSize(execContent.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        sash.setWeights(new int[] { 2, H_WEIGHT });

        pcl = new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                runProcessContextChanged(evt);
            }
        };

        streamListener = new IStreamListener() {

            public void streamAppended(String text, IStreamMonitor monitor) {
                IProcessMessage message = new ProcessMessage(ProcessMessage.MsgType.STD_OUT, text);
                processContext.addDebugResultToConsole(message);
            }
        };
        addListeners();
        createLineLimitedControl(execContent);
    }

    /**
     * DOC bqian Comment method "createLineLimitedControl".
     */
    private void createLineLimitedControl(Composite container) {
        Composite composite = new Composite(container, SWT.NONE);
        composite.setLayoutData(new GridData());

        FormLayout formLayout = new FormLayout();
        formLayout.marginWidth = 7;
        formLayout.marginHeight = 4;
        formLayout.spacing = 7;
        composite.setLayout(formLayout);

        enableLineLimitButton = new Button(composite, SWT.CHECK);
        enableLineLimitButton.setText("Line limit");
        FormData formData = new FormData();
        enableLineLimitButton.setLayoutData(formData);
        enableLineLimitButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                lineLimitText.setEditable(enableLineLimitButton.getSelection());
                RunProcessPlugin.getDefault().getPluginPreferences().setValue(RunprocessConstants.ENABLE_CONSOLE_LINE_LIMIT,
                        enableLineLimitButton.getSelection());
            }
        });

        lineLimitText = new Text(composite, SWT.BORDER);
        formData = new FormData();
        formData.width = 120;
        formData.left = new FormAttachment(enableLineLimitButton, 0, SWT.RIGHT);
        lineLimitText.setLayoutData(formData);
        lineLimitText.addListener(SWT.Verify, new Listener() {

            // this text only receive number here.
            public void handleEvent(Event e) {
                String s = e.text;
                if (!s.equals("")) {
                    try {
                        Integer.parseInt(s);
                        RunProcessPlugin.getDefault().getPluginPreferences().setValue(
                                RunprocessConstants.CONSOLE_LINE_LIMIT_COUNT, lineLimitText.getText() + s);
                    } catch (Exception ex) {
                        e.doit = false;
                    }
                }
            }
        });
        lineLimitText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                RunProcessPlugin.getDefault().getPluginPreferences().setValue(RunprocessConstants.CONSOLE_LINE_LIMIT_COUNT,
                        lineLimitText.getText());
            }
        });

        boolean enable = RunProcessPlugin.getDefault().getPluginPreferences().getBoolean(
                RunprocessConstants.ENABLE_CONSOLE_LINE_LIMIT);
        enableLineLimitButton.setSelection(enable);
        lineLimitText.setEditable(enable);
        String count = RunProcessPlugin.getDefault().getPluginPreferences().getString(
                RunprocessConstants.CONSOLE_LINE_LIMIT_COUNT);
        if (count.equals("")) {
            count = "100";
        }
        lineLimitText.setText(count);
    }

    private int getConsoleRowLimit() {
        if (!enableLineLimitButton.isDisposed()) {
            if (enableLineLimitButton.getSelection()) {
                try {
                    return Integer.parseInt(lineLimitText.getText());
                } catch (Exception e) {
                }
            }
        }
        return SWT.DEFAULT;
    }

    /**
     * DOC amaumont Comment method "getTargetExecutionComposite".
     * 
     * @param parent
     * @return
     */
    protected Composite createTargetExecutionComposite(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);

        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        composite.setLayout(layout);

        StyledText text = new StyledText(composite, SWT.NONE);
        text.setText(Messages.getString("ProcessComposite.targetExecutionTabTooltipAvailable"));
        text.setWordWrap(true);
        text.setEditable(false);
        text.setLayoutData(new GridData(GridData.FILL_BOTH));

        return composite;
    }

    public boolean hasProcess() {
        return processContext != null;
    }

    public org.talend.core.model.process.IProcess getProcess() {
        return processContext.getProcess();
    }

    protected void addListeners() {
        perfBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                processContext.setMonitorPerf(perfBtn.getSelection());
            }
        });

        traceBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                processContext.setMonitorTrace(traceBtn.getSelection());
            }
        });

        clearTracePerfBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (processContext == null) {
                    clearTracePerfBtn.setEnabled(false);
                    return;
                }
                ClearPerformanceAction clearPerfAction = new ClearPerformanceAction();
                clearPerfAction.setProcess(processContext.getProcess());
                clearPerfAction.run();
                ClearTraceAction clearTraceAction = new ClearTraceAction();
                clearTraceAction.setProcess(processContext.getProcess());
                clearTraceAction.run();
            }
        });
        /*
         * clearLogBtn.addSelectionListener(new SelectionAdapter() {
         * 
         * @Override public void widgetSelected(SelectionEvent e) { processContext.clearMessages(); } });
         */
        execBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                execButtonPressed();
            }

        });

        killBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                kill();
            }
        });

        debugBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                debug();
            }
        });

        watchBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                processContext.setWatchAllowed(watchBtn.getSelection());
            }

        });
        saveJobBeforeRunButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                processContext.setSaveBeforeRun(saveJobBeforeRunButton.getSelection());
            }
        });

        clearBeforeExec.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                processContext.setClearBeforeExec(clearBeforeExec.getSelection());
            }
        });
    }

    protected boolean checkKillAllowed() {
        return true;
    }

    /**
     * bqian Comment method "execButtonPressed".
     */
    public void execButtonPressed() {
        if (processContext == null) {
            execBtn.setEnabled(false);
            return;
        }
        if (execBtn.getData().equals(ProcessView.PAUSE_ID)) {
            pause(ProcessView.PAUSE_ID);
        } else if (execBtn.getData().equals(ProcessView.RESUME_ID)) {
            pause(ProcessView.RESUME_ID);
        } else if (execBtn.getData().equals(ProcessView.EXEC_ID)) {
            addInHistoryRunningList();
            execBtn.setData(ProcessView.PAUSE_ID);
            // exec();

        }
    }

    public void pause(int id) {
        boolean isPause = id == ProcessView.PAUSE_ID;
        setExecBtn(isPause);
        if (isPause) {
            execBtn.setText("Resume");
            execBtn.setToolTipText("Resume");
            execBtn.setData(ProcessView.RESUME_ID);
        } else {
            execBtn.setData(ProcessView.PAUSE_ID);
        }
        processContext.setTracPause(isPause);
    }

    /**
     * 
     * DOC ggu Comment method "setCurRunMode".
     * 
     * for the F6 shortcut to run
     */
    public void setCurRunMode(int id) {
        pause(id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.widgets.Widget#dispose()
     */
    @Override
    public void dispose() {
        if (processContext != null) {
            processContext.removePropertyChangeListener(pcl);
        }
        super.dispose();
    }

    /**
     * Set the layout data of the button to a GridData with appropriate heights and widths.
     * 
     * @param button
     */
    protected static void setButtonLayoutData(final Button button) {
        GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        final int widthHint = 80;
        Point minSize = button.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
        data.widthHint = Math.max(widthHint, minSize.x);
        button.setLayoutData(data);
    }

    public void setProcessContext(RunProcessContext processContext) {

        IPreferenceStore preferenceStore = DesignerPlugin.getDefault().getPreferenceStore();
        String languagePrefix = LanguageManager.getCurrentLanguage().toString() + "_";
        if (this.processContext != null) {
            this.processContext.removePropertyChangeListener(pcl);
        }
        this.processContext = processContext;
        if (processContext != null) {
            processContext.addPropertyChangeListener(pcl);
        }

        boolean disableAll = false;
        if (processContext != null) {
            disableAll = processContext.getProcess().disableRunJobView();
        }

        perfBtn.setSelection(processContext != null && processContext.isMonitorPerf());
        traceBtn.setSelection(processContext != null && processContext.isMonitorTrace());
        watchBtn.setSelection(processContext != null && processContext.isWatchAllowed());
        // perfBtn.setSelection(RunProcessPlugin.getDefault().getPreferenceStore().getBoolean(
        // RunProcessPrefsConstants.ISSTATISTICSRUN)
        // && !disableAll);
        // traceBtn.setSelection(RunProcessPlugin.getDefault().getPreferenceStore().getBoolean(RunProcessPrefsConstants.
        // ISTRACESRUN)
        // && !disableAll);
        if (this.processContext != null) {
            this.processContext.setMonitorTrace(traceBtn.getSelection());
        }

        // watchBtn.setSelection(RunProcessPlugin.getDefault().getPreferenceStore().getBoolean(
        // RunProcessPrefsConstants.ISEXECTIMERUN)
        // && !disableAll);
        // saveJobBeforeRunButton.setSelection(RunProcessPlugin.getDefault().getPreferenceStore().getBoolean(
        // RunProcessPrefsConstants.ISSAVEBEFORERUN)
        // && !disableAll);
        // clearBeforeExec.setSelection(RunProcessPlugin.getDefault().getPreferenceStore().getBoolean(
        // RunProcessPrefsConstants.ISCLEARBEFORERUN)
        // && !disableAll);

        saveJobBeforeRunButton.setSelection(processContext != null && processContext.isSaveBeforeRun());
        setRunnable(processContext != null && !processContext.isRunning() && !disableAll);
        killBtn.setEnabled(processContext != null && processContext.isRunning() && !disableAll);
        clearBeforeExec.setEnabled(processContext != null);
        clearBeforeExec.setSelection(processContext != null && processContext.isClearBeforeExec());
        contextComposite.setProcess(((processContext != null) && !disableAll ? processContext.getProcess() : null));
        fillConsole(processContext != null ? processContext.getMessages() : new ArrayList<IProcessMessage>());
    }

    protected void setRunnable(boolean runnable) {
        perfBtn.setEnabled(runnable);
        traceBtn.setEnabled(runnable);
        clearTracePerfBtn.setEnabled(runnable);

        setExecBtn(runnable);
        debugBtn.setEnabled(runnable);
        contextComposite.setEnabled(runnable);
        clearBeforeExec.setEnabled(runnable);
        saveJobBeforeRunButton.setEnabled(runnable);
        watchBtn.setEnabled(runnable);
    }

    /**
     * qzhang Comment method "setExecBtn".
     * 
     * @param runnable
     */
    private void setExecBtn(final boolean runnable) {
        if (traceBtn.getSelection()) {
            boolean b = processContext != null;
            if (!runnable && b) {
                execBtn.setText("Pause");
                execBtn.setToolTipText("Pause the Job");
                execBtn.setImage(ImageProvider.getImage(ERunprocessImages.PAUSE_PROCESS_ACTION));
            } else {
                execBtn.setText(Messages.getString("ProcessComposite.exec")); //$NON-NLS-1$
                execBtn.setData(ProcessView.EXEC_ID);
                execBtn.setToolTipText(Messages.getString("ProcessComposite.execHint")); //$NON-NLS-1$
                execBtn.setImage(ImageProvider.getImage(ERunprocessImages.RUN_PROCESS_ACTION));
            }
            execBtn.setEnabled(b);
        } else {
            execBtn.setEnabled(runnable);
            execBtn.setText(Messages.getString("ProcessComposite.exec")); //$NON-NLS-1$
            execBtn.setToolTipText(Messages.getString("ProcessComposite.execHint")); //$NON-NLS-1$
            execBtn.setImage(ImageProvider.getImage(ERunprocessImages.RUN_PROCESS_ACTION));
            execBtn.setData(ProcessView.EXEC_ID);
        }

    }

    private void appendToConsole(final IProcessMessage message) {
        getDisplay().asyncExec(new Runnable() {

            public void run() {

                if (message.getType() == MsgType.CORE_OUT || message.getType() == MsgType.CORE_ERR) {
                    // always display job end msgs
                    callViewToDisplayMsg(message);
                } else {
                    if (!isHideConsoleLine()) {
                        callViewToDisplayMsg(message);
                    }
                }
            }

            /**
             * DOC yexiaowei Comment method "callViewToDisplayMsg".
             * 
             * @param message
             */
            private void callViewToDisplayMsg(final IProcessMessage message) {
                doAppendToConsole(message);
                scrollToEnd();
            }
        });
    }

    private void doAppendToConsole(final IProcessMessage message) {
        if (consoleText.isDisposed()) {
            return;
        }
        // see feature 0004895: Font size of the output console are very small
        setConsoleFont();

        String[] rows = message.getContent().split("\n");
        int rowLimit = getConsoleRowLimit();
        String content = null;
        if (rowLimit != SWT.DEFAULT) {
            int currentRows = consoleText.getLineCount();
            // if (consoleText.getText().equals("")) {
            currentRows--;
            // }
            if (currentRows >= rowLimit) {
                return;
            } else if (currentRows + rows.length <= rowLimit) {
                content = message.getContent();
            } else {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < rowLimit - currentRows; i++) {
                    sb.append(rows[i]).append("\n");
                }
                content = sb.toString();
            }
        }

        if (content == null) {
            content = message.getContent();
        }
        StyleRange style = new StyleRange();
        style.start = consoleText.getText().length();
        style.length = content.length();
        if (message.getType() == MsgType.CORE_OUT || message.getType() == MsgType.CORE_ERR) {
            style.fontStyle = SWT.ITALIC;
        }
        Color color;
        switch ((MsgType) message.getType()) {
        case CORE_OUT:
            color = getDisplay().getSystemColor(SWT.COLOR_BLUE);
            break;
        case CORE_ERR:
            color = getDisplay().getSystemColor(SWT.COLOR_DARK_RED);
            break;
        case STD_ERR:
            color = getDisplay().getSystemColor(SWT.COLOR_RED);
            break;
        case STD_OUT:
        default:
            color = getDisplay().getSystemColor(SWT.COLOR_BLACK);
            break;
        }
        style.foreground = color;

        consoleText.append(content);
        consoleText.setStyleRange(style);
    }

    /**
     * DOC chuang Comment method "setConsoleFont".
     */
    private boolean setConsoleFont() {
        IPreferenceStore preferenceStore = RunProcessPlugin.getDefault().getPreferenceStore();
        String fontType = preferenceStore.getString(RunProcessPrefsConstants.CONSOLE_FONT);
        if (StringUtils.isNotEmpty(fontType)) {
            FontData fontData = new FontData(fontType);
            if (consoleText.getFont() != null) {
                FontData oldFont = consoleText.getFont().getFontData()[0];
                // font is same
                if (oldFont.equals(fontData)) {
                    return false;
                }
            }
            Font font = new Font(this.getDisplay(), fontData);
            consoleText.setFont(font);
            return true;

        }
        return false;
    }

    private void fillConsole(Collection<IProcessMessage> messages) {
        consoleText.setText(""); //$NON-NLS-1$

        for (IProcessMessage processMessage : messages) {
            doAppendToConsole(processMessage);
        }
        scrollToEnd();
    }

    private void scrollToEnd() {
        if (consoleText.isDisposed()) {
            return;
        }
        consoleText.setCaretOffset(consoleText.getText().length());
        consoleText.showSelection();
    }

    /**
     * DOC bqian Comment method "addInHistoryRunningList".
     */
    private void addInHistoryRunningList() {
        if (getProcessContext() == null) {
            return;
        }
        // Add this job to running history list.
        IEditorPart activeEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        if (CorePlugin.getDefault().getDesignerCoreService().isTalendEditor(activeEditor)) {
            JobLaunchShortcutManager.run(activeEditor);
        } else {
            exec();
        }

    }

    public void exec() {

        setHideconsoleLine(false);
        if (getProcessContext() == null) {
            return;
        }

        CorePlugin.getDefault().getDesignerCoreService().saveJobBeforeRun(getProcessContext().getProcess());
        if (clearBeforeExec.getSelection()) {
            processContext.clearMessages();
        }
        // if (watchBtn.getSelection()) {
        // processContext.switchTime();
        // }
        processContext.setWatchAllowed(watchBtn.getSelection());
        processContext.setMonitorPerf(perfBtn.getSelection());
        processContext.setMonitorTrace(traceBtn.getSelection());

        checkSaveBeforeRunSelection();

        processContext.setSelectedContext(contextComposite.getSelectedContext());
        processContext.exec(getShell());
    }

    public void kill() {
        killBtn.setEnabled(false);
        setHideconsoleLine(true);
        processContext.kill();
    }

    boolean debugMode = false;

    public void debug() {

        setHideconsoleLine(false);
        if ((processContext.getProcess()) instanceof org.talend.designer.core.ui.editor.process.Process) {
            ((org.talend.designer.core.ui.editor.process.Process) processContext.getProcess()).checkDifferenceWithRepository();
        }

        final IPreferenceStore preferenceStore = DebugUIPlugin.getDefault().getPreferenceStore();
        final boolean oldValueConsoleOnOut = preferenceStore.getBoolean(IDebugPreferenceConstants.CONSOLE_OPEN_ON_OUT);
        final boolean oldValueConsoleOnErr = preferenceStore.getBoolean(IDebugPreferenceConstants.CONSOLE_OPEN_ON_ERR);

        preferenceStore.setValue(IDebugPreferenceConstants.CONSOLE_OPEN_ON_OUT, false);

        preferenceStore.setValue(IDebugPreferenceConstants.CONSOLE_OPEN_ON_ERR, false);

        checkSaveBeforeRunSelection();

        if (contextComposite.promptConfirmLauch()) {
            setRunnable(false);
            final IContext context = contextComposite.getSelectedContext();

            IRunnableWithProgress worker = new IRunnableWithProgress() {

                public void run(IProgressMonitor monitor) {
                    IProcessor processor = ProcessorUtilities.getProcessor(processContext.getProcess(), context);
                    monitor.beginTask("Launching debugger", IProgressMonitor.UNKNOWN); //$NON-NLS-1$
                    try {
                        // use this function to generate childrens also.
                        ProcessorUtilities.generateCode(processContext.getProcess(), context, false, false, true);
                        ILaunchConfiguration config = processor.debug();
                        if (config != null) {
                            // PlatformUI.getWorkbench().getActiveWorkbenchWindow().addPerspectiveListener(new
                            // DebugInNewWindowListener());
                            DebugUITools.launch(config, ILaunchManager.DEBUG_MODE);

                        } else {
                            MessageDialog.openInformation(getShell(), Messages.getString("ProcessDebugDialog.debugBtn"), //$NON-NLS-1$
                                    Messages.getString("ProcessDebugDialog.errortext")); //$NON-NLS-1$ 
                        }
                    } catch (ProcessorException e) {
                        IStatus status = new Status(IStatus.ERROR, RunProcessPlugin.PLUGIN_ID, IStatus.OK,
                                "Debug launch failed.", e); //$NON-NLS-1$
                        RunProcessPlugin.getDefault().getLog().log(status);
                        MessageDialog.openError(getShell(), Messages.getString("ProcessDebugDialog.debugBtn"), ""); //$NON-NLS-1$ //$NON-NLS-2$
                    } finally {
                        monitor.done();
                    }
                }
            };

            IProgressService progressService = PlatformUI.getWorkbench().getProgressService();
            try {
                progressService.runInUI(PlatformUI.getWorkbench().getProgressService(), worker, ResourcesPlugin.getWorkspace()
                        .getRoot());
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        debugMode = true;
        try {
            Thread thread = new Thread() {

                @Override
                public void run() {
                    while (debugMode) {
                        final IProcess process = DebugUITools.getCurrentProcess();
                        if (process != null && process.isTerminated()) {
                            getDisplay().asyncExec(new Runnable() {

                                public void run() {
                                    setRunnable(true);
                                    killBtn.setEnabled(false);
                                    preferenceStore.setValue(IDebugPreferenceConstants.CONSOLE_OPEN_ON_OUT, oldValueConsoleOnOut);

                                    preferenceStore.setValue(IDebugPreferenceConstants.CONSOLE_OPEN_ON_ERR, oldValueConsoleOnErr);

                                    if (isAddedStreamListener) {
                                        process.getStreamsProxy().getOutputStreamMonitor().removeListener(streamListener);
                                        isAddedStreamListener = false;

                                        if (processContext.isRunning()) {
                                            final String endingPattern = Messages.getString("ProcessComposite.endPattern"); //$NON-NLS-1$
                                            MessageFormat mf = new MessageFormat(endingPattern);
                                            String byeMsg;
                                            try {
                                                byeMsg = "\n"
                                                        + mf.format(new Object[] { processContext.getProcess().getName(),
                                                                new Date(), new Integer(process.getExitValue()) });
                                                processContext.addDebugResultToConsole(new ProcessMessage(MsgType.CORE_OUT,
                                                        byeMsg));
                                            } catch (DebugException e) {
                                                e.printStackTrace();
                                            }
                                            processContext.setRunning(false);
                                        }
                                    }
                                    debugMode = false;
                                }
                            });
                        } else {
                            if (process != null) { // (one at leat) process
                                // still running
                                getDisplay().asyncExec(new Runnable() {

                                    public void run() {
                                        setRunnable(false);
                                        killBtn.setEnabled(true);
                                        processContext.setRunning(true);
                                        processContext.setDebugProcess(process);
                                        if (!isAddedStreamListener) {
                                            process.getStreamsProxy().getOutputStreamMonitor().addListener(streamListener);
                                            if (clearBeforeExec.getSelection()) {
                                                processContext.clearMessages();
                                            }
                                            if (watchBtn.getSelection()) {
                                                processContext.switchTime();
                                            }

                                            ClearPerformanceAction clearPerfAction = new ClearPerformanceAction();
                                            clearPerfAction.setProcess(processContext.getProcess());
                                            clearPerfAction.run();

                                            ClearTraceAction clearTraceAction = new ClearTraceAction();
                                            clearTraceAction.setProcess(processContext.getProcess());
                                            clearTraceAction.run();
                                            isAddedStreamListener = true;

                                            final String startingPattern = Messages.getString("ProcessComposite.startPattern"); //$NON-NLS-1$
                                            MessageFormat mf = new MessageFormat(startingPattern);
                                            String welcomeMsg = mf.format(new Object[] { processContext.getProcess().getName(),
                                                    new Date() });
                                            processContext.addDebugResultToConsole(new ProcessMessage(MsgType.CORE_OUT,
                                                    welcomeMsg));
                                        }
                                    }
                                });
                            } else { // no process running
                                getDisplay().asyncExec(new Runnable() {

                                    public void run() {
                                        setRunnable(true);
                                        killBtn.setEnabled(false);
                                    }
                                });
                            }
                        }
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            thread.start();
        } catch (Exception e) {
            ExceptionHandler.process(e);
            processContext.addErrorMessage(e);
            kill();
        }
    }

    /**
     * DOC Administrator Comment method "checkSaveBeforeRunSelection".
     */
    private void checkSaveBeforeRunSelection() {
        if (saveJobBeforeRunButton.getSelection()) {
            SaveJobBeforeRunAction action = new SaveJobBeforeRunAction(processContext.getProcess());
            action.run();
        }
    }

    private void runProcessContextChanged(final PropertyChangeEvent evt) {
        String propName = evt.getPropertyName();
        if (ProcessMessageManager.PROP_MESSAGE_ADD.equals(propName)
                || ProcessMessageManager.PROP_DEBUG_MESSAGE_ADD.equals(propName)) {
            appendToConsole((IProcessMessage) evt.getNewValue());
        } else if (ProcessMessageManager.PROP_MESSAGE_CLEAR.equals(propName)) {
            getDisplay().asyncExec(new Runnable() {

                public void run() {
                    if (!consoleText.isDisposed()) {
                        consoleText.setText(""); //$NON-NLS-1$
                    }
                }
            });
        } else if (RunProcessContext.PROP_MONITOR.equals(propName)) {
            perfBtn.setSelection(((Boolean) evt.getNewValue()).booleanValue());
        } else if (RunProcessContext.TRACE_MONITOR.equals(propName)) {
            traceBtn.setSelection(((Boolean) evt.getNewValue()).booleanValue());
        } else if (RunProcessContext.PROP_RUNNING.equals(propName)) {
            getDisplay().asyncExec(new Runnable() {

                public void run() {
                    if (isDisposed()) {
                        return;
                    }
                    boolean running = ((Boolean) evt.getNewValue()).booleanValue();
                    setRunnable(!running);
                    killBtn.setEnabled(running);
                }
            });
        }
    }

    @Override
    public Display getDisplay() {
        return Display.getDefault();
    }

    /**
     * Getter for targetExecutionTabItem.
     * 
     * @return the targetExecutionTabItem
     */
    public CTabItem getTargetExecutionTabItem() {
        return this.targetExecutionTabItem;
    }

    /**
     * Getter for leftTabFolder.
     * 
     * @return the leftTabFolder
     */
    public CTabFolder getLeftTabFolder() {
        return this.leftTabFolder;
    }

    protected RunProcessContext getProcessContext() {
        return this.processContext;
    }

    /**
     * Getter for debugBtn.
     * 
     * @return the debugBtn
     */
    public Button getDebugBtn() {
        return this.debugBtn;
    }

    /**
     * Getter for infoview.
     * 
     * @return the infoview
     */
    public boolean isHideConsoleLine() {
        return this.hideConsoleLine;
    }

    /**
     * Sets the infoview.
     * 
     * @param infoview the infoview to set
     */
    public void setHideconsoleLine(boolean infoview) {
        this.hideConsoleLine = infoview;
    }

}
