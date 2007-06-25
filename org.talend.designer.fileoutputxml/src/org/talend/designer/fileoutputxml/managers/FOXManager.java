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
package org.talend.designer.fileoutputxml.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.designer.fileoutputxml.FileOutputXMLComponent;
import org.talend.designer.fileoutputxml.data.Attribute;
import org.talend.designer.fileoutputxml.data.Element;
import org.talend.designer.fileoutputxml.data.FOXTreeNode;
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

        treeData = new ArrayList<FOXTreeNode>();
        FOXTreeNode rootNode = null;
        FOXTreeNode current = null;
        FOXTreeNode temp = null;
        List<Map<String, String>> rootTags = (List<Map<String, String>>) foxComponent
                .getTableList(FileOutputXMLComponent.ROOT_TAGS);
        for (Map<String, String> rootLabel : rootTags) {

            temp = new Element(rootLabel.get(FileOutputXMLComponent.TAG));
            if (rootNode == null) {
                current = temp;
                rootNode = current;
            } else {
                current.addChild(temp);
                current = temp;
            }
        }
        List<Map<String, String>> mapping = (List<Map<String, String>>) foxComponent
                .getTableList(FileOutputXMLComponent.MAPPING);
        int depth = 0;
        List<IMetadataColumn> columns = null;
        if (foxComponent.getMetadataTable() != null) {
            columns = foxComponent.getMetadataTable().getListColumns();
        }
        if (columns == null) {
            columns = new ArrayList<IMetadataColumn>();
        }
        for (Map<String, String> map : mapping) {
            String columnLabel = map.get(FileOutputXMLComponent.COLUMN);
            IMetadataColumn column = null;
            for (IMetadataColumn tc : columns) {
                if (tc.getLabel().equals(columnLabel)) {
                    column = tc;
                    break;
                }
            }

            String att = map.get(FileOutputXMLComponent.ATTRIBUTE);
            if (Boolean.parseBoolean(att)) {
                FOXTreeNode attNode = new Attribute(map.get(FileOutputXMLComponent.LABEL));
                attNode.setColumn(column);
                current.addChild(attNode);
            } else {
                int curDepth = Integer.parseInt(map.get(FileOutputXMLComponent.DEPTH));
                if (curDepth == 1) {
                    FOXTreeNode tag = new Element(map.get(FileOutputXMLComponent.LABEL));
                    tag.setColumn(column);
                    tag.setLoop(true); // this node is tagged as loop
                    current.addChild(tag);
                    current = tag;
                    depth = curDepth;
                } else {
                    FOXTreeNode father = current;
                    while (depth >= curDepth) {
                        father = father.getParent();
                        depth--;
                    }
                    FOXTreeNode tag = new Element(map.get(FileOutputXMLComponent.LABEL));
                    tag.setColumn(column);
                    father.addChild(tag);
                    current = tag;
                    depth = curDepth;
                }
            }
        }
        if (rootNode == null) {
            rootNode = new Element("rootTag"); //$NON-NLS-1$
        }

        rootNode.setParent(null);

        treeData.add(rootNode);

    }

    private void preTraversingTree(Element node, List<Map<String, String>> result, int depth) {
        Map<String, String> newMap = new HashMap<String, String>();
        newMap.put(FileOutputXMLComponent.LABEL, node.getLabel());
        newMap.put(FileOutputXMLComponent.COLUMN, node.getColumnLabel());
        newMap.put(FileOutputXMLComponent.ATTRIBUTE, "false"); //$NON-NLS-1$
        newMap.put(FileOutputXMLComponent.DEPTH, depth + ""); //$NON-NLS-1$
        result.add(newMap);
        for (FOXTreeNode att : node.getAttributeChildren()) {
            newMap = new HashMap<String, String>();
            newMap.put(FileOutputXMLComponent.LABEL, att.getLabel());
            newMap.put(FileOutputXMLComponent.COLUMN, att.getColumnLabel());
            newMap.put(FileOutputXMLComponent.ATTRIBUTE, "true"); //$NON-NLS-1$
            newMap.put(FileOutputXMLComponent.DEPTH, depth + ""); //$NON-NLS-1$
            result.add(newMap);
        }
        List<FOXTreeNode> children = node.getElementChildren();
        for (FOXTreeNode child : children) {
            preTraversingTree((Element) child, result, depth + 1);
        }
    }

    public List<Map<String, String>> getMapping() {
        List<Map<String, String>> result = new ArrayList<Map<String, String>>();
        Element loopNode = (Element) TreeUtil.getLoopNode(this.treeData.get(0));
        if (loopNode != null) {
            preTraversingTree(loopNode, result, 1);
        }
        return result;
    }

    public List<Map<String, String>> getRootTags() {
        List<Map<String, String>> result = new ArrayList<Map<String, String>>();

        FOXTreeNode loopNode = treeData.get(0);

        Element node = (Element) loopNode;

        Map<String, String> map = new HashMap<String, String>();

        map.put(FileOutputXMLComponent.TAG, node.getLabel());

        result.add(map);

        if (node.getElementChildren().size() == 0) {
            return result;
        }
        do {

            node = (Element) node.getElementChildren().get(0);

            if (node.isLoop()) {
                return result;
            }

            map = new HashMap<String, String>();

            map.put(FileOutputXMLComponent.TAG, node.getLabel());

            result.add(map);

            if (node.getElementChildren().size() == 0) {
                return result;
            }
        } while (true);
    }

    public boolean saveDataToComponent() {
        boolean result = false;
        if (foxComponent.setTableElementParameter(getRootTags(), FileOutputXMLComponent.ROOT_TAGS)) {
            result = true;
        }
        if (foxComponent.setTableElementParameter(getMapping(), FileOutputXMLComponent.MAPPING)) {
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
}
