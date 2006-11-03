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
package org.talend.repository.ui.wizards.metadata.connection.files.xml;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Tree;
import org.talend.commons.ui.swt.drawing.background.BackgroundRefresher;
import org.talend.commons.ui.swt.drawing.link.HorizontalBezierLink;
import org.talend.commons.ui.swt.drawing.link.IDrawableLink;
import org.talend.commons.ui.swt.drawing.link.StyleLink;


/**
 * DOC amaumont  class global comment. Detailled comment
 * <br/>
 *
 * $Id$
 *
 */
public class TreeToTableLinker extends BackgroundRefresher {

    private Tree tree;
    
    private Table table;

    private StyleLink styleLink;

    /**
     * DOC amaumont TreeToTableLinker constructor comment.
     * @param tree
     * @param table
     */
    public TreeToTableLinker(Composite commonParent, Tree tree, Table table) {
        super(commonParent);
        this.tree = tree;
        this.table = table;
        init();
    }

    private void init() {
        styleLink = new StyleLink();
        styleLink.setForegroundColor(commonParent.getDisplay().getSystemColor(SWT.COLOR_BLACK));
        styleLink.setDrawableLink(new HorizontalBezierLink(styleLink));

        ControlListener controlListener = new ControlListener() {

            public void controlMoved(ControlEvent e) {
                updateBackground();
            }

            public void controlResized(ControlEvent e) {
                updateBackground();
            }
            
        };
        
        table.addControlListener(controlListener);
        tree.addControlListener(controlListener);
        
    }
    
    /**
     * Getter for table.
     * @return the table
     */
    public Table getTable() {
        return this.table;
    }

    
    /**
     * Getter for tree.
     * @return the tree
     */
    public Tree getTree() {
        return this.tree;
    }



    /* (non-Javadoc)
     * @see org.talend.commons.ui.swt.drawing.link.BackgroundRefresher#drawBackground(org.eclipse.swt.graphics.GC)
     */
    @Override
    public void drawBackground(GC gc) {
        IDrawableLink drawableLink = styleLink.getDrawableLink();
        drawableLink.setPoint1(new Point(140, 143));
        
        Point point = table.getDisplay().map(table, commonParent, new Point(0, 0 + 25));
//        System.out.println(point);
        drawableLink.setPoint2(point);
//        drawableLink.setPoint2(new Point(140, 243));
        drawableLink.draw(gc);
    }

}
