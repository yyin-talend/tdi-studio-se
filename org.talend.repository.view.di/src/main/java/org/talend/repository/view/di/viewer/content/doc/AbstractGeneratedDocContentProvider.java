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
package org.talend.repository.view.di.viewer.content.doc;

import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.utils.data.container.RootContainer;
import org.talend.core.model.general.Project;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.ProjectRepositoryNode;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.view.di.viewer.tester.GeneratedDocNodeTester;
import org.talend.repository.viewer.content.SubEmptyTopNodeContentProvider;

/**
 * DOC ggu class global comment. Detailled comment
 */
public abstract class AbstractGeneratedDocContentProvider extends SubEmptyTopNodeContentProvider {

    GeneratedDocNodeTester nodeTester = new GeneratedDocNodeTester();

    public AbstractGeneratedDocContentProvider() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.viewer.content.SingleTopLevelContentProvider#isRootNodeType(java.lang.Object)
     */
    @Override
    protected boolean isRootNodeType(Object element) {
        if (element instanceof RepositoryNode) {
            return nodeTester.isGeneratedDocTopNode((RepositoryNode) element);
        } else {
            return false;
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.viewer.content.ProjectRepoAbstractContentProvider#getTopLevelNodeFromProjectRepositoryNode
     * (org.talend.repository.model.ProjectRepositoryNode)
     */
    @Override
    protected RepositoryNode getTopLevelNodeFromProjectRepositoryNode(ProjectRepositoryNode projectNode) {
        return projectNode.getRootRepositoryNode(getTopLevelNodeType());
    }

    abstract protected ERepositoryObjectType getTopLevelNodeType();

    /**
     * FIXME later, shall make this to work. and need check the triggerPoints in extension point.
     * 
     * And comment the line 792 "parent == docNode" in ProjectRepositoryNode to make the DocumentationContentProvider to
     * work also.
     */
    @Override
    protected void initializeChildren(RepositoryNode parent) {
        try {
            Project currentProject = ProjectManager.getInstance().getCurrentProject();

            RootContainer<String, IRepositoryViewObject> jobsContainer = getFactory().getMetadata(currentProject,
                    getTopLevelNodeType(), true);

            convertChildren(currentProject, jobsContainer, parent, getTopLevelNodeType(), (RepositoryNode) parent.getRoot()
                    .getRecBinNode());
        } catch (PersistenceException e) {
            ExceptionHandler.process(e);
        }
    }

}
