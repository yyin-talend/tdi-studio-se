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
package org.talend.hadoop.distribution;

/**
 * Enumeration that describes component types with their information. This Enumeration is used by the components when
 * they used a HADOOP_DISTRIBUTION field to declare which kind of components they are.
 *
 */
public enum ComponentType {
    HDFS("org.talend.hadoop.distribution.component.HDFSComponent", "DISTRIBUTION", "DB_VERSION"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    PIG("org.talend.hadoop.distribution.component.PigComponent", "DISTRIBUTION", "PIG_VERSION"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    MAPREDUCE("org.talend.hadoop.distribution.component.MRComponent", "DISTRIBUTION", "MR_VERSION"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    HIVE("org.talend.hadoop.distribution.component.HiveComponent", "DISTRIBUTION", "HIVE_VERSION"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    SQOOP("org.talend.hadoop.distribution.component.SqoopComponent", "DISTRIBUTION", "DB_VERSION"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    HCATALOG("org.talend.hadoop.distribution.component.HCatalogComponent", "DISTRIBUTION", "HCAT_VERSION"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    HBASE("org.talend.hadoop.distribution.component.HBaseComponent", "DISTRIBUTION", "HBASE_VERSION"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    SPARKBATCH("org.talend.hadoop.distribution.component.SparkBatchComponent", "DISTRIBUTION", "SPARK_VERSION"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    SPARKSTREAMING("org.talend.hadoop.distribution.component.SparkStreamingComponent", "DISTRIBUTION", "SPARK_VERSION"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    IMPALA("org.talend.hadoop.distribution.component.ImpalaComponent", "DISTRIBUTION", "IMPALA_VERSION"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

    /**
     * @param service - the interface of the service
     * @param distributionParameter - the name of the parameter to create for the distribution on the component side.
     * @param versionParameter - the name of the parameter to create for the version on the component side.
     */
    ComponentType(String service, String distributionParameter, String versionParameter) {
        this.mService = service;
        this.mDistributionParameter = distributionParameter;
        this.mVersionParameter = versionParameter;
    }

    private String mService;

    private String mDistributionParameter;

    private String mVersionParameter;

    public static ComponentType getComponentType(String type) {
        for (ComponentType ct : values()) {
            if (ct.name().equals(type)) {
                return ct;
            }
        }
        return null;
    }

    public String getService() {
        return this.mService;
    }

    public String getDistributionParameter() {
        return this.mDistributionParameter;
    }

    public String getVersionParameter() {
        return this.mVersionParameter;
    }
}
