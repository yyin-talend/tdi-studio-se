// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
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

import java.io.Serializable;

/**
 * DOC mhirt class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class LightJetBean implements Serializable {

    private static final long serialVersionUID = 2549445027941196550L;

    private String templateRelativeUri = "";

    private String className = "";

    private String methodName = "";

    private String version = "";
    
    private long crc = 0;

    /**
     * DOC mhirt LightJetBean constructor comment.
     * 
     * @param templateRelativeUri
     * @param className
     * @param methodName
     * @param version
     */
    public LightJetBean(String templateRelativeUri, String className, String methodName, String version, long crc) {
        this.templateRelativeUri = templateRelativeUri;
        this.className = className;
        this.methodName = methodName;
        this.version = version;
        this.crc = crc;
    }

    /**
     * Getter for className.
     * 
     * @return the className
     */
    public String getClassName() {
        return this.className;
    }

    /**
     * Sets the className.
     * 
     * @param className the className to set
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * Getter for methodName.
     * 
     * @return the methodName
     */
    public String getMethodName() {
        return this.methodName;
    }

    /**
     * Sets the methodName.
     * 
     * @param methodName the methodName to set
     */
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    /**
     * Getter for templateRelativeUri.
     * 
     * @return the templateRelativeUri
     */
    public String getTemplateRelativeUri() {
        return this.templateRelativeUri;
    }

    /**
     * Sets the templateRelativeUri.
     * 
     * @param templateRelativeUri the templateRelativeUri to set
     */
    public void setTemplateRelativeUri(String templateRelativeUri) {
        this.templateRelativeUri = templateRelativeUri;
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

    
    /**
     * Getter for crc.
     * @return the crc
     */
    public long getCrc() {
        return this.crc;
    }

    
    /**
     * Sets the crc.
     * @param crc the crc to set
     */
    public void setCrc(long crc) {
        this.crc = crc;
    }
    
    
}
