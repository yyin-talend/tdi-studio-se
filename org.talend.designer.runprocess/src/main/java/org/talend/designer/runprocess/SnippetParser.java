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
package org.talend.designer.runprocess;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.utils.StringUtils;
import org.talend.commons.utils.data.container.RootContainer;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.properties.SnippetItem;
import org.talend.core.model.properties.SnippetVariable;
import org.talend.core.model.repository.IRepositoryObject;

/**
 * DOC bqian class global comment. Detailled comment <br/>
 * 
 */
public class SnippetParser {

    /**
     * store the snippet temporarily <br/>
     * 
     */
    class SnippetStore {

        String id = null;

        String name = null;

        Map<String, String> variables = new HashMap<String, String>();
    }

    // public static String SNIPPETSTRING = "first statment\n" + "/*SNIPPET_START ID=1133 aaa(P1,P2)*/\n" + "/*oooo*/\n"
    // + "String.valueOf()\n" + "/*SNIPPET_END*/\n" + "/* SNIPPET_START ID=1134 aaa(P1,P2) */\n" + "String.valueOf()\n"
    // + "/*SNIPPET_END*/\n" + "/* 8888 */\n" + "the last statment";
    //
    // public static String SNIPPETSTRING2 = "first statment\n" + "#SNIPPET_START ID=1133 NameX(P1=AA,P2=BB)\n" +
    // "/*oooo*/\n"
    // + "String.valueOf()\n" + "#SNIPPET_END\n" + "# SNIPPET_START ID=1134 aaa(P1,P2) \n" + "String.valueOf()\n"
    // + "#SNIPPET_END\n" + "/* 8888 */\n" + "the last statment";

    // public static void main(String[] args) {
    // System.out.println("The JAVA(/*) sample String is:\n" + SnippetParse.SNIPPETSTRING);
    // Object[] matchString = SnippetParse.findFirstSnippetId(SNIPPETSTRING);
    // for (int i = 0; i < matchString.length; i++) {
    // System.out.println("\nmatchString is:" + matchString[i]);
    // }
    // System.out.println("\nThe replace result:\n" + SnippetParse.replaceFristSnippet("ReplaceString", SNIPPETSTRING));
    // System.out.println("\n&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&\n");
    // System.out.println("The Perl(#) sample String is:\n" + SnippetParse.SNIPPETSTRING2);
    // matchString = SnippetParse.findFirstSnippetId(SNIPPETSTRING2);
    // for (int i = 0; i < matchString.length; i++) {
    // System.out.println("\nmatchString is:" + matchString[i]);
    // }
    // System.out.println("\nThe replace result:\n" + SnippetParse.replaceFristSnippet("ReplaceString",
    // SNIPPETSTRING2));
    // }

    protected List<String> findFirstSnippetId(String allSnippetString) {
        List<String> resultList = new ArrayList<String>();
        String snippetId = null;
        String snippetName = null;
        String vString = null;
        try {
            Pattern regex = Pattern.compile("(/\\*|#)SNIPPET_START\\s+ID=(\\w+)\\s+([^(]+)\\(([^)]*)\\)", Pattern.CANON_EQ);
            Matcher regexMatcher = regex.matcher(allSnippetString);
            regexMatcher.find();
            snippetId = regexMatcher.group(2);
            snippetName = regexMatcher.group(3);
            vString = regexMatcher.group(4);
            resultList.add(snippetId);
            resultList.add(snippetName);
            String[] multiVarible = vString.split(" ");
            for (int i = 0; i < multiVarible.length; i++) {
                resultList.add(multiVarible[i]);
            }
        } catch (RuntimeException ex) {
        }
        return resultList;
    }

    protected String replaceFristSnippet(String replaceString, String allSnappetString) {
        StringBuffer resultString = new StringBuffer();
        try {
            Pattern regex = Pattern.compile("(/\\*|#)SNIPPET_START\\s+ID=(\\w+)(.*(\\r\\n|\\n))+?(/\\*|#)SNIPPET_END(\\*/)*",
                    Pattern.CANON_EQ);
            Matcher regexMatcher = regex.matcher(allSnappetString);
            regexMatcher.find();
            try {
                regexMatcher.appendReplacement(resultString, replaceString);
            } catch (IllegalStateException ex) {
                // appendReplacement() called without a prior successful call to find()
            } catch (IllegalArgumentException ex) {
                // Syntax error in the replacement text (unescaped $ signs?)
            } catch (IndexOutOfBoundsException ex) {
                // Non-existent backreference used the replacement text
            }
            regexMatcher.appendTail(resultString);
        } catch (PatternSyntaxException ex) {
            // Syntax error in the regular expression
        }
        return resultString.toString();
    }

