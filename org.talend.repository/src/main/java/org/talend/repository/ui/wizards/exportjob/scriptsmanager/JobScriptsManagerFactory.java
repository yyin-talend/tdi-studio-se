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
package org.talend.repository.ui.wizards.exportjob.scriptsmanager;

import org.talend.core.language.ECodeLanguage;
import org.talend.repository.ui.wizards.exportjob.JavaJobScriptsExportWSWizardPage;

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

    public JobScriptsManager createManagerInstance(ECodeLanguage language, String exportType) {

        JobScriptsManager manager = null;
        if (language == ECodeLanguage.JAVA) {
            if (exportType.endsWith(JavaJobScriptsExportWSWizardPage.EXPORTTYPE_POJO)) {
                manager = new JobJavaScriptsManager();
            } else if (exportType.endsWith(JavaJobScriptsExportWSWizardPage.EXPORTTYPE_WSWAR)) {
                manager = new JobJavaScriptsWSManager();
            } else if (exportType.endsWith(JavaJobScriptsExportWSWizardPage.EXPORTTYPE_WSZIP)) {
                manager = new JobJavaScriptsWSManager();
            }

        } else if (language == ECodeLanguage.PERL) {
            manager = new JobPerlScriptsManager();
        }
        return manager;
    }

}
