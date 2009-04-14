// ============================================================================
//
// Copyright (C) 2006-2009 Talend Inc. - www.talend.com
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
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.markers.internal.MarkerMessages;
import org.epic.perleditor.editors.util.TalendPerlValidator;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.process.Element;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.process.Problem;
import org.talend.core.model.process.TalendProblem;
import org.talend.core.model.process.Problem.ProblemStatus;
import org.talend.core.model.process.Problem.ProblemType;
import org.talend.core.model.properties.Information;
import org.talend.core.model.properties.InformationLevel;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.PropertiesFactory;
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

    public static List<Node> nodeList = new ArrayList<Node>();

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

    public static ProblemList getProblemList() {
        return problemList;
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
            String uniName, Integer charStart, Integer charEnd, ProblemType type) {
        Problem problem = new TalendProblem(status, javaUnitName, marker, markerErrorMessage, lineN, uniName, charStart, charEnd,
                type);
        add(problem);
        // addErrorMark();
    }

    public static void addAll(List<Problem> problems) {
        for (Problem current : problems) {
            add(current);
        }
    }

    public static void add(Problem pro) {
        problemList.add(pro);
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
        boolean hasInfo = false;

        for (Problem problem : problemList) {
            if (problem.getElement() == null) {
                continue;
            } else if (problem.getElement() != null && (!problem.getElement().equals(node))) {
                continue;
            }
            if (problem.getStatus().equals(ProblemStatus.INFO)) {
                hasInfo = true;
                node.addStatus(Process.INFO_STATUS);
            } else if (problem.getStatus().equals(ProblemStatus.WARNING)) {
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
        if (!hasInfo) {
            node.removeStatus(Process.INFO_STATUS);
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
    public static List<Information> addRoutineFile(IFile file, final Property property) {
        if (file == null || !file.exists()) {
            return null;
        }

        String routineFileName = null;
        String uniName = null;
        if (property == null) {
            routineFileName = getFileName(file);
        } else {
            routineFileName = property.getLabel();
        }

        List<Information> informations = new ArrayList<Information>();
        try {
            IMarker[] markers = file.findMarkers(IMarker.PROBLEM, true, IResource.DEPTH_ONE);

            Problems.clearAllComliationError(routineFileName);
            for (IMarker marker : markers) {
                Integer lineNr = (Integer) marker.getAttribute(IMarker.LINE_NUMBER);
                String message = (String) marker.getAttribute(IMarker.MESSAGE);
                Integer severity = (Integer) marker.getAttribute(IMarker.SEVERITY);
                Integer start = (Integer) marker.getAttribute(IMarker.CHAR_START);
                Integer end = (Integer) marker.getAttribute(IMarker.CHAR_END);

                Information information = PropertiesFactory.eINSTANCE.createInformation();
                information.setText(message);
                informations.add(information);

                ProblemStatus status = null;
                switch (severity) {
                case IMarker.SEVERITY_ERROR:
                    status = ProblemStatus.ERROR;
                    information.setLevel(InformationLevel.ERROR_LITERAL);
                    String path = file.getLocation().toString();
                    uniName = getNodeUniName(path, lineNr);
                    break;
                // case IMarker.SEVERITY_WARNING:
                // status = ProblemStatus.WARNING;
                // information.setLevel(InformationLevel.WARN_LITERAL);
                // break;
                case IMarker.SEVERITY_INFO:
                    status = ProblemStatus.INFO;
                    information.setLevel(InformationLevel.INFO_LITERAL);
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
                    if ("".equals(uniName) || uniName == null) {
                        uniName = "uniName";//$NON-NLS-1$
                    }
                    add(status, marker, routineFileName, message, lineNr, uniName, start, end, type);
                }
            }
            if (!(LanguageManager.getCurrentLanguage().equals(ECodeLanguage.PERL))) {
                addErrorMark();
            }

        } catch (org.eclipse.core.runtime.CoreException e) {
            ExceptionHandler.process(e);
        }

        if (property == null) {
            return null;
        }

        return informations;
    }

    /**
     * See also AbstractEMFRepositoryFactory.computePropertyMaxInformationLevel
     * 
     * @param property
     */
    public static void computePropertyMaxInformationLevel(Property property) {
        EList<Information> informations = property.getInformations();
        InformationLevel maxLevel = null;
        for (int i = 0; i < informations.size(); i++) {
            Information information = informations.get(i);
            if (i == 0) {
                maxLevel = information.getLevel();
                continue;
            }
            int value = information.getLevel().getValue();
            if (maxLevel == null || value > maxLevel.getValue()) {
                maxLevel = information.getLevel();
            }
        }
        if (maxLevel != null) {
            property.setMaxInformationLevel(maxLevel);
        } else {
            property.setMaxInformationLevel(InformationLevel.DEBUG_LITERAL);
        }
    }

    private static String getFileName(IFile file) {
        if (file == null) {
            return ""; //$NON-NLS-1$
        }
        String fileName = file.getName();
        String ext = file.getFileExtension();
        if (ext == null || "".equals(ext)) { //$NON-NLS-1$
            return fileName;
        }
        fileName = fileName.substring(0, fileName.lastIndexOf(ext) - 1);
        return fileName;
    }

    private static String getNodeUniName(String path, int lineNum) {
        String uniName = null;
        String[][] s = TalendPerlValidator.instance().matchString(path, lineNum);
        int first = 0;
        int second = 0;
        if (s != null) {
            if (s[0][0] != null) {
                first = Integer.parseInt(s[0][0]);
            }
            if (s[1][0] != null) {
                second = Integer.parseInt(s[1][0]);

            }
        }

        if (lineNum > first && lineNum < second && s != null) {
            int index1 = s[1][1].indexOf("[");//$NON-NLS-1$
            int index2 = s[1][1].indexOf("]");//$NON-NLS-1$
            if (index1 > 0 && index2 > index1) {
                String nodeAllName = s[1][1].substring(index1, index2);
                int index3 = nodeAllName.indexOf(" ");//$NON-NLS-1$
                if (index3 > 0) {
                    uniName = nodeAllName.substring(1, index3);
                }

            }

        }
        return uniName;
    }

    public static void addErrorMark() {
        nodeList.clear();
        for (int i = 0; i < problemList.getProblemList().size(); i++) {
            Problem problem = problemList.getProblemList().get(i);
            for (IProcess process : openJobs) {
                if (((Process) process).isActivate()) {
                    for (INode inode : ((Process) process).getGraphicalNodes()) {
                        if (inode instanceof Node) {
                            Node node = (Node) inode;
                            if (node.isActivate()) {
                                if (problem.getStatus().equals(ProblemStatus.ERROR)) {
                                    if (problem instanceof TalendProblem) {
                                        TalendProblem tProblem = (TalendProblem) problem;
                                        if (tProblem.getUnitName().equals(node.getUniqueName())) {
                                            nodeList.add(node);
                                        } else {
                                            node.setErrorFlag(false);
                                            node.setErrorInfo(null);
                                            node.getNodeError().updateState("UPDATE_STATUS", false);//$NON-NLS-1$
                                            node.setErrorInfoChange("ERRORINFO", false);//$NON-NLS-1$
                                        }

                                    }
                                } else {
                                    node.setErrorFlag(false);
                                    node.setErrorInfoChange("ERRORINFO", false);//$NON-NLS-1$
                                }
                            }
                        }
                    }
                }
            }
        }
        for (Node node : nodeList) {
            node.setErrorFlag(true);
            node.setErrorInfo(null);
            node.getNodeError().updateState("UPDATE_STATUS", false);//$NON-NLS-1$
            node.setErrorInfoChange("ERRORINFO", true);//$NON-NLS-1$
        }
    }

}
