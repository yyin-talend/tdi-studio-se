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
package org.talend.designer.xmlmap;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.talend.core.model.components.IODataComponent;
import org.talend.core.model.components.IODataComponentContainer;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.process.EConnectionType;
import org.talend.designer.xmlmap.model.emf.xmlmap.InputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.NodeType;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputTreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlMapData;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapFactory;
import org.talend.designer.xmlmap.model.tree.XML_MAP_LOOKUP_MODE;
import org.talend.designer.xmlmap.model.tree.XML_MAP_MATCHING_MODE;
import org.talend.designer.xmlmap.ui.MapperUI;
import org.talend.designer.xmlmap.ui.tabs.MapperManager;
import org.talend.designer.xmlmap.util.XmlMapUtil;

/**
 * wchen class global comment. Detailled comment
 */
public class MapperMain {

    private XmlMapComponent mapperComponent;

    private XmlMapData copyOfMapData;

    private MapperUI mapperUI;

    private MapperManager mapperManager;

    public MapperMain(XmlMapComponent mapperComponent) {
        this.mapperComponent = mapperComponent;
        prepareModel();
        mapperManager = new MapperManager(mapperComponent, copyOfMapData);
    }

    public Shell createUI(Display display) {
        mapperUI = new MapperUI(mapperManager);
        return mapperUI.createWindow(display);
    }

    private void prepareModel() {
        if (mapperComponent == null) {
            return;
        }
        IODataComponentContainer ioDataContainer = mapperComponent.getIODataComponents();
        List<IMetadataTable> outputMetadataTables = mapperComponent.getMetadataList();
        if (mapperComponent.getExternalEmfData() != null) {
            copyOfMapData = EcoreUtil.copy((XmlMapData) mapperComponent.getExternalEmfData());
            prepareModelInputs(ioDataContainer.getInputs());
            prepareModelOutputs(ioDataContainer.getOuputs(), outputMetadataTables);
        }
    }

    private void prepareModelInputs(List<IODataComponent> inputConn) {
        // remove no used input tree
        if (copyOfMapData.getInputTrees().size() != inputConn.size()) {
            List treesToRemove = new ArrayList();
            for (InputXmlTree inputTree : copyOfMapData.getInputTrees()) {
                boolean found = false;
                for (IODataComponent connection : inputConn) {
                    if (inputTree.getName().equals(connection.getName())) {
                        found = true;
                    }
                }
                if (!found) {
                    for (TreeNode treeNode : inputTree.getNodes()) {
                        XmlMapUtil.detachConnectionsTarget(treeNode, copyOfMapData);
                    }
                    treesToRemove.add(inputTree);
                }
            }

            copyOfMapData.getInputTrees().removeAll(treesToRemove);
        }

        for (IODataComponent inData : inputConn) {
            String name = inData.getName();
            InputXmlTree inputTree = null;
            for (InputXmlTree in : copyOfMapData.getInputTrees()) {
                if (in.getName() != null && in.getName().equals(name)) {
                    inputTree = in;
                    break;
                }
            }
            if (inputTree == null) {
                inputTree = XmlmapFactory.eINSTANCE.createInputXmlTree();
                inputTree.setName(name);
                inputTree.setLookup(EConnectionType.FLOW_MAIN != inData.getConnection().getLineStyle());
                copyOfMapData.getInputTrees().add(inputTree);
            }
            if (inputTree.getLookupMode() == null) {
                inputTree.setLookupMode(XML_MAP_LOOKUP_MODE.LOAD_ONCE.toString());
            }
            if (inputTree.getMatchingMode() == null) {
                inputTree.setMatchingMode(XML_MAP_MATCHING_MODE.ALL_ROWS.toString());
            }

            List<IMetadataColumn> listColumns = inData.getTable().getListColumns();
            if (inData.getTable() != null && listColumns != null) {
                EList<TreeNode> nodes = inputTree.getNodes();
                for (int i = 0; i < listColumns.size(); i++) {
                    IMetadataColumn column = listColumns.get(i);
                    TreeNode found = null;
                    int j = 0;
                    for (; j < nodes.size(); j++) {
                        TreeNode node = nodes.get(j);
                        if (node.getName() != null && node.getName().equals(column.getLabel())) {
                            found = node;
                            break;
                        }
                    }
                    if (found != null) {
                        // set in case talend type changed in metadata
                        found.setType(column.getTalendType());
                        if (i != j) {
                            // do switch to keep the same sequence
                            TreeNode temp = nodes.get(j);
                            nodes.remove(j);
                            nodes.add(i, temp);
                        }
                    } else {
                        found = XmlmapFactory.eINSTANCE.createTreeNode();
                        found.setName(column.getLabel());
                        found.setType(column.getTalendType());
                        found.setNullable(column.isNullable());
                        found.setXpath(XmlMapUtil.getXPath(inputTree.getName(), found.getName(), found.getNodeType()));

                        nodes.add(i, found);
                    }

                    // add a default root for document
                    if (XmlMapUtil.DOCUMENT.equals(found.getType())) {
                        EList<TreeNode> children = found.getChildren();
                        if (children.isEmpty()) {
                            TreeNode treeRoot = XmlmapFactory.eINSTANCE.createTreeNode();
                            treeRoot.setName("root");
                            treeRoot.setType(XmlMapUtil.DEFAULT_DATA_TYPE);
                            treeRoot.setNodeType(NodeType.ELEMENT);
                            treeRoot.setXpath(XmlMapUtil.getXPath(found.getXpath(), treeRoot.getName(), treeRoot.getNodeType()));
                            children.add(treeRoot);
                        }
                    }
                    // remove children and connections for children if not document
                    else {
                        EList<TreeNode> children = found.getChildren();
                        if (!children.isEmpty()) {
                            XmlMapUtil.detachConnectionsTarget(found, copyOfMapData);
                            found.getChildren().clear();
                        }
                    }

                }

                if (nodes.size() > listColumns.size()) {
                    List unUsed = new ArrayList();
                    for (int i = listColumns.size(); i < nodes.size(); i++) {
                        XmlMapUtil.detachConnectionsTarget(nodes.get(i), copyOfMapData);
                        unUsed.add(nodes.get(i));
                    }
                    nodes.removeAll(unUsed);
                }

            }
        }

    }

