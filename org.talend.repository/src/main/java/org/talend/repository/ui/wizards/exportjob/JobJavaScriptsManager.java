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
package org.talend.repository.ui.wizards.exportjob;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.talend.core.model.properties.ProcessItem;

/**
 * Manages the job scripts to be exported. <br/>
 * 
 * $Id: JobScriptsManager.java 1 2006-12-14 下午05:06:49 bqian
 * 
 */
public class JobJavaScriptsManager extends JobScriptsManager {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.wizards.exportjob.JobScriptsManager#getExportResources(org.talend.core.model.properties.ProcessItem[],
     * boolean, boolean, boolean, boolean, boolean, boolean, boolean, java.lang.String)
     */
    @Override
    public List<URL> getExportResources(ProcessItem[] process, boolean needLauncher, boolean needSystemRoutine,
            boolean needUserRoutine, boolean needModel, boolean needJob, boolean needContext, boolean needGenerateCode,
            String contextName) {

        return new ArrayList<URL>();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.wizards.exportjob.JobScriptsManager#getJobContexts(org.talend.core.model.properties.ProcessItem)
     */
    @Override
    public List<String> getJobContexts(ProcessItem processItem) {
        // TODO Auto-generated method stub
        return new ArrayList<String>();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.wizards.exportjob.JobScriptsManager#getLauncher()
     */
    @Override
    public String getLauncher() {
        // TODO Auto-generated method stub
        return "";
    }

}
