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
package org.talend.designer.core.ui.editor;

import org.eclipse.jdt.core.compiler.IProblem;


/**
 * DOC amaumont  class global comment. Detailled comment
 * <br/>
 *
 * $Id$
 *
 */
public class DetailedProblem {

    private IProblem problem;
    private String key;
    private String source;

    /**
     * DOC amaumont ProblemWithLine constructor comment.
     * @param problem
     * @param line
     * @param source 
     */
    public DetailedProblem(IProblem problem, String line, String source) {
        this.problem = problem;
        this.key = line;
        this.source = source;
    }

    
    /**
     * Getter for line.
     * @return the line
     */
    public String getKey() {
        return this.key;
    }

    
    /**
     * Getter for problem.
     * @return the problem
     */
    public IProblem getJdtProblem() {
        return this.problem;
    }


    
    /**
     * Getter for source.
     * @return the source
     */
    public String getSource() {
        return this.source;
    }
    
    

}
