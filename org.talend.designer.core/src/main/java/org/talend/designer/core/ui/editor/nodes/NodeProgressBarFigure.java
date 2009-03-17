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

    // private static int contentPro = 0;

    // private static final int MAX_VARIABLE_WIDTH = 30;
    //
    // private static final int MAX_VARIABLE_HEIGHT = 16;

    int widthFi;

    int heightFi;

    private Node node;

    public NodeProgressBarFigure(Node node) {
        ToolbarLayout layout = new ToolbarLayout();
        setLayoutManager(layout);
        this.node = node;
        // progressFig = new ImageFigure();
        // Image image = ImageProvider.getImage(ECoreImage.PROGRESSBAR);
        // progressFig.setImage(image);
        // progressFig.setVisible(true);
        // widthFi = image.getImageData().width;
        // heightFi = image.getImageData().height;
        //
        // progressFig.setPreferredSize(widthFi, heightFi);
        // progressFig.setSize(progressFig.getPreferredSize());
        // this.setSize(progressFig.getSize());
        //
        // this.add(progressFig);

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

    // private void initProgressBar() {
    // proBar = new ProgressBar(group1, SWT.BORDER | SWT.HORIZONTAL | SWT.SMOOTH);
    // proBar.setSize(30, 16);
    // proBar.setMinimum(0);
    // proBar.setMaximum(100);
    // proBar.setForeground(new Color(display, 90, 160, 200));
    // }

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
