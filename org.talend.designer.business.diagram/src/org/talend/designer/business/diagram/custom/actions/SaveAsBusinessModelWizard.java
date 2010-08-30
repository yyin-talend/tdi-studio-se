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
package org.talend.designer.business.diagram.custom.actions;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.EditorPart;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.commons.utils.VersionUtils;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.properties.BusinessProcessItem;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.ui.images.ECoreImage;
import org.talend.designer.business.model.business.BusinessFactory;
import org.talend.designer.business.model.business.BusinessProcess;
import org.talend.designer.business.model.business.diagram.part.BusinessDiagramEditor;
import org.talend.designer.core.DesignerPlugin;
import org.talend.repository.editor.RepositoryEditorInput;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IRepositoryService;
import org.talend.repository.model.RepositoryNode;

/**
 * Wizard for the creation of a new project. <br/>
 * 
 * $Id: NewProcessWizard.java 46332 2010-08-05 06:48:56Z cli $
 * 
 */
public class SaveAsBusinessModelWizard extends Wizard {

    /** Main page. */
    private SimpleBusinessCreationWizardPage mainPage;

    /** Created project. */
    private BusinessProcessItem businessProcessItem;

    private Property property;

    private IPath path;

    private IProxyRepositoryFactory repositoryFactory;

    private RepositoryEditorInput repositoryEditorInput;

    public SaveAsBusinessModelWizard(EditorPart editorPart) {

        this.repositoryEditorInput = ((BusinessDiagramEditor) editorPart).getDiagramEditorInput();

        RepositoryNode repositoryNode = repositoryEditorInput.getRepositoryNode();
        // see: RepositoryEditorInput.setRepositoryNode(IRepositoryNode repositoryNode)
        if (repositoryNode == null) {
            repositoryNode = (RepositoryNode) CorePlugin.getDefault().getRepositoryService().getRepositoryNode(
                    repositoryEditorInput.getItem().getProperty().getId(), false);
        }

        IRepositoryService service = DesignerPlugin.getDefault().getRepositoryService();
        this.path = service.getRepositoryPath((RepositoryNode) repositoryNode);

        this.property = PropertiesFactory.eINSTANCE.createProperty();
        this.property.setAuthor(((RepositoryContext) CorePlugin.getContext().getProperty(Context.REPOSITORY_CONTEXT_KEY))
                .getUser());
        this.property.setVersion(VersionUtils.DEFAULT_VERSION);
        this.property.setStatusCode("");

        Property oldProperty = repositoryEditorInput.getItem().getProperty();

        this.property.setPurpose(oldProperty.getPurpose());
        this.property.setDescription(oldProperty.getDescription());
        this.property.setLabel(oldProperty.getLabel());

        businessProcessItem = PropertiesFactory.eINSTANCE.createBusinessProcessItem();

        businessProcessItem.setProperty(property);

        repositoryFactory = service.getProxyRepositoryFactory();

        setDefaultPageImageDescriptor(ImageProvider.getImageDesc(ECoreImage.PROCESS_WIZ));
    }

    public void addPages() {
        mainPage = new SimpleBusinessCreationWizardPage(property, path);
        // overwrite it.
        mainPage.setTitle("Save As");
        mainPage.setDescription("Save as another new business model.");

        addPage(mainPage);
        setWindowTitle("Save As");
    }

    public boolean performFinish() {

        boolean ok = false;
        try {

            property.setId(repositoryFactory.getNextId());

            IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
            DiagramResourceManager diagramResourceManager = new DiagramResourceManager(page, new NullProgressMonitor());
            IFile file = diagramResourceManager.createDiagramFile();
            diagramResourceManager.updateFromResource(businessProcessItem, file);

            BusinessProcessItem oldItem = (BusinessProcessItem) repositoryEditorInput.getItem();

            BusinessProcess businessProcess = BusinessFactory.eINSTANCE.createBusinessProcess();
            businessProcessItem.setSemantic(businessProcess);

            // don't set these values directly
            // businessProcessItem.setSemantic(oldItem.getSemantic());
            // businessProcessItem.setNotation(oldItem.getNotation());
            // businessProcessItem.setNotationHolder(oldItem.getNotationHolder());

            repositoryFactory.create(businessProcessItem, mainPage.getDestinationPath());

            ok = true;

        } catch (Exception e) {
            MessageDialog.openError(getShell(), "Error", "Business model could not be saved" + " : " + e.getMessage());
            ExceptionHandler.process(e);
        }

        return ok;
    }

    public BusinessProcessItem getBusinessProcessItem() {
        return this.businessProcessItem;
    }
}
