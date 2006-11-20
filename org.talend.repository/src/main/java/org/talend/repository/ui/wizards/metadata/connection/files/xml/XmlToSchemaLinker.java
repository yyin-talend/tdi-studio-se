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

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.talend.commons.ui.swt.drawing.link.BezierHorizontalLink;
import org.talend.commons.ui.swt.drawing.link.IExtremityLink;
import org.talend.commons.ui.swt.drawing.link.LinkDescriptor;
import org.talend.commons.ui.swt.drawing.link.StyleLink;
import org.talend.commons.ui.swt.drawing.link.TreeItemExtremityDescriptor;
import org.talend.commons.ui.swt.linking.TreeToTableLinker;
import org.talend.commons.ui.swt.proposal.TextCellEditorWithProposal;
import org.talend.commons.ui.swt.proposal.xpath.XPathProposalProvider;
import org.talend.commons.ui.swt.tableviewer.IModifiedBeanListener;
import org.talend.commons.ui.swt.tableviewer.ModifiedBeanEvent;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator;
import org.talend.commons.ui.utils.TableUtils;
import org.talend.commons.utils.data.list.IListenableListListener;
import org.talend.commons.utils.data.list.ListenableListEvent;
import org.talend.commons.utils.time.TimeMeasure;
import org.talend.commons.xml.NodeRetriever;
import org.talend.core.model.metadata.builder.connection.SchemaTarget;
import org.talend.core.model.targetschema.editor.XPathNodeSchemaModel;
import org.talend.repository.ui.wizards.metadata.connection.files.xml.dnd.XmlToSchemaDragAndDropHandler;
import org.w3c.dom.Node;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class XmlToSchemaLinker extends TreeToTableLinker<Object, SchemaTarget> {

    private StyleLink styleLink;

    private TreePopulator treePopulator;

    private XPathNodeSchemaEditorView tableEditorView;

    private NodeRetriever nodeRetriever;

    /**
     * DOC amaumont XmlToMetadataTableLinker constructor comment.
     * 
     * @param commonParent common main parent of tree and table, it and its children should have backgoundMode
     * configured with SWT.INHERIT_FORCE, same configuration for parents of tree and table.
     * @param tree
     * @param table
     */
    public XmlToSchemaLinker(Composite commonParent, Tree tree, XPathNodeSchemaEditorView tableEditorView, TreePopulator treePopulator) {
        super(commonParent, tree, tableEditorView.getTableViewerCreator().getTable());
        this.treePopulator = treePopulator;
        this.tableEditorView = tableEditorView;
        this.nodeRetriever = new NodeRetriever(treePopulator.getFilePath());
        XPathProposalProvider proposalProvider = new XPathProposalProvider(this.nodeRetriever);
        TextCellEditorWithProposal xPathCellEditor = tableEditorView.getXPathCellEditor();
        xPathCellEditor.setContentProposalProvider(proposalProvider);
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
        this.styleLink = new StyleLink();
        this.styleLink.setDrawableLink(new BezierHorizontalLink(styleLink));
        this.styleLink.setForegroundColor(tree.getDisplay().getSystemColor(SWT.COLOR_GRAY));
        this.styleLink.setLineWidth(2);
        initListeners();
        createLinks();

    }

    /**
     * DOC amaumont Comment method "createLinks".
     */
    private void createLinks() {
        TableItem[] tableItems = table.getItems();
        List<SchemaTarget> schemaTargetList = tableEditorView.getXpathNodeSchemaModel().getBeansList();
        for (int i = 0; i < tableItems.length; i++) {
            TableItem tableItem = tableItems[i];
            SchemaTarget schemaTarget = schemaTargetList.get(i);
            String xPathQuery = schemaTarget.getXPathQuery();
            createLinks(xPathQuery, tableItem);
        }
        updateBackground();
    }

    /**
     * DOC amaumont Comment method "initListeners".
     */
    private void initListeners() {

        XPathNodeSchemaModel schemaModel = this.tableEditorView.getXpathNodeSchemaModel();

        schemaModel.addModifiedBeanListenerListener(new IModifiedBeanListener<SchemaTarget>() {

            public void handleEvent(ModifiedBeanEvent<SchemaTarget> event) {
                handleModifiedBeanEvent(event);
            }

            private void handleModifiedBeanEvent(ModifiedBeanEvent<SchemaTarget> event) {
                if (event.column == tableEditorView.getXPathColumn()) {
                    onXPathValueChanged((String) event.previousValue, (String) event.newValue, event.index);
                }

            }

        });

        schemaModel.addBeforeOperationListListener(-50, new IListenableListListener() {

            public void handleEvent(ListenableListEvent event) {
                handleListenableListBeforeTableViewerRefreshedEvent(event);
            }

        });

        schemaModel.addAfterOperationListListener(new IListenableListListener() {

            public void handleEvent(ListenableListEvent event) {
                handleListenableListAfterTableViewerRefreshedEvent(event);
            }

        });

        new XmlToSchemaDragAndDropHandler(this);
    }

    /**
     * DOC amaumont Comment method "handleListenableListEvent".
     * 
     * @param event
     */
    private void handleListenableListBeforeTableViewerRefreshedEvent(ListenableListEvent<SchemaTarget> event) {
        TableViewerCreator<SchemaTarget> tableViewerCreator = tableEditorView.getTableViewerCreator();
        if (event.type == ListenableListEvent.TYPE.REMOVED) {
            Collection<SchemaTarget> removedObjects = event.removedObjects;
            for (SchemaTarget target : removedObjects) {
                TableItem tableItem = TableUtils.getTableItem(tableViewerCreator.getTable(), target);
                linksManager.removeLink(tableItem);
            }
            updateBackground();
        }
        // tableViewerCreator.getTableViewer().refresh(); // force refresh
    }

    public void handleListenableListAfterTableViewerRefreshedEvent(ListenableListEvent<SchemaTarget> event) {
        TableViewerCreator<SchemaTarget> tableViewerCreator = tableEditorView.getTableViewerCreator();
        if (event.type == ListenableListEvent.TYPE.ADDED) {
            Table table = tableViewerCreator.getTable();
            TableItem[] tableItems = table.getItems();
            for (int i = 0; i < tableItems.length; i++) {
                TableItem tableItem = tableItems[i];
                SchemaTarget schemaTarget = (SchemaTarget) tableItem.getData();
                boolean changeApplied = linksManager.setNewGraphicalItemToExtremity2(schemaTarget, tableItem);
                if (!changeApplied) {
                    createLinks(schemaTarget.getXPathQuery(), tableItem);
                }
            }
            updateBackground();
        } else if (event.type == ListenableListEvent.TYPE.SWAPED) {
            Table table = tableViewerCreator.getTable();

            TableItem[] tableItems = table.getItems();
            for (int i = 0; i < tableItems.length; i++) {
                TableItem tableItem = tableItems[i];
                SchemaTarget schemaTarget = (SchemaTarget) tableItem.getData();
                linksManager.setNewGraphicalItemToExtremity2(schemaTarget, tableItem);
            }
            updateBackground();
        }
    }

    /**
     * DOC amaumont Comment method "addLink".
     * 
     * @param treeItem
     * @param tableItem
     */
    public void addLink(TreeItem treeItem, TableItem tableItem) {
        LinkDescriptor<TreeItem, Object, TableItem, SchemaTarget> link = new LinkDescriptor<TreeItem, Object, TableItem, SchemaTarget>(
                new TreeItemExtremityDescriptor(treeItem), new TableItemExtremityDescriptor(tableItem, (SchemaTarget) tableItem.getData()));

        link.setDrawableLink(this.styleLink.getDrawableLink());
        linksManager.addLink(link);
    }

    /**
     * DOC amaumont Comment method "removeAllLinks".
     */
    public void removeAllLinks() {
        linksManager.clearLinks();
    }

    /**
     * DOC amaumont Comment method "onXPathValueChanged".
     * 
     * @param previousValue
     * @param newValue
     * @param itemIndex
     */
    public void onXPathValueChanged(String previousValue, String newValue, int itemIndex) {
        TableItem tableItem = table.getItem(itemIndex);
        linksManager.removeLink(tableItem);
        createLinks(newValue, tableItem);
        updateBackground();

    }

    /**
     * DOC amaumont Comment method "addLinks".
     * 
     * @param xPathExpression
     * @param tableItemTarget
     */
    private void createLinks(String xPathExpression, TableItem tableItemTarget) {
        Set<String> alreadyProcessedXPath = new HashSet<String>();
//        TimeMeasure.start(xPathExpression);
        List<Node> nodeList = this.nodeRetriever.retrieveListOfNodes(xPathExpression);
//        TimeMeasure.end(xPathExpression);
        // System.out.println("nodeList.size()="+nodeList.size());

        if (nodeList != null) {
            for (Node node : nodeList) {
                String absoluteXPathFromNode = NodeRetriever.getAbsoluteXPathFromNode(node);
                if (!alreadyProcessedXPath.contains(absoluteXPathFromNode)) {
                    TreeItem treeItemFromAbsoluteXPath = treePopulator.getTreeItem(absoluteXPathFromNode);
                    if (treeItemFromAbsoluteXPath != null) {
                        addLink(treeItemFromAbsoluteXPath, tableItemTarget);
                        alreadyProcessedXPath.add(absoluteXPathFromNode);
                    }
                }
            }
        }
    }

    public SchemaTarget getNewTargetEntry(String absoluteXPathValue) {

        XPathNodeSchemaModel xpathNodeSchemaModel = tableEditorView.getXpathNodeSchemaModel();
        SchemaTarget schemaTarget = xpathNodeSchemaModel.createNewSchemaTarget();
        schemaTarget.setXPathQuery(absoluteXPathValue);

        return schemaTarget;
    }

    /**
     * Getter for tableEditorView.
     * 
     * @return the tableEditorView
     */
    public XPathNodeSchemaEditorView getTableEditorView() {
        return this.tableEditorView;
    }

}
