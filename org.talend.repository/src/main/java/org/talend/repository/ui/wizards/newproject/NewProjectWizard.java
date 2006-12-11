// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
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
package org.talend.repository.ui.wizards.newproject;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.talend.commons.exception.MessageBoxExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.general.Project;
import org.talend.core.model.temp.ECodeLanguage;
import org.talend.core.ui.images.EImage;
import org.talend.core.ui.images.ImageProvider;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.ProxyRepositoryFactory;

/**
 * Wizard for the creation of a new project. <br/>
 * 
 * $Id$
 * 
 */
public class NewProjectWizard extends Wizard {

    /** Main page. */
    private NewProjectWizardPage mainPage;

    private Project project;

    /**
     * Constructs a new NewProjectWizard.
     * 
     * @param author Project author.
     * @param server
     * @param password
     * @param port2
     */
    public NewProjectWizard() {
        super();
        setDefaultPageImageDescriptor(ImageProvider.getImageDesc(EImage.PROJECT_WIZ));
    }

    /**
     * @see org.eclipse.jface.wizard.Wizard#addPages()
     */
    @Override
    public void addPages() {
        mainPage = new NewProjectWizardPage();
        addPage(mainPage);
        setWindowTitle(Messages.getString("NewProjectWizard.windowTitle"));
    }

    /**
     * @see org.eclipse.jface.wizard.Wizard#performFinish()
     */
    @Override
    public boolean performFinish() {
        IProxyRepositoryFactory repositoryFactory = ProxyRepositoryFactory.getInstance();
        Context ctx = CorePlugin.getContext();
        RepositoryContext repositoryContext = (RepositoryContext) ctx.getProperty(Context.REPOSITORY_CONTEXT_KEY);
        try {
            project = repositoryFactory.createProject(mainPage.getName(), mainPage.getDescription(), ECodeLanguage
                    .getCodeLanguage(mainPage.getLanguage()), repositoryContext.getUser());
            return true;
        } catch (PersistenceException e) {
            MessageDialog.openError(getShell(), Messages.getString("NewProjectWizard.failureTitle"), Messages
                    .getString("NewProjectWizard.failureText")); //$NON-NLS-1$ //$NON-NLS-2$
            MessageBoxExceptionHandler.process(e);
            return false;
        }
    }

    /**
     * Getter for project.
     * 
     * @return the project
     */
    public Project getProject() {
        return this.project;
    }

}
