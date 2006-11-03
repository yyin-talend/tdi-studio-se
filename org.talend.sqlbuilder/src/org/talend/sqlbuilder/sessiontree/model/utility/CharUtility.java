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
package org.talend.sqlbuilder.sessiontree.model.utility;

/**
 * Char Utility.
 * <br/>
 *
 * $Id: CharUtility.java,v 1.3 2006/11/01 07:49:10 peiqin.hou Exp $
 *
 */
class CharUtility {

    private static final int NUMBER_128 = 128;
    private static final int NUMBER_121 = 121;
    private static final int NUMBER_24 = 24;
    private static final int NUMBER_97 = 97;
    private static final int NUMBER_89 = 89;
    private static final int NUMBER_65 = 65;

    /** 
     * Returns an int value that is negative if cCompare comes before cRef in the alphabet, zero if
	 * the two are equal, and positive if cCompare comes after cRef in the alphabet.
     * @param cCompare cCompare
     * @param cRef cRef
     * @return int
	 */
	public static int compareCharsAlphabetically(char cCompare, char cRef) {
		return (alphabetizeChar(cCompare) - alphabetizeChar(cRef));
	}
	
    /**
     * @param c char.
     * @return int
     */
	private static int alphabetizeChar(char c) {
		if (c < NUMBER_65) {
            return c;
        }
		if (c < NUMBER_89) {
            return (2 * c) - NUMBER_65;
        }
		if (c < NUMBER_97) {
            return c + NUMBER_24;
        }
		if (c < NUMBER_121) {
            return (2 * c) - NUMBER_128;
        }
		return c;
	}
	
}
