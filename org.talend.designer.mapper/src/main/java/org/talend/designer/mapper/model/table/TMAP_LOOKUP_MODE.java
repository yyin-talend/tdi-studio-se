// ============================================================================
//
// Copyright (C) 2006-2009 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.mapper.model.table;

/**
 * 
 * Lookup types.
 * 
 * $Id$
 * 
 */
public enum TMAP_LOOKUP_MODE implements IUILookupMode {
    LOAD_ONCE(LOOKUP_MODE.LOAD_ONCE, "Load once", //$NON-NLS-1$
              "Load once the lookup at subjob start"//$NON-NLS-1$
    ),
    LOAD_ONCE_AND_EDIT(LOOKUP_MODE.LOAD_ONCE_AND_EDIT, "Load once and update", //$NON-NLS-1$
                       "Load once at subjob start and add/update the lookup during the process"), //$NON-NLS-1$
    RELOAD(LOOKUP_MODE.RELOAD, "Reload at each row", //$NON-NLS-1$ 
           "Reload the lookup at each row"), //$NON-NLS-1$ 
    CACHE_OR_RELOAD(LOOKUP_MODE.CACHE_OR_RELOAD, "Reload at each row (cache)",//$NON-NLS-1$
                    "At each row, get result from the cache or reload the lookup"), //$NON-NLS-1$
    ;

    private String label;

    private LOOKUP_MODE multipleMatchingMode;

    private String tooltipText;

    /**
     * 
     * DOC amaumont LOOKUP_TYPE constructor comment.
     * 
     * @param label
     */
    TMAP_LOOKUP_MODE(LOOKUP_MODE multipleMatchingMode, String label, String tooltipText) {
        this.label = label;
        this.multipleMatchingMode = multipleMatchingMode;
        this.tooltipText = tooltipText;

    }

    /**
     * Getter for label.
     * 
     * @return the label
     */
    public String getLabel() {
        return this.label;
    }

    /**
     * Getter for multipleMatchingMode.
     * 
     * @return the multipleMatchingMode
     */
    public LOOKUP_MODE getLookupMode() {
        return this.multipleMatchingMode;
    }

    public String getTooltipText() {
        return this.tooltipText;
    }

    public static IUILookupMode parse(String matchingMode) {
        TMAP_LOOKUP_MODE multipleMatchingMode = null;
        TMAP_LOOKUP_MODE[] tmapMultipleMatchingModes = values();
        for (TMAP_LOOKUP_MODE tmapMultipleMatchingMode : tmapMultipleMatchingModes) {
            if (tmapMultipleMatchingMode.toString().equals(matchingMode)) {
                multipleMatchingMode = tmapMultipleMatchingMode;
                break;
            }
        }
        return multipleMatchingMode;
    }

};
