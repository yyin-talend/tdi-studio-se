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
import org.talend.repository.model.IRepositoryNode.EProperties;
import org.talend.repository.model.RepositoryNode;

public class NodeTester extends PropertyTester {

    /**
     * 
     */
    private static final String IS_DELETED = "isDeleted"; //$NON-NLS-1$

    /**
     * 
     */
    private static final String IS_BUSINESS_MODEL_BRANCH = "isBusinessModelBranch"; //$NON-NLS-1$

    /**
     * 
     */
    private static final String ALWAYS_FALSE = "alwaysFalse"; //$NON-NLS-1$

    /**
     * property used to check if object is a job
     */
    public static final String IS_JOB_BRANCH = "isJobBranch"; //$NON-NLS-1$

    private static final Object IS_METADATA_TOP_NODE = "isMetadataTopNode"; //$NON-NLS-1$

    private static final Object IS_CODE_TOP_NODE = "isCodeTopNode"; //$NON-NLS-1$

    private static final Object IS_RULES_TOP_NODE = "isRulesTopNode"; //$NON-NLS-1$

    @Override
    public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
        if (receiver instanceof RepositoryNode) {
            RepositoryNode repositoryNode = (RepositoryNode) receiver;
            if (IS_JOB_BRANCH.equals(property)) {
                return isJobBranch(repositoryNode);
            }
            if (IS_METADATA_TOP_NODE.equals(property)) {
                return isMetadataTopNode(repositoryNode);
            }
            if (IS_CODE_TOP_NODE.equals(property)) {
                return isCodeTopNode(repositoryNode);
            }
            if (IS_RULES_TOP_NODE.equals(property)) {
                return isRulesTopNode(repositoryNode);
            }
            if (ALWAYS_FALSE.equals(property)) {
                return false;
            }
            if (IS_BUSINESS_MODEL_BRANCH.equals(property)) {
                return isBusinessModelBranch(repositoryNode);
            }
            if (IS_DELETED.equals(property)) {
                return isDeleted(repositoryNode);
            }
            Assert.isTrue(false);// cause we should never be here
        }
        return false;
    }

    /**
     * DOC sgandon Comment method "isBusinessModel".
     * 
     * @param repositoryNode
     */
    public boolean isBusinessModelBranch(RepositoryNode repositoryNode) {
        boolean isBM = repositoryNode.getProperties(EProperties.CONTENT_TYPE) == ERepositoryObjectType.BUSINESS_PROCESS;
        // && repositoryNode.getType() == ENodeType.REPOSITORY_ELEMENT;
        return isBM;

    }

    /**
     * DOC sgandon Comment method "isJob".
     * 
     * @param repositoryNode
     * @return
     */
    public boolean isJobBranch(RepositoryNode repositoryNode) {
        boolean isJob = repositoryNode.getProperties(EProperties.CONTENT_TYPE) == ERepositoryObjectType.PROCESS;
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

    /**
     * 
     * DOC ggu Comment method "isRulesTopNode".
     * 
     * @param repositoryNode
     * @return
     */
    public boolean isRulesTopNode(RepositoryNode repositoryNode) {
        return repositoryNode.getProperties(EProperties.CONTENT_TYPE) == ERepositoryObjectType.METADATA_RULES_MANAGEMENT;
    }

    public boolean isDeleted(RepositoryNode repositoryNode) {
        return repositoryNode.getObject() != null ? repositoryNode.getObject().isDeleted() : false;
    }
}
