package org.talend.designer.xmlmap.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.ui.runtime.image.ImageUtils.ICON_SIZE;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.components.IODataComponent;
import org.talend.core.model.components.IODataComponentContainer;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.editor.MetadataTableEditor;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.IExternalNode;
import org.talend.core.model.process.INode;
import org.talend.core.ui.branding.IBrandingService;
import org.talend.core.ui.images.CoreImageProvider;
import org.talend.core.ui.metadata.editor.MetadataTableEditorView;
import org.talend.designer.core.ui.AbstractMultiPageTalendEditor;
import org.talend.designer.core.ui.editor.cmd.ExternalNodeChangeCommand;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.xmlmap.XmlMapComponent;
import org.talend.designer.xmlmap.editor.XmlMapEditor;
import org.talend.designer.xmlmap.model.emf.xmlmap.InputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.NodeType;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputTreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlMapData;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlmapFactory;
import org.talend.designer.xmlmap.ui.tabs.TabFolderEditors;
import org.talend.designer.xmlmap.util.XmlMapUtil;

public class MapperUI {

    private XmlMapComponent mapperComponent;

    private XmlMapData copyOfMapData;

    private int mapperResponse;

    private Shell mapperShell;

    private SashForm datasViewSashForm;

    private SashForm mainSashForm;

    private TabFolderEditors tabFolderEditors;

    public MapperUI(XmlMapComponent mapperComponent) {
        this.mapperComponent = mapperComponent;
    }

    public Shell createWindow(final Display display) {

        Shell activeShell = display.getActiveShell();
        int style = SWT.DIALOG_TRIM | SWT.MIN | SWT.MAX | SWT.APPLICATION_MODAL | SWT.RESIZE;
        if (activeShell == null) {
            mapperShell = new Shell(mapperShell, style);
        } else {
            mapperShell = new Shell(activeShell, style);
        }

        mapperShell.setMaximized(true);

        mapperShell.setImage(CoreImageProvider.getComponentIcon(mapperComponent.getComponent(), ICON_SIZE.ICON_32));

        IBrandingService brandingService = (IBrandingService) GlobalServiceRegister.getDefault().getService(
                IBrandingService.class);
        String productName = brandingService.getFullProductName();
        mapperShell.setText(productName
                + " - " + mapperComponent.getComponent().getName() + " - " + mapperComponent.getUniqueName()); //$NON-NLS-1$

        GridLayout parentLayout = new GridLayout(1, true);
        mapperShell.setLayout(parentLayout);
        // Composite composite = new Composite(mapperShell, SWT.NONE);
        // composite.setBackground(display.getSystemColor(SWT.COLOR_YELLOW));

        mainSashForm = new SashForm(mapperShell, SWT.SMOOTH | SWT.VERTICAL);
        GridData mainSashFormGridData = new GridData(GridData.FILL_BOTH);
        mainSashForm.setLayoutData(mainSashFormGridData);

        datasViewSashForm = new SashForm(mainSashForm, SWT.SMOOTH | SWT.HORIZONTAL | SWT.BORDER);
        XmlMapEditor editor = new XmlMapEditor();
        editor.createPartControl(datasViewSashForm);
        editor.setContent(copyOfMapData);
        // editor.setContent(getContents());

        tabFolderEditors = new TabFolderEditors(mainSashForm, SWT.BORDER);
        refreshTabFolderEditors();
        mainSashForm.setWeights(new int[] { 70, 30 });

        FooterComposite footerComposite = new FooterComposite(mapperShell, this);
        footerComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        mapperShell.open();
        return mapperShell;

    }

