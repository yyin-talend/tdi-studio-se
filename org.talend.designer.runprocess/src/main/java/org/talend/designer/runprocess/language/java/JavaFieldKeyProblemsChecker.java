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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
import org.eclipse.jdt.core.compiler.CategorizedProblem;
import org.eclipse.jdt.core.compiler.IProblem;
import org.talend.core.language.CodeProblemsChecker;
import org.talend.core.model.process.Problem;
import org.talend.designer.core.ui.editor.DetailedProblem;
import org.talend.designer.core.ui.editor.TalendJavaEditor;

/**
 * Check syntax of Java expressions.
 * 
 * $Id$
 * 
 */
public class JavaFieldKeyProblemsChecker extends CodeProblemsChecker {

    /**
     * DOC amaumont PerlExpressionSyntaxChecker constructor comment.
     */
    public JavaFieldKeyProblemsChecker() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.language.perl.CodeSyntaxChecker#checkSyntax(java.lang.String)
     */
    public List<Problem> checkProblems(String expression) {
        throw new UnsupportedOperationException();
    }

    public List<Problem> checkProblems() {
        List<DetailedProblem> list = TalendJavaEditor.getProblems();

        ArrayList<Problem> problems = null;
        if (list != null) {

            problems = new ArrayList<Problem>();
            for (DetailedProblem problemWithLine : list) {
                Problem talendProblem = new Problem();
                talendProblem.setDescription("Error on '" + problemWithLine.getSource() + "'  =>  "
                        + problemWithLine.getJdtProblem().getMessage());
                talendProblem.setStatus(Problem.ProblemStatus.ERROR);
                talendProblem.setAction(Problem.ProblemAction.ADDED);
                talendProblem.setKey(problemWithLine.getKey());
                problems.add(talendProblem);
            }
        }

        setProblems(problems);
        return problems;
    }

}
