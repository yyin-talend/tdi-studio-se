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
import org.talend.scheduler.core.ScheduleTask;
import org.talend.scheduler.core.StringUtils;

public class MonthGroup extends Group
{
	ResultChangedListener changeListener = null;

	private List<Button> buttons = new ArrayList<Button>();
	
	protected final String ALL_MONTHS = "*";

	private List<String> months = new ArrayList<String>();

	public void setChangeListener(ResultChangedListener changeListener)
	{
		this.changeListener = changeListener;
	}

	private void addMonths(String month)
	{
		if (month == null)
		{
			return;
		}
		months.add(month);
	}

	private void delMonths(String month)
	{
		if (month == null)
		{
			return;
		}
		months.remove(month);
	}

	@SuppressWarnings("unchecked")
	private void displayMonths()
	{
		Collections.sort(months, new Comparator()
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
		for (Iterator it = months.iterator(); it.hasNext();)
		{
			sb.append(it.next());
			if (it.hasNext())
			{
				sb.append(",");
			}
		}
		
		changeListener.monthChanged(StringUtils.format(sb.toString()));
	}
	
	private SelectionAdapter listener = new SelectionAdapter()
	{
		public void widgetSelected(SelectionEvent e)
		{
			Button btn = (Button) e.getSource();
			// Select the button
			if (btn.getSelection())
			{
				addMonths((String) btn.getData());
				if (months.size() == 12)
				{
					months.clear();
					months.add(ALL_MONTHS);
				}
			}
			else
			{
				if (months.size() == 1 && months.get(0).equals(ALL_MONTHS))
				{
					months.clear();
					for (int i = 1; i < 13; i++)
					{
						months.add(String.valueOf(i));
					}
				}
				delMonths((String) btn.getData());
			}

			displayMonths();
		}
	};

	/**
	 * Create the composite
	 * 
	 * @param parent
	 * @param style
	 */
	public MonthGroup(Composite parent, int style)
	{
		super(parent, style);
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 5;
		setLayout(gridLayout);
		setText("Month");

		createMonthButton("January", "1");

		createMonthButton("February", "2");

		createMonthButton("March", "3");

		createMonthButton("April", "4");

		createMonthButton("May", "5");

		createMonthButton("June", "6");

		createMonthButton("July", "7");

		createMonthButton("August", "8");

		createMonthButton("September", "9");

		createMonthButton("October", "10");

		createMonthButton("November", "11");

		createMonthButton("December", "12");
	}

	private void createMonthButton(String txt, String data)
	{
		final Button btn = new Button(this, SWT.TOGGLE);
		btn.addSelectionListener(listener);
		btn.setData(data);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL, SWT.CENTER, false,
				false);
		gd.widthHint = 80;
		btn.setLayoutData(gd);
		btn.setText(txt);
		
		buttons.add(btn);
	}

	@Override
	public void dispose()
	{
		super.dispose();
	}

	@Override
	protected void checkSubclass()
	{
		// Disable the check that prevents subclassing of SWT components
	}
	
	public void update(ScheduleTask task) 
	{
		String monthString = task.getMonth();
		if(monthString == null || monthString.length() == 0)
		{
			return;
		}
		if(monthString.equals("*"))
		{
			setAllButtonSelection(true);
		}
		
		String[] months = monthString.split(",");
		for (int i = 0; i < months.length; i++)
		{
			String month = months[i];
			if(month.indexOf("-") != -1)
			{
				for(int j = Integer.valueOf(month.split("-")[0]); j<= Integer.valueOf(month.split("-")[1]); j++)
				{
					buttons.get(j - 1).setSelection(true);
                    addMonths(String.valueOf(j));
				}
			}
			else
			{
				buttons.get(Integer.valueOf(month) - 1).setSelection(true);
                addMonths(month);
			}
		}
	}

	private void setAllButtonSelection(boolean isSelection)
	{
		for(Button button : buttons)
		{
			button.setSelection(isSelection);
		}
	}
}
