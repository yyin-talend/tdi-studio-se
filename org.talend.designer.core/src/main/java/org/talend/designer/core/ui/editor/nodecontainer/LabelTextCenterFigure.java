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
package org.talend.designer.core.ui.editor.nodecontainer;

import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Point;

/**
 * DOC YeXiaowei class global comment. Detailled comment
 */
public class LabelTextCenterFigure extends Label {

    private static final int X_OFFSET = 5;

    private static final int Y_OFFSET = 4;

    @Override
    protected Point getTextLocation() {
        Point iconLocation = getIconLocation();
        Point textLocation = new Point(iconLocation.x + X_OFFSET, iconLocation.y + Y_OFFSET);
        return textLocation;
    }

}
