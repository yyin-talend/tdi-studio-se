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
package org.talend.designer.core.ui.views.properties;

import org.talend.core.model.process.EComponentCategory;

/**
 * yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40Z nrousseau $
 * 
 */
public enum EElementType {

    NODE(EComponentCategory.MAIN, EComponentCategory.PROPERTY, EComponentCategory.VIEW, EComponentCategory.DOC),
    NODE_LABEL(EComponentCategory.MAIN, EComponentCategory.PROPERTY, EComponentCategory.VIEW, EComponentCategory.DOC),
    CONNECTION(EComponentCategory.MAIN),
    NOTE(EComponentCategory.MAIN);

    private EComponentCategory[] categories;

    private EElementType(EComponentCategory... categories) {
        this.categories = categories;
    }

    public EComponentCategory[] getCategories() {
        return categories;
    }

}
