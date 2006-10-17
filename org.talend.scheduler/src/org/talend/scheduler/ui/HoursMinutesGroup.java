package org.talend.scheduler.ui;


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

public class HoursMinutesGroup extends Group {
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
	
	final Label hoursLabel = new Label(this, SWT.NONE);
	final Label amLabel = new Label(this, SWT.NONE);
	final Button hoursButton_0 = new Button(this, SWT.TOGGLE);
	final Button hoursButton_1 = new Button(this, SWT.TOGGLE);
	final Button hoursButton_2 = new Button(this, SWT.TOGGLE);
	final Button hoursButton_3 = new Button(this, SWT.TOGGLE);
	final Button hoursButton_4 = new Button(this, SWT.TOGGLE);
	final Button hoursButton_5 = new Button(this, SWT.TOGGLE);
	final Button hoursButton_6 = new Button(this, SWT.TOGGLE);
	final Button hoursButton_7 = new Button(this, SWT.TOGGLE);
	final Button hoursButton_8 = new Button(this, SWT.TOGGLE);
	final Button hoursButton_9 = new Button(this, SWT.TOGGLE);
	final Button hoursButton_10 = new Button(this, SWT.TOGGLE);
	final Button hoursButton_11 = new Button(this, SWT.TOGGLE);
	final Label pmLabel = new Label(this, SWT.NONE);
	
	final Button hoursButton_12 = new Button(this, SWT.TOGGLE);
	final Button hoursButton_13 = new Button(this, SWT.TOGGLE);
	final Button hoursButton_14 = new Button(this, SWT.TOGGLE);
	final Button hoursButton_15 = new Button(this, SWT.TOGGLE);
	final Button hoursButton_16 = new Button(this, SWT.TOGGLE);
	final Button hoursButton_17 = new Button(this, SWT.TOGGLE);
	final Button hoursButton_18 = new Button(this, SWT.TOGGLE);
	final Button hoursButton_19 = new Button(this, SWT.TOGGLE);
	final Button hoursButton_20 = new Button(this, SWT.TOGGLE);
	final Button hoursButton_21 = new Button(this, SWT.TOGGLE);
	final Button hoursButton_22 = new Button(this, SWT.TOGGLE);
	final Button hoursButton_23 = new Button(this, SWT.TOGGLE);
	
