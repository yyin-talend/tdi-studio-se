// ============================================================================
//
// Copyright (C) 2006-2013 Talend Inc. - www.talend.com
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

import org.eclipse.draw2d.FigureListener;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editpolicies.SelectionEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.handles.ConnectionHandleLocator;
import org.talend.designer.core.ui.editor.connections.TalendConnectionHandle;

/**
 * DOC Talend class global comment. Detailled comment
 */
public class SelectionFeedbackEditPolicy extends SelectionEditPolicy implements LayerConstants {

    public static final Object TALEND_SELECTION_FEEDBACK_ROLE = "TalendSelectionFeedback";

    public static final String TALEND_FEEDBACK_LAYER = "TalendFeedbackLayer";

    private IFigure layer;

    private FigureListener figureListener;

    private IFigure sourceFigure;

    private NodePart nodePart;

    private TalendConnectionHandle handle = null;

    public SelectionFeedbackEditPolicy(NodePart nodePart) {
        this.nodePart = nodePart;
        this.figureListener = new FigureListener() {

            @Override
            public void figureMoved(IFigure source) {
                List selectedEditPart = getHost().getViewer().getSelectedEditParts();
                if (selectedEditPart.contains(getHost())) {
                    hideSelection();
                    showFeedback();
                }

            }
        };
    }

    @Override
    public void activate() {
        super.activate();
    }

    @Override
    public void showSelection() {
        if (this.sourceFigure == null) {
            this.sourceFigure = getHostFigure();
            this.sourceFigure.addFigureListener(this.figureListener);
        }
        showFeedback();
    }

    void showFeedback() {
        Rectangle bounds;
        if (this.layer == null) {
            this.layer = getLayer(TALEND_FEEDBACK_LAYER);
        }
        bounds = this.nodePart.getFigure().getBounds();
        Point center = bounds.getCenter();
        Point referencePoint = new Point(center.x + bounds.width / 2, center.y);
        if (figureIsDisplayed()) {
            return;
        }

        ConnectionHandleLocator locator = new ConnectionHandleLocator(getHostFigure(), referencePoint);

        if (getHost().getModel() instanceof Node) {
            // if (handle == null) {
            handle = new TalendConnectionHandle(this.nodePart);
            handle.setLocator(locator);
            layer.add(handle);
            // }

            // locator.addHandle(handle);
            // handle.addMouseMotionListener(this);

            // Register this figure with it's host editpart so mouse events
            // will be propagated to it's host.
            getHost().getViewer().getVisualPartMap().put(handle, getHost());
        }

    }

    @Override
    public void hideSelection() {
        hideFeedback();
    }

    private void hideFeedback() {
        if (figureIsDisplayed()) {
            this.layer.remove(this.handle);
        }
    }

    private boolean figureIsDisplayed() {
        return ((this.handle != null) && (this.layer != null) && (this.layer.getChildren().contains(this.handle)));
    }

    @Override
    public void deactivate() {
        super.deactivate();
    }

    @Override
    protected IFigure getLayer(Object layer) {
        return super.getLayer(layer);
    }

    public void performCollapse() {
        hideFeedback();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editpolicies.SelectionEditPolicy#addSelectionListener()
     */
    @Override
    protected void addSelectionListener() {
        super.addSelectionListener();
    }
}
