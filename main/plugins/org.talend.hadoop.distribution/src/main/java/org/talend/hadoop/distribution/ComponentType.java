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
    HDFS("org.talend.hadoop.distribution.component.HDFSComponent", "DISTRIBUTION", "DISTRIBUTION", "DB_VERSION", "DB_VERSION"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
    PIG("org.talend.hadoop.distribution.component.PigComponent", "DISTRIBUTION", "DISTRIBUTION", "PIG_VERSION", "PIG_VERSION"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
    PIGOUTPUT(
              "org.talend.hadoop.distribution.component.PigComponent", "DISTRIBUTION", "DISTRIBUTION", "HBASE_VERSION", "PIG_VERSION"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
    MAPREDUCE("org.talend.hadoop.distribution.component.MRComponent", "DISTRIBUTION", "DISTRIBUTION", "MR_VERSION", "DB_VERSION"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
    HIVE("org.talend.hadoop.distribution.component.HiveComponent", "DISTRIBUTION", "DISTRIBUTION", "HIVE_VERSION", "HIVE_VERSION"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
    SQOOP(
          "org.talend.hadoop.distribution.component.SqoopComponent", "DISTRIBUTION", "HADOOP_PROPERTY/DISTRIBUTION", "DB_VERSION", "HADOOP_PROPERTY/DB_VERSION"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
    HCATALOG(
             "org.talend.hadoop.distribution.component.HCatalogComponent", "DISTRIBUTION", "DISTRIBUTION", "HCAT_VERSION", "HCAT_VERSION"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
    HBASE(
          "org.talend.hadoop.distribution.component.HBaseComponent", "DISTRIBUTION", "HBASE_DISTRIBUTION", "HBASE_VERSION", "HBASE_VERSION"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
    SPARKBATCH(
               "org.talend.hadoop.distribution.component.SparkBatchComponent", "DISTRIBUTION", "DISTRIBUTION", "SPARK_VERSION", "DB_VERSION"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
    SPARKSTREAMING(
                   "org.talend.hadoop.distribution.component.SparkStreamingComponent", "DISTRIBUTION", "DISTRIBUTION", "SPARK_VERSION", "DB_VERSION"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
    IMPALA(
           "org.talend.hadoop.distribution.component.ImpalaComponent", "DISTRIBUTION", "DISTRIBUTION", "IMPALA_VERSION", "IMPALA_VERSION"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

    /**
     * @param service - the interface of the service
     * @param distributionParameter - the name of the parameter to create for the distribution on the component side.
     * @param distributionRepositoryValueParameter - the name of the repository value parameter for the distribution on
     * the component side.
     * @param versionParameter - the name of the parameter to create for the version on the component side.
     * @param versionRepositoryValueParameter - the name of the repository value parameter for the version on the
     * component side.
     */
    ComponentType(String service, String distributionParameter, String distributionRepositoryValueParameter,
            String versionParameter, String versionRepositoryValueParameter) {
        this.mService = service;
        this.mDistributionParameter = distributionParameter;
        this.mVersionParameter = versionParameter;
        this.mDistributionRepositoryValueParameter = distributionRepositoryValueParameter;
        this.mVersionRepositoryValueParameter = versionRepositoryValueParameter;
    }

    private String mService;

    private String mDistributionParameter;

    private String mVersionParameter;

    private String mDistributionRepositoryValueParameter;

    private String mVersionRepositoryValueParameter;

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

    public String getDistributionRepositoryValueParameter() {
        return this.mDistributionRepositoryValueParameter;
    }

    public String getVersionRepositoryValueParameter() {
        return this.mVersionRepositoryValueParameter;
    }
}
