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

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.talend.commons.exception.MessageBoxExceptionHandler;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.core.model.properties.DocumentationItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.ui.images.ECoreImage;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNode.ENodeType;
import org.talend.repository.ui.actions.AContextualAction;

/**
 * Saves the content of a document on the local file system. <br/>
 * 
 * $Id$
 * 
 */
public class ExtractDocumentationAction extends AContextualAction {

    /**
     * Constructs a new ExtractDocumentationAction.
     */
    public ExtractDocumentationAction() {
        super();

        setText(Messages.getString("ExtractDocumentationAction.text.saveAs")); //$NON-NLS-1$
        setToolTipText(Messages.getString("ExtractDocumentationAction.toolTipText.extractDoctoFileSys")); //$NON-NLS-1$
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

        FileDialog fileDlg = new FileDialog(Display.getCurrent().getActiveShell(), SWT.SAVE);
        String initialFileName = documentationItem.getName();
        if (documentationItem.getExtension() != null) {
            initialFileName = initialFileName + "." + documentationItem.getExtension(); //$NON-NLS-1$
        }
        fileDlg.setFileName(initialFileName);
        String filename = fileDlg.open();
        if (filename != null) {
            File file = new File(filename);
            try {
                documentationItem.getContent().setInnerContentToFile(file);
            } catch (IOException ioe) {
                MessageBoxExceptionHandler.process(ioe);
            }
        }
    }

}
