// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
//
// ============================================================================
package org.talend.repository.ui.wizards.metadata.connection.files.xml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.ui.swt.drawing.link.BezierHorizontalLink;
import org.talend.commons.ui.swt.drawing.link.ExtremityEastArrow;
import org.talend.commons.ui.swt.drawing.link.IExtremityLink;
import org.talend.commons.ui.swt.drawing.link.IStyleLink;
import org.talend.commons.ui.swt.drawing.link.LinkDescriptor;
import org.talend.commons.ui.swt.drawing.link.StyleLink;
import org.talend.commons.ui.swt.drawing.link.TreeExtremityDescriptor;
import org.talend.commons.ui.swt.extended.table.ExtendedControlEvent;
import org.talend.commons.ui.swt.extended.table.IExtendedControlListener;
import org.talend.commons.ui.swt.extended.table.AbstractExtendedControlViewer.EVENT_TYPE;
import org.talend.commons.ui.swt.linking.TreeToTablesLinker;
import org.talend.commons.ui.swt.proposal.TextCellEditorWithProposal;
import org.talend.commons.ui.swt.tableviewer.IModifiedBeanListener;
import org.talend.commons.ui.swt.tableviewer.ModifiedBeanEvent;
import org.talend.commons.ui.swt.tableviewer.selection.ILineSelectionListener;
import org.talend.commons.ui.swt.tableviewer.selection.LineSelectionEvent;
import org.talend.commons.ui.swt.tableviewer.selection.SelectionHelper;
import org.talend.commons.ui.utils.TableUtils;
import org.talend.commons.ui.ws.WindowSystem;
import org.talend.commons.utils.data.list.IListenableListListener;
import org.talend.commons.utils.data.list.ListenableListEvent;
import org.talend.commons.utils.data.list.ListenableListEvent.TYPE;
import org.talend.commons.xml.NodeRetriever;
import org.talend.core.model.metadata.builder.connection.SchemaTarget;
import org.talend.core.model.metadata.builder.connection.XmlXPathLoopDescriptor;
import org.talend.core.model.targetschema.editor.XmlExtractorFieldModel;
import org.talend.core.model.targetschema.editor.XmlExtractorLoopModel;
import org.talend.repository.ui.wizards.metadata.connection.files.xml.dnd.XmlToSchemaDragAndDropHandler;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class XmlToXPathLinker extends TreeToTablesLinker<Object, Object> {

    private TreePopulator treePopulator;

    private ExtractionFieldsWithXPathEditorView fieldsTableEditorView;

    private NodeRetriever nodeRetriever;

    private ExtractionLoopWithXPathEditorView loopTableEditorView;

    private StyleLink selectedLoopStyleLink;

    private ArrayList<String> loopXpathNodes = new ArrayList<String>(0);

    private Comparator<LinkDescriptor<TreeItem, Object, Table, Object>> drawingLinksComparator;

    private ArrayList<Node> uniqueLoopNodes = new ArrayList<Node>();

    private ArrayList<Node> allLoopNodes = new ArrayList<Node>();

    /**
     * DOC amaumont XmlToMetadataTableLinker constructor comment.
     * 
     * @param commonParent common main parent of tree and table, it and its children should have backgoundMode
     * configured with SWT.INHERIT_FORCE, same configuration for parents of tree and table.
     * @param tree
     * @param loopTableEditorView
     * @param table
     */
    public XmlToXPathLinker(Composite commonParent) {
        super(commonParent);
    }

    public void init(Tree tree, ExtractionLoopWithXPathEditorView loopTableEditorView,
            ExtractionFieldsWithXPathEditorView fieldsTableEditorView, TreePopulator treePopulator) {
        init(tree, new Table[] { loopTableEditorView.getExtendedTableViewer().getTableViewerCreator().getTable(),
                fieldsTableEditorView.getExtendedTableViewer().getTableViewerCreator().getTable(), }, new XmlExtractorBgRefresher(this));
        this.treePopulator = treePopulator;
        this.loopTableEditorView = loopTableEditorView;
        this.fieldsTableEditorView = fieldsTableEditorView;
        this.nodeRetriever = new NodeRetriever(treePopulator.getFilePath(), getCurrentLoopXPath());
        TextCellEditorWithProposal xPathCellEditor = loopTableEditorView.getXPathCellEditor();
        xPathCellEditor.setContentProposalProvider(new XPathProposalProvider(this, false));
        xPathCellEditor = fieldsTableEditorView.getXPathCellEditor();
        xPathCellEditor.setContentProposalProvider(new XPathProposalProvider(this, true));
        init();
    }

    public String getAbsoluteXPath(TreeItem treeItem) {
        return treePopulator.getAbsoluteXPath(treeItem);
    }

    /**
     * DOC amaumont Comment method "init".
     * 
     * @param tree
     */
    private void init() {

        // commonParent.addDisposeListener(new DisposeListener() {
        // public void widgetDisposed(DisposeEvent e) {
        //        
        // commonParent.removeDisposeListener(this);
        // }
        // });

        StyleLink unselectedStyleLink = new StyleLink();
        unselectedStyleLink.setDrawableLink(new BezierHorizontalLink(unselectedStyleLink));
        unselectedStyleLink.setForegroundColor(getBgDrawableComposite().getDisplay().getSystemColor(SWT.COLOR_GRAY));
        unselectedStyleLink.setLineWidth(2);
        unselectedStyleLink.setExtremity2(new ExtremityEastArrow(unselectedStyleLink, -ExtremityEastArrow.WIDTH_ARROW - 2, 0));
        setUnselectedStyleLink(unselectedStyleLink);

        StyleLink selectedStyleLink = new StyleLink();
        selectedStyleLink.setDrawableLink(new BezierHorizontalLink(selectedStyleLink));
        selectedStyleLink.setForegroundColor(getBgDrawableComposite().getDisplay().getSystemColor(SWT.COLOR_BLUE));
        selectedStyleLink.setLineWidth(2);
        selectedStyleLink.setExtremity2(new ExtremityEastArrow(selectedStyleLink, -ExtremityEastArrow.WIDTH_ARROW - 2, 0));
        setSelectedStyleLink(selectedStyleLink);

        initListeners();
        createLinks();

    }

    /**
     * DOC amaumont Comment method "initListeners".
     */
    private void initListeners() {
        initLoopListeners();
        initFieldsListeners();
        new XmlToSchemaDragAndDropHandler(this);
    }

    /**
     * DOC amaumont Comment method "createLinks".
     */
    public void createLinks() {
        TableItem[] tableItems = loopTableEditorView.getTable().getItems();
        List<XmlXPathLoopDescriptor> xpathLoopDescriptorList = loopTableEditorView.getModel().getBeansList();
        for (int i = 0; i < tableItems.length; i++) {
            TableItem tableItem = tableItems[i];
            XmlXPathLoopDescriptor xpathLoopDescriptor = xpathLoopDescriptorList.get(i);
            String xPathQuery = xpathLoopDescriptor.getAbsoluteXPathQuery();
            if (xPathQuery != null) {
                createLoopLinks(xPathQuery, tableItem);
            }
        }

        tableItems = fieldsTableEditorView.getTable().getItems();
        List<SchemaTarget> schemaTargetList = fieldsTableEditorView.getModel().getBeansList();
        for (int i = 0; i < tableItems.length; i++) {
            TableItem tableItem = tableItems[i];
            SchemaTarget schemaTarget = schemaTargetList.get(i);
            String relativeXpathQuery = schemaTarget.getRelativeXPathQuery();
            createFieldLinks(relativeXpathQuery, tableItem);
        }
        getBackgroundRefresher().refreshBackground();
    }

    /**
     * DOC amaumont Comment method "initListeners".
     */
    private void initLoopListeners() {

        XmlExtractorLoopModel loopModel = this.loopTableEditorView.getModel();

        final Table loopTable = this.loopTableEditorView.getTableViewerCreator().getTable();

        loopModel.addModifiedBeanListener(new IModifiedBeanListener<XmlXPathLoopDescriptor>() {

            public void handleEvent(ModifiedBeanEvent<XmlXPathLoopDescriptor> event) {
                handleModifiedBeanEvent(event);
            }

            private void handleModifiedBeanEvent(ModifiedBeanEvent<XmlXPathLoopDescriptor> event) {
                if (event.column == loopTableEditorView.getXPathColumn()) {
                    onXPathValueChanged(loopTable, (String) event.newValue, event.index);
                    parseAllFieldsXPathExpressions();
                }

            }

        });

        this.loopTableEditorView.getExtendedTableViewer().addListener(new IExtendedControlListener() {

            public void handleEvent(ExtendedControlEvent event) {
                if (event.getType() == EVENT_TYPE.MODEL_CHANGED) {
                    nodeRetriever.setCurrentLoopXPath(getCurrentLoopXPath());
                }
            }

        });

        SelectionHelper selectionHelper = this.loopTableEditorView.getTableViewerCreator().getSelectionHelper();
        final ILineSelectionListener afterLineSelectionListener = new ILineSelectionListener() {

            public void handle(LineSelectionEvent e) {
                updateLinksStyleAndControlsSelection(e.source.getTable());
            }
        };
        selectionHelper.addAfterSelectionListener(afterLineSelectionListener);
    }

    /**
     * DOC amaumont Comment method "initListeners".
     */
    private void initFieldsListeners() {

        XmlExtractorFieldModel schemaModel = this.fieldsTableEditorView.getModel();

        final Table fieldsTable = this.fieldsTableEditorView.getTable();

        schemaModel.addModifiedBeanListener(new IModifiedBeanListener<SchemaTarget>() {

            public void handleEvent(ModifiedBeanEvent<SchemaTarget> event) {
                handleModifiedBeanEvent(event);
            }

            private void handleModifiedBeanEvent(ModifiedBeanEvent<SchemaTarget> event) {
                if (event.column == fieldsTableEditorView.getXPathColumn()) {
                    onXPathValueChanged(fieldsTable, (String) event.newValue, event.index);
                }

            }

        });

        schemaModel.addBeforeOperationListListener(-50, new IListenableListListener<SchemaTarget>() {

            public void handleEvent(ListenableListEvent<SchemaTarget> event) {
                handleListenableListBeforeTableViewerRefreshedEvent(event);
            }

        });

        schemaModel.addAfterOperationListListener(new IListenableListListener<SchemaTarget>() {

            public void handleEvent(ListenableListEvent<SchemaTarget> event) {
                handleListenableListAfterTableViewerRefreshedEvent(event);
            }

        });

        SelectionHelper selectionHelper = this.fieldsTableEditorView.getTableViewerCreator().getSelectionHelper();
        final ILineSelectionListener afterLineSelectionListener = new ILineSelectionListener() {

            public void handle(LineSelectionEvent e) {
                updateLinksStyleAndControlsSelection(e.source.getTable());
            }
        };
        selectionHelper.addAfterSelectionListener(afterLineSelectionListener);

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.swt.linking.TreeToTableLinker#drawBackground(org.eclipse.swt.graphics.GC)
     */
    @Override
    public void drawBackground(GC gc) {

        Rectangle clipBounds = getTree().getBounds();

        List<Table> tables = getTables();

        if (tables == null || tables.size() == 0) {
            throw new IllegalStateException();
        }
        Table table = tables.get(0);

        Rectangle tableBounds = table.getDisplay().map(table, getBgDrawableComposite(), table.getBounds());
        // System.out.println(tableBounds);
        int offset = 20;

        clipBounds.width = tableBounds.x;
        clipBounds.height += offset - 4;
        clipBounds.x = 0;
        clipBounds.y = offset;

//        if(!WindowSystem.isGTK()) {
        	gc.setClipping(clipBounds);
//        }

        super.drawBackground(gc);
        
//        if(!WindowSystem.isGTK()) {
        	gc.setClipping((Rectangle) null);
//        }
    }

    private void handleListenableListBeforeTableViewerRefreshedEvent(ListenableListEvent<SchemaTarget> event) {
        if (event.type == TYPE.REMOVED) {
            Collection<SchemaTarget> removedObjects = event.removedObjects;
            for (SchemaTarget target : removedObjects) {
                linksManager.removeLinksFromDataItem2(target);
            }
        }

    }

    public void handleListenableListAfterTableViewerRefreshedEvent(ListenableListEvent<SchemaTarget> event) {
        if (event.type == ListenableListEvent.TYPE.ADDED) {
            // event.indicesTarget;
            Collection<SchemaTarget> addedObjects = event.addedObjects;
            for (SchemaTarget schemaTarget : addedObjects) {
                TableItem tableItem = TableUtils.getTableItem(fieldsTableEditorView.getTable(), schemaTarget);
                if (tableItem == null) {
                    throw new IllegalStateException("tableItem shouldn't be null");
                }
                createFieldLinks(schemaTarget.getRelativeXPathQuery(), tableItem);
            }
            getBackgroundRefresher().refreshBackground();
        } else if (event.type == ListenableListEvent.TYPE.SWAPED) {
            getBackgroundRefresher().refreshBackground();
        } else if (event.type == TYPE.REMOVED) {
            getBackgroundRefresher().refreshBackground();
        }

    }

    /**
     * DOC amaumont Comment method "addLink".
     * 
     * @param treeItem
     * @param dataItem1
     * @param table
     * @param dataItem2
     */
    public void addLoopLink(TreeItem treeItem, Object dataItem1, Table table, XmlXPathLoopDescriptor dataItem2) {
        LinkDescriptor<TreeItem, Object, Table, Object> link = addLink(treeItem, dataItem1, table, dataItem2);
        link.setStyleLink(getSelectedLoopStyleLink());
    }

    /**
     * DOC amaumont Comment method "addLink".
     * 
     * @param treeItem
     * @param dataItem1
     * @param table
     * @param dataItem2
     */
    private LinkDescriptor<TreeItem, Object, Table, Object> addLink(TreeItem treeItem, Object dataItem1, Table table, Object dataItem2) {
        LinkDescriptor<TreeItem, Object, Table, Object> link = new LinkDescriptor<TreeItem, Object, Table, Object>(
                new TreeExtremityDescriptor(treeItem, dataItem1), new ExtremityLink<Table, Object>(table, dataItem2));

        link.setStyleLink(getUnselectedStyleLink());
        getLinksManager().addLink(link);
        updateLinksStyleAndControlsSelection(table);
        return link;
    }

    /**
     * DOC amaumont Comment method "addLink".
     * 
     * @param treeItem
     * @param dataItem1
     * @param table
     * @param dataItem2
     */
    public void addFieldLink(TreeItem treeItem, Object dataItem1, Table table, SchemaTarget dataItem2) {
        LinkDescriptor<TreeItem, Object, Table, Object> link = addLink(treeItem, dataItem1, table, dataItem2);
        // link.setStyleLink(getSelectedStyleLink());
    }

    /**
     * DOC amaumont Comment method "removeAllLinks".
     */
    public void removeAllLinks() {
        getLinksManager().clearLinks();
    }

    public boolean removeLinksFromTableEntry(SchemaTarget schemaTarget) {
        return getLinksManager().removeLinksFromDataItem2(schemaTarget);
    }

    /**
     * DOC amaumont Comment method "onXPathValueChanged".
     * 
     * @param table
     * @param newValue
     * @param itemIndex
     */
    public void onXPathValueChanged(Table table, String newValue, int itemIndex) {
        if (isLoopTable(table)) {
            nodeRetriever.setCurrentLoopXPath(newValue);
        }
        TableItem tableItem = table.getItem(itemIndex);
        linksManager.removeLinksFromDataItem2(tableItem.getData());
        if (isLoopTable(table)) {
            createLoopLinks(newValue, tableItem);
        } else {
            createFieldLinks(newValue, tableItem);
        }
        getBackgroundRefresher().refreshBackground();

    }

    /**
     * DOC amaumont Comment method "isLoopTable".
     * 
     * @param table
     * @return
     */
    public boolean isLoopTable(Table table) {
        return loopTableEditorView.getTableViewerCreator().getTable() == table;
    }

    /**
     * DOC amaumont Comment method "createLoopLinks".
     * 
     * @param pathQuery
     * @param tableItem
     */
    private void createLoopLinks(String xPathExpression, TableItem tableItemTarget) {

        if (xPathExpression == null || xPathExpression.trim().length() == 0) {
            loopXpathNodes.clear();
            uniqueLoopNodes.clear();
            allLoopNodes.clear();
            return;
        }

        Set<String> alreadyProcessedXPath = new HashSet<String>();
        List<Node> nodeList = null;

        try {
            nodeList = this.nodeRetriever.retrieveListOfNodes(xPathExpression);
        } catch (XPathExpressionException e) {
            ExceptionHandler.process(e);
        }
        loopXpathNodes = new ArrayList<String>();
        uniqueLoopNodes = new ArrayList<Node>();
        allLoopNodes = new ArrayList<Node>();
        if (nodeList != null) {
            for (Node node : nodeList) {
                allLoopNodes.add(node);
                String absoluteXPathFromNode = NodeRetriever.getAbsoluteXPathFromNode(node);
                if (!alreadyProcessedXPath.contains(absoluteXPathFromNode)) {
                    TreeItem treeItemFromAbsoluteXPath = treePopulator.getTreeItem(absoluteXPathFromNode);
                    if (treeItemFromAbsoluteXPath != null) {
                        loopXpathNodes.add(absoluteXPathFromNode);
                        uniqueLoopNodes.add(node);
                        addLoopLink(treeItemFromAbsoluteXPath, (Object) treeItemFromAbsoluteXPath.getData(), tableItemTarget.getParent(),
                                (XmlXPathLoopDescriptor) tableItemTarget.getData());
                        alreadyProcessedXPath.add(absoluteXPathFromNode);
                    }
                }
            }
        }
    }

    /**
     * DOC amaumont Comment method "addLinks".
     * 
     * @param relativeXpath
     * @param tableItemTarget
     * @throws XPathExpressionException
     */
    private void createFieldLinks(final String relativeXpathPrm, final TableItem tableItemTarget) {

        if (relativeXpathPrm == null || relativeXpathPrm.trim().length() == 0) {
            return;
        }

        boolean expressionIsAbsolute = false;
        if (relativeXpathPrm.trim().startsWith("/")) {
            expressionIsAbsolute = true;
        }

        String relativeXpath = relativeXpathPrm;
        boolean limitLoopExceeded = false;
        boolean limitFieldExceeded = false;

        Set<String> alreadyProcessedXPath = new HashSet<String>();
        
        if (!expressionIsAbsolute) {

            int nodesLoopMax = 50;
            int nodesFieldMax = 50;

            int lstSize = allLoopNodes.size();

            int loopNumber = lstSize;

            if (lstSize > nodesLoopMax) {
                loopNumber = nodesLoopMax;
                limitLoopExceeded = true;
            }

            // System.out.println("lstSize=" + lstSize);
            for (int i = 0; i < loopNumber; i++) {
                // System.out.println("main node index : " + i);
                Node loopNode = allLoopNodes.get(i);

                NodeList relativeNodeList = null;
                try {
                    // TimeMeasure.start("relative");
                    relativeNodeList = this.nodeRetriever.retrieveNodeListFromNode(relativeXpath, loopNode);
                    // if (false) {
                    // break;
                    // }
                } catch (XPathExpressionException e) {
                    continue;
                } finally {
                    // TimeMeasure.end("relative");
                }

                int length = relativeNodeList.getLength();

                if (length > nodesFieldMax) {
                    length = nodesFieldMax;
                    limitFieldExceeded = true;
                }

                for (int j = 0; j < length; j++) {

                    Node relativeNode = relativeNodeList.item(i);

                    if (relativeNode != null) {
                        String absoluteXPathFromNode = NodeRetriever.getAbsoluteXPathFromNode(relativeNode);
                        if (!alreadyProcessedXPath.contains(absoluteXPathFromNode)) {
                            TreeItem treeItemFromAbsoluteXPath = treePopulator.getTreeItem(absoluteXPathFromNode);
                            if (treeItemFromAbsoluteXPath != null) {
                                addFieldLink(treeItemFromAbsoluteXPath, (Object) treeItemFromAbsoluteXPath.getData(), tableItemTarget
                                        .getParent(), (SchemaTarget) tableItemTarget.getData());
                                alreadyProcessedXPath.add(absoluteXPathFromNode);
                            }
                        }
                    }
                }

                if (limitFieldExceeded) {
                    break;
                }

            }
        }

        if (limitLoopExceeded || limitFieldExceeded || expressionIsAbsolute) {

            String expression = null;

            if (relativeXpath == null) {
                relativeXpath = "";
            }

            String currentLoopXPath = getCurrentLoopXPath();

            if (relativeXpath.trim().startsWith("/")) {
                expression = relativeXpath;
            } else {
                expression = currentLoopXPath + "/" + relativeXpath;
            }

            NodeList nodeList = null;
            try {
                nodeList = this.nodeRetriever.retrieveNodeList(expression);
            } catch (XPathExpressionException e) {
                ExceptionHandler.process(e);
            }

            if (nodeList != null) {
                int length = nodeList.getLength();
                for (int i = 0; i < length; i++) {
                    // System.out.println("main node index : " + i);
                    Node loopNode = nodeList.item(i);
                    String absoluteXPathFromNode = NodeRetriever.getAbsoluteXPathFromNode(loopNode);
                    if (!alreadyProcessedXPath.contains(absoluteXPathFromNode)) {
                        TreeItem treeItemFromAbsoluteXPath = treePopulator.getTreeItem(absoluteXPathFromNode);
                        if (treeItemFromAbsoluteXPath != null) {
                            addFieldLink(treeItemFromAbsoluteXPath, (Object) treeItemFromAbsoluteXPath.getData(), tableItemTarget
                                    .getParent(), (SchemaTarget) tableItemTarget.getData());
                            alreadyProcessedXPath.add(absoluteXPathFromNode);
                        }
                    }
                }
            }

        }

    }

    public SchemaTarget getNewSchemaTargetEntry(String relativeXPathValue) {

        XmlExtractorFieldModel xpathNodeSchemaModel = fieldsTableEditorView.getModel();
        SchemaTarget schemaTarget = xpathNodeSchemaModel.createNewSchemaTarget();
        schemaTarget.setRelativeXPathQuery(relativeXPathValue);

        return schemaTarget;
    }

    /**
     * Getter for tableEditorView.
     * 
     * @return the tableEditorView
     */
    public ExtractionFieldsWithXPathEditorView getFieldsTableEditorView() {
        return this.fieldsTableEditorView;
    }

    /**
     * Getter for loopTableEditorView.
     * 
     * @return the loopTableEditorView
     */
    public ExtractionLoopWithXPathEditorView getLoopTableEditorView() {
        return this.loopTableEditorView;
    }

    @SuppressWarnings("unchecked")
    public void updateLinksStyleAndControlsSelection(Control currentControl) {

        boolean selectedControlIsTable = false;
        if (currentControl instanceof Table) {
            selectedControlIsTable = true;
        } else if (currentControl instanceof Tree) {
            selectedControlIsTable = false;
        } else {
            throw new IllegalArgumentException("This type of currentControl is unsupported");
        }

        HashSet selectedItems = new HashSet();
        Map itemsToSelect = new HashMap();

        if (selectedControlIsTable) {
            for (Table table : getTables()) {
                if (table != currentControl) {
                    table.deselectAll();
                    if (isFieldsTable(table)) {
                        fieldsTableEditorView.getExtendedToolbar().updateEnabledStateOfButtons();
                    }
                }
            }

            TableItem[] selection = ((Table) currentControl).getSelection();
            for (int i = 0; i < selection.length; i++) {
                TableItem tableItem = selection[i];
                selectedItems.add(tableItem.getData());
            }

        } else {

            TreeItem[] selection = getTree().getSelection();
            for (int i = 0; i < selection.length; i++) {
                TreeItem treeItem = selection[i];
                selectedItems.add(treeItem.getData());
            }

        }

        List<LinkDescriptor<TreeItem, Object, Table, Object>> links = linksManager.getLinks();
        for (LinkDescriptor<TreeItem, Object, Table, Object> link : links) {
            IStyleLink styleLink = null;
            IExtremityLink extremity = null;
            IExtremityLink otherExtremity = null;
            if (selectedControlIsTable) {
                extremity = link.getExtremity2();
                otherExtremity = link.getExtremity1();
            } else {
                extremity = link.getExtremity1();
                otherExtremity = link.getExtremity2();
            }
            boolean currentItemIsSelected = selectedItems.contains(extremity.getDataItem());

            if (extremity.getGraphicalObject() == loopTableEditorView.getTableViewerCreator().getTable()
                    || otherExtremity.getGraphicalObject() == loopTableEditorView.getTableViewerCreator().getTable()) {
                styleLink = getSelectedLoopStyleLink();
            } else {

                if (currentItemIsSelected) {
                    styleLink = getSelectedStyleLink();
                    if (selectedControlIsTable) {

                        itemsToSelect.put((TreeItem) otherExtremity.getGraphicalObject(), null);

                    } else {

                        Table currentTable = (Table) otherExtremity.getGraphicalObject();
                        List<TableItem> tableItemsToSelect = (List<TableItem>) itemsToSelect.get(currentTable);

                        if (tableItemsToSelect == null) {
                            tableItemsToSelect = new ArrayList<TableItem>();
                            itemsToSelect.put(currentTable, tableItemsToSelect);
                        }
                        TableItem tableItem = TableUtils.getTableItem(currentTable, otherExtremity.getDataItem());
                        tableItemsToSelect.add(tableItem);
                    }
                } else {
                    styleLink = getUnselectedStyleLink();
                }
            }
            if (styleLink == null) {
                styleLink = getDefaultStyleLink();
            }
            link.setStyleLink(styleLink);

        }
        if (selectedControlIsTable) {
            ((Tree) getTree()).setSelection((TreeItem[]) itemsToSelect.keySet().toArray(new TreeItem[0]));
        } else {
            Set<Table> set = itemsToSelect.keySet();
            if (set.size() > 0) {
                for (Table table : set) {
                    ArrayList<TableItem> tableItemsToSelect = (ArrayList<TableItem>) itemsToSelect.get(table);
                    table.deselectAll();
                    TableItem[] tableItems = (TableItem[]) tableItemsToSelect.toArray(new TableItem[0]);
                    table.setSelection(tableItems);
                }
            } else {
                loopTableEditorView.getTable().deselectAll();
                fieldsTableEditorView.getTable().deselectAll();
            }
            fieldsTableEditorView.getExtendedToolbar().updateEnabledStateOfButtons();
        }
        getLinksManager().sortLinks(getDrawingLinksComparator());
        getBackgroundRefresher().refreshBackground();
    }

    /**
     * DOC amaumont Comment method "isFieldsTable".
     * 
     * @param table
     * @return
     */
    public boolean isFieldsTable(Table table) {
        return fieldsTableEditorView.getTableViewerCreator().getTable() == table;
    }

    public StyleLink getSelectedLoopStyleLink() {
        if (this.selectedLoopStyleLink == null) {
            StyleLink styleLink = new StyleLink();
            styleLink.setDrawableLink(new BezierHorizontalLink(styleLink));
            styleLink.setForegroundColor(getTree().getDisplay().getSystemColor(SWT.COLOR_GREEN));
            styleLink.setLineWidth(2);
            styleLink.setExtremity2(new ExtremityEastArrow(styleLink, -ExtremityEastArrow.WIDTH_ARROW - 2, 0));
            this.selectedLoopStyleLink = styleLink;
        }
        return this.selectedLoopStyleLink;
    }

    public String getCurrentLoopXPath() {
        return loopTableEditorView.getExtendedTableModel().getBeansList().get(0).getAbsoluteXPathQuery();
    }

    public void parseAllFieldsXPathExpressions() {

        List<SchemaTarget> beansList = fieldsTableEditorView.getExtendedTableModel().getBeansList();

        int lstSize = beansList.size();
        for (int i = 0; i < lstSize; i++) {
            SchemaTarget schemaTarget = beansList.get(i);
            onXPathValueChanged(fieldsTableEditorView.getTable(), schemaTarget.getRelativeXPathQuery(), i);
        }

    }

    /**
     * Getter for loopXpathNodes.
     * 
     * @return the loopXpathNodes
     */
    public ArrayList<String> getLoopXpathNodes() {
        return this.loopXpathNodes;
    }

    /**
     * DOC amaumont Comment method "validateXPathExpression".
     * 
     * @param newValue
     * @return null if expression is valid, else return the error message.
     */
    public String validateXPathExpression(String xpathExpression) {
        if (xpathExpression == null || xpathExpression.trim().length() == 0) {
            return null;
        }
        XPathFactory xpf = XPathFactory.newInstance();
        XPath xpath = xpf.newXPath();
        try {
            xpath.compile(xpathExpression);
        } catch (Exception e) {
            return "The current XPath expression is invalid";
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.swt.linking.TreeToTablesLinker#getDrawingLinksComparator()
     */
    @Override
    protected Comparator<LinkDescriptor<TreeItem, Object, Table, Object>> getDrawingLinksComparator() {
        if (this.drawingLinksComparator == null) {
            this.drawingLinksComparator = new Comparator<LinkDescriptor<TreeItem, Object, Table, Object>>() {

                public int compare(LinkDescriptor<TreeItem, Object, Table, Object> link1,
                        LinkDescriptor<TreeItem, Object, Table, Object> link2) {
                    IStyleLink link1StyleLink = link1.getStyleLink();
                    IStyleLink link2StyleLink = link2.getStyleLink();
                    if (link1StyleLink == link2StyleLink) {
                        return 0;
                    }
                    if (link1StyleLink == getSelectedLoopStyleLink()) {
                        return 1;
                    }
                    if (link2StyleLink == getSelectedLoopStyleLink()) {
                        return -1;
                    }
                    if (link1StyleLink == getUnselectedStyleLink()) {
                        return -1;
                    }
                    return 1;
                }

            };
        }
        return this.drawingLinksComparator;
    }

    /**
     * Getter for allLoopNodes.
     * 
     * @return the allLoopNodes
     */
    public ArrayList<Node> getAllLoopNodes() {
        return this.allLoopNodes;
    }

    /**
     * Getter for uniqueLoopNodes.
     * 
     * @return the uniqueLoopNodes
     */
    public ArrayList<Node> getUniqueLoopNodes() {
        return this.uniqueLoopNodes;
    }

    /**
     * Getter for nodeRetriever.
     * 
     * @return the nodeRetriever
     */
    public NodeRetriever getNodeRetriever() {
        return this.nodeRetriever;
    }

}
