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
package org.talend.designer.codegen.config;

/**
 * DOC xtan class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ææäº, 29 ä¹æ 2006) nrousseau $
 * 
 */
public enum JetSkeleton {

    FOOTER("org.talend.designer.codegen", "resources/footer_java.skeleton", "0.0.1"),
    SUBPROCESS_HEADER_JAVA("org.talend.designer.codegen", "resources/subprocess_header_java.skeleton", "0.0.1"),
    DB_OUTPUT_BULK("org.talend.designer.components.localprovider", "components/templates/db_output_bulk.skeleton", "0.0.1"),
    SLOWLY_CHANGE_DIMENSION(
                            "org.talend.designer.components.localprovider",
                            "components/templates/slowly_change_dimension.skeleton",
                            "0.0.1");

    private String pluginId;

    private String skeletonLocation;

    private String version;

    /**
     * DOC xtan JetSkeleton constructor comment.
     * 
     * @param skeletonLocation
     * @param version
     */
    private JetSkeleton(String pluginId, String skeletonLocation, String version) {
        this.pluginId = pluginId;
        this.skeletonLocation = skeletonLocation;
        this.version = version;
    }

    /**
     * Getter for skeletonLocation.
     * 
     * @return the skeletonLocation
     */
    public String getSkeletonLocation() {
        return this.skeletonLocation;
    }

    /**
     * Getter for version.
     * 
     * @return the version
     */
    public String getVersion() {
        return this.version;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return this.skeletonLocation;
    }

    /**
     * Getter for pluginId.
     * 
     * @return the pluginId
     */
    public String getPluginId() {
        return this.pluginId;
    }

}
