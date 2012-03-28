// ============================================================================
//
// Copyright (C) 2006-2012 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.view.di.viewer.content;

import java.util.Collection;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.IPath;
import org.talend.core.repository.constants.FileConstants;
import org.talend.repository.model.ProjectRepositoryNode;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.navigator.RepoViewCommonViewer;
import org.talend.repository.viewer.content.ProjectRepoDirectChildrenNodeContentProvider;
import org.talend.repository.viewer.content.ResouceChangeVisitorListener;

public class RecycleBinContentProvider extends ProjectRepoDirectChildrenNodeContentProvider {

    /**
     * DOC sgandon class global comment. Detailled comment <br/>
     * 
     * $Id: talend.epf 55206 2011-02-15 17:32:14Z mhirt $
     * 
     */
    private final class RecycleBinChildrenNodeVisitor extends ResouceChangeVisitorListener.RunnableResourceVisitor {

        @Override
        protected boolean visit(IResourceDelta delta, Collection<Runnable> runnables) {
            IResource resource = delta.getResource();
            IPath path = resource.getProjectRelativePath();
            if (getTopLevelNode() != null) {
                // be sure we are the last path of the resources and then check for the right folder and then check for
                // file of type .properties or folder.

                if (FileConstants.PROPERTIES_EXTENSION.equals(path.getFileExtension())) {
                    if (viewer instanceof RepoViewCommonViewer) {
                        runnables.add(new Runnable() {

                            @Override
                            public void run() {
                                refreshTopLevelNode();
                            }

                        });
                    }

                } else {// not the propoer resouce change so ignors and continue exploring
                    return true;
                }
            }// else no root node so ignor children
            return false;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.viewer.content.ProjectRepoChildrenNodeContentProvider#getTopLevelNodeFromProjectRepositoryNode
     * (org.talend.repository.model.ProjectRepositoryNode)
     */
    @Override
    protected RepositoryNode getTopLevelNodeFromProjectRepositoryNode(ProjectRepositoryNode projectRepositoryNode) {

        return projectRepositoryNode.getRecBinNode();
    }

    /**
     * this is overriden to create a specific visitor that will refresh the recycle bin every time there is a change in
     * a .properties file
     * 
     * @return
     */
    @Override
    protected ResouceChangeVisitorListener createResourceChangedVisitor() {
        return new ResouceChangeVisitorListener(viewer, new RecycleBinChildrenNodeVisitor());
    }

}
