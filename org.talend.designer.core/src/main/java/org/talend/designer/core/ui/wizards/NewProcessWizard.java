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
package org.talend.designer.core.ui.wizards;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.general.Version;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.ui.ImageProvider;
import org.talend.core.ui.ImageProvider.EImage;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;
import org.talend.designer.core.model.utils.emf.talendfile.TalendFileFactory;
import org.talend.repository.model.IRepositoryFactory;
import org.talend.repository.model.RepositoryFactoryProvider;

/**
 * Wizard for the creation of a new project. <br/>
 * 
 * $Id$
 * 
 */
public class NewProcessWizard extends Wizard {

    /** Main page. */
    private NewProcessWizardPage mainPage;

    /** Created project. */
    private ProcessItem processItem;

    private Property property;

    private IPath path;

    /**
     * Constructs a new NewProjectWizard.
     * 
     * @param author Project author.
     * @param server
     * @param password
     */
    public NewProcessWizard(IPath path) {
        super();
        this.path = path;

        this.property = PropertiesFactory.eINSTANCE.createProperty();
        this.property.setAuthor(((RepositoryContext) CorePlugin.getContext().getProperty(Context.REPOSITORY_CONTEXT_KEY))
                .getUser().getEmfUser());
        this.property.setVersion(new Version().toString());
        this.property.setStatusCode("");

        processItem = PropertiesFactory.eINSTANCE.createProcessItem();

        processItem.setProperty(property);

        setDefaultPageImageDescriptor(ImageProvider.getImageDesc(EImage.PROCESS_WIZ));
    }

    /**
     * @see org.eclipse.jface.wizard.Wizard#addPages()
     */
    @Override
    public void addPages() {
        mainPage = new NewProcessWizardPage(property, path);
        addPage(mainPage);
        setWindowTitle(Messages.getString("NewProcessWizard.windowTitle"));
    }

    /**
     * @see org.eclipse.jface.wizard.Wizard#performFinish()
     */
    @Override
    public boolean performFinish() {
        RepositoryContext repositoryContext = (RepositoryContext) org.talend.core.CorePlugin.getContext().getProperty(
                org.talend.core.context.Context.REPOSITORY_CONTEXT_KEY);
        IRepositoryFactory repositoryFactory = RepositoryFactoryProvider.getInstance(repositoryContext);
        try {
            property.setId(repositoryFactory.getNextId());

            ProcessType process = TalendFileFactory.eINSTANCE.createProcessType();

            processItem.setProcess(process);

            repositoryFactory.create(processItem, path);
        } catch (PersistenceException e) {
            MessageDialog.openError(getShell(), Messages.getString("NewProcessWizard.failureTitle"), Messages
                    .getString("NewProcessWizard.failureText")); //$NON-NLS-1$ //$NON-NLS-2$
            ExceptionHandler.process(e);
        }

        return processItem != null;
    }

    /**
     * Getter for project.
     * 
     * @return the project
     */
    public ProcessItem getProcess() {
        return this.processItem;
    }
}
