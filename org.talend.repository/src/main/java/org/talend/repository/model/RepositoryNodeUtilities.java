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
package org.talend.repository.model;

import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.repository.RepositoryPlugin;
import org.talend.repository.model.RepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryNode.EProperties;
import org.talend.repository.ui.views.IRepositoryView;
import org.talend.repository.ui.views.RepositoryView;

/**
 * Utility class to manage RepositoryNode.<br/>
 * 
 * $Id: RepositoryNodeUtilities.java 1368 2007-01-10 09:50:53Z smallet $
 * 
 */
public class RepositoryNodeUtilities {

    public static IPath getPath(RepositoryNode node) {
        if (node == null) {
            return null;
        }
        if (node.isBin()) {
            return new Path("bin"); //$NON-NLS-1$
        }
        if ((node.getType() == ENodeType.STABLE_SYSTEM_FOLDER && node.getContentType() != ERepositoryObjectType.JOBS && node
                .getContentType() != ERepositoryObjectType.JOBLETS)
                || node.getType() == ENodeType.SYSTEM_FOLDER) {
            return new Path(""); //$NON-NLS-1$
        }
        if (node.getType() == ENodeType.SIMPLE_FOLDER) {
            String label = node.getObject().getProperty().getLabel();
            return getPath(node.getParent()).append(label);
        }

        String label = node.getLabel();
        // checks if node is under Documentations/Generatated/Jobs
        if (node.getType() == ENodeType.STABLE_SYSTEM_FOLDER
                && (node.getContentType() == ERepositoryObjectType.JOBS || node.getContentType() == ERepositoryObjectType.JOBLETS)) {
            String nodeLabel = (String) node.getProperties(EProperties.LABEL);
            if (nodeLabel.equalsIgnoreCase(ERepositoryObjectType.JOBS.toString())) {
                return new Path("");
            } else {
                return getPath(node.getParent()).append(label);
            }
        } else {
            if (!label.equals(ERepositoryObjectType.PROCESS.toString()) && !label.equals(ERepositoryObjectType.JOBLET.toString())) {
                return getPath(node.getParent()).append(label);
            } else {
                return getPath(node.getParent());
            }
        }

    }

    /**
     * 
     * ggu Comment method "getPath".
     * 
     * get path by repository item id. can't get the folders.
     */
    public static IPath getPath(final String id) {
        if (id == null || "".equals(id) || "-1".equals(id)) {
            return null;
        }
        IProxyRepositoryFactory factory = RepositoryPlugin.getDefault().getRepositoryService().getProxyRepositoryFactory();
        try {
            final IRepositoryObject lastVersion = factory.getLastVersion(id);
            return getPath(lastVersion);
        } catch (PersistenceException e) {
            //
        }
        return null;
    }

    public static IPath getPath(IRepositoryObject curNode) {
        if (curNode == null) {
            return null;
        }
        final RepositoryNode repositoryNode = getRepositoryNode(curNode);
        if (repositoryNode != null) {
            return getPath(repositoryNode);
        }
        return null;
    }

    /**
     * 
     * ggu Comment method "getRepositoryNode".
     * 
     * get the repository node by a IRepositoryObject.
     */
    public static RepositoryNode getRepositoryNode(RepositoryNode rootNode, IRepositoryObject curNode) {
        if (rootNode == null || curNode == null) {
            return null;
        }
        final List<RepositoryNode> children = rootNode.getChildren();

        if (children != null) {
            for (RepositoryNode childNode : children) {
                if (childNode.getId().equals(curNode.getId())) {
                    return childNode;
                } else {
                    final RepositoryNode repositoryNode = getRepositoryNode(childNode, curNode);
                    if (repositoryNode != null) {
                        return repositoryNode;
                    }
                }

            }
        }

        return null;
    }

    public static RepositoryNode getRepositoryNode(IRepositoryObject curNode) {
        IRepositoryView view = RepositoryView.show();
        if (view == null || curNode == null) {
            return null;
        }
        return getRepositoryNode(view.getRoot(), curNode);
    }

}
