// ============================================================================
//
// Copyright (C) 2006-2015 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.hl7.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.actions.SelectionProviderAction;
import org.talend.core.model.metadata.builder.connection.HL7Connection;
import org.talend.core.model.metadata.builder.connection.HL7FileNode;
import org.talend.designer.hl7.managers.HL7OutputManager;
import org.talend.designer.hl7.ui.HL7UI;
import org.talend.designer.hl7.ui.data.Attribute;
import org.talend.designer.hl7.ui.data.Element;
import org.talend.designer.hl7.ui.data.HL7TreeNode;
import org.talend.designer.hl7.ui.data.NameSpaceNode;
import org.talend.designer.hl7.ui.form.AbstractHL7StepForm;
import org.talend.designer.hl7.ui.header.HL7Parse;
import org.talend.designer.hl7.util.HL7PublicUtil;

import ca.uhn.hl7v2.model.Message;

/**
 * DOC hwang class global comment. Detailled comment
 */
public class ImportHL7StructureAction extends SelectionProviderAction {

    private TreeViewer xmlViewer;

    private HL7UI hl7ui;

    private AbstractHL7StepForm form;

    private int order = 1;

    private HL7Connection hl7Connection;

    private HL7PublicUtil hl7Util = new HL7PublicUtil();

    private Map<String, Integer> orderMap = new HashMap<String, Integer>();

    /**
     * Create constructor comment.
     * 
     * @param provider
     * @param text
     */
    public ImportHL7StructureAction(HL7Connection hl7Connection, TreeViewer xmlViewer, String text, AbstractHL7StepForm form) {
        super(xmlViewer, text);
        this.xmlViewer = xmlViewer;
        this.form = form;
        this.hl7Connection = hl7Connection;
    }

    public ImportHL7StructureAction(TreeViewer xmlViewer, HL7UI hl7ui, String text) {
        super(xmlViewer, text);
        this.xmlViewer = xmlViewer;
        this.hl7ui = hl7ui;
    }

    private List treeNodeAdapt() {
        List<HL7TreeNode> list = new ArrayList<HL7TreeNode>();
        FileDialog f = null;
        if (hl7ui != null) {
            f = new FileDialog(hl7ui.getHl7UIParent().getShell());
        } else if (form != null) {
            f = new FileDialog(form.getShell());
        }
        String file = f.open();
        if (file == null) {
            return list;
        }
        HL7Parse hl7Parse = new HL7Parse();
        List<String> msgContentList = new ArrayList<String>();

        List<Message> messageList = hl7Parse.doParse(file, "'\\u000b'", "'\\u001c'");
        List<HL7TreeNode> nodeList = hl7Util.getHL7TreeNodes(messageList);

        List schemaList = new ArrayList();

        for (Message message : messageList) {
            schemaList.addAll(hl7Util.getFirstLevelChild(message));
        }
        // for (Object obj : schemaList) {
        //
        // }
        if (!nodeList.isEmpty()) {
            HL7TreeNode hl7TreeNode = nodeList.get(0);
            List<HL7FileNode> table = new ArrayList<HL7FileNode>();
            if (hl7Connection != null) {
                EList root = hl7Connection.getRoot();
                root.clear();
                if (hl7TreeNode != null) {
                    hl7Util.initNodeOrder(hl7TreeNode, orderMap, order);
                    hl7Util.tableLoader((Element) hl7TreeNode, "", root, hl7TreeNode.getDefaultValue(), orderMap);
                }
                table.addAll(root);
            } else {
                if (hl7TreeNode != null) {
                    hl7Util.initNodeOrder(hl7TreeNode, orderMap, order);
                    hl7Util.tableLoader((Element) hl7TreeNode, "", table, hl7TreeNode.getDefaultValue(), orderMap);
                }
            }

            // list.add(hl7TreeNode);
            initXmlTreeData(schemaList, table, list);
        }

        // try {
        // ATreeNode treeNode = SchemaPopulationUtil.getSchemaTree(file, true, 0);
        // String schemaName = getSelectedSchema();
        // HL7TreeNode root = cloneATreeNode(treeNode, schemaName);
        // root = ((Element) root).getElementChildren().get(0);
        // root.setParent(null);
        // list.add(root);
        // } catch (Exception e) {
        // // e.printStackTrace();
        // ExceptionHandler.process(e);
        // }
        return list;
    }

