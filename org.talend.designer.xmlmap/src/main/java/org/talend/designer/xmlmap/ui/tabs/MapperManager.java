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
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CellEditor;
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
import org.talend.core.ui.metadata.dialog.CustomTableManager;
import org.talend.core.ui.metadata.editor.AbstractMetadataTableEditorView;
import org.talend.core.ui.metadata.editor.MetadataTableEditorView;
import org.talend.designer.xmlmap.XmlMapComponent;
import org.talend.designer.xmlmap.model.emf.xmlmap.AbstractInOutTree;
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
import org.talend.designer.xmlmap.parts.TreeNodeEditPart;
import org.talend.designer.xmlmap.parts.directedit.ExpressionCellEditor;
import org.talend.designer.xmlmap.parts.directedit.XmlMapNodeDirectEditManager;
import org.talend.designer.xmlmap.ui.MapperUI;
import org.talend.designer.xmlmap.ui.tabs.table.TreeSchemaTableEntry;
import org.talend.designer.xmlmap.util.XmlMapUtil;
import org.talend.designer.xmlmap.util.problem.ProblemsAnalyser;

/**
 * wchen class global comment. Detailled comment
 */
public class MapperManager implements ISelectionChangedListener {

    private XmlMapComponent mapperComponent;

    private XmlMapData copyOfMapData;

    private MapperUI mapperUI;

    private InputXmlTree selectedInputTree;

    private OutputXmlTree selectedOutputTree;

    private ProblemsAnalyser problemsAnalyser;

    private XmlMapNodeDirectEditManager currentDirectEditManager;

    private boolean isDieOnError;

    public static final String ERROR_REJECT = "ErrorReject";//$NON-NLS-1$

    public static final String ERROR_REJECT_MESSAGE = "errorMessage";

    public static final String ERROR_REJECT_STACK_TRACE = "errorStackTrace";

