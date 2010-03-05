// ============================================================================
//
// Copyright (C) 2006-2010 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.ui.wizards.newproject.copyfromeclipse;

import org.eclipse.core.resources.IPathVariableManager;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.internal.wizards.datatransfer.WizardProjectsImportPage;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.CorePlugin;

/**
 * DOC zhangchao.wang class global comment. Detailled comment
 */
public class TalendWizardProjectsImportPage extends WizardProjectsImportPage {

    public TalendWizardProjectsImportPage() {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.internal.wizards.datatransfer.WizardProjectsImportPage#updateProjectsList(java.lang.String)
     */
    @Override
    public void updateProjectsList(String sourcePath) {
        String destinationJavaPath = null;
        String destinationPerlPath = null;

        try {
            if (!("".equals(sourcePath))) { //$NON-NLS-1$
                destinationJavaPath = CorePlugin.getDefault().getLibrariesService().getJavaLibrariesPath();
                destinationPerlPath = CorePlugin.getDefault().getLibrariesService().getPerlLibrariesPath();

                IPathVariableManager pathVariableManager = ResourcesPlugin.getWorkspace().getPathVariableManager();
                pathVariableManager.setValue(EXTERNAL_LIB_JAVA_PATH, new Path(destinationJavaPath));
                pathVariableManager.setValue(EXTERNAL_LIB_PERL_PATH, new Path(destinationPerlPath));

                super.updateProjectsList(sourcePath);

            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
    }

    public final static String EXTERNAL_LIB_JAVA_PATH = "external_lib_java_path"; //$NON-NLS-1$

    public final static String EXTERNAL_LIB_PERL_PATH = "external_lib_perl_path"; //$NON-NLS-1$

}
