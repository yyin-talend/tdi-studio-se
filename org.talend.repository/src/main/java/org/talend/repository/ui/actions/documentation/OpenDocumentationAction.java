// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the  agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//   
// ============================================================================
package org.talend.repository.ui.actions.documentation;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.program.Program;
import org.talend.commons.exception.MessageBoxExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.commons.utils.workbench.resources.ResourceUtils;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.DocumentationItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.ui.images.ECoreImage;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.RepositoryConstants;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.ResourceModelUtils;
import org.talend.repository.model.RepositoryNode.ENodeType;
import org.talend.repository.ui.actions.AContextualAction;

/**
 * Action opening a IDocumentation with the associated OS program. <br/>
 * 
 * $Id$
 * 
 */
public class OpenDocumentationAction extends AContextualAction {

    /**
     * Constructs a new OpenDocumentationAction.
     */
    public OpenDocumentationAction() {
        super();

        setText(Messages.getString("OpenDocumentationAction.openDocAction.openDoc")); //$NON-NLS-1$
        setToolTipText(Messages.getString("OpenDocumentationAction.openDocAcitonTipText.openDoc")); //$NON-NLS-1$
        setImageDescriptor(ImageProvider.getImageDesc(ECoreImage.DOCUMENTATION_ICON));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.actions.ITreeContextualAction#init(org.eclipse.jface.viewers.TreeViewer,
     * org.eclipse.jface.viewers.IStructuredSelection)
     */
    public void init(TreeViewer viewer, IStructuredSelection selection) {
        boolean canWork = !selection.isEmpty() && selection.size() == 1;
        if (canWork) {
            RepositoryNode node = (RepositoryNode) selection.getFirstElement();
            canWork = node.getType() == ENodeType.REPOSITORY_ELEMENT
                    && node.getObject().getType() == ERepositoryObjectType.DOCUMENTATION;
        }
        setEnabled(canWork);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    public void run() {
        RepositoryNode node = (RepositoryNode) ((IStructuredSelection) getSelection()).getFirstElement();
        DocumentationItem documentationItem = (DocumentationItem) node.getObject().getProperty().getItem();

        Program program = null;
        if (documentationItem.getExtension() != null) {
            program = Program.findProgram(documentationItem.getExtension());
        }
        if (program != null) {
            // Save data to a temporary file
            Project project = ((RepositoryContext) CorePlugin.getContext().getProperty(Context.REPOSITORY_CONTEXT_KEY))
                    .getProject();
            try {
                IProject fsProject = ResourceModelUtils.getProject(project);
                IFolder tmpFolder = ResourceUtils.getFolder(fsProject, RepositoryConstants.TEMP_DIRECTORY, true);
                String tmpFilename = "DOC" + documentationItem.getProperty().getId(); //$NON-NLS-1$
                IFile fileTmp = tmpFolder.getFile(tmpFilename);
                File file = fileTmp.getLocation().toFile();
                documentationItem.getContent().setInnerContentToFile(file);

                program.execute(fileTmp.getLocation().toOSString());
            } catch (PersistenceException e) {
                MessageBoxExceptionHandler.process(e);
            } catch (IOException e) {
                MessageBoxExceptionHandler.process(e);
            }
        } else {
            ExtractDocumentationAction extractAction = new ExtractDocumentationAction();
            extractAction.run();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.actions.AContextualView#getClassForDoubleClick()
     */
    @Override
    public Class getClassForDoubleClick() {
        return DocumentationItem.class;
    }

}
