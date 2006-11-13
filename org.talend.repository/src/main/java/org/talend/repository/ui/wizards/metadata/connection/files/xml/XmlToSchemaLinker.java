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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.talend.commons.ui.swt.drawing.link.BezierHorizontalLink;
import org.talend.commons.ui.swt.drawing.link.LinkDescriptor;
import org.talend.commons.ui.swt.drawing.link.StyleLink;
import org.talend.commons.ui.swt.drawing.link.TableItemExtremityDescriptor;
import org.talend.commons.ui.swt.drawing.link.TreeItemExtremityDescriptor;
import org.talend.commons.ui.swt.proposal.ContentProposalAdapterExtended;
import org.talend.commons.ui.swt.proposal.TextCellEditorWithProposal;
import org.talend.commons.ui.swt.proposal.xpath.XPathProposalProvider;
import org.talend.commons.utils.data.list.ListenableList;
import org.talend.commons.utils.time.TimeMeasure;
import org.talend.commons.xml.NodeRetriever;
import org.talend.core.model.metadata.builder.connection.SchemaTarget;
import org.talend.core.model.targetschema.editor.ITargetSchemaEditorListener;
import org.talend.core.model.targetschema.editor.TargetSchemaEditor2;
import org.talend.core.model.targetschema.editor.TargetSchemaEditorEvent;
import org.talend.core.model.targetschema.editor.TargetSchemaEditorEvent.TYPE;
import org.talend.core.ui.targetschema.editor.TargetSchemaTableEditorView2;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class XmlToSchemaLinker extends TreeToTableLinker {

    private StyleLink styleLink;

    private TreePopulator treePopulator;

    private TargetSchemaTableEditorView2 tableEditorView;

    private NodeRetriever nodeRetriever;

    private ITargetSchemaEditorListener targetSchemaEditorListener;

    /**
     * DOC amaumont XmlToMetadataTableLinker constructor comment.
     * 
     * @param commonParent common main parent of tree and table, it and its children should have backgoundMode
     * configured with SWT.INHERIT_FORCE, same configuration for parents of tree and table.
     * @param tree
     * @param table
     */
    public XmlToSchemaLinker(Composite commonParent, Tree tree, TargetSchemaTableEditorView2 tableEditorView,
            TreePopulator treePopulator) {
        super(commonParent, tree, tableEditorView.getTableViewerCreator().getTable());
        this.treePopulator = treePopulator;
        this.tableEditorView = tableEditorView;
        this.nodeRetriever = new NodeRetriever(treePopulator.getFilePath());
        XPathProposalProvider proposalProvider = new XPathProposalProvider(this.nodeRetriever);
        TextCellEditorWithProposal xPathCellEditor = tableEditorView.getXPathCellEditor();
        xPathCellEditor.setContentProposalProvider(proposalProvider);
//        xPathCellEditor.getContentProposalAdapter().setFilterStyle(ContentProposalAdapterExtended.FILTER_NONE);
//        xPathCellEditor.getContentProposalAdapter().addContentProposalListener(new IContentProposalListener() {
//
//            public void proposalAccepted(IContentProposal proposal) {
//                proposal.
//            }
//            
//        });
        
        init();
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
        ListenableList<SchemaTarget> schemaTargetList = tableEditorView.getTargetSchemaEditor().getSchemaTargetList();
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

        TargetSchemaEditor2 targetSchemaEditor = this.tableEditorView.getTargetSchemaEditor();

        if (targetSchemaEditorListener == null) {
            targetSchemaEditorListener = new ITargetSchemaEditorListener() {

                public void handleEvent(TargetSchemaEditorEvent event) {
                    handleTargetEditorEvent(event);
                }

            };
            targetSchemaEditor.addListener(targetSchemaEditorListener);
        }
    }

    /**
     * DOC amaumont Comment method "addLink".
     * 
     * @param treeItem
     * @param tableItem
     */
    public void addLink(TreeItem treeItem, TableItem tableItem) {
        LinkDescriptor<TreeItem, TableItem> link = new LinkDescriptor<TreeItem, TableItem>(
                new TreeItemExtremityDescriptor(treeItem), new TableItemExtremityDescriptor(tableItem));

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
     * DOC amaumont Comment method "handleTargetEditorEvent".
     * 
     * @param event
     */
    private void handleTargetEditorEvent(TargetSchemaEditorEvent event) {
        if (event.type == TYPE.XPATH_VALUE_CHANGED) {
            onXPathValueChanged((String) event.previousValue, (String) event.newValue, event.entriesIndices[0]);
        }
    }

    /**
     * DOC amaumont Comment method "onXPathValueChanged".
     * 
     * @param previousValue
     * @param newValue
     * @param i
     */
    private void onXPathValueChanged(String previousValue, String newValue, int itemIndex) {
        TableItem tableItem = table.getItem(itemIndex);
        TableItemExtremityDescriptor itemExtremityDescriptor = new TableItemExtremityDescriptor(tableItem);
        Set<LinkDescriptor<TreeItem, TableItem>> linksFromExtremity2 = linksManager
                .getLinksFromExtremity2(itemExtremityDescriptor);
        for (LinkDescriptor<TreeItem, TableItem> link : linksFromExtremity2) {
            linksManager.removeLink(link);
        }

        createLinks(newValue, tableItem);
        updateBackground();

    }

    /**
     * DOC amaumont Comment method "addLinks".
     * @param xPathExpression
     * @param tableItemTarget
     */
    private void createLinks(String xPathExpression, TableItem tableItemTarget) {
        Set<String> alreadyProcessedXPath = new HashSet<String>();
        TimeMeasure.start(xPathExpression);
        List<Node> nodeList = this.nodeRetriever.retrieveListOfNodes(xPathExpression);
        TimeMeasure.end(xPathExpression);
//        System.out.println("nodeList.size()="+nodeList.size());
        
        if (nodeList != null) {
            for (Node node : nodeList) {
                String absoluteXPathFromNode = NodeRetriever.getAbsoluteXPathFromNode(node);
                if (!alreadyProcessedXPath.contains(absoluteXPathFromNode)) {
                    TreeItem treeItemFromAbsoluteXPath = treePopulator
                            .getTreeItemFromAbsoluteXPath(absoluteXPathFromNode);
                    if (treeItemFromAbsoluteXPath != null) {
                        addLink(treeItemFromAbsoluteXPath, tableItemTarget);
                        alreadyProcessedXPath.add(absoluteXPathFromNode);
                    }
                }
            }
        }
    }

}
