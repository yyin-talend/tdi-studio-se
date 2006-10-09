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
import org.talend.core.model.process.Element;
import org.talend.core.model.process.IElement;
import org.talend.core.model.process.Problem;
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
        problems.clear();
    }

    public static void add(ProblemStatus status, Element element, String description) {
        Problem problem = new Problem(element, description, status);
        add(problem);
    }

    public static void add(Problem problem) {
        problems.put(problem.getStatus(), problem);
    }

    public static void remove(ProblemStatus status, Element element) {
        List<Problem> problemsToRemove = new ArrayList<Problem>();

        for (Problem problem : problems.get(status)) {
            if (problem.getElement().equals(element)) {
                problemsToRemove.add(problem);
            }
        }
        problems.removeAll(status, problemsToRemove);
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

        List<Problem> currentProblemList = problemsView.getProblemList(ProblemStatus.WARNING);
        for (Problem problem : currentProblemList) {
            // clear all old warning status for nodes
            IElement elem = problem.getElement();
            if (elem instanceof Node) {
                ((Node) elem).removeStatus(Process.WARNING_STATUS);
            }

            // if the new list doesn't contain this warning, then remove it
            boolean contain = false;
            for (Problem newWarning : problems.get(ProblemStatus.WARNING)) {
                if (problem.getElement().equals(newWarning.getElement())
                        && problem.getDescription().equals(newWarning.getDescription())) {
                    contain = true;
                }
            }
            if (!contain) {
                problemsView.removeProblem(ProblemStatus.WARNING, problem);
            }
        }
        for (Problem problem : problems.get(ProblemStatus.WARNING)) {
            // if the current list doesn't contain this warning, then add it
            boolean contain = false;
            for (Problem currentWarning : currentProblemList) {
                if (problem.getElement().equals(currentWarning.getElement())
                        && problem.getDescription().equals(currentWarning.getDescription())) {
                    contain = true;
                }
            }
            if (!contain) {
                problemsView.addProblem(ProblemStatus.WARNING, problem);
            }

            // add all warning status on nodes
            IElement elem = problem.getElement();
            if (elem instanceof Node) {
                ((Node) elem).addStatus(Process.WARNING_STATUS);
            }
        }

        currentProblemList = problemsView.getProblemList(ProblemStatus.ERROR);
        for (Problem problem : currentProblemList) {
            // clear all old error status for nodes
            IElement elem = problem.getElement();
            if (elem instanceof Node) {
                ((Node) elem).removeStatus(Process.ERROR_STATUS);
            }

            // if the new list doesn't contain this error, then remove it
            boolean contain = false;
            for (Problem newError : problems.get(ProblemStatus.ERROR)) {
                if (problem.getElement().equals(newError.getElement())
                        && problem.getDescription().equals(newError.getDescription())) {
                    contain = true;
                }
            }
            if (!contain) {
                problemsView.removeProblem(ProblemStatus.ERROR, problem);
            }
        }
        for (Problem problem : problems.get(ProblemStatus.ERROR)) {
            // if the current list doesn't contain this error, then add it
            boolean contain = false;
            for (Problem currentError : currentProblemList) {
                if (problem.getElement().equals(currentError.getElement())
                        && problem.getDescription().equals(currentError.getDescription())) {
                    contain = true;
                }
            }
            if (!contain) {
                problemsView.addProblem(ProblemStatus.ERROR, problem);
            }

            // add old error status on nodes
            IElement elem = problem.getElement();
            if (elem instanceof Node) {
                ((Node) elem).addStatus(Process.ERROR_STATUS);
            }
        }
    }
}
