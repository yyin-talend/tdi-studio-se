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

import java.util.List;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.graphics.Image;
import org.talend.commons.ui.image.ImageProvider;
import org.talend.core.ui.images.ECoreImage;

/**
 * DOC Administrator class global comment. Detailled comment
 */
public class NodeProgressBarFigure extends Figure {

    private ImageFigure progressFig;

    private int alpha = -1;

    int widthFi;

    int heightFi;

    private Node node;

    public NodeProgressBarFigure(Node node) {
        ToolbarLayout layout = new ToolbarLayout();
        setLayoutManager(layout);
        this.node = node;

    }

    public void updateVisible(boolean flag) {
        setProgressData(0);
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

    public void setProgressData(int extent) {
        // contentPro = extent;
        List childs = this.getChildren();
        childs.clear();
        Figure progressBarFigure = new Figure();
        progressBarFigure.setLayoutManager(new ToolbarLayout(true));
        progressBarFigure.setVisible(true);
        progressBarFigure.setSize(60, 16);
        progressBarFigure.setPreferredSize(60, 16);
        int nodeX = progressBarFigure.getLocation().x;
        int nodeY = progressBarFigure.getLocation().y;
        for (int i = 0; i < extent; i++) {
            ImageFigure progressDataFigure = new ImageFigure();
            Image image = ImageProvider.getImage(ECoreImage.PROGRESSBAR);
            progressDataFigure.setImage(image);
            progressDataFigure.setVisible(true);
            progressBarFigure.add(progressDataFigure);
            int imageWith = image.getImageData().width;
            if (i != 0) {
                Point point = new Point(nodeX + i * imageWith, nodeY);
                progressDataFigure.setLocation(point);
            }

        }
        this.add(progressBarFigure);
        this.setSize(progressBarFigure.getSize());
    }

}
