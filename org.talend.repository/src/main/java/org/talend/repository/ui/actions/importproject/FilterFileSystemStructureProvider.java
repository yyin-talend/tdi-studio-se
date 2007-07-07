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
package org.talend.repository.ui.actions.importproject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
import org.eclipse.ui.wizards.datatransfer.IImportStructureProvider;

/**
 * This class provides information regarding the structure and content of specified file system File objects.
 */
public class FilterFileSystemStructureProvider implements IImportStructureProvider {

    private static final String SVN_FOLDER = ".svn";

    /**
     * Holds a singleton instance of this class.
     */
    public final static FilterFileSystemStructureProvider INSTANCE = new FilterFileSystemStructureProvider();

    /**
     * Creates an instance of <code>FileSystemStructureProvider</code>.
     */
    private FilterFileSystemStructureProvider() {
        super();
    }

    /*
     * (non-Javadoc) Method declared on IImportStructureProvider
     */
    public List getChildren(Object element) {
        File folder = (File) element;

        File[] files = folder.listFiles(new FilenameFilter() {

            public boolean accept(File dir, String name) {
                return name.lastIndexOf(SVN_FOLDER) == -1 ? true : false;
            }
        });
        int childrenLength = files == null ? 0 : files.length;
        List<File> result = new ArrayList(childrenLength);
        for (int i = 0; i < childrenLength; i++) {
            result.add(files[i]);
        }

        return result;
    }

    /*
     * (non-Javadoc) Method declared on IImportStructureProvider
     */
    public InputStream getContents(Object element) {
        try {
            return new FileInputStream((File) element);
        } catch (FileNotFoundException e) {
            IDEWorkbenchPlugin.log(e.getLocalizedMessage(), e);
            return null;
        }
    }

    /*
     * (non-Javadoc) Method declared on IImportStructureProvider
     */
    public String getFullPath(Object element) {
        return ((File) element).getPath();
    }

    /*
     * (non-Javadoc) Method declared on IImportStructureProvider
     */
    public String getLabel(Object element) {

        // Get the name - if it is empty then return the path as it is a file root
        File file = (File) element;
        String name = file.getName();
        if (name.length() == 0) {
            return file.getPath();
        }
        return name;
    }

    /*
     * (non-Javadoc) Method declared on IImportStructureProvider
     */
    public boolean isFolder(Object element) {
        return ((File) element).isDirectory();
    }
}
