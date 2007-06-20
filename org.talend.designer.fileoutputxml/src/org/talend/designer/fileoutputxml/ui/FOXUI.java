// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2007 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License.
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
package org.talend.designer.fileoutputxml.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.talend.commons.ui.swt.formtools.Form;
import org.talend.commons.ui.ws.WindowSystem;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.designer.fileoutputxml.FileOutputXMLComponent;
import org.talend.designer.fileoutputxml.action.CreateAttributeAction;
import org.talend.designer.fileoutputxml.action.CreateElementAction;
import org.talend.designer.fileoutputxml.action.DeleteNodeAction;
import org.talend.designer.fileoutputxml.action.DisconnectAction;
import org.talend.designer.fileoutputxml.action.EditLabelAction;
import org.talend.designer.fileoutputxml.action.GuessLoopAction;
import org.talend.designer.fileoutputxml.action.ImportTreeFromXMLAction;
import org.talend.designer.fileoutputxml.action.SetForLoopAction;
import org.talend.designer.fileoutputxml.data.FOXTreeNode;
import org.talend.designer.fileoutputxml.managers.FOXManager;
import org.talend.designer.fileoutputxml.ui.edit.FOXTargetTreeViewerProvider;
import org.talend.designer.fileoutputxml.ui.edit.Schema2XMLLinker;
import org.talend.designer.fileoutputxml.ui.edit.SchemaTableViewerProvider;
import org.talend.designer.fileoutputxml.ui.footer.FooterComposite;
import org.talend.repository.i18n.Messages;

/**
 * DOC bqian class global comment. Detailled comment <br/>
 * 
 * $Id: FOXUI.java,v 1.1 2007/06/12 07:20:38 gke Exp $
 * 
 */
public class FOXUI {

    private FOXManager foxManager;

    private Composite foxUIParent;

    private FileOutputXMLComponent externalNode;

    private SashForm xmlToSchemaSash;

    private TableViewer schemaViewer;

    private TreeViewer xmlViewer;

    private Schema2XMLLinker linker;

    private IAction createAction;

    private IAction deleteAction;

    private IAction disconnectAction;

    private IAction createAttributeAction;

    private IAction editAction;

    private IAction importFromXMLAction;

    private IAction guessLoopAction;

    private IAction setLoopAction;

    public FOXUI(Composite parent, FOXManager foxManager) {
        this.foxManager = foxManager;
        this.foxManager.getUiManager().setFoxUI(this);
        externalNode = foxManager.getFoxComponent();

        // add listeners.
        this.foxUIParent = parent;
        foxUIParent.setLayout(new GridLayout());
    }

    /**
     * bqian Comment method "init".
     */
    public void init() {
        createContent(foxUIParent);
    }

    /**
     * Comment method "createContent".
     * 
     * @param child
     */
    private void createContent(Composite mainComposite) {
        // Splitter
        xmlToSchemaSash = new SashForm(mainComposite, SWT.HORIZONTAL | SWT.SMOOTH);
        xmlToSchemaSash.setLayoutData(new GridData(GridData.FILL_BOTH));
        xmlToSchemaSash.setBackgroundMode(SWT.INHERIT_FORCE);

        addSchemaViewer(xmlToSchemaSash, 300, 110);
        addXMLViewer(xmlToSchemaSash, 400, 110);

        xmlToSchemaSash.setWeights(new int[] { 40, 60 });
        linker = new Schema2XMLLinker(this.xmlToSchemaSash);
        linker.init(schemaViewer.getTable(), xmlViewer);
        initSchemaTable();
        new FooterComposite(mainComposite, SWT.NONE, foxManager);

        Tree xmlTree = xmlViewer.getTree();

        TreeItem root = xmlTree.getItem(0);

        TableItem[] tableItems = schemaViewer.getTable().getItems();

        initLinker(root, tableItems);

    }

    /**
     * DOC ke Comment method "initLinker".
     * 
     * @param node
     * @param tableItems
     */
    private void initLinker(TreeItem node, TableItem[] tableItems) {
        IMetadataColumn column = ((FOXTreeNode) node.getData()).getColumn();
        if (column != null) {
            for (int i = 0; i < tableItems.length; i++) {
                if (tableItems[i].getData().equals(column)) {
                    linker.addLoopLink(tableItems[i], tableItems[i].getData(), xmlViewer.getTree(), (FOXTreeNode) node
                            .getData());
                    break;
                }
            }
        }
        TreeItem[] children = node.getItems();
        for (int i = 0; i < children.length; i++) {
            initLinker(children[i], tableItems);
        }
    }

    /**
     * DOC ke Comment method "redrawLinkers".
     */
    public void redrawLinkers() {
        linker.removeAllLinks();
        TreeItem root = xmlViewer.getTree().getItem(0);
        TableItem[] tableItems = schemaViewer.getTable().getItems();
        initLinker(root, tableItems);
        if (linker.linkSize() == 0) {
            linker.updateLinksStyleAndControlsSelection(xmlViewer.getTree());
        }
    }

    protected void initSchemaTable() {
        List<IMetadataTable> metadataTables = this.externalNode.getMetadataList();
        if (metadataTables != null && metadataTables.size() > 0) {
            List<IMetadataColumn> columnList = metadataTables.get(0).getListColumns();
            schemaViewer.setInput(columnList == null ? new ArrayList<IMetadataColumn>() : columnList);
        } else {
            schemaViewer.setInput(new ArrayList<IMetadataColumn>());
        }
    }

