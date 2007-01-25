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
package org.talend.designer.runprocess;

import org.apache.log4j.Level;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.talend.core.language.ICodeProblemsChecker;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.temp.ECodeLanguage;
import org.talend.designer.runprocess.data.PerformanceData;
import org.talend.designer.runprocess.java.JavaProcessor;
import org.talend.designer.runprocess.language.SyntaxCheckerFactory;
import org.talend.designer.runprocess.perl.PerlProcessor;
import org.talend.designer.runprocess.perl.PerlUtils;

/**
 * DOC qian class global comment. An implementation of the IRunProcessService. <br/>
 * 
 * $Id: talend-code-templates.xml 1 2006-09-29 17:06:40 +0000 (星期五, 29 九月 2006) nrousseau $
 * 
 */
public class RunProcessService implements IRunProcessService {

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
     * org.eclipse.core.runtime.IPath, org.eclipse.core.runtime.IPath, org.apache.log4j.Level, java.lang.String,
     * java.lang.String, int, int, java.lang.String)
     */
    public int exec(StringBuffer out, StringBuffer err, IPath absCodePath, IPath absContextPath, Level level,
            String perlInterpreterLibOption, String perlInterpreterLibCtxOption, String perlModuleDirectoryOption,
            int statOption, int traceOption, String... codeOptions) throws ProcessorException {
        return Processor.exec(out, err, absCodePath, absContextPath, level, perlInterpreterLibOption,
                perlInterpreterLibCtxOption, perlModuleDirectoryOption, statOption, traceOption, codeOptions);

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
     * @param process
     * @param filenameFromLabel
     * @return
     */
    private IProcessor createPerlProcessor(IProcess process, boolean filenameFromLabel) {
        return new PerlProcessor(process, filenameFromLabel);
    }

   
    /**
     * DOC xue Comment method "createJavaProcessor".
     * @param process
     * @param filenameFromLabel
     * @return
     */
    private IProcessor createJavaProcessor(IProcess process, boolean filenameFromLabel) {
        return new JavaProcessor(process, filenameFromLabel);
    }

    
    public IPerformanceData createPerformanceData(String data) {
        return new PerformanceData(data);
    }

    /**
     * DOC qian Gets the perl project.
     * 
     * @return IProject
     * @throws CoreException
     */
    public IProject getProject() throws CoreException {
        return PerlUtils.getProject();
    }

    public String getRoutineFilenameExt() {
        return PerlUtils.ROUTINE_FILENAME_EXT;
    }

}
