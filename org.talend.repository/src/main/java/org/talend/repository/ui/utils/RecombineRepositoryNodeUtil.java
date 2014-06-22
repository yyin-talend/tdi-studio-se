// ============================================================================
//
// Copyright (C) 2006-2011 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.ui.utils;

import java.util.ArrayList;
import java.util.List;

import org.talend.commons.utils.time.TimeMeasure;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.ProjectRepositoryNode;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.ui.dialog.RepositoryReviewDialog;
import org.talend.repository.ui.views.RepositoryContentProvider;

/**
 * ggu class global comment. Detailled comment
 */
public final class RecombineRepositoryNodeUtil {

    public static RepositoryNode getFixingTypeInputRoot(RepositoryContentProvider contentProvider, ERepositoryObjectType type) {
        List<ERepositoryObjectType> types = new ArrayList<ERepositoryObjectType>();
        if (type != null) {
            types.add(type);
        }
        return new RecombineRepositoryNodeUtil(types).getFixingTypesInputRoot(contentProvider);
    }

    public static RepositoryNode getFixingTypesInputRoot(RepositoryContentProvider contentProvider,
            List<ERepositoryObjectType> types) {
        return new RecombineRepositoryNodeUtil(types).getFixingTypesInputRoot(contentProvider);
    }

    private List<ERepositoryObjectType> types;

    private RecombineRepositoryNodeUtil(List<ERepositoryObjectType> types) {
        super();
        this.types = types;
    }

    private List<ERepositoryObjectType> getTypes() {
        return types;
    }

    private RepositoryNode getFixingTypesInputRoot(RepositoryContentProvider contentProvider) {
        RepositoryNode tmpRootNode = new RepositoryNode(null, null, null);
        TimeMeasure.step(RepositoryReviewDialog.class.getSimpleName(), "before getInputRoot, in MultiTypesProcessor"); //$NON-NLS-1$

        List<RepositoryNode> rootNodes = getRepositoryNodesByTypes(contentProvider);
        TimeMeasure.step(RepositoryReviewDialog.class.getSimpleName(), "finished main project, in MultiTypesProcessor"); //$NON-NLS-1$ 

        if (rootNodes != null) {
            tmpRootNode.getChildren().addAll(rootNodes);
        }
        // referenced project.
        addSubReferencedProjectNodes(tmpRootNode, contentProvider.getRoot());
        TimeMeasure.step(RepositoryReviewDialog.class.getSimpleName(), "finished ref-projects, in MultiTypesProcessor"); //$NON-NLS-1$

        return tmpRootNode;
    }

    private void addSubReferencedProjectNodes(RepositoryNode contextNode, ProjectRepositoryNode parentRefProject) {

        RepositoryNode referenceProjectNode = parentRefProject.getRootRepositoryNode(ERepositoryObjectType.REFERENCED_PROJECTS);
        if (referenceProjectNode != null) {
            initNode(referenceProjectNode);

            List<IRepositoryNode> refProjects = referenceProjectNode.getChildren();
            if (refProjects != null && !refProjects.isEmpty()) {
                List<IRepositoryNode> nodesList = new ArrayList<IRepositoryNode>();

                for (IRepositoryNode repositoryNode : refProjects) {
                    ProjectRepositoryNode refProject = (ProjectRepositoryNode) repositoryNode;
                    ProjectRepositoryNode newProject = new ProjectRepositoryNode(refProject);

                    List<RepositoryNode> rootNodes = getRepositoryNodesByTypes(refProject);
                    if (rootNodes != null) {
                        newProject.getChildren().addAll(rootNodes);
                    }
                    // sub ref-project
                    addSubReferencedProjectNodes(newProject, refProject);

                    nodesList.add(newProject);

                }
                contextNode.getChildren().addAll(nodesList);
            }
        }
    }

    private List<RepositoryNode> getRepositoryNodesByTypes(Object provider) {
        List<RepositoryNode> rootNodes = new ArrayList<RepositoryNode>();
        List<ERepositoryObjectType> types = getTypes();
        if (types != null) {
            for (ERepositoryObjectType type : types) {
                RepositoryNode rootNode = null;
                if (provider instanceof RepositoryContentProvider) {
                    rootNode = ((RepositoryContentProvider) provider).getRootRepositoryNode(type);
                }
                if (provider instanceof ProjectRepositoryNode) {
                    rootNode = ((ProjectRepositoryNode) provider).getRootRepositoryNode(type);
                }
                if (rootNode != null) {
                    initNode(rootNode);
                    rootNodes.add(rootNode);
                }
            }
        }
        return rootNodes;
    }

    protected final void initNode(RepositoryNode rootTypeNode) {
        if (rootTypeNode.getParent() instanceof ProjectRepositoryNode && !rootTypeNode.isInitialized()) {
            ((ProjectRepositoryNode) rootTypeNode.getParent()).initializeChildren(rootTypeNode);
            rootTypeNode.setInitialized(true);
        }
    }
}
