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
package org.talend.sqlbuilder.dataset.actions;

import net.sourceforge.sqlexplorer.Messages;
import net.sourceforge.sqlexplorer.util.ImageUtil;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.talend.sqlbuilder.SqlBuilderPlugin;

/**
 * Copy the column name of the selected column to the clipboard.
 * 
 * @author Davy Vanherbergen
 */
public class CopyColumnNameAction extends AbstractDataSetTableContextAction {

    private static final ImageDescriptor IMAGE = ImageUtil.getDescriptor("Images.ExportToClipBoardIcon");


    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.IAction#getText()
     */
    public String getText() {
        return Messages.getString("DataSetTable.Actions.CopyColumnName");
    }


    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.IAction#getImageDescriptor()
     */
    public ImageDescriptor getImageDescriptor() {
        return IMAGE;
    }


    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.IAction#run()
     */
    public void run() {

        try {

            Clipboard clipBoard = new Clipboard(Display.getCurrent());
            TextTransfer textTransfer = TextTransfer.getInstance();
            
            TableItem[] items = ptable.getSelection();
            
            if (items == null || items.length == 0) {
                return;
            }
                       
            int columnIndex = pcursor.getColumn();    
            TableColumn column = ptable.getColumn(columnIndex);

            clipBoard.setContents(new Object[] {column.getText()}, new Transfer[] {textTransfer});

        } catch (Exception e) {
            SqlBuilderPlugin.log("Error exporting cell to clipboard ", e);
        }
    }


    /**
     * Only show action if something is selected
     * @see net.sourceforge.sqlexplorer.dataset.actions.AbstractDataSetTableContextAction#isAvailable()
     */
    public boolean isAvailable() {
 
        return (ptable.getSelectionIndex() != -1);
    }

    
    
}
