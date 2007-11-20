// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2007 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License.
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
package org.talend.repository.ui.actions.documentation;

import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.core.ui.images.ECoreImage;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNode.EProperties;
import org.talend.repository.ui.wizards.exportjob.ExportFileResource;
import org.talend.repository.ui.wizards.genHTMLDoc.DocumentationHelper;
import org.talend.repository.ui.wizards.genHTMLDoc.JobHTMLScriptsManager;

/**
 * DOC ftang class global comment. Detailed comment <br/>
 * 
 */
public class UpdateHTMLDocumentationAction extends HTMLDocumentationAction {

    /**
     * Constructs a new OpenDocumentationAction.
     */
    public UpdateHTMLDocumentationAction() {
        super();

        setText("Upate documentation"); //$NON-NLS-1$
        setToolTipText("Upate documentation"); //$NON-NLS-1$
        setImageDescriptor(ImageProvider.getImageDesc(ECoreImage.DOCUMENTATION_ICON));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    public void run() {
        RepositoryNode currentNode = getCurrentJobNode();

        JobHTMLScriptsManager manager = new JobHTMLScriptsManager();

        java.io.File folder = DocumentationHelper.getHTMLFilePath(currentNode);
       

        String targetPath = folder.toString();

        List<ExportFileResource> exportResources = manager.getExportResources(DocumentationHelper
                .getExportFileResources(currentNode), targetPath);

        if (exportResources == null || exportResources.size() == 0) {
            MessageDialog.openError(Display.getDefault().getActiveShell(), "Talend Open Studio",
                    "Documentation update failed, please see the log to get more informations.");
        } else {
            MessageDialog.openInformation(Display.getDefault().getActiveShell(), "Talend Open Studio",
                    "Documentation was generated successfully.");
        }
    }

}
