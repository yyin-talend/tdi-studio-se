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
package org.talend.designer.runprocess.shadow;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Status;
import org.talend.core.utils.CsvArray;
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
    @SuppressWarnings("unchecked") //$NON-NLS-1$
    public CsvArray preview(IProcessDescription description, String type) throws CoreException {
        CsvArray res = null;
        
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
