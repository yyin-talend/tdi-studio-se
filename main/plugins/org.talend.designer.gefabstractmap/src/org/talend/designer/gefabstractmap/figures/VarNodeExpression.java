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
package org.talend.designer.gefabstractmap.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * DOC talend class global comment. Detailled comment
 */
public class VarNodeExpression extends ExpressionFigure {

    @Override
    protected void paintFigure(Graphics graphics) {

        if (isOpaque()) {
            super.paintFigure(graphics);
        }
        Rectangle bounds = getBounds();
        graphics.translate(bounds.x, bounds.y);
        if (getIcon() != null) {
            graphics.drawImage(getIcon(), getIconLocation());
        }
        if (!isEnabled()) {
            graphics.translate(1, 1);
            graphics.setForegroundColor(ColorConstants.buttonLightest);
            graphics.drawText(getSubStringText(), getTextLocation());
            graphics.translate(-1, -1);
            graphics.setForegroundColor(ColorConstants.buttonDarker);
        }
        graphics.drawText(getSubStringText(), getTextLocation());
        graphics.translate(-bounds.x, -bounds.y);

    }

}
