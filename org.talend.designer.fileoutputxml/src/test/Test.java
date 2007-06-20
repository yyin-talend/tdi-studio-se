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
package test;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.datatools.enablement.oda.xml.util.ui.ATreeNode;
import org.eclipse.datatools.enablement.oda.xml.util.ui.SchemaPopulationUtil;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.talend.commons.ui.swt.formtools.Form;
import org.talend.commons.ui.ws.WindowSystem;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.MetadataColumn;
import org.talend.designer.fileoutputxml.action.CreateAttributeAction;
import org.talend.designer.fileoutputxml.action.CreateElementAction;
import org.talend.designer.fileoutputxml.action.DeleteNodeAction;
import org.talend.designer.fileoutputxml.action.DisconnectAction;
import org.talend.designer.fileoutputxml.data.Attribute;
import org.talend.designer.fileoutputxml.data.Element;
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
 * $Id: Test.java,v 1.1 2007/06/12 07:20:39 gke Exp $
 * 
 */
public class Test {

    private Schema2XMLLinker linker;

    private TableViewer schemaViewer;

    private TreeViewer xmlViewer;

    private SashForm xmlToSchemaSash;

    private IAction createAction;

    private IAction deleteAction;
    
    private IAction disconnectAction;

    private IAction createAttributeAction;

    public void init() {
        Display display = new Display();

        Shell shell = new Shell(display);
        shell.setLayout(new FillLayout());

        final ScrolledComposite sc = new ScrolledComposite(shell, SWT.V_SCROLL | SWT.BORDER);

        final Composite child = new Composite(sc, SWT.NONE);
        child.setLayout(new GridLayout(1, false));

        createContent(child);

        sc.setContent(child);
        sc.setExpandVertical(true);
        sc.setExpandHorizontal(true);
        sc.addControlListener(new ControlAdapter() {

            public void controlResized(ControlEvent e) {
                Rectangle r = sc.getClientArea();
                sc.setMinSize(child.computeSize(r.width, SWT.DEFAULT));
            }
        });

        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        shell.dispose();
        display.dispose();

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
        
        new FooterComposite(mainComposite, SWT.BORDER);
        
        xmlToSchemaSash.setWeights(new int[] { 40, 60 });
        this.linker = new Schema2XMLLinker(this.xmlToSchemaSash);
        this.linker.init(schemaViewer.getTable(), xmlViewer);
        initialize();

        TableItem tableItem1 = schemaViewer.getTable().getItem(0);
        TreeItem treeItem = xmlViewer.getTree().getItem(0);
        linker.addLoopLink(tableItem1, tableItem1.getData(), xmlViewer.getTree(), (FOXTreeNode) treeItem.getData());
        // List<TreeItem> treeItems = TreeUtils.collectAllItems(xmlViewer.getTree());
        //
        // for (TreeItem treeItem : treeItems) {
        // linker.addLoopLink(tableItem1, tableItem1.getData(), xmlViewer.getTree(), (FOXTreeNode) treeItem.getData());
        // }
    }

    protected void initialize() {
        IMetadataColumn data = new MetadataColumn();
        data.setLabel("label");
        List list = new ArrayList();
        list.add(data);
        schemaViewer.setInput(list);

    }

    /**
     * Comment method "fillContextMenu".
     * 
     * @param manager
     */
    protected void fillContextMenu(IMenuManager manager) {
        manager.add(createAction);
        manager.add(createAttributeAction);
        manager.add(new Separator());
        manager.add(deleteAction);
        manager.add(disconnectAction);
    }

