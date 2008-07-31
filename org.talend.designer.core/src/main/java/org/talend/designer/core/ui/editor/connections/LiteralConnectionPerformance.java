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

/**
 * DOC xtan Control the statistical message that display on "OnComponentOK/OnComponentError/OnSubJobOK/OnSubJobError/If"
 * link. (feature No.3443: show "true/false" on If trigger Links , show "ok/error" on OK / Error links)
 */
public class LiteralConnectionPerformance extends ConnectionPerformance {

    private static final String COLOR_OK = "#229922";

    private static final String COLOR_ERROR = "#AA3322";

    private static final String COLOR_TRUE = "#229922";

    private static final String COLOR_FALSE = "#AA3322";

    /**
     * DOC xtan LiteralConnectionPerformance constructor comment.
     * 
     * @param conn
     */
    public LiteralConnectionPerformance(Connection conn) {
        super(conn);
    }

    @Override
    protected void resetStatus() {
        super.resetStatus();
    }

    /**
     * msg like this style: if1|true, if1|false, OnComponentOK1|ok, OnComponentError1|error.
     */
    @Override
    public void setLabel(String msg) {
        if (StringUtils.isEmpty(msg)) {
            // handle by super class
            super.setLabel(msg);
            return;
        }

        String[] part = msg.split("\\|");
        if (part != null && part.length == 3) {
            // update label
            String oldLabel = label;
            label = createHtmlText(part[1]);
            firePropertyChange(LABEL_PROP, oldLabel, label);

        }

    }

    private String createHtmlText(String literal) {
        StringBuilder html = new StringBuilder();
        String pattern = "<font color='%1$s'>%2$s</font><br>";
        html.append(String.format(pattern, getColorStatus(literal), literal));
        return html.toString();
    }

    private String getColorStatus(String status) {
        if ("true".equalsIgnoreCase(status)) {
            return COLOR_TRUE;
        } else if ("false".equalsIgnoreCase(status)) {
            return COLOR_FALSE;
        } else if ("ok".equalsIgnoreCase(status)) {
            return COLOR_OK;
        } else if ("error".equalsIgnoreCase(status)) {
            return COLOR_ERROR;
        }

        return COLOR_FALSE;
    }
}
