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
package org.talend.repository.preview;

/**
 * Describes a process for shadow execution. <br/>
 * 
 * $Id$
 * 
 */
public class ProcessDescription extends AbstractProcessDescription {

    private LDAPSchemaBean ldapSchemaBean;

    /**
     * Constructs a new ProcessDescription.
     */
    public ProcessDescription() {
        super();
    }

    public LDAPSchemaBean getLdapSchemaBean() {
        return this.ldapSchemaBean;
    }

    public void setLdapSchemaBean(LDAPSchemaBean ldapSchemaBean) {
        this.ldapSchemaBean = ldapSchemaBean;
    }

}
