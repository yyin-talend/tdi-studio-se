// ============================================================================
//
// Copyright (C) 2006-2012 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.preference;

import org.talend.core.model.repository.IRepositoryPrefConstants;
import org.talend.repository.i18n.Messages;

/**
 * DOC ycbai class global comment. Detailled comment
 */
public class OSGIBundleMavenScriptPreferencePage extends AbstractScriptPreferencePage {

    public OSGIBundleMavenScriptPreferencePage() {
        super();
    }

    @Override
    protected String getPreferenceKey() {
        return IRepositoryPrefConstants.MAVEN_OSGI_SCRIPT_TEMPLATE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.preference.AbstractScriptPreferencePage#getHeadTitle()
     */
    @Override
    protected String getHeadTitle() {
        return Messages.getString("OSGIBundleMavenScriptPreferencePage_Title"); //$NON-NLS-1$
    }
}
