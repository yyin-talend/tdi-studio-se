// ============================================================================
//
// Copyright (C) 2006-2010 Talend Inc. - www.talend.com
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
import org.talend.commons.CommonsPlugin;
import org.talend.commons.ui.image.EImage;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.core.CorePlugin;
import org.talend.core.model.process.Element;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.nodes.NodeError;
import org.talend.designer.core.ui.editor.nodes.NodeLabel;
import org.talend.designer.core.ui.editor.nodes.NodePerformance;
import org.talend.designer.core.ui.editor.nodes.NodeProgressBar;
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

    private NodeError nodeError;

    private NodeProgressBar nodeProgressBar;

    protected List<Element> elements = new ArrayList<Element>();

    private NodePerformance nodePerformance;

    private Point breakpointLocation = new Point();

    private Point warningLocation = new Point();

    private Point infoLocation = new Point();

    private Point errorLocation = new Point();

    private Point parallelLocation = new Point();

    private Point markLocation = new Point();

    private SubjobContainer subjobContainer;

    private Point errorMarkLocation = new Point();

    private Dimension breakpointSize;

    private Dimension errorSize;

    private Dimension warningSize;

    private Dimension infoSize;

    private Dimension parallelSize;

    private Dimension errorMarkSize;

    private Rectangle errorMarkRectangle;

    public NodeContainer(Node node) {
        this.node = node;
        node.setNodeContainer(this);
        this.nodeLabel = node.getNodeLabel();
        this.nodeError = node.getNodeError();
        this.nodeProgressBar = node.getNodeProgressBar();
        elements.add(node);
        elements.add(nodeLabel);
        elements.add(nodeError);
        if (node.isFileScaleComponent()) {
            elements.add(nodeProgressBar);
        }
        nodePerformance = new NodePerformance(this);
        elements.add(nodePerformance);

        if (!CommonsPlugin.isHeadless()) {
            Image image = ImageProvider.getImage(CorePlugin.getImageDescriptor(NodeContainerFigure.BREAKPOINT_IMAGE));
            breakpointSize = new Dimension(image.getImageData().width, image.getImageData().height);
            image = ImageProvider.getImage(EImage.ERROR_SMALL);
            errorSize = new Dimension(image.getImageData().width, image.getImageData().height);
            image = ImageProvider.getImage(EImage.WARNING_SMALL);
            warningSize = new Dimension(image.getImageData().width, image.getImageData().height);
            image = ImageProvider.getImage(EImage.INFORMATION_SMALL);
            infoSize = new Dimension(image.getImageData().width, image.getImageData().height);
            image = ImageProvider.getImage(EImage.PARALLEL_EXECUTION);
            parallelSize = new Dimension(image.getImageData().width, image.getImageData().height);
            image = ImageProvider.getImage(EImage.Error_Mark);
            errorMarkSize = new Dimension(image.getImageData().width, image.getImageData().height);
        }
    }

    private Rectangle prepareStatus(Point nodeLocation, Dimension nodeSize) {
        Rectangle statusRectangle = new Rectangle();
        Rectangle breakpointRectangle, warningRectangle, errorRectangle, infoRectangle, parallelLocationRectangle, errorMarkRectangle;

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

        infoLocation.x = nodeLocation.x + nodeSize.width;
        infoLocation.y = nodeLocation.y - infoSize.height;
        infoRectangle = new Rectangle(infoLocation, infoSize);
        statusRectangle.union(infoRectangle);

        markLocation.x = statusRectangle.x;
        markLocation.y = statusRectangle.y;

        errorMarkLocation.x = nodeLocation.x - (errorMarkSize.width - nodeSize.width) / 2;
        errorMarkLocation.y = markLocation.y - errorMarkSize.height;
        errorMarkRectangle = new Rectangle(errorMarkLocation, errorMarkSize);
        this.errorMarkRectangle = errorMarkRectangle;
        statusRectangle.union(errorMarkRectangle);

        parallelLocation.x = nodeLocation.x - nodeSize.width / 2 - parallelSize.width;
        parallelLocation.y = nodeLocation.y - parallelSize.height;
        parallelLocationRectangle = new Rectangle(parallelLocation, parallelSize);

        statusRectangle.union(parallelLocationRectangle);

        return statusRectangle;
    }

    private Rectangle prepareCleanStatus(Point nodeLocation, Dimension nodeSize) {
        Rectangle statusRectangle = new Rectangle();
        Rectangle breakpointRectangle, warningRectangle, errorRectangle, infoRectangle, errorMarkRectangle;

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

        infoLocation.x = nodeLocation.x + nodeSize.width;
        infoLocation.y = nodeLocation.y - infoSize.height;
        infoRectangle = new Rectangle(infoLocation, infoSize);
        statusRectangle.union(infoRectangle);

        markLocation.x = statusRectangle.x;
        markLocation.y = statusRectangle.y;

        errorMarkLocation.x = nodeLocation.x - (errorMarkSize.width - nodeSize.width) / 2;
        errorMarkLocation.y = markLocation.y - errorMarkSize.height;
        errorMarkRectangle = new Rectangle(errorMarkLocation, errorMarkSize);
        this.errorMarkRectangle = errorMarkRectangle;
        statusRectangle.union(errorMarkRectangle);

        return statusRectangle;
    }

    public Rectangle getNodeContainerRectangle() {
        Point nodeLocation;
        nodeLocation = node.getLocation();

        Dimension nodeSize;
        Dimension labelSize;
        Dimension errorNodeSize;
        Dimension progressNodeSize;
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

        Point errorLocation = nodeError.getLocation().getCopy();
        errorNodeSize = nodeError.getErrorSize();
        Rectangle errorNodeRectangle = new Rectangle(errorLocation, new Dimension(60, 60));

        Point progressLocation = nodeProgressBar.getLocation().getCopy();
        progressNodeSize = nodeProgressBar.getProgressSize();
        Rectangle progressNodeRectangle = new Rectangle(progressLocation, new Dimension(80, 80));

        Dimension perfSize = nodePerformance.getSize();
        Point perfLocation = nodePerformance.getLocation();

        Rectangle perfRectangle = new Rectangle(perfLocation, perfSize);

        Rectangle finalRect;
        finalRect = nodeRectangle.getUnion(labelRectangle).getUnion(perfRectangle).getUnion(statusRectangle);

        finalRect.height += errorNodeSize.height;// .getUnion(errorNodeRectangle)
        finalRect.height += errorMarkSize.height;
        if (node.isFileScaleComponent()) {
            finalRect = finalRect.getUnion(progressNodeRectangle);// finalRect.height += progressNodeSize.height;//
        }
        return finalRect;
    }

    public Rectangle getNodeMarkRectangle() {
        Point nodeLocation;
        nodeLocation = node.getLocation();

        Dimension nodeSize;
        Dimension labelSize;
        Dimension errorNodeSize;
        Dimension progressNodeSize;
        nodeSize = node.getSize();
        Rectangle nodeRectangle = new Rectangle(nodeLocation, nodeSize);

        Rectangle statusRectangle = prepareCleanStatus(nodeLocation, nodeSize);

        labelSize = nodeLabel.getLabelSize();

        errorNodeSize = nodeError.getErrorSize();

        progressNodeSize = nodeProgressBar.getProgressSize();

        Dimension perfSize = nodePerformance.getSize();
        Point perfLocation = nodePerformance.getLocation();

        Rectangle perfRectangle = new Rectangle(perfLocation, perfSize);

        Rectangle finalRect;
        finalRect = nodeRectangle.getUnion(perfRectangle).getUnion(statusRectangle);
        finalRect.height += labelSize.height;
        finalRect.height += errorNodeSize.height;
        finalRect.height += progressNodeSize.height;
        return finalRect;
    }

    public Rectangle getErrorMarkRectangle() {
        return errorMarkRectangle;
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

    public NodeError getNodeError() {
        return this.nodeError;
    }

    public void setNodeError(NodeError nodeError) {
        this.nodeError = nodeError;
    }

    public NodeProgressBar getNodeProgressBar() {
        return this.nodeProgressBar;
    }

    public void setNodeProgressBar(NodeProgressBar nodeProgressBar) {
        this.nodeProgressBar = nodeProgressBar;
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
     * Getter for warningLocation.
     * 
     * @return the warningLocation
     */
    public Point getInfoLocation() {
        return this.infoLocation;
    }

    /**
     * Getter for errorMarkLocation.
     * 
     * @return the errorMarkLocation
     */
    public Point getErrorMarkLocation() {
        return this.errorMarkLocation;
    }

    /**
     * Getter for parallelLocation.
     * 
     * @return the parallelLocation
     */
    public Point getParallelLocation() {
        return this.parallelLocation;
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
     * Getter for markLocation.
     * 
     * @return the markLocation
     */
    public Point getMarkLocation() {
        return this.markLocation;
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

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "NodeContainer{" + node.toString() + "}"; //$NON-NLS-1$ //$NON-NLS-2$
    }
}
