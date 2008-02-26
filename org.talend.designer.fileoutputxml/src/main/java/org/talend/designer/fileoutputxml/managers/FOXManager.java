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
package org.talend.designer.fileoutputxml.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.talend.core.model.metadata.IMetadataColumn;
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
        FOXTreeNode mainNode = null;
        String mainPath = null;
        String currentPath = null;

        // build root tree
        List<Map<String, String>> rootTable = (List<Map<String, String>>) foxComponent.getTableList(FileOutputXMLComponent.ROOT);
        for (Map<String, String> rootMap : rootTable) {
            String newPath = rootMap.get(FileOutputXMLComponent.PATH);
            if (rootMap.get(FileOutputXMLComponent.ATTRIBUTE).equals("attri")) {
                temp = new Attribute(newPath);
                current.addChild(temp);
            } else if (rootMap.get(FileOutputXMLComponent.ATTRIBUTE).equals("ns")) {
                temp = new NameSpaceNode(newPath);
                current.addChild(temp);
            } else {
                temp = this.addElement(current, currentPath, newPath);
                if (rootNode == null) {
                    rootNode = temp;
                }
                if (rootMap.get(FileOutputXMLComponent.ATTRIBUTE).equals("main")) {
                    temp.setMain(true);
                    mainNode = temp;
                    mainPath = newPath;
                }
                current = temp;
                currentPath = newPath;
            }
            String columnName = rootMap.get(FileOutputXMLComponent.COLUMN);
            if (columnName != null && columnName.length() > 0) {
                temp.setColumn(metadataTable.getColumn(columnName));
            }
        }

        // build group tree
        current = mainNode;
        currentPath = mainPath;
        boolean isFirst = true;
        List<Map<String, String>> groupTable = (List<Map<String, String>>) foxComponent
                .getTableList(FileOutputXMLComponent.GROUP);
        for (Map<String, String> groupMap : groupTable) {
            String newPath = groupMap.get(FileOutputXMLComponent.PATH);
            if (groupMap.get(FileOutputXMLComponent.ATTRIBUTE).equals("attri")) {
                temp = new Attribute(newPath);
                current.addChild(temp);
            } else if (groupMap.get(FileOutputXMLComponent.ATTRIBUTE).equals("ns")) {
                temp = new NameSpaceNode(newPath);
                current.addChild(temp);
            } else {
                temp = this.addElement(current, currentPath, newPath);
                if (groupMap.get(FileOutputXMLComponent.ATTRIBUTE).equals("main")) {
                    temp.setMain(true);
                    mainNode = temp;
                    mainPath = newPath;
                }
                if (isFirst) {
                    temp.setGroup(true);
                    isFirst = false;
                }
                current = temp;
                currentPath = newPath;
            }
            String columnName = groupMap.get(FileOutputXMLComponent.COLUMN);
            if (columnName != null && columnName.length() > 0) {
                temp.setColumn(metadataTable.getColumn(columnName));
            }
        }

        // build loop tree
        current = mainNode;
        currentPath = mainPath;
        isFirst = true;
        List<Map<String, String>> loopTable = (List<Map<String, String>>) foxComponent.getTableList(FileOutputXMLComponent.LOOP);
        for (Map<String, String> loopMap : loopTable) {
            String newPath = loopMap.get(FileOutputXMLComponent.PATH);
            if (loopMap.get(FileOutputXMLComponent.ATTRIBUTE).equals("attri")) {
                temp = new Attribute(newPath);
                current.addChild(temp);
            } else if (loopMap.get(FileOutputXMLComponent.ATTRIBUTE).equals("ns")) {
                temp = new NameSpaceNode(newPath);
                current.addChild(temp);
            } else {
                temp = this.addElement(current, currentPath, newPath);
                if (loopMap.get(FileOutputXMLComponent.ATTRIBUTE).equals("main")) {
                    temp.setMain(true);
                }
                if (isFirst) {
                    temp.setLoop(true);
                    isFirst = false;
                }
                current = temp;
                currentPath = newPath;
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
        newMap.put(FileOutputXMLComponent.ATTRIBUTE, element.isMain() ? "main" : "branch"); //$NON-NLS-1$
        newMap.put(FileOutputXMLComponent.VALUE, ""); //$NON-NLS-1$
        table.add(newMap);
        for (FOXTreeNode att : element.getAttributeChildren()) {
            newMap = new HashMap<String, String>();
            newMap.put(FileOutputXMLComponent.PATH, att.getLabel());
            newMap.put(FileOutputXMLComponent.COLUMN, att.getColumnLabel());
            newMap.put(FileOutputXMLComponent.ATTRIBUTE, "attri"); //$NON-NLS-1$
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

    public List<IMetadataColumn> getSchemaData() {
        return foxComponent.getMetadataTable().getListColumns();
    }

    public void setTreeData(List<FOXTreeNode> treeData) {
        this.treeData = treeData;
    }

    private FOXTreeNode addElement(FOXTreeNode current, String currentPath, String newPath) {
        String name = newPath.substring(newPath.lastIndexOf("/") + 1);
        String parentPath = newPath.substring(0, newPath.lastIndexOf("/"));
        FOXTreeNode temp = new Element(name);

        if (current == null) {// root node
            return temp;
        }

        if (currentPath.equals(parentPath)) {
            current.addChild(temp);
        } else {
            String[] nods = currentPath.split("/");
            String[] newNods = parentPath.split("/");
            int parentLevel = 0;
            int checkLength = nods.length < newNods.length ? nods.length : newNods.length;
            for (int i = 1; i < checkLength; i++) {
                if (nods[i].equals(newNods[i])) {
                    parentLevel = i;
                }
            }
            FOXTreeNode parent = current;
            for (int i = 0; i < nods.length - (parentLevel + 1); i++) {
                parent = parent.getParent();
            }
            parent.addChild(temp);
        }

        return temp;
    }
}
