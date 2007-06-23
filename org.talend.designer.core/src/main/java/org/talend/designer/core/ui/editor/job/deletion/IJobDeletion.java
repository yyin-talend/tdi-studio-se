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
package org.talend.designer.core.ui.editor.job.deletion;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.CoreException;
import org.talend.designer.core.ui.editor.process.Process;

/**
 * Resource registration and deletion when Talend editor opended and closed.
 * 
 * yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: IJobDeletion.java 下午04:00:39 2007-5-28 +0000 (2007-5-28) yzhang $
 * 
 */
public interface IJobDeletion {

    /**
     * Registration of the resources added by Talend editor opened.
     * 
     * yzhang Comment method "storeResource".
     * 
     * @param root
     * @param process
     */
    public void storeResource(IResourceDelta root, Process process);

    /**
     * Delete the registed resources related to current job.
     * 
     * yzhang Comment method "deleteJobs".
     */
    public void deleteJobs();

    /**
     * Delete all the related scripts files to job file, including folders and context files.
     * 
     * @param file the Job scripts. In java project, it's .java file, and in perl project, it's .pl file.
     */
    public void deleteRelatedJobs(IFile file) throws CoreException;

}
