// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//   
// ============================================================================
package org.talend.repository.ui.wizards.genHTMLDoc;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.talend.core.model.genhtml.HTMLDocUtils;
import org.talend.repository.ui.wizards.exportjob.ExportFileResource;

/**
 * This class has utility methods for generating job information as HTML file.
 * 
 * $Id: JobHTMLScriptsManager.java 2007-3-8,下午03:15:50 ftang $
 * 
 */
public class JobHTMLScriptsManager {

    private HTMLDocGenerator docGenerator = new HTMLDocGenerator();

    /**
     * Gets the set of <code>ExportFileResource</code>
     * 
     * @param process
     * @return
     */
    public List<ExportFileResource> getExportResources(ExportFileResource[] process) {
        for (int i = 0; i < process.length; i++) {
            docGenerator.generateHTMLFile(process[i]);
        }
        return Arrays.asList(process);
    }

    /**
     * Deletes the temporary files.
     */
    public void deleteTempFiles() {
        String tmpFold = HTMLDocUtils.getTmpFolder();
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
                // System.out.println("" + entries[i].delete() + " *** " + entries[i]);
                entries[i].delete();
            }
        }
        dir.delete();
    }
}
