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
package org.talend.designer.runprocess.shadow;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Status;
import org.talend.core.utils.XmlArray;
import org.talend.designer.runprocess.ProcessorException;
import org.talend.designer.runprocess.RunProcessPlugin;
import org.talend.designer.runprocess.shadow.ShadowProcess.EShadowProcessType;
import org.talend.repository.preview.IPreview;
import org.talend.repository.preview.IProcessDescription;

/**
 * Previewer for a file delimited input. <br/>
 * 
 * $Id$
 * 
 */
public class ShadowFilePreview implements IPreview {

    public ShadowProcess shadowProcess;

    /**
     * Constructs a new ShadowPreview.
     */
    public ShadowFilePreview() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.preview.filedelimited.IFileDelimitedPreview#
     *      preview(org.talend.repository.preview.filedelimited.ProcessDescription)
     */
    @SuppressWarnings("unchecked")
    public XmlArray preview(IProcessDescription description, String type) throws CoreException {
        XmlArray res = null;
        
        EShadowProcessType typeShadow = EShadowProcessType.valueOf(type);
        
        shadowProcess = new ShadowProcess(description, typeShadow);
        try {
            res = shadowProcess.run();
        } catch (ProcessorException e) {
            Status status = new Status(Status.ERROR, RunProcessPlugin.PLUGIN_ID, Status.OK, e.getMessage(), e);
            RunProcessPlugin.getDefault().getLog().log(status);
            throw new CoreException(status);
        }
        return res;
    }

    
    /**
     * Stop loading preview.
     */
    public void stopLoading() {
        shadowProcess.destroy();
    }
    
}
