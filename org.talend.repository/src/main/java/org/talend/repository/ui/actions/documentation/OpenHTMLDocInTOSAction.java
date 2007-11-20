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

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.ui.internal.browser.WebBrowserEditorInput;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.core.model.properties.HTMLDocumentationItem;
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
public class OpenHTMLDocInTOSAction extends HTMLDocumentationAction {

    /**
     * Constructs a new OpenDocumentationAction.
     */
    public OpenHTMLDocInTOSAction() {
        super();

        setText("Open in TOS"); //$NON-NLS-1$
        setToolTipText("Open in TOS"); //$NON-NLS-1$
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

        Object jobName = currentNode.getProperties(EProperties.LABEL);

        String fileSuffix = "_" + currentNode.getObject().getVersion();
        String targetPath = folder.toString();
        String htmlFileName = jobName.toString() + fileSuffix + ".html";

        File htmlFile = new File(targetPath + fileSuffix + IPath.SEPARATOR + htmlFileName);

        // Generates HTML file if it is not existing.
        if (!htmlFile.exists()) {
            List<ExportFileResource> exportResources = manager.getExportResources(DocumentationHelper
                    .getExportFileResources(currentNode), targetPath);
        }
        TalendWebBrowserEdtior browserEdtior = new TalendWebBrowserEdtior();
        URL url = null;
        try {
            url = htmlFile.toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        WebBrowserEditorInput input = new WebBrowserEditorInput(url);

        input.setName(htmlFileName);
        input.setToolTipText(htmlFileName);
        browserEdtior.open(input);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.actions.AContextualAction#getClassForDoubleClick()
     */
    public Class getClassForDoubleClick() {
        return HTMLDocumentationItem.class;
    }

}
