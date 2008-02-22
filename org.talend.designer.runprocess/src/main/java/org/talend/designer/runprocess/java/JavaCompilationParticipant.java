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

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.compiler.BuildContext;
import org.eclipse.jdt.core.compiler.CompilationParticipant;
import org.eclipse.swt.widgets.Display;
import org.talend.core.CorePlugin;
import org.talend.designer.core.ui.views.problems.Problems;

/**
 * DOC xhuang class global comment. Detailled comment <br/>
 * 
 */
public class JavaCompilationParticipant extends CompilationParticipant {

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
        boolean recordRoutineFileModified = false;
        super.processAnnotations(files);
        for (BuildContext context : files) {
            String filePath = (context.getFile().getProjectRelativePath()).toString();
            if (!isRoutineFile(filePath)) {
                continue;
            }
            Problems.addRoutineFile(context.getFile(), null);
            recordRoutineFileModified =true;
        }
        if (recordRoutineFileModified) {
            Display.getDefault().asyncExec(new Runnable() {

                public void run() {

                    Problems.refreshRepositoryView();
                    Problems.refreshProblemTreeView();
                }
            });
        }
    }

    private boolean isRoutineFile(String filePath) {
        int endIndex = filePath.lastIndexOf("/");
        String javaFileCatalog = filePath.substring(0, endIndex);
        if (javaFileCatalog.equals("src/routines")) {
            return true;
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jdt.core.compiler.CompilationParticipant#isActive(org.eclipse.jdt.core.IJavaProject)
     */
    @Override
    public boolean isActive(IJavaProject project) {
        if (CorePlugin.getContext().isHeadless()) {
            return false;
        }
        if (JavaProcessor.getJavaProject() != null) {
            return JavaProcessor.getJavaProject().equals(project);
        }
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
