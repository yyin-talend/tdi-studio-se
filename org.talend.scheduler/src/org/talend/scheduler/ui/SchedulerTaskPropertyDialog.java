package org.talend.scheduler.ui;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.talend.scheduler.core.ScheduleTask;

public class SchedulerTaskPropertyDialog extends Dialog {

	public enum DialogType {
		ADD, MODIFY
	}

	DialogType dialogType = DialogType.ADD;

	private ScheduleTask task;

	private CommandModeGroup commandGroup;

	private DisplayResultComposite resultComposite;

	private HoursMinutesGroup hourMinGroup;

	private DayOfWeekGroup dayofWeekGroup;

	private MonthGroup monthGroup;

	private DayOfMonthGroup dayofMonth;

	/**
	 * Create a new task
	 * 
	 * @param parentShell
	 */
	public SchedulerTaskPropertyDialog(Shell parentShell) {
		super(parentShell);
		dialogType = DialogType.ADD;
	}

	/**
	 * Used for updating a existing task
	 * 
	 * @param parentShell
	 * @param task
	 */
	public SchedulerTaskPropertyDialog(Shell parentShell, ScheduleTask task) {
		super(parentShell);
		this.task = task;
		dialogType = DialogType.MODIFY;
	}

	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		// Set the title bar text
		if (dialogType == DialogType.ADD) {
			shell.setText("TOS - Talend Scheduler - Add a task");
		} else {
			shell.setText("TOS - Talend Scheduler - Modify a task");
		}
		
	}

	@Override
	protected void setShellStyle(int newShellStyle) {
		super.setShellStyle(newShellStyle | SWT.RESIZE);
	}

	/**
	 * Create contents of the dialog
	 * 
	 * @param parent
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

		commandGroup.setChangeListener(resultComposite);

		hourMinGroup.setChangeListener(resultComposite);

		dayofWeekGroup.setChangeListener(resultComposite);

		monthGroup.setChangeListener(resultComposite);

		dayofMonth.setChangeListener(resultComposite);

		updateUI();

		return container;
	}

	/**
	 * Refresh the UI according to the input task
	 */
	private void updateUI() {
		if (task == null) {
			return;
		}

		commandGroup.update(task);
		hourMinGroup.update(task);
		dayofWeekGroup.update(task);
		monthGroup.update(task);
		dayofMonth.update(task);
		resultComposite.update(task);
	}

	@Override
	protected void okPressed() {
		String checkResult = resultComposite.check();
		if (checkResult != null) {

			MessageDialog.openError(this.getShell(), "Warn", checkResult);

			return;
		}

		if (task == null) {
			task = new ScheduleTask();
		}
		resultComposite.updateTaskProperty(task);
		super.okPressed();
	}

	public ScheduleTask getResult() {
		return task;
	}

	private void createResultComposite(Composite container) {
		resultComposite = new DisplayResultComposite(container, SWT.NONE);
		final GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.horizontalSpan = 2;
		resultComposite.setLayoutData(gridData);

	}

	private void createCommandGroup(Composite container) {
		commandGroup = new CommandModeGroup(container, SWT.NONE);
		final GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 2;
		commandGroup.setLayoutData(gridData);
	}

	private void createDayofmonthGroup(Composite container) {
		dayofMonth = new DayOfMonthGroup(container, SWT.NONE);
		final GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		dayofMonth.setLayoutData(gridData);
	}

	private void createMonthGroup(Composite container) {
		monthGroup = new MonthGroup(container, SWT.NONE);
		final GridData gridData = new GridData(GridData.FILL_HORIZONTAL
				| GridData.VERTICAL_ALIGN_FILL);
		monthGroup.setLayoutData(gridData);
	}

	private void createDayofweekGroup(Composite container) {
		dayofWeekGroup = new DayOfWeekGroup(container, SWT.NONE);
		final GridData gridData = new GridData(GridData.FILL_HORIZONTAL
				| GridData.VERTICAL_ALIGN_FILL);
		dayofWeekGroup.setLayoutData(gridData);
	}

	private void createHourminuteGroup(Composite container) {
		hourMinGroup = new HoursMinutesGroup(container, SWT.NONE);
		final GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		hourMinGroup.setLayoutData(gridData);
	}

	/**
	 * Create contents of the button bar
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		if (dialogType == DialogType.ADD) {
			createButton(parent, IDialogConstants.OK_ID, "Add this entry", true);
		} else {
			createButton(parent, IDialogConstants.OK_ID, "Modify this entry",
					true);
		}
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	class CommandModeGroup extends Group {

		ResultChangedListener changeListener = null;

		private Text text;

		public void setChangeListener(ResultChangedListener changeListener) {
			this.changeListener = changeListener;
		}

		public void update(ScheduleTask task) {
			text.setText(task.getCommand());

		}

		public CommandModeGroup(Composite parent, int style) {
			super(parent, style);
			this.setText("Command");

			final GridLayout gridLayout_1 = new GridLayout();
			gridLayout_1.numColumns = 3;
			this.setLayout(gridLayout_1);

			final Label label = new Label(this, SWT.NONE);
			label.setText("Command");

			text = new Text(this, SWT.BORDER);
			final GridData gridData_1 = new GridData(SWT.FILL, SWT.CENTER,
					true, false);
			gridData_1.widthHint = 525;
			text.setLayoutData(gridData_1);
			text.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					if (changeListener == null) {
						return;
					}
					changeListener.commandChanged(text.getText());
				}
			});

			final Button button = new Button(this, SWT.NONE);
			button.setText("...");
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
		}

		public void updateFileName(String newFileName) {
			String cmd = text.getText();
			int index = cmd.indexOf(">>");
			if (index != -1) {
				cmd = cmd.substring(0, index);
			}
			cmd = cmd.trim();
			cmd += " >> " + newFileName;
			text.setText(cmd);
		}

		protected void checkSubclass() {
		}
	}

}
