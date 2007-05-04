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
package org.talend.designer.runprocess;

import org.apache.log4j.Level;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.ICodeProblemsChecker;
import org.talend.core.model.process.IProcess;

/**
 * DOC qian class global comment. An implementation of the IRunProcessService. <br/>
 * 
 * $Id: talend-code-templates.xml 1 2006-09-29 17:06:40 +0000 (星期五, 29 九月 2006) nrousseau $
 * 
 */
public class RunProcessService implements IRunProcessService {

    private IRunProcessService delegateService;

    /**
     * DOC amaumont RunProcessService constructor comment.
     */
    public RunProcessService() {
        super();
        setDelegateService(new DefaultRunProcessService());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IRunProcessService#createCodeProcessor(org.talend.core.model.process.IProcess,
     * org.talend.core.language.ECodeLanguage, boolean)
     */
    public IProcessor createCodeProcessor(IProcess process, ECodeLanguage language, boolean filenameFromLabel) {
        return delegateService.createCodeProcessor(process, language, filenameFromLabel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IRunProcessService#createPerformanceData(java.lang.String)
     */
    public IPerformanceData createPerformanceData(String data) {
        return delegateService.createPerformanceData(data);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IRunProcessService#getProject(org.talend.core.language.ECodeLanguage)
     */
    public IProject getProject(ECodeLanguage language) throws CoreException {
        return delegateService.getProject(language);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IRunProcessService#getRoutineFilenameExt()
     */
    public String getRoutineFilenameExt() {
        return delegateService.getRoutineFilenameExt();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IRunProcessService#getSyntaxChecker(org.talend.core.language.ECodeLanguage)
     */
    public ICodeProblemsChecker getSyntaxChecker(ECodeLanguage codeLanguage) {
        return delegateService.getSyntaxChecker(codeLanguage);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IRunProcessService#perlExec(java.lang.StringBuffer, java.lang.StringBuffer,
     * org.eclipse.core.runtime.IPath, java.lang.String, org.apache.log4j.Level, java.lang.String, java.lang.String,
     * int, int, java.lang.String[])
     */
    public int perlExec(StringBuffer out, StringBuffer err, IPath absCodePath, String contextName, Level level,
            String perlInterpreterLibOption, String perlModuleDirectoryOption, int statOption, int traceOption,
            String... codeOptions) throws ProcessorException {
        return delegateService.perlExec(out, err, absCodePath, contextName, level, perlInterpreterLibOption,
                perlModuleDirectoryOption, statOption, traceOption, codeOptions);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IRunProcessService#removeProcess(org.talend.core.model.process.IProcess)
     */
    public void removeProcess(IProcess activeProcess) {
        delegateService.removeProcess(activeProcess);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IRunProcessService#setActiveProcess(org.talend.core.model.process.IProcess)
     */
    public void setActiveProcess(IProcess activeProcess) {
        delegateService.setActiveProcess(activeProcess);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IRunProcessService#setDelegateService(org.talend.designer.runprocess.IRunProcessService)
     */
    public void setDelegateService(IRunProcessService delegateService) {
        boolean isValidDelegate = delegateService != null && !(delegateService instanceof RunProcessService); 
        if (isValidDelegate) {
            this.delegateService = delegateService;
        } else {
            throw new IllegalArgumentException("delegateService can't be null and can't inherit from RunProcessService");
        }
    }

}
