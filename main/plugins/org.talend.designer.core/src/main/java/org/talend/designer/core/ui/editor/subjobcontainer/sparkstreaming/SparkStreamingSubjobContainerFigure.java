// ============================================================================
//
// Copyright (C) 2006-2015 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.core.ui.editor.subjobcontainer.sparkstreaming;

import java.util.List;

import org.eclipse.draw2d.RoundedRectangle;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.talend.commons.ui.utils.image.ColorUtils;
import org.talend.commons.ui.utils.workbench.gef.SimpleHtmlFigure;
import org.talend.core.model.process.ElementParameterParser;
import org.talend.core.model.process.INode;
import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.ui.editor.nodecontainer.NodeContainerUtils;
import org.talend.designer.core.ui.editor.subjobcontainer.SubjobContainer;
import org.talend.designer.core.ui.editor.subjobcontainer.SubjobContainerFigure;
import org.talend.designer.core.utils.DesignerColorUtils;

/**
 * DOC nrousseau class global comment. Detailled comment
 */
public class SparkStreamingSubjobContainerFigure extends SubjobContainerFigure {

    private RoundedRectangle timeoutFigureRect;

    private SimpleHtmlFigure timeoutFigureText;

    private String timeoutText;

    private boolean showTimeout = false;

    private final static int TIMEOUT_FIGURE_WIDTH = 150;

    private final static int TIMEOUT_FIGURE_HEIGHT = 40;

    // This attribute is used to define if a subjob has more than one node (or not). We won't display the stats if there
    // is only one node.
    private boolean subjobHasMoreThanOneNode = false;

    public SparkStreamingSubjobContainerFigure(SubjobContainer subjobContainerTmp) {
        super(subjobContainerTmp);

        updateData();

        this.subjobHasMoreThanOneNode = this.subjobContainer.getNodeContainers().size() > 1;
    }

    @Override
    protected void initFigure() {
        super.initFigure();
        timeoutFigureRect = new RoundedRectangle();
        timeoutFigureText = new SimpleHtmlFigure();
        timeoutFigureText.setOpaque(false);
    }

    @Override
    public void initializeSubjobContainer(Rectangle rectangle) {

        // Resize the rectangle to take the spark streaming information into account in the rectangle dimensions.^M
        // This part is for the information at the top left corner.^M

        this.subjobHasMoreThanOneNode = subjobContainer.getNodeContainers().size() > 1;

        if (this.subjobHasMoreThanOneNode) {
            rectangle.resize((rectangle.width() < TIMEOUT_FIGURE_WIDTH) ? TIMEOUT_FIGURE_WIDTH : 0, TIMEOUT_FIGURE_HEIGHT + 10);
            rectangle.setY(rectangle.y() - TIMEOUT_FIGURE_HEIGHT - 10);
        }

        super.initializeSubjobContainer(rectangle);
        Point location = this.getLocation();

        timeoutFigureText.setText(timeoutText);
        timeoutFigureText
                .setSize(TIMEOUT_FIGURE_WIDTH - 5, showTimeout ? TIMEOUT_FIGURE_HEIGHT - 10 : TIMEOUT_FIGURE_HEIGHT - 22);
        timeoutFigureText.setLocation(new Point(location.x() + 10, location.y() + 25));
        timeoutFigureText.setVisible(false);
        if (this.subjobHasMoreThanOneNode) {
            timeoutFigureText.setVisible(true);
        }
        timeoutFigureText.setBackgroundColor(new Color(Display.getDefault(), mainColor));// //////////////////////
        timeoutFigureText.setForegroundColor(new Color(Display.getDefault(), subjobTitleColor));

        timeoutFigureRect.setLocation(new Point(location.x() + 5, location.y() + 20));
        timeoutFigureRect.setVisible(false);
        if (this.subjobHasMoreThanOneNode) {
            timeoutFigureRect.setVisible(true);
        }
        timeoutFigureRect.setBackgroundColor(new Color(Display.getDefault(), mainColor));// //////////////////////
        timeoutFigureRect.setForegroundColor(new Color(Display.getDefault(), subjobTitleColor));
        timeoutFigureRect.setSize(TIMEOUT_FIGURE_WIDTH, showTimeout ? TIMEOUT_FIGURE_HEIGHT : TIMEOUT_FIGURE_HEIGHT - 14);
    }

