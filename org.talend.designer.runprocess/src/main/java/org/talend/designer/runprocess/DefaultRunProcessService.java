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
import org.talend.core.language.LanguageManager;
import org.talend.core.model.process.IProcess;
import org.talend.designer.runprocess.data.PerformanceData;
import org.talend.designer.runprocess.java.JavaProcessor;
import org.talend.designer.runprocess.language.SyntaxCheckerFactory;
import org.talend.designer.runprocess.perl.PerlProcessor;
import org.talend.designer.runprocess.perl.PerlUtils;


/**
 * DOC amaumont  class global comment. Detailled comment
 * <br/>
 *
 * $Id: talend-code-templates.xml 1 2006-09-29 17:06:40Z nrousseau $
 *
 */
public class DefaultRunProcessService implements IRunProcessService {
    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IRunProcessFactory#getSyntaxChecker(org.talend.core.model.temp.ECodeLanguage)
     */
    public ICodeProblemsChecker getSyntaxChecker(ECodeLanguage codeLanguage) {
        return SyntaxCheckerFactory.getInstance().getSyntaxChecker(codeLanguage);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IRunProcessFactory#setActiveProcess(org.talend.core.model.process.IProcess)
     */
    public void setActiveProcess(IProcess activeProcess) {
        RunProcessPlugin.getDefault().getRunProcessContextManager().setActiveProcess(activeProcess);
    }

    /**
     * DOC qian Removes IProcess.
     * 
     * @param activeProcess IProcess
     */
    public void removeProcess(IProcess activeProcess) {
        RunProcessPlugin.getDefault().getRunProcessContextManager().removeProcess(activeProcess);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IRunProcessFactory#exec(java.lang.StringBuffer, java.lang.StringBuffer,
     * org.eclipse.core.runtime.IPath, java.lang.String, org.apache.log4j.Level, java.lang.String,
     * int, int, java.lang.String)
     */
    public int perlExec(StringBuffer out, StringBuffer err, IPath absCodePath, String contextName, Level level,
            String perlInterpreterLibOption, String perlModuleDirectoryOption,
            int statOption, int traceOption, String... codeOptions) throws ProcessorException {

        return PerlProcessor.exec(out, err, absCodePath, contextName, level, perlInterpreterLibOption,
                perlModuleDirectoryOption, statOption, traceOption, codeOptions);

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.runprocess.IRunProcessFactory#createCodeProcessor(org.talend.core.model.process.IProcess,
     * boolean)
     */
    public IProcessor createCodeProcessor(IProcess process, ECodeLanguage language, boolean filenameFromLabel) {
        switch (language) {
        case PERL:
            return createPerlProcessor(process, filenameFromLabel);
        case JAVA:
            return createJavaProcessor(process, filenameFromLabel);
        default:
            return createPerlProcessor(process, filenameFromLabel);
        }
    }

    /**
     * DOC xue Comment method "createPerlProcessor".
     * 
     * @param process
     * @param filenameFromLabel
     * @return
     */
    protected IProcessor createPerlProcessor(IProcess process, boolean filenameFromLabel) {
        return new PerlProcessor(process, filenameFromLabel);
    }

    /**
     * DOC xue Comment method "createJavaProcessor".
     * 
     * @param process
     * @param filenameFromLabel
     * @return
     */
    protected IProcessor createJavaProcessor(IProcess process, boolean filenameFromLabel) {
        return new JavaProcessor(process, filenameFromLabel);
    }

    public IPerformanceData createPerformanceData(String data) {
        return new PerformanceData(data);
    }

    public String getRoutineFilenameExt() {
        return PerlUtils.ROUTINE_FILENAME_EXT;
    }

    public IProject getProject(ECodeLanguage language) throws CoreException {
        switch (language) {
        case PERL:
            return PerlUtils.getProject();
        case JAVA:
            return JavaProcessor.getProcessorProject();
        default:
            return PerlUtils.getProject();
        }
    }

    /* (non-Javadoc)
     * @see org.talend.designer.runprocess.IRunProcessService#setDelegateService(org.talend.designer.runprocess.IRunProcessService)
     */
    public void setDelegateService(IRunProcessService delegateService) {
        throw new UnsupportedOperationException("This method should'nt be called here, use it on RunProcessService class.");
    }

    public void updateLibraries() throws CoreException {
        switch (LanguageManager.getCurrentLanguage()) {
        case JAVA:
            JavaProcessor.updateClasspath();
        default:
            // nothing
        }
    }

}
