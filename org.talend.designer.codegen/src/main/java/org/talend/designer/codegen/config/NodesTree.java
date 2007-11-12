// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the  agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//   
// ============================================================================
package org.talend.designer.codegen.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.talend.core.model.process.INode;

/**
 * A NodesTree is the Code Gerator Implementation of a process. A NodesTree is built using the Nodes of the Process.
 * It's made of a list of NodesSubTree.
 * 
 * $Id$
 */
public class NodesTree {

    private List<NodesSubTree> subTrees;

    private List<? extends INode> nodes;

    private List<INode> rootNodes;

    /**
     * Constuctor for NodesTree.
     * Note: the param init=false, when it is called in generateComponentCodeWithRows(). 
     * @param List of Available Nodes in this tree.
     * @param execute init method or not
     */
    public NodesTree(List<? extends INode> treeNodes, boolean init) {
        this.nodes = treeNodes;
        buildRootNodes();
        if (init) {
            buildSubTrees(true);
        }
    }

    /**
     * Build SubTrees List.
     * Note: the param init=false, when it is called in generateComponentCodeWithRows().
     */
    public void buildSubTrees(boolean init) {
        subTrees = new ArrayList<NodesSubTree>();
        for (INode node : nodes) {
            if (((node.isSubProcessStart()) && (node.isActivate())) || (rootNodes.contains(node))) {

                // need to unite the merge branches to one subStree
                if (node.isThereLinkWithMerge()&& init) {

                    Map<INode, Integer> mergeInfo = node.getLinkedMergeInfo();
                    if (mergeInfo != null && mergeInfo.values().toArray()[0].equals(1)) {
                        // add the first merge branch
                        subTrees.add(new NodesSubTree(node, nodes));
                    }

                } else {

                    subTrees.add(new NodesSubTree(node, nodes));
                }

            }
        }
    }

    /**
     * Build Root Nodes List.
     * 
     * @return
     */
    public void buildRootNodes() {
        rootNodes = new ArrayList<INode>();
        for (INode node : nodes) {
            if ((node.isStart()) && (node.isActivate())) {
                rootNodes.add(node);
            }
        }
    }

    /**
     * Getter for RootNodes.
     * 
     * @return
     */
    public List<INode> getRootNodes() {
        return rootNodes;
    }

    public void addRootNode(INode node) {
        if (rootNodes != null) {
            rootNodes.add(node);
        }
    }

    /**
     * Getter for subTrees.
     * 
     * @return the subTrees
     */
    public List<NodesSubTree> getSubTrees() {
        return this.subTrees;
    }
}
