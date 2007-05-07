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
