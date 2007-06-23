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
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.language.ECodeLanguage;

/**
 * Delete perl script after job exported.
 * 
 * yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: DeletePerlScriptAfterExport.java 下午12:48:54 2007-6-22 +0000 (2007-6-22) yzhang $
 * 
 */
public class DeletePerlScriptAfterExport extends AbstractJobDeletionAfterExported {

    /**
     * Constructor.
     * 
     * yzhang DeletePerlScriptAfterExport constructor comment.
     * 
     * @param language
     */
    public DeletePerlScriptAfterExport(ECodeLanguage language) {
        super(language);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.wizards.exportjob.deletion.AbstractJobDeletionAfterExported#getPath()
     */
    @Override
    protected String getPath() {
        return ".Perl";
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.wizards.exportjob.deletion.AbstractJobDeletionAfterExported#removeJobResource(java.lang.String)
     */
    @Override
    public void removeJobResource(String jobName) {
        String projectName = ((RepositoryContext) CorePlugin.getContext().getProperty(Context.REPOSITORY_CONTEXT_KEY))
                .getProject().getLabel();

        StringBuffer fileName = new StringBuffer();
        fileName.append(projectName.toUpperCase());
        fileName.append(".");
        fileName.append("job_");
        fileName.append(jobName);
        fileName.append(".pl");

        StringBuffer defaultFileName = new StringBuffer();
        defaultFileName.append(projectName.toUpperCase());
        defaultFileName.append(".");
        defaultFileName.append("job_");
        defaultFileName.append(jobName);
        defaultFileName.append("_Default");
        defaultFileName.append(".pl");

        IResource resource = getResource(fileName.toString());
        IResource resourceDefault = getResource(defaultFileName.toString());
        try {
            if (resource != null) {
                resource.delete(true, null);
            }
            if (resourceDefault != null) {
                resourceDefault.delete(true, null);
            }
        } catch (CoreException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
