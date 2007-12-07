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
package org.talend.designer.core.ui.wizards;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.commons.utils.VersionUtils;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.ui.images.ECoreImage;
import org.talend.designer.core.DesignerPlugin;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;
import org.talend.designer.core.model.utils.emf.talendfile.TalendFileFactory;
import org.talend.repository.model.IProxyRepositoryFactory;

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

    private IProxyRepositoryFactory repositoryFactory;

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
                .getUser());
        this.property.setVersion(VersionUtils.DEFAULT_VERSION);
        this.property.setStatusCode(""); //$NON-NLS-1$

        processItem = PropertiesFactory.eINSTANCE.createProcessItem();

        processItem.setProperty(property);

        repositoryFactory = DesignerPlugin.getDefault().getRepositoryService().getProxyRepositoryFactory();

        setDefaultPageImageDescriptor(ImageProvider.getImageDesc(ECoreImage.PROCESS_WIZ));
    }

    /**
     * @see org.eclipse.jface.wizard.Wizard#addPages()
     */
    @Override
    public void addPages() {
        mainPage = new NewProcessWizardPage(property, path);
        addPage(mainPage);
        setWindowTitle(Messages.getString("NewProcessWizard.windowTitle")); //$NON-NLS-1$
    }

    /**
     * @see org.eclipse.jface.wizard.Wizard#performFinish()
     */
    @Override
    public boolean performFinish() {
        try {

            property.setId(repositoryFactory.getNextId());

            ProcessType process = TalendFileFactory.eINSTANCE.createProcessType();

            processItem.setProcess(process);

            repositoryFactory.create(processItem, mainPage.getDestinationPath());

        } catch (PersistenceException e) {
            MessageDialog.openError(getShell(), Messages.getString("NewProcessWizard.failureTitle"), Messages //$NON-NLS-1$
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
