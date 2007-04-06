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
package org.talend.repository.model.migration.conversions;

import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.repository.model.migration.ComponentUtilities;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
 * 
 */
public class UpdatePropertyComponentConversion implements IComponentConversion {

    private String value;

    private String name;

    public UpdatePropertyComponentConversion(String name, String value) {
        super();
        this.value = value;
        this.name = name;
    }

    public void transform(NodeType node) {
        ComponentUtilities.setNodeValue(node, name, value);
    }

}
