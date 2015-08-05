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
package org.talend.designer.xmlmap.figures;

import org.eclipse.draw2d.Cursors;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.swt.graphics.Cursor;
import org.talend.designer.xmlmap.figures.sash.ISash;

/**
 * DOC talend class global comment. Detailled comment
 */
public class SashSeparator extends ImageFigure implements ISash {

    private int SEPARATOR_WIDTH = 10;

    private int ZONE_MIN_SIZE = 25;

    private IFigure leftFigure;

    private IFigure rightFigure;

    private IFigure parentFigure;

    public SashSeparator() {
        this.addMouseMotionListener(new MouseMotionListener() {

            public void mouseDragged(MouseEvent me) {

            }

            public void mouseEntered(MouseEvent me) {
                // TODO Auto-generated method stub

            }

            public void mouseExited(MouseEvent me) {
            }

            public void mouseHover(MouseEvent me) {
                final Cursor directionalCursor = Cursors.getDirectionalCursor(PositionConstants.EAST, isMirrored());
                setCursor(directionalCursor);
            }

            public void mouseMoved(MouseEvent me) {

            }

        });
        setAlignment(PositionConstants.NORTH);
        // setOpaque(true);
        // setBackgroundColor(ColorConstants.red);
    }

    public int getSashWidth() {
        return SEPARATOR_WIDTH;
    }

    public void setWidth(int width) {
        SEPARATOR_WIDTH = width;
    }

    /**
     * @see IFigure#getPreferredSize(int, int)
     */
    public Dimension getPreferredSize(int wHint, int hHint) {
        if (prefSize != null)
            return prefSize;
        if (getLayoutManager() != null) {
            Dimension d = getLayoutManager().getPreferredSize(this, wHint, hHint);
            if (d != null)
                return d;
        }
        return getSize();
    }

    public void setLeftFigure(IFigure leftFigure) {
        this.leftFigure = leftFigure;
    }

    public void setRightFigure(IFigure rightFigure) {
        this.rightFigure = rightFigure;
    }

    public void setParentFigure(IFigure parentFigure) {
        this.parentFigure = parentFigure;
    }

    public IFigure getLeftFigure() {
        return this.leftFigure;
    }

    public IFigure getRightFigure() {
        return this.rightFigure;
    }

    public IFigure getParentFigure() {
        return this.parentFigure;
    }

    public int getZoneMinSize() {
        return this.ZONE_MIN_SIZE;
    }

}
