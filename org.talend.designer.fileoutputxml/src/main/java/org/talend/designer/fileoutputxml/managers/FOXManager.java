// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the  agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//   
// ============================================================================
package org.talend.designer.fileoutputxml.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.MetadataTable;
import org.talend.designer.fileoutputxml.FileOutputXMLComponent;
import org.talend.designer.fileoutputxml.data.Attribute;
import org.talend.designer.fileoutputxml.data.Element;
import org.talend.designer.fileoutputxml.data.FOXTreeNode;
import org.talend.designer.fileoutputxml.data.NameSpaceNode;
import org.talend.designer.fileoutputxml.util.TreeUtil;

/**
 * DOC ke class global comment. Detailled comment <br/>
 * 
 */
public class FOXManager {

    private FileOutputXMLComponent foxComponent;

    private UIManager uiManager;

    private List<FOXTreeNode> treeData;

    /**
     * constructor.
     */
    public FOXManager(FileOutputXMLComponent foxComponent) {
        this.foxComponent = foxComponent;
        this.uiManager = new UIManager(this);
        initModel();
    }

    /**
     * Getter for k.
     * 
     * @return the foxComponent
     */
    public FileOutputXMLComponent getFoxComponent() {
        return this.foxComponent;
    }

    /**
     * Sets the foxComponent.
     * 
     * @param foxComponent the foxComponent to set
     */
    public void setFoxComponent(FileOutputXMLComponent foxComponent) {
        this.foxComponent = foxComponent;
    }

    /**
     * Getter for uiManager.
     * 
     * @return the uiManager
     */
    public UIManager getUiManager() {
        return this.uiManager;
    }

    /**
     * Sets the uiManager.
     * 
     * @param uiManager the uiManager to set
     */
    public void setUiManager(UIManager uiManager) {
        this.uiManager = uiManager;
    }

    public void initModel() {

        IMetadataTable metadataTable = foxComponent.getMetadataTable();
        if (metadataTable == null) {
            metadataTable = new MetadataTable();
        }

        treeData = new ArrayList<FOXTreeNode>();
        FOXTreeNode rootNode = null;
        FOXTreeNode current = null;
        FOXTreeNode temp = null;

        // build root tree
        List<Map<String, String>> rootTable = (List<Map<String, String>>) foxComponent
                .getTableList(FileOutputXMLComponent.ROOT);
        for (Map<String, String> rootMap : rootTable) {
            String path = rootMap.get(FileOutputXMLComponent.PATH);
            if (rootMap.get(FileOutputXMLComponent.ATTRIBUTE).equals("true")) {
                temp = new Attribute(path);
                current.addChild(temp);
            } else if (rootMap.get(FileOutputXMLComponent.ATTRIBUTE).equals("ns")) {
                temp = new NameSpaceNode(path);
                current.addChild(temp);
            } else {
                String name = path.substring(path.lastIndexOf("/") + 1);
                String parentPath = path.substring(0, path.lastIndexOf("/"));
                temp = new Element(name);
                if (rootNode == null) {
                    rootNode = temp;
                } else {
                    String parentName = parentPath.substring(parentPath.lastIndexOf("/") + 1);
                    while (!current.getLabel().equals(parentName)) {
                        current = current.getParent();
                    }
                    current.addChild(temp);
                }
                current = temp;
            }
            String columnName = rootMap.get(FileOutputXMLComponent.COLUMN);
            if (columnName != null && columnName.length() > 0) {
                temp.setColumn(metadataTable.getColumn(columnName));
            }
        }

        // build group tree
        current = null;
        List<Map<String, String>> groupTable = (List<Map<String, String>>) foxComponent
                .getTableList(FileOutputXMLComponent.GROUP);
        for (Map<String, String> groupMap : groupTable) {
            String path = groupMap.get(FileOutputXMLComponent.PATH);
            if (groupMap.get(FileOutputXMLComponent.ATTRIBUTE).equals("true")) {
                temp = new Attribute(path);
                current.addChild(temp);
            } else if (groupMap.get(FileOutputXMLComponent.ATTRIBUTE).equals("ns")) {
                temp = new NameSpaceNode(path);
                current.addChild(temp);
            } else {
                String name = path.substring(path.lastIndexOf("/") + 1);
                String parentPath = path.substring(0, path.lastIndexOf("/"));
                temp = new Element(name);
                if (current == null) {
                    temp.setGroup(true);
                    FOXTreeNode parent = this.selectNode(parentPath, rootNode);
                    parent.addChild(temp);
                } else {
                    String parentName = parentPath.substring(parentPath.lastIndexOf("/") + 1);
                    while (!current.getLabel().equals(parentName)) {
                        current = current.getParent();
                    }
                    current.addChild(temp);
                }
                current = temp;
            }
            String columnName = groupMap.get(FileOutputXMLComponent.COLUMN);
            if (columnName != null && columnName.length() > 0) {
                temp.setColumn(metadataTable.getColumn(columnName));
            }
        }

        // build loop tree
        current = null;
        List<Map<String, String>> loopTable = (List<Map<String, String>>) foxComponent
                .getTableList(FileOutputXMLComponent.LOOP);
        for (Map<String, String> loopMap : loopTable) {
            String path = loopMap.get(FileOutputXMLComponent.PATH);
            if (loopMap.get(FileOutputXMLComponent.ATTRIBUTE).equals("true")) {
                temp = new Attribute(path);
                current.addChild(temp);
            } else if (loopMap.get(FileOutputXMLComponent.ATTRIBUTE).equals("ns")) {
                temp = new NameSpaceNode(path);
                current.addChild(temp);
            } else {
                String name = path.substring(path.lastIndexOf("/") + 1);
                String parentPath = path.substring(0, path.lastIndexOf("/"));
                temp = new Element(name);
                if (current == null) {
                    temp.setLoop(true);
                    FOXTreeNode parent = this.selectNode(parentPath, rootNode);
                    parent.addChild(temp);
                } else {
                    String parentName = parentPath.substring(parentPath.lastIndexOf("/") + 1);
                    while (!current.getLabel().equals(parentName)) {
                        current = current.getParent();
                    }
                    current.addChild(temp);
                }
                current = temp;
            }
            String columnName = loopMap.get(FileOutputXMLComponent.COLUMN);
            if (columnName != null && columnName.length() > 0) {
                temp.setColumn(metadataTable.getColumn(columnName));
            }
        }

        if (rootNode == null) {
            rootNode = new Element("rootTag"); //$NON-NLS-1$
        }

        rootNode.setParent(null);

        treeData.add(rootNode);

    }

