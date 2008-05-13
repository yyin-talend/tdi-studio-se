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
package org.talend.designer.core.ui.editor.connections;

import org.apache.commons.lang.StringUtils;
import org.talend.designer.runprocess.IPerformanceData;

/**
 * Control the statistical message that display on iterate link.
 */
public class IterateConnectionPerformance extends ConnectionPerformance {

    /**
     * The number of process that is running.
     */
    private int runningProcess = 0;

    /**
     * The number of process that is finished.
     */
    private int finishedProcess = 0;

    /**
     * DOC hcw IterateConnectionPerformance constructor comment.
     * 
     * @param conn
     */
    public IterateConnectionPerformance(Connection conn) {
        super(conn);
    }

    @Override
    public void setLabel(String msg) {
        if (StringUtils.isEmpty(msg)) {
            // handle by super class
            super.setLabel(msg);
            return;
        }
        String[] part = msg.split("|");
        if (part != null && part.length == 3) {
            // update process status
            if (part.equals(IPerformanceData.ACTION_START)) {
                runningProcess++;
            } else if (part.equals(IPerformanceData.ACTION_STOP)) {
                finishedProcess++;
            }
            // update label
            String oldLabel = label;
            label = createHtmlText();
            firePropertyChange(LABEL_PROP, oldLabel, label);
        }
    }

    /**
     * DOC hcw Comment method "createHtmlText".
     * 
     * @return
     */
    private String createHtmlText() {
        StringBuilder html = new StringBuilder(150);

        String pattern = "<font color='%1$s'>%2$s %3$s</font><br>";
        String color = "#AA3322";
        String execString = "exec running";
        if (runningProcess > 1) {
            execString = "execs running";
        }
        html.append(String.format(pattern, color, runningProcess, execString));

        color = "#229922";
        execString = "exec finished";
        if (finishedProcess > 1) {
            execString = "execs finished";
        }
        html.append(String.format(pattern, color, finishedProcess, execString));
        return html.toString();
    }

}
