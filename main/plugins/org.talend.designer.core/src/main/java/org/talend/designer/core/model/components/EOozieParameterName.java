// ============================================================================
//
// Copyright (C) 2006-2015 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.core.model.components;

import org.talend.designer.core.i18n.Messages;

/**
 * DOC ycbai class global comment. Detailled comment
 */
public enum EOozieParameterName {

    HADOOP_APP_PATH(Messages.getString("EOozieParameterName.hadoopAppPath")), //$NON-NLS-1$

    JOBID_FOR_OOZIE(Messages.getString("EOozieParameterName.jobId")), //$NON-NLS-1$

    REPOSITORY_CONNECTION_ID(Messages.getString("EOozieParameterName.repositoryConnectionId")), //$NON-NLS-1$
    ;

    private String displayName;

    EOozieParameterName(String displayName) {
        this.displayName = displayName;
    }

    public String getName() {
        return this.toString();
    }

    public String getDisplayName() {
        return this.displayName;
    }
}