    private void prepareModelOutputs(List<IODataComponent> outputConn, List<IMetadataTable> outputMetadataTables) {
        for (IMetadataTable meatadataTable : outputMetadataTables) {
            String name = meatadataTable.getTableName();
            OutputXmlTree outputTree = null;
            for (OutputXmlTree out : copyOfMapData.getOutputTrees()) {
                if (out.getName() != null && out.getName().equals(name)) {
                    outputTree = out;
                    break;
                }
            }
            if (outputTree == null) {
                outputTree = XmlmapFactory.eINSTANCE.createOutputXmlTree();
                outputTree.setName(name);
                copyOfMapData.getOutputTrees().add(outputTree);
            }

            List<IMetadataColumn> listColumns = meatadataTable.getListColumns();
            if (listColumns != null) {
                EList<OutputTreeNode> nodes = outputTree.getNodes();
                for (int i = 0; i < listColumns.size(); i++) {
                    IMetadataColumn column = listColumns.get(i);
                    OutputTreeNode found = null;
                    int j = 0;
                    for (; j < nodes.size(); j++) {
                        OutputTreeNode node = nodes.get(j);
                        if (node.getName() != null && node.getName().equals(column.getLabel())) {
                            found = node;
                            break;
                        }
                    }
                    if (found != null) {
                        // set in case talend type changed in metadata
                        found.setType(column.getTalendType());
                        if (i != j) {
                            // do switch to keep the same sequence
                            OutputTreeNode temp = nodes.get(j);
                            nodes.remove(j);
                            nodes.add(i, temp);
                        }
                    } else {
                        found = XmlmapFactory.eINSTANCE.createOutputTreeNode();
                        found.setName(column.getLabel());
                        found.setType(column.getTalendType());
                        found.setNullable(column.isNullable());
                        found.setXpath(XmlMapUtil.getXPath(outputTree.getName(), found.getName(), found.getNodeType()));

                        nodes.add(i, found);
                    }

                    // add a default root for document
                    if (XmlMapUtil.DOCUMENT.equals(found.getType())) {
                        EList<TreeNode> children = found.getChildren();
                        if (children.isEmpty()) {
                            TreeNode treeRoot = XmlmapFactory.eINSTANCE.createOutputTreeNode();
                            treeRoot.setName("root");
                            treeRoot.setType(XmlMapUtil.DEFAULT_DATA_TYPE);
                            treeRoot.setNodeType(NodeType.ELEMENT);
                            treeRoot.setXpath(XmlMapUtil.getXPath(found.getXpath(), treeRoot.getName(), treeRoot.getNodeType()));
                            children.add(treeRoot);
                        }
                    }
                    // remove children and connections for children if not document
                    else {
                        EList<TreeNode> children = found.getChildren();
                        if (!children.isEmpty()) {
                            XmlMapUtil.detachConnectionsSouce(found, copyOfMapData);
                            found.getChildren().clear();
                        }
                    }

                }

                if (nodes.size() > listColumns.size()) {
                    List unUsed = new ArrayList();
                    for (int i = listColumns.size(); i < nodes.size(); i++) {
                        OutputTreeNode node = nodes.get(i);
                        XmlMapUtil.detachConnectionsSouce(node, copyOfMapData);
                        unUsed.add(node);
                    }
                    nodes.removeAll(unUsed);
                }

            }
            copyOfMapData.getOutputTrees().add(outputTree);
        }
    }

    public int getMapperDialogResponse() {
        return mapperUI.getMapperDialogResponse();
    }

}
