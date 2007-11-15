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
package org.talend.designer.mapper.ui.image;

/**
 * DOC mhelleboid class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public enum ImageInfo {

    MINIMIZE_ICON("/icons/collapse16.png"), //$NON-NLS-1$
    INTERMEDIATE_SIZE_ICON("/icons/checked.png"), //$NON-NLS-1$
    RESTORE_ICON("/icons/uncollapse16.png"), //$NON-NLS-1$
    // MAPPER_ICON("/icons/tMap32.ico"),
    ADD_CONSTRAINT_ICON("/icons/add_constraint16.png"), //$NON-NLS-1$
    REMOVE_CONSTRAINT_ICON("/icons/remove_constraint16.png"), //$NON-NLS-1$
    CHECKED_ICON("/icons/checked.png"), //$NON-NLS-1$
    UNCHECKED_ICON("/icons/unchecked.png"), //$NON-NLS-1$
    ADD_FILTER_ICON("/icons/addfilter.png"), //$NON-NLS-1$
    ACTIVATE_FILTER_ICON("/icons/addfilter.png"), //$NON-NLS-1$
    REJECT_FILTER_ICON("/icons/rejectfilter.png"), //$NON-NLS-1$
    REJECT_LOOKUP_ICON("/icons/rejectlookup.png"), //$NON-NLS-1$
    LOOKUP_KEY_ICON("/icons/lookup_key16.png"), //$NON-NLS-1$
    ;

    private String path;

    ImageInfo(String path) {
        this.path = path;
    }

    public String getPath() {
        return this.path;
    }

}
