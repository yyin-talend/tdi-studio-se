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
package org.talend.designer.mapper.ui.visualmap.link;

import org.eclipse.swt.graphics.GC;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public interface IGraphicLink {

    public void draw(GC gc, int yMinVisiblePoints, int yMaxVisiblePoints);

    public void calculate();

    public PointLinkDescriptor getPointLinkDescriptorSource();

    public PointLinkDescriptor getPointLinkDescriptorTarget();

    /**
     * DOC amaumont Comment method "setSate".
     * 
     * @param linkState
     */
    public void setState(LinkState linkState);

    /**
     * DOC amaumont Comment method "getSate".
     * 
     * @return stateLink
     */
    public LinkState getState();

}
