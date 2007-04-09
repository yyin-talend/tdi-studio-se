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
 * bqian An util tools for perl version resources. <br/>
 * 
 */
public class PerlResourcesHelper {

    public static final String CONTEXT_FILE_SUFFIX = ".pl";

    /**
     * Gets the specific resource in the perl project.
     * 
     * @param name
     * @param projectName
     * 
     * @return
     */
    public static IResource getSpecificResourceInPerlProject(IPath path) throws CoreException {
        IProject project = RepositoryPlugin.getDefault().getRunProcessService().getProject(ECodeLanguage.PERL);
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
        name = name.toUpperCase();

        return name;
    }

    /**
     * ftang Comment method "escapeSpace".
     * 
     * @param name
     * @return
     */
    public static String escapeSpace(String name) {
        return name != null ? name.replace(" ", "") : ""; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }
}
