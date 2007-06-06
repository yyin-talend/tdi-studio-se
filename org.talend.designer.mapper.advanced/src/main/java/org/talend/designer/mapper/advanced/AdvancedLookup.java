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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.collections.list.GrowthList;
import org.talend.commons.utils.data.map.MapOfLazyCollections;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * @param <V> value
 */
public class AdvancedLookup<V> {

    private MapOfLazyCollections mapOfCol;

    private List<V> list = new ArrayList<V>();

    private Object[] arrayValues;

    private boolean useHashKeys;

    private boolean arrayIsDirty = true;

    private List<V> listResult;

    private V objectResult;

    /**
     * DOC amaumont AdvancedLookup constructor comment.
     */
    public AdvancedLookup(boolean useHashKeys) {
        super();
        this.useHashKeys = useHashKeys;
        if (useHashKeys) {
            mapOfCol = new MapOfLazyCollections(new HashMap()) {

                @Override
                public Collection instanciateNewCollection() {
                    return new GrowthList(3);
                }

            };
        }
    }

    public Object[] getResultArray() {
        if (this.useHashKeys) {
            return listResult.toArray();
        } else {
            if (listResult == null) {
                listResult = list;
            }
            if (arrayIsDirty) {
                arrayValues = listResult.toArray();
                arrayIsDirty = false;
            }
            return arrayValues;
        }
    }

    public List<V> getResultList() {
        return listResult;
    }

    public V getResultObject() {
        return objectResult;
    }

    public void get(V key) {
        if (this.useHashKeys && key != null) {
            Object v = mapOfCol.get(key);
            if (v instanceof List) {
                listResult = (List) v;
                objectResult = null;
            } else {
                objectResult = (V) v;
                listResult = null;
            }
        } else {
            listResult = list;
            objectResult = null;
        }
    }

    public boolean resultIsObject() {
        return objectResult != null;
    }

    public boolean resultIsList() {
        return listResult != null;
    }

    public Object put(V value) {
        if (value != null) {
            if (this.useHashKeys) {
                arrayIsDirty = true;
                return mapOfCol.put(value, value);
            } else {
                list.add(value);
                return null;
            }
        } else {
            return null;
        }
    }

    public void clear() {
        mapOfCol.clear();
        arrayValues = null;
    }

    /**
     * DOC amaumont Comment method "hasResult".
     * 
     * @return
     */
    public boolean hasResult() {
        return resultIsObject() || resultIsList();
    }

    /**
     * Getter for hasHashKeys.
     * 
     * @return the hasHashKeys
     */
    public boolean isUseHashKeys() {
        return this.useHashKeys;
    }

}
