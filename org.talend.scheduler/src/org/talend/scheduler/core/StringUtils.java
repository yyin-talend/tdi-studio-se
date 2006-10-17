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
package org.talend.scheduler.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Detailled comment for this class. <br/>
 * $Id$
 * @author XXXXXXXX (Soyatec)
 * 
 */
public class StringUtils
{
    /**
     * format String , for example:
     * 1,2,3,4,6,8 ====> 1-4,6,8
     * 
     * @param string
     * @return
     */
    public static String format(String string)
    {
        if(string == null || string.trim().length() == 0 || string.equals("*"))
        {
            return string;
        }
        
        List<StringBuffer> sections = new ArrayList<StringBuffer>();
        String[] days = string.split(",");
        String tempDay = null;
        for (int i = 0; i < days.length; i++)
        {
            String day = days[i];
            if(tempDay != null && isConsecutiveDays(tempDay, day))
            {
                StringBuffer tempSb = getLastSectionDays(sections);
                tempSb.append(",");
                tempSb.append(day);
            }
            else
            {
                StringBuffer sb = new StringBuffer();
                sb.append(day);
                sections.add(sb);
            }
            tempDay = day;
        }
        
        StringBuffer sb = new StringBuffer();
        for(StringBuffer section : sections)
        {
            String[] sectionDays = section.toString().split(",");
            if(sectionDays.length > 2)
            {
                sb.append(sectionDays[0]);
                sb.append("-");
                sb.append(sectionDays[sectionDays.length - 1]);
            }
            else
            {
                sb.append(section);
            }
            sb.append(",");
        }
        
        if(sb.length() > 1)
        {
            return sb.substring(0, sb.length() - 1);
        }
        
        return sb.toString(); 
    }
    
    private static StringBuffer getLastSectionDays(List sections)
    {
        if(sections.size() == 0)
        {
            return new StringBuffer();
        }
        else
        {
            return (StringBuffer) sections.get(sections.size() - 1);
        }
    }

    /**
     * check if two days are consecutive
     * 
     * @param tempDay
     * @param day
     * @return
     */
    private static boolean isConsecutiveDays(String tempDay, String day)
    {
        if(Integer.valueOf(day) - Integer.valueOf(tempDay) == 1)
        {
            return true;
        }
        return false;
    }
}
