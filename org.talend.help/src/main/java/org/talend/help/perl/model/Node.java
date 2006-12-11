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
package org.talend.help.perl.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Node.java.
 * 
 */
public class Node {

    private final EType type;

    private List<Node> children = new ArrayList<Node>();

    private Map<EProperty, String> properties = new HashMap<EProperty, String>();

    public Node(final EType type) {
        super();
        this.type = type;
    }

    public List<Node> getChildren() {
        return children;
    }

    public Map<EProperty, String> getProperties() {
        return properties;
    }

    public EType getType() {
        return type;
    }

    public String toString() {
        return "[Node] " + type.getElementName();
    }

}
