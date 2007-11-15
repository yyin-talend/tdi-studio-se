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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.talend.scheduler.core.ScheduleTask;
import org.talend.scheduler.core.SchedulerException;
import org.talend.scheduler.i18n.Messages;

/**
 * 
 * This class is using for displaying result composite.
 * 
 * $Id$
 * 
 */
public class DisplayResultComposite extends Composite implements IResultChangedListener {

    private static final int NUM_COLUMN = 8;

    private Text txtCustom;

    private Text txtCommand;

    private Text txtWeekday;

    private Text txtDay;

    private Text txtMonth;

    private Text txtHour;

    private Text txtMinute;

    private Button btnCopy;

    private Button customCrontabEntryButton;

    private Button rdbtnGenerated;

    public DisplayResultComposite(Composite parent, int style) {
        super(parent, style);
        init();
    }

    /**
     * 
     * Initialize.
     */
    private void init() {
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = NUM_COLUMN;
        this.setLayout(gridLayout);

        new Label(this, SWT.NONE);
        final Label lblMinute = new Label(this, SWT.NONE);
        GridData gd = new GridData();
        gd.horizontalSpan = 1;
        lblMinute.setLayoutData(gd);
        lblMinute.setText(Messages.getString("DisplayResultComposite.minuteLabel")); //$NON-NLS-1$

        final Label lblHour = new Label(this, SWT.NONE);
        lblHour.setLayoutData(new GridData());
        lblHour.setText(Messages.getString("DisplayResultComposite.hourLabel")); //$NON-NLS-1$

        final Label lblMonth = new Label(this, SWT.NONE);
        lblMonth.setLayoutData(new GridData());
        lblMonth.setText(Messages.getString("DisplayResultComposite.monthLabel")); //$NON-NLS-1$

        final Label lblDay = new Label(this, SWT.NONE);
        lblDay.setLayoutData(new GridData());
        lblDay.setText(Messages.getString("DisplayResultComposite.dayLabel")); //$NON-NLS-1$

        final Label lblWeekday = new Label(this, SWT.NONE);
        lblWeekday.setLayoutData(new GridData());
        lblWeekday.setText(Messages.getString("DisplayResultComposite.weekdayLabel")); //$NON-NLS-1$

        final Label lblCommand = new Label(this, SWT.NONE);
        lblCommand.setLayoutData(new GridData());
        lblCommand.setText(Messages.getString("DisplayResultComposite.commandLabel")); //$NON-NLS-1$
        new Label(this, SWT.NONE);

        rdbtnGenerated = new Button(this, SWT.RADIO);
        rdbtnGenerated.setLayoutData(new GridData());
        rdbtnGenerated.setText(Messages.getString("DisplayResultComposite.generatedCronTabText")); //$NON-NLS-1$
        rdbtnGenerated.setSelection(true);

        txtMinute = new Text(this, SWT.BORDER);
        txtMinute.setEditable(false);
        txtMinute.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));

        txtHour = new Text(this, SWT.BORDER);
        txtHour.setEditable(false);
        txtHour.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));

        txtMonth = new Text(this, SWT.BORDER);
        txtMonth.setEditable(false);
        txtMonth.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));

        txtDay = new Text(this, SWT.BORDER);
        txtDay.setEditable(false);
        txtDay.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));

        txtWeekday = new Text(this, SWT.BORDER);
        txtWeekday.setEditable(false);
        txtWeekday.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));

        txtCommand = new Text(this, SWT.BORDER);
        txtCommand.setEditable(false);
        final GridData gridData1 = new GridData(SWT.FILL, SWT.CENTER, true, false);
        gridData1.heightHint = 15;
        gridData1.widthHint = 130;
        txtCommand.setLayoutData(gridData1);

        btnCopy = new Button(this, SWT.NONE);
        final GridData gridData2 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 2);
        gridData2.widthHint = 53;
        btnCopy.setLayoutData(gridData2);
        btnCopy.setText(Messages.getString("DisplayResultComposite.copyButtonText")); //$NON-NLS-1$

        btnCopy.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                String data = null;
                if (customCrontabEntryButton.getSelection()) {
                    data = txtCustom.getText();

                }
                if (rdbtnGenerated.getSelection()) {
                    String tempTxt = txtCommand.getText();

                    // Checks if command is seted or not.
                    if (tempTxt == null || tempTxt.length() == 0) {
                        return;
                    }
                    String space = " "; //$NON-NLS-1$
                    data = checkString(txtMinute.getText()) + space + checkString(txtHour.getText()) + space
                            + checkString(txtDay.getText()) + space + checkString(txtMonth.getText()) + space
                            + checkString(txtWeekday.getText()) + space + txtCommand.getText();
                }

                // Clipboard board = new Clipboard(null);

                if (data != null && data.length() > 0) {
                    // board.setContents(new Object[] { data },
                    // new Transfer[] { TextTransfer.getInstance()});
                    txtCustom.setText(data);
                }
            }

            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

        });

        customCrontabEntryButton = new Button(this, SWT.RADIO);
        customCrontabEntryButton.setLayoutData(new GridData());
        customCrontabEntryButton.setText(Messages.getString("DisplayResultComposite.customCronTabText")); //$NON-NLS-1$

        customCrontabEntryButton.addSelectionListener(new SelectionAdapter() {

            /**
             * @Override
             */
            public void widgetSelected(SelectionEvent e) {
                if (customCrontabEntryButton.getSelection()) {
                    txtCustom.setEnabled(true);
                    // all the widgets are disabled
                    enableAllWidgets(false);

                } else {
                    txtCustom.setEnabled(false);
                    // all the widgets are enabled
                    enableAllWidgets(true);
                }
            }
        });

        customCrontabEntryButton.setSelection(false);

        txtCustom = new Text(this, SWT.BORDER);
        txtCustom.setEnabled(false);

        GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_CENTER);
        gridData.horizontalSpan = 6;
        gridData.widthHint = 200;

        txtCustom.setLayoutData(gridData);
    }

    List<IWidgetEnableListener> enableWidgetsLisenter = new ArrayList<IWidgetEnableListener>();

    /**
     * 
     * Adds widgetEnableListener.
     * 
     * @param l IWidgetEnableListener
     */
    public void addWidgetEnableListener(IWidgetEnableListener l) {
        enableWidgetsLisenter.add(l);
    }

    /**
     * Set the enablements of the widgets which implemented IWidgetEnableListener contained in the List
     * enableWidgetsLisenter.
     * 
     * @param enable boolean.
     */
    public void enableAllWidgets(boolean enable) {
        btnCopy.setEnabled(enable);

        for (IWidgetEnableListener listener : enableWidgetsLisenter) {
            listener.enableAll(enable);
        }
    }

    /**
     * @param newCommand String
     */
    public void commandChanged(String newCommand) {
        txtCommand.setText(newCommand);
    }

    /**
     * @param newHour String
     */
    public void hourChanged(String newHour) {
        txtHour.setText(newHour);
    }

    /**
     * @param newminute String
     */
    public void minuteChanged(String newminute) {
        txtMinute.setText(newminute);
    }

    /**
     * @param newDayofWeek String
     */
    public void dayofWeekChanged(String newDayofWeek) {
        txtWeekday.setText(newDayofWeek);
    }

    /**
     * @param newdayofMonth String
     */
    public void dayofMonthChanged(String newdayofMonth) {
        txtDay.setText(newdayofMonth);
    }

    /**
     * @param newMonth String
     */
    public void monthChanged(String newMonth) {
        txtMonth.setText(newMonth);
    }

    /**
     * Updates the propertes of task from GUI wigdet.
     * 
     * @param task ScheduleTask
     * @throws SchedulerException e
     */
    public void updateTaskProperty(ScheduleTask task) throws SchedulerException {

        if (task == null) {
            return;
        }

        if (rdbtnGenerated.getSelection()) {
            check();
            task.setCommand(txtCommand.getText());
            task.setDay(txtDay.getText());
            task.setHour(txtHour.getText());
            task.setMinute(txtMinute.getText());
            task.setMonth(txtMonth.getText());
            task.setWeekly(txtWeekday.getText());
        } else {
            checkPlainCommand();
            task.setPlainCommand(txtCustom.getText());
        }
    }

    /**
     * 
     * Checks plain command.
     * 
     * @throws SchedulerException e
     */
    private void checkPlainCommand() throws SchedulerException {
        if (txtCustom.getText() == null || txtCustom.getText().trim().equals("")) { //$NON-NLS-1$
            throw new SchedulerException(Messages.getString("DisplayResultComposite.customCronTabEmptyPrompt")); //$NON-NLS-1$
        }
    }

    /**
     * 
     * Checks if string is null or empty.
     * 
     * @param s String
     * @return String
     */
    private String checkString(String s) {
        if (s == null || s.length() == 0) {
            return "*"; //$NON-NLS-1$
        }
        return s;
    }

    /**
     * 
     * Checks the value of day, month, weekday, hour, minute.
     * 
     * @throws SchedulerException e
     */
    public void check() throws SchedulerException {
        String message = null;
        if (txtCommand.getText().equals("")) { //$NON-NLS-1$
            message = Messages.getString("DisplayResultComposite.commandEmptyPrompt"); //$NON-NLS-1$
        } else if (txtDay.getText().equals("")) { //$NON-NLS-1$
            message = Messages.getString("DisplayResultComposite.dayEmptyPrompt"); //$NON-NLS-1$
        } else if (txtMonth.getText().equals("")) { //$NON-NLS-1$
            message = Messages.getString("DisplayResultComposite.monthEmptyPrompt"); //$NON-NLS-1$
        } else if (txtWeekday.getText().equals("")) { //$NON-NLS-1$
            message = Messages.getString("DisplayResultComposite.weekdayEmptyPrompt"); //$NON-NLS-1$
        } else if (txtHour.getText().equals("")) { //$NON-NLS-1$
            message = Messages.getString("DisplayResultComposite.hourEmptyPrompt"); //$NON-NLS-1$
        } else if (txtMinute.getText().equals("")) { //$NON-NLS-1$
            message = Messages.getString("DisplayResultComposite.minuteEmptyPrompt"); //$NON-NLS-1$
        }

        if (message != null) {
            throw new SchedulerException(message);
        }
    }

    /**
     * Refreshes the GUI widget according to the task input.
     * 
     * @param task ScheduleTask
     */
    public void update(ScheduleTask task) {
        txtCommand.setText(task.getCommand());
        txtDay.setText(task.getDay());
        txtHour.setText(task.getHour());
        txtMinute.setText(task.getMinute());
        txtMonth.setText(task.getMonth());
        txtWeekday.setText(task.getWeekly());
        txtCustom.setText(task.getPlainCommand());
    }
}
