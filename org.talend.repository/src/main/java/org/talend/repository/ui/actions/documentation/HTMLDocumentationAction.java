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

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryNode.EProperties;
import org.talend.repository.ui.actions.AContextualAction;
import org.talend.repository.ui.views.IRepositoryView;

/**
 * DOC ftang class global comment. Detailed comment <br/>
 * 
 */
public class HTMLDocumentationAction extends AContextualAction {

    private RepositoryNode returnRepositoryNode = null;

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.swt.actions.ITreeContextualAction#init(org.eclipse.jface.viewers.TreeViewer,
     * org.eclipse.jface.viewers.IStructuredSelection)
     */
    public void init(TreeViewer viewer, IStructuredSelection selection) {
        boolean canWork = false;

        Object firstElement = selection.getFirstElement();
        if (firstElement instanceof RepositoryNode) {
            RepositoryNode repositoryObject = (RepositoryNode) firstElement;
            if (repositoryObject.getType() != ENodeType.SIMPLE_FOLDER
                    && repositoryObject.getProperties(EProperties.CONTENT_TYPE) == ERepositoryObjectType.HTML_DOC) {
                canWork = true;
            }
        }

        setEnabled(canWork);
    }

    /**
     * ftang Comment method "getCurrentNode".
     * 
     * @return
     */
    protected RepositoryNode getCurrentJobNode() {
        RepositoryNode selectedNode = (RepositoryNode) (((IStructuredSelection) getSelection()).getFirstElement());

        boolean isLevelOneNode = false;
        RepositoryNode parent = selectedNode.getParent();
        if (parent.getType() == ENodeType.STABLE_SYSTEM_FOLDER
                && parent.getProperties(EProperties.LABEL).toString().equals(ERepositoryObjectType.JOBS.toString())) {
            isLevelOneNode = true;
        }

        String selectedJobName = ((String) (selectedNode.getProperties(EProperties.LABEL)));

        System.out.println("Run in OpenHTMLDocInTOSAction");

        IRepositoryView viewPart = getViewPart();
        RepositoryNode root = viewPart.getRoot();
        boolean isJobsNode = selectedNode.getProperties(EProperties.LABEL).toString().equals(
                ERepositoryObjectType.JOBS.toString());

        boolean isGeneratedNode = selectedNode.getProperties(EProperties.LABEL).toString().equals(
                ERepositoryObjectType.GENERATED.toString());

        for (RepositoryNode node : root.getChildren()) {
            if (node.getContentType() == ERepositoryObjectType.PROCESS) {

                // Checks if current node is "Jobs" or "Generated", equals to "Job Designs".
                if (isJobsNode || isGeneratedNode) {
                    return node;
                }
                this.returnRepositoryNode = null;
                checkJobs(selectedJobName, node, isLevelOneNode);
                return this.returnRepositoryNode;
            }
        }

        return selectedNode;
    }

    /**
     * DOC Administrator Comment method "getCurrentJob".
     * 
     * @param selectedNode
     * @param selectedJobName
     * @param currentNode
     * @param repositoryNode
     * @return
     */

    private void checkJobs(String selectedJobName, RepositoryNode repositoryNode, boolean isLevelOneNode) {
        if (returnRepositoryNode != null) {
            return;
        }

        for (RepositoryNode subNode : repositoryNode.getChildren()) {
            if (!isLevelOneNode && subNode.getType() == ENodeType.SIMPLE_FOLDER && subNode.getChildren().size() > 0) {
                checkJobs(selectedJobName, subNode, isLevelOneNode);
            } else if (subNode.getType() == ENodeType.REPOSITORY_ELEMENT) {
                String label = (String) subNode.getProperties(EProperties.LABEL);
                if (selectedJobName.equals(label)) {
                    returnRepositoryNode = subNode;
                    return;
                }
            }
        }
    }

    protected RepositoryNode getCurrentDocumentationNode(String selectedJobName, ERepositoryObjectType objectType) {
        RepositoryNode selectedNode = (RepositoryNode) (((IStructuredSelection) getSelection()).getFirstElement());
        IRepositoryView viewPart = getViewPart();
        RepositoryNode root = viewPart.getRoot();
        boolean isJobsNode = selectedNode.getProperties(EProperties.LABEL).toString().equals(
                ERepositoryObjectType.JOBS.toString());

        boolean isGeneratedNode = selectedNode.getProperties(EProperties.LABEL).toString().equals(
                ERepositoryObjectType.GENERATED.toString());

        for (RepositoryNode node : root.getChildren()) {
            // Goto the Generated Node;
            if (node.getContentType() != ERepositoryObjectType.GENERATED) {
                continue;
            }

            // Goto the Jobs Node;
            for (RepositoryNode subNode : node.getChildren()) {
                if (subNode.getContentType() != ERepositoryObjectType.JOBS) {
                    continue;
                }

                for (RepositoryNode grandChildNode : subNode.getChildren()) {

                }
            }

        }

        return null;

    }
}
