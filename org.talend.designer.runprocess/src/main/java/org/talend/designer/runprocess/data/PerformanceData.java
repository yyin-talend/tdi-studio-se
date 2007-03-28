// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2007 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License.
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
package org.talend.designer.runprocess.data;

import org.talend.designer.runprocess.IPerformanceData;

/**
 * Describes process performance data. <br/>
 * 
 * $Id$
 * 
 */
public final class PerformanceData implements IPerformanceData {

    /** Field separator. */
    private static final String FIELD_SEP = "|"; //$NON-NLS-1$

    /** Data string to be parsed. */
    private String data;

    /**
     * Constructs a new PerformanceData.
     * 
     * @param data Data string to be parsed.
     */
    public PerformanceData(final String data) {
        super();

        this.data = data;
    }

    public String getNodeId() {
        int sepIndex = data.indexOf(FIELD_SEP);
        return sepIndex != -1 ? data.substring(0, sepIndex) : null;
    }

    public long getLineCount() {
        int sepIndex1 = data.indexOf(FIELD_SEP);
        int sepIndex2 = sepIndex1 != -1 ? data.indexOf(FIELD_SEP, sepIndex1 + 1) : -1;
        long lineCount;
        if (sepIndex2 != -1) {
            String lineCountStr = data.substring(sepIndex1 + 1, sepIndex2);
            try {
                lineCount = Long.parseLong(lineCountStr);
            } catch (NumberFormatException nfe) {
                lineCount = 0;
            }
        } else {
            lineCount = 0;
        }
        return lineCount;
    }

    public long getProcessingTime() {
        int sepIndex1 = data.indexOf(FIELD_SEP);
        int sepIndex2 = sepIndex1 != -1 ? data.indexOf(FIELD_SEP, sepIndex1 + 1) : -1;
        int sepIndex3 = sepIndex2 != -1 ? data.indexOf(FIELD_SEP, sepIndex2 + 1) : -1;
        if (sepIndex3 == -1 && sepIndex2 != -1) {
            sepIndex3 = data.length();
        }
        long time;
        if (sepIndex3 != -1) {
            String timeStr = data.substring(sepIndex2 + 1, sepIndex3);
            try {
                time = Long.parseLong(timeStr);
            } catch (NumberFormatException nfe) {
                time = 0;
            }
        } else {
            time = 0;
        }
        return time;
    }

    public String getAction() {
        if (data == null) {
            return ACTION_CLEAR;
        }
        if (data.equals("")) { //$NON-NLS-1$
            return ACTION_CLEAR;
        }
        int sepIndex1 = data.indexOf(FIELD_SEP);
        int sepIndex2 = sepIndex1 != -1 ? data.indexOf(FIELD_SEP, sepIndex1 + 1) : -1;
        int sepIndex3 = sepIndex2 != -1 ? data.indexOf(FIELD_SEP, sepIndex2 + 1) : -1;
        String action = sepIndex3 != -1 ? data.substring(sepIndex3 + 1, data.length()) : ACTION_PERF;
        return action;
    }
}
