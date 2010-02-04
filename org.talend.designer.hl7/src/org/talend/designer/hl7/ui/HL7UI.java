// ============================================================================
//
// Copyright (C) 2006-2009 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.hl7.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.ui.swt.formtools.Form;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.builder.ConvertionHelper;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.editor.MetadataEmfTableEditor;
import org.talend.designer.hl7.HL7InputComponent;
import org.talend.designer.hl7.edit.HL7Tree2SchemaLinker;
import org.talend.designer.hl7.managers.HL7Manager;
import org.talend.designer.hl7.model.IModel;
import org.talend.designer.hl7.model.PrimitiveModel;
import org.talend.designer.hl7.ui.footer.FooterComposite;
import org.talend.designer.hl7.ui.header.HeaderComposite;
import org.talend.designer.hl7.ui.provider.HL7MessageTreeContentProvider;
import org.talend.designer.hl7.ui.provider.HL7MessageTreeLabelProvider;
import org.talend.designer.hl7.ui.view.HL7MetadataEmfTableEditorView;

import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.GenericParser;

/**
 * DOC bqian class global comment. Detailled comment <br/>
 * 
 * $Id: FOXUI.java,v 1.1 2007/06/12 07:20:38 gke Exp $
 * 
 */
public class HL7UI {

    protected HL7Manager hl7Manager;

    private Composite hl7UIParent;

    protected HL7InputComponent externalNode;

    private SashForm MsgToSchemaSash;

    protected TreeViewer messageViewer;

    protected HL7Tree2SchemaLinker linker;

    private String messageContent;

    private Group schemaTargetGroup;

    // private HL7SchemaEditorView hl7SchemaEditorView;
    protected HL7MetadataEmfTableEditorView hl7SchemaEditorView;

    protected MetadataEmfTableEditor metadataEditor;

    protected HL7MessageTreeContentProvider contentProvider;

    protected HL7MessageTreeLabelProvider labelProvider;

    protected String filePath;

    protected HeaderComposite header;

    public HL7UI(Composite parent, HL7Manager hl7Manager) {
        this.hl7Manager = hl7Manager;
        this.hl7Manager.getUiManager().setHl7UI(this);
        this.messageContent = hl7Manager.getMessageContent();
        this.filePath = hl7Manager.getFilePath();
        externalNode = hl7Manager.getHl7Component();
        this.hl7UIParent = parent;
        hl7UIParent.setLayout(new GridLayout());
    }

    public void init() {
        createContent(hl7UIParent);
    }

    /**
     * Comment method "createContent".
     * 
     * @param child
     */
    private void createContent(Composite mainComposite) {
        header = new HeaderComposite(mainComposite, SWT.NONE, this.filePath, hl7Manager);

        MsgToSchemaSash = new SashForm(mainComposite, SWT.HORIZONTAL | SWT.SMOOTH);
        MsgToSchemaSash.setLayoutData(new GridData(GridData.FILL_BOTH));
        MsgToSchemaSash.setBackgroundMode(SWT.INHERIT_FORCE);

        addMessageViewer(MsgToSchemaSash, 300, 110);
        addSchemaViewer(MsgToSchemaSash, 400, 110);

        MsgToSchemaSash.setWeights(new int[] { 40, 60 });

        linker = new HL7Tree2SchemaLinker(this.MsgToSchemaSash);
        linker.setMainui(this);
        hl7SchemaEditorView.setLinker(linker);

        GridData data2 = new GridData(GridData.FILL_HORIZONTAL);
        data2.heightHint = 300;
        hl7SchemaEditorView.initGraphicComponents();
        final Composite tableEditorComposite = hl7SchemaEditorView.getMainComposite();
        tableEditorComposite.setLayoutData(data2);
        tableEditorComposite.setBackground(null);
        metadataEditor = new MetadataEmfTableEditor(""); //$NON-NLS-1$
        metadataEditor.setDefaultLabel("newColumn"); //$NON-NLS-1$
        metadataEditor.registerDataList(new ArrayList());
        hl7SchemaEditorView.setMetadataEditor(metadataEditor);
        linker.init(messageViewer, hl7SchemaEditorView);
        linker.setManager(hl7Manager);
        initMessageTree();
        new FooterComposite(mainComposite, SWT.NONE, hl7Manager);
        initSchemaCombo();
        initlinkers();
    }

    /**
     * 
     * wzhang Comment method "createCombo".
     * 
     * @param mainComposite
     */
    protected void createCombo(Composite mainComposite) {

    }

    @SuppressWarnings("unchecked")
    protected void initlinkers() {
        // 1.set tableviewer by component
        List<IMetadataTable> tables = externalNode.getMetadataList();
        for (IMetadataTable table : tables) {
            MetadataTable loaded = ConvertionHelper.convert(table);
            List<MetadataColumn> columns = loaded.getColumns();
            if (hl7Manager.getSchemaRelationMap().get(table.getLabel()) != null) {
                hl7Manager.getSchemaRelationMap().remove(table.getLabel());
                hl7Manager.getSchemaRelationMap().put(table.getLabel(), columns);
            } else {
                hl7Manager.getSchemaRelationMap().put(table.getLabel(), columns);
            }
            if (this.getMetaTableViewer().getSelection() instanceof IStructuredSelection) {
                IStructuredSelection selection = (IStructuredSelection) this.getMetaTableViewer().getSelection();
                if (selection.getFirstElement() != null) {
                    String name = ((IModel) selection.getFirstElement()).getDisplayName();
                    if (name.equals(table.getLabel())) {
                        hl7SchemaEditorView.getMetadataEditor().addAll(columns);
                    }
                }
            }
        }
        redrawLinkers();
        linker.getBackgroundRefresher().refreshBackground();
        // 2.initlinks initLinkersByPrimitiveModels(this.labelProvider.getAllPrimitives());
    }

