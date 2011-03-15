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

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.ScrollPane;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.talend.designer.xmlmap.figures.layout.EqualWidthLayout;
import org.talend.designer.xmlmap.figures.layout.TreeContainerLayout;
import org.talend.designer.xmlmap.figures.layout.XmlMapDataLayout;
import org.talend.designer.xmlmap.figures.treetools.zone.InputZoneToolBar;
import org.talend.designer.xmlmap.figures.treetools.zone.OutputZoneToolBar;
import org.talend.designer.xmlmap.model.emf.xmlmap.InputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlMapData;
import org.talend.designer.xmlmap.ui.resource.ColorInfo;
import org.talend.designer.xmlmap.ui.resource.ColorProviderMapper;

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

    private ISelectionChangedListener selectionListener;

    private static int IN_OUT_BORDER = 20;

    @Override
    protected IFigure createFigure() {
        Figure mainFigure = new Figure();

        XmlMapDataLayout manager2 = new XmlMapDataLayout(this);
        mainFigure.setLayoutManager(manager2);

        // input
        Figure inputZone = new Figure();
        inputZone.setLayoutManager(new InOutZoneLayout());

        inputToolBar = new InputZoneToolBar(this);
        inputZone.add(inputToolBar);

        inputScroll = new ScrollPane();
        inputScroll.setHorizontalScrollBarVisibility(ScrollPane.NEVER);
        leftFigure = new RectangleFigure();
        leftFigure.setBorder(new LineBorder(ColorConstants.darkBlue));
        // ToolbarLayout subManager = new ToolbarLayout();
        ToolbarLayout subManager = new TreeContainerLayout();
        subManager.setSpacing(20);
        subManager.setVertical(true);
        leftFigure.setLayoutManager(subManager);
        leftFigure.setBorder(new MarginBorder(IN_OUT_BORDER));
        inputScroll.getViewport().setContents(leftFigure);
        inputZone.add(inputScroll);
        mainFigure.add(inputZone);

        // var
        varScroll = new ScrollPane();
        varScroll.setHorizontalScrollBarVisibility(ScrollPane.NEVER);
        centerFigure = new RectangleFigure();
        // GridLayout centerLayout = new GridLayout();
        // centerFigure.setLayoutManager(centerLayout);
        subManager = new ToolbarLayout();
        subManager.setSpacing(20);
        subManager.setVertical(true);
        centerFigure.setLayoutManager(subManager);
        centerFigure.setBorder(new MarginBorder(5, 60, 5, 60));
        varScroll.getViewport().setContents(centerFigure);
        mainFigure.add(varScroll);

        // output
        Figure outputZone = new Figure();
        outputZone.setLayoutManager(new InOutZoneLayout());

        outputToolBar = new OutputZoneToolBar(this);
        if (!((XmlMapData) getModel()).getOutputTrees().isEmpty()) {
            outputToolBar.setRemoveButtonEnable(true);
        }
        outputZone.add(outputToolBar);
        outputScroll = new ScrollPane();
        outputScroll.setHorizontalScrollBarVisibility(ScrollPane.NEVER);
        rightFigure = new RectangleFigure();
        rightFigure.setBorder(new LineBorder(ColorConstants.darkBlue));

        // subManager = new ToolbarLayout();
        subManager = new TreeContainerLayout();
        subManager.setSpacing(20);
        subManager.setVertical(true);
        rightFigure.setLayoutManager(subManager);
        rightFigure.setBorder(new MarginBorder(IN_OUT_BORDER));
        outputScroll.getViewport().setContents(rightFigure);
        outputZone.add(outputScroll);
        mainFigure.add(outputZone);

        mainFigure.setOpaque(true);
        mainFigure.setBackgroundColor(ColorProviderMapper.getColor(ColorInfo.COLOR_BACKGROUND_LINKS_ZONE));
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

    public IFigure getLeftFigure() {
        return this.inputScroll;
    }

    public IFigure getCenterFigure() {
        return this.varScroll;
    }

    public IFigure getRightFigure() {
        return this.outputScroll;
    }

    @Override
    public void activate() {
        super.activate();

        selectionListener = new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                List selectedEditParts = getViewer().getSelectedEditParts();
                boolean outputRemoveEnable = false;
                boolean outputMoveUp = false;
                boolean outputMoveDown = false;
                boolean inputMoveUp = false;
                boolean inputMoveDown = false;
                EList<OutputXmlTree> outputTrees = ((XmlMapData) getModel()).getOutputTrees();
                EList<InputXmlTree> inputTrees = ((XmlMapData) getModel()).getInputTrees();

                /*
                 * if there is one can't be moved in selectedEditParts ,force disable and no need to check the left
                 */
                boolean forceDisableOutputMoveup = false;
                boolean forceDisableOutputMoveDown = false;

                boolean forceDisableInputMoveup = false;
                boolean forceDisableInputMoveDown = false;

                for (Object obj : selectedEditParts) {
                    if (obj instanceof OutputXmlTreeEditPart) {
                        outputRemoveEnable = true;
                        int index = outputTrees.indexOf(((OutputXmlTreeEditPart) obj).getModel());
                        if (!forceDisableOutputMoveup) {
                            if (index != -1 && index != 0) {
                                outputMoveUp = true;
                            } else {
                                forceDisableOutputMoveup = true;
                                outputMoveUp = false;
                            }
                        }
                        if (!forceDisableOutputMoveDown) {
                            if (index != -1 && index != outputTrees.size() - 1) {
                                outputMoveDown = true;
                            } else {
                                forceDisableOutputMoveDown = true;
                                outputMoveDown = false;
                            }
                        }
                    } else if (obj instanceof InputXmlTreeEditPart) {
                        InputXmlTree tree = (InputXmlTree) ((InputXmlTreeEditPart) obj).getModel();

                        if (!tree.isLookup()) {
                            forceDisableInputMoveup = true;
                            forceDisableInputMoveDown = true;
                            inputMoveUp = false;
                            inputMoveDown = false;
                        }

                        if (tree.isLookup()) {
                            int indexOf = inputTrees.indexOf(tree);
                            if (!forceDisableInputMoveup) {
                                if (indexOf != -1 && indexOf > 1) {
                                    inputMoveUp = true;
                                } else {
                                    forceDisableInputMoveup = true;
                                    inputMoveUp = false;
                                }
                            }
                            if (!forceDisableInputMoveDown) {
                                if (indexOf > 0 && indexOf != inputTrees.size() - 1) {
                                    inputMoveDown = true;
                                } else {
                                    forceDisableInputMoveDown = true;
                                    inputMoveDown = false;
                                }
                            }

                        }

                    }
                }
                outputToolBar.setRemoveButtonEnable(outputRemoveEnable);
                outputToolBar.setMoveUpEnable(outputMoveUp);
                outputToolBar.setMoveDownEnable(outputMoveDown);

                inputToolBar.setMoveDownEnable(inputMoveDown);
                inputToolBar.setMoveUpEnable(inputMoveUp);

            }

        };
        getViewer().addSelectionChangedListener(selectionListener);
    }

    @Override
    public void deactivate() {
        super.deactivate();
        getViewer().removeSelectionChangedListener(selectionListener);
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

        private static final int TOOL_BAR_HEIGHT = 40;

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
