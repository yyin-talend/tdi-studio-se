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
package org.talend.repository.tester;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.core.runtime.Assert;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.IRepositoryNode.EProperties;
import org.talend.repository.model.RepositoryNode;

public class NodeTester extends PropertyTester {

    /**
     * property used to check if object is a job
     */
    public static final String IS_JOB = "isJob"; //$NON-NLS-1$

    private static final Object IS_METADATA_TOP_NODE = "isMetadataTopNode"; //$NON-NLS-1$

    private static final Object IS_CODE_TOP_NODE = "isCodeTopNode"; //$NON-NLS-1$

    public NodeTester() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
        if (receiver instanceof RepositoryNode) {
            RepositoryNode repositoryNode = (RepositoryNode) receiver;
            if (IS_JOB.equals(property)) {
                return isJob(repositoryNode);
            }
            if (IS_METADATA_TOP_NODE.equals(property)) {
                return isMetadataTopNode(repositoryNode);
            }
            if (IS_CODE_TOP_NODE.equals(property)) {
                return isCodeTopNode(repositoryNode);
            }
            Assert.isTrue(false);// cause we should never be here
        }
        return false;
    }

    /**
     * DOC sgandon Comment method "isJob".
     * 
     * @param repositoryNode
     * @return
     */
    public boolean isJob(RepositoryNode repositoryNode) {
        boolean isJob = repositoryNode.getProperties(EProperties.CONTENT_TYPE) == ERepositoryObjectType.PROCESS
                && repositoryNode.getType() == ENodeType.REPOSITORY_ELEMENT;
        return isJob;
    }

    /**
     * DOC sgandon Comment method "isJob".
     * 
     * @param repositoryNode
     * @return
     */
    public boolean isMetadataTopNode(RepositoryNode repositoryNode) {
        return repositoryNode.getProperties(EProperties.CONTENT_TYPE) == ERepositoryObjectType.METADATA;
    }

    /**
     * DOC sgandon Comment method "isCodeTopNode".
     * 
     * @param element
     * @return
     */
    public boolean isCodeTopNode(RepositoryNode repositoryNode) {
        return repositoryNode.getProperties(EProperties.CONTENT_TYPE) == ERepositoryObjectType.CODE;
    }
}
