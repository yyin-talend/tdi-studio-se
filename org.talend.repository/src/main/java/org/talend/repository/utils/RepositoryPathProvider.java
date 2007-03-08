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
package org.talend.repository.utils;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.workbench.resources.ResourceUtils;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.general.Project;
import org.talend.repository.model.ResourceModelUtils;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class RepositoryPathProvider {

    public static IProject getProject() throws PersistenceException {
        RepositoryContext repositoryContext = (RepositoryContext) CorePlugin.getContext().getProperty(
                Context.REPOSITORY_CONTEXT_KEY);
        Project project = repositoryContext.getProject();
        return ResourceModelUtils.getProject(project);
    }

    public static IPath getPathRootProject() {

        try {
            IProject iProject = getProject();
            return iProject.getFullPath();
        } catch (PersistenceException e) {
            return null;
        }
    }

    public static IPath getPathProjectFolder(String folderName) {
        try {
            IProject iProject = getProject();
            IFolder folder = ResourceUtils.getFolder(iProject, folderName, true);
            return folder.getLocation();
        } catch (PersistenceException e) {
            return null;
        }

    }

    public static IFolder getFolder(String folderName) {
        try {
            IProject iProject = getProject();
            IFolder folder = ResourceUtils.getFolder(iProject, folderName, true);
            return folder;
        } catch (PersistenceException e) {
            return null;
        }
    }

    public static IPath getPathFileName(String folderName, String fileName) {
        IPath pathProjectFolder = getPathProjectFolder(folderName);
        return pathProjectFolder.append(fileName);
    }

}
