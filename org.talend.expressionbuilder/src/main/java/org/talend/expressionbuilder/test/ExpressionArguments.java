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
package org.talend.expressionbuilder.test;

import java.util.ArrayList;
import java.util.List;

import org.talend.expressionbuilder.test.shadow.Variable;

/**
 * The argument that transfered to code generator.
 * 
 * yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: ExpressionArguments.java 上午09:55:03 2007-6-29 +0000 (2007-6-29) yzhang $
 * 
 */
public class ExpressionArguments {

    private String expression;

    private List<Variable> variables;

    /**
     * Initialize member variables.
     * 
     * yzhang ExpressionArguments constructor comment.
     */
    public ExpressionArguments() {
        variables = new ArrayList<Variable>();
    }

    /**
     * Getter for expression.
     * 
     * @return the expression
     */
    public String getExpression() {
        return this.expression;
    }

    /**
     * Getter for variables.
     * 
     * @return the variables
     */
    public List<Variable> getVariables() {
        return this.variables;
    }

    /**
     * Sets the variables.
     * 
     * @param variables the variables to set
     */
    public void setVariables(List<Variable> variables) {
        this.variables = variables;
    }

    /**
     * Sets the expression.
     * 
     * @param expression the expression to set
     */
    public void setExpression(String expression) {
        this.expression = expression;
    }

}
