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
package org.talend.sqlbuilder.dataset.actions;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import net.sourceforge.sqlexplorer.IConstants;
import net.sourceforge.sqlexplorer.Messages;
import net.sourceforge.sqlexplorer.dataset.DataSet;
import net.sourceforge.sqlexplorer.util.ImageUtil;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.TableItem;
import org.talend.sqlbuilder.SqlBuilderPlugin;

/**
 * Copy an entire datasettable to the clipboard.
 * 
 * @author Davy Vanherbergen
 */
public class ExportXLSAction extends AbstractDataSetTableContextAction {

    private static final ImageDescriptor IMAGE = ImageUtil.getDescriptor("Images.ExportIcon"); //$NON-NLS-1$

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.IAction#getText()
     */
    public String getText() {
        return Messages.getString("DataSetTable.Actions.Export.XLS"); //$NON-NLS-1$
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
     * Copy all table data to clipboard.
     * 
     * @see org.eclipse.jface.action.IAction#run()
     */
    public void run() {

        FileDialog fileDialog = new FileDialog(ptable.getShell(), SWT.SAVE);
        String[] filterExtensions = new String[] { "*.xls" }; //$NON-NLS-1$
        fileDialog.setFilterExtensions(filterExtensions);

        final String fileName = fileDialog.open();
        if (fileName == null && fileName.trim().length() == 0) {
            return;
        }

        BusyIndicator.showWhile(Display.getCurrent(), new Runnable() {

            public void run() {

                try {

                    File file = new File(fileName);

                    if (file.exists()) {
                        // overwrite existing files
                        file.delete();
                    }

                    file.createNewFile();
                    BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                    StringBuffer buffer = new StringBuffer(""); //$NON-NLS-1$

                    // get preferences
                    boolean includeColumnNames = SqlBuilderPlugin.getDefault().getPreferenceStore().getBoolean(
                            IConstants.CLIP_EXPORT_COLUMNS);

                    TableItem[] items = ptable.getItems();
                    DataSet dataSet = (DataSet) ptable.getData();

                    if (items == null || dataSet == null) {
                        return;
                    }

                    writer.write("<table>"); //$NON-NLS-1$
                    writer.newLine();

                    // export column names
                    if (includeColumnNames) {

                        buffer.append("<tr>"); //$NON-NLS-1$
                        String[] columnNames = dataSet.getColumnLabels();
                        for (int i = 0; i < columnNames.length; i++) {
                            buffer.append("<th>"); //$NON-NLS-1$
                            buffer.append(columnNames[i]);
                            buffer.append("</th>"); //$NON-NLS-1$
                        }
                        buffer.append("</tr>"); //$NON-NLS-1$
                        writer.write(buffer.toString());
                        writer.newLine();
                    }

                    // export column data
                    int columnCount = ptable.getColumnCount();
                    for (int i = 0; i < items.length; i++) {

                        buffer = new StringBuffer("<tr>"); //$NON-NLS-1$

                        for (int j = 0; j < columnCount; j++) {
                            buffer.append("<td>"); //$NON-NLS-1$
                            buffer.append(items[i].getText(j));
                            buffer.append("</td>"); //$NON-NLS-1$
                        }

                        buffer.append("</tr>"); //$NON-NLS-1$

                        writer.write(buffer.toString());
                        writer.newLine();
                    }

                    writer.write("</table>"); //$NON-NLS-1$
                    writer.newLine();

                    writer.close();

                } catch (final Exception e) {
                    ptable.getShell().getDisplay().asyncExec(new Runnable() {

                        public void run() {
                            MessageDialog.openError(ptable.getShell(), Messages
                                    .getString("SQLResultsView.Error.Export.Title"), e.getMessage()); //$NON-NLS-1$
                            SqlBuilderPlugin.log(Messages.getString("SQLResultsView.Error.Export.Title"), e); //$NON-NLS-1$
                        }
                    });
                }
            }
        });

    }

}
