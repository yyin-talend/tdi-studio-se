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
package org.talend.repository.ui.wizards.exportjob.deletion;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.CoreException;
import org.talend.commons.exception.RuntimeExceptionHandler;
import org.talend.core.CorePlugin;
import org.talend.core.language.ECodeLanguage;

/**
 * Delete jobs after exported.
 * 
 * yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: AbstractJobDeletionAfterExported.java 下午01:19:32 2007-6-22 +0000 (2007-6-22) yzhang $
 * 
 */
public abstract class AbstractJobDeletionAfterExported {

    protected IWorkspace workspace;

    /**
     * Initialize workspace.
     * 
     * yzhang AbstractJobDeletionAfterExported constructor comment.
     */
    public AbstractJobDeletionAfterExported(ECodeLanguage language) {
        try {
            workspace = CorePlugin.getDefault().getRunProcessService().getProject(language).getWorkspace();
        } catch (CoreException e) {
            RuntimeExceptionHandler.process(e);
        }
    }

    /**
     * Return the path of resources.
     * 
     * yzhang Comment method "getPath".
     * 
     * @return
     */
    abstract protected String getPath();

    /**
     * Remove job resources.
     * 
     * yzhang Comment method "removeJobResource".
     * 
     * @param jobName
     */
    abstract public void removeJobResource(String jobName);

    /**
     * Return the related resource under the given name.
     * 
     * yzhang Comment method "getResource".
     * 
     * @param resourcName
     * @return
     */
    public IResource getResource(String resourcName) {
        return workspace.getRoot().findMember(getPath() + "/" + resourcName);
    }

}
