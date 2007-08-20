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
package org.talend.designer.core.ui.views.problems;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.markers.internal.MarkerMessages;
import org.talend.core.model.process.Element;
import org.talend.core.model.process.IElement;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.process.Problem;
import org.talend.core.model.process.Problem.ProblemStatus;
import org.talend.core.model.process.Problem.ProblemType;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.process.Process;

import com.ibm.icu.text.MessageFormat;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class Problems {

    /**
     * This enum is used for marking the group type for problems. <br/>
     * 
     */
    public enum Group {
        SEVERITY,
        TYPE,
        NONE;
    }

    private static IProcess currentProcess = null;

    private static String currentTitle = ""; //$NON-NLS-1$

    private static String newTitle = ""; //$NON-NLS-1$

    private static Group group = Group.SEVERITY;

    public static ProblemCategory[] categories = null;

    private static ProblemList problemList = new ProblemList();

    public static void clearAll() {
        problemList.clear();
    }

    public static String getSummary() {
        int[] counts = problemList.getMarkerCounts();
        return MessageFormat.format("{0,choice,0#0 errors|1#{0,number,integer} error|1<{0,number,integer} errors}, {1,choice,0#0 warnings|1#{1,number,integer} warning|1<{1,number,integer} warnings}, {2,choice,0#0 infos|1#{2,number,integer} info|1<{2,number,integer} infos}", new Object[] { new Integer(counts[0]),
                new Integer(counts[1]), new Integer(counts[2]) });
    }

    private static void buildHierarchy() {
        if (group.equals(Group.SEVERITY)) {
            categories = new ProblemCategory[3];
            categories[0] = new SeverityProblemCategory(problemList, ProblemStatus.ERROR);
            categories[0].setName(Messages.getString("Problems.category.errors")); //$NON-NLS-1$

            categories[1] = new SeverityProblemCategory(problemList, ProblemStatus.WARNING);
            categories[1].setName(Messages.getString("Problems.category.warnings")); //$NON-NLS-1$

            categories[2] = new SeverityProblemCategory(problemList, ProblemStatus.INFO);
            categories[2].setName(Messages.getString("Problems.category.infos")); //$NON-NLS-1$
        } else if (group.equals(Group.TYPE)) {
            categories = new ProblemCategory[2];
            categories[0] = new TypeProblemCategory(problemList, ProblemType.JOB);
            categories[0].setName(Messages.getString("Problems.category.jobs")); //$NON-NLS-1$

            categories[1] = new TypeProblemCategory(problemList, ProblemType.ROUTINE);
            categories[1].setName(Messages.getString("Problems.category.routines")); //$NON-NLS-1$
        } else {
            categories = null;
        }

    }

    public static List<? extends Problem> getRoot() {
        if (categories == null) {
            buildHierarchy();
        }
        if (categories != null) {
            return Arrays.asList(categories);
        } else {
            return problemList.getProblemList();
        }
    }

    public static void clearAll(Element element) {
        for (ProblemStatus status : ProblemStatus.values()) {
            // remove(status, element);
        }
    }

    public static void add(ProblemStatus status, Element element, String description) {
        Problem problem = new Problem(element, description, status);
        add(problem);
    }

    public static void addAll(List<Problem> problems) {
        for (Problem current : problems) {
            add(current);
        }
    }

    public static void add(Problem problem) {
        problemList.add(problem);
        refreshView();
    }

    public static List<String> getStatusList(ProblemStatus status, Element element) {
        List<String> statusList = new ArrayList<String>();

        for (Problem problem : problemList.getProblemList()) {
            if (problem.getElement().equals(element)&&problem.getStatus().equals(status)) {
                statusList.add(problem.getDescription());
            }
        }
        return statusList;
    }

    /**
     * DOC set the current process value,according the current process id to initial the current problems.
     * 
     * @param process
     */
    public static void setCurrentProcess(IProcess process) {
        currentProcess = process;
        initCurrentProblems();

    }

    /**
     * DOC check the problems the corresponding of current process .
     */
    private static void initCurrentProblems() {
        ((Process) currentProcess).checkProcess();
    }

    public static void recheckCurrentProblems(ProblemsView view) {
        if (currentProcess == null) {
            return;
        }
        ((Process) currentProcess).checkNodeProblems();
        view.refresh();
    }

    public static void setTitle(String title) {
        newTitle = title;
    }

    public static void refreshView() {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IViewPart view = page.findView("org.talend.designer.core.ui.views.ProblemsView"); //$NON-NLS-1$
        if (view instanceof ProblemsView) {
            ProblemsView problemsView = (ProblemsView) view;
            refreshView(problemsView);
        }
    }

    public static void refreshView(ProblemsView problemsView) {
        if (problemsView != null) {
            if (!newTitle.equals(currentTitle)) {
                problemsView.setPartName(newTitle);
                currentTitle = newTitle;
            }
        }

        problemsView.refresh();

        List<? extends INode> nodes = currentProcess.getGraphicalNodes();
        for (INode inode : nodes) {
            if (inode instanceof Node) {
                Node node = (Node) inode;
                refreshNodeStatus(node, problemList.getProblemList());
            }
        }
    }

    /**
     * DOC bqian Comment method "refreshNodeStatus".
     * 
     * @param node
     * @param problemList2
     */
    private static void refreshNodeStatus(Node node, List<Problem> problemList) {

        boolean hasStatus = false;
        for (Problem problem : problemList) {
            if (!problem.getElement().equals(node)) {
                continue;
            }
            hasStatus = true;
            if (problem.getStatus().equals(ProblemStatus.WARNING)) {
                node.addStatus(Process.WARNING_STATUS);
            } else if (problem.getStatus().equals(ProblemStatus.ERROR)) {
                node.addStatus(Process.ERROR_STATUS);
            }
        }
        if (!hasStatus) {
            node.removeStatus(Process.WARNING_STATUS);
            node.removeStatus(Process.ERROR_STATUS);
        }
    }

    /**
     * DOC bqian Comment method "removeProblemsByProcessId".
     * 
     * @param id
     */
    public static void removeProblemsByProcessId(String id) {
        // TODO Auto-generated method stub

    }
}
