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
package org.talend.sqlbuilder.sessiontree.model;

import java.util.ArrayList;

import org.talend.sqlbuilder.Messages;

/**
 * The root node in the session tree; It shows the number of active sessions.
 */
public class RootSessionTreeNode implements ISessionTreeNode {

    private static final int CAPACITY = 10;
    
    private ArrayList ls = new ArrayList(CAPACITY);

    public RootSessionTreeNode() {
    }
    
    /**
     * @return SessionTreeNode[]
     */
    @SuppressWarnings("unchecked") //$NON-NLS-1$
    public SessionTreeNode[] getSessionTreeNodes() {
        return (SessionTreeNode[]) ls.toArray(new SessionTreeNode[0]);
    }
    
    /**
     * @return Children.
     */
    public Object[] getChildren() {
        return ls.toArray();
    }

    /**
     * @return Parent.
     * @see org.gnu.amaz.ISessionTreeNode#getParent()
     */
    public Object getParent() {
        return null;
    }
    
    /**
     * Override toString method.
     * @return String.
     */
    public String toString() {
        int sz = ls.size();
        if (sz == 0) {
            return ""; //$NON-NLS-1$
        } else if (sz == 1) {
            return ""; //$NON-NLS-1$
        } else {
            return "" + ls.size() + ""; //$NON-NLS-1$ //$NON-NLS-2$
        }
    }
    
    /**
     * @param node SessionTreeNode.
     */
    @SuppressWarnings("unchecked") //$NON-NLS-1$
    public void add(ISessionTreeNode node) {
        ls.add(node);
    }
    
    /**
     * @param nd SessionTreeNode.
     */
    public void remove(SessionTreeNode nd) {
        ls.remove(nd);
    }
    
    /**
     * Close All Connections.
     */
    public void closeAllConnections() {
        int s = ls.size();
        Object[] obj = ls.toArray();
        for (int i = 0; i < s; i++) {
            ((SessionTreeNode) obj[i]).close();
        }
    }
}
