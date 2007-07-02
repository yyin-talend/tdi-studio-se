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
            return "";
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

}
