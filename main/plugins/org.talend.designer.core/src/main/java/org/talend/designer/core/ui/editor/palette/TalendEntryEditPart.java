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
import org.eclipse.draw2d.ColorConstants;
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
import org.eclipse.jface.resource.ImageDescriptor;
import org.talend.commons.exception.CommonExceptionHandler;
import org.talend.core.ui.images.CoreImageProvider;

/**
 * DOC talend class global comment. Detailled comment
 */
public class TalendEntryEditPart extends ToolEntryEditPart {

    protected DetailedLabelFigure customLabel;

    protected static int ARROW_WIDTH = 9;

    protected static Border TOOLBAR_ITEM_BORDER = new ButtonBorder(ButtonBorder.SCHEMES.TOOLBAR);

    protected static Border LIST_BORDER = new MarginBorder(3, ARROW_WIDTH + 7, 4, 0);

    protected static Border ICON_BORDER = new MarginBorder(4, 4, 3, ARROW_WIDTH + 4);

    protected static final Insets LIST_HIGHLIGHT_INSETS = new Insets(1, 5, 2, 0);

    protected static final Insets ICON_HIGHLIGHT_INSETS = new Insets(2, 1, 2, 1);

    protected static Method func_getSelectionRectangle;

    /**
     * DOC talend TalendEntryEditPart constructor comment.
     * 
     * @param paletteEntry
     */
    public TalendEntryEditPart(PaletteEntry paletteEntry) {
        super(paletteEntry);
    }

    @Override
    protected void setImageDescriptor(ImageDescriptor desc) {
        setImageInFigure(CoreImageProvider.getComponentImageFromDesc(desc));
    }

    @Override
    public IFigure createFigure() {
        Field customLabelField;
        try {
            customLabel = new DetailedLabelFigure();
            customLabelField = ToolEntryEditPart.class.getDeclaredField("customLabel"); //$NON-NLS-1$
            customLabelField.setAccessible(true);
            customLabelField.set(this, customLabel);
        } catch (Exception e) {
            CommonExceptionHandler.process(e);
        }
        Clickable button = new ToolEntryToggle(customLabel);
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
            if (customLabel.getBorder() == ICON_BORDER) {
                rect.width -= ARROW_WIDTH;
            } else if (customLabel.getBorder() == LIST_BORDER) {
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
                setForegroundColor(ColorConstants.gray);
            }
        }

        @Override
        protected void paintFigure(Graphics graphics) {
            super.paintFigure(graphics);

            if (!isToolbarItem() && isEnabled() && isRolloverEnabled()) {
                ButtonModel model = getModel();

                if (model.isSelected()) {
                    // graphics.setBackgroundColor(PaletteColorUtil.getSelectedColor());
                    graphics.setBackgroundColor(ColorConstants.blue);
                    graphics.fillRoundRectangle(getSelectionRectangle(getLayoutSetting(), customLabel), 3, 3);
                } else if (model.isMouseOver() || showHoverFeedback) {
                    // graphics.setBackgroundColor(PaletteColorUtil.getHoverColor());
                    graphics.setBackgroundColor(ColorConstants.yellow);
                    graphics.fillRoundRectangle(getSelectionRectangle(getLayoutSetting(), customLabel), 3, 3);
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
                    graphics.setForegroundColor(ColorConstants.red);
                    graphics.setBackgroundColor(ColorConstants.green);

                    Rectangle area = isToolbarItem() ? getClientArea() : getSelectionRectangle(getLayoutSetting(), customLabel);
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
