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
package org.talend.designer.components.commons;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.list.GrowthList;
import org.talend.commons.utils.data.map.MapOfLazyCollections;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * @param <V> value
 */
public class AdvancedLookup<V> {

    private MapOfLazyCollections mapOfCol;

    private Map<V, V> uniqueHash;

    private boolean countValuesForEachKey;

    private Map<V, Integer> counterHash;

    private List<V> list = new ArrayList<V>();

    private Object[] arrayValues;

    private boolean useHashKeys;

    private boolean arrayIsDirty = true;

    private List<V> listResult;

    private V objectResult;

    private boolean uniqueMatch;

    private boolean keepAllValues;

    private MULTIPLE_MATCHING_MODE multipleMatchingMode;

    private static final int ZERO = 0;

    private static final int ONE = 1;

    /**
     * 
     * DOC amaumont AdvancedLookup class global comment. Detailled comment <br/>
     * 
     */
    public enum MULTIPLE_MATCHING_MODE {
        FIRST_MATCH,
        LAST_MATCH,
        ALL_MATCHES,
    }

    /**
     * 
     * <code>AdvancedLookup</code> can be configured to store values in different modes.
     * 
     * @param useHashKeys use <code>equals()</code> and <code>hashCode()</code> methods by storing objects in hash
     * maps
     * @param multipleMatchingMode to optimize storing and searching, and to specify which matching mode should used
     * @param uniqueMatch keep in the lookup only the last put object, but store the current number of same values for
     * each key
     * @param keepAllValues keep all identical values (with same key values) in each list of each key
     * @param countValuesForEachKey force internal count of values
     */
    public AdvancedLookup(boolean useHashKeys, boolean uniqueMatch, MULTIPLE_MATCHING_MODE multipleMatchingMode,
            boolean keepAllValues, boolean countValuesForEachKey) {
        super();
        this.useHashKeys = useHashKeys;
        this.uniqueMatch = uniqueMatch;
        this.keepAllValues = keepAllValues;
        this.countValuesForEachKey = countValuesForEachKey;
        this.multipleMatchingMode = multipleMatchingMode;
        if (useHashKeys) {
            if (uniqueMatch && !keepAllValues) {
                uniqueHash = new HashMap<V, V>();
            }
            if (countValuesForEachKey) {
                counterHash = new HashMap<V, Integer>();
            }
            mapOfCol = new MapOfLazyCollections(new HashMap()) {

                @Override
                public Collection instanciateNewCollection() {
                    return new GrowthList(3);
                }

            };
        }
    }

    public static <V> AdvancedLookup<V> getHashedMultiRowsLookup() {
        return new AdvancedLookup<V>(true, false, MULTIPLE_MATCHING_MODE.ALL_MATCHES, false, false);
    }

    public static <V> AdvancedLookup<V> getHashedMultiRowsLookup(MULTIPLE_MATCHING_MODE multipleMatchingMode) {
        return new AdvancedLookup<V>(true, false, multipleMatchingMode, false, false);
    }
    
    public static <V> AdvancedLookup<V> getUnhashedMultiRowsLookup() {
        return new AdvancedLookup<V>(false, false, null, false, false);
    }

    public static <V> AdvancedLookup<V> getUniqueRowLookup() {
        return new AdvancedLookup<V>(true, true, null, false, false);
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
        if (uniqueMatch) {
            listResult = null;
            objectResult = uniqueHash.get(key);
        } else {
            if (this.useHashKeys && key != null) {
                Object v = mapOfCol.get(key);
                if (v instanceof List) {
                    List<V> localList = (List<V>) v;
                    if (multipleMatchingMode == MULTIPLE_MATCHING_MODE.ALL_MATCHES) {
                        listResult = localList;
                        objectResult = null;
                    } else if (multipleMatchingMode == MULTIPLE_MATCHING_MODE.FIRST_MATCH) {
                        objectResult = localList.get(ZERO);
                    } else if (multipleMatchingMode == MULTIPLE_MATCHING_MODE.LAST_MATCH) {
                        listResult = null;
                        objectResult = localList.get(localList.size() - ONE);
                    }
                } else {
                    objectResult = (V) v;
                    listResult = null;
                }
            } else {
                listResult = list;
                objectResult = null;
            }
        }
    }

    public boolean resultIsObject() {
        return objectResult != null;
    }

    public boolean resultIsList() {
        return listResult != null;
    }

    public V put(V value) {
        if (value != null) {
            if (uniqueMatch && !keepAllValues) {
                V previousValue = uniqueHash.put(value, value);
                incrementCountValues(value, previousValue);
                return previousValue;
            } else {
                if (this.useHashKeys) {
                    arrayIsDirty = true;
                    V previousValue = (V) mapOfCol.put(value, value);
                    incrementCountValues(value, previousValue);
                    return previousValue;
                } else {
                    list.add(value);
                    return null;
                }
            }
        }
        return null;
    }

    /**
     * DOC amaumont Comment method "incrementCountValues".
     * 
     * @param value
     * @param previousValue
     */
    private void incrementCountValues(V value, V previousValue) {
        if (countValuesForEachKey && previousValue != null) {
            Integer count = counterHash.get(value);
            if (count == null) {
                count = ONE;
            } else {
                count++;
            }
            counterHash.put(value, count);
        }
    }

    public void clear() {
        if (mapOfCol != null) {
            mapOfCol.clear();
        }
        if (uniqueHash != null) {
            uniqueHash.clear();
        }
        if (counterHash != null) {
            counterHash.clear();
        }
        arrayValues = null;
        if (list != null) {
            list.clear();
        }
        if (listResult != null) {
            listResult = null;
        }
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

    /**
     * Getter for countValuesForEachKey.
     * 
     * @return the countValuesForEachKey
     */
    public boolean isCountValuesForEachKey() {
        return this.countValuesForEachKey;
    }

    /**
     * Getter for keepAllValues.
     * 
     * @return the keepAllValues
     */
    public boolean isKeepAllValues() {
        return this.keepAllValues;
    }

    /**
     * Getter for uniqueMatch.
     * 
     * @return the uniqueMatch
     */
    public boolean isUniqueMatch() {
        return this.uniqueMatch;
    }

    public int getCount(V key) {
        if (countValuesForEachKey) {
            Integer count = counterHash.get(key);
            if (count == null) {
                return ZERO;
            } else {
                return count;
            }
        } else if (uniqueMatch && !keepAllValues) {
            if (uniqueHash.get(key) != null) {
                return ONE;
            } else {
                return ZERO;
            }

        } else if (useHashKeys) {
            Object v = mapOfCol.get(key);
            if (v instanceof List) {
                List<V> localList = (List<V>) v;
                return localList.size();
            } else {
                if (v != null) {
                    return ONE;
                } else {
                    return ZERO;
                }
            }
        } else {
            if (list.contains(key)) {
                return 1;
            } else {
                return ZERO;
            }
        }
    }

}
