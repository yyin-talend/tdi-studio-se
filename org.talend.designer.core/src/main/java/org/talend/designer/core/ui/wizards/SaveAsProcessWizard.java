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
package org.talend.designer.core.ui.wizards;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.part.EditorPart;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.commons.utils.VersionUtils;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.process.IProcess2;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.ui.images.ECoreImage;
import org.talend.designer.core.DesignerPlugin;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;
import org.talend.repository.editor.JobEditorInput;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IRepositoryService;
import org.talend.repository.model.RepositoryNode;

/**
 * Wizard for the creation of a new project. <br/>
 * 
 * $Id: NewProcessWizard.java 46332 2010-08-05 06:48:56Z cli $
 * 
 */
public class SaveAsProcessWizard extends Wizard {

    /** Main page. */
    private NewProcessWizardPage mainPage;

    /** Created project. */
    private ProcessItem processItem;

    private Property property;

    private IPath path;

    private IProxyRepositoryFactory repositoryFactory;

    private JobEditorInput jobEditorInput;

    public SaveAsProcessWizard(EditorPart editorPart) {

        this.jobEditorInput = (JobEditorInput) editorPart.getEditorInput();

        RepositoryNode repositoryNode = jobEditorInput.getRepositoryNode();
        // see: RepositoryEditorInput.setRepositoryNode(IRepositoryNode repositoryNode)
        if (repositoryNode == null) {
            repositoryNode = (RepositoryNode) CorePlugin.getDefault().getRepositoryService().getRepositoryNode(
                    jobEditorInput.getItem().getProperty().getId(), false);
        }

        IRepositoryService service = DesignerPlugin.getDefault().getRepositoryService();
        this.path = service.getRepositoryPath((RepositoryNode) repositoryNode);

        this.property = PropertiesFactory.eINSTANCE.createProperty();
        this.property.setAuthor(((RepositoryContext) CorePlugin.getContext().getProperty(Context.REPOSITORY_CONTEXT_KEY))
                .getUser());
        this.property.setVersion(VersionUtils.DEFAULT_VERSION);
        this.property.setStatusCode("");

        Property oldProperty = jobEditorInput.getItem().getProperty();

        this.property.setPurpose(oldProperty.getPurpose());
        this.property.setDescription(oldProperty.getDescription());
        this.property.setLabel(oldProperty.getLabel());

        processItem = PropertiesFactory.eINSTANCE.createProcessItem();

        processItem.setProperty(property);

        repositoryFactory = service.getProxyRepositoryFactory();

        setDefaultPageImageDescriptor(ImageProvider.getImageDesc(ECoreImage.PROCESS_WIZ));
    }

    public void addPages() {
        mainPage = new NewProcessWizardPage(property, path);
        // overwrite it.
        mainPage.setTitle("Save As");
        mainPage.setDescription("Save as another new job.");

        addPage(mainPage);
        setWindowTitle("Save As");
    }

    public boolean performFinish() {

        boolean ok = false;
        try {

            property.setId(repositoryFactory.getNextId());

            IProcess2 loadedProcess = jobEditorInput.getLoadedProcess();
            ProcessType processType = loadedProcess.saveXmlFile();
            processItem.setProcess(processType);

            // don't need to add depended routines.

            repositoryFactory.create(processItem, mainPage.getDestinationPath());

            ok = true;

        } catch (Exception e) {
            MessageDialog.openError(getShell(), "Error", "Job could not be saved" + " : " + e.getMessage());
            ExceptionHandler.process(e);
        }

        return ok;
    }

    public ProcessItem getProcess() {
        return this.processItem;
    }
}