    private void initXmlTreeData(List schemaList, List<HL7FileNode> root, List<HL7TreeNode> list) {
        if (hl7ui != null) {
            if (hl7ui.gethl7Manager() instanceof HL7OutputManager) {
                ((HL7OutputManager) hl7ui.gethl7Manager()).getContents().clear();
            }
        }
        for (Object obj : schemaList) {
            List<HL7TreeNode> treeNodes = null;
            HL7TreeNode rootNode = null;
            HL7TreeNode current = null;
            HL7TreeNode temp = null;
            HL7TreeNode mainNode = null;
            String mainPath = null;
            String currentPath = null;
            String defaultValue = null;
            int nodeOrder = 0;
            boolean haveOrder = true;
            String schemaId = hl7Util.getLabel(obj, true) + ":";//((MetadataTable) obj).getLabel() + ":"; //$NON-NLS-1$
            if (hl7ui != null) {
                if (hl7ui.gethl7Manager() instanceof HL7OutputManager) {
                    treeNodes = ((HL7OutputManager) hl7ui.gethl7Manager()).getTreeData(hl7Util.getLabel(obj, true));
                }
            }
            if (treeNodes == null) {
                treeNodes = new ArrayList<HL7TreeNode>();
            }
            // build root tree
            for (int i = 0; i < root.size(); i++) {
                HL7FileNode node = (HL7FileNode) root.get(i);
                String newPath = node.getFilePath();
                defaultValue = node.getDefaultValue();
                String columnName = node.getRelatedColumn();
                // String type = node.getType();
                String orderValue = String.valueOf(node.getOrder());
                if (orderValue == null || "".equals(orderValue)) {
                    haveOrder = false;
                }
                if (haveOrder) {
                    nodeOrder = node.getOrder();
                }
                String flag = columnName + ":"; //$NON-NLS-1$
                if (columnName != null && columnName.length() > 0 && !flag.startsWith(schemaId)) {
                    continue;
                }
                if (node.getAttribute().equals("attri")) {
                    temp = new Attribute(newPath);
                    temp.setDefaultValue(defaultValue);
                    temp.setAttribute(true);
                    // temp.setDataType(type);
                    current.addChild(temp);
                } else if (node.getAttribute().equals("ns")) {
                    temp = new NameSpaceNode(newPath);
                    temp.setDefaultValue(defaultValue);
                    temp.setNameSpace(true);
                    // temp.setDataType(type);
                    current.addChild(temp);
                } else {
                    temp = this.addElement(current, currentPath, newPath, defaultValue);
                    // temp.setDataType(type);
                    if (rootNode == null) {
                        rootNode = temp;
                    }
                    if (node.getAttribute().equals("main")) {
                        temp.setMain(true);
                        mainNode = temp;
                        mainPath = newPath;
                    }
                    current = temp;
                    currentPath = newPath;
                }
                if (haveOrder) {
                    temp.setOrder(nodeOrder);
                }
                // if (columnName != null && columnName.length() > 0) {
                // temp.setColumn(getColumn(columnName));
                //
                // }
                temp.setRow(hl7Util.getLabel(obj, true));
                if (columnName != null && columnName.length() > 0 && columnName.startsWith(schemaId)) {
                    columnName = columnName.replace(schemaId, ""); //$NON-NLS-1$
                    // IMetadataTable iTable = ConvertionHelper.convert((MetadataTable) obj);
                    // // temp.setColumn(iTable.getColumn(columnName));
                    // temp.setTable(iTable);
                }
            }

            // build group tree
            current = mainNode;
            currentPath = mainPath;
            boolean isFirst = true;

            // build loop tree
            current = mainNode;
            currentPath = mainPath;
            isFirst = true;

            if (rootNode == null) {
                rootNode = new Element("rootTag");
            }
            // rootNode.setParent(null);
            if (haveOrder) {
                orderNode(rootNode);
            }
            list.add(rootNode);
            rootNode.setRow(hl7Util.getLabel(obj, true));
            // contents.put(((MetadataTable) obj).getLabel(), rootNode);
            treeNodes.clear();
            treeNodes.add(rootNode);
            if (hl7ui != null) {
                if (hl7ui.gethl7Manager() instanceof HL7OutputManager) {
                    ((HL7OutputManager) hl7ui.gethl7Manager()).getContents().put(hl7Util.getLabel(obj, true), treeNodes);
                }

            } else if (form != null) {
                for (HL7TreeNode hl7Node : treeNodes) {
                    form.getContents().put(hl7Util.getLabel(obj, true), hl7Node);
                }
            }
        }

        // if (haveOrder) {
        // orderNode(rootNode);
        // }
        // treeData.add(rootNode);
        // rootNode.setRow(metadataTable.getLabel());
        //
    }

