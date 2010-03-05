// ============================================================================
//
// Copyright (C) 2006-2010 Talend Inc. - www.talend.com
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

    public static final String RESOURCES_DIRECTORY = "resources"; //$NON-NLS-1$

    // TODO SML Replace by File.separator
    public static final String DIR_SEP = "/"; //$NON-NLS-1$

    public static final String EXT_SEP = "."; //$NON-NLS-1$

    public static final String TEMPLATE_EXT = "jet"; //$NON-NLS-1$

    private String resourceName = ""; //$NON-NLS-1$

    private String version = ""; //$NON-NLS-1$

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
