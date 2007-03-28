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
package org.talend.repository.model;

import java.util.ArrayList;
import java.util.Collection;

import org.talend.core.model.repository.IRepositoryObject;

/**
 * Used to manage IRepositoryObject list with allowing or not of all versions of each object. Constructor parameter
 * <code>allVersion</code> specify, if <code>false</code>, that only the most recent version of an object must be
 * kept. This functionnality is realized by the <code>add</code> method.<br/>
 * 
 * PTODO SML Overide other way to put object in the list
 * 
 * $Id$
 * 
 */
public class VersionList extends ArrayList<IRepositoryObject> {

    private static final long serialVersionUID = 6286880354826726354L;

    private boolean allVersion;

    /**
     * DEfault constructor.
     * 
     * @param allVersion - <code>true</code> if all versions of each object must be kept, <code>false</code>
     * otherwise.
     */
    public VersionList(boolean allVersion) {
        super();
        this.allVersion = allVersion;
    }

    /**
     * Add object in the list following the versionning strategy specify by the <code>allVersion</code> field.<br/>
     * 
     * If <code>false</code>, method will add object only if
     * <ul>
     * <li> no object with this id could be found or</li>
     * <li>an object with this id can be found and new object version is most recent than old object. Then old is
     * removed and new is added.</li>
     * </ul>
     * So only the most recent version of the object is kept.
     * 
     * @param o - the object to add
     * @return see Collection.add contract
     * @see Collection.add
     */
    @Override
    public boolean add(IRepositoryObject o) {
        if (allVersion) {
            return super.add(o);
        } else {
            IRepositoryObject o2 = get(o);
            if (o2 == null || o.getVersion().compareTo(o2.getVersion()) > 0) {
                this.remove(o2);
                return super.add(o);
            } else {
                return false;
            }
        }
    }

    /**
     * 
     * Returns the first object with the same id as parameter object or <code>null</code> if no object with this id
     * can be found.
     * 
     * @param o
     * @return
     */
    private IRepositoryObject get(IRepositoryObject o) {
        for (IRepositoryObject current : this) {
            if (current.getId().equals(o.getId())) {
                return current;
            }
        }
        return null;
    }
}