    private SnippetStore convert2SnippetModel(List<String> list) {
        SnippetStore snippet = new SnippetStore();
        snippet.id = list.get(0);
        snippet.name = list.get(1);
        list.remove(0);
        list.remove(0);
        for (String string : list) {
            String[] para = string.split("=");
            snippet.variables.put(para[0].trim(), para[1].trim());
        }
        return snippet;
    }

    public String convertSnippet(String processCode) {
        while (true) {
            List<String> resultList = findFirstSnippetId(processCode);

            if (resultList.isEmpty()) {
                break;
            }

            SnippetStore store = convert2SnippetModel(resultList);
            SnippetItem item = findSnippet(store);
            String snippetString = generateSnippetString(item, store);

            if (snippetString != null) {
                processCode = replaceFristSnippet(snippetString, processCode);
            } else {
                processCode = replaceFristSnippet("", processCode);
            }
        }
        return processCode;
    }

    /**
     * bqian Comment method "generateSnippetString".
     * 
     * @param item
     * @param store
     * @return
     */
    private String generateSnippetString(SnippetItem item, SnippetStore store) {
        String code = getInsertSnippetCode(item, store);
        StringBuilder sb = new StringBuilder();
        if (LanguageManager.getCurrentLanguage().equals(ECodeLanguage.PERL)) {
            sb.append("#SNIPPET_GENERATED_START {0}");
            sb.append("\n");
            sb.append("{1}");
            sb.append("\n#SNIPPET_GENERATED_END");
        } else {
            // Java comment format
            sb.append("/*SNIPPET_GENERATED_START {0}*/");
            sb.append("\n");
            sb.append("{1}");
            sb.append("\n/*SNIPPET_GENERATED_END*/");
        }
        // StringBuilder b = new StringBuilder();
        // b.append("(");
        // for (SnippetVariable var : (List<SnippetVariable>) item.getVariables()) {
        // b.append(var.getValue()).append(",");
        // }
        // b.deleteCharAt(b.length() - 1);
        // b.append(")");
        String snippetDefinition = "ID=" + item.getProperty().getId() + " " + item.getProperty().getLabel();

        String msg = sb.toString();
        MessageFormat format = new MessageFormat(msg);
        Object[] args = new Object[] { snippetDefinition, code }; //$NON-NLS-1$
        msg = format.format(args);

        return msg;
    }

    /**
     * Caculate the code for snippet.
     * 
     * @param item
     * @return
     */
    private String getInsertSnippetCode(SnippetItem fItem, SnippetStore store) {
        // this could be horribly inefficient
        String text = fItem.getContent();
        List<SnippetVariable> variables = (List<SnippetVariable>) fItem.getVariables();

        for (int i = 0; i < variables.size(); i++) {
            SnippetVariable var = variables.get(i);
            String varName = var.getName();
            String varValue = null;
            if (store.variables.get(varName) != null) {
                varValue = store.variables.get(varName);
            } else {
                varValue = var.getValue();
            }
            text = StringUtils.replace(text, "${" + var.getName() + "}", varValue); //$NON-NLS-1$ //$NON-NLS-2$
        }

        // remove all cursor markers
        text = StringUtils.replace(text, "${cursor}", ""); //$NON-NLS-1$ //$NON-NLS-2$

        // Update EOLs (bug 80231)
        String systemEOL = System.getProperty("line.separator"); //$NON-NLS-1$
        text = StringUtils.replace(text, "\r\n", "\n"); //$NON-NLS-1$ //$NON-NLS-2$
        text = StringUtils.replace(text, "\r", "\n"); //$NON-NLS-1$ //$NON-NLS-2$
        if (!"\n".equals(systemEOL) && systemEOL != null) { //$NON-NLS-1$
            text = StringUtils.replace(text, "\n", systemEOL); //$NON-NLS-1$
        }
        return text;
    }

    private SnippetItem findSnippet(SnippetStore store) {
        try {
            RootContainer<String, IRepositoryObject> snippets = RunProcessPlugin.getDefault().getRepositoryService()
                    .getProxyRepositoryFactory().getSnippets();
            List<IRepositoryObject> objects = snippets.getMembers();

            SnippetItem item = null;
            for (IRepositoryObject repositoryObject : objects) {
                item = (SnippetItem) repositoryObject.getProperty().getItem();
                if (item.getProperty().getId().equals(store)) {
                    break;
                }
            }
            return item;
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
        return null;
    }

}