    public List<Map<String, String>> getLoopTable() {
        List<Map<String, String>> result = new ArrayList<Map<String, String>>();
        Element loopNode = (Element) TreeUtil.getLoopNode(this.treeData.get(0));
        if (loopNode != null) {
            String path = TreeUtil.getPath(loopNode);
            tableLoader(loopNode, path.substring(0, path.lastIndexOf("/")), result);
        }
        return result;
    }

    public List<Map<String, String>> getGroupTable() {
        List<Map<String, String>> result = new ArrayList<Map<String, String>>();
        Element groupNode = (Element) TreeUtil.getGroupNode(this.treeData.get(0));
        if (groupNode != null) {
            String path = TreeUtil.getPath(groupNode);
            tableLoader(groupNode, path.substring(0, path.lastIndexOf("/")), result);
        }
        return result;
    }

    public List<Map<String, String>> getRootTable() {
        List<Map<String, String>> result = new ArrayList<Map<String, String>>();
        FOXTreeNode rootNode = treeData.get(0);
        if (rootNode != null) {
            this.tableLoader((Element) rootNode, "", result);
        }
        return result;
    }

    private void tableLoader(Element element, String parentPath, List<Map<String, String>> table) {
        Map<String, String> newMap = new HashMap<String, String>();
        String currentPath = parentPath + "/" + element.getLabel();
        newMap.put(FileOutputXMLComponent.PATH, currentPath);
        newMap.put(FileOutputXMLComponent.COLUMN, element.getColumnLabel());
        newMap.put(FileOutputXMLComponent.ATTRIBUTE, "false"); //$NON-NLS-1$
        newMap.put(FileOutputXMLComponent.VALUE, ""); //$NON-NLS-1$
        table.add(newMap);
        for (FOXTreeNode att : element.getAttributeChildren()) {
            newMap = new HashMap<String, String>();
            newMap.put(FileOutputXMLComponent.PATH, att.getLabel());
            newMap.put(FileOutputXMLComponent.COLUMN, att.getColumnLabel());
            newMap.put(FileOutputXMLComponent.ATTRIBUTE, "true"); //$NON-NLS-1$
            newMap.put(FileOutputXMLComponent.VALUE, ""); //$NON-NLS-1$
            table.add(newMap);
        }
        for (FOXTreeNode att : element.getNameSpaceChildren()) {
            newMap = new HashMap<String, String>();
            newMap.put(FileOutputXMLComponent.PATH, att.getLabel());
            newMap.put(FileOutputXMLComponent.COLUMN, att.getColumnLabel());
            newMap.put(FileOutputXMLComponent.ATTRIBUTE, "ns"); //$NON-NLS-1$
            newMap.put(FileOutputXMLComponent.VALUE, ""); //$NON-NLS-1$
            table.add(newMap);
        }
        List<FOXTreeNode> children = element.getElementChildren();
        for (FOXTreeNode child : children) {
            if (!child.isGroup() && !child.isLoop()) {
                tableLoader((Element) child, currentPath, table);
            }
        }
    }

    public boolean saveDataToComponent() {
        boolean result = false;
        if (foxComponent.setTableElementParameter(getRootTable(), FileOutputXMLComponent.ROOT)) {
            result = true;
        }
        if (foxComponent.setTableElementParameter(getLoopTable(), FileOutputXMLComponent.LOOP)) {
            result = true;
        }
        if (foxComponent.setTableElementParameter(getGroupTable(), FileOutputXMLComponent.GROUP)) {
            result = true;
        }
        return result;
    }

    public List<FOXTreeNode> getTreeData() {
        return treeData;
    }

    public void setTreeData(List<FOXTreeNode> treeData) {
        this.treeData = treeData;
    }

    // get the node by the path.
    private FOXTreeNode selectNode(String path, FOXTreeNode root) {
        // example path = "/root/a/aa", so nodes[0]==""
        String[] nodes = path.split("/");
        if (root.getLabel().equals(nodes[1])) {
            FOXTreeNode result = root;
            for (int i = 2; i < nodes.length; i++) {
                List<FOXTreeNode> list = result.getChildren();
                FOXTreeNode tmp = null;
                for (FOXTreeNode node : list) {
                    if (node.getLabel().equals(nodes[i])) {
                        tmp = node;
                        result = node;
                        break;
                    }
                }
                if (tmp == null) {
                    return null;
                }
            }
            return result;
        }

        return null;
    }
}
