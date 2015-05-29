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
package org.talend.designer.maven.job.setting.project.initializer;

import org.eclipse.jface.preference.IPreferenceStore;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.runtime.projectsetting.IProjectSettingPreferenceConstants;
import org.talend.core.runtime.projectsetting.IProjectSettingTemplateConstants;
import org.talend.designer.maven.job.MavenJobPlugin;
import org.talend.designer.maven.model.TalendMavenConstants;
import org.talend.designer.maven.template.MavenTemplateManager;
import org.talend.designer.maven.ui.setting.project.initializer.AbstractProjectPreferenceInitializer;

/**
 * DOC ggu class global comment. Detailled comment
 * 
 */
public class MavenJobScriptsProjectSettingInitializer extends AbstractProjectPreferenceInitializer {

    @Override
    protected IPreferenceStore getPreferenceStore() {
        return MavenJobPlugin.getDefault().getProjectPreferenceManager().getPreferenceStore();
    }

    @Override
    protected void initializeFields(IPreferenceStore preferenceStore) {
        super.initializeFields(preferenceStore);

        try {

            // FIXME, later, should move the template from maven to maven.job plugin.
            String pomJobContent = MavenTemplateManager
                    .getBundleTemplateContent(IProjectSettingTemplateConstants.POM_JOB_TEMPLATE_FILE_NAME);
            preferenceStore.setDefault(IProjectSettingPreferenceConstants.TEMPLATE_STANDALONE_JOB_POM, pomJobContent);

            String assemblyContent = MavenTemplateManager
                    .getBundleTemplateContent(IProjectSettingTemplateConstants.ASSEMBLY_JOB_TEMPLATE_FILE_NAME);
            preferenceStore.setDefault(IProjectSettingPreferenceConstants.TEMPLATE_STANDALONE_JOB_ASSEMBLY, assemblyContent);

            setDefault(preferenceStore, IProjectSettingPreferenceConstants.TEMPLATE_OSGI_BUNDLE_POM,
                    IProjectSettingTemplateConstants.PATH_RESOURCES_TEMPLATES + '/' + TalendMavenConstants.OSGI_BUNDLE_NAME + '/'
                            + IProjectSettingTemplateConstants.POM_JOB_TEMPLATE_FILE_NAME);
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }

    }

}
