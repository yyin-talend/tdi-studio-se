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
package org.talend.expressionbuilder.test.shadow;

import org.talend.designer.rowgenerator.ui.editor.MetadataColumnExt;

/**
 * yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: VirtualMetadataColumn.java 下午03:42:46 2007-7-24 +0000 (2007-7-24) yzhang $
 * 
 */
public class VirtualMetadataColumn extends MetadataColumnExt {

    /**
     * yzhang VirtualMetadataColumn constructor comment.
     */
    public VirtualMetadataColumn() {
        this.setComment("");
        this.setDefault("");
        this.setLabel("newColumn");
        this.setTalendType("id_String");
        this.setType("");
        this.setId(0);

        this.setLength(null);
        this.setPrecision(null);
        this.setDefault("");

        this.setKey(false);

        this.setNullable(false);
        this.setPattern("");
    }
}
