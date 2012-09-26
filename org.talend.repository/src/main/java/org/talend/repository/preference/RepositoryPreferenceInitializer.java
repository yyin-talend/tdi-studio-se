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

import java.io.File;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.repository.IRepositoryPrefConstants;
import org.talend.core.model.repository.RepositoryManager;
import org.talend.resource.IResourceService;

/**
 * ggu class global comment. Detailled comment
 */
public class RepositoryPreferenceInitializer extends AbstractPreferenceInitializer {

    private static final String EMPTY_STR = ""; //$NON-NLS-1$

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
     */
    @Override
    public void initializeDefaultPreferences() {
        final IPreferenceStore preferenceStore = RepositoryManager.getPreferenceStore();
        preferenceStore.setDefault(IRepositoryPrefConstants.MERGE_REFERENCE_PROJECT, true);
        preferenceStore.setDefault(IRepositoryPrefConstants.ANT_SCRIPT_TEMPLATE,
                getScriptTemplate(IRepositoryPrefConstants.ANT_SCRIPT_TEMPLATE));
        preferenceStore.setDefault(IRepositoryPrefConstants.MAVEN_SCRIPT_TEMPLATE,
                getScriptTemplate(IRepositoryPrefConstants.MAVEN_SCRIPT_TEMPLATE));
        preferenceStore.setDefault(IRepositoryPrefConstants.MAVEN_OSGI_SCRIPT_TEMPLATE,
                getScriptTemplate(IRepositoryPrefConstants.MAVEN_OSGI_SCRIPT_TEMPLATE));
    }

    private String getScriptTemplate(String type) {
        IResourceService resourceService = (IResourceService) GlobalServiceRegister.getDefault().getService(
                IResourceService.class);
        if (resourceService == null) {
            return EMPTY_STR;
        }
        File templateScriptFile = null;
        if (type == IRepositoryPrefConstants.MAVEN_SCRIPT_TEMPLATE) {
            templateScriptFile = new File(resourceService.getMavenScriptFilePath("pom.xml"));
        } else if (type == IRepositoryPrefConstants.MAVEN_OSGI_SCRIPT_TEMPLATE) {
            templateScriptFile = new File(resourceService.getMavenScriptFilePath("pom_osgi.xml"));
        } else {

            templateScriptFile = new File(resourceService.getAntScriptFilePath());
        }
        if (!templateScriptFile.exists()) {
            return EMPTY_STR;
        }

        SAXReader saxReader = new SAXReader();
        Document document = null;
        try {
            document = saxReader.read(templateScriptFile);
        } catch (DocumentException e) {
            ExceptionHandler.process(e);
        }

        return document.asXML();
    }

}
