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
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.talend.commons.ui.swt.extended.table.ExtendedTableModel;
import org.talend.commons.utils.data.list.IListenableListListener;
import org.talend.commons.utils.data.list.ListenableListEvent;
import org.talend.commons.utils.data.list.ListenableListEvent.TYPE;
import org.talend.core.model.components.IODataComponent;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.editor.MetadataTableEditor;
import org.talend.core.ui.metadata.editor.MetadataTableEditorView;
import org.talend.designer.xmlmap.XmlMapComponent;
import org.talend.designer.xmlmap.model.emf.xmlmap.InputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputTreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlMapData;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapFactory;
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

    private OutputXmlTree oldSelectedOut;

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
                        selectInputXmlTree(inputTree);
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

    public void selectInputXmlTree(final InputXmlTree inputTree) {
        if (inputTree != selectedInputTree) {
            selectedInputTree = inputTree;
            MetadataTableEditorView inputMetaEditorView = mapperUI.getTabFolderEditors().getInputMetaEditorView();
            List<IODataComponent> inputs = mapperComponent.getIODataComponents().getInputs();
            IMetadataTable table = null;
            for (int i = 0; i < inputs.size(); i++) {
                IODataComponent ioDataComponent = inputs.get(i);
                if (inputTree.getName() != null && inputTree.getName().equals(ioDataComponent.getConnection().getName())) {
                    table = ioDataComponent.getTable();
                    break;
                }
            }
            if (table != null) {
                MetadataTableEditor editor = new MetadataTableEditor(table, table.getLabel());
                editor.setModifiedBeanListenable(inputMetaEditorView.getTableViewerCreator());
                editor.addAfterOperationListListener(new IListenableListListener() {

                    public void handleEvent(ListenableListEvent event) {
                        if (event.type == TYPE.ADDED) {
                            EList<TreeNode> nodes = inputTree.getNodes();
                            List<IMetadataColumn> metadataColumns = (List<IMetadataColumn>) event.addedObjects;
                            if (event.index != null) {
                                int index = event.index;
                                for (IMetadataColumn column : metadataColumns) {
                                    TreeNode createTreeNode = XmlmapFactory.eINSTANCE.createTreeNode();
                                    createTreeNode.setName(column.getLabel());
                                    createTreeNode.setType(column.getTalendType());
                                    createTreeNode.setNullable(column.isNullable());
                                    createTreeNode.setPattern(column.getPattern());
                                    createTreeNode.setXpath(inputTree.getName() + XmlMapUtil.XPATH_SEPARATOR
                                            + createTreeNode.getName());
                                    inputTree.getNodes().add(index, createTreeNode);
                                    index = index + 1;
                                }
                            }

                        } else if (event.type == TYPE.REMOVED) {
                            List<IMetadataColumn> metadataColumns = (List<IMetadataColumn>) event.removedObjects;
                            List treeNodeToRemove = new ArrayList();
                            for (IMetadataColumn column : metadataColumns) {
                                for (TreeNode node : inputTree.getNodes()) {
                                    if (node.getName() != null && node.getName().equals(column.getLabel())) {
                                        treeNodeToRemove.add(node);
                                    }
                                }
                            }
                            inputTree.getNodes().removeAll(treeNodeToRemove);

                        } else if (event.type == TYPE.SWAPED) {
                            List<Integer> listIndexTarget = event.indicesTarget;
                            List<Integer> listIndexOrignal = event.indicesOrigin;
                            for (int i = 0; i < listIndexOrignal.size(); i++) {
                                int orignal = listIndexOrignal.get(i);
                                int target = listIndexTarget.get(i);
                                if (orignal < inputTree.getNodes().size()) {
                                    Collections.swap(inputTree.getNodes(), orignal, target);
                                    System.out.println();
                                }
                            }

                        }
                    }

                });

                inputMetaEditorView.setMetadataTableEditor(editor);
            }

            refreshInputTreeSchemaEditor(inputTree);
        }

    }

    public void selectOutputXmlTree(final OutputXmlTree outputTree) {
        if (outputTree != oldSelectedOut) {
            oldSelectedOut = outputTree;
            MetadataTableEditorView outputMetaEditorView = mapperUI.getTabFolderEditors().getOutputMetaEditorView();
            List<IMetadataTable> metadataList = mapperComponent.getMetadataList();
            IMetadataTable table = null;
            for (int i = 0; i < metadataList.size(); i++) {
                if (outputTree.getName() != null && outputTree.getName().equals(metadataList.get(i).getTableName())) {
                    table = metadataList.get(i);
                    break;
                }
            }
            if (table != null) {
                MetadataTableEditor editor = new MetadataTableEditor(table, table.getLabel());
                outputMetaEditorView.setMetadataTableEditor(editor);
                editor.setModifiedBeanListenable(outputMetaEditorView.getTableViewerCreator());
                editor.addAfterOperationListListener(new IListenableListListener() {

                    public void handleEvent(ListenableListEvent event) {

                        if (event.type == TYPE.ADDED) {
                            EList<OutputTreeNode> nodes = outputTree.getNodes();
                            List<IMetadataColumn> metadataColumns = (List<IMetadataColumn>) event.addedObjects;
                            if (event.index != null) {
                                int index = event.index;
                                for (IMetadataColumn column : metadataColumns) {
                                    OutputTreeNode createTreeNode = XmlmapFactory.eINSTANCE.createOutputTreeNode();
                                    createTreeNode.setName(column.getLabel());
                                    createTreeNode.setType(column.getTalendType());
                                    createTreeNode.setNullable(column.isNullable());
                                    createTreeNode.setPattern(column.getPattern());
                                    createTreeNode.setXpath(outputTree.getName() + XmlMapUtil.XPATH_SEPARATOR
                                            + createTreeNode.getName());
                                    outputTree.getNodes().add(index, createTreeNode);
                                    index = index + 1;
                                }
                            }

                        } else if (event.type == TYPE.REMOVED) {
                            List<IMetadataColumn> metadataColumns = (List<IMetadataColumn>) event.removedObjects;
                            List treeNodeToRemove = new ArrayList();
                            for (IMetadataColumn column : metadataColumns) {
                                for (TreeNode node : outputTree.getNodes()) {
                                    if (node.getName() != null && node.getName().equals(column.getLabel())) {
                                        treeNodeToRemove.add(node);
                                    }
                                }
                            }
                            outputTree.getNodes().removeAll(treeNodeToRemove);

                        } else if (event.type == TYPE.SWAPED) {
                            List<Integer> listIndexTarget = event.indicesTarget;
                            List<Integer> listIndexOrignal = event.indicesOrigin;
                            for (int i = 0; i < listIndexOrignal.size(); i++) {
                                int orignal = listIndexOrignal.get(i);
                                int target = listIndexTarget.get(i);
                                if (orignal < outputTree.getNodes().size()) {

                                }
                            }

                        }

                    }

                });
            }
        }

    }
}
