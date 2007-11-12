// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the  agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//   
// ============================================================================
package org.talend.designer.codegen;

import org.eclipse.core.runtime.jobs.Job;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
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

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.codegen.ICodeGeneratorService#createJavaRoutineSynchronizer()
     */
    public IRoutineSynchronizer createJavaRoutineSynchronizer() {
        // TODO Auto-generated method stub
        return new JavaRoutineSynchronizer();
    }

    public IRoutineSynchronizer createRoutineSynchronizer() {
        ECodeLanguage lan = LanguageManager.getCurrentLanguage();
        if (lan.equals(ECodeLanguage.PERL)) {
            return createPerlRoutineSynchronizer();
        } else if (lan.equals(ECodeLanguage.JAVA)) {
            return createJavaRoutineSynchronizer();
        }
        throw new IllegalArgumentException("invalid language type.");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.codegen.ICodeGeneratorService#initializeTemplates(org.eclipse.core.runtime.IProgressMonitor)
     */
    public Job initializeTemplates() {
        return CodeGeneratorEmittersPoolFactory.initialize();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.codegen.ICodeGeneratorService#generationInit()
     */
    public void generationInit() {
        CodeGeneratorEmittersPoolFactory.initialize();
    }

}
