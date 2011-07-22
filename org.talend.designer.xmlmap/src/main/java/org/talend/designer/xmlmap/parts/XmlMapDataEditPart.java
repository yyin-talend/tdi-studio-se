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
package org.talend.designer.xmlmap.parts;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.ScrollPane;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.talend.designer.xmlmap.figures.SashSeparator;
import org.talend.designer.xmlmap.figures.layout.EqualWidthLayout;
import org.talend.designer.xmlmap.figures.layout.TreeContainerLayout;
import org.talend.designer.xmlmap.figures.layout.XmlMapDataLayout;
import org.talend.designer.xmlmap.figures.treetools.zone.InputZoneToolBar;
import org.talend.designer.xmlmap.figures.treetools.zone.OutputZoneToolBar;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlMapData;
import org.talend.designer.xmlmap.ui.resource.ColorInfo;
import org.talend.designer.xmlmap.ui.resource.ColorProviderMapper;
import org.talend.designer.xmlmap.ui.resource.ImageInfo;
import org.talend.designer.xmlmap.ui.resource.ImageProviderMapper;
import org.talend.designer.xmlmap.util.SeparatorType;

/**
 * wchen class global comment. Detailled comment
 */
public class XmlMapDataEditPart extends BaseEditPart {

    private IFigure leftFigure;

    private IFigure centerFigure;

    private IFigure rightFigure;

    private ScrollPane inputScroll;

    private ScrollPane varScroll;

    private ScrollPane outputScroll;

    private OutputZoneToolBar outputToolBar;

    private InputZoneToolBar inputToolBar;

    @Override
    protected IFigure createFigure() {
        Figure mainFigure = new Figure();
        XmlMapDataLayout manager2 = new XmlMapDataLayout(this);
        mainFigure.setLayoutManager(manager2);

        // input
        Figure inputZone = new Figure();
        inputZone.setLayoutManager(new InOutZoneLayout());

        inputToolBar = new InputZoneToolBar(this);
        inputToolBar.setBorder(new LineBorder(ColorProviderMapper.getColor(ColorInfo.COLOR_TREE_BORDER)));
        inputZone.add(inputToolBar);

        inputScroll = new ScrollPane();
        inputScroll.setHorizontalScrollBarVisibility(ScrollPane.NEVER);
        leftFigure = new Figure();
        // ToolbarLayout subManager = new ToolbarLayout();
        ToolbarLayout subManager = new TreeContainerLayout();
        subManager.setSpacing(20);
        subManager.setVertical(true);
        leftFigure.setLayoutManager(subManager);
        leftFigure.setBorder(new MarginBorder(20, 40, 20, 40));
        inputScroll.getViewport().setContents(leftFigure);
        inputScroll.getViewport().setContentsTracksWidth(true);

        inputZone.add(inputScroll);
        mainFigure.add(inputZone);

        // separator 1
        SashSeparator separatorLeft = new SashSeparator();
        separatorLeft.setType(SeparatorType.ZONE_SEPARATOR);
        separatorLeft.setImage(ImageProviderMapper.getImage(ImageInfo.ZONE_SASH));
        mainFigure.add(separatorLeft);

        // var
        varScroll = new ScrollPane();
        varScroll.setHorizontalScrollBarVisibility(ScrollPane.NEVER);
        centerFigure = new Figure();

        subManager = new ToolbarLayout();
        subManager.setSpacing(20);
        subManager.setVertical(true);
        centerFigure.setLayoutManager(subManager);
        centerFigure.setBorder(new MarginBorder(5, 60, 5, 60));
        varScroll.getViewport().setContents(centerFigure);

        mainFigure.add(varScroll);

        // separetor 2
        SashSeparator separatorRight = new SashSeparator();
        separatorRight.setType(SeparatorType.ZONE_SEPARATOR);
        separatorRight.setImage(ImageProviderMapper.getImage(ImageInfo.ZONE_SASH));
        mainFigure.add(separatorRight);

        // output
        Figure outputZone = new Figure();
        outputZone.setLayoutManager(new InOutZoneLayout());

        outputToolBar = new OutputZoneToolBar(this);
        outputToolBar.setBorder(new LineBorder(ColorProviderMapper.getColor(ColorInfo.COLOR_TREE_BORDER)));
        outputZone.add(outputToolBar);
        outputScroll = new ScrollPane();
        outputScroll.setHorizontalScrollBarVisibility(ScrollPane.NEVER);
        rightFigure = new Figure();

        subManager = new TreeContainerLayout();
        subManager.setSpacing(20);
        subManager.setVertical(true);
        rightFigure.setLayoutManager(subManager);
        rightFigure.setBorder(new MarginBorder(20, 40, 20, 40));
        outputScroll.getViewport().setContents(rightFigure);
        outputScroll.getViewport().setContentsTracksWidth(true);
        outputZone.add(outputScroll);
        mainFigure.add(outputZone);

        mainFigure.setOpaque(true);
        mainFigure.setBackgroundColor(ColorProviderMapper.getColor(ColorInfo.COLOR_BACKGROUND_LINKS_ZONE));

        separatorLeft.setLeftFigure(inputZone);
        separatorLeft.setRightFigure(varScroll);
        separatorLeft.setParentFigure(mainFigure);

        separatorRight.setLeftFigure(varScroll);
        separatorRight.setRightFigure(outputZone);
        separatorRight.setParentFigure(mainFigure);

        return mainFigure;
    }

