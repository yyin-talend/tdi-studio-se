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
import java.lang.reflect.Method;

import org.eclipse.draw2d.ActionEvent;
import org.eclipse.draw2d.ActionListener;
import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.ButtonBorder;
import org.eclipse.draw2d.ButtonModel;
import org.eclipse.draw2d.Clickable;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.Toggle;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.internal.ui.palette.editparts.DetailedLabelFigure;
import org.eclipse.gef.internal.ui.palette.editparts.ToolEntryEditPart;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.ui.palette.PaletteViewerPreferences;
import org.eclipse.jface.resource.ImageDescriptor;
import org.talend.commons.exception.CommonExceptionHandler;
import org.talend.core.ui.images.CoreImageProvider;
import org.talend.themes.core.elements.stylesettings.TalendPaletteCSSStyleSetting;

/**
 * DOC talend class global comment. Detailled comment
 */
public class TalendEntryEditPart extends ToolEntryEditPart {

    protected DetailedLabelFigure talendCustomLabel;

    protected static Border TOOLBAR_ITEM_BORDER = new ButtonBorder(ButtonBorder.SCHEMES.TOOLBAR);

    protected static Border LIST_BORDER = null;

    protected static Border ICON_BORDER = null;

    protected static Method func_getSelectionRectangle;

    protected TalendPaletteCSSStyleSetting cssStyleSetting;

    /**
     * DOC talend TalendEntryEditPart constructor comment.
     * 
     * @param paletteEntry
     */
    public TalendEntryEditPart(PaletteEntry paletteEntry, TalendPaletteCSSStyleSetting cssStyleSetting) {
        super(paletteEntry);
        this.cssStyleSetting = cssStyleSetting;
        init();
    }

    protected void init() {
        LIST_BORDER = cssStyleSetting.getEntryEditPartListBorder();
        ICON_BORDER = cssStyleSetting.getEntryEditPartIconBorder();
    }

    @Override
    protected void setImageDescriptor(ImageDescriptor desc) {
        setImageInFigure(CoreImageProvider.getComponentImageFromDesc(desc));
    }