    private void createAction() {
        createAction = new CreateElementAction(xmlViewer, "Create Element");
        createAttributeAction = new CreateAttributeAction(xmlViewer, "Create Attribute");
        deleteAction = new DeleteNodeAction(xmlViewer, "Delete");
        disconnectAction = new DisconnectAction(xmlViewer, "Disconnect");

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
        group.setBackgroundMode(SWT.INHERIT_NONE);

        xmlViewer = new TreeViewer(group, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION);
        GridData gridData = new GridData(GridData.FILL_BOTH);
        xmlViewer.getControl().setLayoutData(gridData);
        xmlViewer.setUseHashlookup(true);

        Tree tree = xmlViewer.getTree();
        TreeColumn column1 = new TreeColumn(tree, SWT.LEFT);
        column1.setText("");
        column1.setWidth(250);
        TreeColumn column2 = new TreeColumn(tree, SWT.LEFT);
        column2.setText("");
        column2.setWidth(100);
        // tree.setBackground(tree.getDisplay().getSystemColor(SWT.COLOR_WHITE));
        tree.setHeaderVisible(true);
        tree.setBackgroundMode(SWT.INHERIT_FORCE);
        FOXTargetTreeViewerProvider provider = new FOXTargetTreeViewerProvider();
        xmlViewer.setLabelProvider(provider);
        xmlViewer.setContentProvider(provider);
        xmlViewer.setInput(Test.treeNodeAdapt());
        xmlViewer.expandAll();

        createAction();
        MenuManager menuMgr = new MenuManager("#PopupMenu");
        menuMgr.setRemoveAllWhenShown(true);
        menuMgr.addMenuListener(new IMenuListener() {

            public void menuAboutToShow(IMenuManager manager) {
                Test.this.fillContextMenu(manager);
            }
        });
        Menu menu = menuMgr.createContextMenu(xmlViewer.getControl());
        xmlViewer.getControl().setMenu(menu);
    }

    private void addSchemaViewer(final Composite mainComposite, final int width, final int height) {
        // Group Schema Viewer
        final Group group = Form.createGroup(mainComposite, 1, Messages.getString("XmlFileStep1.groupSchemaTarget"), height); //$NON-NLS-1$
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
        // schemaViewer.getTable().setBackground(schemaViewer.getTable().getDisplay().getSystemColor(SWT.COLOR_WHITE));
        SchemaTableViewerProvider provider = new SchemaTableViewerProvider();
        schemaViewer.setContentProvider(provider);
        schemaViewer.setLabelProvider(provider);

        GridData data2 = new GridData(GridData.FILL_BOTH);
        Table table = schemaViewer.getTable();
        table.setHeaderVisible(true);
        TableColumn column1 = new TableColumn(table, SWT.LEFT);
        column1.setText("Schema");
        column1.setWidth(100);
        table.setLayoutData(data2);

        // ///////////////////////////////////////////
        // to correct graphic bug under Linux-GTK when the wizard is opened the first time
        if (WindowSystem.isGTK()) {
            table.addListener(SWT.Paint, new Listener() {

                public void handleEvent(Event event) {
                    Point offsetPoint = event.display.map(linker.getBgDrawableComposite(), schemaViewer.getTable(), new Point(0,
                            0));
                    linker.setOffset(offsetPoint);
                    linker.drawBackground(event.gc);
                }
            });
        }
    }

    /**
     * Comment method "main".
     * 
     * @param args
     */
    public static void main(String[] args) {
        Test t = new Test();
        t.init();
    }

    // only for test
    public static List treeNodeAdapt() {
        List list = new ArrayList();
        try {
            ATreeNode treeNode = SchemaPopulationUtil.getSchemaTree("c:/mapping_IBMDB2.xml", true, 0);
            FOXTreeNode root = cloneATreeNode(treeNode);
            list.add(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    static FOXTreeNode cloneATreeNode(ATreeNode treeNode) throws Exception {
        FOXTreeNode node = null;
        if (treeNode.getType() == ATreeNode.ATTRIBUTE_TYPE) {
            node = new Attribute();
        } else {
            node = new Element();
        }

        node.setLabel((String)treeNode.getValue());

        Object[] children = treeNode.getChildren();
        if (children != null) {
            for (int i = 0; i < children.length; i++) {
                ATreeNode child = (ATreeNode) children[i];
                FOXTreeNode foxChild = cloneATreeNode(child);
                node.addChild(foxChild);
            }
        }
        return node;
    }
}
