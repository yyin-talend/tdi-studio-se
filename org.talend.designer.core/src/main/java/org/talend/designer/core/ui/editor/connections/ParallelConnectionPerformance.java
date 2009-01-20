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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.eclipse.draw2d.geometry.Point;
import org.talend.core.model.process.EConnectionType;
import org.talend.designer.core.DesignerPlugin;
import org.talend.designer.runprocess.IPerformanceData;
import org.talend.designer.runprocess.IRunProcessService;

/**
 * Control the statistical message that display on row link for parallel execution, if no parallel execution existed,
 * just delegate to super class.
 */
public class ParallelConnectionPerformance extends ConnectionPerformance {

    /**
     * only display 4 line of execution.
     */
    private static final int DISPLAY_LIMIT = 4;

    private static final String COLOR_FINISHED = "#229922"; //$NON-NLS-1$

    private static final String COLOR_RUNNING = "#AA3322"; //$NON-NLS-1$

    /**
     * store the ids of exec that will be displayed on current row link.
     */
    private List<String> displayedExecutionId = new ArrayList<String>(DISPLAY_LIMIT);

    /**
     * store the ids of exec that have already stopped.
     */
    private Set<String> stoppeddExecutionId = new HashSet<String>();

    /**
     * store the ids of exec that are running now.
     */
    private Set<String> runningExecutionId = new HashSet<String>();

    /**
     * store entry of connetionId and IPerformanceData.
     */
    private Map<String, IPerformanceData> performanceDataMap = new HashMap<String, IPerformanceData>();

    /**
     * ParallelConnectionPerformance constructor.
     * 
     * @param conn
     */
    public ParallelConnectionPerformance(Connection conn) {
        super(conn);
    }

    @Override
    public void resetStatus() {
        displayedExecutionId.clear();
        stoppeddExecutionId.clear();
        performanceDataMap.clear();
        runningExecutionId.clear();
    }

    @Override
    public void setLabel(String msg) {
        IRunProcessService service = DesignerPlugin.getDefault().getRunProcessService();
        IPerformanceData data = service.createPerformanceData(msg);

        if (StringUtils.isEmpty(msg) || !connection.getLineStyle().equals(EConnectionType.FLOW_MAIN)
                || data.getConnectionId().indexOf('.') < 0) {
            // if message has format as row1, handle by super class
            super.setLabel(msg);

        } else {
            // has format as row1.72, means have parallel execution
            String connectionId = data.getConnectionId();
            String execId = connectionId.substring(connectionId.indexOf('.') + 1);

            performanceDataMap.put(execId, data);
            if (data.getAction().equals(IPerformanceData.ACTION_START)) {
                runningExecutionId.add(execId);
            } else if (data.getAction().equals(IPerformanceData.ACTION_STOP)) {
                stoppeddExecutionId.add(execId);
                runningExecutionId.remove(execId);
            }

            displayIfPossible(execId, data);

            StringBuilder builder = new StringBuilder(1024);
            for (String id : displayedExecutionId) {
                builder.append(constructLabel(performanceDataMap.get(id)));
            }

            // check if there are other exec running that cannot display on row link.
            checkOtherRunningExecution(builder);

            // set the label location
            offset = computeLabelOffset();

            // update label
            String oldLabel = label;
            label = builder.toString();
            firePropertyChange(LABEL_PROP, oldLabel, label);
        }
    }

    /**
     * DOC hcw Comment method "checkOtherRunningExecution".
     * 
     * @param builder
     */
    private void checkOtherRunningExecution(StringBuilder builder) {
        int otherExecution = runningExecutionId.size() - DISPLAY_LIMIT;
        if (otherExecution > 0) {
            String execString = "exec"; //$NON-NLS-1$
            if (otherExecution > 1) {
                execString = "execs"; //$NON-NLS-1$
            }
            builder.append(String.format("<font color='#AA3322'>             %1$s other %2$s   </font>", otherExecution, //$NON-NLS-1$
                    execString));
        }
    }

    /**
     * DOC hcw Comment method "computeLabelOffset".
     * 
     * @return
     */
    private Point computeLabelOffset() {
        int size = performanceDataMap.keySet().size();
        if (size > DISPLAY_LIMIT) {
            size = DISPLAY_LIMIT;
        }
        if (runningExecutionId.size() > DISPLAY_LIMIT) {
            size++;
        }
        return new Point(0, -14 * size);
    }

    private void displayIfPossible(String execId, IPerformanceData data) {
        if (displayedExecutionId.contains(execId)) {
            // if the execution has already been displayed, do nothing.
            return;
        }

        if (displayedExecutionId.size() < DISPLAY_LIMIT) {
            // will be displayed on label
            displayedExecutionId.add(execId);
        } else {
            // If we can find a finished execution that is already displayed, replace it with this new data. Otherwise
            // the new data will not be displayed.
            for (int i = 0; i < displayedExecutionId.size(); i++) {
                String id = displayedExecutionId.get(i);
                if (stoppeddExecutionId.contains(id)) {
                    // replace the finished execution with new data
                    displayedExecutionId.set(i, execId);
                    return;
                }
            }
        }
    }

    /**
     * Construct a label from IPerformanceData, which will be displayed on current link.
     * 
     * @param data
     */
    private String constructLabel(IPerformanceData data) {
        String connectionId = data.getConnectionId();
        String execId = connectionId.substring(connectionId.indexOf('.') + 1);
        long lineCount = data.getLineCount();
        long processingTime = data.getProcessingTime();
        double avg = processingTime > 0 ? lineCount * 1000.0 / processingTime : 0.0;
        String color = COLOR_RUNNING;
        String pattern = "<font color='%1$s'>exec %2$s : %3$5d rows, %4$5.0f rows/second</font><br>"; //$NON-NLS-1$
        if (data.getAction().equals(IPerformanceData.ACTION_STOP)) {
            color = COLOR_FINISHED;
        }
        return String.format(pattern, color, execId, lineCount, avg);
    }
}
