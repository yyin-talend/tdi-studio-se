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
package org.talend.designer.mapper.language.perl;

import org.apache.commons.lang.StringUtils;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.designer.runprocess.Processor;
import org.talend.designer.runprocess.ProcessorException;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class PerlExpressionSyntaxChecker {

    public static final int PERL_STATUS_OK = 0;

    public static final int PERL_STATUS_ERROR = 2;

    public String checkSyntax(String expression) {
        StringBuffer out = new StringBuffer();
        StringBuffer err = new StringBuffer();
        int status = -1;

        // System.out.println("expression='"+ expression + "'");
        String expressionEscaped = StringUtils.replace(expression, "\"", "\\\"");
        String expressionEscapedQuoted = "\"" + expressionEscaped + "\"";
        try {
            status = Processor.exec(out, err, null, null, "-ce", expressionEscapedQuoted, -1, -1, new String[0]);
        } catch (ProcessorException e) {
            ExceptionHandler.process(e);
        }

        String messageResponse = null;

        if (status == PERL_STATUS_OK) {

        } else if (status == PERL_STATUS_ERROR) {
            messageResponse = err.toString();
        } else {
            messageResponse = "Perl response status unknown:\n" + err.toString();
        }

        // System.out.println("\nout= " + out.toString());
        // System.out.println("err= " + err.toString());

        return messageResponse;
    }

}
