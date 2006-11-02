// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
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
package org.talend.designer.mapper.ui.visualmap.link;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.talend.designer.mapper.geometry.Curve2DBezier;
import org.talend.designer.mapper.geometry.Point2D;
import org.talend.designer.mapper.geometry.Point2DList;
import org.talend.designer.mapper.managers.MapperManager;
import org.talend.designer.mapper.managers.UIManager;
import org.talend.designer.mapper.model.tableentry.ConstraintTableEntry;
import org.talend.designer.mapper.model.tableentry.IColumnEntry;
import org.talend.designer.mapper.ui.color.ColorInfo;
import org.talend.designer.mapper.ui.color.ColorProviderMapper;
import org.talend.designer.mapper.ui.visualmap.table.DataMapTableView;
import org.talend.designer.mapper.ui.visualmap.zone.Zone;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class AbstractLink implements IGraphicLink {

    private static final boolean BEZIER_MODE = true;

    private static final int NORMAL_CONNECTOR_WIDTH = 20;

    private static final int BEZIER_CONNECTOR_WIDTH = 70;

    private PointLinkDescriptor pointDescriptor1;

    private PointLinkDescriptor pointDescriptor2;

    private Point point1;

    private Point point2;

    private LinkState linkState;

    private Curve2DBezier curve;

    private UIManager uiManager;

    private MapperManager mapperManager;

    private int widthTable1;

    private int widthTable2;

    private static Color normalLinkColor;

    private static Color highlightLinkColor;

    private static final int DISTANCE_REFERENCE = 20000;

    private static final int SIDE_CONNECTOR_WIDTH = 5;

    public static int keyLinksCounter;

    public AbstractLink(PointLinkDescriptor pointDescriptor1, PointLinkDescriptor pointDescriptor2, MapperManager mapperManager) {
        super();
        this.pointDescriptor1 = pointDescriptor1;
        this.pointDescriptor2 = pointDescriptor2;
        this.mapperManager = mapperManager;
        this.uiManager = mapperManager.getUiManager();
        calculate();

    }

    /**
     * DOC amaumont Comment method "calculate".
     */
    public void calculate() {
        point1 = this.uiManager.getTableEntryPosition(pointDescriptor1.getTableEntry(), true);
        point2 = this.uiManager.getTableEntryPosition(pointDescriptor2.getTableEntry(), true);
        DataMapTableView viewTable1 = this.mapperManager.retrieveDataMapTableView(this.mapperManager
                .retrieveTable(pointDescriptor1.getTableEntry()));
        DataMapTableView viewTable2 = this.mapperManager.retrieveDataMapTableView(this.mapperManager
                .retrieveTable(pointDescriptor2.getTableEntry()));
        widthTable1 = viewTable1.getBounds().width + 2 * viewTable1.getBorderWidth();
        widthTable2 = viewTable2.getBounds().width + 2 * viewTable2.getBorderWidth();
    }

    @Override
    public String toString() {

        return pointDescriptor1 + " " + pointDescriptor2;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.ui.datamap.linking.GraphicLink#draw(org.eclipse.swt.graphics.GC, int, int, int)
     */
    public void draw(GC gc, int yMinVisiblePoints, int yMaxVisiblePoints) {

        ColorInfo foregroundColorInfo = null;
        if (linkState == LinkState.SELECTED && pointDescriptor2.getTableEntry() instanceof IColumnEntry) {
            foregroundColorInfo = ColorInfo.COLOR_LINK_HIGHLIGHTED;
        } else if (linkState == LinkState.SELECTED && pointDescriptor2.getTableEntry() instanceof ConstraintTableEntry) {
            foregroundColorInfo = ColorInfo.COLOR_LINK_CONSTRAINT;
        } else {
            foregroundColorInfo = ColorInfo.COLOR_UNSELECTED_METADATA_LINK;
        }
        gc.setForeground(ColorProviderMapper.getColor(foregroundColorInfo));

        Zone zone1 = pointDescriptor1.getZone();
        Zone zone2 = pointDescriptor2.getZone();

        int yOffsetPoint1 = this.uiManager.getVerticalScrolledOffsetBar(zone1);
        int yOffsetPoint2 = this.uiManager.getVerticalScrolledOffsetBar(zone2);

        boolean leftSidePosition = ((zone1 == Zone.INPUTS || zone1 == Zone.VARS) && zone1 == zone2);

        // System.out.println("halfTableWidth1="+halfTableWidth1);
        // System.out.println("halfTableWidth2="+halfTableWidth2);

        // int point1x_halfTableWidth1 = (int)Math.floor(point1.x + (leftSidePosition ? 0 : endTable1));
        int point1x_forTraceLink = point1.x + (leftSidePosition ? 0 : widthTable1 - 4);
        int point2x_forTraceLink = point2.x + (leftSidePosition ? 0 : 4);

        int point1x_forTraceArrow = point1.x + (leftSidePosition ? 0 : widthTable1 - 4);
        int point2x_forTraceArrow = point2.x + (leftSidePosition ? 0 : 0);

        // gc.drawLine(point1x_halfTableWidth1, 0, point1x_halfTableWidth1, 1000);
        // gc.drawLine(point2x_halfTableWidth2, 0, point2x_halfTableWidth2, 1000);

        int point1y_yOffsetPoint1 = point1.y + yOffsetPoint1;
        int point2y_yOffsetPoint2 = point2.y + yOffsetPoint2;

        double distance;
        // int spaceBetweenPoints = 1;

        if (BEZIER_MODE) {
            distance = new java.awt.Point(point1x_forTraceLink, point1y_yOffsetPoint1).distance(point2x_forTraceLink,
                    point2y_yOffsetPoint2);
            // //System.out.println("distance= "+distance);
            // spaceBetweenPoints = (int) ((double) (100d * (double) MAX_SPACE_BETWEEN_POINTS) / distance);
            // // //System.out.println("spaceBetweenPoints = " +spaceBetweenPoints);
            // if (spaceBetweenPoints < 1) {
            // spaceBetweenPoints = 1;
            // }
            // if (spaceBetweenPoints > MAX_SPACE_BETWEEN_POINTS) {
            // spaceBetweenPoints = MAX_SPACE_BETWEEN_POINTS;
            // }

        }

        int heightRefComposite = this.uiManager.getReferenceComposite().getSize().y;

        if (point1y_yOffsetPoint1 > heightRefComposite || point2y_yOffsetPoint2 > heightRefComposite || point1y_yOffsetPoint1 < 0
                || point2y_yOffsetPoint2 < 0) {
            // gc.setLineDash(new int[] { 1, spaceBetweenPoints, 1 });
            gc.setLineStyle(SWT.LINE_DOT);
            // gc.setLineStyle(SWT.LINE_CUSTOM);
            // System.out.println("SWT.LINE_DOT");
        } else {
            gc.setLineStyle(SWT.LINE_SOLID);
            // System.out.println("SWT.LINE_SOLID");
        }

        // gc.drawLine(point1.x, 0,
        // point1.x, 1000);
        // gc.drawLine(point2.x, 0,
        // point2.x, 1000);
        // gc.drawLine(point1x_halfTableWidth1, 0,
        // point1x_halfTableWidth1, 1000);
        // gc.drawLine(point2x_halfTableWidth2, 0,
        // point2x_halfTableWidth2, 1000);
        long timeDrawLines = System.currentTimeMillis();
        if (BEZIER_MODE && !leftSidePosition) {

            Point2DList pl = null;

            if (curve == null) {
                curve = new Curve2DBezier();
                pl = new Point2DList();
                curve.setPointList(pl);
                for (int i = 0; i < 5; i++) {
                    pl.add(new Point2D());
                }
            } else {
                pl = (Point2DList) curve.getPointList();
            }

            // System.out.println("distance = " + distance);
            // System.out.println("subdiv = " + subdiv);
            curve.setSubdiv((int) (DISTANCE_REFERENCE / distance));
            // double coefDist = distance / 1500;
            double coefDist_connectorWidth = distance / 1500 * BEZIER_CONNECTOR_WIDTH;

            int point1x_halfTableWidth1_connWidth = point1x_forTraceLink + BEZIER_CONNECTOR_WIDTH;
            int point2x_halfTableWidth2_connWidth = point2x_forTraceLink - BEZIER_CONNECTOR_WIDTH;

            ((Point2D) pl.get(0)).setLocation(point1x_forTraceLink, point1y_yOffsetPoint1);
            ((Point2D) pl.get(1)).setLocation(point1x_halfTableWidth1_connWidth + coefDist_connectorWidth, point1y_yOffsetPoint1);

            ((Point2D) pl.get(2))
                    .setLocation(
                            (point1x_halfTableWidth1_connWidth + coefDist_connectorWidth + (point2x_halfTableWidth2_connWidth - coefDist_connectorWidth)) / 2,
                            (point1y_yOffsetPoint1 + (point2y_yOffsetPoint2)) / 2);

            ((Point2D) pl.get(3)).setLocation(point2x_halfTableWidth2_connWidth - coefDist_connectorWidth, point2y_yOffsetPoint2);
            ((Point2D) pl.get(4)).setLocation(point2x_forTraceLink, point2y_yOffsetPoint2);

            // //System.out.println("");
            // //System.out.println(DISTANCE_REFERENCE / distance);
            // //System.out.println(distance);

            curve.draw(gc, pl, pl, yMinVisiblePoints, yMaxVisiblePoints);

        } else if (!BEZIER_MODE && !leftSidePosition) {
            // long timeDrawLine1 = System.currentTimeMillis();
            // gc.drawLine(point1x_halfTableWidth1, point1y_yOffsetPoint1, point1x_halfTableWidth1 +
            // NORMAL_CONNECTOR_WIDTH,
            // point1y_yOffsetPoint1);
            // System.out.println("Time Draw Line 1 :" + (System.currentTimeMillis() - timeDrawLine1) + " ms");
            // // // in1 pr
            // long timeDrawLine2 = System.currentTimeMillis();
            // gc.drawLine(point1x_halfTableWidth1 + NORMAL_CONNECTOR_WIDTH, point1y_yOffsetPoint1,
            // point2x_halfTableWidth2
            // - NORMAL_CONNECTOR_WIDTH, point2y_yOffsetPoint2);
            // System.out.println("Time Draw Line 2 :" + (System.currentTimeMillis() - timeDrawLine2) + " ms");
            // // // connector pr (in)
            // long timeDrawLine3 = System.currentTimeMillis();
            // gc.drawLine(point2x_halfTableWidth2 - NORMAL_CONNECTOR_WIDTH, point2y_yOffsetPoint2,
            // point2x_halfTableWidth2,
            // point2y_yOffsetPoint2);
            // System.out.println("Time Draw Line 3 :" + (System.currentTimeMillis() - timeDrawLine3) + " ms");

            long timeDrawPolyLine = System.currentTimeMillis();
            gc.drawPolyline(new int[] { point1x_forTraceLink, point1y_yOffsetPoint1,
                    point1x_forTraceLink + NORMAL_CONNECTOR_WIDTH, point1y_yOffsetPoint1,
                    point2x_forTraceLink - NORMAL_CONNECTOR_WIDTH, point2y_yOffsetPoint2, point2x_forTraceLink,
                    point2y_yOffsetPoint2 });
            // if(System.currentTimeMillis() - timeDrawPolyLine > 10) {
            // System.out.println("Time Draw PolyLine :" + (System.currentTimeMillis() - timeDrawPolyLine) + " ms");
            // System.out.println("zone1 :" + zone1 + " zone2:"+zone2);
            // }
        } else if (leftSidePosition) {

            boolean point1Above = point1y_yOffsetPoint1 > point2y_yOffsetPoint2;

            int mult = point1Above ? 1 : -1;

            int halfSizeCircle = 10;
            int xOffset = 4 * keyLinksCounter + 1;

            gc.drawLine(point1x_forTraceLink, point1y_yOffsetPoint1, point1x_forTraceLink - SIDE_CONNECTOR_WIDTH - xOffset,
                    point1y_yOffsetPoint1);

            gc.drawArc(point1x_forTraceLink - SIDE_CONNECTOR_WIDTH - halfSizeCircle - xOffset, point1y_yOffsetPoint1
                    - (point1Above ? 2 * halfSizeCircle : 0), 2 * halfSizeCircle, 2 * halfSizeCircle, point1Above ? 180 : 90, 90);

            // // in1 pr
            gc.drawLine(point1x_forTraceLink - SIDE_CONNECTOR_WIDTH - halfSizeCircle - xOffset, point1y_yOffsetPoint1 - mult
                    * halfSizeCircle, point2x_forTraceLink - SIDE_CONNECTOR_WIDTH - halfSizeCircle - xOffset,
                    point2y_yOffsetPoint2 + mult * halfSizeCircle);

            gc.drawArc(point2x_forTraceLink - SIDE_CONNECTOR_WIDTH - halfSizeCircle - xOffset, point2y_yOffsetPoint2
                    - (point1Above ? 0 : 2 * halfSizeCircle), 2 * halfSizeCircle, 2 * halfSizeCircle, point1Above ? 90 : 180, 90);

            // // connector pr (in)
            gc.drawLine(point2x_forTraceLink - SIDE_CONNECTOR_WIDTH - xOffset, point2y_yOffsetPoint2, point2x_forTraceLink,
                    point2y_yOffsetPoint2);

            keyLinksCounter++;
        }
        if (linkState == LinkState.SELECTED) {
            int widthImageArrow = 5;
            int heightImageArrow = 10;
            int widthArrow = 5;
            int heightArrow = 10;
            int FOREGROUND_INDEX_PALETTE_COLOR = 1;
            RGB transparentColor = new RGB(1, 1, 1);
            PaletteData paletteData = new PaletteData(new RGB[] { transparentColor, foregroundColorInfo.getRGB(), });
            // PaletteData paletteData = new PaletteData(1, 1, 1);
            ImageData imageData = new ImageData(widthImageArrow, heightImageArrow, 1, paletteData);

            int transparentPixel = imageData.palette.getPixel(transparentColor);
            imageData.transparentPixel = transparentPixel;
            // int foregroundPixel = imageData.palette.getPixel(foregroundColorInfo.getRGB());

            for (int i = 0; i < heightArrow / 2; i++) {
                for (int j = i; j < heightArrow / 2; j++) {
                    imageData.setPixel(i, j, FOREGROUND_INDEX_PALETTE_COLOR);
                    imageData.setPixel(i, heightArrow - j - 1, FOREGROUND_INDEX_PALETTE_COLOR);
                }
            }
            Image eastArrow = new Image(gc.getDevice(), imageData);
            int xOffset = 0;
            if (leftSidePosition) {
                xOffset = -widthArrow;
                ImageData src = imageData;
                ImageData dest = new ImageData(src.width, src.height, src.depth, new PaletteData(src.palette.getRGBs()));
                dest.transparentPixel = transparentPixel;

                /* rotate by rearranging the pixels */
                for (int i = 0; i < src.width; i++) {
                    for (int j = 0; j < src.height; j++) {
                        int pixel = src.getPixel(i, j);
                        dest.setPixel(src.width - 1 - i, j, pixel);
                    }
                }
                Image westArrow = new Image(gc.getDevice(), dest);
                gc.drawImage(westArrow, point1x_forTraceArrow + xOffset, point1y_yOffsetPoint1 - heightArrow / 2);
                westArrow.dispose();
            } else {
                gc.drawImage(eastArrow, point1x_forTraceArrow, point1y_yOffsetPoint1 - heightArrow / 2);
            }
            gc.drawImage(eastArrow, point2x_forTraceArrow - widthArrow, point2y_yOffsetPoint2 - heightArrow / 2);
            eastArrow.dispose();
        }

        // if(System.currentTimeMillis() - timeDrawLines != 0)
        // System.out.println("Time Draw Lines :" + (System.currentTimeMillis() - timeDrawLines) + " ms");
        //
        // if(System.currentTimeMillis() - timeDrawLink != 0)
        // System.out.println("Time Draw Link :" + (System.currentTimeMillis() - timeDrawLink) + " ms");

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.ui.datamap.linking.GraphicLink#getPointLinkDescriptor1()
     */
    public PointLinkDescriptor getPointLinkDescriptorSource() {
        return this.pointDescriptor1;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.ui.datamap.linking.GraphicLink#getPointLinkDescriptor2()
     */
    public PointLinkDescriptor getPointLinkDescriptorTarget() {
        return this.pointDescriptor2;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.ui.visualmap.link.IGraphicLink#setSate(org.talend.designer.mapper.ui.visualmap.link.StateLink)
     */
    public void setState(LinkState linkState) {
        this.linkState = linkState;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.ui.visualmap.link.IGraphicLink#getState()
     */
    public LinkState getState() {
        return this.linkState;
    }

}
