package org.talend.repository.ui.processor;

import org.eclipse.jface.viewers.Viewer;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.repository.model.RepositoryNode;

/**
 * xye class global comment. Detailled comment
 */
public class ContextTypeProcessor extends SingleTypeProcessor {

    /**
     * xye RepositoryTypeProcessor constructor comment.
     * 
     * @param repositoryType
     */
    public ContextTypeProcessor(String repositoryType) {
        super(repositoryType);
    }

    @Override
    protected ERepositoryObjectType getType() {
        return ERepositoryObjectType.CONTEXT;
    }

    @Override
    protected boolean selectRepositoryNode(Viewer viewer, RepositoryNode parentNode, RepositoryNode node) {
        return true;
    }

}