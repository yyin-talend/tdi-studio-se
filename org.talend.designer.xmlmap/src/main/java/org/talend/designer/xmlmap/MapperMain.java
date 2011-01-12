// ============================================================================
//
// Copyright (C) 2006-2010 Talend Inc. - www.talend.com
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
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputTreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlMapData;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapFactory;
import org.talend.designer.xmlmap.ui.MapperUI;
import org.talend.designer.xmlmap.util.XmlMapUtil;

/**
 * wchen class global comment. Detailled comment
 */
public class MapperMain {

    private XmlMapComponent mapperComponent;

    private XmlMapData copyOfMapData;

    private MapperUI mapperUI;

    public MapperMain(XmlMapComponent mapperComponent) {
        this.mapperComponent = mapperComponent;
    }

    public Shell createUI(Display display) {
        MapperUI uiManager = new MapperUI(mapperComponent);
        return uiManager.createWindow(display);
    }

    public void prepareModel(IODataComponentContainer ioDataContainer, List<IMetadataTable> outputMetadataTables) {

        if (mapperComponent.getEmfMapData() != null) {
            copyOfMapData = EcoreUtil.copy(mapperComponent.getEmfMapData());
        } else {
            copyOfMapData = XmlmapFactory.eINSTANCE.createXmlMapData();
        }
        prepareModelInputs(ioDataContainer.getInputs());
        prepareModelOutputs(ioDataContainer.getOuputs(), outputMetadataTables);
    }

    private void prepareModelInputs(List<IODataComponent> inputConn) {
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
                        found.setXpath(inputTree.getName() + XmlMapUtil.XPATH_SEPARATOR + found.getName());
                        nodes.add(i, found);
                    }
                }

                if (nodes.size() > listColumns.size()) {
                    List unUsed = new ArrayList();
                    for (int i = listColumns.size(); i < nodes.size(); i++) {
                        unUsed.add(nodes.get(i));
                    }
                    nodes.removeAll(unUsed);
                }

            }
        }

    }

    private void prepareModelOutputs(List<IODataComponent> outputConn, List<IMetadataTable> outputMetadataTables) {
        for (IODataComponent outData : outputConn) {
            String name = outData.getName();
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

            List<IMetadataColumn> listColumns = outData.getTable().getListColumns();
            if (outData.getTable() != null && listColumns != null) {
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
                        found.setXpath(outputTree.getName() + XmlMapUtil.XPATH_SEPARATOR + found.getName());
                        nodes.add(i, found);
                    }
                }

                if (nodes.size() > listColumns.size()) {
                    List unUsed = new ArrayList();
                    for (int i = listColumns.size(); i < nodes.size(); i++) {
                        unUsed.add(nodes.get(i));
                    }
                    nodes.removeAll(unUsed);
                }

            }
            copyOfMapData.getOutputTrees().add(outputTree);
        }
    }

}
