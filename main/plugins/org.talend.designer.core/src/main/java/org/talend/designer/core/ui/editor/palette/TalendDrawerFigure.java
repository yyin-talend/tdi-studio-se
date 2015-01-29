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
package org.talend.designer.core.ui.editor.palette;

import java.lang.reflect.Field;

import org.eclipse.draw2d.Animation;
import org.eclipse.draw2d.BorderLayout;
import org.eclipse.draw2d.ButtonModel;
import org.eclipse.draw2d.ChangeEvent;
import org.eclipse.draw2d.ChangeListener;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.ScrollPane;
import org.eclipse.draw2d.Toggle;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.internal.ui.palette.editparts.DrawerFigure;
import org.eclipse.gef.internal.ui.palette.editparts.PinFigure;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.talend.commons.exception.CommonExceptionHandler;

/**
 * 
 */
public class TalendDrawerFigure extends DrawerFigure {

    private static final int COLOR_INCREMENT = 15;

    private static final int X_OFFSET = 17;

    protected Toggle collapseToggle;

    protected ScrollPane scrollpane;

    protected Label talendDrawerLabel, tipLabel;

    protected boolean skipNextEvent;

    protected TalendEditPartTipHelper tipHelper;

    protected PinFigure talendPinFigure;

    private static Color baseColor = FigureUtilities.mixColors(ColorConstants.green, ColorConstants.blue, 0.1);

    public TalendDrawerFigure(Control control, int childLevel) {
        super(null);// must be null

        try {
            Field collapseToggleField = DrawerFigure.class.getDeclaredField("collapseToggle");
            collapseToggleField.setAccessible(true);
            Object oldCollapseToggle = collapseToggleField.get(this);
            remove((IFigure) oldCollapseToggle);

            Figure title = new Figure();
            title.setBorder(TITLE_MARGIN_BORDER);
            BorderLayout borderLayout = new BorderLayout();
            borderLayout.setHorizontalSpacing(2);
            title.setLayoutManager(borderLayout);

            Field pinFigureField = DrawerFigure.class.getDeclaredField("pinFigure");
            pinFigureField.setAccessible(true);
            talendPinFigure = new PinFigure();
            pinFigureField.set(this, talendPinFigure);
            Field drawerLabelField = DrawerFigure.class.getDeclaredField("drawerLabel");
            drawerLabelField.setAccessible(true);
            talendDrawerLabel = new Label();
            talendDrawerLabel.setLabelAlignment(Label.LEFT);
            drawerLabelField.set(this, talendDrawerLabel);

            title.add(talendPinFigure, BorderLayout.RIGHT);
            title.add(talendDrawerLabel, BorderLayout.CENTER);
            collapseToggle = new CollapseToggle(title);
            collapseToggleField.set(this, collapseToggle);
            add(collapseToggle);

            createHoverHelp(control);

            Field scrollpaneField = DrawerFigure.class.getDeclaredField("scrollpane");
            scrollpaneField.setAccessible(true);
            scrollpane = (ScrollPane) scrollpaneField.get(this);
            Color backgroundColor = new Color(Display.getCurrent(), getNewValue(baseColor.getRed(), childLevel), getNewValue(
                    baseColor.getGreen(), childLevel), getNewValue(baseColor.getBlue(), childLevel));
            scrollpane.getContents().setBackgroundColor(backgroundColor);

        } catch (Exception e) {
            CommonExceptionHandler.process(e);
        }

        // getContentPane().setBackgroundColor(backgroundColor);
    }

