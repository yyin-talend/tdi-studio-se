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
package org.talend.designer.mapper.model.table;

import org.talend.designer.components.commons.AdvancedLookup.MATCHING_MODE;

/**
 * 
 * Lookup types.
 * 
 * $Id$
 * 
 */
public enum TMAP_MATCHING_MODE implements ILookupType {
    ALL_ROWS(MATCHING_MODE.ALL_ROWS, "All rows"),
    UNIQUE_MATCH(MATCHING_MODE.UNIQUE_MATCH, "Unique match"),
    FIRST_MATCH(MATCHING_MODE.FIRST_MATCH, "First match"),
    LAST_MATCH(MATCHING_MODE.LAST_MATCH, "Last match"),
    ALL_MATCHES(MATCHING_MODE.ALL_MATCHES, "All matches"), ;

    private String label;

    private MATCHING_MODE multipleMatchingMode;

    /**
     * 
     * DOC amaumont LOOKUP_TYPE constructor comment.
     * 
     * @param label
     */
    TMAP_MATCHING_MODE(MATCHING_MODE multipleMatchingMode, String label) {
        this.label = label;
        this.multipleMatchingMode = multipleMatchingMode;
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
    public MATCHING_MODE getMultipleMatchingMode() {
        return this.multipleMatchingMode;
    }

    public static ILookupType parse(String matchingMode) {
        TMAP_MATCHING_MODE multipleMatchingMode = null;
        TMAP_MATCHING_MODE[] tmapMultipleMatchingModes = values();
        for (TMAP_MATCHING_MODE tmapMultipleMatchingMode : tmapMultipleMatchingModes) {
            if (tmapMultipleMatchingMode.toString().equals(matchingMode)) {
                multipleMatchingMode = tmapMultipleMatchingMode;
                break;
            }
        }
        return multipleMatchingMode;
    }

};
