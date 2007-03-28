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
package org.talend.sqlbuilder.util;

import java.util.AbstractList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

import org.talend.sqlbuilder.Messages;
/**
 * 
 * DOC dev  class global comment. Detailled comment
 * <br/>
 *
 * $Id: SortedList.java,v 1.2 2006/10/26 21:42:08 qiang.zhang Exp $
 *
 */
@SuppressWarnings("unchecked") //$NON-NLS-1$
public class SortedList extends AbstractList {

    protected int psize;

    protected Object[] pdata;

    protected Comparator pcomp;

    private int pindex;


    public SortedList(Comparator comp) {
        pcomp = comp;
        pdata = new Object[25];
    }


    public SortedList(Comparator comp, Collection collection) {
        pcomp = comp;
        pdata = new Object[collection.size() + 25];
        addAll(collection);
    }


    /**
     * @see java.util.List#get(int)
     */
    public Object get(int index) {
        return pdata[index];
    }


    /**
     * @see java.util.Collection#size()
     */
    public int size() {
        return psize;
    }


    /**
     * @see java.util.Collection#add(java.lang.Object)
     */
    public boolean add(Object o) {
        int index = indexOfIntern(o);
        if (index < 0) {
        	index = -(index + 1);
        }
        try {
            if (index != psize) {
                System.arraycopy(pdata, index, pdata, index + 1, psize - index);
            }
            pdata[index] = o;
        } catch (ArrayIndexOutOfBoundsException ex) {
            Object[] data = new Object[psize + 25];
            System.arraycopy(pdata, 0, data, 0, index);
            System.arraycopy(pdata, index, data, index + 1, psize - index);
            data[index] = o;
            pdata = data;
        }
        pindex = index;
        psize++;
        return true;
    }


    public void addAll(Iterator it) {
        while (it.hasNext()) {
            add(it.next());
        }
    }


    public int getInsertIndex() {
        return pindex;
    }


    public int indexOf(Object o) {
        int index = indexOfIntern(o);
        return index < 0 ? -1 : index;
    }


    /**
     * can return number lower then -1!!!
     */
    protected int indexOfIntern(Object o) {
        int low = 0;
        int high = psize - 1;
        while (low <= high) {
            int mid = (low + high) >> 1;
            Object midVal = pdata[mid];
            int cmp = pcomp.compare(midVal, o);
            if (cmp < 0) {
            	low = mid + 1;
            } else if (cmp > 0) {
            	high = mid - 1;
            } else {
            	return mid; // key found
            }
        }
        return -(low + 1);
    }


    /**
     * @see java.util.Collection#contains(java.lang.Object)
     */
    public boolean contains(Object o) {
        return indexOfIntern(o) >= 0;
    }


    /**
     * @see java.util.List#remove(int)
     */
    public Object remove(int index) {
        if (index >= psize || index < 0) {
        	throw new ArrayIndexOutOfBoundsException(Messages.getString("SortedList.exceptionMessage") + index);  //$NON-NLS-1$
        }

        Object data = pdata[index];

        System.arraycopy(pdata, index + 1, pdata, index, psize - index - 1);
        --psize;
        return data;
    }


    /**
     * @see java.util.Collection#remove(java.lang.Object)
     */
    public boolean remove(Object o) {
        int index = indexOfIntern(o);
        if (index >= 0) {
        	remove(index);
         }
        return index >= 0;
    }

}
