package org.talend.designer.business.diagram.custom.util;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import org.apache.batik.svggen.SVGGraphics2DIOException;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.LayerManager;
import org.eclipse.gef.editparts.SimpleRootEditPart;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.export.GraphicsSVG;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;

/**
 * SVG exporter.
 */
public class SVGImageExporter {

    public static void export(GraphicalViewer viewer, OutputStream outputStream, List businessItems) {
        /*
         * 1. First get the figure whose visuals we want to save as image. So we would like to save the rooteditpart
         * which actually hosts all the printable layers.
         * 
         * NOTE: ScalableRootEditPart manages layers and is registered graphicalviewer's editpartregistry with the key
         * LayerManager.ID ... well that is because ScalableRootEditPart manages all layers that are hosted on a
         * FigureCanvas. Many layers exist for doing different things
         */
        SimpleRootEditPart rootEditPart = (SimpleRootEditPart) viewer.getEditPartRegistry().get(LayerManager.ID);
        IFigure rootFigure = ((LayerManager) rootEditPart).getLayer(LayerConstants.PRINTABLE_LAYERS);// rootEditPart.
        // getFigure();
        Rectangle bounds = rootFigure.getBounds();
        GraphicsSVG graphics = GraphicsSVG.getInstance(bounds.getTranslated(bounds.getLocation().negate()));

        TalendSVGIDGenerator generator = new TalendSVGIDGenerator(businessItems);

        graphics.getSVGGraphics2D().getGeneratorContext().setIDGenerator(generator);
        graphics.translate(bounds.getLocation().negate());
        rootFigure.paint(graphics);
        try {
            graphics.getSVGGraphics2D().stream(new BufferedWriter(new OutputStreamWriter(outputStream)));
        } catch (SVGGraphics2DIOException e) {
            ExceptionHandler.process(e);
        }
    }

}
