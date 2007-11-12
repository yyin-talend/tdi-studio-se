// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the  agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//   
// ============================================================================
package org.talend.designer.codegen.config;

/**
 * Internal Templates for Code Generator, must be always available to encapsulate rela parts of code.
 * 
 * $Id$
 * 
 */
public enum EInternalTemplate {
    HEADER("header", "0.0.1"),
    FOOTER("footer", "0.0.1"),
    CONTEXT("context", "0.0.1"),
    SUBPROCESS_HEADER("subprocess_header", "0.0.1"),
    SUBPROCESS_FOOTER("subprocess_footer", "0.0.1"),
    SUBTREE_BEGIN("subtree_begin", "0.0.1"),
    SUBTREE_END("subtree_end", "0.0.1"),
    CLOSE_BLOCKS_CODE("close_blocks_code", "0.0.1"),
    PART_HEADER("component_part_header", "0.0.1"),
    PART_FOOTER("component_part_footer", "0.0.1"),
    PART_ENDMAIN("component_part_endmain", "0.0.1"),
    PROCESSINFO("footer_process_info", "0.0.1");

    private String templateName;

    private String version;

    /**
     * 
     * @param templateName
     */
    private EInternalTemplate(String templateName, String version) {
        this.templateName = templateName;
        this.version = version;
    }

    /**
     * Getter for templateName.
     * 
     * @return the templateName
     */
    public String getTemplateName() {
        return this.templateName;
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
        return this.templateName;
    }
}
