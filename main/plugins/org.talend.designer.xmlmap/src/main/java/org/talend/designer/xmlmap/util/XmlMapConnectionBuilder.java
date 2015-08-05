// ============================================================================
//
// Copyright (C) 2006-2015 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.xmlmap.util;

import java.util.ArrayList;
import java.util.List;

import org.talend.designer.xmlmap.model.emf.xmlmap.AbstractInOutTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.AbstractNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.Connection;
import org.talend.designer.xmlmap.model.emf.xmlmap.FilterConnection;
import org.talend.designer.xmlmap.model.emf.xmlmap.InputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.LookupConnection;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputTreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.VarNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlMapData;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapFactory;
import org.talend.designer.xmlmap.ui.expressionutil.TableEntryLocation;
import org.talend.designer.xmlmap.ui.expressionutil.XmlMapExpressionManager;

/**
 * 
 * DOC wchen Utils class to deal with connections inside xmlmap ui
 */
public class XmlMapConnectionBuilder {

    public static void rebuildLinks(TreeNode docNode, XmlMapData mapData) {
        if (docNode == null || mapData == null) {
            return;
        }
        AbstractInOutTree inputTree = XmlMapUtil.getAbstractInOutTree(docNode);

        int index = mapData.getInputTrees().indexOf(inputTree);
        if (index == -1) {
            index = mapData.getInputTrees().size();
        }
        List<TreeNode> nodes = new ArrayList<TreeNode>();
        nodes.add(docNode);
        rebuildLink(index, nodes, mapData);
    }

    private static void rebuildLink(int inputTreeIndex, List<TreeNode> children, XmlMapData mapData) {
        for (TreeNode treeNode : children) {
            if (XmlMapUtil.isDragable(treeNode)) {
                String expression = XmlMapUtil.convertToExpression(treeNode.getXpath());
                XmlMapExpressionManager expressionManager = new XmlMapExpressionManager();
                TableEntryLocation sourceLocation = expressionManager.parseTableEntryLocation(expression).get(0);
                // LOOKUP ,FILTER
                for (int i = inputTreeIndex; i < mapData.getInputTrees().size(); i++) {
                    InputXmlTree treeTarget = mapData.getInputTrees().get(i);
                    if (hasMaptchedLocation(expressionManager, sourceLocation, treeTarget.getExpressionFilter())) {
                        createFilterConnection(treeNode, treeTarget, mapData);
                    }
                    checkTargetChildren(expressionManager, treeTarget.getNodes(), treeNode, sourceLocation, mapData);
                }
                // VAR
                for (VarNode varNode : mapData.getVarTables().get(0).getNodes()) {
                    if (hasMaptchedLocation(expressionManager, sourceLocation, varNode.getExpression())) {
                        createConnection(treeNode, varNode, mapData);
                    }
                }
                // OUTPUT,FILTER
                for (int i = 0; i < mapData.getOutputTrees().size(); i++) {
                    OutputXmlTree outputTree = mapData.getOutputTrees().get(i);
                    if (hasMaptchedLocation(expressionManager, sourceLocation, outputTree.getExpressionFilter())) {
                        createFilterConnection(treeNode, outputTree, mapData);
                    }
                    checkTargetChildren(expressionManager, outputTree.getNodes(), treeNode, sourceLocation, mapData);
                }
            }
            if (!treeNode.getChildren().isEmpty()) {
                rebuildLink(inputTreeIndex, treeNode.getChildren(), mapData);
            }
        }
    }

    private static void checkTargetChildren(XmlMapExpressionManager expressionManager, List<? extends TreeNode> children,
            TreeNode sourceNode, TableEntryLocation sourceLocation, XmlMapData mapData) {
        for (TreeNode targetNode : children) {
            if (XmlMapUtil.isExpressionEditable(targetNode)) {
                if (hasMaptchedLocation(expressionManager, sourceLocation, targetNode.getExpression())) {
                    if (targetNode instanceof OutputTreeNode) {
                        createConnection(sourceNode, targetNode, mapData);
                    } else {
                        createLookupConnection(sourceNode, targetNode, mapData);
                    }
                }
            }
            if (!targetNode.getChildren().isEmpty()) {
                checkTargetChildren(expressionManager, targetNode.getChildren(), sourceNode, sourceLocation, mapData);
            }
        }
    }

    private static boolean hasMaptchedLocation(XmlMapExpressionManager expressionManager, TableEntryLocation sourceLocation,
            String targetExpression) {
        if (!"".equals(targetExpression) && targetExpression != null) {
            List<TableEntryLocation> targetLocations = expressionManager.parseTableEntryLocation(targetExpression);
            for (TableEntryLocation target : targetLocations) {
                if (sourceLocation.equals(target)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void createConnection(AbstractNode sourceNode, AbstractNode targetNode, XmlMapData mapData) {
        Connection conn = XmlmapFactory.eINSTANCE.createConnection();
        conn.setSource(sourceNode);
        conn.setTarget(targetNode);
        targetNode.getIncomingConnections().add(conn);
        sourceNode.getOutgoingConnections().add(conn);
        mapData.getConnections().add(conn);
    }

    public static void createLookupConnection(TreeNode sourceNode, TreeNode targetNode, XmlMapData mapData) {
        LookupConnection conn = XmlmapFactory.eINSTANCE.createLookupConnection();
        conn.setSource(sourceNode);
        conn.setTarget(targetNode);
        targetNode.getLookupIncomingConnections().add(conn);
        sourceNode.getLookupOutgoingConnections().add(conn);
        mapData.getConnections().add(conn);
    }

    public static void createFilterConnection(AbstractNode sourceNode, AbstractInOutTree targetTree, XmlMapData mapData) {
        FilterConnection connection = XmlmapFactory.eINSTANCE.createFilterConnection();
        connection.setSource(sourceNode);
        connection.setTarget(targetTree);
        targetTree.getFilterIncomingConnections().add(connection);
        sourceNode.getFilterOutGoingConnections().add(connection);
        mapData.getConnections().add(connection);
    }

}
