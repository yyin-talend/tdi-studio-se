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
 * CodeGenerator Templates Ressources Utils.
 * 
 * $Id$
 * 
 */
public class TemplateUtil {

    public static final String RESOURCES_DIRECTORY = "resources";

    // TODO SML Replace by File.separator
    public static final String DIR_SEP = "/";

    public static final String EXT_SEP = ".";

    public static final String TEMPLATE_EXT = "jet";

    private String resourceName = "";

    private String version = "";

    /**
     * Constructor.
     * 
     * @param resourceName
     */
    public TemplateUtil(EInternalTemplate template) {
        this.resourceName = template.getTemplateName();
        this.version = template.getVersion();
    }

    /**
     * Getter for resourceName.
     * 
     * @return the resourceName
     */
    public String getResourceName() {
        return this.resourceName;
    }

    /**
     * Sets the resourceName.
     * 
     * @param resourceName the resourceName to set
     */
    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    /**
     * Getter for version.
     * 
     * @return the version
     */
    public String getVersion() {
        return this.version;
    }

    /**
     * Sets the version.
     * 
     * @param version the version to set
     */
    public void setVersion(String version) {
        this.version = version;
    }
}
