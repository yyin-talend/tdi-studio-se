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
package org.talend.designer.mapper.ui.dnd;

import org.eclipse.jface.dialogs.PopupDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.talend.commons.ui.ws.WindowSystem;
import org.talend.designer.mapper.managers.MapperManager;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public final class DraggingInfosPopup {

    private Popup popup;

    private Point cursorPosition;

    private boolean insertionEntryContext;

    private boolean expressionContext;

    private boolean mapOneToOne;

    private MapperManager mapperManager;

    private Shell parent;

    private boolean outputToOutputMode;

    private boolean dropInvalid = true;

    private DraggingInfosPopup(Shell parent, MapperManager mapperManager) {
        this.mapperManager = mapperManager;
        this.parent = parent;
        init(parent);
    }

    /**
     * 
     * DOC amaumont DraggingInfosPopup class global comment. Detailled comment <br/>
     * 
     * $Id$
     * 
     */
    private class Popup extends PopupDialog {

        private Label labelEntries;

        private Composite mainComposite;

        private Label labelWriteMode;

        private Label labelMappingMode;

        private Label labelInsertionEntryMode;

        private Label labelDropInvalid;

        public Popup(Shell parent) {
            super(parent, SWT.ON_TOP, false, false, false, false, null, null);
        }

        @Override
        protected Control createDialogArea(Composite parent) {
            mainComposite = new Composite(parent, SWT.NONE);
            RowLayout rowLayout = new RowLayout(SWT.VERTICAL);
            rowLayout.fill = true;
            rowLayout.pack = true;
            rowLayout.marginWidth = 2;
            rowLayout.marginHeight = 0;
            rowLayout.marginBottom = 5;
            rowLayout.spacing = 0;
            mainComposite.setLayout(rowLayout);
            labelEntries = new Label(mainComposite, SWT.HORIZONTAL);
            labelEntries.setLayoutData(new RowData());

            labelWriteMode = new Label(mainComposite, SWT.HORIZONTAL);
            labelWriteMode.setLayoutData(new RowData());

            labelMappingMode = new Label(mainComposite, SWT.HORIZONTAL);
            labelMappingMode.setLayoutData(new RowData());

            labelInsertionEntryMode = new Label(mainComposite, SWT.HORIZONTAL);
            labelInsertionEntryMode.setLayoutData(new RowData());

            labelDropInvalid = new Label(mainComposite, SWT.HORIZONTAL);
            labelDropInvalid.setLayoutData(new RowData());

            setLabelsVisible(false, false, false, true);

            return mainComposite;
        }

        @Override
        protected void adjustBounds() {

            if (cursorPosition == null) {
                cursorPosition = new Point(0, 0);
            }
            Shell popupShell = popup.getShell();
            Rectangle popupBounds = popupShell.getBounds();
            final Shell mapperShell = popup.getParentShell();

            Point point = mapperShell.getDisplay().map(popupShell, mapperShell, new Point(0, 0));

            Rectangle boundsToRedraw = new Rectangle(point.x, point.y, popupBounds.width, popupBounds.height);

            Point preferredSize = getPreferredSize();
            int width = preferredSize.x;
            int height = preferredSize.y;
            boolean isVisible = popup.isVisible();
            if (width != popupBounds.width || height != popupBounds.height) {
                setVisible(false);
            }
            int newX = cursorPosition.x - width - 20;
            if (newX < 0) {
                newX = 0;
            }
            int newY = cursorPosition.y - height - 20;
            if(WindowSystem.isGTK()) {
                newY -= 25;
            }
            if (newY < 0) {
                newY = 0;
            }
            Rectangle newBounds = new Rectangle(newX, newY, width, height);
            getShell().setBounds(newBounds);

            // to get round refresh problem
            if (isVisible != isVisible) {
                setVisible(isVisible);
                // System.out.println("Adjust setVisible");
            } else {
                mapperShell.redraw(boundsToRedraw.x, boundsToRedraw.y, boundsToRedraw.width, boundsToRedraw.height, false);
                // System.out.println("Adjust redraw");
            }
        }

        /**
         * DOC amaumont Comment method "setVisible".
         * 
         * @param visible
         */
        public void setVisible(boolean visible) {
            if (visible != getShell().isVisible()) {
                getShell().setVisible(visible);
            }
        }

        public boolean isVisible() {
            return getShell().isVisible();
        }


        /**
         * DOC amaumont Comment method "setText".
         * 
         * @param text
         */
        public void setCountEntriesText(String text) {
            if (!text.equals(labelEntries.getText())) {
                // System.out.println("setCountEntriesText");
                labelEntries.setText(text);
                adjustBounds();
            }
        }

        /**
         * DOC amaumont Comment method "setText".
         * 
         * @param text
         */
        public void setWriteModeText(String text) {
            if (!text.equals(labelWriteMode.getText())) {
                // System.out.println("setWriteModeText");
                labelWriteMode.setText(text);
                adjustBounds();
            }
        }

        /**
         * DOC amaumont Comment method "setMappingModeText".
         * 
         * @param mode
         */
        public void setMappingModeText(String text) {
            if (!text.equals(labelMappingMode.getText())) {
                // System.out.println("setMappingModeText");
                labelMappingMode.setText(text);
                adjustBounds();
            }
        }

        /**
         * DOC amaumont Comment method "setMappingModeText".
         * 
         * @param mode
         */
        public void setInsertionEntryText(String text) {
            if (!text.equals(labelInsertionEntryMode.getText())) {
                // System.out.println("setInsertionEntryText");
                labelInsertionEntryMode.setText(text);
                adjustBounds();
            }
        }

        /**
         * DOC amaumont Comment method "setMappingModeText".
         * 
         * @param mode
         */
        public void setDropInvalidText(String text) {
            if (!text.equals(labelDropInvalid.getText())) {
                labelDropInvalid.setText(text);
                adjustBounds();
            }
        }

        private Point getPreferredSize() {
            return mainComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        }

        public void setLabelsVisible(boolean boolLabelWriteMode, boolean boolLabelMappingMode, boolean boolLabelInsertionEntryMode, boolean boolDropInvalid) {
            // System.out.println("setLabelsVisible");
            ((RowData) labelWriteMode.getLayoutData()).exclude = !boolLabelWriteMode;
            ((RowData) labelMappingMode.getLayoutData()).exclude = !boolLabelMappingMode;
            ((RowData) labelInsertionEntryMode.getLayoutData()).exclude = !boolLabelInsertionEntryMode;
            ((RowData) labelDropInvalid.getLayoutData()).exclude = !boolDropInvalid;
            labelWriteMode.setVisible(boolLabelWriteMode);
            labelMappingMode.setVisible(boolLabelMappingMode);
            labelInsertionEntryMode.setVisible(boolLabelInsertionEntryMode);
            labelDropInvalid.setVisible(boolDropInvalid);
        }

    }

    /**
     * DOC amaumont Comment method "init".
     * 
     * @param parent
     */
    private void init(Shell parent) {
        popup = new Popup(parent);
        popup.open();
        popup.setVisible(false);
        setDropInvalid(true);
        setOverwriteMode(false);
        setMapOneToOneMode(false, true);
    }

    public static DraggingInfosPopup getNewShell(Shell parent, MapperManager mapperManager) {
        return new DraggingInfosPopup(parent, mapperManager);
    }

    /**
     * DOC amaumont Comment method "getBounds".
     * 
     * @return
     */
    public Rectangle getBounds() {
        return popup.getShell().getBounds();
    }

    /**
     * DOC amaumont Comment method "setVisible".
     * 
     * @param b
     */
    public void setVisible(boolean visible) {
        popup.setVisible(visible);
    }

    /**
     * DOC amaumont Comment method "setCountDragginEntries".
     * 
     * @param countEntries
     */
    public void setCountDraggingEntries(int countEntries) {
        String entries = " entries";
        if (countEntries <= 1) {
            entries = " entry";
        }
        popup.setCountEntriesText("Dragging " + countEntries + entries + ".");
    }

    /**
     * DOC amaumont Comment method "setCountDragginEntries".
     * 
     * @param countEntries
     */
    public void setOverwriteMode(boolean overwrite) {
        String mode = null;
        if (overwrite) {
            mode = " > Overwrite mode";
        } else {
            if(WindowSystem.isGTK()) {
                mode = " > Append mode";
            } else {
                mode = " > Append mode (Ctrl key to overwrite)";
            }
        }
        popup.setWriteModeText(mode);
    }

    /**
     * method "setCountDragginEntries".
     * 
     * if mapOneToOneAuthorized, mapOneToOne is forced to false
     * 
     * @param mapOneToOne
     * @param mapOneToOneAuthorized
     */
    public void setMapOneToOneMode(boolean mapOneToOne, boolean mapOneToOneAuthorized) {
        String mode = null;
        if (mapOneToOne && mapOneToOneAuthorized) {
            mode = " > Each source entry " + (this.outputToOutputMode ? "expression " : "") + "to each target expression";
            this.mapOneToOne = mapOneToOne;
        } else {
            mode = " > All source entries " + (this.outputToOutputMode ? "expression " : "") + "to a single one "
                    + (mapOneToOneAuthorized && !WindowSystem.isGTK() ? "target expression (Shift key to change mapping)" : "target expression");
            this.mapOneToOne = false;
        }
        popup.setMappingModeText(mode);
    }

    public void setOutputToOutputMode(boolean outputToOutputMode) {
        this.outputToOutputMode = outputToOutputMode;
    }

    /**
     * DOC amaumont Comment method "setCursorPosition".
     * 
     * @param x
     * @param y
     */
    public void setCursorPosition(int x, int y) {
        this.cursorPosition = new Point(x, y);
        // System.out.println("setCursorPosition");
        popup.adjustBounds();
    }

    public boolean isInsertionEntryContext() {
        return this.insertionEntryContext;
    }

    public void setInsertionEntryContext(boolean insertionEntryMode) {
        this.insertionEntryContext = insertionEntryMode;
        String newText = "null";
        if (this.insertionEntryContext && !this.mapOneToOne) {
            newText = " > Insert all selected entries";
        } else if (this.insertionEntryContext && this.mapOneToOne) {
            newText = " > Insert remaining entries";
        }
        if (!popup.labelInsertionEntryMode.getText().equals(newText)) {
            popup.setInsertionEntryText(newText);
            updateVisibleLabels();
        }
        this.insertionEntryContext = insertionEntryMode;
    }

    public void updateVisibleLabels() {
        // System.out.println(expressionContext + " "+insertionEntryContext);
        popup.setLabelsVisible(expressionContext, expressionContext, this.insertionEntryContext, this.dropInvalid);
        popup.adjustBounds();
    }

    public boolean isExpressionContext() {
        return this.expressionContext;
    }

    public void setExpressionContext(boolean expressionMode) {
        if (expressionMode != this.expressionContext) {
            this.expressionContext = expressionMode;
            updateVisibleLabels();
        }
    }

    public Point getPositionFromMapperShellOrigin() {
        Display display = popup.getShell().getDisplay();
        return display.map(popup.getShell(), this.parent, new Point(0, 0));
    }

    public boolean isMapOneToOne() {
        return this.mapOneToOne;
    }

    public void setDropInvalid(boolean dropInvalid) {
        String newText = "null";
        if (dropInvalid) {
            if(WindowSystem.isGTK()) {
                newText = "\n<< Drop invalid >>\n";
            }
        }
        if(WindowSystem.isGTK()) {
            this.dropInvalid = dropInvalid;
        } else {
            this.dropInvalid = false;
        }
            
        if (!popup.labelDropInvalid.getText().equals(newText)) {
            popup.setDropInvalidText(newText);
            updateVisibleLabels();
        }
    }

}
