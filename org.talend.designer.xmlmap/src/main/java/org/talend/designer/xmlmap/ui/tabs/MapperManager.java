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
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.talend.commons.ui.swt.extended.table.ExtendedTableModel;
import org.talend.designer.xmlmap.XmlMapComponent;
import org.talend.designer.xmlmap.model.emf.xmlmap.InputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputTreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlMapData;
import org.talend.designer.xmlmap.parts.TreeNodeEditPart;
import org.talend.designer.xmlmap.ui.MapperUI;
import org.talend.designer.xmlmap.ui.tabs.table.TreeSchemaTableEntry;
import org.talend.designer.xmlmap.util.XmlMapUtil;

/**
 * wchen class global comment. Detailled comment
 */
public class MapperManager implements ISelectionChangedListener {

    private XmlMapComponent mapperComponent;

    private XmlMapData copyOfMapData;

    private MapperUI mapperUI;

    private InputXmlTree selectedInputTree;

    private List<TreeSchemaTableEntry> treeSchemaEntrys = new ArrayList<TreeSchemaTableEntry>();

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
        treeSchemaEntrys.clear();
        ExtendedTableModel<TreeSchemaTableEntry> tableModel = new ExtendedTableModel<TreeSchemaTableEntry>("Tree Schema",
                treeSchemaEntrys);

        if (inputXmlTree != null) {
            EList<TreeNode> nodes = inputXmlTree.getNodes();
            for (TreeNode node : nodes) {
                if (XmlMapUtil.DOCUMENT.equals(node.getType())) {
                    addTreeSchemaEnties(tableModel, node.getChildren());
                }
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

    public void setMapperUI(MapperUI mapperUI) {
        this.mapperUI = mapperUI;
    }

    public void selectionChanged(SelectionChangedEvent event) {
        if (!event.getSelection().isEmpty() && event.getSelection() instanceof IStructuredSelection) {
            Object firstElement = ((IStructuredSelection) event.getSelection()).getFirstElement();
            if (firstElement instanceof TreeNodeEditPart) {
                Object model = ((TreeNodeEditPart) firstElement).getModel();
                if (model instanceof TreeNode && !(model instanceof OutputTreeNode)) {
                    TreeNode inputTreeNodeRoot = XmlMapUtil.getInputTreeNodeRoot((TreeNode) model);
                    if (inputTreeNodeRoot != null) {
                        InputXmlTree inputTree = (InputXmlTree) inputTreeNodeRoot.eContainer();
                        if (selectedInputTree != inputTree) {
                            selectedInputTree = inputTree;
                            ExtendedTableModel<TreeSchemaTableEntry> selectedInputTreeSchemaModel = getSelectedInputTreeSchemaModel(selectedInputTree);
                            mapperUI.getTabFolderEditors().getInputTreeSchemaEditor()
                                    .setExtendedControlModel(selectedInputTreeSchemaModel);
                            mapperUI.getTabFolderEditors().getInputTreeSchemaEditor().getTableViewerCreator().refresh();
                        }
                    }
                }
            }
        } else {
            ExtendedTableModel<TreeSchemaTableEntry> oldModel = mapperUI.getTabFolderEditors().getInputTreeSchemaEditor()
                    .getExtendedTableModel();
            if (oldModel != null && oldModel.getBeanCount() != 0) {
                mapperUI.getTabFolderEditors().getInputTreeSchemaEditor()
                        .setExtendedControlModel(new ExtendedTableModel<TreeSchemaTableEntry>("Tree Schema", treeSchemaEntrys));
                mapperUI.getTabFolderEditors().getInputTreeSchemaEditor().getTableViewerCreator().refresh();
            }
        }

    }

    public void refreshInputTreeSchemaEditor(InputXmlTree tree) {
        if (tree == null) {
            return;
        }
        ExtendedTableModel<TreeSchemaTableEntry> selectedInputTreeSchemaModel = getSelectedInputTreeSchemaModel(tree);
        mapperUI.getTabFolderEditors().getInputTreeSchemaEditor().setExtendedControlModel(selectedInputTreeSchemaModel);
        mapperUI.getTabFolderEditors().getInputTreeSchemaEditor().getTableViewerCreator().refresh();
    }
}
