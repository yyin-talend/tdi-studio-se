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
package org.talend.repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Processes the changes of the repository.
 * 
 * $Id: ChangeProcessor.java 2006-12-31下午01:15:32 bqian $
 * 
 */
public class ChangeProcessor {

    List<IRepositoryChangedListener> listeners = new ArrayList<IRepositoryChangedListener>();

    public ChangeProcessor() {

    }

    /*
     * Need to clone defensively the listener information, in case some listener is reacting to some notification
     * iteration by adding/changing/removing any of the other (for example, if it deregisters itself).
     */
    public synchronized void addRepositoryChangedListener(IRepositoryChangedListener listener) {
        if (listeners.contains(listener)) {
            return;
        }
        listeners.add(listener);
    }

    public synchronized void removeRepositoryChangedListener(IRepositoryChangedListener listener) {
        listeners.remove(listener);
    }

    public synchronized void repositoryChanged(IRepositoryElementDelta delta) {
        for (IRepositoryChangedListener listener : listeners) {
            final RepositoryChangedEvent event = new RepositoryChangedEvent(delta);
            listener.repositoryChanged(event);
        }
    }

    /**
     * qzhang Comment method "registerRepositoryChangedListenerAsFirst".
     * 
     * @param listener
     */
    public void registerRepositoryChangedListenerAsFirst(IRepositoryChangedListener listener) {
        if (listeners.contains(listener)) {
            return;
        }
        listeners.add(0, listener);

    }

}
