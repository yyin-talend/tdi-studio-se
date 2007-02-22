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
package org.talend.designer.codegen;

import org.eclipse.core.runtime.IProgressMonitor;
import org.talend.core.model.process.IProcess;
import org.talend.designer.codegen.model.CodeGeneratorEmittersPoolFactory;

/**
 * DOC bqian class global comment. Provides services for CodeGenerator plugin. <br/>
 * 
 * $Id: talend-code-templates.xml 1 2006-09-29 17:06:40 +0000 (星期五, 29 九月 2006) nrousseau $
 * 
 */
public class CodeGeneratorService implements ICodeGeneratorService {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.codegen.ICodeGeneratorFactory#getCodeGenerator()
     */
    public ICodeGenerator createCodeGenerator() {
        return new CodeGenerator();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.codegen.ICodeGeneratorFactory#getCodeGenerator(org.talend.core.model.process.IProcess,
     * boolean, boolean, boolean, java.lang.String)
     */
    public ICodeGenerator createCodeGenerator(IProcess process, boolean statistics, boolean trace, String... options) {
        return new CodeGenerator(process, statistics, trace, options);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.codegen.ICodeGeneratorService#getRoutineSynchronizer()
     */
    public IRoutineSynchronizer createPerlRoutineSynchronizer() {
        return new PerlRoutineSynchronizer();
    }

    /* (non-Javadoc)
     * @see org.talend.designer.codegen.ICodeGeneratorService#createJavaRoutineSynchronizer()
     */
    public IRoutineSynchronizer createJavaRoutineSynchronizer() {
        // TODO Auto-generated method stub
        return new JavaRoutineSynchronizer();
    }

    /* (non-Javadoc)
     * @see org.talend.designer.codegen.ICodeGeneratorService#initializeTemplates(org.eclipse.core.runtime.IProgressMonitor)
     */
    public void initializeTemplates() {
        CodeGeneratorEmittersPoolFactory.initialize();
    }

}
