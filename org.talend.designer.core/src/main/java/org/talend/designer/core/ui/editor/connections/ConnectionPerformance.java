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

import java.text.MessageFormat;
import java.util.Locale;

import org.eclipse.draw2d.geometry.Point;
import org.talend.core.model.process.Element;
import org.talend.designer.core.DesignerPlugin;
import org.talend.designer.runprocess.IPerformanceData;
import org.talend.designer.runprocess.IRunProcessService;

/**
 * Model part of connection performance.
 * 
 * yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: ConnectionPerformance.java 下午02:22:20 2007-6-8 +0000 (2007-6-8) yzhang $
 * 
 */
public class ConnectionPerformance extends Element {

    public static final String LABEL_PROP = "label.prop";

    private String label = "";

    private Point offset;

    private Connection connection;

    /**
     * Constructor.
     * 
     * yzhang ConnectionPerformance constructor comment.
     */
    public ConnectionPerformance(Connection conn) {
        connection = conn;
        offset = new Point(0, -30);
    }

    /**
     * Sets the label.
     * 
     * @param label the label to set
     */
    public void setLabel(String msg) {
        String oldData = this.label;
        if (!oldData.equals(msg)) {
            label = htmlFromPerformance(msg);
            firePropertyChange(LABEL_PROP, oldData, label);
        }
    }

    /**
     * Getter for label.
     * 
     * @return the label
     */
    public String getLabel() {
        return this.label;
    }

    /**
     * Get offset.
     * 
     * yzhang Comment method "getLocation".
     * 
     * @return
     */
    public Point getOffset() {
        return offset;
    }

    /**
     * Format performance to a simple HTML.
     * 
     * @param data Performance data to be rendered.
     * @return HTML string.
     */
    private String htmlFromPerformance(String data) {
        StringBuffer html = new StringBuffer();

        IRunProcessService service = DesignerPlugin.getDefault().getRunProcessService();
        IPerformanceData perf = service.createPerformanceData(data);

        if (IPerformanceData.ACTION_PERF.equals(perf.getAction())) {
            final String perfPattern = "<font color=''#4477BB''>" + "{0, number,#} rows - {1,number,#.##}s<br>" //$NON-NLS-1$ //$NON-NLS-2$
                    + "<b>{2,number,#.##} rows/s</b>" + "</font>"; //$NON-NLS-1$ //$NON-NLS-2$
            long lineCount = perf.getLineCount();
            long processingTime = perf.getProcessingTime();
            double avg = processingTime > 0 ? (double) lineCount * 1000d / (double) processingTime : 0d;
            MessageFormat mf = new MessageFormat(perfPattern, Locale.US);
            html.append(mf.format(new Object[] { new Long(lineCount), new Double((double) processingTime / 1000d),
                    new Double(avg) }));
        } else if (IPerformanceData.ACTION_START.equals(perf.getAction())) {
            final String perfPattern = "<font color='#AA3322'>" + "<i>Starting</i>" + "</font>"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            html.append(perfPattern);
        } else if (IPerformanceData.ACTION_STOP.equals(perf.getAction())) {
            final String perfPattern = "<font color=''#229922''>" + "{0, number,#} rows in {1,number,#.##}s<br>" //$NON-NLS-1$ //$NON-NLS-2$
                    + "<i>{2,number,#.##} rows/s</i>" + "</font>"; //$NON-NLS-1$ //$NON-NLS-2$
            long lineCount = perf.getLineCount();
            long processingTime = perf.getProcessingTime();
            double avg = processingTime > 0 ? (double) lineCount * 1000d / (double) processingTime : 0d;
            MessageFormat mf = new MessageFormat(perfPattern);
            html.append(mf.format(new Object[] { new Long(lineCount), new Double((double) processingTime / 1000d),
                    new Double(avg) }));
        }

        return html.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.Element#getElementName()
     */
    @Override
    public String getElementName() {
        return connection.getElementName();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElement#isReadOnly()
     */
    public boolean isReadOnly() {
        return connection.isReadOnly();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IElement#setReadOnly(boolean)
     */
    public void setReadOnly(boolean readOnly) {
        // do nothing.
    }

}
