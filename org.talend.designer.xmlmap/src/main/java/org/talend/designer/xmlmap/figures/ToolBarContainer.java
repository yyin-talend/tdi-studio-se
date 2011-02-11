// ============================================================================
//
// Copyright (C) 2006-2011 Talend Inc. - www.talend.com
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

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Label;

/**
 * wchen class global comment. Detailled comment. If parent use ToolBarLayout , only add one children's height.
 */
public class ToolBarContainer extends Figure {

    private ButtonsImageToolBarFigure toolbar;

    private Label varText;

    public ButtonsImageToolBarFigure getToolbar() {
        return this.toolbar;
    }

    public void setToolbar(ButtonsImageToolBarFigure toolbar) {
        this.toolbar = toolbar;
    }

    public Label getVarText() {
        return this.varText;
    }

    public void setVarText(Label varText) {
        this.varText = varText;
    }

}
