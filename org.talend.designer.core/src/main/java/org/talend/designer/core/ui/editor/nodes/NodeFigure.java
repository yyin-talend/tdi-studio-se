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
package org.talend.designer.core.ui.editor.nodes;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.resource.ImageDescriptor;
import org.talend.commons.utils.workbench.gef.SimpleHtmlFigure;

/**
 * This class create a figure with the given image. <br/>eh
 * 
 * $Id$
 * 
 */
public class NodeFigure extends Figure {

    private ImageFigure fig;

    private ImageDescriptor im;

    private SimpleHtmlFigure hint;

    private int alpha = -1;

    public NodeFigure(ImageDescriptor imageDescriptor) {
        fig = new ImageFigure();
        im = imageDescriptor;
        fig.setImage(im.createImage());
        add(fig);
        this.setSize(getPreferredSize());
        this.setOpaque(true);
        hint = new SimpleHtmlFigure();
    }

    public void setHint(String hintText) {
        if (hintText.equals("")) { //$NON-NLS-1$
            setToolTip(null);
        } else {
            hint.setText(hintText);
            setToolTip(hint);
        }
    }

    public Dimension getNodeSize() {
        Dimension size = new Dimension();
        size.height = im.getImageData().height;
        size.width = im.getImageData().width;
        return size;
    }

    public ImageDescriptor getImageDescriptor() {
        return im;
    }

    public void setBounds(final Rectangle rect) {
        super.setBounds(rect);
        this.fig.setBounds(rect);
    }

    @Override
    public void paint(Graphics graphics) {
        if (alpha != -1) {
            graphics.setAlpha(alpha);
        }
        super.paint(graphics);
    }

    public int getAlpha() {
        return this.alpha;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

}