    @Override
    protected void addChildVisual(EditPart childEditPart, int index) {
        IFigure child = ((GraphicalEditPart) childEditPart).getFigure();

        if (childEditPart instanceof InputXmlTreeEditPart) {
            /* get first figure to put all input tables figures in */
            Object model = childEditPart.getModel();
            index = ((XmlMapData) getModel()).getInputTrees().indexOf(model);
            if (index != -1) {
                leftFigure.add(child, index);
            } else {
                leftFigure.add(child);
            }

        }
        if (childEditPart instanceof OutputXmlTreeEditPart) {
            /* get third figure to put all output tables figures in */
            Object model = childEditPart.getModel();
            index = ((XmlMapData) getModel()).getOutputTrees().indexOf(model);
            if (index != -1) {
                rightFigure.add(child, index);
            } else {
                rightFigure.add(child);
            }
        }
        if (childEditPart instanceof VarTableEditPart) {
            Object model = childEditPart.getModel();
            index = ((XmlMapData) getModel()).getVarTables().indexOf(model);
            if (index != -1) {
                centerFigure.add(child, index);
            } else {
                centerFigure.add(child);
            }
        }

    }

    @Override
    protected void removeChildVisual(EditPart childEditPart) {
        IFigure child = ((GraphicalEditPart) childEditPart).getFigure();
        if (childEditPart instanceof InputXmlTreeEditPart) {
            leftFigure.remove(child);
        }
        if (childEditPart instanceof OutputXmlTreeEditPart) {
            rightFigure.remove(child);
        }
        if (childEditPart instanceof VarTableEditPart) {
            centerFigure.remove(child);
        }
    }

    @Override
    public List getModelChildren() {
        List children = new ArrayList();
        children.addAll(((XmlMapData) getModel()).getInputTrees());
        children.addAll(((XmlMapData) getModel()).getOutputTrees());
        children.addAll(((XmlMapData) getModel()).getVarTables());
        return children;
    }

    public OutputZoneToolBar getOutputZoneToolBar() {
        return this.outputToolBar;
    }

    public InputZoneToolBar getInputZoneToolBar() {
        return this.inputToolBar;
    }

    @Override
    public void notifyChanged(Notification notification) {
        int eventType = notification.getEventType();
        switch (eventType) {
        case Notification.ADD:
        case Notification.REMOVE:
        case Notification.REMOVE_MANY:
            refreshChildren();
            break;

        default:
            break;
        }
    }

    class InOutZoneLayout extends EqualWidthLayout {

        private static final int TOOL_BAR_HEIGHT = 30;

        @Override
        public void layout(IFigure parent) {
            List children2 = parent.getChildren();
            if (children2.size() != 2) {
                super.layout(parent);
            }
            org.eclipse.swt.graphics.Point avilableSize = getViewer().getControl().getSize();
            Rectangle clientArea = parent.getClientArea();
            int x = clientArea.x;
            int y = clientArea.y;
            Figure toolBarFigure = (Figure) children2.get(0);
            Rectangle newBounds = new Rectangle(x, y, clientArea.width, TOOL_BAR_HEIGHT);
            toolBarFigure.setBounds(newBounds);

            Figure scroll = (Figure) children2.get(1);
            Rectangle bounds = new Rectangle(x, y + TOOL_BAR_HEIGHT, clientArea.width, avilableSize.y - TOOL_BAR_HEIGHT);
            scroll.setBounds(bounds);

        }
    }

}
