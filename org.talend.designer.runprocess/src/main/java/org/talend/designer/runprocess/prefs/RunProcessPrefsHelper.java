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
package org.talend.designer.runprocess.prefs;

import org.eclipse.jface.preference.IPreferenceStore;
import org.talend.designer.runprocess.RunProcessPlugin;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 */
public final class RunProcessPrefsHelper {

    private static RunProcessPrefsHelper instance;

    public static RunProcessPrefsHelper getInstance() {
        if (instance == null) {
            instance = new RunProcessPrefsHelper();
        }
        return instance;
    }

    /**
     * DOC amaumont RunRemoteProcessPrefsHelper constructor comment.
     */
    private RunProcessPrefsHelper() {
        super();
    }

    public IPreferenceStore getPreferenceStore() {
        return RunProcessPlugin.getDefault().getPreferenceStore();
    }

    public int getClientStatsPortBound1() {
        return getPreferenceStore().getInt(RunProcessPrefsConstants.CLIENT_STATS_PORT_BOUND1);
    }
    
    public int getClientStatsPortBound2() {
        return getPreferenceStore().getInt(RunProcessPrefsConstants.CLIENT_STATS_PORT_BOUND2);
    }
    
    public int getClientTracePortBound1() {
        return getPreferenceStore().getInt(RunProcessPrefsConstants.CLIENT_TRACE_PORT_BOUND1);
    }
    
    public int getClientTracePortBound2() {
        return getPreferenceStore().getInt(RunProcessPrefsConstants.CLIENT_TRACE_PORT_BOUND2);
    }
    
}
