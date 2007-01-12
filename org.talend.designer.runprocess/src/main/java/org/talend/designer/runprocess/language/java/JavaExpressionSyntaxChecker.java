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
package org.talend.designer.runprocess.language.java;

import org.talend.core.language.ICodeSyntaxChecker;
import org.talend.core.model.process.Problem;

/**
 * Check syntax of Java expressions.
 * 
 * $Id$
 * 
 */
public class JavaExpressionSyntaxChecker implements ICodeSyntaxChecker {

    /**
     * DOC amaumont PerlExpressionSyntaxChecker constructor comment.
     */
    public JavaExpressionSyntaxChecker() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.language.perl.CodeSyntaxChecker#checkSyntax(java.lang.String)
     */
    public Problem checkSyntax(String expression) {
        
        return null;
    }

}