    public void initMessageTree() {
        Message message = getHL7MessageInput();
        if (message != null) {
            messageViewer.setInput(new Message[] { message });
        } else {
            messageViewer.setInput(null);
        }
    }

    public void initSchemaCombo() {

    }

    private void addSchemaViewer(final Composite mainComposite, final int width, final int height) {

        // Group Schema Viewer
        schemaTargetGroup = Form.createGroup(mainComposite, 1, "Schema View", height); //$NON-NLS-1$
        schemaTargetGroup.setBackgroundMode(SWT.INHERIT_FORCE);
        createCombo(schemaTargetGroup);
        hl7SchemaEditorView = new HL7MetadataEmfTableEditorView(schemaTargetGroup, SWT.BORDER, false);
        hl7SchemaEditorView.setShowDbTypeColumn(true, true, false);
        hl7SchemaEditorView.setShowDbColumnName(true, false);

    }

    // just judge if select the advance model

    private void addMessageViewer(final Composite mainComposite, final int width, final int height) {
        // Group Schema Viewer
        final Group group = Form.createGroup(mainComposite, 1, "Message View", height); //$NON-NLS-1$
        messageViewer = new TreeViewer(group);
        GridData data2 = new GridData(GridData.FILL_BOTH);
        Tree tree = messageViewer.getTree();
        // TreeItem ti = new TreeItem(tree, SWT.NONE);
        // ti.setText("Segment");
        tree.setLayoutData(data2);
        messageViewer.setAutoExpandLevel(AbstractTreeViewer.ALL_LEVELS);
        contentProvider = new HL7MessageTreeContentProvider();
        labelProvider = new HL7MessageTreeLabelProvider();
        messageViewer.setContentProvider(contentProvider);
        messageViewer.setLabelProvider(labelProvider);
    }

    private Message getHL7MessageInput() {
        GenericParser p = new GenericParser();
        Message message = null;
        try {
            if (messageContent != null) {
                message = p.parse(messageContent);
            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
            MessageBox errorBox = new MessageBox(new Shell(), SWT.APPLICATION_MODAL | SWT.OK);
            errorBox.setText("Parse error"); //$NON-NLS-1$
            errorBox.setMessage("The content can't be parsed correctly,please check the file"); //$NON-NLS-1$
            if (errorBox.open() == SWT.OK) {
                errorBox.getParent().getShell().close();
            }
        }
        return message;
    }

    public void redrawLinkers() {
        linker.removeAllLinks();
        List<PrimitiveModel> pms = this.labelProvider.getAllPrimitives();
        initLinkersByPrimitiveModels(pms);
    }

    protected void initLinkersByPrimitiveModels(List<PrimitiveModel> pms) {
        TableItem[] tableItems = this.linker.getTarget().getItems();
        TreeItem[] treeItems = this.linker.getSource().getItems();
        TreeItem sourceItem = null;
        String sourceItemName = "";
        Object targetItemData = null;
        List<linkMapPropertis> allLinkProp = new ArrayList<linkMapPropertis>();
        for (PrimitiveModel pm : pms) {
            for (TableItem ti : tableItems) {
                Object data = ti.getData();
                MetadataColumn column = (MetadataColumn) data;
                if (column.getOriginalField().equals(pm.getDisplayName())) {
                    sourceItemName = pm.getDisplayName();
                    sourceItem = findSourceItem(sourceItemName, treeItems);
                    targetItemData = ti.getData();
                    linkMapPropertis linkProp = new linkMapPropertis(sourceItem, targetItemData);
                    allLinkProp.add(linkProp);
                }
            }
        }
        for (linkMapPropertis linkProp : allLinkProp) {
            linker.addLinks(linkProp.getSource(), linkProp.getSource().getData(), this.linker.getTarget(), linkProp
                    .getTargetData());
        }
    }

    private TreeItem findSourceItem(String sourceItemName, TreeItem[] items) {
        TreeItem item2return = null;
        for (TreeItem item : items) {
            if (item.getData() != null && (item.getData() instanceof PrimitiveModel)
                    && ((PrimitiveModel) item.getData()).getDisplayName().equals(sourceItemName)) {
                item2return = item;
            } else {
                item2return = findSourceItem(sourceItemName, item.getItems());
            }
            if (item2return == null) {
                continue;
            } else {
                break;
            }
        }
        return item2return;
    }

    class linkMapPropertis {

        private TreeItem source;

        private Object targetData;

        linkMapPropertis(TreeItem source, Object targetData) {
            this.source = source;
            this.targetData = targetData;
        }

        public TreeItem getSource() {
            return this.source;
        }

        public void setSource(TreeItem source) {
            this.source = source;
        }

        public Object getTargetData() {
            return this.targetData;
        }

        public void setTargetData(Object targetData) {
            this.targetData = targetData;
        }

    }

    public Composite getHl7UIParent() {
        return this.hl7UIParent;
    }

    public HL7Manager gethl7Manager() {
        return hl7Manager;
    }

    public HL7MessageTreeContentProvider getContentProvider() {
        return this.contentProvider;
    }

    public ComboViewer getMetaTableViewer() {
        return null;
    }

    public MetadataEmfTableEditor getMetadataEditor() {
        return this.metadataEditor;
    }

    public String getMessageContent() {
        return this.messageContent;
    }

    public TreeViewer getMessageViewer() {
        return this.messageViewer;
    }

    public HeaderComposite getHeader() {
        return this.header;
    }

}
