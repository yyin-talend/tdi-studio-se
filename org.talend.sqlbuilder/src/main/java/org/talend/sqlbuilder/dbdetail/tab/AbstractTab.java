// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2007 Talend - www.talend.com
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
package org.talend.sqlbuilder.dbdetail.tab;

import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.talend.sqlbuilder.dbdetail.IDetailTab;
import org.talend.sqlbuilder.dbstructure.nodes.INode;


/**
 * @author Davy Vanherbergen
 *
 */
public abstract class AbstractTab implements IDetailTab {

    private INode pNode;
    
    public final void setNode(INode node) {
        pNode = node;
    }
   
    public final INode getNode() {
        return pNode;
    }
    
    public abstract void fillDetailComposite(Composite composite);
    
    public final void fillComposite(final Composite composite) {
        
        BusyIndicator.showWhile(Display.getCurrent(), new Runnable() {

            public void run() {                
                fillDetailComposite(composite);                
            }
        });
        
    }

    public abstract String getLabelText();

    
    /* (non-Javadoc)
     * @see net.sourceforge.sqlexplorer.dbdetail.IDetailTab#getLabelToolTipText()
     */
    public String getLabelToolTipText() {        
        return getLabelText();
    }

    
    
}
