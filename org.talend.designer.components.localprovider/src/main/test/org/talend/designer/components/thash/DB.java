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
package org.talend.designer.components.thash;

import gnu.trove.THashMap;

import java.util.Map;

/**
 * 
 * DOC amaumont  class global comment. Detailled comment
 * <br/>
 *
 */
class DB {

    private static Map<Integer, Object> map = new THashMap<Integer, Object>(10000, 1.0f);
    
    public static Object get(String container, int id) {
        return map.get(id);
    }

    public static void put(String container, int id, Object bean) {
        map.put(id, bean);
    }
    
}
