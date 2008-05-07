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

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.compiler.BuildContext;
import org.eclipse.jdt.core.compiler.CompilationParticipant;
import org.eclipse.swt.widgets.Display;
import org.talend.commons.CommonsPlugin;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.exception.SystemException;
import org.talend.core.CorePlugin;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.designer.codegen.ITalendSynchronizer;
import org.talend.designer.core.ui.views.problems.Problems;
import org.talend.designer.runprocess.IRunProcessService;
import org.talend.repository.model.IProxyRepositoryFactory;

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

        boolean fileModified = false;
        super.processAnnotations(files);

        for (BuildContext context : files) {

            String filePath = (context.getFile().getProjectRelativePath()).toString();

            if (isRoutineFile(filePath)) {
                updateProblems(ERepositoryObjectType.ROUTINES, filePath);
            } else {
                updateProblems(ERepositoryObjectType.PROCESS, filePath);
            }

            fileModified = true;
        }

        if (fileModified) {
            Display.getDefault().asyncExec(new Runnable() {

                public void run() {
                    Problems.refreshRepositoryView();
                    Problems.refreshProblemTreeView();
                }
            });
        }

    }

    /**
     * yzhang Comment method "updateProblems".
     */
    private void updateProblems(ERepositoryObjectType type, String filePath) {

        IRunProcessService runProcessService = CorePlugin.getDefault().getRunProcessService();
        IProxyRepositoryFactory factory = CorePlugin.getDefault().getProxyRepositoryFactory();

        try {

            IProject javaProject = runProcessService.getProject(ECodeLanguage.JAVA);
            IFile file = javaProject.getFile(filePath);
            String fileName = file.getName();

            List<IRepositoryObject> routineObjectList = factory.getAll(type, false);
            for (IRepositoryObject repositoryObject : routineObjectList) {
                Property property = repositoryObject.getProperty();
                ITalendSynchronizer synchronizer = CorePlugin.getDefault().getCodeGeneratorService().createRoutineSynchronizer();
                if (fileName.equals(synchronizer.getFile(property.getItem()).getName())) {
                    Problems.addRoutineFile(synchronizer.getFile(property.getItem()), property);
                    break;
                }

            }

        } catch (PersistenceException e) {
            ExceptionHandler.process(e);
        } catch (SystemException e) {
            ExceptionHandler.process(e);
        } catch (CoreException e) {
            ExceptionHandler.process(e);
        }
    }

    private boolean isRoutineFile(String filePath) {
        int endIndex = filePath.lastIndexOf("/");
        String javaFileCatalog = filePath.substring(0, endIndex);
        if (javaFileCatalog.contains("src/routines")) {
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
        if (CommonsPlugin.isHeadless()) {
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
