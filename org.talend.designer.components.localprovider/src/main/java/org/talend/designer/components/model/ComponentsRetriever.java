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
package org.talend.designer.components.model;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.eclipse.jface.preference.IPreferenceStore;
import org.talend.core.CorePlugin;
import org.talend.designer.components.Activator;
import org.talend.designer.components.ui.ComponentsPreferencePage;

/**
 * This class aim is to retrieve components from a file system folder to the location they must be for loading.<br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
 * 
 */
public class ComponentsRetriever {

    public static void retrieveComponents(File target) {
        File externalComponentsLocation = getExternalComponentsLocation();
        try {
            copyFolder(externalComponentsLocation, target, true);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void copyFolder(File source, File target, boolean emptyTargetBeforeCopy) throws IOException {
        if (emptyTargetBeforeCopy) {
            emptyFolder(target);
        }

        FileFilter folderFilter = new FileFilter() {

            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }

        };
        FileFilter fileFilter = new FileFilter() {

            public boolean accept(File pathname) {
                return !pathname.isDirectory();
            }

        };

        for (File current : source.listFiles(folderFilter)) {
            File newFolder = new File(target, current.getName());
            newFolder.mkdir();
            copyFolder(current, newFolder, emptyTargetBeforeCopy);
        }

        for (File current : source.listFiles(fileFilter)) {
            File out = new File(target, current.getName());
            copyFile(current, out);
        }
    }

    private static void emptyFolder(File toEmpty) {
        for (File current : toEmpty.listFiles()) {
            if (current.isDirectory()) {
                emptyFolder(current);
            }
            current.delete();
        }
    }

    public static void copyFile(File source, File target) throws IOException {
        FileInputStream fis = new FileInputStream(source);
        FileOutputStream fos = new FileOutputStream(target);
        byte[] buf = new byte[1024];
        int i = 0;
        while ((i = fis.read(buf)) != -1) {
            fos.write(buf, 0, i);
        }
        fis.close();
        fos.close();
    }

    private static File getExternalComponentsLocation() {
        IPreferenceStore prefStore = Activator.getDefault().getPreferenceStore();
        return new File(prefStore.getString(ComponentsPreferencePage.USER_COMPONENTS_FOLDER));
    }
}
