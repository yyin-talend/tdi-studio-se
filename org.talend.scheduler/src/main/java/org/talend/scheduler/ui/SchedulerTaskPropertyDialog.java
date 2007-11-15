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
package org.talend.scheduler.ui;

import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.talend.designer.core.model.utils.emf.talendfile.JobType;
import org.talend.designer.runprocess.ProcessorUtilities;
import org.talend.scheduler.SchedulerPlugin;
import org.talend.scheduler.core.CommandModeType;
import org.talend.scheduler.core.ExceptionHandler;
import org.talend.scheduler.core.ScheduleTask;
import org.talend.scheduler.core.SchedulerException;
import org.talend.scheduler.core.TalendJobManager;
import org.talend.scheduler.i18n.Messages;

/**
 * A Scheduler Task property dialog. $Id: SchedulerTaskPropertyDialog.java 292 2006-11-02 04:31:28 +0000 (jeu., 02 nov.
 * 2006) nrousseau $
 */
public class SchedulerTaskPropertyDialog extends Dialog {

    /** Enum to distinguish Dialog's type add or modify. */
    public enum DialogType {
        ADD,
        MODIFY
    }

    DialogType dialogType = DialogType.ADD;

    private ScheduleTask task;

    private CommandModeComposite commandComposite;

    private DisplayResultComposite resultComposite;

    private HoursMinutesGroup hourMinGroup;

    private DayOfWeekGroup dayofWeekGroup;

    private MonthGroup monthGroup;

    private DayOfMonthGroup dayofMonth;

    /**
     * Create a new task.
     * 
     * @param parentShell
     */
    public SchedulerTaskPropertyDialog(Shell parentShell) {
        super(parentShell);
        dialogType = DialogType.ADD;
    }

    /**
     * Used for updating a existing task.
     * 
     * @param parentShell
     * @param task
     */
    public SchedulerTaskPropertyDialog(Shell parentShell, ScheduleTask task) {
        super(parentShell);
        this.task = task;
        dialogType = DialogType.MODIFY;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
     */
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        // Set the title bar text
        if (dialogType == DialogType.ADD) {
            shell.setText(Messages.getString("SchedulerTaskPropertyDialog.addTaskPrompt")); //$NON-NLS-1$
        } else {
            shell.setText(Messages.getString("SchedulerTaskPropertyDialog.modifyTaskPrompt")); //$NON-NLS-1$
        }
    }

    /**
     * @Override
     */
    protected void setShellStyle(int newShellStyle) {
        super.setShellStyle(newShellStyle | SWT.RESIZE);
    }

    /*
     * Create contents of the dialog.
     * 
     * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite) super.createDialogArea(parent);
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        container.setLayout(gridLayout);

        createCommandGroup(container);
        createDayofmonthGroup(container);

        createMonthGroup(container);
        createDayofweekGroup(container);
        createHourminuteGroup(container);

        createResultComposite(container);

        commandComposite.setChangeListener(resultComposite);

        hourMinGroup.setChangeListener(resultComposite);

        dayofWeekGroup.setChangeListener(resultComposite);

        monthGroup.setChangeListener(resultComposite);

        dayofMonth.setChangeListener(resultComposite);

        resultComposite.addWidgetEnableListener(commandComposite);
        resultComposite.addWidgetEnableListener(hourMinGroup);
        resultComposite.addWidgetEnableListener(dayofWeekGroup);
        resultComposite.addWidgetEnableListener(monthGroup);
        resultComposite.addWidgetEnableListener(dayofMonth);

        resultComposite.enableAllWidgets(true);
        commandComposite.refreshTalendJobCommand();

        updateUI();

        return container;
    }

    /**
     * Refresh the UI according to the input task.
     */
    private void updateUI() {
        if (task == null) {
            return;
        }

        commandComposite.update(task);
        hourMinGroup.update(task);
        dayofWeekGroup.update(task);
        monthGroup.update(task);
        dayofMonth.update(task);
        resultComposite.update(task);

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#okPressed()
     */
    @Override
    protected void okPressed() {

        if (task == null) {
            task = new ScheduleTask();
        }
        try {
            resultComposite.updateTaskProperty(task);
        } catch (SchedulerException e) {
            ExceptionHandler.handleErrorWithDialog(this.getShell(), e);
            return;
            // MessageDialog.openError(this.getShell(), "Warn", e.getMessage());
        }

        if (commandComposite.getCommandMode() == CommandModeType.TalendJob) {
            commandComposite.updateTaskProperty(task);
        } else if (commandComposite.getCommandMode() == CommandModeType.Crontab) {
            task.setContext(null);
            task.setProject(null);
            task.setJob(null);
        }
        super.okPressed();
    }

    /**
     * Gets the schduler task added or modified.
     * 
     * @return ScheduleTask
     */
    public ScheduleTask getResult() {
        return task;
    }

    /**
     * Creates the result coposite .
     * 
     * @param container Composite
     */
    private void createResultComposite(Composite container) {
        resultComposite = new DisplayResultComposite(container, SWT.NONE);
        final GridData gridData = new GridData(GridData.FILL_BOTH);
        gridData.horizontalSpan = 2;
        resultComposite.setLayoutData(gridData);

    }

    /**
     * Creates the command coposite .
     * 
     * @param container Composite
     */
    private void createCommandGroup(Composite container) {
        commandComposite = new CommandModeComposite(container, SWT.NONE);
        final GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.horizontalSpan = 2;
        commandComposite.setLayoutData(gridData);
    }

    /**
     * Creates the Dayofmonth coposite .
     * 
     * @param container Composite
     */
    private void createDayofmonthGroup(Composite container) {
        dayofMonth = new DayOfMonthGroup(container, SWT.NONE);
        final GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        dayofMonth.setLayoutData(gridData);
    }

    /**
     * Creates the Month coposite .
     * 
     * @param container Composite
     */
    private void createMonthGroup(Composite container) {
        monthGroup = new MonthGroup(container, SWT.NONE);
        final GridData gridData = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_FILL);
        monthGroup.setLayoutData(gridData);
    }

