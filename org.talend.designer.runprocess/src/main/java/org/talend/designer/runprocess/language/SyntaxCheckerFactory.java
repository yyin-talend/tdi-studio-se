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
package org.talend.designer.runprocess.language;

import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.ICodeProblemsChecker;
import org.talend.designer.runprocess.language.java.JavaCodeProblemsChecker;
import org.talend.designer.runprocess.language.perl.PerlCodeProblemsChecker;


/**
 * DOC amaumont  class global comment. Detailled comment
 * <br/>
 *
 * $Id$
 *
 */
public class SyntaxCheckerFactory {

    private static SyntaxCheckerFactory instance;

    public static SyntaxCheckerFactory getInstance() {
        if (instance == null) {
            instance = new SyntaxCheckerFactory();
        }
        return instance;
    }
    
    public ICodeProblemsChecker getSyntaxChecker(ECodeLanguage codeLanguage) {
        if (codeLanguage == ECodeLanguage.PERL) {
            return (ICodeProblemsChecker) new PerlCodeProblemsChecker();
        } else if (codeLanguage == ECodeLanguage.JAVA) {
            return (ICodeProblemsChecker) new JavaCodeProblemsChecker();
        } else {
            throw new IllegalArgumentException("Syntax checker not found for this language: " + codeLanguage.getName()); //$NON-NLS-1$
        }
    }
    
}
