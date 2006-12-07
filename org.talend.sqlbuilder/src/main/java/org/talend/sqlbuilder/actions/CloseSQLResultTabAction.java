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
package org.talend.sqlbuilder.actions;

import java.net.URL;

import org.talend.sqlbuilder.util.URLUtil;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Display;
import org.talend.sqlbuilder.Messages;
import org.talend.sqlbuilder.SqlBuilderPlugin;



/**
 * DOC dev  class global comment. Detailled comment
 * <br/>
 *
 * $Id: CloseSQLResultTabAction.java,v 1.6 2006/11/03 10:09:21 yi.zhang Exp $
 *
 */
public class CloseSQLResultTabAction extends Action {
    private ImageDescriptor img = getDescriptor(Messages.getString("Images.CloseIcon"));
    /**
     * Holds a reference to the TableItem we should close.
     */
    private CTabItem tabItem;
    
    /**
     * Default Constructor.
     *
     */
    public CloseSQLResultTabAction() {
        super();
    }
    
    /**
     * Constructor.
     * @param tabItem
     */
    public CloseSQLResultTabAction(CTabItem tabItem) {
        super();
        this.tabItem = tabItem;
    }
    
    public ImageDescriptor getHoverImageDescriptor() {
        return img;
    }

    public ImageDescriptor getImageDescriptor() {
        return img;
    }

    public String getToolTipText() {
        return "Close";  //$NON-NLS-1$
    }
    
    private ImageDescriptor getDescriptor(String path) {
        if (path == null || path.trim().length() == 0) {
            SqlBuilderPlugin.log("Missing image path for " + path, null);
            return null;
        }

        // create image
        URL url = URLUtil.getFragmentResourceURL(SqlBuilderPlugin.PLUGIN_ID, path);
        return ImageDescriptor.createFromURL(url);
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {
        BusyIndicator.showWhile(Display.getCurrent(), new Runnable() {
            public void run() {
                tabItem.dispose();
            }
        });
    }
}
