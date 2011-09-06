// ============================================================================
//
// Copyright (C) 2006-2011 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.runprocess.prefs;

import org.eclipse.jface.preference.IPreferenceStore;
import org.talend.core.token.AbstractTokenCollector;
import org.talend.core.token.PrefTokenKey;
import org.talend.designer.runprocess.RunProcessPlugin;

import us.monoid.json.JSONObject;

/**
 * ggu class global comment. Detailled comment
 */
public class RunProcessTokenCollector extends AbstractTokenCollector {

    public static final PrefTokenKey NUM_RUN = new PrefTokenKey("runs", "times_run"); //$NON-NLS-1$  //$NON-NLS-2$

    public static final PrefTokenKey NUM_DEBUG_RUN = new PrefTokenKey("debugRuns", "times_debug_run"); //$NON-NLS-1$  //$NON-NLS-2$

    /**
     * ggu RunProcessTokenCollector constructor comment.
     */
    public RunProcessTokenCollector() {
    }

    @Override
    protected void collectProperties(JSONObject propertiesObject) throws Exception {
        IPreferenceStore preferenceStore = RunProcessPlugin.getDefault().getPreferenceStore();
        int numRun = preferenceStore.getInt(NUM_RUN.getPrefKey());
        propertiesObject.put(NUM_RUN.getKey(), numRun);

        int numDebugRun = preferenceStore.getInt(NUM_DEBUG_RUN.getPrefKey());
        propertiesObject.put(NUM_DEBUG_RUN.getKey(), numDebugRun);
    }

}
