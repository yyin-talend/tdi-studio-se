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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.CoreException;
import org.talend.commons.exception.MessageBoxExceptionHandler;
import org.talend.designer.core.ui.editor.TalendEditor;
import org.talend.designer.core.ui.editor.process.Process;

/**
 * Delete java jobs after Talend editor is closed.
 * 
 * yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: JavaJobDeletion.java 下午04:02:44 2007-5-28 +0000 (2007-5-28) yzhang $
 * 
 */
public class JavaJobDeletion extends AbstractJobDeletion implements IJobDeletion {

    private static Map<Process, Set<IResource>> jobFolder = Collections
            .synchronizedMap(new HashMap<Process, Set<IResource>>());

    /**
     * Initialize process.
     * 
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
    public void storeResource(IResourceDelta root, Process p) {
        if (root == null || p == null || root.getAffectedChildren().length == 0) {
            return;
        }
        this.process = p;

        IResourceDelta[] childDeltas = root.getAffectedChildren();

        for (int i = 0; i < childDeltas.length; i++) {

            storeResource(childDeltas[i], p);
            IResourceDelta rd = childDeltas[i];

            if ((rd.getKind() == IResourceDelta.ADDED)
                    && rd.getResource().getType() == IResource.FOLDER
                    && (process.getLabel().equalsIgnoreCase(rd.getResource().getName()) || (withinRunJob(rd
                            .getResource().getName())))) {
                String[] jobName = containRunJob(this.process);
                if (jobName != null) {
                    for (int j = 0; j < jobName.length; j++) {
                        if (!jobNamesWithInRunJob.contains(jobName[j])) {
                            jobNamesWithInRunJob.add(jobName[j]);
                        }
                    }
                }
                if (jobFolder.get(this.process) == null) {
                    Set<IResource> set = new HashSet<IResource>();
                    set.add(rd.getResource());
                    jobFolder.put(this.process, set);
                } else {
                    Set<IResource> set = jobFolder.get(this.process);
                    set.add(rd.getResource());
                }

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
                    for (Process storedProcess : jobFolder.keySet()) {
                        if (storedProcess.getName().equals(jobName[i]) && storedProcess.isClosed()) {
                            for (IResource resrouce : jobFolder.get(storedProcess)) {
                                resrouce.delete(true, null);
                            }
                            jobs.add(storedProcess);
                        }
                    }
                    jobNamesWithInRunJob.remove(jobName[i]);
                }
                if (jobs.size() > 0) {
                    for (Process job : jobs) {
                        jobFolder.remove(job);
                    }
                }
                for (IResource resource : jobFolder.get(this.process)) {
                    Process p = TalendEditor.isProcessOpend(resource.getName());
                    if (p != null) {
                        Set<IResource> set = jobFolder.get(p);
                        if (set == null) {
                            set = new HashSet<IResource>();
                            set.add(resource);
                            jobFolder.put(p, set);
                        } else {
                            set.add(resource);
                        }
                        continue;
                    }
                    resource.delete(true, null);
                }
                jobFolder.remove(this.process);

            } else if (!withinRunJob(this.process)) {
                for (IResource resource : jobFolder.get(this.process)) {
                    resource.delete(true, null);
                }
                jobFolder.remove(this.process);
            }

        } catch (CoreException e) {
            MessageBoxExceptionHandler.process(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.ui.editor.job.deletion.IJobDeletion#deleteRelatedJobs(org.eclipse.core.resources.IFile)
     */
    public void deleteRelatedJobs(IFile file) throws CoreException {
        // get the parent package first , then delete it. Because the package contains the job and context scripts.
        file.getParent().delete(true, null);
    }
}
