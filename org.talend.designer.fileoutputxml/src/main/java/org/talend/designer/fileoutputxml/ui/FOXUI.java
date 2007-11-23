// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
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
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellEditorListener;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
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
import org.eclipse.swt.widgets.Text;
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
import org.talend.designer.fileoutputxml.action.CreateNameSpaceAction;
import org.talend.designer.fileoutputxml.action.DeleteNodeAction;
import org.talend.designer.fileoutputxml.action.DisconnectAction;
import org.talend.designer.fileoutputxml.action.ImportTreeFromXMLAction;
import org.talend.designer.fileoutputxml.action.RemoveGroupAction;
import org.talend.designer.fileoutputxml.action.SetForLoopAction;
import org.talend.designer.fileoutputxml.action.SetGroupAction;
import org.talend.designer.fileoutputxml.data.FOXTreeNode;
import org.talend.designer.fileoutputxml.i18n.Messages;
import org.talend.designer.fileoutputxml.managers.FOXManager;
import org.talend.designer.fileoutputxml.ui.edit.FOXTargetTreeViewerProvider;
import org.talend.designer.fileoutputxml.ui.edit.Schema2XMLLinker;
import org.talend.designer.fileoutputxml.ui.edit.SchemaTableViewerProvider;
import org.talend.designer.fileoutputxml.ui.footer.FooterComposite;
import org.talend.designer.fileoutputxml.util.StringUtil;

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

    private IAction createNamespaceAction;

    private IAction importFromXMLAction;

    // private IAction guessLoopAction;

    private IAction setLoopAction;

    private IAction setGroupAction;

    private IAction removeGroupAction;

    private String selectedText;

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
                    linker.addLoopLink(tableItems[i], tableItems[i].getData(), xmlViewer.getTree(), (FOXTreeNode) node.getData());
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
        IMetadataTable metadataTable = this.externalNode.getMetadataTable();
        if (metadataTable != null) {
            List<IMetadataColumn> columnList = metadataTable.getListColumns();
            schemaViewer.setInput(columnList);
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
        Group group = Form.createGroup(mainComposite, 1, Messages.getString("FOXUI.0"), height); //$NON-NLS-1$
        // group.setBackgroundMode(SWT.INHERIT_FORCE);

        xmlViewer = new TreeViewer(group, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION);
        GridData gridData = new GridData(GridData.FILL_BOTH);
        xmlViewer.getControl().setLayoutData(gridData);
        xmlViewer.setUseHashlookup(true);
        Tree tree = xmlViewer.getTree();
        tree.setLinesVisible(true);
        tree.setBackground(tree.getDisplay().getSystemColor(SWT.COLOR_WHITE));
        TreeColumn column1 = new TreeColumn(tree, SWT.LEFT);
        column1.setText(Messages.getString("FOXUI.1")); //$NON-NLS-1$
        column1.setWidth(250);

        TreeColumn column2 = new TreeColumn(tree, SWT.CENTER);
        column2.setText(Messages.getString("FOXUI.2")); //$NON-NLS-1$
        column2.setWidth(110);

        TreeColumn column3 = new TreeColumn(tree, SWT.CENTER);
        column3.setText(Messages.getString("FOXUI.3")); //$NON-NLS-1$
        column3.setWidth(90);

        tree.setHeaderVisible(true);
        // tree.setBackgroundMode(SWT.INHERIT_NONE);
        FOXTargetTreeViewerProvider provider = new FOXTargetTreeViewerProvider();
        xmlViewer.setLabelProvider(provider);

        xmlViewer.setCellModifier(new ICellModifier() {

            public boolean canModify(Object element, String property) {
                if (property.equals("C1")) { //$NON-NLS-1$
                    FOXTreeNode node = (FOXTreeNode) element;
                    if (node.getLabel() != null && node.getLabel().length() > 0) {
                        return true;
                    }
                }
                return false;
            }

            public Object getValue(Object element, String property) {
                FOXTreeNode node = (FOXTreeNode) element;
                if (property.equals("C1")) { //$NON-NLS-1$
                    return node.getLabel();
                }
                return null;
            }

            public void modify(Object element, String property, Object value) {
                TreeItem treeItem = (TreeItem) element;
                FOXTreeNode node = (FOXTreeNode) treeItem.getData();
                if (property.equals("C1")) { //$NON-NLS-1$
                    node.setLabel((String) value);
                }
                xmlViewer.refresh(node);
            }
        });
        xmlViewer.setColumnProperties(new String[] { "C1", "C2", "C3" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        CellEditor editor = new TextCellEditor(xmlViewer.getTree());

        editor.addListener(new DialogErrorXMLLabelCellEditor(editor));

        xmlViewer.setCellEditors(new CellEditor[] { editor, null, null });

        xmlViewer.setContentProvider(provider);
        xmlViewer.setInput(this.foxManager.getTreeData());
        xmlViewer.expandAll();
        createAction();
        MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
        menuMgr.setRemoveAllWhenShown(true);
        menuMgr.addMenuListener(new IMenuListener() {

            public void menuAboutToShow(IMenuManager manager) {
                FOXUI.this.fillContextMenu(manager);
            }
        });
        Menu menu = menuMgr.createContextMenu(xmlViewer.getControl());
        xmlViewer.getControl().setMenu(menu);
        xmlViewer.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                // TODO Auto-generated method stub

            }

        });
    }

    /**
     * Comment method "fillContextMenu".
     * 
     * @param manager
     */
    protected void fillContextMenu(IMenuManager manager) {
        manager.add(createAction);
        manager.add(createAttributeAction);
        manager.add(createNamespaceAction);
        // manager.add(editAction);
        manager.add(new Separator());
        manager.add(deleteAction);
        manager.add(disconnectAction);
        manager.add(new Separator());
        // manager.add(guessLoopAction);
        manager.add(setLoopAction);
        manager.add(new Separator());
        manager.add(setGroupAction);
        manager.add(removeGroupAction);
        manager.add(new Separator());
        manager.add(importFromXMLAction);
    }

    private void createAction() {
        createAction = new CreateElementAction(xmlViewer, this, Messages.getString("FOXUI.10")); //$NON-NLS-1$
        createAttributeAction = new CreateAttributeAction(xmlViewer, this, Messages.getString("FOXUI.11")); //$NON-NLS-1$
        createNamespaceAction = new CreateNameSpaceAction(xmlViewer, this, Messages.getString("FOXUI.22"));
        // editAction = new EditLabelAction(xmlViewer, "Edit Label");
        deleteAction = new DeleteNodeAction(xmlViewer, this, Messages.getString("FOXUI.12")); //$NON-NLS-1$
        disconnectAction = new DisconnectAction(xmlViewer, this, Messages.getString("FOXUI.13")); //$NON-NLS-1$
        // disconnectAction.setToolTipText("Disconnect the linker of the current tree node.");
        importFromXMLAction = new ImportTreeFromXMLAction(xmlViewer, this, Messages.getString("FOXUI.14")); //$NON-NLS-1$
        // importFromXMLAction
        // .setToolTipText("Discard the current tree and then import a hierachy tree from an existing xml file.");
        // guessLoopAction = new GuessLoopAction(xmlViewer, Messages.getString("FOXUI.15")); //$NON-NLS-1$
        setLoopAction = new SetForLoopAction(xmlViewer, Messages.getString("FOXUI.16")); //$NON-NLS-1$
        setGroupAction = new SetGroupAction(xmlViewer, Messages.getString("FOXUI.17")); //$NON-NLS-1$
        removeGroupAction = new RemoveGroupAction(xmlViewer, Messages.getString("FOXUI.18")); //$NON-NLS-1$

    }

    private void addSchemaViewer(final Composite mainComposite, final int width, final int height) {
        // Group Schema Viewer
        final Group group = Form.createGroup(mainComposite, 1, Messages.getString("FOXUI.19"), height); //$NON-NLS-1$
        // group.setBackgroundMode(SWT.INHERIT_FORCE);
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

        // schemaViewer.set
        // schemaViewer.getTable().setBackground(schemaViewer.getTable().getDisplay().getSystemColor(SWT.COLOR_WHITE));

        SchemaTableViewerProvider provider = new SchemaTableViewerProvider();
        schemaViewer.setContentProvider(provider);
        schemaViewer.setLabelProvider(provider);

        GridData data2 = new GridData(GridData.FILL_BOTH);
        Table table = schemaViewer.getTable();
        // table.setLinesVisible(true);
        table.setHeaderVisible(true);
        TableColumn column1 = new TableColumn(table, SWT.CENTER);
        column1.setText(Messages.getString("FOXUI.20")); //$NON-NLS-1$
        column1.setWidth(100);
        table.setLayoutData(data2);

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

    public Composite getFoxUIParent() {
        return this.foxUIParent;
    }

    public FOXManager getFoxManager() {
        return foxManager;
    }

    /**
     * DOC gke FOXUI class global comment. Detailled comment <br/>
     * 
     */
    class DialogErrorXMLLabelCellEditor implements ICellEditorListener {

        CellEditor editor;

        public void applyEditorValue() {
            String text = getControl().getText();
            onValueChanged(text, true);
        }

        public void cancelEditor() {
        }

        public void editorValueChanged(boolean oldValidState, boolean newValidState) {
            onValueChanged(getControl().getText(), false);
        }

        private void onValueChanged(final String newValue, boolean showAlertIfError) {
            final Text text = getControl();

            String errorMessage = null;

            if (!StringUtil.validateLabelForXML(text.getText())) {
                errorMessage = Messages.getString("FOXUI.21"); //$NON-NLS-1$
            }

            if (errorMessage == null) {
                text.setBackground(text.getDisplay().getSystemColor(SWT.COLOR_WHITE));
            } else {
                text.setBackground(text.getDisplay().getSystemColor(SWT.COLOR_RED));
                if (showAlertIfError) {
                    text.setText(selectedText);
                    MessageDialog.openError(text.getShell(), "Invalid XML label.", errorMessage); //$NON-NLS-1$
                }
            }
        }

        public DialogErrorXMLLabelCellEditor(CellEditor editor) {
            super();
            this.editor = editor;
        }

        private Text getControl() {
            return (Text) editor.getControl();
        }

    }

    public void setSelectedText(String label) {
        selectedText = label;
    }
}