    private void refreshTabFolderEditors() {
        // refresh the edit with the first one, this one should be modified later
        MetadataTableEditorView inputMetaEditorView = tabFolderEditors.getInputMetaEditorView();
        MetadataTableEditorView outputMetaEditorView = tabFolderEditors.getOutputMetaEditorView();
        IODataComponentContainer ioDataComponents = mapperComponent.getIODataComponents();

        if (!ioDataComponents.getInputs().isEmpty()) {
            IODataComponent ioDataComponent = ioDataComponents.getInputs().get(0);
            IMetadataTable table = ioDataComponent.getTable();
            MetadataTableEditor editor = new MetadataTableEditor(table, table.getLabel());
            inputMetaEditorView.setMetadataTableEditor(editor);
        }

        if (!ioDataComponents.getOuputs().isEmpty()) {
            IODataComponent ioDataComponent = ioDataComponents.getOuputs().get(0);
            IMetadataTable table = ioDataComponent.getTable();
            MetadataTableEditor editor = new MetadataTableEditor(table, table.getLabel());
            outputMetaEditorView.setMetadataTableEditor(editor);
        }

    }

    public void closeMapperDialog(int response) {
        mapperResponse = response;
        if (response == SWT.OK || response == SWT.APPLICATION_MODAL) {
            mapperComponent.setEmfMapData(copyOfMapData);
            if (response == SWT.APPLICATION_MODAL) {
                IExternalNode externalNode = mapperComponent;
                IWorkbenchPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();

                if (externalNode != null && (part instanceof AbstractMultiPageTalendEditor)) {
                    INode node = externalNode.getOriginalNode();
                    if (node != null && node instanceof Node) {
                        Command cmd = new ExternalNodeChangeCommand((Node) node, externalNode);
                        CommandStack cmdStack = (CommandStack) part.getAdapter(CommandStack.class);
                        cmdStack.execute(cmd);
                    }
                }
            }
        }
        if (response == SWT.OK || response == SWT.CANCEL) {
            mapperShell.close();
        }

    }

    public int getMapperDialogResponse() {
        return mapperResponse;
    }

    public XmlMapComponent getMapperComponent() {
        return mapperComponent;
    }

    public void prepareModel(IODataComponentContainer ioDataContainer, List<IMetadataTable> outputMetadataTables) {

        if (mapperComponent.getEmfMapData() != null) {
            copyOfMapData = EcoreUtil.copy(mapperComponent.getEmfMapData());
        } else {
            copyOfMapData = XmlmapFactory.eINSTANCE.createXmlMapData();
        }
        prepareModelInputs(ioDataContainer.getInputs());
        prepareModelOutputs(ioDataContainer.getOuputs(), outputMetadataTables);
    }

    private void prepareModelInputs(List<IODataComponent> inputConn) {
        if (inputConn == null || inputConn.isEmpty()) {
            copyOfMapData.getInputTrees().clear();
            return;
        }
        for (IODataComponent inData : inputConn) {
            String name = inData.getName();
            InputXmlTree inputTree = null;
            for (InputXmlTree in : copyOfMapData.getInputTrees()) {
                if (in.getName() != null && in.getName().equals(name)) {
                    inputTree = in;
                    break;
                }
            }
            if (inputTree == null) {
                inputTree = XmlmapFactory.eINSTANCE.createInputXmlTree();
                inputTree.setName(name);
                inputTree.setLookup(EConnectionType.FLOW_MAIN != inData.getConnection().getLineStyle());
                copyOfMapData.getInputTrees().add(inputTree);
            }

            List<IMetadataColumn> listColumns = inData.getTable().getListColumns();
            if (inData.getTable() != null && listColumns != null) {
                EList<TreeNode> nodes = inputTree.getNodes();
                for (int i = 0; i < listColumns.size(); i++) {
                    IMetadataColumn column = listColumns.get(i);
                    TreeNode found = null;
                    int j = 0;
                    for (; j < nodes.size(); j++) {
                        TreeNode node = nodes.get(j);
                        if (node.getName() != null && node.getName().equals(column.getLabel())) {
                            found = node;
                            break;
                        }
                    }
                    if (found != null) {
                        if (i != j) {
                            // do switch to keep the same sequence
                            TreeNode temp = nodes.get(j);
                            nodes.remove(j);
                            nodes.add(i, temp);
                        }
                    } else {
                        found = XmlmapFactory.eINSTANCE.createTreeNode();
                        found.setName(column.getLabel());
                        found.setType(column.getTalendType());
                        found.setNullable(column.isNullable());
                        found.setXpath(inputTree.getName() + XmlMapUtil.XPATH_SEPARATOR + found.getName());
                        nodes.add(i, found);
                    }

                    // add a default root for document
                    if (XmlMapUtil.DOCUMENT.equals(found.getType())) {
                        EList<TreeNode> children = found.getChildren();
                        if (children.isEmpty()) {
                            TreeNode treeRoot = XmlmapFactory.eINSTANCE.createTreeNode();
                            treeRoot.setName("root");
                            treeRoot.setType(XmlMapUtil.DEFAULT_DATA_TYPE);
                            treeRoot.setXpath(found.getXpath() + XmlMapUtil.XPATH_SEPARATOR + treeRoot.getName());
                            treeRoot.setNodeType(NodeType.ELEMENT);
                            children.add(treeRoot);
                        }
                    }

                }

                if (nodes.size() > listColumns.size()) {
                    List unUsed = new ArrayList();
                    for (int i = listColumns.size(); i < nodes.size(); i++) {
                        unUsed.add(nodes.get(i));
                    }
                    nodes.removeAll(unUsed);
                }

            }
        }

    }

