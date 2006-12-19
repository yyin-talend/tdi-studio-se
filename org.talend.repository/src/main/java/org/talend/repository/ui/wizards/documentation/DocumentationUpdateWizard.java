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
import java.io.InputStream;

import org.eclipse.core.runtime.IPath;
import org.eclipse.ui.IWorkbench;
import org.talend.commons.exception.MessageBoxExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.core.model.properties.DocumentationItem;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.core.ui.images.ECoreImage;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.ui.wizards.RepositoryWizard;

/**
 * Wizard to update a IDocumentation. <br/>
 * 
 * $Id$
 * 
 */
public class DocumentationUpdateWizard extends RepositoryWizard implements IDocumentationContext {

    /** Main wizard page. */
    private DocumentationPage mainPage;

    private DocumentationItem documentationItem;

    private IPath docFilePath;

    /**
     * Constructs a new DocumentationUpdateWizard.
     */
    public DocumentationUpdateWizard(IWorkbench workbench, IRepositoryObject object, IPath destinationPath) {
        super(workbench, false);
        this.pathToSave = destinationPath;
        this.documentationItem = (DocumentationItem) object.getProperty().getItem();

        this.repositoryObject = object;

        setWindowTitle("Update Documentation");
        setDefaultPageImageDescriptor(ImageProvider.getImageDesc(ECoreImage.DOCUMENTATION_WIZ));
        setNeedsProgressMonitor(false);
        initLockStrategy();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.Wizard#addPages()
     */
    @Override
    public void addPages() {
        mainPage = new DocumentationPage(documentationItem.getProperty(), pathToSave);
        mainPage.setDescription("Update the Documentation");
        mainPage.setUpdate(true);
        mainPage.setEditPath(false);
        addPage(mainPage);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.Wizard#performFinish()
     */
    @Override
    public boolean performFinish() {
        boolean updated = false;
        InputStream stream = null;
        IProxyRepositoryFactory repositoryFactory = ProxyRepositoryFactory.getInstance();
        try {
            if (getDocFilePath() != null && getDocFilePath().segmentCount() != 0) {
                documentationItem.getContent().setInnerContentFromFile(getDocFilePath().toFile());
                documentationItem.setName(getDocFilePath().removeFileExtension().lastSegment());
                documentationItem.setExtension(getDocFilePath().getFileExtension());
            }

            repositoryFactory.save(documentationItem);
            closeLockStrategy();
            updated = true;
        } catch (PersistenceException e) {
            MessageBoxExceptionHandler.process(e);
        } catch (IOException ioe) {
            MessageBoxExceptionHandler.process(ioe);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException ioe) {
                    // Do nothing
                }
            }
        }
        return updated;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.wizards.IDocumentationContext#getDocFilePath()
     */
    public IPath getDocFilePath() {
        return docFilePath;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.wizards.IDocumentationContext#setDocFilePath(org.eclipse.core.runtime.IPath)
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

    /**
     * Getter for docOriginalExtension.
     * 
     * @return the docOriginalExtension
     */
    public String getDocOriginalExtension() {
        if (getDocFilePath() != null) {
            return "." + getDocFilePath().getFileExtension();
        } else {
            return "." + documentationItem.getExtension();
        }
    }

    /**
     * Sets the docOriginalExtension.
     * 
     * @param docOriginalExtension the docOriginalExtension to set
     */
    public void setDocOriginalExtension(String docOriginalExtension) {
        documentationItem.setExtension(docOriginalExtension);
    }

    /**
     * Getter for docOriginalName.
     * 
     * @return the docOriginalName
     */
    public String getDocOriginalName() {
        if (getDocFilePath() != null) {
            return getDocFilePath().lastSegment();
        } else {
            return documentationItem.getName() + "." + documentationItem.getExtension();
        }
    }

    /**
     * Sets the docOriginalName.
     * 
     * @param docOriginalName the docOriginalName to set
     */
    public void setDocOriginalName(String docOriginalName) {
        documentationItem.setName(docOriginalName);
    }
}
