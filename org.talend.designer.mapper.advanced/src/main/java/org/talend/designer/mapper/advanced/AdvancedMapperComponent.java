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
package org.talend.designer.mapper.advanced;

import java.util.ArrayList;
import java.util.List;

import org.talend.core.model.process.BlockCode;
import org.talend.core.model.process.IHashableInputConnections;
import org.talend.designer.mapper.MapperComponent;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id: MapperComponent.java 3351 2007-05-04 12:14:00Z plegall $
 * 
 */
public class AdvancedMapperComponent extends MapperComponent {

    /**
     * DOC amaumont MapperComponent constructor comment.
     */
    public AdvancedMapperComponent() {
        super();
    }

    @Override
    public List<BlockCode> getBlocksCodeToClose() {
//        ArrayList<BlockCode> list = new ArrayList<BlockCode>();
//        for (int i = 0; i < 2; i++) {
//            list.add(new BlockCode("lookup " + (i + 1) + " tAdvancedMap"));
//        }
//        return list;
        
        return getGenerationManager().getBlocksCodeToClose();
    }

}
