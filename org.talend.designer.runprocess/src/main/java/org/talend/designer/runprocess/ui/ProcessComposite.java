// ============================================================================
//
// Copyright (C) 2006-2010 Talend Inc. - www.talend.com
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
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
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
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
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressService;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.core.CorePlugin;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.IProcess2;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.RepositoryManager;
import org.talend.core.prefs.ITalendCorePrefConstants;
import org.talend.core.ui.branding.IBrandingService;
import org.talend.designer.core.DesignerPlugin;
import org.talend.designer.core.ReplaceNodesInProcessProvider;
import org.talend.designer.core.debug.JobLaunchShortcutManager;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.ui.editor.connections.Connection;
import org.talend.designer.core.ui.editor.connections.ConnectionTrace;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.views.problems.Problems;
import org.talend.designer.runprocess.IProcessMessage;
import org.talend.designer.runprocess.IProcessor;
import org.talend.designer.runprocess.JobErrorsChecker;
import org.talend.designer.runprocess.ProcessMessage;
import org.talend.designer.runprocess.ProcessMessageManager;
import org.talend.designer.runprocess.Processor;
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

    private static final int BUTTON_SIZE = 100;

    private static final int H_WEIGHT = 15;

    private static final int MINIMUM_HEIGHT = 65;

    private static final int MINIMUM_WIDTH = 530;

    private static RunProcessContext processContext;

    /** Context composite. */
    private ProcessContextComposite contextComposite;

    private Composite argumentsComposite;

    private JobVMArgumentsComposite argumentsViewer;

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

    /** Kill button. */
    private Button killBtn;

    /** Move Button */
    private Button moveButton;

    /** Execution console. */
    private StyledText consoleText;

    /** RunProcessContext property change listener. */
    private PropertyChangeListener pcl;

    private CTabItem targetExecutionTabItem;

    private CTabItem jobVMTabItem;

    private CTabFolder leftTabFolder;

    private IStreamListener streamListener;

    private boolean isAddedStreamListener;

    private boolean hideConsoleLine = false;

    private Button enableLineLimitButton;

    private Text lineLimitText;

    private SashForm sash;

    private ToolBar toolBar;

    private ToolItem itemDropDown;

    private Double extend = new Double(0);

    public HashMap<String, IProcessMessage> errorMessMap = new HashMap<String, IProcessMessage>();

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
     * DOC qli Comment method "initGraphicComponents".
     * 
     * @param text
     * 
     */
    private Point computeSize(String text) {
        GC gc = new GC(itemDropDown.getDisplay());
        final Point p = gc.textExtent(text);
        gc.dispose();
        return p;
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
        sash = new SashForm(this, SWT.HORIZONTAL | SWT.SMOOTH);
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
        // targetExecutionTabItem.setToolTipText(Messages.getString(
        // "ProcessComposite.targetExecutionTabTooltipAvailable"
        // ));
        targetExecutionTabItem.setControl(targetExecutionComposite);

        // Job Run VM Arguments Tab if language is java.
        if (LanguageManager.getCurrentLanguage() == ECodeLanguage.JAVA) {
            jobVMTabItem = new CTabItem(leftTabFolder, SWT.BORDER);
            jobVMTabItem.setText(Messages.getString("ProcessComposite.JVMTab")); //$NON-NLS-1$
            argumentsComposite = new Composite(leftTabFolder, SWT.NONE);
            argumentsComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
            GridLayout gridLayoutArguments = new GridLayout(1, false);
            argumentsComposite.setLayout(gridLayoutArguments);
            argumentsViewer = new JobVMArgumentsComposite("vmarguments", Messages
                    .getString("RunProcessPreferencePage.vmArgument"), //$NON-NLS-1$
                    argumentsComposite);
            // argumentsViewer.setEnabled(false, argumentsComposite);
            jobVMTabItem.setControl(argumentsComposite);
        }

        // group Button
        // qli,see the feature 6366.
        Composite buttonComposite = new Composite(sash, SWT.ERROR);
        buttonComposite.setLayout(new GridLayout());

        moveButton = new Button(buttonComposite, SWT.PUSH);
        moveButton.setText("<<"); //$NON-NLS-1$
        moveButton.setToolTipText(Messages.getString("ProcessComposite.hideContext")); //$NON-NLS-1$

        final GridData layoutData = new GridData();
        layoutData.verticalAlignment = GridData.CENTER;
        layoutData.horizontalAlignment = GridData.CENTER;
        layoutData.grabExcessHorizontalSpace = true;
        layoutData.grabExcessVerticalSpace = true;
        moveButton.setLayoutData(layoutData);

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
        // qli
        // see the feature 6366
        toolBar = new ToolBar(execHeader, SWT.FLAT | SWT.RIGHT);

        itemDropDown = new ToolItem(toolBar, SWT.ARROW);
        itemDropDown.setText(" " + Messages.getString("ProcessComposite.exec"));//$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
        itemDropDown.setData(ProcessView.EXEC_ID);
        itemDropDown.setToolTipText(Messages.getString("ProcessComposite.execHint"));//$NON-NLS-1$
        itemDropDown.setImage(ImageProvider.getImage(ERunprocessImages.RUN_PROCESS_ACTION));

        final Menu menu = new Menu(execHeader);
        itemDropDown.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent event) {
                if (event.detail == SWT.ARROW) {
                    Rectangle bounds = itemDropDown.getBounds();
                    Point point = toolBar.toDisplay(bounds.x, bounds.y + bounds.height);
                    menu.setLocation(point);
                    menu.setVisible(true);
                } else {
                    // qli modified to fix the bug 6659.
                    RepositoryManager.refreshCreatedNode(ERepositoryObjectType.ROUTINES);
                    RepositoryManager.refreshCreatedNode(ERepositoryObjectType.JOBLET);
                    ToolItem item = (ToolItem) event.widget;
                    errorMessMap.clear();
                    if (item.getData().equals(ProcessView.DEBUG_ID)) {
                        debug();
                    } else {
                        execButtonPressed();
                    }
                }
            }
        });

        // Run
        final MenuItem menuItem1 = new MenuItem(menu, SWT.PUSH);
        menuItem1.setText(" " + Messages.getString("ProcessComposite.exec"));//$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
        menuItem1.setImage(ImageProvider.getImage(ERunprocessImages.RUN_PROCESS_ACTION));
        menuItem1.setData(ProcessView.EXEC_ID);
        menuItem1.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent event) {
                if (!itemDropDown.getData().equals(ProcessView.PAUSE_ID) && !itemDropDown.getData().equals(ProcessView.RESUME_ID)) {
                    itemDropDown.setText(menuItem1.getText());
                    itemDropDown.setData(ProcessView.EXEC_ID);
                    itemDropDown.setImage(ImageProvider.getImage(ERunprocessImages.RUN_PROCESS_ACTION));
                    itemDropDown.setToolTipText(Messages.getString("ProcessComposite.execHint"));//$NON-NLS-1$
                    toolBar.getParent().layout();
                }
            }
        });
        IBrandingService brandingService = (IBrandingService) GlobalServiceRegister.getDefault().getService(
                IBrandingService.class);
        if (brandingService.getBrandingConfiguration().isAllowDebugMode()) {
            // Debug
            debugMenuItem = new MenuItem(menu, SWT.PUSH);
            debugMenuItem.setText(" " + Messages.getString("ProcessDebugDialog.debugBtn")); //$NON-NLS-1$//$NON-NLS-2$
            debugMenuItem.setData(ProcessView.DEBUG_ID);
            debugMenuItem.setImage(ImageProvider.getImage(ERunprocessImages.DEBUG_PROCESS_ACTION));
            debugMenuItem.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent event) {
                    if (!itemDropDown.getData().equals(ProcessView.PAUSE_ID)
                            && !itemDropDown.getData().equals(ProcessView.RESUME_ID)) {
                        itemDropDown.setText(debugMenuItem.getText());
                        itemDropDown.setData(ProcessView.DEBUG_ID);
                        itemDropDown.setImage(ImageProvider.getImage(ERunprocessImages.DEBUG_PROCESS_ACTION));
                        itemDropDown.setToolTipText(Messages.getString("ProcessComposite.debugHint"));//$NON-NLS-1$
                        toolBar.getParent().layout();
                    }

                }
            });
        }
        toolBar.setEnabled(false);
        FormData formData = new FormData();
        // see the feature 6366,qli comment.
        // make a judge when the text change in diffrent languages.

        Point debugSize = null;
        Point execSize = null;
        formData.left = new FormAttachment(0);
        if (brandingService.getBrandingConfiguration().isAllowDebugMode()) {
            // set debug text to judge size
            itemDropDown.setText(debugMenuItem.getText());
            debugSize = computeSize(itemDropDown.getText());

            // set exec text to judge size
            itemDropDown.setText(menuItem1.getText());
            execSize = computeSize(itemDropDown.getText());
            if (debugSize.x > execSize.x) {
                formData.right = new FormAttachment(0, debugSize.x + 70);
            } else {
                formData.right = new FormAttachment(0, execSize.x + 70);
            }
        } else {
            // set exec text to judge size
            itemDropDown.setText(menuItem1.getText());
            execSize = computeSize(itemDropDown.getText());
            formData.right = new FormAttachment(0, execSize.x + 70);
        }
        toolBar.setLayoutData(formData);

        killBtn = new Button(execHeader, SWT.PUSH);
        killBtn.setText(Messages.getString("ProcessComposite.kill")); //$NON-NLS-1$
        killBtn.setToolTipText(Messages.getString("ProcessComposite.killHint")); //$NON-NLS-1$
        killBtn.setImage(ImageProvider.getImage(ERunprocessImages.KILL_PROCESS_ACTION));
        setButtonLayoutData(killBtn);
        killBtn.setEnabled(false);
        formData = new FormData();
        formData.top = new FormAttachment(toolBar, 0, SWT.TOP);
        formData.left = new FormAttachment(toolBar, 0, SWT.RIGHT);
        // qli modified to fix the bug "7302".
        Point killSize = computeSize(killBtn.getText());
        if (brandingService.getBrandingConfiguration().isAllowDebugMode()) {
            if ((killSize.x > debugSize.x) && (killSize.x > execSize.x)) {
                formData.right = new FormAttachment(toolBar, killSize.x + 70, SWT.RIGHT);
            } else if (debugSize.x > execSize.x) {
                formData.right = new FormAttachment(toolBar, debugSize.x + 70, SWT.RIGHT);
            } else {
                formData.right = new FormAttachment(toolBar, execSize.x + 70, SWT.RIGHT);
            }
        } else {
            if (killSize.x > execSize.x) {
                formData.right = new FormAttachment(toolBar, killSize.x + 70, SWT.RIGHT);
            } else {
                formData.right = new FormAttachment(toolBar, execSize.x + 70, SWT.RIGHT);
            }
        }
        formData.height = 30;
        killBtn.setLayoutData(formData);

        saveJobBeforeRunButton = new Button(execHeader, SWT.CHECK);
        saveJobBeforeRunButton.setText(Messages.getString("ProcessComposite.saveBeforeRun")); //$NON-NLS-1$
        saveJobBeforeRunButton.setToolTipText(Messages.getString("ProcessComposite.saveBeforeRunHint")); //$NON-NLS-1$
        // saveJobBeforeRunButton.setEnabled(false);
        saveJobBeforeRunButton.setSelection(RunProcessPlugin.getDefault().getPreferenceStore().getBoolean(
                RunProcessPrefsConstants.ISSAVEBEFORERUN));
        data = new GridData();
        data.horizontalSpan = 2;
        data.horizontalAlignment = SWT.END;
        saveJobBeforeRunButton.setLayoutData(data);
        formData = new FormData();
        formData.top = new FormAttachment(toolBar, 0, SWT.BOTTOM);
        formData.left = new FormAttachment(toolBar, 0, SWT.LEFT);
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
        formData.top = new FormAttachment(toolBar, 0, SWT.BOTTOM);
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
        // formData.right = new FormAttachment(100, 0);
        formData.left = new FormAttachment(watchBtn, 0, SWT.RIGHT);
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
        // feature 6875, add searching capability, nma
        consoleText.addKeyListener(new KeyListener() {

            public void keyPressed(KeyEvent evt) {
                // select all
                if ((evt.stateMask == SWT.CTRL) && (evt.keyCode == 'a')) {
                    if (consoleText.getText().length() > 0)
                        consoleText.setSelection(0, (consoleText.getText().length() - 1));
                }
                // search special string value
                else if ((evt.stateMask == SWT.CTRL) && (evt.keyCode == 'f')) {
                    FindDialog td = new FindDialog(Display.getCurrent().getActiveShell());
                    td.setConsoleText(consoleText);
                    td.setBlockOnOpen(true);
                    td.open();

                }
            }

            public void keyReleased(KeyEvent arg0) {

            }
        });
        // see feature 0004895: Font size of the output console are very small
        if (!setConsoleFont()) {
            Font font = new Font(parent.getDisplay(), "courier", 8, SWT.NONE); //$NON-NLS-1$
            consoleText.setFont(font);
        }

        execScroll.setMinSize(execContent.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        sash.setSashWidth(0);
        sash.setWeights(new int[] { 7, 1, H_WEIGHT });

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
        enableLineLimitButton.setText(Messages.getString("ProcessComposite.lineLimited")); //$NON-NLS-1$
        FormData formData = new FormData();
        enableLineLimitButton.setLayoutData(formData);
        enableLineLimitButton.setEnabled(false);
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
        lineLimitText.setEnabled(false);
        lineLimitText.addListener(SWT.Verify, new Listener() {

            // this text only receive number here.
            public void handleEvent(Event e) {
                String s = e.text;
                if (!s.equals("")) { //$NON-NLS-1$
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
        if (count.equals("")) { //$NON-NLS-1$
            count = "100"; //$NON-NLS-1$
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
        text.setText(Messages.getString("ProcessComposite.targetExecutionTabTooltipAvailable")); //$NON-NLS-1$
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
        // qli comment "addSelectionListener" .
        moveButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                if (moveButton.getText().equals("<<")) { //$NON-NLS-1$
                    sash.setWeights(new int[] { 0, 1, 23 });
                    moveButton.setText(">>"); //$NON-NLS-1$
                    moveButton.setToolTipText(Messages.getString("ProcessComposite.showContext")); //$NON-NLS-1$
                } else if (moveButton.getText().equals(">>")) { //$NON-NLS-1$
                    sash.setWeights(new int[] { 7, 1, H_WEIGHT });
                    moveButton.setText("<<"); //$NON-NLS-1$
                    moveButton.setToolTipText(Messages.getString("ProcessComposite.hideContext"));//$NON-NLS-1$
                }
            }
        });

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
                org.talend.core.model.process.IProcess process = processContext.getProcess();
                List<INode> nodeList = (List<INode>) process.getGraphicalNodes();
                for (INode node : nodeList) {
                    for (Connection connection : (List<Connection>) node.getOutgoingConnections()) {
                        ConnectionTrace traceNode = connection.getConnectionTrace();
                        if (traceNode == null) {
                            continue;
                        }
                        traceNode.setPropertyValue(EParameterName.TRACES_SHOW_ENABLE.getName(), traceBtn.getSelection());
                    }
                }

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

        killBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                kill();
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
            toolBar.setEnabled(false);
            return;
        }
        if (itemDropDown.getData().equals(ProcessView.PAUSE_ID)) {
            pause(ProcessView.PAUSE_ID);
        } else if (itemDropDown.getData().equals(ProcessView.RESUME_ID)) {
            pause(ProcessView.RESUME_ID);
        } else if (itemDropDown.getData().equals(ProcessView.EXEC_ID)) {
            addInHistoryRunningList();
            itemDropDown.setData(ProcessView.PAUSE_ID);
            // exec();

        }
    }

    public void pause(int id) {
        boolean isPause = id == ProcessView.PAUSE_ID;
        setExecBtn(isPause);
        if (isPause) {
            itemDropDown.setText(Messages.getString("ProcessComposite.textContent")); //$NON-NLS-1$
            itemDropDown.setToolTipText(Messages.getString("ProcessComposite.tipTextContent")); //$NON-NLS-1$
            itemDropDown.setData(ProcessView.RESUME_ID);
        } else {
            itemDropDown.setData(ProcessView.PAUSE_ID);
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
        String languagePrefix = LanguageManager.getCurrentLanguage().toString() + "_"; //$NON-NLS-1$
        if (this.processContext != null) {
            this.processContext.removePropertyChangeListener(pcl);
        }
        this.processContext = processContext;
        if (processContext != null) {
            processContext.addPropertyChangeListener(pcl);
        }
        if (argumentsViewer != null) {
            argumentsViewer.setProcessContext(processContext);
        }
        boolean disableAll = false;
        if (processContext != null) {
            disableAll = processContext.getProcess().disableRunJobView();

        }

        perfBtn.setSelection(processContext != null && processContext.isMonitorPerf());
        traceBtn.setSelection(processContext != null && processContext.isMonitorTrace());
        watchBtn.setSelection(processContext != null && processContext.isWatchAllowed());
        // perfBtn.setSelection(RunProcessPlugin.getDefault().getPreferenceStore(
        // ).getBoolean(
        // RunProcessPrefsConstants.ISSTATISTICSRUN)
        // && !disableAll);
        // traceBtn.setSelection(RunProcessPlugin.getDefault().getPreferenceStore
        // ().getBoolean(RunProcessPrefsConstants.
        // ISTRACESRUN)
        // && !disableAll);
        if (this.processContext != null) {
            this.processContext.setMonitorTrace(traceBtn.getSelection());
        }

        // watchBtn.setSelection(RunProcessPlugin.getDefault().getPreferenceStore
        // ().getBoolean(
        // RunProcessPrefsConstants.ISEXECTIMERUN)
        // && !disableAll);
        // saveJobBeforeRunButton.setSelection(RunProcessPlugin.getDefault().
        // getPreferenceStore().getBoolean(
        // RunProcessPrefsConstants.ISSAVEBEFORERUN)
        // && !disableAll);
        // clearBeforeExec.setSelection(RunProcessPlugin.getDefault().
        // getPreferenceStore().getBoolean(
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
        contextComposite.setEnabled(runnable);
        if (argumentsComposite != null) {
            argumentsComposite.setEnabled(runnable);
        }
        clearBeforeExec.setEnabled(runnable);
        saveJobBeforeRunButton.setEnabled(runnable);
        watchBtn.setEnabled(runnable);

        enableLineLimitButton.setEnabled(runnable);
        lineLimitText.setEnabled(runnable);

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
                itemDropDown.setText(" " + Messages.getString("ProcessComposite.pause")); //$NON-NLS-1$//$NON-NLS-2$
                itemDropDown.setToolTipText(Messages.getString("ProcessComposite.pauseJob")); //$NON-NLS-1$
                itemDropDown.setImage(ImageProvider.getImage(ERunprocessImages.PAUSE_PROCESS_ACTION));
                itemDropDown.setData(ProcessView.PAUSE_ID);
                toolBar.getParent().layout();
            } else {
                itemDropDown.setText(" " + Messages.getString("ProcessComposite.exec") + "  "); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
                itemDropDown.setData(ProcessView.EXEC_ID);
                itemDropDown.setToolTipText(Messages.getString("ProcessComposite.execHint")); //$NON-NLS-1$
                itemDropDown.setImage(ImageProvider.getImage(ERunprocessImages.RUN_PROCESS_ACTION));
            }
            toolBar.setEnabled(b);
        } else {
            // qli modified to fix the bug 7354.
            toolBar.setEnabled(runnable);
            if (itemDropDown.getData().equals(ProcessView.DEBUG_ID)) {
                debugMenuItem.setText(" " + Messages.getString("ProcessDebugDialog.debugBtn")); //$NON-NLS-1$//$NON-NLS-2$
                debugMenuItem.setData(ProcessView.DEBUG_ID);
                debugMenuItem.setImage(ImageProvider.getImage(ERunprocessImages.DEBUG_PROCESS_ACTION));
            } else {
                itemDropDown.setText(" " + Messages.getString("ProcessComposite.exec") + "  "); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
                itemDropDown.setToolTipText(Messages.getString("ProcessComposite.execHint")); //$NON-NLS-1$
                itemDropDown.setImage(ImageProvider.getImage(ERunprocessImages.RUN_PROCESS_ACTION));
                itemDropDown.setData(ProcessView.EXEC_ID);
            }
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

        String[] rows = message.getContent().split("\n"); //$NON-NLS-1$
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
                    sb.append(rows[i]).append("\n"); //$NON-NLS-1$
                }
                content = sb.toString();
            }
        }

        if (content == null) {
            content = message.getContent();
        }

        StyleRange style = new StyleRange();
        style.start = consoleText.getText().length();

        String[] contents = content.split("\n");
        for (int i = 0; i < contents.length; i++) {
            if (isPattern(contents[i]) || isPatternFor(contents[i])) {
                consoleText.append(""); //$NON-NLS-1$
                content = ""; //$NON-NLS-1$
            } else {
                consoleText.append(contents[i]);
                consoleText.append("\n");
            }
        }
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

        // added by hyWang for bug 0007411
        if ((style.start + style.length) > consoleText.getCharCount()) {
            style.length = consoleText.getCharCount() - style.start;
        }

        consoleText.setStyleRange(style);
    }

    /**
     * DOC chuang Comment method "setConsoleFont".
     */
    private boolean setConsoleFont() {
        IPreferenceStore preferenceStore = CorePlugin.getDefault().getPreferenceStore();
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
        if (getProcessContext().getProcess() instanceof IProcess2) {
            ReplaceNodesInProcessProvider.beforeRunJobInGUI((IProcess2) getProcessContext().getProcess());
        }
        CorePlugin.getDefault().getRunProcessService().saveJobBeforeRun(getProcessContext().getProcess());
        if (clearBeforeExec.getSelection()) {
            processContext.clearMessages();
        }
        // if (watchBtn.getSelection()) {
        // processContext.switchTime();
        // }
        processContext.setWatchAllowed(watchBtn.getSelection());
        processContext.setMonitorPerf(perfBtn.getSelection());
        processContext.setMonitorTrace(traceBtn.getSelection());

        processContext.setSelectedContext(contextComposite.getSelectedContext());
        processContext.exec(getShell());

        checkSaveBeforeRunSelection();

        ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
        ILaunch[] launches = manager.getLaunches();
        manager.removeLaunches(launches);
    }

    public void kill() {
        killBtn.setEnabled(false);
        setHideconsoleLine(true);
        processContext.kill();
    }

    boolean debugMode = false;

    private MenuItem debugMenuItem;

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
                        ProcessorUtilities.generateCode(processContext.getProcess(), context, false, false, true, monitor);

                        // For bug 5430, modifyed by xzhang
                        processContext.startPerformanceMonitor();

                        ILaunchConfiguration config = ((Processor) processor).getDebugConfiguration(processContext
                                .getStatisticsPort(), processContext.getTracesPort(), null);

                        // see feature 0004820: The run job doesn't verify if
                        // code is correct before launching
                        if (!JobErrorsChecker.hasErrors(ProcessComposite.this.getShell())) {

                            if (config != null) {
                                // PlatformUI.getWorkbench().
                                // getActiveWorkbenchWindow
                                // ().addPerspectiveListener(new
                                // DebugInNewWindowListener());
                                DebugUITools.launch(config, ILaunchManager.DEBUG_MODE);

                            } else {
                                MessageDialog.openInformation(getShell(), Messages.getString("ProcessDebugDialog.debugBtn"), //$NON-NLS-1$
                                        Messages.getString("ProcessDebugDialog.errortext")); //$NON-NLS-1$ 
                            }
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
                // e.printStackTrace();
                ExceptionHandler.process(e);
            } catch (InterruptedException e) {
                // e.printStackTrace();
                ExceptionHandler.process(e);
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
                                                byeMsg = "\n" //$NON-NLS-1$
                                                        + mf.format(new Object[] { processContext.getProcess().getName(),
                                                                new Date(), new Integer(process.getExitValue()) });
                                                processContext.addDebugResultToConsole(new ProcessMessage(MsgType.CORE_OUT,
                                                        byeMsg));
                                            } catch (DebugException e) {
                                                // e.printStackTrace();
                                                ExceptionHandler.process(e);
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
                                                    welcomeMsg + "\r\n"));//$NON-NLS-1$
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
                            // e.printStackTrace();
                            ExceptionHandler.process(e);
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
            IProcessMessage psMess = (IProcessMessage) evt.getNewValue();

            if (errorMessMap.size() <= CorePlugin.getDefault().getPreferenceStore()
                    .getInt(ITalendCorePrefConstants.PREVIEW_LIMIT)) {
                if (!(LanguageManager.getCurrentLanguage().equals(ECodeLanguage.PERL))) {
                    getAllErrorMess(psMess);
                } else {
                    addPerlMark(psMess);
                }
            }
            appendToConsole(psMess);
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

    protected static RunProcessContext getProcessContext() {
        return processContext;
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

    public void setDebugEnabled(boolean enabled) {
        IBrandingService brandingService = (IBrandingService) GlobalServiceRegister.getDefault().getService(
                IBrandingService.class);
        if (brandingService.getBrandingConfiguration().isAllowDebugMode()) {
            debugMenuItem.setEnabled(enabled);
        }
    }

    public void getAllErrorMess(IProcessMessage psMess) {
        if (psMess.getType().equals(MsgType.STD_ERR)) {
            String mess = psMess.getContent();
            String[] linesMess = mess.split("\n");//$NON-NLS-1$
            StringBuffer currentMess = new StringBuffer();
            String currenctJobName = processContext.getProcess().getName();
            for (int i = 0; i < linesMess.length; i++) {
                String tRunJobName = currenctJobName;
                String linemess = linesMess[i].trim(); //$NON-NLS-1$
                Pattern pattern = Pattern.compile("^Exception\\s*in\\s*component\\s*(\\w)+_\\d$");//$NON-NLS-1$
                Matcher m = pattern.matcher(linemess);
                if (m.find()) {
                    String[] allwords = linemess.split("\\s"); //$NON-NLS-1$
                    String componentName = allwords[allwords.length - 1];
                    int currentI = i;
                    if (currentI < linesMess.length - 1) {
                        do {
                            tRunJobName = linesMess[++currentI];
                        } while ((tRunJobName.lastIndexOf("(") == -1 || tRunJobName.lastIndexOf(".java") == -1)
                                && currentI < linesMess.length - 1);
                        if (tRunJobName.lastIndexOf("(") != -1 && tRunJobName.lastIndexOf(".java") != -1)
                            tRunJobName = tRunJobName.substring(tRunJobName.lastIndexOf("(") + 1, tRunJobName
                                    .lastIndexOf(".java"));
                        else
                            tRunJobName = currenctJobName;
                    }
                    if (tRunJobName != null && tRunJobName.equals(currenctJobName)) {
                        if (i == 0) {
                            errorMessMap.put(componentName, psMess);
                        } else {
                            for (int j = i; j < linesMess.length; j++) {
                                currentMess.append(linesMess[j] + "\n"); //$NON-NLS-1$
                            }
                            IProcessMessage currentProMess = new ProcessMessage(MsgType.STD_ERR, currentMess.toString());
                            errorMessMap.put(componentName, currentProMess);
                        }
                    }
                    // break;
                }

            }
        }
        refreshNode(psMess);
    }

    public void refreshNode(final IProcessMessage psMess) {
        Display.getDefault().asyncExec(new Runnable() {

            public void run() {
                org.talend.core.model.process.IProcess process = processContext.getProcess();
                List<INode> nodeList = (List<INode>) process.getGraphicalNodes();
                for (INode inode : nodeList) {
                    if (!inode.isActivate()) {
                        continue;
                    }
                    String nodeUniqueName = inode.getUniqueName();
                    if (LanguageManager.getCurrentLanguage().equals(ECodeLanguage.PERL) && Problems.nodeList.size() > 0) {
                        errorMessMap.clear();
                    }
                    if (errorMessMap.get(nodeUniqueName) != null) {
                        if (inode instanceof Node) {
                            IProcessMessage messPro = errorMessMap.get(nodeUniqueName);
                            Node node = (Node) inode;
                            node.setErrorFlag(true);
                            node.setCompareFlag(false);
                            node.setErrorInfo(messPro.getContent());
                            node.getNodeError().updateState("UPDATE_STATUS", true); //$NON-NLS-1$
                            if (node.isFileScaleComponent()) {
                                refreshProgress(psMess, node, nodeUniqueName);
                            }
                            node.setErrorInfoChange("ERRORINFO", true); //$NON-NLS-1$
                        }
                    } else {
                        if (inode instanceof Node) {
                            Node node = (Node) inode;
                            if (Problems.nodeList.size() > 0) {
                                String befor = "Error in the component's properties:";
                                Iterator<Entry<Node, StringBuffer>> set = Problems.nodeList.entrySet().iterator();
                                while (set.hasNext()) {
                                    Entry<Node, StringBuffer> en = set.next();
                                    Node no = en.getKey();
                                    String des = en.getValue().toString();
                                    if (node == no) {
                                        node.setErrorFlag(true);
                                        node.setCompareFlag(false);
                                        node.setErrorInfo(befor + des);
                                        node.getNodeError().updateState("UPDATE_STATUS", false);//$NON-NLS-1$
                                        node.setErrorInfoChange("ERRORINFO", true);//$NON-NLS-1$
                                    }
                                }
                            } else {
                                if (node.isErrorFlag() == true) {
                                    node.setErrorFlag(false);
                                    node.setCompareFlag(false);
                                    node.setErrorInfo(null);
                                    node.getNodeError().updateState("UPDATE_STATUS", false); //$NON-NLS-1$
                                }

                                if (node.isFileScaleComponent()) {
                                    refreshProgress(psMess, node, nodeUniqueName);
                                }
                                if (node.isErrorFlag() == true) {
                                    node.setErrorInfoChange("ERRORINFO", false); //$NON-NLS-1$
                                }
                            }
                        }
                    }
                }
            }

        });
    }

    public void refreshProgress(IProcessMessage psMess, Node node, String nodeUniqueName) {
        String mess = ""; //$NON-NLS-1$
        String uniqueName = ""; //$NON-NLS-1$
        String[] message = psMess.getContent().split("\n");

        for (int i = 0; i < message.length; i++) {
            if (isPattern(message[i])) {

                int firIndex = message[i].indexOf("$"); //$NON-NLS-1$
                int secIndex = message[i].indexOf("%"); //$NON-NLS-1$
                uniqueName = message[i].substring(0, firIndex);
                mess = message[i].substring(firIndex + 1, secIndex);
            }

            Double extentPro = new Double(0);
            if ((!"".equals(mess)) && mess != null) { //$NON-NLS-1$  
                extentPro = Double.parseDouble(mess);
            }

            if (((extend != extentPro) && nodeUniqueName.equals(uniqueName))) {
                node.getNodeProgressBar().updateState("UPDATE_STATUS", extentPro); //$NON-NLS-1$
                extend = extentPro;
            }

        }

    }

    private boolean isPattern(String content) {
        Pattern pattern = Pattern.compile("\\$\\s*\\d+(\\.\\d*)?%"); //$NON-NLS-1$
        Matcher m = pattern.matcher(content);
        return m.find();
    }

    private boolean isPatternFor(String content) {
        Pattern pattern = Pattern.compile("\\[\\s*\\d+(\\.\\d*)?%\\]"); //$NON-NLS-1$
        Matcher m = pattern.matcher(content);
        return m.find();
    }

    private void addPerlMark(IProcessMessage psMess) {
        if (psMess.getType().equals(MsgType.STD_ERR)) {
            String content = psMess.getContent();
            String path = null;
            String uniName = null;
            int lineNo = -1;
            Pattern errorPattern = Pattern.compile("(.*) at (\\S+) line (\\d+)[\\.,]");//$NON-NLS-1$
            Matcher m = errorPattern.matcher(content);
            String matchContent = null;

            while (m.find()) {
                path = m.group(2);
                lineNo = parseInt(m.group(3));

                matchContent = m.group();

                if ((!("".equals(path)) && path != null) && lineNo > 0) {//$NON-NLS-1$ 
                    uniName = Problems.setErrorMark(path, lineNo);
                }

                if (uniName != null) {
                    if (!errorMessMap.containsKey(uniName)) {
                        errorMessMap.put(uniName, new ProcessMessage(MsgType.STD_ERR, matchContent));
                    } else {
                        String uniMess = errorMessMap.get(uniName).getContent();
                        errorMessMap.put(uniName, new ProcessMessage(MsgType.STD_ERR, uniMess.concat(matchContent)));
                    }
                }

            }
        }
        refreshNode(psMess);
    }

    private int parseInt(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

}
