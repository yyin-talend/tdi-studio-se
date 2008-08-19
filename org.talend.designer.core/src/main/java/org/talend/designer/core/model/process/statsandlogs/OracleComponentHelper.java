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
package org.talend.designer.core.model.process.statsandlogs;

import org.talend.core.model.process.IProcess;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.model.process.DataNode;
import org.talend.designer.core.model.process.jobsettings.JobSettingsConstants;
import org.talend.designer.core.ui.preferences.StatsAndLogsConstants;

/**
 * DOC YeXiaowei class global comment. Detailled comment
 */
public final class OracleComponentHelper {

    /**
     * 
     * DOC YeXiaowei Comment method "setConnectionTypeForOracle".
     * 
     * @param node
     * @param process
     */
    public static void setConnectionTypeForOracle(final DataNode node, final IProcess process, final String dbtype) {
        String dbType = (String) process.getElementParameter(dbtype).getValue();
        if (dbType != null) {
            if (dbType.startsWith("tOracle")) {
                if (dbType.contains("sid")) {
                    // Sid
                    node.getElementParameter(EParameterName.CONNECTION_TYPE.getName()).setValue(
                            StatsAndLogsConstants.ORACLE_WITH_SID_CONN_TYPE);
                } else if (dbType.contains("servername")) {
                    node.getElementParameter(EParameterName.CONNECTION_TYPE.getName()).setValue(
                            StatsAndLogsConstants.ORACLE_WITH_SERVICE_CONN_TYPE);
                } else {
                    node.getElementParameter(EParameterName.CONNECTION_TYPE.getName()).setValue(StatsAndLogsConstants.ORACLE_OCI);
                }
            }
        }

    }

    /**
     * 
     * DOC YeXiaowei Comment method "filterOracleComponentName".
     * 
     * @param components
     * @return
     */
    public static String filterOracleComponentName(String components) {

        if (components.equals(StatsAndLogsConstants.ORACLE_SID_ALIAS) || components.equals(StatsAndLogsConstants.ORACLE_SN_ALIAS)) {
            return "tOracleOutput";
        }

        if (components.equals(JobSettingsConstants.ORACLE_INOUT_SN_ALIAS)
                || components.equals(JobSettingsConstants.ORACLE_INPUT_SID_ALIAS)) {
            return "tOracleInput";
        }

        return components;
    }
}
