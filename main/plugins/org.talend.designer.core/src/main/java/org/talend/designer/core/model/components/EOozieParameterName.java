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
    
    PROPERTY_TYPENAME(Messages.getString("EOozieParameterName.propertyType")),

    HADOOP_APP_PATH(Messages.getString("EOozieParameterName.hadoopAppPath")), //$NON-NLS-1$

    JOBID_FOR_OOZIE(Messages.getString("EOozieParameterName.jobId")), //$NON-NLS-1$

    REPOSITORY_CONNECTION_ID(Messages.getString("EOozieParameterName.repositoryConnectionId")), //$NON-NLS-1$
    
    HADOOP_DISTRIBUTION(Messages.getString("EOozieParameterName.hadoopDistribution")),
    
    HADOOP_VERSION(Messages.getString("EOozieParameterName.hadoopVersion")),
    
    ENABLE_KERBEROS(Messages.getString("EOozieParameterName.enableKerberos")),
    
    NAME_NODE_PRINCIPAL(Messages.getString("EOozieParameterName.nameNodePrincipal")),
    
    USERNAME(Messages.getString("EOozieParameterName.userName")),
    
    GROUP(Messages.getString("EOozieParameterName.group")),
    
    USE_KEYTAB(Messages.getString("EOozieParameterName.useKeytab")),
    
    KT_PRINCIPAL(Messages.getString("EOozieParameterName.ktPrincipal")),
    
    KEY_TAB(Messages.getString("EOozieParameterName.keytab")),
    
    ENABLE_OO_KERBEROS(Messages.getString("EOozieParameterName.enableOoKerberos")),
    
    NAME_NODE_END_POINT(Messages.getString("EOozieParameterName.nameNodeEndPoint")),
    
    JOB_TRACKER_ENDPOINT(Messages.getString("EOozieParameterName.jobTrackerEndPoint")),
    
    OOZIE_END_POINT(Messages.getString("EOozieParameterName.oozieEndPointValue")),
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
