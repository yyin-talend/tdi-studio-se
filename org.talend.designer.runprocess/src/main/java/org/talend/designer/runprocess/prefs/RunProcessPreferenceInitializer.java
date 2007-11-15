// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
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

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.talend.designer.runprocess.RunProcessPlugin;

/**
 * 
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 */
public class RunProcessPreferenceInitializer extends AbstractPreferenceInitializer {

    public RunProcessPreferenceInitializer() {
        super();
    }

    @Override
    public void initializeDefaultPreferences() {
        IPreferenceStore prefs = getPluginPreferenceStore();

        prefs.putValue(RunProcessPrefsConstants.CLIENT_STATS_PORT_BOUND1, "3334");
        prefs.putValue(RunProcessPrefsConstants.CLIENT_STATS_PORT_BOUND2, "4333");
        prefs.putValue(RunProcessPrefsConstants.CLIENT_TRACE_PORT_BOUND1, "4334");
        prefs.putValue(RunProcessPrefsConstants.CLIENT_TRACE_PORT_BOUND2, "5333");
        prefs.setDefault(RunProcessPrefsConstants.ISCLEARBEFORERUN, true);
        prefs.setDefault(RunProcessPrefsConstants.STRACESTIME, 1000);
        prefs.setDefault(RunProcessPrefsConstants.VMARGUMENTS, " -Xms256M -Xmx1024M");
    }

    /**
     * DOC amaumont Comment method "getPluginPreferenceStore".
     * 
     * @return
     */
    public static IPreferenceStore getPluginPreferenceStore() {
        IPreferenceStore prefs = RunProcessPlugin.getDefault().getPreferenceStore();
        return prefs;
    }

}
