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
/**
 * 
 * DOC dev  class global comment. Detailled comment
 * <br/>
 *
 * $Id$
 *
 */
public interface IResultChangedListener {
    /**
     * 
     * DOC dev Comment method "commandChanged".
     * @param newCommand
     */
	public void commandChanged(String newCommand);
    /**
     * 
     * DOC dev Comment method "hourChanged".
     * @param newHour
     */
	public void hourChanged(String newHour);

    /**
     * 
     * DOC dev Comment method "minuteChanged".
     * @param newMinute
     */
	public void minuteChanged(String newMinute);

    /**
     * 
     * DOC dev Comment method "dayofWeekChanged".
     * @param newDayofWeek
     */
	public void dayofWeekChanged(String newDayofWeek);

    /**
     * 
     * DOC dev Comment method "dayofMonthChanged".
     * @param newdayofMonth
     */
	public void dayofMonthChanged(String newdayofMonth);

    /**
     * 
     * DOC dev Comment method "monthChanged".
     * @param newMonth
     */
	public void monthChanged(String newMonth);
}
