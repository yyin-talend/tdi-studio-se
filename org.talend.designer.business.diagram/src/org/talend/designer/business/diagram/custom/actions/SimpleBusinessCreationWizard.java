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

package org.talend.designer.business.diagram.custom.actions;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.ui.IWorkbench;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.commons.utils.VersionUtils;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.properties.BusinessProcessItem;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.ui.images.ECoreImage;
import org.talend.designer.business.diagram.i18n.Messages;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.ui.wizards.RepositoryWizard;

public class SimpleBusinessCreationWizard extends RepositoryWizard {

    private Property property;

    private IPath destinationPath;

    private BusinessProcessItem businessProcessItem;

    private SimpleBusinessCreationWizardPage mainPage;

    public SimpleBusinessCreationWizard(IWorkbench workbench, IPath destinationPath) {

        super(workbench, true);
        this.destinationPath = destinationPath;

        property = PropertiesFactory.eINSTANCE.createProperty();
        property.setAuthor(((RepositoryContext) CorePlugin.getContext().getProperty(Context.REPOSITORY_CONTEXT_KEY)).getUser());
        property.setVersion(VersionUtils.DEFAULT_VERSION);
        property.setStatusCode(""); //$NON-NLS-1$

        businessProcessItem = PropertiesFactory.eINSTANCE.createBusinessProcessItem();
        businessProcessItem.setProperty(property);

        setWindowTitle(Messages.getString("SimpleBusinessCreationWizard.NewBusinessModel")); //$NON-NLS-1$
        setDefaultPageImageDescriptor(ImageProvider.getImageDesc(ECoreImage.BUSINESS_PROCESS_WIZ));
    }

    public void addPages() {
        mainPage = new SimpleBusinessCreationWizardPage(property, destinationPath);
        addPage(mainPage);
    }

    public boolean performFinish() {
        IProxyRepositoryFactory repositoryFactory = ProxyRepositoryFactory.getInstance();
        String nextId = repositoryFactory.getNextId();

        property.setId(nextId);

        DiagramResourceManager diagramResourceManager = new DiagramResourceManager(getWorkbench().getActiveWorkbenchWindow()
                .getActivePage(), new NullProgressMonitor());
        IFile file = diagramResourceManager.createDiagramFile();
        diagramResourceManager.updateFromResource(businessProcessItem, file);

        try {
            repositoryFactory.create(businessProcessItem, mainPage.getDestinationPath());
        } catch (PersistenceException e1) {
            throw new IllegalStateException(e1);
        }

        diagramResourceManager.openEditor(businessProcessItem, file);

        return true;
    }
}