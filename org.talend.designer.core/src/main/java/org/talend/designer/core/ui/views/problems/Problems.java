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
package org.talend.designer.core.ui.views.problems;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.markers.internal.MarkerMessages;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.model.process.Element;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.process.Problem;
import org.talend.core.model.process.TalendProblem;
import org.talend.core.model.process.Problem.ProblemStatus;
import org.talend.core.model.process.Problem.ProblemType;
import org.talend.core.model.properties.InformationLevel;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.RoutineItem;
import org.talend.designer.core.DesignerPlugin;
import org.talend.designer.core.i18n.Messages;
import org.talend.designer.core.ui.editor.nodes.Node;
import org.talend.designer.core.ui.editor.process.Process;
import org.talend.repository.ui.views.IRepositoryView;

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

    private static ProblemsView problemView;

    private static List<IProcess> openJobs = new ArrayList<IProcess>();

    private static String currentTitle = ""; //$NON-NLS-1$

    private static Group groupField = null;

    public static void setGroupField(Group group) {
        groupField = group;
    }

    static ProblemsView getProblemView() {
        if (problemView == null) {
            problemView = ProblemsView.show();
        }
        return problemView;
    }

    public static Group getGroupField() {
        if (groupField == null) {
            IPreferenceStore store = DesignerPlugin.getDefault().getPreferenceStore();
            String key = store.getString(ProblemsView.PROBLEM_TYPE_SELECTION);
            if (key == null || key.length() == 0) {
                groupField = Group.SEVERITY;
            } else {
                groupField = Group.valueOf(key);
            }
        }

        return groupField;
    }

    public static ProblemCategory[] categories = null;

    private static ProblemList problemList = new ProblemList();

    public static void clearAll() {
        problemList.clear();
    }

    public static void clearAllComliationError(String javaEditorName) {
        for (Iterator<Problem> iter = problemList.getProblemList().iterator(); iter.hasNext();) {
            Problem problem = iter.next();
            if (problem instanceof TalendProblem) {
                TalendProblem routineProblem = (TalendProblem) problem;
                if (routineProblem.getJavaUnitName() != null && (routineProblem.getJavaUnitName().equals(javaEditorName))) {
                    iter.remove();
                }

            }
        }

    }

    public static String getSummary() {
        int[] counts = problemList.getMarkerCounts();
        return MessageFormat.format(MarkerMessages.problem_statusSummaryBreakdown, new Object[] { new Integer(counts[0]),
                new Integer(counts[1]), new Integer(counts[2]) });
    }

    private static void buildHierarchy() {
        Group group = getGroupField();
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
        buildHierarchy();
        if (categories != null) {
            return Arrays.asList(categories);
        } else {
            return problemList.getProblemList();
        }
    }

    public static void clearAll(Element element) {
        removeProblemsByElement(element);
    }

    public static void add(ProblemStatus status, Element element, String description) {
        Problem problem = new Problem(element, description, status);
        add(problem);
    }

    public static void add(ProblemStatus status, IMarker marker, String javaUnitName, String markerErrorMessage, Integer lineN,
            Integer charStart, Integer charEnd, ProblemType type) {
        Problem problem = new TalendProblem(status, javaUnitName, marker, markerErrorMessage, lineN, charStart, charEnd, type);
        add(problem);
    }

    public static void addAll(List<Problem> problems) {
        for (Problem current : problems) {
            add(current);
        }
    }

    public static void add(Problem problem) {
        problemList.add(problem);
    }

    public static List<String> getStatusList(ProblemStatus status, Element element) {
        List<String> statusList = new ArrayList<String>();

        for (Problem problem : problemList.getProblemList()) {
            if (problem.getElement() != null && problem.getElement().equals(element) && problem.getStatus().equals(status)) {
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
    public static void addProcess(IProcess process) {
        if (openJobs.contains(process)) {
            return;
        }
        openJobs.add(process);
        initCurrentProblems(process);
    }

    /**
     * DOC check the problems the corresponding of current process .
     */
    private static void initCurrentProblems(IProcess process) {
        // for (IProcess process : openJobs) {
        ((Process) process).checkProcess();
        // }

    }

    public static void recheckCurrentProblems() {
        for (IProcess process : openJobs) {
            ((Process) process).checkNodeProblems();
        }
        getProblemView().refresh();
    }

    public static void refreshProcessAllNodesStatus(IProcess process) {
        List<? extends INode> processNodes = process.getGraphicalNodes();
        for (INode inode : processNodes) {
            if (inode instanceof Node) {
                Node node = (Node) inode;
                refreshNodeStatus(node, problemList.getProblemList());
            }
        }
    }

    public static void refreshOneNodeStatus(INode iNode) {
        if (iNode instanceof Node) {
            Node node = (Node) iNode;
            refreshNodeStatus(node, problemList.getProblemList());
        }
    }

    /*
     * DOC xhuang refresh the structure of problems view
     */
    public static void refreshProblemTreeView() {
        if (getProblemView() != null) {
            Display.getDefault().syncExec(new Runnable() {

                /*
                 * (non-Javadoc)
                 * 
                 * @see java.lang.Runnable#run()
                 */
                public void run() {
                    getProblemView().refresh();
                }
            });
        }
    }

    public static void refreshEditorTitleImage() {
    }

    /**
     * DOC xtan Comment method "refreshRepositoryView".
     */
    public static void refreshRepositoryView() {
        Display.getDefault().syncExec(new Runnable() {

            /*
             * (non-Javadoc)
             * 
             * @see java.lang.Runnable#run()
             */
            public void run() {
                IRepositoryView viewPart = (IRepositoryView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                        .findView(IRepositoryView.VIEW_ID);
                viewPart.refresh();
            }
        });
    }

    /**
     * DOC bqian Comment method "refreshNodeStatus".
     * 
     * @param node
     * @param problemList2
     */
    private static void refreshNodeStatus(Node node, List<Problem> problemList) {

        boolean hasWarning = false;
        boolean hasError = false;

        for (Problem problem : problemList) {
            if (problem.getElement() == null) {
                continue;
            } else if (problem.getElement() != null && (!problem.getElement().equals(node))) {
                continue;
            }
            if (problem.getStatus().equals(ProblemStatus.WARNING)) {
                hasWarning = true;
                node.addStatus(Process.WARNING_STATUS);
            } else if (problem.getStatus().equals(ProblemStatus.ERROR)) {
                hasError = true;
                node.addStatus(Process.ERROR_STATUS);
            }
        }

        if (!hasWarning) {
            node.removeStatus(Process.WARNING_STATUS);
        }
        if (!hasError) {
            node.removeStatus(Process.ERROR_STATUS);
        }

        node.updateStatus();
    }

    /**
     * DOC bqian Comment method "removeProblemsByProcessId".
     * 
     * @param id
     */
    public static void removeProblemsByProcess(IProcess process) {

        for (Iterator<Problem> iter = problemList.getProblemList().iterator(); iter.hasNext();) {
            Problem problem = iter.next();
            if (problem.getJob() != null && (problem.getJob().equals(process))) {
                iter.remove();
            }

        }
        // refreshProcessAllNodesStatus(process);
        refreshProblemTreeView();
    }

    public static void removeJob(IProcess process) {
        openJobs.remove(process);
    }

    public static void removeProblemsByElement(Element element) {

        for (Iterator<Problem> iter = problemList.getProblemList().iterator(); iter.hasNext();) {
            Problem problem = iter.next();
            if (problem.getElement() != null && (problem.getElement().equals(element))) {
                iter.remove();
            }
        }
        refreshProblemTreeView();
    }

    public static void removeProblemsByRoutine(String routineName) {
        for (Iterator<Problem> iter = problemList.getProblemList().iterator(); iter.hasNext();) {
            Problem problem = iter.next();
            if (problem.getType().equals(ProblemType.ROUTINE)) {
                TalendProblem rp = (TalendProblem) problem;
                if (rp.getJavaUnitName().equals(routineName)) {
                    iter.remove();
                }
            }
        }
        refreshProblemTreeView();
    }

    /**
     * Sets the problemView.
     * 
     * @param problemView the problemView to set
     */
    public static void setProblemView(ProblemsView problemView) {
        Problems.problemView = problemView;
    }

    /**
     * 
     * ggu Comment method "addRoutineFile".
     * 
     * 
     */
    public static void addRoutineFile(IFile file, final Property property) {
        if (file == null || !file.exists()) {
            return;
        }

        boolean hasError = false;

        String routineFileName = null;
        if (property == null) {
            routineFileName = getFileName(file);
        } else {
            routineFileName = property.getLabel();
        }

        try {
            IMarker[] markers = file.findMarkers(IMarker.PROBLEM, true, IResource.DEPTH_ONE);

            Problems.clearAllComliationError(routineFileName);
            for (IMarker marker : markers) {
                Integer lineNr = (Integer) marker.getAttribute(IMarker.LINE_NUMBER);
                String message = (String) marker.getAttribute(IMarker.MESSAGE);
                Integer severity = (Integer) marker.getAttribute(IMarker.SEVERITY);
                Integer start = (Integer) marker.getAttribute(IMarker.CHAR_START);
                Integer end = (Integer) marker.getAttribute(IMarker.CHAR_END);

                ProblemStatus status = null;
                switch (severity) {
                case IMarker.SEVERITY_ERROR:
                    status = ProblemStatus.ERROR;
                    hasError = true;
                    break;
                case IMarker.SEVERITY_WARNING:
                    status = ProblemStatus.WARNING;
                    break;
                case IMarker.SEVERITY_INFO:
                    status = ProblemStatus.INFO;
                    break;
                default:
                    break;
                }

                if (status != null) {
                    ProblemType type = ProblemType.NONE;
                    if (property.getItem() instanceof RoutineItem) {
                        type = ProblemType.ROUTINE;
                    } else if (property.getItem() instanceof ProcessItem) {
                        type = ProblemType.JOB;
                        if (status != ProblemStatus.ERROR) {
                            continue;
                        }
                    }
                    add(status, marker, routineFileName, message, lineNr, start, end, type);
                }

            }

        } catch (org.eclipse.core.runtime.CoreException e) {
            ExceptionHandler.process(e);
        }

        if (property == null) {
            return;
        }

        if (hasError) {
            property.setMaxInformationLevel(InformationLevel.ERROR_LITERAL);
        } else {
            property.setMaxInformationLevel(InformationLevel.DEBUG_LITERAL);
        }
    }

    private static String getFileName(IFile file) {
        if (file == null) {
            return "";
        }
        String fileName = file.getName();
        String ext = file.getFileExtension();
        if (ext == null || "".equals(ext)) {
            return fileName;
        }
        fileName = fileName.substring(0, fileName.lastIndexOf(ext) - 1);
        return fileName;
    }

}
