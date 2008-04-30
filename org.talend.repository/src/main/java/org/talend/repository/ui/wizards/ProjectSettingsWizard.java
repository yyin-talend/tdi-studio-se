// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.ui.wizards;

import org.eclipse.jface.wizard.Wizard;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.i18n.Messages;
import org.talend.core.model.general.Project;

/**
 * DOC qwei class global comment. Detailled comment
 */
public class ProjectSettingsWizard extends Wizard {

    private ProjectSettingsWizardPage mainPage;

    public ProjectSettingsWizard() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.Wizard#performFinish()
     */
    @Override
    public boolean performFinish() {
        mainPage.finish();
        return true;
    }

    @Override
    public void addPages() {
        RepositoryContext repositoryContext = (RepositoryContext) CorePlugin.getContext().getProperty(
                Context.REPOSITORY_CONTEXT_KEY);
        Project pro = repositoryContext.getProject();

        mainPage = new ProjectSettingsWizardPage("WizardPage", pro);
        addPage(mainPage);
        setWindowTitle(Messages.getString("ProjectSettingsWizard.EditProjectPageTitle")); //$NON-NLS-1$
    }

}
