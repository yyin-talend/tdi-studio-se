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
package org.talend.designer.core.ui.editor.subjobcontainer;

import org.eclipse.draw2d.ActionEvent;
import org.eclipse.draw2d.ActionListener;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.RoundedRectangle;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.talend.commons.utils.workbench.gef.SimpleHtmlFigure;
import org.talend.core.model.utils.TalendTextUtils;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.ui.editor.cmd.PropertyChangeCommand;
import org.talend.designer.core.ui.editor.connections.CollapseFigure;

/**
 * DOC nrousseau class global comment. Detailled comment
 */
public class SubjobContainerFigure extends Figure {

    private static final Color SUBJOB_TITLE_COLOR = new Color(null, 50, 50, 250);

    private static final Color PREJOB_TITLE_COLOR = new Color(null, 230, 100, 0);

    private static final Color POSTJOB_TITLE_COLOR = new Color(null, 230, 100, 0);

    private SubjobContainer subjobContainer;

    private RoundedRectangle outlineFigure;

    private SimpleHtmlFigure titleFigure;

    private RoundedRectangle rectFig;

    private CollapseFigure collapseFigure;

    private Color mainColor;

    private boolean showTitle;

    private String title;

    private Color subjobTitleColor;

    /**
     * DOC nrousseau SubjobContainerFigure constructor comment.
     * 
     * @param model
     */
    public SubjobContainerFigure(SubjobContainer subjobContainerTmp) {
        setLayoutManager(new FreeformLayout());
        this.subjobContainer = subjobContainerTmp;

        outlineFigure = new RoundedRectangle();
        rectFig = new RoundedRectangle();
        titleFigure = new SimpleHtmlFigure();
        collapseFigure = new CollapseFigure();
        // deactivate the collapse due to the problems with GEF.
        collapseFigure.setVisible(false);
        // ////
        collapseFigure.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                PropertyChangeCommand ppc = new PropertyChangeCommand(subjobContainer, EParameterName.COLLAPSED.getName(),
                        !subjobContainer.isCollapsed());
                subjobContainer.getProcess().getCommandStack().execute(ppc);
            }
        });

        updateData();

        initializeSubjobContainer(subjobContainer.getSubjobContainerRectangle());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.draw2d.Figure#paint(org.eclipse.draw2d.Graphics)
     */
    @Override
    public void paint(Graphics graphics) {
        graphics.setAlpha(100);
        super.paint(graphics);
    }

    public void initializeSubjobContainer(Rectangle rectangle) {
        Point location = this.getLocation();
        collapseFigure.setCollapsed(subjobContainer.isCollapsed());

        titleFigure.setText("<b> " + title + "</b>"); //$NON-NLS-1$ //$NON-NLS-2$
        Dimension preferedSize = titleFigure.getPreferredSize();
        preferedSize = preferedSize.getExpanded(0, 2);
        titleFigure.setSize(rectangle.width - preferedSize.height, preferedSize.height);
        titleFigure.setLocation(location);
        titleFigure.setVisible(showTitle);

        outlineFigure.setLocation(location);
        outlineFigure.setVisible(showTitle);
        outlineFigure.setBackgroundColor(subjobTitleColor);
        outlineFigure.setForegroundColor(subjobTitleColor);
        outlineFigure.setSize(rectangle.width, preferedSize.height);

        collapseFigure.setLocation(new Point(rectangle.width - preferedSize.height + location.x, location.y));
        collapseFigure.setSize(preferedSize.height, preferedSize.height);
        // collapseFigure.setBackgroundColor(new Color(null, 50, 50, 250));

        rectFig.setLocation(new Point(location.x, /* preferedSize.height + */location.y));
        rectFig.setSize(new Dimension(rectangle.width, rectangle.height /*- preferedSize.height*/));
        rectFig.setBackgroundColor(mainColor);
        rectFig.setForegroundColor(subjobTitleColor);
    }

    public void updateData() {
        mainColor = new Color(null, TalendTextUtils.stringToRGB((String) subjobContainer
                .getPropertyValue(EParameterName.SUBJOB_COLOR.getName())));

        showTitle = (Boolean) subjobContainer.getPropertyValue(EParameterName.SHOW_SUBJOB_TITLE.getName());

        title = (String) subjobContainer.getPropertyValue(EParameterName.SUBJOB_TITLE.getName());

        subjobTitleColor = SUBJOB_TITLE_COLOR;
        if (subjobContainer.getSubjobStartNode().getComponent().getName().equals("tPrejob")) {
            title = " Prejob:" + title;
            subjobTitleColor = PREJOB_TITLE_COLOR;
        }
        if (subjobContainer.getSubjobStartNode().getComponent().getName().equals("tPostjob")) {
            title = " Postjob:" + title;
            subjobTitleColor = POSTJOB_TITLE_COLOR;
        }

        this.getChildren().remove(outlineFigure);
        this.getChildren().remove(rectFig);
        outlineFigure.getChildren().clear();
        rectFig.getChildren().clear();

        if (showTitle) {
            outlineFigure.add(titleFigure);
            outlineFigure.add(collapseFigure);
            add(rectFig, null, 0);
            add(outlineFigure, null, 1);
        } else {
            outlineFigure.add(titleFigure);
            rectFig.add(collapseFigure);
            add(outlineFigure, null, 0);
            add(rectFig, null, 1);
        }
    }

}
