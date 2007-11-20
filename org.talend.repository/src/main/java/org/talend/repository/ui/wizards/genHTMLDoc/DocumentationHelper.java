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
package org.talend.repository.ui.wizards.genHTMLDoc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.CorePlugin;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.core.prefs.ITalendCorePrefConstants;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryNode.EProperties;
import org.talend.repository.ui.wizards.exportjob.ExportFileResource;

/**
 * DOC ftang class global comment. Detailled comment <br/>
 * 
 */
public class DocumentationHelper {

    /**
     * ftang Comment method "isSyncWithDocumentation".
     * 
     * @return
     */
    public static boolean isSyncWithDocumentation() {
        boolean isSync = CorePlugin.getDefault().getPreferenceStore().getBoolean(ITalendCorePrefConstants.DOC_GENERATION);
        return isSync;
    }

    /**
     * ftang Comment method "isFolderExisting".
     * 
     * @param type
     * @param path
     * @param folderName
     * @return
     */
    public static boolean isPathValid(ERepositoryObjectType type, IPath path, String folderName) {
        IProxyRepositoryFactory repositoryFactory = ProxyRepositoryFactory.getInstance();
        try {
            return repositoryFactory.isPathValid(type, path, folderName);
        } catch (PersistenceException e) {
            ExceptionHandler.process(e);
            return false;
        }
    }

    /**
     * ftang Comment method "getExportFileResources".
     * 
     * @param nodes
     * @return
     */
    public static ExportFileResource[] getExportFileResources(RepositoryNode node) {

        List<ExportFileResource> list = new ArrayList<ExportFileResource>();
        if (node.getType() == ENodeType.SYSTEM_FOLDER || node.getType() == ENodeType.SIMPLE_FOLDER) {
            String folderName = "";
            if (!node.getProperties(EProperties.LABEL).toString().equals(ERepositoryObjectType.PROCESS.toString())) {
                folderName = node.getProperties(EProperties.LABEL).toString();
            }
            addTreeNode(node, folderName, list);
        }
        if (node.getType() == ENodeType.REPOSITORY_ELEMENT) {
            IRepositoryObject repositoryObject = node.getObject();
            if (repositoryObject.getProperty().getItem() instanceof ProcessItem) {
                ProcessItem processItem = (ProcessItem) repositoryObject.getProperty().getItem();
                ExportFileResource resource = new ExportFileResource(processItem, processItem.getProperty().getLabel());
                list.add(resource);
            }
        }

        ExportFileResource[] resourceArray = list.toArray(new ExportFileResource[list.size()]);
        return resourceArray;
    }

    /**
     * DOC Administrator Comment method "addTreeNode".
     * 
     * @param node
     * @param path
     * @param list
     */
    private static void addTreeNode(RepositoryNode node, String path, List<ExportFileResource> list) {
        if (node != null && node.getType() == ENodeType.REPOSITORY_ELEMENT) {
            IRepositoryObject repositoryObject = node.getObject();
            if (repositoryObject.getProperty().getItem() instanceof ProcessItem) {
                ProcessItem processItem = (ProcessItem) repositoryObject.getProperty().getItem();
                ExportFileResource resource = new ExportFileResource(processItem, path);
                list.add(resource);
            }
        }
        Object[] nodes = node.getChildren().toArray();
        if (nodes.length <= 0) {
            return;
        }
        for (int i = 0; i < nodes.length; i++) {
            addTreeNode((RepositoryNode) nodes[i], path + "/" //$NON-NLS-1$
                    + ((RepositoryNode) nodes[i]).getProperties(EProperties.LABEL).toString(), list);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.actions.AContextualAction#getHTMLFilePath(org.talend.repository.model.RepositoryNode)
     */
    public static File getHTMLFilePath(RepositoryNode currentNode) {
        IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(getProject().getTechnicalLabel());
        java.io.File file = project.getLocation().toFile();
        Object jobName = currentNode.getProperties(EProperties.LABEL);
        String string = file.toString() + IPath.SEPARATOR + "documentations/generated/jobs";
        if (!jobName.toString().equals(ERepositoryObjectType.PROCESS.toString())
                && currentNode.getType() != ENodeType.SYSTEM_FOLDER) {
            string = string + getCurrentJobPath(currentNode) + IPath.SEPARATOR + jobName;
        }

        java.io.File folder = new File(string);
        return folder;
    }

    /**
     * DOC Administrator Comment method "getCurrentJobPath".
     * 
     * @param repositoryNode
     * @return
     */
    public static String getCurrentJobPath(RepositoryNode repositoryNode) {
        String path = "";

        RepositoryNode parent = repositoryNode.getParent();
        if (parent.getType() == ENodeType.SIMPLE_FOLDER) {
            return getParentPath(parent, path);
        }
        return path;
    }

    /**
     * DOC Administrator Comment method "getParentPath".
     * 
     * @param parent
     * @param path
     */
    private static String getParentPath(RepositoryNode parent, String path) {
        path = IPath.SEPARATOR + parent.getProperties(EProperties.LABEL).toString() + path;
        if (parent.getParent().getType() == ENodeType.SIMPLE_FOLDER) {
            return getParentPath(parent.getParent(), path);
        }
        return path;
    }

    /**
     * Get the current project.
     * 
     * @return an instance of <code>Project</code>
     */
    private static Project getProject() {
        return ((org.talend.core.context.RepositoryContext) CorePlugin.getContext().getProperty(
                org.talend.core.context.Context.REPOSITORY_CONTEXT_KEY)).getProject();
    }
}