    /**
     * create xml viewer.
     * 
     * @param mainComposite
     * @param form
     * @param width
     * @param height
     */
    private void addXMLViewer(final Composite mainComposite, final int width, final int height) {

        // Group Schema Viewer
        Group group = Form.createGroup(mainComposite, 1, Messages.getString("XmlFileStep1.sourceSchema"), height); //$NON-NLS-1$
        group.setBackgroundMode(SWT.INHERIT_FORCE);

        xmlViewer = new TreeViewer(group, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION);
        GridData gridData = new GridData(GridData.FILL_BOTH);
        xmlViewer.getControl().setLayoutData(gridData);
        xmlViewer.setUseHashlookup(true);

        Tree tree = xmlViewer.getTree();
        tree.setLinesVisible(true);
        // tree.setBackground(tree.getDisplay().getSystemColor(SWT.COLOR_WHITE));
        TreeColumn column1 = new TreeColumn(tree, SWT.LEFT);
        column1.setText("XML Label");
        column1.setWidth(250);

        TreeColumn column2 = new TreeColumn(tree, SWT.LEFT);
        column2.setText("Column name");
        column2.setWidth(100);

        TreeColumn column3 = new TreeColumn(tree, SWT.LEFT);
        column3.setText("Node Status");
        column3.setWidth(100);

        tree.setHeaderVisible(true);
        tree.setBackgroundMode(SWT.INHERIT_NONE);
        FOXTargetTreeViewerProvider provider = new FOXTargetTreeViewerProvider();
        xmlViewer.setLabelProvider(provider);
        xmlViewer.setContentProvider(provider);
        xmlViewer.setInput(this.foxManager.getTreeData());
        xmlViewer.expandAll();
        createAction();
        MenuManager menuMgr = new MenuManager("#PopupMenu");
        menuMgr.setRemoveAllWhenShown(true);
        menuMgr.addMenuListener(new IMenuListener() {

            public void menuAboutToShow(IMenuManager manager) {
                FOXUI.this.fillContextMenu(manager);
            }
        });
        Menu menu = menuMgr.createContextMenu(xmlViewer.getControl());
        xmlViewer.getControl().setMenu(menu);
    }

    /**
     * Comment method "fillContextMenu".
     * 
     * @param manager
     */
    protected void fillContextMenu(IMenuManager manager) {
        manager.add(createAction);
        manager.add(createAttributeAction);
        manager.add(editAction);
        manager.add(new Separator());
        manager.add(deleteAction);
        manager.add(disconnectAction);
        manager.add(new Separator());
        manager.add(guessLoopAction);
        manager.add(setLoopAction);
        manager.add(new Separator());
        manager.add(importFromXMLAction);
    }

    private void createAction() {
        createAction = new CreateElementAction(xmlViewer, this, "Add Sub-element");
        createAttributeAction = new CreateAttributeAction(xmlViewer, this, "Add Attribute");
        editAction = new EditLabelAction(xmlViewer, "Edit Label");
        deleteAction = new DeleteNodeAction(xmlViewer, this, "Delete");
        disconnectAction = new DisconnectAction(xmlViewer, this, "Disconnect Linker");
        // disconnectAction.setToolTipText("Disconnect the linker of the current tree node.");
        importFromXMLAction = new ImportTreeFromXMLAction(xmlViewer, this, "Import Tree From XML");
        // importFromXMLAction
        // .setToolTipText("Discard the current tree and then import a hierachy tree from an existing xml file.");
        guessLoopAction = new GuessLoopAction(xmlViewer, "Guess Loop Element");
        setLoopAction = new SetForLoopAction(xmlViewer, "Set As Loop Element");

    }

    private void addSchemaViewer(final Composite mainComposite, final int width, final int height) {
        // Group Schema Viewer
        final Group group = Form.createGroup(mainComposite, 1,
                Messages.getString("XmlFileStep1.groupSchemaTarget"), height); //$NON-NLS-1$
        group.setBackgroundMode(SWT.INHERIT_FORCE);
        // ///////////////////////////////////////////
        // to correct graphic bug under Linux-GTK when the wizard is opened the first time
        if (WindowSystem.isGTK()) {
            group.addListener(SWT.Paint, new Listener() {

                public void handleEvent(Event event) {
                    Point offsetPoint = event.display.map(linker.getBgDrawableComposite(), group, new Point(0, 0));
                    linker.setOffset(offsetPoint);
                    linker.drawBackground(event.gc);
                }

            });
        }
        schemaViewer = new TableViewer(group);
        
//        schemaViewer.set
        // schemaViewer.getTable().setBackground(schemaViewer.getTable().getDisplay().getSystemColor(SWT.COLOR_WHITE));
        SchemaTableViewerProvider provider = new SchemaTableViewerProvider();
        schemaViewer.setContentProvider(provider);
        schemaViewer.setLabelProvider(provider);

        GridData data2 = new GridData(GridData.FILL_BOTH);
        Table table = schemaViewer.getTable();
//        table.setLinesVisible(true);
        table.setHeaderVisible(true);
        TableColumn column1 = new TableColumn(table, SWT.LEFT);
        column1.setText("Schema");
        column1.setWidth(100);
        table.setLayoutData(data2);

        // to correct graphic bug under Linux-GTK when the wizard is opened the first time
        if (WindowSystem.isGTK()) {
            table.addListener(SWT.Paint, new Listener() {

                public void handleEvent(Event event) {
                    Point offsetPoint = event.display.map(linker.getBgDrawableComposite(), schemaViewer.getTable(),
                            new Point(0, 0));
                    linker.setOffset(offsetPoint);
                    linker.drawBackground(event.gc);
                }
            });
        }
    }

    public Composite getFoxUIParent() {
        return this.foxUIParent;
    }

    
    public FOXManager getFoxManager() {
        return foxManager;
    }
}
