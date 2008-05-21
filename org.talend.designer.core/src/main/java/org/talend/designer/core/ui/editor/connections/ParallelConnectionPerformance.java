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
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

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

    private static final String COLOR_FINISHED = "#229922";

    private static final String COLOR_RUNNING = "#AA3322";

    private List<IPerformanceData> perfDataList = new ArrayList<IPerformanceData>();

    /**
     * store the ids of different exec for current row link.
     */
    private Set<String> execIdSet = new TreeSet<String>();

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
        perfDataList.clear();
        execIdSet.clear();
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
            // set the label location
            offset = new Point(0, -70);
            // has format as row1.72, means have parallel execution
            String connectionId = data.getConnectionId();
            String execId = connectionId.substring(connectionId.indexOf('.') + 1);
            execIdSet.add(execId);
            perfDataList.add(data);

            // get 4 latest update performance data
            int start = perfDataList.size() - 4;
            if (start < 0) {
                start = 0;
            }
            StringBuilder builder = new StringBuilder(1024);
            for (int i = perfDataList.size() - 1; i >= start; i--) {
                builder.append(process(perfDataList.get(i)));
            }
            // check if there are more than 4 process executing
            if (execIdSet.size() > 4) {
                String execString = "exec";
                if (execIdSet.size() > 5) {
                    execString = "execs";
                }
                builder.append(String.format("<font color='#AA3322'>             %1$s other %2$s   </font>",
                        execIdSet.size() - 4, execString));
            }

            // update label
            String oldLabel = label;
            label = builder.toString();
            firePropertyChange(LABEL_PROP, oldLabel, label);
        }
    }

    /**
     * DOC hcw Comment method "process".
     * 
     * @param data
     */
    private String process(IPerformanceData data) {
        String connectionId = data.getConnectionId();
        String execId = connectionId.substring(connectionId.indexOf('.') + 1);
        long lineCount = data.getLineCount();
        long processingTime = data.getProcessingTime();
        double avg = processingTime > 0 ? lineCount * 1000.0 / processingTime : 0.0;
        String color = COLOR_RUNNING;
        String pattern = "<font color='%1$s'>exec %2$s : %3$5d rows, %4$5.0f rows/second</font><br>";
        if (data.getAction().equals(IPerformanceData.ACTION_STOP)) {
            color = COLOR_FINISHED;
        }
        return String.format(pattern, color, execId, lineCount, avg);
    }
}
