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

/**
 * DOC ycbai class global comment. Detailled comment
 */
public class AutonomousJobAntScriptPreferencePage extends AbstractScriptPreferencePage {

    /**
     * DOC ggu AntScriptPreferencePage constructor comment.
     */
    public AutonomousJobAntScriptPreferencePage() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.preference.AbstractScriptPreferencePage#getPreferenceKey()
     */
    @Override
    protected String getPreferenceKey() {
        return IRepositoryPrefConstants.ANT_SCRIPT_TEMPLATE;
    }

}
