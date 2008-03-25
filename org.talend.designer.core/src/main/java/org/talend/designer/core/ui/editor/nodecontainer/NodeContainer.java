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

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Image;
import org.talend.commons.ui.image.EImage;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.core.CorePlugin;
import org.talend.core.model.process.Element;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.nodes.NodeLabel;
import org.talend.designer.core.ui.editor.nodes.NodePerformance;
import org.talend.designer.core.ui.editor.subjobcontainer.SubjobContainer;

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

    private Point breakpointLocation = new Point();

    private Point warningLocation = new Point();

    private Point errorLocation = new Point();

    private SubjobContainer subjobContainer;

    private Dimension breakpointSize;

    private Dimension errorSize;

    private Dimension warningSize;

    public NodeContainer(Node node) {
        this.node = node;
        node.setNodeContainer(this);
        this.nodeLabel = node.getNodeLabel();
        elements.add(node);
        elements.add(nodeLabel);
        nodePerformance = new NodePerformance(this);
        elements.add(nodePerformance);

        if (!CorePlugin.getContext().isHeadless()) {
            Image image = ImageProvider.getImage(CorePlugin.getImageDescriptor(NodeContainerFigure.BREAKPOINT_IMAGE));
            breakpointSize = new Dimension(image.getImageData().width, image.getImageData().height);
            image = ImageProvider.getImage(EImage.ERROR_SMALL);
            errorSize = new Dimension(image.getImageData().width, image.getImageData().height);
            image = ImageProvider.getImage(EImage.WARNING_SMALL);
            warningSize = new Dimension(image.getImageData().width, image.getImageData().height);
        }
    }

    private Rectangle prepareStatus(Point nodeLocation, Dimension nodeSize) {
        Rectangle statusRectangle = new Rectangle();
        Rectangle breakpointRectangle, warningRectangle, errorRectangle;

        breakpointLocation.x = nodeLocation.x - breakpointSize.width;
        breakpointLocation.y = nodeLocation.y - breakpointSize.height;
        breakpointRectangle = new Rectangle(breakpointLocation, breakpointSize);
        statusRectangle = breakpointRectangle;

        errorLocation.x = nodeLocation.x + nodeSize.width;
        errorLocation.y = nodeLocation.y - errorSize.height;
        errorRectangle = new Rectangle(errorLocation, errorSize);
        statusRectangle.union(errorRectangle);

        warningLocation.x = nodeLocation.x + nodeSize.width;
        warningLocation.y = nodeLocation.y - warningSize.height;
        warningRectangle = new Rectangle(warningLocation, warningSize);
        statusRectangle.union(warningRectangle);

        return statusRectangle;
    }

    public Rectangle getNodeContainerRectangle() {
        Point nodeLocation;
        nodeLocation = node.getLocation();

        Dimension nodeSize;
        Dimension labelSize;
        nodeSize = node.getSize();
        Rectangle nodeRectangle = new Rectangle(nodeLocation, nodeSize);

        Rectangle statusRectangle = prepareStatus(nodeLocation, nodeSize);

        Point labelLocation = nodeLabel.getLocation().getCopy();
        Point offset = nodeLabel.getOffset();
        Point textOffset = new Point();
        labelSize = nodeLabel.getLabelSize();
        textOffset.y = nodeSize.height;
        textOffset.x = (nodeSize.width - labelSize.width) / 2;
        nodeLabel.setTextOffset(textOffset);
        labelLocation.translate(offset);
        labelLocation.translate(textOffset);
        Rectangle labelRectangle = new Rectangle(labelLocation, labelSize);

        Dimension perfSize = nodePerformance.getSize();
        Point perfLocation = nodePerformance.getLocation();

        Rectangle perfRectangle = new Rectangle(perfLocation, perfSize);

        Rectangle finalRect;
        finalRect = nodeRectangle.getUnion(labelRectangle).getUnion(perfRectangle).getUnion(statusRectangle);
        return finalRect;
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

    /**
     * Getter for breakpointLocation.
     * 
     * @return the breakpointLocation
     */
    public Point getBreakpointLocation() {
        return this.breakpointLocation;
    }

    /**
     * Getter for warningLocation.
     * 
     * @return the warningLocation
     */
    public Point getWarningLocation() {
        return this.warningLocation;
    }

    /**
     * Getter for errorLocation.
     * 
     * @return the errorLocation
     */
    public Point getErrorLocation() {
        return this.errorLocation;
    }

    /**
     * Getter for subjobContainer.
     * 
     * @return the subjobContainer
     */
    public SubjobContainer getSubjobContainer() {
        return this.subjobContainer;
    }

    /**
     * Sets the subjobContainer.
     * 
     * @param subjobContainer the subjobContainer to set
     */
    public void setSubjobContainer(SubjobContainer subjobContainer) {
        this.subjobContainer = subjobContainer;
    }
}
