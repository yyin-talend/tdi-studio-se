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
package org.talend.designer.codegen;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.exception.SystemException;
import org.talend.commons.utils.io.FilesUtils;
import org.talend.commons.utils.workbench.resources.ResourceUtils;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.general.ILibrariesService;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.RoutineItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.designer.runprocess.IRunProcessService;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.RepositoryConstants;
import org.talend.repository.model.ResourceModelUtils;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
 * 
 */
public class PerlRoutineSynchronizer implements IRoutineSynchronizer {

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
            if (!routineItem.isBuiltIn()) {
                syncRoutine(routineItem, false);
            }
        }
    }

    public IFile syncRoutine(RoutineItem routineItem, boolean copyToTemp) throws SystemException {
        try {
            IRunProcessService service = CodeGeneratorActivator.getDefault().getRunProcessService();
            Project project = ((RepositoryContext) CorePlugin.getContext().getProperty(Context.REPOSITORY_CONTEXT_KEY))
                    .getProject();

            if (!routineItem.isBuiltIn()) {
                // Copy the routine in external "lib/perl" folder:
                String librariesPath = CorePlugin.getDefault().getLibrariesService().getLibrariesPath() + IPath.SEPARATOR
                        + ILibrariesService.SOURCE_PERL_ROUTINES_FOLDER + IPath.SEPARATOR + project.getTechnicalLabel()
                        + IPath.SEPARATOR + routineItem.getProperty().getLabel() + service.getRoutineFilenameExt();
                File target = new File(librariesPath);

                FilesUtils.copyFile(new ByteArrayInputStream(routineItem.getContent().getInnerContent()), target);
            }

            // Create a temp file to return:
            IProject fsProject = ResourceModelUtils.getProject(project);
            IFolder folder = ResourceUtils.getFolder(fsProject, RepositoryConstants.TEMP_DIRECTORY, true);
            IFile tempfile = ResourceUtils.getFile(folder, "tempRoutine" + routineItem.getProperty().getId(), false); //$NON-NLS-1$

            if (copyToTemp) {
                routineItem.getContent().setInnerContentToFile(tempfile.getLocation().toFile());
            }
            if (!tempfile.exists()) {
                tempfile.refreshLocal(1, null);
            }
            return tempfile;

        } catch (CoreException e) {
            throw new SystemException(e);
        } catch (IOException e) {
            throw new SystemException(e);
        }
    }

}