    public MapperManager(XmlMapComponent mapperComponent, XmlMapData copyOfMapData) {
        this.mapperComponent = mapperComponent;
        this.copyOfMapData = copyOfMapData;
        problemsAnalyser = new ProblemsAnalyser(this);
        problemsAnalyser.checkProblems();
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
            tableModel.setName(inputXmlTree.getName());
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
            tableModel.setName(outputXmlTree.getName());
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
            Iterator iterator = ((IStructuredSelection) event.getSelection()).iterator();
            while (iterator.hasNext()) {
                Object firstElement = iterator.next();
                if (firstElement instanceof AbstractNodePart) {
                    AbstractNode model = (AbstractNode) ((AbstractNodePart) firstElement).getModel();
                    boolean isInputMain = false;
                    if (model instanceof OutputTreeNode) {
                        OutputTreeNode outputTreeNodeRoot = (OutputTreeNode) XmlMapUtil.getTreeNodeRoot((OutputTreeNode) model);
                        if (outputTreeNodeRoot != null && outputTreeNodeRoot.eContainer() instanceof OutputXmlTree) {
                            selectOutputXmlTree((OutputXmlTree) outputTreeNodeRoot.eContainer());

                            onSelectedEntries((IStructuredSelection) event.getSelection(), selectedOutputTree);
                        }
                    } else if (model instanceof TreeNode) {
                        TreeNode inputTreeNodeRoot = XmlMapUtil.getTreeNodeRoot((TreeNode) model);
                        if (inputTreeNodeRoot != null && inputTreeNodeRoot.eContainer() instanceof InputXmlTree) {
                            selectInputXmlTree((InputXmlTree) inputTreeNodeRoot.eContainer());
                            isInputMain = !((InputXmlTree) inputTreeNodeRoot.eContainer()).isLookup();
                            onSelectedEntries((IStructuredSelection) event.getSelection(), selectedInputTree);
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

    private void onSelectedEntries(IStructuredSelection selection, AbstractInOutTree selectedTree) {
        // do selection in metadata schema editor
        EList<? extends TreeNode> nodes = null;
        if (selectedTree instanceof InputXmlTree) {
            nodes = ((InputXmlTree) selectedTree).getNodes();
        } else {
            nodes = ((OutputXmlTree) selectedTree).getNodes();
        }
        List<Integer> selectionIndices = new ArrayList<Integer>();

        Iterator iterator = selection.iterator();
        while (iterator.hasNext()) {
            Object obj = iterator.next();
            if (obj instanceof TreeNodeEditPart) {
                TreeNode model = (TreeNode) ((TreeNodeEditPart) obj).getModel();
                if (model.eContainer() == selectedTree) {
                    selectionIndices.add(nodes.indexOf(model));
                }
            }
        }

        int selections[] = new int[selectionIndices.size()];
        for (int i = 0; i < selectionIndices.size(); i++) {
            selections[i] = selectionIndices.get(i);
        }

        MetadataTableEditorView metaEditorView = null;
        if (selectedTree instanceof InputXmlTree) {
            metaEditorView = mapperUI.getTabFolderEditors().getInputMetaEditorView();
        } else {
            metaEditorView = mapperUI.getTabFolderEditors().getOutputMetaEditorView();
        }
        metaEditorView.getExtendedTableViewer().getTableViewerCreator().getSelectionHelper().setSelection(selections);

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
        ExtendedTableModel<TreeSchemaTableEntry> selectedInputTreeSchemaModel = getSelectedInputTreeSchemaModel(tree);
        mapperUI.getTabFolderEditors().getInputTreeSchemaEditor().setExtendedControlModel(selectedInputTreeSchemaModel);
        mapperUI.getTabFolderEditors().getInputTreeSchemaEditor().getTableViewerCreator().refresh();
    }

    public void refreshOutputTreeSchemaEditor(OutputXmlTree tree) {
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
                MetadataTableEditor editor = new MetadataTableEditor(table, selectedInputTree.getName());
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
                                    if (treeNode.getType() != null && treeNode.getType().equals(XmlMapUtil.DOCUMENT)) {
                                        XmlMapUtil.detachNodeConnections(treeNode, copyOfMapData, true);
                                        treeNode.getChildren().clear();
                                    }
                                    treeNode.setType((String) event.newValue);

                                    if (XmlMapUtil.DOCUMENT.equals(event.newValue)) {
                                        XmlMapUtil.detachNodeConnections(treeNode, copyOfMapData, true);
                                        TreeNode createTreeNode = XmlmapFactory.eINSTANCE.createTreeNode();
                                        createTreeNode.setName("root");
                                        createTreeNode.setType(XmlMapUtil.DEFAULT_DATA_TYPE);
                                        createTreeNode.setNodeType(NodeType.ELEMENT);
                                        createTreeNode.setXpath(XmlMapUtil.getXPath(treeNode.getXpath(),
                                                createTreeNode.getName(), createTreeNode.getNodeType()));
                                        treeNode.getChildren().add(createTreeNode);

                                    }
                                    problemsAnalyser.checkLoopProblems(selectedInputTree);
                                    mapperUI.updateStatusBar();
                                    refreshInputTreeSchemaEditor(selectedInputTree);
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
                                        XmlMapUtil.detachNodeConnections(node, copyOfMapData, true);
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
        if (outputTree != selectedOutputTree) {
            selectedOutputTree = outputTree;
            MetadataTableEditorView outputMetaEditorView = mapperUI.getTabFolderEditors().getOutputMetaEditorView();
            List<IMetadataTable> metadataList = mapperComponent.getMetadataList();
            IMetadataTable table = null;
            for (int i = 0; i < metadataList.size(); i++) {
                if (outputTree.getName() != null && outputTree.getName().equals(metadataList.get(i).getTableName())) {
                    table = metadataList.get(i);
                    break;
                }
            }
            if (outputTree.isErrorReject()) {
                for (IMetadataColumn column : table.getListColumns()) {
                    if (ERROR_REJECT_MESSAGE.equals(column.getLabel()) || ERROR_REJECT_STACK_TRACE.equals(column.getLabel())) {
                        column.setCustom(true);
                    }
                }

                CustomTableManager.addCustomManagementToTable(mapperUI.getTabFolderEditors().getOutputMetaEditorView(), true);
            }

            if (table != null) {
                MetadataTableEditor editor = new MetadataTableEditor(table, table.getLabel());
                outputMetaEditorView.setMetadataTableEditor(editor);
                editor.setModifiedBeanListenable(outputMetaEditorView.getTableViewerCreator());

                IModifiedBeanListener<IMetadataColumn> columnListener = new IModifiedBeanListener<IMetadataColumn>() {

                    public void handleEvent(ModifiedBeanEvent<IMetadataColumn> event) {
                        if (AbstractMetadataTableEditorView.ID_COLUMN_NAME.equals(event.column.getId())) {
                            if (event.index < selectedOutputTree.getNodes().size()) {
                                TreeNode treeNode = selectedOutputTree.getNodes().get(event.index);
                                if (treeNode != null) {
                                    treeNode.setName((String) event.newValue);
                                    XmlMapUtil.updateXPathAndExpression(copyOfMapData, treeNode, treeNode.getName(),
                                            XmlMapUtil.getXPathLength(treeNode.getXpath()), true);
                                }
                            }
                        } else if (AbstractMetadataTableEditorView.ID_COLUMN_TYPE.equals(event.column.getId())) {
                            if (event.index < selectedOutputTree.getNodes().size()) {
                                TreeNode treeNode = selectedOutputTree.getNodes().get(event.index);
                                if (treeNode != null) {
                                    if (treeNode.getType() != null && treeNode.getType().equals(XmlMapUtil.DOCUMENT)) {
                                        XmlMapUtil.detachNodeConnections(treeNode, copyOfMapData, true);
                                        treeNode.getChildren().clear();
                                    }
                                    treeNode.setType((String) event.newValue);

                                    if (XmlMapUtil.DOCUMENT.equals(event.newValue)) {
                                        XmlMapUtil.detachNodeConnections(treeNode, copyOfMapData, true);
                                        OutputTreeNode createTreeNode = XmlmapFactory.eINSTANCE.createOutputTreeNode();
                                        createTreeNode.setName("root");
                                        createTreeNode.setType(XmlMapUtil.DEFAULT_DATA_TYPE);
                                        createTreeNode.setNodeType(NodeType.ELEMENT);
                                        createTreeNode.setXpath(XmlMapUtil.getXPath(treeNode.getXpath(),
                                                createTreeNode.getName(), createTreeNode.getNodeType()));
                                        treeNode.getChildren().add(createTreeNode);

                                    }
                                    problemsAnalyser.checkLoopProblems(selectedOutputTree);
                                    mapperUI.updateStatusBar();
                                    refreshOutputTreeSchemaEditor(selectedOutputTree);
                                }
                            }
                        } else if (AbstractMetadataTableEditorView.ID_COLUMN_KEY.equals(event.column.getId())) {
                            if (event.index < selectedOutputTree.getNodes().size()) {
                                TreeNode treeNode = selectedOutputTree.getNodes().get(event.index);
                                treeNode.setKey((Boolean) event.newValue);
                            }
                        } else if (AbstractMetadataTableEditorView.ID_COLUMN_PATTERN.equals(event.column.getId())) {
                            if (event.index < selectedOutputTree.getNodes().size()) {
                                TreeNode treeNode = selectedOutputTree.getNodes().get(event.index);
                                treeNode.setPattern((String) event.newValue);
                            }
                        } else if (AbstractMetadataTableEditorView.ID_COLUMN_NULLABLE.equals(event.column.getId())) {
                            if (event.index < selectedOutputTree.getNodes().size()) {
                                TreeNode treeNode = selectedOutputTree.getNodes().get(event.index);
                                treeNode.setNullable((Boolean) event.newValue);
                            }
                        }

                    }

                };
                editor.addModifiedBeanListener(columnListener);

                editor.addAfterOperationListListener(new IListenableListListener() {

                    public void handleEvent(ListenableListEvent event) {

                        if (event.type == TYPE.ADDED) {
                            EList<OutputTreeNode> nodes = selectedOutputTree.getNodes();
                            List<IMetadataColumn> metadataColumns = (List<IMetadataColumn>) event.addedObjects;
                            if (event.index != null) {
                                int index = event.index;
                                for (IMetadataColumn column : metadataColumns) {
                                    OutputTreeNode createTreeNode = XmlmapFactory.eINSTANCE.createOutputTreeNode();
                                    createTreeNode.setName(column.getLabel());
                                    createTreeNode.setType(column.getTalendType());
                                    createTreeNode.setNullable(column.isNullable());
                                    createTreeNode.setPattern(column.getPattern());
                                    createTreeNode.setXpath(XmlMapUtil.getXPath(selectedOutputTree.getName(),
                                            createTreeNode.getName(), createTreeNode.getNodeType()));

                                    selectedOutputTree.getNodes().add(index, createTreeNode);
                                    index = index + 1;
                                }
                            }

                        } else if (event.type == TYPE.REMOVED) {
                            List<IMetadataColumn> metadataColumns = (List<IMetadataColumn>) event.removedObjects;
                            List treeNodeToRemove = new ArrayList();
                            for (IMetadataColumn column : metadataColumns) {
                                for (TreeNode node : selectedOutputTree.getNodes()) {
                                    if (node.getName() != null && node.getName().equals(column.getLabel())) {
                                        XmlMapUtil.detachNodeConnections(node, copyOfMapData, true);
                                        treeNodeToRemove.add(node);
                                    }
                                }
                            }
                            selectedOutputTree.getNodes().removeAll(treeNodeToRemove);

                        } else if (event.type == TYPE.SWAPED) {
                            List<Integer> listIndexTarget = event.indicesTarget;
                            List<Integer> listIndexOrignal = event.indicesOrigin;
                            for (int i = 0; i < listIndexOrignal.size(); i++) {
                                int orignal = listIndexOrignal.get(i);
                                int target = listIndexTarget.get(i);
                                if (orignal < selectedOutputTree.getNodes().size()) {
                                    OutputTreeNode tempTreeNode = selectedOutputTree.getNodes().get(orignal);
                                    selectedOutputTree.getNodes().remove(orignal);
                                    selectedOutputTree.getNodes().add(target, tempTreeNode);
                                }
                            }

                        }

                    }

                });
            }
            refreshOutputTreeSchemaEditor(outputTree);
        }

    }

    public boolean isDieOnError() {
        return isDieOnError;
    }

    public void setDieOnError(boolean isDieOnError) {
        this.isDieOnError = isDieOnError;
    }

    public ProblemsAnalyser getProblemsAnalyser() {
        return this.problemsAnalyser;
    }

    public void setCurrentDirectEditManager(XmlMapNodeDirectEditManager currentDirectEditManager) {
        this.currentDirectEditManager = currentDirectEditManager;
    }

    public void fireCurrentDirectEditApply() {
        if (currentDirectEditManager != null) {
            CellEditor cellEditor = currentDirectEditManager.getCellEditor();
            if (cellEditor instanceof ExpressionCellEditor) {
                ((ExpressionCellEditor) cellEditor).fireApplyEditorValue();
            }
        }
        currentDirectEditManager = null;
    }
}
