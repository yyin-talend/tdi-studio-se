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
package org.talend.help.perl.model;

/**
 * EType.java.
 * 
 */
public enum EType {
    ROOT("help"), //$NON-NLS-1$
    FAMILY("family"), //$NON-NLS-1$
    CATEGORY("category"), //$NON-NLS-1$
    FUNCTION("function"), //$NON-NLS-1$
    ANCHOR("anchor"); //$NON-NLS-1$

    private final String elementName;

    EType(String name) {
        elementName = name;
    }

    public String getElementName() {
        return elementName;
    }

    public static final EType find(String name) {
        if (name.equals(ROOT.elementName)) {
            return ROOT;
        }
        if (name.equals(FAMILY.elementName)) {
            return FAMILY;
        }
        if (name.equals(CATEGORY.elementName)) {
            return CATEGORY;
        }
        if (name.equals(FUNCTION.elementName)) {
            return FUNCTION;
        }
        if (name.equals(ANCHOR.elementName)) {
            return ANCHOR;
        }
        return null;
    }
}
