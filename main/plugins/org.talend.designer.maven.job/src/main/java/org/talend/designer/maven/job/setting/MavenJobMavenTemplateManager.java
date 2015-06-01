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
package org.talend.designer.maven.job.setting;

import java.io.InputStream;

import org.talend.designer.maven.template.AbstractMavenTemplateManager;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class MavenJobMavenTemplateManager extends AbstractMavenTemplateManager {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.maven.template.AbstractMavenTemplateManager#readBundleStream(java.lang.String)
     */
    @Override
    public InputStream readBundleStream(String bundleTemplatePath) throws Exception {
        return doProxyReadBundleStream(bundleTemplatePath);
    }

    /**
     * 
     * FIXME, so far, because the maven scripts for standalone job are still in "/org.talend.designer.maven" plugin.
     * Later, should move the scripts to "/org.talend.designer.maven.job" bundle. So do proxy reader here first.
     */
    protected InputStream doProxyReadBundleStream(String bundleTemplatePath) throws Exception {
        String realBundleName = this.bundleName;
        try {
            // find the resources
            this.bundleName = "org.talend.designer.maven"; //$NON-NLS-1$

            return super.readBundleStream(bundleTemplatePath);
        } finally {
            this.bundleName = realBundleName;
        }
    }
}
