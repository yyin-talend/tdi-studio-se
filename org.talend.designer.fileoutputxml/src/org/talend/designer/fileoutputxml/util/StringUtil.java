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
package org.talend.designer.fileoutputxml.util;

/**
 * DOC ke class global comment. Detailled comment <br/>
 * 
 */
public class StringUtil {

    public static boolean validateLabelForXML(String label) {
        if (label == null) {
            return false;
        }
        if (label.length() < 1) {
            return false;
        }
        char firstChar = label.charAt(0);
        if (!Character.isLetter(firstChar)) {
            return false;
        }
        if (label.toLowerCase().startsWith("xml")) {
            return false;
        }
        char[] array = label.toCharArray();
        for (int i = 0; i < array.length; i++) {
            if (Character.isSpaceChar(array[i]) || Character.isWhitespace(array[i])) {
                return false;
            }
        }
        return true;
    }

}
