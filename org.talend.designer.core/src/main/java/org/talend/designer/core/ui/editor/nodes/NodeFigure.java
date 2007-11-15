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
package org.talend.designer.core.ui.editor.nodes;

import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.talend.commons.utils.workbench.gef.SimpleHtmlFigure;
import org.talend.designer.core.DesignerPlugin;
import org.talend.designer.core.ui.preferences.TalendDesignerPrefConstants;

/**
 * This class create a figure with the given image. <br/>eh
 * 
 * $Id$
 * 
 */
public class NodeFigure extends Figure {

    private final ImageFigure fig;

    private final ImageDescriptor im;

    private final SimpleHtmlFigure hint;

    private int alpha = -1;

    private final NodeBorder lineBorder = new NodeBorder();

    public static final Color START_COLOR = new Color(null, new RGB(0xB0, 0xE7, 0));

    public NodeFigure(Node node) {
        fig = new ImageFigure();
        im = node.getIcon32();
        fig.setImage(im.createImage());
        fig.setSize(new Dimension(Node.DEFAULT_SIZE, Node.DEFAULT_SIZE));
        add(fig);
        this.setSize(node.getSize());
        this.setOpaque(true);
        hint = new SimpleHtmlFigure();
        this.setBorder(lineBorder);
    }

    public void setHint(String hintText) {
        if (hintText.equals("")) { //$NON-NLS-1$
            setToolTip(null);
        } else {
            hint.setText(hintText);
            setToolTip(hint);
        }
    }

    public ImageDescriptor getImageDescriptor() {
        return im;
    }

    @Override
    public void setBounds(final Rectangle rect) {
        super.setBounds(rect);

        Point location = (new Point(rect.getCenter())).translate(new Point(-fig.getSize().width / 2,
                -(fig.getSize().height / 2)));
        Rectangle figBounds = new Rectangle(location, fig.getSize());
        this.fig.setBounds(figBounds);

        if (!rect.getSize().equals(fig.getSize())) {
            lineBorder.setUseRectangle(true);
        } else {
            lineBorder.setUseRectangle(false);
        }
    }

    @Override
    public void paint(Graphics graphics) {
        if (alpha != -1) {
            graphics.setAlpha(alpha);
        }
        if (DesignerPlugin.getDefault().getPreferenceStore()
                .getBoolean(TalendDesignerPrefConstants.EDITOR_ANTIALIASING)) {
            graphics.setInterpolation(SWT.HIGH);
        }
        super.paint(graphics);
    }

    public int getAlpha() {
        return this.alpha;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    public void setStart(boolean start) {
        if (start) {
            setBackgroundColor(START_COLOR);
            setOpaque(true);
        } else {
            setOpaque(false);
        }
    }

    /**
     * DOC nrousseau NodeFigure class global comment. Detailled comment <br/>
     * 
     */
    class NodeBorder extends AbstractBorder {

        private boolean useRectangle;

        public Insets getInsets(IFigure figure) {
            return null;
        }

        public void paint(IFigure figure, Graphics g, Insets theInsets) {
            Rectangle r = getPaintRectangle(figure, theInsets);

            if (useRectangle) {
                g.setLineWidth(2);
                g.setForegroundColor(Display.getDefault().getSystemColor(SWT.COLOR_DARK_GRAY));
                g.drawRectangle(r);
                g.drawLine(r.x, 1, r.right(), 1);
                g.drawLine(1, r.y, 1, r.bottom());

                g.setForegroundColor(Display.getDefault().getSystemColor(SWT.COLOR_GRAY));
                g.drawLine(r.x, r.bottom() - 1, r.right(), r.bottom() - 1);
                g.drawLine(r.right() - 1, r.y, r.right() - 1, r.bottom());
            }

        }

        public void setUseRectangle(boolean useRectangle) {
            this.useRectangle = useRectangle;
        }

    };

}
