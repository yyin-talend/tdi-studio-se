package org.talend.scheduler.ui;

public interface ResultChangedListener {
	public void commandChanged(String newCommand);

	public void hourChanged(String newHour);

	public void minuteChanged(String newMinute);

	public void dayofWeekChanged(String newDayofWeek);

	public void dayofMonthChanged(String newdayofMonth);

	public void monthChanged(String newMonth);
}
