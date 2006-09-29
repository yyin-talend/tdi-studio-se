// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
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
package org.talend.designer.core.ui.editor.nodecontainer;

import java.util.List;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.talend.commons.utils.workbench.gef.SimpleHtmlFigure;
import org.talend.core.CorePlugin;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.nodes.NodeLabel;
import org.talend.designer.core.ui.editor.nodes.NodePerformance;
import org.talend.designer.core.ui.editor.process.Process;
import org.talend.designer.core.ui.views.problems.Problems;

/**
 * This class create a figure with the given image. <br/>eh
 * 
 * $Id$
 * 
 */
public class NodeContainerFigure extends Figure {

    private int alpha = -1;

    private NodeContainer nodeContainer;

    private ImageFigure breakpointFigure;

    private Dimension breakpointSize;

    private ImageFigure errorFigure;

    private Dimension errorSize;

    private ImageFigure warningFigure;

    private Dimension warningSize;

    private SimpleHtmlFigure htmlStatusHint;

    private Point breakpointLocation = new Point();

    private Point warningLocation = new Point();

    private Point errorLocation = new Point();

    private static final String BREAKPOINT_IMAGE = "icons/breakpoint.png";

    private static final String ERROR_IMAGE = "icons/error_tsk.gif";

    private static final String WARNING_IMAGE = "icons/warn_tsk.gif";

    public NodeContainerFigure(NodeContainer nodeContainer) {
        this.nodeContainer = nodeContainer;
        this.setLayoutManager(new FreeformLayout());
//        this.setOpaque(true);
//        this.setBackgroundColor(new Color(null, new RGB(200, 100, 200)));

        breakpointFigure = new ImageFigure();
        breakpointFigure.setImage(CorePlugin.getImageDescriptor(BREAKPOINT_IMAGE).createImage());
        breakpointFigure.setVisible(false);
        breakpointFigure.setSize(breakpointFigure.getPreferredSize());
        this.add(breakpointFigure);
        breakpointSize = new Dimension(breakpointFigure.getImage().getImageData().width, breakpointFigure.getImage()
                .getImageData().height);

        errorFigure = new ImageFigure();
        errorFigure.setImage(CorePlugin.getImageDescriptor(ERROR_IMAGE).createImage());
        errorFigure.setVisible(false);
        errorFigure.setSize(errorFigure.getPreferredSize());
        this.add(errorFigure);
        errorSize = new Dimension(errorFigure.getImage().getImageData().width,
                errorFigure.getImage().getImageData().height);

        warningFigure = new ImageFigure();
        warningFigure.setImage(CorePlugin.getImageDescriptor(WARNING_IMAGE).createImage());
        warningFigure.setVisible(false);
        warningFigure.setSize(warningFigure.getPreferredSize());
        this.add(warningFigure);
        warningSize = new Dimension(warningFigure.getImage().getImageData().width, warningFigure.getImage()
                .getImageData().height);

        htmlStatusHint = new SimpleHtmlFigure();
    }

    public void setBounds(final Rectangle rect) {
        super.setBounds(rect);
    }

    public void updateStatus(int status) {
        if ((status & Process.BREAKPOINT_STATUS) != 0) {
            breakpointFigure.setVisible(true);
        } else {
            breakpointFigure.setVisible(false);
        }

        if ((status & Process.ERROR_STATUS) != 0) {
            warningFigure.setVisible(false);
            errorFigure.setVisible(true);
        } else {
            errorFigure.setVisible(false);
            errorFigure.setToolTip(null);
        }

        if (((status & Process.WARNING_STATUS) != 0) && !errorFigure.isVisible()) {
            warningFigure.setVisible(true);
        } else {
            warningFigure.setVisible(false);
            warningFigure.setToolTip(null);
        }

        if (errorFigure.isVisible() || warningFigure.isVisible()) {
            List<String> problemsList;

            String text = "<b>" + nodeContainer.getNode().getUniqueName() + "</b><br><br>";
            text += "<i>Warnings:</i><br>";

            problemsList = Problems.getStatusList(Problems.WARNING_STATUS, nodeContainer.getNode());
            for (String str : problemsList) {
                text += "\t" + str + "<br>";
            }
            text += "<i>Errors:</i><br>";
            problemsList = Problems.getStatusList(Problems.ERROR_STATUS, nodeContainer.getNode());
            for (String str : problemsList) {
                text += "\t" + str + "<br>";
            }
            htmlStatusHint.setText(text);
            if (errorFigure.isVisible()) {
                warningFigure.setToolTip(null);
                errorFigure.setToolTip(htmlStatusHint);
            } else {
                errorFigure.setToolTip(null);
                warningFigure.setToolTip(htmlStatusHint);
            }
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
        Node node = nodeContainer.getNode();
        nodeLocation = node.getLocation();

        Dimension nodeSize;
        Dimension labelSize;
        nodeSize = new Dimension();
        nodeSize.height = node.getImageDescriptor().getImageData().height;
        nodeSize.width = node.getImageDescriptor().getImageData().width;
        Rectangle nodeRectangle = new Rectangle(nodeLocation, nodeSize);

        Rectangle statusRectangle = prepareStatus(nodeLocation, nodeSize);

        NodeLabel nodeLabel = node.getNodeLabel();
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

        NodePerformance nodePerf = nodeContainer.getNodePerformance();
        Dimension perfSize = nodePerf.getSize();
        Point perfLocation = nodePerf.getLocation();

        Rectangle perfRectangle = new Rectangle(perfLocation, perfSize);

        Rectangle finalRect;
        finalRect = nodeRectangle.getUnion(labelRectangle).getUnion(perfRectangle).getUnion(statusRectangle);
        return finalRect;
    }

    @Override
    public void paint(Graphics graphics) {
        if (alpha != -1) {
            graphics.setAlpha(alpha);
        }
        breakpointFigure.setLocation(breakpointLocation);
        errorFigure.setLocation(errorLocation);
        warningFigure.setLocation(warningLocation);

        super.paint(graphics);
    }

    public int getAlpha() {
        return this.alpha;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }
}
