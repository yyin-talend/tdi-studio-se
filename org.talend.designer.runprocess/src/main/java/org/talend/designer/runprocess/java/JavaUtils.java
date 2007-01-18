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
package org.talend.designer.runprocess.java;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

/**
 * DOC yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: JavaUtils.java 1 2007-1-18 下午12:52:43 +0000 (下午12:52:43, 2007-1-18 2007) yzhang $
 * 
 */
public class JavaUtils {

    public static final String JAVA_PROJECT_NAME = ".Java";

    private static final String JAVA_NATURE = "org.eclipse.jdt.core.javanature";

    /**
     * DOC yzhang Comment method "getProject".
     * 
     * @return
     * @throws CoreException
     */
    public static IProject getProject() throws CoreException {
        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        IProject prj = root.getProject(JAVA_PROJECT_NAME);

        // Does the java nature exists in the environment
        IExtensionRegistry registry = Platform.getExtensionRegistry();
        IExtension nature = registry.getExtension("org.eclipse.core.resources.natures", JAVA_NATURE);

        if (!prj.exists()) {
            final IWorkspace workspace = ResourcesPlugin.getWorkspace();
            final IProjectDescription desc = workspace.newProjectDescription(prj.getName());
            if (nature != null) {
                desc.setNatureIds(new String[] { JAVA_NATURE });
            }
            prj.create(desc, null);
            prj.open(IResource.BACKGROUND_REFRESH, null);
        } else if (prj.getNature(JAVA_NATURE) == null && nature != null) {
            IProjectDescription description = prj.getDescription();
            String[] natures = description.getNatureIds();
            String[] newNatures = new String[natures.length + 1];
            System.arraycopy(natures, 0, newNatures, 0, natures.length);
            newNatures[natures.length] = JAVA_NATURE;
            description.setNatureIds(newNatures);
            prj.setDescription(description, null);
        }

        return prj;
    }
}
