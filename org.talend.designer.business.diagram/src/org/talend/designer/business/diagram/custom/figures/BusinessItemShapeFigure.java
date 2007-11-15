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
package org.talend.designer.business.diagram.custom.figures;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.gef.ui.figures.DefaultSizeNodeFigure;

/**
 * DOC mhelleboid class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class BusinessItemShapeFigure extends DefaultSizeNodeFigure {

    /**
     * DOC mhelleboid BusinessItemShapeFigure constructor comment.
     */
    public BusinessItemShapeFigure() {
        // PTODO mhelleboid 50
        super(50, 50);
    }

    /**
     * DOC mhelleboid Comment method "getInnerBounds".
     */
    protected Rectangle getInnerBounds() {
        Rectangle innerBounds = new Rectangle();

        innerBounds.x = bounds.x;
        innerBounds.y = bounds.y;
        innerBounds.width = bounds.width - 1;
        innerBounds.height = bounds.height - 1;

        return innerBounds;
    }
}
