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
package org.talend.repository.ui.wizards.genHTMLDoc;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.talend.repository.ui.wizards.exportjob.ExportFileResource;

/**
 * This class has utility methods for generating job information as HTML file.
 * 
 * $Id: JobHTMLScriptsManager.java 2007-3-8,下午03:15:50 ftang $
 * 
 */
public class JobHTMLScriptsManager {

    /**
     * Gets the set of <code>ExportFileResource</code>
     * 
     * @param process
     * @return
     */
    public List<ExportFileResource> getExportResources(ExportFileResource[] process) {
        for (int i = 0; i < process.length; i++) {
            HTMLDocGenerator.generateHTMLFile(process[i]);
        }
        return Arrays.asList(process);
    }

    /**
     * Deletes the temporary files.
     */
    public void deleteTempFiles() {
        String tmpFold = HTMLDocGenerator.getTmpFolder();
        File dir = new File(tmpFold);
        if (dir.exists()) {
            deleteDirectory(dir);
        }
    }

    /**
     * This method is used for deleting a directory.
     * 
     * @param dir
     */
    private void deleteDirectory(File dir) {
        File[] entries = dir.listFiles();
        int sz = entries.length;
        for (int i = 0; i < sz; i++) {
            if (entries[i].isDirectory()) {
                deleteDirectory(entries[i]);
            } else {
                entries[i].delete();
            }
        }
        dir.delete();
    }
}
