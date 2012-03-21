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
package org.talend.repository.view.di.viewer.content.rules;

import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.repository.model.ProjectRepositoryNode;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.view.di.viewer.content.AbstractTopNodeContentProvider;

public class RulesTopNodeContentProvider extends AbstractTopNodeContentProvider {

    @Override
    protected boolean isRootNodeType(Object element) {
        return false;
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
        return projectRepositoryNode.getRootRepositoryNode(ERepositoryObjectType.METADATA_RULES_MANAGEMENT);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.viewer.content.SingleTopLevelContentProvider#extractPotentialRootNode(java.lang.Object)
     */
    @Override
    protected RepositoryNode extractPotentialRootNode(Object element) {
        if (element instanceof RepositoryNode) {
            return (RepositoryNode) element;
        }
        return null;
    }

}