    @Override
    public IFigure createFigure() {
        Field customLabelField;
        try {
            talendCustomLabel = new DetailedLabelFigure();
            customLabelField = ToolEntryEditPart.class.getDeclaredField("customLabel"); //$NON-NLS-1$
            customLabelField.setAccessible(true);
            customLabelField.set(this, talendCustomLabel);
        } catch (Exception e) {
            CommonExceptionHandler.process(e);
        }
        Clickable button = new ToolEntryToggle(talendCustomLabel);
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                getPaletteViewer().setActiveTool(getToolEntry());
            }
        });

        return button;
    }

    protected ToolEntry getToolEntry() {
        return (ToolEntry) getPaletteEntry();
    }

    class ToolEntryToggle extends Toggle {

        private boolean showHoverFeedback = false;

        ToolEntryToggle(IFigure contents) {
            super(contents);
            setOpaque(false);
            setEnabled(true);
            if (isToolbarItem()) {
                setStyle(Clickable.STYLE_BUTTON | Clickable.STYLE_TOGGLE);
                setBorder(TOOLBAR_ITEM_BORDER);
            }
        }

        @Override
        public boolean containsPoint(int x, int y) {
            Rectangle rect = getBounds().getCopy();
            int ARROW_WIDTH = cssStyleSetting.getEntryEditPartArrowWidth();
            if (talendCustomLabel.getBorder() == ICON_BORDER) {
                rect.width -= ARROW_WIDTH;
            } else if (talendCustomLabel.getBorder() == LIST_BORDER) {
                rect.width -= ARROW_WIDTH;
                rect.x += ARROW_WIDTH;
            }
            return rect.contains(x, y);
        }

        @Override
        public IFigure findMouseEventTargetAt(int x, int y) {
            return null;
        }

        @Override
        public IFigure getToolTip() {
            return createToolTip();
        }

        @Override
        public void setEnabled(boolean value) {
            super.setEnabled(value);
            if (isEnabled()) {
                setRolloverEnabled(true);
                if (getFlag(STYLE_BUTTON)) {
                    setBorder(TOOLBAR_ITEM_BORDER);
                }
                setForegroundColor(null);
            } else {
                if (getFlag(STYLE_BUTTON)) {
                    setBorder(null);
                }
                setRolloverEnabled(false);
                setForegroundColor(cssStyleSetting.getEntryEditPartToolEntryForgroundDisabledColor());
                cssStyleSetting.disposeRelatedColor(cssStyleSetting.getEntryEditPartToolEntryForgroundDisabledColor());
            }
        }

        @Override
        protected void paintFigure(Graphics graphics) {
            super.paintFigure(graphics);

            if (!isToolbarItem() && isEnabled() && isRolloverEnabled()) {
                ButtonModel model = getModel();

                if (model.isSelected()) {
                    // graphics.setBackgroundColor(PaletteColorUtil.getSelectedColor());
                    graphics.setBackgroundColor(cssStyleSetting.getEntryEditPartToolEntrySelectedBackgroundColor());
                    cssStyleSetting.disposeRelatedColor(cssStyleSetting.getEntryEditPartToolEntrySelectedBackgroundColor());
                    graphics.fillRoundRectangle(getSelectionRectangle(getLayoutSetting(), talendCustomLabel), 3, 3);
                } else if (model.isMouseOver() || showHoverFeedback) {
                    // graphics.setBackgroundColor(PaletteColorUtil.getHoverColor());
                    graphics.setBackgroundColor(cssStyleSetting.getEntryEditPartToolEntryHoverBackgroundColor());
                    cssStyleSetting.disposeRelatedColor(cssStyleSetting.getEntryEditPartToolEntryHoverBackgroundColor());
                    graphics.fillRoundRectangle(getSelectionRectangle(getLayoutSetting(), talendCustomLabel), 3, 3);
                }
            }
        }

        @Override
        protected void paintBorder(Graphics graphics) {
            if (isEnabled()) {

                if (getBorder() != null) {
                    getBorder().paint(this, graphics, NO_INSETS);
                }
                if (hasFocus()) {
                    graphics.setForegroundColor(cssStyleSetting.getEntryEditPartToolEntryBorderFocusForgroundColor());
                    cssStyleSetting.disposeRelatedColor(cssStyleSetting.getEntryEditPartToolEntryHoverBackgroundColor());
                    graphics.setBackgroundColor(cssStyleSetting.getEntryEditPartToolEntryHoverBackgroundColor());
                    cssStyleSetting.disposeRelatedColor(cssStyleSetting.getEntryEditPartToolEntryHoverBackgroundColor());

                    Rectangle area = isToolbarItem() ? getClientArea() : getSelectionRectangle(getLayoutSetting(),
                            talendCustomLabel);
                    if (isStyle(STYLE_BUTTON)) {
                        graphics.drawFocus(area.x, area.y, area.width, area.height);
                    } else {
                        graphics.drawFocus(area.x, area.y, area.width - 1, area.height - 1);
                    }
                }
            } else {
                super.paintBorder(graphics);
            }
        }

        /**
         * Should hover feedback be shown? Allows other palette entities to control when the hover feedback should be
         * shown on this tool entry.
         * 
         * @param showHoverFeedback true if the hover feedback is to be shown; false otherwise.
         */
        public void setShowHoverFeedback(boolean showHoverFeedback) {
            this.showHoverFeedback = showHoverFeedback;
            repaint();
        }
    }

    @Override
    protected void refreshVisuals() {
        PaletteEntry entry = getPaletteEntry();

        talendCustomLabel.setName(entry.getLabel());
        talendCustomLabel.setDescription(entry.getDescription());
        if (getPreferenceSource().useLargeIcons()) {
            setImageDescriptor(entry.getLargeIcon());
        } else {
            setImageDescriptor(entry.getSmallIcon());
        }
        int layoutMode = getLayoutSetting();
        talendCustomLabel.setLayoutMode(layoutMode);
        if (layoutMode == PaletteViewerPreferences.LAYOUT_COLUMNS) {
            talendCustomLabel.setBorder(getTabbedBorder(ICON_BORDER));
        } else if (layoutMode == PaletteViewerPreferences.LAYOUT_LIST || layoutMode == PaletteViewerPreferences.LAYOUT_DETAILS) {
            talendCustomLabel.setBorder(getTabbedBorder(LIST_BORDER));
        } else if (layoutMode == PaletteViewerPreferences.LAYOUT_ICONS && !isToolbarItem()) {
            talendCustomLabel.setBorder(getTabbedBorder(ICON_BORDER));
        } else {
            talendCustomLabel.setBorder(null);
        }

    }

    protected Border getTabbedBorder(Border border) {
        Insets insets = border.getInsets(null);
        PaletteEntry entry = (PaletteEntry) getModel();
        int i = 0;
        while ((entry = entry.getParent()) != null) {
            ++i;
        }
        return new MarginBorder(insets.top, insets.left + 10 * i, insets.bottom, insets.right);

    }

    protected static Rectangle getSelectionRectangle(int layoutMode, DetailedLabelFigure labelFigure) {
        Rectangle rect = Rectangle.SINGLETON;

        try {
            if (func_getSelectionRectangle == null) {
                func_getSelectionRectangle = ToolEntryEditPart.class.getDeclaredMethod("getSelectionRectangle", int.class,
                        DetailedLabelFigure.class);
                func_getSelectionRectangle.setAccessible(true);
            }
            rect = (Rectangle) func_getSelectionRectangle.invoke(ToolEntryEditPart.class, layoutMode, labelFigure);
        } catch (Exception e) {
            CommonExceptionHandler.process(e);
        }

        return rect;
    }
}