    /**
     * yzhang Comment method "updateData".
     */
    @Override
    public void updateData() {

        showTitle = (Boolean) subjobContainer.getPropertyValue(EParameterName.SHOW_SUBJOB_TITLE.getName());
        List<? extends INode> nodes = subjobContainer.getProcess().getNodesOfType("tSparkConfiguration"); //$NON-NLS-1$
        INode sparkConf = (nodes != null && nodes.size() > 0) ? nodes.get(0) : null;
        showTimeout = "true".equals(ElementParameterParser.getValue(sparkConf, "__DEFINE_DURATION__")); //$NON-NLS-1$ //$NON-NLS-2$
        String timeout = (showTimeout ? "<br><b>Duration:</b> " + NodeContainerUtils.formatTime(ElementParameterParser.getValue(sparkConf, "__STREAMING_DURATION__")) : ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        timeoutFigureText.setSize(145, showTimeout ? 30 : 18);
        timeoutFigureRect.setSize(150, showTimeout ? 40 : 26);

        timeoutText = "<b>Batch size:</b> " + NodeContainerUtils.formatTime(ElementParameterParser.getValue(sparkConf, "__STREAMING_BATCH_SIZE__")) + timeout; //$NON-NLS-1$ //$NON-NLS-2$

        title = (String) subjobContainer.getPropertyValue(EParameterName.SUBJOB_TITLE.getName());
        if (subjobContainer.getSubjobStartNode().getComponent().getName().equals("tPrejob")) { //$NON-NLS-1$
            title = " Prejob:" + title; //$NON-NLS-1$
            subjobContainer.getElementParameter(EParameterName.SHOW_SUBJOB_TITLE.getName()).setShow(false);
        } else if (subjobContainer.getSubjobStartNode().getComponent().getName().equals("tPostjob")) { //$NON-NLS-1$
            title = " Postjob:" + title; //$NON-NLS-1$
            subjobContainer.getElementParameter(EParameterName.SHOW_SUBJOB_TITLE.getName()).setShow(false);
        } else {
            subjobContainer.getElementParameter(EParameterName.SHOW_SUBJOB_TITLE.getName()).setShow(true);
        }
        String propertyValue = (String) subjobContainer.getPropertyValue(EParameterName.SUBJOB_TITLE_COLOR.getName());
        if (propertyValue == null || "".equals(propertyValue)) { //$NON-NLS-1$
            RGB colorValue = DesignerColorUtils.getPreferenceSubjobRGB(DesignerColorUtils.SUBJOB_TITLE_COLOR_NAME,
                    DesignerColorUtils.SUBJOB_TITLE_COLOR);
            subjobContainer.setPropertyValue(EParameterName.SUBJOB_TITLE_COLOR.getName(), ColorUtils.getRGBValue(colorValue));
        }
        //
        propertyValue = (String) subjobContainer.getPropertyValue(EParameterName.SUBJOB_COLOR.getName());
        if (propertyValue == null || "".equals(propertyValue)) { //$NON-NLS-1$
            RGB colorValue = DesignerColorUtils.getPreferenceSubjobRGB(DesignerColorUtils.SUBJOB_COLOR_NAME,
                    DesignerColorUtils.SUBJOB_COLOR);
            subjobContainer.setPropertyValue(EParameterName.SUBJOB_COLOR.getName(), ColorUtils.getRGBValue(colorValue));
        }

        mainColor = ColorUtils.parseStringToRGB(propertyValue, DesignerColorUtils.SUBJOB_COLOR);

        this.getChildren().remove(outlineFigure);
        this.getChildren().remove(rectFig);
        outlineFigure.getChildren().clear();
        rectFig.getChildren().clear();
        timeoutFigureRect.getChildren().clear();

        timeoutFigureRect.add(timeoutFigureText);
        rectFig.add(timeoutFigureRect);
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
