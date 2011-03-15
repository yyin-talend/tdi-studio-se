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
package org.talend.designer.xmlmap.ui.tabs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.talend.commons.ui.swt.extended.table.ExtendedTableModel;
import org.talend.commons.ui.swt.tableviewer.IModifiedBeanListener;
import org.talend.commons.ui.swt.tableviewer.ModifiedBeanEvent;
import org.talend.commons.ui.utils.threading.AsynchronousThreading;
import org.talend.commons.utils.data.list.IListenableListListener;
import org.talend.commons.utils.data.list.ListenableListEvent;
import org.talend.commons.utils.data.list.ListenableListEvent.TYPE;
import org.talend.core.model.components.IODataComponent;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.editor.MetadataTableEditor;
import org.talend.core.ui.metadata.editor.AbstractMetadataTableEditorView;
import org.talend.core.ui.metadata.editor.MetadataTableEditorView;
import org.talend.designer.xmlmap.XmlMapComponent;
import org.talend.designer.xmlmap.model.emf.xmlmap.AbstractNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.InputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.NodeType;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputTreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlMapData;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapFactory;
import org.talend.designer.xmlmap.parts.AbstractNodePart;
import org.talend.designer.xmlmap.parts.InputXmlTreeEditPart;
import org.talend.designer.xmlmap.parts.OutputXmlTreeEditPart;
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

    private AbstractNode selectedNode;

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
        List<TreeSchemaTableEntry> treeSchemaEntrys = new ArrayList<TreeSchemaTableEntry>();
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

    public ExtendedTableModel<TreeSchemaTableEntry> getSelectedOutputTreeSchemaModel(OutputXmlTree outputXmlTree) {
        if (outputXmlTree == null && !copyOfMapData.getOutputTrees().isEmpty()) {
            outputXmlTree = copyOfMapData.getOutputTrees().get(0);
        }
        List<TreeSchemaTableEntry> treeSchemaEntrys = new ArrayList<TreeSchemaTableEntry>();
        ExtendedTableModel<TreeSchemaTableEntry> tableModel = new ExtendedTableModel<TreeSchemaTableEntry>("Tree Schema",
                treeSchemaEntrys);

        if (outputXmlTree != null) {
            EList<OutputTreeNode> nodes = outputXmlTree.getNodes();
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
            TreeSchemaTableEntry entry = new TreeSchemaTableEntry(node);
            tableModel.add(entry);
            if (!node.getChildren().isEmpty()) {
                addTreeSchemaEnties(tableModel, node.getChildren());
            }
        }

    }

    public void setMapperUI(MapperUI mapperUI) {
        this.mapperUI = mapperUI;
    }

    public MapperUI getMapperUI() {
        return this.mapperUI;
    }

    public void selectionChanged(SelectionChangedEvent event) {
        if (!event.getSelection().isEmpty() && event.getSelection() instanceof IStructuredSelection) {
            Object firstElement = ((IStructuredSelection) event.getSelection()).getFirstElement();
            if (firstElement instanceof AbstractNodePart) {
                AbstractNode model = (AbstractNode) ((AbstractNodePart) firstElement).getModel();
                selectedNode = model;
                boolean isInputMain = false;
                if (model instanceof OutputTreeNode) {
                    OutputTreeNode outputTreeNodeRoot = XmlMapUtil.getOutputTreeNodeRoot((OutputTreeNode) model);
                    if (outputTreeNodeRoot != null && outputTreeNodeRoot.eContainer() instanceof OutputXmlTree) {
                        selectOutputXmlTree((OutputXmlTree) outputTreeNodeRoot.eContainer());
                    }
                } else if (model instanceof TreeNode) {
                    TreeNode inputTreeNodeRoot = XmlMapUtil.getInputTreeNodeRoot((TreeNode) model);
                    if (inputTreeNodeRoot != null && inputTreeNodeRoot.eContainer() instanceof InputXmlTree) {
                        selectInputXmlTree((InputXmlTree) inputTreeNodeRoot.eContainer());
                        isInputMain = !((InputXmlTree) inputTreeNodeRoot.eContainer()).isLookup();
                    }
                }
                if (!isInputMain) {
                    refreshStyledTextEditor(model);
                } else {
                    refreshStyledTextEditor(null);
                }
            } else if (firstElement instanceof InputXmlTreeEditPart) {
                selectInputXmlTree((InputXmlTree) ((InputXmlTreeEditPart) firstElement).getModel());
                refreshStyledTextEditor(null);
            } else if (firstElement instanceof OutputXmlTreeEditPart) {
                selectOutputXmlTree((OutputXmlTree) ((OutputXmlTreeEditPart) firstElement).getModel());
                refreshStyledTextEditor(null);
            }
        } else {
            ExtendedTableModel<TreeSchemaTableEntry> oldModel = mapperUI.getTabFolderEditors().getInputTreeSchemaEditor()
                    .getExtendedTableModel();
            if (oldModel != null && oldModel.getBeanCount() != 0) {
                List<TreeSchemaTableEntry> treeSchemaEntrys = new ArrayList<TreeSchemaTableEntry>();
                mapperUI.getTabFolderEditors().getInputTreeSchemaEditor()
                        .setExtendedControlModel(new ExtendedTableModel<TreeSchemaTableEntry>("Tree Schema", treeSchemaEntrys));
                mapperUI.getTabFolderEditors().getInputTreeSchemaEditor().getTableViewerCreator().refresh();
            }
            refreshStyledTextEditor(null);
        }

    }

    public void refreshStyledTextEditor(AbstractNode node) {
        if (node == null) {
            mapperUI.getTabFolderEditors().getStyledTextHandler().setTextWithoutNotifyListeners("");
            mapperUI.getTabFolderEditors().getStyledTextHandler().getStyledText().setEnabled(false);
            mapperUI.getTabFolderEditors().getStyledTextHandler().getStyledText().setEditable(false);
            return;
        }
        if (node instanceof TreeNode) {
            if (!((TreeNode) node).getChildren().isEmpty()) {
                mapperUI.getTabFolderEditors().getStyledTextHandler().setTextWithoutNotifyListeners("");
                mapperUI.getTabFolderEditors().getStyledTextHandler().getStyledText().setEnabled(false);
                mapperUI.getTabFolderEditors().getStyledTextHandler().getStyledText().setEditable(false);
                return;
            }
        }

        String expression = node.getExpression();
        if (expression == null) {
            expression = "";
        }
        mapperUI.getTabFolderEditors().getStyledTextHandler().setTextWithoutNotifyListeners(expression);
        mapperUI.getTabFolderEditors().getStyledTextHandler().getStyledText().setEnabled(true);
        mapperUI.getTabFolderEditors().getStyledTextHandler().getStyledText().setEditable(true);
        mapperUI.getTabFolderEditors().getStyledTextHandler().setSelectedNode(node);
    }

    public void refreshInputTreeSchemaEditor(InputXmlTree tree) {
        if (tree == null) {
            return;
        }
        ExtendedTableModel<TreeSchemaTableEntry> selectedInputTreeSchemaModel = getSelectedInputTreeSchemaModel(tree);
        mapperUI.getTabFolderEditors().getInputTreeSchemaEditor().setExtendedControlModel(selectedInputTreeSchemaModel);
        mapperUI.getTabFolderEditors().getInputTreeSchemaEditor().getTableViewerCreator().refresh();
    }

    public void refreshOutputTreeSchemaEditor(OutputXmlTree tree) {
        if (tree == null) {
            return;
        }
        ExtendedTableModel<TreeSchemaTableEntry> selectedInputTreeSchemaModel = getSelectedOutputTreeSchemaModel(tree);
        if (mapperUI.getTabFolderEditors().getOutputTreeSchemaEditor() != null) {
            mapperUI.getTabFolderEditors().getOutputTreeSchemaEditor().setExtendedControlModel(selectedInputTreeSchemaModel);
            mapperUI.getTabFolderEditors().getOutputTreeSchemaEditor().getTableViewerCreator().refresh();
        }
    }

    public void selectInputXmlTree(InputXmlTree inputTree) {
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
                IModifiedBeanListener<IMetadataColumn> columnListener = new IModifiedBeanListener<IMetadataColumn>() {

                    public void handleEvent(ModifiedBeanEvent<IMetadataColumn> event) {
                        if (AbstractMetadataTableEditorView.ID_COLUMN_NAME.equals(event.column.getId())) {
                            if (event.index < selectedInputTree.getNodes().size()) {
                                TreeNode treeNode = selectedInputTree.getNodes().get(event.index);
                                if (treeNode != null) {
                                    treeNode.setName((String) event.newValue);
                                    processColumnNameChanged(treeNode);
                                }
                            }
                        } else if (AbstractMetadataTableEditorView.ID_COLUMN_TYPE.equals(event.column.getId())) {
                            if (event.index < selectedInputTree.getNodes().size()) {
                                TreeNode treeNode = selectedInputTree.getNodes().get(event.index);
                                if (treeNode != null) {

                                    XmlMapUtil.detachConnectionsTarget(treeNode, copyOfMapData);
                                    XmlMapUtil.detachLookupSource(treeNode, copyOfMapData);
                                    XmlMapUtil.detachLookupTarget(treeNode, copyOfMapData);
                                    treeNode.getChildren().clear();
                                    treeNode.setType((String) event.newValue);

                                    if (XmlMapUtil.DOCUMENT.equals(event.newValue)) {
                                        TreeNode createTreeNode = XmlmapFactory.eINSTANCE.createTreeNode();
                                        createTreeNode.setName("root");
                                        createTreeNode.setType(XmlMapUtil.DEFAULT_DATA_TYPE);
                                        createTreeNode.setNodeType(NodeType.ELEMENT);
                                        createTreeNode.setXpath(XmlMapUtil.getXPath(treeNode.getXpath(),
                                                createTreeNode.getName(), createTreeNode.getNodeType()));
                                        treeNode.getChildren().add(createTreeNode);
                                    }

                                }
                            }
                        } else if (AbstractMetadataTableEditorView.ID_COLUMN_KEY.equals(event.column.getId())) {
                            if (event.index < selectedInputTree.getNodes().size()) {
                                TreeNode treeNode = selectedInputTree.getNodes().get(event.index);
                                treeNode.setKey((Boolean) event.newValue);
                            }
                        } else if (AbstractMetadataTableEditorView.ID_COLUMN_PATTERN.equals(event.column.getId())) {
                            if (event.index < selectedInputTree.getNodes().size()) {
                                TreeNode treeNode = selectedInputTree.getNodes().get(event.index);
                                treeNode.setPattern((String) event.newValue);
                            }
                        } else if (AbstractMetadataTableEditorView.ID_COLUMN_NULLABLE.equals(event.column.getId())) {
                            if (event.index < selectedInputTree.getNodes().size()) {
                                TreeNode treeNode = selectedInputTree.getNodes().get(event.index);
                                treeNode.setNullable((Boolean) event.newValue);
                            }
                        }

                    }

                };
                editor.addModifiedBeanListener(columnListener);

                editor.addAfterOperationListListener(new IListenableListListener() {

                    public void handleEvent(ListenableListEvent event) {
                        if (event.type == TYPE.ADDED) {
                            EList<TreeNode> nodes = selectedInputTree.getNodes();
                            List<IMetadataColumn> metadataColumns = (List<IMetadataColumn>) event.addedObjects;
                            if (event.index != null) {
                                int index = event.index;
                                for (IMetadataColumn column : metadataColumns) {
                                    TreeNode createTreeNode = XmlmapFactory.eINSTANCE.createTreeNode();
                                    createTreeNode.setName(column.getLabel());
                                    createTreeNode.setType(column.getTalendType());
                                    createTreeNode.setNullable(column.isNullable());
                                    createTreeNode.setPattern(column.getPattern());
                                    createTreeNode.setXpath(XmlMapUtil.getXPath(selectedInputTree.getName(),
                                            createTreeNode.getName(), createTreeNode.getNodeType()));

                                    selectedInputTree.getNodes().add(index, createTreeNode);
                                    index = index + 1;
                                }
                            }

                        } else if (event.type == TYPE.REMOVED) {
                            List<IMetadataColumn> metadataColumns = (List<IMetadataColumn>) event.removedObjects;
                            List treeNodeToRemove = new ArrayList();
                            for (IMetadataColumn column : metadataColumns) {
                                for (TreeNode node : selectedInputTree.getNodes()) {
                                    if (node.getName() != null && node.getName().equals(column.getLabel())) {
                                        XmlMapUtil.detachConnectionsTarget(node, copyOfMapData);
                                        XmlMapUtil.detachLookupSource(node, copyOfMapData);
                                        XmlMapUtil.detachLookupTarget(node, copyOfMapData);
                                        treeNodeToRemove.add(node);
                                    }
                                }
                            }
                            selectedInputTree.getNodes().removeAll(treeNodeToRemove);

                        } else if (event.type == TYPE.SWAPED) {
                            List<Integer> listIndexTarget = event.indicesTarget;
                            List<Integer> listIndexOrignal = event.indicesOrigin;
                            for (int i = 0; i < listIndexOrignal.size(); i++) {
                                int orignal = listIndexOrignal.get(i);
                                int target = listIndexTarget.get(i);
                                if (orignal < selectedInputTree.getNodes().size()) {
                                    TreeNode tempTreeNode = selectedInputTree.getNodes().get(orignal);
                                    selectedInputTree.getNodes().remove(orignal);
                                    selectedInputTree.getNodes().add(target, tempTreeNode);
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

    private void processColumnNameChanged(final TreeNode treeNode) {
        new AsynchronousThreading(20, false, mapperUI.getTabFolderEditors().getDisplay(), new Runnable() {

            public void run() {
                boolean propagate = MessageDialog.openQuestion(mapperUI.getTabFolderEditors().getShell(), "Propagate",
                        "Propagate changes to all related expressions in order to keep the links valid ?");
                XmlMapUtil.updateXPathAndExpression(copyOfMapData, treeNode, treeNode.getName(),
                        XmlMapUtil.getXPathLength(treeNode.getXpath()), propagate);
            }
        }).start();
    }

    public void selectOutputXmlTree(OutputXmlTree outputTree) {
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

                IModifiedBeanListener<IMetadataColumn> columnListener = new IModifiedBeanListener<IMetadataColumn>() {

                    public void handleEvent(ModifiedBeanEvent<IMetadataColumn> event) {
                        if (AbstractMetadataTableEditorView.ID_COLUMN_NAME.equals(event.column.getId())) {
                            if (event.index < oldSelectedOut.getNodes().size()) {
                                TreeNode treeNode = oldSelectedOut.getNodes().get(event.index);
                                if (treeNode != null) {
                                    treeNode.setName((String) event.newValue);
                                }
                            }
                        } else if (AbstractMetadataTableEditorView.ID_COLUMN_TYPE.equals(event.column.getId())) {
                            if (event.index < oldSelectedOut.getNodes().size()) {
                                TreeNode treeNode = oldSelectedOut.getNodes().get(event.index);
                                if (treeNode != null) {

                                    XmlMapUtil.detachConnectionsSouce(treeNode, copyOfMapData);
                                    treeNode.getChildren().clear();
                                    treeNode.setType((String) event.newValue);

                                    if (XmlMapUtil.DOCUMENT.equals(event.newValue)) {
                                        OutputTreeNode createTreeNode = XmlmapFactory.eINSTANCE.createOutputTreeNode();
                                        createTreeNode.setName("root");
                                        createTreeNode.setType(XmlMapUtil.DEFAULT_DATA_TYPE);
                                        createTreeNode.setNodeType(NodeType.ELEMENT);
                                        createTreeNode.setXpath(XmlMapUtil.getXPath(treeNode.getXpath(),
                                                createTreeNode.getName(), createTreeNode.getNodeType()));
                                        treeNode.getChildren().add(createTreeNode);
                                    }

                                }
                            }
                        } else if (AbstractMetadataTableEditorView.ID_COLUMN_KEY.equals(event.column.getId())) {
                            if (event.index < oldSelectedOut.getNodes().size()) {
                                TreeNode treeNode = oldSelectedOut.getNodes().get(event.index);
                                treeNode.setKey((Boolean) event.newValue);
                            }
                        } else if (AbstractMetadataTableEditorView.ID_COLUMN_PATTERN.equals(event.column.getId())) {
                            if (event.index < oldSelectedOut.getNodes().size()) {
                                TreeNode treeNode = oldSelectedOut.getNodes().get(event.index);
                                treeNode.setPattern((String) event.newValue);
                            }
                        } else if (AbstractMetadataTableEditorView.ID_COLUMN_NULLABLE.equals(event.column.getId())) {
                            if (event.index < oldSelectedOut.getNodes().size()) {
                                TreeNode treeNode = oldSelectedOut.getNodes().get(event.index);
                                treeNode.setNullable((Boolean) event.newValue);
                            }
                        }

                    }

                };
                editor.addModifiedBeanListener(columnListener);

                editor.addAfterOperationListListener(new IListenableListListener() {

                    public void handleEvent(ListenableListEvent event) {

                        if (event.type == TYPE.ADDED) {
                            EList<OutputTreeNode> nodes = oldSelectedOut.getNodes();
                            List<IMetadataColumn> metadataColumns = (List<IMetadataColumn>) event.addedObjects;
                            if (event.index != null) {
                                int index = event.index;
                                for (IMetadataColumn column : metadataColumns) {
                                    OutputTreeNode createTreeNode = XmlmapFactory.eINSTANCE.createOutputTreeNode();
                                    createTreeNode.setName(column.getLabel());
                                    createTreeNode.setType(column.getTalendType());
                                    createTreeNode.setNullable(column.isNullable());
                                    createTreeNode.setPattern(column.getPattern());
                                    createTreeNode.setXpath(XmlMapUtil.getXPath(oldSelectedOut.getName(),
                                            createTreeNode.getName(), createTreeNode.getNodeType()));

                                    oldSelectedOut.getNodes().add(index, createTreeNode);
                                    index = index + 1;
                                }
                            }

                        } else if (event.type == TYPE.REMOVED) {
                            List<IMetadataColumn> metadataColumns = (List<IMetadataColumn>) event.removedObjects;
                            List treeNodeToRemove = new ArrayList();
                            for (IMetadataColumn column : metadataColumns) {
                                for (TreeNode node : oldSelectedOut.getNodes()) {
                                    if (node.getName() != null && node.getName().equals(column.getLabel())) {
                                        XmlMapUtil.detachConnectionsSouce(node, copyOfMapData);
                                        treeNodeToRemove.add(node);
                                    }
                                }
                            }
                            oldSelectedOut.getNodes().removeAll(treeNodeToRemove);

                        } else if (event.type == TYPE.SWAPED) {
                            List<Integer> listIndexTarget = event.indicesTarget;
                            List<Integer> listIndexOrignal = event.indicesOrigin;
                            for (int i = 0; i < listIndexOrignal.size(); i++) {
                                int orignal = listIndexOrignal.get(i);
                                int target = listIndexTarget.get(i);
                                if (orignal < oldSelectedOut.getNodes().size()) {
                                    OutputTreeNode tempTreeNode = oldSelectedOut.getNodes().get(orignal);
                                    oldSelectedOut.getNodes().remove(orignal);
                                    oldSelectedOut.getNodes().add(target, tempTreeNode);
                                }
                            }

                        }

                    }

                });
            }
            refreshOutputTreeSchemaEditor(outputTree);
        }

    }
}
