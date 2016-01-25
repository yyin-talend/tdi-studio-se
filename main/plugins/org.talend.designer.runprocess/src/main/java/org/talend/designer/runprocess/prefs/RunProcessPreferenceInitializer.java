// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
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

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.core.GlobalServiceRegister;
import org.talend.designer.runprocess.IRunProcessService;
import org.talend.designer.runprocess.RunProcessPlugin;

/**
 * 
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 */
public class RunProcessPreferenceInitializer extends AbstractPreferenceInitializer {

    protected static final String EMPTY_STR = ""; //$NON-NLS-1$

    private static final String commonLogFilePath = "log/common-logging.properties_template"; //$NON-NLS-1$

    private static final String log4jFilePath = "log/log4j.properties_template"; //$NON-NLS-1$

    public RunProcessPreferenceInitializer() {
        super();
    }

    @Override
    public void initializeDefaultPreferences() {
        IPreferenceStore prefs = getPluginPreferenceStore();
        prefs.setDefault(RunProcessPrefsConstants.CLIENT_STATS_PORT_BOUND1, "3334"); //$NON-NLS-1$
        prefs.setDefault(RunProcessPrefsConstants.CLIENT_STATS_PORT_BOUND2, "4333"); //$NON-NLS-1$
        prefs.setDefault(RunProcessPrefsConstants.CLIENT_TRACE_PORT_BOUND1, "4334"); //$NON-NLS-1$
        prefs.setDefault(RunProcessPrefsConstants.CLIENT_TRACE_PORT_BOUND2, "5333"); //$NON-NLS-1$

        // added by wzhang for feature 7428.
        prefs.setDefault(RunProcessPrefsConstants.ISCLEARBEFORERUN, true);
        prefs.setDefault(RunProcessPrefsConstants.ISSAVEBEFORERUN, true);
        prefs.setDefault(RunProcessPrefsConstants.ISSTATISTICSRUN, true);
        prefs.setDefault(RunProcessPrefsConstants.STRACESTIME, 1000);
        prefs.setDefault(RunProcessPrefsConstants.VMARGUMENTS, " -Xms256M -Xmx1024M"); //$NON-NLS-1$

        // for logs
        prefs.setDefault(RunProcessPrefsConstants.COMMON_LOGGING_PROPERTIES_TEMPLATE, getLogTemplate(commonLogFilePath));
        prefs.setDefault(RunProcessPrefsConstants.LOG4J_PROPERTIES_TEMPLATE, getLogTemplate(log4jFilePath));
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

    protected String getLogTemplate(String logPath) {
        IRunProcessService service = null;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IRunProcessService.class)) {
            service = (IRunProcessService) GlobalServiceRegister.getDefault().getService(IRunProcessService.class);
        }
        if (service == null) {
            return EMPTY_STR;
        }

        File templateFile = new File(service.getResourceFilePath(logPath));
        if (!templateFile.exists()) {
            return EMPTY_STR;
        }

        return getLogTemplateString(templateFile);
    }

    protected String getLogTemplateString(File templateScriptFile) {
        if (templateScriptFile != null && templateScriptFile.exists()) {
            try {
                return new Scanner(templateScriptFile).useDelimiter("\\A").next(); //$NON-NLS-1$
            } catch (FileNotFoundException e) {
                ExceptionHandler.process(e);
            }
        }
        return EMPTY_STR;
    }

}
