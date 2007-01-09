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
package org.talend.designer.core.ui.views.problems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.utils.data.container.MapList;
import org.talend.core.model.process.Element;
import org.talend.core.model.process.IElement;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.process.Problem;
import org.talend.core.model.process.Problem.ProblemAction;
import org.talend.core.model.process.Problem.ProblemStatus;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.process.Process;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class Problems {

    private static MapList<ProblemStatus, Problem> defaultProblems = new MapList<ProblemStatus, Problem>();

    private static MapList<ProblemStatus, Problem> currentProblems = getDefaultProblems();

    private static Map<String, MapList<ProblemStatus, Problem>> problemsMap = new HashMap<String, MapList<ProblemStatus, Problem>>();

    private static IProcess currentProcess = null;

    private static String currentTitle = "";

    private static String newTitle = "";

    public static void clearAll() {
        for (List<Problem> problemList : currentProblems.values()) {
            for (Problem problem : problemList) {
                problem.setAction(ProblemAction.DELETED);
            }
        }
    }

    public static void clearAll(Element element) {
        for (ProblemStatus status : ProblemStatus.values()) {
            remove(status, element);
        }
    }

    private static MapList<ProblemStatus, Problem> getProblemsByProcessId(String processId) {
        MapList<ProblemStatus, Problem> curProblems = problemsMap.get(processId);
        if (curProblems == null) {
            curProblems = new MapList<ProblemStatus, Problem>();
            problemsMap.put(processId, curProblems);
        }
        return curProblems;
    }

    public static void add(ProblemStatus status, Element element, String description) {
        Problem problem = new Problem(element, description, status);
        add(problem);
    }

    public static void add(Problem problem) {
        Problem oldProblem = null;
        boolean found = false;
        // check if the problem already exists
        for (int i = 0; i < currentProblems.get(problem.getStatus()).size() && !found; i++) {
            Problem currentProblem = currentProblems.get(problem.getStatus()).get(i);
            if (problem.getElement().equals(currentProblem.getElement())) {
                if (problem.getDescription().equals(currentProblem.getDescription())) {
                    if (currentProblem.getAction() != ProblemAction.DELETED) {
                        // problem already exists
                        oldProblem = currentProblem;
                        found = true;
                    }
                }
            }
        }
        if (!found) {
            currentProblems.put(problem.getStatus(), problem);
            problem.setAction(ProblemAction.ADDED);
        } else {
            oldProblem.setAction(ProblemAction.NONE);
        }
    }

    public static void remove(ProblemStatus status, Element element) {
        // List<Problem> problemsToRemove = new ArrayList<Problem>();

        for (Problem problem : currentProblems.get(status)) {
            if (problem.getElement().equals(element)) {
                // problemsToRemove.add(problem);
                problem.setAction(ProblemAction.DELETED);
            }
        }
        // problems.removeAll(status, problemsToRemove);
    }

    public static List<String> getStatusList(ProblemStatus status, Element element) {
        List<String> statusList = new ArrayList<String>();

        for (Problem problem : currentProblems.get(status)) {
            if (problem.getAction() != ProblemAction.DELETED) {
                if (problem.getElement().equals(element)) {
                    statusList.add(problem.getDescription());
                }
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
        currentProblems = getProblemsByProcessId(currentProcess.getId());
        if (currentProblems.size() == 0) {
            ((Process) currentProcess).checkProcess();
        }
    }

    public static void recheckCurrentProblems(ProblemsView view) {
        if (currentProcess == null || problemsMap.size() == 0) {
            return;
        }
        currentProblems = getProblemsByProcessId(currentProcess.getId());
        currentProblems.clear();
        ((Process) currentProcess).checkNodeProblems();
        view.setProblems(currentProblems);
    }

    private static MapList<ProblemStatus, Problem> getDefaultProblems() {
        defaultProblems.clear();
        return defaultProblems;
    }

    public static MapList<ProblemStatus, Problem> getCurrentProblems() {
        return currentProblems == null ? getDefaultProblems() : currentProblems;
    }

    /**
     * DOC remove problems by process id.
     */
    public static void removeProblemsByProcessId(String processId) {
        problemsMap.remove(processId);
        if (problemsMap.size() == 0) {
            currentProblems = getDefaultProblems();
            switchToCurProblemView();
        }
    }

    public static void setTitle(String title) {
        newTitle = title;
    }

    /**
     * DOC switch to the current problem view.
     */
    public static void switchToCurProblemView() {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IViewPart view = page.findView("org.talend.designer.core.ui.views.ProblemsView");
        ProblemsView problemsView = (ProblemsView) view;
        if (problemsView == null) {
            return;
        }
        if (!newTitle.equals(currentTitle)) {
            problemsView.setPartName(newTitle);
            currentTitle = newTitle;
        }
        problemsView.setProblems(getCurrentProblems());
    }

    public static void refreshView() {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IViewPart view = page.findView("org.talend.designer.core.ui.views.ProblemsView");
        ProblemsView problemsView = (ProblemsView) view;
        refreshView(problemsView);
    }

    public static void refreshView(ProblemsView problemsView) {
        if (problemsView != null) {
            if (!newTitle.equals(currentTitle)) {
                problemsView.setPartName(newTitle);
                currentTitle = newTitle;
            }
        }

        List<Problem> warningsToRemove = new ArrayList<Problem>();
        List<Problem> errorsToRemove = new ArrayList<Problem>();

        for (List<Problem> problemList : currentProblems.values()) {
            for (Problem problem : problemList) {
                switch (problem.getAction()) {
                case ADDED:
                    if (problemsView != null) {
                        problemsView.addProblem(problem.getStatus(), problem);
                        problem.setAction(ProblemAction.NONE);
                    }
                    IElement elem = problem.getElement();
                    if (elem instanceof Node) {
                        switch (problem.getStatus()) {
                        case WARNING:
                            ((Node) elem).addStatus(Process.WARNING_STATUS);
                            break;
                        case ERROR:
                            ((Node) elem).addStatus(Process.ERROR_STATUS);
                            break;
                        default:
                        }
                    }
                    break;
                case DELETED:
                    if (problemsView != null) {
                        problemsView.removeProblem(problem.getStatus(), problem);
                    }
                    switch (problem.getStatus()) {
                    case WARNING:
                        warningsToRemove.add(problem);
                        break;
                    case ERROR:
                        errorsToRemove.add(problem);
                        break;
                    default:
                    }
                    break;
                default:
                }
            }
        }
        for (Problem warning : warningsToRemove) {
            IElement elem = warning.getElement();
            if (elem instanceof Node) {
                if (getStatusList(warning.getStatus(), warning.getElement()).isEmpty()) {
                    ((Node) elem).removeStatus(Process.WARNING_STATUS);
                } else {
                    ((Node) elem).updateStatus();
                }
            }
        }
        for (Problem error : errorsToRemove) {
            IElement elem = error.getElement();
            if (elem instanceof Node) {
                if (getStatusList(error.getStatus(), error.getElement()).isEmpty()) {
                    ((Node) elem).removeStatus(Process.ERROR_STATUS);
                } else {
                    ((Node) elem).updateStatus();
                }
            }
        }
        if (problemsView != null) {
            currentProblems.removeAll(ProblemStatus.ERROR, errorsToRemove);
            currentProblems.removeAll(ProblemStatus.WARNING, warningsToRemove);
        }
    }
}
