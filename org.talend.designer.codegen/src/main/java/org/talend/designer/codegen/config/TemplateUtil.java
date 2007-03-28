// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2007 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
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
