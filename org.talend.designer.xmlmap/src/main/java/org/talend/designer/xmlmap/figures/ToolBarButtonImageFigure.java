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
package org.talend.designer.xmlmap.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;

/**
 * DOC hywang class global comment. Detailled comment
 */
public class ToolBarButtonImageFigure extends ImageFigure {

    protected boolean enable;

    protected Image oldImage;

    protected MouseMotionListener listener;

    public ToolBarButtonImageFigure() {
        setOpaque(true);
    }

    public ToolBarButtonImageFigure(Image image, int alignment) {
        super(image, alignment);
        setOpaque(true);
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
                ToolBarButtonImageFigure.this.setBackgroundColor(ColorConstants.white);
                ToolBarButtonImageFigure.this.validate();
            }

            public void mouseEntered(MouseEvent me) {

            }

            public void mouseDragged(MouseEvent me) {

            }
        };
        if (listener != null) {
            this.addMouseMotionListener(listener);
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
}

class AddButtonImageFigure extends ToolBarButtonImageFigure {

    public AddButtonImageFigure() {
    }

    public AddButtonImageFigure(Image image, int alignment) {
        super(image, alignment);
    }

    public AddButtonImageFigure(Image image) {
        super(image);
    }

}

class RemoveButtonImageFigure extends ToolBarButtonImageFigure {

    public RemoveButtonImageFigure() {
    }

    public RemoveButtonImageFigure(Image image, int alignment) {
        super(image, alignment);
    }

    public RemoveButtonImageFigure(Image image) {
        super(image);
    }

}

class MoveUpButtonImageFigure extends ToolBarButtonImageFigure {

    public MoveUpButtonImageFigure() {
    }

    public MoveUpButtonImageFigure(Image image, int alignment) {
        super(image, alignment);
    }

    public MoveUpButtonImageFigure(Image image) {
        super(image);
        listener = new MouseMotionListener() {

            public void mouseMoved(MouseEvent me) {
                // TODO Auto-generated method stub

            }

            public void mouseHover(MouseEvent me) {
                // TODO Auto-generated method stub

            }

            public void mouseExited(MouseEvent me) {
                // TODO Auto-generated method stub

            }

            public void mouseEntered(MouseEvent me) {
                // TODO Auto-generated method stub

            }

            public void mouseDragged(MouseEvent me) {
                // TODO Auto-generated method stub

            }
        };

    }

}

class MoveDownButtonImageFigure extends ToolBarButtonImageFigure {

    public MoveDownButtonImageFigure() {
    }

    public MoveDownButtonImageFigure(Image image, int alignment) {
        super(image, alignment);
    }

    public MoveDownButtonImageFigure(Image image) {
        super(image);
    }

}

class MinisizeButtonImageFigure extends ToolBarButtonImageFigure {

    public MinisizeButtonImageFigure() {
    }

    public MinisizeButtonImageFigure(Image image, int alignment) {
        super(image, alignment);
    }

    public MinisizeButtonImageFigure(Image image) {
        super(image);
    }

}

class CheckButtonImageFigure extends ToolBarButtonImageFigure {

    public CheckButtonImageFigure() {
    }

    public CheckButtonImageFigure(Image image, int alignment) {
        super(image, alignment);
    }

    public CheckButtonImageFigure(Image image) {
        super(image);
    }

}
