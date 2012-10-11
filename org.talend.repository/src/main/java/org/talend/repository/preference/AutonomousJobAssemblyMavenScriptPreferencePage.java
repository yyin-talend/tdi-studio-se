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
 * DOC ggu class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 55206 2011-02-15 17:32:14Z mhirt $
 * 
 */
public class AutonomousJobAssemblyMavenScriptPreferencePage extends AbstractScriptPreferencePage {

    /**
     * DOC ggu AutonomousJobAssemblyMavenScriptPreferencePage constructor comment.
     */
    public AutonomousJobAssemblyMavenScriptPreferencePage() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.preference.AbstractScriptPreferencePage#getPreferenceKey()
     */
    @Override
    protected String getPreferenceKey() {
        return IRepositoryPrefConstants.MAVEN_SCRIPT_AUTONOMOUSJOB_ASSEMBLY_TEMPLATE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.preference.AbstractScriptPreferencePage#getHeadTitle()
     */
    @Override
    protected String getHeadTitle() {
        return "Assembly script(assembly.xml) for Maven plugin: maven-assembly-plugin"; //$NON-NLS-1$
    }

}
