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

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.swt.graphics.Image;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.core.ui.images.ECoreImage;

/**
 * DOC Administrator class global comment. Detailled comment
 */
public class NodeErrorFigure extends Figure {

    private ImageFigure errorFig;

    private int alpha = -1;

    int widthFi;

    int heightFi;

    public NodeErrorFigure() {
        errorFig = new ImageFigure();
        Image image = ImageProvider.getImage(ECoreImage.TRIANGLE);
        errorFig.setImage(image);
        errorFig.setVisible(false);
        widthFi = image.getImageData().width;
        heightFi = image.getImageData().height;
        this.add(errorFig);

    }

    public void updateVisible(boolean flag) {
        errorFig.setVisible(flag);
        if (flag) {
            errorFig.setPreferredSize(widthFi, heightFi);
            errorFig.setSize(errorFig.getPreferredSize());
            this.setSize(errorFig.getSize());
        }
    }

    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);
    }

    public int getAlpha() {
        return this.alpha;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

}
