package org.talend.scheduler.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.talend.scheduler.core.ScheduleTask;
import org.talend.scheduler.core.StringUtils;

public class DayOfMonthGroup extends Group
{
	private ResultChangedListener changeListener = null;

	private static final String ALL_DAYS = "*";

	private List<String> days = new ArrayList<String>();

	private List<Button> buttons = new ArrayList<Button>();

	private Button btnEveryday;

	public void setChangeListener(ResultChangedListener changeListener)
	{
		this.changeListener = changeListener;
	}

	private void addDays(String day)
	{
		if (day == null)
		{
			return;
		}
		days.add(day);
	}

	private void delDays(String day)
	{
		if (day == null)
		{
			return;
		}

		days.remove(day);
	}

	@SuppressWarnings("unchecked")
	private void displayDays()
	{
		Collections.sort(days, new Comparator()
		{
			public int compare(Object o1, Object o2)
			{
				int day1 = Integer.valueOf((String) o1).intValue();
				int day2 = Integer.valueOf((String) o2).intValue();
				if (day1 > day2)
				{
					return 1;
				}
				else if (day1 < day2)
				{
					return -1;
				}
				else
				{
					return 0;
				}
			}
		});
		
		StringBuffer sb = new StringBuffer();
		for (Iterator it = days.iterator(); it.hasNext();)
		{
			sb.append(it.next());
			if (it.hasNext())
			{
				sb.append(",");
			}
		}
		
		changeListener.dayofMonthChanged(StringUtils.format(sb.toString()));
	}
	
	/**
	 * Create the composite
	 * 
	 * @param parent
	 * @param style
	 */
	public DayOfMonthGroup(Composite parent, int style)
	{
		super(parent, style);
		setText("Day of month");
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 10;
		this.setLayout(gridLayout);

		for (int i = 1; i < 32; i++)
		{
			createDayButton(String.valueOf(i));
		}

		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);

		btnEveryday = new Button(this, SWT.TOGGLE);
		btnEveryday.setData(ALL_DAYS);
		btnEveryday.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent e)
			{
				Button btn = (Button) e.getSource();
				// Select the button
				if (btn.getSelection())
				{
					days.clear();
					addDays((String) btn.getData());
//					setAllDaysSelection(true);
				}
				else
				{
					delDays((String) btn.getData());
//					setAllDaysSelection(false);
					
					for(Button button : buttons)
					{
						if(button.getSelection())
						{
							addDays((String)button.getData());
						}
					}
				}

				displayDays();
			}

		});
		final GridData gridData_1 = new GridData(SWT.FILL, SWT.CENTER, false,
				false, 3, 1);
		gridData_1.widthHint = 58;
		btnEveryday.setLayoutData(gridData_1);
		btnEveryday.setText("Everyday");
		//
	}

	private SelectionAdapter listener = new SelectionAdapter()
	{
		public void widgetSelected(SelectionEvent e)
		{
			Button btn = (Button) e.getSource();
			// Select the button
			if (btn.getSelection())
			{
				addDays((String) btn.getData());
				if (days.size() == 31)
				{
					days.clear();
					days.add(ALL_DAYS);
					btnEveryday.setSelection(true);
				}
			}
			else
			{
				if (days.size() == 1 && days.get(0).equals(ALL_DAYS))
				{
					days.clear();
					for (int i = 1; i < 32; i++)
					{
						days.add(String.valueOf(i));
					}
				}
				delDays((String) btn.getData());
				btnEveryday.setSelection(false);
			}

			displayDays();
		}
	};

	private void createDayButton(String txt)
	{
		Button btn1 = new Button(this, SWT.TOGGLE);
		btn1.setData(txt);
		btn1.addSelectionListener(listener);
		btn1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		btn1.setText(txt);
		buttons.add(btn1);
	}

	@Override
	public void dispose()
	{
		super.dispose();
	}

	@Override
	protected void checkSubclass()
	{
	}
	
	public void update(ScheduleTask task) 
	{
		String dayString = task.getDay();
		if(dayString == null || dayString.length() == 0)
		{
			return;
		}
		if(dayString.equals("*"))
		{
			btnEveryday.setSelection(true);
		}
		
		String[] days = dayString.split(",");
		for (int i = 0; i < days.length; i++)
		{
			String day = days[i];
			if(day.indexOf("-") != -1)
			{
				for(int j = Integer.valueOf(day.split("-")[0]); j<= Integer.valueOf(day.split("-")[1]); j++)
				{
					buttons.get(j - 1).setSelection(true);
                    addDays(String.valueOf(j));
				}
			}
			else
			{
				buttons.get(Integer.valueOf(day) - 1).setSelection(true);
                addDays(day);
			}
		}
	}
}
