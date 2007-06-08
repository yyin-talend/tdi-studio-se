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
package org.talend.designer.mapper.model.table;

import org.talend.designer.components.commons.AdvancedLookup.MULTIPLE_MATCHING_MODE;

/**
 * 
 * Lookup types.
 * 
 * $Id$
 * 
 */
public enum TMAP_MULTIPLE_MATCHING_MODE implements ILookupType {
    FIRST_MATCH(MULTIPLE_MATCHING_MODE.FIRST_MATCH, "First match"),
    LAST_MATCH(MULTIPLE_MATCHING_MODE.LAST_MATCH, "Last match"),
    ALL_MATCHES(MULTIPLE_MATCHING_MODE.ALL_MATCHES, "All matches"), ;

    private String label;

    private MULTIPLE_MATCHING_MODE multipleMatchingMode;

    /**
     * 
     * DOC amaumont LOOKUP_TYPE constructor comment.
     * 
     * @param label
     */
    TMAP_MULTIPLE_MATCHING_MODE(MULTIPLE_MATCHING_MODE multipleMatchingMode, String label) {
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
    public MULTIPLE_MATCHING_MODE getMultipleMatchingMode() {
        return this.multipleMatchingMode;
    }

    public static ILookupType parse(String matchingMode) {
        TMAP_MULTIPLE_MATCHING_MODE multipleMatchingMode = null;
        TMAP_MULTIPLE_MATCHING_MODE[] tmapMultipleMatchingModes = values();
        for (TMAP_MULTIPLE_MATCHING_MODE tmapMultipleMatchingMode : tmapMultipleMatchingModes) {
            if (tmapMultipleMatchingMode.toString().equals(matchingMode)) {
                multipleMatchingMode = tmapMultipleMatchingMode;
                break;
            }
        }
        return multipleMatchingMode;
    }

};
