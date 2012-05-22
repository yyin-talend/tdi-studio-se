// ============================================================================
//
// Copyright (C) 2006-2011 Talend Inc. - www.talend.com
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

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.talend.designer.core.model.process.jobsettings.JobSettingsConstants;
import org.talend.designer.core.ui.preferences.StatsAndLogsConstants;

/**
 * DOC Administrator class global comment. Detailled comment
 */
public class OracleComponentHelperTest {

    /**
     * Test method for
     * {@link org.talend.designer.core.model.process.statsandlogs.OracleComponentHelper#filterOracleConnectionType(java.lang.String)}
     * .
     */
    @Test
    public void testFilterOracleConnectionType() {
        String type = "ORACLE_SID";
        assertEquals(StatsAndLogsConstants.ORACLE_WITH_SID_CONN_TYPE, OracleComponentHelper.filterOracleConnectionType(type));
    }

    /**
     * Test method for
     * {@link org.talend.designer.core.model.process.statsandlogs.OracleComponentHelper#filterOracleComponentName(java.lang.String)}
     * .
     */
    @Test
    public void testFilterOracleComponentName() {
        String component = "tOracleOutput_oci";
        assertEquals(JobSettingsConstants.ORACLE_OUTPUT, OracleComponentHelper.filterOracleComponentName(component));
    }

}
