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

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TableItem;
import org.talend.sqlbuilder.IConstants;
import org.talend.sqlbuilder.Messages;
import org.talend.sqlbuilder.SqlBuilderPlugin;
import org.talend.sqlbuilder.dataset.dataset.DataSet;
import org.talend.sqlbuilder.util.ImageUtil;

/**
 * Copy an entire datasettable to the clipboard.
 * 
 * @author Davy Vanherbergen
 */
public class CopyTableAction extends AbstractDataSetTableContextAction {

    private static final ImageDescriptor IMAGE = ImageUtil.getDescriptor("Images.CopyIcon");
    
    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.IAction#getText()
     */
    public String getText() {
        return Messages.getString("DataSetTable.Actions.CopyToClipboard");
    }


    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.IAction#getImageDescriptor()
     */
    public ImageDescriptor getImageDescriptor() {
        return IMAGE;
    }


    /**
     * Copy all table data to clipboard
     * @see org.eclipse.jface.action.IAction#run()
     */
    public void run() {

        BusyIndicator.showWhile(Display.getCurrent(), new Runnable() {

            public void run() {

                try {

                    // create clipboard
                    Clipboard clipBoard = new Clipboard(Display.getCurrent());
                    TextTransfer textTransfer = TextTransfer.getInstance();
                    StringBuffer buffer = new StringBuffer("");
                    
                    // get preferences
                    String lineSeparator = System.getProperty("line.separator");
                    String columnSeparator = SqlBuilderPlugin.getDefault().getPreferenceStore().getString(IConstants.CLIP_EXPORT_SEPARATOR);
                    boolean includeColumnNames = SqlBuilderPlugin.getDefault().getPreferenceStore().getBoolean(IConstants.CLIP_EXPORT_COLUMNS);
                    
                    
                    TableItem[] items = ptable.getItems();                    
                    DataSet dataSet = (DataSet) ptable.getData();
                    
                    if (items == null || dataSet == null) {
                        return;
                    }
                    
                    // export column names
                    if (includeColumnNames) {
                        
                        String[] columnNames = dataSet.getColumnLabels();
                        for (int i = 0; i < columnNames.length; i++) {
                            buffer.append(columnNames[i]);
                            buffer.append(columnSeparator);
                        }
                        buffer.append(lineSeparator);
                    }

                    // export column data
                    int columnCount = ptable.getColumnCount();
                    for (int i = 0; i < items.length; i++) {
                                               
                        for (int j = 0; j < columnCount; j++) {
                            buffer.append(items[i].getText(j));
                            buffer.append(columnSeparator);
                        }
                        buffer.append(lineSeparator);
                    }

                    // put all on clipboard
                    clipBoard.setContents(new Object[] {buffer.toString()}, new Transfer[] {textTransfer});

                } catch (Exception e) {
                    SqlBuilderPlugin.log("Error exporting to clipboard ", e);
                }
            }
        });

    }

}