    protected void createHoverHelp(final Control control) {
        if (control == null) {
            return;
        }
        // If a control was provided, create the tipLabel -- if the text in the
        // header is
        // truncated, it will display it as a tooltip.
        tipLabel = new Label() {

            /**
             * @see org.eclipse.draw2d.Figure#getToolTip()
             */
            @Override
            public IFigure getToolTip() {
                return buildTooltip();
            }

            @Override
            protected void paintFigure(Graphics graphics) {
                Rectangle r = Rectangle.SINGLETON;
                r.setBounds(getBounds());
                graphics.pushState();
                paintToggleGradient(graphics, getBounds());
                graphics.popState();
                super.paintFigure(graphics);
            }
        };
        tipLabel.setOpaque(false);
        tipLabel.setBorder(TOOLTIP_BORDER);
        try {
            Field tipLabelField = DrawerFigure.class.getDeclaredField("tipLabel");
            tipLabelField.setAccessible(true);
            tipLabelField.set(this, tipLabel);
        } catch (Exception e) {
            CommonExceptionHandler.process(e);
        }

        collapseToggle.addMouseMotionListener(new MouseMotionListener.Stub() {

            @Override
            public void mouseMoved(MouseEvent e) {
                if (!talendDrawerLabel.getBounds().contains(e.getLocation())) {
                    return;
                }
                if (skipNextEvent) {
                    skipNextEvent = false;
                    return;
                }
                if (talendDrawerLabel.isTextTruncated() && !TalendEditPartTipHelper.isCurrent(tipHelper)) {
                    tipLabel.setText(talendDrawerLabel.getText());
                    tipLabel.setIcon(talendDrawerLabel.getIcon());
                    tipLabel.setFont(talendDrawerLabel.getFont());
                    tipHelper = new TalendEditPartTipHelper(control);
                    Rectangle bounds = talendDrawerLabel.getBounds().getExpanded(2, 2);
                    talendDrawerLabel.translateToAbsolute(bounds);
                    org.eclipse.swt.graphics.Rectangle loc = new org.eclipse.swt.graphics.Rectangle(bounds.x, bounds.y,
                            bounds.width, bounds.height);
                    loc = Display.getCurrent().map(control, null, loc);
                    tipHelper.displayToolTipAt(tipLabel, loc.x, loc.y);
                }
            }
        });
        tipLabel.addMouseListener(new MouseListener.Stub() {

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.button == 1) {
                    Rectangle original = getCollapseToggle().getBounds().getCopy();
                    getCollapseToggle().requestFocus();
                    setExpanded(!isExpanded());
                    // Hide the tip if expanding the drawer causes the collapse
                    // toggle to move
                    if (!original.equals(getCollapseToggle().getBounds())) {
                        tipHelper.hide();
                    }
                } else {
                    tipHelper.hide();
                    if (e.button == 3) {
                        skipNextEvent = true;
                    }
                }
            }
        });
    }

    public void disposeColors() {
        if (getContentPane().getBackgroundColor() != null && !getContentPane().getBackgroundColor().isDisposed()) {
            getContentPane().getBackgroundColor().dispose();
        }
    }

    private int getNewValue(int oldValue, int childLevel) {
        int result = oldValue - childLevel * COLOR_INCREMENT;
        return (result > 0 ? result : 0);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(bounds.x + X_OFFSET, bounds.y, bounds.width, bounds.height);
    }

    IFigure buildTooltip() {
        return null;
    }

    private class CollapseToggle extends Toggle {

        public CollapseToggle(IFigure contents) {
            super(contents);
            setSelected(true);
            setRequestFocusEnabled(true);
            addChangeListener(new ChangeListener() {

                @Override
                public void handleStateChanged(ChangeEvent e) {
                    if (e.getPropertyName().equals(ButtonModel.SELECTED_PROPERTY)) {
                        Animation.markBegin();
                        handleExpandStateChanged();
                        Animation.run(150);
                    } else if (e.getPropertyName().equals(ButtonModel.MOUSEOVER_PROPERTY)) {
                        repaint();
                    }
                }
            });
        }

        @Override
        public IFigure getToolTip() {
            return buildTooltip();
        }

        @Override
        protected void paintFigure(Graphics g) {
            super.paintFigure(g);
            Rectangle r = Rectangle.SINGLETON;
            r.setBounds(getBounds());

            // draw top border of drawer figure
            // g.setForegroundColor(PaletteColorUtil.WIDGET_NORMAL_SHADOW);
            g.setForegroundColor(ColorConstants.red);
            g.drawLine(r.getTopLeft(), r.getTopRight());
            g.setForegroundColor(ColorConstants.white);
            g.drawLine(r.getTopLeft().getTranslated(0, 1), r.getTopRight().getTranslated(0, 1));
            r.crop(new Insets(2, 0, 0, 0));
            if (isExpanded()) {
                // g.setForegroundColor(PaletteColorUtil.WIDGET_BACKGROUND_NORMAL_SHADOW_65);
                g.setForegroundColor(ColorConstants.red);
                g.drawLine(r.getLocation(), r.getTopRight());
                r.crop(new Insets(1, 0, 0, 0));
            }

            // draw bottom border of drawer figure
            if (!isExpanded()) {
                // g.setForegroundColor(ColorConstants.white);
                g.setForegroundColor(ColorConstants.red);
                g.drawLine(r.getBottomLeft().getTranslated(0, -1), r.getBottomRight().getTranslated(0, -1));
                r.crop(new Insets(0, 0, 1, 0));
            }

            paintToggleGradient(g, r);

        }
    }

    private void paintToggleGradient(Graphics g, Rectangle rect) {
        if (isExpanded()) {
            // g.setBackgroundColor(PaletteColorUtil.WIDGET_BACKGROUND_LIST_BACKGROUND_85);
            g.setBackgroundColor(ColorConstants.blue);
            g.fillRectangle(rect);
        } else if (collapseToggle.getModel().isMouseOver()) {
            Color color1 = ColorConstants.red;
            Color color2 = ColorConstants.red;
            Color color3 = ColorConstants.yellow;
            Color color4 = ColorConstants.yellow;
            // Color color1 = PaletteColorUtil.WIDGET_BACKGROUND_LIST_BACKGROUND_60;
            // Color color2 = PaletteColorUtil.WIDGET_BACKGROUND_NORMAL_SHADOW_90;
            // Color color3 = PaletteColorUtil.WIDGET_BACKGROUND_NORMAL_SHADOW_95;
            // Color color4 = PaletteColorUtil.WIDGET_BACKGROUND_LIST_BACKGROUND_90;

            g.setForegroundColor(color1);
            g.setBackgroundColor(color2);
            g.fillGradient(rect.x, rect.y, rect.width, rect.height - 4, true);

            g.setForegroundColor(color2);
            g.setBackgroundColor(color3);
            g.fillGradient(rect.x, rect.bottom() - 4, rect.width, 2, true);

            g.setForegroundColor(color3);
            g.setBackgroundColor(color4);
            g.fillGradient(rect.x, rect.bottom() - 2, rect.width, 2, true);
        } else {
            // g.setForegroundColor(PaletteColorUtil.WIDGET_BACKGROUND_LIST_BACKGROUND_85);
            // g.setBackgroundColor(PaletteColorUtil.WIDGET_BACKGROUND_NORMAL_SHADOW_45);
            g.setForegroundColor(ColorConstants.blue);
            g.setBackgroundColor(ColorConstants.blue);
            g.fillGradient(rect, true);
        }
    }

}
