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
import org.eclipse.core.runtime.CoreException;
import org.talend.commons.exception.RuntimeExceptionHandler;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.language.ECodeLanguage;

/**
 * Delete java jobs after job exported.
 * 
 * yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: DeleteJavaJobAfterExport.java 下午01:59:54 2007-6-19 +0000 (2007-6-19) yzhang $
 * 
 */
public class DeleteJavaJobAfterExport extends AbstractJobDeletionAfterExported {

    /**
     * Initialize resources related to the jobs. e.g. workspace.
     * 
     * yzhang DeleteJavaJobAfterExport constructor comment.
     */
    public DeleteJavaJobAfterExport(ECodeLanguage language) {
        super(language);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.wizards.exportjob.deletion.AbstractJobDeletionAfterExported#removeJobResource(java.lang.String)
     */
    @Override
    public void removeJobResource(String resourceName) {
        IResource javaRecs = getResource(resourceName.toLowerCase());
        if (javaRecs != null && javaRecs.getType() == IResource.FOLDER) {
            try {
                javaRecs.delete(true, null);
            } catch (CoreException e) {
                RuntimeExceptionHandler.process(e);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.wizards.exportjob.deletion.AbstractJobDeletionAfterExported#getPath()
     */
    @Override
    protected String getPath() {
        String project = ((RepositoryContext) CorePlugin.getContext().getProperty(Context.REPOSITORY_CONTEXT_KEY))
                .getProject().getLabel();
        return ".Java/src" + "/" + project;
    }

}
