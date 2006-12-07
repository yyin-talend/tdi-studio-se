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
package org.talend.repository.model;

import org.talend.core.ui.images.IImage;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
 * 
 */
public class StableRepositoryNode extends RepositoryNode {

    private IImage icon;

    private String label;

    private int order;

    /**
     * DOC smallet StableRepositoryNode constructor comment.
     * 
     * @param object
     * @param parent
     * @param type
     */
    public StableRepositoryNode(RepositoryNode parent, String label, IImage icon, int order) {
        super(null, parent, ENodeType.STABLE_SYSTEM_FOLDER);
        this.label = label;
        this.icon = icon;
        this.order = order;
    }

    /**
     * Getter for icon.
     * 
     * @return the icon
     */
    public IImage getIcon() {
        return this.icon;
    }

    /**
     * Sets the icon.
     * 
     * @param icon the icon to set
     */
    public void setIcon(IImage icon) {
        this.icon = icon;
    }

    /**
     * Getter for label.
     * 
     * @return the label
     */
    public String getLabel() {
        return this.label;
    }

    /**
     * Sets the label.
     * 
     * @param label the label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Getter for order.
     * 
     * @return the order
     */
    public int getOrder() {
        return this.order;
    }

    /**
     * Sets the order.
     * 
     * @param order the order to set
     */
    public void setOrder(int order) {
        this.order = order;
    }

}