    private void orderNode(HL7TreeNode node) {
        // reset the order.
        if (node != null) {
            List<HL7TreeNode> firstSubChildren = node.getChildren();
            HL7TreeNode foundNode = null;
            for (HL7TreeNode childen : firstSubChildren) {
                if (childen.isRepetable()) {
                    foundNode = childen;
                    sortOrder(foundNode, node);
                    break;
                } else if (childen.isGroup()) {
                    foundNode = childen;
                    sortOrder(foundNode, node);
                    orderNode(childen);
                } else {
                    orderNode(childen);
                }
            }
        }
    }

    private void sortOrder(HL7TreeNode treeNode, HL7TreeNode node) {
        if (node != null) {
            List<HL7TreeNode> children = node.getChildren();
            if (treeNode != null) {
                int tmpOrder = 0;
                int attrNsCount = 0;
                for (HL7TreeNode child : children) {
                    if (child.getOrder() < treeNode.getOrder()) {
                        tmpOrder++;
                    }
                    if (child.isAttribute() || child.isNameSpace()) {
                        attrNsCount++;
                    }
                }
                if (tmpOrder > -1) {
                    int oldOrder = children.indexOf(treeNode);
                    if (oldOrder != -1 && oldOrder != tmpOrder) {
                        node.removeChild(treeNode);
                        if (children.size() > tmpOrder) {
                            node.addChild(tmpOrder - attrNsCount, treeNode);
                        } else {
                            node.addChild(treeNode);
                        }
                    }
                }
            }
        }

    }

    protected HL7TreeNode addElement(HL7TreeNode current, String currentPath, String newPath, String defaultValue) {
        String name = newPath.substring(newPath.lastIndexOf("/") + 1); //$NON-NLS-1$
        String parentPath = newPath.substring(0, newPath.lastIndexOf("/")); //$NON-NLS-1$
        HL7TreeNode temp = new Element(name, defaultValue);

        if (current == null) {// root node
            return temp;
        }

        if (currentPath.equals(parentPath)) {
            current.addChild(temp);
        } else {
            String[] nods = currentPath.split("/"); //$NON-NLS-1$
            String[] newNods = parentPath.split("/"); //$NON-NLS-1$
            int parentLevel = 0;
            int checkLength = nods.length < newNods.length ? nods.length : newNods.length;
            for (int i = 1; i < checkLength; i++) {
                if (nods[i].equals(newNods[i])) {
                    parentLevel = i;
                }
            }
            HL7TreeNode parent = current;
            for (int i = 0; i < nods.length - (parentLevel + 1); i++) {
                HL7TreeNode tmpParent = parent.getParent();
                if (tmpParent == null) {
                    break;
                }
                parent = tmpParent;
            }

            if (parent != null)
                parent.addChild(temp);
        }

        return temp;
    }

    /**
     * 
     * wzhang Comment method "getSelectedSchema".
     * 
     * @return
     */
    private String getSelectedSchema() {
        TreeItem[] selection = xmlViewer.getTree().getSelection();
        if (selection.length > 0) {
            Object data = selection[0].getData();
            if (data instanceof HL7TreeNode) {
                return ((HL7TreeNode) data).getRow();
            }
        }
        return "";// hl7ui.gethl7Manager().getCurrentSchema();
    }

    public void init() {
        this.setEnabled(true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {
        List<HL7TreeNode> newInput = treeNodeAdapt();
        if (newInput.size() == 0) {
            return;
        }
        List<HL7TreeNode> treeData = null;
        if (form != null) {
            treeData = form.getTreeData();

        } else if (hl7ui != null) {
            if (hl7ui.gethl7Manager() instanceof HL7OutputManager) {
                treeData = ((HL7OutputManager) hl7ui.gethl7Manager()).getTreeData();
            }
        }
        treeData.clear();
        treeData.addAll(newInput);
        xmlViewer.setInput(treeData);
        // TreeUtil.guessAndSetLoopNode((HL7TreeNode) xmlViewer.getTree().getItem(0).getData());
        xmlViewer.refresh();
        xmlViewer.expandAll();
        // hl7ui.updateStatus();
        // hl7ui.redrawLinkers();
        if (hl7ui != null) {
            hl7ui.redrawLinkers();
        } else if (form != null) {
            form.refreshLinks();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.actions.SelectionProviderAction#selectionChanged(org.eclipse.jface.viewers.IStructuredSelection)
     */
    @Override
    public void selectionChanged(IStructuredSelection selection) {
        this.setEnabled(true);
        HL7TreeNode node = (HL7TreeNode) this.getStructuredSelection().getFirstElement();
        if (node != null && form != null) {
            // hl7ui.setSelectedText(node.getLabel());
            // Added by Marvin Wang on Sep. 11, 2012 for bug TDI-20702.
            form.setSelectedText(node.getLabel());
        }
    }

}
