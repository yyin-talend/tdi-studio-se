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
import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.BorderLayout;
import org.eclipse.draw2d.ButtonModel;
import org.eclipse.draw2d.ChangeEvent;
import org.eclipse.draw2d.ChangeListener;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MarginBorder;
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
import org.talend.themes.core.elements.interfaces.ICSSStylingChangedListener;
import org.talend.themes.core.elements.stylesettings.CommonCSSStyleSetting;
import org.talend.themes.core.elements.stylesettings.TalendPaletteCSSStyleSetting;

/**
 * 
 */
public class TalendDrawerFigure extends DrawerFigure implements ICSSStylingChangedListener {

    protected static int COLOR_INCREMENT;

    protected static int X_OFFSET;

    protected Toggle collapseToggle;

    protected ScrollPane scrollpane;

    protected Label talendDrawerLabel, tipLabel;

    protected boolean skipNextEvent;

    protected TalendEditPartTipHelper tipHelper;

    protected PinFigure talendPinFigure;

    private static Color baseColor;

    protected TalendPaletteCSSStyleSetting cssStyleSetting;

    protected Figure title;

    protected Border titleBorder;

    protected int childLevel;

    public TalendDrawerFigure(Control control, int childLevel, TalendPaletteCSSStyleSetting cssStyleSetting) {
        super(null);// must be null

        this.cssStyleSetting = cssStyleSetting;
        cssStyleSetting.setStylingChangeListener(this);
        initStyleInfos();
        this.childLevel = childLevel;

        try {
            Field collapseToggleField = DrawerFigure.class.getDeclaredField("collapseToggle");
            collapseToggleField.setAccessible(true);
            Object oldCollapseToggle = collapseToggleField.get(this);
            remove((IFigure) oldCollapseToggle);

            title = new Figure();
            title.setBorder(getTitleBorder());
            BorderLayout borderLayout = new BorderLayout();
            borderLayout.setHorizontalSpacing(10 * (childLevel + 1));
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

    protected Border getTitleBorder() {
        Insets insets = TITLE_MARGIN_BORDER.getInsets(null);
        Border talendTitleBorder = new MarginBorder(insets.top, insets.left + 10 * childLevel, insets.bottom, insets.right);
        return talendTitleBorder;
    }

    protected void initStyleInfos() {
        TalendPaletteCSSStyleSetting.copyBorderSetting(this.cssStyleSetting.getScrollPaneBorder(), SCROLL_PANE_BORDER);
        this.cssStyleSetting.setScrollPaneBorderInstance(SCROLL_PANE_BORDER);
        TalendPaletteCSSStyleSetting.copyBorderSetting(this.cssStyleSetting.getScrollPaneListBorder(), SCROLL_PANE_LIST_BORDER);
        this.cssStyleSetting.setScrollPaneListBorderInstance(SCROLL_PANE_LIST_BORDER);
        TalendPaletteCSSStyleSetting.copyBorderSetting(this.cssStyleSetting.getTitleMarginBorder(), TITLE_MARGIN_BORDER);
        this.cssStyleSetting.setTitleMarginBorderInstance(TITLE_MARGIN_BORDER);
        X_OFFSET = this.cssStyleSetting.getxOffset();
        baseColor = this.cssStyleSetting.getDrawerFigureBaseColor();
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
        // return new Rectangle(bounds.x + X_OFFSET, bounds.y, bounds.width, bounds.height);
        return new Rectangle(bounds.x, bounds.y, bounds.width, bounds.height);
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

            Color color = null;

            // draw top border of drawer figure
            // g.setForegroundColor(PaletteColorUtil.WIDGET_NORMAL_SHADOW);
            color = cssStyleSetting.getCollapseTopBorderForgroundLineColor1();
            if (color != null) {
                g.setForegroundColor(color);
                g.drawLine(r.getTopLeft(), r.getTopRight());
                cssStyleSetting.disposeRelatedColor(color);
            }

            // g.setForegroundColor(ColorConstants.white);
            color = cssStyleSetting.getCollapseTopBorderForgroundLineColor2();
            if (color != null) {
                g.setForegroundColor(color);
                g.drawLine(r.getTopLeft().getTranslated(0, 1), r.getTopRight().getTranslated(0, 1));
                // r.crop(new Insets(2, 0, 0, 0));
                r.crop(new Insets(1, 0, 0, 0));
                cssStyleSetting.disposeRelatedColor(color);
            }
            if (isExpanded()) {
                // g.setForegroundColor(PaletteColorUtil.WIDGET_BACKGROUND_NORMAL_SHADOW_65);
                color = cssStyleSetting.getCollapseExpandedLineForgroundColor();
                if (color != null) {
                    g.setForegroundColor(color);
                    g.drawLine(r.getLocation(), r.getTopRight());
                    // r.crop(new Insets(1, 0, 0, 0));
                    r.crop(new Insets(1, 0, 0, 0));
                    cssStyleSetting.disposeRelatedColor(color);
                }
            }

            // draw bottom border of drawer figure
            if (!isExpanded()) {
                // g.setForegroundColor(ColorConstants.white);
                color = cssStyleSetting.getCollapseNotExpandedLineForgroundColor();
                if (color != null) {
                    g.setForegroundColor(color);
                    g.drawLine(r.getBottomLeft().getTranslated(0, -1), r.getBottomRight().getTranslated(0, -1));
                    // r.crop(new Insets(0, 0, 1, 0));
                    r.crop(new Insets(0, 0, 1, 0));
                    cssStyleSetting.disposeRelatedColor(color);
                }
            }

            paintToggleGradient(g, r);

        }
    }

    private void paintToggleGradient(Graphics g, Rectangle rect) {
        if (isExpanded()) {
            // g.setBackgroundColor(PaletteColorUtil.WIDGET_BACKGROUND_LIST_BACKGROUND_85);
            g.setBackgroundColor(cssStyleSetting.getExpandedBackgroundColor());
            cssStyleSetting.disposeRelatedColor(cssStyleSetting.getExpandedBackgroundColor());
            g.fillRectangle(rect);
        } else if (collapseToggle.getModel().isMouseOver()) {
            // Color color1 = ColorConstants.red;
            // Color color2 = ColorConstants.red;
            // Color color3 = ColorConstants.yellow;
            // Color color4 = ColorConstants.yellow;
            // Color color1 = PaletteColorUtil.WIDGET_BACKGROUND_LIST_BACKGROUND_60;
            // Color color2 = PaletteColorUtil.WIDGET_BACKGROUND_NORMAL_SHADOW_90;
            // Color color3 = PaletteColorUtil.WIDGET_BACKGROUND_NORMAL_SHADOW_95;
            // Color color4 = PaletteColorUtil.WIDGET_BACKGROUND_LIST_BACKGROUND_90;

            g.setForegroundColor(cssStyleSetting.getMouseOverForgroundColor1());
            cssStyleSetting.disposeRelatedColor(cssStyleSetting.getMouseOverForgroundColor1());
            g.setBackgroundColor(cssStyleSetting.getMouseOverBackgroundColor1());
            cssStyleSetting.disposeRelatedColor(cssStyleSetting.getMouseOverBackgroundColor1());
            g.fillGradient(rect.x, rect.y, rect.width, rect.height - 4, true);

            g.setForegroundColor(cssStyleSetting.getMouseOverForgroundColor2());
            cssStyleSetting.disposeRelatedColor(cssStyleSetting.getMouseOverForgroundColor2());
            g.setBackgroundColor(cssStyleSetting.getMouseOverBackgroundColor2());
            cssStyleSetting.disposeRelatedColor(cssStyleSetting.getMouseOverBackgroundColor2());
            g.fillGradient(rect.x, rect.bottom() - 4, rect.width, 2, true);

            g.setForegroundColor(cssStyleSetting.getMouseOverForgroundColor3());
            cssStyleSetting.disposeRelatedColor(cssStyleSetting.getMouseOverForgroundColor3());
            g.setBackgroundColor(cssStyleSetting.getMouseOverBackgroundColor3());
            cssStyleSetting.disposeRelatedColor(cssStyleSetting.getMouseOverBackgroundColor3());
            g.fillGradient(rect.x, rect.bottom() - 2, rect.width, 2, true);
        } else {
            // g.setForegroundColor(PaletteColorUtil.WIDGET_BACKGROUND_LIST_BACKGROUND_85);
            // g.setBackgroundColor(PaletteColorUtil.WIDGET_BACKGROUND_NORMAL_SHADOW_45);
            g.setForegroundColor(cssStyleSetting.getCollapsedForgroundColor());
            cssStyleSetting.disposeRelatedColor(cssStyleSetting.getCollapsedForgroundColor());
            g.setBackgroundColor(cssStyleSetting.getCollapsedBackgroundColor());
            cssStyleSetting.disposeRelatedColor(cssStyleSetting.getCollapsedBackgroundColor());
            g.fillGradient(rect, true);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.themes.core.elements.interfaces.ICSSStylingChangedLisener#applyChange(org.talend.themes.core.elements
     * .stylesettings.CommonCSSStyleSetting)
     */
    @Override
    public void applyChange(CommonCSSStyleSetting setting) {
        this.X_OFFSET = cssStyleSetting.getxOffset();
        this.COLOR_INCREMENT = cssStyleSetting.getColorIncrement();
        title.setBorder(getTitleBorder());
        // this.cssStyleSetting.setTitleMarginBorderInstance(TITLE_MARGIN_BORDER);
    }

    public Color getSubColor(Color color) {
        Color newColor = null;

        return newColor;
    }
}
