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
package org.talend.designer.core.language.perl;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.epic.perleditor.editors.util.PerlValidatorErrors;
import org.epic.perleditor.editors.util.PerlValidatorErrors.ErrorMessage;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.utils.time.TimeMeasure;
import org.talend.core.model.process.Problem;
import org.talend.core.model.process.Problem.ProblemStatus;
import org.talend.designer.runprocess.Processor;
import org.talend.designer.runprocess.ProcessorException;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class PerlExpressionSyntaxChecker {

    private static int maxErrorsShown = 500;

    public static final String SEARCHED_STRING1 = " at -e line ";

    public static final String REPLACED_STRING1 = " at line ";

    public static final String SEARCHED_STRING2 = "-e had compilation errors";

    public static final String REPLACED_STRING2 = "had compilation errors";

    public static final int PERL_STATUS_OK = 0;

    public static final int PERL_STATUS_ERROR = 2;

//    private PerlValidatorErrors errors;

    /**
     * DOC amaumont PerlExpressionSyntaxChecker constructor comment.
     */
    public PerlExpressionSyntaxChecker() {
        super();
//        this.errors = new PerlValidatorErrors();
    }

    public Problem checkSyntax(String expression) {
        if (expression == null) {
            return null;
        }
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

        String stdErr = null;

        if (status == PERL_STATUS_OK) {

        } else if (status == PERL_STATUS_ERROR) {
            stdErr = err.toString();
//            List<String> lines = makeLinesList(err.toString());
//            
//            stdErr = lines.get(lines.size()-2);
//            stdErr = StringUtils.capitalize(stdErr);
//            for (int i = 0; i < lines.size() - 2; i++) {
//                stdErr += "\n" + lines.get(i);
//            }
            stdErr = stdErr.replaceAll(SEARCHED_STRING1, REPLACED_STRING1);
            
//            ErrorMessage errorMessage = findErrorMessage(stdErr);
//            System.out.println("######################################");
//            System.out.println(errorMessage.getExplanation());
//            System.out.println(errorMessage.getSeverity());
//            System.out.println(errorMessage.isUnknown());
//            System.out.println(errorMessage.isWarning());

            stdErr = stdErr.replaceAll(SEARCHED_STRING2, REPLACED_STRING2);
        } else {
            stdErr = "Perl response status unknown:\n" + err.toString();
        }

        if(stdErr != null) {
            return new Problem(null, stdErr, ProblemStatus.ERROR);
        }
        
        // System.out.println("\nout= " + out.toString());
        // System.out.println("err= " + err.toString());

        return null;
    }

//    /**
//     * DOC amaumont Comment method "findErrorMessage".
//     * 
//     * @param stdErr
//     */
//    private ErrorMessage findErrorMessage(String stdErr) {
//        List lines = makeLinesList(stdErr);
//        boolean continued = false;
//
//        PerlValidatorErrors.ErrorMessage errorMsg = null;
//        // Markers have to be added in reverse order
//        // Otherwise lower line number will appear at the end of the list
//        for (int i = lines.size() - 1; i >= 0; i--) {
//            String line = (String) lines.get(i);
//
//            // Is this a continuation of the line i-1?
//            if (line.startsWith(" ")) {
//                continued = true;
//                continue;
//            } else {
//                if (continued) {
//                    line += lines.get(i + 1);
//                }
//                continued = false;
//            }
//
//            ParsedErrorLine pline = new ParsedErrorLine(line);
//            errorMsg = errors.getErrorMessage(pline.getMessage());
//        }
//        return errorMsg;
//
//    }
//
//    /**
//     * Splits up the given text content into a list of up to maxErrorsShown lines. If there are more lines in content,
//     * remaining lines are ignored.
//     * 
//     * @return a list of Strings, one per line (without line terminators)
//     */
//    private static List makeLinesList(String perlOutput) {
//        List lines = new ArrayList();
//        StringTokenizer st = new StringTokenizer(perlOutput, "\r\n");
//        int lineCount = 0;
//
//        while (st.hasMoreTokens() && lineCount < maxErrorsShown) {
//            lines.add(st.nextToken());
//            lineCount++;
//        }
//        return lines;
//    }
//
//    /**
//     * 
//     * DOC amaumont PerlExpressionSyntaxChecker class global comment. Detailled comment <br/>
//     * 
    // * $Id$
//     * 
//     */
//    protected static class ParsedErrorLine {
//
//        private static final Pattern ERROR_LINE_NO_PATTERN = Pattern.compile("^(.*) at (\\S+) line (\\d+)[\\.,]");
//
//        private static final Pattern CGI_CARP_PATTERN = Pattern.compile("^\\[.*?\\] \\S+: (.*)");
//
//        private final String line;
//
//        private final String msg;
//
//        private final String path;
//
//        private final int lineNo;
//
//        public ParsedErrorLine(String line) {
//            this.line = line;
//
//            Matcher m = ERROR_LINE_NO_PATTERN.matcher(line);
//            if (m.find()) {
//                msg = normalizeMsg(m.group(1));
//                path = m.group(2);
//                lineNo = parseInt(m.group(3));
//            } else {
//                msg = normalizeMsg(line);
//                path = "-";
//                lineNo = -1;
//            }
//        }
//
//        public int getLineNumber() {
//            return lineNo;
//        }
//
//        public String getMessage() {
//            return msg;
//        }
//
//        public String getPath() {
//            return path;
//        }
//
//        public boolean isLocalError() {
//            return "-".equals(path);
//        }
//
//        public String toString() {
//            return msg + ", " + path + ":" + lineNo;
//        }
//
//        private int parseInt(String str) {
//            try {
//                return Integer.parseInt(str);
//            } catch (NumberFormatException e) {
//                // this one should never occur
    // ExceptionHandler.process(new RuntimeException("Could not parse line number contained in Perl " + "error message
    // {" + line
//                        + "}; report it as a bug ", e));
//                return -1;
//            }
//        }
//
//        private String normalizeMsg(String msg) {
//            return stripCGICarpOutput(msg);
//        }
//
//        /**
//         * @return msg with CGI::Carp's timestamp stripped from the beginning (if it was present)
//         */
//        private String stripCGICarpOutput(String msg) {
//            if (msg.startsWith("[")) {
//                Matcher m = CGI_CARP_PATTERN.matcher(msg);
//                if (m.find())
//                    return m.group(1);
//            }
//            return msg;
//        }
//    }

}
