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
package org.talend.designer.runprocess.java;

/**
 * DOC bqian class global comment. Detailled comment <br/>
 * 
 */
public class HtmlRegexHandle {

    public static void main(String[] args) {
    }

    // public static final String SNIPPET_REGEX = SnippetManager.SNIPPET_PREFFIX + "(.*?)" +
    // SnippetManager.SNIPPET_SUFFIX;

    // public static String subjectString = "ppppuuu<fragment>secondFragement</fragmen>hh";

    public static String extractFristMatchFromFragment(String subjectString) {
        // String extractString = null;
        // try {
        // Pattern regex = Pattern.compile(SNIPPET_REGEX);
        // Matcher regexMatcher = regex.matcher(subjectString);
        // regexMatcher.find();
        // extractString = regexMatcher.group(1);
        // } catch (Exception e) {
        // if (!e.getMessage().equals("No match found")) {
        // ExceptionHandler.process(e);
        // }
        // }
        // return extractString;
        return "";
    }

    public static String insertIntoFirstMatchFragment(String replaceString, String subjectString) {
        // StringBuffer resultString = new StringBuffer();
        // try {
        // Pattern regex = Pattern.compile(SNIPPET_REGEX);
        // Matcher regexMatcher = regex.matcher(subjectString);
        // regexMatcher.find();
        // try {
        // regexMatcher.appendReplacement(resultString, replaceString);
        // } catch (IllegalStateException ex) {
        // // appendReplacement() called without a prior successful call to find()
        // } catch (IllegalArgumentException ex) {
        // // Syntax error in the replacement text (unescaped $ signs?)
        // }
        // regexMatcher.appendTail(resultString);
        // } catch (PatternSyntaxException ex) {
        // // Syntax error in the regular expression
        // }
        // return resultString.toString();
        return "";
    }

}