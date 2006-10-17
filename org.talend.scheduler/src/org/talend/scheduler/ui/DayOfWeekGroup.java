package org.talend.scheduler.ui;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import org.talend.scheduler.core.ScheduleTask;

public class DayOfWeekGroup extends Group {
	ResultChangedListener changeListener = null;

	public void setChangeListener(ResultChangedListener changeListener) {
		this.changeListener = changeListener;
	}
	/**
	 * Create the composite
	 * 
	 * @param parent
	 * @param style
	 */
	ArrayList<Button> buttons=new ArrayList<Button>();
	int buttonStyle = SWT.TOGGLE;
	
	class WeekDayButtonListener extends SelectionAdapter {
		int selectedButton[] = new int[7];
		WeekDayButtonListener() {

			for (int j = 0; j < selectedButton.length; j++)
				selectedButton[j] = 38;
		}

		public void widgetSelected(SelectionEvent e) {

			Iterator it=buttons.iterator();
			while(it.hasNext()){
				Button b1=(Button)it.next();
				int tmp=Integer.parseInt((String)b1.getData());
				if(b1.getSelection()){
					selectedButton[tmp]=tmp;
				}else{
					selectedButton[tmp]=38;
				}
			}
			boolean firstFlag = true;
			String cronRunningDays = "";
			if("0123456".equalsIgnoreCase(""+selectedButton[0]+
					selectedButton[1]+selectedButton[2]+
					selectedButton[3]+selectedButton[4]+
					selectedButton[5]+selectedButton[6]))
				cronRunningDays="*";
			else{
			for (int i = 0; i < selectedButton.length; i++) {
				if (selectedButton[i] != 38) {
					if (firstFlag) {
						firstFlag = false;
						cronRunningDays += selectedButton[i];
					} else {
						cronRunningDays += "," + selectedButton[i];
					}
				}
			}
			}
			changeListener.dayofWeekChanged(cronRunningDays);
		}

	}

	public DayOfWeekGroup(Composite parent, int style) {
		super(parent, style);
		setText("Day of week");
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		setLayout(gridLayout);
		
		createButton("Monday","1");
		
		createButton("Tuesday","2");
		
		createButton("Wednesday","3");
		
		createButton("Thursday","4");
		
		createButton("Friday","5");
		
		createButton("Saturday","6");
		
		createButton("Sunday","0");
		
	}

	private void createButton(String txt, String data)
	{
		final Button btn = new Button(this, SWT.TOGGLE);
		btn.addSelectionListener(new WeekDayButtonListener());
		btn.setData(data);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL, SWT.CENTER, false,
				false);
		gd.widthHint = 80;
		btn.setLayoutData(gd);
		btn.setText(txt);
		buttons.add(btn);
	}
	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	public void update(ScheduleTask task) {
		// TODO Auto-generated method stub
		
		String cronRunningDays=task.getWeekly();
		if(cronRunningDays.equalsIgnoreCase("*")){
			
		}else{
		  while(cronRunningDays!=null&&cronRunningDays.length()!=0){
			int douIndex=cronRunningDays.indexOf(",");
  			int tmp=0;
  			if(douIndex==-1)
  			{
  				tmp=Integer.parseInt(cronRunningDays);
  				cronRunningDays="";
  				
  			}else{
  				tmp=Integer.parseInt(cronRunningDays.substring(0, douIndex));
  				cronRunningDays=cronRunningDays.substring(douIndex+1);
  			}
		    }
		}
	}
	

}
