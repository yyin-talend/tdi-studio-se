// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
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
