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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.CoreException;
import org.talend.commons.exception.MessageBoxExceptionHandler;
import org.talend.designer.core.ui.editor.process.Process;

/**
 * DOC yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: JavaJobDeletion.java 下午04:02:44 2007-5-28 +0000 (2007-5-28) yzhang $
 * 
 */
public class JavaJobDeletion extends AbstractJobDeletion implements IJobDeletion {

    private static Map<Process, IResource> jobFolders = Collections.synchronizedMap(new HashMap<Process, IResource>());

    /**
     * yzhang JavaJobDeletion constructor comment.
     * 
     * @param process
     */
    public JavaJobDeletion(Process process) {
        super(process);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.job.deletion.IJobDeletion#storeResource(org.eclipse.core.resources.IResourceDelta)
     */
    public void storeResource(IResourceDelta root) {

        if (root.getAffectedChildren().length == 0) {
            return;
        }

        IResourceDelta[] childDeltas = root.getAffectedChildren();
        for (int i = 0; i < childDeltas.length; i++) {

            storeResource(childDeltas[i]);
            IResourceDelta rd = childDeltas[i];

            if ((rd.getKind() == IResourceDelta.ADDED) && rd.getResource().getType() == IResource.FOLDER
                    && process.getLabel().equals(rd.getResource().getName())) {
                String[] jobName = containRunJob(this.process);
                if (jobName != null) {
                    for (int j = 0; j < jobName.length; j++) {
                        if (!jobNamesWithInRunJob.contains(jobName[j])) {
                            jobNamesWithInRunJob.add(jobName[j]);
                        }
                    }
                }
                jobFolders.put(this.process, rd.getResource());

            }

        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.job.deletion.IJobDeletion#deleteJobs()
     */
    public void deleteJobs() {
        try {
            String[] jobName = containRunJob(this.process);
            if (jobName != null) {
                List<Process> jobs = new ArrayList<Process>();
                for (int i = 0; i < jobName.length; i++) {
                    for (Process storedProcess : jobFolders.keySet()) {
                        if (storedProcess.getName().equals(jobName[i]) && storedProcess.isClosed()) {
                            jobFolders.get(storedProcess).delete(true, null);
                            jobs.add(storedProcess);
                        }
                    }
                    jobNamesWithInRunJob.remove(jobName[i]);
                }
                if (jobs.size() > 0) {
                    for (Process job : jobs) {
                        jobFolders.remove(job);
                    }
                }
                jobFolders.get(this.process).delete(true, null);
                jobFolders.remove(this.process);

            } else if (!withinRunJob(this.process)) {
                jobFolders.get(this.process).delete(true, null);
                jobFolders.remove(this.process);
            }

        } catch (CoreException e) {
            MessageBoxExceptionHandler.process(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.job.deletion.IJobDeletion#setProcess(org.talend.designer.core.ui.editor.process.Process)
     */
    public void setProcess(Process pro) {
        this.process = pro;

    }

}
