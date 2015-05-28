// ============================================================================
//
// Copyright (C) 2006-2015 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.maven.job.setting.project.page.osgi;

import org.talend.core.runtime.projectsetting.IProjectSettingPreferenceConstants;
import org.talend.designer.maven.job.i18n.Messages;
import org.talend.designer.maven.job.setting.project.page.AbstractMavenJobProjectSettingPage;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class OSGiBundlePomProjectSettingPage extends AbstractMavenJobProjectSettingPage {

    @Override
    protected String getPreferenceKey() {
        return IProjectSettingPreferenceConstants.TEMPLATE_OSGI_BUNDLE_POM;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.preference.AbstractScriptPreferencePage#getHeadTitle()
     */
    @Override
    protected String getHeadTitle() {
        return Messages.getString("OsgiBundleMavenProjectSettingPage_Title"); //$NON-NLS-1$
    }

}
