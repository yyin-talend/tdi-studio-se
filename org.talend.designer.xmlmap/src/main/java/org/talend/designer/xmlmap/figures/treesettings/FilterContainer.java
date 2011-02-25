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
package org.talend.designer.xmlmap.figures.treesettings;

import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.talend.designer.xmlmap.figures.layout.EqualWidthLayout;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputXmlTree;
import org.talend.designer.xmlmap.ui.resource.ImageInfo;
import org.talend.designer.xmlmap.ui.resource.ImageProviderMapper;

/**
 * wchen class global comment. Detailled comment
 */
public class FilterContainer extends Figure {

    private static final int DEFAULT_HEIGHT = 40;

    private OutputXmlTree outputTree;

    private ImageFigure button;

    public FilterContainer(OutputXmlTree outputTree) {
        this.outputTree = outputTree;
        createContent();
    }

    private void createContent() {
        FilterContainerLayout manager = new FilterContainerLayout();
        manager.setSpacing(5);
        setLayoutManager(manager);

        FilterTextArea textArea = new FilterTextArea();
        textArea.setText(outputTree.getExpressionFilter());
        this.add(textArea);

        textArea.setOpaque(true);
        setBackgroundColor(ColorConstants.white);

        button = new ImageFigure(ImageProviderMapper.getImage(ImageInfo.FILTER_BUTTON));
        this.add(button);

        setOpaque(true);
        setBackgroundColor(ColorConstants.yellow);
        setBorder(new MarginBorder(2));

    }

    class FilterContainerLayout extends EqualWidthLayout {

        @Override
        public void layout(IFigure parent) {
            List children = parent.getChildren();
            int numChildren = children.size();

            Rectangle clientArea = transposer.t(parent.getClientArea());
            int x = clientArea.x;
            int y = clientArea.y;

            Dimension prefSizes[] = new Dimension[numChildren];
            Dimension minSizes[] = new Dimension[numChildren];

            int wHint = -1;
            int hHint = -1;
            hHint = parent.getClientArea(Rectangle.SINGLETON).height;
            wHint = parent.getClientArea(Rectangle.SINGLETON).width;

            IFigure child;

            for (int i = 0; i < numChildren; i++) {
                child = (IFigure) children.get(i);
                prefSizes[i] = transposer.t(getChildPreferredSize(child, wHint, hHint));
                minSizes[i] = transposer.t(getChildMinimumSize(child, wHint, hHint));

            }
            Insets insets = parent.getBorder().getInsets(null);

            if (numChildren >= 2) {
                int avaliableWith = clientArea.width - insets.left - insets.right - spacing;
                IFigure child0 = (IFigure) children.get(0);

                Rectangle newBounds = new Rectangle(x + insets.left, y, prefSizes[0].width, DEFAULT_HEIGHT);
                newBounds.width = avaliableWith - prefSizes[1].width;
                child0.setBounds(newBounds);

                x = x + newBounds.width + spacing;

                IFigure child1 = (IFigure) children.get(1);
                newBounds = new Rectangle(x, y, prefSizes[1].width, DEFAULT_HEIGHT);
                child1.setBounds(newBounds);
            }

            // for (int i = numChildren - 1; i >= 0; i--) {
            // int prefWidth = prefSizes[i].width;
            // Rectangle newBounds = new Rectangle(x, y, prefWidth, DEFAULT_HEIGHT);
            //
            // newBounds.x = x - prefWidth - 2;
            //
            // if (i == 0) {
            // newBounds.width = widthColumn0;
            // } else if (i == 1) {
            // newBounds.width = widthColumn1;
            // }
            //
            // child = (IFigure) children.get(i);
            //
            // child.setBounds(transposer.t(newBounds));
            //
            // x = x + newBounds.width + spacing;
            // }

        }

        @Override
        protected Dimension calculatePreferredSize(IFigure container, int wHint, int hHint) {
            Dimension dimension = super.calculatePreferredSize(container, wHint, hHint);
            Insets insets = container.getBorder().getInsets(null);
            dimension.height = DEFAULT_HEIGHT + insets.top + insets.bottom;
            return dimension;
        }

    }

}
