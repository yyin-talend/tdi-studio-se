// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2007 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License.
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
package org.talend.repository.ui.utils;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.model.general.Project;
import org.talend.repository.RepositoryPlugin;

/**
 * An util tools for java version resources. Detailled comment <br/>
 * 
 */
/**
 * bqian An util tools for java version resources. <br/>
 * 
 */
public class JavaResourcesHelper {

    /**
     * Gets the specific resource in the java project.
     * 
     * @param name
     * @param projectName
     * 
     * @return
     */
    public static IResource getSpecificResourceInJavaProject(IPath path) throws CoreException {
        IProject project = RepositoryPlugin.getDefault().getRunProcessService().getProject(ECodeLanguage.JAVA);
        IResource resource = project.findMember(path);
        return resource;
    }

    /**
     * Gets current project name.
     * 
     * @return
     */
    public static String getCurrentProjectName() {
        RepositoryContext repositoryContext = (RepositoryContext) CorePlugin.getContext().getProperty(
                Context.REPOSITORY_CONTEXT_KEY);
        Project project = repositoryContext.getProject();

        String name = project.getLabel();
        name = name.replaceAll(" ", "_"); //$NON-NLS-1$ //$NON-NLS-2$
        name = name.toLowerCase();

        return name;
    }

    public static String getJobFolderName(String jobName) {
        return jobName.toLowerCase();
    }
}
