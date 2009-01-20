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
package org.talend.repository.model;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jface.preference.PreferenceNode;

/**
 * DOC aimingchen class global comment. Detailled comment
 */
public class ProjectSettingNode extends PreferenceNode {

    private String category;

    private String order;

    /**
     * DOC aimingchen ProjectSettingNode constructor comment.
     * 
     * @param id
     */
    public ProjectSettingNode(String id) {
        super(id);
        // TODO Auto-generated constructor stub
    }

    public ProjectSettingNode(IConfigurationElement element) {
        this(element.getAttribute("id")); //$NON-NLS-1$
        category = element.getAttribute("category"); //$NON-NLS-1$
        order = element.getAttribute("order"); //$NON-NLS-1$
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getOrder() {
        return this.order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

}
