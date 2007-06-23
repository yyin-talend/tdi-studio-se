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
import java.util.Iterator;
import java.util.List;

import org.talend.designer.core.model.components.EParameterName;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.process.Process;

/**
 * DOC yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: AbstractJobDeletion.java 下午05:39:01 2007-5-28 +0000 (2007-5-28) yzhang $
 * 
 */
public abstract class AbstractJobDeletion {

    protected static List<String> jobNamesWithInRunJob = new ArrayList<String>();

    protected Process process;

    /**
     * Constructor of this class.
     * 
     * yzhang AbstractJobDeletion constructor comment.
     */
    private AbstractJobDeletion() {
        // do nothing.
    }

    /**
     * Constructor of this class.
     * 
     * yzhang AbstractJobDeletion constructor comment.
     * 
     * @param pro
     */
    public AbstractJobDeletion(Process pro) {
        process = pro;
    }

    /**
     * If there a tRunJob node within the job.
     * 
     * yzhang Comment method "containRunJob".
     * 
     * @param job
     * @return
     */
    protected String[] containRunJob(Process job) {
        if (job == null) {
            return null;
        }
        List list = job.getGraphicalNodes();
        List<String> strList = new ArrayList<String>();
        for (Iterator iter = list.iterator(); iter.hasNext();) {
            Node node = (Node) iter.next();
            if ((node != null) && node.getComponent().getName().equals("tRunJob")) {
                strList.add(((String) node.getPropertyValue(EParameterName.PROCESS_TYPE_PROCESS.getName())).replaceAll(
                        "'", ""));
            }
        }
        return strList.size() > 0 ? strList.toArray(new String[1]) : null;
    }

    /**
     * To see whether this job is within a run job component.
     * 
     * yzhang Comment method "withinRunJob".
     * 
     * @param job
     * @return
     */
    protected boolean withinRunJob(Process job) {
        if (job == null) {
            return true;
        }
        for (String jobName : jobNamesWithInRunJob) {
            if (job.getName().equalsIgnoreCase(jobName)) {
                return true;
            }
        }

        return false;
    }

    /**
     * To see whether this job is within a run job component.
     * 
     * yzhang Comment method "withinRunJob".
     * 
     * @param str
     * @return
     */
    protected boolean withinRunJob(String str) {
        for (String jobName : jobNamesWithInRunJob) {
            if (str.equalsIgnoreCase(jobName)) {
                return true;
            }
        }
        return false;
    }
}
