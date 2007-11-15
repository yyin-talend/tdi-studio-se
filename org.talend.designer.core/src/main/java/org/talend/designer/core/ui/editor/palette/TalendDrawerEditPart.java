// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
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

import org.eclipse.draw2d.FocusEvent;
import org.eclipse.draw2d.FocusListener;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.internal.ui.palette.editparts.DrawerEditPart;
import org.eclipse.gef.internal.ui.palette.editparts.DrawerFigure;
import org.eclipse.gef.palette.PaletteDrawer;

/**
 * 
 */
public class TalendDrawerEditPart extends DrawerEditPart {

    private int childLevel = 0;
    
    public TalendDrawerEditPart(PaletteDrawer drawer) {
        super(drawer);
    }

    public IFigure createFigure() {
        if (getParent() instanceof TalendDrawerEditPart) {
            TalendDrawerEditPart parent = (TalendDrawerEditPart) getParent();
            childLevel = parent.childLevel + 1;

            TalendDrawerFigure fig = new TalendDrawerFigure(getViewer().getControl(), childLevel) {
                IFigure buildTooltip() {
                    return createToolTip();
                }
            };
            fig.setExpanded(getDrawer().isInitiallyOpen());
            fig.setPinned(getDrawer().isInitiallyPinned());

            fig.getCollapseToggle().addFocusListener(new FocusListener.Stub() {
                public void focusGained(FocusEvent fe) {
                    getViewer().select(TalendDrawerEditPart.this);
                }
            });

            return fig;
        }
        return super.createFigure();
    }

}
