package org.talend.scheduler.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.talend.scheduler.core.ScheduleTask;

/**
 * 
 * @author Tang Fengneng
 * 
 */
public class DisplayResultComposite extends Composite implements
		ResultChangedListener {

	private Text txtCustom;

	private Text txtCommand;

	private Text txtWeekday;

	private Text txtDay;

	private Text txtMonth;

	private Text txtHour;

	private Text txtMinute;

	private Button btnCopy;

	private Button customCrontabEntryButton;

	public DisplayResultComposite(Composite parent, int style) {
		super(parent, style);
		init();
	}

	private void init() {
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 8;
		this.setLayout(gridLayout);

		new Label(this, SWT.NONE);
		final Label lblMinute = new Label(this, SWT.NONE);
		GridData gd = new GridData();
		gd.horizontalSpan = 1;
		lblMinute.setLayoutData(gd);
		lblMinute.setText("Minute");

		final Label lblHour = new Label(this, SWT.NONE);
		lblHour.setLayoutData(new GridData());
		lblHour.setText("Hour");

		final Label lblMonth = new Label(this, SWT.NONE);
		lblMonth.setLayoutData(new GridData());
		lblMonth.setText("Month");

		final Label lblDay = new Label(this, SWT.NONE);
		lblDay.setLayoutData(new GridData());
		lblDay.setText("Day");

		final Label lblWeekday = new Label(this, SWT.NONE);
		lblWeekday.setLayoutData(new GridData());
		lblWeekday.setText("Weekday");

		final Label lblCommand = new Label(this, SWT.NONE);
		lblCommand.setLayoutData(new GridData());
		lblCommand.setText("Command");
		new Label(this, SWT.NONE);

		final Button rdbtnGenerated = new Button(this, SWT.RADIO);
		rdbtnGenerated.setLayoutData(new GridData());
		rdbtnGenerated.setText("Generated CronTab entry");
		rdbtnGenerated.setSelection(true);

		txtMinute = new Text(this, SWT.BORDER);
		txtMinute.setEditable(false);
		txtMinute
				.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		txtHour = new Text(this, SWT.BORDER);
		txtHour.setEditable(false);
		txtHour.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		txtMonth = new Text(this, SWT.BORDER);
		txtMonth.setEditable(false);
		txtMonth.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		txtDay = new Text(this, SWT.BORDER);
		txtDay.setEditable(false);
		txtDay.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		txtWeekday = new Text(this, SWT.BORDER);
		txtWeekday.setEditable(false);
		txtWeekday
				.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		txtCommand = new Text(this, SWT.BORDER);
		txtCommand.setEditable(false);
		final GridData gridData_1 = new GridData(SWT.FILL, SWT.CENTER, true,
				false);
		gridData_1.heightHint = 15;
		gridData_1.widthHint = 130;
		txtCommand.setLayoutData(gridData_1);

		btnCopy = new Button(this, SWT.NONE);
		final GridData gridData_2 = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 2);
		gridData_2.widthHint = 53;
		btnCopy.setLayoutData(gridData_2);
		btnCopy.setText("Copy");

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
					String space = " ";
					data = checkString(txtMinute.getText()) + space
							+ checkString(txtHour.getText()) + space
							+ checkString(txtDay.getText()) + space
							+ checkString(txtMonth.getText()) + space
							+ checkString(txtWeekday.getText()) + space
							+ txtCommand.getText();
				}

				Clipboard board = new Clipboard(null);

				if (data != null && data.length() > 0) {
					board.setContents(new Object[] { data },
							new Transfer[] { TextTransfer.getInstance() });
				}

			}

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

		});

		customCrontabEntryButton = new Button(this, SWT.RADIO);
		customCrontabEntryButton.setLayoutData(new GridData());
		customCrontabEntryButton.setText("Custom crontab entry");

		txtCustom = new Text(this, SWT.BORDER);
		txtCustom.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				6, 1));
	}

	public void commandChanged(String newCommand) {
		txtCommand.setText(newCommand);
	}

	public void hourChanged(String newHour) {
		txtHour.setText(newHour);
	}

	public void minuteChanged(String newminute) {
		txtMinute.setText(newminute);
	}

	public void dayofWeekChanged(String newDayofWeek) {
		txtWeekday.setText(newDayofWeek);
	}

	public void dayofMonthChanged(String newdayofMonth) {
		txtDay.setText(newdayofMonth);
	}

	public void monthChanged(String newMonth) {
		txtMonth.setText(newMonth);
	}

	public void updateTaskProperty(ScheduleTask task) {
		task.setCommand(txtCommand.getText());
		task.setDay(txtDay.getText());
		task.setHour(txtHour.getText());
		task.setMinute(txtMinute.getText());
		task.setMonth(txtMonth.getText());
		task.setWeekly(txtWeekday.getText());
	}

	private String checkString(String s) {
		if (s == null || s.length() == 0) {
			return "*";
		}
		return s;
	}

	public String check() {
		if (txtCommand.getText().equals("")) {
			return "Command can not be empty.";
		} else if (txtDay.getText().equals("")) {
			return "Day can not be empty.";
		} else if (txtMonth.getText().equals("")) {
			return ("Month can not be empty.");
		} else if (txtWeekday.getText().equals("")) {
			return ("Weekday can not be empty.");
		} else if (txtHour.getText().equals("")) {
			return ("Hour can not be empty.");
		} else if (txtMinute.getText().equals("")) {
			return ("Minute can not be empty.");
		}

		return null;
	}

	public void update(ScheduleTask task) {
		txtCommand.setText(task.getCommand());
		txtDay.setText(task.getDay());
		txtHour.setText(task.getHour());
		txtMinute.setText(task.getMinute());
		txtMonth.setText(task.getMonth());
		txtWeekday.setText(task.getMonth());
	}

}