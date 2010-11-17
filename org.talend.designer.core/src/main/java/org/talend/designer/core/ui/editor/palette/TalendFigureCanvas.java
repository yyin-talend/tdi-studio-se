// ============================================================================
//
// Copyright (C) 2006-2010 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.core.ui.editor.palette;

import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.ScrollPaneSolver;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * cli class global comment. Detailled comment
 */
public class TalendFigureCanvas extends FigureCanvas {

    private int cashedToolHeight = 24;

    protected Control toolControl;

    public TalendFigureCanvas(Composite parent, LightweightSystem lws, TalendPaletteViewer toolViewer) {
        this(SWT.DOUBLE_BUFFERED, parent, lws, toolViewer);
    }

    public TalendFigureCanvas(int style, Composite parent, LightweightSystem lws, TalendPaletteViewer toolViewer) {
        super(style | DEFAULT_STYLES, parent, lws);

        if (toolViewer != null) {
            toolControl = toolViewer.creatToolControl(this);
        }

        if (toolControl != null && toolViewer != null) {
            org.eclipse.swt.graphics.Point bounds = toolControl.computeSize(SWT.DEFAULT, SWT.DEFAULT);
            if (cashedToolHeight < bounds.y) {
                cashedToolHeight = bounds.y;
            }
        }

    }

    @Override
    protected void layoutViewport() {
        ScrollPaneSolver.Result result;
        int viewPortY = 0;
        if (toolControl != null) {
            viewPortY = cashedToolHeight;
        }
        result = ScrollPaneSolver.solve(new Rectangle(getBounds()).setLocation(0, viewPortY), getViewport(),
                getHorizontalScrollBarVisibility(), getVerticalScrollBarVisibility(), computeTrim(0, 0, 0, 0).width,
                computeTrim(0, 0, 0, 0).height);
        getLightweightSystem().setIgnoreResize(true);
        try {
            if (getHorizontalBar().getVisible() != result.showH)
                getHorizontalBar().setVisible(result.showH);
            if (getVerticalBar().getVisible() != result.showV)
                getVerticalBar().setVisible(result.showV);
            Rectangle r = new Rectangle(getClientArea());
            if (toolControl != null) {
                toolControl.setBounds(0, 0, r.width, cashedToolHeight);
                r.height -= cashedToolHeight;
                r.setLocation(0, cashedToolHeight);
            } else {
                r.setLocation(0, 0);
            }
            getLightweightSystem().getRootFigure().setBounds(r);
        } finally {
            getLightweightSystem().setIgnoreResize(false);
        }
    }

}
