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
package org.talend.designer.codegen;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.exception.SystemException;
import org.talend.commons.utils.generation.JavaUtils;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.RoutineItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.designer.codegen.javamodule.JavaModuleService;
import org.talend.designer.runprocess.IRunProcessService;
import org.talend.repository.model.IProxyRepositoryFactory;

/**
 * Routine synchronizer of java project.
 * 
 * yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: JavaRoutineSynchronizer.java JavaRoutineSynchronizer 2007-2-2 下午03:29:12 +0000 (下午03:29:12, 2007-2-2 2007)
 * yzhang $
 * 
 */
public class JavaRoutineSynchronizer implements IRoutineSynchronizer {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.codegen.IRoutineSynchronizer#syncAllRoutines()
     */
    public void syncAllRoutines() throws SystemException {
        IProxyRepositoryFactory repositoryFactory = CodeGeneratorActivator.getDefault().getRepositoryService()
                .getProxyRepositoryFactory();

        List<IRepositoryObject> routines;
        try {
            routines = repositoryFactory.getAll(ERepositoryObjectType.ROUTINES);
        } catch (PersistenceException e) {
            throw new SystemException(e);
        }

        for (IRepositoryObject routine : routines) {
            RoutineItem routineItem = (RoutineItem) routine.getProperty().getItem();
            syncRoutine(routineItem);
        }

        // PTODO MHIRT Vérifier avec MHELLEBOID ou SMALLET si on ne pourrait pas faire mieux
        try {
            JavaModuleService jms = new JavaModuleService();
            List<URL> systemModuleURL = jms.getModule();

            for (URL url : systemModuleURL) {
                String fileName = url.getPath();
                if (fileName.startsWith("/")) {
                    fileName = fileName.substring(1);
                }
                File f = new File(url.getPath());
                if (f.isDirectory()) {
                    syncModule(f.listFiles());
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.codegen.IRoutineSynchronizer#syncRoutine(org.talend.core.model.properties.RoutineItem)
     */
    public IFile syncRoutine(RoutineItem routineItem) throws SystemException {
        try {
            IRunProcessService service = CodeGeneratorActivator.getDefault().getRunProcessService();
            IProject javaProject = service.getProject(ECodeLanguage.JAVA);
            Project project = ((RepositoryContext) CorePlugin.getContext().getProperty(Context.REPOSITORY_CONTEXT_KEY))
                    .getProject();
            initRoutineFolder(javaProject, project);
            IFile file = javaProject.getFile(JavaUtils.JAVA_SRC_DIRECTORY + "/"
                    + JavaUtils.JAVA_ROUTINES_DIRECTORY + "/"
                    + routineItem.getProperty().getLabel() + JavaUtils.JAVA_EXTENSION);

            routineItem.getContent().setInnerContentToFile(file.getLocation().toFile());
            if (!file.exists()) {
                file.refreshLocal(1, null);
            }
            return file;
        } catch (CoreException e) {
            throw new SystemException(e);
        } catch (IOException e) {
            throw new SystemException(e);
        }
    }

    /**
     * DOC mhirt Comment method "initRoutineFolder".
     * 
     * @param javaProject
     * @param project
     * @throws CoreException
     */
    private void initRoutineFolder(IProject javaProject, Project project) throws CoreException {
        IFolder rep = javaProject.getFolder(JavaUtils.JAVA_SRC_DIRECTORY + "/" + JavaUtils.JAVA_ROUTINES_DIRECTORY);
        if (!rep.exists()) {
            rep.create(true, true, null);
        }
    }

    private void initModuleFolder(IProject javaProject, Project project) throws CoreException {
        IFolder rep = javaProject.getFolder(JavaUtils.JAVA_SRC_DIRECTORY + "/" + JavaUtils.JAVA_ROUTINES_DIRECTORY + "/"
                + JavaUtils.JAVA_SYSTEM_ROUTINES_DIRECTORY);
        if (!rep.exists()) {
            rep.create(true, true, null);
        }
    }

    public void copyFile(File in, IFile out) throws Exception {
        if (out.exists()) {
            out.delete(true, null);
        }
        FileInputStream fis = new FileInputStream(in);
        out.create(fis, false, null);
        fis.close();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.codegen.IRoutineSynchronizer#syncRoutine(org.talend.core.model.properties.RoutineItem)
     */
    public IFile syncModule(File[] modules) throws SystemException {
        try {
            IRunProcessService service = CodeGeneratorActivator.getDefault().getRunProcessService();
            IProject javaProject = service.getProject(ECodeLanguage.JAVA);
            Project project = ((RepositoryContext) CorePlugin.getContext().getProperty(Context.REPOSITORY_CONTEXT_KEY))
                    .getProject();
            initModuleFolder(javaProject, project);

            for (File module : modules) {
                if (!module.isDirectory()) {
                    IFile file = javaProject.getFile(JavaUtils.JAVA_SRC_DIRECTORY + "/"
                            + JavaUtils.JAVA_ROUTINES_DIRECTORY + "/" + JavaUtils.JAVA_SYSTEM_ROUTINES_DIRECTORY + "/"
                            + module.getName());

                    copyFile(module, file);
                }
            }
        } catch (CoreException e) {
            throw new SystemException(e);
        } catch (FileNotFoundException e) {
            throw new SystemException(e);
        } catch (IOException e) {
            throw new SystemException(e);
        } catch (Exception e) {
            throw new SystemException(e);
        }
        return null;
    }
}
