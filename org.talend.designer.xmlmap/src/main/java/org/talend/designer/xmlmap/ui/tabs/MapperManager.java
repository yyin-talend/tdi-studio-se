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
package org.talend.designer.xmlmap.ui.tabs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.talend.commons.ui.swt.extended.table.ExtendedTableModel;
import org.talend.designer.xmlmap.XmlMapComponent;
import org.talend.designer.xmlmap.model.emf.xmlmap.InputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputTreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlMapData;
import org.talend.designer.xmlmap.ui.tabs.table.TreeSchemaTableEntry;
import org.talend.designer.xmlmap.util.XmlMapUtil;

/**
 * wchen class global comment. Detailled comment
 */
public class MapperManager {

    private XmlMapComponent mapperComponent;

    private XmlMapData copyOfMapData;

    private List<TreeSchemaTableEntry> treeSchemaEntrys = new ArrayList<TreeSchemaTableEntry>();

    private Map<InputXmlTree, ExtendedTableModel<TreeSchemaTableEntry>> inputTreeModelMap = new HashMap<InputXmlTree, ExtendedTableModel<TreeSchemaTableEntry>>();

    private Map<OutputXmlTree, ExtendedTableModel<TreeSchemaTableEntry>> outputTreeModelMap = new HashMap<OutputXmlTree, ExtendedTableModel<TreeSchemaTableEntry>>();

    public MapperManager(XmlMapComponent mapperComponent, XmlMapData copyOfMapData) {
        this.mapperComponent = mapperComponent;
        this.copyOfMapData = copyOfMapData;
    }

    public XmlMapComponent getMapperComponent() {
        return mapperComponent;
    }

    public XmlMapData getCopyOfMapData() {
        return copyOfMapData;
    }

    public ExtendedTableModel<TreeSchemaTableEntry> getSelectedInputTreeSchemaModel(InputXmlTree inputXmlTree) {
        if (inputXmlTree == null && !copyOfMapData.getInputTrees().isEmpty()) {
            inputXmlTree = copyOfMapData.getInputTrees().get(0);
        }
        ExtendedTableModel<TreeSchemaTableEntry> tableModel = inputTreeModelMap.get(inputXmlTree);
        if (tableModel == null) {
            tableModel = new ExtendedTableModel<TreeSchemaTableEntry>("Tree Schema", treeSchemaEntrys);
        }

        if (inputXmlTree != null) {
            EList<TreeNode> nodes = inputXmlTree.getNodes();
            for (TreeNode node : nodes) {
                if (XmlMapUtil.DOCUMENT.equals(node.getType())) {
                    addTreeSchemaEnties(tableModel, node.getChildren());
                }
            }
            inputTreeModelMap.put(inputXmlTree, tableModel);
        }
        return tableModel;
    }

    public ExtendedTableModel<TreeSchemaTableEntry> getSelectedOutputTreeSchemaModel(OutputXmlTree outputXmlTree) {
        if (outputXmlTree == null && !copyOfMapData.getInputTrees().isEmpty()) {
            outputXmlTree = copyOfMapData.getOutputTrees().get(0);
        }
        ExtendedTableModel<TreeSchemaTableEntry> tableModel = outputTreeModelMap.get(outputXmlTree);
        if (tableModel == null) {
            tableModel = new ExtendedTableModel<TreeSchemaTableEntry>("Tree Schema", treeSchemaEntrys);
        }

        if (outputXmlTree != null) {
            EList<OutputTreeNode> nodes = outputXmlTree.getNodes();
            for (TreeNode node : nodes) {
                if (XmlMapUtil.DOCUMENT.equals(node.getType())) {
                    addTreeSchemaEnties(tableModel, node.getChildren());
                }
                outputTreeModelMap.put(outputXmlTree, tableModel);
            }
        }
        return tableModel;
    }

    private void addTreeSchemaEnties(ExtendedTableModel<TreeSchemaTableEntry> tableModel, EList<TreeNode> nodes) {
        for (TreeNode node : nodes) {
            if (node.getChildren().isEmpty()) {
                TreeSchemaTableEntry entry = new TreeSchemaTableEntry(node);
                tableModel.add(entry);
            } else {
                addTreeSchemaEnties(tableModel, node.getChildren());
            }
        }

    }
}
