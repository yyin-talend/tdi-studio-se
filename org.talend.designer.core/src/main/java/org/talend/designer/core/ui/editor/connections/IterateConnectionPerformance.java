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

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.talend.designer.runprocess.IPerformanceData;

/**
 * Control the statistical message that display on iterate link.
 */
public class IterateConnectionPerformance extends ConnectionPerformance {

    /**
     * 
     */
    private static final String COLOR_FINISHED = "#229922";

    /**
     * 
     */
    private static final String COLOR_RUNNING = "#AA3322";

    /**
     * store the ids of exec that have already stopped.
     */
    private Set<String> stoppeddExecutionId = new HashSet<String>();

    /**
     * store the ids of exec that are running now.
     */
    private Set<String> runningExecutionId = new HashSet<String>();

    /**
     * DOC hcw IterateConnectionPerformance constructor comment.
     * 
     * @param conn
     */
    public IterateConnectionPerformance(Connection conn) {
        super(conn);
    }

    @Override
    public void resetStatus() {
        stoppeddExecutionId.clear();
        runningExecutionId.clear();
    }

    @Override
    public void setLabel(String msg) {
        if (StringUtils.isEmpty(msg)) {
            // handle by super class
            super.setLabel(msg);
            return;
        }
        String[] part = msg.split("\\|");
        if (part != null && part.length == 3) {
            // update process status
            if (part[2].equals(IPerformanceData.ACTION_START)) {
                runningExecutionId.add(part[1]);
            } else if (part[2].equals(IPerformanceData.ACTION_STOP)) {
                stoppeddExecutionId.add(part[1]);
                runningExecutionId.remove(part[1]);
            }
            // update label
            String oldLabel = label;
            label = createHtmlText();
            firePropertyChange(LABEL_PROP, oldLabel, label);

        } else if (part != null && part.length == 2) { // iterate1.0|exec0, it means running.
            runningExecutionId.add(part[1]);

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
        String color = COLOR_RUNNING;
        String execString = "exec running";
        if (runningExecutionId.size() > 0) {
            execString = "execs running";
            html.append(String.format(pattern, color, runningExecutionId.size(), execString));
        }

        color = COLOR_FINISHED;
        execString = "exec finished";
        if (stoppeddExecutionId.size() > 0) {
            execString = "execs finished";
            html.append(String.format(pattern, color, stoppeddExecutionId.size(), execString));
        }

        return html.toString();
    }

}
