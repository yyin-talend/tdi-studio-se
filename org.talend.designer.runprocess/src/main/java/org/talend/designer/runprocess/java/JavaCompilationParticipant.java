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
package org.talend.designer.runprocess.java;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.compiler.BuildContext;
import org.eclipse.jdt.core.compiler.CompilationParticipant;
import org.eclipse.swt.widgets.Display;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.model.process.Problem.ProblemStatus;
import org.talend.designer.core.ui.views.problems.Problems;
import org.talend.designer.runprocess.i18n.Messages;

/**
 * DOC xhuang class global comment. Detailled comment <br/>
 * 
 */
public class JavaCompilationParticipant extends CompilationParticipant {

    private org.eclipse.jdt.core.ICompilationUnit unit;

    /**
     * DOC xhuang CompilationParticipant constructor comment.
     */
    public JavaCompilationParticipant() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jdt.core.compiler.CompilationParticipant#processAnnotations(org.eclipse.jdt.core.compiler.BuildContext[])
     */
    @Override
    public void processAnnotations(BuildContext[] files) {
        super.processAnnotations(files);
        for (BuildContext context : files) {
            String filePath = (context.getFile().getProjectRelativePath()).toString();
            if (!isRoutineFile(filePath)) {
                continue;
            }
            addErrorProblem(context);
        }
    }

    private void addErrorProblem(BuildContext context) {
        try {
            IMarker[] markers = context.getFile().findMarkers(IMarker.PROBLEM, true, IResource.DEPTH_ONE);
            String javaEditorName = context.getFile().getName();
            Problems.clearAllComliationError(javaEditorName);
            for (IMarker marker : markers) {
                Integer lineNr = (Integer) marker.getAttribute(IMarker.LINE_NUMBER);
                String message = (String) marker.getAttribute(IMarker.MESSAGE);
                Integer severity = (Integer) marker.getAttribute(IMarker.SEVERITY);
                if (severity == IMarker.SEVERITY_ERROR) {
                    Problems.add(ProblemStatus.ERROR, marker, javaEditorName, message, lineNr);

                } else if (severity == IMarker.SEVERITY_WARNING) {
                    Problems.add(ProblemStatus.WARNING, marker, javaEditorName, message, lineNr);

                } else if (severity == IMarker.SEVERITY_INFO) {
                    Problems.add(ProblemStatus.INFO, marker, javaEditorName, message, lineNr);
                }
            }

            Display.getDefault().asyncExec(new Runnable() {

                public void run() {
                    Problems.refreshRoutinErrorProblemView();
                }
            });

        } catch (CoreException e) {
            ExceptionHandler.process(e);
        }
    }

    private boolean isRoutineFile(String filePath) {
        int endIndex = filePath.lastIndexOf("/");
        String javaFileCatalog = filePath.substring(0, endIndex);
        if (javaFileCatalog.equals(Messages.getString("Routine.javaFilePath")))
            return true;
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jdt.core.compiler.CompilationParticipant#isActive(org.eclipse.jdt.core.IJavaProject)
     */
    @Override
    public boolean isActive(IJavaProject project) {
        if (JavaProcessor.getJavaProject() != null)
            return JavaProcessor.getJavaProject().equals(project);
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jdt.core.compiler.CompilationParticipant#isAnnotationProcessor()
     */
    @Override
    public boolean isAnnotationProcessor() {
        return true;
    }

}