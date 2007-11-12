// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the  agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//   
// ============================================================================
package org.talend.sbi.engines.client.ui.wizards;

import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.repository.ui.wizards.exportjob.scriptsmanager.JobScriptsManager;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 */
public class JobScriptsManagerFactory {

    private static JobScriptsManagerFactory instance;
    

    public static JobScriptsManagerFactory getInstance() {
        if (instance == null) {
            instance = new JobScriptsManagerFactory();
        }
        return instance;
    }

    public JobScriptsManager createManagerInstance() {
        
        JobScriptsManager manager = null;
        ECodeLanguage language = LanguageManager.getCurrentLanguage();
        if (language == ECodeLanguage.JAVA) {
            manager = new JobJavaScriptsManager();
        } else if (language == ECodeLanguage.PERL) {
            manager = new JobPerlScriptsManager();
        }        
        return manager;
    }

}
