// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2007 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
//
// ============================================================================
package org.talend.designer.core.ui.editor;

import org.eclipse.draw2d.ConnectionLayer;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayeredPane;
import org.eclipse.draw2d.LayeredPane;
import org.eclipse.draw2d.ScalableFigure;
import org.eclipse.draw2d.ScalableFreeformLayeredPane;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.editparts.GridLayer;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gmf.runtime.diagram.ui.internal.editparts.ZoomableEditPart;
import org.eclipse.gmf.runtime.gef.ui.internal.editparts.AnimatableZoomManager;
import org.eclipse.ui.IEditorInput;

/**
 * Modification of the default RootEditPart to add the possibility to change the color of the background and change the
 * grid.
 * 
 * $Id$
 * 
 */
public class TalendScalableFreeformRootEditPart extends ScalableFreeformRootEditPart implements ZoomableEditPart {

    public static final String PROCESS_BACKGROUND_LAYER = "processBackgroundLayer"; //$NON-NLS-1$

    private IEditorInput editorInput;

    private AnimatableZoomManager zoomManager;

    private double[] zoomLevels = { .05, .1, .25, .5, .75, 1, 1.25, 1.5, 1.75, 2, 4 };

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editparts.ScalableFreeformRootEditPart#getZoomManager()
     */
    @Override
    public ZoomManager getZoomManager() {
        if (zoomManager == null) {
            zoomManager = new AnimatableZoomManager((ScalableFigure) getScaledLayers(), ((Viewport) getFigure()));
            zoomManager.setZoomLevels(zoomLevels);
            zoomManager.setZoomAnimationStyle(ZoomManager.ANIMATE_ZOOM_IN_OUT);
        }
        return zoomManager;
        // return super.getZoomManager();
    }

    public TalendScalableFreeformRootEditPart(IEditorInput editorInput) {
        this.editorInput = editorInput;
    }

    @Override
    protected LayeredPane createPrintableLayers() {
        FreeformLayeredPane layeredPane = new FreeformLayeredPane();
        layeredPane.add(new FreeformLayer(), PRIMARY_LAYER);
        layeredPane.add(new ConnectionLayer(), CONNECTION_LAYER);
        return layeredPane;
    }

    @Override
    protected GridLayer createGridLayer() {
        return new TalendGridLayer();
    }

    protected ScalableFreeformLayeredPane createScaledLayers() {
        ScalableFreeformLayeredPane layers = new ScalableFreeformLayeredPane();
        layers.add(new FreeformLayer(), PROCESS_BACKGROUND_LAYER);
        layers.add(createGridLayer(), GRID_LAYER);
        layers.add(getPrintableLayers(), PRINTABLE_LAYERS);
        layers.add(new FeedbackLayer(), SCALED_FEEDBACK_LAYER);
        return layers;
    }

    /**
     * Modification fo the default Layer. <br/>
     * 
     * $Id$
     * 
     */
    class FeedbackLayer extends FreeformLayer {

        FeedbackLayer() {
            setEnabled(false);
        }
    }

    public IEditorInput getEditorInput() {
        return editorInput;
    }

    public void zoomIn() {
        zoomManager.zoomIn();
    }

    public void zoomIn(Point center) {
        zoomManager.zoomTo(zoomManager.getNextZoomLevel(), center);
    }

    public void zoomOut() {
        zoomManager.zoomOut();
    }

    public void zoomOut(Point center) {
        zoomManager.zoomTo(zoomManager.getPreviousZoomLevel(), center);
    }

    public void zoomTo(Rectangle rect) {
        zoomManager.zoomTo(rect);
    }

    public void zoomTo(double zoom, Point center) {
        zoomManager.zoomTo(zoom, center);
    }
}
