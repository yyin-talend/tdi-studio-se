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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.talend.sqlbuilder.Messages;
import org.talend.sqlbuilder.SqlBuilderPlugin;
import org.talend.sqlbuilder.util.IConstants;
import org.talend.sqlbuilder.util.ImageUtil;

/**
 * This class is responsible for opening an existing file in current OS.
 * <br/>
 *
 * @author ftang
 *
 */
public class OpenFileAction extends  AbstractEditorAction {

    private ImageDescriptor img = ImageUtil.getDescriptor("Images.OpenFileIcon");


    public String getText() {
        return Messages.getString("Open_1"); //$NON-NLS-1$
    }

    public boolean isEnabled() {
        return true;
    }

    public void run() {

        //FileDialog dlg = new FileDialog(_editor.getSite().getShell(), SWT.OPEN | SWT.MULTI);
        
       FileDialog dlg = new FileDialog(editor.getShell(), SWT.OPEN | SWT.MULTI);

        dlg.setFilterExtensions(new String[] {"*.sql;*.txt;*.*"});

        String path = dlg.open();
        if (path != null) {
            String[] files = dlg.getFileNames();
            loadFiles(files, dlg.getFilterPath());
        }

    }


    /**
     * Load one or more files into the editor.
     * 
     * @param files string[] of relative file paths
     * @param filePath path where all files are found
     */
    public void loadFiles(String[] files, String filePath) {

        BufferedReader reader = null;

        try {

            StringBuffer all = new StringBuffer();
            String str = null;
            char delimiter = IConstants.LINE_DELIMITER;

            for (int i = 0; i < files.length; i++) {

                String path = "";
                if (filePath != null) {
                    path += filePath + File.separator;
                }
                path += files[i];

                reader = new BufferedReader(new FileReader(path));

                while ((str = reader.readLine()) != null) {
                    all.append(str);
                    all.append(delimiter);
                }

                if (files.length > 1) {
                    all.append(delimiter);
                }
            }

            editor.setEditorContent(all.toString());

        } catch (Throwable e) {
            SqlBuilderPlugin.log("Error loading document", e);

        } finally {
            try {
                reader.close();
            } catch (java.io.IOException e) {
                // noop
            }
        }

    }
    
    
    public String getToolTipText() {
        return Messages.getString("Open_2"); 
    }


    public ImageDescriptor getHoverImageDescriptor() {
        return img;
    }


    public ImageDescriptor getImageDescriptor() {
        return img;
    };
}
