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
import org.talend.repository.i18n.Messages;
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

        setWindowTitle(Messages.getString("DocumentationUpdateWizard.windowTitle")); //$NON-NLS-1$
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
        mainPage.setDescription(Messages.getString("DocumentationUpdateWizard.mainPageDescription")); //$NON-NLS-1$
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
            return "." + getDocFilePath().getFileExtension(); //$NON-NLS-1$
        } else {
            return "." + documentationItem.getExtension(); //$NON-NLS-1$
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
            return documentationItem.getName() + "." + documentationItem.getExtension(); //$NON-NLS-1$
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
