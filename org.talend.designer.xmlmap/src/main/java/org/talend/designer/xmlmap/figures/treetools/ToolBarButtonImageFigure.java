// ============================================================================
//
// Copyright (C) 2006-2011 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.xmlmap.figures.treetools;

import org.eclipse.draw2d.Clickable;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.talend.designer.xmlmap.ui.resource.ColorInfo;
import org.talend.designer.xmlmap.ui.resource.ColorProviderMapper;

/**
 * DOC hywang class global comment. Detailled comment
 */
public class ToolBarButtonImageFigure extends ImageFigure {

    protected boolean enable;

    protected Image oldImage;

    private int style = Clickable.STYLE_BUTTON;

    private boolean selected;

    protected MouseMotionListener listener;

    public ToolBarButtonImageFigure(Image image, int alignment) {
        super(image, alignment);
        setOpaque(true);
        initlistener();
    }

    public ToolBarButtonImageFigure(Image image) {
        super(image);
        setOpaque(true);
        initlistener();
    }

    /**
     * DOC Administrator Comment method "initlistener".
     */
    protected void initlistener() {
        listener = new MouseMotionListener() {

            public void mouseMoved(MouseEvent me) {

            }

            public void mouseHover(MouseEvent me) {
                ToolBarButtonImageFigure.this.setBackgroundColor(ColorConstants.button);
                ToolBarButtonImageFigure.this.validate();
            }

            public void mouseExited(MouseEvent me) {
                if (Clickable.STYLE_TOGGLE != getStyle()) {
                    ToolBarButtonImageFigure.this.setBorder(null);
                    ToolBarButtonImageFigure.this.setBackgroundColor(getContainerBgColor(ToolBarButtonImageFigure.this));
                } else if (!isSelected()) {
                    ToolBarButtonImageFigure.this.setBorder(null);
                    ToolBarButtonImageFigure.this.setBackgroundColor(getContainerBgColor(ToolBarButtonImageFigure.this));
                }
                ToolBarButtonImageFigure.this.validate();
            }

            public void mouseEntered(MouseEvent me) {

            }

            public void mouseDragged(MouseEvent me) {

            }
        };
        this.addMouseMotionListener(listener);

        this.addMouseListener(new MouseListener() {

            public void mousePressed(MouseEvent me) {
                toolBarButtonPressed(me);
            }

            public void mouseReleased(MouseEvent me) {
                // TODO Auto-generated method stub

            }

            public void mouseDoubleClicked(MouseEvent me) {
                // TODO Auto-generated method stub

            }

        });
    }

    public void toolBarButtonPressed(MouseEvent me) {
        setSelected(!isSelected());
        if (isSelected()) {
            setBorder(new LineBorder(ColorProviderMapper.getColor(ColorInfo.COLOR_TREE_BORDER)));
            setBackgroundColor(ColorConstants.buttonLightest);
        } else {
            setBorder(null);
            setBackgroundColor(getContainerBgColor(this));
        }
    }

    private Color getContainerBgColor(IFigure figure) {
        if (figure == null) {
            return ColorConstants.menuBackground;
        }
        if (figure.getParent() != null) {
            if (figure.getParent().isOpaque()) {
                return figure.getParent().getBackgroundColor();
            } else {
                return getContainerBgColor(figure.getParent());
            }
        } else {
            return ColorConstants.menuBackground;
        }

    }

    public void setEnabled(boolean value) {
        super.setEnabled(value);
        this.enable = value;
        changeImage();
    }

    private void changeImage() {
        if (enable) {
            if (oldImage != null) {
                this.setImage(oldImage);
                this.validate();
            }
        }
        if (!enable) {
            oldImage = getImage();
            Image newImage = new Image(this.getImage().getDevice(), this.getImage(), SWT.IMAGE_DISABLE);
            this.setImage(newImage);
            this.validate();
        }

    }

    public int getStyle() {
        return style;
    }

    public void setStyle(int style) {
        this.style = style;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

}
