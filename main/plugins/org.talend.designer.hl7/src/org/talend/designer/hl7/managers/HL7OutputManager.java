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
package org.talend.designer.hl7.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.process.ElementParameterParser;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IConnectionCategory;
import org.talend.core.model.utils.NodeUtil;
import org.talend.designer.hl7.HL7InputComponent;
import org.talend.designer.hl7.ui.data.Attribute;
import org.talend.designer.hl7.ui.data.Element;
import org.talend.designer.hl7.ui.data.HL7TreeNode;
import org.talend.designer.hl7.ui.data.NameSpaceNode;

/**
 * DOC hwang class global comment. Detailled comment
 */
public class HL7OutputManager extends HL7Manager {

    private List<HL7TreeNode> treeData;

    private String currentSchema;

    private Map<String, List<HL7TreeNode>> contents = new HashMap<String, List<HL7TreeNode>>();

    private Map<String, Integer> orderMap = new HashMap<String, Integer>();

    private int order = 1;

    private Map<String, String> schemaMap = new HashMap<String, String>();

    public HL7OutputManager(HL7InputComponent hl7Component) {
        super(hl7Component);
        uiManager = new UIManager(this);
        initModel();
    }

    public void initModel() {
        int i = 0;
        List<? extends IConnection> incomingConnections = NodeUtil.getIncomingConnections(hl7Component, IConnectionCategory.FLOW);
        // HL7Root root = new HL7Root("root");
        List<Map<String, String>> maps = (List<Map<String, String>>) ElementParameterParser.getObjectValue(hl7Component,
                "__SCHEMAS__"); //$NON-NLS-1$
        List<String> schemaList = new ArrayList<String>();
        List<Map<String, String>> rootTable = hl7Component.getTableList(HL7InputComponent.ROOT);
        List<String> columnList = new ArrayList<String>();
        if (rootTable != null && rootTable.size() > 0) {
            for (Map<String, String> rootMap : rootTable) {
                String columnName = rootMap.get(HL7InputComponent.COLUMN);
                if (columnName.contains(":")) {
                    columnName = columnName.substring(0, columnName.indexOf(":"));
                }
                if (!columnList.contains(columnName) && !"".equals(columnName)) {
                    columnList.add(columnName);
                }
            }
            HL7TreeNode rootNode = null;
            for (String rowName : columnList) {
                IMetadataTable metadataTable = null;
                String metadataTableName = rowName;
                for (IConnection connection : incomingConnections) {
                    if (connection.getUniqueName().equals(rowName)) {
                        metadataTable = connection.getMetadataTable();
                        metadataTable.setLabel(connection.getUniqueName());
                        metadataTableName = metadataTable.getLabel();
                    }
                }

                // for (Map<String, String> map : maps) {
                // if (map.containsValue(rowName)) {
                // if (map.get("PARENT_ROW") != null && map.get("PARENT_ROW").equals(rowName)) {
                // String schemaName = map.get("SCHEMA");
                // int first = schemaName.indexOf("_");
                // int second = schemaName.lastIndexOf("_");
                // if (first > 0 && first < second) {
                // schemaName = schemaName.substring(first + 1, second);
                // }
                // if (!schemaList.contains(schemaName)) {
                // schemaList.add(schemaName);
                // rowName = schemaName;// map.get(rowName);
                // schemaMap.put(metadataTableName, rowName);
                // break;
                // }
                //
                // }
                //
                // }
                // }

                treeData = new ArrayList<HL7TreeNode>();
                if (i == 0) {
                    currentSchema = metadataTableName;// metadataTable.getLabel();
                }

                HL7TreeNode current = null;
                HL7TreeNode temp = null;
                HL7TreeNode mainNode = null;
                String mainPath = null;
                String currentPath = null;
                String defaultValue = null;
                int nodeOrder = 0;
                boolean haveOrder = true;

                String schemaId = rowName + ":";

                for (Map<String, String> rootMap : rootTable) {
                    String newPath = rootMap.get(HL7InputComponent.PATH);
                    String columnName = rootMap.get(HL7InputComponent.COLUMN);
                    defaultValue = rootMap.get(HL7InputComponent.VALUE);
                    String orderValue = rootMap.get(HL7InputComponent.ORDER);
                    boolean repeatable = Boolean.valueOf(rootMap.get("REPEATABLE"));
                    if (orderValue == null || "".equals(orderValue)) {
                        haveOrder = false;
                    }
                    if (haveOrder) {
                        nodeOrder = Integer.valueOf(rootMap.get(HL7InputComponent.ORDER)).intValue();
                    }
                    String flag = columnName + ":"; //$NON-NLS-1$
                    if (columnName != null && columnName.length() > 0 && !flag.startsWith(rowName + ":")) { //$NON-NLS-1$
                        continue;
                    }
                    if (rootMap.get(HL7InputComponent.ATTRIBUTE).equals("attri")) { //$NON-NLS-1$
                        temp = new Attribute(newPath);
                        temp.setDefaultValue(defaultValue);
                        current.addChild(temp);
                    } else if (rootMap.get(HL7InputComponent.ATTRIBUTE).equals("ns")) { //$NON-NLS-1$
                        temp = new NameSpaceNode(newPath);
                        temp.setDefaultValue(defaultValue);
                        current.addChild(temp);
                    } else {
                        temp = addElement(current, currentPath, newPath, defaultValue);
                        if (rootNode == null) {
                            rootNode = temp;
                        } else if (rowName.equals(columnName) && rowName.equals(temp.getLabel())) {
                            if (!rootNode.getChildren().contains(temp)) {
                                rootNode.addChild(temp);
                            }
                        }
                        if (rootMap.get(HL7InputComponent.ATTRIBUTE).equals("main")) { //$NON-NLS-1$
                            temp.setMain(true);
                            mainNode = temp;
                            mainPath = newPath;
                        }
                        current = temp;
                        currentPath = newPath;
                    }
                    temp.setRepetable(repeatable);
                    if (haveOrder) {
                        temp.setOrder(nodeOrder);
                    }
                    if (columnName != null && columnName.length() > 0 && columnName.startsWith(schemaId)) {
                        columnName = columnName.replace(schemaId, ""); // $!=Nnull-1$
                        // group node can not get the metadata table
                        if (metadataTable == null) {
                            IMetadataTable metadataTableTemp = null;
                            for (IConnection connection : incomingConnections) {
                                metadataTableTemp = connection.getMetadataTable();
                                if (columnName.startsWith(metadataTableTemp.getLabel())) {
                                    break;
                                }
                            }
                            if (metadataTableTemp != null) {
                                temp.setColumn(metadataTableTemp.getColumn(columnName));
                                temp.setTable(metadataTableTemp);
                            }
                        } else {
                            temp.setColumn(metadataTable.getColumn(columnName));
                            temp.setTable(metadataTable);
                        }
                    }
                }

                if (rootNode == null) {
                    rootNode = new Element("rootTag"); //$NON-NLS-1$
                    // rootNode.setParent(root);
                    // root.addChild(rootNode);
                }

                if (haveOrder) {
                    orderNode(rootNode);
                }
                i++;
            }
            if (rootNode != null && treeData != null) {
                treeData.add(rootNode);
                contents.put(rootNode.getColumnLabel(), treeData);
            }
            initCurrentSchema();

        } else if (rootTable != null && rootTable.size() <= 0) {
            for (IConnection connection : incomingConnections) {
                IMetadataTable metadataTable = connection.getMetadataTable();
                metadataTable.setLabel(connection.getUniqueName());
                String metadataTableName = metadataTable.getLabel();
                String rowName = metadataTable.getLabel();
                for (Map<String, String> map : maps) {
                    if (map.containsValue(rowName)) {
                        if (map.get("PARENT_ROW") != null && map.get("PARENT_ROW").equals(rowName)) {
                            String schemaName = map.get("SCHEMA");
                            int first = schemaName.indexOf("_");
                            int second = schemaName.lastIndexOf("_");
                            if (first > 0 && first < second) {
                                schemaName = schemaName.substring(first + 1, second);
                            }
                            if (!schemaList.contains(schemaName)) {
                                schemaList.add(schemaName);
                                rowName = schemaName;// map.get(rowName);
                                schemaMap.put(metadataTableName, rowName);
                                break;
                            }

                        }

                    }
                }
                treeData = new ArrayList<HL7TreeNode>();
                if (i == 0) {
                    currentSchema = metadataTableName;// metadataTable.getLabel();
                }
                HL7TreeNode rootNode = null;
                HL7TreeNode current = null;
                HL7TreeNode temp = null;
                HL7TreeNode mainNode = null;
                String mainPath = null;
                String currentPath = null;
                String defaultValue = null;
                int nodeOrder = 0;
                boolean haveOrder = true;

                String schemaId = rowName + ":";//metadataTable.getLabel() + ":"; //$NON-NLS-1$

                // build root tree
                // List<Map<String, String>> rootTable = (List<Map<String, String>>)
                // hl7Component.getTableList(HL7InputComponent.ROOT);
                if (rootTable != null) {
                    for (Map<String, String> rootMap : rootTable) {
                        String newPath = rootMap.get(HL7InputComponent.PATH);
                        String columnName = rootMap.get(HL7InputComponent.COLUMN);
                        defaultValue = rootMap.get(HL7InputComponent.VALUE);
                        String orderValue = rootMap.get(HL7InputComponent.ORDER);
                        boolean repeatable = Boolean.valueOf(rootMap.get("REPEATABLE"));
                        if (orderValue == null || "".equals(orderValue)) {
                            haveOrder = false;
                        }
                        if (haveOrder) {
                            nodeOrder = Integer.valueOf(rootMap.get(HL7InputComponent.ORDER)).intValue();
                        }
                        String flag = columnName + ":"; //$NON-NLS-1$
                        if (columnName != null && columnName.length() > 0 && !flag.startsWith(rowName + ":")) { //$NON-NLS-1$
                            continue;
                        }
                        if (rootMap.get(HL7InputComponent.ATTRIBUTE).equals("attri")) { //$NON-NLS-1$
                            temp = new Attribute(newPath);
                            temp.setDefaultValue(defaultValue);
                            current.addChild(temp);
                        } else if (rootMap.get(HL7InputComponent.ATTRIBUTE).equals("ns")) { //$NON-NLS-1$
                            temp = new NameSpaceNode(newPath);
                            temp.setDefaultValue(defaultValue);
                            current.addChild(temp);
                        } else {
                            temp = addElement(current, currentPath, newPath, defaultValue);
                            if (rootNode == null) {
                                rootNode = temp;
                            }
                            if (rootMap.get(HL7InputComponent.ATTRIBUTE).equals("main")) { //$NON-NLS-1$
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
                        temp.setRepetable(repeatable);
                        temp.setRow(rowName);
                        if (columnName != null && columnName.length() > 0 && columnName.startsWith(schemaId)) {
                            columnName = columnName.replace(schemaId, ""); //$NON-NLS-1$
                            temp.setColumn(metadataTable.getColumn(columnName));
                            temp.setTable(metadataTable);
                        }

                    }
                    if (rootNode == null) {
                        rootNode = new Element("rootTag"); //$NON-NLS-1$
                        // rootNode.setParent(root);
                        // root.addChild(rootNode);
                    }

                    if (haveOrder) {
                        orderNode(rootNode);
                    }
                    treeData.add(rootNode);
                    rootNode.setRow(rowName);
                    contents.put(metadataTableName, treeData);
                    i++;
                }
            }
        }

    }

    // // build group tree
    // current = mainNode;
    // currentPath = mainPath;
    // boolean isFirst = true;
    // List<Map<String, String>> groupTable = (List<Map<String, String>>)
    // hl7Component.getTableList(HL7InputComponent.GROUP);
    // if (groupTable != null) {
    // for (Map<String, String> groupMap : groupTable) {
    // String newPath = groupMap.get(HL7InputComponent.PATH);
    // String columnName = groupMap.get(HL7InputComponent.COLUMN);
    // defaultValue = groupMap.get(HL7InputComponent.VALUE);
    // String orderValue = groupMap.get(HL7InputComponent.ORDER);
    // if (orderValue == null || "".equals(orderValue)) {
    // haveOrder = false;
    // }
    // if (haveOrder) {
    // nodeOrder = Integer.valueOf(groupMap.get(HL7InputComponent.ORDER)).intValue();
    // }
    //                    String flag = columnName + ":"; //$NON-NLS-1$
    //                    if (columnName != null && columnName.length() > 0 && !flag.startsWith(rowName + ":")) { //$NON-NLS-1$
    // continue;
    // }
    //                    if (groupMap.get(HL7InputComponent.ATTRIBUTE).equals("attri")) { //$NON-NLS-1$
    // temp = new Attribute(newPath);
    // temp.setDefaultValue(defaultValue);
    // current.addChild(temp);
    //                    } else if (groupMap.get(HL7InputComponent.ATTRIBUTE).equals("ns")) { //$NON-NLS-1$
    // temp = new NameSpaceNode(newPath);
    // temp.setDefaultValue(defaultValue);
    // current.addChild(temp);
    // } else {
    // temp = this.addElement(current, currentPath, newPath, defaultValue);
    //                        if (groupMap.get(HL7InputComponent.ATTRIBUTE).equals("main")) { //$NON-NLS-1$
    // temp.setMain(true);
    // mainNode = temp;
    // mainPath = newPath;
    // }
    // if (isFirst) {
    // temp.setGroup(true);
    // isFirst = false;
    // }
    // current = temp;
    // currentPath = newPath;
    // }
    // if (haveOrder) {
    // temp.setOrder(nodeOrder);
    // }
    // temp.setRow(rowName);
    // if (columnName != null && columnName.length() > 0 && columnName.startsWith(schemaId)) {
    //                        columnName = columnName.replace(schemaId, ""); //$NON-NLS-1$
    // temp.setColumn(metadataTable.getColumn(columnName));
    // temp.setTable(metadataTable);
    // }
    // }
    // }
    //
    // // build loop tree
    // current = mainNode;
    // currentPath = mainPath;
    // isFirst = true;
    // List<Map<String, String>> loopTable = (List<Map<String, String>>)
    // hl7Component.getTableList(HL7InputComponent.LOOP);
    // if (loopTable != null) {
    // for (Map<String, String> loopMap : loopTable) {
    // String newPath = loopMap.get(HL7InputComponent.PATH);
    // String columnName = loopMap.get(HL7InputComponent.COLUMN);
    // defaultValue = loopMap.get(HL7InputComponent.VALUE);
    // String orderValue = loopMap.get(HL7InputComponent.ORDER);
    // if (orderValue == null || "".equals(orderValue)) {
    // haveOrder = false;
    // }
    // if (haveOrder) {
    // nodeOrder = Integer.valueOf(loopMap.get(HL7InputComponent.ORDER)).intValue();
    // }
    //                    String flag = columnName + ":"; //$NON-NLS-1$
    //                    if (columnName != null && columnName.length() > 0 && !flag.startsWith(rowName + ":")) { //$NON-NLS-1$
    // continue;
    // }
    //                    if (loopMap.get(HL7InputComponent.ATTRIBUTE).equals("attri")) { //$NON-NLS-1$
    // temp = new Attribute(newPath);
    // temp.setDefaultValue(defaultValue);
    // current.addChild(temp);
    //                    } else if (loopMap.get(HL7InputComponent.ATTRIBUTE).equals("ns")) { //$NON-NLS-1$
    // temp = new NameSpaceNode(newPath);
    // temp.setDefaultValue(defaultValue);
    // current.addChild(temp);
    // } else {
    // temp = this.addElement(current, currentPath, newPath, defaultValue);
    //                        if (loopMap.get(HL7InputComponent.ATTRIBUTE).equals("main")) { //$NON-NLS-1$
    // temp.setMain(true);
    // }
    // if (isFirst) {
    // temp.setRepetable(true);
    // isFirst = false;
    // }
    // current = temp;
    // currentPath = newPath;
    // }
    // if (haveOrder) {
    // temp.setOrder(nodeOrder);
    // }
    // temp.setRow(rowName);
    // if (columnName != null && columnName.length() > 0 && columnName.startsWith(schemaId)) {
    //                        columnName = columnName.replace(schemaId, ""); //$NON-NLS-1$
    // temp.setColumn(metadataTable.getColumn(columnName));
    // temp.setTable(metadataTable);
    // }
    // }
    //
    // if (rootNode == null) {
    //                    rootNode = new Element("rootTag"); //$NON-NLS-1$
    // // rootNode.setParent(root);
    // // root.addChild(rootNode);
    // }
    //
    // if (haveOrder) {
    // orderNode(rootNode);
    // }
    // treeData.add(rootNode);
    // rootNode.setRow(rowName);
    // contents.put(metadataTableName, treeData);
    // i++;
    // }
    // }
    // this.setTreeRoot(root);
    // }

    public List<Map<String, String>> getRootTable() {
        List<Map<String, String>> result = new ArrayList<Map<String, String>>();
        for (HL7TreeNode rootNode : this.getOriginalNodes()) {
            // if (!(rootNode instanceof HL7Root)) {
            initNodeOrder(rootNode);
            order = 1;
            // }
            tableLoader((Element) rootNode, "", result, rootNode.getDefaultValue()); //$NON-NLS-1$
        }
        return result;
    }

    public List<Map<String, String>> getLoopTable() {
        List<Map<String, String>> result = new ArrayList<Map<String, String>>();
        for (HL7TreeNode rootNode : this.getOriginalNodes()) {
            Element loopNode = (Element) getLoopNode(rootNode);
            if (loopNode != null) {
                String path = getPath(loopNode);
                tableLoader(loopNode, path.substring(0, path.lastIndexOf("/")), result, loopNode.getDefaultValue()); //$NON-NLS-1$
            }
        }
        return result;
    }

    public String getPath(HL7TreeNode treeNode) {
        StringBuffer path = new StringBuffer();
        HL7TreeNode tmp = treeNode;
        while (tmp != null) {
            path.insert(0, "/" + tmp.getLabel()); //$NON-NLS-1$
            tmp = tmp.getParent();
        }
        return path.toString();
    }

    public HL7TreeNode getLoopNode(HL7TreeNode root) {
        if (root != null && root instanceof Element) {
            Element e = (Element) root;
            if (e.isRepetable()) {
                return e;
            }
            for (HL7TreeNode child : e.getElementChildren()) {
                HL7TreeNode loopNode = getLoopNode(child);
                if (loopNode != null) {
                    return loopNode;
                }
            }
        }
        return null;
    }

    // public void initNodeOrder(HL7TreeNode node) {
    // if (node == null) {
    // return;
    // }
    // HL7TreeNode parent = node.getParent();
    // if (parent == null) {
    // node.setOrder(1);
    // String path = TreeUtil.getPath(node);
    // orderMap.put(path, order);
    // order++;
    // }
    // List<HL7TreeNode> childNode = node.getChildren();
    // for (HL7TreeNode child : childNode) {
    // child.setOrder(order);
    // String path = TreeUtil.getPath(child);
    // orderMap.put(path, order);
    // order++;
    // if (child.getChildren().size() > 0) {
    // initNodeOrder(child);
    // }
    // }
    //
    // }

    protected void tableLoader(Element element, String parentPath, List<Map<String, String>> table, String defaultValue) {
        Map<String, String> newMap = new HashMap<String, String>();
        String currentPath = parentPath + "/" + element.getLabel(); //$NON-NLS-1$
        newMap.put(HL7InputComponent.PATH, currentPath);
        newMap.put(HL7InputComponent.COLUMN, element.getColumnLabel());
        newMap.put(HL7InputComponent.ATTRIBUTE, element.isMain() ? "main" : "branch");
        newMap.put(HL7InputComponent.VALUE, defaultValue);
        newMap.put(HL7InputComponent.ORDER, String.valueOf(getNodeOrder(element)));
        newMap.put("REPEATABLE", String.valueOf(element.isRepetable()));

        table.add(newMap);
        for (HL7TreeNode att : element.getAttributeChildren()) {
            newMap = new HashMap<String, String>();
            newMap.put(HL7InputComponent.PATH, att.getLabel());
            newMap.put(HL7InputComponent.COLUMN, att.getColumnLabel());
            newMap.put(HL7InputComponent.ATTRIBUTE, "attri"); //$NON-NLS-1$
            newMap.put(HL7InputComponent.VALUE, att.getDefaultValue());
            newMap.put(HL7InputComponent.ORDER, String.valueOf(getNodeOrder(att)));
            newMap.put("REPEATABLE", String.valueOf(att.isRepetable()));
            table.add(newMap);
        }
        for (HL7TreeNode att : element.getNameSpaceChildren()) {
            newMap = new HashMap<String, String>();
            newMap.put(HL7InputComponent.PATH, att.getLabel());
            newMap.put(HL7InputComponent.COLUMN, att.getColumnLabel());
            newMap.put(HL7InputComponent.ATTRIBUTE, "ns"); //$NON-NLS-1$
            newMap.put(HL7InputComponent.VALUE, att.getDefaultValue());
            newMap.put("REPEATABLE", String.valueOf(att.isRepetable()));
            newMap.put(HL7InputComponent.ORDER, String.valueOf(getNodeOrder(att)));
            table.add(newMap);
        }
        List<HL7TreeNode> children = element.getElementChildren();
        for (HL7TreeNode child : children) {
            // if (!child.isGroup() && !child.isRepetable()) {// && !child.isRepetable()
            tableLoader((Element) child, currentPath, table, child.getDefaultValue());
            // }
        }
    }

    public void orderNode(HL7TreeNode node) {
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

    public void sortOrder(HL7TreeNode treeNode, HL7TreeNode node) {
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

    public boolean isRepetable() {
        return false;
    }

    public HL7TreeNode getRootHL7TreeNode(HL7TreeNode node) {
        if (node != null) {
            HL7TreeNode parent = node.getParent();
            if (parent == null) {
                return node;
            }
            return getRootHL7TreeNode(parent);
        }
        return null;
    }

    @Override
    public List<HL7TreeNode> getTreeData(String curSchema) {
        if (currentSchema == null) {
            if (treeData == null) {
                return new ArrayList<HL7TreeNode>();
            }
            return treeData;
        } else {
            if (!contents.containsKey(curSchema)) {
                return new ArrayList<HL7TreeNode>();
            }
            return contents.get(curSchema);
        }

    }

    public List<HL7TreeNode> getTreeData() {
        if (currentSchema == null) {
            return treeData;
        } else {
            return getOriginalNodes();
        }
    }

    /**
     * Sets the treeData.
     * 
     * @param treeData the treeData to set
     */
    public void setTreeData(List<HL7TreeNode> treeData) {
        this.treeData = treeData;
        contents.put(currentSchema, treeData);
    }

    /**
     * 
     * DOC hwang Comment method "getOriginalNodes".
     * 
     * @return
     */
    protected List<HL7TreeNode> getOriginalNodes() {
        List<HL7TreeNode> tmpTreeData = new ArrayList<HL7TreeNode>();
        Set<Entry<String, List<HL7TreeNode>>> set = contents.entrySet();
        Iterator<Entry<String, List<HL7TreeNode>>> iterator = set.iterator();
        while (iterator.hasNext()) {
            Entry<String, List<HL7TreeNode>> entry = iterator.next();
            List<HL7TreeNode> list = entry.getValue();
            tmpTreeData.addAll(list);
        }
        // List<? extends IConnection> incomingConnections = NodeUtil.getIncomingConnections(getHl7Component(),
        // IConnectionCategory.FLOW);
        // if (incomingConnections.size() > 0) {
        // for (IConnection conn : incomingConnections) {
        // String uniqueName = conn.getUniqueName();
        // List<HL7TreeNode> list = contents.get(uniqueName);
        // tmpTreeData.addAll(list);
        // }
        // }
        return tmpTreeData;
    }

    /**
     * Sets the currentSchema.
     * 
     * @param currentSchema the currentSchema to set
     */
    public void setCurrentSchema(String currentSchema) {
        this.currentSchema = currentSchema;
        List<Map<String, String>> maps = (List<Map<String, String>>) ElementParameterParser.getObjectValue(hl7Component,
                "__SCHEMAS__"); //$NON-NLS-1$
        for (Map<String, String> map : maps) {
            if (map.containsValue(currentSchema)) {
                if (map.get("PARENT_ROW") != null && map.get("PARENT_ROW").equals(currentSchema)) {
                    String schemaName = map.get("SCHEMA");
                    int first = schemaName.indexOf("_");
                    int second = schemaName.lastIndexOf("_");
                    if (first > 0 && first < second) {
                        schemaName = schemaName.substring(first + 1, second);
                    }
                    schemaMap.put(currentSchema, schemaName);
                    break;
                }

            }
        }
    }

    @Override
    public String getCurrentSchema(boolean sign) {
        if (sign && schemaMap.get(currentSchema) != null && !"".equals(schemaMap.get(currentSchema))) {
            return schemaMap.get(currentSchema);
        }
        return this.currentSchema;
    }

    public String initCurrentSchema() {
        List<? extends IConnection> incomingConnections = NodeUtil.getIncomingConnections(hl7Component, IConnectionCategory.FLOW);
        if (incomingConnections.size() <= 0) {
            return this.currentSchema;
        }
        List<String> connNameList = new ArrayList<String>();
        for (IConnection connection : incomingConnections) {
            connNameList.add(connection.getUniqueName());
        }
        // if (connNameList.contains(this.currentSchema)) {
        // return this.currentSchema;
        // }
        this.currentSchema = connNameList.get(0);
        return this.currentSchema;
    }

    protected HL7TreeNode addElement(HL7TreeNode current, String currentPath, String newPath, String defaultValue) {
        String name = newPath.substring(newPath.lastIndexOf("/") + 1); //$NON-NLS-1$
        String parentPath = "";
        if (newPath.contains("/")) {
            parentPath = newPath.substring(0, newPath.lastIndexOf("/")); //$NON-NLS-1$
        }
        //        String parentPath = newPath.substring(0, newPath.lastIndexOf("/")); //$NON-NLS-1$
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

            if (parent != null) {
                parent.addChild(temp);
            }
        }

        return temp;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.hl7.managers.HL7Manager#saveDataToComponent()
     */
    @Override
    public boolean saveDataToComponent() {
        boolean result = false;
        List<Map<String, String>> root = new ArrayList<Map<String, String>>();

        root.addAll(getRootTable());

        if (hl7Component.setTableElementParameter(root, HL7InputComponent.ROOT)) {
            result = true;
        }
        return result;
    }

    public void initNodeOrder(HL7TreeNode node) {
        if (node == null) {
            return;
        }
        HL7TreeNode parent = node.getParent();
        if (parent == null) {
            node.setOrder(1);
            String path = getPath(node);
            orderMap.put(path, order);
            order++;
        }
        List<HL7TreeNode> childNode = node.getChildren();
        for (HL7TreeNode child : childNode) {
            child.setOrder(order);
            String path = getPath(child);
            orderMap.put(path, order);
            order++;
            if (child.getChildren().size() > 0) {
                initNodeOrder(child);
            }
        }
    }

    public int getNodeOrder(HL7TreeNode node) {
        if (node != null) {
            String path = getPath(node);
            return orderMap.get(path);
        }
        return 0;
    }

    public Map<String, List<HL7TreeNode>> getContents() {
        return this.contents;
    }

    @Override
    public List<IMetadataColumn> getSchemaData(String currentSchema) {
        List<? extends IConnection> incomingConnections = NodeUtil.getIncomingConnections(hl7Component, IConnectionCategory.FLOW);
        for (IConnection connection : incomingConnections) {
            IMetadataTable metadataTable = connection.getMetadataTable();
            if (metadataTable.getLabel() != null && currentSchema != null && metadataTable.getLabel().equals(currentSchema)) {
                return metadataTable.getListColumns();
            }
        }
        return new ArrayList<IMetadataColumn>();
    }

}
