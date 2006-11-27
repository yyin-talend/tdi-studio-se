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
import java.util.List;

import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.utils.data.container.MapList;
import org.talend.commons.utils.time.TimeMeasure;
import org.talend.core.model.process.Element;
import org.talend.core.model.process.IElement;
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

    private static MapList<ProblemStatus, Problem> problems = new MapList<ProblemStatus, Problem>();

    private static String currentTitle = "";

    private static String newTitle = "";

    public static void clearAll() {
        for (List<Problem> problemList : problems.values()) {
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

    public static void add(ProblemStatus status, Element element, String description) {
        Problem problem = new Problem(element, description, status);
        add(problem);
    }

    public static void add(Problem problem) {
        Problem oldProblem = null;
        boolean found = false;
        // check if the problem already exists
        for (int i = 0; i < problems.get(problem.getStatus()).size() && !found; i++) {
            Problem currentProblem = problems.get(problem.getStatus()).get(i);
            if (problem.getElement().equals(currentProblem.getElement())) {
                if (problem.getDescription().equals(currentProblem.getDescription())) {
                    // problem already exists
                    oldProblem = currentProblem;
                    found = true;
                }
            }
        }
        if (!found) {
            problems.put(problem.getStatus(), problem);
            problem.setAction(ProblemAction.ADDED);
        } else {
            oldProblem.setAction(ProblemAction.NONE);
        }
    }

    public static void remove(ProblemStatus status, Element element) {
        // List<Problem> problemsToRemove = new ArrayList<Problem>();

        for (Problem problem : problems.get(status)) {
            if (problem.getElement().equals(element)) {
                // problemsToRemove.add(problem);
                problem.setAction(ProblemAction.DELETED);
            }
        }
        // problems.removeAll(status, problemsToRemove);
    }

    public static List<String> getStatusList(ProblemStatus status, Element element) {
        List<String> statusList = new ArrayList<String>();

        for (Problem problem : problems.get(status)) {
            if (problem.getElement().equals(element)) {
                statusList.add(problem.getDescription());
            }
        }
        return statusList;
    }

    public static void setTitle(String title) {
        newTitle = title;
    }

    public static void refreshView() {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IViewPart view = page.findView("org.talend.designer.core.ui.views.ProblemsView");
        ProblemsView problemsView = (ProblemsView) view;
        refreshView(problemsView);
    }

    public static void refreshView(ProblemsView problemsView) {
        if (problemsView == null) {
            return;
        }

        if (!newTitle.equals(currentTitle)) {
            problemsView.setPartName(newTitle);
            currentTitle = newTitle;
        }

        List<Problem> warningsToRemove = new ArrayList<Problem>();
        List<Problem> errorsToRemove = new ArrayList<Problem>();

        for (List<Problem> problemList : problems.values()) {
            for (Problem problem : problemList) {
                switch (problem.getAction()) {
                case DELETED:
                    IElement elem = problem.getElement();
                    if (elem instanceof Node) {
                        switch (problem.getStatus()) {
                        case WARNING:
                            ((Node) elem).removeStatus(Process.WARNING_STATUS);
                            break;
                        case ERROR:
                            ((Node) elem).removeStatus(Process.ERROR_STATUS);
                            break;
                        default:
                        }
                    }
                    break;
                default:
                }
            }
        }

        for (List<Problem> problemList : problems.values()) {
            for (Problem problem : problemList) {
                switch (problem.getAction()) {
                case ADDED:
                    problemsView.addProblem(problem.getStatus(), problem);
                    problem.setAction(ProblemAction.NONE);
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
                    problemsView.removeProblem(problem.getStatus(), problem);
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
        problems.removeAll(ProblemStatus.ERROR, errorsToRemove);
        problems.removeAll(ProblemStatus.WARNING, warningsToRemove);
    }
}
