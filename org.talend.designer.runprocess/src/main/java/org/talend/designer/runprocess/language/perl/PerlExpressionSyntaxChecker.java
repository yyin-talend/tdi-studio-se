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
package org.talend.designer.runprocess.language.perl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.model.process.Problem;
import org.talend.core.model.process.Problem.ProblemStatus;
import org.talend.designer.core.language.perl.ICodeSyntaxChecker;
import org.talend.designer.runprocess.Processor;
import org.talend.designer.runprocess.ProcessorException;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class PerlExpressionSyntaxChecker implements ICodeSyntaxChecker {

    private static File tempFile;

    public static final String STRING1_EXPRESSION = "-e";

    public static final String STRING1_PREFIX = " at ";

    public static final String STRING1_SUFFIX = " line ";

    public static final String STRING1_SUFFIX_PREFIX = STRING1_PREFIX + STRING1_SUFFIX;

    public static final String STRING2_SUFFIX = " had compilation errors";

    public static final String STRING2_REPLACED = ", expression had compilation errors";

    public static final int PERL_STATUS_OK = 0;

    public static final int PERL_STATUS_ERROR_EXPRESSION = 2;

    public static final int PERL_STATUS_ERROR_FILE = 9;

    // private PerlValidatorErrors errors;

    /**
     * DOC amaumont PerlExpressionSyntaxChecker constructor comment.
     */
    public PerlExpressionSyntaxChecker() {
        super();
    }

    /**
     * DOC amaumont Comment method "getTempFile".
     */
    private File getTempFile() {
        if (tempFile == null || !tempFile.exists()) {
            try {
                tempFile = File.createTempFile("perlSyntaxChecker", ".tmp");
                return tempFile;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            return tempFile;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.core.language.perl.CodeSyntaxChecker#checkSyntax(java.lang.String)
     */
    public Problem checkSyntax(String expression) {
        if (expression == null) {
            return null;
        }

        StringBuffer out = new StringBuffer();
        StringBuffer err = new StringBuffer();

        int status = -1;
//        // if expression contains a backslash, we must parse a File because parsing expression is not reliable
//        if (false && expression.indexOf('\\') == -1) {
//            TimeMeasure.start("expression");
//            status = callPerlCommandWithExpression(expression, out, err);
//            TimeMeasure.end("expression");
//        } else {
//            TimeMeasure.start("file");
            status = callPerlCommandWithFile(expression, out, err);
//            TimeMeasure.end("file");
//        }

        String stdErr = null;

        if (status == PERL_STATUS_OK) {

        } else if (status == PERL_STATUS_ERROR_EXPRESSION || status == PERL_STATUS_ERROR_FILE) {
            stdErr = err.toString();
            if (status == PERL_STATUS_ERROR_EXPRESSION) {
                stdErr = replaceStdErrStringForExpression(stdErr);

            } else if (status == PERL_STATUS_ERROR_FILE) {
                stdErr = replaceStdErrStringForFile(stdErr);
            }

        } else {
            stdErr = "Perl response status unknown:\n" + err.toString();
        }

        if (stdErr != null) {
            return new Problem(null, stdErr, ProblemStatus.ERROR);
        }

        // System.out.println("\nout= " + out.toString());
        // System.out.println("err= " + err.toString());

        return null;
    }

    /**
     * DOC amaumont Comment method "replaceStdErrStringForFile".
     * 
     * @param stdErr
     * @return
     */
    private String replaceStdErrStringForFile(String stdErr) {
        String absolutePath = tempFile.getAbsolutePath();
        stdErr = StringUtils.replace(stdErr, STRING1_PREFIX + absolutePath + STRING1_SUFFIX, STRING1_SUFFIX_PREFIX);
        stdErr = StringUtils.replace(stdErr, absolutePath + STRING2_SUFFIX, STRING2_REPLACED);
        return stdErr;
    }

    /**
     * DOC amaumont Comment method "replaceStdErrStringForExpression".
     * 
     * @param stdErr
     * @return
     */
    private String replaceStdErrStringForExpression(String stdErr) {
        stdErr = StringUtils.replace(stdErr, STRING1_PREFIX + STRING1_EXPRESSION + STRING1_SUFFIX, STRING1_SUFFIX_PREFIX);
        stdErr = StringUtils.replace(stdErr, STRING1_EXPRESSION + STRING2_SUFFIX, STRING2_REPLACED);
        return stdErr;
    }

    /**
     * DOC amaumont Comment method "callPerlCommandWithExpression".
     * 
     * @param err
     * @param out
     * @param expression
     * @return status
     */
    private int callPerlCommandWithExpression(String expression, StringBuffer out, StringBuffer err) {
        int status = -1;

        // System.out.println("expression='"+ expression + "'");
        String expressionEscaped = StringUtils.replace(expression, "\"", "\\\"");
        for (int i = expression.length(); i > 0; i--) {
            String chara = expression.substring(i - 1, i);
            if (!chara.equals("\\")) {
                if (i % 2 == 0 && i < expression.length()) {
                    expressionEscaped = expressionEscaped + "\\";
                    break;
                }
                break;
            }
        }

        String expressionEscapedQuoted = "\"" + expressionEscaped + "\"";
//        System.out.println(expressionEscapedQuoted);
        try {
            status = Processor.exec(out, err, null, null, Level.TRACE, "-ce", expressionEscapedQuoted, -1, -1, new String[0]);
        } catch (ProcessorException e) {
            ExceptionHandler.process(e);
        }

        return status;
    }

    /**
     * DOC amaumont Comment method "callPerlCommandWithExpression".
     * 
     * @param err
     * @param out
     * @param expression
     * @return status
     */
    private int callPerlCommandWithFile(String expression, StringBuffer out, StringBuffer err) {
        int status = -1;

        File file = getTempFile();

        if (file.canWrite()) {
            try {
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(expression);
                fileWriter.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new RuntimeException("File is not writable");
        }

//        System.out.println(expression);
        try {
            status = Processor.exec(out, err, null, null, Level.TRACE, "-c", file.getAbsolutePath(), -1, -1, new String[0]);
        } catch (ProcessorException e) {
            ExceptionHandler.process(e);
        }

        return status;
    }

    // /**
    // * DOC amaumont Comment method "findErrorMessage".
    // *
    // * @param stdErr
    // */
    // private ErrorMessage findErrorMessage(String stdErr) {
    // List lines = makeLinesList(stdErr);
    // boolean continued = false;
    //
    // PerlValidatorErrors.ErrorMessage errorMsg = null;
    // // Markers have to be added in reverse order
    // // Otherwise lower line number will appear at the end of the list
    // for (int i = lines.size() - 1; i >= 0; i--) {
    // String line = (String) lines.get(i);
    //
    // // Is this a continuation of the line i-1?
    // if (line.startsWith(" ")) {
    // continued = true;
    // continue;
    // } else {
    // if (continued) {
    // line += lines.get(i + 1);
    // }
    // continued = false;
    // }
    //
    // ParsedErrorLine pline = new ParsedErrorLine(line);
    // errorMsg = errors.getErrorMessage(pline.getMessage());
    // }
    // return errorMsg;
    //
    // }
    //
    // /**
    // * Splits up the given text content into a list of up to maxErrorsShown lines. If there are more lines in content,
    // * remaining lines are ignored.
    // *
    // * @return a list of Strings, one per line (without line terminators)
    // */
    // private static List makeLinesList(String perlOutput) {
    // List lines = new ArrayList();
    // StringTokenizer st = new StringTokenizer(perlOutput, "\r\n");
    // int lineCount = 0;
    //
    // while (st.hasMoreTokens() && lineCount < maxErrorsShown) {
    // lines.add(st.nextToken());
    // lineCount++;
    // }
    // return lines;
    // }
    //
    // /**
    // *
    // * DOC amaumont PerlExpressionSyntaxChecker class global comment. Detailled comment <br/>
    // *
    // * $Id$
    // *
    // */
    // protected static class ParsedErrorLine {
    //
    // private static final Pattern ERROR_LINE_NO_PATTERN = Pattern.compile("^(.*) at (\\S+) line (\\d+)[\\.,]");
    //
    // private static final Pattern CGI_CARP_PATTERN = Pattern.compile("^\\[.*?\\] \\S+: (.*)");
    //
    // private final String line;
    //
    // private final String msg;
    //
    // private final String path;
    //
    // private final int lineNo;
    //
    // public ParsedErrorLine(String line) {
    // this.line = line;
    //
    // Matcher m = ERROR_LINE_NO_PATTERN.matcher(line);
    // if (m.find()) {
    // msg = normalizeMsg(m.group(1));
    // path = m.group(2);
    // lineNo = parseInt(m.group(3));
    // } else {
    // msg = normalizeMsg(line);
    // path = "-";
    // lineNo = -1;
    // }
    // }
    //
    // public int getLineNumber() {
    // return lineNo;
    // }
    //
    // public String getMessage() {
    // return msg;
    // }
    //
    // public String getPath() {
    // return path;
    // }
    //
    // public boolean isLocalError() {
    // return "-".equals(path);
    // }
    //
    // public String toString() {
    // return msg + ", " + path + ":" + lineNo;
    // }
    //
    // private int parseInt(String str) {
    // try {
    // return Integer.parseInt(str);
    // } catch (NumberFormatException e) {
    // // this one should never occur
    // ExceptionHandler.process(new RuntimeException("Could not parse line number contained in Perl " + "error message
    // {" + line
    // + "}; report it as a bug ", e));
    // return -1;
    // }
    // }
    //
    // private String normalizeMsg(String msg) {
    // return stripCGICarpOutput(msg);
    // }
    //
    // /**
    // * @return msg with CGI::Carp's timestamp stripped from the beginning (if it was present)
    // */
    // private String stripCGICarpOutput(String msg) {
    // if (msg.startsWith("[")) {
    // Matcher m = CGI_CARP_PATTERN.matcher(msg);
    // if (m.find())
    // return m.group(1);
    // }
    // return msg;
    // }
    // }

}
