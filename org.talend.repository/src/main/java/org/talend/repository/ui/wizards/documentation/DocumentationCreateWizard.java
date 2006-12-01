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
package org.talend.repository.ui.wizards.documentation;

import java.io.IOException;

import org.eclipse.core.runtime.IPath;
import org.eclipse.ui.IWorkbench;
import org.talend.commons.exception.MessageBoxExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.VersionUtils;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.properties.ByteArray;
import org.talend.core.model.properties.DocumentationItem;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.ui.images.EImage;
import org.talend.core.ui.images.ImageProvider;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.ui.wizards.RepositoryWizard;

/**
 * Wizard to create a new IDocumentation. <br/>
 * 
 * $Id$
 * 
 */
public class DocumentationCreateWizard extends RepositoryWizard implements IDocumentationContext {

    /** Main wizard page. */
    private DocumentationPage mainPage;

    private IPath docFilePath;

    private String docOriginalName;

    private String docOriginalExtension;

    private Property property;

    private DocumentationItem documentationItem;

    /**
     * Constructs a new DocumentationCreateWizard.
     * 
     * @param destinationPath Path in the repository where the documentation must be saved.
     */
    public DocumentationCreateWizard(IWorkbench workbench, IPath destinationPath) {
        super(workbench, true);

        pathToSave = destinationPath;

        setWindowTitle("New Documentation");
        setDefaultPageImageDescriptor(ImageProvider.getImageDesc(EImage.DOCUMENTATION_WIZ));
        setNeedsProgressMonitor(true);

        property = PropertiesFactory.eINSTANCE.createProperty();
        property.setAuthor(((RepositoryContext) CorePlugin.getContext().getProperty(Context.REPOSITORY_CONTEXT_KEY)).getUser());
        property.setVersion(VersionUtils.DEFAULT_VERSION);
        property.setStatusCode("");

        documentationItem = PropertiesFactory.eINSTANCE.createDocumentationItem();
        documentationItem.setProperty(property);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.Wizard#addPages()
     */
    @Override
    public void addPages() {
        mainPage = new DocumentationPage(property, pathToSave);
        mainPage.setDescription("Create a new Documentation");
        mainPage.setUpdate(false);
        addPage(mainPage);

        setNeedsProgressMonitor(true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.Wizard#performFinish()
     */
    @Override
    public boolean performFinish() {
        boolean created = false;
        ProxyRepositoryFactory repositoryFactory = ProxyRepositoryFactory.getInstance();

        try {
            property.setId(repositoryFactory.getNextId());

            ByteArray byteArray = PropertiesFactory.eINSTANCE.createByteArray();
            byteArray.setInnerContentFromFile(getDocFilePath().toFile());

            documentationItem.setName(getDocFilePath().removeFileExtension().lastSegment());
            documentationItem.setExtension(getDocFilePath().getFileExtension());
            documentationItem.setContent(byteArray);

            // PTODO MHE
            // doc.setOriginalDocPath(getDocFilePath());

            repositoryFactory.create(documentationItem, pathToSave);

            created = true;
        } catch (PersistenceException e) {
            MessageBoxExceptionHandler.process(e);
        } catch (IOException ioe) {
            MessageBoxExceptionHandler.process(ioe);
        }
        return created;
    }

    /**
     * Getter for docOriginalName.
     * 
     * @return the docOriginalName
     */
    public String getDocOriginalName() {
        if (getDocFilePath() != null) {
            docOriginalName = getDocFilePath().lastSegment();
        } else {
            docOriginalName = property.getLabel();
        }
        return docOriginalName;
    }

    /**
     * Sets the docOriginalName.
     * 
     * @param docOriginalName the docOriginalName to set
     */
    public void setDocOriginalName(String docOriginalName) {
        // do nothing
    }

    public static final String DEFAULT_FILENAME_EXT = "doc";

    /**
     * Getter for docOriginalExtension.
     * 
     * @return the docOriginalExtension
     */
    public String getDocOriginalExtension() {
        if (getDocFilePath() != null) {
            this.docOriginalExtension = getDocFilePath().getFileExtension();
        } else {
            this.docOriginalExtension = DEFAULT_FILENAME_EXT;
        }
        return "." + this.docOriginalExtension;
    }

    /**
     * Sets the docOriginalExtension.
     * 
     * @param docOriginalExtension the docOriginalExtension to set
     */
    public void setDocOriginalExtension(String docOriginalExtension) {
        // do nothing
    }

    /**
     * Getter for docFilePath.
     * 
     * @return the docFilePath
     */
    public IPath getDocFilePath() {
        return this.docFilePath;
    }

    /**
     * Sets the docFilePath.
     * 
     * @param docFilePath the docFilePath to set
     */
    public void setDocFilePath(IPath docFilePath) {
        this.docFilePath = docFilePath;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.wizards.IDocumentationContext#isDocNameEditable()
     */
    public boolean isDocNameEditable() {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.wizards.IDocumentationContext#isDocVersionEditable()
     */
    public boolean isDocVersionEditable() {
        return true;
    }
}