    /**
     * Creates the week coposite .
     * 
     * @param container Composite
     */
    private void createDayofweekGroup(Composite container) {
        dayofWeekGroup = new DayOfWeekGroup(container, SWT.NONE);
        final GridData gridData = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_FILL);
        dayofWeekGroup.setLayoutData(gridData);
    }

    /**
     * Creates the Hour/Minutes coposite .
     * 
     * @param container Composite
     */
    private void createHourminuteGroup(Composite container) {
        hourMinGroup = new HoursMinutesGroup(container, SWT.NONE);
        final GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        hourMinGroup.setLayoutData(gridData);
    }

    /**
     * Create contents of the button bar.
     * 
     * @param parent Composite
     */
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        if (dialogType == DialogType.ADD) {
            createButton(parent, IDialogConstants.OK_ID, Messages
                    .getString("SchedulerTaskPropertyDialog.addTaskButton"), true); //$NON-NLS-1$
        } else {
            createButton(parent, IDialogConstants.OK_ID, Messages
                    .getString("SchedulerTaskPropertyDialog.modifyTaskButton"), true); //$NON-NLS-1$
        }
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
    }

    /**
     * The Command mode composite,it contains a tab folder with command and Talend job tab.
     * 
     * @author qianbing
     * 
     */
    class CommandModeComposite extends Composite implements IWidgetEnableListener {

        IResultChangedListener changeListener = null;

        private Text text;

        private Combo contextCombo;

        private Combo jobCombo;

        private Combo projectCombo;

        private Button button;

        private TabFolder tf;

        private TalendJobManager jobManager;

        /**
         * Sets the changeListener.
         * 
         * @param changeListener IResultChangedListener
         */
        public void setChangeListener(IResultChangedListener changeListener) {
            this.changeListener = changeListener;
        }

        /**
         * DOC qianbing Comment method "updateTaskProperty".
         * 
         * @param taskInput ScheduleTask
         */
        public void updateTaskProperty(ScheduleTask taskInput) {
            taskInput.setJob(jobCombo.getText());
            taskInput.setContext(contextCombo.getText());
            taskInput.setProject(projectCombo.getText());

            String[] splitedJobName = taskInput.getJob().split(String.valueOf(IPath.SEPARATOR));
            List<JobType> runJobs = jobManager.getRunJobs(splitedJobName[splitedJobName.length - 1]);
            if (runJobs != null) {
                taskInput.setSubJobs(runJobs.toArray(new JobType[runJobs.size()]));
            }

            // generated the code of jobs.
            ProcessorUtilities.generateCode(splitedJobName[splitedJobName.length - 1], contextCombo.getText(), false,
                    false);
        }

        /**
         * Update the command text or the combo by the input ScheduleTasks.
         * 
         * @param taskInput ScheduleTask
         */
        public void update(ScheduleTask taskInput) {
            if (taskInput.getTaskMode() == CommandModeType.Crontab) {
                tf.setSelection(CommandModeType.Crontab.ordinal());
                text.setText(taskInput.getCommand());

            } else {
                tf.setSelection(CommandModeType.TalendJob.ordinal());
                projectCombo.setText(taskInput.getProject());
                jobCombo.setText(taskInput.getJob());
                contextCombo.setText(taskInput.getContext());
            }
        }

        public CommandModeComposite(Composite parent, int style) {
            super(parent, style);
            createAreaContent();
        }

        /**
         * Gets the command mode type.
         * 
         * @return CommandModeType
         */
        public CommandModeType getCommandMode() {
            if (tf.getSelectionIndex() == CommandModeType.Crontab.ordinal()) {
                return CommandModeType.Crontab;
            } else {
                return CommandModeType.TalendJob;
            }
        }

        /**
         * Creates the content area.
         */
        private void createAreaContent() {

            GridLayout layout = new GridLayout();
            this.setLayout(layout);

            tf = new TabFolder(this, SWT.NONE);
            GridData gd = new GridData(GridData.FILL_HORIZONTAL);
            tf.setLayoutData(gd);

            // Begin modify the defect 0000218: Default value : "Talend Jobs tab" and "Generated Cron Tab entry"
            // selected
            TabItem tab2 = new TabItem(tf, SWT.BORDER);
            tab2.setText(Messages.getString("SchedulerTaskPropertyDialog.talendJobText")); //$NON-NLS-1$
            tab2.setControl(createTalendCommand(tf));
            // End modify the defect 0000218: Default value : "Talend Jobs tab" and "Generated Cron Tab entry" selected

            TabItem tab1 = new TabItem(tf, SWT.BORDER);
            tab1.setText(Messages.getString("SchedulerTaskPropertyDialog.commandLabel")); //$NON-NLS-1$
            tab1.setControl(createCrontabCommand(tf));

            tf.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent e) {

                    refreshResultCompositeByTab();

                }
            });
        }

        /**
         * DOC dev Comment method "refreshResultCompositeByTab".
         */
        protected void refreshResultCompositeByTab() {

            if (tf.getSelectionIndex() == CommandModeType.Crontab.ordinal()) {
                // selects crontab command tab
                refreshCommandDiaplay(text.getText());
            } else {
                refreshTalendJobCommand();
            }
        }

        /**
         * Creates Talend Command composite.
         * 
         * @param parent Composite
         * @return Composite
         */
        private Composite createTalendCommand(Composite parent) {
            Composite container = new Composite(parent, SWT.NONE);
            GridLayout layout = new GridLayout();
            layout.numColumns = 6;
            layout.horizontalSpacing = 20;
            container.setLayout(layout);

            Label label = new Label(container, SWT.NONE);
            label.setText(Messages.getString("SchedulerTaskPropertyDialog.projectLabel")); //$NON-NLS-1$

            projectCombo = new Combo(container, SWT.PUSH);

            GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
            // gridData_1.widthHint = 525;
            projectCombo.setLayoutData(gd);

            label = new Label(container, SWT.NONE);
            label.setText(Messages.getString("SchedulerTaskPropertyDialog.jobLabel")); //$NON-NLS-1$

            jobCombo = new Combo(container, SWT.PUSH);

            gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
            // gridData_1.widthHint = 525;
            jobCombo.setLayoutData(gd);

            label = new Label(container, SWT.NONE);
            label.setText(Messages.getString("SchedulerTaskPropertyDialog.contextLabel")); //$NON-NLS-1$

            contextCombo = new Combo(container, SWT.PUSH);

            gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
            // gridData_1.widthHint = 525;
            contextCombo.setLayoutData(gd);

            initailCombo();

            ModifyListener genCommandListener = new ModifyListener() {

                public void modifyText(ModifyEvent e) {
                    refreshTalendJobCommand();
                }

            };

            projectCombo.addModifyListener(genCommandListener);
            jobCombo.addModifyListener(genCommandListener);
            contextCombo.addModifyListener(genCommandListener);

            ModifyListener syncJobAndContextListener = new ModifyListener() {

                public void modifyText(ModifyEvent e) {
                    String jobName = jobCombo.getText();
                    jobName = jobName.substring(jobName.lastIndexOf("/") + 1); //$NON-NLS-1$

                    List<String> contextList = jobManager.updateContextList(jobName);

                    contextCombo.setItems(contextList.toArray(new String[contextList.size()]));

                    contextCombo.select(0);

                }
            };

            jobCombo.addModifyListener(syncJobAndContextListener);

            return container;
        }

        /**
         * Refreshes talend Job Command.
         */
        private void refreshTalendJobCommand() {
            try {
                String command = jobManager.getCommandByTalendJob(projectCombo.getText(), jobCombo.getText(),
                        contextCombo.getText());
                refreshCommandDiaplay(command);
            } catch (Exception ex) {
                SchedulerPlugin.log(ex);
            }
        }

        /**
         * Initial the status of the project combo,job combo and context combo.
         */
        void initailCombo() {
            jobManager = new TalendJobManager();

            projectCombo.setItems(new String[] { (jobManager.getCurrentProject().getLabel()) });
            projectCombo.select(0);

            List<String> jobs = jobManager.getCurrentProjectJobs();
            if (jobs.isEmpty()) {
                return;
            }
            String[] jobItems = jobs.toArray(new String[jobs.size()]);

            jobCombo.setItems(jobItems);

            if (dialogType.equals(DialogType.ADD)) {

                jobCombo.select(0);

                String selectedJobName = jobItems[0];
                selectedJobName = selectedJobName.substring(selectedJobName.lastIndexOf("/") + 1); //$NON-NLS-1$
                // change context's value
                List<String> contextList = jobManager.updateContextList(selectedJobName);
                contextCombo.setItems(contextList.toArray(new String[contextList.size()]));
                contextCombo.select(0);

            }

            if (dialogType.equals(DialogType.MODIFY)) {

                if (task.getTaskMode() != CommandModeType.TalendJob) {
                    return;
                }
                String selectedJobName = task.getJob();

                jobCombo.setText(selectedJobName);

                selectedJobName = selectedJobName.substring(selectedJobName.lastIndexOf("/") + 1); //$NON-NLS-1$

                // change context's value
                List<String> contextList = jobManager.updateContextList(selectedJobName);
                contextCombo.setItems(contextList.toArray(new String[contextList.size()]));

                String selectedContextName = task.getContext();
                contextCombo.setText(selectedContextName);
            }
        }

        /**
         * Creates Crontab Command composite.
         * 
         * @param parent Composite
         * @return Composite
         */
        private Composite createCrontabCommand(Composite parent) {
            Composite container = new Composite(parent, SWT.NONE);
            GridLayout layout = new GridLayout();
            layout.numColumns = 3;
            container.setLayout(layout);

            final Label label = new Label(container, SWT.NONE);
            label.setText(Messages.getString("SchedulerTaskPropertyDialog.commandLabel")); //$NON-NLS-1$

            text = new Text(container, SWT.BORDER);
            final GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
            gd.widthHint = 525;
            text.setLayoutData(gd);
            text.addModifyListener(new ModifyListener() {

                public void modifyText(ModifyEvent e) {
                    refreshCommandDiaplay(text.getText());
                }
            });

            button = new Button(container, SWT.NONE);
            button.setText("..."); //$NON-NLS-1$
            button.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent e) {
                    FileDialog fd = new FileDialog(getParent().getShell());
                    String fileName = fd.open();
                    if (fileName == null) {
                        return;
                    }
                    updateFileName(fileName);
                }
            });
            return container;
        }

        /**
         * When command changes,refresh the displaying of the command field.
         * 
         * @param msg String
         */
        private void refreshCommandDiaplay(String msg) {
            if (changeListener == null) {
                return;
            }
            changeListener.commandChanged(msg);
        }

        /**
         * Caculates the file's name.
         * 
         * @param newFileName String
         */
        public void updateFileName(String newFileName) {
            String cmd = text.getText();
            int index = cmd.indexOf(">>"); //$NON-NLS-1$
            if (index != -1) {
                cmd = cmd.substring(0, index);
            }
            cmd = cmd.trim();
            cmd += " >> " + newFileName; //$NON-NLS-1$
            text.setText(cmd);
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.swt.widgets.Composite#checkSubclass()
         */
        protected void checkSubclass() {
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.scheduler.ui.IWidgetEnableListener#enableAll(boolean)
         */
        public void enableAll(boolean enabled) {
            button.setEnabled(enabled);
            text.setEnabled(enabled);
            contextCombo.setEnabled(enabled);
            jobCombo.setEnabled(enabled);
            projectCombo.setEnabled(enabled);
        }
    }

}
