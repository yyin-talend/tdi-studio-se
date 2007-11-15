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
package org.talend.designer.core.ui.editor.nodecontainer;

import java.util.ArrayList;
import java.util.List;

import org.talend.core.model.process.Element;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.nodes.NodeLabel;
import org.talend.designer.core.ui.editor.nodes.NodePerformance;

/**
 * This element will contain all elements that will be linked to a node.
 * 
 * $Id$
 * 
 */
public class NodeContainer extends Element {

    private Node node;

    private NodeLabel nodeLabel;

    protected List<Element> elements = new ArrayList<Element>();

    private NodePerformance nodePerformance;
    
    public NodeContainer(Node node) {
        this.node = node;
        node.setNodeContainer(this);
        this.nodeLabel = node.getNodeLabel();
        elements.add(nodeLabel);
        nodePerformance = new NodePerformance(this);
        elements.add(nodePerformance);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.Element#getElementName()
     */
    @Override
    public String getElementName() {
        return node.getUniqueName();
    }

    public Node getNode() {
        return this.node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public NodeLabel getNodeLabel() {
        return this.nodeLabel;
    }

    public void setNodeLabel(NodeLabel nodeLabel) {
        this.nodeLabel = nodeLabel;
    }

    public List getElements() {
        return elements;
    }

    public NodePerformance getNodePerformance() {
        return this.nodePerformance;
    }

    public boolean isReadOnly() {
        return node.getProcess().isReadOnly();
    }

    public void setReadOnly(boolean readOnly) {
    }
}
