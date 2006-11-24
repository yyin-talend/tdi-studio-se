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
package org.talend.repository.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Helper class for lock managment.
 */
public class LocalLockHelper {

    private static final Set<Listerner> LISTENER = new HashSet<Listerner>();

    public static void addListener(Listerner listener) {
        LISTENER.add(listener);
    }

    public static void removeListener(Listerner listener) {
        LISTENER.remove(listener);
    }

    public static void fireLockEvent(String id) {
        for (Listerner listener : LISTENER) {
            listener.lockAdded(id);
        }
    }

    public static void fireUnlockEvent(String id) {
        for (Listerner listener : LISTENER) {
            listener.lockRemoved(id);
        }
    }

    /**
     * Listerner.
     */
    public abstract static class Listerner {

        public void lockAdded(String id) {
        }

        public void lockRemoved(String id) {
        }
    }
}
