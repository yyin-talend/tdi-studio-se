// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the  agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//   
// ============================================================================
package org.talend.scheduler.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Detailled comment for this class. <br/> $Id$
 * 
 * @author phou
 * 
 */
public class StringUtils {

    /**
     * format String , for example: 1,2,3,4,6,8 ====> 1-4,6,8.
     * 
     * @param string
     * @return
     */
    public static String format(String string) {
        if (string == null || string.trim().length() == 0 || string.equals("*")) { //$NON-NLS-1$
            return string;
        }

        String delimiter = ","; //$NON-NLS-1$

        List<StringBuffer> sections = new ArrayList<StringBuffer>();
        String[] days = string.split(delimiter);
        String tempDay = null;
        for (int i = 0; i < days.length; i++) {
            String day = days[i];
            if (tempDay != null && isConsecutiveDays(tempDay, day)) {
                StringBuffer tempSb = getLastSectionDays(sections);
                tempSb.append(delimiter);
                tempSb.append(day);
            } else {
                StringBuffer sb = new StringBuffer();
                sb.append(day);
                sections.add(sb);
            }
            tempDay = day;
        }

        StringBuffer sb = new StringBuffer();
        for (StringBuffer section : sections) {
            String[] sectionDays = section.toString().split(delimiter);
            if (sectionDays.length > 2) {
                sb.append(sectionDays[0]);
                sb.append("-"); //$NON-NLS-1$
                sb.append(sectionDays[sectionDays.length - 1]);
            } else {
                sb.append(section);
            }
            sb.append(delimiter);
        }

        if (sb.length() > 1) {
            return sb.substring(0, sb.length() - 1);
        }

        return sb.toString();
    }

    private static StringBuffer getLastSectionDays(List sections) {
        if (sections.size() == 0) {
            return new StringBuffer();
        } else {
            return (StringBuffer) sections.get(sections.size() - 1);
        }
    }

    /**
     * check if two days are consecutive.
     * 
     * @param tempDay
     * @param day
     * @return
     */
    private static boolean isConsecutiveDays(String tempDay, String day) {
        if (Integer.valueOf(day) - Integer.valueOf(tempDay) == 1) {
            return true;
        }
        return false;
    }
}
