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

import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * DOC bqian class global comment. Detailled comment <br/>
 * 
 */
public class SnippetParserTest {

    static String testCode = null;

    static String snippetID = "_34ejdsfy8768sdf79";

    static String snippetName = "SnippetAA";

    /**
     * DOC bqian Comment method "setUpBeforeClass".
     * 
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        // Java comment format
        StringBuilder sb = new StringBuilder();
        String para = "(Var1=xxx Var2=yyy)";
        String code = "String.valueof(${Var1})";
        sb.append("other code start");
        sb.append("\n/*SNIPPET_START ").append("ID=" + snippetID + " " + snippetName + para).append("*/");
        sb.append("\n");
        sb.append(code);
        sb.append("\n/*SNIPPET_END*/\n");
        sb.append("other code end");
        testCode = sb.toString();
    }

    /**
     * DOC bqian Comment method "tearDownAfterClass".
     * 
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    /**
     * DOC bqian Comment method "setUp".
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {

    }

    /**
     * DOC bqian Comment method "tearDown".
     * 
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {

    }

    /**
     * Test method for {@link org.talend.designer.runprocess.SnippetParser#findFirstSnippetId(java.lang.String)}.
     */
    @Test
    public void testFindFirstSnippetId() {
        SnippetParser sp = new SnippetParser();
        String[] result = sp.findFirstSnippetId(testCode).toArray(new String[0]);

        Assert.assertEquals(result.length, 4);
        Assert.assertEquals(result[0], snippetID);
        Assert.assertEquals(result[1], snippetName);
        Assert.assertEquals(result[2], "Var1=xxx");
        Assert.assertEquals(result[3], "Var2=yyy");

    }

    @Test
    public void testFindFirstSnippetIdNoResult() {
        SnippetParser sp = new SnippetParser();
        String[] result = sp.findFirstSnippetId("sdfsdf").toArray(new String[0]);

        Assert.assertEquals(result.length, 0);

    }

    /**
     * Test method for
     * {@link org.talend.designer.runprocess.SnippetParser#replaceFristSnippet(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testReplaceFristSnippet() {
        SnippetParser sp = new SnippetParser();

        StringBuilder sb = new StringBuilder();
        String para = "(Var1=xxx Var2=yyy)";
        String code = "String.valueof(XXX,YYY)";
        sb.append("/*SNIPPET_GERERATED_START ").append("ID=" + snippetID + " " + snippetName + para).append("*/");
        sb.append("\n");
        sb.append(code);
        sb.append("\n/*SNIPPET_GERERATED_END*/");

        String result = sp.replaceFristSnippet("mycode", testCode);
        // System.out.println("result is:" + result);
        Assert.assertEquals("other code start\nmycode\nother code end", result);
    }

    /**
     * Test method for
     * {@link org.talend.designer.runprocess.SnippetParser#replaceFristSnippet(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testReplaceFristSnippet2() {
        System.out.println(testCode);
        System.out.println("---------------------");
        SnippetParser sp = new SnippetParser();
        StringBuilder sb = new StringBuilder();
        sb.append("start\n");
        sb.append("/*SNIPPET_START ID=_9ufnsJ45Edy4ur SnippetMoney(name_2=list name_1=String)*/");
        sb.append("\n");
        sb.append("for(${name_1} str: ${name_2})");
        sb.append("\n");
        sb.append("{Sysout.out.println(str);}");
        sb.append("\n");
        sb.append("/*SNIPPET_END*/");
        sb.append("\nend");
        System.out.println("testCode is: \n" + sb.toString());
        String result = sp.replaceFristSnippet("my", sb.toString());
        // System.out.println("result is:" + result);
        Assert.assertEquals("start\nmy\nend", result);
    }
}
