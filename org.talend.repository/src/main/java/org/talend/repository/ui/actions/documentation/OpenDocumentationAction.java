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
package org.talend.repository.ui.actions.documentation;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Display;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.MessageBoxExceptionHandler;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.commons.ui.swt.dialogs.ProgressDialog;
import org.talend.core.model.properties.ByteArray;
import org.talend.core.model.properties.DocumentationItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.LinkDocumentationItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.ui.images.ECoreImage;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNode.ENodeType;
import org.talend.repository.ui.actions.AContextualAction;
import org.talend.repository.ui.wizards.documentation.LinkDocumentationHelper;
import org.talend.repository.ui.wizards.documentation.LinkUtils;

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
        Item item = node.getObject().getProperty().getItem();
        if (item == null) {
            return;
        }
        String extension = null;
        if (item instanceof DocumentationItem) {
            DocumentationItem documentationItem = (DocumentationItem) item;
            if (documentationItem.getExtension() != null) {
                extension = documentationItem.getExtension();
            }
        } else if (item instanceof LinkDocumentationItem) { // link documenation
            LinkDocumentationItem linkDocItem = (LinkDocumentationItem) item;
            if (!LinkUtils.validateLink(linkDocItem.getLink())) {
                MessageDialog.openError(Display.getCurrent().getActiveShell(), Messages
                        .getString("ExtractDocumentationAction.fileErrorTitle"), //$NON-NLS-1$
                        Messages.getString("ExtractDocumentationAction.fileErrorMessages")); //$NON-NLS-1$
                return;
            }

            if (linkDocItem.getExtension() != null) {
                extension = linkDocItem.getExtension();
            }
        }
        progress(item, extension);

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.actions.AContextualView#getClassForDoubleClick()
     */
    @Override
    public Class getClassForDoubleClick() {
        RepositoryNode node = null;
        try {
            node = getCurrentRepositoryNode();
        } catch (Exception e) {
            //
        }
        if (node == null) { // default, when init the double click action
            return DocumentationItem.class;
        }
        Item item = node.getObject().getProperty().getItem();
        if (item == null) {
            return null;
        }
        if (item instanceof DocumentationItem) {
            return DocumentationItem.class;
        }
        if (item instanceof LinkDocumentationItem) {
            return LinkDocumentationItem.class;
        }
        return null;
    }

    private void progress(final Item item, final String extension) {
        if (item == null) {
            return;
        }
        ProgressDialog progressDialog = new ProgressDialog(Display.getCurrent().getActiveShell()) {

            @Override
            public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

                // use the system program to open the documentation by extension.
                Program program = null;
                if (extension != null) {
                    program = Program.findProgram(extension);
                }
                boolean opened = false;
                if (program != null) {
                    IFile file = LinkDocumentationHelper.getTempFile(item.getProperty().getId());
                    if (file != null) {
                        try {
                            boolean canExec = false;
                            if (item instanceof DocumentationItem) {
                                DocumentationItem documentationItem = (DocumentationItem) item;
                                documentationItem.getContent().setInnerContentToFile(file.getLocation().toFile());
                                canExec = true;
                            } else if (item instanceof LinkDocumentationItem) { // link documenation
                                LinkDocumentationItem linkDocItem = (LinkDocumentationItem) item;
                                ByteArray byteArray = LinkDocumentationHelper.getLinkItemContent(linkDocItem);
                                if (byteArray != null) {
                                    byteArray.setInnerContentToFile(file.getLocation().toFile());
                                    canExec = true;
                                }
                            }
                            if (canExec) {
                                program.execute(file.getLocation().toOSString());
                                opened = true;
                            }
                        } catch (IOException e) {
                            MessageBoxExceptionHandler.process(e);
                        }
                    }
                }
                // if not opened, extract the content.
                if (!opened) {
                    ExtractDocumentationAction extractAction = new ExtractDocumentationAction();
                    extractAction.setWorkbenchPart(getWorkbenchPart());
                    extractAction.run();
                }
            }

        };
        try {
            progressDialog.executeProcess();
        } catch (InvocationTargetException e) {
            ExceptionHandler.process(e);
        } catch (InterruptedException e) {
            // Nothing to do
        }
    }
}
