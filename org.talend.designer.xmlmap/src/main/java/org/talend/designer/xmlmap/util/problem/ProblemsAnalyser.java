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
package org.talend.designer.xmlmap.util.problem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.talend.core.model.process.Problem;
import org.talend.core.model.process.Problem.ProblemStatus;
import org.talend.designer.xmlmap.model.emf.xmlmap.AbstractInOutTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.InputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlMapData;
import org.talend.designer.xmlmap.ui.tabs.MapperManager;
import org.talend.designer.xmlmap.util.XmlMapUtil;

/**
 * 
 * DOC wchen class global comment. Detailled comment
 */
public class ProblemsAnalyser {

    private MapperManager mapperManager;

    private Map<AbstractInOutTree, List<Problem>> treeAndProblems = new HashMap<AbstractInOutTree, List<Problem>>();

    private static final String ERROR_MESSAGE_START = "Require loop element for Document :";

    /**
     * DOC wchen ProblemsAnalyser constructor comment.
     */
    public ProblemsAnalyser(MapperManager mapperManager) {
        super();
        this.mapperManager = mapperManager;
    }

    public List<Problem> checkProblems() {
        treeAndProblems.clear();

        final XmlMapData copyOfMapData = mapperManager.getCopyOfMapData();
        if (copyOfMapData != null) {
            for (InputXmlTree inputTree : copyOfMapData.getInputTrees()) {
                checkTreeNodesProblem(inputTree, inputTree.getNodes());
            }
            for (OutputXmlTree outputTree : copyOfMapData.getOutputTrees()) {
                checkTreeNodesProblem(outputTree, outputTree.getNodes());
            }

        }

        return getProblems();
    }

    public void checkLoopProblems(AbstractInOutTree abstractTree) {
        // clear problems for the tree before recheck it
        if (treeAndProblems.get(abstractTree) != null) {
            treeAndProblems.get(abstractTree).clear();
        }
        List<? extends TreeNode> nodes = null;
        if (abstractTree instanceof InputXmlTree) {
            nodes = ((InputXmlTree) abstractTree).getNodes();
        } else if (abstractTree instanceof OutputXmlTree) {
            nodes = ((OutputXmlTree) abstractTree).getNodes();
        }
        if (nodes != null) {
            checkTreeNodesProblem(abstractTree, nodes);
        }

        if (treeAndProblems.get(abstractTree) == null || treeAndProblems.get(abstractTree).isEmpty()) {
            treeAndProblems.remove(abstractTree);
        }
    }

    private void checkTreeNodesProblem(AbstractInOutTree abstractTree, List<? extends TreeNode> treeNodes) {
        for (TreeNode treeNode : treeNodes) {
            if (XmlMapUtil.DOCUMENT.equals(treeNode.getType())) {
                if (!hasDocumentLoop(treeNode)) {
                    String message = ERROR_MESSAGE_START + treeNode.getXpath();
                    addProblem(abstractTree, new Problem(null, message, ProblemStatus.ERROR));
                }
            }
        }
    }

    private boolean hasDocumentLoop(TreeNode treeNode) {
        if (treeNode.isLoop()) {
            return true;
        } else {
            if (!treeNode.getChildren().isEmpty()) {
                for (TreeNode child : treeNode.getChildren()) {
                    if (hasDocumentLoop(child)) {
                        return true;
                    }
                }
            } else {
                return false;
            }
        }
        return false;
    }

    public List<Problem> getProblems() {
        List<Problem> problemsList = new ArrayList<Problem>();
        for (List<Problem> problems : treeAndProblems.values()) {
            problemsList.addAll(problems);
        }
        return new ArrayList<Problem>(problemsList);
    }

    private void addProblem(AbstractInOutTree tree, Problem problem) {
        if (problem != null) {
            List<Problem> list = treeAndProblems.get(tree);
            if (list == null) {
                list = new ArrayList<Problem>();
                list.add(problem);
                treeAndProblems.put(tree, list);
            } else {
                list.add(problem);
            }
        }
    }

    public String getErrorMessage() {
        String errorMessage = ERROR_MESSAGE_START;
        final List<Problem> problems = getProblems();
        if (problems.isEmpty()) {
            return null;
        } else {
            for (Problem problem : problems) {
                final String description = problem.getDescription();
                if (description != null) {
                    errorMessage = errorMessage + description.substring(ERROR_MESSAGE_START.length(), description.length()) + ",";
                }
            }
        }

        return errorMessage.substring(0, errorMessage.length() - 1);
    }

}
