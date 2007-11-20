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
package org.talend.repository.ui.actions.metadata.database;

/**
 * DOC ggu class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ææäº, 29 ä¹æ 2006) nrousseau $
 * 
 */
public class ValueTypeBean {

    private String connName;

    private String value;

    /**
     * DOC ggu ValueTypeBean constructor comment.
     * 
     * @param connName
     * @param value
     */
    public ValueTypeBean(String connName, String value) {
        super();
        this.connName = connName;
        this.value = value;
    }

    /**
     * Getter for connName.
     * 
     * @return the connName
     */
    public String getConnName() {
        return this.connName;
    }

    /**
     * Getter for value.
     * 
     * @return the value
     */
    public String getValue() {
        return this.value;
    }

}
