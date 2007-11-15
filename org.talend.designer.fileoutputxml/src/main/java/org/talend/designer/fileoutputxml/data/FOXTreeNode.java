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
package org.talend.designer.fileoutputxml.data;

import java.util.ArrayList;
import java.util.List;

import org.talend.core.model.metadata.IMetadataColumn;

/**
 * DOC bqian class global comment. Detailled comment <br/>
 * 
 * $Id: FOXTreeNode.java,v 1.1 2007/06/12 07:20:39 gke Exp $
 * 
 */
public abstract class FOXTreeNode {

    private IMetadataColumn column = null;

    private String label;

    private List<FOXTreeNode> children = null;

    private FOXTreeNode parent = null;

    private boolean isLoopNode = false;

    private boolean isGroupNode = false;

    public boolean hasLink() {
        return column != null;
    }

    /**
     * FOXTreeNode constructor comment.
     */
    public FOXTreeNode() {
        children = new ArrayList<FOXTreeNode>();
    }

    /**
     * FOXTreeNode constructor comment.
     */
    public FOXTreeNode(String label) {
        children = new ArrayList<FOXTreeNode>();
        this.label = label;
    }

    /**
     * Getter for children.
     * 
     * @return the children
     */
    public List<FOXTreeNode> getChildren() {
        return this.children;
    }

    /**
     * Getter for parent.
     * 
     * @return the parent
     */
    public FOXTreeNode getParent() {
        return this.parent;
    }

    /**
     * Sets the parent.
     * 
     * @param parent the parent to set
     */
    public void setParent(FOXTreeNode parent) {
        this.parent = parent;
    }

    /**
     * DOC ke Comment method "getColumnLabel".
     * 
     * @return
     */
    public String getColumnLabel() {
        if (column == null) {
            return ""; //$NON-NLS-1$
        } else {
            return this.column.getLabel();
        }
    }

    /**
     * Getter for schema.
     * 
     * @return the schema
     */
    public IMetadataColumn getColumn() {
        return column;
    }

    /**
     * Sets the schema.
     * 
     * @param schema the schema to set
     */
    public void setColumn(IMetadataColumn column) {
        this.column = column;
    }

    /**
     * Getter for value.
     * 
     * @return the value
     */
    public String getLabel() {
        return this.label;
    }

    /**
     * Getter for value.
     * 
     * @return the value
     */
    public String getLabelForViewer() {
        return this.label;
    }

    /**
     * Sets the value.
     * 
     * @param value the value to set
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * DOC ke Comment method "addChild".
     * 
     * @param child
     */
    public void addChild(FOXTreeNode child) {
        children.add(child);
        child.setParent(this);

    }

    /**
     * DOC ke Comment method "removeChild".
     * 
     * @param child
     */
    public void removeChild(FOXTreeNode child) {
        children.remove(child);
        child.setParent(null);
    }

    /**
     * DOC ke Comment method "hasChildren".
     * 
     * @return
     */
    public boolean hasChildren() {
        return children.size() > 0;
    }

    /**
     * DOC ke Comment method "isLoop".
     * 
     * @return
     */
    public boolean isLoop() {
        return this.isLoopNode;
    }

    /**
     * DOC ke Comment method "setLoop".
     * 
     * @param b
     */
    public void setLoop(boolean b) {
        this.isLoopNode = b;
    }

    /**
     * DOC ke Comment method "isGroup".
     * 
     * @return
     */
    public boolean isGroup() {
        return this.isGroupNode;
    }

    /**
     * DOC ke Comment method "setGroup".
     * 
     * @param b
     */
    public void setGroup(boolean b) {
        this.isGroupNode = b;
    }

}