	final Label label = new Label(this, SWT.NONE);
	{
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
	}
	final Label minutesLabel = new Label(this, SWT.NONE);
	{
		new Label(this, SWT.NONE);
	}
	final Button minutesButton_0 = new Button(this, SWT.TOGGLE);
	final Button minutesButton_5 = new Button(this, SWT.TOGGLE);
	final Button minutesButton_10 = new Button(this, SWT.TOGGLE);
	final Button minutesButton_15 = new Button(this, SWT.TOGGLE);
	final Button minutesButton_20 = new Button(this, SWT.TOGGLE);
	final Button minutesButton_25 = new Button(this, SWT.TOGGLE);
	final Button minutesButton_30 = new Button(this, SWT.TOGGLE);
	final Button minutesButton_35 = new Button(this, SWT.TOGGLE);
	final Button minutesButton_40 = new Button(this, SWT.TOGGLE);
	final Button minutesButton_45 = new Button(this, SWT.TOGGLE);
	final Button minutesButton_50 = new Button(this, SWT.TOGGLE);
	final Button minutesButton_55 = new Button(this, SWT.TOGGLE);

	
	public HoursMinutesGroup(Composite parent, int style) {
		super(parent, style);
		setText("Hours/minutes");
		final GridLayout gridLayout_1 = new GridLayout();
		gridLayout_1.verticalSpacing = 4;
		gridLayout_1.marginTop = 10;
		gridLayout_1.marginLeft = 15;
		gridLayout_1.marginHeight = 8;
		gridLayout_1.marginBottom = 10;
		gridLayout_1.horizontalSpacing = 6;
		gridLayout_1.numColumns = 14;
		setLayout(gridLayout_1);
	
		hoursLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 2));
		hoursLabel.setText("Hours");
		
		amLabel.setText("AM");
		
		pmLabel.setText("PM");
		
		
		class HoursButtonListener extends SelectionAdapter {
			int selectedButtons[] = new int[24];

			HoursButtonListener() {
				for (int j = 0; j < selectedButtons.length; j++)
					selectedButtons[j] = 38;
			}

			public void widgetSelected(SelectionEvent e) {
				
				if (hoursButton_0.getSelection())
					selectedButtons[0] = 0;
				else
					selectedButtons[0] = 38;
				if (hoursButton_1.getSelection())
					selectedButtons[1] = 1;
				else
					selectedButtons[1] = 38;
				if (hoursButton_2.getSelection())
					selectedButtons[2] = 2;
				else
					selectedButtons[2] = 38;
				if (hoursButton_3.getSelection())
					selectedButtons[3] = 3;
				else
					selectedButtons[3] = 38;
				if (hoursButton_4.getSelection())
					selectedButtons[4] = 4;
				else
					selectedButtons[4] = 38;
				if (hoursButton_5.getSelection())
					selectedButtons[5] = 5;
				else
					selectedButtons[5] = 38;
				if (hoursButton_6.getSelection())
					selectedButtons[6] = 6;
				else
					selectedButtons[6] = 38;
				if (hoursButton_7.getSelection())
					selectedButtons[7] = 7;
				else
					selectedButtons[7] = 38;
				if (hoursButton_8.getSelection())
					selectedButtons[8] = 8;
				else
					selectedButtons[8] = 38;
				if (hoursButton_9.getSelection())
					selectedButtons[9] = 9;
				else
					selectedButtons[9] = 38;
				if (hoursButton_10.getSelection())
					selectedButtons[10] = 10;
				else
					selectedButtons[10] = 38;
				if (hoursButton_11.getSelection())
					selectedButtons[11] = 11;
				else
					selectedButtons[11] = 38;
				if (hoursButton_12.getSelection())
					selectedButtons[12] = 12;
				else
					selectedButtons[12] = 38;
				if (hoursButton_13.getSelection())
					selectedButtons[13] = 13;
				else
					selectedButtons[13] = 38;
				if (hoursButton_14.getSelection())
					selectedButtons[14] = 14;
				else
					selectedButtons[14] = 38;
				if (hoursButton_15.getSelection())
					selectedButtons[15] = 15;
				else
					selectedButtons[15] = 38;
				if (hoursButton_16.getSelection())
					selectedButtons[16] = 16;
				else
					selectedButtons[16] = 38;
				if (hoursButton_17.getSelection())
					selectedButtons[17] = 17;
				else
					selectedButtons[17] = 38;
				if (hoursButton_18.getSelection())
					selectedButtons[18] = 18;
				else
					selectedButtons[18] = 38;
				if (hoursButton_19.getSelection())
					selectedButtons[19] = 19;
				else
					selectedButtons[19] = 38;
				if (hoursButton_20.getSelection())
					selectedButtons[20] = 20;
				else
					selectedButtons[20] = 38;
				if (hoursButton_21.getSelection())
					selectedButtons[21] = 21;
				else
					selectedButtons[21] = 38;
				if (hoursButton_22.getSelection())
					selectedButtons[22] = 22;
				else
					selectedButtons[22] = 38;
				if (hoursButton_23.getSelection())
					selectedButtons[23] = 23;

				else
					selectedButtons[23] = 38;
				
				
				boolean firstFlag = true;
				boolean segmentFlag = true;
				String cronRunningHours = "";
				int start = -1;
				int end = 0;
				for (int j = 0; j < selectedButtons.length; j++) {
					if (selectedButtons[j] != 38) {

						if (firstFlag) {
							firstFlag = false;
							start = j;
							end = j;
							if (j != 23)
								continue;
						} else
							end = j;
					}
					if (start == 0 && end == 23)
						cronRunningHours = "*";
					else {
						if (!firstFlag && start >= end) {

							firstFlag = true;
							if (segmentFlag) {
								segmentFlag = false;
								cronRunningHours += selectedButtons[start];
							} else
								cronRunningHours += ","
										+ selectedButtons[start];
						} else if (!firstFlag && (end < j || end == 23)) {

							if (segmentFlag) {
								segmentFlag = false;
								cronRunningHours += start + "-" + end;
							} else
								cronRunningHours += "," + start + "-" + end;
							firstFlag = true;
						}
					}
				}
				changeListener.hourChanged(cronRunningHours);

			}

		}

		hoursButton_0.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false));
		hoursButton_0.setText("0");
		hoursButton_0.addSelectionListener(new HoursButtonListener());

		hoursButton_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false));
		hoursButton_1.setText("1");
		hoursButton_1.addSelectionListener(new HoursButtonListener());

		hoursButton_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false));
		hoursButton_2.setText("2");
		hoursButton_2.addSelectionListener(new HoursButtonListener());

		hoursButton_3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false));
		hoursButton_3.setText("3");
		hoursButton_3.addSelectionListener(new HoursButtonListener());

		hoursButton_4.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false));
		hoursButton_4.setText("4");
		hoursButton_4.addSelectionListener(new HoursButtonListener());

		hoursButton_5.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false));
		hoursButton_5.setText("5");
		hoursButton_5.addSelectionListener(new HoursButtonListener());

		hoursButton_6.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false));
		hoursButton_6.setText("6");
		hoursButton_6.addSelectionListener(new HoursButtonListener());

		hoursButton_7.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false));
		hoursButton_7.setText("7");
		hoursButton_7.addSelectionListener(new HoursButtonListener());

		hoursButton_8.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false));
		hoursButton_8.setText("8");
		hoursButton_8.addSelectionListener(new HoursButtonListener());

		hoursButton_9.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false));
		hoursButton_9.setText("9");
		hoursButton_9.addSelectionListener(new HoursButtonListener());

		hoursButton_10.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false));
		hoursButton_10.setText("10");
		hoursButton_10.addSelectionListener(new HoursButtonListener());

		hoursButton_11.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false));
		hoursButton_11.setText("11");
		hoursButton_11.addSelectionListener(new HoursButtonListener());

		hoursButton_12.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false));
		hoursButton_12.setText("12");
		hoursButton_12.addSelectionListener(new HoursButtonListener());

		hoursButton_13.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false));
		hoursButton_13.setText("13");
		hoursButton_13.addSelectionListener(new HoursButtonListener());

		hoursButton_14.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false));
		hoursButton_14.setText("14");
		hoursButton_14.addSelectionListener(new HoursButtonListener());

		hoursButton_15.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false));
		hoursButton_15.setText("15");
		hoursButton_15.addSelectionListener(new HoursButtonListener());

		hoursButton_16.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false));
		hoursButton_16.setText("16");
		hoursButton_16.addSelectionListener(new HoursButtonListener());

		hoursButton_17.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false));
		hoursButton_17.setText("17");
		hoursButton_17.addSelectionListener(new HoursButtonListener());

		hoursButton_18.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false));
		hoursButton_18.setText("18");
		hoursButton_18.addSelectionListener(new HoursButtonListener());

		hoursButton_19.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false));
		hoursButton_19.setText("19");
		hoursButton_19.addSelectionListener(new HoursButtonListener());

		hoursButton_20.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false));
		hoursButton_20.setText("20");
		hoursButton_20.addSelectionListener(new HoursButtonListener());

		hoursButton_21.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false));
		hoursButton_21.setText("21");
		hoursButton_21.addSelectionListener(new HoursButtonListener());

		hoursButton_22.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false));
		hoursButton_22.setText("22");
		hoursButton_22.addSelectionListener(new HoursButtonListener());

		hoursButton_23.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false));
		hoursButton_23.setText("23");
		hoursButton_23.addSelectionListener(new HoursButtonListener());
		
		minutesLabel.setText("Minutes");

		class MinutesButtonListener extends SelectionAdapter {
			int selectedButton[] = new int[12];

			MinutesButtonListener() {
				for (int j = 0; j < selectedButton.length; j++)
					selectedButton[j] = 38;
			}

			public void widgetSelected(SelectionEvent e) {
				boolean firstFlag = true;
				if (minutesButton_0.getSelection())
					selectedButton[0] = 0;
				else
					selectedButton[0] = 38;
				if (minutesButton_5.getSelection())
					selectedButton[1] = 5;
				else
					selectedButton[1] = 38;
				if (minutesButton_10.getSelection())
					selectedButton[2] = 10;
				else
					selectedButton[2] = 38;
				if (minutesButton_15.getSelection())
					selectedButton[3] = 15;
				else
					selectedButton[3] = 38;
				if (minutesButton_20.getSelection())
					selectedButton[4] = 20;
				else
					selectedButton[4] = 38;
				if (minutesButton_25.getSelection())
					selectedButton[5] = 25;
				else
					selectedButton[5] = 38;
				if (minutesButton_30.getSelection())
					selectedButton[6] = 30;
				else
					selectedButton[6] = 38;
				if (minutesButton_35.getSelection())
					selectedButton[7] = 35;
				else
					selectedButton[7] = 38;
				if (minutesButton_40.getSelection())
					selectedButton[8] = 40;
				else
					selectedButton[8] = 38;
				if (minutesButton_45.getSelection())
					selectedButton[9] = 45;
				else
					selectedButton[9] = 38;
				if (minutesButton_50.getSelection())
					selectedButton[10] = 50;
				else
					selectedButton[10] = 38;
				if (minutesButton_55.getSelection())
					selectedButton[11] = 55;
				else
					selectedButton[11] = 38;
				String cronRunningMinutes = "";
				for (int i = 0; i < selectedButton.length; i++) {
					if (selectedButton[i] != 38) {
						if (firstFlag) {
							firstFlag = false;
							cronRunningMinutes += selectedButton[i];
						} else {
							cronRunningMinutes += "," + selectedButton[i];
						}
					}
				}
				changeListener.minuteChanged(cronRunningMinutes);
			}
		}

		minutesButton_0.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false));
		minutesButton_0.addSelectionListener(new MinutesButtonListener());
		minutesButton_0.setText("0");

		minutesButton_5.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false));
		minutesButton_5.setText("5");
		minutesButton_5.addSelectionListener(new MinutesButtonListener());

		minutesButton_10.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				false, false));
		minutesButton_10.addSelectionListener(new MinutesButtonListener());
		minutesButton_10.setText("10");

		minutesButton_15.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				false, false));
		minutesButton_15.addSelectionListener(new MinutesButtonListener());
		minutesButton_15.setText("15");

		minutesButton_20.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				false, false));
		minutesButton_20.addSelectionListener(new MinutesButtonListener());
		minutesButton_20.setText("20");

		minutesButton_25.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				false, false));
		minutesButton_25.addSelectionListener(new MinutesButtonListener());
		minutesButton_25.setText("25");

		minutesButton_30.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				false, false));
		minutesButton_30.addSelectionListener(new MinutesButtonListener());
		minutesButton_30.setText("30");

		minutesButton_35.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				false, false));
		minutesButton_35.addSelectionListener(new MinutesButtonListener());
		minutesButton_35.setText("35");

		minutesButton_40.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				false, false));
		minutesButton_40.addSelectionListener(new MinutesButtonListener());
		minutesButton_40.setText("40");

		minutesButton_45.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				false, false));
		minutesButton_45.addSelectionListener(new MinutesButtonListener());
		minutesButton_45.setText("45");

		minutesButton_50.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				false, false));
		minutesButton_50.addSelectionListener(new MinutesButtonListener());
		minutesButton_50.setText("50");

		minutesButton_55.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				false, false));
		minutesButton_55.addSelectionListener(new MinutesButtonListener());
		minutesButton_55.setText("55");
		//
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
		updateSelectedMinutesButtons(task.getMinute());
		updateSelectedHoursButtons(task.getHour());
	}

	private void updateSelectedMinutesButtons(String cronRunningMinutes){
		
		while(cronRunningMinutes!=null&&cronRunningMinutes.length()!=0){
			int douIndex=cronRunningMinutes.indexOf(",");
  			int tmp=0;
  			if(douIndex==-1)
  			{
  				tmp=Integer.parseInt(cronRunningMinutes);
  				cronRunningMinutes="";
  				
  			}else{
  				tmp=Integer.parseInt(cronRunningMinutes.substring(0, douIndex));
  				cronRunningMinutes=cronRunningMinutes.substring(douIndex+1);
  			}
  				switch(tmp){
  				case 0:{
  					minutesButton_0.setSelection(true); break;
  				}
  				case 5:{
  					minutesButton_5.setSelection(true); break;
  				}
  				case 10:{
  					minutesButton_10.setSelection(true); break;
  				}
  				case 15:{
  					minutesButton_15.setSelection(true); break;
  				}
  				case 20:{
  					minutesButton_20.setSelection(true); break;
  				}
  				case 25:{
  					minutesButton_25.setSelection(true); break;
  				}
  				case 30:{
  					minutesButton_30.setSelection(true); break;
  				}
  				case 35:{
  					minutesButton_35.setSelection(true); break;
  				}
  				case 40:{
  					minutesButton_40.setSelection(true); break;
  				}
  				case 45:{
  					minutesButton_45.setSelection(true); break;
  				}
  				case 50:{
  					minutesButton_50.setSelection(true); break;
  				}
  				
  				case 55:{
  					minutesButton_55.setSelection(true); break;
  				}
  				default:break;
  				}
		}
	}
	private void updateSelectedHoursButtons(String cronRunningHours){
		if(cronRunningHours.equalsIgnoreCase("*")){
			hoursButton_0.setSelection(true);
			hoursButton_1.setSelection(true);
			hoursButton_2.setSelection(true);
			hoursButton_3.setSelection(true);
			hoursButton_4.setSelection(true);
			hoursButton_5.setSelection(true);
			hoursButton_6.setSelection(true);
			hoursButton_7.setSelection(true);
			hoursButton_8.setSelection(true);
			hoursButton_9.setSelection(true);
			hoursButton_10.setSelection(true);
			hoursButton_11.setSelection(true);
			hoursButton_12.setSelection(true);
			hoursButton_13.setSelection(true);
			hoursButton_14.setSelection(true);
			hoursButton_15.setSelection(true);
			hoursButton_16.setSelection(true);
			hoursButton_17.setSelection(true);
			hoursButton_18.setSelection(true);
			hoursButton_19.setSelection(true);
			hoursButton_20.setSelection(true);
			hoursButton_21.setSelection(true);
			hoursButton_22.setSelection(true);
			hoursButton_23.setSelection(true);
			
		}else{
			
		}
	}
	
}