    private void prepareModelOutputs(List<IODataComponent> outputConn, List<IMetadataTable> outputMetadataTables) {
        if (outputConn == null || outputConn.isEmpty()) {
            copyOfMapData.getOutputTrees().clear();
            return;
        }
        for (IODataComponent outData : outputConn) {
            String name = outData.getName();
            OutputXmlTree outputTree = null;
            for (OutputXmlTree out : copyOfMapData.getOutputTrees()) {
                if (out.getName() != null && out.getName().equals(name)) {
                    outputTree = out;
                    break;
                }
            }
            if (outputTree == null) {
                outputTree = XmlmapFactory.eINSTANCE.createOutputXmlTree();
                outputTree.setName(name);
                copyOfMapData.getOutputTrees().add(outputTree);
            }

            List<IMetadataColumn> listColumns = outData.getTable().getListColumns();
            if (outData.getTable() != null && listColumns != null) {
                EList<OutputTreeNode> nodes = outputTree.getNodes();
                for (int i = 0; i < listColumns.size(); i++) {
                    IMetadataColumn column = listColumns.get(i);
                    OutputTreeNode found = null;
                    int j = 0;
                    for (; j < nodes.size(); j++) {
                        OutputTreeNode node = nodes.get(j);
                        if (node.getName() != null && node.getName().equals(column.getLabel())) {
                            found = node;
                            break;
                        }
                    }
                    if (found != null) {
                        if (i != j) {
                            // do switch to keep the same sequence
                            OutputTreeNode temp = nodes.get(j);
                            nodes.remove(j);
                            nodes.add(i, temp);
                        }
                    } else {
                        found = XmlmapFactory.eINSTANCE.createOutputTreeNode();
                        found.setName(column.getLabel());
                        found.setType(column.getTalendType());
                        found.setNullable(column.isNullable());
                        found.setXpath(outputTree.getName() + XmlMapUtil.XPATH_SEPARATOR + found.getName());
                        nodes.add(i, found);
                    }

                    // add a default root for document
                    if (XmlMapUtil.DOCUMENT.equals(found.getType())) {
                        EList<TreeNode> children = found.getChildren();
                        if (children.isEmpty()) {
                            TreeNode treeRoot = XmlmapFactory.eINSTANCE.createOutputTreeNode();
                            treeRoot.setName("root");
                            treeRoot.setType(XmlMapUtil.DEFAULT_DATA_TYPE);
                            treeRoot.setXpath(found.getXpath() + XmlMapUtil.XPATH_SEPARATOR + treeRoot.getName());
                            treeRoot.setNodeType(NodeType.ELEMENT);
                            children.add(treeRoot);
                        }
                    }

                }

                if (nodes.size() > listColumns.size()) {
                    List unUsed = new ArrayList();
                    for (int i = listColumns.size(); i < nodes.size(); i++) {
                        unUsed.add(nodes.get(i));
                    }
                    nodes.removeAll(unUsed);
                }

            }
            copyOfMapData.getOutputTrees().add(outputTree);
        }
    }

}
