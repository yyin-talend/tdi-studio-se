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


/**
 * This class will create a virtual component that will create the logs for the job. It's not used at all in the
 * designer, only during the code generation. <br/>
 * 
 */
public class JobStatsComponent extends AbstractStatsLogsComponent {

    public JobStatsComponent(boolean useFile, boolean useConsole, String dbComponent) {
        if (dbComponent != null) {
            this.useDb = true;
            this.dbComponent = dbComponent;
        }
        this.useFile = useFile;
        this.useConsole = useConsole;
        this.componentId = "STATS"; //$NON-NLS-1$
        this.subComponent = "tStatCatcher"; //$NON-NLS-1$

        loadMultipleComponentManager();
    }

    public String getVersion() {
        return "0.1"; //$NON-NLS-1$
    }
}
