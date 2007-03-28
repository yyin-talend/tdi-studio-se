// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006 Talend – www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License.
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
